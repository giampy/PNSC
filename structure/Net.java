package structure;
import geom.ArcSketch;
import geom.Dir;
import geom.SelectingArea;
import geom.Sketch;
import gui.ComposeMode;
import gui.Grid;
import gui.NetPanel;
import gui.PopupMenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import xml.Properties;
import xml.Settings;
import xml.XMLNode;
import xml.XMLTree;


import algo.ActiveCase;
import algo.Case;
import algo.Check;
import algo.NetTemplate;
import animation.MarkingsHistory;
import animation.NetHistory;
import animation.RandomTimer;


public class Net {

	private Vector<Place> places;
	private Vector<Transition> transitions;
	private Vector<Arc> arcs;
	
	private Vector<Place> initialMarking;
	private MarkingsHistory markingHistory;
	private RandomTimer timer = null;
	
	private NetHistory netHistory;
	
	private String name = "New net";
	private String pathName = null;
	
	private Sketch sketch;
	private boolean isCtrlDown = false;
	private Vector<Element> selected = new Vector<Element>();
	
	public Net() {
		
		places = new Vector<Place>();
		transitions = new Vector<Transition>();
		arcs = new Vector<Arc>();
		
		initialMarking = new Vector<Place>();
		markingHistory = new MarkingsHistory();
		netHistory = new NetHistory();
		netHistory.addNet(this);
	}
	
	public Net(String pathName) {
		
		places = new Vector<Place>();
		transitions = new Vector<Transition>();
		arcs = new Vector<Arc>();
		
		initialMarking = new Vector<Place>();

		setPathAndName(pathName);
		markingHistory = new MarkingsHistory();
		netHistory = new NetHistory();
		netHistory.addNet(this);
	}
	
	public Net(Net net) {
		
		name = net.getName();
		pathName = net.getPathName();
		places = new Vector<Place>();
		transitions = new Vector<Transition>();
		arcs = new Vector<Arc>();

		Map<Node, Node> nodeMap = new Hashtable<Node, Node>(); 

		for (int s = 0; s < net.getPlaces().size(); s++) {
			
			Place oldPlace = net.getPlaces().get(s);
			Place newPlace = new Place(oldPlace);
			nodeMap.put(oldPlace, newPlace);
			places.add(newPlace);
		}

		for (int t = 0; t < net.getTransitions().size(); t++) {
			
			Transition oldTransition = net.getTransitions().get(t);
			Transition newTransition = new Transition(oldTransition);
			nodeMap.put(oldTransition, newTransition);
			transitions.add(newTransition);
		}
		
		for (int a = 0; a < net.getArcs().size(); a++) {
			
			Arc oldArc = net.getArcs().get(a);
			arcs.add(new Arc(oldArc.getPosition(), nodeMap.get(oldArc.getSource()), nodeMap.get(oldArc.getTarget()), newId(Element.ARC)));
		}
		
		initialMarking = new Vector<Place>();
		markingHistory = new MarkingsHistory();
		markingHistory.addMarking(initialMarking);

		netHistory = new NetHistory();
		fixInitialMarking();
		netHistory.erase();
		netHistory.addNet(this);
		
		updateNodesProperties();
	}
	
	public void setTo(NetTemplate netTemplate) {
		
		places = new Vector<Place>();
		transitions = new Vector<Transition>();
		arcs = new Vector<Arc>();
		
		initialMarking = new Vector<Place>();		
		
		Hashtable<Node, Node> newNodes = new Hashtable<Node, Node>();
		
		Vector<Place> places = netTemplate.getPlaces();
		for (int s = 0; s < places.size(); s++) {
			
			Place newPlace = new Place(places.get(s));
			newNodes.put(places.get(s), newPlace);
			this.places.add(newPlace);
		}
		
		Vector<Transition> transitions = netTemplate.getTransitions();
		for (int t = 0; t < transitions.size(); t++) {
			
			Transition newTransition = new Transition(transitions.get(t));
			newNodes.put(transitions.get(t), newTransition);
			this.transitions.add(newTransition);
		}
		
		Vector<Arc> arcs = netTemplate.getArcs();
		for (int a = 0; a < arcs.size(); a++) {
			
				Arc arc = arcs.get(a);
				this.arcs.add(new Arc(arc.getPosition(), newNodes.get(arc.getSource()), newNodes.get(arc.getTarget()), arc.getId()));
		}
		
		Vector<Place> initialMarking = netTemplate.getInitialMarking();
		for (int i = 0; i < initialMarking.size(); i++) { 
			
			Place newPlace = (Place)newNodes.get(initialMarking.get(i));
			if (newPlace != null) {
				
				newPlace.putInTheInitialMarking(true);
				this.initialMarking.add(newPlace);
			}
		}
	}
	
