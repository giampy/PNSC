package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import structure.Place;
import structure.PositivePBNIResult;
import structure.Transition;

import algo.Check;

import xml.Properties;



public class PropertyCheckForRealTimeBar extends EditingModeBar{
	private JToggleButton a, b, c;

	public PropertyCheckForRealTimeBar(MainPane mainPane){
		if(Properties.isCheckRealTimeOn()){
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
		button.setPreferredSize(new Dimension(60, button.getSize().height));
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
					else if(item.getText().equals("PBNI+")){
						//*****
						boolean value;
						PositivePBNIResult res=new PositivePBNIResult();
						value=Check.PositivePBNI(mainPane.getNet(), res);
						mainPane.getNet().showIfAnyPotentialCausal(res.getPotentialCausal());
						mainPane.getNet().showIfAnyPotentialConflict(res.getPotentialConflict());
						mainPane.getNet().showIfAnyActiveCausal(res.getActiveCausal());
						mainPane.getNet().showIfAnyActiveConflict(res.getActiveConflict());
						mainPane.repaint();
						//*****
						JOptionPane.showMessageDialog(mainPane, "Net is " + (value ? "" : "not ") + "PBNI+",
								"PBNI+ Property",JOptionPane.INFORMATION_MESSAGE);
						}
				}
			}
			
		});
		return button;
	}

	private JToggleButton JToggleButtonItem(final MainPane mainPane, String str) {
		// TODO Auto-generated method stub
		JToggleButton button = new JToggleButton(str);
		button.setPreferredSize(new Dimension(60, button.getSize().height));
		if(mainPane.getNet()==null)
			button.setEnabled(false);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mainPane.getToolbar().updateLabel();
			}
		});
		button.setToolTipText("Real Time "+str+" Check");
		if(str.equals("PBNI+") && mainPane.getNet()!=null)
			button.setSelected(true);
		return button;
	}
}
