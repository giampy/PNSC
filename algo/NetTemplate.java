package algo;



import java.util.Hashtable;
import java.util.Vector;

import structure.Arc;
import structure.Net;
import structure.Node;
import structure.Place;
import structure.Transition;


public class NetTemplate {
	
	private Vector<Place> places = new Vector<Place>();
	private Vector<Transition> transitions = new Vector<Transition>();
	private Vector<Arc> arcs = new Vector<Arc>();
	
	private Vector<Place> initialMarking = new Vector<Place>();

	public NetTemplate(Net net) {
		
		Hashtable<Node, Node> newNodes = new Hashtable<Node, Node>();
		
		Vector<Place> places = net.getPlaces();
		for (int s = 0; s < places.size(); s++) {
			
			Place newPlace = new Place(places.get(s));
			newNodes.put(places.get(s), newPlace);
			this.places.add(newPlace);
		}
		
		Vector<Transition> transitions = net.getTransitions();
		for (int t = 0; t < transitions.size(); t++) {
			
			Transition newTransition = new Transition(transitions.get(t));
			newNodes.put(transitions.get(t), newTransition);
			this.transitions.add(newTransition);
		}
		
		Vector<Arc> arcs = net.getArcs();
		for (int a = 0; a < arcs.size(); a++) {
			
				Arc arc = arcs.get(a);
				this.arcs.add(new Arc(arc.getPosition(), newNodes.get(arc.getSource()), newNodes.get(arc.getTarget()), arc.getId()));
		}
		
		Vector<Place> initialMarking = net.getInitialMarking();
		for (int i = 0; i < initialMarking.size(); i++)
			if (newNodes.get(initialMarking.get(i)) != null)
			this.initialMarking.add((Place)newNodes.get(initialMarking.get(i)));
	}
	
	public Vector<Place> getPlaces() {
		
		return places;
	}
	
	public Vector<Transition> getTransitions() {
		
		return transitions;
	}
	
	public Vector<Arc> getArcs() {
		
		return arcs;
	}
	
	public Vector<Place> getInitialMarking() {
		
		return initialMarking;
	}
}
