package de.nitri.frogger.elements;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import de.nitri.frogger.Game;
import de.nitri.frogger.data.GameData;
import de.nitri.frogger.data.ImageCache;
import de.nitri.frogger.data.Sounds;
import de.nitri.frogger.screens.GameScreen;
import de.nitri.frogger.sprites.Animation;
import de.nitri.frogger.sprites.MovingSprite;


public class Player extends MovingSprite implements Animation.AnimationEventListener {

    public static final int MOVE_TOP = 0;
    public static final int MOVE_DOWN = 1;
    public static final int MOVE_LEFT = 2;
    public static final int MOVE_RIGHT = 3;


    public boolean hasBonus = false;
    public float tierSpeed = 0;
    public boolean dead = false;
    public boolean moving = false;

    public int tierIndex = 0;

    private TextureRegion _frogStand;
    private TextureRegion _frogSide;
    private TextureRegion _frogJump;
    private TextureRegion _frogSideJump;
    private TextureRegion _restFrame;

    private Animation _deathAnimation;
    private float _animationTime;

    private int _sideStep = 22;
    private Vector3 _startPoint;
    private Sprite _sprite;
    private float _moveCnt = 0.0f;

    public Player(Game game, float x, float y) {

        super(game, x, y);

        tierSpeed = 0.0f;

        //store textures for frog
        _frogStand = ImageCache.getTexture("frog_stand");
        _frogJump = ImageCache.getTexture("frog_jump");
        _frogSide = ImageCache.getTexture("frog_side");
        _frogSideJump = ImageCache.getTexture("frog_side_jump");
        _restFrame = _frogStand;

        _deathAnimation = new Animation(0.1f, ImageCache.getFrame("death_", 1),
                ImageCache.getFrame("death_", 2),
                ImageCache.getFrame("death_", 3),
                ImageCache.getFrame("death_", 4),
                ImageCache.getFrame("death_", 4),
                ImageCache.getFrame("death_", 4),
                ImageCache.getFrame("death_", 4),
                _frogStand
        );

        _animationTime = 0f;
        _deathAnimation.addEventListener(this);

        setSkin(_frogStand);

        //null skin so we draw sprite instead
        skin = null;
        _sprite = new Sprite(_frogStand);
        _sprite.setPosition(x, y);

        _startPoint = new Vector3(x - width * 0.5f, y - height * 0.5f, 0f);

        _game.screen.elements.add(this);

    }

    @Override
    public void reset() {
        visible = true;
        dead = false;
        _animationTime = 0f;

        x = nextX = _startPoint.x;
        y = nextY = _startPoint.y;
        _sprite.setPosition(x, y);

        tierIndex = 0;
        active = true;
        hasBonus = false;
        moving = false;

    }

    public void moveFrogUp() {
        if (!moving) {
            moving = true;
            tierIndex++;
            if (tierIndex >= Tier.TIER_Y.length) tierIndex = Tier.TIER_Y.length - 1;
            nextY = _game.screenHeight - Tier.TIER_Y[tierIndex] - height;
            _game.gameData.score += GameData.POINTS_JUMP;
            if (_game.gameData.sound)
                Sounds.play(Sounds.jump);
            showMoveFrame(MovingSprite.UP);

        }
    }


    public void moveFrogDown() {
        if (!moving) {
            moving = true;
            tierIndex--;
            if (tierIndex < 0) tierIndex = 0;
            nextY = _game.screenHeight - Tier.TIER_Y[tierIndex] - height;
            _game.gameData.score += GameData.POINTS_JUMP;
            if (_game.gameData.sound)
                Sounds.play(Sounds.jump);
            showMoveFrame(MovingSprite.DOWN);
        }
    }

    public void moveFrogLeft() {
        if (!moving) {
            moving = true;
            nextX -= _sideStep;
            if (_game.gameData.sound)
                Sounds.play(Sounds.jump);
            showMoveFrame(MovingSprite.LEFT);
        }
    }

    public void moveFrogRight() {
        if (!moving) {
            moving = true;
            nextX += _sideStep;
            if (_game.gameData.sound)
                Sounds.play(Sounds.jump);
            showMoveFrame(MovingSprite.RIGHT);
        }
    }

    @Override
    public void update(float dt) {

        if (dead) {
            _animationTime += dt;
            return;
        }
        if (moving) {
            int _moveInterval = 6;
            if (_moveCnt > _moveInterval) {
                moving = false;
                _sprite.setRegion(_restFrame);
                _moveCnt = 0.0f;
            }
            _moveCnt += 20 * dt;
        }
        //add tier speed if player is on top of a moving object
        nextX += tierSpeed * dt;
    }

    @Override
    public void place() {
        //limit movement if player is not on water Tiers so frog does not leave the screen
        if (tierIndex < 7) {
            if (nextX < 0)
                nextX = 0;
            if (nextX > _game.screenWidth - width)
                nextX = _game.screenWidth - width;
        } else {
            //make player go back to start if frog leaves screen on water Tiers
            if (nextX < 0 || nextX > _game.screenWidth - width) {
                if (_game.gameData.sound)
                    Sounds.play(Sounds.outofbounds);
                reset();
            }
        }
        super.place();
        _sprite.setPosition(x, y);
    }

    public void kill() {
        tierSpeed = 0;
        _game.gameData.gameMode = Game.GAME_STATE_ANIMATE;
        active = false;
        dead = true;
        _sprite.setScale(1f, 1f);
    }

    private void showMoveFrame(int dir) {
        switch (dir) {
            case MovingSprite.LEFT:
                _sprite.setScale(-1, 1);
                _sprite.setRegion(_frogSideJump);
                _restFrame = _frogSide;
                break;
            case MovingSprite.RIGHT:
                _sprite.setScale(1, 1);
                _sprite.setRegion(_frogSideJump);
                _restFrame = _frogSide;
                break;
            case MovingSprite.UP:
                _sprite.setScale(1, 1);
                _sprite.setRegion(_frogJump);
                _restFrame = _frogStand;
                break;
            case MovingSprite.DOWN:
                _sprite.setScale(1, -1);
                _sprite.setRegion(_frogJump);
                _restFrame = _frogStand;
                break;
        }

    }

    @Override
    public void draw() {
        if (dead) {
            //draw frame from death animation
            _sprite.setRegion(_deathAnimation.getKeyFrame(_animationTime, false));
        }
        _sprite.draw(_game.spriteBatch);
    }

    public void onAnimationEnded(Animation.AnimationEvent e) {
        visible = false;
        dead = false;
        _sprite.setRegion(_frogStand);
        _restFrame = _frogStand;

        if (_game.gameData.lives >= 0) {
            reset();
            _game.gameData.gameMode = Game.GAME_STATE_PLAY;
        } else {
            GameScreen screen = (GameScreen) _game.screen;
            screen.gameOver();
        }
    }
}
