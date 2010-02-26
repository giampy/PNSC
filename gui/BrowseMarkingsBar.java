package gui;




import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import structure.Net;


public class BrowseMarkingsBar {

	protected Vector<JButton> buttons = new Vector<JButton>();
	private final JButton previousButton, nextButton;
	private int shown;
	
	public BrowseMarkingsBar(final NetPanel netPanel, final Vector<Net> nets) {
		
		shown = 0;
		
		// Create both buttons at the same time so they can call each other
		previousButton = new JButton(new ImageIcon("toolbarButtonGraphics/navigation/Back16.gif"));
		nextButton = new JButton(new ImageIcon("toolbarButtonGraphics/navigation/Forward16.gif"));
		
		previousButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				shown--;
				netPanel.switchFocus(nets.get(shown));
				previousButton.setEnabled(shown > 0);
				nextButton.setEnabled(shown < nets.size() - 1);
			}
		});
		previousButton.setEnabled(shown > 0);
		previousButton.setToolTipText("Previous marking");
		
		nextButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				shown++;
				netPanel.switchFocus(nets.get(shown));
				previousButton.setEnabled(shown > 0);
				nextButton.setEnabled(shown < nets.size() - 1);
			}
		});
		nextButton.setEnabled(shown < nets.size() - 1);
		nextButton.setToolTipText("Next marking");
			
		buttons.add(previousButton);
		buttons.add(nextButton);
	}
	
	public void addTo(ToolBar toolBar) {
		
		for (int b = 0; b < buttons.size(); b++)
			toolBar.add(buttons.get(b), 26 + b);
		toolBar.revalidate();
	}
	
	public void removeFrom(ToolBar toolBar) {
		
		for (int b = 0; b < buttons.size(); b++)
			toolBar.remove(buttons.get(b));
		toolBar.revalidate();
		toolBar.repaint();
	}
}
