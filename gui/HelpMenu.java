package gui;




import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import structure.Net;
import xml.Settings;
import xml.XMLDocument;



public class HelpMenu extends JMenu {

	public HelpMenu() {
		
		super("Help");
		
		JMenuItem bisim = new JMenuItem("Check bisimilar");
		bisim.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				
				Net net1 = null;
				Net net2 = null;
				
				JFileChooser chooser = new JFileChooser();
		        //FileNameExtensionFilter filter = new FileNameExtensionFilter("", "pnml");
		        //chooser.setFileFilter(filter);
		        chooser.setCurrentDirectory(new File(Settings.startingDir()));
		        int returnVal = chooser.showOpenDialog(null);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	
		        	try {
		        		
		        		XMLDocument doc = new XMLDocument(chooser.getSelectedFile().getAbsolutePath());
		        		net1 = doc.extractNet();
		        		if (net1 == null)
		        			ErrorDialog.show("Unable to open the net");
		        		
		        		Settings.setStartingDir(chooser.getSelectedFile().getPath());
		        	} catch (Exception exc) {
		        		
		        		ErrorDialog.show(exc);
		        	}
		        }		

		        chooser.setCurrentDirectory(new File(Settings.startingDir()));
		        returnVal = chooser.showOpenDialog(null);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	
		        	try {
		        		
		        		XMLDocument doc = new XMLDocument(chooser.getSelectedFile().getAbsolutePath());
		        		net2 = doc.extractNet();
		        		if (net2 == null)
		        			ErrorDialog.show("Unable to open the net");
		        		
		        		Settings.setStartingDir(chooser.getSelectedFile().getPath());
		        	} catch (Exception exc) {
		        		
		        		ErrorDialog.show(exc);
		        	}
		        }		

		        net1.isBisimilar(net2);
			}
		});
		
		add(bisim);
	}
}
