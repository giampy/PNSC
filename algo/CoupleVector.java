package algo;


import java.util.Vector;


public class CoupleVector extends Vector<Couple> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3432896741501125662L;

	public CoupleVector() {
		
		super();
	}
	
	public boolean contains(Couple couple) {
		
		for (int c = 0; c < size(); c++) 
			if (get(c).equals(couple))
				return true;
		
		return false;
	}
}
