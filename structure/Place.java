package structure;



import geom.Circle;
import gui.NetPanel;
import gui.PopupMenu;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JMenuItem;

import xml.Properties;
import xml.XMLAttribute;
import xml.XMLNode;
import xml.XMLTree;


import algo.ActiveCase;
import algo.Case;





public class Place extends Node {
	
	private int tokens = 0;
	private boolean inTheInitialMarking = false;
	
	private JMenuItem potentialCausalItem = potentialCausalItem();
	private JMenuItem potentialConflictItem = potentialConflictItem();
	private JMenuItem activeCausalItem = activeCausalItem();
	private JMenuItem activeConflictItem = activeConflictItem();
	
	public boolean amIinASelfLoop(){
		for(int index=0; index<this.postset().size(); ++index){
			for(int i=0; i<this.postset().get(index).postset().size(); ++i)
						if ((Place)this.postset().get(index).postset().get(i) == this)
							return true;	
				}
		return false;
	}
	
	
	
	public Place(Point point, String id) {
		
		super(Element.STATE, id);
		
		position = point;
		lastStablePosition = position;
		shape = new Circle(point);
		color = Color.BLUE;
	}
	
	public Place(Point point, int tokens, String id) {
		
		super(Element.STATE, id);
		
		this.tokens = tokens;
		position = point;
		lastStablePosition = position;
		shape = new Circle(point);
		color = Color.BLUE;

	}
	
	public Place(Place place) {
		
		super(Element.STATE, place.getId());
		
		tokens = place.getTokens();
		position = place.getPosition();
		lastStablePosition = position;
		shape = new Circle(position);
		color = Color.BLUE;
		

	}
	
	public boolean isFilled(){
		return this.tokens==1;
	}
	
	public void addToken() {
		
		if (tokens == 0)
			tokens = 1;
		else tokens = 0;
	}
	
	public void putInTheInitialMarking(boolean b) {

		inTheInitialMarking = b;
	}
	
	public void removeToken() {
		
		tokens = 0;
	}
	
	public int getTokens() {
		
		return tokens;
	}
		
	public void paint(Graphics2D graphics) {
		if (((Properties.isCheckPotentialCausalOn() && propertyBox.isPotentialCausal()) || 
				(Properties.isCheckPotentialConflictOn() && propertyBox.isPotentialConflict())) &&
				
				!((Properties.isCheckActiveCausalOn() && (propertyBox.isActiveCausal()) ||
						((Properties.isCheckActiveConflictOn() && propertyBox.isActiveConflict()))))) {

			Composite oldComposite = graphics.getComposite();
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			graphics.setColor(Color.ORANGE);
			graphics.fill((Ellipse2D)shape);
			graphics.setComposite(oldComposite);
		}

		if ((Properties.isCheckActiveCausalOn() && propertyBox.isActiveCausal()) ||
				(Properties.isCheckActiveConflictOn() && propertyBox.isActiveConflict())) {

			Composite oldComposite = graphics.getComposite();
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			graphics.setColor(Color.RED);
			graphics.fill((Ellipse2D)shape);
			graphics.setComposite(oldComposite);
		}
		
		super.paint(graphics);
		
		if (tokens == 1) {

			graphics.setColor(Color.BLACK);
			graphics.fillOval(position.x - 3, position.y - 3, 6, 6);
		}
		
		if (inTheInitialMarking) {
			
			graphics.setColor(Color.DARK_GRAY);
			graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
			graphics.drawOval(position.x - 4, position.y - 4, 8, 8);			
		}
	}
	
	public XMLTree toPnml() {
		
		XMLNode root = new XMLNode("place", "");
		root.addAttribute(new XMLAttribute("id", String.valueOf(id)));
		
		Vector<XMLTree> children = new Vector<XMLTree>();
		children.add(getPnmlPosition());
		
		if (tokens > 0) {
			
			Vector<XMLTree> imChildren = new Vector<XMLTree>();
			imChildren.add(new XMLTree(new XMLNode("value", String.valueOf(tokens))));
			
			children.add(new XMLTree(new XMLNode("initialMarking", ""), imChildren));
		}
		
		return new XMLTree(root, children);
	}
	
