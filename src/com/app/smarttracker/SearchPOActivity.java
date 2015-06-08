package com.app.smarttracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.app.smarttracker.dbadapter.DatabaseHelper;

public class SearchPOActivity extends Activity {
	Button btnSearch, btnViewAll;
	EditText etSearchKeyword;
	ProgressDialog prgDialog;
	RadioGroup radioGroup;
	RadioButton rdbSelected;
	String currentUserType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_po_entry);
		
		currentUserType = UserSession.getUserType(getApplicationContext());

		etSearchKeyword = (EditText)findViewById(R.id.etSearchKeyword);
		radioGroup = (RadioGroup) findViewById(R.id.rgSearchType);        
				radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
				{
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// checkedId is the RadioButton selected
						rdbSelected = (RadioButton)findViewById(checkedId);
					}
				});
		btnSearch = (Button)findViewById(R.id.btnSearchPO);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(rdbSelected != null){
					if(Utility.isNull(etSearchKeyword.getText().toString())){
						etSearchKeyword.setError("Enter keyword to Search");
					}else{
						Intent nextActivityIntent = new Intent(SearchPOActivity.this, ListPOSummaryActivity.class);
						nextActivityIntent.putExtra("search_type", rdbSelected.getText().toString());
						nextActivityIntent.putExtra("search_keyword", etSearchKeyword.getText().toString());
						startActivity(nextActivityIntent);
					}
				}else{
					Toast.makeText(getApplicationContext(), "Please select Criteria", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnViewAll = (Button)findViewById(R.id.btnViewAll);
		btnViewAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent nextActivityIntent = new Intent(SearchPOActivity.this, ListPOSummaryActivity.class);
				startActivity(nextActivityIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		if(currentUserType.equalsIgnoreCase("Supplier")){
			getMenuInflater().inflate(R.menu.menu_add_po, menu);
		}else{
			getMenuInflater().inflate(R.menu.main, menu);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem){
		
		int selectedMenuItemId = menuItem.getItemId();
		switch (selectedMenuItemId) {
		case R.id.actionbar_add_po:
			Intent nextActivityIntent = new Intent(SearchPOActivity.this, AddPODetailsActivity.class);
			startActivity(nextActivityIntent);
			break;
			
		case R.id.logout:
			Utility.destroyUserSession(getApplicationContext());
			break;
		default:
			super.onOptionsItemSelected(menuItem);
		}
		return false;

	}
	
}
