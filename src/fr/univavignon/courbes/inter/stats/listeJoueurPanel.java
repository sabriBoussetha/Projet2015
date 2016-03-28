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
 * panel qui contient principalement la liste des données statistiques des joueurs
 * 
 * @author charlie
 *
 */
public class listeJoueurPanel extends JPanel{
	
	public JTable tableau; //le tableau 
	public TabStat modele; //le modele du tableau, qui contient les donnees
	
	public listeJoueurPanel()
	{
		this.setLayout(new BorderLayout());
		
		//this.add(new JLabel("TABLEAU DE JOUEUR. en construction..."), BorderLayout.NORTH);
		
		//Les données du tableau
		
		//le tableau est un JTable classique mais qui utilise un modele perso
		modele = new TabStat();
	    tableau = new JTable(modele);
	    
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    
	    tableau.setDefaultRenderer(String.class, centerRenderer);
	    tableau.setDefaultRenderer(Integer.class, centerRenderer);
	   // tableau.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		//on ajoute le table au panel
		this.add(new JScrollPane( tableau),BorderLayout.CENTER);

	}
	

}
