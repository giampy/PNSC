package gui;




import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import structure.Net;
import xml.Settings;
import xml.XMLDocument;



public class InputDialog {

	public static void open(MainPane mainPane) {
		
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("", "pnml");
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(new File(Settings.startingDir()));
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	
        	try {
        		
        		XMLDocument doc = new XMLDocument(chooser.getSelectedFile().getAbsolutePath());
        		Net net = doc.extractNet();
        		if (net != null) {
        			
        			net.getNetHistory().erase();
        			net.getNetHistory().addNet(net);
        			mainPane.openNet(net);
        		} else ErrorDialog.show("Unable to open the net");
        		
        		Settings.setStartingDir(chooser.getSelectedFile().getPath());
        	} catch (Exception exc) {
        		
        		ErrorDialog.show(exc);
        	}
        }		
	}
	
	public static void save(MainPane mainPane) {
		
		if (mainPane.getNet() != null) { 
			
			if (mainPane.getNet().getPathName() == null) {

				JFileChooser chooser = new JFileChooser();
				//FileNameExtensionFilter filter = new FileNameExtensionFilter("", "pnml");
				//chooser.setFileFilter(filter);
				chooser.setCurrentDirectory(new File(Settings.startingDir()));
				int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					String name = chooser.getSelectedFile().getAbsolutePath();
					String extension = name.lastIndexOf(".") != -1? name.substring(name.lastIndexOf(".")) : "";
					if (!extension.equals(".pnml"))
						name += ".pnml";
					mainPane.saveNet(name);
					
					mainPane.getNet().setPathAndName(name);
					mainPane.updateName();
				}
			} else {

				mainPane.saveNet(mainPane.getNet().getPathName());
				JOptionPane.showMessageDialog(null, mainPane.getNet().getName() + " saved", "Saved", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	public static void saveAs(MainPane mainPane) {
		
		if (mainPane.getNet() != null) { 

			JFileChooser chooser = new JFileChooser();
			//FileNameExtensionFilter filter = new FileNameExtensionFilter("", "pnml");
			//chooser.setFileFilter(filter);
			chooser.setCurrentDirectory(new File(Settings.startingDir()));
			if (mainPane.getNet().getPathName() != null) 
				chooser.setCurrentDirectory(new File(mainPane.getNet().getPathName()));
			int returnVal = chooser.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				String name = chooser.getSelectedFile().getAbsolutePath();
				String extension = name.lastIndexOf(".") != -1? name.substring(name.lastIndexOf(".")) : "";
				if (!extension.equals(".pnml"))
					name += ".pnml";
				mainPane.saveNet(name);

				mainPane.getNet().setPathAndName(name);
				mainPane.updateName();
			}
		}				
	}
}
