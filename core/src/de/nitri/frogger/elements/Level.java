package de.nitri.frogger.elements;


import de.nitri.frogger.Game;
import de.nitri.frogger.sprites.NumberSprite;

public class Level extends NumberSprite {

	public Level(Game game, float x, float y, String nameRoot) {
		super(game, x, y, nameRoot);
	}
	
	@Override 
	public void draw () {
		value = _game.gameData.level;
		super.draw();
	}

}
