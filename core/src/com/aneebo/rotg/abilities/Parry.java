package com.aneebo.rotg.abilities;

import com.aneebo.rotg.abilities.util.Target;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Parry extends Ability {

	private ComponentMapper<StatComponent> sc = ComponentMapper.getFor(StatComponent.class);
	private ComponentMapper<AbilityComponent> ac = ComponentMapper.getFor(AbilityComponent.class);
	private ComponentMapper<PositionComponent> pc = ComponentMapper.getFor(PositionComponent.class);
	private StatComponent stat;
	private AbilityComponent ab;
	private PositionComponent pos;
	private Entity e;
	
	private Vector2 abilityDst;
	
	public Parry(int id, int castTime, int range, AbilityType type, String name, int cooldown, float damage, float energy_cost) {
		super(id, castTime, range, type, name, cooldown, damage, energy_cost);
		
		abilityDst = new Vector2();
	}

	
	public Parry(Ability ability) {
		this(ability.getId(), ability.getCastTime(), ability.getRange(), ability.getType(), ability.getName(), ability.getCooldown(), ability.getDamage(), ability.getEnergy_cost());
	}

	@Override
	protected void onAbilityEnd(Entity me) {}


	@Override
	protected void onAbilityStart(Entity me) {
		stat = sc.get(me);
		if(stat.energy - energy_cost < 0) {
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
			ab = ac.get(e);
			for(Ability a : ab.abilitySlots) {
				if(a.isActivated) {
					stat.eValue = a.interrupt();
					a.onAbilityEnd(e);
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

}
