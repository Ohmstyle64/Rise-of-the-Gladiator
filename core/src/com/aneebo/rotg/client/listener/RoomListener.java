package com.aneebo.rotg.client.listener;

import com.aneebo.rotg.client.ServerRequestController;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;

public class RoomListener implements RoomRequestListener {

	private ServerRequestController callback;
	
	public RoomListener(ServerRequestController callback) {
		this.callback = callback;
	}
	
	@Override
	public void onGetLiveRoomInfoDone(LiveRoomInfoEvent arg0) {
		callback.onLiveRoomInfoDone(arg0);
	}

	@Override
	public void onJoinRoomDone(RoomEvent arg0) {
		callback.onJoinRoomDone(arg0);
	}

	@Override
	public void onLeaveRoomDone(RoomEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLockPropertiesDone(byte arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetCustomRoomDataDone(LiveRoomInfoEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubscribeRoomDone(RoomEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnSubscribeRoomDone(RoomEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnlockPropertiesDone(byte arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdatePropertyDone(LiveRoomInfoEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
