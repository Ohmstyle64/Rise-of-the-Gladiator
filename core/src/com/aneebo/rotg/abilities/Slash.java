package com.aneebo.rotg.abilities;

import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityType;
import com.aneebo.rotg.utils.Target;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Slash extends Ability {
	
	private ComponentMapper<StatComponent> sc = ComponentMapper.getFor(StatComponent.class);
	private ComponentMapper<PositionComponent> pc = ComponentMapper.getFor(PositionComponent.class);
	private StatComponent stat;
	private PositionComponent pos;
	
	private Vector2 abilityDst;
	
	private static final float DAMAGE = 10f;
	private static final float ENERGY_COST = 10f;
	
	public Slash(int id, int castTime, int range, AbilityType type, String name, int cooldown) {
		super(id, castTime, range, type, name, cooldown);
		abilityDst = new Vector2();
	}
	
	public Slash(Ability ability) {
		this(ability.getId(), ability.getCastTime(), ability.getRange(), ability.getType(), ability.getName(), ability.getCooldown());
	}

	@Override
	protected void onAbilityEnd(Entity me) {
		for(Entity e : targets) {
			stat = sc.get(e);
			stat.health -= (1-(sc.get(e).eValue+sc.get(me).eValue))*DAMAGE;
			sc.get(me).eValue = 0;
		}
	}

	@Override
	protected void onAbilityStart(Entity me) {
		stat = sc.get(me);
		if(stat.energy - ENERGY_COST < 0) {
			Gdx.app.log(stat.name, "Not enough energy!");
			stat.energy = 0;
			isInterrupted = true;
			return;
		}
		stat.energy -= ENERGY_COST;
	}

	@Override
	public Array<Entity> getTargets(Entity me, Entity[] allEnemies) {
		
		targets.clear();
		targets.addAll(Target.oneDirectional(me, allEnemies, getRange()));
		
		return targets;
	}
}
