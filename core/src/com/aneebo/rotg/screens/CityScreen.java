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

public class CityScreen implements Screen {
	
	private String cityName;
	private String jsonData;
	
	private Stage stage;
	private Table table;
	private Skin skin;

	public CityScreen(String cityName, String jsonData) {
		this.cityName = cityName;
		this.jsonData = jsonData;
	}

	@Override
	public void show() {
		stage = new Stage();
		skin = Assets.assetManager.get(Constants.UI_SKIN, Skin.class);
		table = new Table(skin);
		
		table.setFillParent(true);
		Gdx.input.setInputProcessor(stage);
		
		stage.setViewport(new FitViewport(Constants.WIDTH, Constants.HEIGHT));
		
		TextButton fightBtn = new TextButton("Fight", skin);
		fightBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game)Gdx.app.getApplicationListener()).setScreen(new MatchmakingScreen(jsonData, cityName));
			}
		});
		TextButton vendorBtn = new TextButton("Vendor", skin);
		vendorBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game)Gdx.app.getApplicationListener()).setScreen(new VendorScreen(jsonData));
			}
		});
		TextButton trainerBtn = new TextButton("Trainer", skin);
		trainerBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game)Gdx.app.getApplicationListener()).setScreen(new TrainerScreen(jsonData));
			}
		});
		TextButton characterBtn = new TextButton("Character", skin);
		characterBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game)Gdx.app.getApplicationListener()).setScreen(new CharacterScreen(jsonData));
			}
		});
		
		table.add(fightBtn).row();
		table.add(vendorBtn).row();
		table.add(trainerBtn).row();
		table.add(characterBtn);
		
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
		// TODO Auto-generated method stub
		
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

}
