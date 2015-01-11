package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.PositionComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

public class MovementSystem extends EntitySystem {

	private static final float SPEED = 1.5f;
	
	private ComponentMapper<PositionComponent> pc = ComponentMapper.getFor(PositionComponent.class);
	private ImmutableArray<Entity> entities;
	
	private PositionComponent pos;
	private Entity e;
	
	public MovementSystem() {
		super(3);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class));
	}
	
	@Override
	public void update(float deltaTime) {
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			pos = pc.get(e);
			
			if(pos.isStopped()) continue;
			
			if(pos.curXPos < pos.nXPos) {
				pos.curXPos += SPEED*deltaTime;
				if(pos.curXPos >= pos.nXPos) pos.curXPos = pos.nXPos;
			}else if(pos.curXPos > pos.nXPos) {
				pos.curXPos -=SPEED*deltaTime;
				if(pos.curXPos <= pos.nXPos) pos.curXPos = pos.nXPos;
			}else if(pos.curYPos < pos.nYPos) {
				pos.curYPos += SPEED*deltaTime;
				if(pos.curYPos >= pos.nYPos) pos.curYPos = pos.nYPos;
			}else if(pos.curYPos > pos.nYPos) {
				pos.curYPos -=SPEED*deltaTime;
				if(pos.curYPos <= pos.nYPos) pos.curYPos = pos.nYPos;
			}
			
			if(!pos.isStopped()) {
				pos.pXPos = (int)pos.curXPos;
				pos.pYPos = (int)pos.curYPos;
			}
		}
	}
	
	
	
	
	public void dispose() {
		
	}

}
