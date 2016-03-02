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
	
	AudioClip audio;
	
	static Sound soundD = new Sound("test.wav");
 	
	public Sound(String fileName)
	{
		try{
			/*File yourFile = new File(fileName);
		    AudioInputStream stream;
		    AudioFormat format;
		    DataLine.Info info;
		    Clip clip;

		    stream = AudioSystem.getAudioInputStream(yourFile);
		    format = stream.getFormat();
		    info = new DataLine.Info(Clip.class, format);
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		    clip.start();*/
			audio = Applet.newAudioClip(Sound.class.getResource(fileName));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void play()
	{	try{
			new Thread(){
				public void run(){
					audio.play();
				}
			}.start();
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	
}
