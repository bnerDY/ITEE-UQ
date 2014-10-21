package sample;

/**
 * 
 * @author Mingyang Zhong
 * Feb. 2014
 * the University of Queensland
 * Code example for course: COMS3200
 * 
 This is simple network program based on Java-IO, UDP blocking mode and single thread. 
 The UDPClient reads inputs from the keyboard then sends it to UDPServer.
 The UDPServer reads packets from the socket channel and convert it to upper case, and then sends back to UDPClient. 
 The program assumes that the data in any received packets will be in string form.
 * 
 */

import java.io.*;
import java.net.*;

class UDPClient {
	public static void main(String args[]) throws Exception {
		// construct datagram socket
		DatagramSocket clientSocket = new DatagramSocket();
		// set server's ip address
		InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
		// set buffers
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		// get the message from user
		System.out.println("Please input your message:");
		BufferedReader inBuf = new BufferedReader(new InputStreamReader(System.in));
		String msg = inBuf.readLine();
		sendData = msg.getBytes();
		// send the message to server
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
		clientSocket.send(sendPacket);
		// receive reply message from server
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		// print the reply
		String upMsg = new String(receivePacket.getData());
		System.out.println("Message from Server:" + upMsg);
		// close up
		clientSocket.close();
	}
}