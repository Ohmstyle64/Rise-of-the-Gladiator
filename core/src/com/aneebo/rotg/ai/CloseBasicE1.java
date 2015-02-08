package com.aneebo.rotg.ai;

import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.types.Direction;
import com.aneebo.rotg.utils.Astar;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;

public class CloseBasicE1 extends AiPlan {
	
	public enum AIState {
		idle,chase,fight,flee
	}
	
	private AIState aiState;
	
	private AbilityComponent eAbilityComponent;
	private PositionComponent playerPos;
	private PositionComponent enemPos;
	private Entity e;

	private Vector2 dir;
	private Astar astar;
	private static final int ROWS = (int) (Constants.HEIGHT / Constants.TILE_WIDTH);
	private static final int COLS = (int) (Constants.WIDTH / Constants.TILE_WIDTH);
	private static final int MAP_SIZE = ROWS * COLS;
	
	
	public CloseBasicE1(Engine engine) {
		super(engine);
		
		aiState = AIState.idle;
		
		boolean[] validityMap = new boolean[COLS*ROWS];
		
		for(int i = 0; i < ROWS * COLS; i++) {
			validityMap[i] = false;
		}
		
		astar = new Astar(COLS, ROWS, validityMap);
		
		dir = new Vector2();
	}
	
	@Override
	public void update(float deltaTime) {
		updateGridPositions();

		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
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
	}
	
	private void idle() {
		aiState = AIState.chase;		
	}

	private void flee() {
		
	}

	private void fight() {
		//Check facing
		enemPos = Mappers.posMap.get(e);
		playerPos = Mappers.posMap.get(player);
		correctFacing(enemPos, playerPos);
		//Check ability
		eAbilityComponent = Mappers.abMap.get(e);
		if(!inAbilityRange(eAbilityComponent, Constants.AT_SLASH)) {
			aiState = AIState.chase;
			return;
		}
		if(Mappers.staMap.get(e).energy >= eAbilityComponent.abilityMap.get(Constants.AT_SLASH).getEnergy_cost())
			eAbilityComponent.abilityMap.get(Constants.AT_SLASH).isActivated = true;
	}

	private void chase() {
		//Check facing
		enemPos = Mappers.posMap.get(e);
		playerPos = Mappers.posMap.get(player);
		correctFacing(enemPos, playerPos);
		
		eAbilityComponent = Mappers.abMap.get(e);
		if(inAbilityRange(eAbilityComponent, Constants.AT_SLASH)) {
			aiState = AIState.fight;
			return;
		}
		
		enemyPathToPoint(enemPos, playerPos);
	}

	private boolean inAbilityRange(AbilityComponent abil, int abilityId) {
		if(abil.abilityMap.get(abilityId).getTargets(e, new Entity[] {player}).size > 0) {
			return true;
		}
		return false;
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

		mePos.nXPos = pathToPt.get(pathToPt.size - 2);
		mePos.nYPos = pathToPt.get(pathToPt.size - 1);
	}
	
	private void correctFacing(PositionComponent mePos, PositionComponent otherPos) {
		//Check if 1 space away
		float d = dir.set(otherPos.curXPos, otherPos.curYPos).sub(mePos.curXPos, mePos.curYPos).len();
		if(d <= 1f) {
			if(dir.x < 0) {
				mePos.direction = Direction.Left;
			}else if(dir.x > 0) {
				mePos.direction = Direction.Right;
			}else if(dir.y < 0) {
				mePos.direction = Direction.Down;
			}else if(dir.y > 0){
				mePos.direction = Direction.Up;
			}
		}
	}
}
