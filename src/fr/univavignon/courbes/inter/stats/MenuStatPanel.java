package fr.univavignon.courbes.inter.stats;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.JPanel;

import com.orsoncharts.util.json.JSONArray;
import com.orsoncharts.util.json.JSONObject;
import com.orsoncharts.util.json.parser.JSONParser;
import com.orsoncharts.util.json.parser.ParseException;

import fr.univavignon.courbes.inter.simpleimpl.AbstractConfigurationPanel;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;

/**
 * Panel qui represente le contenu central du menu statistiques (listeJoueurPanel ou graphEloPanel)
 * et les boutons suivant precedents qui permettent de passer de du graphique au tableau
 * 
 * @author charlie
 *
 */
public class MenuStatPanel extends AbstractConfigurationPanel{
	
	boolean afficherGraphique = false;
	//FALSE : on est sur la page du tableau
	//TRUE : on est sur le graphique ELO
	
	listeJoueurPanel listeJoueursPanel;

	public MenuStatPanel(MainWindow mainWindow) {
		super(mainWindow, "Statistiques de joueurs");
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	protected void initContent() {
		// TODO Auto-generated method stub
		
		listeJoueursPanel = new listeJoueurPanel();
		//par defaut, on charge la liste des joueurs
		this.add(listeJoueursPanel);
	}

	@Override
	protected void nextStep() throws IOException {
		// TODO Auto-generated method stub
		
		//on presse le bouton suivant sur la page du graphique
		if (afficherGraphique)
		{
			//il ne se passe rien
		}
		
		//on presse le suivant  sur la page du tableau
		else
		{
			afficherGraphique = true;
			
			//on supprime tout
			this.removeAll();
			
			//on reajoute tout les elements composant la page du graphique
			initTitle("ELO");
			this.add(new graphEloPanel());
			add(Box.createVerticalGlue());
			initButtons();
			nextButton.setEnabled(false);
			
			this.validate();
			this.repaint();
			
			//on affiche en console la lite des id selectioinne
			LinkedList listId = listeJoueursPanel.modele.getIdChecked();
			for (int i = 0; i < listId.size(); i++)
			{
				System.out.println(listId.get(i));
			}
			
		}
		
	}

	@Override
	protected void previousStep() {
		
		// TODO Auto-generated method stub
		
		//on presse le bouton precedent sur la page du graphique
		if (afficherGraphique)
		{
			afficherGraphique = false;
			
			//on supprime tout
			this.removeAll();
			
			//on reajoute tout les elements composant la page du graphique
			initTitle("Statistiques des joueurs");
			this.add(listeJoueursPanel);
			add(Box.createVerticalGlue());
			initButtons();
			
			this.validate();
			this.repaint();
		}
		
		//on presse le bouton precedent sur la page du tableau
		else
		{
			//on retourne au menu
			mainWindow.displayPanel(PanelName.MAIN_MENU);
		}
		
	}

}
