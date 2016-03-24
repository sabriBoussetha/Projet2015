package fr.univavignon.courbes.sounds;
/**
 *  Cette interface contient des méthodes qui ajoutes les effects sonores
 *  à l'application
 * 
 * @author Sabri
 */
public interface SoundEngine {
	
	/**
	 *	Cette fonction est appelée quand il y a une collision avec un autre snake ou avec 
	 *  le snake même
	 */
	void collisionWithSnakeSound();
	
	/**
	 * 	Cette fonction est appelée quand il y a une item
	 */
	void collisionWithItemSound();
	
	/**
	 * 	Cette fonction est appelée quand il y a une bordure
	 */
	void collisionWithBordSound();
	
	/**
	 * 	Cette fonction est appelée quand il s'agit du début de la partie
	 */
	void newGameSound();
	
	/**
	 * 	Cette fonction est appelée à la fin de la partie 
	 */
	void endGameSound();
	
	/**
	 *  Fonction qui fait un effet sonore lors d'un click
	 */
	
	void clickSound();
	/**
	 * Fonction qui lance le son en arrière plan durant tout le jeu 
	 * 
	 */
	void backGroundMusic();

}