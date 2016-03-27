package fr.univavignon.courbes.inter.central;

import java.awt.Dimension;
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
import fr.univavignon.courbes.inter.simpleimpl.remote.AbstractConnectionPanel;
import fr.univavignon.courbes.inter.simpleimpl.remote.RemotePlayerConfigPanel;
import fr.univavignon.courbes.inter.simpleimpl.remote.RemotePlayerSelectionPanel;
import fr.univavignon.courbes.network.ClientCommunication;
import fr.univavignon.courbes.network.central.simpleimpl.PhpCommunication;
import fr.univavignon.courbes.network.kryonet.ClientCommunicationKryonetImpl;
import fr.univavignon.courbes.network.simpleimpl.client.ClientCommunicationImpl;





/**
 * @author sabri
 * 
 * 	Cette classe corréspond à la fênetre d'attente d'une partie publique sur le serveur central
 * 
 * */


public class ClientGameCentralConnectionPanel  extends AbstractConnectionPanel implements ClientConnectionHandler{
	
	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	
	private static final String TITLE = "Connexion au serveur central";
	
	/** Instance de la classe PhpCommunication permetant de se connecter aux serveurs disponibles sur le central*/ 
	private PhpCommunication search;
	
	private CentralAvailableServers []servers = null;
	
	private int numberServers;
	
	JPanel panel;
	JButton join;
	JLabel serverLabel;
	
	/**
	 * @param mainWindow
	 */
	public ClientGameCentralConnectionPanel(MainWindow mainWindow)
	{	
		super(mainWindow, TITLE);
		///JLabel lab1 = new JLabel("User Name", JLabel.LEFT);
		//this.add(new JLabel("Hiiiiiii"));
		search = new PhpCommunication();
		/*boolean connected = connect();
		
		if(connected)
		{	// on désactive les boutons le temps de l'attente
			//backButton.setEnabled(false);
			//nextButton.setEnabled(false);
			System.out.println("yo");
			// puis on se contente d'attendre la réponse : acceptation ou rejet
			// la méthode correspondante du handler sera automatiquement invoquée
		}*/
	}
	
	/**
	 * @return 
	 */
	private boolean connect()
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
			ipStr = search.searchGame("All servers");
			// URL du fichier qui se trouve dans le serveur est qui contient la liste des serveurs disponible
			allServers = "https://pedago02a.univ-avignon.fr/~uapv1501163/server/listServers.json";
			
		    URL url = new URL(allServers);
			 
		    // Lecture à partir du fichier JSON
		    Scanner scan = new Scanner(url.openStream());
		    String str = new String();
		    while (scan.hasNext())
		        str += scan.nextLine();		// Mettre le contenu du fichier dans une chaine de charactère
		    scan.close();
		    
		    //JSONObject objectJson = new JSONObject(str);
		    JSONArray arrayJSON = new JSONArray(str);			// Création d'un object JSONArray à partir la variable String
		    numberServers = arrayJSON.length();
		    servers = new CentralAvailableServers[arrayJSON.length()];		
		    JSONObject tmp;
			//this.removeAll();
		    // Pour tout les elements du JSONArray
		    for(int i=0; i<arrayJSON.length(); i++)	
		    {
		    	tmp=arrayJSON.getJSONObject(i);
		    	servers[i] = new CentralAvailableServers();
		    	servers[i].setIpHost(tmp.getString("ip_host"));
		    	servers[i].setAvailablePlaces(tmp.getInt("available_places"));
		    	
		    	System.out.println(tmp.getString("ip_host") + " | " + tmp.getInt("available_places"));
		    	//this.add(new JLabel(servers[i].getIpHost() + " " + servers[i].getAvailablePlaces()));
		    }
		    this.validate();
			this.repaint();
			// On enlève une place disponible dans la table	
			search.sendInformation(ipStr, -1, 3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		ipStr = ipTextField.getText();
		clientCom.setIp(ipStr);
		SettingsManager.setLastServerIp(ipStr);
		
		// On récupère le numéro de port
		int port = 9999;
//		String portStr = portTextField.getText();
//		int port = Integer.parseInt(portStr);
		clientCom.setPort(port);
		SettingsManager.setLastServerPort(port);
		
		// puis on se connecte
		boolean result = clientCom.launchClient();
		return result;
	}
	
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

	@Override
	public String getDefaultIp() {
		return null;	
	}

	@Override
	public int getDefaultPort() {
		
		return 0;
	}

	@Override
	protected void nextStep() throws IOException {
		// on se connecte
		/*boolean connected = connect();
		
		if(connected)
		{	// on désactive les boutons le temps de l'attente
			backButton.setEnabled(false);
			nextButton.setEnabled(false);
		
			// puis on se contente d'attendre la réponse : acceptation ou rejet
				// la méthode correspondante du handler sera automatiquement invoquée
		}*/
		
		//else
		//{	
		sound.errorSound();
		JOptionPane.showMessageDialog(mainWindow, 
				"<html>Il n'est pas possible d'établir une connexion avec le serveur.</html>");
		//}
		
	}

	@Override
	protected void previousStep() {
		mainWindow.clientCom = null;
		mainWindow.displayPanel(PanelName.CLIENT_GAME_PLAYER_SELECTION);
		
	}
	
	/**
	 * Panel contenant la liste des serveurs disponibles
	 * */
	

	
	@Override
	protected void initContent(){	
		super.initContent();
		//boolean connected = connect();
		ipTextField.setEnabled(false);	// je désactive le text field	
		
		panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(layout);
		add(panel);
		
		JPanel titlePanel = new JPanel();
		layout = new BoxLayout(titlePanel, BoxLayout.LINE_AXIS);
		titlePanel.setLayout(layout);
		panel.add(titlePanel);
		int height = 20;
		Dimension dim;
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		
		JLabel serverLabel = new JLabel("Servers");
		serverLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dim = new Dimension(500,height);
		serverLabel.setPreferredSize(dim);
		serverLabel.setMaximumSize(dim);
		serverLabel.setMinimumSize(dim);
		serverLabel.setBorder(border);
		titlePanel.add(serverLabel);

		titlePanel.add(Box.createHorizontalGlue());
		
		
		/*JLabel leftLabel = new JLabel("Rejeter");
		leftLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dim = new Dimension(200,height);
		leftLabel.setPreferredSize(dim);
		leftLabel.setMaximumSize(dim);
		leftLabel.setMinimumSize(dim);
		leftLabel.setBorder(border);
		titlePanel.add(leftLabel);*/
		
		add(Box.createHorizontalGlue());
		System.out.println(numberServers);

		join = new JButton("Joindre Ce Serveur");
		join.addActionListener(this);
		dim = new Dimension(200,height);
		join.setPreferredSize(dim);
		join.setMaximumSize(dim);
		join.setMinimumSize(dim);
		join.setBackground(Constants.PLAYER_COLORS[2]);
		add(this.join);
		join.setEnabled(false);
			
			/*dim = new Dimension(200,height);
			//serverLabel.setPreferredSize(dim);
			serverLabel.setMaximumSize(dim);
			serverLabel.setMinimumSize(dim);
			//serverLabel.setBackground(Constants.PLAYER_COLORS[player.playerId]);
			serverLabel.setOpaque(true);
			add(serverLabel);*/
			
			//add(Box.createHorizontalGlue());
		boolean connected = connect();
		if(connected){
			for(int i=0; i<servers.length; i++)
			{
				this.add(new JLabel(servers[i].getIpHost() + " " + servers[i].getAvailablePlaces()));
			}
		}
			
	}
	
	
	protected void intPlayerPanel()
	{
		
	}

}
