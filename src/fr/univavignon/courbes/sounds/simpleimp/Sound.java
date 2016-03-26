package fr.univavignon.courbes.sounds.simpleimp;

import java.applet.Applet;

import java.applet.AudioClip;
import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.*;

public class Sound extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private Clip clip;

 	
	public Sound(String fileName)
	{
		
	
		    /*  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		      this.setTitle("Test Sound Clip");
		      this.setSize(300, 200);
		      this.setVisible(true);*/
			// Pour tester 
		
		try{
				URL file = Sound.class.getResource(fileName);

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
	
	public void play(final boolean loop)
	{	

			try{
				new Thread(){
					public void run(){
						if (clip == null) return;
						stopC();
						clip.setFramePosition(0);
						clip.start();
						if(loop) clip.loop(clip.LOOP_CONTINUOUSLY);
					}
				}.start();
			}catch(Exception ee){
				ee.printStackTrace();
			}	

	}
	
	public void stopC()
	{
		if (clip.isRunning()) clip.stop();
	}
	
	public void close()
	{
		stopC();
		clip.close();
	}
}
