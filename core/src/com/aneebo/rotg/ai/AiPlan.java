package com.aneebo.rotg.ai;

import com.aneebo.rotg.components.AIComponent;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.InputComponent;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

public abstract class AiPlan {

	protected Engine engine;
	protected ImmutableArray<Entity> entities;
	protected Entity player;
	
	public AiPlan(Engine engine) {
		this.engine = engine;
		entities = engine.getEntitiesFor(Family.getFor(AIComponent.class, AbilityComponent.class));
		player = engine.getEntitiesFor(Family.getFor(InputComponent.class)).first();
	}
	
	
	public abstract void update(float deltaTime);
}
