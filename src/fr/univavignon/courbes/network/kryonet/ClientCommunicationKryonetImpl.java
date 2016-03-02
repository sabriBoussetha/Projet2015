package fr.univavignon.courbes.network.kryonet;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.ItemType;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.common.SmallUpdate;
import fr.univavignon.courbes.common.UpdateInterface;
import fr.univavignon.courbes.inter.ClientConnectionHandler;
import fr.univavignon.courbes.inter.ClientGameHandler;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.ErrorHandler;

/*
 * Courbes
 * Copyright 2015-16 L3 Info UAPV 2015-16
 * 
 * This file is part of Courbes.
 * 
 * Courbes is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 2 of the License, or (at your option) any later version.
 * 
 * Courbes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Courbes. If not, see <http://www.gnu.org/licenses/>.
 */

import fr.univavignon.courbes.network.ClientCommunication;
import fr.univavignon.courbes.network.simpleimpl.NetworkConstants;

/**
 * Implémentation de la classe {@link ClientCommunication}. Elle se repose
 * sur deux autres classes pour les entrées ({@link ClientReadRunnable}) et 
 * les sorties ({@link ClientWriteRunnable}).
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ClientCommunicationKryonetImpl extends Listener implements ClientCommunication
{	
	////////////////////////////////////////////////////////////////
	////	ADRESSE IP
	////////////////////////////////////////////////////////////////
	/** Variable qui contient l'adresse ip du serveur */
	private String ip;


	@Override
	public String getIp()
	{	return ip;
	}

	@Override
	public void setIp(String ip)
	{	this.ip = ip;
	}

	////////////////////////////////////////////////////////////////
	////	PORT
	////////////////////////////////////////////////////////////////
	/** Variable qui contient le port du serveur */
	private int port = Constants.DEFAULT_PORT;//2345;

	@Override
	public int getPort()
	{	return port;
	}

	@Override
	public void setPort(int port)
	{	this.port = port;
	}
	
	////////////////////////////////////////////////////////////////
	////	HANDLER DE CONNEXION
	////////////////////////////////////////////////////////////////
	/** Handler de connexion */
	public ClientConnectionHandler connectionHandler;

	@Override
	public void setConnectionHandler(ClientConnectionHandler connectionHandler)
	{	this.connectionHandler = connectionHandler;
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 */
	protected void gotAccepted()
	{	if(connectionHandler!=null)
			connectionHandler.gotAccepted();
		else
			System.err.println("Le handler de connexion n'a pas été renseigné !");
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 */
	protected void gotRefused()
	{	closeClient();
		if(connectionHandler!=null)
			connectionHandler.gotRefused();
		else
			System.err.println("Le handler de connexion n'a pas été renseigné !");
	}
	
	////////////////////////////////////////////////////////////////
	////	HANDLER DE PROFILS
	////////////////////////////////////////////////////////////////
	/** Handler de profils */
	private ClientProfileHandler profileHandler;

	@Override
	public void setProfileHandler(ClientProfileHandler configHandler)
	{	this.profileHandler = configHandler;
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 */
	protected void gotKicked()
	{	closeClient();
		if(profileHandler!=null)
			profileHandler.gotKicked();
		else
			System.err.println("Le handler de profils n'a pas été renseigné !");
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param profiles
	 * 		Tableau de profils à transmettre au handler.
	 */
	protected void updateProfiles(Profile[] profiles)
	{	
		/* If the handler is not defined */
		int iteration = 0;

		/* Wait to give enough time for method <init> from ClientGameWaitPanel to be called to set the handler */
		while(profileHandler == null && iteration < 100){

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			iteration++;
		}
		
				
		if(profileHandler!=null)
			profileHandler.updateProfiles(profiles);
	
		else
			System.err.println("Le handler de profils n'a pas été renseigné !1");
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param round
	 * 		Manche à transmettre au handler.
	 */
	protected void startGame(Round round)
	{	if(profileHandler!=null)
			profileHandler.startGame(round);
		else
			System.err.println("Le handler de profils n'a pas été renseigné !2");
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 */
	public void connectionLost()
	{	if(profileHandler!=null)
			profileHandler.connectionLost();
		else
			System.err.println("Le handler de profils n'a pas été renseigné !3");
	}
	
	////////////////////////////////////////////////////////////////
	////	HANDLER DE LA PARTIE
	////////////////////////////////////////////////////////////////
	/** Handler de la partie */
	public ClientGameHandler gameHandler;

	@Override
	public void setGameHandler(ClientGameHandler gameHandler)
	{	this.gameHandler = gameHandler;
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param round
	 * 		Manche à transmettre au handler.
	 */
	public void fetchRound(Round round)
	{	if(gameHandler!=null)
		gameHandler.fetchRound(round);
		else
			System.err.println("Le handler de partie n'a pas été renseigné !");
	}
	
	////////////////////////////////////////////////////////////////
	////	HANDLER D'ERREUR
	////////////////////////////////////////////////////////////////
	/** Handler d'erreurs */
	public ErrorHandler errorHandler;

	@Override
	public void setErrorHandler(ErrorHandler errorHandler)
	{	this.errorHandler = errorHandler;
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param message
	 * 		Message à transmettre au handler.
	 */
	public void displayError(String message)
	{	if(errorHandler!=null)
		errorHandler.displayError(message);
		else
			System.err.println("Le handler d'erreur n'a pas été renseigné !");
	}
	
	////////////////////////////////////////////////////////////////
	////	CONNEXION
	////////////////////////////////////////////////////////////////
	private Client client;
	private Profile profile;

	/** Indentifie la premier manche reçue */
	private boolean firstRound;
	
	@Override
	public synchronized boolean launchClient()
	{	boolean result = true;
	
		client = new Client();
		client.start();
			
		firstRound = true;
			
		Network.register(client);
			
		client.addListener(this);



		
	     int timeout = 5000;
	     try {
System.out.println("CCI: connection to port " + port + "...");	    	 
			client.connect(timeout, ip, port, port+1);
			System.out.println("CCI: connected");
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
		}

		return result;
	}
		

	@Override
	public synchronized void closeClient()
	{	client.stop();
	}

	@Override
	public synchronized boolean isConnected()
	{	return client != null && client.isConnected();
	}
	
	/**
	 * Méthode appelée quand la connexion avec le serveur est perdue accidentellement.
	 */
	protected synchronized void lostConnection()
	{	if(client!=null)
		{	connectionLost();
			closeClient();
		}
	}
	
	////////////////////////////////////////////////////////////////
	////	ENTREES
	////////////////////////////////////////////////////////////////
	@Override
	public Integer retrievePointThreshold()
	{	Integer result = pointsLimits.poll();
		return result;
	}

	@Override
	public UpdateInterface retrieveUpdate()
	{	UpdateInterface result = updateData.poll();
	
		if(result != null && result instanceof SmallUpdate){
			
			SmallUpdate su = (SmallUpdate)result;
			
			if(su.newItem != null)
				System.out.println("CCI: retrieveUpdate new item: " + su.newItem.type);
			
		}
		return result;
	}
	
	////////////////////////////////////////////////////////////////
	////	SORTIES
	////////////////////////////////////////////////////////////////
	@Override
	public void sendCommand(Direction direction)
	{	
		Integer intToSend = new Integer(0);
		switch(direction){
		case LEFT:intToSend = new Integer(-1);break;
		case RIGHT:intToSend = new Integer(1);break;
		}
		client.sendTCP(intToSend);
		
	}

	@Override
	public void sendProfile(Profile profile)
	{	client.sendTCP(profile);
		this.profile = profile;	
System.out.println("CCI: send profile");
	}
	
	@Override
	public void sendAcknowledgment()
	{	client.sendTCP(NetworkConstants.ANNOUNCE_ACKNOWLEDGMENT);
		System.out.println("CCI: send acknowledgement");
	}
	
	////////////////////////////////////////////////////////////////
	////	FILES DE DONNEES
	////////////////////////////////////////////////////////////////
	/** File des aires de jeu reçues du serveur et en attente de récupération par l'Interface Utilisateur */
	protected Queue<UpdateInterface> updateData = new ConcurrentLinkedQueue<UpdateInterface>();
	
	/** File des limites de points reçues du serveur et en attente de récupération par l'Interface Utilisateur */
	protected Queue<Integer> pointsLimits = new ConcurrentLinkedQueue<Integer>();

	
	public void connected(Connection connection){
		System.out.println("CCI: connected, does not send profile to server");
		
		client.sendTCP(new Boolean(true));
//		client.sendTCP(profile);
	}

    public void received(Connection connection, Object object){

    	if(object instanceof String)
		{
			String string = (String)object;
			System.out.println("CCI: received String: "+ string);
			
			if(string.equals(NetworkConstants.ANNOUNCE_REJECTED_CONNECTION))
				gotRefused();
			
			else if(string.equals(NetworkConstants.ANNOUNCE_ACCEPTED_CONNECTION))
				gotAccepted();
			
			else if(string.equals(NetworkConstants.ANNOUNCE_REJECTED_PROFILE))
				gotKicked();
		}
    	
    	else if(object instanceof UpdateInterface)
		{	
			UpdateInterface ud = (UpdateInterface)object;
//			System.out.print("!");
			//System.out.println(boards.size());
			if(ud instanceof SmallUpdate){
				SmallUpdate su = (SmallUpdate)ud;
				
				if(su.newItem != null){
					System.out.println("\nCCI: received SmallUpdate with new item: " + su.newItem.type);
//					su.newItem.type = new ItemType(su.newItem.type);
				}
			}
			
			updateData.offer(ud);
		}	
		else if(object instanceof Integer)
		{	
    		System.out.println("CCI: received Integer");
    		Integer integer = (Integer)object;
			pointsLimits.offer(integer);
		}
		
		else if(object instanceof Round)
		{	
    		System.out.println("CCI: received round");
    		Round round = (Round)object;
			if(firstRound)
			{	startGame(round);
				firstRound = false;
			}
			else
				fetchRound(round);
		}
		else if(object instanceof Profile[])
		{	
    		System.out.println("CCI: received profile[]");
    		Profile[] profiles = (Profile[])object;
			updateProfiles(profiles);
		}
		else if(!(object instanceof FrameworkMessage.KeepAlive))
			System.out.println("SCI: unknown class: "+ object.getClass());
		else
			System.out.print(".");
		
    }

    public void disconnected(Connection connection){

    	   EventQueue.invokeLater(new Runnable(){
	   	public void run(){
		       lostConnection();
		}
	   });

    }
}
