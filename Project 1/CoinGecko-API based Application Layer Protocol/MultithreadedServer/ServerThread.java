

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;


class ServerThread extends Thread
{
    protected BufferedReader is;
    protected PrintWriter os;
    protected Socket s;
    private String line = new String();

    /**
     * Creates a server thread on the input socket
     *
     * @param s input socket to create a thread on
     */
    public ServerThread(Socket s)
    {
        this.s = s;
    }

    /**
     * The server thread, echos the client until it receives the QUIT string from the client
     */
    public void run()
    {
    	String urlPrefix = "https://api.coingecko.com/api/v3/nfts/";
		URL url;

        try
        {
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());

            line = is.readLine();
            while (line.compareTo("QUIT") != 0)
            {
            	String[] tokens = line.split(" ");
            	if(tokens[0].equals("GET") && tokens.length==2) {
                    String dataFromAPI = "";
                    String messageToClient = "";
                 
            		try {        			
            			String urlString = urlPrefix + tokens[1];
            			url = new URL(urlString);
            			
            	    	//The server connects to CoinGecko-API
            			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            			
            			conn.setRequestMethod("GET");
            			conn.connect();
            			
            			try {
                			Scanner scanner = new Scanner(url.openStream());
                			
                			while(scanner.hasNext()) {
                				dataFromAPI += scanner.nextLine();
                			}
                			scanner.close();
                			
                			if(tokens[1].equals("list")) {
                    			JSONArray jsonObject = new JSONArray(dataFromAPI);
                    			
                    			for(int i=0; i<jsonObject.length(); i++) {
                    				String id = jsonObject.getJSONObject(i).getString("id");
                    				String name = jsonObject.getJSONObject(i).getString("name");
                    				messageToClient += "id: " + id + " name: " + name + ",";
                    			}                				
                			}else { //data belonging to specific NFT is extracted
                				JSONObject object = new JSONObject(dataFromAPI);
                				String id = object.getString("id");
                				String name = object.getString("name");
                				String platformId = object.getString("asset_platform_id");
            					JSONObject priceObject = (JSONObject)object.get("floor_price");
            					String priceInUSD = priceObject.get("usd").toString();

                				messageToClient += "id: " + id + ",name: " + name + ",asset platform id: " + platformId
                						+ ",floor price: $" + priceInUSD;
                			}
                			os.println("200 OK" + ";" + messageToClient);
                			os.flush();
            				
            			}catch(FileNotFoundException e) {
            				os.println("404 Not Found");
                			os.flush();
            			}catch(IOException e) { //when the number of API calls made exceeds the limit
            				os.println("429 Too Many Requests");
            				os.flush();
            			}
	
            		} catch (MalformedURLException e) {
            			e.printStackTrace();
            		} catch (IOException e) {
            			e.printStackTrace();
            		}
 		
            	}else { //wrong input format as request
            		os.println("405 Method Not Allowed");
        			os.flush();
            	}

                System.out.println("Client " + s.getRemoteSocketAddress() + " requested :  " + line);
  		
                line = is.readLine();
            }
        }
        catch (IOException e)
        {
            line = this.getName(); //reused String line for getting thread name
            System.err.println("Server Thread. Run. IO Error/ Client " + s.getRemoteSocketAddress() + " terminated abruptly");
        }
        catch (NullPointerException e)
        {
            line = this.getName(); //reused String line for getting thread name
            System.err.println("Server Thread associated with client " + s.getRemoteSocketAddress() + " Closed");
        } finally
        {
            try
            {
                if (is != null)
                {
                    is.close();
                }

                if (os != null)
                {
                    os.close();
                }
                if (s != null)
                {
                    s.close();
                }

            }
            catch (IOException ie)
            {
                System.err.println("Socket Close Error");
            }
        }//end finally
    }
}
