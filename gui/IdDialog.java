package gui;



import javax.swing.JOptionPane;

import structure.Net;


public class IdDialog extends JOptionPane {

	public static String open(Net net, String message, String initialValue) {
		
		String result = null;
		
		while (result == null) {
			
			result = showInputDialog(message, initialValue);
			
			if (result != null) {
				
				if (!result.equals("")) {
						
					if (net.acceptsNewId(result, initialValue))
						return result;
					else result = null;
				} else {
					
					result = null;
					showMessageDialog(null, "Not a valid name", "Invalid name", JOptionPane.ERROR_MESSAGE);;					
				}
			} else result = "";
		}
		
		return initialValue;
	}
}
