package com.aneebo.rotg.client;

import java.util.HashMap;

import com.aneebo.rotg.client.listener.ConnectionListener;
import com.aneebo.rotg.client.listener.RoomListener;
import com.aneebo.rotg.client.listener.ZoneListener;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.paas.sdk.java.storage.StorageService;
import com.shephertz.app42.paas.sdk.java.user.UserService;

public class ServerRequestController {
	private static ServerRequestController instance;
	
	private WarpClient warpClient;
	public UserService userService;
	public StorageService storageService;
	
	private RoomUsers roomUsers;
	private String localUser;
	private String roomId;
	private String docId;
	private boolean inRoom;
	private byte userRoomId;
	
	private PlatformConnector platformConnector;
	
	private ServerRequestController(PlatformConnector platformConnector) {
		this.platformConnector = platformConnector;
		initServer();
		
		inRoom = false;
	}
	
	
	public byte getUserRoomId() {
		return userRoomId;
	}


	public String getLocalUser() {
		return localUser;
	}

	public String getRoomId() {
		return roomId;
	}
	
	public String getDocId() {
		return docId;
	}

	public WarpClient getWarpClient() {
		return warpClient;
	}


	public static ServerRequestController loadInstance(PlatformConnector platformConnector) {
		if(instance == null) {
			instance = new ServerRequestController(platformConnector);
		}
		return instance;
	}
	
	public static ServerRequestController getInstance() {
		return instance;
	}
	
	public void initUdp() {
		warpClient.initUDP();
	}

	private void initServer() {
		try {
			WarpClient.initialize(Constants.API_KEY, Constants.SECRET_KEY);
			warpClient = WarpClient.getInstance();
			warpClient.addConnectionRequestListener(new ConnectionListener(this));
			warpClient.addRoomRequestListener(new RoomListener(this));
			warpClient.addZoneRequestListener(new ZoneListener(this));
			platformConnector.initialize();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void connectUser(String userName) {
		localUser = userName;
		platformConnector.setLocalUser(userName);
		warpClient.connectWithUserName(userName);
	}
	
	//TODO:Need to remove. Used in development..
	public void joinRoomId() {
		warpClient.joinRoom("315081425");
	}
	
	public void loadArenas(final String cityName, final LoadArena callback) {
		platformConnector.loadArenas(cityName, callback);
	}
	
	public void saveNewUserOrUpdate(String json, final SaveNewUser callback) {
		platformConnector.saveNewUserOrUpdate(json, callback);
	}
	
	public void findPlayerData(final FindUserData callback) {
		platformConnector.findPlayerData(callback);
	}
	
	public void findAUsersData(final FindUserData callback, String userName) {
		platformConnector.findAUsersData(callback, userName);
	}
	
	public void authenticate(String userName, String password, Authenticate callback) {
		platformConnector.authenticate(userName, password, callback);
	}
	
	public void createUser(String userName, String password, String email, Authenticate callback) {
		platformConnector.createUser(userName, password, email, callback);
	}
	
	public void joinRoom(String arena) {
		HashMap<String, Object> properties = new HashMap<String, Object>(1);
		properties.put("Arena", arena);
		warpClient.joinRoomWithProperties(properties);
	}
		
	public void onJoinRoomDone(RoomEvent event) {
		if(event.getResult()==WarpResponseResultCode.SUCCESS) {
			this.roomId = event.getData().getId();
			platformConnector.setRoomId(roomId);
			warpClient.subscribeRoom(roomId);
			inRoom = true;
		} else if(event.getResult()==WarpResponseResultCode.RESOURCE_NOT_FOUND) {
			//Handle the room isn't wasn't found
			System.out.println("Room not found! "+event.getResult());
		} else {
			//Handle something else broken
			System.out.println("Something else is broken! "+event.getResult());
			
		}
	}
	
	public void sendUpdatePeers(byte[] update) {
		if(inRoom) {
			update[0] = userRoomId;
			warpClient.sendUpdatePeers(update);
		}
	}
	
	public void sendUpdatePeersFast(byte[] update) {
		if(inRoom) {
			update[0] = userRoomId;
			warpClient.sendUDPUpdatePeers(update);
		}
	}
	
	public void getLiveRoomInfo(RoomUsers callback) {
		if(inRoom) {
			roomUsers = callback;
			warpClient.getLiveRoomInfo(roomId);
		}
	}
	
	public void onLiveRoomInfoDone(final LiveRoomInfoEvent roomInfo) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				roomUsers.getRoomUsers(roomInfo);
			}
		});
	}

	public void onUserLeftRoom(RoomData arg0, String userName) {
		
	}

	public void disconnect() {
		warpClient.unsubscribeRoom(roomId);
		warpClient.leaveRoom(roomId);
	}
	
	
}
