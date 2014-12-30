package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.CollisionComponent;
import com.aneebo.rotg.components.MovementComponent;
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
	
	private ComponentMapper<MovementComponent> mc = ComponentMapper.getFor(MovementComponent.class);
	private ComponentMapper<CollisionComponent> cc = ComponentMapper.getFor(CollisionComponent.class);
	private ImmutableArray<Entity> entities;
	
	private MovementComponent ent1Move;
	private MovementComponent ent2Move;
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
		entities = engine.getEntitiesFor(Family.getFor(MovementComponent.class, CollisionComponent.class));
	}
	
	@Override
	public void update(float deltaTime) {
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			ent1Move = mc.get(e);
			if(ent1Move.isStopped()) continue;
			
			//CHECK IF ENTITIY HIT A WALL
			if(wallLayer.getCell(ent1Move.nXPos, ent1Move.nYPos) != null) {
				ent1Move.nXPos = (int)ent1Move.curXPos;
				ent1Move.nYPos = (int)ent1Move.curYPos;
			}
			
			ent1Col = cc.get(e);
			//LOOP THROUGH THE OTHER ENTITIES
			for(int j = 0; j < size; j++) {
				//CONTINUE IF WE FOUND ENT1 = ENT2
				if(e.equals(entities.get(j))) continue;
				
				e = entities.get(j);
				
				ent2Move = mc.get(e);
				//CHECK IF THE ENT2 IS WHERE ENT1 WANTS TO GO
				if(ent1Move.nXPos==ent2Move.curXPos && ent1Move.nYPos==ent2Move.curYPos) {
					ent2Col = cc.get(e);
					//CHECK IF ENT1 IS A CHARACTER
					if(ent1Col.type==ColliderType.character) {
						//CHECK THE COLLIDER TYPE OF ENT2
						switch(ent2Col.type) {
							case character:
								ent1Move.nXPos = (int)ent1Move.curXPos;
								ent1Move.nYPos = (int)ent1Move.curYPos;
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
