package xml;


import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLTree {
	
	private XMLNode root;
	private Vector<XMLTree> children;
	
	public XMLTree(Node node) {
		
		root = new XMLNode(node);
		
   		children = new Vector<XMLTree>();
		if (node.hasChildNodes())
    		for (Node kid = node.getFirstChild(); kid != null; kid = kid.getNextSibling())
    			if (kid.getNodeType() == Node.ELEMENT_NODE)
    				children.add(new XMLTree(kid));
	}
	
	public XMLTree(XMLNode root) {
		
		this.root = root;
		children = new Vector<XMLTree>();
	}
	
	public XMLTree(XMLNode root, Vector<XMLTree> children) {
		
		this.root = root;
		this.children = children;
	}
	
	public void addChild(XMLTree child) {
		
		children.add(child);
	}

	public XMLNode root() {
		
		return root;
	}
	
	public Vector<XMLTree> children() {
		
		return children;
	}
	
	public XMLNode findFirst(String name) {
		
		if (root.name().equals(name))
			return root;
		else {
			
			XMLNode result = null;
			for (int c = 0; c < children.size() && result == null; c++)
				result = children.get(c).findFirst(name);
			
			if (result != null)
				return result;
			else return null;
		}
	}
	
	public Element getElement(Document doc) {
		
		Element element = root.getElement(doc);
		
		for (int c = 0; c < children.size(); c++) {
			
			element.appendChild(children.get(c).getElement(doc));
			element.appendChild(doc.createTextNode("\n"));
		}
		
		return element;
	}
}
