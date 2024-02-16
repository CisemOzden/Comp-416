import java.net.*;
import java.io.*;

public class HotColdClient {

	public static void main(String[] args) throws IOException{
		int port;
        String host = "localhost";
        String player1;
        String player2;
        
		System.out.println("Enter server socket's port");
		
		try(BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))){
	        String userInput;
	        userInput = stdIn.readLine();
	        port = Integer.valueOf(userInput);
	        
			try(Socket echoSocket = new Socket(host, port);
		            PrintWriter out = new PrintWriter (echoSocket.getOutputStream(), true);
		            BufferedReader in = new BufferedReader (new InputStreamReader(echoSocket.getInputStream()));
					){
				System.out.println("Server socket: " + echoSocket.getRemoteSocketAddress());
				player1 = in.readLine();
				System.out.println("Player 2 you will be playing with " + player1 + ", please enter your name:");
				player2 = stdIn.readLine();
				out.println(player2);
				
				for(int i=0; i<3; i++) {
					System.out.println("Waiting for player 1 guess...");

					String messagePrompt = in.readLine();
					System.out.println(messagePrompt);
					
					String guess2 = stdIn.readLine();
					out.println(guess2);
					
					String messageWinner = in.readLine();
					System.out.println(messageWinner);
				}
				System.out.println(in.readLine());
			}
		}		
	}
}
