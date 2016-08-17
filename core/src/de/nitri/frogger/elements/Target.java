package de.nitri.frogger.elements;


import de.nitri.frogger.Game;
import de.nitri.frogger.data.ImageCache;
import de.nitri.frogger.sprites.TierSprite;

public class Target extends TierSprite {
	
	public static final int TARGET = 0;
	public static final int FLY = 1;
	public static final int CROC = 2;
	public static final int BONUS_200 = 3;
	public static final int BONUS_400 = 4;

	public int type;

	public Target(Game game, float x, float y, int type) {
		
		super(game, x, y);
		this.type = type;
		
		switch (type) {
			case Target.TARGET:
				setSkin(ImageCache.getTexture("frog_target"));
				break;
			case Target.FLY:
				setSkin(ImageCache.getTexture("fly"));
				break;
			case Target.CROC:
				setSkin(ImageCache.getTexture("alligator"));
				break;
			case Target.BONUS_200:
				setSkin(ImageCache.getFrame("label_", 200));
				break;
			case Target.BONUS_400:
				setSkin(ImageCache.getFrame("label_", 400));
				break;
		}
		
		visible = false;
		_game.screen.elements.add(this);
	}

}
