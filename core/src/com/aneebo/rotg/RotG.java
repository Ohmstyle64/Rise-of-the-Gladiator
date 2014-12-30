package com.aneebo.rotg;

import com.aneebo.rotg.screens.Play;
import com.badlogic.gdx.Game;

public class RotG extends Game {
	
	@Override
	public void create () {
		setScreen(new Play());
	}

}
