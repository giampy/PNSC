package gui;


import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;

public class EditingModeBar {

	protected Vector<AbstractButton> buttons = new Vector<AbstractButton>();
	protected ButtonGroup buttonGroup = new ButtonGroup();
	
	protected void add(JToggleButton button) {
		
		buttons.add(button);
		buttonGroup.add(button);
	}
	
	protected void add(JButton button) {
		
		buttons.add(button);
	}
	
	public void addTo(ToolBar toolBar) {
		
		for (int b = 0; b < buttons.size(); b++)
			toolBar.add(buttons.get(b));
	}
	
	public void select(ComposeMode cm) {
		
		buttons.get(cm.getKey()).setSelected(true);
	}
}
