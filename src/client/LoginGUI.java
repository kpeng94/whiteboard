package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

/**
 * The Login GUI provides a simple interface for the user to login. 
 * This is the first screen that users will see when trying to access
 * the whiteboard server.
 */
@SuppressWarnings("serial")
public class LoginGUI extends JFrame {

	private final JLabel loginText;

	private final JLabel displayIP;
	private final JLabel displayPort;
	private final JLabel displayUserName;

	private final JTextField typeIP;
	private final JTextField typePort;
	private final JTextField typeUserName;

	private final JButton ok;
	private final JButton cancel;

	// Used for linking back to a central client process when
	// the proper data is received.
	private final WhiteboardClientMain client;

	/**
	 * Constructor for this GUI. 
	 * @param clientObject Gives GUI functionality to send messages to the server.
	 */
	public LoginGUI(WhiteboardClientMain clientObject){
		// Create the various labels
		loginText = new JLabel("Log into the whiteboard server.");
		displayIP = new JLabel("Server IP");
		displayPort = new JLabel("Server Port");
		displayUserName = new JLabel("Username");

		// Create the text fields for user input and let users press "Enter"
		// to submit the form
		typeIP = new JTextField("");
		typeIP.addActionListener(new OKListener());
		typePort = new JTextField("4444");
		typePort.addActionListener(new OKListener());
		typeUserName = new JTextField("");
		typeUserName.addActionListener(new OKListener());
		
		// Create the buttons and add the appropriate listeners
		ok = new JButton("OK");
		ok.addActionListener(new OKListener());
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
	
	/**
	 * Creates an error dialogue indicating a failure to connect to the server properly.
	 * Also toggles the OK/Cancel buttons of the GUI so that they are re-enabled again.
	 */
	public void throwErrorMessage(){
		JOptionPane.showMessageDialog(null, 
				"Your connection was refused or is invalid. Check your IP/port, or your firewall.",
				"Your connection was refused or is invalid. Check your IP.port, or your firewall.",
				JOptionPane.ERROR_MESSAGE);
		ok.setEnabled(true);
		cancel.setEnabled(true);
	}

	/**
	 * Action listener for pressing the cancel button.
	 * Will close the current LoginGUI screen and end the program.
	 */
	private class CancelListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			LoginGUI.this.setVisible(false);
			LoginGUI.this.dispose();
		}
	}

	/**
	 * Action listener for pressing the OK button.
	 * Tries to connect to the user. Upon failure, returns an error message 
	 * the user will be prompted to pick another username. The connection
	 * times out in 5 seconds, to prevent the GUI from possibly hanging infinitely.
	 */
	private class OKListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// Disable the user from trying to connect while another connection is in progress
			ok.setEnabled(false);
			cancel.setEnabled(false);
			try{
				// Try to connect to the host using another thread
				final String host = typeIP.getText();
				final int port = Integer.parseInt(typePort.getText());
				SwingWorker<Socket, Void> worker = new SwingWorker<Socket, Void>(){
					@Override
					public Socket doInBackground() {
						try {
							// Time out in 5 seconds to prevent everything from hanging
							Socket socket = new Socket();
							socket.connect(new InetSocketAddress(host,port), 5000);
							return socket;
						} catch (IOException e) {
							throwErrorMessage();
						}
						return null;
					}

					public void done(){
						Socket socket;
						try {
							socket = get();
						} catch(InterruptedException | ExecutionException e){
							throw new RuntimeException("Fatal error occurred");
						}
						if(socket != null){
							// If the socket successfully connected, move on from the login screen
							client.initializeClient(socket, typeUserName.getText());
							LoginGUI.this.setVisible(false);
							LoginGUI.this.dispose();
						}
					}
				};

				worker.execute();
			} catch (Exception e){
				throwErrorMessage();
			}
		}
	}
}
