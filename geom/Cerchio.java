package geom;


import java.awt.Point;

public class Cerchio {
	
	protected Point center;
	protected double raggio;
	
	public Cerchio(Point center, double raggio) {
		
		this.center = center;
		this.raggio = raggio;
	}
	
	public Point getCenter() {
		
		return center;
	}
	
	public double getRaggio() {
		
		return raggio;
	}
	
	public Point getPointAtAngle(double angle) {
		
		return Dir.endOf(center, angle, raggio);
	}
	
	public double getAngle(Point point) {

		double xDiff = (point.x - center.x);
		double cos =  (xDiff == 0)?0:xDiff / raggio;

		double yDiff = (point.y - center.y);
		double negSin =  (yDiff == 0)?0:yDiff / raggio;

		double angle;
		if (negSin > 0) {
			if (cos < 0) {
				angle = Math.asin(negSin * -1) + Math.PI; // Quadrante sud - ovest 
			} else {
				angle = Math.asin(negSin); // Quadrante sud - est 
			}
		} else {
			if (cos < 0) {
				angle = Math.acos(cos) * -1; // Quadrante nord - ovest
			} else {
				angle = Math.asin(negSin); // Quadrante nord - est
			}
		}
		
		return angle;
	}

}