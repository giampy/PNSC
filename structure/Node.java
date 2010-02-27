package structure;



import gui.Grid;
import gui.IdDialog;
import gui.NetPanel;
import gui.PopupMenu;
import gui.PropertyBox;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JMenuItem;

import xml.Properties;
import xml.Settings;





public abstract class Node extends Element {

	protected Vector<Arc> arcs;
	
	public PropertyBox propertyBox = new PropertyBox();
	
	private JMenuItem simpleItem = simpleItem();
	private JMenuItem reducedItem = reducedItem();
	private JMenuItem contactsItem = contactsItem();
	
	public Node(int key, String id) {
		
		super(key, id);
		arcs = new Vector<Arc>();
		
		popup.add(renameItem());
	}
	
	public boolean isNode() {
		
		return true;
	}
	
	public Point getPointOfBorderTowards(Point point) {
		
		return shape.getPointOfBorderTowards(point);
	}
	
	public void add(Arc arc) {
		
		arcs.add(arc);
	}
	
	public Vector<Arc> getArcs() {
		
		return arcs;
	}
	
	public void remove(Arc arc) {
		
		arcs.remove(arc);
	}
	
	public Vector<Node> preset() {
		
		Vector<Node> preset = new Vector<Node>();
		
		for (int a = 0; a < arcs.size(); a++) 
			if (arcs.get(a).getTarget().equals(this)) 
				preset.add(arcs.get(a).getSource());
			
		return preset;
	}
		
	public Vector<Node> postset() {
		
		Vector<Node> postset = new Vector<Node>();
		
		for (int a = 0; a < arcs.size(); a++) 
			if (arcs.get(a).getSource().equals(this)) 
				postset.add(arcs.get(a).getTarget());
			
		return postset;
	}
		
	public void deleteFrom(Net net) {
		
		for (int a = arcs.size() - 1; a >= 0; a--)
			arcs.get(a).deleteFrom(net);
		
		net.remove(this);
	}
	
	public void moveTarget(Point point) {

		position = Grid.closestAllowedPoint(point);
		Point offset = new Point((position.x - lastStablePosition.x) / 2, (position.y - lastStablePosition.y)/ 2);
		shape.setPosition(position);
		
		for (int a = 0; a < arcs.size(); a++) 
			arcs.get(a).updateArc();
	}
	
	public void updateArcs() {
		
		for (int a = 0; a < arcs.size(); a++)
			arcs.get(a).updateArc();
	}
	
	public Element settleTarget(Point point) {
		
		Point before = new Point(lastStablePosition);
		lastStablePosition = getPosition();
		
		if (!lastStablePosition.equals(before)) 
			return this;
		else return null;
	}
	
	public PopupMenu getPopupMenu(NetPanel netPanel) {
		
		popup.setNet(netPanel);
		if (Properties.isCheckSimpleOn() && !propertyBox.isSimple()) {
			
			popup.add(simpleItem);
		} else popup.remove(simpleItem);
		
		if (Properties.isCheckReducedOn() && !propertyBox.isReduced()) {
			
			popup.add(reducedItem);
		} else popup.remove(reducedItem);

		if (Properties.isCheckContactsOn() && !propertyBox.isContactFree()) {
			
			popup.add(contactsItem);
		} else popup.remove(contactsItem);

		return popup;
	}
	
	private JMenuItem simpleItem() {
		
		JMenuItem item = new JMenuItem("Not simple");
		
		final Node thisNode = this;
			
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				Vector<Element> vec = new Vector<Element>();
				Vector<Node> simple = thisNode.propertyBox.getSimple();
				vec.addAll(simple);
				vec.addAll(thisNode.preset());
				vec.addAll(thisNode.postset());
				for (int s = 0; s < simple.size(); s++)
					vec.addAll(simple.get(s).getArcs());
				
				Net focus = new StillNet();
				for (int v = 0; v < vec.size(); v++)
					focus.add(vec.get(v));
				popup.getNetPanel().focusOn(focus);
			}
		});
		
		return item;		
	}
	
	private JMenuItem reducedItem() {
		
		JMenuItem item = new JMenuItem("Not reduced");
		
		final Node thisNode = this;
		
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				Net focus = new StillNet();
				focus.add(thisNode);
				popup.getNetPanel().focusOn(focus);
			}
		});
		
		return item;		
	}
	
	private JMenuItem contactsItem() {
		
		JMenuItem item = new JMenuItem("Not contact free");

		final Node thisNode = this;
		
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				Vector<Vector<Place>> contacts = thisNode.propertyBox.getContacts();
				Vector<Net> focus = new Vector<Net>();
				
				for (int c = 0; c < contacts.size(); c++) {
					
					Net net = new StillNet();
					Transition transition = (Transition)thisNode;
					Transition newTransition = new Transition(transition);
					newTransition.propertyBox = transition.propertyBox;
					net.add(newTransition);
					
					Vector<Place> marking = contacts.get(c);
					for (int s = 0; s < marking.size(); s++) {
						
						Place place = marking.get(s);
						Place newPlace = new Place(place);
						newPlace.propertyBox = place.propertyBox;
						if (newPlace.getTokens() == 0)
							newPlace.addToken();
						net.add(newPlace);
						
						Vector<Arc> arcs = place.getArcs();
						for (int a = 0; a < arcs.size(); a++)
							if (arcs.get(a).getSource().equals(transition))
								net.add(new Arc(arcs.get(a).getPosition(), newTransition, newPlace, net.newId(Element.ARC)));
							else if (arcs.get(a).getTarget().equals(transition))
								net.add(new Arc(arcs.get(a).getPosition(), newPlace, newTransition, net.newId(Element.ARC)));
						}
					
					focus.add(net);
				}
				
				popup.getNetPanel().focusOn(focus);
			}
		});
		
		return item;		
	}
	
	private JMenuItem renameItem() {
		
		JMenuItem item = new JMenuItem("Rename");
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				id = IdDialog.open(popup.getNet(), "Type new name", id);
				popup.getNet().getNetHistory().addNet(popup.getNet());
				popup.getNetPanel().repaint();
			}
		});
		
		return item;		
	}
	
	public void paint(Graphics2D graphics) {
		
		if (propertyBox != null) {
			
			if ((Properties.isCheckSimpleOn() && !propertyBox.isSimple())
					|| (Properties.isCheckReducedOn() && !propertyBox.isReduced())
					|| (Properties.isCheckContactsOn() && !propertyBox.isContactFree())) {
				
				Composite oldComposite = graphics.getComposite();
				graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
				graphics.setColor(Color.LIGHT_GRAY);
				graphics.fill((java.awt.Shape)shape);
				graphics.setComposite(oldComposite);
			}
		}
		
		super.paint(graphics);
		
		if (Settings.isShowNodesNameOn()) {

			graphics.setColor(Color.BLACK);
			graphics.drawString(id, position.x + Settings.elementsRadius(), position.y - Settings.elementsRadius());
		}
	}
	
	public String toString() {
		
		return id;
	}
}
