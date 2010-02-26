package animation;




import gui.MainPane;

import java.util.TimerTask;
import java.util.Vector;

import structure.Net;
import structure.Transition;


public class RandomTask extends TimerTask {
	
	private Net net;
	private MainPane mainPane;
	
	public RandomTask(Net net, MainPane mainPane) {
		
		this.net = net;
		this.mainPane = mainPane;
	}

	public void run() {
		
		Vector<Transition> enabled = net.getEnabledTransitions();
		
		if (enabled.size() > 0) {

			int choice = (int)(Math.random() * enabled.size());
			net.fire(enabled.get(choice));
			mainPane.repaint();
		}
	}
}
