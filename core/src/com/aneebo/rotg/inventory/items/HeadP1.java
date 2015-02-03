package com.aneebo.rotg.inventory.items;

import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.graphics.Texture;

public class HeadP1 extends Item {

	public HeadP1() {
		super("HeadP1", Assets.assetManager.get(Constants.HELMET_1, Texture.class), Constants.HEAD);
	}

	@Override
	public float getDamageMitigation() {
		return 0.05f;
	}

	@Override
	public float getMagicResist() {
		return 00.07f;
	}

	@Override
	public float getIncreaseToRange() {
		return 2.0f;
	}

	@Override
	public float getAttackSpeed() {
		return 0;
	}

	@Override
	public float getDamage() {
		return 0;
	}

}
