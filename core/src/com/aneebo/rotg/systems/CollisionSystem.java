package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.CollisionComponent;
import com.aneebo.rotg.components.LevelChangerComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.screens.Play;
import com.aneebo.rotg.types.ColliderType;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class CollisionSystem extends EntitySystem {
	
	private ImmutableArray<Entity> entities;
	
	private PositionComponent ent1Pos;
	private PositionComponent ent2Pos;
	private CollisionComponent ent1Col;
	private CollisionComponent ent2Col;
	private ProjectileComponent proj;
	private Entity e1;
	private Entity e2;
	
	private TiledMapTileLayer wallLayer;
	
	public CollisionSystem(TiledMap map) {
		super(2);
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
			e1 = entities.get(i);
			ent1Pos = Mappers.posMap.get(e1);
			if(ent1Pos.isStopped()) continue;
			
			//CHECK IF ENTITIY HIT A WALL
			if(wallLayer.getCell((int)ent1Pos.curXPos, (int)ent1Pos.curYPos) != null ||
					wallLayer.getCell(ent1Pos.nXPos, ent1Pos.nYPos) != null) {
				ent1Pos.nXPos = (int)ent1Pos.curXPos;
				ent1Pos.nYPos = (int)ent1Pos.curYPos;
				proj = Mappers.projMap.get(e1);
				if(proj != null) proj.hitInAnimmate = true;
			}
			
			ent1Col = Mappers.colMap.get(e1);
			//LOOP THROUGH THE OTHER ENTITIES
			for(int j = 0; j < size; j++) {
				//CONTINUE IF WE FOUND ENT1 = ENT2
				if(e1.equals(entities.get(j))) continue;
				
				e2 = entities.get(j);
				
				ent2Pos = Mappers.posMap.get(e2);
				ent2Col = Mappers.colMap.get(e2);
				//CHECK IF THE ENT2 IS WHERE ENT1 WANTS TO GO
				if(ent1Col.type == ColliderType.character) {
					if(ent2Col.type != ColliderType.projectile &&
							ent1Pos.nXPos == ent2Pos.curXPos &&
							ent1Pos.nYPos == ent2Pos.curYPos) {
						switch(ent2Col.type) {
						case character:
							ent1Pos.nXPos = (int)ent1Pos.curXPos;
							ent1Pos.nYPos = (int)ent1Pos.curYPos;
							break;
						case trap:
							break;
						case levelChange:
							LevelChangerComponent lcc = Mappers.lvlcMap.get(e2);
							Play.levelManager.goToLevel(lcc.level);
							break;
						default:
							break;
						}
					}
				}else{
					float dstX = Math.abs(ent1Pos.curXPos - ent2Pos.curXPos);
					float dstY = Math.abs(ent1Pos.curYPos - ent2Pos.curYPos);
					float totDst = (float) Math.max(dstX, dstY);
					if(totDst <= 1f ) {
						switch(ent1Col.type) {
							case projectile:
								switch(ent2Col.type) {
									case character:
										proj = Mappers.projMap.get(e1);
										if(e2.equals(proj.from)) continue;
										proj.hit = e2;
										break;
									case trap:
										break;
									case projectile:
										break;
								}
								break;
							case trap:
								switch(ent2Col.type) {
								case character:
									break;
								case trap:
									break;
								case projectile:
									break;
								}
								break;
							default:
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
