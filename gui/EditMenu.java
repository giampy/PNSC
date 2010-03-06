package gui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import algo.NetTemplate;
import animation.NetHistory;

public class EditMenu extends JMenu implements ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2921833601943159682L;

	private final MainPane mainPane;
	
	private JMenuItem undoItem = undoItem();
	private JMenuItem redoItem = redoItem();
	
	private JMenuItem selectAllItem = selectAllItem();
	private JMenuItem cutItem = cutItem();
	private JMenuItem copyItem = copyItem();
	private JMenuItem pasteItem = pasteItem();
	private JMenuItem deleteItem = deleteItem();
	
	private JMenuItem cloneItem = cloneItem();
	
	private Vector<JMenuItem> needSelectionItems;
	
	public EditMenu(MainPane mainPane) {
		
		super("Edit");
		
		this.mainPane = mainPane;
		addItemListener(this);

		add(undoItem);
		add(redoItem);
		
		addSeparator();
		
		add(selectAllItem);
		add(copyItem);
		add(cutItem);
		add(pasteItem);
		add(deleteItem);
		addSeparator();
		add(cloneItem);	
		
		needSelectionItems = new Vector<JMenuItem>();
		needSelectionItems.add(cutItem);
		needSelectionItems.add(copyItem);
		needSelectionItems.add(pasteItem);
		needSelectionItems.add(deleteItem);
	}
	
	public void itemStateChanged(ItemEvent ie) {

		if (ie.getStateChange() == ItemEvent.SELECTED) {
			
			if (mainPane.getNet() == null || mainPane.getNet().getSelection().size() == 0) {
				
				for (int nsi = 0; nsi < needSelectionItems.size(); nsi++) {
					
					needSelectionItems.get(nsi).setEnabled(false);
					needSelectionItems.get(nsi).setToolTipText("No component selected");
				}
			}
			
			if (mainPane.getNet() == null) {
				
				undoItem.setEnabled(false);
				redoItem.setEnabled(false);
				selectAllItem.setEnabled(false);
			} else {
				
				NetHistory nh = mainPane.getNet().getNetHistory();
				undoItem.setEnabled(nh.getSelectedIndex() > 0);
				redoItem.setEnabled(nh.getSelectedIndex() < nh.size() - 1);
			}
			
			
		} else {
			
			for (int nsi = 0; nsi < needSelectionItems.size(); nsi++) {
				
				needSelectionItems.get(nsi).setEnabled(true);
				needSelectionItems.get(nsi).setToolTipText("");
			}
			undoItem.setEnabled(true);
			redoItem.setEnabled(true);
			selectAllItem.setEnabled(true);
		}
		
		if (mainPane.getNet() == null) {
			
			cloneItem.setEnabled(false);
			cloneItem.setToolTipText("No net viewed");
		} else {
			
			cloneItem.setEnabled(true);
			cloneItem.setToolTipText(null);
		}
	}

	private JMenuItem undoItem() {
		
		JMenuItem item = new JMenuItem("Undo");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {

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
		
		return item;
	}
	
	private JMenuItem redoItem() {
		
		JMenuItem item = new JMenuItem("Redo");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK));
		item.addActionListener(new ActionListener() {

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
		
		return item;
	}
	
	private JMenuItem selectAllItem() {
		
		JMenuItem item = new JMenuItem("Select all");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.selectAll();
				mainPane.repaint();
			}
		});
		
		return item;
	}
	
	private JMenuItem copyItem() {
		
		JMenuItem item = new JMenuItem("Copy");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.copySelection();
				mainPane.repaint();
			}
		});
		
		return item;
	}
	
	private JMenuItem cutItem() {
		
		JMenuItem item = new JMenuItem("Cut");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.cutSelection();
				mainPane.repaint();
			}
		});
		
		return item;
	}
	
	private JMenuItem pasteItem() {
		
		JMenuItem item = new JMenuItem("Paste");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.pasteSelection();
				mainPane.repaint();
			}
		});
		
		return item;
	}

	private JMenuItem deleteItem() {
		
		JMenuItem item = new JMenuItem("Delete selected");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.deleteSelection();
				mainPane.repaint();
			}
		});
		
		return item;
	}

	private JMenuItem cloneItem() {
		
		JMenuItem item = new JMenuItem("Clone");
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.cloneNet();
			}
		});
		
		return item;
	}
}
