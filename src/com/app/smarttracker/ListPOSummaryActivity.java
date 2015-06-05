package com.app.smarttracker;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smarttracker.dbadapter.DatabaseHelper;
import com.app.smarttracker.slidingmenu.NavDrawerItem;
import com.app.smarttracker.slidingmenu.NavDrawerListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ListPOSummaryActivity extends Activity {

	DatabaseHelper dbHelper;
	TableLayout tablePO;
	TableRow trPOHeader;
	//	TableRow.LayoutParams layoutParams;
	TableLayout.LayoutParams layoutParams;
	RadioGroup radioGroup;
	RadioButton rdbSelected;
	String orderByCriteria = "";
	ListView poList;
	CustomCursorAdapter dataAdapter;
	public ProgressDialog prgDialog;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	String currentUserType;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view_po_details);

		dbHelper = new DatabaseHelper(this);

		poList = (ListView)findViewById(R.id.poListView);

		currentUserType = UserSession.getUserType(getApplicationContext());

		initializeSlidingMenu();
		
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			
			displayView(0);
		}
		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
		prgDialog.setMessage("Please wait...");
		// Set Cancelable as False
		prgDialog.setCancelable(false);

	}

	private void initializeSlidingMenu() {
		// TODO Auto-generated method stub
		mTitle = getTitle();
		mDrawerTitle = "View By";

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
//		// Pages
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
//		// What's hot, We  will add a counter here
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
//		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

//		if (savedInstanceState == null) {
//			// on first time display view for first nav item
//			orderByCriteria="";
//			displayListView();
//		}
	}

private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
			
		}

	}
	
public void displayView(int position) {
	// TODO Auto-generated method stub
			orderByCriteria = navMenuTitles[position];
			displayListView();
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
//			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
}

	private void displayListView() {
		// TODO Auto-generated method stub
		//			  Cursor cursor = dbHelper.fetchAllPOEntries(orderByCriteria);

		//			  // The desired columns to be bound
		//			  String[] columns = new String[] {
		//			    DatabaseHelper.KEY_PO_NUMBER,
		//			    DatabaseHelper.KEY_PRD_NAME,
		//			    DatabaseHelper.KEY_ETD,
		//			    DatabaseHelper.KEY_QTY,
		//			    };
		//			 
		//			  // the XML defined views which the data will be bound to
		//			  int[] to = new int[] { 
		//			    R.id.tvPONumber,
		//			    R.id.tvPOProductName,
		//			    R.id.tvETD,
		//			    R.id.tvQty,
		//			  };

		// create the adapter using the cursor pointing to the desired data 
		//as well as the layout information
 

		// Assign adapter to ListView
		//			  new Handler().post(new Runnable() {
		//		            @Override
		//		            public void run() {
		dbHelper.open();
		dataAdapter = new CustomCursorAdapter(ListPOSummaryActivity.this, dbHelper.fetchAllPOEntries(orderByCriteria));
		poList.setAdapter(dataAdapter);
		poList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		dbHelper.close();
		//		            }
		//		        });

		poList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view, 
					int position, long id) {
				// Get the cursor, positioned to the corresponding row in the result set
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);

				// Get the state's capital from this row in the database.
				final String poNumber = 
						cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PO_NUMBER));
				
			}
		});
	}


	private class CustomCursorAdapter extends CursorAdapter{


		public CustomCursorAdapter(Context context, Cursor c) {
			super(context, c);
			// TODO Auto-generated constructor stub
		}



		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub

			
			LinearLayout layout = (LinearLayout) view.findViewById(R.id.row);
			

			final TextView tvPONumber = (TextView) view.findViewById(R.id.tvPONumber);
			tvPONumber.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_PO_NUMBER)));

			TextView tvProductName = (TextView) view.findViewById(R.id.tvProductName);
			tvProductName.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_PRD_NAME)));

			TextView tvETD = (TextView) view.findViewById(R.id.tvETD);
			tvETD.setText("ETD: " + Utility.getUserFormattedDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ETD))));

			TextView tvETA = (TextView) view.findViewById(R.id.tvETA);
			tvETA.setText("ETA: " + Utility.getUserFormattedDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ETA))));

			//	        TextView tvQty = (TextView) view.findViewById(R.id.tvQty);
			//	        tvQty.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_QTY)));
			
			layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(), AddPODetailsActivity.class);
					String strPONumber = tvPONumber.getText().toString();
					System.out.println("Selected Row id=== "+ strPONumber);
					intent.putExtra("po_number", strPONumber);
					intent.putExtra("view_mode", true);
					startActivity(intent);
				}
			});

