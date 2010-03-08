package algo;




import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import structure.MarkingGraph;
import structure.Net;
import structure.Node;
import structure.Place;
import structure.Transition;



public class Check {
	
	
	public static boolean PositivePBNI(Net net){
		//Check potential
		Hashtable<Place, Vector<Transition>> potentialCausal = checkPotentialCausal(net);
		Hashtable<Place, Vector<Transition>> potentialConflict = checkPotentialConflict(net);
		if(potentialCausal.size()==0 && potentialConflict.size()==0)
			return true;
		//Check active
		if(potentialCausal.size()>0){
			Hashtable<Place, Vector<ActiveCase>> result = new Hashtable<Place, Vector<ActiveCase>>();
			result=checkActiveCausal(net, potentialCausal);
			if (result.size()>0)
				return false;
		}
		if(potentialConflict.size()>0){
			Hashtable<Place, Vector<ActiveCase>> result = new Hashtable<Place, Vector<ActiveCase>>();
			result=checkActiveConflict(net, potentialConflict);
			if (result.size()>0)
				return false;
		}
		return true;
		
	}
	
	public static Hashtable<Place, Vector<Transition>> checkPotentialCausal(Net net) {

		Hashtable<Place, Vector<Transition>> potential = new Hashtable<Place, Vector<Transition>>();
		
		for (int s = 0; s < net.getPlaces().size(); s++) {
			
			Place place = net.getPlaces().get(s);
			Vector<Transition> pc = checkPotentialCausal(place);
			
			if (pc.size() > 0) 
				potential.put(place, pc);
		}
		
		return potential;
	}
	
	private static Vector<Transition> checkPotentialCausal(Place place) {
		
		Vector<Transition> potential = new Vector<Transition>();
		
		Vector<Node> preset = place.preset();
		Vector<Transition> hiPreset = new Vector<Transition>();
		for (int p = 0; p < preset.size(); p++)
			if (((Transition)preset.get(p)).isHigh())
				hiPreset.add((Transition)preset.get(p));

		Vector<Node> postset = place.postset();
		Vector<Transition> loPostset = new Vector<Transition>();
		for (int p = 0; p < postset.size(); p++)
			if (!((Transition)postset.get(p)).isHigh()) 
				loPostset.add((Transition)postset.get(p));
		
		if (hiPreset.size() > 0 && loPostset.size() > 0) {
			
			potential.addAll(hiPreset);
			potential.addAll(loPostset);
		}
		
		return potential;
	}
	
	public static Hashtable<Place, Vector<ActiveCase>> checkActiveCausal(Net net, 
			Hashtable<Place, Vector<Transition>> potential) {
		
		Hashtable<Place, Vector<ActiveCase>> result = new Hashtable<Place, Vector<ActiveCase>>();
		
		MarkingGraph markingGraph = new MarkingGraph(net.getInitialMarking()); //marking iniziale 
		Enumeration<Place> keys = potential.keys(); //gli stati potential causal
		while (keys.hasMoreElements()) {
			
			Place place = keys.nextElement();
			Vector<Transition> transitions = potential.get(place);
			Vector<Transition> high = highSubset(transitions);
			Vector<Transition> low = lowSubset(transitions);
			
			Vector<ActiveCase> cases = new Vector<ActiveCase>();
			for (int h = 0; h < high.size(); h++) 
				for (int l = 0; l < low.size(); l++) {

					Vector<ActiveCase> hlCases = checkActiveCausal(markingGraph, 
														place, high.get(h), low.get(l));
					if (hlCases.size() > 0)
						cases.addAll(hlCases);
				}
					
			if (cases.size() > 0) 
				result.put(place, cases);
		}
		
		return result;
	}
	
	public static Vector<ActiveCase> checkActiveCausal(MarkingGraph mg, Place place, 
			Transition high, Transition low) {
	
		Vector<ActiveCase> result = new Vector<ActiveCase>();

		for (int c = 0; c < mg.size(); c++) {

			if (mg.get(c).getEnabledTransitions().contains(high)) {
				
				Case afterHigh = mg.get(c).goThrough(high);

				MarkingGraph subMg = new MarkingGraph(afterHigh);

				Vector<Case> path = subMg.closestPathTo(low, place);//questa è la sigma..della definizione
				if (path != null) 
					result.add(new ActiveCase(high, path.get(path.size() - 1).firstTransition(), 
							mg.get(c).toVector(), path));
			}
		}
		
		return result;
	}
	
	private static Vector<Transition> lowSubset(Vector<Transition> set) {
		
		Vector<Transition> result = new Vector<Transition>();
		
		for (int t = 0; t < set.size(); t++)
			if (!set.get(t).isHigh())
				result.add(set.get(t));
		
		return result;
	}

	private static Vector<Transition> highSubset(Vector<Transition> set) {
		
		Vector<Transition> result = new Vector<Transition>();
		
		for (int t = 0; t < set.size(); t++)
			if (set.get(t).isHigh())
				result.add(set.get(t));
		
		return result;
	}
	
	
	
