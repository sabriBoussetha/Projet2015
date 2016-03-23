package fr.univavignon.courbes.network.central.simpleimpl;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    ServerCommunicationImpl server = new ServerCommunicationImpl();
	
	@Override
	public boolean sendGameInformation() throws IOException {
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
	    String result = "";
	    System.out.println("Adresse ip du serveur : " + server.getIp());
	    String data = "new_game=" + URLEncoder.encode(server.getIp() + "|" + Constants.MAX_PLAYER_NBR, "UTF-8");
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    //InputStream error = ((HttpURLConnection) connection).getErrorStream();
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
	    	connection.disconnect();
            System.out.println(result);
	    	return true;
	    }catch(Throwable t) {
	        System.out.println("Error: " + t.getMessage());
	        return false;
	    }
	}
	
	@Override
	public void updateGameInformation(Integer newNbPlayer) throws IOException {
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
	    String result = "";
	    String stringNewNbPlayer = Integer.toString(newNbPlayer);
	    String data = "new_nb_player=" + URLEncoder.encode(server.getIp() + "|" + stringNewNbPlayer, "UTF-8");
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
	public void modifPlayer(Integer nbPlayer) throws IOException{
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
	    String result = "";
	    String player = Integer.toString(nbPlayer);
	    String data = "modif_player=" + URLEncoder.encode(server.getIp()+"|"+player, "UTF-8");
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
	public String searchGame() throws IOException{
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
	    String result = "";
	    String data = "search_game";
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
	    	connection.disconnect();
            System.out.println(result);
	    }catch(Throwable t) {
	        System.out.println("Error: " + t.getMessage());
	    }
		return result;
	}
	
	@Override
	public void reset() throws IOException {
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
	    String result = "";
	    String data = "reset_game=" + URLEncoder.encode(server.getIp(), "UTF-8");
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
	    	connection.disconnect();
            System.out.println(result);
	    }catch(Throwable t) {
	        System.out.println("Error: " + t.getMessage());
	    }		
	}	
	
	@Override
	public void deleteGame() throws IOException{
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
	    String result = "";
	    String data = "delete_game=" + URLEncoder.encode(server.getIp(), "UTF-8");
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    System.out.println("o");
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
	    	connection.disconnect();
            System.out.println(result);
	    }catch(Throwable t) {
	        System.out.println("Error: " + t.getMessage());
	    }		
	}
	
	@Override
	public void sendStats() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIP() {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getStats() {
		// TODO Auto-generated method stub
		
	}
}