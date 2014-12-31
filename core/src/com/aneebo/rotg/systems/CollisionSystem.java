package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.CollisionComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.types.ColliderType;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class CollisionSystem extends EntitySystem {
	
	private ComponentMapper<PositionComponent> pc = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<CollisionComponent> cc = ComponentMapper.getFor(CollisionComponent.class);
	private ImmutableArray<Entity> entities;
	
	private PositionComponent ent1Pos;
	private PositionComponent ent2Pos;
	private CollisionComponent ent1Col;
	private CollisionComponent ent2Col;
	private Entity e;
	
	private TiledMapTileLayer wallLayer;
	
	public CollisionSystem(TiledMap map) {
		super(1);
		wallLayer = (TiledMapTileLayer) map.getLayers().get("wall");
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, CollisionComponent.class));
	}
	
	@Override
	public void update(float deltaTime) {
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			ent1Pos = pc.get(e);
			if(ent1Pos.isStopped()) continue;
			
			//CHECK IF ENTITIY HIT A WALL
			if(wallLayer.getCell(ent1Pos.nXPos, ent1Pos.nYPos) != null) {
				ent1Pos.nXPos = (int)ent1Pos.curXPos;
				ent1Pos.nYPos = (int)ent1Pos.curYPos;
			}
			
			ent1Col = cc.get(e);
			//LOOP THROUGH THE OTHER ENTITIES
			for(int j = 0; j < size; j++) {
				//CONTINUE IF WE FOUND ENT1 = ENT2
				if(e.equals(entities.get(j))) continue;
				
				e = entities.get(j);
				
				ent2Pos = pc.get(e);
				//CHECK IF THE ENT2 IS WHERE ENT1 WANTS TO GO
				if(ent1Pos.nXPos==ent2Pos.curXPos && ent1Pos.nYPos==ent2Pos.curYPos) {
					ent2Col = cc.get(e);
					//CHECK IF ENT1 IS A CHARACTER
					if(ent1Col.type==ColliderType.character) {
						//CHECK THE COLLIDER TYPE OF ENT2
						switch(ent2Col.type) {
							case character:
								ent1Pos.nXPos = (int)ent1Pos.curXPos;
								ent1Pos.nYPos = (int)ent1Pos.curYPos;
								break;
							case trap:
								break;
						}
					}
				}
			}
		}
	}
	
	
	
	public void dispose() {
		
	}

}
