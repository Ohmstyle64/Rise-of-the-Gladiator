package com.aneebo.rotg.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static final AssetManager assetManager = new AssetManager();
	
	public static void load() {
		assetManager.load(Constants.DRAGON_FORM, Texture.class);
		assetManager.load(Constants.PIG_FORM, Texture.class);
		assetManager.load(Constants.FIREBALL_EFFECT, Texture.class);
		assetManager.load(Constants.ICE_FORM, Texture.class);
		assetManager.load(Constants.EMPTY_CELL, Texture.class);
		assetManager.load(Constants.PLATE_MAIL_1, Texture.class);
		assetManager.load(Constants.HELMET_1, Texture.class);
		assetManager.load(Constants.ELVEN_SCALEMAIL_1, Texture.class);
		assetManager.load(Constants.SHORTSWORD_1, Texture.class);
		assetManager.load(Constants.SPEAR_1, Texture.class);
		assetManager.load(Constants.BODY_PLAYER, Texture.class);
		assetManager.load(Constants.BODY_SKELETON, Texture.class);
		assetManager.finishLoading();
	}
	
	public static void dispose() {
		assetManager.dispose();
	}
}
