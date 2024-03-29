package com.aneebo.rotg.screens;

import java.util.HashMap;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.base.GladiatorFactory;
import com.aneebo.rotg.base.GladiatorObject;
import com.aneebo.rotg.client.ServerRequestController;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.inventory.items.EmptyItem;
import com.aneebo.rotg.level.Level;
import com.aneebo.rotg.level.LevelManager;
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
import com.aneebo.rotg.types.DirectionType;
import com.aneebo.rotg.types.LevelType;
import com.aneebo.rotg.ui.FloatingTextManager;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.aneebo.rotg.utils.RoTGCamera;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyData;
import com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;

public class FightScreen implements Screen, NotifyListener {
	
	private Stage stage;
	private Skin skin;
	private Table table;
	private VisProgressBar energyBar;
	
	private Entity player;
	private Entity enemy;
	private Engine engine;
	
	private boolean isLoaded;
	
	private PositionComponent pos;
	private StatComponent stat;
	private AbilityComponent ability;
	
	private byte[] update;
	
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
	
	public LevelManager levelManager;
	public FloatingTextManager ftm;
	public RoTGCamera camera;
	
	private String playerJsonData;
	private String enemyJsonData;
	private String cityName;
	private LiveRoomInfoEvent roomInfo;
	
	

	public FightScreen(String cityName, String playerJsonData, String enemyJsonData, LiveRoomInfoEvent roomInfo) {
		this.cityName = cityName;
		this.playerJsonData = playerJsonData;
		this.enemyJsonData = enemyJsonData;
		this.roomInfo = roomInfo;
		isLoaded = false;
	}

