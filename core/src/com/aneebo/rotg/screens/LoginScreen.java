package com.aneebo.rotg.screens;

import com.aneebo.rotg.client.Authenticate;
import com.aneebo.rotg.client.ServerRequestController;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shephertz.app42.paas.sdk.java.App42CallBack;

public class LoginScreen implements Screen, Authenticate {

	private Stage stage;
	private Table table;
	private Skin skin;
	private TextField userName, password;
	
	@Override
	public void show() {
		stage = new Stage();
		skin = Assets.assetManager.get(Constants.UI_SKIN, Skin.class);
		table = new Table(skin);
		
		Gdx.input.setInputProcessor(stage);
		stage.setViewport(new FitViewport(Constants.WIDTH, Constants.HEIGHT));
		
//		Json json = new Json();
//		System.out.println(json.prettyPrint(Gdx.files.internal("document.json").readString()));
		
		userName = new TextField("User name",skin);
		userName.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				userName.setText("");
			}
		});
		password = new TextField("Password", skin);
		password.setPasswordCharacter('*');
		password.setPasswordMode(true);
		password.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				password.setText("");
			}
		});
		
		final TextButton login = new TextButton("Login", skin);
		login.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ServerRequestController.getInstance().authenticate(userName.getText(), password.getText(), LoginScreen.this);
			}
		});
		
		final TextField email = new TextField("Email", skin);
		email.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				email.setText("");
			}
		});
		email.setVisible(false);
		final TextButton createUser = new TextButton("Create", skin);
		createUser.setVisible(false);
		createUser.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ServerRequestController.getInstance().authenticate(userName.getText(), password.getText(), LoginScreen.this);
			}
		});

		TextButton newUser = new TextButton("New User", skin);
		newUser.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				email.setVisible(true);
				createUser.setVisible(true);
			}
		});
		
		table.setFillParent(true);
		table.add(userName).row();
		table.add(password).row();
		table.add(email).row();
		table.add(login);
		table.add(createUser);
		table.add(newUser);
		
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
		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void authenticatedUser(boolean authenticated) {
		if(authenticated) {
			new Dialog("Login", skin) {
				{
					ServerRequestController.getInstance().connectUser(userName.getText());
					text("Success!\n\nWelcome "+userName.getText());
					button("Ok");
				}
				protected void result(Object object) {
					((Game)Gdx.app.getApplicationListener()).setScreen(new LoadUserDataScreen());
				};
			}.show(stage);
		} 
		else {
			userName.setText("");
			password.setText("");
			new Dialog("Login", skin) {
				{
					text("Authentication Failed!");
					button("Ok");
				}
			}.show(stage);
		}
	}

	@Override
	public void createdUser(boolean created) {
		if(created) {
			new Dialog("User Creation", skin) {
				{
					ServerRequestController.getInstance().connectUser(userName.getText());
					text("Success!\n\nWelcome "+userName.getText());
					button("Ok");
				}
				protected void result(Object object) {
					((Game)Gdx.app.getApplicationListener()).setScreen(new MenuScreen(null));
				};
			}.show(stage);
		} 
		else {
			userName.setText("");
			password.setText("");
			new Dialog("User Creation", skin) {
				{
					text("User Creation Failed!");
					button("Ok");
				}
			}.show(stage);
		}
	}

}
