package client;

import java.util.HashMap;

import whiteboard.Whiteboard;

/**
 * User is the abstract datatype that represents a client.
 * User is set to null before a client connects, and is initialized when a client types 
 * 		in a username that has not already been taken by another user.
 * User contains information about the boards it is currently connected to (has opened), 
 * 		and its username (which is a unique identifier).
 * 
 * Rep Invariant: 
 * - none of the Whiteboards in whiteboards is null.
 * - username cannot be null, and cannot change after initialization (final and immutable)
 */
public class User {

	private HashMap<String, Whiteboard> whiteboards;
	private final String username;
	
	public void checkRep() {
		for (Whiteboard board : whiteboards.values()) {
			assert board != null;
		}
		assert username != null;
	}
	
	/**
	 * Constructor for user.
	 * @param username Username for the user
	 */
	public User(String username) {
		this.username = username;
		this.whiteboards = new HashMap<String, Whiteboard>();
		this.checkRep();
	}
	
	/**
	 * Returns the username of this user 
	 * @return username of this user
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Adds a whiteboard for this user.
	 * @param whiteboard Whiteboard to add
	 */
	public void addWhiteboard(Whiteboard whiteboard) {
		this.whiteboards.put(whiteboard.getName(), whiteboard);
	}
	
	/**
	 * Gets the whiteboard by name.
	 * @param name Name of whiteboard to get.
	 * @return whiteboard with given name.
	 */
	public Whiteboard getWhiteboard(String name) {
		return this.whiteboards.get(name);
	}
	
	/**
	 * Removes a whiteboard from this user's list.
	 * @param name Name of whiteboard to remove.
	 */
	public void removeWhiteboard(String name) {
		this.whiteboards.remove(name);
	}
}