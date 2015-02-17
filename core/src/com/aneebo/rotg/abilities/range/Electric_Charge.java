package com.aneebo.rotg.abilities.range;

import com.aneebo.rotg.types.AbilityType;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public class Electric_Charge extends RangeAbility {

	public Electric_Charge(int id, int castTime, int range, AbilityType type,
			String name, int cooldown, float damage, float energy_cost,
			String textureName, Engine engine) {
		super(id, castTime, range, type, name, cooldown, damage, energy_cost,
				textureName, engine);
		// TODO Auto-generated constructor stub
	}
	
	public Electric_Charge(RangeAbility rangeAbility, Engine engine) {
		super(rangeAbility, engine);
	}

	@Override
	public void hit(Entity from, Entity hit) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onAbilityStart(Entity me) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onAbilityEnd(Entity me) {
		// TODO Auto-generated method stub

	}

	@Override
	public Array<Entity> getTargets(Entity me, Entity[] allEnemies) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void activateTier1() {
		// TODO Auto-generated method stub

	}

	@Override
	public void activateTier2() {
		// TODO Auto-generated method stub

	}

	@Override
	public void activateTier3() {
		// TODO Auto-generated method stub

	}

}
