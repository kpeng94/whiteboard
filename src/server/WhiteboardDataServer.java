package server;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import canvas.LineSegment;
import canvas.Whiteboard;

/**
 * Handles all of the data processing of the whiteboard server. This class
 * will always be created implicitly when running the main whiteboard server.
 * It listens to a shared blocking queue for client requests and appropriately
 * returns messages by use of individualized return blocking queues. This is also
 * where all whiteboard data is stored, as well as information about the users currently
 * on the server.
 *
 */
public class WhiteboardDataServer extends Thread {
	// Holds all whiteboard information, hashed by the name.
	private HashMap<String,Whiteboard> whiteboards;
	// Holds the individualized client queue, hashed by username.
	// These queues are used to send messages back to clients.
	private HashMap<String,BlockingQueue<Packet>> users;
	// This queue is shared between all clients, and is used
	// for sending messages to this data server.
	private LinkedBlockingQueue<Packet> in;

	/**
	 * Constructor for the data server.
	 * @param incoming - shared queue for client thread to data thread communication - must not be null
	 */
	public WhiteboardDataServer(LinkedBlockingQueue<Packet> incoming){
		whiteboards = new HashMap<String, Whiteboard>();
		users = new HashMap<String, BlockingQueue<Packet>>();
		in = incoming;
	}

