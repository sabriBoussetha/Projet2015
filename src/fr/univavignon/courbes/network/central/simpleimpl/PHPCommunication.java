package fr.univavignon.courbes.network.central.simpleimpl;



import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import fr.univavignon.courbes.network.central.CentralCommunication;

public class PHPCommunication implements CentralCommunication{

	@Override
	public void sendIP()throws IOException {
		URL url = new URL("https://pedago02a.univ-avignon.fr/~uapv1402577/server/server.php");
	    String result = "";
	    String data = "infos=" + URLEncoder.encode("10.122.2.46"+"|4", "UTF-8");
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    try {
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setUseCaches(false);
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type",
	                "application/x-www-form-urlencoded");

	        // Send the POST data
	        DataOutputStream dataOut = new DataOutputStream(
	                connection.getOutputStream());
	        dataOut.writeBytes(data);
	        dataOut.flush();
	        dataOut.close();

	        //BufferedReader in = null;
            String line;
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = in.readLine()) != null) {
            	result += line;
            }
        
	    	in.close();
	    }
    	finally {
    		connection.disconnect();
            System.out.println(result);
        }
	}

	@Override
	public void sendStats() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getIP() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getStats() {
		// TODO Auto-generated method stub
		
	}
	
}
