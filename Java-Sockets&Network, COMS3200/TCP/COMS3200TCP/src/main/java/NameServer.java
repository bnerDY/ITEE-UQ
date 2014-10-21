import java.net.*;
import java.util.ArrayList;
import java.io.*;


public class NameServer {
	private int ListenPort;
	private static ArrayList<String> registerProcessName = new ArrayList<String>();
	private static ArrayList<Integer> registerProcessPort = new ArrayList<Integer>();
	
	public NameServer(int ListenPort){
		this.ListenPort = ListenPort;
	}
	public int getPort(){
		return ListenPort;
	}
	/**
	 * @param args
	 * argv1 = listening port required.
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		if (args.length != 1){
			System.err.println("Invalid command line argument\n");
			System.exit(1);//Error exit
		}
		int port = Integer.parseInt(args[0]);

		if (port < 1024 || port > 65535) {
			System.err.println("Invalid command line argument for Name Server\n");
			System.exit(1);//Incorrect port number
		}
		NameServer ns = new NameServer(port);
		/**
		 * start a name server waiting for the incoming connection at ns.getPort().
		 */
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(ns.getPort());
		} catch (IOException e) {
			System.err.println("Cannot listen on given port number "
					+ ns.getPort() + "\n");
			System.exit(1);
		}//Can't connect server exit.
		Socket connSocket = null;
		while (true) {
			try {
				System.out.println("Name Server waiting for " +
						"incoming connections ...");
				// block, waiting for a conn. request
				connSocket = serverSocket.accept();
				// At this point, we have a connection
				System.out.println("Connection accepted from "
						+ connSocket.getInetAddress().getHostName() + " port "
						+ connSocket.getPort() + "\n");//For testing.
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Now have a socket to use for communication
			// Create a PrintWriter and BufferedReader for
			// interaction with our stream
			// "true" means we flush the stream on newline
			PrintWriter out = new PrintWriter
					(connSocket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader
		    		(new InputStreamReader(connSocket.getInputStream()));
		    String line;
		    // Read a line from the stream - until the stream closes
		    line = in.readLine();
		    String request[] = line.split(" ");
		    if (request[0].equals("r")){
		    	registerProcessName.add(request[1]);
				registerProcessPort.add(Integer.parseInt(request[2]));
		    } else if (request[0].equals("l")) {
				if(registerProcessName.size() == 0 || 
						registerProcessPort.size() == 0){
					out.println("Error: Process has not registered with the " +
							"Name Server");
				} else {
					if(request[1].equals("L") || request[1].equals("D")){
						for(int i = 0; i < registerProcessName.size(); i ++){
							if (registerProcessName.get(i).equals("LoansServer")){
								out.println(registerProcessName.get(i) + " "
										+ registerProcessPort.get(i));
								break;
							}
						}
					} else if (request[1].equals("C") || request[1].equals("K")){
						for(int i = 0; i < registerProcessName.size(); i ++){
							if (registerProcessName.get(i).equals("CatalogServer")){
								out.println(registerProcessName.get(i) + " "
										+ registerProcessPort.get(i));
								break;
							}
						}
					}
				}
		    } else {
		    	//Rubbish data process below
				out.close();
				in.close();
				connSocket.close();		    	
			}
		}
	}
}
