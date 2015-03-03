package com.aneebo.rotg.abilities.defense;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.upgrades.Upgrade;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityNameType;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class Pure_Heart extends Ability {

	private StatComponent stat;
	private Array<Entity> targets;
	
	public Pure_Heart(AbilityNameType nameType, float castTime,
			float range, AbilityType type, String name, float cooldown,
			float damage, float energy_cost, String textureName, Engine engine,
			Array<Upgrade> upgrades) {
		super(nameType, castTime, range, type, name, cooldown, damage, energy_cost,
				textureName, engine, upgrades);
	}
	
	public Pure_Heart(Ability ability, Engine engine) {
		super(ability, engine);
		
		targets = new Array<Entity>();
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

	}

	@Override
	protected void onAbilityEnd(Entity me) {
		stat = Mappers.staMap.get(me);
		Gdx.app.log(stat.name, " is healed for "+(damage+stat.health > stat.max_health ? stat.max_health - stat.health : damage));
		stat.health += damage;
		if(stat.health > stat.max_health)
			stat.health = stat.max_health;
	}

	@Override
	public Array<Entity> getTargets(Entity me, Entity[] allEnemies) {
		targets.clear();
		targets.add(me);
		
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
