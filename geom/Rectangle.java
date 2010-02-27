package geom;




import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import structure.Transition;
import xml.Settings;




public class Rectangle extends Rectangle2D.Double implements Shape {

	/**
	 * 
	 */
	private static final long serialVersionUID = 685359326840035579L;

	private static double vhRatio = 0.7;
	
	private Transition owner;
	private Point center;
	
	public Rectangle(Point point, Transition owner) {
		
		super(point.x - Settings.elementsRadius(), point.y - (int)(Settings.elementsRadius() * vhRatio), 
				Settings.elementsRadius() * 2, (int)(Settings.elementsRadius() * vhRatio * 2));
		
		this.owner = owner;
		center = point;
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
		y = point.y - (int)(Settings.elementsRadius() * vhRatio);
	}
	
	public Point getPointOfBorderTowards(Point point) {
		
		int diffX = center.x - point.x;
		int diffY = center.y - point.y;
		
		if (diffX == 0) 
			return new Point(center.x, center.y - (int)(Settings.elementsRadius() * vhRatio * Math.signum(diffY)));
		else if (Math.abs(diffY) / Math.abs(diffX) < vhRatio) 
			return new Point(center.x - (int)(Settings.elementsRadius() * Math.signum(diffX)), 
					(int)(center.y - (Settings.elementsRadius() * vhRatio * diffY / diffX) * Math.signum(diffX)));
		else 
			return new Point(center.x - (int)(Settings.elementsRadius() * vhRatio * diffX / diffY * Math.signum(diffY)), 
					center.y - (int)(Settings.elementsRadius() * vhRatio * Math.signum(diffY)));
	}
	
	public void draw(Graphics2D graphics) {

		graphics.draw(this);
		if (owner.isHigh()) 
			graphics.drawLine((int)x, (int)y, (int)x + Settings.elementsRadius() * 2, (int)y + (int)(Settings.elementsRadius() * 1.4));
		else if(owner.isDowngrade())
			graphics.drawString("D", (int) x+Settings.elementsRadius()-4,(int) y+Settings.elementsRadius());
	}

}
