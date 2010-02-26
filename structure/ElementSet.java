package structure;



import geom.Sketch;
import gui.Grid;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;


public class ElementSet implements Sketch {
	
	private Vector<Element> set;
	private Element leader;
	
	public ElementSet(Vector<Element> elements, Element leader) {
		
		this.leader = leader;
		
		set = new Vector<Element>();
		
		for (int e = 0; e < elements.size(); e++)
			if (elements.get(e).is(Element.STATE) || elements.get(e).is(Element.TRANSITION))
				set.add(elements.get(e));
	
		if (set.size() == 0) 
			set = elements;
	}
	
	public void draw(Graphics2D graphics) {
	
	}

	public void moveTarget(Point point) {

		Point effPoint = !leader.is(Element.ARC) ? Grid.closestAllowedPoint(point) : point;

		int diffX = effPoint.x - leader.getPosition().x;
		int diffY = effPoint.y - leader.getPosition().y;

		for (int e = 0; e < set.size(); e++) {

			Point position = set.get(e).getPosition();
			set.get(e).setPosition(new Point(position.x + diffX, position.y + diffY));
			if (!set.get(e).is(Element.ARC))
				((Node)set.get(e)).updateArcs();
			else ((Arc)set.get(e)).updateArc();
		}
	}

	public Element settleTarget(Point point) {

		return set.get(0);
	}

	public boolean isType(int type) {

		return type == Sketch.ELEMENT_SET;
	}
}
