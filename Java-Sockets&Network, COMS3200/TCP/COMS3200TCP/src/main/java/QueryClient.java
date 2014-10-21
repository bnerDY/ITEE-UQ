import java.net.*;
import java.io.*;

public class QueryClient {
	
	/**
	 * @param args
	 * argv1 = port, argv2 = request type, argv3 = request keywords.
	 * 3 arguments required;
	 */
	public static void main(String[] args) throws IOException {
		//Client socket, Standard input, Standard output.
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        
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
		try {
			// Connect to the process listening on NameServer port number on this host (localhost)
			clientSocket = new Socket("127.0.0.1", port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (Exception e) {
			System.err.println("Cannot connect to name server located at " + port +"\n");
            System.exit(1);
        }
		out.println("l"+" "+args[1]+" "+args[2]);
    	String reply = in.readLine();
    	String Message[] = reply.split(" ");

        if (Message.length != 2) {
        	//BUG exit
        	System.err.println(reply +"\n");
        	System.exit(1);   	
        } else {
            int TargetServerPort = Integer.parseInt(Message[1]);
            Socket ClientSocket = null;
            PrintWriter outToServer = null;
            BufferedReader inFromServer = null;
            try {
    			ClientSocket = new Socket("127.0.0.1", TargetServerPort);    			// "true" means flush at end of line
    			outToServer = new PrintWriter
    					(ClientSocket.getOutputStream(), true);
    			inFromServer = new BufferedReader
    					(new InputStreamReader(ClientSocket.getInputStream()));
    			
    		} catch (Exception e) {
    			if(args[1].equals("C") || args[1].equals("K")) {
    				System.err.println("QueryClient unable to " +
        					"connect to CatalogServer\n");
                    System.exit(1);
    			} else {
                    System.err.println("QueryClient unable to " +
    						"connect to LoansServer\n");
    				System.exit(1);
    			}
            }      
    		outToServer.println(args[1] + " " + args[2]);
    		String response;
    		while((response = inFromServer.readLine()) != null) {
    			System.out.println(response);
    		}
        }
	}
}
