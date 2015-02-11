package com.aneebo.rotg.abilities.range;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

public abstract class RangeAbility extends Ability {

	protected Engine engine;
	protected String textureName;
	
	public RangeAbility(int id, int castTime, int range, AbilityType type,
			String name, int cooldown,float damage, float energy_cost, String textureName, Engine engine) {
		super(id, castTime, range, type, name, cooldown, damage, energy_cost);
		this.engine = engine;
		this.textureName = textureName;
	}
	
	public RangeAbility(RangeAbility rangeAbility, Engine engine) {
		super(rangeAbility);
		this.textureName = rangeAbility.textureName;
		this.engine = engine;
	}
	
	public abstract void hit(Entity from, Entity hit);

	protected String getTexture() {
		return textureName;
	}
	
	public Engine getEngine() {
		return engine;
	}
	
}
