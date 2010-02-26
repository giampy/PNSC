package algo;




import java.util.Hashtable;

import structure.Transition;


public class Vertex {
	
	private Case marking;
	private boolean root;
	private Hashtable<Transition, Vertex> links = new Hashtable<Transition, Vertex>();
	private Transition linkFromPrevious = null;
	private Vertex previous = null;
	
	public Vertex(Case marking, boolean root) {
		
		this.marking = marking;
		this.root = root;
	}
	
	public void mark(Vertex vertex, Transition linkFromPrevious) {
		
		if (!root && previous == null) {
			
			previous = vertex;
			this.linkFromPrevious = linkFromPrevious;
		}
	}
	
	public Transition getLinkFromPrevious() {
		
		return linkFromPrevious;
	}
	
	public Vertex getPrevious() {
		
		return previous;
	}
	
	public void addLink(Transition transition, Vertex vertex) {
		
		links.put(transition, vertex);
		vertex.mark(this, transition);
	}
	
	public Case getMarking() {
		
		return marking;
	}
	
	public boolean equals(Vertex vertex) {
		
		return marking.equals(vertex.getMarking());
	}

	
}
