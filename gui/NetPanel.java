package gui;




import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.JPanel;

import structure.Element;
import structure.Net;
import structure.Place;



public class NetPanel extends JPanel {

	private Net net;
	private MainPane mainPane;
	private final ToolBar toolbar;
	
	private Net focus = new Net();
	
	public NetPanel(MainPane mainPane) {
		
		super();
		
		net = new Net();
		this.mainPane = mainPane;
		toolbar = new ToolBar(mainPane);

		mainPane.updateToolbar(toolbar);
		
		NetListener listener = new NetListener(net, this, mainPane);
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}
	
	public NetPanel(MainPane mainPane, Net net) {
		
		super();
		
		this.net = net;
		this.mainPane = mainPane;
		toolbar = new ToolBar(mainPane);
  
		mainPane.updateToolbar(toolbar);
		
		NetListener listener = new NetListener(net, this, mainPane);
		addMouseListener(listener);
		addMouseMotionListener(listener);
	
		setPreferredSize(net.getMaxDimension());
	}
	
	public Net getNet() {
		
		return net;
	}
	
	public ToolBar getToolBar() {
		
		return toolbar;
	}
	
	public void updateToolbar() {
		
		mainPane.updateToolbar(toolbar);
	}
	
	public void requestFocus() {
		
		mainPane.requestFocus(this);
	}
	
	public boolean alreadyHasFocus() {
		
		return equals(mainPane.getNetPanel());
	}

	public void showPopup(Point point) {
		
		net.showPopup(this, point);
	}
	
	public void paint(Graphics g) {
		
		setPreferredSize(net.getMaxDimension());
		revalidate();
		
		Graphics2D graphics = (Graphics2D)g;
		//graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		graphics.setColor(Color.WHITE);
		Dimension size = getSize();
		graphics.fillRect(0, 0, size.width, size.height);
		
		Grid.paint(this, graphics);
		
		net.paint(graphics);
		
		if (focus != null) {
			
			Vector<Element> focusedElements = focus.allElements();
			
			if (focusedElements.size() > 0) {

				Composite oldComposite = graphics.getComposite();
				graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
				graphics.setColor(Color.WHITE);
				graphics.fill(new Rectangle2D.Double(0, 0, size.width, size.height));
				graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
				graphics.setColor(Color.LIGHT_GRAY);
				graphics.fill(new Rectangle2D.Double(0, 0, size.width, size.height));
				graphics.setComposite(oldComposite);

				for (int f = 0; f < focusedElements.size(); f++)
					focusedElements.get(f).paint(graphics);
			}
		}
		
	}
	
	public void focusOn(Net focus) {
		
		this.focus = focus;
		
		removeAllMouseListeners();
		addMouseListener(new FocusedNetListener(this, mainPane));
		repaint();
	}
	
	public void switchFocus(Net focus) {
		
		this.focus = focus;
		repaint();
	}
	
	public void focusOn(final Vector<Net> focus) {
		
		if (focus.size() > 0) {
			
			this.focus = focus.get(0);
			
			final Net net = this.net;
			final Vector<Place> formerMarking = net.getCurrentMarking();
			net.setCurrentMarking(new Vector<Place>());
			
			removeAllMouseListeners();
			
			if (focus.size() > 1) {
				
				final BrowseMarkingsBar bmb = new BrowseMarkingsBar(this, focus);
				bmb.addTo(toolbar);
				
				addMouseListener(new FocusedNetListener(this, mainPane) {
					
					public void mouseClicked(MouseEvent arg0) {

						if (alreadyHasFocus())
							bmb.removeFrom(toolbar);
						super.mouseClicked(arg0);
						
						net.setCurrentMarking(formerMarking);
					}
				});
			} else addMouseListener(new FocusedNetListener(this, mainPane) {
				
				public void mouseClicked(MouseEvent arg0) {
					
					super.mouseClicked(arg0);
					
					net.setCurrentMarking(formerMarking);
				}
			});
			
			repaint();
		}
	}
	
	public void disableFocus() {
		
		this.focus = new Net();
		
		removeAllMouseListeners();
		NetListener listener = new NetListener(net, this, mainPane);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		
		net.updateNodesProperties();
		
		repaint();
	}
	
	private void removeAllMouseListeners() {
		
		MouseListener[] mls = getMouseListeners();
		MouseMotionListener[] mmls = getMouseMotionListeners();
		
		for (int m = 0; m < mls.length; m++)
			removeMouseListener(mls[m]);

		for (int m = 0; m < mmls.length; m++)
			removeMouseMotionListener(mmls[m]);
	}
}

