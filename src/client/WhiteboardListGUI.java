package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 * GUI for listing the whiteboards on the server.
 * This is a client's view; the client has an option to
 * add a new whiteboard, join an existing one, or logout.
 * The Whiteboard List GUI displays the current list of
 * whiteboards that the server contains. The list is updated
 * each time a user creates a new whiteboard.
 *
 */
@SuppressWarnings("serial")
public class WhiteboardListGUI extends JFrame {
	
	private final JLabel label;
	private final JTable table;
	private final JScrollPane scrollPane;
	private final DefaultTableModel model;
	
	private final JButton createWhiteboard;
	private final JButton selectWhiteboard;
	private final JButton logout;
	
	// Provides the ability for the GUI to relay information back to the server.
	private final WhiteboardClient client;
	
	public WhiteboardListGUI(WhiteboardClient clientSocket){
		client = clientSocket;
		
		label = new JLabel("Select the whiteboard to join, or create a new one.");
		
		createWhiteboard = new JButton("New Whiteboard");
		createWhiteboard.addActionListener(new WhiteboardCreateListener());

		selectWhiteboard = new JButton("Select Whiteboard");
		selectWhiteboard.addActionListener(new WhiteboardSelectListener());
		
		logout = new JButton("Logout");
		logout.addActionListener(new LogoutListener());
		
		model = new DefaultTableModel(new Object[]{"Whiteboard Name"}, 0);
		table = new JTable(model){
			public boolean isCellEditable(int row, int column) {                
				return false;               
			}
		};
		table.setShowGrid(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane = new JScrollPane(table);
		
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layout.createSequentialGroup().addComponent(label))
				.addGroup(layout.createSequentialGroup().addComponent(scrollPane))
				.addGroup(layout.createSequentialGroup().addComponent(createWhiteboard)
														.addComponent(selectWhiteboard)
														.addComponent(logout)));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup().addComponent(label))
				.addGroup(layout.createParallelGroup().addComponent(scrollPane))
				.addGroup(layout.createParallelGroup().addComponent(createWhiteboard)
													  .addComponent(selectWhiteboard)
													  .addComponent(logout)));
		
		pack();
	}
	
	// THIS MAY BE CHANGED LATER :: (TODO)
	public void updateTable(ArrayList<String> users){
		model.setRowCount(0);
		for(String s: users){
			model.addRow(new Object[]{s});
		}
	}
	
	public void throwWhiteboardErrorMessage(){
		JOptionPane.showMessageDialog(null, 
				"Your attempt to join this whiteboard failed. You may already have the window open.",
				"Your attempt to join this whiteboard failed. You may already have the window open.",
				JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * An action listener for the "Create Whiteboard" button on the whiteboard list.
	 * Will create a new prompt for the user to enter the name of the whiteboard
	 * they would like to create.
	 */
	private class WhiteboardCreateListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			SimplePromptGUI prompt = new SimplePromptGUI(client, SimplePromptGUI.PROMPT_WHITEBOARD);
			prompt.setVisible(true);
		}
	}
	
	private class WhiteboardSelectListener implements ActionListener {

		/**
		 * Action performed when a user wants to join a whiteboard.
		 * Takes the selected row and tries to join the whiteboard 
		 * listed in that row.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				String selectedWhiteboardName = (String) model.getValueAt(selectedRow, 0);
				client.sendJoinWhiteboardMessage(selectedWhiteboardName);				
			}
		}
	}
	
	/**
	 * An action listener for the "Logout" button on the whiteboard list.
	 * Will send a disconnect user message to the server and appropriately
	 * end the program, closing any other windows that may be open.
	 */
	private class LogoutListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			client.sendDisconnectUsernameMessage();
		}
	}
}
