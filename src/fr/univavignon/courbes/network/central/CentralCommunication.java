package fr.univavignon.courbes.network.central;

import java.io.IOException;

/**
 * Cette interface contient l'ensemble de méthodes qui permettent 
 * à l'application de communiquer avec le server central 
 * 
 * @author : Nathan Cheval
 * @author : Sabri Boussetha
 */
public interface CentralCommunication {
	/**
	 * Méthode permettant d'envoyer les informations nécessaires au serveur
	 * central pour commencer une partie.
	 * <br>
	 * Ces informations sont : 
	 * <ul>
	 * 		<li>L'adresse ip du serveur qui héberge la partie</li>
	 * 		<li>Le nombre maximum de joueur dans cette partie</li>
	 * 		<li>Le nombre de place restante</li>
	 * </ul>
	 * @return 
	 */
	boolean sendGameInformation() throws IOException;
	
	void updateGameInformation(Integer newNbPlayer) throws IOException;
	
	void reset() throws IOException;
	
	String searchGame() throws IOException;
	
	void sendStats();
	
	String getIP();
	
	void getStats();

	void removePlace() throws IOException;	
}
