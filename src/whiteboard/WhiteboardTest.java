package whiteboard;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

import client.Canvas;

/**
 * Tests that methods of the whiteboard ADT function correctly.
 * Tests: getName(), setName(), addUsers(), getUsers(), removeUser()
 * Types of strings to test: empty string, special characters, normal names
 *
 */
public class WhiteboardTest {
	@Test
	public void testGetName() {
		int width = 800; int height = 600;
		Canvas canvas = new Canvas(width, height);
		Whiteboard whiteboard = new Whiteboard("testName", canvas);
		assertEquals("testName", whiteboard.getName());
	}
	
	@Test
	public void testAddGetUsers() {
		int width = 800; int height = 600;
		Canvas canvas = new Canvas(width, height);
		Whiteboard whiteboard = new Whiteboard("", canvas);
		whiteboard.addUser("genghis");
		whiteboard.addUser("kevin");
		whiteboard.addUser("");
		whiteboard.addUser(" ");
		whiteboard.removeUser("kevin");
		
		HashSet<String> result = new HashSet<String>();
		result.add("genghis");
		result.add("");
		result.add(" ");
		assertEquals(result, whiteboard.getUsers());
	}
	
	public void isHashSetEqual() {
		
	}
}
