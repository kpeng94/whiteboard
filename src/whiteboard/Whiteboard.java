package whiteboard;

import java.util.ArrayList;
import java.util.HashSet;

import client.Canvas;
import client.WhiteboardGUI;

/**
 * TODO: ADT, RI
 *
 */
public class Whiteboard {
	private final String name;
	private final HashSet<String> users;
	private final ArrayList<LineSegment> lineSegments;
	// Only applicable for client whiteboards
	private final Canvas canvas;
	private WhiteboardGUI gui;

	/**
	 * Constructor for a new whiteboard object
	 * @param title Name of the whiteboard
	 * @param canvas The canvas associated with the whiteboard
	 * @param users The list of users who are currently accessing this whiteboard
	 */
	public Whiteboard(String name) {
		this.name = name;
		this.users = new HashSet<String>();
		this.canvas = null;
		this.gui = null;
		this.lineSegments = new ArrayList<LineSegment>();
	}
	
	/**
	 * Constructor for a new whiteboard object
	 * @param title Name of the whiteboard
	 * @param canvas The canvas associated with the whiteboard
	 * @param users The list of users who are currently accessing this whiteboard
	 */
	public Whiteboard(String name, Canvas canvas) {
		this.name = name;
		this.canvas = canvas;
		this.users = new HashSet<String>();
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
	 * Returns the list of users currently in this whiteboard.
	 * @return the list of users currently in this whiteboard.
	 */
	public HashSet<String> getUsers() {
		return new HashSet<String>(this.users);
	}

	// TODO setUsers doesn't replace list of users; adds to it (which one do we want?)
	/**
	 * Replaces users of whiteboard with new list of users
	 */
	public void setUsers(HashSet<String> newUsers) {
		for(String user: newUsers){
			addUser(user);
		}
	}

	/**
	 * Adds user to the list of users
	 * @param user User to add
	 */
	public boolean addUser(String user) {
		boolean success = users.add(user);
		if(success && gui != null){
			gui.addUserToModel(user);
		}
		return success;
	}

	/**
	 * Removes user from the list of users
	 * @param user User to remove
	 * @return if removal was successful
	 */
	public boolean removeUser(String user) {
		boolean success = users.remove(user);
		if(success && gui != null){
			gui.removeUserFromModel(user);
		}
		return success;
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

	/**
	 * Gets the list of line segments on this whiteboard.
	 * @return the list of line segments on this whiteboard.
	 */
	public ArrayList<LineSegment> getLineSegments() {
		return this.lineSegments;
	}

	/**
	 * Displays the whiteboard.
	 */
	public void display() {
		gui = new WhiteboardGUI(canvas, name);
	}
}
