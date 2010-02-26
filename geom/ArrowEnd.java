package geom;


import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;


public class ArrowEnd implements LineEnd {

	private Line line;
	
	public ArrowEnd(Line line) {
		
		this.line = line;
	}
	
	public void paint(Graphics2D graphics) {

		Point end = line.getEnd();
		Point ctrl = line.getPosition();
		
		Cerchio ctrlC = new Cerchio(end, Dir.linearDistance(end, ctrl));
		double angle = ctrlC.getAngle(ctrl);
		
		Cerchio ctrlE = new Cerchio(end, 10);
		Point leftSide = ctrlE.getPointAtAngle(angle - Math.PI / 6);
		Point rightSide = ctrlE.getPointAtAngle(angle + Math.PI / 6);
		
		graphics.draw(new Line2D.Double(end, leftSide));
		graphics.draw(new Line2D.Double(end, rightSide));
	}

}
