package gui;




import java.awt.Component;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import structure.Element;
import structure.Net;
import xml.Settings;
import xml.XMLDocument;





public class MultiNetPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4958303666027725612L;
	private MainPane mainPane;
	private MultiNet multiNet;
	private Vector<NetPanel> nets = new Vector<NetPanel>();
	
	private int selected = 0;
	private Vector<Element> selection;
	
	public MultiNetPane(MainPane mainPane) {
		
		super(new GridLayout(1, 1));
		
		this.mainPane = mainPane;
		multiNet = Settings.isCompareMode()?new ComparePane(mainPane):new MultiTabPane(mainPane);
	}
	
	public void newNet(NetPanel net) {
		
		if (nets.size() == 0) {
			
			nets.add(net);
			selected = 0;
			
			replaceWith(new JScrollPane(net));
		} else if (nets.size() == 1) {

			nets.add(net);
			
			multiNet = Settings.isCompareMode()?new ComparePane(mainPane, nets):new MultiTabPane(mainPane, nets);
			selected = 1;
			multiNet.setSelected(selected);
			
			replaceWith((Component)multiNet);
		} else {
			
			net.add(new JLabel(String.valueOf(nets.size() + 1)));
			nets.add(net);
			
			multiNet.newNet(net);
			selected = nets.size() - 1;
			multiNet.setSelected(selected);
			replaceWith((Component)multiNet);
		}
		
		mainPane.updateToolbar(new ToolBar(mainPane));
		
	}
		
	public void saveNet(String filePath) {
		
		if (selected > -1 && selected < nets.size()) {
			
			XMLDocument doc = new XMLDocument(nets.get(selected).getNet(), filePath);
			
			doc.save();
			
		} else System.out.println("No net selected");
	}
	
	public void removeNet() {
		
		if (nets.size() > 0) {
			
			if (nets.size() > 2) {
				
				int index = multiNet.removeNet(selected);
				if (index != -1)
					nets.remove(index);
				
				if (selected == nets.size())
					selected--;
				multiNet.setSelected(selected);
				
				replaceWith((Component)multiNet);
			} else if (nets.size() == 2) {
				
				int index = multiNet.removeNet(selected);
				if (index != -1) {
					NetPanel p = nets.get(1 - index );
					nets.remove(index);
					selected = 0;
					replaceWith(new JScrollPane(p));
				}
			} else {
				
				nets.remove(0);
				selected = -1;
				mainPane.updateToolbar(new ToolBar(mainPane));
				replaceWith(new JPanel());
			}
		}
	}
	
	public void removeAllNets() {

		nets = new Vector<NetPanel>();
		replaceWith(new JPanel());
	}
	
	public void requestFocus(NetPanel netPanel) {
		
		for (int n = 0; n < nets.size(); n++)
			if (netPanel != null) {
				
				if (netPanel.equals(nets.get(n))) {
					
					if (n != selected) {
						
						mainPane.updateToolbar(nets.get(n).getToolBar());
						selected = n;
						mainPane.repaint();
					}
				}
			}
	}
	
	public Net getSelectedNet() {
		
		if (selected > -1 && selected < nets.size())
			return nets.get(selected).getNet();
		else return null;
	}
	
	public NetPanel getSelectedNetPanel() {
		
		if (getComponentCount() > 0) {
			
			if (nets.size() != 1) {
				
				if (selected > -1 && selected < nets.size())
					return multiNet.getPanel(selected);
			} else return (NetPanel)((JScrollPane)getComponent(0)).getViewport().getView();
		}
		return null;
	}
	
	public void setSelected(int index) {

		selected = index;
	}
		
	public void setCompareMode(boolean mode) {

		if (mode != Settings.isCompareMode()) {
			
			Settings.setCompareMode(mode);
			if (nets.size() > 1) {

				int formerSelected = selected;
				
				if (mode) {
					
					//((MultiTabPane)multiNet).ignoreStateChanges();
					multiNet = new ComparePane(mainPane, nets);
				} else multiNet = new MultiTabPane(mainPane, nets);
				
				multiNet.setSelected(formerSelected);
				replaceWith((Component)multiNet);

			}
		}
	}
	
	private void replaceWith(Component component) {
		
		removeAll();
		add(component);
		revalidate();
	}
	
	public void selectAll() {

		if (selected > -1)
			nets.get(selected).getNet().selectAll();
	}
	
	public void copySelection() {

		if (selected > -1)
			selection = nets.get(selected).getNet().getSelection();
	}
	
	public void cutSelection() {

		if (selected > -1 && nets.get(selected) != null) {
			selection = nets.get(selected).getNet().getSelection();
			nets.get(selected).getNet().deleteSelection();
		}
	}
	
	public void pasteSelection() {
		
		if (selected > -1 && selection != null)
			nets.get(selected).getNet().paste(selection);
	}

	public void deleteSelection() {
		
		if (selected > -1)
			nets.get(selected).getNet().deleteSelection();
	}

	public void updateNodesProperties() {
		
		if (selected > -1)
			nets.get(selected).getNet().updateNodesProperties();
	}

	public void fixInitialMarking() {
		
		if (selected > -1)
			nets.get(selected).getNet().fixInitialMarking();
	}
	
	public void updateSelectedName() {
		
		if (nets.size() > 1 && selected > -1)
			multiNet.updateName(selected, getSelectedNet().getName());
	}
}
