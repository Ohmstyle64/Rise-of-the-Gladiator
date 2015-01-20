package com.aneebo.rotg.abilities;

import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityType;
import com.aneebo.rotg.utils.Target;
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
	//TODO: This needs to be a field in the constructor
	private static final float ENERGY_COST = 20f;
	
	public Parry(int id, int castTime, int range, AbilityType type, String name, int cooldown) {
		super(id, castTime, range, type, name, cooldown);
		
		abilityDst = new Vector2();
	}

	
	public Parry(Ability ability) {
		this(ability.getId(), ability.getCastTime(), ability.getRange(), ability.getType(), ability.getName(), ability.getCooldown());
	}

	@Override
	protected void onAbilityEnd(Entity me) {
		stat = sc.get(me);
		Gdx.app.log(stat.name+" activates ability ",getName());
	}


	@Override
	protected void onAbilityStart(Entity me) {
		stat = sc.get(me);
		if(stat.energy - ENERGY_COST < 0) {
			//TODO:This needs to be an alert in the GUI
			Gdx.app.log(stat.name, "Not enough energy!");
			stat.energy = 0;
			isInterrupted = true;
			return;
		}
		stat.energy -= ENERGY_COST;
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

}
