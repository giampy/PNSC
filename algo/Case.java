package algo;




import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import structure.Node;
import structure.Place;
import structure.Transition;


public class Case extends Vector<Place>{

	private static final long serialVersionUID = -8690855404589186965L;
	private Hashtable<Transition, Case> links = new Hashtable<Transition, Case>();
	

	public Case() {
		
		super();
	
	}
	
	public Case(Vector<Place> places) {
		
		super(places);
		
	}
	
	public Vector<Place> toVector() {
		
		return this;
	}
	
	public void addLink(Transition transition, Case marking) {
		
		links.put(transition, marking);
	}

	public Transition firstTransition() {
		
		if (links.size() > 0)
			return links.keys().nextElement();
		else return null;
	}
	
	public Hashtable<Transition, Case> getLinks() {
		
		return links;		
	}
	
	public Vector<Transition> getEnabledTransitions() {
		
		Vector<Transition> transitions = new Vector<Transition>();
		Vector<Transition> enabledTransitions = new Vector<Transition>();
		
		for (int s = 0; s < size(); s++) {
			
			Vector<Node> postset = get(s).postset();
			for (int t = 0; t < postset.size(); t++)
				if (!transitions.contains(postset.get(t)))
					transitions.add((Transition)postset.get(t));
		}
		
		for (int t = 0; t < transitions.size(); t++) {
			
			if (containsAll(transitions.get(t).preset()) && containsNoneOf(transitions.get(t).postset()))
				enabledTransitions.add(transitions.get(t));
		}
			
		return enabledTransitions;
	}
	
	public Vector<Transition> getContacts() {
		
		Vector<Transition> contacts = new Vector<Transition>();
		Vector<Transition> transitions = new Vector<Transition>();
		
		for (int s = 0; s < size(); s++) {
			
			Vector<Node> postset = get(s).postset();
			for (int t = 0; t < postset.size(); t++)
				if (!transitions.contains(postset.get(t)))
					transitions.add((Transition)postset.get(t));
		}
		
		for (int t = 0; t < transitions.size(); t++) {
			
			if (containsAll(transitions.get(t).preset()) && !containsNoneOf(transitions.get(t).postset()))
				contacts.add(transitions.get(t));
		}
		
		return contacts;
	}
	
	private boolean containsNoneOf(Vector<Node> nodes) {
		
		for (int n = 0; n < nodes.size(); n++)
			if (contains(nodes.get(n)))
				return false;
		
		return true;
	}

	public boolean isLinkedTo(Case newCase) {

		Enumeration<Transition> keys = links.keys();
		while (keys.hasMoreElements()) {
			
			Case c = links.get(keys.nextElement());
			if (c.containsAll(newCase) && newCase.containsAll(c))
				return true;
		}
		
		return false;
	}
	
	public Case goThrough(Transition t) {

		if (links.get(t) != null)
			return links.get(t);
		else return new Case();
	}
	
	public boolean isSameMarking(Vector<Place> ac) {
		
		return (containsAll(ac) && ac.containsAll(this));
	}
	
	public String toString() {
		
		String result = "<";
		for (int s = 0; s < size(); s++) {
			
			if (s > 0) 
				result += ", ";
			result += get(s).toString();
		}
		
		return result + ">";
	}
	
	public String getOrderedValue(){
		String str="";
		for(int i=0; i<size(); ++i)
			str+=get(i).toString()+(get(i).getTokens()==1?"1":"0");
		return str;
	}
	
	
}
