package gui;


import java.awt.Component;

import javax.swing.JOptionPane;

public class ErrorDialog extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8762587002594153264L;

	public static void show(Exception e) {
		
		JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void show(String str) {
		
		JOptionPane.showMessageDialog(null, str, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
