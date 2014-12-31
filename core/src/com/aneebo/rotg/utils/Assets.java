package com.aneebo.rotg.utils;

import com.badlogic.gdx.assets.AssetManager;

public class Assets {
	public static final AssetManager assetManager = new AssetManager();
	
	public static void load() {
		
	}
	
	public static void dispose() {
		assetManager.dispose();
	}
}
