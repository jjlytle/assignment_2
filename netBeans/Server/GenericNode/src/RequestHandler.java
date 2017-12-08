
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
                    putRequestRecieved(myClientInput, myServerOutput, inputArray);
                    break;
                case "get":
                    getRequestRecieved(myClientInput, myServerOutput, inputArray);
                    break;
                case "del":
                    delRequestRecieved(myClientInput, myServerOutput, inputArray);
                    break;
                case "store":
                    storeRequestRecieved(myClientInput, myServerOutput, inputArray);
                    break;
                case "exit":
                    exitRequestRecieved(myClientInput, myServerOutput, inputArray);
                    break;
                case "dput1":
                    dput1RequestRecieved(myClientInput, myServerOutput, inputArray);
                    break;
                case "dput2":
                    dput2RequestRecieved(myClientInput, myServerOutput, inputArray);
                    break;
                case "dputabort":
                    dputabortRequestRecieved(myClientInput, myServerOutput, inputArray);
                    break;
                case "ddel1":
                    ddel1RequestRecieved(myClientInput, myServerOutput, inputArray);
                    break;
                case "ddel2":
                    ddel2RequestRecieved(myClientInput, myServerOutput, inputArray);
                    break;
                case "ddelabort":
                    ddelabortRequestRecieved(myClientInput, myServerOutput, inputArray);
                    break;
                case "getdnodes":
                    getdnodesRequestRecieved(myClientInput, myServerOutput, inputArray);
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
        int voteCount = 0;
        String[] putKeyValueArray = inputArray[1].split("=");
        System.out.println("Sending Commit request to all server nodes...");
        myServerOutput.println("Sending Commit request to all server nodes...");
        for (Map.Entry<String, Integer> entry : Main.myNodes.entrySet()) 
        {
            String myIpAddress = entry.getKey();
            Integer myPort = entry.getValue();
            ServerRequests myRequest = new ServerRequests(myIpAddress, myPort);
            voteCount = voteCount + myRequest.sendCommitRequest(putKeyValueArray[0], putKeyValueArray[1]);
        }
        System.out.println("VoteCount = " + voteCount + ", Expected Count " + (Main.myNodes.size()-1));
        myServerOutput.println("VoteCount = " + voteCount + ", Expected Count " + (Main.myNodes.size()-1));
        
        if (voteCount == (Main.myNodes.size()-1)) 
        {
            System.out.println("Recieved all nessecary votes sending commit to all server nodes...");
            myServerOutput.println("Recieved all nessecary votes sending commit to all server nodes...");
            for (Map.Entry<String, Integer> entry : Main.myNodes.entrySet()) 
            {
                String myIpAddress = entry.getKey();
                Integer myPort = entry.getValue();
                System.out.print("GETDNODES Key = " + myIpAddress);
                System.out.println(", Value = " + myPort + " ");
                myServerOutput.println("GETDNODES Key = " + myIpAddress);
                myServerOutput.println(", Value = " + myPort + " ");
                ServerRequests myRequest = new ServerRequests(myIpAddress, myPort);
                System.out.print(myRequest.sendCommitConfirmed(putKeyValueArray[0], putKeyValueArray[1]));
            }
        } 
        else 
        {
            System.out.println("Did not recieved all nessecary votes sending abort to all server nodes...");
            myServerOutput.println("Did not recieved all nessecary votes sending abort to all server nodes...");
            for (Map.Entry<String, Integer> entry : Main.myNodes.entrySet()) 
            {
                String myIpAddress = entry.getKey();
                Integer myPort = entry.getValue();
                ServerRequests myRequest = new ServerRequests(myIpAddress, myPort);
                voteCount = voteCount + myRequest.sendAbortPutRequest(putKeyValueArray[0], putKeyValueArray[1]);
            }
        }  
    }
    
    public void getRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        String[] getKeyValueArray = inputArray[1].split("=");
        System.out.println("GET Key = " + getKeyValueArray[0]);
        myServerOutput.println("GET Key = " + getKeyValueArray[0]);   
    }
    
    public void delRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        int voteCount = 0;
        String[] putKeyValueArray = inputArray[1].split("=");
        System.out.println("Sending Delete request to all server nodes...");
        myServerOutput.println("Sending Delete request to all server nodes...");
        for (Map.Entry<String, Integer> entry : Main.myNodes.entrySet()) 
        {
            String myIpAddress = entry.getKey();
            Integer myPort = entry.getValue();
            ServerRequests myRequest = new ServerRequests(myIpAddress, myPort);
            voteCount = voteCount + myRequest.sendDeleteRequest(putKeyValueArray[0]);
        }
        System.out.println("VoteCount = " + voteCount + ", Expected Count " + (Main.myNodes.size()-1));
        myServerOutput.println("VoteCount = " + voteCount + ", Expected Count " + (Main.myNodes.size()-1));
        
        if (voteCount == (Main.myNodes.size()-1)) 
        {
            System.out.println("Recieved all votes sending commit Delete to all server nodes...");
            myServerOutput.println("Recieved all votes sending commit Delete to all server nodes...");
            for (Map.Entry<String, Integer> entry : Main.myNodes.entrySet()) 
            {
                String myIpAddress = entry.getKey();
                Integer myPort = entry.getValue();
                System.out.print("GETDNODES Key = " + myIpAddress);
                System.out.println(", Value = " + myPort + " ");
                myServerOutput.println("GETDNODES Key = " + myIpAddress);
                myServerOutput.println(", Value = " + myPort + " ");
                ServerRequests myRequest = new ServerRequests(myIpAddress, myPort);
                System.out.print(myRequest.sendDeleteConfirmed(putKeyValueArray[0]));
            }
        } 
        else 
        {
            System.out.println("Did not recieved all votes sending abort Delete to all server nodes...");
            myServerOutput.println("Did not recieved all votes sending abort Delete to all server nodes...");
            for (Map.Entry<String, Integer> entry : Main.myNodes.entrySet()) 
            {
                String myIpAddress = entry.getKey();
                Integer myPort = entry.getValue();
                ServerRequests myRequest = new ServerRequests(myIpAddress, myPort);
                voteCount = voteCount + myRequest.sendAbortDelRequest(putKeyValueArray[0], putKeyValueArray[1]);
            }
        }    
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
