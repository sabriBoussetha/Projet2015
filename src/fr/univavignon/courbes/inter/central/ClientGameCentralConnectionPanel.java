package fr.univavignon.courbes.inter.central;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import fr.univavignon.courbes.inter.ClientConnectionHandler;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager.NetEngineImpl;
import fr.univavignon.courbes.inter.simpleimpl.remote.AbstractConnectionPanel;
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
	
	private PhpCommunication search = new PhpCommunication();
	
	/**
	 * @param mainWindow
	 */
	public ClientGameCentralConnectionPanel(MainWindow mainWindow)
	{	
		super(mainWindow, TITLE);
		boolean connected = connect();
		
		if(connected)
		{	// on désactive les boutons le temps de l'attente
			//backButton.setEnabled(false);
			nextButton.setEnabled(false);
		
			// puis on se contente d'attendre la réponse : acceptation ou rejet
			// la méthode correspondante du handler sera automatiquement invoquée
		}
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
		
		String ipStr = null;
		try {
			System.out.println("Connexion...");
			ipStr = search.searchGame();
			// On enlève une place disponible dans la table
			search.modifPlayer(-1,ipStr);
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
			{	JOptionPane.showMessageDialog(mainWindow, 
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
		JOptionPane.showMessageDialog(mainWindow, 
				"<html>Il n'est pas possible d'établir une connexion avec le serveur.</html>");
		//}
		
	}

	@Override
	protected void previousStep() {
		mainWindow.clientCom = null;
		mainWindow.displayPanel(PanelName.CLIENT_GAME_PLAYER_SELECTION);
		
	}
	
	@Override
	protected void initContent(){	
		super.initContent();
		ipTextField.setEnabled(false);	// je désactive le text field
	}

}
