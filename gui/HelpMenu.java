package gui;




import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;




public class HelpMenu extends JMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3375921014569944071L;

	public HelpMenu() {
		
		super("Help");
		
		JMenuItem bisim = new JMenuItem("Check bisimilar");
		bisim.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				
				/*Net net1 = null;
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
*/
		      JOptionPane.showMessageDialog(null, "Not implemented Yet");
		      
			}
		});
		
		add(bisim);
	}
}
