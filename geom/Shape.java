package geom;


import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D.Double;

public interface Shape {

	public boolean contains(Point point);
	
	public void setPosition(Point point);
	
	public Point getPointOfBorderTowards(Point point);
	
	public void draw(Graphics2D graphics);

	public boolean isContainedOrIntersects(Double rect);
}
