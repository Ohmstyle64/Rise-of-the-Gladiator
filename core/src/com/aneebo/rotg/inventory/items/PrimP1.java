package com.aneebo.rotg.inventory.items;

import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.graphics.Texture;

public class PrimP1 extends Item {

	public PrimP1() {
		super("PrimP1", Assets.assetManager.get(Constants.SHORTSWORD_1, Texture.class), Constants.PRIMARY);
	}

	@Override
	public float getDamageMitigation() {
		return 0;
	}

	@Override
	public float getMagicResist() {
		return 0.05f;
	}

	@Override
	public float getIncreaseToRange() {
		return 0;
	}

	@Override
	public float getAttackSpeed() {
		return 0.2f;
	}

	@Override
	public float getDamage() {
		return 2.0f;
	}

}
