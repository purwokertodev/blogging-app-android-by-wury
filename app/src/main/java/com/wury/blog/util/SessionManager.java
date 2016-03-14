package com.wury.blog.util;

import java.util.HashMap;

public interface SessionManager {
	
	public static final Integer PRIVATE_MODE = 0;
	public static final String PREF_NAME = "blogging";
	public static final String IS_LOGIN = "islogin";
	public static final String KEY_ID = "id";
	public static final String KEY_USER_NAME = "username";
	public static final String KEY_PASSWORD = "password";
	
	void createLoginSession(String id, String username, String password);
	void checkLogin();
	HashMap<String, String> getUserDetails();
	void logout();
	boolean isLoggedIn();
	

}
