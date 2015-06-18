package com.aneebo.rotg.screens;

import com.aneebo.rotg.base.GladiatorFactory;
import com.aneebo.rotg.base.GladiatorObject;
import com.aneebo.rotg.client.SaveNewUser;
import com.aneebo.rotg.client.ServerRequestController;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class NewCharacterScreen implements Screen, SaveNewUser  {

	private Stage stage;
	private Skin skin;
	private Table table;
	private String gender;
	private GladiatorObject go;
	
	@Override
	public void show() {
		stage = new Stage();
		skin = Assets.assetManager.get(Constants.UI_SKIN, Skin.class);
		table = new Table(skin);
		
		table.setFillParent(true);
		stage.setViewport(new FitViewport(Constants.WIDTH, Constants.HEIGHT));
		
		Gdx.input.setInputProcessor(stage);
		Label nameLbl = new Label("Name:", skin);
		Label classLbl = new Label("Class:", skin);
		Label genderLbl = new Label("Gender:", skin);
		Label previewLbl = new Label("Preview:", skin);
		
		TextField nameTf = new TextField("", skin);
		
		ButtonGroup<TextButton> classGroup = new ButtonGroup<TextButton>();
		TextButton warriorBtn = new TextButton("Warrior", skin);
		TextButton wizardBtn = new TextButton("Wizard", skin);
		TextButton rogueBtn = new TextButton("Rogue", skin);
		classGroup.add(warriorBtn);
		classGroup.add(wizardBtn);
		classGroup.add(rogueBtn);

		
		ButtonGroup<TextButton> genderGroup = new ButtonGroup<TextButton>();
		gender = "Male";
		TextButton maleBtn = new TextButton("Male", skin);
		maleBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gender = "Male";
			}
		});
		TextButton femaleBtn = new TextButton("Female", skin);
		femaleBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gender = "Female";
			}
		});
		genderGroup.add(maleBtn);
		genderGroup.add(femaleBtn);
		
		Image previewImg = new Image(Assets.assetManager.get(Constants.DRAGON_FORM, Texture.class));
		
		GladiatorFactory.getInstance();
		go = GladiatorFactory.getMaleWarrior();
		go.getStat().name = nameTf.getText();
		go.getStat().gender = gender;
		go.setUserName(ServerRequestController.getInstance().getLocalUser());
		TextButton createBtn = new TextButton("Create", skin);
		createBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ServerRequestController.getInstance().saveNewUserOrUpdate(go.toJsonString(), NewCharacterScreen.this);
			}
		});
		table.add(nameLbl);
		table.add(nameTf).row();
		table.add(classLbl);
		table.add(warriorBtn);
		table.add(wizardBtn);
		table.add(rogueBtn).row();
		table.add(genderLbl);
		table.add(maleBtn);
		table.add(femaleBtn).row();
		table.add(previewLbl);
		table.add(previewImg).row();
		table.add(createBtn);
		
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
		Texture.setAssetManager(Assets.assetManager);
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
	public void result(boolean worked) {
		System.out.println(worked);
		if(worked) {
			((Game)Gdx.app.getApplicationListener()).setScreen(new CitySelectScreen(go.toJsonString()));
		}
	}

}
