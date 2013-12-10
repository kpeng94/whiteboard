package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WhiteboardServerThread extends Thread {
	private String userName = null;
	private BlockingQueue<Packet> toServerQ;
	private BlockingQueue<Packet> incomingQ;
	private Socket socket;

	public WhiteboardServerThread(Socket clientSocket, BlockingQueue<Packet> queueToServer){
		socket = clientSocket;
		toServerQ = queueToServer;
		incomingQ = new LinkedBlockingQueue<Packet>();
	}

	/**
	 * Handle a single client connection. Returns when client disconnects.
	 * 
	 * @throws IOException if connection has an error or terminates unexpectedly
	 */
	private void handleConnection() throws IOException {
		final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		final PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		// Create a new thread for listening to the socket
		Thread clientBlocker = new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					for(String line = in.readLine(); line != null; line = in.readLine()){
						incomingQ.offer(new Packet(true, line));
					}
				} catch (IOException e){
					;	
				}
			}
		});
		
		clientBlocker.start();

		try {
			// handle responses in the blocking queue
			for(Packet response = incomingQ.take(); response != null; response = incomingQ.take()){
				String responseString = response.getStringData();
				String[] responseSplit = responseString.split(" ", 3);
				if(response.getType() == Packet.FORWARD_PACKET){
					// handles username requests
					if(responseSplit[0].equals("add") && responseSplit[1].equals("username")){
						Packet packet = new Packet(userName, responseString, incomingQ);
						toServerQ.add(packet);
					}
					// forward all other requests
					else{
						toServerQ.add(new Packet(userName, responseString));
						if(responseSplit[0].equals("disconnect")){
							break;
						}
					}
				}
				else if(response.getType() == Packet.SERVER_PACKET){
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