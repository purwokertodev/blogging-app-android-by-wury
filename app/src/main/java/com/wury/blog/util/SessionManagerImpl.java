package com.wury.blog.util;

import java.util.HashMap;

import com.wury.blog.LoginActivity;
import com.wury.blog.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManagerImpl implements SessionManager{
	
	SharedPreferences preferences;
	Editor editor;
	Context context;
	
	public SessionManagerImpl(Context ctx){
		this.context = ctx;
		preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = preferences.edit();
	}

	@Override
	public void createLoginSession(String id, String username, String password) {
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_ID, id);
		editor.putString(KEY_USER_NAME, username);
		editor.putString(KEY_PASSWORD, password);
		editor.commit();
	}

	@Override
	public void checkLogin() {
		if(!this.isLoggedIn()){
			Intent in = new Intent(context, LoginActivity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(in);
		}
	}

	@Override
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> users = new HashMap<String, String>();
		users.put(KEY_ID, preferences.getString(KEY_ID, null));
		users.put(KEY_USER_NAME, preferences.getString(KEY_USER_NAME, null));
		users.put(KEY_PASSWORD, preferences.getString(KEY_PASSWORD, null));
		return users;
	}

	@Override
	public void logout() {
		editor.clear();
		editor.commit();
		
		Intent in = new Intent(context, MainActivity.class);
		in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(in);
	}

	@Override
	public boolean isLoggedIn() {
		return preferences.getBoolean(IS_LOGIN, false);
	}

}
