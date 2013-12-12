package server;

import static org.junit.Assert.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

/**
 * Test 3 different constructors of Packet, and test methods: getUser, getStringData, getType, getQueue.
 * getUser is only called if it is a normal packet and getQueue is only called if it is a queue packet. These
 * 		are checked by assert statements in Packet.
 *
 */
public class PacketTest {
	// test server packet constructor and its pertinent get methods (getType, getStringData)
	@Test
	public void testServerPacketConstructor() {
		String stringData = "data";
		Packet packet = new Packet(stringData);
		assertEquals(packet.getType(), 1);
		assertEquals(packet.getStringData(), stringData);
	}
	
	// test normal packet constructor and its pertinent get methods (getType, getStringData, getUser)
	@Test
	public void testNormalPacketConstructor() {
		String stringData = "data";
		String username = "genghis";
		Packet packet = new Packet(username, stringData);
		assertEquals(packet.getType(), 2);
		assertEquals(packet.getStringData(), stringData);
		assertEquals(packet.getUser(), username);
	}
	
	// test queue packet constructor and its pertinent get methods (getType, getStringData, getQueue)
	@Test
	public void testQueuePacketConstructor() {
		String stringData = "data";
		BlockingQueue<Packet> bq = new LinkedBlockingQueue<Packet>();
		bq.add(new Packet("some message"));
		Packet packet = new Packet(stringData, bq);
		assertEquals(packet.getType(), 0);
		assertEquals(packet.getStringData(), stringData);
		assertEquals(packet.getQueue(), bq);
	}
}
