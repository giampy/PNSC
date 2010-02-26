package algo;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class StateTemplate {

	public String name;
	public Hashtable<TransitionTemplate, StateTemplate> links;
	
	public StateTemplate(String name) {
		
		this.name = name;
		links = new Hashtable<TransitionTemplate, StateTemplate>();
	}
	
	public void linkTo(TransitionTemplate transition, StateTemplate state) {
		
		try {
			
		links.put(transition, state);
		} catch (Exception e) {
			System.out.println (transition + "   " + state);
			e.printStackTrace();
		}
	}
	
	public StateTemplate leadsTo(TransitionTemplate transition) {
		
		if (transition.equals(TransitionTemplate.NULL_TRANSITION))
			return this;
		else return links.get(transition);
	}
	
	public Hashtable<TransitionTemplate, Vector<StateTemplate>> reachLow() {
		
		return reachLow(new Vector<StateTemplate>());
	}
	
	public Hashtable<TransitionTemplate, Vector<StateTemplate>> reachLow(Vector<StateTemplate> path) {
		
		Hashtable<TransitionTemplate, Vector<StateTemplate>> reach = new Hashtable<TransitionTemplate, Vector<StateTemplate>>();
		
		Enumeration<TransitionTemplate> tr = links.keys();
		if (tr.hasMoreElements()) {
			while (tr.hasMoreElements()) {

				TransitionTemplate t = tr.nextElement();
				if (!t.isHigh) {

					if (reach.get(t) == null)
						reach.put(t, new Vector<StateTemplate>());
					reach.get(t).add(this);
				}
			}

			tr = links.keys();
			while (tr.hasMoreElements()) {

				TransitionTemplate t = tr.nextElement();
				if (t.isHigh) {

					StateTemplate newState = leadsTo(t);
					Vector<StateTemplate> newPath = new Vector<StateTemplate>(path);
					if (!path.contains(newState)) {

						newPath.add(newState);
						Hashtable<TransitionTemplate, Vector<StateTemplate>> newReach = newState.reachLow(newPath);
						Enumeration<TransitionTemplate> newTr = newReach.keys();
						while (newTr.hasMoreElements()) {

							TransitionTemplate newT = newTr.nextElement();
							Vector<StateTemplate> newStates = newReach.get(newT);
							for (int s = 0; s < newStates.size(); s++) {

								if (reach.get(newT) == null) 
									reach.put(newT, new Vector<StateTemplate>());

								if (!reach.get(newT).contains(newStates.get(s)))
									reach.get(newT).add(newStates.get(s));
							}
						}
					}
				}
			}
		} else {
			
			if (reach.get(TransitionTemplate.NULL_TRANSITION) == null)
				reach.put(TransitionTemplate.NULL_TRANSITION, new Vector<StateTemplate>());
			reach.get(TransitionTemplate.NULL_TRANSITION).add(this);		
		}
		
		return reach;
	}
	
	public String toString() {
		
		return name;
	}
}
