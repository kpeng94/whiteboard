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

/**
 * A GUI that prompts the user to perform 
 * a specified action. There are three possible
 * uses for this GUI window - for prompting the user
 * to enter a username or whiteboard name after
 * failing, or for prompting the user to enter
 * a whiteboard name after clicking the "Create
 * Whiteboard" button in the Whiteboard list GUI.
 */
@SuppressWarnings("serial")
public class SimplePromptGUI extends JFrame {
	
	private final JLabel promptText;
	private final JLabel message;
	private final JTextField inputName;
	private final JButton ok;
	private final JButton cancel;
	private final int type;
	
	// Provides GUI a way to send messages to the server.
	private final WhiteboardClient client;
	
	public static final int REPROMPT_USERNAME = 0;
	public static final int PROMPT_WHITEBOARD = 1;
	public static final int REPROMPT_WHITEBOARD = 2;
	
	/**
	 * Constructor for this GUI.
	 * @param clientObject Gives GUI functionality to send messages to server
	 * @param guiType Type of prompt message
	 */
	public SimplePromptGUI(WhiteboardClient clientObject, int guiType){
		client = clientObject;
		type = guiType;
		
		// User needs to type another username
		if(type == REPROMPT_USERNAME){
			promptText = new JLabel("<html>Username already taken or invalid. "
					+ "Enter a new one. <br/> The name must not contain any whitespace.</html>");
			message = new JLabel("New Username");
			setTitle("Re-enter Username");
		}
		// User needs to give a whiteboard name
		else if(type == PROMPT_WHITEBOARD){
			promptText = new JLabel("<html>Please enter the desired whiteboard name. <br/> "
					+ "The name must not contain any whitespace.</html>");
			message = new JLabel("Whiteboard Name");
			setTitle("Name Whiteboard");
		}
		// User needs to type another whiteboard name
		else if(type == REPROMPT_WHITEBOARD){
			promptText = new JLabel("<html>Whiteboard name already taken or invalid. "
					+ "Enter a new one. <br/> The name must not contain any whitespace.</html>");
			message = new JLabel("New Whiteboard Name");
			setTitle("Name Whiteboard");
		}
		// No other type is allowed
		else{
			throw new RuntimeException("Illegal constructor call");
		}
		
		inputName = new JTextField();
		inputName.addActionListener(new OKListener());
		ok = new JButton("OK");
		ok.addActionListener(new OKListener());
		cancel = new JButton("Cancel");
		cancel.addActionListener(new CancelListener());
		setResizable(false);
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);;
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addGroup(layout.createSequentialGroup().addComponent(promptText))
				.addGroup(layout.createSequentialGroup().addComponent(message).addComponent(inputName))
				.addGroup(layout.createSequentialGroup().addComponent(ok).addComponent(cancel)));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(promptText))
				.addGroup(layout.createParallelGroup().addComponent(message).addComponent(inputName))
				.addGroup(layout.createParallelGroup().addComponent(ok).addComponent(cancel)));
		
		pack();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Action listener for the cancel button; 
	 * will exit the program if the window is asking for the username.
	 * Otherwise, only the window was close.
	 */
	private class CancelListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			SimplePromptGUI.this.setVisible(false);
			if(type == SimplePromptGUI.REPROMPT_USERNAME){
				System.exit(0);
			}
			SimplePromptGUI.this.dispose();
		}	
	}
	
	/**
	 * Action listener for pressing the OK button.
	 * Will send appropriate messages to the server when
	 * triggered, as well as close the window.
	 */
	private class OKListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg){
			if(type == SimplePromptGUI.REPROMPT_USERNAME){
				client.sendAddUsernameMessage(inputName.getText());
			}
			else if(type == SimplePromptGUI.PROMPT_WHITEBOARD || 
					type == SimplePromptGUI.REPROMPT_WHITEBOARD){
				client.sendCreateWhiteboardMessage(inputName.getText());
			}
			SimplePromptGUI.this.setVisible(false);
			SimplePromptGUI.this.dispose();
		}
	}
}