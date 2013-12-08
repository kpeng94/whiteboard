package server;

import java.net.Socket;

public class Packet {
	private String data;
	private Socket socketData = null;
	private int type;
	
	public static int STRING_PACKET = 0;
	public static int SOCKET_PACKET = 1;
	
	public Packet(String stringData){
		data = stringData;
		type = Packet.STRING_PACKET;
	}
	
	public Packet(String stringData, Socket clientSocket){
		data = stringData;
		socketData = clientSocket;
	}
	
	public Object getStringData(){
		return data;
	}
	
	public int getType(){
		return type;
	}
	
	public Socket getSocketData(){
		return socketData;
	}
}
