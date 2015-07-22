package com.aneebo.rotg.abilities.offense;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.upgrades.Upgrade;
import com.aneebo.rotg.abilities.util.Target;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityNameType;
import com.aneebo.rotg.types.AbilityType;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

public class Blade_Strike extends Ability {

	private StatComponent statComponent;
	private PositionComponent positionComponent;
	private AbilityComponent abilityComponent;
	private Sound hit;
	
	public Blade_Strike(AbilityNameType nameType, float castTime,
			float range, AbilityType type, String name, float cooldown,
			float damage, float energy_cost, String textureName, Engine engine,
			Array<Upgrade> upgrades) {
		super(nameType, castTime, range, type, name, cooldown, damage, energy_cost,
				textureName, engine, upgrades);
	}

	public Blade_Strike(Ability ability, Engine engine) {
		super(ability, engine);
	}

	@Override
	protected void onAbilityEnd(Entity me) {
		for(Entity e : targets) {
			statComponent = Mappers.staMap.get(e);
			positionComponent = Mappers.posMap.get(e);
			abilityComponent = Mappers.abMap.get(e);
			
			float damageDealt = ((1+Mappers.staMap.get(me).eValue)*damage+Mappers.staMap.get(me).increaseToDamage)*(1-statComponent.damageMitigation);
			statComponent.health -= damageDealt;
			abilityComponent.ftm.addMessages(-damageDealt+"", positionComponent.curXPos*Constants.TILE_WIDTH, (positionComponent.curYPos+1)*Constants.TILE_HEIGHT);
			Gdx.app.log(Mappers.staMap.get(me).name," has dealt "+damageDealt+" to "+statComponent.name);
			Mappers.staMap.get(me).eValue = 0;
			if(statComponent.isPlayer) {
				hit = Assets.assetManager.get(Constants.TEST_GET_HIT, Sound.class);
				hit.play();
				Gdx.input.vibrate(1000);
				Mappers.renMap.get(e).shake = true;
			} else {
				Mappers.renMap.get(e).flashRed(10);
			}
		}
	}

	@Override
	protected void onAbilityStart(Entity me) {
		statComponent = Mappers.staMap.get(me);
		statComponent.energy -= energy_cost;
		if(statComponent.energy < 0) {
			positionComponent = Mappers.posMap.get(me);
			abilityComponent = Mappers.abMap.get(me);
			abilityComponent.ftm.addMessages("Not enough energy!", positionComponent.curXPos*Constants.TILE_WIDTH, (positionComponent.curYPos+1)*Constants.TILE_HEIGHT);
			Gdx.app.log(statComponent.name, "Not enough energy!");
			statComponent.energy = 0;
			isInterrupted = true;
			return;
		}
		statComponent.energy -= energy_cost;
	}

	@Override
	public Array<Entity> getTargets(Entity me, Entity[] allEnemies) {
		
		targets.clear();
		targets.addAll(Target.oneDirectional(me, allEnemies, getRange()));
		
		return targets;
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