	public String getName() {
		
		return name;
	}
	
	public String getPathName() {
		
		return pathName;
	}
	
	public void setPathAndName(String pathName) {
		
		this.pathName = pathName;
		name = pathName.substring(pathName.lastIndexOf(File.separator) + 1);
	}
	
	public Vector<Place> getPlaces() {
		
		return places;
	}
	
	public Vector<Transition> getTransitions() {
		
		return transitions;
	}
	
	public Vector<Arc> getArcs() {
		
		return arcs;
	}
	
	public Vector<Place> getInitialMarking() {
		
		return initialMarking;
	}
	
	public MarkingsHistory getMarkingHistory() {
		
		return markingHistory;
	}
	
	public NetHistory getNetHistory() {
		
		return netHistory;
	}
	
	public void fixInitialMarking() {
		
		Vector<Place> oldInitialMarking = new Vector<Place>();
		for (int i = 0; i < initialMarking.size(); i++) 
			oldInitialMarking.add(initialMarking.get(i));
		
		initialMarking = new Vector<Place>();
		
		for (int s = 0; s < places.size(); s++) 
			if (places.get(s).getTokens() > 0) {
				
				initialMarking.add(places.get(s));
				places.get(s).putInTheInitialMarking(true);
			} else places.get(s).putInTheInitialMarking(false);
		
		if (!(oldInitialMarking.containsAll(initialMarking) && initialMarking.containsAll(oldInitialMarking)))
			netHistory.addNet(this);
	}
	
	public Vector<Place> getCurrentMarking() {
		
		Vector<Place> marking = new Vector<Place>();
		for (int s = 0; s < places.size(); s++) 
			if (places.get(s).getTokens() > 0) 
				marking.add(places.get(s));
		
		return marking;
	}
	
	public void setCurrentMarking(Vector<Place> marking) {
		
		for (int s = 0; s < places.size(); s++)
			if (marking.contains(places.get(s))) {
				if (places.get(s).getTokens() == 0)
					places.get(s).addToken();
			} else if (places.get(s).getTokens() > 0)
				places.get(s).removeToken();	
	}
	
	public Vector<Transition> getEnabledTransitions() {
		
		Vector<Transition> enabled = new Vector<Transition>();
		for (int t = 0; t < transitions.size(); t++)
			if (transitions.get(t).isEnabled())
				enabled.add(transitions.get(t));
		
		return enabled;
	}
	
	public Dimension getMaxDimension() {
		
		Dimension maxDimension = new Dimension(0, 0);
		
		Vector<Element> elements = allElements();
		for (int e = 0; e < elements.size(); e++) {
			
			if (elements.get(e).getPosition().x + 50 > maxDimension.width)
				maxDimension.width = elements.get(e).getPosition().x + 50;
			if (elements.get(e).getPosition().y + 50 > maxDimension.height)
				maxDimension.height = elements.get(e).getPosition().y + 50;
		}
			
		return maxDimension;
	}
	
	public void zeroAllPlaces(){
		for(int i=0; i<places.size(); ++i){
			places.get(i).propertyBox.setPotentialCausal(new Vector<Transition>());
			places.get(i).propertyBox.setPotentialConflict(new Vector<Transition>());
			places.get(i).propertyBox.setActiveCausal(new Vector<ActiveCase>());
			places.get(i).propertyBox.setActiveConflict(new Vector<ActiveCase>());
		}
	}
	
