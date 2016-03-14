package com.wury.blog;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.wury.blog.json.JsonParser;
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

public class TambahAuthorActivity extends Activity{
	
	private ProgressDialog progressDialog;
	private JsonParser parser = new JsonParser();;
	private EditText txtNama, txtEmail, txtWebsite, txtUsername, txtPassword;
	private Button btnTambah;
	
	//private static final String URL_CREATE = "http://10.0.2.2/Blogging/page/model/android_app/add_authors.php";
	private static final String URL_CREATE = "http://192.168.1.132/Blogging/page/model/android_app/add_authors.php";
	private static final String TAG_SUCCESS = "success";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tambah_author);
		
		txtNama = (EditText) findViewById(R.id.txt_nama);
		txtEmail = (EditText) findViewById(R.id.txt_email);
		txtWebsite = (EditText)findViewById(R.id.txt_website);
		txtUsername = (EditText) findViewById(R.id.txt_username);
		txtPassword = (EditText) findViewById(R.id.txt_password);
		btnTambah = (Button) findViewById(R.id.btn_save);
		
		btnTambah.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(txtNama.getText().toString().trim().equals("")){
					Toast.makeText(getApplicationContext(), "Isi data dengan benar..", Toast.LENGTH_SHORT).show();
				}else if(txtEmail.getText().toString().trim().equals("")){
					Toast.makeText(getApplicationContext(), "Isi data dengan benar..", Toast.LENGTH_SHORT).show();
				}else if(txtWebsite.getText().toString().trim().equals("")){
					Toast.makeText(getApplicationContext(), "Isi data dengan benar..", Toast.LENGTH_SHORT).show();
				}else if(txtUsername.getText().toString().trim().equals("")){
					Toast.makeText(getApplicationContext(), "Isi data dengan benar..", Toast.LENGTH_SHORT).show();
				}else if(txtPassword.getText().toString().trim().equals("")){
					Toast.makeText(getApplicationContext(), "Isi data dengan benar..", Toast.LENGTH_SHORT).show();
				}else{
					new AddAuthor().execute();
				}
			}
		});
		
	}
	
	class AddAuthor extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(TambahAuthorActivity.this);
			progressDialog.setTitle("Connecting to server");
			progressDialog.setMessage("Please wait..");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String nama = txtNama.getText().toString().trim();
			String email = txtEmail.getText().toString().trim();
			String website = txtWebsite.getText().toString().trim();
			String username = txtUsername.getText().toString().trim();
			String password = txtPassword.getText().toString().trim();
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("nama", nama));
			pairs.add(new BasicNameValuePair("email", email));
			pairs.add(new BasicNameValuePair("website", website));
			pairs.add(new BasicNameValuePair("username", username));
			pairs.add(new BasicNameValuePair("password", password));
			
			JSONObject jo = parser.makeHttpRequest(URL_CREATE, "POST", pairs);
			
			Log.d("response", jo.toString());
			
			try{
				int success = jo.getInt(TAG_SUCCESS);
				if(success == 1){
					Intent in = new Intent(getApplicationContext(), DaftarAuthorActivity.class);
					startActivity(in);
					finish();
				}
			}catch(JSONException ex){
				
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
		}
		
	}

}
