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
import com.aneebo.rotg.systems.ProjectileSystem;
import com.aneebo.rotg.systems.RegenSystem;
import com.aneebo.rotg.systems.RenderSystem;
import com.aneebo.rotg.types.AIState;
import com.aneebo.rotg.types.ColliderType;
import com.aneebo.rotg.types.Direction;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class Play implements Screen {

	//Systems
	private CollisionSystem collisionSystem;
	private InputSystem inputSystem;
	private MovementSystem movementSystem;
	private RenderSystem renderSystem;
	private AbilitySystem abilitySystem;
	private AISystem aiSystem;
	private RegenSystem regenSystem;
	private ProjectileSystem projectileSystem;
	
	private Engine engine;
	private Entity player;
	private StatComponent stat;
	private PositionComponent pos;
	private AbilityComponent ability;
	
	
	private OrthogonalTiledMapRenderer renderer;
	//UI
	private Table table;
	private Skin skin;
	private Stage stage;
	private ProgressBar healthBar;
	private ProgressBar energyBar;
	
	@Override
	public void show() {
		renderer = new OrthogonalTiledMapRenderer(new TmxMapLoader().load("img/arena/arena_1.tmx"));
		
		TextureAtlas atlas = new TextureAtlas("img/gui/uiskin.atlas");
		skin = new Skin(Gdx.files.internal("img/gui/uiskin.json"),atlas);
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		table = new Table(skin);
		table.setFillParent(true);
		
		engine = new Engine();
		
		//Create Entities
		Array<Ability> abilityList = new Array<Ability>();
		abilityList.add(Constants.abilityMap.get(Constants.AT_SLASH));
		abilityList.add(Constants.abilityMap.get(Constants.DF_PARRY));
		abilityList.add(Constants.abilityMap.get(Constants.AT_FIREBLAST));

		player = new Entity();
		player.add(pos = new PositionComponent(3,4, Direction.Right));
		player.add(new InputComponent());
		player.add(new RenderComponent(new Texture("img/characters/dragon_form.png")));
		player.add(new CollisionComponent(ColliderType.character));
		player.add(ability = new AbilityComponent(abilityList, engine));
		player.add(stat = new StatComponent("Kevin", 25f, 40f, Color.RED, 5, 5, 1.5f));
		
		Entity enemy_1 = new Entity();
		enemy_1.add(new PositionComponent(10,7, Direction.Left));
		enemy_1.add(new RenderComponent(new Texture("img/characters/pig_form.png")));
		enemy_1.add(new CollisionComponent(ColliderType.character));
		enemy_1.add(new AIComponent(AIState.idle));
		enemy_1.add(new AbilityComponent(abilityList, engine));
		enemy_1.add(new StatComponent("Enemy_1", 25f, 30f, Color.BLUE, 5, 5, 0));

		//Add Entities
		engine.addEntity(player);
		engine.addEntity(enemy_1);
		
		//Create Systems
		createSystems();

		//Build GUI
		createGUI();
		
		//Add Systems
		engine.addSystem(collisionSystem);
		engine.addSystem(inputSystem);
		engine.addSystem(renderSystem);
		engine.addSystem(abilitySystem);
		engine.addSystem(movementSystem);
		engine.addSystem(aiSystem);
		engine.addSystem(regenSystem);
		engine.addSystem(projectileSystem);
		
	}

	private void createGUI() {
		
		table.add(createStats()).top().left().row();	
		table.add(createMovementButtons()).bottom().left().expand();
		table.add(createAbilitySlots());
		
		stage.addActor(table);
	}
	
	private Table createStats() {
		Table statTable = new Table();
		
		Label name = new Label(stat.name, skin);
		Label health = new Label("Health ", skin);
		healthBar = new ProgressBar(0, 1f, 0.01f, false, skin);
		healthBar.setValue(1f);
		Label energy = new Label("Energy ", skin);
		energyBar = new ProgressBar(0, 1f, 0.01f, false, skin);
		energyBar.setValue(1f);
		
		statTable.add(name).left().row();
		statTable.add(health);
		statTable.add(healthBar).row();
		statTable.add(energy);
		statTable.add(energyBar);
		
		return statTable;
	}

	private Table createAbilitySlots() {
		Table abilityTable = new Table();
		
		TextureAtlas atlas = new TextureAtlas("img/gui/guiImg.pack");
		TextureRegionDrawable region;
		
		//Left Button Style
		ImageButtonStyle abilityImgBtnStyle = new ImageButtonStyle();
		region = new TextureRegionDrawable(atlas.findRegion("tab_label_spell"));
		abilityImgBtnStyle.up = region;
		abilityImgBtnStyle.down = region;
		
		for(final Ability a : ability.abilitySlots) {
			if(a != null) {
				Label abilityName = new Label(a.getName(), skin);
				ImageButton abilityBtn = new ImageButton(abilityImgBtnStyle);
				abilityBtn.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						a.isActivated = true;
					}
				});
				abilityTable.add(abilityName);
				abilityTable.add(abilityBtn).row();
			} else {
				Label abilityName = new Label("Empty", skin);
				ImageButton abilityBtn = new ImageButton(abilityImgBtnStyle);
				abilityTable.add(abilityName);
				abilityTable.add(abilityBtn).row();
			}
		}
		
		return abilityTable;
		
	}

	private Table createMovementButtons() {
		Table movTable = new Table();
		
		TextureAtlas atlas = new TextureAtlas("img/gui/guiImg.pack");
		TextureRegionDrawable region;

		//Left Button Style
		ImageButtonStyle leftimgBtnStyle = new ImageButtonStyle();
		region = new TextureRegionDrawable(atlas.findRegion("tab_unselected"));
		leftimgBtnStyle.up = region;
		region = new TextureRegionDrawable(atlas.findRegion("tab_selected"));
		leftimgBtnStyle.down = region;
		
		//Create Left Button
		ImageButton leftBtn = new ImageButton(leftimgBtnStyle);
		leftBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(pos.isStopped()) {
					pos.direction = Direction.Left;
					if(pos.isMoveable)
						pos.nXPos--;
				}
			}
		});
		
		//Right Button Style
		ImageButtonStyle rightimgBtnStyle = new ImageButtonStyle();
		TextureRegion tRegionRight = new TextureRegion(atlas.findRegion("tab_unselected"));
		tRegionRight.flip(true, false);
		region = new TextureRegionDrawable(tRegionRight);
		rightimgBtnStyle.up = region;
		tRegionRight = new TextureRegion(atlas.findRegion("tab_selected"));
		tRegionRight.flip(true, false);
		region = new TextureRegionDrawable(tRegionRight);
		rightimgBtnStyle.down = region;
		
		//Create Right Button
		ImageButton rightBtn = new ImageButton(rightimgBtnStyle);
		rightBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(pos.isStopped()) {
					pos.direction = Direction.Right;
					if(pos.isMoveable)
						pos.nXPos++;
				}
			}
		});
		
		//Up Button Style
		ImageButtonStyle upimgBtnStyle = new ImageButtonStyle();
		TextureRegion tRegionUp = new TextureRegion(atlas.findRegion("tab_unselected_cw"));
		region = new TextureRegionDrawable(tRegionUp);
		upimgBtnStyle.up = region;
		tRegionUp = new TextureRegion(atlas.findRegion("tab_selected_cw"));
		tRegionUp.flip(true, false);
		region = new TextureRegionDrawable(tRegionUp);
		upimgBtnStyle.down = region;
		
		//Create Up Button
		ImageButton upBtn = new ImageButton(upimgBtnStyle);
		upBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(pos.isStopped()) {
					pos.direction = Direction.Up;
					if(pos.isMoveable)
						pos.nYPos++;
				}
			}
		});
		
		//Down Button Style
		ImageButtonStyle downimgBtnStyle = new ImageButtonStyle();
		TextureRegion tRegionDown = new TextureRegion(atlas.findRegion("tab_unselected_cw"));
		tRegionDown.flip(false, true);
		region = new TextureRegionDrawable(tRegionDown);
		downimgBtnStyle.up = region;
		tRegionDown = new TextureRegion(atlas.findRegion("tab_selected_cw"));
		tRegionDown.flip(true, false);
		region = new TextureRegionDrawable(tRegionDown);
		downimgBtnStyle.down = region;
		
		//Create Down Button
		ImageButton downBtn = new ImageButton(downimgBtnStyle);
		downBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(pos.isStopped()) {
					pos.direction = Direction.Down;
					if(pos.isMoveable)
						pos.nYPos--;
				}
			}
		});
		movTable.add(upBtn).colspan(3).row();
		movTable.add(leftBtn);
		movTable.add().pad(10);
		movTable.add(rightBtn).row();
		movTable.add(downBtn).colspan(3);
		
		return movTable;
	}

	private void createSystems() {
		collisionSystem = new CollisionSystem(renderer.getMap());
		inputSystem = new InputSystem();
		movementSystem = new MovementSystem();
		renderSystem = new RenderSystem(renderer);
		abilitySystem = new AbilitySystem();
		aiSystem = new AISystem();
		regenSystem = new RegenSystem();
		projectileSystem = new ProjectileSystem();
	}
	
	
	@Override
	public void render(float delta) {
		
		stage.setDebugAll(Constants.DEBUG);
		stage.act(delta);
		engine.update(delta);
		UIUpdates();

		stage.draw();
		
	}

	private void UIUpdates() {
		healthBar.setValue(stat.health / stat.max_health);
		energyBar.setValue(stat.energy / stat.max_energy);
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
		abilitySystem.dispose();
		regenSystem.dispose();
		projectileSystem.dipose();
		stage.dispose();
	}

}
