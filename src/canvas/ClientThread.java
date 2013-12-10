package canvas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import server.Packet;

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
	private LinkedBlockingQueue<String> incomingQueue; 
	
	private PrintWriter outOut;

	public ClientThread(Socket clientSocket){
		socket = clientSocket;
		incomingQueue = new LinkedBlockingQueue<String>();
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
        				String output = handleMessages(input);
        				//process the in;
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
			return "success username";		
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
					whiteboardNames = newWhiteboardNames;
					return "list of whiteboards updated";
				} else if (commandArgs[0].equals("list") && commandArgs[0].equals("whiteboard-user")) {
					ArrayList<String> newUserList = new ArrayList<String>();
					for (int i = 2; i < commandArgs.length; i++) {
						newUserList.add(commandArgs[i]);
					}
					whiteboardUsers = newUserList;
					return "list of whiteboard users updated";
				} else if (commandArgs[0].equals("add")) {
					whiteboardUsers.add(commandArgs[2]);
					return "user added";
				} else if (commandArgs[0].equals("retry") && commandArgs[1].equals("whiteboard")) {
					// (retry whiteboard naming)
					// use simpleDialogGUI
					return "retry whiteboard";
				} else if (commandArgs[0].equals("remove")) {
					String usernameToRemove = commandArgs[2];
					int index = whiteboardUsers.indexOf(usernameToRemove);
					if (index != -1) {
						whiteboardUsers.remove(index);
						return "user removed";
					}
					return "failed to remove";			
				} else if (commandArgs[0].equals("error")) {
					// (error whiteboard)
					return "error";
				} else if (commandArgs[0].equals("success") && commandArgs[2].equals("join")) {
					// success whiteboard join [WHITEBOARD]
					return "success join";
				} else if (commandArgs[0].equals("success") && commandArgs[2].equals("exit")) {
					// (success whiteboard exit)
					return "success exit";
				} else if (commandArgs[0].equals("draw")) {
					// (draw whiteboard [WHITEBOARD NAME] [x1] [y1] [x2] [y2] 
					// [red] [green] [blue] [stroke size])
				}
			}
        // Should never get here--make sure to return in each of the valid cases above.
        throw new UnsupportedOperationException();
		}
    }
    
    /*
    public String sendMessage(String message) {
    	
    }
    */
	

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
}