	public void compose(ComposeMode composeMode, Point point, boolean isCtrlDown) {

		if (composeMode.is(ComposeMode.SELECT)) {
			
			if (!isCtrlDown) {
				
				for (int s = 0; s < selected.size(); s++)
					selected.get(s).setSelected(false);
				
				selected = new Vector<Element>();
			}
			
			Element element = findWhoContains(point);
			
			if (element != null) {
				
				if (selected.contains(element)) {
					
					selected.remove(element);
					element.setSelected(false);
				} else {
					
					selected.add(element);
					element.setSelected(true);
				}
			}
				
		} else {
			
			for (int s = 0; s < selected.size(); s++)
				selected.get(s).setSelected(false);
			
			selected = new Vector<Element>();
		}
		
		if (composeMode.is(ComposeMode.STATE) && noNodeInTheWay(point)) {
			
			if(!Properties.isCheckRealTimeOn())
				zeroAllPlaces();
			places.add(new Place(Grid.closestAllowedPoint(point), newId(Element.STATE)));
			netHistory.addNet(this);
		} 
		else if (composeMode.is(ComposeMode.LOW_TRANSITION)) {
			
			
			if (noNodeInTheWay(point)) {
				
				transitions.add(new Transition(Grid.closestAllowedPoint(point), Transition.LOW, newId(Element.TRANSITION)));
				netHistory.addNet(this);
				if(!Properties.isCheckRealTimeOn())
					zeroAllPlaces();
			} else {
				
				Element element = findWhoContains(point);
				if (element != null && element.is(Element.TRANSITION) && !((Transition)element).isLow()){					
					((Transition)element).setSecurityLevel(Transition.LOW);
					netHistory.addNet(this);
					if(!Properties.isCheckRealTimeOn())
						zeroAllPlaces();
					
				}
			}
		} 
		//aggiungiamo gestione downgrade transition 
		else if (composeMode.is(ComposeMode.DOWNGRADE_TRANSITION)) {
			if (noNodeInTheWay(point)) {
				if(!Properties.isCheckRealTimeOn())
					zeroAllPlaces();
				transitions.add(new Transition(Grid.closestAllowedPoint(point), Transition.DOWNGRADE, newId(Element.TRANSITION)));
				netHistory.addNet(this);
			} else {
				Element element = findWhoContains(point); 
				if (element != null && element.is(Element.TRANSITION) && !((Transition)element).isDowngrade()) {
					((Transition)element).setSecurityLevel(Transition.DOWNGRADE);
					netHistory.addNet(this);
					if(!Properties.isCheckRealTimeOn())
						zeroAllPlaces();
					
				}
			}
		}
		
		else if (composeMode.is(ComposeMode.HIGH_TRANSITION)) {
			
			if (noNodeInTheWay(point)) {
				
				transitions.add(new Transition(Grid.closestAllowedPoint(point), Transition.HIGH, newId(Element.TRANSITION)));
				netHistory.addNet(this);
				if(!Properties.isCheckRealTimeOn())
					zeroAllPlaces();
			} else {
				Element element = findWhoContains(point);//se sono in compose mode=HIGHTRANS e clicko su una bassa me la trasforma in alta...
				if (element != null && element.is(Element.TRANSITION) && !((Transition)element).isHigh())  {
					((Transition)element).setSecurityLevel(Transition.HIGH);
					netHistory.addNet(this);
					if(!Properties.isCheckRealTimeOn())
						zeroAllPlaces();
				}
			}
		} 
		
		
		else if (composeMode.is(ComposeMode.TOKEN)) {
			
			Place place = findPlaceContaining(point);
			if (place != null) {
				
				place.addToken();
				netHistory.addNet(this);
				
				if(!Properties.isCheckRealTimeOn())
					for(int i=0; i<places.size(); ++i)
						zeroAllPlaces();
			}
		} else if (composeMode.is(ComposeMode.DELETE)) {
			
			Element toDelete = findWhoContains(point);
			if (toDelete != null) {
				
				toDelete.deleteFrom(this);
				netHistory.addNet(this);
				if(!Properties.isCheckRealTimeOn())
					zeroAllPlaces();
			}
		}
		
		updateNodesProperties();
	}
	
