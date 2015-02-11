package com.aneebo.rotg.abilities.range;

import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.types.AbilityType;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.Array;

public class TeleportMostDst extends RangeAbility {

	private Entity player;
	private AbilityComponent pAbilityComponent;
	private PositionComponent posMe;
	
	private Array<Entity> entities;
	
	public TeleportMostDst(int id, int castTime, int range, AbilityType type,
			String name, int cooldown, float damage, float energy_cost,
			String textureName, Engine engine) {
		super(id, castTime, range, type, name, cooldown, damage, energy_cost,
				textureName, engine);
		// TODO Auto-generated constructor stub
	}
	
	public TeleportMostDst(RangeAbility rangeAbility, Engine engine) {
		super(rangeAbility, engine);
		player = engine.getEntitiesFor(Family.getFor(InputComponent.class)).first();
		entities = new Array<Entity>(1);
		entities.add(player);
	}

	@Override
	public void hit(Entity from, Entity hit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onAbilityStart(Entity me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onAbilityEnd(Entity me) {
		PositionComponent playerPos = Mappers.posMap.get(player);
		PositionComponent mePos = Mappers.posMap.get(me);
		

		
		if(playerPos.curXPos == mePos.curXPos) {
			float dstLast = Math.abs(playerPos.curYPos - Constants.last_row + 1);
			float dstFirst = Math.abs(playerPos.curYPos - Constants.first_row - 1);
			
			if(dstLast > dstFirst) {
				mePos.curXPos = playerPos.curXPos;
				mePos.curYPos = Constants.last_row;
				mePos.nXPos = (int) mePos.curXPos;
				mePos.nYPos = (int) mePos.curYPos;
			}else {
				mePos.curXPos = playerPos.curXPos;
				mePos.curYPos = Constants.first_row;
				mePos.nXPos = (int) mePos.curXPos;
				mePos.nYPos = (int) mePos.curYPos;
			}
		}else {
			float dstLast = Math.abs(playerPos.curXPos - Constants.last_col + 1);
			float dstFirst = Math.abs(playerPos.curXPos - Constants.first_col - 1);
			
			if(dstLast > dstFirst) {
				mePos.curXPos = Constants.last_col;
				mePos.curYPos = playerPos.curYPos;
				mePos.nXPos = (int) mePos.curXPos;
				mePos.nYPos = (int) mePos.curYPos;
			}else {
				mePos.curXPos = Constants.first_col;
				mePos.curYPos = playerPos.curYPos;
				mePos.nXPos = (int) mePos.curXPos;
				mePos.nYPos = (int) mePos.curYPos;
			}
			
		}
	}

	@Override
	public Array<Entity> getTargets(Entity me, Entity[] allEnemies) {
		
		return entities;
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
