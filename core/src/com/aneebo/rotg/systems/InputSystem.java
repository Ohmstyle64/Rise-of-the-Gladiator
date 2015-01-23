package com.aneebo.rotg.systems;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.types.Direction;
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
	private Entity player;
	
	public InputSystem() {
		super(0);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(InputComponent.class, AbilityComponent.class));
		player = entities.first();
		posComponent = pc.get(player);
		abilityComponent = ac.get(player);
	}

	@Override
	public void update(float deltaTime) {
		//SINGLEPLAYER ONLY
		abilitySlots = abilityComponent.abilitySlots;
		if(Gdx.input.isKeyJustPressed(Keys.NUMPAD_1)) {
			abilitySlots.get(0).isActivated = true;
		}else if(Gdx.input.isKeyJustPressed(Keys.NUMPAD_2)) {
			abilitySlots.get(1).isActivated = true;
		}
		
		if(!posComponent.isStopped()) return;
		
		if(Gdx.input.isKeyJustPressed(Keys.A)) {
			posComponent.direction = Direction.Left;
			if(posComponent.isMoveable)
				posComponent.nXPos--;
		}else if(Gdx.input.isKeyJustPressed(Keys.D)) {
			posComponent.direction = Direction.Right;
			if(posComponent.isMoveable)
				posComponent.nXPos++;
		}else if(Gdx.input.isKeyJustPressed(Keys.W)) {
			posComponent.direction = Direction.Up;
			if(posComponent.isMoveable)
				posComponent.nYPos++;
		}else if(Gdx.input.isKeyJustPressed(Keys.S)) {
			posComponent.direction = Direction.Down;
			if(posComponent.isMoveable)
				posComponent.nYPos--;
		}
	}

	public void dispose() {
		
	}

}
