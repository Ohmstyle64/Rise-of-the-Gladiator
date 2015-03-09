package com.aneebo.rotg.abilities.defense;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.upgrades.Upgrade;
import com.aneebo.rotg.abilities.util.Target;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityNameType;
import com.aneebo.rotg.types.AbilityType;
import com.aneebo.rotg.ui.FloatingTextManager;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class Blade_Block extends Ability {

	private StatComponent stat;
	private AbilityComponent ab;
	private Entity e;
	

	public Blade_Block(AbilityNameType nameType, float castTime,
			float range, AbilityType type, String name, float cooldown,
			float damage, float energy_cost, String textureName, Engine engine,
			Array<Upgrade> upgrades) {
		super(nameType, castTime, range, type, name, cooldown, damage, energy_cost,
				textureName, engine, upgrades);
	}

	public Blade_Block(Ability ability, Engine engine) {
		super(ability, engine);
	}

	

	@Override
	protected void onAbilityEnd(Entity me) {
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
		int size = targets.size;
		for(int i = 0; i < size; i++) {
			e = targets.get(i);
			ab = Mappers.abMap.get(e);
			for(Ability a : ab.abilitySlots) {
				if(a.isActivated && a.getType() == AbilityType.melee_offensive) {
					stat.eValue = a.interrupt();
					isInterrupted = true;
					break;
				}
			}
		}
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
