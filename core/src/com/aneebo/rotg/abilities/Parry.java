package com.aneebo.rotg.abilities;

import com.aneebo.rotg.types.AbilityType;
import com.badlogic.gdx.Gdx;

public class Parry extends Ability {

	public Parry(int id, int castTime, int range, AbilityType type, String name) {
		super(id, castTime, range, type, name);
	}

	
	@Override
	protected void onLoopStart(float delta) {
		super.onLoopStart(delta);
	}
	
	@Override
	protected void onLoopEnd() {
		super.onLoopEnd();
		Gdx.app.log("Ability",getName());
	}
}
