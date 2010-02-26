package gui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import algo.NetTemplate;

public class UndoBar {

	protected Vector<JButton> buttons = new Vector<JButton>();
	
	protected UndoBar(final MainPane mainPane) {
		
		final JButton undoButton = new JButton(new ImageIcon("toolbarButtonGraphics/general/Undo16.gif"));
		final JButton redoButton = new JButton(new ImageIcon("toolbarButtonGraphics/general/Redo16.gif"));
		
		undoButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (mainPane.getNet() != null) {
					
					mainPane.getNet().getNetHistory().back();
					NetTemplate nt = mainPane.getNet().getNetHistory().getSelected();
					if (nt != null) {
						
						mainPane.getNet().setTo(nt);
						mainPane.updateNodesProperties();
						mainPane.repaint();
					}
				}
			}
		});
		undoButton.setToolTipText("Undo");
		
		// Forward Button
		redoButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (mainPane.getNet() != null) {

					mainPane.getNet().getNetHistory().forward();
					NetTemplate nt = mainPane.getNet().getNetHistory().getSelected();
					if (nt != null) {
						
						mainPane.getNet().setTo(nt);
						mainPane.updateNodesProperties();
						mainPane.repaint();
					}
				}
			}
		});
		redoButton.setToolTipText("Redo");

		buttons.add(undoButton);
		buttons.add(redoButton);
	}
	
	public void addTo(ToolBar toolBar) {
		
		for (int b = 0; b < buttons.size(); b++)
			toolBar.add(buttons.get(b));
	}
}
