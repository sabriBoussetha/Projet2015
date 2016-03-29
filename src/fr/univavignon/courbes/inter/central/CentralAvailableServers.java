package fr.univavignon.courbes.inter.central;

public class CentralAvailableServers {
	
	private String ipHost;
	private int availablePlace;
	
	public CentralAvailableServers(){
		ipHost="";
		availablePlace=0;
	}
	
	public String getIpHost(){
		return ipHost;
	}
	
	public int getAvailablePlaces(){
		return availablePlace;
	}
	
	public void setIpHost(String ipHost){
		this.ipHost=ipHost;
	}
	
	public void setAvailablePlaces(int available){
		availablePlace = available;
	}
}

