package geom;


import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

public class Line extends QuadCurve2D.Double implements Shape {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1820451024690907072L;
	private LineEnd lineEnd;
	
	public Line(Point start, Point control, Point end) {
		
		super(start.x, start.y, control.x, control.y, end.x, end.y);
		lineEnd = new ArrowEnd(this);
	}
	
	public boolean contains(Point point) {

		int offset = 5;
		
		Point enCorner = new Point(point.x + offset, point.y - offset);
		Point esCorner = new Point(point.x + offset, point.y + offset);
		Point wsCorner = new Point(point.x - offset, point.y + offset);
		Point wnCorner = new Point(point.x - offset, point.y - offset);
		
		Line2D line = new Line2D.Double(getP1(), getP2());
		Rectangle2D sq = new Rectangle2D.Double(wnCorner.x, wnCorner.y, offset * 2, offset * 2);
		
		if (getFlatness() > offset) {
			
			boolean someIsIn = super.contains(enCorner) || super.contains(esCorner) || super.contains(wsCorner) || super.contains(wnCorner);
			boolean someIsNotIn = !super.contains(enCorner) || !super.contains(esCorner) || !super.contains(wsCorner) || !super.contains(wnCorner);
			boolean isOutTheRightSide = !line.intersects(sq);

			return someIsIn && someIsNotIn && isOutTheRightSide;
		} else return line.intersects(sq);
	}
	
	public boolean isContainedOrIntersects(java.awt.geom.Rectangle2D.Double rect) {
		
		return rect.contains(getBounds2D()) || intersects(rect);
	}
	
	public Point getPosition() {
		
		return new Point((int)ctrlx, (int)ctrly);
	}
	
	public void setPosition(Point point) {
		
		ctrlx = point.x;
		ctrly = point.y;
	}
	
	public Point getEnd() {
		
		return new Point((int)x2, (int)y2);
	}
	
	public void setEnds(Point start, Point end) {
		
		x1 = start.x;
		y1 = start.y;
		
		x2 = end.x;
		y2 = end.y;
	}

	public Point getPointOfBorderTowards(Point point) {
		
		return null;
	}
	
	public void draw(Graphics2D graphics) {
		
		graphics.draw(this);
		lineEnd.paint(graphics);
	}
}
