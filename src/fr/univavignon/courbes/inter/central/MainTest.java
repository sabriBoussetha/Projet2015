package fr.univavignon.courbes.inter.central;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.*;

public class MainTest {

	public static void main(String[] args) throws IOException {
	
		/*String str = "{ \"0\":{\"ip_host\":\"10.30.1.246\",\"available_places\":4},\"1\":{\"ip_host\":\"10.122.1.245\",\"available_places\":5}}";
		JSONObject obj = new JSONObject(str);
		//String n = obj.getString("ip_host");
		JSONObject ar = obj.getJSONObject("0");
		int a = ar.getInt("available_places");
		//System.out.println(a);*/
		
		//String str1 = "[{\"ip_host\": \"10.30.1.246\",\"available_places\": 4},{\"ip_host\":\"10.122.1.245\",\"available_places\":5}]";
		
		String str1 = "https://pedago02a.univ-avignon.fr/~uapv1501163/server/listServers.json";
	    URL url = new URL(str1);
	 
	    // read from the URL
	    Scanner scan = new Scanner(url.openStream());
	    String str2 = new String();
	    while (scan.hasNext())
	        str2 += scan.nextLine();
	    scan.close();
		
		JSONArray b = new JSONArray(str2);
		JSONObject test;
		int aa;
		String ip;
		for(int i=0; i<b.length(); i++)
		{
			test = b.getJSONObject(i);
			ip = test.getString("ip_host");
			aa = test.getInt("available_places");
			System.out.println(ip + "|" + aa);
		}
	}

}
