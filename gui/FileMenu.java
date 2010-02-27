package gui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import xml.Properties;
import xml.Settings;
//import javax.swing.filechooser.FileNameExtensionFilter;



public class FileMenu extends JMenu implements ItemListener {

	private final MainPane mainPane;
	private final Window window;

	private JMenuItem saveItem = saveItem();
	private JMenuItem saveAsItem = saveAsItem();
	private JMenuItem closeItem = closeItem();
	private JMenuItem closeAllItem = closeAllItem();
	
	private Vector<JMenuItem> needOpenNet;
	
	public FileMenu(MainPane mainPane, Window window) {
		
		super("File");
		
		this.mainPane = mainPane;
		this.window = window;
		addItemListener(this);
		
		add(newItem());
		add(openItem());
		add(saveItem);
		add(saveAsItem);
		//add(saveAllItem());
		add(closeItem);
		add(closeAllItem);
		addSeparator();
		add(exitItem());
		
		needOpenNet = new Vector<JMenuItem>();
		needOpenNet.add(saveItem);
		needOpenNet.add(saveAsItem);
		needOpenNet.add(closeItem);
		needOpenNet.add(closeAllItem);
	}
	
	public void itemStateChanged(ItemEvent ie) {
	
		if (ie.getStateChange() == ie.SELECTED) {
			
			if (mainPane.getNet() == null)
				for (int non = 0; non < needOpenNet.size(); non++)
					needOpenNet.get(non).setEnabled(false);
		} else for (int non = 0; non < needOpenNet.size(); non++)
			needOpenNet.get(non).setEnabled(true);
	}
	
	private JMenuItem newItem() {
		
		JMenuItem item = new JMenuItem("New");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.newNet();
			}
		});
		
		return item;
	}
	
	private JMenuItem openItem() {
		
		JMenuItem item = new JMenuItem("Open");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				InputDialog.open(mainPane);
			}
		});
		
		return item;
	}
	
	private JMenuItem saveItem() {
		
		JMenuItem item = new JMenuItem("Save");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				InputDialog.save(mainPane);
			}
		});
		
		return item;
	}
	
	private JMenuItem saveAsItem() {
		
		JMenuItem item = new JMenuItem("Save as");
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				InputDialog.saveAs(mainPane);
			}
		});
		
		return item;
	}
	
	private JMenuItem saveAllItem() {
		
		JMenuItem item = new JMenuItem("Save all");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK));
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				
			}
		});
		
		return item;
	}
	
	private JMenuItem closeItem() {
		
		JMenuItem item = new JMenuItem("Close");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.removeNet();
			}
		});
		
		return item;
	}
	
	private JMenuItem closeAllItem() {
		
		JMenuItem item = new JMenuItem("Close All");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK));
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainPane.removeAllNets();
			}
		});
		
		return item;
	}
	
	private JMenuItem exitItem() {
		
		JMenuItem item = new JMenuItem("Exit");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Settings.store();
				Properties.store();
				window.windowClosing(new WindowEvent(window, 0));
			}
		});
		
		return item;
	}
}
