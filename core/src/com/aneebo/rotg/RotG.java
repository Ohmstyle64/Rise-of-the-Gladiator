package com.aneebo.rotg;

import com.aneebo.rotg.client.PlatformConnector;
import com.aneebo.rotg.client.ServerRequestController;
import com.aneebo.rotg.screens.SplashScreen;
import com.badlogic.gdx.Game;

public class RotG extends Game {
		
	public RotG(PlatformConnector platformConnector) {
		ServerRequestController.loadInstance(platformConnector);
	}
	
	@Override
	public void create () {
		setScreen(new SplashScreen());
	}

}
