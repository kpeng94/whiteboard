package canvas;

import java.awt.Color;
import java.awt.Point;

/**
 * This class represents a line segment that is drawn on the whiteboard. This class
 * will keep track of the endpoints of the line segment as well as the color so that 
 * each whiteboard can remember what it has drawn up until that point.
 */
public class LineSegment {
	private Point start;
	private Point end;
	private Color color;
	private int strokeSize;
	
	/**
	 * The constructor for the LineSegment class.
	 * @param x1 - the x coordinate of the first endpoint
	 * @param y1 - the y coordinate of the first endpoint
	 * @param x2 - the x coordinate for the second endpoint
	 * @param y2 - the y coordinate for the second endpoint
	 * @param lineColor - the color that the line is drawn in
	 * @param lineStrokeSize - the size of the stroke
	 */
	public LineSegment(int x1, int y1, int x2, int y2, Color lineColor, int lineStrokeSize) {
		start = new Point(x1, y1);
		end = new Point(x2, y2);
		color = lineColor;
		strokeSize = lineStrokeSize;
	}
	
	/** 
	 * Returns a Point object with the x and y coordinates of the beginning of the line segment.
	 * @return the beginning point
	 */
	public Point getStartPoint(){
		return new Point(start);
	}
	
	/**
	 * Returns a Point object with the x and y coordinates of the end of the line segment.
	 * @return the ending point
	 */
	public Point getEndPoint(){
		return new Point(end);
	}
	
	/**
	 * Returns the color that the line segment is drawn in.
	 * @return color of line segment
	 */
	public Color getColor(){
		return color;
	}
		
	/**
	 * Returns the size of the stroke
	 * @return size of the stroke
	 */
	public int getStrokeSize() {
		return strokeSize;
	}
	
	/**
	 * Returns a String representation of the line segment
	 */
	public String toString(){
		return start.x + " " + start.y + " " + end.x + " " + end.y + " " + color.getRed() + " "
				+ color.getGreen() + " " + color.getBlue() + " " + strokeSize;
	}
}
