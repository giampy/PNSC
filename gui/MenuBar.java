package gui;



import javax.swing.JMenuBar;

public class MenuBar extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5538316666611056315L;

	public MenuBar(MainPane mainPane, Window window) {
		
		super();
				
		add(new FileMenu(mainPane, window));
		add(new EditMenu(mainPane));
		add(new SettingsMenu(mainPane));
		add(new CheckPropertiesMenu(mainPane));
		add(new HelpMenu());
	}
}
