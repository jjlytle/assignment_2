// server

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main 
{

    private static int portNumber;
    public static boolean isServerUp = true;
    public static Map<String, Integer> myNodes = new HashMap<String, Integer>();
    public static void main(String[] args) 
    {
        if( args.length < 2 )
        {
            System.out.println("Error: Missing required arguments");
            System.out.println( "Usage: java -jar GenericNode.jar tc <port>" );
            System.exit( 0 );
        }
        try {
            portNumber = Integer.parseInt(args[1]);   
        } catch (Exception e) {
            System.out.println("Error: Invalid port");
            System.out.println( "Usage: java -jar GenericNode.jar tc <port>" );
            //System.exit( 0 );
        }
        MySocketServer server = new MySocketServer(portNumber);
        server.serverStart();
        while(isServerUp)
        {
            try {
                Thread.sleep(60000);
                getNodes();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        server.serverStop();    
        System.exit( 0 );
    }
    
    private static void getNodes() throws IOException {
	
        File nodeList = new File("/testCode/Nodes");
	BufferedReader br = new BufferedReader(new FileReader(nodeList));
 
	String inputLine = null;
	while ((inputLine = br.readLine()) != null) {
		String[] inputArray = inputLine.split(":");
                myNodes.put(inputArray[0], Integer.parseInt(inputArray[1]));               
	}
 
	br.close();
    }
}