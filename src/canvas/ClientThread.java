package canvas;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread {
	private Socket socket;
	private String userName = null;
	// User must be created (become non-null) following a "success username" 
	private User user = null;
	// list of all available whiteboard names
	private ArrayList<String> whiteboardNames;
	// user's current whiteboard
	private Whiteboard whiteboard;
	// users associated with current whiteboard
	private ArrayList<String> whiteboardUsers = whiteboard.getUsers();
	
	// for initializing a canvas
	private final int width = 800;
	private final int height = 600;
	
	private PrintWriter outOut;

	public ClientThread(Socket clientSocket){
		socket = clientSocket;
		try {
			handleConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle a single client connection. Returns when client disconnects.
	 * 
	 * @throws IOException if connection has an error or terminates unexpectedly
	 */
	private void handleConnection() throws IOException {
		final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        
        outOut = out;

        Thread GUIListener = new Thread(new Runnable() {
        	@Override
        	public void run() {
        		try {
        			for (String input = in.readLine(); input != null; input = in.readLine()) {
        				// maybe just calling handleMessage(input) is enough to process the message
        				// then get rid of the String output that handleMessages(input) currently returns.
        				String output = handleMessages(input);
        			}
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        });
        
        GUIListener.start();
        
        socket.close();

	}
	
	//create methods for sending shit
	public void sendAddUsernameMessage(String username) {
		String message = "add username " + username;
		outOut.println(message);
	}
	public void sendDisconnectUsernameMessage() {
		String message = "disconnect username " + userName;
		outOut.println(message);
	}
	public void sendCreateWhiteboardMessage(String name) {
		String message = "create whiteboard " + name;
		outOut.println(message);
	}
	public void sendJoinWhiteboardMessage(String name) {
		String message = "join whiteboard " + name;
		outOut.println(message);
	}
	
	public void sendExitWhiteboardMessage() {
		String message = "exit whiteboard " + whiteboard.getName();
		outOut.println(message);
	}
	
	public void sendDrawMessage(int x1, int y1, int x2, int y2, int r, int g, int b, int strokeSize) {
		// draw whiteboard [WHITEBOARD NAME] [x1] [y1] [x2] [y2] 
		// [red] [green] [blue] [stroke size]
		String message = "draw whiteboard " + whiteboard.getName() + " " + String.valueOf(x1) + " " + String.valueOf(y1) + " "
						+ String.valueOf(x2) + " " + String.valueOf(y2) + " " + String.valueOf(r) + " " + String.valueOf(g)
						+ " " + String.valueOf(b) + " " + String.valueOf(strokeSize);
		outOut.println(message);	
	}	
	
	/**
     * Handler for client input, performing requested operations and returning an output message.
     * 
     * @param input message from client
     * @return message to client
     */
    private String handleMessages(String input) {
    	/*
        String regex = "(look)|(dig -?\\d+ -?\\d+)|(flag -?\\d+ -?\\d+)|"
                + "(deflag -?\\d+ -?\\d+)|(help)|(bye)";
        if ( ! input.matches(regex)) {
            // invalid input
            return null;
        }
        */
    	String[] commandArgs = input.split(" ");
		// handles username requests
		if(commandArgs[0].equals("success") && commandArgs[1].equals("username")){
			User user = new User(commandArgs[2]);	
			return "success";		
		} else if (commandArgs[0].equals("retry") && commandArgs[1].equals("username")){
			// use simpleDialogGUI
			return "retry username";
			// tell GUI to re-enter a username (retry username)
		} else {
			if (user != null) {
				if(commandArgs[0].equals("list") && commandArgs[0].equals("whiteboard")) {
					ArrayList<String> newWhiteboardNames = new ArrayList<String>();
					for (int i = 2; i < commandArgs.length; i++) {
						newWhiteboardNames.add(commandArgs[i]);
					}
					setWhiteboardNames(newWhiteboardNames);
					return "success";
				} else if (commandArgs[0].equals("list") && commandArgs[0].equals("whiteboard-user")) {
					ArrayList<String> newUserList = new ArrayList<String>();
					for (int i = 2; i < commandArgs.length; i++) {
						newUserList.add(commandArgs[i]);
					}
					whiteboardUsers = newUserList;
					return "success";
				} else if (commandArgs[0].equals("add")) {
					whiteboardUsers.add(commandArgs[2]);
					return "success";
				} else if (commandArgs[0].equals("retry") && commandArgs[1].equals("whiteboard")) {
					// (retry whiteboard naming)
					// use simpleDialogGUI
					return "retry whiteboard";
				} else if (commandArgs[0].equals("remove")) {
					String usernameToRemove = commandArgs[2];
					int index = whiteboardUsers.indexOf(usernameToRemove);
					if (index != -1) {
						whiteboardUsers.remove(index);
						return "success";
					}
					return "fail";			
				} else if (commandArgs[0].equals("error")) {
					return "error";
				} else if (commandArgs[0].equals("success") && commandArgs[2].equals("join")) {
					Canvas canvas = new Canvas(width, height);
					ArrayList<String> initialUsers = new ArrayList<String>();
					whiteboard = new Whiteboard(commandArgs[2], canvas, initialUsers);
					return "success";
				} else if (commandArgs[0].equals("success") && commandArgs[2].equals("exit")) {
					whiteboard = null;
					return "success";
				} else if (commandArgs[0].equals("draw")) {
					// draw whiteboard [WHITEBOARD NAME] [x1] [y1] [x2] [y2] 
					// [red] [green] [blue] [stroke size]
					if (whiteboard != null) {
						int x1 = Integer.parseInt(commandArgs[3]);
						int y1 = Integer.parseInt(commandArgs[4]);
						int x2 = Integer.parseInt(commandArgs[5]);
						int y2 = Integer.parseInt(commandArgs[6]);
						Color color = new Color(Integer.parseInt(commandArgs[7]), Integer.parseInt(commandArgs[8]), Integer.parseInt(commandArgs[9]));
						int strokeSize = Integer.parseInt(commandArgs[10]);
						LineSegment lineSegment = new LineSegment(x1, y1, x2, y2, color, strokeSize);
						whiteboard.addLineSegment(lineSegment);
						return "success";
					} else {
						return "no whiteboard";
					}
					
				}
			}
        // Should never get here--make sure to return in each of the valid cases above.
        throw new UnsupportedOperationException();
		}
    }
    
	/**
	 * Starts the client thread.
	 */
	@Override
	public void run(){
		try {
			handleConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getWhiteboardNames() {
		return (ArrayList<String>) whiteboardNames.clone();
	}

	public void setWhiteboardNames(ArrayList<String> whiteboardNames) {
		this.whiteboardNames = whiteboardNames;
	}
}
