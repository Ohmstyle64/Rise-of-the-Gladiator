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
	private LevelChangerComponent llc;
	private Entity e1;
	private Entity e2;
	
	private TiledMapTileLayer wallLayer;
	
	public CollisionSystem(TiledMap map) {
		super(2);
		wallLayer = (TiledMapTileLayer) map.getLayers().get("wall");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(PositionComponent.class, CollisionComponent.class).get());
	}
	
	@Override
	public void update(float deltaTime) {
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e1 = entities.get(i);
			ent1Pos = Mappers.posMap.get(e1);
			if(ent1Pos.isStopped()) continue;
			
			//CHECK IF ENTITIY HIT A WALL
			if(wallLayer.getCell(ent1Pos.gridCurXPos, ent1Pos.gridCurYPos) != null ||
					wallLayer.getCell(ent1Pos.gridNXPos, ent1Pos.gridNYPos) != null) {
				ent1Pos.gridNXPos = ent1Pos.gridCurXPos;
				ent1Pos.gridNYPos = ent1Pos.gridCurYPos;
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
				
				switch(ent1Col.type) {
					case character:{
						switch(ent2Col.type) {
							case character: {
								if(ent1Pos.gridNXPos == ent2Pos.gridCurXPos && ent1Pos.gridNYPos == ent2Pos.gridCurYPos ||
										ent1Pos.gridNXPos == ent2Pos.gridNXPos && ent1Pos.gridNYPos == ent2Pos.gridNYPos) {
									ent1Pos.setCurPos(ent1Pos.gridCurXPos, ent1Pos.gridCurYPos);
								}
								break;
							}
							case levelChange: {
								if(ent1Pos.gridNXPos == ent2Pos.gridCurXPos && ent1Pos.gridNYPos == ent2Pos.gridCurYPos ||
										ent1Pos.gridNXPos == ent2Pos.gridNXPos && ent1Pos.gridNYPos == ent2Pos.gridNYPos) {
									llc = Mappers.lvlcMap.get(e2);
									Play.levelManager.goToLevel(llc.level);
								}
								break;
							}
							case merchant: {
								if(ent1Pos.gridNXPos == ent2Pos.gridCurXPos && ent1Pos.gridNYPos == ent2Pos.gridCurYPos ||
										ent1Pos.gridNXPos == ent2Pos.gridNXPos && ent1Pos.gridNYPos == ent2Pos.gridNYPos) {
									Mappers.mercMap.get(e2).isInitiated = true;
									ent1Pos.setCurPos(ent1Pos.gridCurXPos, ent1Pos.gridCurYPos);
								}
								break;
							}
							case projectile : {
								if(ent1Pos.gridNXPos == ent2Pos.gridCurXPos && ent1Pos.gridNYPos == ent2Pos.gridCurYPos ||
										ent1Pos.gridNXPos == ent2Pos.gridNXPos && ent1Pos.gridNYPos == ent2Pos.gridNYPos ||
										ent1Pos.gridCurXPos == ent2Pos.gridCurXPos && ent1Pos.gridCurXPos == ent2Pos.gridCurYPos) {
									proj = Mappers.projMap.get(e2);
									if(e1.equals(proj.from)) continue;
									proj.hit = e1;
								}
								break;
							}
							case trap: {
								//TODO: Need to implement
								break;
							}
						}
						break;
					}
					case levelChange: {
						if(ent2Col.type.equals(ColliderType.character)) {
							if(ent1Pos.gridNXPos == ent2Pos.gridCurXPos && ent1Pos.gridNYPos == ent2Pos.gridCurYPos ||
									ent1Pos.gridNXPos == ent2Pos.gridNXPos && ent1Pos.gridNYPos == ent2Pos.gridNYPos) {
								llc = Mappers.lvlcMap.get(e2);
								Play.levelManager.goToLevel(llc.level);
							}
						}
						break;
					}
					case merchant: {
						if(ent2Col.type.equals(ColliderType.character)) {
							if(ent1Pos.gridNXPos == ent2Pos.gridCurXPos && ent1Pos.gridNYPos == ent2Pos.gridCurYPos ||
									ent1Pos.gridNXPos == ent2Pos.gridNXPos && ent1Pos.gridNYPos == ent2Pos.gridNYPos) {
								Mappers.mercMap.get(e2).isInitiated = true;
								ent1Pos.setCurPos(ent1Pos.gridCurXPos, ent1Pos.gridCurYPos);
							}
						}
						break;
					}
					case projectile : {
						switch(ent2Col.type) {
							case character: {
								if(ent1Pos.gridNXPos == ent2Pos.gridCurXPos && ent1Pos.gridNYPos == ent2Pos.gridCurYPos ||
										ent1Pos.gridNXPos == ent2Pos.gridNXPos && ent1Pos.gridNYPos == ent2Pos.gridNYPos ||
										ent1Pos.gridCurXPos == ent2Pos.gridCurXPos && ent1Pos.gridCurXPos == ent2Pos.gridCurYPos) {
									proj = Mappers.projMap.get(e1);
									if(e2.equals(proj.from)) continue;
									proj.hit = e2;
								}
								break;
							}
							case projectile : {
								//TODO: Need to implement
								break;
							}
							case trap : {
								//TODO: Need to implement
								break;
							}
							default:
								break;
						}
						break;
					}
					case trap: {
						switch(ent2Col.type) {
							case character : {
								//TODO: Need to implement
								break;
							}
							case projectile: {
								//TODO: Need to implement
								break;
							}
							case trap: {
								//TODO: Need to implement
								break;
							}
							default:
								break;
						}
						break;
					}
				}
			}
		}
	}
	
	
	
	public void dispose() {
		
	}

}
