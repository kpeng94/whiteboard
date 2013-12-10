package server;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import canvas.Canvas;
import canvas.LineSegment;
import canvas.Whiteboard;

public class WhiteboardDataServer extends Thread {
	private HashMap<String,Whiteboard> whiteboards;
	private HashMap<String,BlockingQueue<Packet>> users;
	private LinkedBlockingQueue<Packet> in;

	public WhiteboardDataServer(LinkedBlockingQueue<Packet> incoming){
		whiteboards = new HashMap<String, Whiteboard>();
		users = new HashMap<String, BlockingQueue<Packet>>();
		in = incoming;
	}

	private void handleMessages(){
		try {
			for(Packet packet = in.take(); packet != null; packet = in.take()){
				String request = packet.getStringData().trim();
				String[] packetInfo = packet.getStringData().split(" ");

				if(packet.getType() == Packet.QUEUE_PACKET){
					if(packetInfo.length != 3 || users.containsKey(packetInfo[2].toLowerCase(Locale.ENGLISH))){
						packet.getQueue().offer(new Packet(false, "retry username"));
					}
					else{
						String newUserName = packetInfo[2].toLowerCase(Locale.ENGLISH);
						users.put(newUserName, packet.getQueue());
						packet.getQueue().offer(new Packet(false, "success username " + newUserName));
						packet.getQueue().offer(new Packet(false, createWhiteboardList()));
					}
				}
				else{
					if(packetInfo[0].equals("disconnect")){
						String dcUser = packetInfo[2].toLowerCase(Locale.ENGLISH);
						users.remove(dcUser);
						for(Whiteboard whiteboard: whiteboards.values()){
							if(whiteboard.getUsers().contains(dcUser)){
								whiteboard.removeUser(dcUser);
								for(String user: whiteboard.getUsers()){
									users.get(user).offer(new Packet(false, "remove whiteboard-user " + dcUser));
								}
								break;
							}
						}
					}
					else if(packetInfo[0].equals("create")){
						if(packetInfo.length != 3 || whiteboards.containsKey(packetInfo[2].toLowerCase(Locale.ENGLISH))){
							users.get(packet.getUser()).offer(new Packet(false, "retry whiteboard naming"));
						}
						else{
							String whiteboardName = packetInfo[2].toLowerCase(Locale.ENGLISH);
							whiteboards.put(whiteboardName, new Whiteboard(packetInfo[2], new Canvas(800,600), new ArrayList<String>()));
							for(BlockingQueue<Packet> bq: users.values()){
								bq.offer(new Packet(false, createWhiteboardList()));
							}
						}
					}
					else if(packetInfo[0].equals("join")){
						if(packetInfo.length == 3 && whiteboards.containsKey(packetInfo[2].toLowerCase(Locale.ENGLISH))){
							String whiteboardName = packetInfo[2].toLowerCase(Locale.ENGLISH);							
							Whiteboard whiteboard = whiteboards.get(whiteboardName);
							String newUser = packet.getUser();
							users.get(newUser).offer(new Packet(false, "success whiteboard join " + whiteboardName));
							users.get(newUser).offer(new Packet(false, createUserList(whiteboardName)));
							whiteboard.addUser(newUser);
							for(String user: whiteboard.getUsers()){
								users.get(user).offer(new Packet(false, "add whiteboard-user " + newUser));
							}
						}
						else{
							users.get(packet.getUser()).offer(new Packet(false, "error whiteboard"));
						}
					}
					else if(packetInfo[0].equals("exit")){
						String whiteboardName = packetInfo[2].toLowerCase(Locale.ENGLISH);
						Whiteboard whiteboard = whiteboards.get(whiteboardName);
						String exitUser = packet.getUser();
						whiteboard.removeUser(exitUser);
						users.get(exitUser).offer(new Packet(false, "success whiteboard exit"));
						for(String user: whiteboard.getUsers()){
							users.get(user).offer(new Packet(false, "remove whiteboard-user " + exitUser));
						}
					}
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
							users.get(user).offer(new Packet(false, request));
						}
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String createUserList(String whiteboardName){
		Whiteboard whiteboard = whiteboards.get(whiteboardName);
		StringBuilder request = new StringBuilder();
		request.append("list whiteboard-user ");
		for(String user: whiteboard.getUsers()){
			request.append(user + " ");
		}
		return request.substring(0, request.length()-1);
	}

	private String createWhiteboardList(){
		StringBuilder request = new StringBuilder();
		request.append("list whiteboard ");
		for(String whiteboard: whiteboards.keySet()){
			request.append(whiteboard + " ");
		}
		return request.substring(0, request.length()-1);
	}

	@Override
	public void run(){
		handleMessages();
	}
}