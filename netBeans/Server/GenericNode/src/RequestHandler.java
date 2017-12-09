
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RequestHandler extends Thread
{
    private final Socket myClientSocket;
    private ConcurrentHashMap myConcurrentHashMap;
    private static ReadWriteLock readWriteLock;
    
    RequestHandler(Socket myClientSocket, ConcurrentHashMap myConcurrentHashMap) 
    {
        this.myClientSocket = myClientSocket;
        this.myConcurrentHashMap = myConcurrentHashMap;
        readWriteLock = new ReentrantReadWriteLock();
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

    public synchronized void putRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray) 
    {
        
        //todo make sure that it does not send a dput request to itself i.e. remove itself from node list
        
        int voteCount = 0;
        String[] putKeyValueArray = inputArray[1].split("=");
        System.out.println("Sending Put Commit request to all server nodes...");
        myServerOutput.println("Sending Put Commit request to all server nodes...");
        for (Map.Entry<String, Integer> entry : Main.myNodes.entrySet()) 
        {
            String myIpAddress = entry.getKey();
            Integer myPort = entry.getValue();
            System.out.println("----" + myIpAddress + "----" + myPort + "----");
            ServerRequests myRequest = new ServerRequests(myIpAddress, myPort);
            voteCount = voteCount + myRequest.sendCommitRequest(putKeyValueArray[0], putKeyValueArray[1]);
            //startSender(myIpAddress, myPort, putKeyValueArray[0], putKeyValueArray[1]);
        }
        System.out.println("VoteCount = " + voteCount + ", Expected Count " + Main.myNodes.size());
        myServerOutput.println("VoteCount = " + voteCount + ", Expected Count " + Main.myNodes.size());
        
        if (voteCount == Main.myNodes.size()) 
        {
            System.out.println("Recieved all votes sending put commit to all server nodes...");
            myServerOutput.println("Recieved all votes sending put commit to all server nodes...");
            for (Map.Entry<String, Integer> entry : Main.myNodes.entrySet()) 
            {
                String myIpAddress = entry.getKey();
                Integer myPort = entry.getValue();
                ServerRequests myRequest = new ServerRequests(myIpAddress, myPort);
                System.out.print(myRequest.sendCommitConfirmed(putKeyValueArray[0], putKeyValueArray[1]));
            }
            try {
                readWriteLock.writeLock().lock();
                myConcurrentHashMap.putIfAbsent(putKeyValueArray[0], putKeyValueArray[1]);
            } finally {
                readWriteLock.writeLock().unlock();
            }
            
            
        } 
        else 
        {
            System.out.println("Did not recieved all votes sending abort to all server nodes...");
            myServerOutput.println("Did not recieved all votes sending abort to all server nodes...");
            voteCount = 0;
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
        String value = (String) myConcurrentHashMap.get(getKeyValueArray[0]);
        System.out.println("GET Key = " + getKeyValueArray[0] + ", Value = " + value);
        myServerOutput.println("GET Key = " + getKeyValueArray[0] + ", Value = " + value);   
    }
    
    public synchronized void delRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        // todo make sure this sever removes it self from the node list
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
        System.out.println("VoteCount = " + voteCount + ", Expected Count " + Main.myNodes.size());
        myServerOutput.println("VoteCount = " + voteCount + ", Expected Count " + Main.myNodes.size());
        
        if (voteCount == Main.myNodes.size()) 
        {
            System.out.println("Recieved all votes sending commit Delete to all server nodes...");
            myServerOutput.println("Recieved all votes sending commit Delete to all server nodes...");
            for (Map.Entry<String, Integer> entry : Main.myNodes.entrySet()) 
            {
                String myIpAddress = entry.getKey();
                Integer myPort = entry.getValue();
                ServerRequests myRequest = new ServerRequests(myIpAddress, myPort);
                System.out.print(myRequest.sendDeleteConfirmed(putKeyValueArray[0]));
            }
            try {
                readWriteLock.writeLock().lock();
                myConcurrentHashMap.remove(putKeyValueArray[0]);
            } finally {
                readWriteLock.writeLock().unlock();
            }
            
            
        } 
        else 
        {
            System.out.println("Did not recieved all votes sending abort Delete to all server nodes...");
            myServerOutput.println("Did not recieved all votes sending abort Delete to all server nodes...");
            voteCount = 0;
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
        //todo send and print keyset and valueset
        System.out.println(inputArray[0]);
        myServerOutput.println(inputArray[0]);
        Set keys = myConcurrentHashMap.keySet();
        for (Iterator i = keys.iterator(); i.hasNext();) {
            String key = (String) i.next();
            String value = (String) myConcurrentHashMap.get(key);
            System.out.print("PUT Key = " + key);
            System.out.println(", Value = " + value);
            myServerOutput.println("PUT Key = " + key);
            myServerOutput.println(", Value = " + value);
        }
    }
    
    public void exitRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        //complete
        System.out.println(inputArray[0]);
        myServerOutput.println(inputArray[0]);
        Main.isServerUp = false;   
    }
    
    public synchronized void dput1RequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray) {
        String[] dput1KeyValueArray = inputArray[1].split("=");
        System.out.println("Testing lock");

        try {
            //readWriteLock.writeLock().lock();
            myServerOutput.println("commited");
            System.out.print("PUT Key = " + dput1KeyValueArray[0] + " Locked...");
        } catch (Exception e) {
            myServerOutput.println("abort");
        }

    }
        
    public synchronized void dput2RequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        String[] dput2KeyValueArray = inputArray[1].split("=");
        try {
            myConcurrentHashMap.putIfAbsent(dput2KeyValueArray[0], dput2KeyValueArray[1]);
            System.out.print("PUT Key = " + dput2KeyValueArray[0]);
            System.out.println(", Value = " + dput2KeyValueArray[1]);
            myServerOutput.println("PUT Key = " + dput2KeyValueArray[0]);
            myServerOutput.println(", Value = " + dput2KeyValueArray[1]);    
        } finally {
            //readWriteLock.writeLock().unlock();
        }
    }
    
    public synchronized void dputabortRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        //String[] dputabortKeyValueArray = inputArray[1].split("=");
        //readWriteLock.writeLock().unlock();
        myServerOutput.println("aborted");
        // todo release locked coherent hashmap
    }
    
    public synchronized void ddel1RequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray) {
        String[] dput1KeyValueArray = inputArray[1].split("=");

        try {
            //readWriteLock.writeLock().lock();
            myServerOutput.println("commited");
            System.out.print("DEL Key = " + dput1KeyValueArray[0] + " Locked...");
        } catch (Exception e) {
            myServerOutput.println("abort");
        }

    }
    
    public synchronized void ddel2RequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        String[] dput2KeyValueArray = inputArray[1].split("=");
        try {
            myConcurrentHashMap.remove(dput2KeyValueArray[0]);
            System.out.print("DEL Key = " + dput2KeyValueArray[0]);
            myServerOutput.println("DEL Key = " + dput2KeyValueArray[0]);    
        } finally {
            //readWriteLock.writeLock().unlock();
        }
    }
    
    public synchronized void ddelabortRequestRecieved(BufferedReader myClientInput, PrintWriter myServerOutput, String[] inputArray)
    {
        //String[] ddelabortKeyValueArray = inputArray[1].split("=");
        //readWriteLock.writeLock().unlock();
        myServerOutput.println("aborted");
        // relese locked cohernt hasmap
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
