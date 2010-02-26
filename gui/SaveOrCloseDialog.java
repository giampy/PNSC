package gui;


import javax.swing.JOptionPane;

public class SaveOrCloseDialog {
	
	public static int SAVE = 0;
	public static int CLOSE = 1;
	public static int CANCEL = 2;

	public static int show(String str) {
		
		String[] options = new String[3];
		options[SAVE] = "Save";
		options[CLOSE] = "Close";
		options[CANCEL] = "Cancel";
		
		return JOptionPane.showOptionDialog(null, str, "Unsaved file", JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.WARNING_MESSAGE, null, options, null);
	}
}
