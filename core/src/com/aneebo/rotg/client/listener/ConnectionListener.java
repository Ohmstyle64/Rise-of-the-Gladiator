package com.aneebo.rotg.client.listener;

import com.aneebo.rotg.client.ServerRequestController;
import com.badlogic.gdx.Gdx;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;

public class ConnectionListener implements ConnectionRequestListener {

	private ServerRequestController callback;
	private int numTries;
	
	public ConnectionListener(ServerRequestController callback) {
		this.callback = callback;
		numTries = 0;
	}

	@Override
	public void onConnectDone(ConnectEvent arg0) {
		if(arg0.getResult() == WarpResponseResultCode.SUCCESS) {
			ServerRequestController.getInstance().initUdp();
		}
	}

	@Override
	public void onDisconnectDone(ConnectEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitUDPDone(byte arg0) {
		if(arg0 == WarpResponseResultCode.SUCCESS) {
			Gdx.app.log("NETWORK", "SUCCESS");
		}
		else {
			if(numTries > 10) Gdx.app.exit();
			numTries++;
			Gdx.app.log("NETWORK", "Won't work!");
			ServerRequestController.getInstance().initUdp();
		}
	}

}
