package de.nitri.frogger.elements;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import de.nitri.frogger.Game;
import de.nitri.frogger.sprites.GameSprite;


public class Controls extends GameSprite {

	private Vector3 _center;
	
	public Controls(Game game, float x, float y) {
		
		super(game, x, y);
		setSkin("control");
		
		_game.screen.elements.add(this);
		_center = new Vector3 (x, y , 0f);
	}
	
	public int getDirection (Vector3 p) {
		
		double diffx = p.x - _center.x;
		double diffy = p.y - _center.y;
		
		double rad = Math.atan2(diffy, diffx);
		
		int angle = (int) (180 * rad / Math.PI);
		if (angle < 360) angle += 360;
		if (angle > 360) angle -=  360;
		
		
		if (angle > 315 || angle < 45) {
			//Gdx.app.log("CONTROL CLICK:", "RIGHT");
			return Player.MOVE_RIGHT;
		} else if (angle >= 45 && angle <= 135) {
			//Gdx.app.log("CONTROL CLICK:", "TOP");
			return Player.MOVE_TOP;
		} else if (angle > 135 && angle < 225) {
			//Gdx.app.log("CONTROL CLICK:", "LEFT");
			return Player.MOVE_LEFT;
		} else {
			//Gdx.app.log("CONTROL CLICK:", "DOWN");
			return Player.MOVE_DOWN;
		}
		
	}
	
	@Override
	public Rectangle bounds () {
		return new Rectangle(x, y, width, height);
	}

}
