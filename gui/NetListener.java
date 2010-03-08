package gui;




import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import structure.Net;
import xml.Properties;


public class NetListener implements  MouseListener, MouseMotionListener {

	private Net net;
	private NetPanel netPanel;
	private MainPane mainPane;
	
	public NetListener(Net net, NetPanel netPanel, MainPane mainPane) {
		
		this.net = net;
		this.netPanel = netPanel;
		this.mainPane = mainPane;
	}
	
	public void mouseClicked(MouseEvent me) {
		
		if (me.getClickCount() == 2)
			net.fire(me.getPoint());
		else 
			if (me.getButton() == MouseEvent.BUTTON1) {

				net.compose(mainPane.getComposeMode(), me.getPoint(), me.isControlDown());
				netPanel.requestFocus();
				netPanel.repaint();
			} else if (me.getButton() == MouseEvent.BUTTON3) {

				netPanel.showPopup(me.getPoint());
				netPanel.requestFocus();
				netPanel.repaint();
			}
			mainPane.getToolbar().updateLabel();
		}


	public void mouseEntered(MouseEvent me) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent me) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent me) {
		
		if (me.getButton() == MouseEvent.BUTTON1) 
			net.aboutToDrag(mainPane.getComposeMode(), me.getPoint());
	}

	public void mouseReleased(MouseEvent me) {
	
		
		if (me.getButton() == MouseEvent.BUTTON1) {

			net.quitDragging(me.getPoint(), me.isControlDown());
			netPanel.requestFocus();
			mainPane.repaint();
			mainPane.getToolbar().updateLabel();
		}
	}

	public void mouseDragged(MouseEvent me) {

		net.drag(me.getPoint());
		netPanel.requestFocus();
		mainPane.repaint();
	}

	public void mouseMoved(MouseEvent me) {
		net.justHovering(me.getPoint());
		mainPane.repaint();
	}
}
