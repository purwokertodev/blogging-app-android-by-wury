package com.wury.blog.author;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wury.blog.json.JsonParser;
import com.wury.blog.util.SessionManager;
import com.wury.blog.util.SessionManagerImpl;
import com.wury.bukutamu.R;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DaftarPostByAuthorActivity extends ListActivity{
	
	private String id;
	
	private ListView listView;
	private ProgressDialog progressDialog;
	private JsonParser parser = new JsonParser();
	private List<HashMap<String, String>> postList;
	private JSONArray data = null;
	
	private SessionManager sessionManager;
	
	//private static final String URL_POST = "http://10.0.2.2/Blogging/page/model/android_app/all_post_by_author.php";
	private static final String URL_POST = "http://192.168.1.132/Blogging/page/model/android_app/all_post_by_author.php";
	
	//json node
	private static final String TAG_DATA = "data";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ID = "id";
	private static final String TAG_POST_TITLE = "judul";
	private static final String TAG_POST = "isi";
	private static final String TAG_DATE = "tanggal";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daftar_post_by_author);
		sessionManager = new SessionManagerImpl(getApplicationContext());
		sessionManager.checkLogin();
		
		HashMap<String, String> val = sessionManager.getUserDetails();
		id = val.get(SessionManager.KEY_ID);
		
		postList = new ArrayList<HashMap<String,String>>();
		
		new LoadPost().execute();
		
		listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				String postId = ((TextView) findViewById(R.id.post_id_by_author)).getText().toString();
				Intent in = new Intent(DaftarPostByAuthorActivity.this, UbahPostActivity.class);
				in.putExtra("id_post", postId);
				startActivityForResult(in, 100);
			}
		});	
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == 100){
			Intent in = getIntent();
			startActivity(in);
			finish();
		}
	}
	
	
	class LoadPost extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(DaftarPostByAuthorActivity.this);
			progressDialog.setTitle("Connecting to server");
			progressDialog.setMessage("Please wait..");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair(TAG_ID, id));
			JSONObject jo = parser.makeHttpRequest(URL_POST, "GET", list);
			Log.d("response", jo.toString());
			
			try{
				int success = jo.getInt(TAG_SUCCESS);
				if(success == 1){
					data = jo.getJSONArray(TAG_DATA);
					for(int i=0;i<data.length();i++){
						JSONObject c = data.getJSONObject(i);
						
						String id = c.getString(TAG_ID);
                        String judul = c.getString(TAG_POST_TITLE);
                        String isi = c.getString(TAG_POST);
                        String tanggal = c.getString(TAG_DATE);
                        
                        // buat new hashmap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_POST_TITLE, judul);
                        map.put(TAG_POST, isi);
                        map.put(TAG_DATE, tanggal);
 
                        // masukan HashList ke ArrayList
                        postList.add(map);
					}
				}
			}catch(JSONException ex){
				
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
					ListAdapter adapter = new SimpleAdapter(getApplicationContext(),postList,
							R.layout.list_post_by_author, new String[]{TAG_ID, TAG_POST_TITLE}, new int[]{R.id.post_id_by_author, R.id.post_title_by_author});
					setListAdapter(adapter);
				}
			});
		}
		
	}

}
