package gui;



import java.util.Vector;

import javax.swing.JScrollPane;

public class MultiTabPane extends TabbedPane implements MultiNet {
	
	public MultiTabPane(final MainPane mainPane) {
		
		super(mainPane);
	}
	
	public MultiTabPane(MainPane mainPane, Vector<NetPanel> nets) {
		
		super(mainPane);
		
		addChangeListener(this);
		
		if (nets != null) 
			for (int n = 0; n < nets.size(); n++)
				newNet(nets.get(n));
	}
	
	public boolean is(boolean kind) {
		
		return kind == MultiNet.MULTITAB_PANE;
	}

	public void newNet(NetPanel net) {
		
		int index = getSelectedIndex();
		addTab(net.getNet().getName(), new JScrollPane(net));
		setSelectedIndex(index != -1?index:0);
	}

	public int removeNet(int index) {
		
		remove(index);
		return index;
	}
	
	public void setSelected(int index) {
		
		setSelectedIndex(index);
	}

	public NetPanel getPanel(int selected) {
		
		if (getSelectedComponent() != null)
			return (NetPanel)((JScrollPane)getSelectedComponent()).getViewport().getView();
		else return null;
	}
	
	public void updateName(int selected, String name) {
		
		setTitleAt(selected, name);
	}
}
