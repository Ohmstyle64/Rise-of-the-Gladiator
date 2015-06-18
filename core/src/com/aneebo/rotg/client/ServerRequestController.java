package com.aneebo.rotg.client;

import java.util.HashMap;

import com.aneebo.rotg.client.listener.ConnectionListener;
import com.aneebo.rotg.client.listener.NotifyRoomListener;
import com.aneebo.rotg.client.listener.RoomListener;
import com.aneebo.rotg.client.listener.ZoneListener;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.paas.sdk.java.storage.StorageService;
import com.shephertz.app42.paas.sdk.java.user.UserService;

public class ServerRequestController {
	private static ServerRequestController instance;
	
	private WarpClient warpClient;
	public UserService userService;
	public StorageService storageService;
	
	private Array<Entity> enemies;
	private RoomUsers roomUsers;
	private String localUser;
	private String roomId;
	private String docId;
	private boolean inRoom;
	private byte userRoomId;
	
	private PositionComponent pos;
	private StatComponent stat;
	private AbilityComponent abc;
	
	private PlatformConnector platformConnector;
	
	//Constants for the update byte array
	private static final int VAL = 255;
	private static final int X1_POS = 0;
	private static final int X2_POS = 1;
	private static final int Y1_POS = 2;
	private static final int Y2_POS = 3;
	private static final int DIR = 4;

	private ServerRequestController(PlatformConnector platformConnector) {
		this.platformConnector = platformConnector;
		initServer();
		
		warpClient.addConnectionRequestListener(new ConnectionListener(this));
		warpClient.addRoomRequestListener(new RoomListener(this));
		warpClient.addNotificationListener(new NotifyRoomListener(this));
		warpClient.addZoneRequestListener(new ZoneListener(this));
		enemies = new Array<Entity>();
		inRoom = false;
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

	public static ServerRequestController loadInstance(PlatformConnector platformConnector) {
		if(instance == null) {
			instance = new ServerRequestController(platformConnector);
		}
		return instance;
	}
	
	public static ServerRequestController getInstance() {
		return instance;
	}

	private void initServer() {
		try {
			WarpClient.initialize(Constants.API_KEY, Constants.SECRET_KEY);
			warpClient = WarpClient.getInstance();
			
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
	
	public void setEnemiesToUpdate(Array<Entity> enemies, byte userRoomId) {
		this.enemies = enemies;
		this.userRoomId = userRoomId;
	}
	
	public void sendUpdatePeers(byte[] update) {
		if(inRoom) {
			update[0] = userRoomId;
			warpClient.sendUpdatePeers(update);
		}
	}
	
	public void onSendUpdatePeersDone(final UpdateEvent event) {
		byte[] update = event.getUpdate();
		if(update[0]==userRoomId) return;
		Gdx.app.log("NETWORK", "Update received!");
		for(Entity e : enemies) {
			pos = Mappers.posMap.get(e);
			pos.gridNXPos = update[1];
			pos.gridNYPos = update[2];
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
	
	
}
