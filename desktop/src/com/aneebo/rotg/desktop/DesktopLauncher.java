package com.aneebo.rotg.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.aneebo.rotg.RotG;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.x = -1;
		config.y = -1;
		config.width = 800;
		config.height = 480;
		config.resizable = false;
		config.title = "RISE OF THE GLADIATOR";
		config.addIcon("img/characters/statue_form.png", FileType.Internal);
		new LwjglApplication(new RotG(), config);
	}
}
