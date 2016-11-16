package de.nitri.frogger.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteCache;

import de.nitri.frogger.Game;
import de.nitri.frogger.sprites.GameSprite;


public class MenuScreen extends Screen {

    private SpriteCache _spriteCache;
    private int _spriteCacheIndex;

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void createScreen() {

        if (elements.size() == 0) {

            GameSprite logo = new GameSprite("logo", _game, _game.screenWidth * 0.5f, _game.screenHeight * 0.9f);
            GameSprite label1 = null;
            GameSprite label2 = null;
            GameSprite label3 = null;
            GameSprite label4 = null;
            switch (Gdx.app.getType()) {
                case Android:
                    label1 = new GameSprite("label_how_to", _game, _game.screenWidth * 0.5f, _game.screenHeight * 0.53f);
                    label2 = new GameSprite("label_instructions", _game, _game.screenWidth * 0.5f, _game.screenHeight * 0.45f);
                    label3 = new GameSprite("label_tap", _game, _game.screenWidth * 0.5f, _game.screenHeight * 0.25f);
                    break;
                case Desktop:
                    label4 = new GameSprite("label_click", _game, _game.screenWidth * 0.5f, _game.screenHeight * 0.25f);
                    break;
            }
            GameSprite frog = new GameSprite("frogger_frog", _game, _game.screenWidth * 0.5f, _game.screenHeight * 0.7f);

			
			
			/*
            //OPTION 1: With SpriteBatch
			elements.add(logo);
			elements.add(label1);
			elements.add(label2);
			elements.add(label3);
			elements.add(control);
			*/

            //OPTION 2: With SpriteCache
            _spriteCache = new SpriteCache();
            _spriteCache.beginCache();
            _spriteCache.add(logo.skin, logo.x, logo.y);

            _spriteCache.add(frog.skin, frog.x, frog.y);
            switch (Gdx.app.getType()) {
                case Android:
                    if (null != label1)
                        _spriteCache.add(label1.skin, label1.x, label1.y);
                    if (null != label2)
                        _spriteCache.add(label2.skin, label2.x, label2.y);
                    if (null != label3)
                        _spriteCache.add(label3.skin, label3.x, label3.y);
                    break;
                case Desktop:
                    if (null != label4)
                        _spriteCache.add(label4.skin, label4.x, label4.y);
                    break;
            }
            _spriteCacheIndex = _spriteCache.endCache();

        }

    }

    @Override
    public void update(float dt) {


        if (Gdx.input.justTouched()) {
            //Gdx.app.log("A HIT!", "A MOST PALPABLE HIT");
            _game.setScreen("GameScreen");

        } else {
            GL20 gl = Gdx.gl;
            gl.glClearColor(0.0f, 0.0f, 0.239f, 1);
            gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            _game.camera.update();

			/*
			//OPTION 1: With SpriteBatch
			_game.spriteBatch.setProjectionMatrix(_game.camera.combined);
			_game.spriteBatch.enableBlending();
			_game.spriteBatch.begin();
			
			int len = elements.size();
			GameSprite element;
			for (int i = 0; i < len; i++) {
				element = elements.get(i);
				_game.spriteBatch.draw(element.skin, element.x, element.y);
			}
			_game.spriteBatch.end();
			*/

            //OPTION 2: With SpriteCache
            gl.glEnable(GL20.GL_BLEND);
            gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            _spriteCache.setProjectionMatrix(_game.camera.combined);
            _spriteCache.begin();
            _spriteCache.draw(_spriteCacheIndex);
            _spriteCache.end();

        }

    }

}