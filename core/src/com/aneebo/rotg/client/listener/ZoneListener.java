package com.aneebo.rotg.client.listener;

import com.aneebo.rotg.client.ServerRequestController;
import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;

public class ZoneListener implements ZoneRequestListener {
	
	private ServerRequestController callback;

	public ZoneListener(ServerRequestController callback) {
		this.callback = callback;
	}

	@Override
	public void onCreateRoomDone(RoomEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeleteRoomDone(RoomEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetAllRoomsDone(AllRoomsEvent arg0) {

	}

	@Override
	public void onGetLiveUserInfoDone(LiveUserInfoEvent arg0) {
		
	}

	@Override
	public void onGetMatchedRoomsDone(MatchedRoomsEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetOnlineUsersDone(AllUsersEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetCustomUserDataDone(LiveUserInfoEvent arg0) {
		// TODO Auto-generated method stub

	}

}
