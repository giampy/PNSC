package geom;




import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import structure.Element;
import structure.Net;



public class SelectingArea extends Rectangle2D.Double implements Sketch {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5692810163409669755L;
	private Net net;
	
	public SelectingArea(Point point, Net net) {
		
		super(point.x, point.y, 0, 0);
		
		this.net = net;
	}

	public boolean isType(int type) {

		return type == Sketch.SELECTING_AREA;
	}

	public void draw(Graphics2D graphics) {
			
		Rectangle2D rect = rightRectangle();
		
		graphics.setStroke(new BasicStroke(1));
		graphics.setColor(Color.DARK_GRAY);
		graphics.draw(rect);
		
		Composite oldComposite = graphics.getComposite();
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		graphics.setColor(Color.BLUE);
		graphics.fill(rect);
		graphics.setComposite(oldComposite);
	}

	public void moveTarget(Point point) {
		
		width = point.x - x;
		height = point.y - y;
	}

	public Element settleTarget(Point point) {
		
		net.selectElementsContainedIn(rightRectangle());
		return null;
	}
	
	private Rectangle2D.Double rightRectangle() {

		Rectangle2D.Double rect = new Rectangle2D.Double();

		rect.x = width >= 0? x : x + width;
		rect.width = Math.abs(width);

		rect.y = height >= 0? y : y + height;
		rect.height = Math.abs(height);

		return rect;
	}
}
