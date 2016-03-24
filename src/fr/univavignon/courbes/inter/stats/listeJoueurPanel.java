package fr.univavignon.courbes.inter.stats;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class listeJoueurPanel extends JPanel{
	
	public listeJoueurPanel()
	{
		this.setLayout(new BorderLayout());
		
		this.add(new JLabel("TABLEAU DE JOUEUR. en construction..."), BorderLayout.NORTH);
		
		//Les données du tableau

	    Object[][] data = {

	      {2500, "Charlie", 10, 0.8},
	      {2300, "Nathan", 10, 0.7},
	      {2200, "Sabri", 10, 0.6},
	      {1800, "Alex", 10, 0.3},
	      {1700, "Alex2", 10, 0.2},
	      {1600, "Alex3", 10, 0.1},

	    };


	    //Les titres des colonnes

	    String  title[] = {"classement ELO", "pseudo", "nombre de parties joués", "ratio de victoires" };

	    JTable tableau = new JTable(data, title);
		
		//on ajoute le table au panel
		this.add(new JScrollPane(tableau),BorderLayout.CENTER);

	}
}
