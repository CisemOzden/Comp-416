import java.net.*;
import java.util.Random;
import java.io.*;

public class HotColdServer {

	public static void main(String[] args) throws IOException{
		int port;
		String player1;
		String player2;
		int player1Score = 0;
		int player2Score = 0;
		
		Random rand = new Random();
		int x = rand.nextInt(256);
		int y = rand.nextInt(256);
		
		System.out.println("Enter welcoming socket's port");
		
		try(BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))){
	        String userInput;
	        userInput = stdIn.readLine();
	        port = Integer.valueOf(userInput);
	        System.out.println("Waiting for client to connect...");
	        
			try(ServerSocket serverSocket = new ServerSocket(port);
		            Socket clientSocket = serverSocket.accept();
		            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					){
				System.out.println("Client socket: " + clientSocket.getRemoteSocketAddress());
				System.out.println("Player 1 please enter your name:");
				player1 = stdIn.readLine();
				out.println(player1);
				System.out.println("Waiting for player 2 name...");
				player2 = in.readLine();
				System.out.println("You are playing with " + player2);
				
				for(int i=0; i<3; i++) {
					System.out.println(player1 + " please enter your x and y guesses, comma separated.");
					
					String guess1 = stdIn.readLine();
					int x1 = Integer.valueOf(guess1.split(",")[0]);
					int y1 = Integer.valueOf(guess1.split(",")[1]);
					
					System.out.println("Waiting for player 2 guess...");
					out.println(player2 + " please enter your x and y guesses, comma separated.");

					String guess2 = in.readLine();

					int x2 = Integer.valueOf(guess2.split(",")[0]);
					int y2 = Integer.valueOf(guess2.split(",")[1]);
					
					if(euclidean(x1, x, y1, y) < euclidean(x2, x, y2, y)) {						
						player1Score++;
						System.out.println("Winner for round " + (i+1) + " is " + player1);
						out.println("Winner for round " + (i+1) + " is " + player1);
					}else if (euclidean(x1, x, y1, y) > euclidean(x2, x, y2, y)){
						player2Score++;
						System.out.println("Winner for round " + (i+1) + " is " + player2);
						out.println("Winner for round " + (i+1) + " is " + player2);
					}else {
						player1Score++;
						player2Score++;
						System.out.println("Winner for round " + (i+1) + " is Both players");
						out.println("Winner for round " + (i+1) + " is Both players");
					}	
				}
				
				if(player1Score>player2Score) {
					System.out.println("Game winner is " + player1);
					out.println("Game winner is " + player1);
				}else if(player1Score<player2Score) {
					System.out.println("Game winner is " + player2);
					out.println("Game winner is " + player2);
				}else {
					System.out.println("Game winner is Both players");
					out.println("Game winner is Both players");
				}		
			}       
		}
	}
	
	public static double euclidean(int x1, int x2, int y1, int y2) {
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}

}
