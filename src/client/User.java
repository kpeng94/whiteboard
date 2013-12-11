package client;

import java.util.HashMap;
import canvas.Whiteboard;

public class User {

	private HashMap<String, Whiteboard> whiteboard;
	private final String username;
	
	public User(String username) {
		this.username = username;
		this.whiteboard = new HashMap<String, Whiteboard>();
	}
	
	/**
	 * Returns the username of this user 
	 * @return username
	 */
	public String getUsername() {
		return this.username;
	}
	
	public void addWhiteboard(Whiteboard whiteboard) {
		this.whiteboard.put(whiteboard.getName(), whiteboard);
	}
	
	public Whiteboard getWhiteboard(String name){
		return this.whiteboard.get(name);
	}
	
	public void removeWhiteboard(String name){
		this.whiteboard.remove(name);
	}
}
