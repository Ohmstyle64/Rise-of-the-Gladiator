package com.aneebo.rotg.screens;

import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.MovementComponent;
import com.aneebo.rotg.components.RenderComponent;
import com.aneebo.rotg.systems.CollisionSystem;
import com.aneebo.rotg.systems.InputSystem;
import com.aneebo.rotg.systems.MovementSystem;
import com.aneebo.rotg.systems.RenderSystem;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Play implements Screen {

	//Systems
	private CollisionSystem collisionSystem;
	private InputSystem inputSystem;
	private MovementSystem movementSystem;
	private RenderSystem renderSystem;
	
	private Engine engine;
	
	private OrthogonalTiledMapRenderer renderer;
	
	@Override
	public void show() {
		renderer = new OrthogonalTiledMapRenderer(new TmxMapLoader().load("img/arena/arena_1.tmx"));
		
		engine = new Engine();
		
		//Create Entities
		Entity player = new Entity();
		player.add(new MovementComponent(3,2));
		player.add(new InputComponent());
		Texture texture = new Texture("img/characters/dragon_form.png");
		player.add(new RenderComponent(texture));
		
		//Add Entities
		engine.addEntity(player);
		
		
		//Create Systems
		createSystems();
		
		
		//Add Systems
		engine.addSystem(collisionSystem);
		engine.addSystem(inputSystem);
		engine.addSystem(movementSystem);
		engine.addSystem(renderSystem);
		
	}

	private void createSystems() {
		collisionSystem = new CollisionSystem(renderer.getMap());
		inputSystem = new InputSystem();
		movementSystem = new MovementSystem();
		renderSystem = new RenderSystem(renderer);
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
		dispose();
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
