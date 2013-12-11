package canvas;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Tests that methods of the whiteboard ADT function correctly.
 * Tests: getName(), setName(), addUsers(), getUsers(), removeUser()
 * Types of strings to test: empty string, special characters, normal names
 *
 */
public class whiteboardTest {
	@Test
	public void testGetSetName() {
		int width = 800; int height = 600;
		Canvas canvas = new Canvas(width, height);
		ArrayList<String> users = new ArrayList<String>();
		Whiteboard whiteboard = new Whiteboard("", canvas, users);
		assertEquals("", whiteboard.getName());
		
		whiteboard.setName("name");
		assertEquals("name", whiteboard.getName());
		
		whiteboard.setName(" ");
		assertEquals(" ", whiteboard.getName());
	}
	
	@Test
	public void testAddGetUsers() {
		int width = 800; int height = 600;
		Canvas canvas = new Canvas(width, height);
		ArrayList<String> users = new ArrayList<String>();
		Whiteboard whiteboard = new Whiteboard("", canvas, users);
		whiteboard.addUser("genghis");
		whiteboard.addUser("kevin");
		whiteboard.addUser("");
		whiteboard.addUser(" ");
		whiteboard.removeUser("kevin");
		
		ArrayList<String> result = new ArrayList<String>();
		result.add("genghis");
		result.add("");
		result.add(" ");
		assertEquals(result, whiteboard.getUsers());
	}
}
