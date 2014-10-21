import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class LoansServer {
	private int NameServerPort;
	
	public LoansServer(int NameServerPort) {
		this.NameServerPort = NameServerPort;
	}
	public int getPort(){
		return NameServerPort;
	}
	
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
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException{
		int LoansPort = 3506;
		String ServerName = "LoansServer";
		if(args.length != 1){
			System.err.println("Invalid command line argument\n");
			System.exit(1);//Error exit
		}
		int port = Integer.parseInt(args[0]);

		if (port < 1024 || port > 65535) {
			System.err.println("Invalid command line argument " +
					"for Loans Server\n");
			System.exit(1);//Incorrect port number
		}
		LoansServer ls = new LoansServer(port);
		List<String> Llist = ReadFile();
		//As client
		Socket clientSocket = null;
        PrintWriter outToNs = null;
        BufferedReader inFromNs = null;
        try {
            clientSocket = new Socket("127.0.0.1", ls.getPort());
            outToNs = new PrintWriter(clientSocket.getOutputStream(), true);
            inFromNs = new BufferedReader(
		    new InputStreamReader(clientSocket.getInputStream()));
        	System.out.println("Successfully connect to Name Server.");
        } catch (Exception e) {
        	System.err.println("Cannot connect to name server located at "  + 
        ls.getPort());
        	System.exit(1);
        }
        //send the register message to NameServer
        outToNs.println("r"+" "+ServerName+" "+LoansPort);
    	//close the connection
        outToNs.close();
        inFromNs.close();
        clientSocket.close();
        
        ServerSocket serverSocket = null;
		try {
			// listen on LoansServer port number
			serverSocket = new ServerSocket(LoansPort);
		} catch (IOException e) {
			//Error handling for LoansServer(unable to listen on port number)
			System.err.println("LoansServer unable to listen on given port\n");
            System.exit(1);
        }
		Socket connSocket = null;
		while(true)
		{
        	try {
        		System.out.println("LoansServer waiting for incoming connections\n");
        		// block, waiting for a conn. request
        		connSocket = serverSocket.accept();
        		// At this point, we have a connection
				//System.out.println("Connection accepted from: " + connSocket.getInetAddress().getHostName() +"\n");
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
        	// Now have a socket to use for communication
        	// Create a PrintWriter and BufferedReader for
        	// interaction with our stream
        	// "true" means we flush the stream on newline
		    PrintWriter outToClient = new PrintWriter
		    		(connSocket.getOutputStream(), true);
		    BufferedReader inFromClient = new BufferedReader
		    		(new InputStreamReader(connSocket.getInputStream()));
		    
		    String line;
		    //read a line from stream
		    line=inFromClient.readLine();	 
		    //split the line to get request detail from QueryClient
		    String request[] = line.split(" ");
		    
		    //search the loans file information
			for (Iterator<String> iterator = Llist.iterator(); iterator.hasNext();) {
				String record = iterator.next().toString();
				String[] item = record.split(" ");
				String userID = item[0];
				String bookID = item[1];
				String dueDate = item[2];
				if(request[0].equals("D")) {
					//D request below
					if(request[1].equals(bookID)){
						outToClient.println(dueDate);	
						break;
					} else {
						outToClient.println("Results not found.");
						break;
					}
				} else if(request[0].equals("L")){
					//L request below
					if(request[1].equals(userID)){
						//keep ouputing the records(if many)
						outToClient.println(record);
					} else {
						outToClient.println("Results not found or Request ends.");
						break;
					}
				}
			}
		}
	}
}
