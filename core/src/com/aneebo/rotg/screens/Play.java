package com.aneebo.rotg.screens;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.CollisionComponent;
import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.MovementComponent;
import com.aneebo.rotg.components.RenderComponent;
import com.aneebo.rotg.systems.AbilitySystem;
import com.aneebo.rotg.systems.CollisionSystem;
import com.aneebo.rotg.systems.InputSystem;
import com.aneebo.rotg.systems.MovementSystem;
import com.aneebo.rotg.systems.RenderSystem;
import com.aneebo.rotg.types.ColliderType;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

public class Play implements Screen {

	//Systems
	private CollisionSystem collisionSystem;
	private InputSystem inputSystem;
	private MovementSystem movementSystem;
	private RenderSystem renderSystem;
	private AbilitySystem abilitySystem;
	
	private Engine engine;
	
	private OrthogonalTiledMapRenderer renderer;
	
	@Override
	public void show() {
		renderer = new OrthogonalTiledMapRenderer(new TmxMapLoader().load("img/arena/arena_1.tmx"));
		
		engine = new Engine();
		
		//Create Entities
		Entity player = new Entity();
		player.add(new MovementComponent(3,7));
		player.add(new InputComponent());
		player.add(new RenderComponent(new Texture("img/characters/dragon_form.png")));
		player.add(new CollisionComponent(ColliderType.character));
		
		Array<Ability> abilityList = new Array<Ability>();
		abilityList.add(Constants.abilityMap.get(Constants.AT_SLASH));
		
		player.add(new AbilityComponent(abilityList));
		
		Entity enemy_1 = new Entity();
		enemy_1.add(new MovementComponent(10,7));
		enemy_1.add(new RenderComponent(new Texture("img/characters/pig_form.png")));
		enemy_1.add(new CollisionComponent(ColliderType.character));
		
		//Add Entities
		engine.addEntity(player);
		engine.addEntity(enemy_1);
		
		//Create Systems
		createSystems();
		
		
		//Add Systems
		engine.addSystem(collisionSystem);
		engine.addSystem(inputSystem);
		engine.addSystem(movementSystem);
		engine.addSystem(renderSystem);
		engine.addSystem(abilitySystem);
		
	}

	private void createSystems() {
		collisionSystem = new CollisionSystem(renderer.getMap());
		inputSystem = new InputSystem();
		movementSystem = new MovementSystem();
		renderSystem = new RenderSystem(renderer);
		abilitySystem = new AbilitySystem();
	}
	
	
	@Override
	public void render(float delta) {
		engine.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		renderSystem.getArenaCam().viewportHeight = height;
		renderSystem.getArenaCam().viewportWidth = width;
		renderSystem.getArenaCam().translate(width / 4, height / 4);
		renderSystem.getArenaCam().update();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		renderSystem.dispose();
		collisionSystem.dispose();
		inputSystem.dispose();
		movementSystem.dispose();
	}

}
