package de.nitri.frogger.elements;


import de.nitri.frogger.Game;
import de.nitri.frogger.data.ImageCache;
import de.nitri.frogger.sprites.GameSprite;
import de.nitri.frogger.sprites.NumberSprite;

public class TimeMsg extends GameSprite {

	public NumberSprite timeLabel;
	
	public TimeMsg(Game game, float x, float y) {

		super(game, x, y);
		setSkin(ImageCache.getTexture("time_box"));
		
		_game.screen.elements.add(this);
		
		timeLabel = new NumberSprite (_game, x + width * 0.1f, y - height*0.3f, "number_time_");
		timeLabel.visible = false;
	}
	
	@Override 
	public void show () {
		visible = true;
		timeLabel.visible = true;
	}
	
	@Override 
	public void hide () {
		visible = false;
		timeLabel.visible = false;
	}
}
