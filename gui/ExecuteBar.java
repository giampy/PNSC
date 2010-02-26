package gui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import animation.RandomTimer;

public class ExecuteBar {

	protected Vector<JButton> buttons = new Vector<JButton>();
	
	protected ExecuteBar(final MainPane mainPane) {
		
		final JButton stopButton = new JButton(new ImageIcon("toolbarButtonGraphics/media/Stop16.gif"));
		final JButton backButton = new JButton(new ImageIcon("toolbarButtonGraphics/media/StepBack16.gif"));
		final JButton playButton = new JButton(new ImageIcon("toolbarButtonGraphics/media/Play16.gif"));
		final JButton forwardButton = new JButton(new ImageIcon("toolbarButtonGraphics/media/StepForward16.gif"));
		
		// Stop Button
		stopButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (mainPane.getNet() != null) {
					
					if (mainPane.getNet().isExecuting()) {
						
						mainPane.getNet().stop();
						playButton.setIcon(new ImageIcon("toolbarButtonGraphics/media/Play16.gif"));
						playButton.setToolTipText("Play");
					}
					mainPane.getNet().setCurrentMarking(mainPane.getNet().getInitialMarking());
					mainPane.getNet().getMarkingHistory().addMarking(mainPane.getNet().getInitialMarking());
					mainPane.repaint();
				}
			}
		});
		stopButton.setToolTipText("Back to initial marking");

		// Back Button
		backButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (mainPane.getNet() != null) {
					
					mainPane.getNet().getMarkingHistory().back();
					mainPane.getNet().setCurrentMarking(mainPane.getNet().getMarkingHistory().getSelected());
					
					mainPane.repaint();
				}
			}
		});
		backButton.setToolTipText("Step back");
		
		// Play Button
		playButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (mainPane.getNet() != null) {

					if (!mainPane.getNet().isExecuting()) {

						mainPane.getNet().execute(new RandomTimer(mainPane.getNet(), mainPane));
						playButton.setIcon(new ImageIcon("toolbarButtonGraphics/media/Pause16.gif"));
						playButton.setToolTipText("Stop");
					} else {

						mainPane.getNet().stop();
						playButton.setIcon(new ImageIcon("toolbarButtonGraphics/media/Play16.gif"));
						playButton.setToolTipText("Play");
					}
				}
			}
		});
		playButton.setToolTipText("Play");
		
		// Forward Button
		forwardButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (mainPane.getNet() != null) {

					mainPane.getNet().getMarkingHistory().forward();
					mainPane.getNet().setCurrentMarking(mainPane.getNet().getMarkingHistory().getSelected());

					mainPane.repaint();
				}
			}
		});
		forwardButton.setToolTipText("Step forward");

		buttons.add(stopButton);
		buttons.add(backButton);
		buttons.add(playButton);
		buttons.add(forwardButton);
	}
	
/*	private JButton stopButton(final MainPane mainPane) {

		JButton button = new JButton(new ImageIcon("toolbarButtonGraphics/media/Stop16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (mainPane.getNet() != null) {
					
					mainPane.getNet().setCurrentMarking(mainPane.getNet().getInitialMarking());
					mainPane.getNet().getMarkingHistory().addMarking(mainPane.getNet().getInitialMarking());
					mainPane.repaint();
				}
			}
		});
		button.setToolTipText("Back to initial marking");
		
		return button;
	}
	
	private JButton backButton(final MainPane mainPane) {

		JButton button = new JButton(new ImageIcon("toolbarButtonGraphics/media/StepBack16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (mainPane.getNet() != null) {
					
					mainPane.getNet().getMarkingHistory().back();
					mainPane.getNet().setCurrentMarking(mainPane.getNet().getMarkingHistory().getSelected());
					mainPane.repaint();
				}
			}
		});
		button.setToolTipText("Step back");
		
		return button;
	}
	
	private JButton playButton(final MainPane mainPane) {

		final JButton button = new JButton(new ImageIcon("toolbarButtonGraphics/media/Play16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (mainPane.getNet() != null) {

					if (!mainPane.getNet().isExecuting()) {

						mainPane.getNet().execute(new RandomTimer(mainPane.getNet(), mainPane));
						button.setIcon(new ImageIcon("toolbarButtonGraphics/media/Pause16.gif"));
						button.setToolTipText("Stop");
					} else {

						mainPane.getNet().stop();
						button.setIcon(new ImageIcon("toolbarButtonGraphics/media/Play16.gif"));
						button.setToolTipText("Play");
					}
				}
			}
		});
		button.setToolTipText("Play");
		
		return button;
	}
	
	private JButton forwardButton(final MainPane mainPane) {

		JButton button = new JButton(new ImageIcon("toolbarButtonGraphics/media/StepForward16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (mainPane.getNet() != null) {

					mainPane.getNet().getMarkingHistory().forward();
					mainPane.getNet().setCurrentMarking(mainPane.getNet().getMarkingHistory().getSelected());
					mainPane.repaint();
				}
			}
		});
		button.setToolTipText("Step forward");
		
		return button;
	}*/
		
	public void addTo(ToolBar toolBar) {
		
		for (int b = 0; b < buttons.size(); b++)
			toolBar.add(buttons.get(b));
	}
}
