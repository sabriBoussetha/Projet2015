package fr.univavignon.courbes.sounds.simpleimp;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Sound {
	

	private Clip clip;

 	
	public Sound(String fileName)
	{
		try{
				AudioInputStream aud = AudioSystem.getAudioInputStream(
						getClass().getResourceAsStream(
								fileName
								)
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
	
	public void play()
	{	/*try{
			new Thread(){
				public void run(){
					audio.play();
				}
			}.start();
		}catch(Exception ee){
			ee.printStackTrace();
		}*/	
		if (clip == null) return;
		stop();
		clip.setFramePosition(0);
		clip.start();
		
	}
	
	public void stop()
	{
		if (clip.isRunning()) clip.stop();
	}
	
	public void close()
	{
		stop();
		clip.close();
	}
}
