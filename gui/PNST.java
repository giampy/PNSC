package gui;



//import java.awt.Point;
import java.util.Locale;

import algo.Case;
//import java.util.TreeMap;
//import java.util.Vector;

//import structure.Place;

//import algo.Case;

import xml.Properties;
import xml.Settings;




public class PNST {

	public static void main(String[] args) {

		try {
			
			Locale.setDefault(Locale.ENGLISH);
			
			Settings.retrieve();
			Properties.retrieve();
		//	@SuppressWarnings("unused")
			Window window = new Window("Petri Net Security Checker");
				
		} catch (Exception e) {
			
			ErrorDialog.show(e);
			System.out.println(e);
		}
	}

}
