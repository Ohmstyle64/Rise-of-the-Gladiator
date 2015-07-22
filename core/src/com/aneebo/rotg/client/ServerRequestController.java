package com.aneebo.rotg.client;

import java.util.HashMap;

import com.aneebo.rotg.client.listener.ConnectionListener;
import com.aneebo.rotg.client.listener.ZoneListener;
import com.aneebo.rotg.utils.Constants;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.paas.sdk.java.storage.StorageService;
import com.shephertz.app42.paas.sdk.java.user.UserService;

public class ServerRequestController {
	private static ServerRequestController instance;
	
	private WarpClient warpClient;
	public UserService userService;
	public StorageService storageService;
	
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
	
	public void getLiveRoomInfo() {
		if(inRoom) {
			warpClient.getLiveRoomInfo(roomId);
		}
	}
	
	public void setUserRoomId(byte id) {
		userRoomId = id;
	}

	public void disconnect() {
		warpClient.unsubscribeRoom(roomId);
		warpClient.leaveRoom(roomId);
	}


	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}


	public void setInRoom(boolean inRoom) {
		this.inRoom = inRoom;
	}
}
