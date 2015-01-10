package com.aneebo.rotg.systems;

import java.util.List;

import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AIComponent;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.types.AIState;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class AISystem extends EntitySystem {
	
	private ComponentMapper<PositionComponent> pc = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<AbilityComponent> ac = ComponentMapper.getFor(AbilityComponent.class);
	private ComponentMapper<AIComponent> aic = ComponentMapper.getFor(AIComponent.class);
	private ImmutableArray<Entity> entities;
	private Entity player;
	
	private Ability ab;
	private AbilityComponent enemAbility;
	private AIComponent ai;
	private PositionComponent playerPos;
	private PositionComponent enemPos;
	private Entity e;

	private Vector2 nPos;
	private List<GridCell> path;
	private NavigationGrid<GridCell> navGrid;
	private AStarGridFinder<GridCell> finder;
	private final int rows = (int) (Gdx.graphics.getWidth() / Constants.TILE_WIDTH);
	private final int cols = (int) (Gdx.graphics.getHeight() / Constants.TILE_WIDTH);
	
	public AISystem() {
		super(1);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(AIComponent.class));
		player = engine.getEntitiesFor(Family.getFor(InputComponent.class)).first();
		GridCell[][] cells = new GridCell[rows][cols];
		playerPos = pc.get(player);
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				cells[i][j] = new GridCell(i,j);
				if(playerPos.curXPos == i && playerPos.curYPos == j) {
					cells[i][j].setWalkable(false);
				}
			}
		}
		navGrid = new NavigationGrid<GridCell>(cells);
		GridFinderOptions opt = new GridFinderOptions();
		opt.allowDiagonal = false;
		finder = new AStarGridFinder<GridCell>(GridCell.class, opt);
		nPos = new Vector2();
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
		enemAbility = ac.get(e);
		ab = inAbilityRange(enemAbility);
		if(ab != null) {
			ai.aiState = AIState.fight;
			return;
		}
		playerPos = pc.get(player);
		
		enemyPathToPoint(enemPos, playerPos);
	}
	private void fight() {
		ab = inAbilityRange(enemAbility);
		if(ab == null) {
			ai.aiState = AIState.chase;
			return;
		}
		enemAbility.ability = activateAbility(ab);
	}
	
	/**
	 * Checks if any offensive ability can be activated.
	 * @param abil
	 * @return offensive ability
	 */
	private Ability inAbilityRange(AbilityComponent abil) {
		return null;
	}
	
	private Ability activateAbility(Ability ability) {
		if(enemAbility.ability != null) 
			return enemAbility.ability;
		return ability;
	}
	
	private void enemyPathToPoint(PositionComponent mePos, PositionComponent otherPos) {
		//Used to add a position buffer
		nPos.set(mePos.curXPos, mePos.curYPos);
		nPos.sub(otherPos.curXPos, otherPos.curYPos).nor();
		
		path = finder.findPath(
				(int)mePos.curXPos, 
				(int)mePos.curYPos, 
				(int)MathUtils.round(otherPos.curXPos+nPos.x), 
				(int)MathUtils.round(otherPos.curYPos+nPos.y), 
				navGrid);
		if(path==null || path.size() == 0) return;
		mePos.nXPos = path.get(0).x;
		mePos.nYPos = path.get(0).y;
	}
	
	private void updateGridPositions() {
		//update player grid pos
		playerPos = pc.get(player);
		navGrid.setWalkable(playerPos.pXPos, playerPos.pYPos, true);
		navGrid.setWalkable((int)playerPos.curXPos, (int)playerPos.curYPos, false);
		
		//update other entities grid pos
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			enemPos = pc.get(e);
			navGrid.setWalkable(enemPos.pXPos, enemPos.pYPos, true);
			navGrid.setWalkable((int)enemPos.curXPos, (int)enemPos.curYPos, false);
		}
		
		//update other grid pos
		//TODO:Add grid position updates for other arena elements
	}
	
	public void dispose() {
		
	}
}
