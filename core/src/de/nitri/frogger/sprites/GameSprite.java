package de.nitri.frogger.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import de.nitri.frogger.Game;
import de.nitri.frogger.data.ImageCache;

public class GameSprite {
	
	public boolean active;
	public boolean visible;
	public float x = 0;
	public float y = 0;
	public int width = 0;
	public int height = 0;
	
	public TextureRegion skin;
	public Rectangle body;
	
	protected Game _game;
	
	public GameSprite (Game game, float x, float y) {
		_game = game;
		this.x = x;
		this.y = y;
		active = true;
		visible = true;
		skin = null;
	}
	
	public GameSprite (String skinName, Game game, float x, float y) {
		_game = game;
		active = true;
		visible = true;
		this.x = x;
		this.y = y;
		setSkin (skinName);	
	}
	
	public void setSkin (String skinName, int skinIndex) {
		setSkin (ImageCache.getFrame(skinName, skinIndex));
	}
	public void setSkin (String skinName) {
		setSkin (ImageCache.getTexture(skinName));
	}
	
	public void setSkin (TextureRegion texture) {
		this.skin = texture;
		width = skin.getRegionWidth();
		height = skin.getRegionHeight();
		x = x - skin.getRegionWidth() * 0.5f;
		y = y - skin.getRegionHeight() * 0.5f;
	}
	
	public float right () {
		return x + width;
	}
	
	public float left () {
		return x;
	}
	
	public float top () {
		return y + height;
	}
	
	public float bottom () {
		return y;
	}
	
	public Rectangle bounds () {
		return new Rectangle(x + width * 0.2f, y + height * 0.2f, width * 0.8f, height * 0.8f);
	}
	
	public void reset () {}
	public void update (float dt) {}
	public void show () {}
	public void hide () {}
	public void draw () {
		_game.spriteBatch.draw(skin, x, y);
	}
}
