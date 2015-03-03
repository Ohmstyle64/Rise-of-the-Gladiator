package com.aneebo.rotg.ai;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityNameType;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class BasicRangeAI extends AiPlan {
	
	private enum AIState {
		idle, chase, fight
	}
	
	private static final float PROXIMITY = 3f;
	private static final float TICK = 6f;
	private static final float FAIL = 0.1f;
	private float timer;
	
	private PositionComponent playerPos;
	private PositionComponent mePos;
	private AbilityComponent abComponent;
	private StatComponent statComponent;
	private Ability ab;
	private AIState aiState;

	public BasicRangeAI(Entity me, Engine engine) {
		super(me, engine);
		
		aiState = AIState.idle;
		timer = 0;
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
		aiState = AIState.fight;
	}

	private void fight() {
		playerPos = Mappers.posMap.get(player);
		mePos = Mappers.posMap.get(me);
		int yDiff = (int) (playerPos.curYPos - mePos.curYPos);
		int xDiff = (int) (playerPos.curXPos - mePos.curXPos);
		
		if(Math.sqrt(yDiff*yDiff + xDiff*xDiff) <= PROXIMITY) {
			aiState = AIState.chase;
			timer = 0;
			return;
		}
		
		statComponent = Mappers.staMap.get(me);
		abComponent = Mappers.abMap.get(me);
		
		correctFacing(mePos, playerPos);
		
		timer += Gdx.graphics.getDeltaTime();
		if(timer >= TICK) {
			timer = 0;
			if(MathUtils.randomBoolean(FAIL)) {
				Gdx.app.log(statComponent.name+" : ", "Ability failed!");
				return;
			}
		} else 
			return;

		ab = abComponent.abilityMap.get(AbilityNameType.AT_WAVE_OF_FIRE);
		
		if(statComponent.energy >= ab.getEnergy_cost()) {
			if(inAbilityRange(abComponent, AbilityNameType.AT_WAVE_OF_FIRE)) {
				ab.isActivated = true;
				return;
			}
		}
	}

	private void chase() {
		abComponent = Mappers.abMap.get(me);
		statComponent = Mappers.staMap.get(me);
		ab = abComponent.abilityMap.get(AbilityNameType.DF_TELEPORT);
		

		if(statComponent.energy >= ab.getEnergy_cost()) {
			if(inAbilityRange(abComponent, AbilityNameType.DF_TELEPORT)) {
				ab.isActivated = true;
				aiState = AIState.fight;
				return;
			}
		}
	}
	

}
