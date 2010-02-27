package animation;




import gui.MainPane;

import java.util.Timer;

import structure.Net;
import xml.Settings;



public class RandomTimer extends Timer {

	public RandomTimer(Net net, MainPane mainPane) {
		
		scheduleAtFixedRate(new RandomTask(net, mainPane), 500, Settings.executionDelay());
	}
}