	public void fire(Point point) {
	
		Element element = findWhoContains(point);
		
		markingHistory.addMarking(getCurrentMarking());
		if (element != null && element.is(Element.TRANSITION))
			((Transition)element).fire();
		markingHistory.addMarking(getCurrentMarking());
	}
	
	public void fire(Transition transition) {
		
		if (transition != null) {
			
			markingHistory.addMarking(getCurrentMarking());
			transition.fire();
			markingHistory.addMarking(getCurrentMarking());
		}
	}
	
	public void execute(RandomTimer timer) {
		
		this.timer = timer;
	}
	
	public void stop() {
		
		timer.cancel();
		timer = null;
	}
	
	public boolean isExecuting() {
		
		return timer != null;
	}
	
	public String newId(int key) {
		
		String prefix = null;
		if (key == Element.STATE)
			prefix = "p";
		else if (key == Element.TRANSITION) 
			prefix = "t";
		else prefix = "a";
		
		Vector<Element> elements = allElements();
		int max = 0;
		for (int e = 0; e < elements.size(); e++) {
			
			String id = elements.get(e).getId();
			if (id.startsWith(prefix)) {
				
				String postfix = id.substring(id.indexOf(prefix) + 1);
				try {
					
					int value = Integer.parseInt(postfix);
					if (value >= max)
						max = value + 1;
				} catch(Exception exception) { }
			}
		}
		
		return prefix + max;
	}
	
	public boolean acceptsNewId(String id, String oldId) {

		Vector<Element> elements = allElements();
		
		for (int e = 0; e < elements.size(); e++)
			if (elements.get(e).getId().equals(id) && !elements.get(e).getId().equals(oldId))
				return false;
		
		return true;
	}
	
	private boolean acceptsId(String id) {

		Vector<Element> elements = allElements();
		
		for (int e = 0; e < elements.size(); e++)
			if (elements.get(e).getId().equals(id))
				return false;
		
		return true;
	}
	
	public void aboutToDrag(ComposeMode composeMode, Point point) {
		
		if (composeMode.is(ComposeMode.SELECT)) {
			
			Element element = findWhoContains(point);
			if (element != null) {
				
				if (!selected.contains(element) || selected.size() == 1)
					sketch = element;
				else sketch = new ElementSet(selected, element);
			} else sketch = new SelectingArea(point, this);
		} else if (composeMode.is(ComposeMode.ARC)) {
		
			Element element = findWhoContains(point);
			if (element != null) {
				
				if (element.isNode())
					sketch = new ArcSketch(this, (Node)element);
				else sketch = element;
			}
		} else {
			
			Element element = findWhoContains(point);
			if (element != null) 
				sketch = element;
		}
	}
	
	public void drag(Point point) {
		
		if (sketch != null)
			sketch.moveTarget(point);
	}
	
	public void quitDragging(Point point, boolean isCtrlDown) {
		
		this.isCtrlDown = isCtrlDown;
		
		if (sketch != null) {

			Element element = sketch.settleTarget(point);
			if (element != null) {
				
				if (element.is(Element.ARC))
					arcs.add((Arc)element);
				
				updateNodesProperties();
				netHistory.addNet(this);
			}
			
			sketch = null;
		}
	}
	
	public void justHovering(Point point) {
		
		Element element = findWhoContains(point);
		
		if (element != null)
			element.hoveringOn();
	}

	public Node findWhoseNodeIdIs(String id) {
		
		for (int s = 0; s < places.size(); s++)
			if (places.get(s).getId().equals(id))
				return places.get(s);
		
		for (int t = 0; t < transitions.size(); t++)
			if (transitions.get(t).getId().equals(id))
				return transitions.get(t);
		
		return null;
	}
	
	public Element findWhoContains(Point point) {
		
		Vector<Element> elements = allElements();
		
		for (int e = 0; e < elements.size(); e++)
			if (elements.get(e).contains(point))
				return elements.get(e);
		
		return null;
	}
	
	public Place findPlaceContaining(Point point) {
		
		for (int s = 0; s < places.size(); s++)
			if (places.get(s).contains(point))
				return places.get(s);
		
		return null;
	}
	
