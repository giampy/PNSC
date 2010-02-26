package structure;


import geom.Cerchio;
import geom.Dir;
import geom.Line;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Vector;

import loadstore.XMLAttribute;
import loadstore.XMLNode;
import loadstore.XMLTree;


public class Arc extends Element {

	private Node source, target;
	
	private double posDistFromLine = 0;
	private double posRatioDistFromSource = 0.5;
	
	public Arc(Point point, Node source, Node target, String id) {
		
		super(Element.ARC, id);
		
		this.source = source;
		this.target = target;
	
		source.add(this);
		target.add(this);
		
		if (point != null)
			setPosition(point);
		else setPosition(new Point((source.getPosition().x + target.getPosition().x) / 2,
				(source.getPosition().y + target.getPosition().y) / 2));
		lastStablePosition = getPosition();
		shape = new Line(source.getPointOfBorderTowards(getPosition()), getPosition(),  target.getPointOfBorderTowards(getPosition()));
		color = Color.BLACK;
	}
	
	public Node getSource() {
		
		return source;
	}
	
	public Node getTarget() {
		
		return target;
	}
	
	public void deleteFrom(Net net) {
		
		source.remove(this);
		target.remove(this);
		net.remove(this);
	}
	
	public XMLTree toPnml() {
		
		XMLNode root = new XMLNode("arc", "");
		root.addAttribute(new XMLAttribute("id", String.valueOf(id)));
		root.addAttribute(new XMLAttribute("source", String.valueOf(source.getId())));
		root.addAttribute(new XMLAttribute("target", String.valueOf(target.getId())));
		
		Vector<XMLTree> children = new Vector<XMLTree>();
		children.add(getPnmlPosition());
		
		return new XMLTree(root, children);
	}
	
	public void setPosition(Point point) {
		
		Line2D.Double line = new Line2D.Double(source.getPosition(), target.getPosition());
		
		double sign = line.relativeCCW(point) >= 0 ? 1 : -1;
		posDistFromLine = line.ptLineDist(point) * sign;
		
		double distSourcePosition = Dir.linearDistance(source.getPosition(), point);
		
		double distSourcePerpIntersection = Math.sqrt(Math.pow(distSourcePosition, 2) - Math.pow(posDistFromLine, 2));
		double distSourceTarget = Dir.linearDistance(source.getPosition(), target.getPosition());
		
		Cerchio cerchio = new Cerchio(source.getPosition(), distSourceTarget);
		double angle = cerchio.getAngle(target.getPosition());
		
		double perpAngle = Dir.perpendicularLeft(angle);
		Line2D.Double perpLine = new Line2D.Double(Dir.endOf(source.getPosition(), perpAngle, Math.abs(posDistFromLine)), 
				Dir.endOf(source.getPosition(), Dir.opposite(perpAngle), Math.abs(posDistFromLine)));
		double perpSign = perpLine.relativeCCW(point) >= 0 ? 1 : -1;
		
		posRatioDistFromSource = (distSourcePerpIntersection / distSourceTarget) * perpSign;
	}
	
	public Point getPosition() {
		
		double distSourceTarget = Dir.linearDistance(source.getPosition(), target.getPosition());

		Cerchio cerchio = new Cerchio(source.getPosition(), distSourceTarget);
		double angle = cerchio.getAngle(target.getPosition());
		
		Point midPoint = Dir.endOf(source.getPosition(), Dir.perpendicularLeft(angle), posDistFromLine);
		return Dir.endOf(midPoint, angle, distSourceTarget * posRatioDistFromSource);
	}
	
	public void moveTarget(Point point) {
		
		setPosition(point);
		point = getPosition();
		shape.setPosition(point);
		((Line)shape).setEnds(source.getPointOfBorderTowards(point), target.getPointOfBorderTowards(point));
	}
	
	public Element settleTarget(Point point) {
		
		Point before = new Point(lastStablePosition);
		lastStablePosition = getPosition();
		
		// Senseless return value
		if (!lastStablePosition.equals(before))
			return getSource();
		else return null;
	}
	
	public void updateArc() {
		
	//	Point midPos = new Point(position.x + offset.x, position.y + offset.y);
		shape = new Line(source.getPointOfBorderTowards(getPosition()), getPosition(),  target.getPointOfBorderTowards(getPosition()));
	}
}
