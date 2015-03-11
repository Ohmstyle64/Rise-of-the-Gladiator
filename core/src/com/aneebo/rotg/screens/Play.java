package com.aneebo.rotg.screens;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.CollisionComponent;
import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.InventoryComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.RenderComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.inventory.Inventory;
import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.inventory.items.ChestP1;
import com.aneebo.rotg.inventory.items.EmptyItem;
import com.aneebo.rotg.inventory.items.HeadP1;
import com.aneebo.rotg.inventory.items.PrimP1;
import com.aneebo.rotg.level.Level;
import com.aneebo.rotg.level.LevelManager;
import com.aneebo.rotg.level.arena.CaravanLevel;
import com.aneebo.rotg.level.arena.TestLevel;
import com.aneebo.rotg.systems.AISystem;
import com.aneebo.rotg.systems.AbilitySystem;
import com.aneebo.rotg.systems.CollisionSystem;
import com.aneebo.rotg.systems.DeathSystem;
import com.aneebo.rotg.systems.InputSystem;
import com.aneebo.rotg.systems.MerchantSystem;
import com.aneebo.rotg.systems.MovementSystem;
import com.aneebo.rotg.systems.ProjectileSystem;
import com.aneebo.rotg.systems.RegenSystem;
import com.aneebo.rotg.systems.RenderSystem;
import com.aneebo.rotg.types.AbilityNameType;
import com.aneebo.rotg.types.ColliderType;
import com.aneebo.rotg.types.DirectionType;
import com.aneebo.rotg.types.LevelType;
import com.aneebo.rotg.ui.FloatingTextManager;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.aneebo.rotg.utils.RoTGCamera;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisProgressBar;

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
	private DeathSystem deathSystem;
	private MerchantSystem merchantSystem;
	
	private Engine engine;
	private Entity player;
	private StatComponent stat;
	private PositionComponent pos;
	private AbilityComponent ability;
	
	public LevelManager levelManager;
	public FloatingTextManager ftm;
	public RoTGCamera camera;
	
	WindowStyle as;
		
	//UI
	private Table table;
	private Stage stage;
	private Skin skin;
	private VisProgressBar energyBar;
	
	@Override
	public void show() {
		Assets.load();

		VisUI.load();
		
		stage = new Stage();
		stage.setViewport(new FitViewport(Constants.WIDTH, Constants.HEIGHT));

		Gdx.input.setInputProcessor(stage);
		skin = Assets.assetManager.get(Constants.UI_SKIN, Skin.class);
		table = new Table(skin);
		table.setFillParent(true);

		ftm = new FloatingTextManager();
		
		camera = new RoTGCamera();
		
		engine = new Engine();

		createPlayer();

		createLevels();
		
		createSystems();

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
		engine.addSystem(deathSystem);
		engine.addSystem(merchantSystem);
		
		//Play music
//		Music song1 = Assets.assetManager.get(Constants.TEST_MUSIC, Music.class);
//		song1.play();
//		song1.setVolume(.1f);
//		song1.setLooping(true);
	}
	
	private void createLevels() {
		levelManager = new LevelManager(ftm, camera);
		TestLevel testLevel = new TestLevel(engine, player, new Vector2(7,2), levelManager);
		CaravanLevel caravanLevel = new CaravanLevel(engine, player, new Vector2(7,2), levelManager);
		ObjectMap<LevelType, Level> levels = new ObjectMap<LevelType, Level>(2);
		levels.put(LevelType.TEST_LEVEL, testLevel);
		levels.put(LevelType.CARAVAN_LEVEL, caravanLevel);
		levelManager.createLevels(levels, LevelType.CARAVAN_LEVEL);
	}

	private void createPlayer() {
		Array<Ability> abilityList = new Array<Ability>();
		abilityList.add(Constants.abilityMap.get(AbilityNameType.AT_BLADE_STRIKE));
		abilityList.add(Constants.abilityMap.get(AbilityNameType.DF_BLADE_BLOCK));
		abilityList.add(Constants.abilityMap.get(AbilityNameType.AT_WAVE_OF_FIRE));
		abilityList.add(Constants.abilityMap.get(AbilityNameType.AT_DASHING_STRIKE));
		abilityList.add(Constants.abilityMap.get(AbilityNameType.DF_TELEPORT));
//		abilityList.add(Constants.abilityMap.get(AbilityNameType.DF_FORCE_FIELD));
//		abilityList.add(Constants.abilityMap.get(AbilityNameType.DF_PURE_HEART));
		
		
		
		player = new Entity();
		player.add(pos = new PositionComponent(3,4, DirectionType.Right));
		player.add(new InputComponent());
		player.add(new CollisionComponent(ColliderType.character));
		player.add(ability = new AbilityComponent(abilityList, engine, ftm));
		player.add(stat = new StatComponent("Player", 35f, 60f, Color.RED, 7, 2, 1.5f, true));
		player.add(new RenderComponent(Constants.DRAGON_FORM, camera));
//		player.add(new AnimationComponent(Assets.assetManager.get(Constants.BODY_PLAYER, Texture.class),64,64, stat.speed / 16));
		ObjectMap<Integer, Item> equipped = new ObjectMap<Integer, Item>();
		ChestP1 cp1 = new ChestP1();
		HeadP1 hp1 =  new HeadP1();
		PrimP1 pp1 = new PrimP1();
		equipped.put(cp1.slot, cp1);
		equipped.put(hp1.slot, hp1);
		equipped.put(pp1.slot, pp1);
		Array<Item> iList = new Array<Item>(true, Constants.INVENTORY_SIZE, Item.class);
		player.add(new InventoryComponent(new Inventory(player, equipped, fillRest(iList))));
		
		engine.addEntity(player);
	}
		
	private Array<Item> fillRest(Array<Item> array) {
		int size = array.items.length;
		for(int i = array.size; i < size; i++) {
			array.add(new EmptyItem());
		}
		return array;
	}
	
	private void createGUI() {
		
		table.add(createStats()).top().left().row();	
		table.add(createMovementButtons()).bottom().left().expand();
		table.add(createAbilitySlots());
		//Add Action Buttons
		//Add Potion Buttons
		stage.addActor(table);
	}
	
	private Table createStats() {
		Table statTable = new Table();
		
		Label name = new Label(stat.name, skin);
		Label energy = new Label("Energy ", skin);
		energyBar = new VisProgressBar(0, stat.max_energy, .1f, false);
		energyBar.setValue(1f);
		
		statTable.add(name).left();
		statTable.add(energy);
		statTable.add(energyBar).center();
		
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
		final PositionComponent pos = Mappers.posMap.get(player);
		for(final Ability a : ability.abilitySlots) {
			if(a != null) {
				Label abilityName = new Label(a.getName(), skin);
				ImageButton abilityBtn = new ImageButton(abilityImgBtnStyle);
				abilityBtn.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(a instanceof Ability) {
							if(pos.isStopped()) {
								a.isActivated = true;
							}
						}else 
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
					pos.direction = DirectionType.Left;
					if(pos.isMoveable)
						pos.gridNXPos--;
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
					pos.direction = DirectionType.Right;
					if(pos.isMoveable)
						pos.gridNXPos++;
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
					pos.direction = DirectionType.Up;
					if(pos.isMoveable)
						pos.gridNYPos++;
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
		tRegionDown.flip(true, true);
		region = new TextureRegionDrawable(tRegionDown);
		downimgBtnStyle.down = region;
		
		//Create Down Button
		ImageButton downBtn = new ImageButton(downimgBtnStyle);
		downBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(pos.isStopped()) {
					pos.direction = DirectionType.Down;
					if(pos.isMoveable)
						pos.gridNYPos--;
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
		collisionSystem = new CollisionSystem(levelManager);
		inputSystem = new InputSystem(stage);
		movementSystem = new MovementSystem();
		renderSystem = new RenderSystem(levelManager.renderer(), stage, camera);
		abilitySystem = new AbilitySystem();
		aiSystem = new AISystem();
		regenSystem = new RegenSystem();
		projectileSystem = new ProjectileSystem();
		deathSystem = new DeathSystem();
		merchantSystem = new MerchantSystem();
	}
	
	@Override
	public void render(float delta) {
		
		stage.setDebugAll(Constants.DEBUG);
		
		stage.act(delta);
		
		engine.update(delta);
		
		UIUpdates();
		
		levelManager.update(delta);
		
		stage.draw();
	}

	private void UIUpdates() {
		energyBar.setValue(stat.energy);
	}

	@Override
	public void resize(int width, int height) {
		renderSystem.getViewport().update(width, height, true);
		stage.getViewport().update(width, height, true);
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
		deathSystem.dipose();
		VisUI.dispose();
		stage.dispose();
		Assets.dispose();
	}

}
