package client;

import java.net.Socket;

public class WhiteboardClientMain {
	
	private WhiteboardClient client;
	
	/**
	 * Sets up a client for a whiteboard with the given client socket.
	 * Sends the server a message to add this client's username.
	 * 
	 * @param clientSocket client socket to connect to
	 * @param userName username for the client
	 */
	public void initializeClient(Socket clientSocket, String userName){
		client = new WhiteboardClient(this, clientSocket);
		client.sendAddUsernameMessage(userName);
	}
	
	/**
	 * Pops up a prompt GUI that tells the user to try another username.
	 */
	public void retryUsername(){
		SimplePromptGUI newUsername = new SimplePromptGUI(client, SimplePromptGUI.REPROMPT_USERNAME);
		newUsername.setVisible(true);
	}
	
	public static void main(String[] args){
		LoginGUI login = new LoginGUI(new WhiteboardClientMain());
		login.setVisible(true);
	}
}