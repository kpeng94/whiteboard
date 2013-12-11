package client;

import java.net.Socket;

/**
 * TODO: word this better?
 * A class for managing a whiteboard client.
 */
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
	
	public static void main(String[] args){
		LoginGUI login = new LoginGUI(new WhiteboardClientMain());
		login.setVisible(true);
	}
}
