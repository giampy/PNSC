package gui;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import loadstore.Settings;


public class Grid {

	public static Point closestAllowedPoint(Point point) {
		
		if (!Settings.isSnapToGridOn())
			return point;
		else {
			
			int x = Math.round(((float)point.x / Settings.gridSize())) * Settings.gridSize();
			int y = Math.round(((float)point.y / Settings.gridSize())) * Settings.gridSize();
			return new Point(x, y);
		}
	}
	
	public static void paint(NetPanel panel, Graphics2D g) {
		
		if (Settings.isShowGridOn()) {
			int side = Settings.gridSize();

			g.setColor(Color.LIGHT_GRAY);
			
			int maxX = (int)(((panel.getSize().width - side * 0.5) / side) * side);
			int maxY = (int)(((panel.getSize().height - side * 0.5) / side) * side);
			for (int x = side; x < (int)(panel.getSize().width - side * 0.5); x += side) 
				g.drawLine(x, (int)(side * 0.5), x, maxY);
			for (int y = side; y < (int)(panel.getSize().height - side * 0.5); y += side) 
				g.drawLine((int)(side * 0.5), y, maxX, y);
		}
	}
}
