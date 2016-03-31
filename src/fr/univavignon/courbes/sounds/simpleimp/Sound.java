package fr.univavignon.courbes.sounds.simpleimp;

import java.applet.Applet;

import java.applet.AudioClip;
import java.io.File;
import java.io.Serializable;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.*;

/**
 * Classe nécessaire pour la manipulation (lancement/lancement en bouble/arrêt/reprise
 * d'un morceau de son.
 * Cette classe est la classe qui se charge du chargement du ficher et le lancement de ce dernier
 * 
 * @author Sabri
 *
 */

public class Sound implements Serializable {
	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	/** Clip nécessaire pour le lancement du son */
	private Clip clip;

 	/**
 	 * Constructeur avec arguement
 	 * @param fileName
 	 * 		Le nom du fichier en string pour récupérer le bon fichier qu'on veut lancer
 	 */
	public Sound(String fileName)
	{		
		try{
			URL file = Sound.class.getResource(fileName); // Obtenir l'url à partir du nom de fichier
			AudioInputStream aud = AudioSystem.getAudioInputStream(				
					new File(fileName).getAbsoluteFile()
					);
			AudioFormat format = aud.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					format.getSampleRate(),
					16,
					format.getChannels(),
					format.getChannels() * 2,
					format.getSampleRate(),		
					false
					);
			AudioInputStream audD = AudioSystem.getAudioInputStream(
					decodeFormat,
					aud
					);
			clip = AudioSystem.getClip();
			clip.open(audD);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Fonction permet de lancer le fichier son proprement dit
	 * @param loop
	 * 		Avec ce paramètre on décide de lancer le son en boucle ou pas.
	 * 		si {@true} en boucle {@false} sinon.
	 */
	public void play(final boolean loop)
	{
		try{
			new Thread(){
				@Override
				public void run(){
					if (clip == null) return;
					stopC();
					clip.setFramePosition(0);
					clip.start();
					if(loop) clip.loop(clip.LOOP_CONTINUOUSLY);
					}
			}.start();		// Lancement du thread du son
		}catch(Exception ee){
			ee.printStackTrace();	
		}	
	}
	
	/**
	 * Cette fonction arrête le fichier son lancé
	 */
	public void stopC()
	{
		if (clip.isRunning()) clip.stop();
	}
	
	/**
	 * Fonction permettant de fermer le son quand le fichier a fini de jouer
	 */
	public void close()
	{
		stopC();
		clip.close();
	}
}
