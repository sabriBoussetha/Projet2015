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
import fr.univavignon.courbes.network.central.simpleimpl.PhpCommunication;

/**
 * Panel qui represente le contenu central du menu statistiques (listeJoueurPanel ou graphEloPanel)
 * et les boutons suivant precedents qui permettent de passer de du graphique au tableau
 * 
 * @author Charlie
 *
 */
public class MenuStatPanel extends AbstractConfigurationPanel{
	
	/** FALSE : on est sur la page du tableau
	 * TRUE : on est sur le graphique ELO
	 */
	boolean afficherGraphique = false;
	
	/**le tableau des joueurs**/ 
	listeJoueurPanel listeJoueursPanel;

	public MenuStatPanel(MainWindow mainWindow)
	{
		super(mainWindow, "Statistiques de joueurs");
	}
	
	@Override
	protected void initContent() {
		
		listeJoueursPanel = new listeJoueurPanel();
		
		//par defaut, on charge la liste des joueurs
		this.add(listeJoueursPanel);
	}

	@Override
	protected void nextStep() throws IOException {
	
		//on presse le bouton suivant sur la page du graphique
		if (afficherGraphique) {/*il ne se passe rien*/}
		
		//on presse le suivant  sur la page du tableau
		else
		{
			afficherGraphique = true;
			
			//on supprime tout
			this.removeAll();
			//on reajoute tout les elements composant la page du graphique
			initTitle("ELO");
			
			//on recupere les ids selectionné
			LinkedList listId = listeJoueursPanel.modele.getIdChecked();
			
			//on construit le graphEloPanel avec les id selectionné
			this.add(new graphEloPanel(listId));
			add(Box.createVerticalGlue());
			initButtons();
			nextButton.setEnabled(false);
			
			this.validate();
			this.repaint();
		}
		
	}

	@Override
	protected void previousStep() {
		
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
