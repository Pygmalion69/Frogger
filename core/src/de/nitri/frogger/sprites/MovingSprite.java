package de.nitri.frogger.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import de.nitri.frogger.Game;

public class MovingSprite extends GameSprite {

	public static final int  UP = 0;
	public static final int  DOWN = 1;
	public static final int  LEFT = 2;
	public static final int  RIGHT = 3;
	
	public float vx = 0;
	public float vy = 0;
	public float nextX = 0;
	public float nextY = 0;
	public float speed;
	
	public MovingSprite (Game game, float x, float y) {
		super (game, x, y);
		nextX = x;
		nextY = y;
	}
	
	@Override 
	public void setSkin (TextureRegion texture) {
		super.setSkin(texture);
		this.x = nextX = x;
		this.y = nextY = y;
	}
	
	public float next_right () {
		return nextX + width;
	}
	
	public float next_left () {
		return nextX;
	}
	
	public float next_top () {
		return nextY + height;
	}
	
	public float next_bottom () {
		return nextY;
	}
	
	public Rectangle next_bounds () {
		return new Rectangle(next_left(), next_bottom(), width, height);
	}
	
	@Override
	public void update (float dt) {
		nextX += speed * dt;
	}
	
	public void place () {
		x = nextX;
		y = nextY;
	}
}
