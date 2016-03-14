package com.wury.blog;

import java.util.HashMap;

import com.wury.blog.author.DaftarPostByAuthorActivity;
import com.wury.blog.author.TambahPostActivity;
import com.wury.blog.util.SessionManager;
import com.wury.blog.util.SessionManagerImpl;
import com.wury.bukutamu.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DashboardAuthorActivity extends Activity{
	
	private TextView txtStatusPengguna;
	
	private Button btnDaftarPost, btnTambahPost, btnUbahProfil, btnLogout;
	
	private SessionManager sessionManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_author_dashboard);
		
		sessionManager = new SessionManagerImpl(getApplicationContext());
		sessionManager.checkLogin();
		
		HashMap<String, String> val = sessionManager.getUserDetails();
		String statusPenggunaName = val.get(SessionManager.KEY_USER_NAME);
		
		txtStatusPengguna = (TextView) findViewById(R.id.txt_status_pengguna_name);
		txtStatusPengguna.setText("Welcome : "+statusPenggunaName);
		
		btnDaftarPost = (Button) findViewById(R.id.btn_daftar_post);
		btnTambahPost = (Button) findViewById(R.id.btn_tambah_post);
		btnUbahProfil = (Button) findViewById(R.id.btn_ubah_profil);
		btnLogout = (Button) findViewById(R.id.btn_logout);
		
		//Tampilkan daftar post berdasarkan author
		btnDaftarPost.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent in = new Intent(getApplicationContext(), DaftarPostByAuthorActivity.class);
				startActivity(in);
			}
		});
		
		//Tambah post baru
		btnTambahPost.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(getApplicationContext(), TambahPostActivity.class);
				startActivity(in);
			}
		});
		
		//Ubah profil
		btnUbahProfil.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		btnLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder ab = new AlertDialog.Builder(DashboardAuthorActivity.this);
				ab.setTitle("Logout");
				ab.setMessage("Anda yakin ?");
				ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						sessionManager.logout();
					}
				});
				ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// tidak lakukan apa-apa
					}
				});
				ab.show();
			}
		});
		
	}
	
	

}
