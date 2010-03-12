package gui;







import javax.swing.JPopupMenu;

import structure.Net;


public class PopupMenu extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3625695005126865409L;
	protected Net net;
	protected NetPanel netPanel;
	
	public PopupMenu() {
		
		super();
	}
	
	public void setNet(NetPanel netPanel) {
		
		this.netPanel = netPanel;
		net = netPanel.getNet();
	}

	public Net getNet() {
		
		return net;
	}
	
	public NetPanel getNetPanel() {
		
		return netPanel;
	}
}
