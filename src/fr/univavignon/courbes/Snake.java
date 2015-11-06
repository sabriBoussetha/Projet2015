

public class Snake 
{
	private int idPlayer;
	private Position head; 			//Position = (x,y)
	private double direction; 		//angle
	private double ray;				//Size
	private double speed;
	private boolean state;			// 0 = dead 	   ----- 1 = alive
	private boolean crashmode;		// 0 = nocollision ----- 1 = collision   
	private boolean invertmode; 	// 0 = normal keys ----- 1 = invert keys
	private double holerate;		// % hole apparition
	private boolean planemode; 		// 0 = not plane   ----- 1 = plane mode
	private Item[] changes;     // List of Items

	
	public Snake(int id)
	{
		idPlayer 	= id;
		head 		= new Position();
		direction   = -90.0; 		// vers le haut
		ray 		= 3.0;  		// pixel ?
		speed 		= 1.0;			// pixel/frame ?
		state 		= true;
		crashmode 	= true;
		invertmode  = false;
		holerate 	= 5.0;			// 5% ?
		planemode  = false;
		changes = new Item[];
	}
	
	/***************************************************************************************************************************************************************\
	/																																							   *\
	/																		GETTERS & SETTERS																	   *\
	/																																							   *\
	/***************************************************************************************************************************************************************/
	public int getID()
	{
		return idPlayer;
	}
	
	public boolean isAlive()
	{
		return state;
	}
	
	public boolean isCrashMode()
	{
		return crashmode;
	}
	
	public boolean isInversed()
	{
		return invertmode;
	}
	
	public boolean isFlying()
	{
		return planemode;
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public double rateHoles()
	{
		return holerate();
	}
	
	public double getSize()
	{
		return ray;
	}
	
	
	/***************************************************************************************************************************************************************\
	/																																							   *\
	/																		ACTIONS																				   *\
	/																																							   *\
	/***************************************************************************************************************************************************************/

	
	public void turnLeft()
	{
		direction--;
	}
	
	public void turnRight()
	{
		direction++;
	}
}


	
//double axeX1=cos(angle1*PI/180.0);
//double axeY1=sin(angle1*PI/180.0);