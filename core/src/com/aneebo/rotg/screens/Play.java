package com.aneebo.rotg.screens;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AIComponent;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.CollisionComponent;
import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.RenderComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.systems.AISystem;
import com.aneebo.rotg.systems.AbilitySystem;
import com.aneebo.rotg.systems.CollisionSystem;
import com.aneebo.rotg.systems.InputSystem;
import com.aneebo.rotg.systems.MovementSystem;
import com.aneebo.rotg.systems.RenderSystem;
import com.aneebo.rotg.types.AIState;
import com.aneebo.rotg.types.ColliderType;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
	private AISystem aiSystem;
	
	private Engine engine;
	
	private OrthogonalTiledMapRenderer renderer;
	
	@Override
	public void show() {
		renderer = new OrthogonalTiledMapRenderer(new TmxMapLoader().load("img/arena/arena_1.tmx"));
		
		engine = new Engine();
		
		//Create Entities
		Array<Ability> abilityList = new Array<Ability>();
		abilityList.add(Constants.abilityMap.get(Constants.AT_SLASH));
		abilityList.add(Constants.abilityMap.get(Constants.DF_PARRY));

		Entity player = new Entity();
		player.add(new PositionComponent(3,4));
		player.add(new InputComponent());
		player.add(new RenderComponent(new Texture("img/characters/dragon_form.png")));
		player.add(new CollisionComponent(ColliderType.character));
		player.add(new AbilityComponent(abilityList));
		player.add(new StatComponent("Kevin",100f, 100f, Color.RED));
		
		Entity enemy_1 = new Entity();
		enemy_1.add(new PositionComponent(10,7));
		enemy_1.add(new RenderComponent(new Texture("img/characters/pig_form.png")));
		enemy_1.add(new CollisionComponent(ColliderType.character));
		enemy_1.add(new AIComponent(AIState.idle));
		enemy_1.add(new AbilityComponent(abilityList));
		enemy_1.add(new StatComponent("Enemy", 100f, 100f, Color.BLUE));
		
		//Add Entities
		engine.addEntity(player);
		engine.addEntity(enemy_1);
		
		//Create Systems
		createSystems();
		
		
		//Add Systems
		engine.addSystem(collisionSystem);
		engine.addSystem(inputSystem);
		engine.addSystem(renderSystem);
		engine.addSystem(abilitySystem);
		engine.addSystem(movementSystem);
		engine.addSystem(aiSystem);
		
	}

	private void createSystems() {
		collisionSystem = new CollisionSystem(renderer.getMap());
		inputSystem = new InputSystem();
		movementSystem = new MovementSystem();
		renderSystem = new RenderSystem(renderer);
		abilitySystem = new AbilitySystem();
		aiSystem = new AISystem();
	}
	
	
	@Override
	public void render(float delta) {
		engine.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		renderSystem.getArenaCam().viewportHeight = height;
		renderSystem.getArenaCam().viewportWidth = width;
		renderSystem.getArenaCam().position.set(width / 2, height / 2, 0);
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
		aiSystem.dispose();
	}

}
