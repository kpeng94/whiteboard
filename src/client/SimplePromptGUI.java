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

@SuppressWarnings("serial")
public class SimplePromptGUI extends JFrame {
	
	private final JLabel promptText;
	private final JLabel message;
	private final JTextField inputName;
	private final JButton ok;
	private final JButton cancel;
	private final int type;
	private final WhiteboardClient client;
	
	public static final int REPROMPT_USERNAME = 0;
	public static final int PROMPT_WHITEBOARD = 1;
	public static final int REPROMPT_WHITEBOARD = 2;
	
	public SimplePromptGUI(WhiteboardClient clientObject, int guiType){
		client = clientObject;
		type = guiType;
		
		if(type == REPROMPT_USERNAME){
			promptText = new JLabel("<html>Username already taken or invalid. Enter a new one. <br/> The name must not contain any whitespace.</html>");
			message = new JLabel("New Username");
			setTitle("Re-enter Username");
		}
		else if(type == PROMPT_WHITEBOARD){
			promptText = new JLabel("<html>Please enter the desired whiteboard name. <br/> The name must not contain any whitespace.</html>");
			message = new JLabel("Whiteboard Name");
			setTitle("Name Whiteboard");
		}
		else if(type == REPROMPT_WHITEBOARD){
			promptText = new JLabel("<html>Whiteboard name already taken or invalid. Enter a new one. <br/> The name must not contain any whitespace.</html>");
			message = new JLabel("New Whiteboard Name");
			setTitle("Name Whiteboard");
		}
		else{
			throw new RuntimeException("Illegal constructor call");
		}
		
		inputName = new JTextField();
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
	
	private class OKListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg){
			if(type == SimplePromptGUI.REPROMPT_USERNAME){
				client.sendAddUsernameMessage(inputName.getText());
			}
			else if(type == SimplePromptGUI.PROMPT_WHITEBOARD || type == SimplePromptGUI.REPROMPT_WHITEBOARD){
				client.sendCreateWhiteboardMessage(inputName.getText());
			}
			SimplePromptGUI.this.setVisible(false);
			SimplePromptGUI.this.dispose();
		}
	}
}