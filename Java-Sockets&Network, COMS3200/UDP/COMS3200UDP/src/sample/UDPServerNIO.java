package sample;
/**
 * 
 * @author Mingyang Zhong
 * Feb. 2014
 * the University of Queensland
 * Code example for course: COMS3200
 * 
 This is simple network program based on Java-NIO, UDP non-blocking mode and single thread. 
 The UDPClient reads inputs from the keyboard then sends it to UDPServer.
 The UDPServer reads packets from the socket channel and convert it to upper case, and then sends back to UDPClient. 
 The program assumes that the data in any received packets will be in string form.
 Typing 'exit' will close the program.
 * 
 */

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class UDPServerNIO {

	// set Server parameters
	private int port = 9001;
	private Selector selector = null;
	private DatagramChannel channel = null;
	private DatagramSocket socket = null;

	public UDPServerNIO() {
		try {
			// open selector
			selector = Selector.open();
			// open datagram channel
			channel = DatagramChannel.open();
			// set the socket associated with this channel
			socket = channel.socket();
			// set Blocking mode to non-blocking
			channel.configureBlocking(false);
			// bind port
			socket.bind(new InetSocketAddress(port));
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			// registers this channel with the given selector, returning a selection key
			channel.register(selector, SelectionKey.OP_READ, buffer);
			System.out.println("<UDPServer> Server is activated.");

			while (selector.select() > 0) {
				for (SelectionKey key : selector.selectedKeys()) {
					// test whether this key's channel is ready for reading from Client
					if (key.isReadable()) {
						DatagramChannel dc = (DatagramChannel) key.channel();
						// get allocated buffer with size 1024
						ByteBuffer readBuffer = (ByteBuffer) key.attachment();
						// receive from the channel into the buffer
						SocketAddress sa = dc.receive(readBuffer);
						readBuffer.flip();
						CharBuffer charBuffer = Charset.forName("UTF-8").decode(readBuffer);
						System.out.println("Message from Client" + sa.toString() + ": " + charBuffer.toString());
						readBuffer.clear();
						// if exit, close everything
						if ("exit".equalsIgnoreCase(charBuffer.toString().trim())) {
							System.out.println("Server has been shutdown!");
							dc.close();
							selector.close();
						} else {
							List<Object> objList = new ArrayList<Object>();
							objList.add(sa);
							objList.add(charBuffer.toString().toUpperCase());
							// set register status to WRITE
							dc.register(key.selector(), SelectionKey.OP_WRITE, objList);
						}
					}
					// test whether this key's channel is ready for sending to Client
					else if (key.isWritable()) {
						DatagramChannel dc = (DatagramChannel) key.channel();
						List<?> objList = (ArrayList<?>) key.attachment();
						SocketAddress sa = (SocketAddress) objList.get(0);
						String reply = (String) objList.get(1);
						ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
						writeBuffer.put(Charset.forName("UTF-8").encode(reply));
						writeBuffer.flip();
						dc.send(writeBuffer, sa);
						writeBuffer.clear();
						// set register status to READ
						dc.register(selector, SelectionKey.OP_READ, writeBuffer);
					}
				}
				if (!selector.isOpen()) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new UDPServerNIO();
	}

}
