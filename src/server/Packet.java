package server;

import java.util.concurrent.BlockingQueue;

public class Packet {
	private String user = null;
	private String data = null;
	private BlockingQueue<Packet> queueData = null;
	private int type;
	
	public static int FORWARD_PACKET = 0;
	public static int QUEUE_PACKET = 1;
	public static int SERVER_PACKET = 2;
	public static int NORMAL_PACKET = 3;
	
	public Packet(boolean fromClient, String stringData){
		data = stringData;
		if(fromClient){
			type = Packet.FORWARD_PACKET;
		}
		else{
			type = Packet.SERVER_PACKET;
		}
	}
	
	public Packet(String userName, String stringData){
		data = stringData;
		user = userName;
		type = Packet.NORMAL_PACKET;
	}
	
	public Packet(String userName, String stringData, BlockingQueue<Packet> clientQueue){
		user = userName;
		data = stringData;
		queueData = clientQueue;
		type = Packet.QUEUE_PACKET;
	}
	
	public String getUser(){
		return user;
	}
	
	public String getStringData(){
		return data;
	}
	
	public int getType(){
		return type;
	}
	
	public BlockingQueue<Packet> getQueue(){
		return queueData;
	}
}