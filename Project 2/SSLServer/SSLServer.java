import javax.net.ssl.*;
import java.io.FileInputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;

/**
 * Copyright [Yahya Hassanzadeh-Nazarabadi]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
public class SSLServer extends Thread
{
    private final String SERVER_KEYSTORE_FILE = "keystore.jks";
    private final String SERVER_KEYSTORE_PASSWORD = "storepass";
    private final String SERVER_KEY_PASSWORD = "keypass";
    private SSLServerSocket sslServerSocket;
    private SSLServerSocketFactory sslServerSocketFactory;
    //private ServerControlPanel serverControlPanel;

    private ServerSocket serverSocket; //for insecure channel
    
    
    public SSLServer(int port1, int port2)
    {

        try
        {

            //serverControlPanel = new ServerControlPanel("hello server!");


            /*
            Instance of SSL protocol with TLS variance
             */
            SSLContext sc = SSLContext.getInstance("TLS");

            /*
            Key management of the server
             */
            char ksPass[] = SERVER_KEYSTORE_PASSWORD.toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(SERVER_KEYSTORE_FILE), ksPass);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, SERVER_KEY_PASSWORD.toCharArray());
            sc.init(kmf.getKeyManagers(), null, null);


            /*
            SSL socket factory which creates SSLSockets
             */
            sslServerSocketFactory = sc.getServerSocketFactory();
            sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port1);

            System.out.println("SSL server is up and running on port " + port1);
            
            //insecure channel is initiated thru serversocket
            serverSocket = new ServerSocket(port2);
            System.out.println("Opened up a server socket on " + Inet4Address.getLocalHost());
            
            // Create and start a separate thread for SSL connections
            Thread sslThread = new Thread(() -> {
                while (true) {
                    ListenAndAcceptSSL();
                }
            });
            sslThread.start();

            // Create and start a separate thread for TCP connections
            Thread tcpThread = new Thread(() -> {
                while (true) {
                    ListenAndAcceptTCP();
                }
            });
            tcpThread.start();
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /*
    Listens to the line and starts a connection on receiving a request with the client
     */
    private void ListenAndAcceptSSL()
    {
        SSLSocket s;
        try
        {
            s = (SSLSocket) sslServerSocket.accept();
            System.out.println("An SSL connection was established with a client on the address of " + s.getRemoteSocketAddress());
            SSLServerThread st = new SSLServerThread(s);
            st.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Server Class. Connection establishment error inside listen and accept function");
        }
    }

    private void ListenAndAcceptTCP()
    {
        Socket s;
        try
        {
            s = serverSocket.accept();
            System.out.println("A connection was established with a client on the address of " + s.getRemoteSocketAddress());
            TCPServerThread st = new TCPServerThread(s);
            st.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Server Class. Connection establishment error inside listen and accept function");
        }
    }



}