	@Override
	public void show() {
		stage = new Stage();
		skin = Assets.assetManager.get(Constants.UI_SKIN, Skin.class);
		table = new Table(skin);
		
		table.setFillParent(true);
		Gdx.input.setInputProcessor(stage);
		
		stage.setViewport(new FitViewport(Constants.WIDTH, Constants.HEIGHT));
		
		stage.addActor(table);
		
		ftm = new FloatingTextManager();
		
		camera = new RoTGCamera();
		
		engine = new Engine();
		
		loadEntities();
		
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
//		engine.addSystem(merchantSystem);
		
		isLoaded = true;
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
		TextButton backButton = new TextButton("BACK", skin);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ServerRequestController.getInstance().disconnect();
				ServerRequestController.getInstance().getWarpClient().removeNotificationListener(FightScreen.this);
				((Game)Gdx.app.getApplicationListener()).setScreen(new CityScreen(cityName, playerJsonData));
			}
		});
		table.add(backButton);
		//Add Action Buttons
		//Add Potion Buttons
		stage.addActor(table);
	}
	
	private Table createStats() {
		Table statTable = new Table();
		stat = Mappers.staMap.get(player);
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
		pos = Mappers.posMap.get(player);
		ability = Mappers.abMap.get(player);
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
		
		final byte[] update = new byte[5];
		
		//Create Left Button
		ImageButton leftBtn = new ImageButton(leftimgBtnStyle);
		leftBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(pos.isStopped()) {
					pos.direction = DirectionType.Left;
					if(pos.isMoveable) {
						pos.gridNXPos--;
						update[1] = (byte) pos.gridNXPos;
						update[2] = (byte) pos.gridNYPos;
						update[3] = (byte) pos.gridCurXPos;
						update[4] = (byte) pos.gridCurYPos;
						ServerRequestController.getInstance().sendUpdatePeers(update);
					}
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
					if(pos.isMoveable) {
						pos.gridNXPos++;
						update[1] = (byte) pos.gridNXPos;
						update[2] = (byte) pos.gridNYPos;
						update[3] = (byte) pos.gridCurXPos;
						update[4] = (byte) pos.gridCurYPos;
						ServerRequestController.getInstance().sendUpdatePeers(update);				
					}
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
					if(pos.isMoveable) {
						pos.gridNYPos++;
						update[1] = (byte) pos.gridNXPos;
						update[2] = (byte) pos.gridNYPos;
						update[3] = (byte) pos.gridCurXPos;
						update[4] = (byte) pos.gridCurYPos;
						ServerRequestController.getInstance().sendUpdatePeers(update);						
					}
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
					if(pos.isMoveable) {
						pos.gridNYPos--;
						update[1] = (byte) pos.gridNXPos;
						update[2] = (byte) pos.gridNYPos;
						update[3] = (byte) pos.gridCurXPos;
						update[4] = (byte) pos.gridCurYPos;
						ServerRequestController.getInstance().sendUpdatePeers(update);						
					}
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
//		merchantSystem = new MerchantSystem();
		
	}

	private void createLevels() {
		levelManager = new LevelManager(ftm, camera);
		TestLevel testLevel = new TestLevel(engine, player, new Vector2(7,2), levelManager);
		ObjectMap<LevelType, Level> levels = new ObjectMap<LevelType, Level>(1);
		levels.put(LevelType.TEST_LEVEL, testLevel);
		levelManager.createLevels(levels, LevelType.TEST_LEVEL);
	}

	private void loadEntities() {
		GladiatorObject goPlayer = GladiatorFactory.getInstance().getGOFromJson(playerJsonData);
		GladiatorFactory.reset();
		GladiatorObject goEnemy = GladiatorFactory.getInstance().getGOFromJson(enemyJsonData);
		byte playId = 0;
		int cmp = goPlayer.getUserName().compareTo(goEnemy.getUserName());
		if(cmp > 0) {
			goPlayer.getPos().setCurPos(8, 5);
			goEnemy.getPos().setCurPos(16, 5);
		} 
		else {
			goEnemy.getPos().setCurPos(8, 5);
			goPlayer.getPos().setCurPos(16, 5);
			playId = 1;
		}
		player = goPlayer.loadEntity(engine, ftm, true);
		enemy = goEnemy.loadEntity(engine, ftm, false);
		ServerRequestController.getInstance().setUserRoomId(playId);
		engine.addEntity(player);
		engine.addEntity(enemy);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		
		engine.update(delta);
		
		levelManager.update(delta);
		
		stage.draw();
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
		dispose();
	}

	@Override
	public void dispose() {
		stage.dispose();
		Assets.dispose();
	}

	@Override
	public void onChatReceived(ChatEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStarted(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStopped(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoveCompleted(MoveEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNextTurnRequest(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPrivateChatReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPrivateUpdateReceived(String arg0, byte[] arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomCreated(RoomData arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomDestroyed(RoomData arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdatePeersReceived(UpdateEvent arg0) {
		//TODO:No updates should be sent until all users have sent a notification
		//that they have loaded the fightscreen.
		if(!isLoaded) return;
		
		update = arg0.getUpdate();
		if(update[0]==ServerRequestController.getInstance().getUserRoomId()) return;
		pos = Mappers.posMap.get(enemy);
		pos.gridNXPos = update[1];
		pos.gridNYPos = update[2];
		pos.gridCurXPos = update[3];
		pos.gridCurYPos = update[4];
	}

	@Override
	public void onUserChangeRoomProperty(RoomData arg0, String arg1,
			HashMap<String, Object> arg2, HashMap<String, String> arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserJoinedLobby(LobbyData arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserJoinedRoom(RoomData arg0, String arg1) {
		Gdx.app.log("NETWORK", "User Joined: "+arg1);
	}

	@Override
	public void onUserLeftLobby(LobbyData arg0, String arg1) {
		
	}

	@Override
	public void onUserLeftRoom(RoomData arg0, String arg1) {
		Gdx.app.log("NETWORK", "User Left: "+arg1);
	}

	@Override
	public void onUserPaused(String arg0, boolean arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserResumed(String arg0, boolean arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

}
