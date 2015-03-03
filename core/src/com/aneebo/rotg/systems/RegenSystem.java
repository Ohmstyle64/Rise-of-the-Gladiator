package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.StatComponent;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

public class RegenSystem extends EntitySystem {
	
	public ImmutableArray<Entity> entities;
	
	public StatComponent stat;
	public Entity e;
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(StatComponent.class));
	}
	
	@Override
	public void update(float deltaTime) {
		
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			stat = Mappers.staMap.get(e);
			
			stat.health += stat.health_regen*(deltaTime / 6);
			if(stat.health >= stat.max_health) stat.health = stat.max_health;
			
			stat.energy += stat.energy_regen*(deltaTime / 6);
			if(stat.energy >= stat.max_energy) stat.energy = stat.max_energy;
		}
	}
	
	public void dispose() {
		
	}
	
}
