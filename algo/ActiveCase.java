package algo;




import java.util.Vector;

import structure.Place;
import structure.Transition;


public class ActiveCase extends Vector<Place> {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4448551723224808620L;
	private Transition high, low;
	private Vector<Case> pathLeading;
	
	public ActiveCase() {
		
		super();
	}
	
	public ActiveCase(Transition high, Transition low, Vector<Place> places, Vector<Case> path) {
		
		super();
		addAll(places);

		this.high = high;
		this.low =low;
		
		pathLeading = path;
	}
	
	public void setHigh(Transition high) {
		
		this.high = high;
	}
	
	public Transition high() {
		
		return high;
	}
	
	public void setLow(Transition low) {
		
		this.low = low;
	}
	
	public Transition low() {
		
		return low;
	}
	
	public void setPath(Vector<Case> path) {
		
		pathLeading = path;
	}
	
	public Vector<Case> path() {
		
		return pathLeading;
	}
	
	public boolean isSameMarking(Vector<Place> ac) {
		
		return (containsAll(ac) && ac.containsAll(this));
	}
}
