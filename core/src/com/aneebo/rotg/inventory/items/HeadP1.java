package com.aneebo.rotg.inventory.items;

import com.aneebo.rotg.inventory.Inventory;
import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.graphics.Texture;

public class HeadP1 extends Item {

	public HeadP1() {
		super("HeadP1", Assets.assetManager.get(Constants.FIREBALL_EFFECT, Texture.class), Constants.HEAD);
	}

}
