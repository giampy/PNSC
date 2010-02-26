package gui;


import java.awt.Component;

import javax.swing.JOptionPane;

public class ErrorDialog extends Component {

	public static void show(Exception e) {
		
		JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void show(String str) {
		
		JOptionPane.showMessageDialog(null, str, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
