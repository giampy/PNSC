package gui;


public class ComposeMode {

	public static ComposeMode SELECT = new ComposeMode(0);
	public static ComposeMode STATE = new ComposeMode(1);
	public static ComposeMode LOW_TRANSITION = new ComposeMode(2);
	public static ComposeMode HIGH_TRANSITION = new ComposeMode(3);
	public static ComposeMode DOWNGRADE_TRANSITION = new ComposeMode(7);
	public static ComposeMode ARC = new ComposeMode(4);
	public static ComposeMode TOKEN = new ComposeMode(5);
	public static ComposeMode DELETE = new ComposeMode(6);
	
	private int modeKey;
	
	private ComposeMode(int modeKey) {
		
		this.modeKey = modeKey;
	}
	
	public int getKey() {
		
		return modeKey;
	}
	
	public boolean is(ComposeMode mode) {
		
		return modeKey == mode.getKey();
	}
}
