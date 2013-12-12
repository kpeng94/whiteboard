package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * This is the GUI for displaying the whiteboard. 
 */
@SuppressWarnings("serial")
public class WhiteboardGUI extends JFrame {
	
	private final JToolBar toolbar;
	private final JButton colorButton;
	private final JToggleButton eraserPicker;
	private final JSlider strokeSlider;
	private final DefaultTableModel tableModel;
	private final JTable table;
	private final JScrollPane scrollPane;
	
	/**
	 * Constructor for whiteboard GUI
	 * @param canvas The canvas to display in the GUI
	 * @param whiteboardName name of whiteboard
	 */
	public WhiteboardGUI(final Canvas canvas, String whiteboardName){

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// When the window is closed, make sure the client sends a signal to the
		// server notifying it of the closure.
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        canvas.sendDisconnectToServer();
		    }
		});
		setLayout(new BorderLayout());
		setResizable(false);

		// Add toolbar
		toolbar = new JToolBar("Bar");
		add(toolbar, BorderLayout.NORTH);
		toolbar.setFloatable(false);

		// Toolbar buttons
		// Color Picker
		colorButton = new JButton("Choose Color");
		toolbar.add(colorButton);
		colorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Color color = JColorChooser.showDialog(WhiteboardGUI.this, 
													   "Choose Background Color", 
													   Color.WHITE);
				if (color != null) {
					canvas.setColor(color);
				}
			}
		});

		// Add eraser icon
		ImageIcon eraserIcon = new ImageIcon("img/eraser.png");
		eraserPicker = new JToggleButton("eraser", eraserIcon, false);
		toolbar.add(eraserPicker);
		eraserPicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				canvas.toggleEraserMode();
			}            	
		});

		// Add stroke slider
		strokeSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 5);
		toolbar.add(strokeSlider);
		strokeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent c) {
				JSlider s = (JSlider) c.getSource();
				canvas.setStrokeWidth(s.getValue());
			}
		});

		// Add canvas
		add(canvas, BorderLayout.WEST);

		// Add users list
		String[] tableColumns = {"Guests"}; 
		tableModel = new DefaultTableModel(tableColumns, 0);
		table = new JTable(tableModel){
			public boolean isCellEditable(int row, int column) {                
				return false;               
			}
		};
		table.setShowGrid(false);
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(renderer);
		scrollPane = new JScrollPane(table);
		
		add(scrollPane, BorderLayout.CENTER);
		setTitle(whiteboardName);
		
		pack();
		setVisible(true);
	}
	
	/**
	 * Adds a user entry to the user table model
	 * @param user User to add
	 */
	public void addUserToModel(String user){
		tableModel.addRow(new Object[]{user});
	}

	/**
	 * Removes a user entry to the user table model
	 * @param user User to remove
	 */
	public void removeUserFromModel(String user){
		for(int i = 0; i < tableModel.getRowCount(); i++){
			if(((String)tableModel.getValueAt(i, 0)).equals(user)){
				tableModel.removeRow(i);
				break;
			}
		}
	}
}