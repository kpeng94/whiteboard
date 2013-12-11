package client;

import java.util.HashMap;
import canvas.Whiteboard;

public class User {

	private HashMap<String, Whiteboard> whiteboards;
	private final String username;
	
	/**
	 * Constructor for user.
	 * @param username Username for the user
	 */
	public User(String username) {
		this.username = username;
		this.whiteboards = new HashMap<String, Whiteboard>();
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
	public Whiteboard getWhiteboard(String name){
		return this.whiteboards.get(name);
	}
	/**
	 * Removes a whiteboard from this user's list.
	 * @param name Name of whiteboard to remove.
	 */
	public void removeWhiteboard(String name){
		this.whiteboards.remove(name);
	}
}