	public static Hashtable<Place, Vector<ActiveCase>> checkActiveConflict(Net net,
					Hashtable<Place, Vector<Transition>> potential) {
		
		Hashtable<Place, Vector<ActiveCase>> result = new Hashtable<Place, Vector<ActiveCase>>();
		
		MarkingGraph markingGraph = new MarkingGraph(net.getInitialMarking());
		Enumeration<Place> keys = potential.keys();
		while (keys.hasMoreElements()) {
			
			Place place = keys.nextElement();
			Vector<Transition> transitions = potential.get(place);
			Vector<Transition> high = highSubset(transitions);
			Vector<Transition> low = lowSubset(transitions);
			
			Vector<ActiveCase> cases = new Vector<ActiveCase>();
			for (int h = 0; h < high.size(); h++) 
				for (int l = 0; l < low.size(); l++) {

					Vector<ActiveCase> hlCases = checkActiveConflict(markingGraph, place, high.get(h), low.get(l));
					if (hlCases.size() > 0)
						cases.addAll(hlCases);
				}
					
			if (cases.size() > 0) 
				result.put(place, cases);
		}
		
		return result;
	}
	
	
	public static Vector<ActiveCase> checkActiveConflict(MarkingGraph mg, Place place, Transition high, Transition low) {
		
		Vector<ActiveCase> result = new Vector<ActiveCase>();

		for (int c = 0; c < mg.size(); c++) {

			if (mg.get(c).getEnabledTransitions().contains(high)) {
				
				MarkingGraph subMg = new MarkingGraph(mg.get(c));

				Vector<Case> path = subMg.closestPathTo(low, place);
				if (path != null) 
					result.add(new ActiveCase(high, path.get(path.size() - 1).firstTransition(), mg.get(c).toVector(), path));
			}
		}
		
		return result;
	}
	
	
	
	
	public static Hashtable<Place, Vector<Transition>> checkPotentialConflict(Net net) {
		
		Hashtable<Place, Vector<Transition>> potential = new Hashtable<Place, Vector<Transition>>();
		
		for (int s = 0; s < net.getPlaces().size(); s++) {
			
			Place place = net.getPlaces().get(s);
			Vector<Transition> pc = checkPotentialConflict(place);
			
			if (pc.size() > 0) 
				potential.put(place, pc);
		}
		
		return potential;
	}
	
	private static Vector<Transition> checkPotentialConflict(Place place) {
		
		Vector<Transition> potential = new Vector<Transition>();
		
		Vector<Node> postset = place.postset();
		Vector<Transition> hiPostset = new Vector<Transition>();
		for (int p = 0; p < postset.size(); p++)
			if (((Transition)postset.get(p)).isHigh())
				hiPostset.add((Transition)postset.get(p));

		Vector<Transition> loPostset = new Vector<Transition>();
		for (int p = 0; p < postset.size(); p++)
			if (!((Transition)postset.get(p)).isHigh())
				loPostset.add((Transition)postset.get(p));
		
		if (hiPostset.size() > 0 && loPostset.size() > 0) {
			
			potential.addAll(hiPostset);
			potential.addAll(loPostset);
		}
		
		return potential;
	}
	
	
	
	public static boolean BSNNI(Net net) {
		System.out.println("called");
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
				if (!transitions.get(t).isHigh())//Non le alte... (Ma per per le downgrade come si comporta BSNNI?)
					newState.linkTo(transTable.get(transitions.get(t)), stateTable2.get(state.goThrough(transitions.get(t))));
		}
		
		if (lowViewBisimilar(stateTable1.get(markingGraph1.get(0)), stateTable2.get(markingGraph2.get(0))))
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
				if (transitions.get(t).isLow())//modifica per gestire il downgrading
						newState.linkTo(transTable.get(transitions.get(t)), caseTable1.get(state.goThrough(transitions.get(t))));
		}
		
		Enumeration<Case> stEnum2 = caseTable2.keys();
		while (stEnum2.hasMoreElements()) {
			
			Case state = stEnum2.nextElement();
			CaseTemplate newState = caseTable2.get(state);
			
			Vector<Transition> transitions = state.getEnabledTransitions();
			for (int t = 0; t < transitions.size(); t++) 
				if (transitions.get(t).isLow()) //modifica per gestire il downgrading
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
					if (!lowViewBisimilar(caseTable1.get(marking), caseTable2.get(nextMarking)))
						SBNDC = false;
				}
			}
		}
		
		return SBNDC;
	}
	
	public static boolean lowViewBisimilar(CaseTemplate s1, CaseTemplate s2) {
		
		CoupleVector couples = new CoupleVector();
		
		couples.add(new Couple(s1, s2));
		//la size di couples viene modificata nel ciclo
		// è una specie di visita breadth-first con pila
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
							
							if (!couples.contains(newCouple))
								couples.add(newCouple);
							
							Couple newReachedCouple = new Couple(firstReachedStates.get(frs).leadsTo(t), 
														secondReachedStates.get(srs).leadsTo(t));

							if (!couples.contains(newReachedCouple))
								couples.add(newReachedCouple);
						}
					}
				}
			} else 
				return false;
		}
		return true;
	}
}
