package fr.univavignon.courbes.stats;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Classe contenant toutes les fonctions lie au traitement des donnes statistiques
 * 
 * @author charlie
 *
 */

public class Stats {
	
	//ATTRIBUTS
	
	public static double D = 400;
	public static int K = 30;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//DONNEES BRUTS
		
		//associe un id de joueur a son classement
		/*
		Map<Integer, Integer> issue_partie = new HashMap();
		
		issue_partie.put(100, 1);
		issue_partie.put(101, 2);
		issue_partie.put(102, 3);
		issue_partie.put(103, 4);
		*/
		
		int[] issue_partie = {101,102,103,104};
		
		//assosice 
		Map<Integer, Integer> elo = new HashMap();
		elo.put(100, 2000);
		elo.put(101, 2100);
		elo.put(102, 1900);
		elo.put(103, 2200);
		
		//FIN DONNEES BRUT
		
		System.out.println(proba_succes_multi(elo, 101));
		
		//System.out.println(proba_succe_1vs1(1800, 2005));

	}
	
	//mettre a jour toute les stats sur la BDD, a l'issue d'une partie
	//mettre a jour le classement ELO
	//mettre a jour nb_partie faite, gaggne, ....
	
	
	//mettre a jour le classement ELO de tout les joueurs d'une partie a l'issue de celle ci
	public static void majElo(int[] issue_partie)
	{
		//recuperer les ELO de tout les joueur sur la BDD
		//sous la forme d'un tableau associatif [id -> elo]
		
		Map<Integer, Integer> elo = new HashMap();
		elo.put(100, 2000);
		elo.put(101, 2100);
		elo.put(102, 1900);
		elo.put(103, 2200);
		
		double proba_succes;
		double coef_reussite;
		int ancien_elo;
		//on enumere tout les joueurs
		for (int i = 0; i < issue_partie.length; i++)
		{
			//pour chaque joueur
			proba_succes = proba_succes_multi(elo, issue_partie[i]);
			coef_reussite = coef_reussite(issue_partie.length, i+1);
			ancien_elo = elo.get(issue_partie[i]);

			//on calcul son nouveau elo ainsi
			elo.put(issue_partie[i], newElo(ancien_elo, coef_reussite, proba_succes));
		}
		
		//et on reecris dans la base de donné les nouveau classements
	}
	
	//calculer la probabilite moyenne de succes pour un joueur
	//on a juste besoin des elo des joueurs
	public static double proba_succes_multi(Map<Integer, Integer> elo, int id_joueur)
	{
		double proba_succes = 0;
	
		//on enumere tout les elo des autres joueurs
		Iterator<Integer> keySetIterator = elo.keySet().iterator();
		while(keySetIterator.hasNext())
		{
		  Integer key = keySetIterator.next();
		  if (!(key == id_joueur))
		  {
			  //System.out.println(elo.get(id_joueur) + " " + elo.get(key) + " : " + proba_succes_1vs1(elo.get(id_joueur), elo.get(key)));
			 //on somme les probabilité de succes de toutes les sous parties
			  proba_succes += proba_succes_1vs1(elo.get(id_joueur), elo.get(key));
		  }
		 
		}
		
		//on retourne la moyenne
		return proba_succes / (elo.size()-1);
	}
	
	public static int newElo(int lastElo, double coef_reussite, double proba_succes)
	{
		return (int) (lastElo + K * (coef_reussite - proba_succes));
	}
	
	//renvoie la probabilité de succès du joueur ayant un classement elo1
	//quand il joue contre un joueur ayant un classement elo2
	public static double proba_succes_1vs1(int elo1, int elo2)
	{
		return 1.0 / (1.0 + Math.pow(10, (elo2 - elo1)/D));
	}
	
	//calcule du coefficient de reussite
	public static double coef_reussite(int nb_joueur, int classement)
	{
		return (nb_joueur - classement) / ((nb_joueur*(nb_joueur-1))/2);
	}
	
	
}
