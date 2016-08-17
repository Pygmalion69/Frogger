package de.nitri.frogger.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
	
	public static Sound jump;
	public static Sound hit;
	public static Sound pickup;
	public static Sound splash;
	public static Sound outofbounds;
	public static Sound target;
	
	public static void load () {
		
		jump = loadSound("jump.wav");
		hit = loadSound("hit.wav");
		pickup = loadSound("pickup.wav");
		splash = loadSound("splash2.wav");
		outofbounds = loadSound("outofbounds.wav");
		target = loadSound("target.wav");
	}
	
	private static Sound loadSound (String filename) {
		return Gdx.audio.newSound(Gdx.files.internal("data/sounds/" + filename));
	}
	
	public static void play (Sound sound) {
		sound.play(1);
	}
}
