package canvas;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Point;

import org.junit.Test;

/**
 * Test LineSegment's constructor and 4 methods: getStartPoint(), getEndPoint(), getColor(), getStrokeSize()
 * Tests positive and negative coordinates, when end points are less than start points
 * Tests IllegalArgumentException for color
 *
 */

public class LineSegmentTest {
	@Test
	public void testGetStartPoint() {
		LineSegment lineSegment = new LineSegment(0, 0, 1, 1, new Color(10, 10, 10), 3);
		assertEquals(lineSegment.getStartPoint(), new Point(0, 0));
	}
	
	@Test
	public void testGetEndPointNegative() {
		LineSegment lineSegment = new LineSegment(0, 0, -1, -10, new Color(10, 10, 10), 3);
		assertEquals(lineSegment.getEndPoint(), new Point(-1, -10));
	}
	
	@Test
	public void testGetEndPointLess() {
		LineSegment lineSegment = new LineSegment(0, 0, -1, -10, new Color(10, 10, 10), 3);
		assertEquals(lineSegment.getEndPoint(), new Point(-1, -10));
	}
	
	@Test
	public void testGetColor() {
		LineSegment lineSegment = new LineSegment(0, 0, -1, -10, new Color(10, 10, 10), 3);
		assertEquals(lineSegment.getColor(), new Color(10, 10, 10));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testGetColorNegative() {
		LineSegment lineSegment = new LineSegment(0, 0, -1, -10, new Color(-5, 10, 10), 3);
		assertEquals(lineSegment.getColor(), new Color(-5, 10, 10));
	}
	
	@Test
	public void testGetStrokeSize() {
		LineSegment lineSegment = new LineSegment(0, 0, -1, -10, new Color(0, 10, 10), 3);
		assertEquals(lineSegment.getStrokeSize(), 3);
	}
}
