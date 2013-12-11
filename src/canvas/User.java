package canvas;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

public class User {

	private Whiteboard whiteboard;
	private int thickness = 1;
	private final String username;
	private Color color = Color.BLACK;
	
	public User(String username) {
		this.username = username;
		this.whiteboard = whiteboard;
		this.thickness = thickness;
		this.color = color;
	}
	
	/**
	 * Set the color the user is using by rgb values.
	 * @param r int from 0-255; defines red component
	 * @param g int from 0-255; defines green component
	 * @param b int from 0-255; defines blue component
	 */
	public void setColor(int r, int g, int b) {
		color = new Color(r, g, b);
	}
	
	/**
	 * Enum of all possible colors (in uppercase) that the Java Colors 
	 * library supports
	 * @author pzarker
	 *
	 */
	public enum Colors {
		BLACK, BLUE, CYAN, DARK_GRAY, DARKGRAY, GRAY, LIGHT_GRAY, 
		LIGHTGRAY, GREEN, MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW
	}
	
	/**
	 * Set the color the user is using by color name.
	 * If the name the user picks is a valid color name, method returns true and updates
	 * the color the user is using. Otherwise, method returns false, and the user
	 * keeps the color he was using before.
	 * @param colorName
	 * @return boolean
	 */
	public boolean setColor(String colorName) {
		// check if colorName is a valid color name. If not, return false.
		List<String> colors = Arrays.asList("BLACK", "BLUE", "CYAN", "DARK_GRAY", "DARKGRAY", "GRAY",
											"LIGHT_GRAY", "LIGHTGRAY", "GREEN", "MAGENTA", "ORANGE",
											"PINK", "RED", "WHITE", "YELLOW");
		String colorNameUpper = colorName.toUpperCase();
		int isValidColor = colors.indexOf(colorNameUpper);
		if (isValidColor == -1) { 
			return false;
		}
		
		Colors colorEnum = Colors.valueOf(colorNameUpper);
		switch (colorEnum) {
			case BLACK: color = Color.BLACK; break;
			case BLUE: color = Color.BLUE; break;
			case CYAN: color = Color.CYAN; break;
			case DARK_GRAY: color = Color.DARK_GRAY; break;
			case DARKGRAY: color = Color.darkGray; break;
			case GRAY: color = Color.GRAY; break;
			case LIGHT_GRAY: color = Color.LIGHT_GRAY; break;
			case LIGHTGRAY: color = Color.lightGray; break;
			case GREEN: color = Color.GREEN; break;
			case MAGENTA: color = Color.MAGENTA; break;
			case ORANGE: color = Color.ORANGE; break;
			case PINK: color = Color.PINK; break;
			case RED: color = Color.RED; break;
			case WHITE: color = Color.WHITE; break;
			case YELLOW: color = Color.YELLOW; break;
			default: color = Color.BLACK; break;
		}
		return true;
	}
	
	/**
	 * Return an instance of color that represents the color the user is currently using.
	 * @return
	 */
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Set thickness of brush user is using.
	 * @param thick
	 */
	public void setThickness(int thick) {
		this.thickness = thick;
	}
	
	/**
	 * Return the int that represents the thickness of the brush.
	 * @return
	 */
	public int getThickness() {
		return this.thickness;
	}

	/**
	 * Sends a string in the form "add username [username]"
	 * @param newUsername
	 * @return String representing the message for adding a user by username
	 */
	public String addUsername(String newUsername) {
		return "add username " + newUsername;
	}
	
	/**
	 * Returns the username of THIS user 
	 * @return this.username
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Sends a string in the form 
	 * "draw whiteboard [WHITEBOARD NAME] [X1] [Y1] [X2] [Y2] [RED] [GREEN] [BLUE] [THICKNESS]"
	 * @param point1 start Point of line segment
	 * @param point2 end Point of line segment
	 * @return String representing the message for drawing a line segment
	 */
	public String sendLineSegment(Point point1, Point point2) {
		String name = this.whiteboard.getName();
		String pt1x = Integer.toString(point1.x);
		String pt1y = Integer.toString(point1.y);
		String pt2x = Integer.toString(point2.x);
		String pt2y = Integer.toString(point2.y);
		String red = Integer.toString(this.color.getRed());
		String green = Integer.toString(this.color.getGreen());
		String blue = Integer.toString(this.color.getBlue());
		String t = Integer.toString(this.thickness);
		
		return "draw whiteboard " + name + " " + pt1x + " " + pt1y + " " + pt2x + " " + pt2y + " " + red + " " + green
				+ " " + blue + " " + t;	
	}
	
	/**
	 * Sends a string in the form "create whiteboard [NAME]"
	 * @param board
	 * @return String representing the message for creating a whiteboard
	 */
	public String createWhiteboard(String whiteboardName) {
		return "create whiteboard " + whiteboardName;
	}
	
	/**
	 * Sends a string in the form "exit whiteboard [NAME]"
	 * @return String representing the message for exiting THIS whiteboard
	 */
	public String exitWhiteboard() {
		return "exit whiteboard " + this.whiteboard.getName();
	}
	
	/**
	 * Sends a string in the form "join whiteboard [NAME]"
	 * @param whiteboardName the name of the existing whiteboard
	 * @return String representing the message for joining an existing whiteboard
	 */
	public String joinWhiteboard(String whiteboardName) {
		return "join whiteboard " + whiteboardName;
	}
	
	public Whiteboard getWhiteboard() {
		return this.whiteboard;
	}
	
	public void setWhiteboard(Whiteboard board) {
		this.whiteboard = board;
	}
}
