package gui;




import javax.swing.BoxLayout;
import javax.swing.JPanel;

import structure.Net;


public class MainPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -718962506484803989L;
	private ToolBar toolBar;
	private MultiNetPane multiNetPane;
	
	private ComposeMode composeMode = ComposeMode.SELECT;
	
	public MainPane() {
		
		super();
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
        // Set the toolbar and add it to the main panel
        toolBar = new ToolBar(this);
        add(toolBar);
        
        // Set the tab pane and add it to the main panel
        multiNetPane = new MultiNetPane(this);
        add(multiNetPane);
        
        // Add first, empty net
        //newNet();
	}
	
	public void newNet() {
		
		NetPanel np = new NetPanel(this);
		
		multiNetPane.newNet(np);
	}
	
	public void openNet(Net net) {
		
		multiNetPane.newNet(new NetPanel(this, net));
	}
	
	public void saveNet(String filePath) {
		
		if (getNet() != null) {
			
			getNet().getNetHistory().save();
			multiNetPane.saveNet(filePath);
		}
	}
	
	public void removeNet() {
		
		if (getNet() != null) {

			int res = SaveOrCloseDialog.CLOSE;

			if (getNet().getNetHistory().hasToBeSaved()) {
				
				res = SaveOrCloseDialog.show("Save " + getNet().getName() + "?");

				if (res == SaveOrCloseDialog.SAVE)
					InputDialog.save(this);
			}
			
			if (res != SaveOrCloseDialog.CANCEL)
				multiNetPane.removeNet();
		}
	}
	
	public void removeNetWithoutAsking() {
		
		if (getNet() != null) 
			multiNetPane.removeNet();
	}
	
	public void removeAllNets() {
		
		boolean abort = false;
		while (!abort && getNet() != null) {

			int res = SaveOrCloseDialog.CLOSE;

			if (getNet().getNetHistory().hasToBeSaved()) {
				
				res = SaveOrCloseDialog.show("Save " + getNet().getName() + "?");

				if (res == SaveOrCloseDialog.SAVE) 
					InputDialog.save(this);
			}
			
			if (res != SaveOrCloseDialog.CANCEL)
				multiNetPane.removeNet();			
			else abort = true;
		}
	}
	
	public void selectAll() {
		
		multiNetPane.selectAll();
	}

	public void copySelection() {
			
		multiNetPane.copySelection();
	}
	
	public void cutSelection() {
		
		multiNetPane.cutSelection();
	}
	
	public void pasteSelection() {
		
		multiNetPane.pasteSelection();
	}
	
	public void deleteSelection() {

		multiNetPane.deleteSelection();
	}
	
	public void cloneNet() {
		
		Net selectedNet = multiNetPane.getSelectedNet();
		if (selectedNet != null) 
			multiNetPane.newNet(new NetPanel(this, new Net(selectedNet)));
	}
	
	public void requestFocus(NetPanel netPanel) {
		
		multiNetPane.requestFocus(netPanel);
	}
	
	public void setSelected(int index) {
		
		multiNetPane.setSelected(index);
	}
	
	public ComposeMode getComposeMode() {
		
		return composeMode;
	}

	public void setComposeMode(ComposeMode composeMode) {
		
		this.composeMode = composeMode;
	}
	
	public void setCompareMode(boolean mode) {

		multiNetPane.setCompareMode(mode);
	}

	public void updateNodesProperties() {
		
		if (getNet() != null)
			multiNetPane.updateNodesProperties();
	}

	public void fixInitialMarking() {
		
		multiNetPane.fixInitialMarking();
	}
	
	public Net getNet() {
		
		if (multiNetPane != null)
			return multiNetPane.getSelectedNet();
		else return null;
	}
	
	public NetPanel getNetPanel() {
		
		return multiNetPane.getSelectedNetPanel();
	}
	
	public ToolBar getToolbar() {
		
		return toolBar;
	}

	public void updateToolbar(ToolBar toolBar) {
		
		remove(this.toolBar);
		this.toolBar = toolBar;
		toolBar.select(composeMode);
		toolBar.createUpdateCheckBar();
		add(toolBar, 0);
		revalidate();
	}
	
	public void updateName() {
		
		multiNetPane.updateSelectedName();
	}
}
