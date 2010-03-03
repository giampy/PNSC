package algo;




import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import structure.MarkingGraph;
import structure.Net;
import structure.Transition;



public class Check {

	
	public static boolean BSNNI(Net net) {
		
		MarkingGraph markingGraph1 = new MarkingGraph(net.getInitialMarking());
		MarkingGraph markingGraph2 = new MarkingGraph(net.getInitialMarking(), true);
		
		Hashtable<Transition, TransitionTemplate> transTable = new Hashtable<Transition, TransitionTemplate>();
		Vector<Transition> netTransitions = net.getTransitions();
		for (int t = 0; t < netTransitions.size(); t++) 
			transTable.put(netTransitions.get(t), new TransitionTemplate(netTransitions.get(t).getId(), netTransitions.get(t).isHigh()));
		
		Hashtable<Case, CaseTemplate> stateTable1 = new Hashtable<Case, CaseTemplate>();
		for (int mg = 0; mg < markingGraph1.size(); mg++) {

			Case state = markingGraph1.get(mg);
			
			if (stateTable1.get(state) == null)
				stateTable1.put(state, new CaseTemplate(state.toString()));
		}
		
		Hashtable<Case, CaseTemplate> stateTable2 = new Hashtable<Case, CaseTemplate>();
		for (int mg = 0; mg < markingGraph2.size(); mg++) {

			Case state = markingGraph2.get(mg);
			
			if (stateTable2.get(state) == null)
				stateTable2.put(state, new CaseTemplate(state.toString()));
		}
		
		Enumeration<Case> stEnum1 = stateTable1.keys();
		while (stEnum1.hasMoreElements()) {
			
			Case state = stEnum1.nextElement();
			CaseTemplate newState = stateTable1.get(state);
			
			Vector<Transition> transitions = state.getEnabledTransitions();
			for (int t = 0; t < transitions.size(); t++) 
				newState.linkTo(transTable.get(transitions.get(t)), stateTable1.get(state.goThrough(transitions.get(t))));
		}
		
		Enumeration<Case> stEnum2 = stateTable2.keys();
		while (stEnum2.hasMoreElements()) {
			
			Case state = stEnum2.nextElement();
			CaseTemplate newState = stateTable2.get(state);
			
			Vector<Transition> transitions = state.getEnabledTransitions();
			for (int t = 0; t < transitions.size(); t++) 
				if (!transitions.get(t).isHigh())
					newState.linkTo(transTable.get(transitions.get(t)), stateTable2.get(state.goThrough(transitions.get(t))));
		}
		
		if (bisimilar(stateTable1.get(markingGraph1.get(0)), stateTable2.get(markingGraph2.get(0))))
			return true;
		else return false;
	}
	
