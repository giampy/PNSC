package structure;



import geom.Shape;
import geom.Sketch;
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
import java.awt.geom.Rectangle2D.Double;
import java.util.Vector;

import javax.swing.JMenuItem;

import loadstore.XMLAttribute;
import loadstore.XMLNode;
import loadstore.XMLTree;


public abstract class Element implements Sketch {

	public static int STATE = 0;
	public static int TRANSITION = 1;
	public static int ARC = 2;
	
	protected Point position;
	protected Point lastStablePosition;
	protected Shape shape;
	protected Color color;
	
	protected boolean hoveringOnMe = false;
	protected boolean selected = false;
	protected PopupMenu popup;
	
	protected String id;
	
	private int key;
	
	public Element(int key, String id) {
		
		this.key = key;
		this.id = id;
		
		popup = new PopupMenu();
		popup.add(deleteItem());
	}
	
	public int getKey() {
		
		return key;
	}
	
	public String getId() {
		
		return id;
	}
	
	public void setId(String id) {
		
		this.id = id;
	}
	
	public boolean isNode() {
		
		return false;
	}
	
	public boolean isType(int type) {
		
		return false;
	}
	
	public Point getPosition() {
		
		return position;
	}
	
	public void setPosition(Point point) {
		
		position = point;
		shape.setPosition(position);
	}
	
	public boolean is(int key) {
		
		return key == this.key;
	}
	
	public boolean isSameKindOf(Element element) {
		
		return key == element.getKey();
	}
	
	public boolean contains(Point point) {
		
		return shape.contains(point);
	}
	
	public abstract void deleteFrom(Net net);
	
	public abstract XMLTree toPnml();
	
	public XMLTree getPnmlPosition() {
		
		XMLNode root = new XMLNode("graphics", "");
		
		XMLNode posNode = new XMLNode("position", "");
		posNode.addAttribute(new XMLAttribute("x", String.valueOf(getPosition().x)));
		posNode.addAttribute(new XMLAttribute("y", String.valueOf(getPosition().y)));
		
		XMLTree posTree = new XMLTree(posNode);
		Vector<XMLTree> children = new Vector<XMLTree>();
		children.add(posTree);
		
		return new XMLTree(root, children);
	}
	
	public void hoveringOn() {
		
		hoveringOnMe = true;
	}
	
	public void setSelected(boolean selected) {
		
		this.selected = selected;
	}
	
	public PopupMenu getPopupMenu(NetPanel netPanel) {
		
		popup.setNet(netPanel);
		return popup;
	}
	
	public void paint(Graphics2D graphics) {
		
		Composite oldComposite = graphics.getComposite();
		if (hoveringOnMe || selected)
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		
		shape.draw(graphics);
		graphics.setComposite(oldComposite);
		
		hoveringOnMe = false;
	}

	// Sketch Methods
	
	public void draw(Graphics2D graphics) { }

	public abstract Element settleTarget(Point point);

	public boolean isContainedOrIntersects(Double rect) {
		
		return shape.isContainedOrIntersects(rect);
	}
	
	private JMenuItem deleteItem() {
		
		JMenuItem item = new JMenuItem("Delete");
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				deleteFrom(popup.getNet());
				popup.getNet().getNetHistory().addNet(popup.getNet());
				popup.getNetPanel().repaint();
			}
		});
		
		return item;
	}
}
