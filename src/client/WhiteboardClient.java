package client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

import whiteboard.LineSegment;
import whiteboard.Whiteboard;

/**
 * Handles all of the message processing for the client.
 */
public class WhiteboardClient{
	private final Socket socket;

	// User must be created (become non-null) following a "success username" 
	private User user = null;

	// GUI for listing the whiteboards (i.e. main screen)
	private WhiteboardListGUI mainGUI;

	// for initializing a canvas
	private final int width = 800;
	private final int height = 600;

	private final PrintWriter printServer;
	private final BufferedReader readServer;

	/**
	 * Constructor for a whiteboard client.
	 * @param initiate Helpers manage the whiteboard client
	 * @param clientSocket Socket for the client to connect to
	 */
	public WhiteboardClient(WhiteboardClientMain initiate, Socket clientSocket){
		socket = clientSocket;
		try {
			printServer = new PrintWriter(socket.getOutputStream(), true);
			readServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			throw new RuntimeException("IO failed");
		}
		handleServerRequests();
	}

	/**
	 * Creates a thread to handle server messages.
	 */
	private void handleServerRequests(){
		Thread serverListener = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (String input = readServer.readLine(); input != null; input = readServer.readLine()) {
						handleMessages(input);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		serverListener.start();
	}
	
	/**
	 * Sends a message to the server to add a username
	 * @param username Username to add
	 */
	public void sendAddUsernameMessage(String username) {
		String message = "add username " + username;
		printServer.println(message);	
	}

	/**
	 * Sends a message to disconnect a username
	 */
	public void sendDisconnectUsernameMessage() {
		String message = "disconnect username " + user.getUsername();
		printServer.println(message);	
		
		// Close the socket and exit the program.
		try {
			printServer.close();
			readServer.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * Sends a message to create a new whiteboard
	 * @param name Name of whiteboard
	 */
	public void sendCreateWhiteboardMessage(String name) {
		String message = "create whiteboard " + name;
		printServer.println(message);	
	}

	/**
	 * Sends a message to join an existing whiteboard
	 * @param name Name of whiteboard
	 */
	public void sendJoinWhiteboardMessage(String name) {
		String message = "join whiteboard " + name;
		printServer.println(message);	
	}

	/**
	 * Sends a message to exit a whiteboard
	 * @param name Name of whiteboard
	 */
	public void sendExitWhiteboardMessage(String name) {
		String message = "exit whiteboard " + name;
		printServer.println(message);	
	}

	/**
	 * Sends a message to draw a line on a whiteboard
	 * @param whiteboardName Name of whiteboard to draw on
	 * @param x1 Starting x-coordinate
	 * @param y1 Starting y-coordinate
	 * @param x2 Ending x-coordinate
	 * @param y2 Ending y-coordinate
	 * @param r Red values
	 * @param g Green values
	 * @param b Blue values
	 * @param strokeSize Size of the stroke
	 */
	public void sendDrawMessage(String whiteboardName, int x1, int y1, int x2, int y2, 
								int r, int g, int b, int strokeSize) {
		// draw whiteboard [WHITEBOARD NAME] [x1] [y1] [x2] [y2] 
		// [red] [green] [blue] [stroke size]
		String message = "draw whiteboard " + whiteboardName + " " + 
				String.valueOf(x1) + " " + String.valueOf(y1) + " " + 
				String.valueOf(x2) + " " + String.valueOf(y2) + " " + 
				String.valueOf(r) + " " + String.valueOf(g) + " " + 
				String.valueOf(b) + " " + String.valueOf(strokeSize);

		printServer.println(message);	
	}	

	/**
	 * Handler for server messages, performing requested operations.
	 * 
	 * @param input message from server
	 */
	private void handleMessages(String input) {
		String[] request = input.split(" ");

		// Handles username requests
		// PROTOCOL: success username [USERNAME]
		if(request[0].equals("success") && request[1].equals("username")){
			user = new User(request[2]);
			mainGUI = new WhiteboardListGUI(this);
			mainGUI.setTitle("Whiteboard - Logged in as: " + request[2]);	
			mainGUI.setVisible(true);
		} 
		// PROTOCOL: retry username
		else if (request[0].equals("retry") && request[1].equals("username")){
			SimplePromptGUI newUsername = new SimplePromptGUI(this, SimplePromptGUI.REPROMPT_USERNAME);
			newUsername.setVisible(true);
		} else {
			if (user != null) {
				// PROTOCOL: list whiteboard [WHITEBORD] [WHITEBOARD] [WHITEBOARD] ...
				if(request[0].equals("list") && request[1].equals("whiteboard")) {					
					ArrayList<String> newWhiteboardNames = new ArrayList<String>();
					for (int i = 2; i < request.length; i++) {
						newWhiteboardNames.add(request[i]);
					}
					mainGUI.addWhiteboards(newWhiteboardNames);
				} 
				// PROTOCOL: list whitboard-user [WHITEBOARD] [USER] [USER] [USER] ...
				else if (request[0].equals("list") && 
						request[1].equals("whiteboard-user")) {
					HashSet<String> newUserList = new HashSet<String>();
					for (int i = 3; i < request.length; i++) {
						// Adds a message if the user is you!
						if(request[i].equals(user.getUsername())){
							request[i] += " (me!)";
						}
						newUserList.add(request[i]);
					}
					user.getWhiteboard(request[2]).setUsers(newUserList);
				} 
				// PROTOCOL: add whiteboard [WHITEBOARD]
				else if (request[0].equals("add") &&
						request[1].equals("whiteboard")) {
					mainGUI.addWhiteboard(request[2]);
				} 
				// PROTOCOL: add whiteboard-user [WHITEBOARD] [NAME]
				else if (request[0].equals("add") &&
						request[1].equals("whiteboard-user")) {
					// Adds a message if the user is you!
					// Should never trigger, used as failsafe
					if(request[3].equals(user.getUsername())){
						request[3] += " (me!)";
					}
					user.getWhiteboard(request[2]).addUser(request[3]);
				} 
				// PROTOCOL: retry whiteboard naming
				else if (request[0].equals("retry") && request[1].equals("whiteboard")) {
					SimplePromptGUI newWhiteboard = new SimplePromptGUI(this, SimplePromptGUI.REPROMPT_WHITEBOARD);
					newWhiteboard.setVisible(true);
				} 
				// PROTOCOL: remove whiteboard-user [WHITEBOARD] [USER]
				else if (request[0].equals("remove")) {
					String usernameToRemove = request[3];
					user.getWhiteboard(request[2]).removeUser(usernameToRemove);	
				} 
				// PROTOCOL: error whiteboard
				else if (request[0].equals("error")) {
					mainGUI.throwWhiteboardErrorMessage();
				}
				// PROTOCOL: success whiteboard join [WHITEBOARD]
				else if (request[0].equals("success") && request[2].equals("join")) {
					Canvas canvas = new Canvas(width, height, this, request[3]);
					user.addWhiteboard(new Whiteboard(request[3], canvas));
					user.getWhiteboard(request[3]).display();
				} 
				// PROTOCOL: success whiteboard exit [WHITEBOARD]
				else if (request[0].equals("success") && request[2].equals("exit")) {
					user.removeWhiteboard(request[3]);
				} 
				// PROTOCOL: draw whiteboard [WHITEBOARD] [X1] [Y1] [X2] [Y2] [RED] [GREEN] [BLUE] [WIDTH]
				else if (request[0].equals("draw")) {
					Whiteboard whiteboard = user.getWhiteboard(request[2]);
					if (whiteboard != null) {
						int x1 = Integer.parseInt(request[3]);
						int y1 = Integer.parseInt(request[4]);
						int x2 = Integer.parseInt(request[5]);
						int y2 = Integer.parseInt(request[6]);
						Color color = new Color(Integer.parseInt(request[7]), 
								Integer.parseInt(request[8]), 
								Integer.parseInt(request[9]));
						int strokeSize = Integer.parseInt(request[10]);
						LineSegment lineSegment = new LineSegment(x1, y1, x2, y2, color, strokeSize);
						whiteboard.addLineSegment(lineSegment);
					}
				}
			}
		}
	}
}