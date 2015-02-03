package com.aneebo.rotg.inventory.items;

import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.graphics.Texture;

public class PrimP2 extends Item {

	public PrimP2() {
		super("PrimP2", Assets.assetManager.get(Constants.SPEAR_1, Texture.class), Constants.PRIMARY);
	}

	@Override
	public float getDamageMitigation() {
		return 0;
	}

	@Override
	public float getMagicResist() {
		return 0;
	}

	@Override
	public float getIncreaseToRange() {
		return 1f;
	}

	@Override
	public float getAttackSpeed() {
		return 0.1f;
	}

	@Override
	public float getDamage() {
		return 3f;
	}

}
