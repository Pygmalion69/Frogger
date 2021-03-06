package de.nitri.frogger.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import de.nitri.frogger.FroggerGame;

public class DesktopLauncher {
	public static void main (String[] arg) {

		boolean rebuildAtlas = false;

		if (rebuildAtlas) {
			TexturePacker.Settings settings = new TexturePacker.Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.pot = false;
			settings.edgePadding = false;
			settings.duplicatePadding = true;
			settings.debug = false;
			TexturePacker.process(settings, "assets-raw/images", "images", "frogger.atlas");
		}


		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 320;
		config.height = 480;
		config.resizable = false;
		new LwjglApplication(new FroggerGame(), config);
	}
}
