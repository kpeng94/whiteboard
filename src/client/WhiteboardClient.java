package client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import canvas.Canvas;
import canvas.LineSegment;
import canvas.Whiteboard;

public class WhiteboardClient{
	private final Socket socket;

	// User must be created (become non-null) following a "success username" 
	private User user = null;

	private WhiteboardClientMain handler;

	// GUI for listing the whiteboards (i.e. main screen)
	private WhiteboardListGUI mainGUI;

	// for initializing a canvas
	private final int width = 800;
	private final int height = 600;

	private PrintWriter printServer;
	private BufferedReader readServer;

	public WhiteboardClient(WhiteboardClientMain initiate, Socket clientSocket){
		socket = clientSocket;
		handler = initiate;
		try {
			printServer = new PrintWriter(socket.getOutputStream(), true);
			readServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			throw new RuntimeException("IO failed");
		}
		handleConnection();
	}

	/**
	 * Handle a single client connection. Returns when client disconnects.
	 * 
	 * @throws IOException if connection has an error or terminates unexpectedly
	 */
	private void handleConnection(){
		final BufferedReader in = readServer;

		Thread GUIListener = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (String input = in.readLine(); input != null; input = in.readLine()) {
						// maybe just calling handleMessage(input) is enough to process the message
						// then get rid of the String output that handleMessages(input) currently returns.
						String output = handleMessages(input);
						System.out.println(output);
					}
				} catch (IOException e) {
					;
				}
			}
		});

		GUIListener.start();
	}

	public void sendAddUsernameMessage(String username) {
		String message = "add username " + username;
		synchronized(printServer){
			printServer.println(message);	
		}
	}

	public void sendDisconnectUsernameMessage() {
		String message = "disconnect username " + user.getUsername();
		synchronized(printServer){
			printServer.println(message);	
		}
		try {
			printServer.close();
			readServer.close();
			socket.close();
		} catch (IOException e) {
			;
		}
		System.exit(0);
	}

	public void sendCreateWhiteboardMessage(String name) {
		String message = "create whiteboard " + name;
		synchronized(printServer){
			printServer.println(message);	
		}
	}

	public void sendJoinWhiteboardMessage(String name) {
		String message = "join whiteboard " + name;
		synchronized(printServer){
			printServer.println(message);	
		}
	}

	public void sendExitWhiteboardMessage(String name) {
		String message;
		if (user != null) {
			message = "exit whiteboard " + name;
		} else {
			message = "exit failure";
		}

		synchronized(printServer){
			printServer.println(message);	
		}
	}

	public void sendDrawMessage(String whiteboardName, int x1, int y1, int x2, int y2, int r, int g, int b, int strokeSize) {
		// draw whiteboard [WHITEBOARD NAME] [x1] [y1] [x2] [y2] 
		// [red] [green] [blue] [stroke size]
		String message = "draw whiteboard " + whiteboardName + " " + 
				String.valueOf(x1) + " " + String.valueOf(y1) + " " + 
				String.valueOf(x2) + " " + String.valueOf(y2) + " " + 
				String.valueOf(r) + " " + String.valueOf(g) + " " + 
				String.valueOf(b) + " " + String.valueOf(strokeSize);
		System.out.println(message);
		synchronized(printServer){
			printServer.println(message);	
		}
	}	

	/**
	 * Handler for client input, performing requested operations and returning an output message.
	 * 
	 * @param input message from client
	 * @return message to client
	 */
	private String handleMessages(String input) {
		String[] commandArgs = input.split(" ");

		// handles username requests

		// Successful username attempt
		if(commandArgs[0].equals("success") && commandArgs[1].equals("username")){
			user = new User(commandArgs[2]);
			mainGUI = new WhiteboardListGUI(this);
			mainGUI.setTitle("Whiteboard - Logged in as: " + commandArgs[2]);	
			mainGUI.setVisible(true);

			return "success";
		} else if (commandArgs[0].equals("retry") && commandArgs[1].equals("username")){
			// Failed username attempt

			SimplePromptGUI newUsername = new SimplePromptGUI(this, SimplePromptGUI.REPROMPT_USERNAME);
			newUsername.setVisible(true);

			return "--------------------------------------------------------------------------";
		} else {
			if (user != null) {
				//ArrayList<String> whiteboardUsers = user.getWhiteboard().getUsers();
				//Whiteboard whiteboard = user.getWhiteboard();
				if(commandArgs[0].equals("list") && commandArgs[1].equals("whiteboard")) {					
					ArrayList<String> newWhiteboardNames = new ArrayList<String>();
					for (int i = 2; i < commandArgs.length; i++) {
						newWhiteboardNames.add(commandArgs[i]);
					}
					mainGUI.updateTable(newWhiteboardNames);
					return "success0";
				} else if (commandArgs[0].equals("list") && 
						commandArgs[1].equals("whiteboard-user")) {
					ArrayList<String> newUserList = new ArrayList<String>();
					for (int i = 3; i < commandArgs.length; i++) {
						newUserList.add(commandArgs[i]);
					}
					user.getWhiteboard(commandArgs[2]).setUsers(newUserList);
					return "success1";
				} else if (commandArgs[0].equals("add")) {
					user.getWhiteboard(commandArgs[2]).addUser(commandArgs[3]);
					return "success2";
				} else if (commandArgs[0].equals("retry") && commandArgs[1].equals("whiteboard")) {
					SimplePromptGUI newWhiteboard = new SimplePromptGUI(this, SimplePromptGUI.REPROMPT_WHITEBOARD);
					newWhiteboard.setVisible(true);
					return "retry whiteboard";
				} else if (commandArgs[0].equals("remove")) {
					String usernameToRemove = commandArgs[3];
					ArrayList<String> whiteboardUsers = user.getWhiteboard(commandArgs[2]).getUsers();
					int index = whiteboardUsers.indexOf(usernameToRemove);
					if (index != -1) {
						whiteboardUsers.remove(index);
						return "success";
					}
					return "fail";			
				} else if (commandArgs[0].equals("error")) {
					mainGUI.throwWhiteboardErrorMessage();
					return "error";
				} else if (commandArgs[0].equals("success") && commandArgs[2].equals("join")) {
					Canvas canvas = new Canvas(width, height, this, commandArgs[3]);
					ArrayList<String> initialUsers = new ArrayList<String>();
					user.addWhiteboard(new Whiteboard(commandArgs[3], canvas, initialUsers));
					user.getWhiteboard(commandArgs[3]).display();
					return "successful join";
				} else if (commandArgs[0].equals("success") && commandArgs[2].equals("exit")) {
					user.removeWhiteboard(commandArgs[3]);
					return "success3";
				} else if (commandArgs[0].equals("draw")) {
					// draw whiteboard [WHITEBOARD NAME] [x1] [y1] [x2] [y2] 
					// 				   [red] [green] [blue] [stroke size]
					Whiteboard whiteboard = user.getWhiteboard(commandArgs[2]);
					if (whiteboard != null) {
						int x1 = Integer.parseInt(commandArgs[3]);
						int y1 = Integer.parseInt(commandArgs[4]);
						int x2 = Integer.parseInt(commandArgs[5]);
						int y2 = Integer.parseInt(commandArgs[6]);
						Color color = new Color(Integer.parseInt(commandArgs[7]), 
								Integer.parseInt(commandArgs[8]), 
								Integer.parseInt(commandArgs[9]));
						int strokeSize = Integer.parseInt(commandArgs[10]);
						LineSegment lineSegment = new LineSegment(x1, y1, x2, y2, color, strokeSize);
						whiteboard.addLineSegment(lineSegment);
						return "success4";
					} else {
						return "no whiteboard";
					}

				}
			}
			// Should never get here--make sure to return in each of the valid cases above.
			System.err.println(input);
			throw new UnsupportedOperationException();
		}
	}
}
