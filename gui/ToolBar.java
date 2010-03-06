package gui;



import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
//import javax.swing.filechooser.FileNameExtensionFilter;

public class ToolBar extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4896693608883380603L;

	private final MainPane parent;
	
	private ElementaryNetEditingModeBar 	modeBar;
	private PropertyCheckForRealTimeBar		checkBar;
	private JPanel filler;
	private JButton help;
	public ToolBar(MainPane parent) {
		
		super();

		this.parent = parent;
		
		add(newButton());
		add(openButton());
		add(saveButton());
		add(closeButton());
		
		addSeparator();
		
		add(cutButton());	
		add(copyButton());
		add(pasteButton());
		
		addSeparator();
		
		modeBar = new ElementaryNetEditingModeBar(parent);
		modeBar.addTo(this);
		
		addSeparator();
		
		UndoBar undoBar = new UndoBar(parent);
		undoBar.addTo(this);
		
		addSeparator();
		
		ExecuteBar executeBar = new ExecuteBar(parent);
		executeBar.addTo(this);
		
		
		createUpdateCheckBar();
    
    	
		setFloatable(false);
	}
	
	public void createUpdateCheckBar() {
		// TODO Auto-generated method stub
	
		if(checkBar != null){
				checkBar.removeAll(this);
				this.remove(help);
				this.remove(filler);
		}
		else
			addSeparator();
		
		checkBar = new PropertyCheckForRealTimeBar(parent);
		checkBar.addTo(this);
		filler = new JPanel();
        filler.setLayout(new BoxLayout(filler, BoxLayout.X_AXIS));
        filler.add(Box.createHorizontalGlue());
        add(filler);
        help=helpButton();
        
        add(help);
	}

	public void select(ComposeMode cm) {
		
		modeBar.select(cm);
	}
	
	private JButton newButton() {
		
		JButton button = new JButton(new ImageIcon("toolbarButtonGraphics/general/New16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				parent.newNet();
			}
		});
		button.setToolTipText("New");
				
		return button;
	}
	
	private JButton openButton() {
		
		JButton button = new JButton(new ImageIcon("toolbarButtonGraphics/general/Open16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				InputDialog.open(parent);
 			}
		});
		button.setToolTipText("Open");
				
		return button;
	}

	private JButton saveButton() {
		
		JButton button = new JButton(new ImageIcon("toolbarButtonGraphics/general/Save16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				InputDialog.save(parent);
			}
		});
		button.setToolTipText("Save");
				
		return button;
	}

	private JButton saveAsButton() {
		
		JButton button = new JButton(new ImageIcon("toolbarButtonGraphics/general/SaveAs16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (parent.getNet() != null) { 
					
						JFileChooser chooser = new JFileChooser();
						//FileNameExtensionFilter filter = new FileNameExtensionFilter("", "pnml");
						//chooser.setFileFilter(filter);
						if (parent.getNet().getPathName() != null) 
							chooser.setCurrentDirectory(new File(parent.getNet().getPathName()));
						int returnVal = chooser.showSaveDialog(null);
						if (returnVal == JFileChooser.APPROVE_OPTION) {

							String name = chooser.getSelectedFile().getAbsolutePath();
							String extension = name.lastIndexOf(".") != -1? name.substring(name.lastIndexOf(".")) : "";
							if (!extension.equals(".pnml"))
								name += ".pnml";
							parent.saveNet(name);
							
							parent.getNet().setPathAndName(name);
							parent.updateName();
					}
				}				
			}
		});
		button.setToolTipText("Save as");
				
		return button;
	}

	private JButton closeButton() {
		
		JButton button = new JButton(new ImageIcon("toolbarButtonGraphics/general/Close16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				parent.removeNet();
			}
		});
		button.setToolTipText("Close");
				
		return button;
	}
	private JButton cutButton() {
		
		JButton button = new JButton(new ImageIcon("toolbarButtonGraphics/general/Cut16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				parent.cutSelection();
				parent.repaint();
			}
		});
		button.setToolTipText("Cut");
				
		return button;
	}
	
	private JButton copyButton() {
		
		JButton button = new JButton(new ImageIcon("toolbarButtonGraphics/general/Copy16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				parent.copySelection();
				parent.repaint();
			}
		});
		button.setToolTipText("Copy");
				
		return button;
	}
	
	private JButton pasteButton() {
		
		JButton button = new JButton(new ImageIcon("toolbarButtonGraphics/general/Paste16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				parent.pasteSelection();
				parent.repaint();
			}
		});
		button.setToolTipText("Paste");
				
		return button;
	}
	
	private JButton helpButton() {
		
		JButton button = new JButton(new ImageIcon("toolbarButtonGraphics/general/Help16.gif"));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				try {
					   Desktop.getDesktop().open(new File("lucidinadia.pdf"));
					} catch(Exception e) {
					   System.out.println(e);
					}
			}
		});
		button.setToolTipText("Help");
				
		return button;
	}
}
