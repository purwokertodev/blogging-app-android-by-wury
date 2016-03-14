package com.wury.blog.author;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TambahPostActivity extends Activity implements OnClickListener{
	
	//private static final String URL_CREATE = "http://10.0.2.2/Blogging/page/model/android_app/add_post.php";
	private static final String URL_CREATE = "http://192.168.1.132/Blogging/page/model/android_app/add_post.php";
		
	private static final String TAG_SUCCESS = "success";
	
	private ProgressDialog progressDialog;
	
	private Button btnSimpan;
	private EditText txtJudul, txtIsi;
	
	private JsonParser parser = new JsonParser();;
	private SessionManager sessionManager;
	
	private String idAuthor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tambah_post_activity);
		
		btnSimpan = (Button) findViewById(R.id.btn_save_post);
		txtJudul = (EditText) findViewById(R.id.txt_post_judul);
		txtIsi = (EditText) findViewById(R.id.txt_post_isi);
		
		btnSimpan.setOnClickListener(this);
		
		
		sessionManager = new SessionManagerImpl(TambahPostActivity.this);
		sessionManager.checkLogin();
		
		HashMap<String, String> val = sessionManager.getUserDetails();
		idAuthor = val.get(SessionManager.KEY_ID);
		
		
	}

	@Override
	public void onClick(View v) {
		if(txtJudul.getText().toString().trim().equals("")){
			Toast.makeText(getApplicationContext(), "Isi data dengan benar..", Toast.LENGTH_SHORT).show();
		}else if(txtIsi.getText().toString().trim().equals("")){
			Toast.makeText(getApplicationContext(), "Isi data dengan benar..", Toast.LENGTH_SHORT).show();
		}else{
			new SimpanPost().execute();
		}
	}
	
	class SimpanPost extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(TambahPostActivity.this);
			progressDialog.setTitle("Connecting to server..");
			progressDialog.setMessage("Please wait..");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
			
		}

		@Override
		protected String doInBackground(String... params) {
			String judul = txtJudul.getText().toString().trim();
			String isi = txtIsi.getText().toString().trim();
			
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id_author", idAuthor));
			list.add(new BasicNameValuePair("judul", judul));
			list.add(new BasicNameValuePair("isi", isi));
			
			JSONObject jo = parser.makeHttpRequest(URL_CREATE, "POST",list);
			Log.d("response", jo.toString());
			
			try {
				int success = jo.getInt(TAG_SUCCESS);
				if(success == 1){
					Intent in = new Intent(TambahPostActivity.this, DaftarPostByAuthorActivity.class);
					startActivity(in);
					finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
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
