import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.Socket;


public class TCPServerThread extends Thread
{
    private final String SERVER_ACK_MESSAGE = "server_ack (TCP)";
    private Socket tcpSocket;
    private String line = new String();
    private BufferedReader is;
    private PrintWriter os;
    
    public TCPServerThread(Socket s)
    {
        tcpSocket = s;
    }

    public void run()
    {
        try
        {
            is = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            os= new PrintWriter(tcpSocket.getOutputStream());
            
            line = is.readLine();
            System.out.println(line);
            while(true) {
            	os.println(SERVER_ACK_MESSAGE);
            	os.flush(); // Add this line to flush the buffer and ensure the message is sent immediately
                System.out.println("Client " + tcpSocket.getRemoteSocketAddress() + " sent : " + line);
                line = is.readLine();
                System.out.println(line);
                if(line==null) {
                	break;
                }
            }
          
        }
        
        catch (IOException e)
        {
            line = this.getClass().toString(); //reused String line for getting thread name
            System.out.println("Server Thread. Run. IO Error/ Client " + line + " terminated abruptly");
        }
        catch (NullPointerException e)
        {
            line = this.getClass().toString(); //reused String line for getting thread name
            System.out.println("Server Thread. Run.Client " + line + " Closed");
        } 
        finally
        {
            try
            {
                System.out.println("Closing the connection");
                if (is != null)
                {
                    is.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if (os != null)
                {
                    os.close();
                    System.out.println("Socket Out Closed");
                }
                if (tcpSocket != null)
                {
                    tcpSocket.close();
                    System.out.println("Socket Closed");
                }

            }
            catch (IOException ie)
            {
                System.out.println("Socket Close Error");
            }
        }//end finally
    }
}
