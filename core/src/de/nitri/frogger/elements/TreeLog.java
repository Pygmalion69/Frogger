package de.nitri.frogger.elements;


import com.badlogic.gdx.math.Rectangle;

import de.nitri.frogger.Game;
import de.nitri.frogger.sprites.TierSprite;


public class TreeLog extends TierSprite {

	public TreeLog (String skinName, Game game, float x, float y) {
		
		super(game, x, y);
		
		this._game = game;
		active = true;
		visible = true;
		
		setSkin (skinName);
		
		this.x = x - skin.getRegionWidth() * 0.5f;
		this.y = y - skin.getRegionHeight() * 0.5f;
		
		game.screen.elements.add(this);
	}
	
	@Override 
	public Rectangle bounds () {
		body = super.bounds();
		body.width = width * 0.9f;
		return body;
	}
	

}
