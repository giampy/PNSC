package gui;



import java.util.Locale;

import algo.Check;
import algo.CaseTemplate;
import algo.TransitionTemplate;

import xml.Properties;
import xml.Settings;



public class PNST {

	public static void main(String[] args) {

		try {
			
			Locale.setDefault(Locale.ENGLISH);
			
			Settings.retrieve();
			Properties.retrieve();
			Window window = new Window("Petri Net Security Checker");
		
		} catch (Exception e) {
			
			ErrorDialog.show(e);
			System.out.println(e);
		}
	}

}
