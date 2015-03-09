package com.aneebo.rotg.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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
		assetManager.load(Constants.TEST_GET_HIT, Sound.class);
		assetManager.load(Constants.TEST_MUSIC, Music.class);
		assetManager.load(Constants.UI_SKIN, Skin.class, new SkinLoader.SkinParameter(Constants.UI_ATLAS));
		assetManager.load(Constants.FONT, BitmapFont.class);
		assetManager.load(Constants.CASTTIMER, Texture.class);
		assetManager.load(Constants.HEALTH_ATLAS, TextureAtlas.class);
		
		assetManager.finishLoading();
	}
	
	public static void dispose() {
		assetManager.dispose();
	}
}
