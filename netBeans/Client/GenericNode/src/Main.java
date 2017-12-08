// Client
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Main {

    private static String machineName;
    private static int portNumber;
    private static String requestMethod;
    private static String myKey = null;
    private static String myValue = null;
    
    
    public static void main(String[] args) 
    {
        //System.out.println(args.length);
        if( args.length < 1 )
        {
            System.out.println("Error: Protocol type is reqired (tc is only current option)");
            System.out.println( "1 Usage: java -jar GenericNode.jar tc <IP address> <port> <Request Method> (optional)<Key> (optional)<value>" );
            System.exit( 0 );
        }
        try {
            machineName = args[1];   
        } catch (Exception e) {
            System.out.println("Error: Ip address is required or invalid Ip address");
            System.out.println( "2 Usage: java -jar GenericNode.jar tc <IP address> <port> <Request Method> (optional)<Key> (optional)<value>" );
            //System.exit( 0 );
        }
        try {
            portNumber = Integer.parseInt(args[2]);   
        } catch (Exception e) {
            System.out.println("Error: Port is required or invalid port");
            System.out.println( "3 Usage: java -jar GenericNode.jar tc <IP address> <port> <Request Method> (optional)<Key> (optional)<value>" );
            //System.exit( 0 );
        }
        try {
            requestMethod = args[3];   
        } catch (Exception e) {
            System.out.println("Error: Request method is required");
            System.out.println( "4 Usage: java -jar GenericNode.jar tc <IP address> <port> <Request Method> (optional)<Key> (optional)<value>" );
            System.exit( 0 );
        }

        if(args.length >= 5)
        {
            myKey = args[4];
        }
        if(args.length == 6)
        {
            myValue = args[5];
        }
        else if(args.length > 6)
        {
            System.out.println("Error: To many arguments");
            System.out.println( "Usage: java -jar GenericNode.jar tc <IP address> <port> <Request Method> (optional)<Key> (optional)<value>" );
            System.exit( 0 );
        }

        Socket myClient;
        BufferedReader myServerInput;
        PrintWriter myClientOutput;
        
        try {
            myClient = new Socket(machineName, portNumber);
            myServerInput = new BufferedReader(new InputStreamReader(myClient.getInputStream()));
            myClientOutput = new PrintWriter(myClient.getOutputStream(), true);
            myClientOutput.println(requestMethod + ":" + myKey + "=" + myValue);
            myClientOutput.flush();
            String serverInput = myServerInput.readLine();
            while( serverInput != null && serverInput.length() > 0 )
            {
                System.out.print(serverInput);
                serverInput = myServerInput.readLine();
            }
            System.out.println("");
            myServerInput.close();
            myClientOutput.close();
            myClient.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
