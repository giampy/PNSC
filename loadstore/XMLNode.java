package loadstore;


import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XMLNode {

	private String name;
	private String value;
	
	private Vector<XMLAttribute> attributes;
	
	public XMLNode(Node node) {
	
		name = node.getNodeName();
		value = getElementValue(node);
		
		attributes = new Vector<XMLAttribute>();
		NamedNodeMap attrs = node.getAttributes();
		for (int a = 0; a < attrs.getLength(); a++) 
			attributes.add(new XMLAttribute(attrs.item(a)));
	}
	
	public XMLNode(String name, String value) {
		
		this.name = name; 
		this.value = value;
		attributes = new Vector<XMLAttribute>();
	}
	
	public XMLNode(String name, String value, Vector<XMLAttribute> attributes) {
		
		this.name = name; 
		this.value = value;
		this.attributes = attributes;
	}

	public void addAttribute(XMLAttribute attribute) {
		
		attributes.add(attribute);
	}
	
	public String name() {
		
		return name;
	}
	
	public String value() {
		
		return value;
	}
	
	public void setValue(Object obj) {
		
		value = obj.toString();
	}
	
	public Vector<XMLAttribute> attributes() {
		
		return attributes;
	}
	
    private String getElementValue(Node elem) {
        
    	Node kid;
    	if (elem != null)
    		if (elem.hasChildNodes())
    			for (kid = elem.getFirstChild(); kid != null; kid = kid.getNextSibling())
    				if (kid.getNodeType() == Node.TEXT_NODE)
    					return kid.getNodeValue();

    	return null;
    }
    
    public Element getElement(Document doc) {
    	
    	Element element = doc.createElement(name);
    	
    	for (int a = 0; a < attributes.size(); a++) 
    		element.setAttributeNode(attributes.get(a).getAttribute(doc));
    	
    	element.appendChild(doc.createTextNode(value));
    	
    	return element;
    }
}
