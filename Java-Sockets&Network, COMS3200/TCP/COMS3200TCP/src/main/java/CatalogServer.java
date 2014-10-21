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


public class CatalogServer {
	private int NameServerPort;

	public CatalogServer(int NameServerPort) {
		this.NameServerPort = NameServerPort;
	}
	public int getPort(){
		return NameServerPort;
	}

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
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		int CatalogPort = 2310;
		String ServerName = "CatalogServer";
		if (args.length != 1) {
			System.err.println("Invalid command line argument\n");
			System.exit(1);//Error exit
		}
		int port = Integer.parseInt(args[0]);

		if (port < 1024 || port > 65535) {
			System.err.println("Invalid command line argument " +
					"for Catalog Server\n");
			System.exit(1);//Incorrect port number
		}
		CatalogServer cs = new CatalogServer(port);
	
		List<String> Clist = ReadFile();
		//As client
		Socket clientSocket = null;
        PrintWriter outToNs = null;
        BufferedReader inFromNs = null;
        try {
            clientSocket = new Socket("127.0.0.1", cs.getPort());
            outToNs = new PrintWriter(clientSocket.getOutputStream(), true);
            inFromNs = new BufferedReader(
		    new InputStreamReader(clientSocket.getInputStream()));
        	System.out.println("Successfully connect to Name Server.");
        } catch (Exception e) {
        	System.err.println("Cannot connect to name server located at "  + 
        cs.getPort());
        	System.exit(1);
        }
        //send the register message to NameServer
    	outToNs.println("r"+" "+ServerName+" "+CatalogPort);
    	//close the connection
        outToNs.close();
        inFromNs.close();
        clientSocket.close();
        
        ServerSocket serverSocket = null;
		try {
			// listen on CatalogServer port number
			serverSocket = new ServerSocket(CatalogPort);
		} catch (IOException e) {
			//if CatalogServer is unable to listen on its port number, 
			//it print standard error and exit
			System.err.println("CatalogServer unable to listen on given port\n");
            System.exit(1);
        }
		Socket connSocket = null;
		while(true) {
        	try {
        		System.out.println("CatalogServer waiting for incoming connections\n");
        		// block, waiting for a conn. request
        		connSocket = serverSocket.accept();
        		// At this point, we have a connection
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
		    line = inFromClient.readLine();	
		    //split the line to get request detail from QueryClient
		    String request[] = line.split(" ");
		    for (Iterator<String> iterator = Clist.iterator(); iterator.hasNext();) {
				String record = iterator.next().toString();
				String[] item = record.split("( \")");
				String bookID = item[0];
				String title = item[1].replaceAll("\"", "");
				String author = item[2].replaceAll("\"", "");
				//C request below
				if (request[0].equals("C")) {
					if (request[1].equals(bookID)) {
						outToClient.println(record);
						break;
					} else {
						outToClient.println("Results not found.");
						break;
					}
				// K request below.
				} else if(request[0].equals("K")){
					//keep ouputing the records
					if (title.contains(request[1]) || author.contains(request[1])) {
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
