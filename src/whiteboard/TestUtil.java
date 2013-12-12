package whiteboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import server.WhiteboardMainServer;

/**
 * Utility class for helping set up a server-client connection.
 *
 */
public class TestUtil {
    
    private static final int port = 1234;
    private static final String portStr = Integer.toString(port);

    /**
     * Starts the server.
     */
    public static void startServer() {      
      startServer("--port", portStr);
    }

    /**
     * Creates a connection to the server
     * @return Socket for the connection
     * @throws IOException if too many attempts or connection interrupted
     */
    public static Socket connect() throws IOException {
      Socket ret = null;
      final int MAX_ATTEMPTS = 50;
      int attempts = 0;
      do {
        try {
          ret = new Socket("127.0.0.1", port);
        } catch (ConnectException ce) {
          try {
            if (++attempts > MAX_ATTEMPTS)
              throw new IOException("Exceeded max connection attempts", ce);
            Thread.sleep(300);
          } catch (InterruptedException ie) {
            throw new IOException("Unexpected InterruptedException", ie);
          }
        }
      } while (ret == null);
      ret.setSoTimeout(3000);
      return ret;
    }

    /**
     * Starts the server given specific arguments.
     * @param args The arguments
     */
    private static void startServer(final String... args) {
        final String myArgs[] = args;
        new Thread(new Runnable() {
            public void run() {
                try {
                    WhiteboardMainServer.main(myArgs);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }

    /**
     * Gets the string representation of the next non empty line.
     * @param in Buffered reader
     * @return next non empty line 
     * @throws IOException if the buffered reader throws an IOException
     */
    public static String nextNonEmptyLine(BufferedReader in) throws IOException {
        while (true) {
            String ret = in.readLine();
            if (ret == null || !ret.equals(""))
                return ret;
        }
    }
}