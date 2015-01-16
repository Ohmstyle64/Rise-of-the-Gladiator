package com.aneebo.rotg.abilities;

import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class Slash extends Ability {
	
	private ComponentMapper<StatComponent> sc = ComponentMapper.getFor(StatComponent.class);
	private StatComponent stat;
	
	private static final float DAMAGE = 10f;
	private static final float ENERGY_COST = 10f;
	
	public Slash(int id, int castTime, int range, AbilityType type, String name, int cooldown) {
		super(id, castTime, range, type, name, cooldown);

	}
	
	public Slash(Ability ability) {
		this(ability.getId(), ability.getCastTime(), ability.getRange(), ability.getType(), ability.getName(), ability.getCooldown());
	}

	@Override
	public void render(Batch batch) {
		super.render(batch);
		if(castTimeTimer >=castTime) {
			//DO ABILITY EFFECT
		}
	}

	@Override
	protected void onAbilityEnd(Entity me) {
		for(Entity e : targets) {
			stat = sc.get(e);
			stat.health -= (sc.get(me).eValue+1)*DAMAGE;
			sc.get(me).eValue = 0;
		}
		
		stat = sc.get(me);
		Gdx.app.log(stat.name+" activates ability ",getName());
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
	public void setTargetScheme(Entity me) {
		
	}

	@Override
	public Array<Entity> getTargets(Entity me) {
		return null;
	}
}
