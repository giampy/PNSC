package algo;



public class TransitionTemplate {
	
	public static TransitionTemplate NULL_TRANSITION = new TransitionTemplate("null", false);
	
	public String name;
	public boolean isHigh;
	
	public TransitionTemplate(String name, boolean high) {
		
		this.name = name;
		isHigh = high;
	}
	
	public String toString() {
		
		return name;
	}
}
