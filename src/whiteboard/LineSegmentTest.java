package whiteboard;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Point;

import org.junit.Test;

/**
 * Test LineSegment's constructor and 4 methods: getStartPoint(), getEndPoint(), getColor(), getStrokeSize(), toString()
 * Tests positive and negative coordinates, when end points are less than start points
 * Tests IllegalArgumentException for color
 *
 */

public class LineSegmentTest {
	// test for getting the starting point of a line segment- 
	// should return first two arguments as a point
	@Test
	public void testGetStartPoint() {
		LineSegment lineSegment = new LineSegment(0, 0, 1, 1, new Color(10, 10, 10), 3);
		assertEquals(lineSegment.getStartPoint(), new Point(0, 0));
	}
	
	// test for getting the end point of a line segment-
	// should return the third and fourth arguments as a point
	@Test
	public void testGetEndPointNegative() {
		LineSegment lineSegment = new LineSegment(-10, -11, -1, -10, new Color(10, 10, 10), 3);
		assertEquals(lineSegment.getEndPoint(), new Point(-1, -10));
	}
	
	// end point test- if end points are on the lower left hand corner
	@Test
	public void testGetEndPointLess() {
		LineSegment lineSegment = new LineSegment(0, 0, -1, -10, new Color(10, 10, 10), 3);
		assertEquals(lineSegment.getEndPoint(), new Point(-1, -10));
	}
	
	// tests if the right color is returned (5th arguments)
	@Test
	public void testGetColor() {
		LineSegment lineSegment = new LineSegment(0, 0, -1, -10, new Color(10, 10, 10), 3);
		assertEquals(lineSegment.getColor(), new Color(10, 10, 10));
	}
	
	// tests if an Illegal Argument Exception is thrown if color gets an invalid argument
	@Test (expected = IllegalArgumentException.class)
	public void testGetColorNegative() {
		LineSegment lineSegment = new LineSegment(0, 0, -1, -10, new Color(-5, 10, 10), 3);
		assertEquals(lineSegment.getColor(), new Color(-5, 10, 10));
	}
	
	// tests if right stroke size is returned (6th argument)
	@Test
	public void testGetStrokeSize() {
		LineSegment lineSegment = new LineSegment(0, 0, -1, -10, new Color(0, 10, 10), 3);
		assertEquals(lineSegment.getStrokeSize(), 3);
	}
	
	// tests toString method
	@Test
	public void testToString() {
		LineSegment lineSegment = new LineSegment(0, 0, -1, -10, new Color(0, 10, 10), 3);
		String result = "0 0 -1 -10 0 10 10 3";
		assertEquals(result, lineSegment.toString());
	}
	
	// tests toString method with different arguments
	@Test
	public void testToString2() {
		LineSegment lineSegment = new LineSegment(-16, 0, 10000, -10, new Color(255, 10, 10), -2);
		String result = "-16 0 10000 -10 255 10 10 -2";
		assertEquals(result, lineSegment.toString());
	}
}
