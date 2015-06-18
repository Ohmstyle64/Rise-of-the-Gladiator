package com.aneebo.rotg.client.listener;

import com.aneebo.rotg.client.ServerRequestController;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;

public class ConnectionListener implements ConnectionRequestListener {

	private ServerRequestController callback;
	
	public ConnectionListener(ServerRequestController callback) {
		this.callback = callback;
	}

	@Override
	public void onConnectDone(ConnectEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnectDone(ConnectEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitUDPDone(byte arg0) {
		// TODO Auto-generated method stub

	}

}
