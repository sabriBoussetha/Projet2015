package fr.univavignon.courbes.inter.stats;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import com.orsoncharts.util.json.JSONArray;
import com.orsoncharts.util.json.JSONObject;
import com.orsoncharts.util.json.parser.JSONParser;
import com.orsoncharts.util.json.parser.ParseException;

import fr.univavignon.courbes.network.central.simpleimpl.PhpCommunication;

/* POUR IMPORTER JFREECHART :
 * 
 * pour importer la bibliotheque jFreeChart :
 * telecharger ici : https://sourceforge.net/projects/jfreechart/files/1.%20JFreeChart/
 * 
 * pour l'importer dans java :
 * project -> properties -> java build path
 * 	 si les .jar ne sont pas dans lib/jfreechart :
 *   -> add external jar et ajouter les .jar contenues dans le dossier lib téléchargé
 *   
 *   si ils y sont : -> add Jar et les ajouter
 */

/**
 * Panel contenant le graphique qui représente l'évolution du classement ELO
 * de 1 ou plusieurs joueurs
 * 
 * @param linkedlist d'id. Ceux des joueurs à afficher
 * 
 * @author Charlie
 *
 */
public class graphEloPanel extends JPanel{
	
	
	public graphEloPanel(LinkedList listId) 
    {
        super();
        //on construit notre chart depuis un dataset
        JFreeChart chart = createChart(createDataset(listId));
        //creation du panel
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        
        //on ajoute le graphique au panel
        this.setLayout(new BorderLayout());
        this.add(panel,  BorderLayout.CENTER); 
    }
	
	/**
	 * fonction qui construit le jeu de donnée, a partie de la liste d'id
	 * @param listId : les joueurs a afficher
	 * 
	 * @return un Dataset de jfreechart.
	 * 
	 */
	private static XYDataset createDataset(LinkedList listId) {
		
		//creation du dataset, contenant plusieur timesseries
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        
        //on enumere les joueurs
        
		//on construit un timeSeries par joueurs
		TimeSeries ts = null;
		String json = null;
		for (int i = 0; i < listId.size(); i++)
		{

			//on recupere les donnees du joueur depuis la bases de donnee
			//sous forme de json
			try {
				json = PhpCommunication.getElo((Integer) listId.get(i));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			//traitement du json
			
			JSONParser parser = new JSONParser();
			
			//tableau contenant un element par couple (date - ELO)
			JSONArray tab = null;
			try {
				tab = (JSONArray) parser.parse(json);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//ligne contenant un tableau associatif date => ELO
			JSONObject ligne;
			
			//on cree un TimeSeries avec le nom du joueur en cours
			try {
				ts = new TimeSeries(PhpCommunication.getPseudo((Integer) listId.get(i)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//on le remplie
			for (int j = 0; j < tab.size(); j++)
			{
				//recuperation des données de la ligne en cours
				ligne = (JSONObject) tab.get(j);
				//on ajoute au timeSeries les valeurs
				ts.add(createDate((String) ligne.get("date_")), (int) (long) ligne.get("score_elo"));
				//on utilise createDate, qui convertis une date en string sous forme de date jfreechart
			}
			
			dataset.addSeries(ts);
		}
		
        return dataset;
    }
	
	/**
	 * fonction qui créé un objet Second, utilisable dans un timeSerie, depuis une chaine date
	 * recupere sur la bdd
	 * @param String date : une chaine sous la forme YYYY-MM-DD hh:mm:ss
	 * 
	 * @return un objet Second, une date jfreechart précis a la seconde près
	 * 
	 * @author Charlie
	 */
	private static Second createDate(String date)
	{

		//recuperation des données de la chaine
		int seconde = Integer.parseInt(date.split(" ")[1].split(":")[2]);
		int minute = Integer.parseInt(date.split(" ")[1].split(":")[1]);
		int heure =  Integer.parseInt(date.split(" ")[1].split(":")[0]);
		
		int jour = Integer.parseInt(date.split(" ")[0].split("-")[2]);
		int mois = Integer.parseInt(date.split(" ")[0].split("-")[1]);
		int annee = Integer.parseInt(date.split(" ")[0].split("-")[0]);
		
		//creation de l'objet
		Second madate = new Second(seconde, minute, heure, jour, mois, annee);
		return madate;
	}
	
	/**
	 * fonction qui créé un graphique depuis un dataset
	 */
    private static JFreeChart createChart(XYDataset dataset) {

    	//un chart semble contenir d'interessant un objet xyplot
    	
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Classement Elo exemple",  // titre
            "temps",             // titre des abscisses
            "ELO",  			 // titre des ordonnées
            dataset,            // data
            true,               // faire 
            true,               // ajouter des outils
            false               // ajouter des liens
        );

        //parametrage de l'affichage du graphique
        
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        //parametrage des couleurs arriere plan, les cadrillages en colonne et ligne
        plot.setBackgroundPaint(Color.lightGray); //arriere plan
        plot.setDomainGridlinePaint(Color.white); //colonne
        plot.setRangeGridlinePaint(Color.white); //ligne
        
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(false);
        plot.setRangeCrosshairVisible(true);
        
        //on parametre le rendu des 'items', les points qui represente une donnée
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true); //si on affiches les points
            renderer.setBaseShapesFilled(true); //si les points sont remplis ou si l'on ne voit que leurs contours
            renderer.setDrawSeriesLineAsPath(true); // j'ai pas compris
        }
        
        //on parametre l'affichage des valeurs l'axe des abscisses
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));

        return chart;
    }

}
