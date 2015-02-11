package com.aneebo.rotg.ai;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

public class RangeTeleportBasicE1 extends AiPlan {
	
	private enum AIState {
		idle, chase, fight
	}
	
	private PositionComponent playerPos;
	private PositionComponent mePos;
	private AbilityComponent abComponent;
	private StatComponent statComponent;
	private Ability ab;
	private AIState aiState;

	public RangeTeleportBasicE1(Entity me, Engine engine) {
		super(me, engine);
		
		aiState = AIState.idle;
	}

	@Override
	public void update(float deltaTime) {
		
		switch(aiState) {
		case chase:
			chase();
			break;
		case fight:
			fight();
			break;
		case idle:
			idle();
			break;
		}
	}

	private void idle() {
		aiState = AIState.chase;
	}

	private void fight() {
		playerPos = Mappers.posMap.get(player);
		mePos = Mappers.posMap.get(me);
		
		if(playerPos.curYPos != mePos.curYPos && playerPos.curXPos != mePos.curXPos) {
			aiState = AIState.chase;
			return;
		}
		
		statComponent = Mappers.staMap.get(me);
		abComponent = Mappers.abMap.get(me);
		
		correctFacing(mePos, playerPos);
				
		ab = abComponent.abilityMap.get(Constants.AT_FIREBLAST);
		
		if(statComponent.energy >= ab.getEnergy_cost()) {
			if(inAbilityRange(abComponent, Constants.AT_FIREBLAST)) {
				ab.isActivated = true;
				return;
			}
		}
	}

	private void chase() {
		abComponent = Mappers.abMap.get(me);
		statComponent = Mappers.staMap.get(me);
		ab = abComponent.abilityMap.get(Constants.DF_TELEPORT_MOST_DST);
		

		if(statComponent.energy >= ab.getEnergy_cost()) {
			if(inAbilityRange(abComponent, Constants.DF_TELEPORT_MOST_DST)) {
				ab.isActivated = true;
				aiState = AIState.fight;
				return;
			}
		}
	}
	

}
