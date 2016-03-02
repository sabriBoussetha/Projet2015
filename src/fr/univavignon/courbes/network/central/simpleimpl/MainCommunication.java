package fr.univavignon.courbes.network.central.simpleimpl;

import java.io.IOException;

public class MainCommunication {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		PHPCommunication server = new PHPCommunication();
		server.sendIP();
		System.out.println("Ok");
	}

}
