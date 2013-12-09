package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WhiteboardServerThread extends Thread {
	private String userName = null;
	private BlockingQueue<Packet> toServerQ;
	private BlockingQueue<Packet> fromServerQ;
	private Socket socket;

	public WhiteboardServerThread(Socket clientSocket, BlockingQueue<Packet> queueToServer){
		socket = clientSocket;
		toServerQ = queueToServer;
		fromServerQ = new LinkedBlockingQueue<Packet>();
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
			while(true){
				if(in.ready()){
					String line = in.readLine();
					String[] commandArgs = line.split(" ", 3);
					// handles username requests
					if(commandArgs[0].equals("add") && commandArgs[1].equals("username")){
						Packet packet = new Packet(userName, line, fromServerQ);
						toServerQ.add(packet);
					}
					// forward all other requests
					else{
						toServerQ.add(new Packet(userName, line));
						if(commandArgs[0].equals("disconnect")){
							break;
						}
					}
				}
				// handle responses back from the data server
				if(!fromServerQ.isEmpty()){
					Packet response = fromServerQ.take();
					String responseString = response.getStringData();
					String[] responseSplit = responseString.split(" ", 3);
					if(responseSplit[0].equals("success") && responseSplit[1].equals("username")){
						userName = responseSplit[2];
						out.println("success username");
					}
					else if(responseString.contains("list whiteboard-user")){
						out.println("success whiteboard join");
						out.println(responseString);
					}
					else{
						out.println(responseString);
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (SocketException e){
			toServerQ.offer(new Packet(userName, "disconnect username " + userName));
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