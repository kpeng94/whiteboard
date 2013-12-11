package client;

import java.net.Socket;

public class WhiteboardClientMain {
	
	private WhiteboardClient client;
	
	public void initializeClient(Socket clientSocket, String userName){
		client = new WhiteboardClient(this, clientSocket);
		client.sendAddUsernameMessage(userName);
	}
	
	public void retryUsername(){
		SimplePromptGUI newUsername = new SimplePromptGUI(client, SimplePromptGUI.REPROMPT_USERNAME);
		newUsername.setVisible(true);
	}
	
	public static void main(String[] args){
		LoginGUI login = new LoginGUI(new WhiteboardClientMain());
		login.setVisible(true);
	}
}
