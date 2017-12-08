
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler extends Thread
{
    private final Socket myClientSocket;
    RequestHandler(Socket myClientSocket) 
    {
        this.myClientSocket = myClientSocket;
    }
    
    @Override
    public void run()
    {
        System.out.println("Recieved connection... \nHandling request....");
        try {
            BufferedReader myClientInput = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream()));
            PrintWriter myServerOutput = new PrintWriter(myClientSocket.getOutputStream());
            
            String inputLine = myClientInput.readLine();
            String[] inputArray = inputLine.split(":");
            switch(inputArray[0])
            {
                case "put":
                    putRequestRecieved(myClientInput,myServerOutput, inputArray);
                    break;
                case "get":
                    getRequestRecieved(myClientInput,myServerOutput, inputArray);
                    break;
                case "del":
                    delRequestRecieved(myClientInput,myServerOutput, inputArray);
                    break;
                case "store":
                    storeRequestRecieved(myClientInput,myServerOutput, inputArray);
                    break;
                case "exit":
                    exitRequestRecieved(myClientInput,myServerOutput, inputArray);
                    break;
                case "dput1":
                    dput1RequestRecieved(myClientInput,myServerOutput, inputArray);
                    break;
                case "dput2":
                    dput2RequestRecieved(myClientInput,myServerOutput, inputArray);
                    break;
                case "dputabort":
                    dputabortRequestRecieved(myClientInput,myServerOutput, inputArray);
                    break;
                case "ddel1":
                    ddel1RequestRecieved(myClientInput,myServerOutput, inputArray);
                    break;
                case "ddel2":
                    ddel2RequestRecieved(myClientInput,myServerOutput, inputArray);
                    break;
                case "ddelabort":
                    ddelabortRequestRecieved(myClientInput,myServerOutput, inputArray);
                    break;
                case "getdnodes":
                    getdnodesRequestRecieved(myClientInput,myServerOutput, inputArray);
                    break;
                default:
                    System.out.println("Request not recognized");
            }
            myServerOutput.flush();
            System.out.println("Connection closed");
            myServerOutput.println("Connection closed");
            myClientInput.close();
            myServerOutput.close();
            myClientSocket.close();   
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void putRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        String[] putKeyValueArray = inputArray[1].split("=");
        System.out.print("PUT Key = " + putKeyValueArray[0]);
        System.out.println(", Value = " + putKeyValueArray[1]);
        myServerOutput.println("PUT Key = " + putKeyValueArray[0]);
        myServerOutput.println(", Value = " + putKeyValueArray[1]);   
    }
    
    public void getRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        String[] getKeyValueArray = inputArray[1].split("=");
        System.out.println("GET Key = " + getKeyValueArray[0]);
        myServerOutput.println("GET Key = " + getKeyValueArray[0]);   
    }
    
    public void delRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        String[] delKeyValueArray = inputArray[1].split("=");
        System.out.println("DEL Key = " + delKeyValueArray[0]);
        myServerOutput.println("DEL Key = " + delKeyValueArray[0]);   
    }
    
    public void storeRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        System.out.println(inputArray[0]);
        myServerOutput.println(inputArray[0]);   
    }
    
    public void exitRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        System.out.println(inputArray[0]);
        myServerOutput.println(inputArray[0]);
        Main.isServerUp = false;   
    }
    
    public void dput1RequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        String[] dput1KeyValueArray = inputArray[1].split("=");
        System.out.print("PUT Key = " + dput1KeyValueArray[0]);
        System.out.println(", Value = " + dput1KeyValueArray[1]);
        myServerOutput.println("PUT Key = " + dput1KeyValueArray[0]);
        myServerOutput.println(", Value = " + dput1KeyValueArray[1]);  
    }
        
    public void dput2RequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        String[] dput2KeyValueArray = inputArray[1].split("=");
        System.out.print("PUT Key = " + dput2KeyValueArray[0]);
        System.out.println(", Value = " + dput2KeyValueArray[1]);
        myServerOutput.println("PUT Key = " + dput2KeyValueArray[0]);
        myServerOutput.println(", Value = " + dput2KeyValueArray[1]);   
    }
    
    public void dputabortRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        String[] dputabortKeyValueArray = inputArray[1].split("=");
        System.out.print("PUT Key = " + dputabortKeyValueArray[0]);
        System.out.println(", Value = " + dputabortKeyValueArray[1]);
        myServerOutput.println("PUT Key = " + dputabortKeyValueArray[0]);
        myServerOutput.println(", Value = " + dputabortKeyValueArray[1]);
    }
    
    public void ddel1RequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        String[] ddel1KeyValueArray = inputArray[1].split("=");
        System.out.println("GET Key = " + ddel1KeyValueArray[0]);
        myServerOutput.println("GET Key = " + ddel1KeyValueArray[0]);
    }
    
    public void ddel2RequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        String[] ddel2KeyValueArray = inputArray[1].split("=");
        System.out.println("GET Key = " + ddel2KeyValueArray[0]);
        myServerOutput.println("GET Key = " + ddel2KeyValueArray[0]);   
    }
    
    public void ddelabortRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        String[] ddelabortKeyValueArray = inputArray[1].split("=");
        System.out.print("PUT Key = " + ddelabortKeyValueArray[0]);
        System.out.println(", Value = " + ddelabortKeyValueArray[1]);
        myServerOutput.println("PUT Key = " + ddelabortKeyValueArray[0]);
        myServerOutput.println(", Value = " + ddelabortKeyValueArray[1]);
    }

    public void getdnodesRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray) {
        for (Map.Entry<String, Integer> entry : Main.myNodes.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.print("GETDNODES Key = " + key);
            System.out.println(", Value = " + value + " ");
            myServerOutput.println("GETDNODES Key = " + key);
            myServerOutput.println(", Value = " + value + " ");
        }
    }

}
