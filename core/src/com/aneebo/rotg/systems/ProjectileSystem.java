package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.ParticleComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.utils.Constants;
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
	private ParticleComponent particle;
	
	private Engine engine;
	
	public ProjectileSystem() {
		super(1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(ProjectileComponent.class).get());
		this.engine = engine;
	}
	
	@Override
	public void update(float deltaTime) {
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			pos = Mappers.posMap.get(e);
			proj = Mappers.projMap.get(e);
			if(proj.path.size > 0) {
				if(proj.isCollided()) {
					proj.rangeAbility.hit(proj, proj.from, proj.hit);
					removeEntity(e);
				} else if(pos.isStopped()) {
					pos.gridNXPos = (int)proj.path.first().x;
					pos.gridNYPos = (int)proj.path.first().y;
					proj.path.removeIndex(0);
					particle = Mappers.partMap.get(e);
					particle.pEffect.setPosition(pos.gridCurXPos*Constants.TILE_WIDTH, pos.gridCurYPos*Constants.TILE_HEIGHT);
					particle.pEffect.start();
				}else if(proj.hitInAnimmate) {
					removeEntity(e);
				}
			}
			else {
				removeEntity(e);
			}
		}
	}
	

	private void removeEntity(Entity e2) {
		particle = Mappers.partMap.get(e);
		if(particle != null) {
			particle.pEffect.dispose();
		}
		engine.removeEntity(e);
	}

	public void dipose() {
		
	}
}
