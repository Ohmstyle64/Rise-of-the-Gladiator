package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.AIComponent;
import com.aneebo.rotg.components.Mappers;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

public class AISystem extends EntitySystem {
	
	private ImmutableArray<Entity> entities;
	private Entity e;
	
	public AISystem() {
		super(1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(AIComponent.class).get());

	}
	
	@Override
	public void update(float deltaTime) {
		int size = entities.size();
		
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			Mappers.aiMap.get(e).plan.update(deltaTime);
		}
	}

	
	public void dispose() {
		
	}
}
