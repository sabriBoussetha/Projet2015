package fr.univavignon.courbes.inter.central;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.inter.ClientConnectionHandler;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager.NetEngineImpl;
import fr.univavignon.courbes.inter.simpleimpl.profiles.ProfileManager;
import fr.univavignon.courbes.inter.simpleimpl.remote.AbstractConnectionPanel;
import fr.univavignon.courbes.inter.simpleimpl.remote.RemotePlayerConfigPanel;
import fr.univavignon.courbes.inter.simpleimpl.remote.RemotePlayerSelectionPanel;
import fr.univavignon.courbes.network.ClientCommunication;
import fr.univavignon.courbes.network.central.simpleimpl.PhpCommunication;
import fr.univavignon.courbes.network.kryonet.ClientCommunicationKryonetImpl;
import fr.univavignon.courbes.network.simpleimpl.client.ClientCommunicationImpl;

/**
 * 
 * 	Cette classe correspond à la fênetre qui affiche tout les parties publiques
 *  disponibles sur le serveur central et ayant des places disponibles.
 *  Elle permet ainsi de joindre le serveur souhaité 
 * 
 * 
 * 	@author sabri
 */


public class ClientGameCentralConnectionPanel  extends AbstractConnectionPanel implements ClientConnectionHandler{
	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	/** Nom de la fenêtre */
	private static final String TITLE = "Connexion au serveur central";
	/** Instance de la classe PhpCommunication permetant de se connecter aux serveurs disponibles sur le central*/ 
	private PhpCommunication search;
	/** Tableau contenant tout les serveurs disponibles du le Central*/
	private CentralAvailableServers []servers = null;
	/** Panel centant l'ensemble de composantes de la fênetre*/
	JPanel panel;
	/** Tableau de boutons corréspondant aux boutons 'joindre' de chaque serveur */
	JButton []join;
	/** Tableau de JLabel contenant l'adresse ip et le nombre de place disponible sur le serveur*/
	JLabel []serverLabel;
	/** Variable bouleénne permettant de décider de se connecter à un serveur dans la méthode connect(int) */
	boolean joinServer=false;
	
	/**	
	 * @param mainWindow
	 */
	public ClientGameCentralConnectionPanel(MainWindow mainWindow)
	{	
		super(mainWindow, TITLE);
		search = new PhpCommunication();
		boolean connected = connect(0);
	}
	
