package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.StatComponent;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

public class DeathSystem extends EntitySystem {

	private ImmutableArray<Entity> entities;
	private Entity e;
	private StatComponent stat;
	private Engine engine;
	
	@Override
	public void addedToEngine(Engine engine) {
		this.engine = engine;
		entities = engine.getEntitiesFor(Family.getFor(StatComponent.class));
	}
	
	@Override
	public void update(float deltaTime) {
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			stat = Mappers.staMap.get(e);
			if(stat.health <= 0) {
				engine.removeEntity(e);
			}
		}
	}
	
	public void dipose() {
		
	}
}
