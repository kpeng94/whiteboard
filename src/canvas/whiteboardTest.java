package canvas;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class whiteboardTest {
	@Test
	public void testGetSetName() {
		String name = "";
		int width = 800; int height = 600;
		Canvas canvas = new Canvas(width, height);
		ArrayList<String> users = new ArrayList<String>();
		Whiteboard whiteboard = new Whiteboard(name, canvas, users);
		assertEquals("", whiteboard.getName());
		
		whiteboard.setName("name");
		assertEquals("name", whiteboard.getName());
		
		whiteboard.setName(" ");
		assertEquals(" ", whiteboard.getName());
	}
	
	@Test
	public void testAddGetUsers() {
		
	}
}
