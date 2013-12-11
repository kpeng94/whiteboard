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

public class WhiteboardClient{
	private final Socket socket;

	// User must be created (become non-null) following a "success username" 
	private User user = null;

	// GUI for listing the whiteboards (i.e. main screen)
	private WhiteboardListGUI mainGUI;

	// for initializing a canvas
	private final int width = 800;
	private final int height = 600;

	private PrintWriter printServer;
	private BufferedReader readServer;

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
	 * Handle a single client connection. Returns when client disconnects.
	 * 
	 * @throws IOException if connection has an error or terminates unexpectedly
	 */
	private void handleServerRequests(){
		final BufferedReader in = readServer;

		Thread GUIListener = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (String input = in.readLine(); input != null; input = in.readLine()) {
						handleMessages(input);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		GUIListener.start();
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
		String message;
		if (user != null) {
			message = "exit whiteboard " + name;
		} else {
			message = "exit failure";
		}

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
	 * Handler for client input, performing requested operations.
	 * 
	 * @param input message from client
	 */
	private void handleMessages(String input) {
		String[] request = input.split(" ");

		// handles username requests
		// Successful username attempt
		if(request[0].equals("success") && request[1].equals("username")){
			user = new User(request[2]);
			mainGUI = new WhiteboardListGUI(this);
			mainGUI.setTitle("Whiteboard - Logged in as: " + request[2]);	
			mainGUI.setVisible(true);
		} else if (request[0].equals("retry") && request[1].equals("username")){
			// Failed username attempt
			SimplePromptGUI newUsername = new SimplePromptGUI(this, SimplePromptGUI.REPROMPT_USERNAME);
			newUsername.setVisible(true);
		} else {
			if (user != null) {
				if(request[0].equals("list") && request[1].equals("whiteboard")) {					
					ArrayList<String> newWhiteboardNames = new ArrayList<String>();
					for (int i = 2; i < request.length; i++) {
						newWhiteboardNames.add(request[i]);
					}
					mainGUI.updateTable(newWhiteboardNames);
				} else if (request[0].equals("list") && 
						request[1].equals("whiteboard-user")) {
					HashSet<String> newUserList = new HashSet<String>();
					for (int i = 3; i < request.length; i++) {
						// Adds a message if the user is you
						if(request[i].equals(user.getUsername())){
							request[i] += " (me!)";
						}
						newUserList.add(request[i]);
					}
					user.getWhiteboard(request[2]).setUsers(newUserList);
				} else if (request[0].equals("add") &&
						request[1].equals("whiteboard")) {
					mainGUI.addWhiteboard(request[2]);
				} else if (request[0].equals("add") &&
						request[1].equals("whiteboard-user")) {
					if(request[3].equals(user.getUsername())){
						request[3] += " (me!)";
					}
					user.getWhiteboard(request[2]).addUser(request[3]);

				} else if (request[0].equals("retry") && request[1].equals("whiteboard")) {
					SimplePromptGUI newWhiteboard = new SimplePromptGUI(this, SimplePromptGUI.REPROMPT_WHITEBOARD);
					newWhiteboard.setVisible(true);
				} else if (request[0].equals("remove")) {
					String usernameToRemove = request[3];
					user.getWhiteboard(request[2]).removeUser(usernameToRemove);	
				} else if (request[0].equals("error")) {
					mainGUI.throwWhiteboardErrorMessage();
				} else if (request[0].equals("success") && request[2].equals("join")) {
					Canvas canvas = new Canvas(width, height, this, request[3]);
					user.addWhiteboard(new Whiteboard(request[3], canvas));
					user.getWhiteboard(request[3]).display();
				} else if (request[0].equals("success") && request[2].equals("exit")) {
					user.removeWhiteboard(request[3]);
				} else if (request[0].equals("draw")) {
					// draw whiteboard [WHITEBOARD NAME] [x1] [y1] [x2] [y2] 
					// 				   [red] [green] [blue] [stroke size]
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