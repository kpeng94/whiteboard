package client;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests constructor and methods for the User ADT.
 * Methods include: getUsername, addWhiteboard, getWhiteboard, removeWhiteboard
 * Test that the following methods return the following strings:
 * public String getUsername() {
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
 *
 */
public class UserTest {
	@Test
	public void testDefaultValues() {
		User user = new User("username");
		assertEquals(user.getUsername(), "username");
	}
}
