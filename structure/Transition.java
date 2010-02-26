package structure;



import geom.Rectangle;
import gui.NetPanel;
import gui.PopupMenu;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JMenuItem;

import loadstore.Properties;
import loadstore.XMLAttribute;
import loadstore.XMLNode;
import loadstore.XMLTree;



public class Transition extends Node {

	public static boolean LOW = false;
	public static boolean HIGH = true;
	
	private boolean isHigh;
	private boolean explicitelyEnabled = true;
	
	private JMenuItem fireItem = fireItem();
	
	public Transition(Point point, boolean isHigh, String id) {
		
		super(Element.TRANSITION, id);
		
		this.isHigh = isHigh;
		
		position = point;
		lastStablePosition = position;
		shape = new Rectangle(point, this);
		color = Color.BLUE;
	}
	
	public Transition(Transition transition) {
		
		super(Element.TRANSITION, transition.getId());
		
		isHigh = transition.isHigh();
		
		position = transition.getPosition();
		lastStablePosition = position;
		shape = new Rectangle(position, this);
		color = Color.BLUE;
	}
	
	public boolean isHigh() {
		
		return isHigh;
	}
	
	public boolean isEnabled() {
		
		Vector<Node> preset = preset();
		for (int p = 0; p < preset.size(); p++)
			if (((Place)(preset.get(p))).getTokens() < 1)
				return false;
		
		Vector<Node> postset = postset();
		for (int p = 0; p < postset.size(); p++)
			if (((Place)(postset.get(p))).getTokens() > 0)
				return false;
			
		return explicitelyEnabled;
	}
	
	public boolean isEnabledAt(Vector<Place> marking) {
		
		Vector<Node> preset = preset();
		for (int p = 0; p < preset.size(); p++)
			if (!marking.contains((Place)(preset.get(p))))
				return false;
		
		Vector<Node> postset = postset();
		for (int p = 0; p < postset.size(); p++)
			if (marking.contains((Place)(postset.get(p))))
				return false;
			
		return true;
	}
	
	public void setSecurityLevel(boolean level) {
		
		isHigh = level;
	}
	
	public void fire() {
		
		if (isEnabled())
			for (int a = 0; a < arcs.size(); a++) 
				if (arcs.get(a).getTarget().equals(this)) {
					
					Place source = (Place)arcs.get(a).getSource();
					source.removeToken();
						
				} else {
					
					Place target = (Place)arcs.get(a).getTarget();
					target.addToken();
				}
	}

	public PopupMenu getPopupMenu(NetPanel netPanel) {
		
		PopupMenu popup = super.getPopupMenu(netPanel);
		
		popup.setNet(netPanel);
		if (isEnabled()) {
			
			popup.add(fireItem);
		} else popup.remove(fireItem);
		return popup;
	}
	
	private JMenuItem fireItem() {
		
		JMenuItem item = new JMenuItem("Fire");
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				fire();
				popup.getNetPanel().repaint();
			}
		});
		
		return item;		
	}
	
	public XMLTree toPnml() {
		
		XMLNode root = new XMLNode("transition", "");
		root.addAttribute(new XMLAttribute("id", String.valueOf(id)));
		
		Vector<XMLTree> children = new Vector<XMLTree>();
		children.add(getPnmlPosition());
		
		XMLNode levelNode = new XMLNode("level", "");
		levelNode.addAttribute(new XMLAttribute("high", String.valueOf(isHigh)));
		
		XMLNode tsRoot = new XMLNode("toolspecific", "");
		tsRoot.addAttribute(new XMLAttribute("tool", "PNST"));
		tsRoot.addAttribute(new XMLAttribute("version", "1.0"));
		
		Vector<XMLTree> tsChildren = new Vector<XMLTree>();
		tsChildren.add(new XMLTree(levelNode));
		
		children.add(new XMLTree(tsRoot, tsChildren));

		return new XMLTree(root, children);
	}
	
	public void paint(Graphics2D graphics) {
		
		if (Properties.isShowEnabledTransitionsOn()) {
			
			if (isEnabled()) {

				Composite oldComposite = graphics.getComposite();
				graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				graphics.setColor(Color.GREEN);
				graphics.fill((Rectangle)shape);
				graphics.setComposite(oldComposite);
			}
		}
		
		super.paint(graphics);
	}
	
	public void disable() {
		
		explicitelyEnabled = false;
	}
}
