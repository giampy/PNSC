package loadstore;




import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Document;

import structure.Arc;
import structure.Net;
import structure.Node;
import structure.Place;
import structure.Transition;

public class XMLDocument {

	private XMLTree tree;
	private String filePath;
	
	public XMLDocument(String filePath) throws Exception {

		this.filePath = filePath;
		
		try {
			
			File file = new File(filePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			
			tree = new XMLTree(doc.getDocumentElement());
		} catch (Exception e) {
			
			throw new Exception(filePath + " does not appear to be valid XML file");
		}
	}
	
	public XMLDocument(Net net, String filePath) {
		
		this.filePath = filePath;
		
		tree = net.toPNML();
	}
	
	public void save() {

		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			
			doc.appendChild(tree.getElement(doc));
			
			File xmlOutputFile = new File(filePath);
			FileOutputStream fos;
			Transformer transformer;
			fos = new FileOutputStream(xmlOutputFile);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(fos);
			
			transformer.transform(source, result);
			
			fos.close();
			
		} catch (Exception e) {

			System.out.println("Problems occurred while saving the xml file \n");
			e.printStackTrace();
		}
	}
	
	public Net extractNet() {

		if (tree.root().name().equals("pnml")) {
			
			for (int n = 0; n < tree.children().size(); n++) {

				Vector<XMLTree> nets = tree.children();
				if (nets.get(n).root().name().equals("net")) {
				
					Net net = new Net(filePath);

					for (int c = 0; c < nets.get(n).children().size(); c++) {

						Node node = extractNode(nets.get(n).children().get(c));
						if (node != null) 
							net.add(node);
					}
					
					for (int c = 0; c < nets.get(n).children().size(); c++) {
						
						Arc arc = extractArc(nets.get(n).children().get(c), net);
						if (arc != null)
							net.add(arc);
					}

					net.fixInitialMarking();
					net.getMarkingHistory().addMarking(net.getInitialMarking());
					net.updateNodesProperties();
					
					return net;
				}
			}
		}
		
		return null;
	}
	
	private Node extractNode(XMLTree tree) {
		
		if (tree.root().name().equals("place")) 
			return new Place(extractPosition(tree), extractTokens(tree), extractId(tree));
		else if (tree.root().name().equals("transition")) 
			return new Transition(extractPosition(tree), extractLevel(tree), extractId(tree));
		else return null;
	}
	
	private Arc extractArc(XMLTree tree, Net net) {
		
		if (tree.root().name().equals("arc"))
			return new Arc(extractPosition(tree), net.findWhoseNodeIdIs(extractSourceId(tree)), net.findWhoseNodeIdIs(extractTargetId(tree)), extractId(tree));
		else return null;
	}
	
	private String extractId(XMLTree tree) {
		
		Vector<XMLAttribute> attributes = tree.root().attributes();
		for (int a = 0; a < attributes.size(); a++)
			if (attributes.get(a).name().equals("id"))
				return attributes.get(a).value();
		
		return null;
	}
	
	private String extractSourceId(XMLTree tree) {
		
		Vector<XMLAttribute> attributes = tree.root().attributes();
		for (int a = 0; a < attributes.size(); a++)
			if (attributes.get(a).name().equals("source"))
				return attributes.get(a).value();
		
		return null;
	}
	
	private String extractTargetId(XMLTree tree) {
		
		Vector<XMLAttribute> attributes = tree.root().attributes();
		for (int a = 0; a < attributes.size(); a++)
			if (attributes.get(a).name().equals("target"))
				return attributes.get(a).value();
		
		return null;
	}
	
	private Point extractPosition(XMLTree tree) {
		
		for (int c = 0; c < tree.children().size(); c++)
			if (tree.children().get(c).root().name().equals("graphics")) {
				
				Vector<XMLTree> gTree = tree.children().get(c).children();
				
				for (int g = 0; g < gTree.size(); g++)
					if (gTree.get(g).root().name().equals("position")) {

						Vector<XMLAttribute> attributes = gTree.get(g).root().attributes();

						int x = 0; 
						int y = 0;
						for (int a = 0; a < attributes.size(); a++) {
							if (attributes.get(a).name().equals("x")) 
								x = (int)Double.parseDouble(attributes.get(a).value());
							else if (attributes.get(a).name().equals("y"))
								y = (int)Double.parseDouble(attributes.get(a).value());
						}

						return new Point(x, y);
					}
			}
		
		return null;
	}
	
	private int extractTokens(XMLTree tree) {
		
		for (int c = 0; c < tree.children().size(); c++)
			if (tree.children().get(c).root().name().equals("initialMarking")) {
				
				Vector<XMLTree> gTree = tree.children().get(c).children();
				
				for (int g = 0; g < gTree.size(); g++)
					if (gTree.get(g).root().name().equals("value")) 
						return Integer.parseInt(gTree.get(g).root().value());
			}
		
		return 0;
	}
	
	private boolean extractLevel(XMLTree tree) {
		
		for (int c = 0; c < tree.children().size(); c++)
			if (tree.children().get(c).root().name().equals("toolspecific")) {
				
				Vector<XMLTree> gTree = tree.children().get(c).children();
				
				for (int g = 0; g < gTree.size(); g++)
					if (gTree.get(g).root().name().equals("level"))
						for (int a = 0; a < gTree.get(g).root().attributes().size(); a++)
							if (gTree.get(g).root().attributes().get(a).name().equals("high"))
									return Boolean.parseBoolean(gTree.get(g).root().attributes().get(a).value());
			}
		
		return false;
	}
	
	public XMLTree getTree() {
		
		return tree;
	}
}
