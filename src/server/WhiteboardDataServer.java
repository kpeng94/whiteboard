package server;

import java.awt.Color;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
				String request = packet.getStringData();
				String[] packetInfo = packet.getStringData().split(" ");

				if(packet.getType() == Packet.QUEUE_PACKET){
					String newUserName = packetInfo[2];
					if(users.containsKey(newUserName)){
						packet.getQueue().offer(new Packet("retry username"));
					}
					else{
						users.put(packetInfo[2], packet.getQueue());
						packet.getQueue().offer(new Packet("success username " + packetInfo[2]));
						packet.getQueue().offer(new Packet(createWhiteboardList()));
					}
				}
				else{
					if(packetInfo[0].equals("disconnect")){
						users.remove(packetInfo[2]);
					}
					else if(packetInfo[0].equals("create")){
						if(whiteboards.containsKey(packetInfo[2])){
							users.get(packet.getUser()).offer(new Packet("retry whiteboard naming"));
						}
						else{
							//TODO: update with proper whiteboard constructor
							whiteboards.put(packetInfo[2], new Whiteboard());
							for(BlockingQueue<Packet> bq: users.values()){
								bq.offer(new Packet(createWhiteboardList()));
							}
						}
					}
					else if(packetInfo[0].equals("join")){
						if(whiteboards.containsKey(packetInfo[2])){
							Whiteboard whiteboard = whiteboards.get(packetInfo[2]);
							String newUser = packet.getUser();
							users.get(newUser).offer(new Packet(createUserList(packetInfo[2])));
							whiteboard.addUser(newUser);
							for(String user: whiteboard.getUsers()){
								users.get(user).offer(new Packet("add whiteboard-user " + newUser));
							}
						}
						else{
							users.get(packet.getUser()).offer(new Packet("error whiteboard"));
						}
					}
					else if(packetInfo[0].equals("exit")){
						Whiteboard whiteboard = whiteboards.get(packetInfo[2]);
						String exitUser = packet.getUser();
						whiteboard.removeUser(exitUser);
						for(String user: whiteboard.getUsers()){
							users.get(user).offer(new Packet("remove whiteboard-user " + exitUser));
						}
					}
					else if(packetInfo[0].equals("draw")){
						Whiteboard whiteboard = whiteboards.get(packetInfo[2]);
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
