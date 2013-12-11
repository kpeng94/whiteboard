 package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class WhiteboardListGUI extends JFrame {
	
	private final JScrollPane scrollPane;
	private final JLabel label;
	private final JTable table;
	private final DefaultTableModel model;
	private final JButton createWhiteboard;
	private final JButton ok;
	private final JButton logout;
	private final WhiteboardClient client;
	
	public WhiteboardListGUI(WhiteboardClient clientSocket){
		client = clientSocket;
		
		label = new JLabel("Select the whiteboard to join, or create a new one.");
		createWhiteboard = new JButton("New Whiteboard");
		createWhiteboard.addActionListener(new WhiteboardCreateListener());
		ok = new JButton("Select Whiteboard");
		logout = new JButton("Logout");
		logout.addActionListener(new LogoutListener());
		
		model = new DefaultTableModel(new Object[]{"Whiteboard Name"}, 0);
		table = new JTable(model){

			public boolean isCellEditable(int row, int column) {                
				return false;               
			}
		};
		scrollPane = new JScrollPane(table);
		
		setResizable(false);
		setTitle("Whiteboards");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layout.createSequentialGroup().addComponent(label))
				.addGroup(layout.createSequentialGroup().addComponent(scrollPane))
				.addGroup(layout.createSequentialGroup().addComponent(createWhiteboard).addComponent(ok).addComponent(logout)));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup().addComponent(label))
				.addGroup(layout.createParallelGroup().addComponent(scrollPane))
				.addGroup(layout.createParallelGroup().addComponent(createWhiteboard).addComponent(ok).addComponent(logout)));
		
		pack();
	}
	
	public void updateTable(ArrayList<String> users){
		model.setRowCount(0);
		for(String s: users){
			model.addRow(new Object[]{s});
		}
	}
	
	private class WhiteboardCreateListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			SimplePromptGUI prompt = new SimplePromptGUI(client, SimplePromptGUI.PROMPT_WHITEBOARD);
			prompt.setVisible(true);
		}
	}
	
	private class LogoutListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			client.sendDisconnectUsernameMessage();
		}
	}
}
