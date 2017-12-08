
import java.net.ServerSocket;
import java.net.Socket;



public class MySocketServer extends Thread
{
    private ServerSocket mySocketServer;
    private final int portNumber;
    private boolean serverUp = false;
    
    public MySocketServer(int portNumber)
    {
        this.portNumber = portNumber;
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
                RequestHandler myClientSocketHandler = new RequestHandler(myClientSocket);
                myClientSocketHandler.start();
                
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    
}
