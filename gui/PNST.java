package gui;



import java.awt.Point;
import java.util.Locale;
import java.util.TreeMap;
import java.util.Vector;

import structure.Place;

import algo.Case;

import xml.Properties;
import xml.Settings;




public class PNST {

	public static void main(String[] args) {

		try {
			
			Locale.setDefault(Locale.ENGLISH);
			
			Settings.retrieve();
			Properties.retrieve();
			Window window = new Window("Petri Net Security Checker");
			/*TreeMap<Integer, Case>	tree=new TreeMap<Integer, Case>();
			Case	c1;
			Case	c2;
			Vector<Place>	vecp1=new Vector<Place>();
			Vector<Place>	vecp2=new Vector<Place>();
			
			for(int i=0; i<10; ++i){
				vecp1.add(new Place(new Point(), Math.random()<0.5?1:0, ""+i));
				vecp2.add(new Place(new Point(), Math.random()<0.5?1:0, ""+i));
			}
		
			c1=new Case(vecp1);
			c2=new Case(vecp2);
			System.out.println(c1.toString());
			System.out.println(c2.toString());
			tree.put(new Integer(c1.getIntValue()), c1);
			tree.put(new Integer(c2.getIntValue()), c2);
			
			System.out.println(tree.get(new Integer(c1.getIntValue())).toString());
			System.out.println(tree.get(new Integer(c1.getIntValue())).toString());*/
			
		} catch (Exception e) {
			
			ErrorDialog.show(e);
			System.out.println(e);
		}
	}

}
