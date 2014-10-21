import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Query Client using UDP sockets
 */
class timeoutThread extends Thread {
	private static int Timeout = 4000; //Set 4s for timeout.
	boolean inResponse = true;
	public boolean checkReceive(){
		return inResponse;
	}
	public void run(){
		try{
			Thread.sleep(Timeout);
			inResponse = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
}

public class QueryClient {
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
	 * @param args
	 * args[0] = port, args[1] = request type, args[2] = request keywords.
	 * The Query Client takes 3 arguments;
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 3){
			System.err.println("Invalid command line arguments\n");
			System.exit(1);//Error exit
		}
		//port number should between 1024 to 65535.
		int port = Integer.parseInt(args[0]);
		if (port < 1024 || port > 65535){
			System.err.println("Invalid command line arguments\n");
			System.exit(1);//Error exit
		}
		if (!(args[1].equals("C") || args[1].equals("K") || 
				args[1].equals("L") || args[1].equals("D"))){
			System.err.println("Invalid command line arguments\n");
			//Check the request type.
			System.exit(1);
		}
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
				sendToNs(toNsSocket, "l" + " "+ args[1] + " " + 
						args[2]+" ", port);
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
		String res[] = reply.split(" ");
		DatagramSocket toServer = new DatagramSocket();
    	//Get the CatalogServer/LoansServer port number from reply
    	int targetServerPort = Integer.parseInt(res[1]);
    	System.out.println(res[1]);
    	do{
			p = Math.random();	
			if(p >= 0.5){
				do{
		        	//send data to target server
					sendToNs(toServer,args[1]+" "+ args[2] + " ",
		        			targetServerPort);
					System.out.println("Send to " + res[0] + " Successfully");
					
					//create a set timeout in new thread
					timeoutThread Timer = new timeoutThread();
					Timer.start();
					
					//receive data from target server
					reply = receiveFromNs(toServer);
					
					//check receive data from target server is timeout or not
					response = Timer.checkReceive();
					if(response){
						//print the result
						System.out.println("Response: "+ reply);
    		        	do {
    		        		reply = receiveFromNs(toServer);
    		        		if(reply != null){
    		        			System.out.println(reply);
    		        		}        		  
    		        	} while(reply != null);
					} else {
						//Resend packet if tries is less than 5
						System.out.println((times - attempt) + 
								" more attempts...Total attempts: "+ attempt );
						attempt ++;
						//if cannot contact TargetServer
						if(attempt > times) {
							System.err.println("Unable to reach TargetServer");
							System.exit(1);
						}						
						System.out.println("Resending...");
					}
				} while(response==false);
				// close up
				toServer.close();
			}
			else {
				System.out.println("Packet loss. Tring to resend...");
			}
		} while(p < 0.5); 
	}
}
