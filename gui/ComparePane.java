package gui;



import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class ComparePane extends JPanel implements MultiNet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7853680087085501610L;
	private MainPane mainPane;
	
	public ComparePane(MainPane mainPane) {
		
		super(new GridLayout(1, 0));
		
		this.mainPane = mainPane;
	}
	
	public ComparePane(MainPane mainPane, Vector<NetPanel> nets) {
		
		super(new GridLayout(1, 0));

		this.mainPane = mainPane;
		
		if (nets != null) 
			for (int n = 0; n < nets.size(); n++) 
				newNet(nets.get(n));
	}
	
	public boolean is(boolean kind) {
		
		return kind == MultiNet.COMPARE_PANE;
	}

	public void newNet(NetPanel net) {

		final JTabbedPane tp = new JTabbedPane();
		tp.addTab(net.getNet().getName(), new JScrollPane(net));
		tp.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {

				if (tp.getComponentCount() > 0)
					mainPane.requestFocus((NetPanel)((JScrollPane)tp.getComponent(0)).getViewport().getView());
			}

			public void focusLost(FocusEvent e) { }
		});
		add(tp);
	}

	public int removeNet(int index) {  

		if (index < getComponentCount()) {

			remove(index);
			return index;
		} else return -1;
	}

	public void setSelected(int index) { }
	
	public NetPanel getPanel(int selected) {
		
		if (getComponentCount() > selected)
			return (NetPanel)((JScrollPane)((JTabbedPane)(getComponent(selected))).getComponent(0)).getViewport().getView();
		else return null;
	}
	
	public void updateName(int selected, String name) {
		
		JTabbedPane tp = (JTabbedPane)getComponent(selected);
		tp.setTitleAt(0, name);
	}
}
