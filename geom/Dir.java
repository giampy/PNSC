package geom;


import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;

public final class Dir {
	
	public static final double EAST = 0;
	public static final double WEST = Math.PI;
	public static final double SOUTH = Math.PI / 2;
	public static final double NORTH = (Math.PI / 2) * 3;
	
	public static Point endOf(Point start, double orientazione, double lunghezza) {
		
		return new Point(start.x + (int)(lunghezza * Math.cos(orientazione)), start.y + (int)(lunghezza * Math.sin(orientazione)));
	}

	public static double opposite(double orientazione) {

		return (orientazione + Math.PI) % (Math.PI * 2);
	}
	
	public static double perpendicularLeft(double orientazione) {
		
		return (orientazione + ((Math.PI / 2) * 3)) % (Math.PI * 2);
	}

	public static double perpendicularRight(double orientazione) {
		
		return (orientazione + (Math.PI / 2)) % (Math.PI * 2);
	}
	
	public static double linearDistance(Point from, Point to) {
		
		return Math.sqrt(Math.pow((from.x - to.x), 2) + Math.pow((from.y - to.y), 2));
	}

	public static boolean intersectsCar(Line2D.Double traiettoria, Polygon shape) {
		
		for (int l = 0; l < shape.npoints; l++) {
			Line2D.Double lato = new Line2D.Double(shape.xpoints[l], shape.ypoints[l], shape.xpoints[(l + 1) % shape.npoints], shape.ypoints[(l + 1) % shape.npoints]);
			if (traiettoria.intersectsLine(lato))
				return true;
		}
			
		return false;
	}
}
