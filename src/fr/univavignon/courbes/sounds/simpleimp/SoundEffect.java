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
		sound = new Sound("res/sounds/Bonus.wav");
		sound.play(false);
	}

	@Override
	public void collisionWithItemSound() {
		sound = new Sound("res/sounds/Bonus.wav");
		sound.play(false);
	}

	@Override
	public void collisionWithBordSound() {
		sound = new Sound("res/sounds/Bonus.wav");
		sound.play(false);
		
	}

	@Override
	public void newGameSound() {
		sound = new Sound("res/sounds/Bonus.wav");
		sound.play(false);
		
	}

	@Override
	public void endGameSound() {
		sound = new Sound("res/sounds/Bonus.wav");
		sound.play(false);
	}
	
	@Override
	public void clickSound(){
		sound = new Sound("res/sounds/mouse_click.wav");
		sound.play(false);
	}
	
	@Override
	public void backGroundMusic()
	{
			sound = new Sound("res/sounds/Bonus.wav");
			sound.play(true);	
	}

}
