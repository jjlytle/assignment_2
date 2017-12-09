
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class MySocketServer extends Thread
{
    private ServerSocket mySocketServer;
    private final int portNumber;
    private boolean serverUp = false;
    public ConcurrentHashMap<String, String> myConcurrentHashMap = null ;
    
    public MySocketServer(int portNumber)
    {
        this.portNumber = portNumber;
        myConcurrentHashMap = new ConcurrentHashMap<>();
    }
    
    public void serverStart()
    {
        try {
            mySocketServer = new ServerSocket(portNumber);
            this.start();
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
    public void serverStop()
    {
        serverUp = false;
        this.interrupt();
    }
    
    @Override
    public void run()
    {
        serverUp = true;
        while(serverUp)
        {
            try {
                System.out.println("Waiting for a connection...");
                Socket myClientSocket = mySocketServer.accept();
                RequestHandler myClientSocketHandler = new RequestHandler(myClientSocket, myConcurrentHashMap);
                myClientSocketHandler.start();
                
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    
}
