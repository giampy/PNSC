package gui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

public class ElementaryNetEditingModeBar extends EditingModeBar {

	public ElementaryNetEditingModeBar(MainPane mainPane) {
	
		add(selectButton(mainPane));
		add(stateButton(mainPane));
		add(lowTransitionButton(mainPane));
		add(downgradeTransitionButton(mainPane));
		add(highTransitionButton(mainPane));
		add(arcButton(mainPane));
		add(tokenButton(mainPane));
		add(setInitialMarkingButton(mainPane));
		add(deleteButton(mainPane));
	}

	private JToggleButton selectButton(final MainPane mainPane) {
		
		JToggleButton button = new JToggleButton(new ImageIcon("toolbarButtonGraphics/general/Select16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.setComposeMode(ComposeMode.SELECT);
			}
		});
		button.setToolTipText("Select");
		
		return button;
	}

	private JToggleButton stateButton(final MainPane mainPane) {
		
		JToggleButton button = new JToggleButton(new ImageIcon("toolbarButtonGraphics/general/State16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.setComposeMode(ComposeMode.STATE);				
			}
		});
		button.setToolTipText("Place");
		
		return button;
	}

	private JToggleButton lowTransitionButton(final MainPane mainPane) {
		
		JToggleButton button = new JToggleButton(new ImageIcon("toolbarButtonGraphics/general/LowTransition16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.setComposeMode(ComposeMode.LOW_TRANSITION);
			}
		});
		button.setToolTipText("Low Transition");
		
		return button;
	}
	private JToggleButton downgradeTransitionButton(final MainPane mainPane) {
		
		JToggleButton button = new JToggleButton(new ImageIcon("toolbarButtonGraphics/general/DowngradeTransition16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.setComposeMode(ComposeMode.DOWNGRADE_TRANSITION);
			}
		});
		button.setToolTipText("Downgrade Transition");
		
		return button;
	}


	private JToggleButton highTransitionButton(final MainPane mainPane) {
		
		JToggleButton button = new JToggleButton(new ImageIcon("toolbarButtonGraphics/general/HighTransition16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.setComposeMode(ComposeMode.HIGH_TRANSITION);
			}
		});
		button.setToolTipText("High Transition");
		
		return button;
	}

	private JToggleButton arcButton(final MainPane mainPane) {
		
		JToggleButton button = new JToggleButton(new ImageIcon("toolbarButtonGraphics/general/Arc16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.setComposeMode(ComposeMode.ARC);
			}
		});
		button.setToolTipText("Arc");
		
		return button;
	}

	private JToggleButton tokenButton(final MainPane mainPane) {
		
		JToggleButton button = new JToggleButton(new ImageIcon("toolbarButtonGraphics/general/Token16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.setComposeMode(ComposeMode.TOKEN);
			}
		});
		button.setToolTipText("Token");
		
		return button;
	}

	private JButton setInitialMarkingButton(final MainPane mainPane) {
		
		JButton button = new JButton(new ImageIcon("toolbarButtonGraphics/general/SetMarking16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (mainPane.getNet() != null) {

					mainPane.fixInitialMarking();
					mainPane.updateNodesProperties();
					mainPane.repaint();

					JOptionPane.showMessageDialog(null, "Initial marking set\n\n" + 
							"Remember that when you edit the marking or it changes due\n" + 
							"to a transition firing, only the current marking changes, not\n" + 
							"the initial. To do so, you can set the initial marking again\n" + 
							"anytime.\n" +
							"Hollow tokens inside the places will always show you what\n" + 
							"the initial marking is.", "Initial marking set", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		button.setToolTipText("Set this as initial marking");
		
		return button;
	}

	private JToggleButton deleteButton(final MainPane mainPane) {
		
		JToggleButton button = new JToggleButton(new ImageIcon("toolbarButtonGraphics/general/Delete16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.setComposeMode(ComposeMode.DELETE);
			}
		});
		button.setToolTipText("Delete");
		
		return button;
	}
}
