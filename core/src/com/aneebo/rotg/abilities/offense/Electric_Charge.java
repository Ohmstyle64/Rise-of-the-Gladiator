package com.aneebo.rotg.abilities.offense;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.upgrades.Upgrade;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public class Electric_Charge extends Ability {

	public Electric_Charge(int id, int castTime, float range, AbilityType type,
			String name, int cooldown, float damage, float energy_cost,
			String textureName, Engine engine, Array<Upgrade> upgrades) {
		super(id, castTime, range, type, name, cooldown, damage, energy_cost,
				textureName, engine, upgrades);
		// TODO Auto-generated constructor stub
	}
	
	public Electric_Charge(Ability ability, Engine engine) {
		super(ability, engine);
	}

	@Override
	public void hit(ProjectileComponent proj, Entity from, Entity hit) {
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

	@Override
	protected void abilityActing(Entity me) {
		// TODO Auto-generated method stub
		
	}

}
