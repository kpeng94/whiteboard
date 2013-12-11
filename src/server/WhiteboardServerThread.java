package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Directly handles communication between the client and the
 * central data server. All messages to and from an individual client
 * will run through this class.
 *
 */
public class WhiteboardServerThread extends Thread {
	// Username begins as null and will be set to non-null
	// when the user picks a valid username.
	private String userName = null;
	
	// Shared queue to send packets to the central data server. 
	private final BlockingQueue<Packet> toServerQ;

	// Individual queue to receive packets from the central data server.
	private final BlockingQueue<Packet> incomingQ;
	private final Socket socket;

	/**
	 * Creates a client thread for a connecting user.
	 * @param clientSocket - socket connecting the server to the user
	 * @param queueToServer - shared queue for sending data to data server
	 */
	public WhiteboardServerThread(Socket clientSocket, BlockingQueue<Packet> queueToServer){
		socket = clientSocket;
		toServerQ = queueToServer;
		incomingQ = new LinkedBlockingQueue<Packet>();
	}

	/**
	 * Handle a single client connection. Returns when client disconnects. Also creates
	 * a helper thread that listens to the socket for messages from the client. The primary
	 * thread listens for messages from the central data server.
	 * 
	 * @throws IOException if connection has an error or terminates unexpectedly
	 */
	private void handleConnection() throws IOException {
		final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		final PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		// Create a new thread for listening to the socket and processes the information
		Thread clientBlocker = new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					for(String response = in.readLine(); response != null; response = in.readLine()){
						String[] responseSplit = response.split(" ", 3);
						// handles username requests
						if(responseSplit[0].equals("add") && responseSplit[1].equals("username")){
							Packet packet = new Packet(response, incomingQ);
							toServerQ.add(packet);
						}
						// forward all other requests
						else{
							toServerQ.add(new Packet(userName, response));
							if(responseSplit[0].equals("disconnect")){
								break;
							}
						}
					}
				} catch (IOException e){
					// Tell the server that the client has disappeared
					toServerQ.add(new Packet(userName, "disconnect username " + userName));
				}
			}
		});
		
		clientBlocker.start();

		try {
			// Handle responses from the server and forwards them to the client
			for(Packet response = incomingQ.take(); response != null; response = incomingQ.take()){
				String responseString = response.getStringData();
				String[] responseSplit = responseString.split(" ", 3);
				if(response.getType() == Packet.SERVER_PACKET){
					// If username request succeeded, set the username
					if(responseSplit[0].equals("success") && responseSplit[1].equals("username")){
						userName = responseSplit[2];
					}
					out.println(responseString);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			out.close();
			in.close();
			socket.close();
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
}