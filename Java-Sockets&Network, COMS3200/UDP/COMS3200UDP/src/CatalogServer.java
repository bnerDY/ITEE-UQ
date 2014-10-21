import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Catalog Server using UDP socket
 */
public class CatalogServer {
	
	/**
	 * Read catalog file and store the values in the arraylist
	 * @return Arraylist
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static List<String> ReadFile() throws IOException{
		String line;
		List<String> Catalogfile = new ArrayList<String>();
		FileReader fd = new FileReader("catalog-file.txt");
		BufferedReader r = new BufferedReader(fd);
		while ((line = r.readLine()) != null) {
			String[] res = line.split("( \")");
			String bookID = res[0];
			String title = res[1];
			String author = res[2];
			Catalogfile.add(bookID + " \"" + title + " \"" + author);
		}
		return Catalogfile;
	}
	
	/**
	 * Send the message to Name Server.
	 * @param Socket, msg, port
	 * @param port
	 * Get two variables. This function is used for sending the message.
	 */
	public static void sendToNs(DatagramSocket socket, String msg, int port){
		try{
			DatagramPacket packet = 
					new DatagramPacket(msg.getBytes(), msg.length(), 
									InetAddress.getLocalHost(), port);
			socket.send(packet);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Receive message from Name Server
	 * @param Socket
	 * @return String incoming message for the requests
	 * @throws Exception
	 */
	public static String receiveFromNs(DatagramSocket Socket) throws Exception{
		String inMessage = null;
		byte[] inData = new byte[1024];
		DatagramPacket inPacket = new DatagramPacket(inData, inData.length);
		try{
			// receive reply message from server
			Socket.receive(inPacket);
			inMessage = new String(inPacket.getData());
		} catch(Exception e){  
            e.printStackTrace();  
		}
		return inMessage;
	}
	
	/**
	 * 
	 * @param args[0] = port, The Catalog Server takes one argument.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		try{
			int CatalogServerPort = 0;
			DatagramSocket catalogSocket = new DatagramSocket(0);
			CatalogServerPort = catalogSocket.getLocalPort(); 
			String ServerName = "CatalogServer";
			if (args.length != 1) {
			System.err.println("Invalid command line argument\n");
			System.exit(1);//Error exit
			}
			int nameServerPort = Integer.parseInt(args[0]);

			if (nameServerPort < 1024 || nameServerPort > 65535) {
				System.err.println("Invalid command line argument " +
					"for Catalog Server\n");
				System.exit(1);//Incorrect port number
			}
			List<String> Clist = ReadFile();
			//Act as client below. Sending the register rquest to Name Server. 
			String reply=null;
			Double p; //probability
			boolean response = true;
			int attempt = 0;
			int times = 10;
			// construct datagram socket
			DatagramSocket toNsSocket = new DatagramSocket();
			//Sending the message to NameServer.
			while (true){
				p = Math.random();
				if(p >= 0.5){
					sendToNs(toNsSocket, "r" + " "+ ServerName + " " + 
							CatalogServerPort+" ", nameServerPort);
					System.out.println("Send request successfully");
					timeoutThread Timer = new timeoutThread();
					Timer.start();
					reply = receiveFromNs(toNsSocket);
					response = Timer.checkReceive();
					if(response){
						//print result
						System.out.println("Response: "+ reply);
						break;
					} else {
						System.out.println((times - attempt) + 
								" more attempts...Total attempts: "+ attempt );
						attempt ++;
						if(attempt > times) {
							System.err.println("Unable to reach NameServer");
							System.exit(1);
						}
						System.out.println("Resending...");
					}
				} else{
					System.out.println("Packet loss. Tring to resend...");
				}
			}
			System.out.println("CatalogServer waiting for incoming "
					+ "connections...");
			//Processing the request from client below
			while (true) {
				// set output&input buffers
				byte[] fromClient = new byte[1024];
				byte[] toClient = new byte[1024];
				// receive message from client
				DatagramPacket fromClientPacket = new DatagramPacket(fromClient, 
						fromClient.length);
				catalogSocket.receive(fromClientPacket);
				// print the message
				String msg = new String(fromClientPacket.getData());
				System.out.println("Request from Client: " + msg);
				// get the port of the client
				InetAddress ip = fromClientPacket.getAddress();
				int port = fromClientPacket.getPort();
				String request[] = msg.split(" ");			
				//If the request type in msg is C or K
				if(request[0].equals("C") || request[0].equals("K")) {
				    int i = 0;
					for (Iterator<String> iterator = Clist.iterator(); 
							iterator.hasNext();) {
						String record = iterator.next().toString();
						String[] item = record.split("( \")");
						String bookID = item[0];
						String title = item[1].replaceAll("\"", "");
						String author = item[2].replaceAll("\"", "");
						//a search by book-id returns the matching record
						if(request[0].equals("C")){
							if(request[1].equals(bookID)){
								toClient = record.getBytes();
								// send the message back to the client 
								DatagramPacket sendPacket = new DatagramPacket(
										toClient,toClient.length, ip, port);				
								catalogSocket.send(sendPacket);
								break;
							} else if(!iterator.hasNext()){
								toClient = ("Results not found.").getBytes();
								// send the message back to the client 
								DatagramPacket sendPacket = new DatagramPacket(
										toClient,toClient.length, ip, port);			
								catalogSocket.send(sendPacket);
							}
						} else if (request[0].equals("K")){
							if(title.contains(request[1]) || 
									author.contains(request[1])){
								i++;
								toClient = record.getBytes();	
								// send the message back to the client 
								DatagramPacket sendPacket = new DatagramPacket(
										toClient,toClient.length, ip, port);					
								catalogSocket.send(sendPacket);	
							} else if(!iterator.hasNext() && i == 0){
								toClient = ("Results not found.").getBytes();
								// send the message back to the client 
								DatagramPacket sendPacket = new DatagramPacket(
										toClient,toClient.length, ip, port);					
								catalogSocket.send(sendPacket);	
							}
						}
					}	
				} else {
					//Rubbish message.
					catalogSocket.close();
				}
			}
		} catch(Exception e){  
			//The port number is taken by other process.
			System.err.println("The port number is taken");
			System.exit(1);   
		}	
	}
}
