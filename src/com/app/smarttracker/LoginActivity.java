package com.app.smarttracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.smarttracker.dbadapter.DatabaseHelper;
import com.app.smarttracker.entity.POEntry;
import com.app.smarttracker.entity.UserAuth;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class LoginActivity extends Activity {

	Button btnLogin;
	EditText etUsername;
	EditText etPassword;
	DatabaseHelper db;
	ProgressDialog prgDialog;
	String userType="";

	boolean b = true;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.login_page_twitter);
		
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);
		db = new DatabaseHelper(getApplicationContext());
//		db.open();
//		db.insertInitialData();
//		db.close();
		//		etPassword.addTextChangedListener(new TextWatcher() {
		//
		//			@Override
		//			public void onTextChanged(CharSequence s, int start, int before,
		//					int count) {
		//				// TODO Auto-generated method stub
		//				validPassword(6, 32, etPassword);
		//
		//			}
		//
		//			@Override
		//			public void beforeTextChanged(CharSequence s, int start, int count,
		//					int after) {
		//				// TODO Auto-generated method stub
		//
		//			}
		//
		//			@Override
		//			public void afterTextChanged(Editable s) {
		//				// TODO Auto-generated method stub
		//				validPassword(6, 32, etPassword);
		//
		//			}
		//		});

		// return matcher.matches();

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				validateUser();	
			}

		});

		// Instantiate Progress Dialog object
		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
		prgDialog.setMessage("Please wait...");
		// Set Cancelable as False
		prgDialog.setCancelable(false);

	}

	protected boolean validateUser() {
		// TODO Auto-generated method stub

		String strUsername = etUsername.getText().toString().trim();
		String strPassword = etPassword.getText().toString().trim();

		if(Utility.isNull(strUsername)){
			etUsername.setError("Enter Username");
			b=false;
		}
		if(Utility.isNull(strPassword)){
			etPassword.setError("Enter Password");
			b=false;
		}
		if(b){
			RequestParams params = new RequestParams();
			// Put Http parameter username with value of Email Edit View control
			params.put("userName", strUsername);
			// Put Http parameter password with value of Password Edit Value control
			params.put("password", strPassword);
			// Invoke RESTful Web Service with Http parameters
			invokeLoginWS(params);

		}
		return b;
	}

	/**
	 * Method that performs RESTful webservice invocations
	 * 
	 * @param params
	 */
	public void invokeLoginWS(RequestParams params){
		// Show Progress Dialog
		prgDialog.show();
		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		//         String webServiceURL = "http://192.168.2.2:9999/useraccount/login/dologin";
		String webServiceURL = "http://www.browseinfo.in/smart/login.php";

		client.get(webServiceURL, params, new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true
					if(obj.getInt("status") == 1){
						Toast.makeText(getApplicationContext(), "Logged in successfully!", Toast.LENGTH_LONG).show();
						// Navigate to Home screen
						userType = obj.getString("type");                             
						navigatetoHomeActivity();
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


	protected void navigatetoHomeActivity() {
		// TODO Auto-generated method stub
		// Save User session--
		UserSession.setUserName(getApplicationContext(), etUsername.getText().toString());
		UserSession.setUserType(getApplicationContext(), userType);
		
		RequestParams params = new RequestParams();
		params.put("mode", "view");
		loadRecordsFromServer(params);
		
	}

	private void loadRecordsFromServer(RequestParams params) {
		// TODO Auto-generated method stub
//		prgDialog.show();
		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		//         String webServiceURL = "http://192.168.2.2:9999/useraccount/login/dologin";
		String webServiceURL = "http://www.browseinfo.in/smart/display_po_details.php";

		client.get(webServiceURL, params, new AsyncHttpResponseHandler() {
			// When the response returned by REST has Http response code '200'
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
//				prgDialog.hide();
				try {
					System.out.println("JSON Response: "+ response);
					// JSON Object
					JSONObject obj = new JSONObject(response);
					// When the JSON response has status boolean value assigned with true
					if(obj.getInt("status") == 1){
						// Navigate to Home screen
//						Toast.makeText(getApplicationContext(), "PO Entry saved successfully!", Toast.LENGTH_LONG).show();
						
						parseJSONResponse(obj);
						
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
//				prgDialog.hide();
				// When Http response code is '404'
				if(statusCode == 404){
					Toast.makeText(getApplicationContext(), "Requested resource not found.", Toast.LENGTH_LONG).show();
				} 
				// When Http response code is '500'
				else if(statusCode == 500){
					Toast.makeText(getApplicationContext(), "Something went wrong at server end.", Toast.LENGTH_LONG).show();
				} 
				// When Http response code other than 404, 500
				else{
					Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Your device might not be connected to Internet or remote server is not up and running.]", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	protected void parseJSONResponse(JSONObject response) {
		// TODO Auto-generated method stub
		try {
			
			// Delete all existing entries from local database. and fetch POEntries from Server.
			db.open();
			db.deleteAllPOEntry();
			db.close();
			
			JSONArray jsonAry = response.getJSONArray("records");
			
			if(jsonAry.length() == 0){
				Intent nextActivityIntent = new Intent(LoginActivity.this, ListPOSummaryActivity.class);
//				Intent nextActivityIntent = new Intent(LoginActivity.this, ViewPODetailsActivity.class);
				nextActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(nextActivityIntent);
				finish();
			}else{
				db.open();

				for(int i=0; i<jsonAry.length(); i++){
					JSONObject data1 = jsonAry.getJSONObject(i);
					JSONObject data = data1.getJSONObject("data");
					
					POEntry poEntry = new POEntry();
	//				poEntry.setId(data.getLong("id"));
					poEntry.setPoNumber(data.getLong("po_num"));
					poEntry.setSuplierName(data.getString("supplier_name"));
					poEntry.setProductName(data.getString("prd_name"));
					poEntry.setQuantity(data.getDouble("quantity"));
					poEntry.setPaymentTerms(data.getString("payment_terms"));
					poEntry.setETD(data.getString("etd"));
					poEntry.setATD(data.getString("atd"));
					poEntry.setETA(data.getString("eta"));
					poEntry.setATA(data.getString("ata"));
					poEntry.setDeparturePortName(data.getString("departure_port_name"));
					poEntry.setArrivalPortName(data.getString("arrival_port_name"));
					poEntry.setDocSent(data.getInt("doc_sent")== 1? true:false);
					poEntry.setDocTrackNumber(data.getString("doc_track_no"));
					poEntry.setDocSentDate(data.getString("shipping_doc_sent_date"));
					poEntry.setDocRecvDate(data.getString("shipping_doc_rec_date"));
					poEntry.setCustomClearanceDate(data.getString("custom_clearance_date"));
					poEntry.setFactoryArrivalDate(data.getString("material_factory_date"));
					poEntry.setSailingDate(data.getString("sailing_date"));
					poEntry.setLastPortName(data.getString("last_port_name"));
					poEntry.setDestinationPortDate(data.getString("destination_port_date"));
					poEntry.setSupplierRemarks(data.getString("sup_remarks"));
					poEntry.setCustomerRemarks(data.getString("cust_remarks"));
					poEntry.setDeleted(data.getInt("is_deleted")== 1? true:false);
					poEntry.setEditCount(data.getInt("edit_count"));
					poEntry.setSynced(data.getInt("is_synced")== 1? true:false);
					poEntry.setTimestamp(data.getString("created_date"));
					
					db.insertPOEntry(poEntry);
					
					Intent nextActivityIntent = new Intent(LoginActivity.this, ListPOSummaryActivity.class);
//					Intent nextActivityIntent = new Intent(LoginActivity.this, ViewPODetailsActivity.class);
					nextActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(nextActivityIntent);
					finish();
				}
				db.close();
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error while parsing JSON data."+e.getMessage());
		}
	}

	public boolean validPassword(int Minlen, int Maxlen, EditText etPassword) {
		// TODO Auto-generated method stub
		if (etPassword.getText().toString().length() == 0) {
			etPassword.setError("Password can not be blank.");
			b = false;

		} else if (etPassword.getText().length() < Minlen
				|| etPassword.getText().length() > Maxlen)

		{
			etPassword.setError("Password must be 6-32 character.");
			b = false;

		} else {
			Pattern pattern;
			Matcher matcher;

			// final String PASSWORD_PATTERN =
			// "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,32})";
			final String PASSWORD_PATTERN = "(?=.*\\d).+$";
			pattern = Pattern.compile(PASSWORD_PATTERN);
			matcher = pattern.matcher(etPassword.getText().toString().trim());
			b = matcher.matches();
			if (b == false) {
				etPassword.setError("Password must contain atleast one digit.");
			} else {

				// return matcher.matches();

				final String PASSWORD_PATTERN1 = "(?=.*[a-z]).+$";
				pattern = Pattern.compile(PASSWORD_PATTERN1);
				matcher = pattern.matcher(etPassword.getText().toString()
						.trim());
				b = matcher.matches();
				if (b == false) {
					etPassword.setError("password must contain one lowercase alphabet.");
				}

				else {
					final String PASSWORD_PATTERN3 = "(?=.*[@#$%]).+$";

					pattern = Pattern.compile(PASSWORD_PATTERN3);
					matcher = pattern.matcher(etPassword.getText().toString()
							.trim());
					b = matcher.matches();
					if (b == false) {
						etPassword.setError("Password must contain atleast one special character.");
					}
				}
			}

		}
		return b;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

//	private class LogOperation extends AsyncTask<Params, Progress, Result>{
//		
//	}
}
