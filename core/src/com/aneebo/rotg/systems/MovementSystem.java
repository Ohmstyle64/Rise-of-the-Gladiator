package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.Direction;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

public class MovementSystem extends EntitySystem {
	
	private ImmutableArray<Entity> entities;
	private PositionComponent pos;
	private ProjectileComponent proj;
	private StatComponent stat;
	private Entity e;
	
	private static final float ACCEL_FACTOR = 9.6f;
	
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
			pos = Mappers.posMap.get(e);
			stat = Mappers.staMap.get(e);
			proj = Mappers.projMap.get(e);
			float speed = (proj != null ? proj.speed : stat.speed);
//			speed += ACCEL_FACTOR * deltaTime;
			if(pos.isStopped()) continue;
			if(!pos.isMoveable) continue;
			if(pos.curXPos < pos.nXPos) {
				pos.curXPos += speed*deltaTime;
				pos.direction = Direction.Right;
				if(pos.curXPos >= pos.nXPos) pos.curXPos = pos.nXPos;
			}else if(pos.curXPos > pos.nXPos) {
				pos.curXPos -= speed*deltaTime;
				pos.direction = Direction.Left;
				if(pos.curXPos <= pos.nXPos) pos.curXPos = pos.nXPos;
			}else if(pos.curYPos < pos.nYPos) {
				pos.curYPos += speed*deltaTime;
				pos.direction = Direction.Up;
				if(pos.curYPos >= pos.nYPos) pos.curYPos = pos.nYPos;
			}else if(pos.curYPos > pos.nYPos) {
				pos.curYPos -= speed*deltaTime;
				pos.direction = Direction.Down;
				if(pos.curYPos <= pos.nYPos) pos.curYPos = pos.nYPos;
			}
		}
	}
	
	
	
	
	public void dispose() {
		
	}

}
