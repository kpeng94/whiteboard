package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class LoginGUI extends JFrame {

	private static final long serialVersionUID = -6157734000544090104L;
	
	private final JLabel loginText;
	private final JLabel displayIP;
	private final JLabel displayPort;
	private final JLabel displayUserName;
	private final JTextField typeIP;
	private final JTextField typePort;
	private final JTextField typeUserName;
	private final JButton ok;
	private final JButton cancel;
	private final WhiteboardClient client;
	
	public LoginGUI(WhiteboardClient clientObject){
		loginText = new JLabel("Log into the whiteboard server.");
		displayIP = new JLabel("Server IP");
		displayPort = new JLabel("Server Port");
		displayUserName = new JLabel("Username");
		typeIP = new JTextField("localhost");
		typePort = new JTextField("4444");
		typeUserName = new JTextField("");
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		cancel.addActionListener(new CancelListener());
		client = clientObject;
		
		setTitle("Whiteboard Login");
		setResizable(false);
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addGroup(layout.createSequentialGroup().addComponent(loginText))
				.addGroup(layout.createSequentialGroup().addComponent(displayIP).addComponent(typeIP))
				.addGroup(layout.createSequentialGroup().addComponent(displayPort).addComponent(typePort))
				.addGroup(layout.createSequentialGroup().addComponent(displayUserName).addComponent(typeUserName))
				.addGroup(layout.createSequentialGroup().addComponent(ok).addComponent(cancel)));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(loginText))
				.addGroup(layout.createParallelGroup().addComponent(displayIP).addComponent(typeIP))
				.addGroup(layout.createParallelGroup().addComponent(displayPort).addComponent(typePort))
				.addGroup(layout.createParallelGroup().addComponent(displayUserName).addComponent(typeUserName))
				.addGroup(layout.createParallelGroup().addComponent(ok).addComponent(cancel)));
		
		pack();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	private class CancelListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			LoginGUI.super.setVisible(false);
			LoginGUI.super.dispose();
		}	
	}
	
	//TODO: Create a ActionListener for the "OK" button
}
