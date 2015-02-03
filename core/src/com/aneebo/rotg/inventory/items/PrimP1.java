package com.aneebo.rotg.inventory.items;

import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.graphics.Texture;

public class PrimP1 extends Item {

	public PrimP1() {
		super("PrimP1", Assets.assetManager.get(Constants.SHORTSWORD_1, Texture.class), Constants.PRIMARY);
	}

}
