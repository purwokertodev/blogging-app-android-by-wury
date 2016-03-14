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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UbahPostActivity extends Activity {
	
	private String postId;
	private String authorId;
	private static final String TAG_POST_ID = "id_post";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ID_AUTHOR = "id_author";
	private static final String TAG_POST_TITLE = "judul";
	private static final String TAG_POST = "isi";
	private SessionManager sessionManager;
	
	private ProgressDialog progressDialog;
	private Button btnEdit, btnHapus;
	private EditText txtJudul, txtIsi;
	
	private JsonParser parser = new JsonParser();
	
	public static final String URL_LOAD_DATA = "http://192.168.1.132/Blogging/page/model/android_app/find_one_post_by_author.php";
	//public static final String URL_LOAD_DATA = "http://10.0.2.2/Blogging/page/model/android_app/find_one_post_by_author.php";
	
	public static final String URL_EDIT = "http://192.168.1.132/Blogging/page/model/android_app/edit_post_by_author.php";
	//public static final String URL_EDIT = "http://10.0.2.2/Blogging/page/model/android_app/edit_post_by_author.php";
	
	public static final String URL_DELETE = "http://192.168.1.132/Blogging/page/model/android_app/hapus_post_by_author.php";
	//public static final String URL_DELETE = "http://10.0.2.2/Blogging/page/model/android_app/hapus_post_by_author.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ubah_post);
		
		txtJudul = (EditText) findViewById(R.id.txt_post_judul_edit);
		txtIsi = (EditText) findViewById(R.id.txt_post_isi_edit);
		btnEdit = (Button) findViewById(R.id.btn_edit_post);
		btnHapus = (Button) findViewById(R.id.btn_hapus_post);
		
		sessionManager = new SessionManagerImpl(UbahPostActivity.this);
		HashMap<String, String> val = sessionManager.getUserDetails();
		authorId = val.get(SessionManager.KEY_ID);
		
		Intent intent = getIntent();
		postId = intent.getStringExtra(TAG_POST_ID);
		
		//Ambil data
		new LoadPostData().execute();
		
		//AKSI BUTTONE EDIT
		btnEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new EditPost().execute();
			}
		});
		
		//AKSI BUTTON HAPUS
		btnHapus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder ab = new AlertDialog.Builder(UbahPostActivity.this);
				ab.setTitle("Hapus");
				ab.setMessage("Anda yakin ?");
				ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
					}
				});
				ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
					}
				});
				ab.show();
			}
		});
		
	}
	
	//INNER CLASS UNTUK AMBIL DATA
	class LoadPostData extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(UbahPostActivity.this);
			progressDialog.setTitle("Connecting to server");
			progressDialog.setMessage("Please wait..");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
			
		}

		@Override
		protected String doInBackground(String... arg0) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair(TAG_POST_ID, postId));
			list.add(new BasicNameValuePair(TAG_ID_AUTHOR, authorId));
			
			JSONObject jo = parser.makeHttpRequest(URL_LOAD_DATA, "GET", list);
			
			Log.d("POST DETAIL", jo.toString());
			
			try{
				int success = jo.getInt(TAG_SUCCESS);
				if(success ==  1){
					
					String judul = jo.getString(TAG_POST_TITLE);
					String isi = jo.getString(TAG_POST);
					txtJudul.setText(judul);
					txtIsi.setText(isi);
				}
			}catch(JSONException ex){
				System.out.println("Error pada "+ex.getMessage());
			}
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			
		}
		
	}
	
	class EditPost extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(UbahPostActivity.this);
			progressDialog.setTitle("Connecting to server");
			progressDialog.setMessage("Please wait..");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair(TAG_POST_ID, postId));
			list.add(new BasicNameValuePair(TAG_ID_AUTHOR, authorId));
			list.add(new BasicNameValuePair(TAG_POST_TITLE, txtJudul.getText().toString().trim()));
			list.add(new BasicNameValuePair(TAG_POST, txtIsi.getText().toString().trim()));
			
			JSONObject jo = parser.makeHttpRequest(URL_EDIT, "POST", list);
			Log.d("EDIT POST", jo.toString());
			try{
				int success = jo.getInt(TAG_SUCCESS);
				if(success == 1){
					Intent in = getIntent();
					setResult(100, in);
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
	
	class DeletePost extends AsyncTask<String, String, String>{


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
		
	}

}