	// Popup stuff
	
	public PopupMenu getPopupMenu(NetPanel netPanel) {
		
		PopupMenu popup = super.getPopupMenu(netPanel);

		if (Properties.isCheckPotentialCausalOn() && propertyBox.isPotentialCausal())
			popup.add(potentialCausalItem);
		else popup.remove(potentialCausalItem);

		if (Properties.isCheckPotentialConflictOn() && propertyBox.isPotentialConflict())
			popup.add(potentialConflictItem);
		else popup.remove(potentialConflictItem);

		if (Properties.isCheckActiveCausalOn() && propertyBox.isActiveCausal())
			popup.add(activeCausalItem);
		else popup.remove(activeCausalItem);

		if (Properties.isCheckActiveConflictOn() && propertyBox.isActiveConflict())
			popup.add(activeConflictItem);
		else popup.remove(activeConflictItem);
		
		
		return popup;
	}
	
	private JMenuItem potentialCausalItem() {
		
		JMenuItem item = new JMenuItem("Potentially causal");
		
		final Node thisNode = this;
			
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				Vector<Element> vec = new Vector<Element>();
				Vector<Transition> pc = thisNode.propertyBox.getPotentialCausal();
				vec.add(thisNode);
				vec.addAll(pc);
				for (int t = 0; t < pc.size(); t++) {
					
					Vector<Arc> arcs = pc.get(t).getArcs();
					for (int a = 0; a < arcs.size(); a++)
						if (arcs.get(a).getSource().equals(thisNode) || arcs.get(a).getTarget().equals(thisNode))
							vec.add(arcs.get(a));
				}
									
				Net focus = new Net();
				for (int v = 0; v < vec.size(); v++)
					focus.add(vec.get(v));
				popup.getNetPanel().focusOn(focus);
			}
		});
		
		return item;		
	}
	
	private JMenuItem potentialConflictItem() {
		
		JMenuItem item = new JMenuItem("Potentially conflict");
		
		final Node thisNode = this;
			
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				Vector<Element> vec = new Vector<Element>();
				Vector<Transition> pc = thisNode.propertyBox.getPotentialConflict();
				vec.add(thisNode);
				vec.addAll(pc);
				for (int t = 0; t < pc.size(); t++) {
					
					Vector<Arc> arcs = pc.get(t).getArcs();
					for (int a = 0; a < arcs.size(); a++)
						if (arcs.get(a).getSource().equals(thisNode) || arcs.get(a).getTarget().equals(thisNode))
							vec.add(arcs.get(a));
				}
									
				Net focus = new Net();
				for (int v = 0; v < vec.size(); v++)
					focus.add(vec.get(v));
				popup.getNetPanel().focusOn(focus);
			}
		});
		
		return item;		
	}
	
