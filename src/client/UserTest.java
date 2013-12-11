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
		assertEquals(user.getUsername(), "username");
	}
}
