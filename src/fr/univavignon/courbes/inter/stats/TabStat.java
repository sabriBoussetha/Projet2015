package fr.univavignon.courbes.inter.stats;

import java.awt.Component;
import java.awt.List;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.orsoncharts.util.json.JSONArray;
import com.orsoncharts.util.json.JSONObject;
import com.orsoncharts.util.json.parser.JSONParser;
import com.orsoncharts.util.json.parser.ParseException;

/**
 * Classe creant un modele de table qui contient les donnees statistiques des joueurs
 *
 * @author charlie
 */
class TabStat extends AbstractTableModel{

  private Object[][] data; //les donnes du tableau
  private String[] title; //le titre des colonnes
  private int[] tabId; //les ids des joueurs, dans l'ordre d'affichage
  
	//Constructeur
	public TabStat(){

		String  title[] = 
		{"classement ELO", "pseudo", "nombre de parties joués", "ratio de victoires","SELECT"};
		
		//TRAITEMENT JSON
		
		 String s = "["+
					"{\"id\" : 101, \"score_elo\" : 2100, \"pseudo\" : \"charlie\" 	,\"nb_partie\" : 10, \"nb_partie_premier\" : 5, \"date_\" : 2016},"+
					"{\"id\" : 102, \"score_elo\" : 1900, \"pseudo\" : \"alex\" 		,\"nb_partie\" : 10, \"nb_partie_premier\" : 4, \"date_\" : 2016},"+
					"{\"id\" : 103, \"score_elo\" : 1800, \"pseudo\" : \"sabri\" 	,\"nb_partie\" : 10, \"nb_partie_premier\" : 3, \"date_\" : 2016},"+
					"{\"id\" : 104, \"score_elo\" : 1700, \"pseudo\" : \"nathan\" 	,\"nb_partie\" : 10, \"nb_partie_premier\" : 2, \"date_\" : 2016},]";
		 
		JSONParser parser = new JSONParser();
		   
		JSONArray tab = null;
		try {
			tab = (JSONArray) parser.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JSONObject ligne;
        
        //on remplis le tableau qui sera affiché a partir des données JSON
        Object[][] data = new Object[tab.size()][5];
        int [] tabId = new int[tab.size()];
        
        //on enumere les elements du tableau
        for (int i = 0; i < tab.size(); i++)
        {
        	ligne = (JSONObject)tab.get(i);
        	System.out.println("id : " + ligne.get("id"));
        	System.out.println("score_elo : " + ligne.get("score_elo"));
        	System.out.println("pseudo : " + ligne.get("pseudo"));
        	System.out.println("nb_partie : " + ligne.get("nb_partie"));
        	System.out.println("nb_partie_premier : " + ligne.get("nb_partie_premier"));
        	System.out.println("date_ : " + ligne.get("date_"));
        	
        	data[i][0] = (int) ligne.get("score_elo");
        	data[i][1] = (String) ligne.get("pseudo");
        	data[i][2] = (int) ligne.get("nb_partie");
        	data[i][3] = (double) ligne.get("nb_partie_premier") / (double) ligne.get("nb_partie");
        	data[i][4] = new Boolean(false);
        	
        	tabId[i] =  (int) ligne.get("id");

        } 
		//FIN TRAITEMENT JSON
		
		/*
		//on construit le tableau depuis la BDD
		//pour l'instant données bruts
		Object[][] data = {
		      {2500, "Charlie", 10, 0.8, new Boolean(false)},
		  {2300, "Nathan", 10, 0.7,new Boolean(false)},
		  {2200, "Sabri", 10, 0.6,new Boolean(false)},
		  {1800, "Alex", 10, 0.3,new Boolean(false)},
		  {1700, "Alex2", 10, 0.2,new Boolean(false)},
		  {1600, "Alex3", 10, 0.1,new Boolean(false)},
		    };
		
		tabId = new int[6];
		tabId[0] = 101;
		tabId[1] = 102;
		tabId[2] = 103;
		tabId[3] = 104;
		tabId[4] = 105;
		tabId[5] = 106;
		//fin donnees bruts
		*/
        
		this.data = data;
		this.title = title;
	}
  
  
	//renvoie le nom de la colonne
	public String getColumnName(int col)
	{
		return this.title[col];
	}
	
	//Retourne le nombre de colonnes
	public int getColumnCount() {
	  return this.title.length;
	}
	
	//Retourne le nombre de lignes
	public int getRowCount() {
	  return this.data.length;
	}
	
	//Retourne la valeur à l'emplacement spécifié
	public Object getValueAt(int row, int col) {
	  return this.data[row][col];
	}
	
	
	//modifie la valeur a l'emplacement donné
	public void setValueAt(Object value, int row, int column) {
	    data[row][column] = value;
	}
	
	//j'implemente la fonction getColumnClass.
	//Swing en a besoin pour afficher les booleens sous forme de case a cocher
	public Class getColumnClass(int col)
	{
		//on retourne simplement le type du 1er element de la colonne
		return this.data[0][col].getClass();
	}
	
	//j'implemente cette fonction pour spécifier les colonnes éditables
	//ici que la colonne des booleens
	public boolean isCellEditable(int row, int col)
	{
		//si le type de la colonne est un boolean, on renvoie true
		if(getValueAt(0, col) instanceof Boolean)
			return true;
		//toutes les autres colonnes ne sont pas éditables
		return false; 
	}

	//retourne un tableu contenant les id qui ont été séléctionnés
	public LinkedList getIdChecked()
	{
		LinkedList listId = new LinkedList();
		for (int i = 0; i < getRowCount(); i++)
		{
			//on recupere la valeur a la ligne i et a la colonne 4
			if ((boolean) data[i][4])
			{
				listId.add(tabId[i]);
			}
		}
		return listId;
	}

  }
