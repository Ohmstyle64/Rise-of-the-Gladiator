package com.aneebo.rotg.android;

import android.os.Bundle;

import com.aneebo.rotg.RotG;
import com.aneebo.rotg.android.client.ServerRequestAndroid;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.hideStatusBar = true;
		config.useWakelock = true;
		config.useImmersiveMode = true;
		initialize(new RotG(new ServerRequestAndroid(this)), config);
	}
}
