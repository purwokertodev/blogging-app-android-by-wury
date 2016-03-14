package com.wury.blog;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.wury.blog.json.JsonParser;
import com.wury.blog.util.SessionManager;
import com.wury.blog.util.SessionManagerImpl;
import com.wury.bukutamu.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private boolean status;
	private String message;
	private ProgressDialog progressDialog;
	private JsonParser parser = new JsonParser();
	private JSONObject jo;
	
	private EditText txtUsername, txtPassword;
	private Button btnLogin;
	
	private SessionManager sessionManager;
	
	//private static final String URL_LOGIN = "http://10.0.2.2/Blogging/page/model/android_app/login_author.php";
	private static final String URL_LOGIN = "http://192.168.1.132/Blogging/page/model/android_app/login_author.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_USER_ID = "id";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_PASSWORD = "password";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		sessionManager = new SessionManagerImpl(getApplicationContext());
		
		txtUsername = (EditText) findViewById(R.id.txt_username_login);
		txtPassword = (EditText) findViewById(R.id.txt_password_login);
		btnLogin = (Button) findViewById(R.id.btn_login);
		
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(txtUsername.getText().toString().trim().equals("")){
					Toast.makeText(getApplicationContext(), "Username atau password tidak valid..", Toast.LENGTH_SHORT).show();
				}else if(txtPassword.getText().toString().trim().equals("")){
					Toast.makeText(getApplicationContext(), "Username atau password tidak valid..", Toast.LENGTH_SHORT).show();
				}else{
					new LoginProses().execute();
				}
			}
		});
	}
	
	class LoginProses extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(LoginActivity.this);
			progressDialog.setTitle("Connecting to server");
			progressDialog.setMessage("Please wait..");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String username = txtUsername.getText().toString().trim();
			String password = txtPassword.getText().toString().trim();
			
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("username", username));
			list.add(new BasicNameValuePair("password", password));
			
			jo = parser.makeHttpRequest(URL_LOGIN, "POST", list);
			
			Log.d("response", jo.toString());
			
			try{
				int success = jo.getInt(TAG_SUCCESS);
				message = jo.getString(TAG_MESSAGE);
				
				if(success == 1){
					status = true;
				}else{
					status = false;
				}
			}catch(JSONException ex){
				Toast.makeText(getApplicationContext(), "Exception "+ex.getMessage(), Toast.LENGTH_SHORT).show();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(status == false){
				txtUsername.setText("");
				txtPassword.setText("");
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			}else{
				try{
					String idTag = jo.getString(TAG_USER_ID);
					String usernameTag = jo.getString(TAG_USERNAME);
					String passwordTag = jo.getString(TAG_PASSWORD);
					Intent in = new Intent(getApplicationContext(), DashboardAuthorActivity.class);
					sessionManager.createLoginSession(idTag, usernameTag, passwordTag);
					startActivity(in);
					finish();
				}catch(JSONException ex){
					
				}
			}
		}
		
	}
	

}
