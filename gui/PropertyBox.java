package gui;




import java.util.Vector;

import structure.Node;
import structure.Place;
import structure.Transition;

import algo.ActiveCase;


public class PropertyBox {

	private Vector<Node> simple;
	private boolean reduced;
	private Vector<Vector<Place>> contacts;
	
	private Vector<Transition> potentialCausal, potentialConflict;
	private Vector<ActiveCase> activeCausal, activeConflict;
	
	public PropertyBox() {	
		
		simple = new Vector<Node>();
		reduced = true;
		contacts = new Vector<Vector<Place>>();
		
		potentialCausal = new Vector<Transition>();
		potentialConflict = new Vector<Transition>();
		
		activeCausal = new Vector<ActiveCase>();
		activeConflict = new Vector<ActiveCase>();
	}
	
	public boolean isSimple() {
		
		return simple.size() == 0;
	}
	
	public void setSimple(Vector<Node> simple) {
		
		this.simple = simple;
	}
	
	public Vector<Node> getSimple() {
		
		return simple;
	}

	public boolean isReduced() {
		
		return reduced;
	}
	
	public void setReduced(boolean reduced) {
		
		this.reduced = reduced;
	}
 	
	public boolean isContactFree() {
		
		return contacts.size() == 0;
	}
	
	public void setContacts(Vector<Vector<Place>> markings) {
		
		contacts = markings;
	}
	
	public Vector<Vector<Place>> getContacts() {
		
		return contacts;
	}
	
	public boolean isPotentialCausal() {
		
		return potentialCausal.size() > 0;
	}
	
	public void setPotentialCausal(Vector<Transition> transitions) {
		
		potentialCausal = transitions;
	}
	
	public Vector<Transition> getPotentialCausal() {
		
		return potentialCausal;
	}	
	
	public boolean isPotentialConflict() {
		
		return potentialConflict.size() > 0;
	}
	
	public void setPotentialConflict(Vector<Transition> transitions) {
		
		potentialConflict = transitions;
	}
	
	public Vector<Transition> getPotentialConflict() {
		
		return potentialConflict;
	}	
	
	public boolean isActiveCausal() {
		
		return activeCausal.size() > 0;
	}
	
	public void setActiveCausal(Vector<ActiveCase> active) {
		
		activeCausal = active;
	}
	
	public Vector<ActiveCase> getActiveCausal() {
		
		return activeCausal;
	}	
	
	public boolean isActiveConflict() {
		
		return activeConflict.size() > 0;
	}
	
	public void setActiveConflict(Vector<ActiveCase> active) {
		
		activeConflict = active;
	}
	
	public Vector<ActiveCase> getActiveConflict() {
		
		return activeConflict;
	}	
}
