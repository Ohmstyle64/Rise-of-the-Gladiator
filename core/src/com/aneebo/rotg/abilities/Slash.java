package com.aneebo.rotg.abilities;

import com.aneebo.rotg.abilities.util.Target;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class Slash extends Ability {
	
	private StatComponent stat;
	
	public Slash(int id, int castTime, int range, AbilityType type, String name, int cooldown, float damage, float energy_cost) {
		super(id, castTime, range, type, name, cooldown, damage, energy_cost);
	}
	
	public Slash(Ability ability) {
		super(ability);
	}

	@Override
	protected void onAbilityEnd(Entity me) {
		for(Entity e : targets) {
			stat = Mappers.staMap.get(e);
			float damageDealt = ((1+Mappers.staMap.get(e).eValue)*damage+Mappers.staMap.get(me).increaseToDamage)*(1-stat.damageMitigation);
			stat.health -= damageDealt;
			Gdx.app.log(Mappers.staMap.get(me).name," has dealt "+damageDealt+" to "+stat.name);
			Mappers.staMap.get(me).eValue = 0;
		}
	}

	@Override
	protected void onAbilityStart(Entity me) {
		stat = Mappers.staMap.get(me);
		
		if(stat.energy - energy_cost < 0) {
			Gdx.app.log(stat.name, "Not enough energy!");
			stat.energy = 0;
			isInterrupted = true;
			return;
		}
		stat.energy -= energy_cost;
	}

	@Override
	public Array<Entity> getTargets(Entity me, Entity[] allEnemies) {
		
		targets.clear();
		targets.addAll(Target.oneDirectional(me, allEnemies, getRange()));
		
		return targets;
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
