package client;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

/**
 * Tests constructor and methods for the User ADT.
 * Methods include: setColor (both methods), getColor, setThickness, getThickness, addUsername, getUsername, 
 * sendLineSegment, createWhiteboard, exitWhiteboard, joinWhiteboard
 * Test that the following methods return the following strings:
 * addUsername(String newUsername): returns "add username [newUsername]"
 * sendLineSegment(Point point1, Point point2): returns "draw whiteboard [whiteboard name] [point1.x] [point1.y]
 * 												[point2.x] [point2.y] [user.getColor().getRed()] [user.getColor().getGreen()]
 * 												[user.getColor().getBlue()] [user.getThickness()]
 * createWhiteboard(String whiteboardName): returns "create whiteboard [whiteboardName]"
 * exitWhiteboard(): returns "exit whiteboard [this.whiteboard.getName()]" -> whiteboard is initialized with a name
 * joinWhiteboard(String whiteboardName): returns "join whiteboard [whiteboardName]"
 * Also make  
 *
 */
public class UserTest {
	@Test
	public void testDefaultValues() {
		User user = new User("username");
		assertEquals(user.getColor(), Color.BLACK);
		assertEquals(user.getThickness(), 1);
		assertEquals(user.getUsername(), "username");
	}
	
	@Test
	public void testGetSetColor() {
		User user = new User("username");
		user.setColor("darkgray");
		assertEquals(user.getColor(), Color.darkGray);
		
		user.setColor("dark_gray");
		assertEquals(user.getColor(), Color.DARK_GRAY);

		user.setColor(255, 100, 0);
		assertEquals(user.getColor(), new Color(255, 100, 0));
	}
	
	// tests the range of possible thickness values (min int, neg value, 0, pos value, max int)
	@Test
	public void testGetSetThickness() {
		User user = new User("username");
		assertEquals(user.getThickness(), 1);
		
		user.setThickness(100);
		assertEquals(user.getThickness(), 100);
		
		user.setThickness(-100);
		assertEquals(user.getThickness(), -100);
		
		user.setThickness(Integer.MAX_VALUE);
		assertEquals(user.getThickness(), Integer.MAX_VALUE);
		
		user.setThickness(Integer.MIN_VALUE);
		assertEquals(user.getThickness(), Integer.MIN_VALUE);
		
		user.setThickness(0);
		assertEquals(user.getThickness(), 0);
	}
	
	
}
