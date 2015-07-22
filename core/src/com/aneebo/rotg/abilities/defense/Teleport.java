package com.aneebo.rotg.abilities.defense;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.upgrades.Upgrade;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityNameType;
import com.aneebo.rotg.types.AbilityType;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Teleport extends Ability {

	private PositionComponent posMe;
	private StatComponent stat;
	
	private Array<Entity> targets;
	
	public Teleport(AbilityNameType nameType, float castTime,
			float range, AbilityType type, String name, float cooldown,
			float damage, float energy_cost, String textureName, Engine engine,
			Array<Upgrade> upgrades) {
		super(nameType, castTime, range, type, name, cooldown, damage, energy_cost,
				textureName, engine, upgrades);
	}
	
	public Teleport(Ability ability, Engine engine) {
		super(ability, engine);
		
		targets = new Array<Entity>();
	}



	@Override
	public void hit(ProjectileComponent proj, Entity from, Entity hit) {
		// TODO Auto-generated method stub
		
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
		
		boolean emptySpot = false;
		int randomX = MathUtils.random(Constants.FIRST_COL, Constants.LAST_COL);
		int randomY = MathUtils.random(Constants.FIRST_ROW, Constants.LAST_ROW);
		int size = targets.size;
		
		while(!emptySpot) {
			for(int i = 0; i < size; i++) {
				posMe = Mappers.posMap.get(targets.get(i));
				if(randomX != (int)posMe.gridCurXPos && randomY != (int)posMe.gridCurYPos) {
					emptySpot = true;
				}else {
					emptySpot = false;
					randomX = MathUtils.random(Constants.FIRST_COL, Constants.LAST_COL);
					randomY = MathUtils.random(Constants.FIRST_ROW, Constants.LAST_ROW);
					break;
				}
			}
		}
		posMe = Mappers.posMap.get(me);
		posMe.setCurPos(randomX, randomY);
	}

	@Override
	public Array<Entity> getTargets(Entity me, Entity[] allEnemies) {
		targets.clear();
		targets.addAll(allEnemies);
		return targets;
	}

	@Override
	protected void abilityActing(Entity me) {
		// TODO Auto-generated method stub
		
	}


}
