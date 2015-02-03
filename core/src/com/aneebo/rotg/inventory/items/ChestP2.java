package com.aneebo.rotg.inventory.items;

import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.graphics.Texture;

public class ChestP2 extends Item {

	public ChestP2() {
		super("ChestP2", Assets.assetManager.get(Constants.ELVEN_SCALEMAIL_1, Texture.class), Constants.BODY);
	}

	@Override
	public float getDamageMitigation() {
		return 0.12f;
	}

	@Override
	public float getMagicResist() {
		return 0.35f;
	}

	@Override
	public float getIncreaseToRange() {
		return 0;
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
