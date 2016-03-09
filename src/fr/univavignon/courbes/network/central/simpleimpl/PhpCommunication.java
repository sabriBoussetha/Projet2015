package fr.univavignon.courbes.network.central.simpleimpl;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.network.central.CentralCommunication;
import fr.univavignon.courbes.network.simpleimpl.server.ServerCommunicationImpl;

/**
 * @author nathan
 *
 */
public class PhpCommunication implements CentralCommunication{
	@Override
	public void sendGameInformation() throws IOException {
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
	    String result = "";
	    ServerCommunicationImpl server = new ServerCommunicationImpl();
	    System.out.println(server.getIp());
	    String data = "infos=" + URLEncoder.encode(server.getIp()+"|"+Constants.MAX_PLAYER_NBR, "UTF-8");
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    try {
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setUseCaches(false);
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type",
	                "application/x-www-form-urlencoded");

	        // Envoyer les données en POST
	        DataOutputStream dataOut = new DataOutputStream(
	                connection.getOutputStream());
	        dataOut.writeBytes(data);
	        dataOut.flush();
	        dataOut.close();

            String line;
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = in.readLine()) != null) {
            	result += line;
            }
        
	    	in.close();
	    }
    	finally {
    		connection.disconnect();
            System.out.println(result);
        }
	}
	
	@Override
	public void updateGameInformation(Integer newNbPlayer) throws IOException {
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
	    String result = "";
	    String stringNewNbPlayer = Integer.toString(newNbPlayer);
	    String data = "new_nb_player=" + URLEncoder.encode(stringNewNbPlayer, "UTF-8");
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    try {
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setUseCaches(false);
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type",
	                "application/x-www-form-urlencoded");

	        // Envoyer les données en POST
	        DataOutputStream dataOut = new DataOutputStream(
	                connection.getOutputStream());
	        dataOut.writeBytes(data);
	        dataOut.flush();
	        dataOut.close();

            String line;
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = in.readLine()) != null) {
            	result += line;
            }
        
	    	in.close();
	    }
    	finally {
    		connection.disconnect();
            System.out.println(result);
        }
	}

	@Override
	public void sendStats() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getIP() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getStats() {
		// TODO Auto-generated method stub
		
	}	
}
