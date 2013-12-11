package client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import whiteboard.Whiteboard;

/**
 * Tests constructor and methods for the User ADT.
 * Methods include: getUsername, addWhiteboard, getWhiteboard, removeWhiteboard
 * Tests the functionality for each of those methods.
 */
public class UserTest {
	
	/**
	 * Tests that getUsername() method returns correct username
	 */
	@Test
	public void testDefaultValues() {
		User user = new User("username");
		assertEquals(user.getUsername(), "username");
	}
	
	/**
	 * Tests that whiteboard does not exist when first initialized
	 */
	@Test 
	public void testWhiteboardDoesNotExistOnStartup() {
		User user = new User("username");
		Whiteboard whiteboard = user.getWhiteboard("nonexistentwhiteboard");
		assertEquals(whiteboard == null, true);
	}
	
	/**
	 * Tests that whiteboard can be properly added and retrieved
	 */
	@Test
	public void testAddWhiteboardExists() {
		User user = new User("username");
		Whiteboard whiteboard = new Whiteboard("whiteboard");
		user.addWhiteboard(whiteboard);
		assertEquals(user.getWhiteboard("whiteboard") != null, true);
	}

	/**
	 * Tests that the whiteboard can be properly removed.
	 */
	@Test
	public void testRemoveWhiteboard() {
		User user = new User("username");
		Whiteboard whiteboard = new Whiteboard("whiteboard");
		user.addWhiteboard(whiteboard);
		user.removeWhiteboard("whiteboard");
		assertEquals(user.getWhiteboard("whiteboard") == null, true);
	}

}