private JMenuItem activeCausalItem() {
		
		JMenuItem item = new JMenuItem("Active causal");

		final Node thisNode = this;
		
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				Vector<ActiveCase> active = thisNode.propertyBox.getActiveCausal();
				Vector<Net> focus = new Vector<Net>();
				
				for (int c = 0; c < active.size(); c++) {
					
					Hashtable<Node, Node> newNodes = new Hashtable<Node, Node>();

					Net net = new StillNet();
					Place place = (Place)thisNode;
					Place newPlace = new Place(place);
					if (newPlace.getTokens() > 0)
						newPlace.removeToken();
					newPlace.propertyBox = place.propertyBox;
					net.add(newPlace);
					newNodes.put(place, newPlace);
					
					ActiveCase activeCase = active.get(c);
					Transition high = new Transition(activeCase.high());
					high.disable();
					net.add(high);
					newNodes.put(activeCase.high(), high);
					Transition low = new Transition(activeCase.low());
					low.disable();
					net.add(low);
					newNodes.put(activeCase.low(), low);
					
					Vector<Arc> arcs = place.getArcs(); 
					for (int a = 0; a < arcs.size(); a++) 
						if (arcs.get(a).getSource().equals(activeCase.high()))
							net.add(arcs.get(a));
						else if (arcs.get(a).getTarget().equals(activeCase.low()))
							net.add(arcs.get(a));
					
					arcs = activeCase.high().getArcs();
					for (int a = 0; a < arcs.size(); a++) 
						if (arcs.get(a).getTarget().equals(activeCase.high()))
							net.add(arcs.get(a));
 					
					for (int s = 0; s < activeCase.size(); s++) {
						
						Place st = activeCase.get(s);
						Place newSt = new Place(st);
						newSt.propertyBox = st.propertyBox;
						if (newSt.getTokens() == 0)
							newSt.addToken();
						net.add(newSt);
						newNodes.put(st, newSt);
					}
					
					// PATH STUFF
					
					Vector<Case> path = activeCase.path();
					for (int p = 0; p < path.size(); p++) {
						
						Transition transition = path.get(p).firstTransition();
						Transition newTransition = new Transition(transition) {

							public void paint(Graphics2D graphics) {

								Composite oldComposite = graphics.getComposite();
								graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

								graphics.setColor(Color.RED);
								shape.draw(graphics);

								graphics.setComposite(oldComposite);
							}
						};
						
						if (!newNodes.contains(transition)) {
							newNodes.put(transition, newTransition);
							if (p < path.size() - 1) 
								net.add(newTransition);
						}

						Vector<Place> places = path.get(p);
						for (int s = 0; s < places.size(); s++) {

							Place ns = new Place(places.get(s)) {

								public void paint(Graphics2D graphics) {

									Composite oldComposite = graphics.getComposite();
									graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

									graphics.setColor(Color.RED);
									shape.draw(graphics);

									graphics.setComposite(oldComposite);
								}
							};

							if (!newNodes.contains(places.get(s))) 
								newNodes.put(places.get(s), ns);
							if (path.get(p).size() > 0)
								net.add(ns);
						}
					}
					
					for (int p = 0; p < path.size(); p++) {
						
						Transition transition = path.get(p).firstTransition();
						arcs = transition.getArcs();

						for (int a = 0; a < arcs.size(); a++) {

							Arc arc = arcs.get(a);
							if (arc.getTarget().equals(transition) && newNodes.get(arc.getSource()) != null)
								net.add(new Arc(arc.getPosition(), newNodes.get(arc.getSource()), newNodes.get(transition), net.newId(Element.ARC)) {

									public void paint(Graphics2D graphics) {

										Composite oldComposite = graphics.getComposite();
										graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

										graphics.setColor(Color.RED);
										shape.draw(graphics);

										graphics.setComposite(oldComposite);
									}
								});
							else if (p < path.size() - 1 && arc.getSource().equals(transition) && newNodes.get(arc.getTarget()) != null)
								net.add(new Arc(arc.getPosition(), newNodes.get(transition), newNodes.get(arc.getTarget()), net.newId(Element.ARC)) {

									public void paint(Graphics2D graphics) {

										Composite oldComposite = graphics.getComposite();
										graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

										graphics.setColor(Color.RED);
										shape.draw(graphics);

										graphics.setComposite(oldComposite);
									}
								});
						}
					}

					focus.add(net);
				}
				
				popup.getNetPanel().focusOn(focus);
			}
		});
		
		return item;		
	}

	private JMenuItem activeConflictItem() {
		
		JMenuItem item = new JMenuItem("Active conflict");

		final Node thisNode = this;
		
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				Vector<ActiveCase> active = thisNode.propertyBox.getActiveConflict();
				Vector<Net> focus = new Vector<Net>();
				
				for (int c = 0; c < active.size(); c++) {
					
					Hashtable<Node, Node> newNodes = new Hashtable<Node, Node>();

					Net net = new StillNet();
					Place place = (Place)thisNode;
					Place newPlace = new Place(place);
					if (newPlace.getTokens() > 0)
						newPlace.removeToken();
					newPlace.propertyBox = place.propertyBox;
					net.add(newPlace);
					newNodes.put(place, newPlace);
					
					ActiveCase activeCase = active.get(c);
					Transition high = new Transition(activeCase.high());
					high.disable();
					net.add(high);
					newNodes.put(activeCase.high(), high);
					Transition low = new Transition(activeCase.low());
					low.disable();
					net.add(low);
					newNodes.put(activeCase.low(), low);
										
					Vector<Arc> arcs = place.getArcs(); 
					for (int a = 0; a < arcs.size(); a++) 
						if (arcs.get(a).getSource().equals(activeCase.high()))
							net.add(arcs.get(a));
						else if (arcs.get(a).getTarget().equals(activeCase.low()))
							net.add(arcs.get(a));
					
					arcs = activeCase.high().getArcs();
					for (int a = 0; a < arcs.size(); a++) 
						if (arcs.get(a).getTarget().equals(activeCase.high()))
							net.add(arcs.get(a));
 					
					for (int s = 0; s < activeCase.size(); s++) {
						
						Place st = activeCase.get(s);
						Place newSt = new Place(st);
						newSt.propertyBox = st.propertyBox;
						if (newSt.getTokens() == 0)
							newSt.addToken();
						net.add(newSt);
						newNodes.put(st, newSt);
					}
					
					// PATH STUFF
					
					Vector<Case> path = activeCase.path();
					for (int p = 0; p < path.size(); p++) {
						
						Transition transition = path.get(p).firstTransition();
						Transition newTransition = new Transition(transition) {

							public void paint(Graphics2D graphics) {

								Composite oldComposite = graphics.getComposite();
								graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

								graphics.setColor(Color.RED);
								shape.draw(graphics);

								graphics.setComposite(oldComposite);
							}
						};
						
						if (!newNodes.contains(transition)) {
							newNodes.put(transition, newTransition);
							if (p < path.size() - 1) 
								net.add(newTransition);
						}

						Vector<Place> places = path.get(p);
						for (int s = 0; s < places.size(); s++) {

							Place ns = new Place(places.get(s)) {

								public void paint(Graphics2D graphics) {

									Composite oldComposite = graphics.getComposite();
									graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

									graphics.setColor(Color.RED);
									shape.draw(graphics);

									graphics.setComposite(oldComposite);
								}
							};

							if (!newNodes.contains(places.get(s))) 
								newNodes.put(places.get(s), ns);
							if (path.get(p).size() > 0)
								net.add(ns);
						}
					}
					
					for (int p = 0; p < path.size(); p++) {
						
						Transition transition = path.get(p).firstTransition();
						arcs = transition.getArcs();

						for (int a = 0; a < arcs.size(); a++) {

							Arc arc = arcs.get(a);
							if (arc.getTarget().equals(transition) && newNodes.get(arc.getSource()) != null)
								net.add(new Arc(arc.getPosition(), newNodes.get(arc.getSource()), newNodes.get(transition), net.newId(Element.ARC)) {

									public void paint(Graphics2D graphics) {

										Composite oldComposite = graphics.getComposite();
										graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

										graphics.setColor(Color.RED);
										shape.draw(graphics);

										graphics.setComposite(oldComposite);
									}
								});
							else if (p < path.size() - 1 && arc.getSource().equals(transition) && newNodes.get(arc.getTarget()) != null)
								net.add(new Arc(arc.getPosition(), newNodes.get(transition), newNodes.get(arc.getTarget()), net.newId(Element.ARC)) {

									public void paint(Graphics2D graphics) {

										Composite oldComposite = graphics.getComposite();
										graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

										graphics.setColor(Color.RED);
										shape.draw(graphics);

										graphics.setComposite(oldComposite);
									}
								});
						}
					}

					focus.add(net);
				}
				
				popup.getNetPanel().focusOn(focus);
			}
		});
		
		return item;		
	}
}
