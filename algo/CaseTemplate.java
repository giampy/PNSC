package algo;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class CaseTemplate {

	public String name;
	public Hashtable<TransitionTemplate, CaseTemplate> links;
	
	public CaseTemplate(String name) {
		
		this.name = name;
		links = new Hashtable<TransitionTemplate, CaseTemplate>();
	}
	
	public void linkTo(TransitionTemplate transition, CaseTemplate state) {
		
		try {
			
		links.put(transition, state);
		} catch (Exception e) {
			System.out.println (transition + "   " + state);
			e.printStackTrace();
		}
	}
	
	public CaseTemplate leadsTo(TransitionTemplate transition) {
		
		if (transition.equals(TransitionTemplate.NULL_TRANSITION))
			return this;
		else return links.get(transition);
	}
	
	public Hashtable<TransitionTemplate, Vector<CaseTemplate>> reachLow() {
		
		return reachLow(new Vector<CaseTemplate>());
	}
	
	public Hashtable<TransitionTemplate, Vector<CaseTemplate>> reachLow(Vector<CaseTemplate> path) {
		
		Hashtable<TransitionTemplate, Vector<CaseTemplate>> reach = new Hashtable<TransitionTemplate, Vector<CaseTemplate>>();
		
		Enumeration<TransitionTemplate> tr = links.keys();
		if (tr.hasMoreElements()) {
			while (tr.hasMoreElements()) {

				TransitionTemplate t = tr.nextElement();
				if (!t.isHigh) {

					if (reach.get(t) == null)
						reach.put(t, new Vector<CaseTemplate>());
					reach.get(t).add(this);
				}
			}

			tr = links.keys();
			while (tr.hasMoreElements()) {

				TransitionTemplate t = tr.nextElement();
				if (t.isHigh) {

					CaseTemplate newState = leadsTo(t);
					Vector<CaseTemplate> newPath = new Vector<CaseTemplate>(path);
					if (!path.contains(newState)) {

						newPath.add(newState);
						Hashtable<TransitionTemplate, Vector<CaseTemplate>> newReach = newState.reachLow(newPath);
						Enumeration<TransitionTemplate> newTr = newReach.keys();
						while (newTr.hasMoreElements()) {

							TransitionTemplate newT = newTr.nextElement();
							Vector<CaseTemplate> newStates = newReach.get(newT);
							for (int s = 0; s < newStates.size(); s++) {

								if (reach.get(newT) == null) 
									reach.put(newT, new Vector<CaseTemplate>());

								if (!reach.get(newT).contains(newStates.get(s)))
									reach.get(newT).add(newStates.get(s));
							}
						}
					}
				}
			}
		} else {
			
			if (reach.get(TransitionTemplate.NULL_TRANSITION) == null)
				reach.put(TransitionTemplate.NULL_TRANSITION, new Vector<CaseTemplate>());
			reach.get(TransitionTemplate.NULL_TRANSITION).add(this);		
		}
		
		return reach;
	}
	
	public String toString() {
		
		return name;
	}
}
