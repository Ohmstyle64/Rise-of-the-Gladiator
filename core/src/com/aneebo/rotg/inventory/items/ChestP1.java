package com.aneebo.rotg.inventory.items;

import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.graphics.Texture;

public class ChestP1 extends Item {

	public ChestP1() {
		super("ChestP1", Assets.assetManager.get(Constants.PLATE_MAIL_1, Texture.class), Constants.BODY);
	}

}
