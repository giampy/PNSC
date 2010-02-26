package gui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import loadstore.Properties;


public class CheckPropertiesMenu extends JMenu implements ItemListener, Closeable {

	private MainPane mainPane;
	
	private JMenuItem fixInitialMarkingItem = fixInitialMarkingItem(); 
	
	public CheckPropertiesMenu(MainPane mainPane) {
		
		super("Check properties");
		
		this.mainPane = mainPane;
		addItemListener(this);
		
		add(fixInitialMarkingItem);
		addSeparator();
		add(showEnabledTransitionsItem());
		addSeparator();
		add(generalProperties());
//		add(checkSimpleItem(mainPane));
//		add(checkReducedItem(mainPane));
//		add(checkContactsItem(mainPane));
		addSeparator();
		add(checkPotentialItem());
		add(checkActiveItem());
	}
	
	public void close(JMenu menu) {
		
		setPopupMenuVisible(false);
		menu.setPopupMenuVisible(false);
		setSelected(false);
	}
	
	public void itemStateChanged(ItemEvent ie) {
		
		removeAll();
		
		fixInitialMarkingItem.setEnabled(ie.getStateChange() == ItemEvent.DESELECTED || (mainPane != null && mainPane.getNet() != null));	
		add(fixInitialMarkingItem);
		addSeparator();
		add(showEnabledTransitionsItem());
		addSeparator();
		add(generalProperties());
//		add(checkSimpleItem(mainPane));
//		add(checkReducedItem(mainPane));
//		add(checkContactsItem(mainPane));
		addSeparator();
		add(checkPotentialItem());
		add(checkActiveItem());
	}

	private JMenuItem fixInitialMarkingItem() {
		
		JMenuItem item = new JMenuItem("Set this as initial marking");
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

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
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		//item.setEnabled(mainPane.getNet() != null);
		
		return item;
	}

	private JCheckBoxMenuItem showEnabledTransitionsItem() {
		
		final JCheckBoxMenuItem item = new JCheckBoxMenuItem("Show enabled transitions", Properties.isShowEnabledTransitionsOn());
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Properties.setShowEnabledTransitions(item.isSelected());
				mainPane.repaint();
			}
		});
		
		return item;
	}
	
	private CheckableSubMenu generalProperties() {
		
		CheckableSubMenu item = new CheckableSubMenu("General properties", this);
		
		item.add(checkSimpleItem());
		item.add(checkReducedItem());
		item.add(checkContactsItem());
		
		return item;
	}

	private JCheckBoxMenuItem checkSimpleItem() {
		
		final JCheckBoxMenuItem item = new JCheckBoxMenuItem("Check simplicity", Properties.isCheckSimpleOn());
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Properties.setCheckSimple(item.isSelected());
				mainPane.updateNodesProperties();
				mainPane.repaint();
			}
		});
		
		return item;
	}

	private JCheckBoxMenuItem checkReducedItem() {
		
		final JCheckBoxMenuItem item = new JCheckBoxMenuItem("Check reducedness", Properties.isCheckReducedOn());
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Properties.setCheckReduced(item.isSelected());
				mainPane.updateNodesProperties();
				mainPane.repaint();
			}
		});
		
		if (mainPane.getNet() == null || mainPane.getNet().getInitialMarking().size() == 0) {
			
			item.setEnabled(false);
			item.setToolTipText("Need to set the initial marking first");
		}
		
		return item;
	}

	private JCheckBoxMenuItem checkContactsItem() {
		
		final JCheckBoxMenuItem item = new JCheckBoxMenuItem("Check contacts", Properties.isCheckContactsOn());
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Properties.setCheckContacts(item.isSelected());
				mainPane.updateNodesProperties();
				mainPane.repaint();
			}
		});
		
		if (mainPane.getNet() == null || mainPane.getNet().getInitialMarking().size() == 0) {
			
			item.setEnabled(false);
			item.setToolTipText("Need to set the initial marking first");
		}
		
		return item;
	}

	private CheckableSubMenu checkPotentialItem() {
		
		CheckableSubMenu item = new CheckableSubMenu("Check potential places", this);
		
		final JCheckBoxMenuItem potCausalItem = new JCheckBoxMenuItem("Potentially causal", Properties.isCheckPotentialCausalOn());
		potCausalItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Properties.setCheckPotentialCausal(potCausalItem.isSelected());
				mainPane.updateNodesProperties();
				mainPane.repaint();
			}
		});
		item.add(potCausalItem);
		
		final JCheckBoxMenuItem potConflictItem = new JCheckBoxMenuItem("Potentially conflict", Properties.isCheckPotentialConflictOn());
		potConflictItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Properties.setCheckPotentialConflict(potConflictItem.isSelected());
				mainPane.updateNodesProperties();
				mainPane.repaint();
			}
		});
		item.add(potConflictItem);
				
		return item;
	}

	private CheckableSubMenu checkActiveItem() {
		
		CheckableSubMenu item = new CheckableSubMenu("Check active places", this);
		
		final JCheckBoxMenuItem actCausalItem = new JCheckBoxMenuItem("Active causal", Properties.isCheckActiveCausalOn());
		actCausalItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Properties.setCheckActiveCausal(actCausalItem.isSelected());
				mainPane.updateNodesProperties();
				mainPane.repaint();
			}
		});
		item.add(actCausalItem);
		
		final JCheckBoxMenuItem actConflictItem = new JCheckBoxMenuItem("Active conflict", Properties.isCheckActiveConflictOn());
		actConflictItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Properties.setCheckActiveConflict(actConflictItem.isSelected());
				mainPane.updateNodesProperties();
				mainPane.repaint();
			}
		});
		item.add(actConflictItem);
		
		if (mainPane.getNet() == null || mainPane.getNet().getInitialMarking().size() == 0) {
			
			actCausalItem.setEnabled(false);
			actConflictItem.setEnabled(false);
			actCausalItem.setToolTipText("Need to set the initial marking first");
			actConflictItem.setToolTipText("Need to set the initial marking first");
		}
		
		return item;
	}
}
