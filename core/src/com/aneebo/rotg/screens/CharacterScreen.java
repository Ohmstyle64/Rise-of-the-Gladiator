package com.aneebo.rotg.screens;

import com.aneebo.rotg.base.GladiatorFactory;
import com.aneebo.rotg.base.GladiatorObject;
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
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CharacterScreen implements Screen {

	private Stage stage;
	
	private GladiatorObject gladiator;
	private String jsonData;
	private String cityName;
	
	@SuppressWarnings("static-access")
	public CharacterScreen(String cityName, String jsonData) {
		this.cityName = cityName;
		this.jsonData = jsonData;
		
		gladiator = GladiatorFactory.getInstance().getGOFromJson(jsonData);
		
	}

	@Override
	public void show() {
		stage = new Stage();
		Skin skin = Assets.assetManager.get(Constants.UI_SKIN, Skin.class);
		Table table = new Table(skin);
		
		table.setFillParent(true);
		Gdx.input.setInputProcessor(stage);
		
		stage.setViewport(new FitViewport(Constants.WIDTH, Constants.HEIGHT));
		
		Label namelbl = new Label("Name: ", skin);
		Label name = new Label(gladiator.getStat().name, skin);
		Label xplbl = new Label("XP Pts: ", skin);
		Label xp = new Label(gladiator.getStat().skillPoints+"", skin);
		
		TextButton back = new TextButton("Back", skin);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game)Gdx.app.getApplicationListener()).setScreen(new CityScreen(cityName, jsonData));
			}
		});
		
		table.add(namelbl);
		table.add(name).row();
		table.add(xplbl);
		table.add(xp).row();
		table.add(back);
		
		
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
