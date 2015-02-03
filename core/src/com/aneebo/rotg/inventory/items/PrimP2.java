package com.aneebo.rotg.inventory.items;

import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.graphics.Texture;

public class PrimP2 extends Item {

	public PrimP2() {
		super("PrimP2", Assets.assetManager.get(Constants.SPEAR_1, Texture.class), Constants.PRIMARY);
	}

}
