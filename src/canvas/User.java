package canvas;

import java.awt.Color;
import java.awt.Point;

public class User {

	private Whiteboard whiteboard;
	private int thickness = 1;
	private final String username;
	private Color color = Color.black;
	
	public User(String username) {
		this.username = username;
		this.whiteboard = whiteboard;
		this.thickness = thickness;
		this.color = color;
		
	}
	// We could have more ways to set the color
	// Could play around with brighter() and darker() methods of Color
	public void setColor(int r, int g, int b) {
		color = new Color(r, g, b);
	}
	
	public void setColor(String colorName) {
		//set color based on color names:
		//black, magenta, white, etc.
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
	
	// returns a String of form "draw [int] [int] [int] [int] from [int] [int] to [int] [int]"
	public String sendLineSegment(Point point1, Point point2, Color c, int thick) {
		String red = Integer.toString(c.getRed());
		String green = Integer.toString(c.getGreen());
		String blue = Integer.toString(c.getBlue());
		String t = Integer.toString(thick);
		String pt1x = Integer.toString(point1.x);
		String pt1y = Integer.toString(point1.y);
		String pt2x = Integer.toString(point2.x);
		String pt2y = Integer.toString(point2.y);
		
		return "draw " + red + " " + green + " " + blue + " " + t + " from " + pt1x + " " + pt1y 
				+ " " + pt2x + " " + pt2y;
		
	}
	
	public void switchWhiteboard(Whiteboard board) {
		//disconnect + join
	}
	
	public void createWhiteboard(Whiteboard board) {
		//create whiteboard
	}
	
	public void disconnectWhiteboard() {
		//disconnect from existing whiteboard, and create prompt to join another
	}
	
	
	
	
}
