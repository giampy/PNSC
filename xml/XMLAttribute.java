package xml;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XMLAttribute {

	private String name;
	private String value;
	
	public XMLAttribute(Node node) {
		
		name = node.getNodeName();
		value = node.getNodeValue();
	}
	
	public XMLAttribute(String name, String value) {
		
		this.name = name;
		this.value = value;
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
	
	public Attr getAttribute(Document doc) {
		
		Attr attr = doc.createAttribute(name);
		attr.setNodeValue(value);
		
		return attr;
	}
}
