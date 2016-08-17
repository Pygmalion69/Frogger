package de.nitri.frogger.elements;


import de.nitri.frogger.Game;
import de.nitri.frogger.sprites.NumberSprite;

public class Score extends NumberSprite {

	public Score(Game game, float x, float y, String nameRoot) {
		super(game, x, y, nameRoot);
	}
	
	@Override 
	public void draw () {
		value = _game.gameData.score;
		super.draw();
	}
}
