package structure;

import java.util.Hashtable;
import java.util.Vector;

import algo.ActiveCase;

public class PositivePBNIResult {
	private Hashtable<Place, Vector<Transition>> potentialCausal;
	private Hashtable<Place, Vector<Transition>> potentialConflict;
	private Hashtable<Place, Vector<ActiveCase>> activeCausal;
	private Hashtable<Place, Vector<ActiveCase>> activeConflict;

	public PositivePBNIResult(){
		potentialCausal 	= new Hashtable<Place, Vector<Transition>> ();
		potentialConflict	= new Hashtable<Place, Vector<Transition>> ();
		activeCausal		= new Hashtable<Place, Vector<ActiveCase>>();
		activeConflict 		= new Hashtable<Place, Vector<ActiveCase>>();
	}
		
	public Hashtable<Place, Vector<Transition>> getPotentialCausal() {
		return potentialCausal;
	}

	public void setPotentialCausal(
			Hashtable<Place, Vector<Transition>> potentialCausal) {
		this.potentialCausal = potentialCausal;
	}

	public Hashtable<Place, Vector<Transition>> getPotentialConflict() {
		return potentialConflict;
	}

	public void setPotentialConflict(
			Hashtable<Place, Vector<Transition>> potentialConflict) {
		this.potentialConflict = potentialConflict;
	}

	public Hashtable<Place, Vector<ActiveCase>> getActiveCausal() {
		return activeCausal;
	}

	public void setActiveCausal(Hashtable<Place, Vector<ActiveCase>> activeCausal) {
		this.activeCausal = activeCausal;
	}

	public Hashtable<Place, Vector<ActiveCase>> getActiveConflict() {
		return activeConflict;
	}

	public void setActiveConflict(
			Hashtable<Place, Vector<ActiveCase>> activeConflict) {
		this.activeConflict = activeConflict;
	}
}