	public Node findNodeContaining(Point point) {
		
		for (int s = 0; s < places.size(); s++)
			if (places.get(s).contains(point))
				return places.get(s);
		
		for (int t = 0; t < transitions.size(); t++)
			if (transitions.get(t).contains(point))
				return transitions.get(t);
		
		return null;		
	}
	
	private boolean noNodeInTheWay(Point point) {
		
		Vector<Node> nodes = new Vector<Node>();
		nodes.addAll(places);
		nodes.addAll(transitions);
		
		for (int n = 0; n < nodes.size(); n++) 
			if (Dir.linearDistance(point, nodes.get(n).getPosition()) < Settings.elementsRadius() * 2)
				return false;
		
		return true;
	}
	
	public void selectElementsContainedIn(Rectangle2D.Double rect) {
		
		Vector<Element> elements = allElements();
		if (!isCtrlDown) {
			
			for (int s = 0; s < selected.size(); s++)
				selected.get(s).setSelected(false);
			selected = new Vector<Element>();
		}
		
		for (int e = 0; e < elements.size(); e++) 
			if (elements.get(e).isContainedOrIntersects(rect)) {
				
				selected.add(elements.get(e));
				elements.get(e).setSelected(true);
			}
	}
	
	public void selectAll() {
		
		selected = allElements();
		for (int s = 0; s < selected.size(); s++)
			selected.get(s).setSelected(true);
	}
	
	public Vector<Element> getSelection() {
		
		Vector<Element> result = new Vector<Element>();
		Hashtable<Node, Node> nodeMap = new Hashtable<Node, Node>();
		
		for (int s = 0; s < selected.size(); s++) 
			if (selected.get(s).is(Element.STATE)) {
				
				Place oldPlace = (Place)selected.get(s);
				Place newPlace = new Place(oldPlace);
				nodeMap.put(oldPlace, newPlace);
				result.add(newPlace);
			} else if (selected.get(s).is(Element.TRANSITION)){
				
				Transition oldTransition = (Transition)selected.get(s);
				Transition newTransition = new Transition(oldTransition);
				nodeMap.put(oldTransition, newTransition);
				result.add(newTransition);
			}
		
		for (int s = 0; s < selected.size(); s++)
			if (selected.get(s).is(Element.ARC)) {
				
				Arc oldArc = (Arc)selected.get(s);
				Node source = nodeMap.get(oldArc.getSource());
				Node target = nodeMap.get(oldArc.getTarget());
				
				if (source != null && target != null)
					result.add(new Arc(oldArc.getPosition(), source, target, newId(Element.ARC)));
			}
			
		return result;
	}
	
	public void paste(Vector<Element> elements) {
		
		// Map elements into new ones
		Hashtable<Element, Element> newElements = new Hashtable<Element, Element>();
		
		for (int e = 0; e < elements.size(); e++) {
			
			Element element = elements.get(e);
			
			if (element.is(Element.STATE)) {
				
				if (!acceptsId(element.getId()))
					element.setId(newId(Element.STATE));
				newElements.put(element, new Place((Place)element));
			} else if (element.is(Element.TRANSITION)) {
				
				if (!acceptsId(element.getId()))
					element.setId(newId(Element.TRANSITION));
				newElements.put(element, new Transition((Transition)element));
			}
		}
		
		for (int e = 0; e < elements.size(); e++)
			if (elements.get(e).is(Element.ARC)) {

				Arc arc = (Arc)elements.get(e);
				newElements.put(arc, new Arc(arc.getPosition(), (Node)newElements.get(arc.getSource()), (Node)newElements.get(arc.getTarget()), newId(Element.ARC)));
			}

		Enumeration<Element> keys = newElements.keys();
		while (keys.hasMoreElements())
			add(newElements.get(keys.nextElement()));
		
		for (int s = 0; s < selected.size(); s++)
			selected.get(s).setSelected(false);
		
		selected = elements;
		
		for (int s = 0; s < selected.size(); s++)
			selected.get(s).setSelected(true);
		
		updateNodesProperties();
		
		netHistory.addNet(this);
	}
	
	public void deleteSelection() {
		
		for (int s = 0; s < selected.size(); s++) 
			selected.get(s).deleteFrom(this);
		
		selected = new Vector<Element>();
		
		netHistory.addNet(this);
	}
	
