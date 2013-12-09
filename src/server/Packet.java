package server;

import java.util.concurrent.BlockingQueue;

public class Packet {
	private String user = null;
	private String data = null;
	private BlockingQueue<Packet> queueData = null;
	private int type;
	
	public static int STRING_PACKET = 0;
	public static int QUEUE_PACKET = 1;
	public static int SERVER_PACKET = 2;
	
	public Packet(String dataToSend){
		data = dataToSend;
		type = Packet.SERVER_PACKET;
	}
	
	public Packet(String userName, String stringData){
		user = userName;
		data = stringData;
		type = Packet.STRING_PACKET;
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
