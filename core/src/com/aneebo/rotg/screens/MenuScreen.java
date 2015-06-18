package com.aneebo.rotg.screens;

import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MenuScreen implements Screen {

	private Stage stage;
	private Table table;
	private Skin skin;
	
	private String jsonData;
	
	
	public MenuScreen(String jsonData) {
		this.jsonData = jsonData;
	}
	
	
	@Override
	public void show() {
		stage = new Stage();
		skin = Assets.assetManager.get(Constants.UI_SKIN, Skin.class);
		table = new Table(skin);
		
		table.setFillParent(true);
		stage.setViewport(new FitViewport(Constants.WIDTH, Constants.HEIGHT));
		
		Gdx.input.setInputProcessor(stage);
		
		TextButton continueGameBtn = new TextButton("Continue", skin);
		continueGameBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game)Gdx.app.getApplicationListener()).setScreen(new CitySelectScreen(jsonData));
			}
		});
		TextButton newGameBtn = new TextButton("New Game", skin);
		newGameBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game)Gdx.app.getApplicationListener()).setScreen(new NewCharacterScreen());
			}
		});
		TextButton ExitBtn = new TextButton("Exit", skin);
		ExitBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		table.add(continueGameBtn).row();
		table.add(newGameBtn).row();
		table.add(ExitBtn);
		if(jsonData == null) {
			table.removeActor(continueGameBtn);
		}
		stage.addActor(table);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		
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

}
