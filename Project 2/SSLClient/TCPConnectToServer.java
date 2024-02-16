import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;

/**
 * This class handles and establishes an SSL connection to a server
 */
public class TCPConnectToServer
{
    private Socket tcpSocket;
    private BufferedReader is;
    private PrintWriter os;

    protected String serverAddress;
    protected int serverPort;

    public TCPConnectToServer(String address, int port)
    {
        serverAddress = address;
        serverPort = port;
    }

    /**
     * Connects to the specified server by serverAddress and serverPort
     */
    public void Connect()
    {
        try
        {
        	tcpSocket=new Socket(serverAddress, serverPort);
            /*
            Read and write buffers on the socket
             */
            is = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            os = new PrintWriter(tcpSocket.getOutputStream());
            System.out.println("Successfully connected to " + serverAddress + " on port " + serverPort);
        }
        catch (IOException e)
        {
            System.err.println("Error: no server has been found on " + serverAddress + "/" + serverPort);
        }
    }


    /**
     * Disconnects form the specified server
     */
    public void Disconnect()
    {
        try
            {
                is.close();
                os.close();
                tcpSocket.close();
            }
        catch (IOException e)
            {
                e.printStackTrace();
            }
    }

    /**
     * Sends a message as a string over the secure channel and receives
     * answer from the server
     * @param message input message
     * @return response from server
     */
    public String SendForAnswer(String message)
    {
        String response = new String();
        try
        {
            os.println(message);
            os.flush();
            response = is.readLine();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("ConnectionToServer. SendForAnswer. Socket read Error");
        }
        return response;
    }

}
