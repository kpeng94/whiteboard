package canvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Whiteboard {
	private String name;
	private Canvas canvas;
	private ArrayList<String> users;
	private ArrayList<LineSegment> lineSegments;
	
	/**
	 * Constructor for a new whiteboard object
	 * @param title Name of the whiteboard
	 * @param canvas The canvas associated with the whiteboard
	 * @param users The list of users who are currently accessing this whiteboard
	 */
	public Whiteboard(String name, ArrayList<String> users) {
		this.name = name;
		this.users = users;
		this.canvas = null;
		this.lineSegments = new ArrayList<LineSegment>();
	}

	
	/**
	 * Constructor for a new whiteboard object
	 * @param title Name of the whiteboard
	 * @param canvas The canvas associated with the whiteboard
	 * @param users The list of users who are currently accessing this whiteboard
	 */
	public Whiteboard(String name, Canvas canvas, ArrayList<String> users) {
		this.name = name;
		this.canvas = canvas;
		this.users = users;
		this.lineSegments = new ArrayList<LineSegment>();
	}
	
    /**
     * Returns the name of the whiteboard.
     * @return the name of the whiteboard.
     */
    public String getName() {
    	return this.name;
    }

    /**
     * Sets the name of the whiteboard.
     * @param name The name to set the whiteboard to.
     */
    public void setName(String name) {
    	this.name = name;
    }
    
    /**
     * Returns the list of users currently in this whiteboard.
     * @return the list of users currently in this whiteboard.
     */
    public ArrayList<String> getUsers() {
    	return new ArrayList<String>(this.users);
    }
    
    /**
     * Adds user to the list of users
     * @param user User to add
     */
    public void addUser(String user) {
    	users.add(user);
    }

    /**
     * Removes user from the list of users
     * @param user User to remove
     * @return if removal was successful
     */
    public boolean removeUser(String user) {
        return users.remove(user);
    }
    
    /**
     * Adds a line segment to the whiteboard's canvas object.
     * @param lineSegment Line segment to add
     */
    public void addLineSegment(LineSegment lineSegment) {
    	this.lineSegments.add(lineSegment);
    	if (this.canvas != null) {
        	this.canvas.drawLineSegment(lineSegment);    		
    	}
    }
    
    public ArrayList<LineSegment> getLineSegments() {
    	return this.lineSegments;
    }
    
    public void display() {
		
                final JFrame window = new JFrame(name);
                    window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    window.setLayout(new BorderLayout());
                    window.setResizable(false);
                    window.setSize(907, 600); // A little buffering for division between
                    						  // list of users and canvas
                    
                    // Add toolbar
                    JToolBar toolbar = new JToolBar("Bar");
                	window.add(toolbar, BorderLayout.NORTH);
                    toolbar.setFloatable(false);
                    
                    // Toolbar buttons
                    // Color Picker
                    JButton colorButton = new JButton("Choose Color");
                	toolbar.add(colorButton);
                	colorButton.addActionListener(new ActionListener() {
            			public void actionPerformed(ActionEvent event) {
            				Color color = JColorChooser.showDialog(window, "Choose Background Color", Color.WHITE);
            				if (color != null) {
            					canvas.setColor(color);
            				}
            			}
                	});
                	
                	// Eraser Icon
            		ImageIcon eraserIcon = new ImageIcon("img/eraser.png");
                    JToggleButton eraserPicker = new JToggleButton("eraser", eraserIcon, false);
                	toolbar.add(eraserPicker);
                	eraserPicker.addActionListener(new ActionListener() {
                		public void actionPerformed(ActionEvent event) {
                			canvas.toggleEraserMode();
                    	}            	
            		});

                	JSlider strokeSlider = new JSlider(JSlider.HORIZONTAL, 1, 30, 5);
                	toolbar.add(strokeSlider);
                	strokeSlider.addChangeListener(new ChangeListener() {
                		public void stateChanged(ChangeEvent c) {
                			JSlider s = (JSlider) c.getSource();
                			canvas.setStrokeWidth(s.getValue());
                		}
                	});
                	
                	// Add canvas
                    window.add(canvas, BorderLayout.WEST);

                    // Add users list
                	String[] tableColumns = {"Guests"}; 
                	DefaultTableModel guessTableModel = new DefaultTableModel(tableColumns, 0);
            		JTable guessTable = new JTable(guessTableModel);
                	TableColumn column = guessTable.getColumnModel().getColumn(0);
                	column.setPreferredWidth(100);
            		guessTableModel.addRow(new Object[]{"Guests In Here"});
            		for (int i = 0; i < users.size(); i++) {
            			guessTableModel.addRow(new Object[]{users.get(i)});
            		}
            		window.add(guessTable, BorderLayout.EAST);
            		window.setVisible(true);
    		
    }
    
}
