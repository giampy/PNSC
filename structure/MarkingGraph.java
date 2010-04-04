package structure;



import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Vector;

import algo.Case;
import algo.Vertex;


public class MarkingGraph extends Vector<Case> {

	private static final long serialVersionUID = 4736881335014091401L;

	private	Case	firstCase;
	private TreeMap<String, Case> tree; //per gestire il lookup nel marking graph di un marking in O(n)
	
	public Case getFirstCase() {
		return firstCase;
	}

	public void setFirstCase(Case firstCase) {
		this.firstCase = firstCase;
	}

	public MarkingGraph(Vector<Place> initialMarking) {
		
		super();
		tree=new TreeMap<String, Case>();
		this.setFirstCase(new Case(initialMarking));
		tree.put(this.getFirstCase().getOrderedValue(), this.getFirstCase());
		
		add(this.getFirstCase());
		
		for(int c = 0; c < size(); c++) {
			Vector<Transition> enabledTransitions = get(c).getEnabledTransitions();
			for (int e = 0; e < enabledTransitions.size(); e++) {
				
				Vector<Node> newMarking = new Vector<Node>();
				newMarking.addAll(get(c));
				newMarking.removeAll(enabledTransitions.get(e).preset());
				newMarking.addAll(enabledTransitions.get(e).postset());
				Case tmp=alreadyInTree(newMarking);
				if (tmp != null) { 
					Case newCase = tmp;
					if (!get(c).isLinkedTo(newCase)) 
						get(c).addLink(enabledTransitions.get(e), newCase);
					
				} else {
					
					Vector<Place> places = new Vector<Place>();
					for (int s = 0; s < newMarking.size(); s++)
						places.add((Place)newMarking.get(s));
					
					Case newCase = new Case(places);
					get(c).addLink(enabledTransitions.get(e), newCase);
					add(newCase);
					tree.put(newCase.getOrderedValue(), newCase);
				}
			}
		}
	}
	
	public MarkingGraph(Vector<Place> initialMarking, boolean restrictionOnH) {
		
		super();
		this.setFirstCase(new Case(initialMarking));
		tree=new TreeMap<String, Case>();
		
		add(this.getFirstCase());
		tree.put(this.getFirstCase().getOrderedValue(), this.getFirstCase());

		for(int c = 0; c < size(); c++) {
			
			Vector<Transition> enabledTransitions = get(c).getEnabledTransitions();
			for (int e = 0; e < enabledTransitions.size(); e++) {

				if (!restrictionOnH || !enabledTransitions.get(e).isHigh()) {

					Vector<Node> newMarking = new Vector<Node>();
					newMarking.addAll(get(c));
					newMarking.removeAll(enabledTransitions.get(e).preset());
					newMarking.addAll(enabledTransitions.get(e).postset());
					Case tmp=alreadyInTree(newMarking);
					if (tmp != null) {
			
						Case newCase = tmp;
						if (!get(c).isLinkedTo(newCase)) {

							get(c).addLink(enabledTransitions.get(e), newCase);
						} 
					} else {

						Vector<Place> places = new Vector<Place>();
						for (int s = 0; s < newMarking.size(); s++)
							places.add((Place)newMarking.get(s));

						Case newCase = new Case(places);
						get(c).addLink(enabledTransitions.get(e), newCase);
						tree.put(newCase.getOrderedValue(), newCase);
						add(newCase);
					}
				}
			}
		}
	}
	private Case alreadyInTree(Vector<Node> marking) {
		Vector <Place>vecP=new Vector<Place>();
	
		for(int i=0; i<marking.size(); ++i)
			if(marking.get(i) instanceof Place)
				vecP.add((Place) marking.get(i));
		return tree.get(new Case(vecP).getOrderedValue());//O(p)
		
	}
	private Case alreadyIn(Vector<Node> marking) {
		for (int c = 0; c < size(); c++)//O(2^p)
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
 				
 				if (!enabled.get(e).postset().contains(place) //if modificato per gestire l'estensione per i self-loop
 						||enabled.get(e).preset().contains(place)){
 					if(vertexes.get(thisCase.goThrough(enabled.get(e)))==null)
 						vertexes.put(thisCase.goThrough(enabled.get(e)), new Vertex(thisCase.goThrough(enabled.get(e)), false));
 					//assolutamente non sicuro della linea sopra da me aggiunta
 					vertex.addLink(enabled.get(e), vertexes.get(thisCase.goThrough(enabled.get(e))));
 					}
 				/*if (!enabled.get(e).postset().contains(place))
                    vertex.addLink(enabled.get(e), vertexes.get(thisCase.goThrough(enabled.get(e))));*/

 			}
		}
		
		return null;
	}
}
