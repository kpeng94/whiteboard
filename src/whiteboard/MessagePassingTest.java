package whiteboard;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.junit.Before;
import org.junit.Test;

public class MessagePassingTest {
    @Before
    public void setUp() {
        TestUtil.startServer();
    }

    @Test
    public void loginTest() throws IOException, InterruptedException {
        // Avoid race where we try to connect to server too early
        Thread.sleep(100);

        try {
        	// Representing user 1: Parker
            Socket sock = TestUtil.connect();
    	    BufferedReader in = new BufferedReader(new InputStreamReader(
    	            sock.getInputStream()));
    	    PrintWriter out = new PrintWriter(sock.getOutputStream(), true);

    	    // Representing user 2: Genghis
    	    Socket sock2 = TestUtil.connect();
    	    BufferedReader in2 = new BufferedReader(new InputStreamReader(
    	            sock2.getInputStream()));
    	    PrintWriter out2 = new PrintWriter(sock2.getOutputStream(), true);

    	    // This particular test ignores extraneous newlines; other tests may not.

    	    // Parker tries a username
    	    out.println("add username parker");
    	    assertEquals("success username parker", TestUtil.nextNonEmptyLine(in));
    	    assertEquals("list whiteboard", TestUtil.nextNonEmptyLine(in));
    	    
    	    // Parker makes new whiteboard
    	    out.println("create whiteboard whiteboardone");
      	    assertEquals("add whiteboard whiteboardone", TestUtil.nextNonEmptyLine(in));

      	    // Parker joins existing whiteboard
      	    out.println("join whiteboard whiteboardone");
      	    assertEquals("success whiteboard join whiteboardone", TestUtil.nextNonEmptyLine(in));
      	    assertEquals("list whiteboard-user whiteboardone parker", TestUtil.nextNonEmptyLine(in));

      	    // Parker tries to join whiteboard that he already joined
      	    out.println("join whiteboard whiteboardone");
      	    assertEquals("error whiteboard", TestUtil.nextNonEmptyLine(in));

      	    // Parker tries to join nonexisting whiteboard
      	    out.println("join whiteboard whiteboardtwo");
      	    assertEquals("error whiteboard", TestUtil.nextNonEmptyLine(in));
      	    
      	    // Parker draws on whiteboard one
      	    out.println("draw whiteboard whiteboardone 0 0 50 129 12 117 34 5");
    	    assertEquals("draw whiteboard whiteboardone 0 0 50 129 12 117 34 5", 
    	    			 TestUtil.nextNonEmptyLine(in));
    	        	    
    	    // Genghis tries a taken username
    	    out2.println("add username parker");
    	    assertEquals("retry username", TestUtil.nextNonEmptyLine(in2));
    	    
    	    // Genghis tries a not taken username
    	    out2.println("add username genghis");
    	    assertEquals("success username genghis", TestUtil.nextNonEmptyLine(in2));
    	    assertEquals("list whiteboard whiteboardone", TestUtil.nextNonEmptyLine(in2));
    	    
      	    // Genghis makes new whiteboard with taken name
    	    out2.println("create whiteboard whiteboardone");
      	    assertEquals("retry whiteboard naming", TestUtil.nextNonEmptyLine(in2));
      	    
      	    // Genghis join existing whiteboard
      	    out2.println("join whiteboard whiteboardone");
      	    assertEquals("success whiteboard join whiteboardone", TestUtil.nextNonEmptyLine(in2));
      	    assertEquals("add whiteboard-user whiteboardone genghis", TestUtil.nextNonEmptyLine(in));
    	    assertEquals("list whiteboard-user whiteboardone parker genghis", TestUtil.nextNonEmptyLine(in2));
    	    assertEquals("draw whiteboard whiteboardone 0 0 50 129 12 117 34 5", TestUtil.nextNonEmptyLine(in2));

    	    // Genghis draws something
    	    out2.println("draw whiteboard whiteboardone 1 1 2 2 3 3 3 4");
    	    assertEquals("draw whiteboard whiteboardone 1 1 2 2 3 3 3 4", TestUtil.nextNonEmptyLine(in));
    	    assertEquals("draw whiteboard whiteboardone 1 1 2 2 3 3 3 4", TestUtil.nextNonEmptyLine(in2));

      	    // Parker leaves existing whiteboard
    	    out.println("exit whiteboard whiteboardone");
    	    assertEquals("success whiteboard exit whiteboardone", TestUtil.nextNonEmptyLine(in));
    	    assertEquals("remove whiteboard-user whiteboardone parker", TestUtil.nextNonEmptyLine(in2));

    	    out.println("disconnect username parker");
    	    
    	    sock.close();
            
        } catch (SocketTimeoutException e) {
            throw new RuntimeException(e);
        }
        
    }
}
