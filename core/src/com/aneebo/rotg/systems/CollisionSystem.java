package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.MovementComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class CollisionSystem extends EntitySystem {
	
	private ComponentMapper<MovementComponent> mc = ComponentMapper.getFor(MovementComponent.class);
	private ImmutableArray<Entity> entities;
	
	private MovementComponent move;
	private Entity e;
	
	private TiledMapTileLayer wallLayer;
	
	public CollisionSystem(TiledMap map) {
		super(1);
		wallLayer = (TiledMapTileLayer) map.getLayers().get("wall");
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
			
			if(wallLayer.getCell(move.nXPos, move.nYPos) != null) {
				move.nXPos = (int)move.curXPos;
				move.nYPos = (int)move.curYPos;
			}
		}
	}
	
	
	
	public void dispose() {
		
	}

}
