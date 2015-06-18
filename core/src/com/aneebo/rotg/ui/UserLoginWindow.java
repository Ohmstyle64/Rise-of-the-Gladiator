package com.aneebo.rotg.ui;

import com.aneebo.rotg.client.ServerRequestController;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.shephertz.app42.paas.sdk.java.App42CallBack;
import com.shephertz.app42.paas.sdk.java.App42NotFoundException;

public class UserLoginWindow extends Window {

	private TextField userName, pw;
	private Skin skin;
	private Stage stage;
	private NewUserWindow newUserWindow;
	
	public UserLoginWindow(final Skin skin, final Stage stage) {
		super("User Login", skin);
		this.skin = skin;
		this.stage = stage;
		
		newUserWindow = new NewUserWindow(skin, stage);
		
		userName = new TextField("Username", skin);
		userName.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				userName.setText("");
			}
		});
		pw = new TextField("Password", skin);
		pw.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				pw.setText("");
			}
		});
		pw.setPasswordCharacter('*');
		pw.setPasswordMode(true);
		
		TextButton login = new TextButton("Login", skin);
		login.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ServerRequestController.getInstance().userService.authenticate(userName.getText(), pw.getText(), new App42CallBack() {
					@Override
					public void onSuccess(Object arg0) {
						new Dialog("Authentication Success", skin) {
							{
								text("Authentication Successful!");
								button("Ok");
								ServerRequestController.getInstance().connectUser(userName.getText());
							}
						}.show(UserLoginWindow.this.stage);
						UserLoginWindow.this.setVisible(false);
					}
					
					@Override
					public void onException(final Exception arg0) {
						pw.setText("");
						new Dialog("Authentication Failure", skin) {
							{
								if(arg0 instanceof App42NotFoundException) {
									text("Authentication Failed");
								}else {
									text(arg0.getMessage());
								}
								button("Ok");
							}
						}.show(stage);
					}
				});
			}
		});
		
		TextButton newUser = new TextButton("New User", skin);
		newUser.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				UserLoginWindow.this.newUserWindow.setVisible(true);
				UserLoginWindow.this.setVisible(false);
			}
		});
		
		
		add(userName).row();
		add(pw).row();
		add(login).padRight(10);
		add(newUser);
		setPosition(Constants.WIDTH / 2 - getWidth() / 2, Constants.HEIGHT - getHeight());
		setModal(true);
		pack();

		setVisible(false);
	}

}
