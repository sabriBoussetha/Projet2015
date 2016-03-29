package fr.univavignon.courbes.inter.stats;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * panel qui contient un tableau qui affiche les 
 * statistiques de tout les joueurs de la base de donnée
 * 
 * @author charlie
 *
 */
public class listeJoueurPanel extends JPanel{
	
	/** le tableau qui represente la liste des joueurs */
	public JTable tableau; //le tableau 
	/** le modele du tableau */
	public TabStat modele;
	
	public listeJoueurPanel()
	{
		this.setLayout(new BorderLayout());

		//Les données du tableau
		
		//le tableau est un JTable classique mais qui utilise un modele perso
		modele = new TabStat();
	    tableau = new JTable(modele);
	    
	    //on parametre le tableau pour que les Integer et 
	    //les String soient centré sur leurs colonnes
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    tableau.setDefaultRenderer(String.class, centerRenderer);
	    tableau.setDefaultRenderer(Integer.class, centerRenderer);
	   
		//on ajoute le table au panel
		this.add(new JScrollPane( tableau),BorderLayout.CENTER);

	}
	

}
