package de.nitri.frogger.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import de.nitri.frogger.Game;
import de.nitri.frogger.data.ImageCache;
import de.nitri.frogger.sprites.TierSprite;

public class Crocodile extends TierSprite {

	public static TextureRegion TEXTURE_1;
	public static TextureRegion TEXTURE_2;
	
	private float _animationCnt;
	
	public Crocodile(Game game, float x, float y) {
		
		super(game, x, y);
		
		_animationCnt = 0f;
		
		if (Crocodile.TEXTURE_1 == null) {
			Crocodile.TEXTURE_1 = ImageCache.getFrame("croc_", 1);
			Crocodile.TEXTURE_2 = ImageCache.getFrame("croc_", 2);
		}
		
		setSkin(Crocodile.TEXTURE_1);
		body = new Rectangle (0, 0, width, height);
		game.screen.elements.add(this);
	}
	
	@Override
	public void update (float dt) {
		
		super.update(dt);
		
		if (_animationCnt > 120) {
			skin = Crocodile.TEXTURE_1;
			body.width = width;
			_animationCnt = 0f;
		} else if (_animationCnt > 60) {
			skin = Crocodile.TEXTURE_2;
			body.width = width*0.4f;
		}
		_animationCnt += 20 * dt;
		
	}
	
	@Override
	public Rectangle bounds () {
		body.x = x - body.width*0.5f;
		body.y = y - body.height*0.5f;
		return body;
	}
}