//			CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
//			checkBox.setOnCheckedChangeListener(mCheckChangeListener);
				        ImageButton btnEdit = (ImageButton) view.findViewById(R.id.btnEdit);
				        btnEdit.setOnClickListener(new OnClickListener() {
			
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(getApplicationContext(), AddPODetailsActivity.class);
								
								String strPONumber = tvPONumber.getText().toString();
								System.out.println("Selected Row id=== "+ strPONumber);
								
								intent.putExtra("po_number", strPONumber);
								intent.putExtra("view_mode", false);
								startActivity(intent);
			
							}
						});
				        
				        ImageButton btnDelete= (ImageButton) view.findViewById(R.id.btnDelete);
				        btnDelete.setOnClickListener(new OnClickListener() {
			
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
			//			if(currentUserType.equalsIgnoreCase("Supplier")){
							String poNumber = tvPONumber.getText().toString();
							System.out.println("Selected Row id=== "+ poNumber);
							
							deletePOEntry(poNumber);
			//			}else{
			//				Toast.makeText(ListPOSummaryActivity.this,"You are not authorised for this action.",Toast.LENGTH_SHORT).show();
			//			}
					}
				});
				        if(currentUserType.equalsIgnoreCase("Supplier")){
				        	btnEdit.setVisibility(View.VISIBLE);
				        	btnDelete.setVisibility(View.VISIBLE);
				        }else{
				        	btnEdit.setVisibility(View.GONE);
				        	btnDelete.setVisibility(View.GONE);
				        }
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			View retView = inflater.inflate(R.layout.custom_list_po_details_layout, parent, false);
			
			return retView;
		}

		private CompoundButton.OnCheckedChangeListener mCheckChangeListener = new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
//				toggleCheckBox(buttonView, isChecked);
				Toast.makeText(getApplicationContext(),  "clicked", Toast.LENGTH_SHORT).show();
			}

		};

	}

	protected void deletePOEntry(final String poNumber) {
		// TODO Auto-generated method stub
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ListPOSummaryActivity.this);

		// set title
		alertDialogBuilder.setTitle(R.string.app_name);

		// set dialog message
		alertDialogBuilder
		.setMessage("Are you sure you want to delete PO Entry?")
		.setCancelable(false)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {

				invokeDeleteWS(poNumber);

			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	public void invokeDeleteWS(final String poNumber){
		// Show Progress Dialog
		prgDialog.show();
		RequestParams params = new RequestParams();
		params.put("po_num", poNumber+"");
		params.put("mode", "delete");

		System.out.println("Parameters: "+params);

		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		//         String webServiceURL = "http://192.168.2.2:9999/useraccount/login/dologin";
		String webServiceURL = "http://www.browseinfo.in/smart/delete_po_details.php";

		client.get(webServiceURL, params, new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					System.out.println("JSON Response: "+ response);
					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true
					if(obj.getInt("status") == 1){
						// Navigate to Home screen
						Toast.makeText(getApplicationContext(), "PO Entry deleted successfully!", Toast.LENGTH_LONG).show();
						deletePOEntryFromLocalDB(Long.parseLong(poNumber));
					} 
					// Else display error message
					else{
						//                             errorMsg.setText(obj.getString("error_msg"));
						Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
					e.printStackTrace();

				}
			}
			// When the response returned by REST has Http response code other than '200'
			@Override
			public void onFailure(int statusCode, Throwable error,
					String content) {
				// Hide Progress Dialog 
				prgDialog.hide();
				// When Http response code is '404'
				if(statusCode == 404){
					Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
				} 
				// When Http response code is '500'
				else if(statusCode == 500){
					Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
				} 
				// When Http response code other than 404, 500
				else{
					Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
				}
			}
		});
	}


	protected void deletePOEntryFromLocalDB(long poNumber) {
		// TODO Auto-generated method stub
		dbHelper.open();
		if(dbHelper.deletePOEntry(poNumber) != 0){
			Toast.makeText(ListPOSummaryActivity.this, "PO entry deleted successfully.", Toast.LENGTH_SHORT).show();
//			displayPOSummary("");
			displayListView();
		}
		dbHelper.close();
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
		
		if (mDrawerToggle.onOptionsItemSelected(menuItem)) {
			return true;
		}
		
		int selectedMenuItemId = menuItem.getItemId();
		switch (selectedMenuItemId) {
		case R.id.actionbar_add_po:
			Intent nextActivityIntent = new Intent(ListPOSummaryActivity.this, AddPODetailsActivity.class);
			startActivity(nextActivityIntent);
			break;
			
		case R.id.logout:
			UserSession.clearUserName(getApplicationContext());
			Intent logout=new Intent(ListPOSummaryActivity.this,LoginActivity.class);
			startActivity(logout);
			finish();
			break;
		default:
			super.onOptionsItemSelected(menuItem);
		}
		return false;

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
