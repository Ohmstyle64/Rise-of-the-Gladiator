package com.aneebo.rotg.abilities;

import com.aneebo.rotg.types.AbilityType;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public abstract class RangeAbility extends Ability {

	protected Engine engine;
	protected Texture texture;
	
	public RangeAbility(int id, int castTime, int range, AbilityType type,
			String name, int cooldown, Texture texture, Engine engine) {
		super(id, castTime, range, type, name, cooldown);
		this.engine = engine;
		this.texture = texture;
	}
	
	protected abstract void onAbilityStart(Entity me);
	
	protected abstract void onAbilityEnd(Entity me);

	public abstract Array<Entity> getTargets(Entity me, Entity[] entities);

	protected Texture getTexture() {
		return texture;
	}
	
}
