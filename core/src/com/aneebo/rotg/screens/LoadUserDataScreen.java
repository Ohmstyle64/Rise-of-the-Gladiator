package com.aneebo.rotg.screens;

import com.aneebo.rotg.client.FindUserData;
import com.aneebo.rotg.client.ServerRequestController;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class LoadUserDataScreen implements Screen, FindUserData {

	private Label time;
	private float timer;
	private Skin skin;
	private Stage stage;
	
	
	
	@Override
	public void show() {
		stage = new Stage();
		skin = Assets.assetManager.get(Constants.UI_SKIN, Skin.class);
		ServerRequestController.getInstance().findPlayerData(this);
		time = new Label("Time: ", skin);
		timer = 0;
		
		stage.addActor(time);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		timer += delta;
		time.setText(timer+"");
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		dispose();
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
		stage.dispose();
	}

	@Override
	public void loadUserData(String jsonData) {
		((Game)Gdx.app.getApplicationListener()).setScreen(new MenuScreen(jsonData));
	}

}
