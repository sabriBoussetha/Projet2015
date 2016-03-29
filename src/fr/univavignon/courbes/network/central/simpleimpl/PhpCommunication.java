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
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.simpleimpl.AbstractRoundPanel;
import fr.univavignon.courbes.network.ClientCommunication;
import fr.univavignon.courbes.network.central.CentralCommunication;
import fr.univavignon.courbes.network.simpleimpl.server.ServerCommunicationImpl;

/**
 * @author : Nathan Cheval
 * @author : Sabri Boussetha
 * @author : Charlie Brugvin
 *
 */
public class PhpCommunication implements CentralCommunication{

	static String servAdr = "https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php";

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
     * 
     * @author Nathan
     */
    
	@Override
	public boolean sendInformation(String ip, Integer nbPlayer,Integer choix) throws IOException {
		if(ip == "NULL"){
			ip = server.getIp();
		}
		URL url = new URL(servAdr);
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
	
	/**
     * Fonction permettant l'envoie d'information au serveur central
     * 
     * Fonction permettant la recherche automatique des parties disponible
     * 
     * Si le String choice = "All servers" --> On retourne un fichier JSON afin d'établir une liste des serveurs disponible
     * et d'en afficher une liste avec la possibilité pour le joueur de choisir son serveur
     * 
     * Si le String choice = "server" --> La fonction PHP appellée renvoi seulement le premier serveur disponible 
     * 
     * @param choice
     * @param userName
     * @param password
     * @throws IOException
     * 
     * @author Sabri
     */
	@Override
	public String searchGame(String choice, String userName, String password) throws IOException{
		String data = null;
		if(choice=="All servers")
			data = "search_game_json"+ URLEncoder.encode(userName+"|"+password, "UTF-8");
		else if(choice=="server")
			data = "search_game" + URLEncoder.encode(userName+"|"+password, "UTF-8");
		URL url = new URL(servAdr);
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
	 * 
	 * @author Nathan
	 */
	public Integer addPlayer(String pseudo, String country,String password) throws IOException{
		URL url = new URL(servAdr);
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
	
	
	/**
	 * 
	 * Fonction permettant de retourner un joueur
	 * 
	 * @author Nathan
	 * @return
	 * @throws IOException
	 */
	public static String getPlayer() throws IOException{
		String data = "get_player=" + URLEncoder.encode("UTF-8");
		URL url = new URL(servAdr);
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
	
	
	/**
	 * 
	 * Fonction qui envoie une reqûte de suppression de joueur
	 * 
	 * @author Charlie
	 * @author Alexandre
	 * 
	 * @param id du joueur
	 * @throws IOException
	 */
	public static void deletePlayer(Integer id) throws IOException{
		String data = "delete_player=" + URLEncoder.encode("" + id, "UTF-8");
		URL url = new URL(servAdr);
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
	}	
	
	/**
	 * Fonction qui recupere les classement ELO d'un joueur, associré a leurs dates
	 * 
	 * @author Charlie
	 * @param identifiant du joueur
	 * @return tableau associant les classements elo a leurs dates sous format JSON
	 * @throws IOException
	 */
	public static String getElo(Integer id) throws IOException{
		String data = "get_elo=" +URLEncoder.encode("" + id, "UTF-8");
		URL url = new URL(servAdr);
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
	
	/**
	 * Fonction qui recupere le pseudo a partir de l'iendentifiant d'un joueur
	 * 
	 * @param identifiant d'un joueur
	 * @return le pseudonyme du joueur
	 * @throws IOException
	 * @author Charlie
	 */
	public static String getPseudo(Integer id) throws IOException{
		String data = "get_pseudo=" +URLEncoder.encode("" + id, "UTF-8");
		URL url = new URL(servAdr);
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
	
	/**
     * Fonction static qui envoie au server les informations à mettre à jour après une manche
     * 
     * @author : Alexandre Latif
     * 
     * @param id : le profileId d'un joueur
     * @param score : le score d'un joueur pendant une manche
     * @param gagne : un boolean renvoyant si le joueur a gagné la manche ou pas (mais ce n'est pas vraiment utilisé)
     * @throws raisonMort : String indiquant la raison de mort d'un joueur
     */
	
	public static void updateManche(Integer id, Integer score, boolean gagne, String raisonMort) throws IOException{
		String data = "update_manche=" +URLEncoder.encode("" + id + "|" + score + "|" + gagne + "|" + raisonMort, "UTF-8");
		URL url = new URL(servAdr);
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
	    
	}
	
	
		
	/**
     * Fonction static qui envoie au server les informations à mettre à jour après une partie
     * 
     * @author : Alexandre Latif
     * 
     * @param idPlayer : un tableau contenant les id des joueurs par ordre décroissant de points totaux
     */	
	public static void updateMatch(int [] idPlayer) throws IOException{
		//on met le nombre de joueurs au début du string d'infos qu'on va envoyer au serveur central
		String info = "" + idPlayer.length + "|";
		//puis on rajoute le profileId de chaque joueur par ordre, séparés par un pipe pour les parser côté serveur
		for (int id : idPlayer){
			info += id + "|";
		}
		
		String data = "update_match=" +URLEncoder.encode("" + info, "UTF-8");
		URL url = new URL(servAdr);
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
	        System.out.println("AAAAA" + result);
	    }catch(Throwable t) {
	        System.out.println("Error: " + t.getMessage());
	    }
    
	}
	
	
	
}