package animation;



import java.util.Vector;

import structure.Net;

import algo.NetTemplate;


public class NetHistory extends Vector<NetTemplate> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5432565813422781493L;

	private int selected = -1;
	
	private int lastSavedIndex = 0;
	private boolean stillInTheTrail = true;
	
	public NetHistory() {
		
		super();
	}
	
	public boolean hasToBeSaved() {
		
		return !(getSelectedIndex() == lastSavedIndex && stillInTheTrail);
	}
	
	public void save() {
		
		lastSavedIndex = getSelectedIndex();
		stillInTheTrail = true;
	}
	
	public void erase() {
		
		selected = -1;
		removeAllElements();
		
		//System.out.println(selected + " - " + this);
	}
	
	public NetTemplate getSelected() {
		
		if (selected < 0 || selected >= size())
			return null;
		else return get(selected);
	}
	
	public int getSelectedIndex() {
		
		return selected;
	}
	
	public void addNet(Net net) {
		
		if (selected < lastSavedIndex && selected > -1)
			stillInTheTrail = false;
		
		removeRange(selected + 1, size());
		add(new NetTemplate(net));
		selected++;
		
		//System.out.println(selected + " - " + this);
	}
	
	public void back() {
		
		if (selected > 0)
			selected--;
		
		//System.out.println(selected + " - " + this);
	}
	
	public void forward() {
		
		if (selected < size() - 1) 
			selected++;
		
		//System.out.println(selected + " - " + this);
	}
}
