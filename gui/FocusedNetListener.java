package gui;



import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FocusedNetListener implements MouseListener {

	private NetPanel netPanel;
	private MainPane mainPane;
	
	public FocusedNetListener(NetPanel netPanel, MainPane mainPane) {
		
		this.netPanel = netPanel;
		this.mainPane = mainPane;
	}
	
	public void mouseClicked(MouseEvent arg0) {
	
		if (!netPanel.alreadyHasFocus()) {

			netPanel.requestFocus();
			mainPane.repaint();
		} else {
			
			netPanel.disableFocus();
			mainPane.repaint();
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mainPane.getToolbar().updateLabel();

	}

}
