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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Set;

public class UDPClientNIO {

	public static void main(String[] args) {
		new UDPClientNIO();
	}

	public UDPClientNIO() {
		DatagramChannel channel = null;
		try {
			// set port from connection
			int port = 9001;
			// open datagram channel
			channel = DatagramChannel.open();
			// set Blocking mode to non-blocking
			channel.configureBlocking(false);
			// set Server info
			SocketAddress sa = new InetSocketAddress("127.0.0.1", port);
			// open selector
			Selector selector = Selector.open();
			// connect to Server
			channel.connect(sa);
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			// registers this channel with the given selector, returning a selection key
			channel.register(selector, SelectionKey.OP_WRITE);

			while (selector.select() > 0) {
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				for (SelectionKey selectionKey : selectedKeys) {
					// test whether this key's channel is ready for writing to Server
					if (selectionKey.isWritable()) {
						DatagramChannel dc = (DatagramChannel) selectionKey.channel();
						// Client input
						BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
						System.out.println("Please input your message:");
						// read Client input
						String command = "";
						while ((command = systemIn.readLine()).trim().isEmpty()) {
						}
						dc.write(Charset.forName("UTF-8").encode(command));
						// set register status to READ
						if ("exit".equalsIgnoreCase(command.trim())) {
							// close everything
							systemIn.close();
							selector.close();
							System.out.println("Client exit !");
							break;
						}
						dc.register(selector, SelectionKey.OP_READ);
					}
					// test whether this key's channel is ready for reading from Server
					else if (selectionKey.isReadable()) {
						DatagramChannel dc = (DatagramChannel) selectionKey.channel();
						dc.read(buffer);
						buffer.flip();

						System.out.println(Charset.forName("UTF-8").decode(buffer).toString());
						buffer.clear();
						// set register status to WRITE
						dc.register(selector, SelectionKey.OP_WRITE);
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

}
