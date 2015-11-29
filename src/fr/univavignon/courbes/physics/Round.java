package fr.univavignon.courbes.physics;

import java.util.HashMap;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

public class Round implements PhysicsEngine {
	private Board board;
	/** Représente les coordonnées aprés la virgule de la position d'un snake **/
	private double deltaSnake[][]; 
	


	@Override
	public Board init(int width, int height, int playerNbr) {
		deltaSnake = new double[playerNbr][2];
		
		board = new Board();
		board.width = width;
		board.height = height;
		
		board.snakesMap = new HashMap<Position, Integer>();
		board.itemsMap = new HashMap<Position, Item>();
		board.snakes = new Snake[playerNbr];
		Position posSpawn;

		for (int i = 0; i < playerNbr ; i++) 
		{
			posSpawn = generateSpawnPos(width, height);
			board.snakes[i] = new Snake();
			initSnake(board.snakes[i], i, posSpawn);
			System.out.println("Snake " + Integer.toString(i) + " spawn a la position " + Integer.toString(posSpawn.x) + " "+Integer.toString(posSpawn.y));
		}
		
		return board;
	}
	
	/**
	 * @param snake
	 * @param id
	 * @param spawnPosition
	 */
	private void initSnake(Snake snake, int id, Position spawnPosition) {
		snake.currentItems  = new HashMap<Item, Long>() ;
		snake.playerId 	    = id;
		snake.currentX      = spawnPosition.x;
		snake.currentY      = spawnPosition.y;
		snake.currentAngle  = (int)(Math.random() * 359); //Génération aléatoire d'un angle entre 0 et 359°
		snake.headRadius 	= 1;		
		snake.currentSpeed  = 0.1;		
		snake.state 		= true;
		snake.collision 	= true;
		snake.inversion     = false;
		snake.fly   		= false;
		snake.holeRate 	    = 1.0;	
		System.out.println("Angle en degré : " + Double.toString(snake.currentAngle));	
	}

	
	@Override
	public void update(long elapsedTime, Map<Integer, Direction> commands) {

		//Maj tête du snake pos
		majSnakesPosition(elapsedTime);
		//Maj la map tracé 
		//Maj les directions des snakes
		
	}

	@Override
	public void forceUpdate(Board board) {
		// TODO Auto-generated method stub
		
	}
	




	/**
	 * Génére une position aléatoire sur la plateau, la fonction générera une position qui n'est pas
	 * trop rapproché des bords du plateau et verifiera qu'elle n'est pas trop proche de la position
	 * d'un autre snake.
	 *
	 * @param widthBoard Largeur de l'aire de jeu, exprimée en pixel.
	 * @param heightBoard Hauteur de l'aire de jeu, exprimée en pixel.
	 * @return La position généré aléatoirement
	 */
	private Position generateSpawnPos(int widthBoard, int heightBoard) {

		Boolean flagPos;
		Position posSpawn = new Position();

		do {
			posSpawn.x = 20 + (int)(Math.random() * heightBoard - 20); 
			posSpawn.y = 20 + (int)(Math.random() * widthBoard - 20); 
			flagPos = true;

			for(int i = 0; i < board.snakes.length ; i++)// Teste de la proximité avec un autre snake
			{
				if(board.snakes[i] != null)
				{
					if(Math.abs(posSpawn.x - board.snakes[i].currentX) +  Math.abs(posSpawn.y - board.snakes[i].currentY) < 40)
					{
						flagPos = false; // Proximité détécté, on cherche alors une nouvelle position
					}
				}
			}
		}while(!flagPos);

		return posSpawn;
	}


	/**
	 * Cette méthode met à jour les positions des têtes de tout les snakes du jeu encore en vie graçe à leur
	 * vitesse et leur direction en degré, elle remplit aussi dans le même temps la Map avec les tracés des snakes.
	 * Elle verifie aussi si le snake n'est pas entré en contact avec un autre snake ou un item.
	 * @param elapsedTime Temps ecoulé en ms depuis le dernier update du plateau
	 */
	public void majSnakesPosition(long elapsedTime) {
		long elapsed;
		double pixStep;
		Position pos = new Position();
		for(Snake snake : board.snakes)
		{
			if(snake.state == true)
			{
				elapsed = elapsedTime;
				pixStep = 0;
				while (elapsed > 0)
				{
					while(pixStep < 1 && elapsed > 0)
					{
						elapsed--;
						pixStep += snake.currentSpeed;
					}
					if(pixStep >= 1)
					{
						deltaSnake[snake.playerId][0] += Math.cos(Math.toRadians(snake.currentAngle));
						deltaSnake[snake.playerId][1] += Math.sin(Math.toRadians(snake.currentAngle));

						if(deltaSnake[snake.playerId][1] >= 1 && deltaSnake[snake.playerId][0] >= 1) {
							snake.currentY--;
							snake.currentX++;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][1]--;
							deltaSnake[snake.playerId][0]--;
						}
						else if(deltaSnake[snake.playerId][1] <= -1 && deltaSnake[snake.playerId][0] >= 1) {
							snake.currentY++;
							snake.currentX++;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][1]++;
							deltaSnake[snake.playerId][0]--;
						}
						else if(deltaSnake[snake.playerId][1] <= -1 && deltaSnake[snake.playerId][0] <= -1) {
							snake.currentY++;
							snake.currentX--;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][1]++;
							deltaSnake[snake.playerId][0]++;
						}
						else if(deltaSnake[snake.playerId][1] >= 1 && deltaSnake[snake.playerId][0] <= -1) {
							snake.currentY--;
							snake.currentX--;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][1]--;
							deltaSnake[snake.playerId][0]++;
						}
						else if(deltaSnake[snake.playerId][1] >= 1) {
							snake.currentY--;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][1]--;
						}
						else if(deltaSnake[snake.playerId][1] <= -1) {
							snake.currentY++;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][1]++;
						}
						else if(deltaSnake[snake.playerId][0] >= 1) {
							snake.currentX++;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][0]--;
						}
						else if(deltaSnake[snake.playerId][0] <= -1) {
							snake.currentX--;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][0]++;
						}

						pixStep --;
						System.out.println("Position snake "+ Integer.toString(snake.playerId)+ " x:" + Integer.toString(snake.currentX) + " y:" + Integer.toString(snake.currentY));
					}
				}
				//TODO gérer si le snake se prend un mur
				
				// Gestion si le snake cogne un autre snake
				pos.x = snake.currentX;
				pos.y = snake.currentY;
				Integer idSnake = board.snakesMap.get(pos);
				if(idSnake != null && idSnake != snake.playerId)
				{
					snake.state = false;	
				}
				// Gérer si le snake se prend un item
				Item itemRecup = board.itemsMap.get(pos);
				if( itemRecup != null )
				{
					if(snake.state)
					{
						snake.currentItems.put(itemRecup, (long)itemRecup.duration); // Ajout de l'item au Snake
					}
				}
			}
		}
	}
	
	public void isSnakeOnIt() {
		
		
	}
	/**
	 * Cette méthode met à jour les différents angles courants des snakes selon la direction
	 * demandée.
	 * @param commands Collection des différentes commandes demandés pour chaque snake 
	 */
	public void majSnakesDirection( Map<Integer, Direction> commands)
	{
		for(Snake snake : board.snakes)
		{
			switch (commands.get(snake.playerId))
			{
			case LEFT:
				snake.currentAngle += 5;
				break;
			case RIGHT:
				snake.currentAngle -= 5;
				break;
			case NONE:
				break;
			default:
				break;
			}
		}
	}
}