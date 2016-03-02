package fr.univavignon.courbes.network.central;

import java.io.IOException;

public interface CentralCommunication {
	void sendIP() throws IOException;
	
	void sendStats();
	
	void getIP();
	
	void getStats();
	
}
