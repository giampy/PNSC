package structure;



import java.util.Hashtable;
import java.util.Vector;

import algo.Case;
import algo.Vertex;

//prossima estensione utilizzare un TreeMap<K, V> per rendere più efficiente il tutto in termini
//di complessità computazionale
public class MarkingGraph extends Vector<Case> {

	private static final long serialVersionUID = 4736881335014091401L;

	public MarkingGraph(Vector<Place> initialMarking) {
		
		super();
		
		add(new Case(initialMarking));
		
		for(int c = 0; c < size(); c++) {
			
			Vector<Transition> enabledTransitions = get(c).getEnabledTransitions();
			for (int e = 0; e < enabledTransitions.size(); e++) {
				
				Vector<Node> newMarking = new Vector<Node>();
				newMarking.addAll(get(c));
				newMarking.removeAll(enabledTransitions.get(e).preset());
				newMarking.addAll(enabledTransitions.get(e).postset());
				
				if (alreadyIn(newMarking) != null) {
					
					Case newCase = alreadyIn(newMarking);
					if (!get(c).isLinkedTo(newCase)) {
						
						get(c).addLink(enabledTransitions.get(e), newCase);
					} 
				} else {
					
					Vector<Place> places = new Vector<Place>();
					for (int s = 0; s < newMarking.size(); s++)
						places.add((Place)newMarking.get(s));
					
					Case newCase = new Case(places);
					get(c).addLink(enabledTransitions.get(e), newCase);
					add(newCase);
				}
			}
		}
	}
	
	public MarkingGraph(Vector<Place> initialMarking, boolean restrictionOnH) {
		
		super();
		
		add(new Case(initialMarking));
		
		for(int c = 0; c < size(); c++) {
			
			Vector<Transition> enabledTransitions = get(c).getEnabledTransitions();
			for (int e = 0; e < enabledTransitions.size(); e++) {

				if (!restrictionOnH || enabledTransitions.get(e).isLow()) {

					Vector<Node> newMarking = new Vector<Node>();
					newMarking.addAll(get(c));
					newMarking.removeAll(enabledTransitions.get(e).preset());
					newMarking.addAll(enabledTransitions.get(e).postset());

					if (alreadyIn(newMarking) != null) {

						Case newCase = alreadyIn(newMarking);
						if (!get(c).isLinkedTo(newCase)) {

							get(c).addLink(enabledTransitions.get(e), newCase);
						} 
					} else {

						Vector<Place> places = new Vector<Place>();
						for (int s = 0; s < newMarking.size(); s++)
							places.add((Place)newMarking.get(s));

						Case newCase = new Case(places);
						get(c).addLink(enabledTransitions.get(e), newCase);
						add(newCase);
					}
				}
			}
		}
	}
	
	private Case alreadyIn(Vector<Node> marking) {
		
		for (int c = 0; c < size(); c++)
			if (get(c).containsAll(marking) && marking.containsAll(get(c)))
				return get(c);
		
		return null;
	}
	
	public Vector<Case> closestPathTo(Transition transition, Place place) {
		
		Hashtable<Case, Vertex> vertexes = new Hashtable<Case, Vertex>();
		for (int c = 0; c < size(); c++) 
			vertexes.put(get(c), new Vertex(get(c), c == 0));
		
		for (int c = 0; c < size(); c++) {
			
			Case thisCase = get(c);
			Vertex vertex = vertexes.get(thisCase);
			
			Vector<Transition> enabled = thisCase.getEnabledTransitions();
 			for (int e = 0; e < enabled.size(); e++) {
 								//nelle dispense c'è scritto che nel modello a tre livelli di sicurezza
 								//per essere active causal la x tale per cui m[hxl> deve appartenere a (L U H)*
 								//perciò togliamo quei percorsi in cui compare una downgrade
 				if (enabled.get(e).equals(transition) && !transition.isDowngrade()) {
 					
 					Vector<Case> path = new Vector<Case>();
 					Vertex step = vertex;
 					Transition linkToNext = transition;
 					Case next = thisCase.goThrough(transition);
 					
 					while (step != null) {
 						
 						Case newCase = new Case(step.getMarking().toVector());
 						newCase.addLink(linkToNext, next);
 						path.add(0, newCase);
 						
 						linkToNext = step.getLinkFromPrevious();
 						next = step.getMarking();
 						step = step.getPrevious();
 					}
 					
 					return path;
 				}
 				
 				if (!enabled.get(e).postset().contains(place)) 
 					vertex.addLink(enabled.get(e), vertexes.get(thisCase.goThrough(enabled.get(e))));
 			}
		}
		
		return null;
	}
}