	public Vector<Element> allElements() {
		
		Vector<Element> elements = new Vector<Element>();
		
		elements.addAll(places);
		elements.addAll(transitions);
		elements.addAll(arcs);
		
		return elements;
	}
		
	public void add(Element element) {
		
		if (element.is(Element.STATE))
			places.add((Place)element);
		else if (element.is(Element.TRANSITION))
			transitions.add((Transition)element);
		else arcs.add((Arc)element);
		
		updateNodesProperties();
	}

	public void remove(Element element) {
		
		if (element.is(Element.STATE))
			places.remove(element);
		else if (element.is(Element.TRANSITION))
			transitions.remove(element);
		else arcs.remove(element);
		
		updateNodesProperties();
		
		//netHistory.addNet(this);
	}
	
	public XMLTree toPNML() {
		
		XMLNode root = new XMLNode("net", "");
		
		Vector<XMLTree> children = new Vector<XMLTree>();
		for (int s = 0; s < places.size(); s++)
			children.add(places.get(s).toPnml());
		
		for (int t = 0; t < transitions.size(); t++)
			children.add(transitions.get(t).toPnml());
		
		for (int a = 0; a < arcs.size(); a++)
			children.add(arcs.get(a).toPnml());
		
		XMLTree netTree = new XMLTree(root, children);
		
		XMLNode pnmlNode = new XMLNode("pnml", "");
		
		Vector<XMLTree> pnmlChildren = new Vector<XMLTree>();
		pnmlChildren.add(netTree);
		
		return new XMLTree(pnmlNode, pnmlChildren);
	}
	
	public void showPopup(NetPanel netPanel, Point point) {

		Element element = findWhoContains(point);
	
		if (element != null) {
			
			PopupMenu pm = element.getPopupMenu(netPanel);
			pm.show(netPanel, point.x, point.y);
		}
	}
	
	public void paint(Graphics2D graphics) {
		
		for (int s = 0; s < places.size(); s++)
			places.get(s).paint(graphics);
		
		for (int t = 0; t < transitions.size(); t++)
			transitions.get(t).paint(graphics);
		
		for (int a = 0; a < arcs.size(); a++) 
			arcs.get(a).paint(graphics);
		
		graphics.setColor(Color.BLACK);
		if (sketch != null) 
			sketch.draw(graphics);
	}
	
	// Check Properties Methods
	
	public void updateNodesProperties() {
		
		if (Properties.isCheckSimpleOn()) {
			
			Vector<Node> nodes = new Vector<Node>();
			nodes.addAll(places);
			nodes.addAll(transitions);
			for (int n = 0; n < nodes.size(); n++)
				nodes.get(n).propertyBox.setSimple(new Vector<Node>());
			
			Vector<Vector<Node>> doubles = checkSimplicity();
			for (int d = 0; d < doubles.size(); d++)
				for (int ds = 0; ds < doubles.get(d).size(); ds++)
					doubles.get(d).get(ds).propertyBox.setSimple(doubles.get(d));
		}
		
		if (Properties.isCheckReducedOn()) {
			
			for (int t = 0; t < transitions.size(); t++)
				transitions.get(t).propertyBox.setReduced(true);
			
			Vector<Transition> notReduced = checkReducedness();
			for (int n = 0; n < notReduced.size(); n++)
				notReduced.get(n).propertyBox.setReduced(false);
		}
		
		if (Properties.isCheckContactsOn()) {
			
			for (int t = 0; t < transitions.size(); t++)
				transitions.get(t).propertyBox.setContacts(new Vector<Vector<Place>>());
			
			Hashtable<Transition, Vector<Case>> contacts = checkContacts();
			Enumeration<Transition> keys = contacts.keys();
			while (keys.hasMoreElements()) {
				
				Transition transition = keys.nextElement();
				Vector<Case> markings = contacts.get(transition);
				
				Vector<Vector<Place>> placeVectors = new Vector<Vector<Place>>();
				for (int m = 0; m < markings.size(); m++)
					placeVectors.add((Vector<Place>)markings.get(m));
				
				transition.propertyBox.setContacts(placeVectors);
			}
		}
		
		if (Properties.isCheckRealTimeOn() && Properties.isCheckPotentialCausalRealTimeOn()) 
			showIfAnyPotentialCausal();
		if (Properties.isCheckRealTimeOn() &&Properties.isCheckActiveCausalRealTimeOn()) 
			showIfAnyActiveCausal();
		if (Properties.isCheckRealTimeOn() &&Properties.isCheckPotentialConflictRealTimeOn()) 
			showIfAnyPotentialConflict();
		if (Properties.isCheckRealTimeOn() &&Properties.isCheckActiveConflictRealTimeOn()) 
			showIfAnyActiveConflict();
	
	}
	
