package geom;




import java.awt.Graphics2D;
import java.awt.Point;

import structure.Element;


public interface Sketch {
	
	public static int SELECTING_AREA = 0;
	public static int ARC_SKETCH = 1;
	public static int ELEMENT_SET = 2;

	public void moveTarget(Point point);
	
	public Element settleTarget(Point point);
	
	public void draw(Graphics2D graphics);
	
	public boolean isType(int type);
}
