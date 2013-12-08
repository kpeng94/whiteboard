package canvas;

import java.awt.Color;
import java.awt.Point;

public class User {

	private Whiteboard whiteboard;
	private int thickness;
	private final String username;
	private Color color;
	
	public User(String username) {
		this.username = username;
		this.whiteboard = whiteboard;
		this.thickness = thickness;
		this.color = color;
		
	}
	// We could have more ways to set the color
	// Could play around with brighter() and darker() methods of Color
	public void setColor(float h, float s, float b) {
		color = Color.getHSBColor(h, s, b);
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void setThickness(int thick) {
		this.thickness = thick;
	}
	
	public int getThickness() {
		return this.thickness;
	}
	
	public void sendLineSegment(Point point1, Point point2, Color c, int thick) {
		//send the line segment message
	}
	
	public void switchWhiteboard(Whiteboard board) {
		this.whiteboard = board;
	}
	
	public void createWhiteboard(Whiteboard board) {
		//create whiteboard
	}
	
	public void disconnectWhiteboard() {
		//disconnect from existing whiteboard, and create prompt to join another
	}
	
	
	
	
}