	/**
	 * Métode permettant de se connecter à un serveur disponible sur la base de données du central.
	 * Ainsi que l'affichage de la liste des serveurs disponibles en utilisant le parse
	 * du fichier JSON qui contient les serveurs disponibles  
	 * 
	 * @param nbButton
	 * 		l'indice du bouton correspondant à un serveur disponible
	 * @return 
	 * 		boolean {@true} si on arrive à se connecter au serveur 
	 */
	private boolean connect(int nbButton)
	{	// on initialise le Moteur Réseau
		ClientCommunication clientCom = null;
		NetEngineImpl netEngineImpl = SettingsManager.getNetEngineImpl();
		switch(netEngineImpl)
		{	case KRYONET:
				clientCom = new ClientCommunicationKryonetImpl();
				break;
			case SOCKET:
				clientCom = new ClientCommunicationImpl();
				break;
		}
		
		mainWindow.clientCom = clientCom;
		clientCom.setErrorHandler(mainWindow);
		clientCom.setConnectionHandler(this);
		
		// On récupère l'adresse ip d'un serveur disponible 	
		String ipStr = null, allServers = null;
		
		try {
			/* En appellant cette methode la méthode searchGameJson()
			 *  mettra les serveurs disponible dans un fihcier JSON */
			ipStr = search.searchGame("All servers",mainWindow.clientPlayer.profile.userName,mainWindow.clientPlayer.profile.password);
			// URL du fichier qui se trouve dans le serveur et qui contient la liste des serveurs disponibles
			allServers = "https://pedago02a.univ-avignon.fr/~uapv1402577/server/listServers.json";
			System.out.println(ipStr);
		    URL url = new URL(allServers);
			 
		    // Lecture à partir du fichier JSON
		    Scanner scan = new Scanner(url.openStream());
		    String str = new String();
		    while (scan.hasNext())
		        str += scan.nextLine();		// Mettre le contenu du fichier dans une chaine de charactère
		    scan.close();
		    
		    // Création d'un object JSONArray à partir la variable String
		    JSONArray arrayJSON = new JSONArray(str);			
		    servers = new CentralAvailableServers[arrayJSON.length()];		
		    JSONObject tmp;
		    // Effacer le contenue de la fênetre 
		    this.removeAll();
			initContent();
			
			BorderLayout borderLayout = new BorderLayout();
			setLayout(borderLayout);
			panel = new JPanel();
			BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
			panel.setLayout(boxLayout);
				
			panel.add(Box.createVerticalGlue());
		 
			serverLabel = new JLabel[arrayJSON.length()];
			join = new JButton[arrayJSON.length()];
			// Pour tout les elements du JSONArray
		    for(int i=0; i<arrayJSON.length(); i++)	
		    {
		    	tmp=arrayJSON.getJSONObject(i);
		    	servers[i] = new CentralAvailableServers(); // Instancier un object pour stocker un serveur 
		    	servers[i].setIpHost(tmp.getString("ip_host"));
		    	servers[i].setAvailablePlaces(tmp.getInt("available_place"));
		    	
		    	serverLabel[i] = new JLabel("Adresse IP : " + servers[i].getIpHost() + "----------- Places disponibles : " + servers[i].getAvailablePlaces());
		    	System.out.println(tmp.getString("ip_host") + " | " + tmp.getInt("available_place"));
		    	panel.add(serverLabel[i]);
		    	
		    	join[i] = new JButton();
		    	join[i] = initButton("Joindre",150,50);
		    	panel.add(join[i]);
		    	panel.add(Box.createVerticalStrut(10));
		    }
		    
		    backButton.setEnabled(true);
		    panel.add(backButton);
		    panel.add(Box.createVerticalGlue());
		    add(panel, BorderLayout.CENTER);
		    
		    this.validate();
			this.repaint();
			// On enlève une place disponible dans la table	
			if(joinServer)
				search.sendInformation(servers[nbButton].getIpHost(), -1, 3);
			
		} catch (IOException e) {
			System.out.println("Cannot coonect!");
		}
		
		
		boolean result=false;
		if(joinServer)		
		{	// puis on se connecte
			clientCom.setIp(servers[nbButton].getIpHost());
			SettingsManager.setLastServerIp(servers[nbButton].getIpHost());
			
			// On récupère le numéro de port
			int port = 9999;

			clientCom.setPort(port);
			SettingsManager.setLastServerPort(port);
			
			result = clientCom.launchClient();
		}
		return result;
	}
	/*
	 * (non-Javadoc)
	 * @see fr.univavignon.courbes.inter.ClientConnectionHandler#gotRefused()
	 */
	@Override
	public void gotRefused() {
		SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	sound.errorSound();
				JOptionPane.showMessageDialog(mainWindow, 
					"<html>Le serveur a rejeté votre candidature, car il ne reste "
					+ "<br/>pas de place dans la partie en cours de configuration.</html>");
			}
	    });
		
	}
	/*
	 * (non-Javadoc)
	 * @see fr.univavignon.courbes.inter.ClientConnectionHandler#gotAccepted()
	 */
	@Override
	public void gotAccepted() {
		SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	mainWindow.clientCom.setConnectionHandler(null);
				mainWindow.displayPanel(PanelName.CLIENT_GAME_WAIT);
			}
	    });		
	}
	/*
	 * (non-Javadoc)
	 * @see fr.univavignon.courbes.inter.simpleimpl.remote.AbstractConnectionPanel#getDefaultIp()
	 */
	@Override
	public String getDefaultIp() {
		return null;	
	}
	/*
	 * (non-Javadoc)
	 * @see fr.univavignon.courbes.inter.simpleimpl.remote.AbstractConnectionPanel#getDefaultPort()
	 */
	@Override
	public int getDefaultPort() {
		return 0;
	}
	/*
	 * (non-Javadoc)
	 * @see fr.univavignon.courbes.inter.simpleimpl.AbstractConfigurationPanel#nextStep()
	 */
	@Override
	protected void nextStep() throws IOException 
	{
		if(joinServer)
		{	// on désactive les boutons le temps de l'attente
			backButton.setEnabled(false);
			nextButton.setEnabled(true);
			// puis on se contente d'attendre la réponse : acceptation ou rejet
				// la méthode correspondante du handler sera automatiquement invoquée
		}
		else
		{	sound.errorSound();
			JOptionPane.showMessageDialog(mainWindow, 
				"<html>Il n'est pas possible d'établir une connexion avec le serveur.</html>");
		}
		
	}
	/**
	 * (non-Javadoc)
	 * @see fr.univavignon.courbes.inter.simpleimpl.AbstractConfigurationPanel#previousStep()
	 */
	@Override
	protected void previousStep() {
		mainWindow.clientCom = null;
		mainWindow.displayPanel(PanelName.CLIENT_GAME_PLAYER_SELECTION);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see fr.univavignon.courbes.inter.simpleimpl.remote.AbstractConnectionPanel#initContent()
	 */
	@Override
	protected void initContent(){	
		super.initContent();
		ipTextField.setEnabled(false);	// je désactive le text field	
	}
	
	/**
	 * Initialise chaque bouton de la même façon.
	 * 
	 * @param text
	 * 		Texte à inclure dans le bouton.
	 * @param width
	 * 		Correspond à la largeur du bouton
	 * @param height
	 * 		La longueur du bouton
	 * @return
	 * 		Bouton convenablement configuré. 
	 */
	private JButton initButton(String text, int width, int height)
	{	JButton result = new JButton(text);
	
		Font font = getFont();
		font = new Font(font.getName(),Font.PLAIN,25);
		result.setFont(font);
		
		Dimension dim = new Dimension(width,height);
		result.setMaximumSize(dim);
		result.setMinimumSize(dim);
		result.setPreferredSize(dim);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		result.addActionListener(this);
		
		return result;
	}
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{	sound.clickSound(); // Son correspondant à un click
		// Parcourir le tableau des boutons
		for(int i=0; i<join.length; i++)	
		{
			if(e.getSource()==join[i])	
			{	joinServer=true;
				boolean a = connect(i);
				System.out.println(a);
			}
		}
		if(e.getSource()==backButton){
			mainWindow.displayPanel(PanelName.CLIENT_GAME_PLAYER_SELECTION);
		}
		
	}

}
