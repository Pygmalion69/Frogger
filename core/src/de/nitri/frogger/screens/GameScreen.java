package de.nitri.frogger.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import de.nitri.frogger.Game;
import de.nitri.frogger.data.Sounds;
import de.nitri.frogger.elements.BonusFrog;
import de.nitri.frogger.elements.Controls;
import de.nitri.frogger.elements.FinalTier;
import de.nitri.frogger.elements.Level;
import de.nitri.frogger.elements.Lives;
import de.nitri.frogger.elements.Player;
import de.nitri.frogger.elements.Score;
import de.nitri.frogger.elements.Tier;
import de.nitri.frogger.elements.TimeBar;
import de.nitri.frogger.elements.TimeMsg;
import de.nitri.frogger.sprites.GameSprite;


public class GameScreen extends Screen {

    private Player _player;
    private BonusFrog _bonusFrog;
    //private Controls _controls;
    private TimeBar _timeBar;
    private GameSprite _gameOverMsg;
    private GameSprite _newLevelMsg;
    private TimeMsg _levelTimeMsg;
    private Score _score;
    private Level _level;
    private Lives _lives;
    private Vector3 _touchPoint;
    private Rectangle _controlBounds;

    private List<Tier> _tiers;
    private GameSprite _sound;
    private GameSprite _mute;


    public GameScreen(Game game) {
        super(game);
        _tiers = new ArrayList<Tier>();
        _touchPoint = new Vector3();
    }

    @Override
    public void createScreen() {

        if (elements.size() == 0) {

            //add bg
            elements.add(new GameSprite("bg", _game, _game.screenWidth * 0.5f, _game.screenHeight * 0.5f));

            //add tiers (cars, trees, crocodiles, turtles...)
            for (int i = 0; i < 12; i++) {
                _tiers.add(new Tier(_game, i));
            }
            _tiers.add(new FinalTier(_game, 12));

            elements.add(new GameSprite("grass", _game, _game.screenWidth * 0.5f, _game.screenHeight - _game.screenHeight * 0.12f));

            _player = new Player(_game, _game.screenWidth * 0.5f, _game.screenHeight - _game.screenHeight * 0.89f);
            _bonusFrog = new BonusFrog(_game, -100, -100, _player);
            _bonusFrog.log = _tiers.get(8).getElement(0);

            elements.add(new GameSprite("label_time", _game, _game.screenWidth * 0.1f, _game.screenHeight * 0.04f));

            _timeBar = new TimeBar(_game, _game.screenWidth * 0.18f, _game.screenHeight * 0.03f);

            _score = new Score(_game, _game.screenWidth * 0.2f, _game.screenHeight - _game.screenHeight * 0.05f, "number_score_");
            _level = new Level(_game, _game.screenWidth * 0.04f, _game.screenHeight - _game.screenHeight * 0.05f, "number_level_");
            _lives = new Lives(_game, _game.screenWidth * 0.68f, _game.screenHeight - _game.screenHeight * 0.06f);

            _sound = new GameSprite("sound", _game, _game.screenWidth * 0.5f, _game.screenHeight - _game.screenHeight * 0.04f);
            _sound.visible = _game.gameData.sound;
            elements.add(_sound);

            _mute = new GameSprite("mute", _game, _game.screenWidth * 0.5f, _game.screenHeight - _game.screenHeight * 0.04f);
            _mute.visible = !_game.gameData.sound;
            elements.add(_mute);


            //_controls = new Controls (_game, _game.screenWidth * 0.82f, _game.screenHeight - _game.screenHeight * 0.88f);
            //_controlBounds = _controls.bounds();

            _gameOverMsg = new GameSprite("game_over_box", _game, _game.screenWidth * 0.5f, _game.screenHeight - _game.screenHeight * 0.53f);
            _gameOverMsg.visible = false;
            elements.add(_gameOverMsg);

            _newLevelMsg = new GameSprite("new_level_box", _game, _game.screenWidth * 0.5f, _game.screenHeight - _game.screenHeight * 0.53f);
            _newLevelMsg.visible = false;
            elements.add(_newLevelMsg);

            _levelTimeMsg = new TimeMsg(_game, _game.screenWidth * 0.5f, _game.screenHeight - _game.screenHeight * 0.53f);
            _levelTimeMsg.visible = false;

        } else {

            _timeBar.reset();
            _player.reset();
            _bonusFrog.reset();
            _score.reset();
            _level.reset();
            _game.gameData.reset();
            _lives.show();

            for (int i = 0; i < _tiers.size(); i++) {
                _tiers.get(i).reset();
            }
        }

        _game.gameData.gameMode = Game.GAME_STATE_PLAY;
    }


