package canvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

public class Whiteboard {
	private String name;
	private Canvas canvas;
	private ArrayList<String> users;
	private ArrayList<LineSegment> segments;
	
	/**
	 * Constructor for a whiteboard object
	 * @param title Name of the whiteboard
	 * 		TODO: Currently, this is the name of the window, do we want that? 
	 * 		      We would have to make it changeable later as well if we were to do that.
	 * @param canvas The canvas associated with the whiteboard
	 * @param users The list of users who are currently accessing this whiteboard
	 */
	public Whiteboard(String name, Canvas canvas, ArrayList<String> users) {
		this.name = name;
		this.canvas = canvas;
		this.users = users;
		this.segments = new ArrayList<LineSegment>();
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
     */
    public void removeUser(String user) {
        users.remove(user);
    }
    
    /**
     * 
     * @param lineSegment
     */
    public void addLineSegment(LineSegment lineSegment) {
//    	this.canvas.setColor(lineSegment.getColor());
//    	this.canvas.setStrokeWidth(lineSegment.getStrokeSize());
//    	this.canvas.drawLineSegment();
    }
    
	/**
	 * Main method, which generates the GUI.
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
//				Whiteboard whiteboard = new Whiteboard("Whiteboard", new Canvas(800, 600), 
//													   new ArrayList<String>());
//                whiteboard.pack();
//
//				whiteboard.setVisible(true);
			}
		});
	}	
}
