package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handles connecting users and is the start point for the 
 * whiteboard server. After creating the data server on a different
 * thread, the instance will be used exclusively to listen 
 * to a server port and accept incoming connections from clients.
 * This class does not perform any of the whiteboard data processing;
 * instead, WhiteboardDataServer handles all messages and whiteboard
 * data.
 *
 */
public class WhiteboardMainServer {
	   private final ServerSocket serverSocket;
	   private final LinkedBlockingQueue<Packet> blockQ;
	   private final WhiteboardDataServer dataServer;
	    
	    /**
	     * Make a WhiteboardServer that listens for connections on the port.
	     *  
	     * @param port - port number, requires 0 <= port <= 65535
	     */
	    public WhiteboardMainServer(int port) throws IOException {
	        serverSocket = new ServerSocket(port);
	        blockQ = new LinkedBlockingQueue<Packet>();
	        dataServer = new WhiteboardDataServer(blockQ);
	        dataServer.start();
	    }

	    /**
	     * Run the server, listening for client connections and handling them.
	     * Never returns unless an exception is thrown.
	     * 
	     * @throws IOException if the main server socket is broken
	     *                     (IOExceptions from individual clients do *not* terminate serve())
	     */
	    public void serve() throws IOException {
	        while (true) {
	            // block until a client connects
	            Socket socket = serverSocket.accept();
	            
	            // handle the client by creating a new thread
	            new WhiteboardServerThread(socket, blockQ).start();
	        }
	    }

	    public static void main(String[] args) {
	        int port = 4444; // default port

	        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
	        try {
	            while (!arguments.isEmpty()) {
	                String flag = arguments.remove();
	                try {
	                	if (flag.equals("--port")) {
	                        port = Integer.parseInt(arguments.remove());
	                        if (port < 0 || port > 65535) {
	                            throw new IllegalArgumentException("port " + port + " out of range");
	                        }
	                    } else {
	                        throw new IllegalArgumentException("unknown option: \"" + flag + "\"");
	                    }
	                } catch (NoSuchElementException nsee) {
	                    throw new IllegalArgumentException("missing argument for " + flag);
	                } catch (NumberFormatException nfe) {
	                    throw new IllegalArgumentException("unable to parse number for " + flag);
	                }
	            }
	        } catch (IllegalArgumentException iae) {
	            System.err.println(iae.getMessage());
	            System.err.println("usage: WhiteboardMainServer [--port PORT]");
	            return;
	        }

	        try {
	            runWhiteboardServer(port);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * Creates a whiteboard server and runs it based off the port number.
	     * @param port - port number
	     * @throws IOException
	     */
	    public static void runWhiteboardServer(int port) throws IOException {
	        WhiteboardMainServer server;
	        
	        server = new WhiteboardMainServer(port);
	        server.serve();
	    }
}