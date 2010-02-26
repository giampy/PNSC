package loadstore;



public class Properties {
	
	private static XMLDocument doc;
	
	private static boolean showEnabledTransitions = false;
	private static boolean checkSimple = false;
	private static boolean checkReduced = false;
	private static boolean checkContacts = false;
	private static boolean checkPotentialCausal = false;
	private static boolean checkPotentialConflict = false;
	private static boolean checkActiveCausal = false;
	private static boolean checkActiveConflict = false;
	
	public static void retrieve() throws Exception {
		
		doc = new XMLDocument("Properties.xml");
		
		showEnabledTransitions = Boolean.parseBoolean(doc.getTree().findFirst("showEnabledTransitions").value());
		checkSimple = Boolean.parseBoolean(doc.getTree().findFirst("checkSimple").value());
		checkReduced = Boolean.parseBoolean(doc.getTree().findFirst("checkReduced").value());
		checkContacts = Boolean.parseBoolean(doc.getTree().findFirst("checkContacts").value());
		checkPotentialCausal = Boolean.parseBoolean(doc.getTree().findFirst("checkPotentialCausal").value());
		checkPotentialConflict = Boolean.parseBoolean(doc.getTree().findFirst("checkPotentialConflict").value());
		checkActiveCausal = Boolean.parseBoolean(doc.getTree().findFirst("checkActiveCausal").value());
		checkActiveConflict = Boolean.parseBoolean(doc.getTree().findFirst("checkActiveConflict").value());
	}
	
	public static void store() {
		
		doc.save();
	}

	public static boolean isShowEnabledTransitionsOn() {

		return showEnabledTransitions;
	}
	
	public static void setShowEnabledTransitions(boolean mode) {
		
		showEnabledTransitions = mode;
		doc.getTree().findFirst("showEnabledTransitions").setValue(new Boolean(showEnabledTransitions));
	}

	public static boolean isCheckSimpleOn() {

		return checkSimple;
	}
	
	public static void setCheckSimple(boolean mode) {
		
		checkSimple = mode;
		doc.getTree().findFirst("checkSimple").setValue(new Boolean(checkSimple));
	}

	public static boolean isCheckReducedOn() {

		return checkReduced;
	}
	
	public static void setCheckReduced(boolean mode) {
		
		checkReduced = mode;
		doc.getTree().findFirst("checkReduced").setValue(new Boolean(checkReduced));
	}

	public static boolean isCheckContactsOn() {

		return checkContacts;
	}
	
	public static void setCheckContacts(boolean mode) {
		
		checkContacts = mode;
		doc.getTree().findFirst("checkContacts").setValue(new Boolean(checkContacts));
	}

	public static boolean isCheckPotentialCausalOn() {

		return checkPotentialCausal;
	}
	
	public static void setCheckPotentialCausal(boolean mode) {
		
		checkPotentialCausal = mode;
		doc.getTree().findFirst("checkPotentialCausal").setValue(new Boolean(checkPotentialCausal));
	}

	public static boolean isCheckPotentialConflictOn() {

		return checkPotentialConflict;
	}
	
	public static void setCheckPotentialConflict(boolean mode) {
		
		checkPotentialConflict = mode;
		doc.getTree().findFirst("checkPotentialConflict").setValue(new Boolean(checkPotentialConflict));
	}

	public static boolean isCheckActiveCausalOn() {

		return checkActiveCausal;
	}
	
	public static void setCheckActiveCausal(boolean mode) {
		
		checkActiveCausal = mode;
		doc.getTree().findFirst("checkActiveCausal").setValue(new Boolean(checkActiveCausal));
	}

	public static boolean isCheckActiveConflictOn() {

		return checkActiveConflict;
	}
	
	public static void setCheckActiveConflict(boolean mode) {
		
		checkActiveConflict = mode;
		doc.getTree().findFirst("checkActiveConflict").setValue(new Boolean(checkActiveConflict));
	}
}
