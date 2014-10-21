import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Name Server using UDP socket.
 */
public class NameServer {
	DatagramSocket inSocket; 
	//Two Arraylist used for storing register process and port number.
	private static ArrayList<String> 
						rProcessName = new ArrayList<String>();
	private static ArrayList<Integer> 
						rProcessPort = new ArrayList<Integer>();
	
	/**
	 * Send Message by corresponding request.
	 * @param msg
	 * @param port
	 * Get two variables. This function is used for sending the message.
	 */
	@SuppressWarnings("resource")
	public void sendMessage(String msg, int port){
		try{
			DatagramSocket socket = new DatagramSocket(); 
			DatagramPacket packet = 
					new DatagramPacket(msg.getBytes(), msg.length(), 
									InetAddress.getLocalHost(), port);
			socket.send(packet);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Name Server constructor.
	 * @param port
	 */
	public NameServer(int port){
		try{
			inSocket = new DatagramSocket(port);
			System.out.println("Name Server "
					+ "waiting for incoming connections...");
			while(true){
				//Buffer setting.
				byte[] inData = new byte[1024];
				DatagramPacket inPacket = new DatagramPacket(inData,
						inData.length);
				inSocket.receive(inPacket);
				//Incoming message processing below
				String msg = new String(inPacket.getData());
				int inPort = inPacket.getPort();
				String request[] = msg.split(" ");
				//System.out.println(msg);
				if(request[0].equals("r")){
					rProcessName.add(request[1]);
					rProcessPort.add(Integer.parseInt(request[2]));
					System.out.println("Accept registration from " + request[1]
							+ " port:" + request[2] + "\n");
					sendMessage("Registration done.\nProcess Name: " + 
					request[1] + "\nListening Port: " + inPort, inPort);
				}else if(request[0].equals("l")){
					if(rProcessName.size() == 0 || rProcessPort.size() == 0){
						//If not found.
						System.err.println("Error: Process has not "
								+ "registered with the Name Server");
					} else {
						//Search the process.
						if(request[1].equals("L") || request[1].equals("D")){
							for(int i = 0; i < rProcessName.size(); i ++){
								if (rProcessName.get(i).equals("LoansServer")){
									//Send to the Client
									sendMessage(rProcessName.get(i) + " " 
											+ rProcessPort.get(i) + " " 
											+ "From:NameServer", inPort);
									break;
								}
							}
						} else if (request[1].equals("C") || request[1].equals("K")){
							for(int i = 0; i < rProcessName.size(); i ++){
								if (rProcessName.get(i).equals("CatalogServer")){
									//Send to the Client
									sendMessage(rProcessName.get(i) + " " 
											+ rProcessPort.get(i) + " "
											+ "From:NameServer", inPort);
									break;
								}
							}
						}
					}
				}
				else{
					//Quit the system.
					inSocket.close();
				}
			}
		} catch (Exception e){
			//Exception for port
			System.err.println("Cannot listen on given port number "+ port);
			System.exit(1);
		}
	}

	/**
	 * @param args[0] = port, The Name Server takes one argument.
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			//Check the arguments.
			System.err.println("Invalid command line argument for Name Server");
			System.exit(1);
		}
		int port = Integer.parseInt(args[0]);
		if (port < 1024 || port > 65535) {
			//Check the ports.
			System.err.println("Invalid command line argument for Name Server");
			System.exit(1);
		}
		//Start the Server.
		new NameServer(port);
	}
}
