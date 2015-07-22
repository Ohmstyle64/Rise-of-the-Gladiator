package com.aneebo.rotg.client;

public interface PlatformConnector {
	static final String COLLECT_GLAD = "gladiatorStats";
	static final String COLLECT_CITYARENA = "cityArenas";
	static final String DB = "master";
	static final String CITY_DOC_ID = "557f4812e4b0ca60f0bb8639";
	
	public void initialize();
	public String getLocalUser();
	public String getDocId();
	public void setLocalUser(String localUser);
	public void setDocId(String docId);
	public void loadArenas(final String cityName, final LoadArena callback);
	public void saveNewUserOrUpdate(String json, final SaveNewUser callback);
	public void findPlayerData(final FindUserData callback);
	public void findAUsersData(final FindUserData callback, String userName);
	public void authenticate(final String userName, final String password, Authenticate callback);
	public void createUser(String userName, String password, String email, Authenticate callback);
}
