package gui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;

import xml.Properties;
import xml.Settings;

import animation.RandomTimer;



public class SettingsMenu extends JMenu implements Closeable {

	public SettingsMenu(MainPane mainPane) {
		
		super("Settings");
		
		CheckableSubMenu gridSettingsItem = new CheckableSubMenu("Grid settings", this);
		gridSettingsItem.add(showGridItem(mainPane));
		gridSettingsItem.add(snapToGridItem(mainPane));
		gridSettingsItem.add(gridSizeItem(mainPane));
		add(gridSettingsItem);
		addSeparator();
		add(showNodesNameItem(mainPane));
		addSeparator();
		add(executionSpeedItem(mainPane));
		addSeparator();
		add(compareItem(mainPane));
		add(mode(mainPane));
	}
	
	public JCheckBoxMenuItem mode(final MainPane mainPane){
         JCheckBoxMenuItem modeCheckingMenu=new JCheckBoxMenuItem("Real Time Verification Mode", false);
         modeCheckingMenu.addActionListener(new ActionListener(){
        	 public void actionPerformed(ActionEvent e){
        		JCheckBoxMenuItem tmp=(JCheckBoxMenuItem)e.getSource();
        		if(tmp.getState())
        			Properties.setCheckRealTime(true);       
        		else
        			Properties.setCheckRealTime(false);
        		
        		mainPane.getToolbar().createUpdateCheckBar();
        	 }
         });
		 return modeCheckingMenu;
	}
	
	
	public void close(JMenu menu) {
		
		setPopupMenuVisible(false);
		MouseListener[] ml = menu.getMouseListeners();
		for (int l = 0; l < ml.length; l++)
			ml[l].mouseExited(new MouseEvent(this, 0, 0, 0, 0, 0, 0, false));
		menu.setPopupMenuVisible(false);
		setSelected(false);
	}
	
	private JCheckBoxMenuItem showGridItem(final MainPane mainPane) {
		
		final JCheckBoxMenuItem item = new JCheckBoxMenuItem("Show grid", Settings.isShowGridOn());
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Settings.setShowGrid(item.isSelected());
				mainPane.repaint();
			}
		});
		
		return item;
	}

	private JCheckBoxMenuItem snapToGridItem(final MainPane mainPane) {
		
		final JCheckBoxMenuItem item = new JCheckBoxMenuItem("Snap to grid", Settings.isSnapToGridOn());
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Settings.setSnapToGrid(item.isSelected());
			}
		});
		
		return item;
	}
	
	private JMenuItem gridSizeItem(final MainPane mainPane) {
		
		JMenuItem item = new JMenuItem("Grid size");
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int value = IntegerDialog.open("Enter the grid cell's size", Settings.gridSize());
				Settings.setGridSize(value);
				mainPane.repaint();
			}
		});
		
		return item;
	}
	
	private JCheckBoxMenuItem showNodesNameItem(final MainPane mainPane) {
		
		final JCheckBoxMenuItem item = new JCheckBoxMenuItem("Show nodes' names", Settings.isShowNodesNameOn());
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Settings.setShowNodesName(item.isSelected());
				mainPane.repaint();
			}
		});
		
		return item;
	}

	private JMenuItem executionSpeedItem(final MainPane mainPane) {
		
		JMenuItem item = new JMenuItem("Execution speed");
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int value = IntegerDialog.open("Enter the delay between each firing", Settings.executionDelay());
				Settings.setExecutionDelay(value);
				if (mainPane.getNet().isExecuting()) {
					
					mainPane.getNet().stop();
					mainPane.getNet().execute(new RandomTimer(mainPane.getNet(), mainPane));
				}
				mainPane.repaint();
			}
		});
		
		return item;
	}
	
	private JCheckBoxMenuItem compareItem(final MainPane mainPane) {
		
		final JCheckBoxMenuItem item = new JCheckBoxMenuItem("Comparison Mode", Settings.isCompareMode());
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.setCompareMode(item.isSelected());
			}
		});
		
		return item;
	}
}
