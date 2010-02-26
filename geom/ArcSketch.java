package geom;




import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import structure.Arc;
import structure.Element;
import structure.Net;
import structure.Node;



public class ArcSketch implements Sketch {

	private Net net;
	private Node start;
	private Line line;
	
	public ArcSketch(Net net, Node start) {
		
		this.net = net;
		this.start = start;
	}

	public void moveTarget(Point point) {
		
		Element target = net.findNodeContaining(point);
		if (target == null || !target.isSameKindOf(start)) {
			
			Point actualStart = start.getPointOfBorderTowards(point);
			Point control = new Point(actualStart.x + (point.x - actualStart.x) / 2, actualStart.y + (point.y - actualStart.y) / 2);
			line = new Line(actualStart, control, point);
		}
		else line = null;
	}
	
	public Element settleTarget(Point point) {

		Node target = net.findNodeContaining(point);
		if (target != null && target.isNode() && !target.isSameKindOf(start)) {

			Point position = new Point(start.getPosition().x + (target.getPosition().x - start.getPosition().x) / 2,
					start.getPosition().y + (target.getPosition().y - start.getPosition().y) / 2);
			return new Arc(position, start, target, net.newId(Element.ARC));
		}
		else return null;	
	}

	public void draw(Graphics2D graphics) {
		
		graphics.setColor(Color.BLACK);
		if (line != null) line.draw(graphics);
			//graphics.draw(line);
	}

	public boolean isType(int type) {

		return type == Sketch.ARC_SKETCH;
	}
}