	public void showIfAnyPotentialCausal(){
		for (int s = 0; s < places.size(); s++)
			places.get(s).propertyBox.setPotentialCausal(new Vector<Transition>());
	
		Hashtable<Place, Vector<Transition>> potentialCausal = Check.checkPotentialCausal(this);

		Enumeration<Place> keys = potentialCausal.keys();
		while (keys.hasMoreElements()) {
			
			Place place = keys.nextElement();
			Vector<Transition> pc = potentialCausal.get(place);
			place.propertyBox.setPotentialCausal(pc);
		}
	}
	public void showIfAnyPotentialCausal(Hashtable<Place, Vector<Transition>> potentialCausal){
		for (int s = 0; s < places.size(); s++)
			places.get(s).propertyBox.setPotentialCausal(new Vector<Transition>());

		Enumeration<Place> keys = potentialCausal.keys();
		while (keys.hasMoreElements()) {
			
			Place place = keys.nextElement();
			Vector<Transition> pc = potentialCausal.get(place);
			place.propertyBox.setPotentialCausal(pc);
		}
	}
	public void showIfAnyActiveCausal(){
		for (int s = 0; s < places.size(); s++)
			places.get(s).propertyBox.setActiveCausal(new Vector<ActiveCase>());
		
		Hashtable<Place, Vector<ActiveCase>> activeCausal = Check.checkActiveCausal(this, Check.checkPotentialCausal(this));
		Enumeration<Place> keys = activeCausal.keys();
		while (keys.hasMoreElements()) {
			
			Place place = keys.nextElement();
			Vector<ActiveCase> activeCases = activeCausal.get(place);
			place.propertyBox.setActiveCausal(activeCases);
		}
	}
	public void showIfAnyActiveCausal(	Hashtable<Place, Vector<ActiveCase>> activeCausal){
		for (int s = 0; s < places.size(); s++)
			places.get(s).propertyBox.setActiveCausal(new Vector<ActiveCase>());
		
		Enumeration<Place> keys = activeCausal.keys();
		while (keys.hasMoreElements()) {
			
			Place place = keys.nextElement();
			Vector<ActiveCase> activeCases = activeCausal.get(place);
			place.propertyBox.setActiveCausal(activeCases);
		}
	}
	
	public void showIfAnyPotentialConflict(){
		
		for (int s = 0; s < places.size(); s++)
			places.get(s).propertyBox.setPotentialConflict(new Vector<Transition>());
		
		Hashtable<Place, Vector<Transition>> potentialConflict = Check.checkPotentialConflict(this);
		Enumeration<Place> keys = potentialConflict.keys();
		while (keys.hasMoreElements()) {
			
			Place place = keys.nextElement();
			Vector<Transition> pc = potentialConflict.get(place);
			
			place.propertyBox.setPotentialConflict(pc);
		}
		
	}
	public void showIfAnyPotentialConflict(Hashtable<Place, Vector<Transition>> potentialConflict){
		
		for (int s = 0; s < places.size(); s++)
			places.get(s).propertyBox.setPotentialConflict(new Vector<Transition>());
		
		Enumeration<Place> keys = potentialConflict.keys();
		while (keys.hasMoreElements()) {
			
			Place place = keys.nextElement();
			Vector<Transition> pc = potentialConflict.get(place);
			
			place.propertyBox.setPotentialConflict(pc);
		}
		
	}
	
