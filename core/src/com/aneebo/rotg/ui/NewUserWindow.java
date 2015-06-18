package com.aneebo.rotg.ui;

import com.aneebo.rotg.client.ServerRequestController;
import com.aneebo.rotg.utils.Assets;
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

public class NewUserWindow extends Window {
	
	private Stage stage;

	public NewUserWindow(Skin skin, Stage stage) {
		super("New User", skin);
		
		final SuccessDialog sDia = new SuccessDialog();
		final FailDialog fDia = new FailDialog();
		
		final TextField userName = new TextField("User Name", skin);
		userName.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				userName.setText("");
			}
		});
		final TextField userPw = new TextField("Password", skin);
		userPw.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				userPw.setText("");
			}
		});
		final TextField userEmail = new TextField("Email", skin);
		userEmail.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				userEmail.setText("");
			}
		});
		userPw.setPasswordCharacter('*');
		userPw.setPasswordMode(true);
		
		TextButton createUser = new TextButton("Create", skin);
		createUser.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ServerRequestController.getInstance().userService.createUser(userName.getText(), userPw.getText(), userEmail.getText(), new App42CallBack() {

					@Override
					public void onSuccess(Object arg0) {
						sDia.show(getStage());
						NewUserWindow.this.setVisible(false);
						ServerRequestController.getInstance().connectUser(userName.getText());
					}

					@Override
					public void onException(Exception arg0) {
						fDia.show(getStage());
					}
					
				});
			}
		});
		
		add(userName).row();
		add(userPw).row();
		add(userEmail).row();
		add(createUser);
		
		setPosition(Constants.WIDTH / 2 - getWidth() / 2, Constants.HEIGHT - getHeight());
		setModal(true);
		pack();
		stage.addActor(this);
		
		setVisible(false);
		
	}
	
	private static class SuccessDialog extends Dialog {
		public SuccessDialog() {
			super("User Created", Assets.assetManager.get(Constants.UI_SKIN, Skin.class));
			
			text("User Successfully Created!");
			button("Ok");
		}
	}
	
	private static class FailDialog extends Dialog {
		public FailDialog() {
			super("User Creation Failure", Assets.assetManager.get(Constants.UI_SKIN, Skin.class));
			text("User Creation Failed!");
			button("Ok");
		}
	}
}
