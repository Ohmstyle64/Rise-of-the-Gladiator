package com.aneebo.rotg.abilities.offense;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.upgrades.Upgrade;
import com.aneebo.rotg.abilities.util.Target;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class Blade_Strike extends Ability {

	private StatComponent stat;
	
	public Blade_Strike(int id, int castTime, float range, AbilityType type,
			String name, int cooldown, float damage, float energy_cost,
			String textureName, Engine engine, Array<Upgrade> upgrades) {
		super(id, castTime, range, type, name, cooldown, damage, energy_cost,
				textureName, engine, upgrades);
		// TODO Auto-generated constructor stub
	}

	public Blade_Strike(Ability ability, Engine engine) {
		super(ability, engine);
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
		stat.energy -= energy_cost;
		if(stat.energy < 0) {
			//TODO:This needs to be an alert in the GUI
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



	@Override
	public void hit(ProjectileComponent proj, Entity from, Entity hit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void abilityActing(Entity me) {
		// TODO Auto-generated method stub
		
	}
}
