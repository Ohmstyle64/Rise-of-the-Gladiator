package com.aneebo.rotg.inventory.items;

import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.graphics.Texture;

public class ChestP2 extends Item {

	public ChestP2() {
		super("ChestP2", Assets.assetManager.get(Constants.ELVEN_SCALEMAIL_1, Texture.class), Constants.BODY);
	}

}
