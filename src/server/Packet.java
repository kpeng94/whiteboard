package server;

import java.net.Socket;

public class Packet {
	private String user;
	private String data;
	private String whiteboard = null;
	private Socket socketData = null;
	private Boolean success = null;
	private int type;
	
	public static int STRING_PACKET = 0;
	public static int SOCKET_PACKET = 1;
	public static int WHITEBOARD_PACKET = 2;
	
	public Packet(String userName, String stringData){
		user = userName;
		data = stringData;
		type = Packet.STRING_PACKET;
	}
	
	public Packet(String userName, String stringData, Socket clientSocket){
		user = userName;
		data = stringData;
		socketData = clientSocket;
		type = Packet.SOCKET_PACKET;
	}
	
	public Packet(String userName, String whiteboardName, String stringData){
		user = userName;
		data = stringData;
		whiteboard = whiteboardName;
		type = Packet.WHITEBOARD_PACKET;
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
	
	public Socket getSocketData(){
		return socketData;
	}
	
	public String getWhiteboard(){
		return whiteboard;
	}
	
	public Boolean getStatus(){
		return success;
	}
	
	public void processAsSuccess(){
		success = true;
	}
	
	public void processAsFailure(){
		success = false;
	}
}
