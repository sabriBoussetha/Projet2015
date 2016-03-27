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
 * Cette classe permet de construire un panel contenant un graphique
 * @author charlie
 *
 */
public class graphEloPanel extends JPanel{
	
	public graphEloPanel(LinkedList listId) 
    {
        super();
        JFreeChart chart = createChart(createDataset(listId));
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        
        this.setLayout(new BorderLayout());
        this.add(panel,  BorderLayout.CENTER); 
    }
	
	//fonction qui construit un dataset en dur
	private static XYDataset createDataset(LinkedList listId) {
		
		//un dataset est une sorte de paquet qui contients plusieurs series
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        
		//on construit un timeSeries par joueurs
		TimeSeries ts = null;
		
		String json = null;
		for (int i = 0; i < listId.size(); i++)
		{
			try {
				System.out.println("id : " + listId.get(i) + " pseudo : " + PhpCommunication.getPseudo((Integer) listId.get(i)));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//on recupere la donnee depuis la bases de donnee
			try {
				json = PhpCommunication.getElo((Integer) listId.get(i));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.out.println(json);
			
			
			
			//on recupe les donne du json
			JSONParser parser = new JSONParser();
			
			JSONArray tab = null;
			try {
				tab = (JSONArray) parser.parse(json);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject ligne;
			
			//on cree un TimeSeries
			try {
				ts = new TimeSeries(PhpCommunication.getPseudo((Integer) listId.get(i)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//on le remplie
			for (int j = 0; j < tab.size(); j++)
			{
				
				ligne = (JSONObject) tab.get(j);
				
				System.out.println(ligne.get("score_elo"));
				System.out.println(ligne.get("date_"));
				
				//ts.add(new Month(j, 2016), (int) (double) ligne.get("score_elo"));
				//System.out.println("ok");
				
				ts.add(createDate((String) ligne.get("date_")), (int) (long) ligne.get("score_elo"));
			}
			
			//System.out.println(ts.toString());
			dataset.addSeries(ts);
		}
		
		/*
    	//une timeSeries sont une serie d'elements chronoliques
        TimeSeries s1 = new TimeSeries("Charlie");
        s1.add(new Month(1, 2016), 2000);
        s1.add(new Month(2, 2016), 2050);
        s1.add(new Month(3, 2016), 2075);
        s1.add(new Month(4, 2016), 2150);
        s1.add(new Month(5, 2016), 2150);
        s1.add(new Month(6, 2016), 2100);
        s1.add(new Month(7, 2016), 2075);
        s1.add(new Month(8, 2016), 2120);
        s1.add(new Month(9, 2016), 2140);
        s1.add(new Month(10, 2016), 2150);
        s1.add(new Month(11, 2016), 2200);
        s1.add(new Month(12, 2016), 2150);
        
        TimeSeries s2 = new TimeSeries("Alex");
        s2.add(new Month(1, 2016), 2000);
        s2.add(new Month(2, 2016), 1900);
        s2.add(new Month(3, 2016), 1800);
        s2.add(new Month(4, 2016), 1700);
        s2.add(new Month(5, 2016), 1600);
        s2.add(new Month(6, 2016), 1500);
        s2.add(new Month(7, 2016), 1400);
        s2.add(new Month(8, 2016), 1300);
        s2.add(new Month(9, 2016), 1200);
        s2.add(new Month(10, 2016), 1100);
        s2.add(new Month(11, 2016), 1000);
        s2.add(new Month(12, 2016), 900);*/

        //un dataset est une sorte de paquet qui contients plusieurs series
        //TimeSeriesCollection dataset = new TimeSeriesCollection();
        //dataset.addSeries(s1);
        //dataset.addSeries(s2);
       // dataset.addSeries(ts);

        return dataset;
    }
	
	/**
	 * fonction qui créé un objet day, utilisable dans un timeSerie, depuis une chaine date
	 * recupere sur la bdd
	 * @param date
	 * @return
	 */
	private static Second createDate(String date)
	{
		//2016-03-26 17:11:32
		//new Day(4, MonthConstants.JANUARY, 2001)
		
		int seconde = Integer.parseInt(date.split(" ")[1].split(":")[2]);
		int minute = Integer.parseInt(date.split(" ")[1].split(":")[1]);
		int heure =  Integer.parseInt(date.split(" ")[1].split(":")[0]);
		
		int jour = Integer.parseInt(date.split(" ")[0].split("-")[2]);
		int mois = Integer.parseInt(date.split(" ")[0].split("-")[1]);
		int annee = Integer.parseInt(date.split(" ")[0].split("-")[0]);
		Second day = new Second(seconde, minute, heure, jour, mois, annee);
		return day;
	}
	
    private static JFreeChart createChart(XYDataset dataset) {

    	//un chart semble contenir d'interessant un objet xyplot
    	
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Classement Elo exemple",  // title
            "temps",             // x-axis label
            "ELO",  			 // y-axis label
            dataset,            // data
            true,               // create legend?
            true,               // generate tooltips?
            false               // generate URLs?
        );

        chart.setBackgroundPaint(Color.white);
        
        //NOTE : je pense que xyplot contient : les axes, le cadrillages, les points, tout
        
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
