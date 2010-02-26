package loadstore;



public class Settings {
	
	private static XMLDocument doc;
	
	private static boolean showGrid = false;
	private static boolean snapToGrid = false;
	private static int gridSize = 50;
	private static boolean showNodesName = false;
	private static int elementsRadius = 15;
	private static boolean compareMode = false;
	
	private static String startingDir = "";
	private static int executionDelay = 50;
	
	public static void retrieve() throws Exception {
		
		doc = new XMLDocument("Settings.xml");
		
		showGrid = Boolean.parseBoolean(doc.getTree().findFirst("showGrid").value());
		snapToGrid = Boolean.parseBoolean(doc.getTree().findFirst("snapToGrid").value());
		gridSize = Integer.parseInt(doc.getTree().findFirst("gridSize").value());
		showNodesName = Boolean.parseBoolean(doc.getTree().findFirst("showNodesName").value());
		compareMode = Boolean.parseBoolean(doc.getTree().findFirst("compareMode").value());
		
		startingDir = doc.getTree().findFirst("startingDir").value();
		executionDelay = Integer.parseInt(doc.getTree().findFirst("executionDelay").value());
	}
	
	public static void store() {
		
		doc.save();
	}

	public static boolean isShowGridOn() {

		return showGrid;
	}
	
	public static void setShowGrid(boolean mode) {
		
		showGrid = mode;
		doc.getTree().findFirst("showGrid").setValue(new Boolean(showGrid));
	}
	
	public static boolean isShowNodesNameOn() {

		return showNodesName;
	}
	
	public static void setShowNodesName(boolean show) {
		
		showNodesName = show;
		doc.getTree().findFirst("showNodesName").setValue(new Boolean(showNodesName));
	}
	
	public static boolean isSnapToGridOn() {

		return snapToGrid;
	}
	
	public static void setSnapToGrid(boolean mode) {
		
		snapToGrid = mode;
		doc.getTree().findFirst("snapToGrid").setValue(new Boolean(snapToGrid));
	}
	
	public static int gridSize() {

		return gridSize;
	}
	
	public static void setGridSize(int size) {
		
		gridSize = size;
		doc.getTree().findFirst("gridSize").setValue(new Integer(gridSize));
	}
	
	public static int elementsRadius() {
		
		return elementsRadius;
	}
	
	public static boolean isCompareMode() {
		
		return compareMode;
	}
	
	public static void setCompareMode(boolean mode) {
		
		compareMode = mode;
		doc.getTree().findFirst("compareMode").setValue(new Boolean(compareMode));
	}
	
	public static String startingDir() {
		
		return startingDir;
	}
	
	public static void setStartingDir(String newDir) {
		
		startingDir = newDir;
		doc.getTree().findFirst("startingDir").setValue(newDir);
	}

	public static int executionDelay() {
		
		return executionDelay;
	}
	
	public static void setExecutionDelay(int ed) {
		
		executionDelay = ed;
		doc.getTree().findFirst("executionDelay").setValue(String.valueOf(ed));
	}
}