    @Override
    public void update(float dt) {

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //check for input
        if (Gdx.input.justTouched()) {
            if (_gameOverMsg.visible) {
                _gameOverMsg.visible = false;
                _game.setScreen("MenuScreen");
            } else {

                if (_game.gameData.gameMode == Game.GAME_STATE_PAUSE) return;

                //test for touch on Controls
                if (!_player.moving && _player.visible && _game.gameData.gameMode == Game.GAME_STATE_PLAY) {
                    _game.camera.unproject(_touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

                    if (_sound.bounds().contains(_touchPoint.x, _touchPoint.y) || _mute.bounds().contains(_touchPoint.x, _touchPoint.y)) {
                        _game.gameData.sound = !_game.gameData.sound;
                        _sound.visible = _game.gameData.sound;
                        _mute.visible = !_game.gameData.sound;
                    } else {
//					if (_controlBounds.contains(_touchPoint.x, _touchPoint.y)) {
//						switch (_controls.getDirection(_touchPoint)) {
//							case Player.MOVE_TOP:
//								_player.moveFrogUp();
//								break;
//							case Player.MOVE_DOWN:
//								_player.moveFrogDown();
//								break;
//							case Player.MOVE_LEFT:
//								_player.moveFrogLeft();
//								break;
//							case Player.MOVE_RIGHT:
//								_player.moveFrogRight();
//								break;
//						}
//					} else {
                        // SH touch controls
                        float deltaX = _touchPoint.x - (_player.x + _player.width * 0.5f);
                        float deltaY = _touchPoint.y - (_player.y + _player.height * 0.5f);

                        if (Math.abs(deltaX) < _player.width * 0.5) deltaX = 0;
                        if (Math.abs(deltaY) < _player.height * 0.5) deltaY = 0;

                        if (deltaX < 0 && Math.abs(deltaX) > Math.abs(deltaY)) {
                            _player.moveFrogLeft();
                        }
                        if (deltaX > 0 && Math.abs(deltaX) > Math.abs(deltaY)) {
                            _player.moveFrogRight();
                        }
                        if (deltaY > 0 && Math.abs(deltaY) > Math.abs(deltaX)) {
                            _player.moveFrogUp();
                        }
                        if (deltaY < 0 && Math.abs(deltaY) > Math.abs(deltaX)) {
                            _player.moveFrogDown();
                        }
                        //	}
                    }
                }
            }
        }

        // key input
        if (!_player.moving && _player.visible && _game.gameData.gameMode == Game.GAME_STATE_PLAY) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                _player.moveFrogLeft();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                _player.moveFrogRight();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                _player.moveFrogUp();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                _player.moveFrogDown();
            }
        }


        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //update elements!
        _player.update(dt);
        _player.place();
        _bonusFrog.update(dt);
        _bonusFrog.place();

        int i;
        int len = _tiers.size();
        //update all tiers
        for (i = 0; i < len; i++) {
            _tiers.get(i).update(dt);
        }

        //check for collisions!
        if (_player.active) {
            //check collision of frog and tier sprites
            if (_tiers.get(_player.tierIndex).checkCollision(_player)) {
                //if tiers with vehicles, and colliding with vehicle
                if (_player.tierIndex < 6) {
                    if (_game.gameData.sound)
                        Sounds.play(Sounds.hit);
                    //if not colliding with anything in the water tiers, drown frog
                } else {
                    if (_game.gameData.sound)
                        Sounds.play(Sounds.splash);
                }
                //kill player
                _player.kill();
                _game.gameData.lives--;
            }
            //check collision of frog and bonus frog
            //if bonus frog is visible and not on frog
            if (_bonusFrog.visible) {
                if (_bonusFrog.bounds().overlaps(_player.bounds())) {
                    _player.hasBonus = true;
                }
            }
        } else {
            if (_player.hasBonus) {
                _bonusFrog.visible = false;
                _player.hasBonus = false;
            }
        }

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //render all elements
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _game.camera.update();


        _game.spriteBatch.setProjectionMatrix(_game.camera.combined);
        _game.spriteBatch.enableBlending();
        _game.spriteBatch.begin();

        len = elements.size();
        GameSprite element;
        for (i = 0; i < len; i++) {
            element = elements.get(i);
            if (!element.visible) continue;
            if (element.skin == null) {
                element.draw();
            } else {
                _game.spriteBatch.draw(element.skin, element.x, element.y);
            }
        }
        _game.spriteBatch.end();
    }

    public void gameOver() {
        _gameOverMsg.visible = true;
        _game.gameData.gameMode = Game.GAME_STATE_PAUSE;
    }

    public void targetReached() {

        //show the time needed to reach this target
        _levelTimeMsg.timeLabel.value = _timeBar.seconds;
        _levelTimeMsg.show();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                _levelTimeMsg.hide();
                timer.cancel();
            }
        }, 3000, 1000);

        _player.reset();
        _timeBar.seconds = 0;

    }

    //start new level
    public void newLevel() {

        _game.gameData.gameMode = Game.GAME_STATE_PAUSE;
        //increase the speeds in the tiers
        _game.gameData.tierSpeed1 += 0.1;
        _game.gameData.tierSpeed2 += 0.2;
        _game.gameData.level++;

        for (int i = 0; i < _tiers.size(); i++) {
            _tiers.get(i).refresh();
        }

        _timeBar.reset();
        _game.gameData.gameMode = Game.GAME_STATE_PLAY;

    }

}