	public void showIfAnyActiveConflict(){
		for (int s = 0; s < places.size(); s++)
			places.get(s).propertyBox.setActiveConflict(new Vector<ActiveCase>());
		
		Hashtable<Place, Vector<ActiveCase>> activeConflict = Check.checkActiveConflict(this, Check.checkPotentialConflict(this));
		Enumeration<Place> keys = activeConflict.keys();
		while (keys.hasMoreElements()) {
			
			Place place = keys.nextElement();
			Vector<ActiveCase> activeCases = activeConflict.get(place);
			place.propertyBox.setActiveConflict(activeCases);
		}
		
	}
	public void showIfAnyActiveConflict(Hashtable<Place, Vector<ActiveCase>> activeConflict){
		for (int s = 0; s < places.size(); s++)
			places.get(s).propertyBox.setActiveConflict(new Vector<ActiveCase>());
		
		Enumeration<Place> keys = activeConflict.keys();
		while (keys.hasMoreElements()) {
			
			Place place = keys.nextElement();
			Vector<ActiveCase> activeCases = activeConflict.get(place);
			place.propertyBox.setActiveConflict(activeCases);
		}
		
	}
	
	
	
	
	private Vector<Vector<Node>> checkSimplicity() {
		
		Vector<Vector<Node>> allDoubles = new Vector<Vector<Node>>();
		
		Vector<Node> states = new Vector<Node>(this.places);
		Vector<Node> transitions = new Vector<Node>(this.transitions);
		
		for (int s = 0; s < states.size(); s++) {
			
			Node state = states.get(s);
			Vector<Node> doubles = new Vector<Node>();
			for (int d = s + 1; d < states.size(); d++)
				if (areTheSame(state.preset(), states.get(d).preset()) && 
						areTheSame(state.postset(), states.get(d).postset()))
						doubles.add(states.get(d));
						
			if (doubles.size() > 0) {
				
				for (int d = 0; d < doubles.size(); d++) 
					states.remove(doubles.get(d));
				doubles.add(state);
				
				allDoubles.add(doubles);
			}
		}
		
		for (int t = 0; t < transitions.size(); t++) {
			
			Node transition = transitions.get(t);
			Vector<Node> doubles = new Vector<Node>();
			for (int d = t + 1; d < transitions.size(); d++)
				if (areTheSame(transition.preset(), transitions.get(d).preset()) && 
						areTheSame(transition.postset(), transitions.get(d).postset()))
						doubles.add(transitions.get(d));
						
			if (doubles.size() > 0) {
				
				for (int d = 0; d < doubles.size(); d++) 
					transitions.remove(doubles.get(d));
				doubles.add(transition);
				
				allDoubles.add(doubles);
			}
		}
		
		return allDoubles;
	}
	
	private boolean areTheSame(Vector<Node> first, Vector<Node> second) {
		
		return first.containsAll(second) && second.containsAll(first);
	}
	
	private Vector<Transition> checkReducedness() {
		
		Vector<Transition> notReduced = new Vector<Transition>();

		if (initialMarking.size() > 0) {
			Vector<Transition> firedTransitions = new Vector<Transition>();

			MarkingGraph mg = new MarkingGraph(this.getInitialMarking());
			for (int c = 0; c < mg.size(); c++) 
				firedTransitions.addAll(mg.get(c).getEnabledTransitions());

			for (int t = 0; t < transitions.size(); t++)
				if (!firedTransitions.contains(transitions.get(t)))
					notReduced.add(transitions.get(t));
		}
		
		return notReduced;
	}
	
	private Hashtable<Transition, Vector<Case>> checkContacts() {
		
		Hashtable<Transition, Vector<Case>> contacts = new Hashtable<Transition, Vector<Case>>();
		
		MarkingGraph mg = new MarkingGraph(this.getInitialMarking());
		for (int c = 0; c < mg.size(); c++) {
			
			Vector<Transition> caseContacts = mg.get(c).getContacts();
			for (int t = 0; t < caseContacts.size(); t++) {
				
				if (!contacts.containsKey(caseContacts.get(t)))
					contacts.put(caseContacts.get(t), new Vector<Case>());
				contacts.get(caseContacts.get(t)).add(mg.get(c));
			}
		}
		
		return contacts;
	}
}
