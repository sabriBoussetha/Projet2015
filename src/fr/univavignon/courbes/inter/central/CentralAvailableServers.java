package fr.univavignon.courbes.inter.central;
/**
 * Classe utilisée pour facilité le parse du fichier JSON trouvant dans le Central
 * 
 * @author sabri
 *
 */
public class CentralAvailableServers {
	/** IP du serveur */
	private String ipHost;
	/** Nombre de places disponibles*/
	private int availablePlace;
	
	/**
	 * Constructeur sans argument 
	 */
	public CentralAvailableServers(){
		ipHost="";
		availablePlace=0;
	}
	/****************
	 *  Getters		*
	 ****************/
	public String getIpHost(){
		return ipHost;
	}
	
	public int getAvailablePlaces(){
		return availablePlace;
	}
	/****************
	 *  Setters		*
	 ****************/
	public void setIpHost(String ipHost){
		this.ipHost=ipHost;
	}
	
	public void setAvailablePlaces(int available){
		availablePlace = available;
	}
}

