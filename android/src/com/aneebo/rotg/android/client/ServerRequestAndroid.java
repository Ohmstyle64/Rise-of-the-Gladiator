package com.aneebo.rotg.android.client;

import java.util.ArrayList;

import android.content.Context;

import com.aneebo.rotg.client.Authenticate;
import com.aneebo.rotg.client.FindUserData;
import com.aneebo.rotg.client.LoadArena;
import com.aneebo.rotg.client.PlatformConnector;
import com.aneebo.rotg.client.SaveNewUser;
import com.aneebo.rotg.utils.Cities;
import com.aneebo.rotg.utils.Constants;
import com.aneebo.rotg.utils.Cities.City;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.Storage.JSONDocument;
import com.shephertz.app42.paas.sdk.android.storage.StorageService;
import com.shephertz.app42.paas.sdk.android.user.UserService;

public class ServerRequestAndroid implements PlatformConnector {

	private UserService userService;
	private StorageService storageService;
	private Context context;
	
	private String localUser;
	private String roomId;
	private String docId;
	
	public ServerRequestAndroid(Context context) {
		this.context = context;
	}
	
	@Override
	public void initialize() {
		App42API.initialize(context, Constants.API_KEY, Constants.SECRET_KEY);
		userService = App42API.buildUserService();
		storageService = App42API.buildStorageService();
	}
	
	@Override
	public void setLocalUser(String localUser) {
		this.localUser = localUser;
	}

	@Override
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	@Override
	public void setDocId(String docId) {
		this.docId = docId;
	}
	
	@Override
	public String getLocalUser() {
		return localUser;
	}

	@Override
	public String getRoomId() {
		return roomId;
	}

	@Override
	public String getDocId() {
		return docId;
	}

	@Override
	public void loadArenas(final String cityName, final LoadArena callback) {
		storageService.findDocumentById(DB, COLLECT_CITYARENA, CITY_DOC_ID, new App42CallBack() {
			@Override
			public void onSuccess(Object arg0) {
				Storage storage = (Storage)arg0;
				ArrayList<JSONDocument> docs = storage.jsonDocList;
				Json json = new Json();
				Cities cities = json.fromJson(Cities.class, docs.get(0).jsonDoc);
				Array<City> c = cities.getCities();
				for(final City city : c) {
					if(city.getCityName().equals(cityName)) {
						Gdx.app.postRunnable(new Runnable() {
							@Override
							public void run() {
								callback.loadArena(city.getArena());
							}
						});
					}
				}
			}
			
			@Override
			public void onException(Exception arg0) {
				arg0.printStackTrace();
			}
		});
	}

	@Override
	public void saveNewUserOrUpdate(String json, final SaveNewUser callback) {
		storageService.saveOrUpdateDocumentByKeyValue(DB, COLLECT_GLAD, "userName", localUser, json, new App42CallBack() {
			@Override
			public void onSuccess(Object arg0) {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						callback.result(true);
					}
				});
			}
			
			@Override
			public void onException(Exception arg0) {
				arg0.printStackTrace();
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						callback.result(false);
					}
				});
			}
		});
	}

	@Override
	public void findPlayerData(final FindUserData callback) {
		findAUsersData(callback, localUser);
	}

	@Override
	public void findAUsersData(final FindUserData callback, String userName) {
		storageService.findDocumentByKeyValue(DB, COLLECT_GLAD, "userName", userName, new App42CallBack() {

			@Override
			public void onSuccess(Object arg0) {
				Storage storage = (Storage)arg0;
				ArrayList<JSONDocument> docs = storage.jsonDocList;
				final String json = docs.get(0).jsonDoc;
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						callback.loadUserData(json);
					}
				});
			}

			@Override
			public void onException(Exception arg0) {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						callback.loadUserData(null);
					}
				});
			}
			
		});
	}
	
	@Override
	public void authenticate(String userName, String password, final Authenticate callback) {
		userService.authenticate(userName, password, new App42CallBack() {
			@Override
			public void onSuccess(Object arg0) {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						callback.authenticatedUser(true);
					}
				});
			}
			
			@Override
			public void onException(Exception arg0) {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						callback.authenticatedUser(false);
					}
				});
			}
		});
	}

	@Override
	public void createUser(String userName, String password, String email, final Authenticate callback) {
		userService.createUser(userName, password, password, new App42CallBack() {
			@Override
			public void onSuccess(Object arg0) {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						callback.createdUser(true);
					}
				});
			}
			
			@Override
			public void onException(Exception arg0) {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						callback.createdUser(false);
					}
				});
			}
		});
	}


}
