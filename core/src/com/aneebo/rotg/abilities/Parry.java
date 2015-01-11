package com.aneebo.rotg.abilities;

import com.aneebo.rotg.types.AbilityType;
import com.badlogic.gdx.Gdx;

public class Parry extends Ability {

	public Parry(int id, int castTime, int range, AbilityType type, String name, int cooldown) {
		super(id, castTime, range, type, name, cooldown);
	}

	
	public Parry(Ability ability) {
		this(ability.getId(), ability.getCastTime(), ability.getRange(), ability.getType(), ability.getName(), ability.getCooldown());
	}


	@Override
	protected void onAbilityEnd() {
		Gdx.app.log("Ability",getName());
		
	}
}
