package gui;


import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class CheckableSubMenu extends JMenu implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2894554295518770665L;
	private JCheckBoxMenuItem cbmi;
	private Closeable menu;
	
	public CheckableSubMenu(String title, Closeable menu) {
		
		super(title);
		this.menu = menu;
		
		addMouseListener(this);
		
		cbmi = new JCheckBoxMenuItem(getText());
	}
	
	public JMenuItem add(JMenuItem item) {
		
		item = super.add(item);
		item.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				checkIfAllChildrenAreChecked();
			}
		});
		
		checkIfAllChildrenAreChecked();
		
		return item;
	}

	private void checkIfAllChildrenAreChecked() {
		
		boolean allChecked = true;
		
		Component[] comp = getMenuComponents();
		for (int c = 0; c < comp.length; c++) 
			if (comp[c] instanceof JCheckBoxMenuItem && !((JCheckBoxMenuItem)comp[c]).isSelected())
				allChecked = false;
		
		cbmi.setSelected(allChecked);
	}
	
	private boolean areAllChildrenEnabled() {
		
		boolean allEnabled = true;
		
		Component[] comp = getMenuComponents();
		for (int c = 0; c < comp.length; c++) 
			if (!comp[c].isEnabled())
				allEnabled = false;
		
		return allEnabled;
	}
	
	public void paint(Graphics g) {
		
		super.paint(g);
		Rectangle rect = getBounds();
		cbmi.setBounds(rect.x, rect.y, rect.width - 12, rect.height);
		cbmi.paint(g);
	}

	public void mouseClicked(MouseEvent arg0) { 

		if (areAllChildrenEnabled()) {

			menu.close(this);
			
			boolean newSelection = !cbmi.isSelected();
			cbmi.setSelected(newSelection);

			Component[] comp = getMenuComponents();
			for (int c = 0; c < comp.length; c++)
				if (comp[c] instanceof JCheckBoxMenuItem && ((JCheckBoxMenuItem)comp[c]).isSelected() != newSelection)
					(((JCheckBoxMenuItem)comp[c])).doClick();

			repaint();
		}
	}

	public void mouseEntered(MouseEvent arg0) {

		cbmi.setArmed(true);
	}

	public void mouseExited(MouseEvent arg0) {

		cbmi.setArmed(false);
	}

	public void mousePressed(MouseEvent arg0) { }

	public void mouseReleased(MouseEvent arg0) { }
}
