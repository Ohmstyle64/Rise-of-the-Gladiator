package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.ProjectileComponent;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

public class ProjectileSystem extends EntitySystem {
	
	private ImmutableArray<Entity> entities;
	private Entity e;
	private PositionComponent pos;
	private ProjectileComponent proj;
	
	private Engine engine;
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(ProjectileComponent.class));
		this.engine = engine;
	}
	
	@Override
	public void update(float deltaTime) {
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			pos = Mappers.posMap.get(e);
			proj = Mappers.projMap.get(e);
			if(pos.isStopped()) {
				engine.removeEntity(e);
			}
		}
	}
	
	public void dipose() {
		
	}
}
