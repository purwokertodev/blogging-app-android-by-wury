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

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DaftarAuthorActivity extends ListActivity {
	
	private ProgressDialog progressDialog;
	private JsonParser jsonParser = new JsonParser();
	private ArrayList<HashMap<String, String>> authorList;
	
	//private static final  String urlAllAuthor = "http://10.0.2.2/Blogging/page/model/android_app/authors.php";
	private static final  String urlAllAuthor = "http://192.168.1.132/Blogging/page/model/android_app/authors.php";
	
	  // JSON Node
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_DATA = "data";
    private static final String TAG_PID = "id";
    private static final String TAG_NAME = "nama";
    
    JSONArray data = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daftar_author);
		authorList = new ArrayList<HashMap<String,String>>();
		new LoadAuthor().execute();
		
		ListView lv = getListView();
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();
				
				Intent in = new Intent(getApplicationContext(), EditAuthorActivity.class);
				in.putExtra(TAG_PID, pid);
				startActivityForResult(in, 100);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 100){
			Intent in = getIntent();
			finish();
			startActivity(in);
		}
	}
	
	
	class LoadAuthor extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(DaftarAuthorActivity.this);
			progressDialog.setTitle("Connecting to server");
			progressDialog.setMessage("Load data..");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			List<NameValuePair> namePairs = new ArrayList<NameValuePair>();
			JSONObject jo = jsonParser.makeHttpRequest(urlAllAuthor, "GET", namePairs);
			 Log.d("Semua data: ", jo.toString());
			 
	            try {
	                // cek jika tag success
	                int success = jo.getInt(TAG_SUCCESS);
	 
	                if (success == 1) {
	                    // data ditemukan
	                    // ambil array data
	                    data = jo.getJSONArray(TAG_DATA);
	 
	                    // tampilkan perulangan semua produk
	                    for (int i = 0; i < data.length(); i++) {
	                        JSONObject c = data.getJSONObject(i);
	 
	                        // simpan pada variabel
	                        String id = c.getString(TAG_PID);
	                        String name = c.getString(TAG_NAME);
	 
	                        // buat new hashmap
	                        HashMap<String, String> map = new HashMap<String, String>();
	 
	                        // key => value
	                        map.put(TAG_PID, id);
	                        map.put(TAG_NAME, name);
	 
	                        // masukan HashList ke ArrayList
	                        authorList.add(map);
	                    }
	                } else {
	                    // jika tidak ada data
	                    // maka jalankan tambahkan data
	                    Intent i = new Intent(getApplicationContext(),
	                            TambahAuthorActivity.class);
	                    // tutup semua proses sebelumnya
	                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                    startActivity(i);
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
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					
					ListAdapter listAdapter = new SimpleAdapter
							(DaftarAuthorActivity.this, authorList, R.layout.list_author, new String[]{TAG_PID, TAG_NAME}, new int[]{R.id.pid, R.id.nama});
					setListAdapter(listAdapter);
				}
			});
		}
		
	}

}
