package fr.univavignon.courbes.inter.stats;

import java.awt.BorderLayout;
import java.awt.Color;
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
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

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
        s2.add(new Month(12, 2016), 900);

        //un dataset est une sorte de paquet qui contients plusieurs series
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);

        return dataset;
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
