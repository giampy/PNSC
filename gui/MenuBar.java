package gui;



import javax.swing.JMenuBar;

public class MenuBar extends JMenuBar {

	public MenuBar(MainPane mainPane, Window window) {
		
		super();
				
		add(new FileMenu(mainPane, window));
		add(new EditMenu(mainPane));
		add(new SettingsMenu(mainPane));
		add(new CheckPropertiesMenu(mainPane));
		add(new HelpMenu());
	}
}
