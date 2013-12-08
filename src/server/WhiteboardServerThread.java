package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class WhiteboardServerThread extends Thread {
	private String whiteboardName = null;
	private String userName = null;
	private BlockingQueue<Packet> toServerQ;
	private Socket socket;
	
	public WhiteboardServerThread(Socket clientSocket, BlockingQueue<Packet> queueToServer){
		socket = clientSocket;
		toServerQ = queueToServer;
	}
	
	/**
	 * Handle a single client connection. Returns when client disconnects.
	 * 
	 * @throws IOException if connection has an error or terminates unexpectedly
	 */
	private void handleConnection() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		try {
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				//TODO: add parsing of incoming messages
			}
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
