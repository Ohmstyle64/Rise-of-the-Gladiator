package com.aneebo.rotg.ai;

import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.utils.Astar;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.IntArray;

public class FastMeleeAI extends AiPlan {
	
	private enum AIState {
		idle,chase,fight,flee
	}
	
	private AIState aiState;
	
	private AbilityComponent eAbilityComponent;
	private PositionComponent playerPos;
	private PositionComponent enemPos;
	private StatComponent statComponent;
	private Entity e;
	
	private int pXPos, pYPos;
	private IntArray pathToPt;

	private Astar astar;

	private float timer;
	private static final int ROWS = (int) (Constants.HEIGHT / Constants.TILE_WIDTH);
	private static final int COLS = (int) (Constants.WIDTH / Constants.TILE_WIDTH);
	private static final int MAP_SIZE = ROWS * COLS;

	public FastMeleeAI(Entity me, Engine engine) {
		super(me, engine);
		
		aiState = AIState.idle;
		
		timer = 0;
		
		boolean[] validityMap = new boolean[COLS*ROWS];
		
		for(int i = 0; i < ROWS * COLS; i++) {
			validityMap[i] = false;
		}
		
		astar = new Astar(COLS, ROWS, validityMap);
		
		playerPos = Mappers.posMap.get(player);
		
		pXPos = -1;
		pYPos = -1;
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
		// TODO Auto-generated method stub
		
	}

	private void flee() {
		// TODO Auto-generated method stub
		
	}

	private void fight() {
		// TODO Auto-generated method stub
		
	}

	private void chase() {
		// TODO Auto-generated method stub
		
		
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
				if(enemPos.gridCurXPos==x && enemPos.gridCurYPos==y) {
					occupied = true;
					break;
				}
			}
			
			//update other grid pos
			//TODO:Add grid position updates for other arena elements
			
			astar.setPositionValidity(x, y, occupied);
		}
		
	}

}
