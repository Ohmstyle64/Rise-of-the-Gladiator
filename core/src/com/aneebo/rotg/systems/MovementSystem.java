package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.MovementComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

public class MovementSystem extends EntitySystem {

	private static final float SPEED = 1.5f;
	
	private ComponentMapper<MovementComponent> mc = ComponentMapper.getFor(MovementComponent.class);
	private ImmutableArray<Entity> entities;
	
	private MovementComponent move;
	private Entity e;
	
	public MovementSystem() {
		super(2);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(MovementComponent.class));
	}
	
	@Override
	public void update(float deltaTime) {
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			move = mc.get(e);
			
			if(move.isStopped()) continue;
			
			if(move.curXPos < move.nXPos) {
				move.curXPos += SPEED*deltaTime;
				if(move.curXPos >= move.nXPos) move.curXPos = move.nXPos;
			}else if(move.curXPos > move.nXPos) {
				move.curXPos -=SPEED*deltaTime;
				if(move.curXPos <= move.nXPos) move.curXPos = move.nXPos;
			}else if(move.curYPos < move.nYPos) {
				move.curYPos += SPEED*deltaTime;
				if(move.curYPos >= move.nYPos) move.curYPos = move.nYPos;
			}else if(move.curYPos > move.nYPos) {
				move.curYPos -=SPEED*deltaTime;
				if(move.curYPos <= move.nYPos) move.curYPos = move.nYPos;
			}
		}
	}
	
	
	
	
	public void dispose() {
		
	}

}
