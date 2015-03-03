package com.aneebo.rotg.abilities.offense;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.upgrades.Upgrade;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.types.AbilityNameType;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public class Quick_Knife extends Ability {

	public Quick_Knife(AbilityNameType nameType, float castTime,
			float range, AbilityType type, String name, float cooldown,
			float damage, float energy_cost, String textureName, Engine engine,
			Array<Upgrade> upgrades) {
		super(nameType, castTime, range, type, name, cooldown, damage, energy_cost,
				textureName, engine, upgrades);
	}
	
	public Quick_Knife(Ability ability, Engine engine) {
		super(ability, engine);
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
	public void hit(ProjectileComponent proj, Entity from, Entity hit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void abilityActing(Entity me) {
		// TODO Auto-generated method stub
		
	}

}
