package canvas;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

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
    private User user;
    private boolean isEraserModeOn;
    
    /**
     * Make a canvas.
     * @param width width in pixels
     * @param height height in pixels
     */
    public Canvas(int width, int height) {
    	this.color = Color.BLACK;
    	this.strokeWidth = 3;
        this.setPreferredSize(new Dimension(width, height));
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
     * 
     */
    public void toggleEraserMode() {
    	isEraserModeOn = !isEraserModeOn;
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
        if (drawingBuffer == null) {
            makeDrawingBuffer();
        }
        
        // Copy the drawing buffer to the screen.
        g.drawImage(drawingBuffer, 0, 0, null);
    }
    
    /*
     * Make the drawing buffer and draw some starting content for it.
     */
    private void makeDrawingBuffer() {
        drawingBuffer = createImage(getWidth(), getHeight());
        fillWithWhite();
    }
    
    /*
     * Make the drawing buffer entirely white.
     */
    private void fillWithWhite() {
        final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0,  0,  getWidth(), getHeight());
        
        // IMPORTANT!  every time we draw on the internal drawing buffer, we
        // have to notify Swing to repaint this component on the screen.
        this.repaint();
    }    
    
    /*
     * Draw a line between two points (x1, y1) and (x2, y2), specified in
     * pixels relative to the upper-left corner of the drawing buffer.
     */
    public LineSegment drawLineSegment(int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
        
        g.setStroke(new BasicStroke(this.strokeWidth));
        if (isEraserModeOn) {
        	g.setColor(Color.WHITE);
        } else {        	
            g.setColor(this.color);
        }
        g.drawLine(x1, y1, x2, y2);
        LineSegment lineSegment = new LineSegment(x1, y1, x2, y2, this.color, this.strokeWidth);
        
        // IMPORTANT!  every time we draw on the internal drawing buffer, we
        // have to notify Swing to repaint this component on the screen.
        this.repaint();
        return lineSegment;
    }
    
    /*
     * Add the mouse listener that supports the user's freehand drawing.
     */
    private void addDrawingController() {
        DrawingController controller = new DrawingController();
        addMouseListener(controller);
        addMouseMotionListener(controller);
    }
    
    
    /*
     * DrawingController handles the user's freehand drawing.
     */
    private class DrawingController implements MouseListener, MouseMotionListener {
        // store the coordinates of the last mouse event, so we can
        // draw a line segment from that last point to the point of the next mouse event.
        private int lastX, lastY;
        private Color color;
        
        /*
         * When mouse button is pressed down, start drawing.
         */
        public void mousePressed(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();
        }

        /*
         * When mouse moves while a button is pressed down,
         * draw a line segment.
         */
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            drawLineSegment(lastX, lastY, x, y);
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
    
 
    public void displayListOfGuests(ArrayList<String> guests, JFrame window) {
    	String[] tableColumns = {"Guests"}; 
    	DefaultTableModel guessTableModel = new DefaultTableModel(tableColumns, 0);
		JTable guessTable = new JTable(guessTableModel);
    	TableColumn column = guessTable.getColumnModel().getColumn(0);
    	column.setPreferredWidth(100);
		guessTableModel.addRow(new Object[]{"Guests In Here"});
		for (int i = 0; i < guests.size(); i++) {
			guessTableModel.addRow(new Object[]{guests.get(i)});
		}
		window.add(guessTable, BorderLayout.EAST);
    }
    
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				// Set up window
                final JFrame window = new JFrame("Freehand Canvas");
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setLayout(new BorderLayout());
                window.setResizable(false);
                window.setLayout(new BorderLayout());
                window.setSize(907, 600); // A little buffering for division between
                						  // list of users and canvas
                
                // Add toolbar
                JToolBar toolbar = new JToolBar("Bar");
            	window.add(toolbar, BorderLayout.NORTH);
                toolbar.setFloatable(false);
                final Canvas canvas = new Canvas(800, 600);
                
                // Toolbar buttons
                // Color Picker
                JButton colorButton = new JButton("Choose Color");
            	toolbar.add(colorButton);
            	colorButton.addActionListener(new ActionListener() {
        			public void actionPerformed(ActionEvent event) {
        				Color color = JColorChooser.showDialog(window, "Choose Background Color", Color.WHITE);
        				if (color != null) {
        					canvas.setColor(color);
        				}
        			}
            	});

        		ImageIcon eraserIcon = new ImageIcon("img/eraser.png");
                JToggleButton eraserPicker = new JToggleButton("eraser", eraserIcon, false);
            	toolbar.add(eraserPicker);
            	eraserPicker.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent event) {
            			canvas.toggleEraserMode();
                	}            	
        		});

            	JSlider strokeSlider = new JSlider(JSlider.HORIZONTAL, 1, 30, 5);
            	toolbar.add(strokeSlider);
            	strokeSlider.addChangeListener(new ChangeListener() {
            		public void stateChanged(ChangeEvent c) {
            			JSlider s = (JSlider) c.getSource();
            			canvas.setStrokeWidth(s.getValue());
            		}
            	});
            	// Add canvas
                window.add(canvas, BorderLayout.WEST);

                // Add users list
                ArrayList<String> guests = new ArrayList<String>();
                guests.add("Genghis");
                canvas.displayListOfGuests(guests, window);
                window.setVisible(true);
			}
		});
	}	
}