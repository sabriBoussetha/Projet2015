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
import fr.univavignon.courbes.network.ClientCommunication;
import fr.univavignon.courbes.network.central.CentralCommunication;
import fr.univavignon.courbes.network.simpleimpl.server.ServerCommunicationImpl;

/**
 * @author : Nathan Cheval
 * @author : Sabri Boussetha
 *
 */
public class PhpCommunication implements CentralCommunication{

    ServerCommunicationImpl server = new ServerCommunicationImpl();
	
    /**
     * Fonction permettant l'envoie d'information au serveur central
     * 
     * Si le choix est égale à 1 --> Envoie des informations de la partie lors de sa création
     * Si le choix est égale à 2 --> Modification du nombre de joueurs en fonction du nombres de joueurs locaux 
     * Si le choix est égale à 3 --> Ajout ou suppression de joueurs distants lors de la connexion ou d'un kick
     * Si le choix est égale à 4 --> Reset de la partie (remise du nombre de joueurs disponible au nombre max)
     * Si le choix est égale à 5 --> Suppression de la partie de la base de donnée
     * 
     * @param ip
     * @param nbPlayer
     * @param choix
     * @throws IOException
     */
    
	@Override
	public boolean sendInformation(String ip, Integer nbPlayer,Integer choix) throws IOException {
		if(ip == "NULL"){
			ip = server.getIp();
		}
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    String result = "";
	    String data = "";
		if(choix == 1){
			System.out.println("Adresse ip du serveur : " + ip);
			data = "new_game=" + URLEncoder.encode(ip + "|" + Constants.MAX_PLAYER_NBR, "UTF-8");
		}
		else if(choix == 2){ 
			String NewNbLocalPlayer = Integer.toString(nbPlayer);
			data = "new_nb_player=" + URLEncoder.encode(ip + "|" + NewNbLocalPlayer, "UTF-8");
		}
		else if(choix == 3){
			String player = Integer.toString(nbPlayer);
		    data = "modif_player=" + URLEncoder.encode(ip+"|"+player, "UTF-8");
		}
		else if(choix == 4){ 
			data = "reset_game=" + URLEncoder.encode(ip, "UTF-8");
		}
		else if(choix == 5){
			data = "delete_game=" + URLEncoder.encode(server.getIp(), "UTF-8");
		}
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
	public String searchGame(String choice) throws IOException{
		String data = null;
		if(choice=="All servers")
			data = "search_game_json";
		else if(choice=="server")
			data = "search_game";
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
	    String result = ""; 
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
	
	/**
	 * 
	 * Fonction permettant l'ajout de joueur dans la base de données
	 * 
	 * @param pseudo
	 * @param country
	 * @param ELO
	 * @param password
	 * @throws IOException
	 */
	public Integer addPlayer(String pseudo, String country,String password) throws IOException{
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
	    String result = "";
	    String data = "add_player=" + URLEncoder.encode(pseudo+"|"+country+"|"+password, "UTF-8");
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
            //System.out.println(result);
	    }catch(Throwable t) {
	        System.out.println("Error: " + t.getMessage());
	    }
	    System.out.println(result);
	    return Integer.parseInt(result);
	}
	
	public static String getPlayer() throws IOException{
		String data = "get_player=" + URLEncoder.encode("UTF-8");
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
	    String result = ""; 
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
            //System.out.println(result);
	    }catch(Throwable t) {
	        System.out.println("Error: " + t.getMessage());
	    }
	    
		return result;
	}
	
	public static String getElo(Integer id) throws IOException{
		String data = "get_elo=" +URLEncoder.encode("" + id, "UTF-8");
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
	    String result = "";
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
            //System.out.println(result);
	    }catch(Throwable t) {
	        System.out.println("Error: " + t.getMessage());
	    }
	    
		return result;
	}
	
	public static String getPseudo(Integer id) throws IOException{
		String data = "get_pseudo=" +URLEncoder.encode("" + id, "UTF-8");
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
	    String result = "";
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
            //System.out.println(result);
	    }catch(Throwable t) {
	        System.out.println("Error: " + t.getMessage());
	    }
	    
		return result;
	}
	
	
}