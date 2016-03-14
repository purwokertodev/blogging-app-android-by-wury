package com.wury.blog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wury.blog.json.JsonParser;
import com.wury.bukutamu.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity{
	
	private Button btnSignIn, btnRegister;
	
	private ProgressDialog progressDialog;
	private JsonParser parser = new JsonParser();
	private List<HashMap<String, String>> postList;
	
	private JSONArray data = null;
	
	//private static final String URL_POST = "http://10.0.2.2/Blogging/page/model/android_app/aLL_post.php";
	private static final String URL_POST = "http://192.168.1.132/Blogging/page/model/android_app/aLL_post.php";
	
	//json node
	private static final String TAG_DATA = "data";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ID = "id";
	private static final String TAG_AUTHOR_ID = "id_author";
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_POST_TITLE = "judul";
	private static final String TAG_POST = "isi";
	private static final String TAG_DATE = "tanggal";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daftar_posting);
		postList = new ArrayList<HashMap<String,String>>();
		new LoadPost().execute();
		
		btnSignIn = (Button) findViewById(R.id.btn_sign_in);
		btnRegister = (Button) findViewById(R.id.btn_register);
		
		btnSignIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent in = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(in);
			}
		});
		
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(getApplicationContext(), TambahAuthorActivity.class);
				startActivity(in);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	class LoadPost extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setTitle("Connecting to server");
			progressDialog.setMessage("Load data..");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> namePairs = new ArrayList<NameValuePair>();
			JSONObject jo = parser.makeHttpRequest(URL_POST, "GET", namePairs);
			 Log.d("Semua data: ", jo.toString());
			 
	            try {
	                // cek jika tag success
	                int success = jo.getInt(TAG_SUCCESS);
	 
	                if (success == 1) {
	                    // data ditemukan
	                    // ambil array data
	                    data = jo.getJSONArray(TAG_DATA);
	 
	                    // tampilkan data
	                    for (int i = 0; i < data.length(); i++) {
	                        JSONObject c = data.getJSONObject(i);
	 
	                        // simpan pada variabel
	                        String id = c.getString(TAG_ID);
	                        String idAuthor = c.getString(TAG_AUTHOR_ID);
	                        String author = c.getString(TAG_AUTHOR);
	                        String judul = c.getString(TAG_POST_TITLE);
	                        String isi = c.getString(TAG_POST);
	                        String tanggal = c.getString(TAG_DATE);
	 
	                        // buat new hashmap
	                        HashMap<String, String> map = new HashMap<String, String>();
	 
	                        // key => value
	                        map.put(TAG_ID, id);
	                        map.put(TAG_AUTHOR_ID, idAuthor);
	                        map.put(TAG_AUTHOR, author);
	                        map.put(TAG_POST_TITLE, judul);
	                        map.put(TAG_POST, isi);
	                        map.put(TAG_DATE, tanggal);
	 
	                        // masukan HashList ke ArrayList
	                        postList.add(map);
	                    }
	                } //else {
	                    // jika tidak ada data
	                    // maka jalankan tambahkan data
	                    //Intent i = new Intent(getApplicationContext(),
	                    //        TambahAuthorActivity.class);
	                    // tutup semua proses sebelumnya
	                    //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                  //  startActivity(i);
	                //}
	            } catch (JSONException e) {
	                
	            }
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					
					ListAdapter listAdapter = new SimpleAdapter
							(MainActivity.this, postList, R.layout.list_post,
									new String[]{TAG_ID, TAG_POST_TITLE, TAG_POST, TAG_AUTHOR_ID, TAG_AUTHOR, TAG_DATE}, new int[]{R.id.post_id, R.id.post_title, R.id.post, R.id.id_author, R.id.author, R.id.tanggal});
					setListAdapter(listAdapter);
				}
			});
		}
		
	}


}
