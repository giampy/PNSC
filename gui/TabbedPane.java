package gui;



import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TabbedPane extends JTabbedPane implements ChangeListener {

	private MainPane mainPane;
	private boolean ignoreStateChanges = false;
	
	public TabbedPane(final MainPane mainPane) {
		
		this.mainPane = mainPane;
		
		addChangeListener(this);
	}
	
/*	public void ignoreStateChanges() {
		
		ignoreStateChanges = true;
	}
*/	
	public void stateChanged(ChangeEvent e) {

		//if (!ignoreStateChanges) {
			
			
			NetPanel np = ((NetPanel)((JScrollPane)((MultiTabPane)e.getSource()).getSelectedComponent()).getViewport().getView());
			mainPane.requestFocus(np);
			mainPane.updateToolbar(np.getToolBar());
		//}
	}

}
