package gui;



public interface MultiNet {
	
	public static boolean COMPARE_PANE = true;
	public static boolean MULTITAB_PANE = false;
	
	public boolean is(boolean kind);

	public void newNet(NetPanel net);
	
	public int removeNet(int index);
	
	public void setSelected(int index);
	
	public NetPanel getPanel(int selected);
	
	public void updateName(int selected, String name);
}
