package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.MovementComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class InputSystem extends EntitySystem {

	private ComponentMapper<MovementComponent> mc = ComponentMapper.getFor(MovementComponent.class);
	private ImmutableArray<Entity> entities;
	
	private MovementComponent move;
	private Entity e;
	
	public InputSystem() {
		super(0);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(InputComponent.class));
	}

	@Override
	public void update(float deltaTime) {
		//SINGLEPLAYER ONLY
		e = entities.first();
		move = mc.get(e);
		
		if(!move.isStopped()) return;
		
		if(Gdx.input.isKeyJustPressed(Keys.A)) {
			move.nXPos--;
		}else if(Gdx.input.isKeyJustPressed(Keys.D)) {
			move.nXPos++;
		}else if(Gdx.input.isKeyJustPressed(Keys.W)) {
			move.nYPos++;
		}else if(Gdx.input.isKeyJustPressed(Keys.S)) {
			move.nYPos--;
		}
	}
	
	public void dispose() {
		
	}

}
