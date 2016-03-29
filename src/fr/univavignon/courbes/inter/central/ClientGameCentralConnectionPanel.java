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
 * @author sabri
 * 
 * 	Cette classe correspond à la fênetre d'attente d'une partie publique sur le serveur central
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
	JButton []join;
	JLabel []serverLabel;
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
	 * @return 
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
			ipStr = search.searchGame("All servers",mainWindow.clientPlayer.profile.userName,mainWindow.clientPlayer.profile.password);
			// URL du fichier qui se trouve dans le serveur est qui contient la liste des serveurs disponible
			allServers = "https://pedago02a.univ-avignon.fr/~uapv1402577/server/listServers.json";
			
		    URL url = new URL(allServers);
			 
		    // Lecture à partir du fichier JSON
		    Scanner scan = new Scanner(url.openStream());
		    String str = new String();
		    while (scan.hasNext())
		        str += scan.nextLine();		// Mettre le contenu du fichier dans une chaine de charactère
		    scan.close();

		    JSONArray arrayJSON = new JSONArray(str);			// Création d'un object JSONArray à partir la variable String
		    numberServers = arrayJSON.length();
		    servers = new CentralAvailableServers[arrayJSON.length()];		
		    JSONObject tmp;
		    
		    this.removeAll();
			initContent();
			
			BorderLayout borderLayout = new BorderLayout();
			setLayout(borderLayout);
			panel = new JPanel();
			BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
			panel.setLayout(boxLayout);
				
			panel.add(Box.createVerticalGlue());
		    // Pour tout les elements du JSONArray
			serverLabel = new JLabel[arrayJSON.length()];
			join = new JButton[arrayJSON.length()];
		    for(int i=0; i<arrayJSON.length(); i++)	
		    {
		    	tmp=arrayJSON.getJSONObject(i);
		    	servers[i] = new CentralAvailableServers();
		    	servers[i].setIpHost(tmp.getString("ip_host"));
		    	servers[i].setAvailablePlaces(tmp.getInt("available_place"));
		    	ipStr = servers[i].getIpHost();
		    	
		    	serverLabel[i] = new JLabel("Adresse IP : " + servers[i].getIpHost() + "----------- Places disponibles : " + servers[i].getAvailablePlaces());
		    	System.out.println(tmp.getString("ip_host") + " | " + tmp.getInt("available_place"));
		    	panel.add(serverLabel[i]);
		    	
		    	join[i] = new JButton();
		    	join[i] = initButton("Joindre",150,50);
		    	panel.add(join[i]);
		    	panel.add(Box.createVerticalStrut(10));
		    }
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
		
		// puis on se connecte
		boolean result=false;
		if(joinServer)
		{
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
		//boolean connected = connect();
		
		if(joinServer)
		{	// on désactive les boutons le temps de l'attente
			backButton.setEnabled(false);
			nextButton.setEnabled(false);
			// puis on se contente d'attendre la réponse : acceptation ou rejet
				// la méthode correspondante du handler sera automatiquement invoquée
		}
		else
		{	
			sound.errorSound();
			JOptionPane.showMessageDialog(mainWindow, 
				"<html>Il n'est pas possible d'établir une connexion avec le serveur.</html>");
		}
		
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
		ipTextField.setEnabled(false);	// je désactive le text field	
	}
	/**
	 * 
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
	{
		for(int i=0; i<join.length; i++)
		{
			if(e.getSource()==join[i])	
			{	sound.clickSound(); // Son correspondant à un click
				joinServer=true;
				boolean a = connect(i);
				System.out.println(a);
			}
		}
		
	}

}
