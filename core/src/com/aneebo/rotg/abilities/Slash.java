package com.aneebo.rotg.abilities;

import com.aneebo.rotg.types.AbilityType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Slash extends Ability {
	
	public Slash(int id, int castTime, int range, AbilityType type, String name, int cooldown) {
		super(id, castTime, range, type, name, cooldown);

	}
	
	public Slash(Ability ability) {
		this(ability.getId(), ability.getCastTime(), ability.getRange(), ability.getType(), ability.getName(), ability.getCooldown());
	}

	@Override
	public void render(Batch batch) {
		super.render(batch);
		if(castTimeTimer >=castTime) {
			//DO ABILITY EFFECT
		}
	}

	@Override
	protected void onAbilityEnd() {
		Gdx.app.log("Ability",getName());
	}

}