	/**
	 * Takes packets from the shared blocking queue and processes them depending on the
	 * data inside the packet. The method also handles sending back appropriate messages
	 * to the client by using the client's personalized blocking queue.
	 */
	private void handleMessages(){
		try {
			// Block on the BlockingQueue
			for(Packet packet = in.take(); packet != null; packet = in.take()){
				// Trim the string data and separate it for analysis
				String request = packet.getStringData().trim();
				String[] packetInfo = packet.getStringData().split(" ");

				// PROTOCOL: add username [USERNAME]
				// If it's a queue packet, we know we're dealing with a username request
				if(packet.getType() == Packet.QUEUE_PACKET){
					// Reject usernames with a space and usernames already taken by another person
					if(packetInfo.length != 3 || users.containsKey(packetInfo[2].toLowerCase(Locale.ENGLISH))){
						packet.getQueue().offer(new Packet("retry username"));
					}
					// If the username is okay, tell the client they're good to go and a list of whiteboards
					else{
						String newUserName = packetInfo[2].toLowerCase(Locale.ENGLISH);
						users.put(newUserName, packet.getQueue());
						packet.getQueue().offer(new Packet("success username " + newUserName));
						packet.getQueue().offer(new Packet(createWhiteboardList()));
					}
				}
				else{
					// PROTOCOL: disconnect username [USERNAME]
					// Disconnect a username, and if user was in whiteboards, remove and notify other users
					if(packetInfo[0].equals("disconnect")){
						String dcUser = packetInfo[2].toLowerCase(Locale.ENGLISH);
						users.remove(dcUser);
						for(Whiteboard whiteboard: whiteboards.values()){
							if(whiteboard.getUsers().contains(dcUser)){
								whiteboard.removeUser(dcUser);
								for(String user: whiteboard.getUsers()){
									users.get(user).offer(new Packet("remove whiteboard-user " + dcUser));
								}
							}
						}
					}
					// PROTOCOL: create whiteboard [WHITEBOARD]
					// Process a whiteboard name
					else if(packetInfo[0].equals("create")){
						// If whiteboard name contains space or name is already taken, reject it
						if(packetInfo.length != 3 || whiteboards.containsKey(packetInfo[2].toLowerCase(Locale.ENGLISH))){
							users.get(packet.getUser()).offer(new Packet("retry whiteboard naming"));
						}
						// If name is okay, tell all clients a new whiteboard was made and create the whiteboard
						else{
							String whiteboardName = packetInfo[2].toLowerCase(Locale.ENGLISH);
							whiteboards.put(whiteboardName, 
											new Whiteboard(packetInfo[2], new ArrayList<String>()));
							for(BlockingQueue<Packet> bq: users.values()){
								bq.offer(new Packet(createWhiteboardList()));
							}
						}
					}
					// PROTOCOL: join whiteboard [WHITEBOARD]
					// PROTOCOL: exit whiteboard [WHITEBOARD]
					// User joining/exiting a whiteboard
					else if(packetInfo[0].equals("join") || packetInfo[0].equals("exit")){
						boolean isJoin = packetInfo[0].equals("join");
						// Makes sure the whiteboard actually exists, notify users in the whiteboard about the user
						// change and tell the changing user if the operation was successful
						if(packetInfo.length == 3 && whiteboards.containsKey(packetInfo[2].toLowerCase(Locale.ENGLISH))){
							String whiteboardName = packetInfo[2].toLowerCase(Locale.ENGLISH);							
							Whiteboard whiteboard = whiteboards.get(whiteboardName);
							String user = packet.getUser();
							// Sends messages if the user is joining
							if(isJoin){
								users.get(user).offer(new Packet("success whiteboard join " + whiteboardName));
								for(String otherUsers: whiteboard.getUsers()){
									users.get(otherUsers).offer(new Packet("add whiteboard-user " + user));
								}
								whiteboard.addUser(user);
								users.get(user).offer(new Packet(createUserList(whiteboardName)));
								for(LineSegment segment: whiteboard.getLineSegments()){
									users.get(user).offer(new Packet("draw whiteboard " + whiteboardName
											 + " " + segment.toString()));
								}
							}
							// Sends messages if the user is exiting
							else{
								// True if the user was actually in the whiteboard, false if the user wasn't
								boolean success = whiteboard.removeUser(user);
								if(success){
									users.get(user).offer(new Packet("success whiteboard exit"));
									for(String otherUsers: whiteboard.getUsers()){
										users.get(otherUsers).offer(new Packet("remove whiteboard-user " + user));
									}
								}
								else{
									users.get(packet.getUser()).offer(new Packet("error whiteboard"));
								}
							}
						}
						// Otherwise tell the user the operation was unsuccessful
						else{
							users.get(packet.getUser()).offer(new Packet("error whiteboard"));
						}
					}
					// PROTOCOL: draw whiteboard [WHITEBOARD] [INT] [INT] [INT] [INT] [INT] [INT] [INT] [INT]
					// Draws a line segment on the server version of the whiteboard and sends out messages
					// to connected users to do the same
					else if(packetInfo[0].equals("draw")){
						String whiteboardName = packetInfo[2].toLowerCase(Locale.ENGLISH);
						Whiteboard whiteboard = whiteboards.get(whiteboardName);
						Color color = new Color(Integer.parseInt(packetInfo[7]), Integer.parseInt(packetInfo[8]),
								Integer.parseInt(packetInfo[9]));
						LineSegment lineSeg = new LineSegment(Integer.parseInt(packetInfo[3]), Integer.parseInt(packetInfo[4]),
								Integer.parseInt(packetInfo[5]), Integer.parseInt(packetInfo[6]),
								color, Integer.parseInt(packetInfo[10]));
						whiteboard.addLineSegment(lineSeg);
						
						for(String user: whiteboard.getUsers()){
							users.get(user).offer(new Packet(request));
						}
					}
				}
			}
		} catch (InterruptedException e) {
			;
		}
	}

	/**
	 * A helper function to create a list of users connected to 
	 * a given whiteboard.
	 * @param whiteboardName - Name of whiteboard - must be a valid whiteboard name
	 * @return list of usernames connected to the whiteboard
	 */
	private String createUserList(String whiteboardName){
		Whiteboard whiteboard = whiteboards.get(whiteboardName);
		StringBuilder request = new StringBuilder();
		request.append("list whiteboard-user ");
		for(String user: whiteboard.getUsers()){
			request.append(user + " ");
		}
		return request.substring(0, request.length()-1);
	}

	/**
	 * A helper function to create a list of whiteboards created
	 * on the server.
	 * @return list of whiteboard names already on the server
	 */
	private String createWhiteboardList(){
		StringBuilder request = new StringBuilder();
		request.append("list whiteboard ");
		for(String whiteboard: whiteboards.keySet()){
			request.append(whiteboard + " ");
		}
		return request.substring(0, request.length()-1);
	}

	/**
	 * Starts the data server thread.
	 */
	@Override
	public void run(){
		handleMessages();
	}
}