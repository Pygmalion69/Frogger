package de.nitri.frogger.elements;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import de.nitri.frogger.Game;
import de.nitri.frogger.data.ImageCache;
import de.nitri.frogger.sprites.MovingSprite;
import de.nitri.frogger.sprites.TierSprite;


public class BonusFrog extends MovingSprite {
	
	public TierSprite log;
	
	
	private TextureRegion _frogStand;
	private TextureRegion _frogSide;
	private Player _frog;
	private int _animationCnt;
	private int _animationInterval;
	private float _xMove;
	
	private Random _random;
	
	public BonusFrog(Game game, float x, float y, Player frog) {
		
		super(game, x, y);
		
		_frog = frog;
		_frogStand = ImageCache.getTexture("frog_bonus_stand");
		_frogSide = ImageCache.getTexture("frog_bonus_side");
		
		log = null;
		setSkin (_frogStand);
		
		visible = false;

		_game.screen.elements.add(this);
		body = new Rectangle (0, 0, width*0.5f, height * 0.5f);
		
		_random = new Random();
		_animationInterval = _random.nextInt(31);
	}
	
	@Override 
	public void reset () {
		visible = false;
		_animationCnt = 0;
	}
	
	@Override
	public void update (float dt) {
		
		if (log == null) return;
		
		if (visible) {
			if (_frog.hasBonus) {
				nextX = _frog.nextX;
				nextY = _frog.nextY;
			} else {
				nextX = log.nextX + _xMove;
				nextY = log.nextY;
				//else, still on log, move with log
				if (_animationCnt > _animationInterval) {
					
					switch (_random.nextInt(5)) {
						case 0:
							skin.flip(true, true);
							break;
						case 1:
							skin = _frogStand;
							break;
						case 2:
							move();
							break;
						case 3:
							skin = _frogSide;
							break;
						case 4:
							if (_random.nextInt(10) > 5) {
								skin.flip(true, false);
							} else {
								skin.flip(false, true);
							}
							move();
							break;
					}
					_animationInterval = _random.nextInt(50);
					_animationCnt = 0;
				}
				_animationCnt++;
			}
		} else {
			//check if log is out of bounds
			if (log.left() < 0) {
				nextX = log.nextX;
				nextY = log.nextY;
				visible = true;
			}
		}
	}
	
	
	private void move () {
		
		if (nextX - width*0.5f > log.next_left()) {
			_xMove = -width*0.5f;
			skin.flip(true, false);
			skin = _frogSide;
		} else if (nextX + width*0.5f < log.next_right()) {
			_xMove = width*0.5f;
			skin.flip(true, false);
			skin = _frogSide;
		}
				
	}
	
	@Override
	public Rectangle bounds () {
		if (!visible) return null;
		body.x = x - width*0.2f;
		body.y = y - height*0.2f;
		return body;
	}
}
