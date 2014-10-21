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
 * Loans Server using UDP socket.
 */
public class LoansServer {
	
	/**
	 * Read loans file and store the values in the arraylist
	 * @return Arraylist
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static List<String> ReadFile() throws IOException{
		String line;
		List<String> Loansfile = new ArrayList<String>();
		FileReader fd = new FileReader("loans-file.txt");
		BufferedReader r = new BufferedReader(fd);
		while((line = r.readLine()) != null){
			String[] res = line.split(" ");
			String userID = res[0];
			String bookID = res[1];
			String date = res[2];
			Loansfile.add(userID + " " + bookID + " " + date);
		}
		return Loansfile;
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
	 * @param args[0] = port, The Loans Server takes one argument.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		int LoansServerPort = 0;
		//The Loans Server port number
		try{
			DatagramSocket loansSocket = new DatagramSocket(0);
			LoansServerPort = loansSocket.getLocalPort(); 
			String ServerName = "LoansServer";
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
			List<String> Llist = ReadFile();
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
							LoansServerPort+" ", nameServerPort);
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
			System.out.println("LoansServer waiting for incoming "
					+ "connections...");
			//Processing the request from client below.
			while (true) {
				// set output&input buffers
				byte[] fromClient = new byte[1024];
				byte[] toClient = new byte[1024];
				// receive message from client
				DatagramPacket fromClientPacket = new DatagramPacket(fromClient, 
						fromClient.length);
				loansSocket.receive(fromClientPacket);
				// print the message
				String msg = new String(fromClientPacket.getData());
				System.out.println("Requests from Client: " + msg);
				// get the port of the client
				InetAddress ip = fromClientPacket.getAddress();
				int port = fromClientPacket.getPort();
				String request[] = msg.split(" ");
				if(request[0].equals("L") || request[0].equals("D")) {
				    int i = 0;		    
				    //search the loans file information
					for (Iterator<String> iterator = Llist.iterator(); 
							iterator.hasNext();) {
						String record = iterator.next().toString();
						String[] item = record.split(" ");
						String userID = item[0];
						String bookID = item[1];
						if (request[0].equals("D")){
							//a request search for a specific book-id by date
							if(request[1].equals(bookID)){
								toClient = record.getBytes();	
								// send the message back to the client 
								DatagramPacket sendPacket = new DatagramPacket(
										toClient, toClient.length, ip, port);					
								loansSocket.send(sendPacket);
								break;
							} else if(!iterator.hasNext()){
								toClient =("Results not found.").getBytes();
								// send the message back to the client 
								DatagramPacket sendPacket = new DatagramPacket(
										toClient, toClient.length, ip, port);
								loansSocket.send(sendPacket);
							}
						} else if (request[0].equals("L")){
							//a request saerch for a specific user-id by loans
							if(request[1].equals(userID)){
								i++;
								toClient = record.getBytes();
								// send the message back to the client 
								DatagramPacket sendPacket = new DatagramPacket(
										toClient, toClient.length, ip, port);
								loansSocket.send(sendPacket);	
							} else if(!iterator.hasNext() && i == 0){
								toClient = ("Results not found.").getBytes();
								// send the message back to the client 
								DatagramPacket sendPacket = new DatagramPacket(
										toClient, toClient.length, ip, port);				
								loansSocket.send(sendPacket);	
							}						
						}
					}					
				} else {
					//Rubbish results below.
					loansSocket.close();
				}		
			}
		} catch(Exception e){ 
			//The port number is taken by other process.
			System.err.println("The port number is taken");
			System.exit(1);   
		}
	}
}
