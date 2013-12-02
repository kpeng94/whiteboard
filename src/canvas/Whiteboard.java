package canvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

public class Whiteboard extends JFrame {
	private Canvas canvas;
	// TODO: Change to thread-safe versions later
	private ArrayList<String> users;
	private final JToggleButton eraserPicker;
	
	/**
	 * Constructor for a whiteboard object
	 * @param title Name of the whiteboard
	 * 		TODO: Currently, this is the name of the window, do we want that? 
	 * 		      We would have to make it changeable later as well if we were to do that.
	 * @param canvas The canvas associated with the whiteboard
	 * @param users The list of users who are currently accessing this whiteboard
	 */
	public Whiteboard(String title, Canvas canvas, ArrayList<String> users) {
		super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
		this.canvas = canvas;
		this.users = users;
		ImageIcon eraserIcon = new ImageIcon("img/eraser.png");
        this.eraserPicker = new JToggleButton("eraser", eraserIcon, false);
        eraserPicker.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		if (getColor().equals(Color.WHITE)) {
        			// TODO: Change this implementation later, but for demo purposes, this will suffice.
        			setColor(Color.BLUE);
        			setStrokeWidth(3);
        		} else {
        			setColor(Color.WHITE);
        			setStrokeWidth(6);
        		}
        	}
        });
        this.add(canvas, BorderLayout.CENTER);
        this.add(eraserPicker, BorderLayout.SOUTH);        
	}
        
	/**
	 * TODO: Change implementation later to attach color to user object
	 * 		instead of attaching it to a canvas object.
	 * @param color
	 */
    private void setColor(Color color) {
    	this.canvas.setColor(color);
    }
    
    /**
     * Gets the color selected for the whiteboard.
     * @return color selected for the whiteboard
     */
    private Color getColor() {
    	return this.canvas.getColor();
    }
    
    private void setStrokeWidth(int strokeWidth) {
    	this.canvas.setStrokeWidth(strokeWidth);
    }
	
	/**
	 * Main method, which generates the GUI.
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Whiteboard whiteboard = new Whiteboard("Whiteboard", new Canvas(800, 600), 
													   new ArrayList<String>());
                whiteboard.pack();

				whiteboard.setVisible(true);
			}
		});
	}	
}
