package canvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

public class Whiteboard extends JFrame {
	private Canvas canvas;
	// TODO: Change to thread-safe versions later
	private ArrayList<String> users;
	private Color color; 
	private final JToggleButton eraserPicker;
	
	public Whiteboard(String title, Canvas canvas, ArrayList<String> users, Color color) {
		super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
		
		this.canvas = canvas;
		this.users = users;
		this.color = color;
        this.eraserPicker = new JToggleButton("eraser", false);
        eraserPicker.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		if (getColor().equals(Color.WHITE)) {
        			setColor(Color.BLUE);
        		} else {
        			System.out.println("Blah");
        			setColor(Color.WHITE);
        		}
        	}
        });
        this.add(canvas, BorderLayout.CENTER);
        this.add(eraserPicker, BorderLayout.SOUTH);        
	}
        
    private void setColor(Color color) {
    	this.color = color;
    	this.canvas.setColor(color);
    }
    
    private Color getColor() {
    	return this.color;
    }
	
	
	
	/**
	 * Main method, which generates the GUI.
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Whiteboard whiteboard = new Whiteboard("Whiteboard", new Canvas(800, 600), 
													   new ArrayList<String>(), Color.BLACK);
                whiteboard.pack();

				whiteboard.setVisible(true);
			}
		});
	}	
}
