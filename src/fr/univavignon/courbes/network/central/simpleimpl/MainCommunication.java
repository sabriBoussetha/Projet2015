package fr.univavignon.courbes.network.central.simpleimpl;

import java.io.IOException;

public class MainCommunication {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		PhpCommunication server = new PhpCommunication();
		server.sendGameInformation();
		System.out.println("Ok");
	}

}
