package fr.univavignon.courbes.network.groupe22;
import fr.univavignon.courbes.network.ClientCommunication;
import java.net.*;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientCom implements ClientCommunication
{
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private String ipServer;
    private int portServer;
    private Socket connexion=null;
  /**
     * Renvoie l'adresse IP du serveur auquel le client se connecte.
     *
     * @return
     *         Une chaîne de caractères qui correspond à l'adresse IP du serveur.
     */
    @Override
    public String getIp() {
        return ipServer;
    }

    /**
     * Modifie l'adresse IP du serveur auquel le client va se connecter.
     * Cette valeur est à modifier avant d'utiliser {@link #launchClient}.
     *
     * @param ip
     *         La nouvelle IP du serveur.
     */
    @Override
    public void setIp(String ip) {
        ipServer=ip;

    }

    /**
     * Renvoie le port du serveur auquel le client se connecte.
     *
     * @return
     *         Un entier qui correspond au port du serveur.
     */
    @Override
    public int getPort() {
        return portServer;
    }

    /**
     * Modifie le port du serveur auquel le client va se connecter.
     * Cette valeur est à modifier avant d'utiliser {@link #launchClient}.
     *
     * @param port
     *         Le nouveau port du serveur.
     */
    @Override
    public void setPort(int port) {
        portServer=port;
    }

    /**
     * Permet au client de se connecter au serveur dont on a préalablement
     * configuré l'adresse IP et le port.
     * <br/>
     * Cette méthode doit être appelée par l'Interface Utilisateur lorsque
     * l'utilisateur décide de se connecter à une partie réseau existante.
     */
    @Override
    public void launchClient() {
        try {
             connexion = new Socket(ipServer, portServer);
        }
        catch (UnknownHostException e) {
             e.printStackTrace();
        }
        catch (IOException e) {
             e.printStackTrace();
        }
    }

     /**
     * Permet à un client de clore sa connexion avec le serveur.
     */
    @Override
    public void closeClient() {
        try {
            connexion.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
  	 * Envoie au serveur le profil d'un joueur désirant participer à la partie
  	 * en cours de configuration. Si plusieurs joueurs utilisent le même client,
  	 * alors la méthode doit être appelée plusieurs fois successivement. Chaque
  	 * joueur peut être refusé par le serveur, par exemple si la partie ne peut
  	 * pas accueillir plus de joueurs.
  	 *
  	 * @param profile
  	 * 		Profil du joueur à ajouter à la partie.
  	 */
  	public void sendProfile(Profile profile);
  
  	/**

     * Récupère la liste des profils des joueurs participant à la manche,
     * envoyée par le serveur. Les profils sont placés dans l'ordre des ID
     * des joueurs pour cette partie.
     * <br/>
     * Cette méthode est invoquée par l'Interface Utilisateur de manière
     * à ce que le client obtienne l'identité des joueurs participant à une partie.
     * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante :
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu.
     *
     * @return
     *         Liste des profils participant à la partie, ou {@code null} si aucune
     *         liste n'a été envoyée.
     */
    public List<Profile> retrieveProfiles();

    /**
     * Récupère la limite de points à atteindre pour gagner la partie,
     * limite envoyée par le serveur auquel ce client est connecté.
     * <br/>
     * Cette méthode est invoquée par l'Interface Utilisateur à chaque
     * début de manche. En effet, la limite peut changer à chaque
     * manche en fonction du nombre de points des joueurs (pour gagner,
     * il faut avoir un certain nombre de points d'avance sur le 2ème).
     * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante :
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu.
     *
     * @return pointThreshold
     *         Limite de point courante de la partie, ou {@code null} si aucune
     *         valeur n'a été envoyée.
     */
    public Integer retrievePointThreshold();

    /**
     * Permet au client de récupérer des informations sur l'évolution de
     * la manche en cours, envoyées par le serveur auquel il est connecté.
     * <br/>
     * Cette méthode est appelée par l'Interface Utilisateur à
     * chaque itération d'une manche.
      * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante :
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu.
    *
     * @return board
     *         Etat courant de l'aire de jeu, ou {@code null} si aucune mise à jour
     *         n'a été envoyée.
     */
    public Board retrieveBoard();

    /**
     * Permet au client d'envoyer les commandes générées par les joueurs qu'il gère.
     * Ces commandes sont passées sous forme de map: l'entier correspond à l'ID du joueur
     * <i>sur le serveur</i>, pour la manche en cours, et la direction correspond à la
     * commande générée par le joueur. Si un joueur n'a pas généré de commande, alors la
     * valeur associée doit être {@link Direction#NONE}.
     * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante :
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu.
     *
     * @param commands
     *         Une liste contenant les directions choisies par chaque joueur local au client.
     */
    public void sendCommands(Map<Integer,Direction> commands);

    /**
     * Permet au client de récupérer un message textuel envoyé par le serveur
     * auquel il est connecté.
     * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante :
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu.
     *
     * @return
     *         Contient le message envoyé par le serveur, ou {@code null} si aucun message
     *         n'a été envoyé.
     */
    public String retrieveText();

    /**
     * Permet au client d'envoyer un message textuel au serveur auquel il est
     * connecté.
     * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante :
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu.
     *
     * @param message
     *         Le message textuel à envoyer au serveur.
     */
    public void sendText(String message);
}
