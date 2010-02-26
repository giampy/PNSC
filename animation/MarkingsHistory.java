package animation;



import java.util.Vector;

import structure.Place;


public class MarkingsHistory extends Vector<Vector<Place>> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3569039003839246309L;
	private int selected = -1;
	
	public MarkingsHistory() {
		
		super();
	}
	
	public Vector<Place> getSelected() {
		
		if (selected < 0 || selected >= size())
			return null;
		else return get(selected);
	}
	
	public int getSelectedIndex() {
		
		return selected;
	}
	
	public void addMarking(Vector<Place> places) {
		
		if (getSelected() == null || !(getSelected().containsAll(places) && places.containsAll(getSelected()))) {

			removeRange(selected + 1, size());
			add(places);
			selected++;
		}
	}
	
	public void back() {
		
		if (selected > 0)
			selected--;
	}
	
	public void forward() {
		
		if (selected < size() - 1) 
			selected++;
	}
}
