package com.Poker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.Poker.Poker;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "POKERACTIVE";
		config.height = 1080;
		config.width = 1920;
		new LwjglApplication(new Poker(), config);
	}
}
