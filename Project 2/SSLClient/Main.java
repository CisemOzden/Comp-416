import java.sql.Time;
import java.util.Scanner;

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
public class Main
{
    public final static String SERVER_ADDRESS = "localhost";
    public final static String MESSAGE_TO_SERVER = "69707COMP416";
    public final static int TLS_SERVER_PORT = 4444;
    public final static int TCP_SERVER_PORT = 2222;


    public static void main(String[] args)
    {
    	//ask user the desired type of connection
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please specify the connection type you want to use (1 for secure, 2 for insecure)");
        
        String message = scanner.nextLine();
        
        while(!message.equals("1") && !message.equals("2")) {
        	System.out.println("You specified a wrong input. Please choose 1 or 2");
        	message = scanner.nextLine();
        }
        
        if(message.equals("1")) {
        	long time1 = System.currentTimeMillis();
            /*
            Creates an SSLConnectToServer object on the specified server address and port
             */
            SSLConnectToServer sslConnectToServer = new SSLConnectToServer(SERVER_ADDRESS, TLS_SERVER_PORT);
            /*
            Connects to the server
             */
            sslConnectToServer.Connect();
            
            /*
            Sends a message over TCP socket to the server and prints out the received message from the server
             */

            for(int i=0; i<MESSAGE_TO_SERVER.length(); i++) {
                System.out.println(sslConnectToServer.SendForAnswer(MESSAGE_TO_SERVER.charAt(i)+ ""));
                try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
        	long time2 = System.currentTimeMillis();
        	System.out.println(time2-time1);

            /*
            Disconnects from the SSL server
             */
            sslConnectToServer.Disconnect();
        	
        	
        }else {
        	long time1 = System.currentTimeMillis();
            /*
            Creates a TCPConnectToServer object on the specified server address and port
             */
            TCPConnectToServer tcpConnectToServer = new TCPConnectToServer(SERVER_ADDRESS, TCP_SERVER_PORT);
            /*
            Connects to the server
             */
            tcpConnectToServer.Connect();
            
            /*
            Sends a message over TCP socket to the server and prints out the received message from the server
             */
            
            for(int i=0; i<MESSAGE_TO_SERVER.length(); i++) {
                System.out.println(tcpConnectToServer.SendForAnswer(MESSAGE_TO_SERVER.charAt(i)+ ""));
                try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
        	long time2 = System.currentTimeMillis();
        	System.out.println(time2-time1);
            
            /*
            Disconnects from the TCP server
             */
            tcpConnectToServer.Disconnect();    	
        }
    }
}
