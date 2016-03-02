package fr.univavignon.courbes.sounds.simpleimp;

import fr.univavignon.courbes.sounds.SoundEngine;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

/**
 * @author Sabri
 *
 */
public class SoundEffect implements SoundEngine {
	
	private Sound sound;
	
	@Override
	public void collisionWithSnakeSound() {
		sound = new Sound("/home/sabri/git/Projet2015/res/sounds/test.wav");
		sound.play();
	}

	@Override
	public void collisionWithItemSound() {
		
		
	}

	@Override
	public void collisionWithBordSound() {
		
		
	}

	@Override
	public void newGameSound() {
		
		
	}

	@Override
	public void endGameSound() {
		
		
	}

}
