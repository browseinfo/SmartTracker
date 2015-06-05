package com.app.smarttracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.smarttracker.dbadapter.DatabaseHelper;
import com.app.smarttracker.entity.POEntry;
import com.app.smarttracker.entity.UserAuth;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button btnLogin;
	DatabaseHelper dbHelper;
	ProgressDialog prgDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);
		
//		dbHelper = new DatabaseHelper(getApplicationContext());
		// Instantiate Progress Dialog object
//				prgDialog = new ProgressDialog(this);
//				// Set Progress Dialog Text
//				prgDialog.setMessage("Please wait...");
//				// Set Cancelable as False
//				prgDialog.setCancelable(false);
				 
		if(!UserSession.getUserName(getApplicationContext()).equalsIgnoreCase("")){
//			RequestParams params = new RequestParams();
//			params.put("mode", "view");
//			loadRecordsFromServer(params);
			
//			Intent nextActivityIntent = new Intent(MainActivity.this, ViewPODetailsActivity.class);
			Intent nextActivityIntent = new Intent(MainActivity.this, ListPOSummaryActivity.class);
			nextActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(nextActivityIntent);
			finish();
		}
		
		btnLogin = (Button)findViewById(R.id.btnHomeLogin);
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent nextActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(nextActivityIntent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
