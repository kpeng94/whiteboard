package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import whiteboard.LineSegment;

/**
 * Canvas represents a drawing surface that allows the user to draw
 * on it freehand, with the mouse.
 */
@SuppressWarnings("serial")
public class Canvas extends JPanel {
    // image where the user's drawing is stored
    private Image drawingBuffer;
    private Color color;
    private int strokeWidth;
    private final WhiteboardClient whiteboardClient;
    private boolean isEraserModeOn = false;
    private String name;
    
    /**
     * Constructor for a canvas for a client.
     * 
     * @param width width in pixels
     * @param height height in pixels
     * @param whiteboardClient client associated with this canvas
     * @param name name of the canvas
     */
    public Canvas(int width, int height, WhiteboardClient whiteboardClient, String name) {
    	this.color = Color.BLACK;
    	this.strokeWidth = 3;
        this.setPreferredSize(new Dimension(width, height));
        this.whiteboardClient = whiteboardClient;
        this.name = name;
        addDrawingController();
        // note: we can't call makeDrawingBuffer here, because it only
        // works *after* this canvas has been added to a window.  Have to
        // wait until paintComponent() is first called.
    }

    /**
     * Sets the color associated with this canvas.
     * This shouldn't be a problem since a canvas should only be associated with one user.
     * @param color Color to set to
     */
    public void setColor(Color color) {
    	this.color = color;
    }
        
    /**
     * Gets the color associated with this canvas
     * @return color associated with this canvas
     */
    public Color getColor() {
    	return this.color;
    }
    
    /**
     * Toggles the state of the eraser / pen.
     */
    public void toggleEraserMode() {
    	this.isEraserModeOn = !this.isEraserModeOn;
    }
    
    /**
     * Sets the stroke width associated with this canvas
     * @param strokeWidth strokeWidth associated with this canvas
     */
    public void setStrokeWidth(int strokeWidth) {
    	this.strokeWidth = strokeWidth;
    }
   
    /**
     * Gets the stroke width associated with this canvas    
     * @return stroke width associated with this canvas
     */
    public int getStrokeWidth() {
    	return this.strokeWidth;
    }
    
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        // If this is the first time paintComponent() is being called,
        // make our drawing buffer.
        if (this.drawingBuffer == null) {
            makeDrawingBuffer();
        }
        
        // Copy the drawing buffer to the screen.
        g.drawImage(this.drawingBuffer, 0, 0, null);
    }
    
    /**
     * Make the drawing buffer and draw some starting content for it.
     */
    private void makeDrawingBuffer() {
        this.drawingBuffer = createImage(getWidth(), getHeight());
        fillWithWhite();
    }
    
    /**
     * Make the drawing buffer entirely white.
     */
    private void fillWithWhite() {
        final Graphics2D g = (Graphics2D) this.drawingBuffer.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0,  0,  getWidth(), getHeight());
        
        // IMPORTANT!  every time we draw on the internal drawing buffer, we
        // have to notify Swing to repaint this component on the screen.
        this.repaint();
    }    
    
    /**
     * Draws the given line segment on to the screen.
     * 
     * @param lineSegment Line segment to draw onto the screen
     */
    public void drawLineSegment(LineSegment lineSegment) {
    	if (this.drawingBuffer == null) {
    		makeDrawingBuffer();
    	}
    	
        final Graphics2D g = (Graphics2D) this.drawingBuffer.getGraphics();
        g.setColor(lineSegment.getColor());
        g.setStroke(new BasicStroke(lineSegment.getStrokeSize(), 
        							BasicStroke.CAP_ROUND, 
        							BasicStroke.JOIN_ROUND));
        g.drawLine(lineSegment.getStartPoint().x, lineSegment.getStartPoint().y,
        		   lineSegment.getEndPoint().x, lineSegment.getEndPoint().y);
        // IMPORTANT!  every time we draw on the internal drawing buffer, we
        // have to notify Swing to repaint this component on the screen.
        this.repaint();      
    }
    
    /**
     * Sends a message from the client to the server requesting to draw a
     * line from (x1, y1) to (x2, y2) using the selected color and stroke
     * width.
     */
    public void sendLineSegmentToServer(int x1, int y1, int x2, int y2) {        
    	Color color;
    	if (this.isEraserModeOn) {
        	color = Color.WHITE;
        } else {        	
            color = this.color;
        }
        whiteboardClient.sendDrawMessage(this.name, x1, y1, x2, y2, color.getRed(), 
        								 color.getGreen(), color.getBlue(), 
        								 this.strokeWidth);
    }
    
    /**
     * Sends a disconnect message to the server.
     */
    public void sendDisconnectToServer(){
    	whiteboardClient.sendExitWhiteboardMessage(this.name);
    }
    
    /**
     * Add the mouse listener that supports the user's freehand drawing.
     */
    private void addDrawingController() {
        DrawingController controller = new DrawingController();
        addMouseListener(controller);
        addMouseMotionListener(controller);
    }
    
    /**
     * DrawingController handles the user's freehand drawing.
     */
    private class DrawingController implements MouseListener, MouseMotionListener {
        // store the coordinates of the last mouse event, so we can
        // draw a line segment from that last point to the point of the next mouse event.
        private int lastX, lastY;
        /**
         * When mouse button is pressed down, start drawing.
         */
        public void mousePressed(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();
        }

        /**
         * When mouse moves while a button is pressed down,
         * draw a line segment.
         */
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            sendLineSegmentToServer(lastX, lastY, x, y);
            lastX = x;
            lastY = y;
        }

        // Ignore all these other mouse events.
        public void mouseMoved(MouseEvent e) { }
        public void mouseClicked(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
    }
}