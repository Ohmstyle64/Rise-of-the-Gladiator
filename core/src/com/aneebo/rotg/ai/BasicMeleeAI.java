package com.aneebo.rotg.ai;

import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityNameType;
import com.aneebo.rotg.utils.Astar;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.IntArray;

public class BasicMeleeAI extends AiPlan {
	
	private enum AIState {
		idle,chase,fight,flee
	}
	
	private static final float TICK = 1.5f;
	private static final float FAIL = 0.1f;
	private float timer;
	
	private AIState aiState;
	
	private AbilityComponent eAbilityComponent;
	private PositionComponent playerPos;
	private PositionComponent enemPos;
	private StatComponent statComponent;
	private Entity e;


	private Astar astar;
	private static final int ROWS = (int) (Constants.HEIGHT / Constants.TILE_WIDTH);
	private static final int COLS = (int) (Constants.WIDTH / Constants.TILE_WIDTH);
	private static final int MAP_SIZE = ROWS * COLS;
	
	
	public BasicMeleeAI(Entity me, Engine engine) {
		super(me, engine);
		
		aiState = AIState.idle;
		
		timer = 0;
		
		boolean[] validityMap = new boolean[COLS*ROWS];
		
		for(int i = 0; i < ROWS * COLS; i++) {
			validityMap[i] = false;
		}
		
		astar = new Astar(COLS, ROWS, validityMap);
		

	}
	
	@Override
	public void update(float deltaTime) {
		updateGridPositions();

		switch(aiState) {
		case chase:
			chase();
			break;
		case fight:
			fight();
			break;
		case flee:
			flee();
			break;
		case idle:
			idle();
			break;
		}
	}
	
	private void idle() {
		aiState = AIState.chase;		
	}

	private void flee() {
		
	}

	private void fight() {
		//Check facing
		enemPos = Mappers.posMap.get(me);
		playerPos = Mappers.posMap.get(player);
		
		statComponent = Mappers.staMap.get(me);
		
		timer += Gdx.graphics.getDeltaTime();
		if(timer >= TICK) {
			timer = 0;
			if(MathUtils.randomBoolean(FAIL)) {
				Gdx.app.log(statComponent.name+" : ", "Ability failed!");
				return;
			}
		} else 
			return;
		
		//Check ability
		eAbilityComponent = Mappers.abMap.get(me);
		if(!inAbilityRange(eAbilityComponent, AbilityNameType.AT_BLADE_STRIKE)) {
			aiState = AIState.chase;
			return;
		}
		if(Mappers.staMap.get(me).energy >= eAbilityComponent.abilityMap.get(AbilityNameType.AT_BLADE_STRIKE).getEnergy_cost())
			eAbilityComponent.abilityMap.get(AbilityNameType.AT_BLADE_STRIKE).isActivated = true;
	}

	private void chase() {
		//Check facing
		enemPos = Mappers.posMap.get(me);
		playerPos = Mappers.posMap.get(player);
		correctFacing(enemPos, playerPos);
		
		eAbilityComponent = Mappers.abMap.get(me);
		if(inAbilityRange(eAbilityComponent, AbilityNameType.AT_BLADE_STRIKE)) {
			aiState = AIState.fight;
			return;
		}
		
		enemyPathToPoint(enemPos, playerPos);
	}

	private void updateGridPositions() {
		boolean occupied = false;
		for(int i = 0; i < MAP_SIZE; i++) {
			occupied = false;
			int x = i % COLS;
			int y = (i - x) / COLS;
			
			//update other entities grid pos
			int size = entities.size();
			for(int j = 0; j < size; j++) {
				e = entities.get(j);
				enemPos = Mappers.posMap.get(e);
				if(enemPos.curXPos==x && enemPos.curYPos==y) {
					occupied = true;
					break;
				}
			}
			
			//update other grid pos
			//TODO:Add grid position updates for other arena elements
			
			astar.setPositionValidity(x, y, occupied);
		}
		
	}
	
	private void enemyPathToPoint(PositionComponent mePos, PositionComponent otherPos) {
		IntArray pathToPt = astar.getPath((int)mePos.curXPos, 
				(int)mePos.curYPos, 
				(int)otherPos.curXPos, 
				(int)otherPos.curYPos);
		
		if(pathToPt == null || pathToPt.size < 2) return;
		
		mePos.nXPos = pathToPt.get(pathToPt.size - 2);
		mePos.nYPos = pathToPt.get(pathToPt.size - 1);
	}
	

}
