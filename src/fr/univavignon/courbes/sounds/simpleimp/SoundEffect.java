package fr.univavignon.courbes.sounds.simpleimp;

import fr.univavignon.courbes.common.ItemType;
import fr.univavignon.courbes.sounds.SoundEngine;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

/**
 * Classe qui implemente l'interface son, comportant les fonctions nécessaires
 * pour le son, des le lancement de l'application jusqu'à la fin
 * de celle ci.
 * 
 * @author Sabri
 *
 */
public class SoundEffect implements SoundEngine, Serializable {
	private static final long serialVersionUID = 1L;
	/** Instance de la classe Sound nécessaire pour la manipulation du son */
	public Sound sound;
	/** */
	public String []soundTrack = new String[]{ "res/sounds/background_sound/adventure.wav",
											   "res/sounds/background_sound/Happy.wav",
											   "res/sounds/background_sound/Bonus.wav",
											   "res/sounds/background_sound/piano.wav"};
	
	/**
	 * Jouer le son correspondant à la collision avec un snake
	 */
	@Override
	public void collisionWithSnakeSound() {
		sound = new Sound("res/sounds/Bonus.wav");
		sound.play(false);
	}

	/**
	 * Jouer le son correspondant à la collision avec un item.
	 * Cette fonction lance différents type de son et ça en fonction de l'item ramasé
	 * @param item Correspond au type de l'item ramasé
	 */
	@Override
	public void collisionWithItemSound(ItemType item) {
		switch (item) {
			case USER_FAST: sound = new Sound("res/sounds/item_sound/Weeeeeeeeeee.wav");
							break;
			case USER_SLOW: sound = new Sound("res/sounds/item_sound/eat_sound.wav");
							break;
			case USER_FLY: sound = new Sound("res/sounds/item_sound/Weeeeeeeeeee.wav");
							break;
			case OTHERS_FAST: sound = new Sound("res/sounds/item_sound/Weeeeeeeeeee.wav");
							break;	
			case OTHERS_THICK: sound = new Sound("res/sounds/item_sound/Weeeeeeeeeee.wav");
							break;
			case OTHERS_SLOW: sound = new Sound("res/sounds/item_sound/Weeeeeeeeeee.wav");
							break;
			default: sound = new Sound("res/sounds/item_sound/Weeeeeeeeeee.wav");
					 break;
			
		}
		sound.play(false);
	}
	
	/**
	 * Lancement du son correspondant à la collision avec la bordure de l'aire de jeu
	 */
	@Override
	public void collisionWithBordSound() {
		sound = new Sound("res/sounds/collisionWithBoard.wav");
		sound.play(false);
		
	}
	
	/**
	 * Cette fonction lance le son de commencement d'une manche
	 * */
	@Override
	public void newGameSound() {
		sound = new Sound("res/sounds/background_sound/Bonus.wav");
		sound.play(false);
		
	}
	
	/**
	 * Fonction qui lance le son à la fin d'une manche
	 */
	@Override
	public void endGameSound() {
		sound = new Sound("res/sounds/Clapps.wav");
		sound.play(false);
	}
	
	/**
	 * Fonction qui lance le son lors d'un click sur la souris ou lors d'une
	 * touche clavier
	 * */
	@Override
	public void clickSound(){
		sound = new Sound("res/sounds/mouse_click.wav");
		sound.play(false);
	}
	/**
	 * Fonction qui lance la musique en arrière plan dans l'application.
	 * @param play 
	 * 		En fonction de cette variable booléenne on joue ou on arrête
	 * 		le morceau joué en arrière plan
	 * @param soundNumber 
	 * 		Indiquant l'indice du morceau en cours dans le tableau soundTrack
	 * 
	 * @return boolean 
	 * 		Si le morceau joue elle retourne {@true} sinon {@false}
	 * */
	@Override
	public boolean backGroundMusic(boolean play, int soundNumber)
	{	
		if(play)
		{
			sound = new Sound(soundTrack[soundNumber]);
			sound.play(true);
			return true;	
		}
		else if(!play)
		{
			sound.stopC();
			return false;
		}
		return play;
	}
	/**
	 * Fonction qui lance le son d'erreur lors d'un message d'erreur qui apparait
	 * */
	@Override
	public void errorSound() 
	{
		sound = new Sound("res/sounds/Error.wav");
		sound.play(false);
	}

}
