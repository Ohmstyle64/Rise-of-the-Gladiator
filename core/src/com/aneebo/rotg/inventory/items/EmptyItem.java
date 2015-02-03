package com.aneebo.rotg.inventory.items;

import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.graphics.Texture;

public class EmptyItem extends Item {

	public EmptyItem() {
		super("Empty", Assets.assetManager.get(Constants.EMPTY_CELL, Texture.class), Constants.BLANK);
	}
	
	public EmptyItem(int location) {
		super("Empty", Assets.assetManager.get(Constants.EMPTY_CELL, Texture.class), location);
	}

}
