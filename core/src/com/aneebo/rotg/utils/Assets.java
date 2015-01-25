package com.aneebo.rotg.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
	public static final AssetManager assetManager = new AssetManager();
	
	public static void load() {
		assetManager.load(Constants.DRAGON_FORM, Texture.class);
		assetManager.load(Constants.PIG_FORM, Texture.class);
		assetManager.load(Constants.FIREBALL_EFFECT, Texture.class);
		assetManager.finishLoading();
	}
	
	public static void dispose() {
		assetManager.dispose();
	}
}
