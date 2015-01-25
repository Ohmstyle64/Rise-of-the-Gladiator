package com.aneebo.rotg.abilities.range;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public abstract class RangeAbility extends Ability {

	protected Engine engine;
	protected Texture texture;
	
	public RangeAbility(int id, int castTime, int range, AbilityType type,
			String name, int cooldown,float damage, float energy_cost, Texture texture, Engine engine) {
		super(id, castTime, range, type, name, cooldown, damage, energy_cost);
		this.engine = engine;
		this.texture = texture;
	}
	
	public abstract void hit(Entity from, Entity hit);

	protected Texture getTexture() {
		return texture;
	}
	
}
