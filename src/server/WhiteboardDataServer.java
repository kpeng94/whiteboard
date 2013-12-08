package server;

import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import canvas.Whiteboard;

public class WhiteboardDataServer extends Thread {
	private HashMap<String,Whiteboard> whiteboards;
	private HashMap<String,Socket> users;
	private LinkedBlockingQueue<Packet> in;
	
	public WhiteboardDataServer(LinkedBlockingQueue<Packet> incoming){
		whiteboards = new HashMap<String, Whiteboard>();
		users = new HashMap<String, Socket>();
		in = incoming;
	}
	
	//TODO: like everything
	
	private void handleMessages(){
		try {
			for(Packet packet = in.take(); packet != null; packet = in.take()){
				if(packet.getType() == Packet.SOCKET_PACKET){
					
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		handleMessages();
	}
}
