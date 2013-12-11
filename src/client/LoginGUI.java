package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

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
	private final WhiteboardClientMain client;

	public LoginGUI(WhiteboardClientMain clientObject){
		loginText = new JLabel("Log into the whiteboard server.");
		displayIP = new JLabel("Server IP");
		displayPort = new JLabel("Server Port");
		displayUserName = new JLabel("Username");
		typeIP = new JTextField("localhost");
		typePort = new JTextField("4444");
		typeUserName = new JTextField("");
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
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private class CancelListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			LoginGUI.this.setVisible(false);
			LoginGUI.this.dispose();
		}
	}

	private class OKListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try{
				String host = typeIP.getText();
				int port = Integer.parseInt(typePort.getText());
				Socket socket = new Socket(host, port);
				client.initializeClient(socket, typeUserName.getText());
				LoginGUI.this.setVisible(false);
				LoginGUI.this.dispose();
			} catch (Exception e){
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Your connection was refused or is invalid. Check your IP and port.", 
						"Your connection was refused or is invalid. Check your IP and port.", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
