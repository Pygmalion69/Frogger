package de.nitri.frogger.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import de.nitri.frogger.Game;
import de.nitri.frogger.data.ImageCache;
import de.nitri.frogger.sprites.TierSprite;


public class Turtle extends TierSprite {

	public static TextureRegion TEXTURE_1;
	public static TextureRegion TEXTURE_2;
	public static TextureRegion TEXTURE_3;
	
	private boolean _animated;
	private float _animationCnt;
	
	public Turtle(Game game, float x, float y, boolean animated) {
		super(game, x, y);
		_animated = animated;
		
		if (Turtle.TEXTURE_1 == null) {
			Turtle.TEXTURE_1 = ImageCache.getFrame("turtle_", 1);
			Turtle.TEXTURE_2 = ImageCache.getFrame("turtle_", 2);
			Turtle.TEXTURE_3 = ImageCache.getFrame("turtle_", 3);
		}
		
		setSkin(Turtle.TEXTURE_1);
		game.screen.elements.add(this);
		_animationCnt = 0f;
	}
	
	@Override 
	public void update (float dt) {
		
		super.update(dt);
		
		if (_animated) {
			
			if (_animationCnt > 220) {
				skin = Turtle.TEXTURE_1;
				_animationCnt = 0f;				
			} else if (_animationCnt > 180) {
				visible = true;
			} else if (_animationCnt > 130) {
				visible = false;
				skin = Turtle.TEXTURE_2;
			} else if (_animationCnt > 105) {					
				skin = Turtle.TEXTURE_3;
			} else if (_animationCnt > 80) {
				skin = Turtle.TEXTURE_2;
			}
			
			_animationCnt += 50 * dt;
		}
	}
	
	@Override 
	public Rectangle bounds () {
		if (!visible || skin == Turtle.TEXTURE_3) return null;
		return super.bounds();
	}
	
	
}
