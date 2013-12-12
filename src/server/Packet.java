package server;

import java.util.concurrent.BlockingQueue;

/**
 * This class represents an immutable packet of information sent between threads
 * in the server. There are three types of packets, indicated by the type.
 * A queue packet is used when a user is requesting a username, and indicates
 * that the packet has a non-null queueData field. 
 * A server packet is used when the server sends messages back to the client and 
 * indicates that both user and queueData are null. 
 * A normal packet is used when
 * the client sends non-username related information to the server.
 *
 */
public class Packet {
	private String user = null;
	private String data = null;
	// Contains a way to send messages back to the user.
	private BlockingQueue<Packet> queueData = null;
	private int type;
	
	public static int QUEUE_PACKET = 0;
	public static int SERVER_PACKET = 1;
	public static int NORMAL_PACKET = 2;
	
	/**
	 * Constructor for creating a server packet, used for server to client communication.
	 * @param stringData - Data being transmitted - must not be null
	 */
	public Packet(String stringData){
		data = stringData;
		type = Packet.SERVER_PACKET;
	}
	
	/**
	 * Constructor for creating a normal packet, used for client to server
	 * communication not involving usernames.
	 * @param userName - Username of the client sending the message - must not be null
	 * @param stringData - Data being transmitted - must not be null
	 */
	public Packet(String userName, String stringData){
		data = stringData;
		user = userName;
		type = Packet.NORMAL_PACKET;
	}
	
	/**
	 * Constructor for creating a queue packet, used for client to server
	 * communication involving usernames.
	 * @param stringData - Data being transmitted (likely username request) - must not be null
	 * @param clientQueue - Queue for server to send back messages to client - must not be null
	 */
	public Packet(String stringData, BlockingQueue<Packet> clientQueue){
		data = stringData;
		queueData = clientQueue;
		type = Packet.QUEUE_PACKET;
	}
	
	/**
	 * Returns the user field of the packet. Only called if
	 * the packet is a normal packet.
	 * @return username of message-sending client
	 */
	public String getUser(){
		assert type == Packet.NORMAL_PACKET;
		return user;
	}
	
	/** 
	 * Returns the string data of the packet, or the requests
	 * being sent.
	 * @return string data of packet
	 */
	public String getStringData(){
		return data;
	}
	
	/**
	 * Returns the type of the packet as an integer.
	 * @return type of packet
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Returns the blocking queue stored in this packet.
	 * Only called if the packet is a queue packet.
	 * @return queue stored in packet
	 */
	public BlockingQueue<Packet> getQueue(){
		assert type == Packet.QUEUE_PACKET;
		return queueData;
	}
}