package gui;



import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import xml.Properties;
import xml.Settings;



public class Window extends JFrame implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5475318375050607243L;
	private MenuBar menuBar;
	private MainPane mainPane;
	
	public Window(String name) {
		
        super(name);
        addWindowListener(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(properSize());

        // Set the main pane
        mainPane = new MainPane();        
        setContentPane(mainPane);
        
        // Set the menu bar
        menuBar = new MenuBar(mainPane, this);
        setJMenuBar(menuBar);
        
        setVisible(true);
	}
	
	private Dimension properSize() {
		
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        size.height -= 29;
        
        return new Dimension(1024, 768);//size;
	}

	// WINDOW LISTENER METHODS
	
	public void windowClosing(WindowEvent e) { 

		boolean abort = false;
		while (!abort && mainPane.getNet() != null) {
			
			int res = SaveOrCloseDialog.CLOSE;

			if (mainPane.getNet().getNetHistory().hasToBeSaved()) {
				
				res = SaveOrCloseDialog.show("Save " + mainPane.getNet().getName() + "?");

				if (res == SaveOrCloseDialog.SAVE) 
					InputDialog.save(mainPane);
			}
			
			if (res != SaveOrCloseDialog.CANCEL)
				mainPane.removeNetWithoutAsking();			
			else abort = true;
		}
		
		if (!abort) {
			
			Settings.store();
			Properties.store();
		
			dispose();
		}
	}
	
	public void windowClosed(WindowEvent e) { }
	public void windowActivated(WindowEvent e) { }
	public void windowDeactivated(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowOpened(WindowEvent e) { }
}
