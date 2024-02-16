import java.net.SocketException;
import java.util.Scanner;


public class MultithreadClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ConnectionToServer connectionToServer = new ConnectionToServer(ConnectionToServer.DEFAULT_SERVER_ADDRESS, ConnectionToServer.DEFAULT_SERVER_PORT);
        connectionToServer.Connect();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a request for the NFTNet server");
        
        String message = scanner.nextLine();
        
        //for the test purpose of the exceeded call to CoinGecko-API       
        /*for(int j=0; j<55; j++) {
        	String response = connectionToServer.SendForAnswer(message);
        	if(response != null && response.equals("CLOSED")) {
        		connectionToServer.Disconnect();
        		break;
        	}
        	String[] lines = response.split("[;,]");
            System.out.println("Response from NFTNetServer:");
            
            for(int i=0; i<lines.length; i++) {
            	System.out.println(lines[i]);
            }
            System.out.println();
        }*/
        while (true)
        {
        	if(message.equals("QUIT")) {
        		connectionToServer.Disconnect();
        		break;
        	}
        	          	
        	String response = connectionToServer.SendForAnswer(message);
        	if(response != null && response.equals("CLOSED")) {
        		connectionToServer.Disconnect();
        		break;
        	}
        	//response coming from the server is divided into lines
        	String[] lines = response.split("[;,]");
            System.out.println("Response from NFTNetServer:");
            
            for(int i=0; i<lines.length; i++) {
            	System.out.println(lines[i]);
            }
            System.out.println();
            System.out.println("Enter a request for the NFTNet server");
            message = scanner.nextLine();

        }
        scanner.close();
    }
    
}
