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

import xml.Properties;
import xml.XMLAttribute;
import xml.XMLNode;
import xml.XMLTree;




public class Transition extends Node {

	/*public static boolean LOW = false;
	public static boolean HIGH = true;*/
	
	/*introduciamo tre livelli di sicurezza*/
	public static final int LOW=0;
	public static final int DOWNGRADE=1;
	public static final int HIGH=2;
	
	public	int secLevel; //livello della transizione
	
	private boolean isHigh; //per ora lo lascio, non da fastidio
	private boolean explicitelyEnabled = true;
	
	private JMenuItem fireItem = fireItem();
	
	public Transition(Point point, boolean isHigh, String id) {
		
		super(Element.TRANSITION, id);
		
		this.isHigh = isHigh;
		if(this.isHigh())
			this.secLevel=HIGH;
		
		position = point;
		lastStablePosition = position;
		shape = new Rectangle(point, this);
		color = Color.BLUE;
	}
	//costruttore che prende il livello (fra i tre) e non il booleano
	public Transition(Point point, int secLevel, String id) {
		
		super(Element.TRANSITION, id);
		
		this.setSecurityLevel(secLevel);
		if(this.secLevel==HIGH) 	//teniamo aggiornata cmq la variabile isHigh anche se penso che la toglierò
				this.setSecurityLevel(true);
		else
			this.setSecurityLevel(false);
		
		position = point;
		lastStablePosition = position;
		shape = new Rectangle(point, this);
		color = Color.BLUE;
	}
	
	public Transition(Transition transition) {
		
		super(Element.TRANSITION, transition.getId());
		
		isHigh = transition.isHigh();
		
		
		this.setSecurityLevel(transition.getSecurityLevel()); 
		
		
		position = transition.getPosition();
		lastStablePosition = position;
		shape = new Rectangle(position, this);
		color = Color.BLUE;
	}
	
	public boolean isHigh() {
		return isHigh;
	}
	public boolean isDowngrade(){
		return this.getSecurityLevel()==DOWNGRADE;
	}
	public boolean isLow(){
		return this.getSecurityLevel()==LOW;
	}
	public int getSecurityLevel(){
		return secLevel;
	}
	
	public boolean isEnabled() {

		
		Vector<Node>	preset=preset();
		Vector<Node>	postset=postset();
		Vector<Place>	intersect=new Vector<Place>();
		
		for(int i=0; i<postset.size(); ++i)
			if(preset.contains(postset.get(i)))
				intersect.add((Place)postset.get(i));
		
		for(int i=0; i<intersect.size(); ++i)
			if(intersect.get(i).getTokens()<1)
				return false;
		
		preset.removeAll(intersect);
		postset.removeAll(intersect);
		
		for (int p = 0; p < preset.size(); p++)
			if (((Place)(preset.get(p))).getTokens() < 1)
				return false;

		for (int p = 0; p < postset.size(); p++)
			if (((Place)(postset.get(p))).getTokens() > 0)
				return false;
			
		return explicitelyEnabled;
	}
	
/*	public boolean isEnabledAt(Vector<Place> marking) {
		
		Vector<Node> preset = preset();
		for (int p = 0; p < preset.size(); p++)
			if (!marking.contains((Place)(preset.get(p))))
				return false;
		
		Vector<Node> postset = postset();
		for (int p = 0; p < postset.size(); p++)
			if (marking.contains((Place)(postset.get(p))))
				return false;
			
		return true;
	}*/
	
	public void setSecurityLevel(boolean level) {
		
		isHigh = level;
	}
	
	public void setSecurityLevel(int secLevel){
		this.secLevel=secLevel;
		if(this.secLevel==HIGH)
			setSecurityLevel(true);
		else
			setSecurityLevel(false);
	}
	
	public void fire() {
		
		if (isEnabled()){
	/*		for (int a = 0; a < arcs.size(); a++) 
				if (arcs.get(a).getTarget().equals(this)) {
					
					Place source = (Place)arcs.get(a).getSource();
					source.removeToken();
						
				} else {
					
					Place target = (Place)arcs.get(a).getTarget();
					target.addToken();
				}*/
		for (int p = 0; p < preset().size(); p++)
			if(preset().get(p) instanceof Place){
				Place p1=(Place)preset().get(p);
				p1.removeToken();
				}
			
		for (int p = 0; p < postset().size(); p++)
			if(postset().get(p) instanceof Place){
				Place p1=(Place)postset().get(p);
				p1.addToken();
			}
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
		
		//aggiungiamo un attributo per gestire la security Label della transizione
		//nello schema a 3 Livelli di sicurezza
		levelNode.addAttribute(new XMLAttribute("secLevel", String.valueOf(this.secLevel)));
		
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
