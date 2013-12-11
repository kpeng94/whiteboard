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
            Socket sock = TestUtil.connect();
    	    BufferedReader in = new BufferedReader(new InputStreamReader(
    	            sock.getInputStream()));
    	    PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
    	
    	    // This particular test ignores extraneous newlines; other tests may not.
    	    out.println("add username parker");
    	    assertEquals("success username parker", TestUtil.nextNonEmptyLine(in));
    	    assertEquals("list whiteboard", TestUtil.nextNonEmptyLine(in));

    	    out.println("create whiteboard whiteboardone");
      	    assertEquals("add whiteboard whiteboardone", TestUtil.nextNonEmptyLine(in));
    	    
      	    out.println("join whiteboard whiteboardone");
      	    assertEquals("success whiteboard join whiteboardone", TestUtil.nextNonEmptyLine(in));
      	    assertEquals("list whiteboard-user whiteboardone parker", TestUtil.nextNonEmptyLine(in));
      	    
      	    out.println("draw whiteboard whiteboardone 0 0 50 129 12 117 34 5");
    	    assertEquals("draw whiteboard whiteboardone 0 0 50 129 12 117 34 5", 
    	    			 TestUtil.nextNonEmptyLine(in));

    	    out.println("exit whiteboard whiteboardone");
    	    assertEquals("success whiteboard exit whiteboardone", TestUtil.nextNonEmptyLine(in));
    	        	    
    	    out.println("disconnect username parker");
    	    sock.close();
            
        } catch (SocketTimeoutException e) {
            throw new RuntimeException(e);
        }
        
    }
}
