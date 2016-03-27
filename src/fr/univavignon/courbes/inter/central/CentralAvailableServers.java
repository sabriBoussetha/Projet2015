package fr.univavignon.courbes.inter.central;

public class CentralAvailableServers {
	
	private String ipHost;
	private int availablePlaces;
	
	public CentralAvailableServers(){
		ipHost="";
		availablePlaces=0;
	}
	
	public String getIpHost(){
		return ipHost;
	}
	
	public int getAvailablePlaces(){
		return availablePlaces;
	}
	
	public void setIpHost(String ipHost){
		this.ipHost=ipHost;
	}
	
	public void setAvailablePlaces(int available){
		availablePlaces = available;
	}
}

