package com.aneebo.rotg.screens;

import com.aneebo.rotg.client.FindUserData;
import com.aneebo.rotg.client.LoadArena;
import com.aneebo.rotg.client.ServerRequestController;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;

public class MatchmakingScreen implements Screen, LoadArena, FindUserData, RoomRequestListener {
	
	private String jsonData;
	private String cityName;
	
	private float timer;
	private float fontTimer;
	private static final float REFRESH = 2f;
	private static final float FONT_REFRESH = 0.5f;
	private LiveRoomInfoEvent roomInfo;
	private boolean allowRequests;
	
	private Stage stage;
	private Table table;
	private Skin skin;
	
	private Label mapNamelbl, mapName, occupancylbl, occupancy, total, statuslbl;
	
	public MatchmakingScreen(String jsonData, String cityName) {
		this.jsonData = jsonData;
		this.cityName = cityName;
		allowRequests = false;
		timer = 0;
		fontTimer = 0;
	}

	@Override
	public void show() {
		stage = new Stage();
		skin = Assets.assetManager.get(Constants.UI_SKIN, Skin.class);
		table = new Table(skin);
		
		table.setFillParent(true);
		stage.setViewport(new FitViewport(Constants.WIDTH, Constants.HEIGHT));
		
		Gdx.input.setInputProcessor(stage);
		
		mapNamelbl = new Label("Map Name: ", skin);
		mapName = new Label("", skin);
		occupancylbl = new Label("Players: ", skin);
		occupancy = new Label("?", skin);
		total = new Label("?", skin);
		statuslbl = new Label("Searching...", skin);
		TextButton back = new TextButton("Back", skin);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ServerRequestController.getInstance().getWarpClient().removeRoomRequestListener(MatchmakingScreen.this);
				ServerRequestController.getInstance().disconnect();
				((Game)Gdx.app.getApplicationListener()).setScreen(new CityScreen(cityName, jsonData));
			}
		});
		
		
		table.add(mapNamelbl);
		table.add(mapName).row();
		table.add(occupancylbl);
		table.add(occupancy);
		table.add(" / ");
		table.add(total).row();
		table.add(statuslbl).row();
		table.add(back);
		
		stage.addActor(table);
		
		ServerRequestController.getInstance().loadArenas(cityName, this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		
		timer += delta;
		if(timer >=REFRESH && allowRequests) {
			timer = 0;
			ServerRequestController.getInstance().getLiveRoomInfo();
			allowRequests = false;
		}
		
//		fontTimer += delta;
//		if(fontTimer >= FONT_REFRESH + 0.5f) {
//			statuslbl.getStyle().fontColor = Color.YELLOW;
//		}else if(fontTimer >= FONT_REFRESH) {
//			statuslbl.getStyle().fontColor = Color.GREEN;
//		} else if(fontTimer < FONT_REFRESH || fontTimer > FONT_REFRESH + 1f) {
//			statuslbl.getStyle().fontColor = Color.WHITE;
//			fontTimer = 0;
//		}

		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);

	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void loadArena(Array<String> arenas) {
		ServerRequestController.getInstance().joinRoom("arena1");
		mapName.setText("arena1");
		statuslbl.setText("Finding Arena...");
		total.setText("2");
	}

	@Override
	public void loadUserData(final String json) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				ServerRequestController.getInstance().getWarpClient().removeRoomRequestListener(MatchmakingScreen.this);
				FightScreen fightScreen = new FightScreen(cityName, jsonData, json, roomInfo);
				ServerRequestController.getInstance().getWarpClient().addNotificationListener(fightScreen);
				((Game)Gdx.app.getApplicationListener()).setScreen(fightScreen);
			}
		});
	}

	@Override
	public void onGetLiveRoomInfoDone(final LiveRoomInfoEvent arg0) {
		roomInfo = arg0;
		if(roomInfo.getJoinedUsers().length >= roomInfo.getData().getMaxUsers()) {
			occupancy.setText("2");
			statuslbl.setText("Loading Arena...");
			for(String user : roomInfo.getJoinedUsers()) {
				if(!user.equals(ServerRequestController.getInstance().getLocalUser())) {
					ServerRequestController.getInstance().findAUsersData(MatchmakingScreen.this, user);
				}
			}
		} else {
			occupancy.setText("1");
			statuslbl.setText("Waiting for players...");
			allowRequests = true;
		}
	}

	@Override
	public void onJoinRoomDone(final RoomEvent arg0) {
		if(arg0.getResult()==WarpResponseResultCode.SUCCESS) {
			String roomId = arg0.getData().getId();
			ServerRequestController.getInstance().setRoomId(roomId);
			ServerRequestController.getInstance().getWarpClient().subscribeRoom(roomId);
			ServerRequestController.getInstance().setInRoom(true);
			Gdx.app.log("NETWORK", "Joined Room"+roomId);
			
			statuslbl.setText("Room Found...");
			allowRequests = true;
		} else if(arg0.getResult()==WarpResponseResultCode.RESOURCE_NOT_FOUND) {
			//Handle the room isn't wasn't found
			System.out.println("Room not found! "+arg0.getResult());
		} else {
			//Handle something else broken
			System.out.println("Something else is broken! "+arg0.getResult());
			
		}
	}

	@Override
	public void onLeaveRoomDone(RoomEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLockPropertiesDone(byte arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetCustomRoomDataDone(LiveRoomInfoEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubscribeRoomDone(RoomEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnSubscribeRoomDone(RoomEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnlockPropertiesDone(byte arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdatePropertyDone(LiveRoomInfoEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
