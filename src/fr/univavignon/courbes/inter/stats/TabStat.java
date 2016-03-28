package fr.univavignon.courbes.inter.stats;

import java.awt.Component;
import java.awt.List;
import java.io.IOException;
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

import fr.univavignon.courbes.network.central.simpleimpl.PhpCommunication;

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

		System.out.println("on construit la tab");
		String  title[] = 
		{"ELO", "Pseudo", "Nb parties", "% Victoire", "Nb manches", "% Victoire", "Points", "mort Bord", "autre","soi même"," "};

	
		//TRAITEMENT JSON
		
		//on recuupere le JSON dans la chaine test
		String test = null;
		try {
			test = PhpCommunication.getPlayer();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println(test);

		JSONParser parser = new JSONParser();
		  
		//on recupere la table principale depuis la chaine
		JSONArray tabPrincipale = null;
		try {
			tabPrincipale = (JSONArray) parser.parse(test);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		JSONArray tabLigne;
		JSONObject ligne;
		
		//on remplis le tableau qui sera affiché a partir des données JSON
        data = new Object[tabPrincipale.size()][title.length];
        tabId = new int[tabPrincipale.size()];

        
        //on enumere les elements du tableau
        for (int i = 0; i < tabPrincipale.size(); i++)
        {
        	tabLigne = (JSONArray)tabPrincipale.get(i);
    		ligne = (JSONObject) tabLigne.get(0);
        	
        	data[i][0] = (int) (long) ligne.get("score_elo");
        	data[i][1] = ligne.get("pseudo");
        	data[i][2] = (int) (long) ligne.get("nb_partie");
        	data[i][3] = pourcentage((long) ligne.get("nb_partie_premier"), (long) ligne.get("nb_partie"));
        	data[i][4] = (int) (long) ligne.get("nb_manche");
        	data[i][5] = pourcentage((long) ligne.get("nb_manche_premier"), (long) ligne.get("nb_manche"));
        	data[i][6] = (int) (long) ligne.get("nb_points");
        	data[i][7] = (int) (long) ligne.get("mort_bord");
        	data[i][8] = (int) (long) ligne.get("mort_autre");
        	data[i][9] = (int) (long) ligne.get("mort_soi_meme");
        	data[i][title.length-1] = new Boolean(false);
        	
        	
        	tabId[i] =  (int) (long) ligne.get("id");

        }
		
		//FINT TRAITEMENT JSON
        
		this.data = data;
		this.title = title;
	}
	
	public String pourcentage(long a, long b)
	{
		if (b == 0) return "0%";
		return "" + 100*a/b + "%";
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
			//on recupere la valeur a la ligne i et a la derniere colonne
			if ((boolean) data[i][this.getColumnCount()-1])
			{
				listId.add(tabId[i]);
			}
		}
		return listId;
	}

  }
