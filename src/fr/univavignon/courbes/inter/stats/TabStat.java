package fr.univavignon.courbes.inter.stats;

import java.awt.Component;
import java.awt.List;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Classe creant une table pour affiche les donnees statistiques des joueurs
 *
 * @author charlie
 */
class TabStat extends AbstractTableModel{

  private Object[][] data; //les donnes du tableau
  private String[] title; //le titre des colonnes
  private int[] tabId; //les ids des joueurs, dans l'ordre d'affichage
  
  int nbChecked; //nb de lignes selectionnées
  
  //Constructeur
  public TabStat(){
	  
    String  title[] = 
    	{"classement ELO", "pseudo", "nombre de parties joués", "ratio de victoires","SELECT"};
    
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
    
    this.data = data;
    this.title = title;
    
  }
  
  public String getColumnName(int col) {

    return this.title[col];

  }

  	//j'implemente la fonction getColumnClass.
    //Swing en a besoin pour afficher les booleens sous forme de case a cocher
	public Class getColumnClass(int col){
		//on retourne simplement le type du 1er element de la colonne
		return this.data[0][col].getClass();
	}

	//j'implement cette fonction pour spécifier les colonnes éditables
	public boolean isCellEditable(int row, int col){
		//si le type de la colonne est un boolean, on renvoie true
		if(getValueAt(0, col) instanceof Boolean)
			return true;
		
		//toutes les autres colonnes ne sont pas éditables
		return false; 

	}
	
	//j'implement cette fonction pour que le booleen soit modifié lorsque je clique
	//sur la checkbox
	public void setValueAt(Object value, int row, int column) {
	    data[row][column] = value;
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
    
	//renvoie un booleen pour specifier si la ligne est selectionné ou non
	public boolean checked(int row)
	{
		return (boolean) data[row][4];
	}
	
	//retourne un tableu contenant les id qui ont été séléctionnés
	public LinkedList getIdChecked()
	{
		LinkedList listId = new LinkedList();
		for (int i = 0; i < getRowCount(); i++)
		{
			if (checked(i))
			{
				listId.add(tabId[i]);
			}
		}
		return listId;
	}

  }
