package com.aneebo.rotg.systems;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AIComponent;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AIState;
import com.aneebo.rotg.types.AbilityType;
import com.aneebo.rotg.types.Direction;
import com.aneebo.rotg.utils.Astar;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;

public class AISystem extends EntitySystem {
	
	private ComponentMapper<PositionComponent> pc = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<AbilityComponent> ac = ComponentMapper.getFor(AbilityComponent.class);
	private ComponentMapper<AIComponent> aic = ComponentMapper.getFor(AIComponent.class);
	private ImmutableArray<Entity> entities;
	private Entity player;
	
	private Ability ab;
	private AbilityComponent eAbilityComponent;
	private AbilityComponent pAbilityComponent;
	private AIComponent ai;
	private PositionComponent playerPos;
	private PositionComponent enemPos;
	private StatComponent enemStat;
	private Entity e;

	private Vector2 dir;
	private Astar astar;
	private static final int ROWS = (int) (Gdx.graphics.getHeight() / Constants.TILE_WIDTH);
	private static final int COLS = (int) (Gdx.graphics.getWidth() / Constants.TILE_WIDTH);
	private static final int MAP_SIZE = ROWS * COLS;
	
	public AISystem() {
		super(1);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(AIComponent.class, AbilityComponent.class));
		player = engine.getEntitiesFor(Family.getFor(InputComponent.class)).first();
		
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
			ai = aic.get(e);
			switch(ai.aiState) {
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
		ai.aiState = AIState.chase;
	}
	
	private void flee() {
		
	}
	private void chase() {
		//Check facing
		enemPos = pc.get(e);
		playerPos = pc.get(player);
		correctFacing(enemPos, playerPos);
		
		eAbilityComponent = ac.get(e);
		if(inAbilityRange(eAbilityComponent, Constants.AT_SLASH)) {
			ai.aiState = AIState.fight;
			return;
		}
		playerPos = pc.get(player);
		
		enemyPathToPoint(enemPos, playerPos);
		
		
	}
	private void fight() {
		//Check facing
		enemPos = pc.get(e);
		playerPos = pc.get(player);
		correctFacing(enemPos, playerPos);
		//Check ability
		eAbilityComponent = ac.get(e);
		if(!inAbilityRange(eAbilityComponent, Constants.AT_SLASH)) {
			ai.aiState = AIState.chase;
			return;
		}
		int size;
		pAbilityComponent = ac.get(player);
		enemStat = Mappers.staMap.get(e);
		size = eAbilityComponent.abilitySlots.size;
		switch(hasActiveAbility(pAbilityComponent)) {
		case offense:
			for(int i = 0; i < size; i++) {
				if(eAbilityComponent.abilitySlots.get(i).getEnergy_cost() > enemStat.energy) continue;

				if(eAbilityComponent.abilitySlots.get(i).getType() == AbilityType.counter) {
					
					eAbilityComponent.abilitySlots.get(i).isActivated = true;
					return;
				}
			}
			break;
		case counter:
			for(int i = 0; i < size; i++) {
				if(eAbilityComponent.abilitySlots.get(i).getEnergy_cost() > enemStat.energy) continue;

				if(eAbilityComponent.abilitySlots.get(i).getType() == AbilityType.defense) {

					eAbilityComponent.abilitySlots.get(i).isActivated = true;
					return;
				}
			}
			break;
		case defense:
			for(int i = 0; i < size; i++) {
				if(eAbilityComponent.abilitySlots.get(i).getEnergy_cost() > enemStat.energy) continue;

				if(eAbilityComponent.abilitySlots.get(i).getType() == AbilityType.offense) {
					
					eAbilityComponent.abilitySlots.get(i).isActivated = true;
					return;
				}
			}
			break;
		default:
			for(int i = 0; i < size; i++) {
				if(eAbilityComponent.abilitySlots.get(i).getEnergy_cost() > enemStat.energy) continue;
				
				if(eAbilityComponent.abilitySlots.get(i).getType() == AbilityType.offense) {
					
					eAbilityComponent.abilitySlots.get(i).isActivated = true;
					return;
				}
			}
		}

	}
	
	private AbilityType hasActiveAbility(AbilityComponent component) {
		int size = component.abilitySlots.size;
		for(int i = 0; i < size; i++) {
			if(component.abilitySlots.get(i).isActivated) 
				return component.abilitySlots.get(i).getType();
		}
		return AbilityType.none;
	}
	
	private boolean inAbilityRange(AbilityComponent abil, int abilityId) {
		int size = abil.abilitySlots.size;
		for(int i = 0; i < size; i++) {
			if(abil.abilitySlots.get(i).getTargets(e, new Entity[] {player}).size > 0) {
				if(abil.abilitySlots.get(i).getId() == abilityId) return true;
			}
		}
		return false;
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
	
	private void enemyPathToPoint(PositionComponent mePos, PositionComponent otherPos) {
		IntArray pathToPt = astar.getPath((int)mePos.curXPos, 
				(int)mePos.curYPos, 
				(int)otherPos.curXPos, 
				(int)otherPos.curYPos);

		mePos.nXPos = pathToPt.get(pathToPt.size - 2);
		mePos.nYPos = pathToPt.get(pathToPt.size - 1);
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
				enemPos = pc.get(e);
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
	
	public void dispose() {
		
	}
}
