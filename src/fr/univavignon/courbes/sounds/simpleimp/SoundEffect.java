package fr.univavignon.courbes.sounds.simpleimp;

import fr.univavignon.courbes.common.ItemType;
import fr.univavignon.courbes.sounds.SoundEngine;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

/**
 * @author Sabri
 *
 */
public class SoundEffect implements SoundEngine {
	
	public Sound sound;
	
	@Override
	public void collisionWithSnakeSound() {
		sound = new Sound("res/sounds/Bonus.wav");
		sound.play(false);
	}

	@Override
	public void collisionWithItemSound(ItemType item) {
		switch (item) {
			case USER_FAST: sound = new Sound("res/sounds/eat_sound.wav");
							break;
			case USER_SLOW: sound = new Sound("res/sounds/eat_sound.wav");
							break;
			case USER_FLY: sound = new Sound("res/sounds/eat_sound.wav");
							break;
			case OTHERS_FAST: sound = new Sound("res/sounds/eat_sound.wav");
							break;	
			case OTHERS_THICK: sound = new Sound("res/sounds/eat_sound.wav");
							break;
			case OTHERS_SLOW: sound = new Sound("res/sounds/eat_sound.wav");
							break;
			default: sound = new Sound("res/sounds/eat_sound.wav");
					 break;
			
		}
		sound.play(false);
	}

	@Override
	public void collisionWithBordSound() {
		sound = new Sound("res/sounds/collisionWithBoard.wav");
		sound.play(false);
		
	}

	@Override
	public void newGameSound() {
		sound = new Sound("res/sounds/Bonus.wav");
		sound.play(false);
		
	}

	@Override
	public void endGameSound() {
		sound = new Sound("res/sounds/Clapps.wav");
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
		sound = new Sound("res/sounds/Happy.wav");
		sound.play(true);	
	}

	@Override
	public void errorSound() 
	{
		sound = new Sound("res/sounds/Error.wav");
		sound.play(false);
	}

}
