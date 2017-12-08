
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jeffreylytle
 */
public class ServerRequests {
    
    private String machineName;
    private int portNumber;
    private String myKey;
    private String myValue;
    private int commitConfirmed;
    
    public void ServerRequsets(String machineName, int portNumber)
    {
        this.machineName = machineName;
        this.portNumber = portNumber;
        commitConfirmed = -1;       
    }
    
    public int sendCommitRequest(String key, String value)
    {
        String requestMethod = "dput1";
        this.myKey = key;
        this.myValue = value;
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
            if(serverInput.equals("commited"))
            {
                commitConfirmed = 1;
            }
            else
            {
                commitConfirmed = 0;
            }
            myServerInput.close();
            myClientOutput.close();
            myClient.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return commitConfirmed;
    }
    
    public String sendCommitConfirmed(String key, String value)
    {
        String requestMethod = "dput2";
        this.myKey = key;
        this.myValue = value;
        String serverInput = null;
        Socket myClient;
        BufferedReader myServerInput;
        PrintWriter myClientOutput;
        
        try {
            myClient = new Socket(machineName, portNumber);
            myServerInput = new BufferedReader(new InputStreamReader(myClient.getInputStream()));
            myClientOutput = new PrintWriter(myClient.getOutputStream(), true);
            myClientOutput.println(requestMethod + ":" + myKey + "=" + myValue);
            myClientOutput.flush();
            serverInput = myServerInput.readLine();
            myServerInput.close();
            myClientOutput.close();
            myClient.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return serverInput;
    }
    
    public int sendAbortPutRequest(String key, String value)
    {
        String requestMethod = "dputabort";
        this.myKey = key;
        this.myValue = value;
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
            if(serverInput.equals("aborted"))
            {
                commitConfirmed = 1;
            }
            else
            {
                commitConfirmed = 0;
            }
            myServerInput.close();
            myClientOutput.close();
            myClient.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return commitConfirmed;
    }
    
    public int sendDeleteRequest(String key)
    {
        String requestMethod = "dput1";
        this.myKey = key;
        Socket myClient;
        BufferedReader myServerInput;
        PrintWriter myClientOutput;
        
        try {
            myClient = new Socket(machineName, portNumber);
            myServerInput = new BufferedReader(new InputStreamReader(myClient.getInputStream()));
            myClientOutput = new PrintWriter(myClient.getOutputStream(), true);
            myClientOutput.println(requestMethod + ":" + myKey);
            myClientOutput.flush();
            String serverInput = myServerInput.readLine();
            if(serverInput.equals("commited"))
            {
                commitConfirmed = 1;
            }
            else
            {
                commitConfirmed = 0;
            }
            myServerInput.close();
            myClientOutput.close();
            myClient.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return commitConfirmed;
    }
    
        public String sendDeleteConfirmed(String key)
    {
        String requestMethod = "ddel2";
        this.myKey = key;
        String serverInput = null;
        Socket myClient;
        BufferedReader myServerInput;
        PrintWriter myClientOutput;
        
        try {
            myClient = new Socket(machineName, portNumber);
            myServerInput = new BufferedReader(new InputStreamReader(myClient.getInputStream()));
            myClientOutput = new PrintWriter(myClient.getOutputStream(), true);
            myClientOutput.println(requestMethod + ":" + myKey);
            myClientOutput.flush();
            serverInput = myServerInput.readLine();
            myServerInput.close();
            myClientOutput.close();
            myClient.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return serverInput;
    }
        
    public int sendAbortDelRequest(String key, String value)
    {
        String requestMethod = "dputabort";
        this.myKey = key;
        this.myValue = value;
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
            if(serverInput.equals("aborted"))
            {
                commitConfirmed = 1;
            }
            else
            {
                commitConfirmed = 0;
            }
            myServerInput.close();
            myClientOutput.close();
            myClient.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return commitConfirmed;
    }
}
