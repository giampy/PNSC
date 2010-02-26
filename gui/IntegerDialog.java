package gui;


import javax.swing.JOptionPane;

public class IntegerDialog extends JOptionPane {

	public static int open(String message, int initialValue) {
		
		String result = null;
		
		while (result == null) {
			
			result = showInputDialog(message, Integer.toString(initialValue));
			
			if (result != null) {
				
				if (!result.equals("")) {
					
					try {
					
						return Integer.parseInt(result);
					} catch(Exception e) {
					
						result = null;
						showMessageDialog(null, "Size must be a number", "Invalid size", JOptionPane.ERROR_MESSAGE);
					}	
				} else {
					
					result = null;
					showMessageDialog(null, "Size must be a number", "Invalid size", JOptionPane.ERROR_MESSAGE);;					
				}
			} else result = "";
		}
		
		return initialValue;
	}
}
