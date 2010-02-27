package geom;



import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

import xml.Settings;



public class Circle extends Ellipse2D.Double implements Shape {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1191108853390613503L;
	private Point center;
	
	public Circle(Point point) {
		
		super(point.x - Settings.elementsRadius(), point.y - Settings.elementsRadius(), 
				Settings.elementsRadius() * 2, Settings.elementsRadius() * 2);
		
		this.center = point;
	}
	
	public boolean contains(Point point) {
		
		return contains(point.x, point.y);
	}
	
	public boolean isContainedOrIntersects(java.awt.geom.Rectangle2D.Double rect) {
		
		return rect.contains(getBounds2D()) || intersects(rect);
	}
	
	public void setPosition(Point point) {
		
		center = point;
		x = point.x - Settings.elementsRadius();
		y = point.y - Settings.elementsRadius();
	}
	
	public Point getPointOfBorderTowards(Point point) {
		
		int diffX = center.x - point.x;
		int diffY = center.y - point.y;
		
		double dist = center.distance(point);
		double proportion = (double)(Settings.elementsRadius()) / dist;
		
		return new Point(center.x - (int)(diffX * proportion), center.y - (int)(diffY * proportion));
	}
	
	public void draw(Graphics2D graphics) {
		
		graphics.draw(this);
	}
}
