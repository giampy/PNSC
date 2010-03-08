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

import structure.PositivePBNIResult;

import algo.Check;

import xml.Properties;



public class CheckPropertiesMenu extends JMenu implements ItemListener, Closeable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4722339566738169507L;

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

		addSeparator();
		if(!Properties.isCheckRealTimeOn()){
			add(checkPotentialItem());
			add(checkActiveItem());
		}
		else{
			add(checkPotentialItemRealTime());
			add(checkActiveItemRealTime());
		}
		addSeparator();
		add(checkSBNDCItem());
		add(checkBSNNIItem());
		add(checkPositivePBNIItem());
		
	}
	
	public void close(JMenu menu) {
		
		setPopupMenuVisible(false);
		menu.setPopupMenuVisible(false);
		setSelected(false);
	}
	
	public void itemStateChanged(ItemEvent ie) {
		
		removeAll();
		
		fixInitialMarkingItem.setEnabled(ie.getStateChange() == ItemEvent.DESELECTED || 
				(mainPane != null && mainPane.getNet() != null));	
		add(fixInitialMarkingItem);
		addSeparator();
		add(showEnabledTransitionsItem());
		addSeparator();
		add(generalProperties());
		addSeparator();
		if(!Properties.isCheckRealTimeOn()){
			Properties.setCheckRealTime(true);
			add(checkPotentialItem());
			add(checkActiveItem());
		}
		else{
			Properties.setCheckRealTime(false);
			add(checkActiveItemRealTime());
			add(checkPotentialItemRealTime());
			}
		addSeparator();
		add(checkSBNDCItem());
		add(checkBSNNIItem());
		add(checkPositivePBNIItem());
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
	private JMenuItem checkPotentialItem() {
		
		JMenu potential = new  JMenu("Potential Places");
		JMenuItem causal = new JMenuItem("Causal Places");
		JMenuItem conflict = new JMenuItem("Conflict Places");
		if(mainPane.getNet() == null){
			causal.setEnabled(false);
			conflict.setEnabled(false);
		}
		causal.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
					mainPane.getNet().showIfAnyPotentialCausal();
					mainPane.repaint();
			}
		});
		conflict.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
					mainPane.getNet().showIfAnyPotentialConflict();
					mainPane.repaint();
			}
		});
		potential.add(causal);
		potential.add(conflict);
		return	potential;
	}
	
	private JMenuItem checkActiveItem() {
		
		JMenu potential = new  JMenu("Active Places");
		JMenuItem causal = new JMenuItem("Causal Places");
		JMenuItem conflict = new JMenuItem("Conflict Places");
		if(mainPane.getNet() == null){
			causal.setEnabled(false);
			conflict.setEnabled(false);
		}
		causal.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
					mainPane.getNet().showIfAnyActiveCausal();
					mainPane.repaint();
			}
		});
		conflict.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
					mainPane.getNet().showIfAnyActiveConflict();
					mainPane.repaint();
			}
		});
		potential.add(causal);
		potential.add(conflict);
		return	potential;
	}
	private CheckableSubMenu checkActiveItemRealTime() {
		
		CheckableSubMenu item = new CheckableSubMenu("Check active places", this);
		
		final JCheckBoxMenuItem actCausalItem = new JCheckBoxMenuItem("Active causal", Properties.isCheckActiveCausalRealTimeOn());
		actCausalItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Properties.setCheckActiveCausalRealTime(actCausalItem.isSelected());
				mainPane.updateNodesProperties();
				mainPane.repaint();
			}
		});
		item.add(actCausalItem);
		
		final JCheckBoxMenuItem actConflictItem = new JCheckBoxMenuItem("Active conflict", Properties.isCheckActiveConflictRealTimeOn());
		actConflictItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Properties.setCheckActiveConflictRealTime(actConflictItem.isSelected());
				mainPane.updateNodesProperties();
				mainPane.repaint();
			}
		});
		item.add(actConflictItem);
		
		return item;
	}
	private CheckableSubMenu checkPotentialItemRealTime() {
		
		CheckableSubMenu item = new CheckableSubMenu("Check potential places", this);
		
		final JCheckBoxMenuItem potCausalItem = new JCheckBoxMenuItem("Potentially causal", Properties.isCheckPotentialCausalRealTimeOn());
		potCausalItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Properties.setCheckPotentialCausalRealTime(potCausalItem.isSelected());
				mainPane.updateNodesProperties();
				mainPane.repaint();
			}
		});
		item.add(potCausalItem);
		
		final JCheckBoxMenuItem potConflictItem = new JCheckBoxMenuItem("Potentially conflict", Properties.isCheckPotentialConflictRealTimeOn());
		potConflictItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Properties.setCheckPotentialConflictRealTime(potConflictItem.isSelected());
				mainPane.updateNodesProperties();
				mainPane.repaint();
			}
		});
		item.add(potConflictItem);
				
		return item;
	}

	private JMenuItem checkSBNDCItem() {
		
		JMenuItem item = new JMenuItem("Check SBNDC");
		if(mainPane.getNet() == null)
			item.setEnabled(false);
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(mainPane, "Net is " + (Check.SBNDC(mainPane.getNet())? "" : "not ") + "SBNDC",
					"SBNDC Property",JOptionPane.INFORMATION_MESSAGE);
			
			}
		});
		
		return item;
	}
	
	private JMenuItem checkBSNNIItem() {
		
		JMenuItem item = new JMenuItem("Check BSNNI");
		if(mainPane.getNet() == null)
			item.setEnabled(false);
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Check.BSNNI(mainPane.getNet());
				JOptionPane.showMessageDialog(mainPane, "Net is " + (Check.BSNNI(mainPane.getNet())? "" : "not ") + "BSNNI",
						"BSNNI Property",JOptionPane.INFORMATION_MESSAGE);
				
			}				
		});
		
		return item;
	}
	private JMenuItem checkPositivePBNIItem() {
		
		JMenuItem item = new JMenuItem("Check PBNI+");
		if(mainPane.getNet() == null)
			item.setEnabled(false);
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PositivePBNIResult res=new PositivePBNIResult();
				boolean	value=Check.PositivePBNI(mainPane.getNet(), res);
				mainPane.getNet().showIfAnyPotentialCausal(res.getPotentialCausal());
				mainPane.getNet().showIfAnyPotentialConflict(res.getPotentialConflict());
				mainPane.getNet().showIfAnyActiveCausal(res.getActiveCausal());
				mainPane.getNet().showIfAnyActiveConflict(res.getActiveConflict());
				mainPane.repaint();
			JOptionPane.showMessageDialog(mainPane, "Net is " + (value ? "" : "not ") + "PBNI+",
					"PBNI+ Property",JOptionPane.INFORMATION_MESSAGE);
			
			}
		});
		
		return item;
	}
	
}
