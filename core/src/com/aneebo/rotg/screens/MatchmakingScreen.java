package com.aneebo.rotg.screens;

import com.aneebo.rotg.client.FindUserData;
import com.aneebo.rotg.client.LoadArena;
import com.aneebo.rotg.client.RoomUsers;
import com.aneebo.rotg.client.ServerRequestController;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;

public class MatchmakingScreen implements Screen, LoadArena, RoomUsers, FindUserData {
	
	private String jsonData;
	private String cityName;
	
	private float timer;
	private static final float REFRESH = 2f;
	private LiveRoomInfoEvent roomInfo;
	
	public MatchmakingScreen(String jsonData, String cityName) {
		this.jsonData = jsonData;
		this.cityName = cityName;
	}

	@Override
	public void show() {
		ServerRequestController.getInstance().loadArenas(cityName, this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		timer += delta;
		if(timer >=REFRESH) {
			timer = 0;
			ServerRequestController.getInstance().getLiveRoomInfo(this);
		}

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadArena(Array<String> arenas) {
		ServerRequestController.getInstance().joinRoom("arena1");
	}

	@Override
	public void getRoomUsers(LiveRoomInfoEvent roomInfo) {
		if(roomInfo.getJoinedUsers().length >= roomInfo.getData().getMaxUsers()) {
			this.roomInfo = roomInfo;
			ServerRequestController.getInstance().findAUsersData(this, roomInfo.getJoinedUsers()[1]);
		}
	}

	@Override
	public void loadUserData(String json) {
		((Game)Gdx.app.getApplicationListener()).setScreen(new FightScreen(jsonData, json, roomInfo));
	}

}