	public static boolean SBNDC(Net net) {
		
		MarkingGraph markingGraph = new MarkingGraph(net.getInitialMarking());
		//MarkingGraph markingGraph1 = new MarkingGraph(net.getInitialMarking());
		MarkingGraph markingGraph2 = new MarkingGraph(net.getInitialMarking());
		
		Hashtable<Transition, TransitionTemplate> transTable = new Hashtable<Transition, TransitionTemplate>();
		Vector<Transition> netTransitions = net.getTransitions();
		
		
		//Passo dalle transizioni vere e proprie ai TransitionTemplate (più leggeri)
		for (int t = 0; t < netTransitions.size(); t++) 
			transTable.put(netTransitions.get(t), 
					new TransitionTemplate(netTransitions.get(t).getId(), netTransitions.get(t).isHigh()));
		
		
		//Passo  dai case del makingGraph a dei più leggeri CaseTemplate
		Hashtable<Case, CaseTemplate> caseTable1 = new Hashtable<Case, CaseTemplate>();
		Hashtable<Case, CaseTemplate> caseTable2 = new Hashtable<Case, CaseTemplate>();
		for (int mg = 0; mg < markingGraph2.size(); mg++) {

			Case state = markingGraph.get(mg);
			
			if (caseTable1.get(state) == null)
				caseTable1.put(state, new CaseTemplate(state.toString()));
			if (caseTable2.get(state) == null)
				caseTable2.put(state, new CaseTemplate(state.toString()));
		}
		
		Enumeration<Case> stEnum1 = caseTable1.keys();
		while (stEnum1.hasMoreElements()) {
			
			Case state = stEnum1.nextElement();
			CaseTemplate newState = caseTable1.get(state);
			
			Vector<Transition> transitions = state.getEnabledTransitions();
			for (int t = 0; t < transitions.size(); t++) 
				if (!transitions.get(t).isHigh())
						newState.linkTo(transTable.get(transitions.get(t)), caseTable1.get(state.goThrough(transitions.get(t))));
		}
		
		Enumeration<Case> stEnum2 = caseTable2.keys();
		while (stEnum2.hasMoreElements()) {
			
			Case state = stEnum2.nextElement();
			CaseTemplate newState = caseTable2.get(state);
			
			Vector<Transition> transitions = state.getEnabledTransitions();
			for (int t = 0; t < transitions.size(); t++) 
				if (!transitions.get(t).isHigh())
					newState.linkTo(transTable.get(transitions.get(t)), caseTable2.get(state.goThrough(transitions.get(t))));
		}
		
		boolean SBNDC = true;
		
		for (int m = 0; m < markingGraph.size(); m++) {
			
			Case marking = markingGraph.get(m);
			
			Vector<Transition> enabledTransitions = marking.getEnabledTransitions();
			for (int e = 0; e < enabledTransitions.size(); e++) {
				
				Transition enabled = enabledTransitions.get(e);
				if (enabled.isHigh()) {
					
					Case nextMarking = marking.goThrough(enabled);
					if (!bisimilar(caseTable1.get(marking), caseTable2.get(nextMarking)))
						SBNDC = false;
				}
			}
		}
		
		return SBNDC;
	}
	
	public static boolean bisimilar(CaseTemplate s1, CaseTemplate s2) {
		
		CoupleVector couples = new CoupleVector();
		
		couples.add(new Couple(s1, s2));
		
		for (int co = 0; co < couples.size(); co++) {
			
			Couple couple = couples.get(co);
			Hashtable<TransitionTemplate, Vector<CaseTemplate>> firstReach = couple.first.reachLow();
			Hashtable<TransitionTemplate, Vector<CaseTemplate>> secondReach = couple.second.reachLow();
			
			Set<TransitionTemplate> firstTransitions = firstReach.keySet();
			Set<TransitionTemplate> secondTransitions = secondReach.keySet();
			
			if (firstTransitions.equals(secondTransitions)) {
				
				Iterator<TransitionTemplate> transIt = firstTransitions.iterator();
				while (transIt.hasNext()) {
					
					TransitionTemplate t = transIt.next();
					
					Vector<CaseTemplate> firstReachedStates = firstReach.get(t);
					Vector<CaseTemplate> secondReachedStates = secondReach.get(t);
					for (int frs = 0; frs < firstReachedStates.size(); frs++) {

						for (int srs = 0; srs < secondReachedStates.size(); srs++) {
							
							Couple newCouple = new Couple(firstReachedStates.get(frs), secondReachedStates.get(srs));
							
							//System.out.println(newCouple);
							
							if (!couples.contains(newCouple))
								couples.add(newCouple);
							
							Couple newReachedCouple = new Couple(firstReachedStates.get(frs).leadsTo(t), 
														secondReachedStates.get(srs).leadsTo(t));

							if (!couples.contains(newReachedCouple))
								couples.add(newReachedCouple);
						}
					}
				}
			} else {
				
				System.out.println("Not bisimilar : " + firstTransitions + " - " + secondTransitions);
				//co = couples.size();
				return false;
			}
		}
		
		System.out.println("Bisimilar");
		return true;
	}
}
