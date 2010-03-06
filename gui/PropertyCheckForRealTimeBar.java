package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import algo.Check;

import xml.Properties;



public class PropertyCheckForRealTimeBar extends EditingModeBar{
	JToggleButton a, b, c;
	public PropertyCheckForRealTimeBar(MainPane mainPane){
		if(Properties.isCheckActiveCausalRealTimeOn()){
			add(a=JToggleButtonItem(mainPane, "BSNNI"));
			add(b=JToggleButtonItem(mainPane, "SBNDC"));
			add(c=JToggleButtonItem(mainPane, "PBNI+"));
		}
		else{
			add(JButtonItem(mainPane, "BSNNI"));
			add(JButtonItem(mainPane, "SBNDC"));
			add(JButtonItem(mainPane, "PBNI+"));
		}
	}

	public String whatIsActive(){
		if(a.isSelected())
			return a.getText();
		else if(b.isSelected())
			return b.getText();
		else
			return c.getText();
	}
	private JButton JButtonItem(final MainPane mainPane, String str) {
		// TODO Auto-generated method stub
		JButton button=new JButton(str);
		if(mainPane.getNet()==null)
			button.setEnabled(false);
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JButton	item;
				if(mainPane.getNet()!=null){
					item=(JButton)e.getSource();
					if(item.getText().equals("BSNNI"))
						JOptionPane.showMessageDialog(mainPane, "Net is " + (Check.BSNNI(mainPane.getNet())? "" : "not ") + "BSNNI",
								"BSNNI Property",JOptionPane.INFORMATION_MESSAGE);
					else if(item.getText().equals("SBNDC"))
						JOptionPane.showMessageDialog(mainPane, "Net is " + (Check.SBNDC(mainPane.getNet())? "" : "not ") + "SBNDC",
								"SBNDC Property",JOptionPane.INFORMATION_MESSAGE);
					else if(item.getText().equals("PBNI+"))
						JOptionPane.showMessageDialog(mainPane, "Net is " + (Check.PositivePBNI(mainPane.getNet())? "" : "not ") + "PBNI+",
								"PBNI+ Property",JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
		});
		return button;
	}

	private JToggleButton JToggleButtonItem(final MainPane mainPane, String str) {
		// TODO Auto-generated method stub
		JToggleButton button = new JToggleButton(str);
		if(mainPane.getNet()==null)
			button.setEnabled(false);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mainPane.getToolbar().updateLabel();
			}
		});
		button.setToolTipText("Real Time "+str+" Check");
		
		return button;
	}
}
