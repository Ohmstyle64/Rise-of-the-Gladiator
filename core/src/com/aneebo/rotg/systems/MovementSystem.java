package com.aneebo.rotg.systems;

import com.aneebo.rotg.client.ServerRequestController;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.DirectionType;
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
	
	private byte[] update;
	
	public MovementSystem() {
		super(3);
		update = new byte[5];
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(PositionComponent.class).get());
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
			if(pos.isStopped()) continue;
			if(!pos.isMoveable) continue;
			if(pos.curXPos < pos.gridNXPos) {
				pos.curXPos += speed*deltaTime;
				pos.direction = DirectionType.Right;
				if(pos.curXPos >= pos.gridNXPos) {
					pos.setCurXPos(pos.gridNXPos);
				}
			}else if(pos.curXPos > pos.gridNXPos) {
				pos.curXPos -= speed*deltaTime;
				pos.direction = DirectionType.Left;
				if(pos.curXPos <= pos.gridNXPos) {
					pos.setCurXPos(pos.gridNXPos);
				}
			}else if(pos.curYPos < pos.gridNYPos) {
				pos.curYPos += speed*deltaTime;
				pos.direction = DirectionType.Up;
				if(pos.curYPos >= pos.gridNYPos) {
					pos.setCurYPos(pos.gridNYPos);
				}
			}else if(pos.curYPos > pos.gridNYPos) {
				pos.curYPos -= speed*deltaTime;
				pos.direction = DirectionType.Down;
				if(pos.curYPos <= pos.gridNYPos) {
					pos.setCurYPos(pos.gridNYPos);
				}
			}
			if(Mappers.inpMap.get(e) != null) {
				update[1] = (byte) pos.gridNXPos;
				update[2] = (byte) pos.gridNYPos;
				update[3] = (byte) pos.gridCurXPos;
				update[4] = (byte) pos.gridCurYPos;
				ServerRequestController.getInstance().sendUpdatePeersFast(update);
			}
		}
	}
	
	
	
	
	public void dispose() {
		
	}
}
