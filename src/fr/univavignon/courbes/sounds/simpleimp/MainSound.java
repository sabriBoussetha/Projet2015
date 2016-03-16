package fr.univavignon.courbes.sounds.simpleimp;

import org.omg.CORBA.portable.InputStream;
import  sun.audio.*;    //import the sun.audio package
import  java.io.*;

import fr.univavignon.courbes.common.Player;

public class MainSound {

	public static void main(String[] args){
		
		SoundEffect a = new SoundEffect();
		a.collisionWithSnakeSound();
	}

}
