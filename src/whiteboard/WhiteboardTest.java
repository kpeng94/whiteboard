package whiteboard;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

import client.Canvas;

/**
 * Tests that methods of the whiteboard ADT function correctly.
 * Methods to test: getName(), addUsers(), getUsers(), removeUser(), setUsers(), addLineSegments(), getLineSegment(), display()?
 * Types of strings to test: empty string, special characters, normal names
 *
 */
public class WhiteboardTest {
	// test default values for getName() and getUsers()
	@Test
	public void testGetName() {
		int width = 800; 
		int height = 600;
		Canvas canvas = new Canvas(width, height);
		Whiteboard whiteboard = new Whiteboard("testName", canvas);
		assertEquals("testName", whiteboard.getName());
		assertTrue(whiteboard.getUsers().equals(new HashSet<String>()));
	}
	
	// test adding multiple users using ascii characters and spaces
	@Test
	public void testAddGetUsers() {
		int width = 800; 
		int height = 600;
		Canvas canvas = new Canvas(width, height);
		Whiteboard whiteboard = new Whiteboard("", canvas);
		whiteboard.addUser("genghis");
		whiteboard.addUser("kevin");
		whiteboard.addUser("");
		whiteboard.addUser(" ");
		whiteboard.addUser("@%#%");
		
		HashSet<String> result = new HashSet<String>();
		result.add("genghis");
		result.add("");
		result.add(" ");
		result.add("kevin");
		result.add("@%#%");
		
		assertTrue(result.equals(whiteboard.getUsers()));
	}
	
	// test removing users
	@Test
	public void testRemoveUsers() {
		int width = 800; 
		int height = 600;
		Canvas canvas = new Canvas(width, height);
		Whiteboard whiteboard = new Whiteboard("", canvas);
		whiteboard.addUser("genghis");
		whiteboard.addUser("kevin");
		whiteboard.addUser("");
		whiteboard.addUser(" ");
		whiteboard.removeUser("kevin");
		whiteboard.removeUser("");
		
		HashSet<String> result = new HashSet<String>();
		result.add("genghis");
		result.add(" ");
		
		assertTrue(result.equals(whiteboard.getUsers()));
	}
	
	// test removing a user that isn't in User hashSet
		@Test
		public void testRemoveUsersFailure() {
			int width = 800; 
			int height = 600;
			Canvas canvas = new Canvas(width, height);
			Whiteboard whiteboard = new Whiteboard("", canvas);
			whiteboard.addUser("genghis");
			whiteboard.addUser("kevin");
			whiteboard.addUser(" ");
			boolean failure = whiteboard.removeUser("whatever");
			assertFalse(failure);
			
			HashSet<String> result = new HashSet<String>();
			result.add("genghis");
			result.add(" ");
			result.add("kevin");
			
			assertTrue(result.equals(whiteboard.getUsers()));
		}
	
	// test setting users
	// TODO determine if setUsers() adds to existing HashSet or replaces it
	@Test
	public void testSetUsers() {
		int width = 800; 
		int height = 600;
		Canvas canvas = new Canvas(width, height);
		Whiteboard whiteboard = new Whiteboard("", canvas);
		whiteboard.addUser("me!");
		HashSet<String> newUsers = new HashSet<String>();
		newUsers.add("psyche!");
		newUsers.add("ok");
		whiteboard.setUsers(newUsers);
		newUsers.add("me!");
		assertTrue(newUsers.equals(whiteboard.getUsers()));
	}
	
	// test what happens when adding a user that already exists in HashSet
	@Test
	public void testAddUserExistingEntry() {
		int width = 800; 
		int height = 600;
		Canvas canvas = new Canvas(width, height);
		Whiteboard whiteboard = new Whiteboard("", canvas);
		whiteboard.addUser("parker");
		boolean success = whiteboard.addUser("parker");
		assertFalse(success);
		HashSet<String> result = new HashSet<String>();
		result.add("parker");
		assertTrue(result.equals(whiteboard.getUsers()));
	}
	
	// tests adding and getting a line segment
	@Test
	public void testAddGetLineSegment() {
		Canvas canvas = null;
		Whiteboard whiteboard = new Whiteboard("", canvas);
		LineSegment lineSegment = new LineSegment(0, 0, 1, 0, new Color(0, 0, 0), 1);
		whiteboard.addLineSegment(lineSegment);
		ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
		lineSegments.add(lineSegment);
		assertEquals(lineSegments, whiteboard.getLineSegments());
		
	}

}
