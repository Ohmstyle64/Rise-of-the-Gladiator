package com.aneebo.rotg.systems;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;

public class InputSystem extends EntitySystem {

	private ComponentMapper<PositionComponent> pc = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<AbilityComponent> ac = ComponentMapper.getFor(AbilityComponent.class);
	private ImmutableArray<Entity> entities;
	
	private Array<Ability> abilitySlots;
	private AbilityComponent abilityComponent;
	private PositionComponent posComponent;
	private Entity e;
	
	public InputSystem() {
		super(0);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(InputComponent.class, AbilityComponent.class));
	}

	@Override
	public void update(float deltaTime) {
		//SINGLEPLAYER ONLY
		e = entities.first();
		posComponent = pc.get(e);
		
		abilityComponent = ac.get(e);
		abilitySlots = abilityComponent.abilitySlots;
		if(Gdx.input.isKeyJustPressed(Keys.NUMPAD_1)) {
			if(abilitySlots.get(0).available)
				abilityComponent.ability = abilitySlots.get(0);
		}
		
		if(!posComponent.isStopped()) return;
		
		if(Gdx.input.isKeyJustPressed(Keys.A)) {
			posComponent.nXPos--;
		}else if(Gdx.input.isKeyJustPressed(Keys.D)) {
			posComponent.nXPos++;
		}else if(Gdx.input.isKeyJustPressed(Keys.W)) {
			posComponent.nYPos++;
		}else if(Gdx.input.isKeyJustPressed(Keys.S)) {
			posComponent.nYPos--;
		}
	}
	
	public void dispose() {
		
	}

}
