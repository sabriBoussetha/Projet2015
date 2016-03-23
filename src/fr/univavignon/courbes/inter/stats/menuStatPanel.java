package fr.univavignon.courbes.inter.stats;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class menuStatPanel extends JPanel implements ActionListener{
	
	boolean afficherGraphique = false;
	//true : affiche le graphique avec les joueurs selectionne
	//false : affiche le tableau avec la liste de joueurs
	
	//les elements qui composent notre fenetre
	JButton btnPrec = new JButton("PRECEDENT");
	JButton btnSuiv = new JButton("SUIVANT");
	JLabel lab = new JLabel("label");
	JPanel boutonPanel = new JPanel();
	
	public menuStatPanel()
	{
		
		//pn construit notre panel de bouton
		JPanel boutonPanel = new JPanel();
		boutonPanel.add(btnPrec);
		boutonPanel.add(btnSuiv);
		
		//on ajoute les actionlistener
		btnPrec.addActionListener(this);
		btnSuiv.addActionListener(this);
		
		
		//on ajoute les elements
		this.setLayout(new BorderLayout());
		this.add(new listeJoueurPanel(), BorderLayout.CENTER);
		this.add(boutonPanel, BorderLayout.SOUTH);
		
		
		
		

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if( e.getSource() == btnPrec )
		{
			//on presse le bouton precedent sur la page du graphique
			if (afficherGraphique)
			{
				//this.removeAll();
				this.add(new listeJoueurPanel(), BorderLayout.CENTER);
				this.validate();
				
				afficherGraphique = false;
				
				System.out.println("PAGE GRAPHE : PRECEDENT");
			}
			//on presse le bouton precedent sur la page du tableau
			else
			{
				//on retourne au menu
				//comment on fait ?
				System.out.println("PAGE TABLEAU : PRECEDENT");
			}
		}
		else if (e.getSource() == btnSuiv)
		{
			//on presse le bouton suivant sur la page du graphique
			if (afficherGraphique)
			{
				//il ne se passe rien
				System.out.println("PAGE GRAPHE : SUIVANT");
			}
			//on presse le suivant  sur la page du tableau
			else
			{
				//on affiche le graphique
				//this.removeAll();
				this.add(new graphEloPanel(), BorderLayout.CENTER);
				this.validate();
				
				afficherGraphique = true;
				
				System.out.println("PAGE TABLEAU : SUIVANT");
			}
		}
	}
}
