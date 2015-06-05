package com.app.smarttracker;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smarttracker.dbadapter.DatabaseHelper;
import com.app.smarttracker.entity.POEntry;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ViewPODetailsActivity extends Activity {

	DatabaseHelper dbHelper;
	TableLayout tablePO;
	TableRow trPOHeader;
	//	TableRow.LayoutParams layoutParams;
	TableLayout.LayoutParams layoutParams;
	RadioGroup radioGroup;
	RadioButton rdbSelected;
	String orderByCriteria;
	String currentUserType;

	private ProgressDialog prgDialog;

	private static final Spannable.Factory spannableFactory = Spannable.Factory.getInstance();
	
	TextView thPONumber, thPrdName, thETD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_po_details);

		dbHelper = new DatabaseHelper(this);

		tablePO = (TableLayout)findViewById(R.id.table_po_details);
		trPOHeader = (TableRow)findViewById(R.id.trPODetailsHeader);
		//		layoutParams = new TableRow.LayoutParams(trPOHeader.getLayoutParams());
		//		layoutParams = new TableRow.LayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
		layoutParams = new TableLayout.LayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
		rdbSelected = (RadioButton)findViewById(R.id.rdbPONumber);

		//		radioGroup = (RadioGroup) findViewById(R.id.rdgViewBy);        
		//		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		//		{
		//			@Override
		//			public void onCheckedChanged(RadioGroup group, int checkedId) {
		//				// checkedId is the RadioButton selected
		//				rdbSelected = (RadioButton)findViewById(checkedId);
		//				displayPOSummary("");
		//			}
		//		});

		currentUserType = UserSession.getUserType(getApplicationContext());

		displayPOSummary("");

		// Instantiate Progress Dialog object
		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
		prgDialog.setMessage("Please wait...");
		// Set Cancelable as False
		prgDialog.setCancelable(false);
	}

	private void displayTableHeader() {
		// TODO Auto-generated method stub
		// TableRow Header
		tablePO.removeAllViews();		

		layoutParams.setMargins(2, 2, 2, 2);

		final TableRow trHeader = new TableRow(this);
		trHeader.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));
		trHeader.setBackgroundColor(getResources().getColor(R.color.app_dark));
		trHeader.setMinimumHeight((int) getResources().getDimension(R.dimen.table_row_min_height));
		trHeader.setGravity(Gravity.CENTER_VERTICAL);

		thPONumber = new TextView(this);
		thPrdName = new TextView(this);
		thETD = new TextView(this);
		
		thPONumber.setId(10001);
		thPONumber.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,2));
		thPONumber.setText(getResources().getString(R.string.label_po_no));
		thPONumber.setTextColor(getResources().getColor(R.color.text_white));
		thPONumber.setTextSize(getResources().getDimension(R.dimen.table_row_text_size));
		thPONumber.setPadding(0, 0, 1, 0);

		//		ImageSpan is = new ImageSpan(ViewPODetailsActivity.this, R.drawable.ic_down_aero);

		//		Drawable d = getResizedDrawable(getResources().getDrawable(R.drawable.ic_down_aero));
		//		thPONumber.setCompoundDrawables(null, null, null, d);

		thPONumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				orderByCriteria = "PO Number";
				thPONumber.setTextColor(getResources().getColor(R.color.selected));
				thPrdName.setTextColor(getResources().getColor(R.color.text_white));
				thETD.setTextColor(getResources().getColor(R.color.text_white));
				displayPOSummary(orderByCriteria);
			}
		});

		trHeader.addView(thPONumber);

		thPrdName.setId(10002);
		thPrdName.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,5));
		thPrdName.setText(getResources().getString(R.string.label_po_product_name));
		thPrdName.setTextColor(getResources().getColor(R.color.text_white));
		thPrdName.setTextSize(getResources().getDimension(R.dimen.table_row_text_size));
		thPrdName.setPadding(0, 0, 3, 0);
		thPrdName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				orderByCriteria = "Product Name";
				thPONumber.setTextColor(getResources().getColor(R.color.text_white));
				thPrdName.setTextColor(getResources().getColor(R.color.selected));
				thETD.setTextColor(getResources().getColor(R.color.text_white));

				displayPOSummary(orderByCriteria);
			}
		});
		trHeader.addView(thPrdName);

		thETD.setId(10003);
		thETD.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,2));
		thETD.setText("ETD");
		thETD.setTextColor(getResources().getColor(R.color.text_white));
		thETD.setTextSize(getResources().getDimension(R.dimen.table_row_text_size));
		thETD.setPadding(0, 0, 3, 0);
		thETD.setGravity(Gravity.CENTER);
		thETD.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				orderByCriteria = "ETD/ETA";
				thPONumber.setTextColor(getResources().getColor(R.color.text_white));
				thPrdName.setTextColor(getResources().getColor(R.color.text_white));
				thETD.setTextColor(getResources().getColor(R.color.selected));

				displayPOSummary(orderByCriteria);
			}
		});
		trHeader.addView(thETD);

		if(currentUserType.equalsIgnoreCase("Supplier")){
			TextView thAction = new TextView(this);
			thAction.setId(10004);
			thAction.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,2));
			thAction.setText("Action");
			thAction.setTextColor(getResources().getColor(R.color.text_white));
			thAction.setTextSize(getResources().getDimension(R.dimen.table_row_text_size));
			thAction.setGravity(Gravity.CENTER);
			trHeader.addView(thAction);
		}else
		{
			TextView thAction = new TextView(this);
			thAction.setId(10004);
			thAction.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,2));
			thAction.setText("ETA");
			thAction.setTextColor(getResources().getColor(R.color.text_white));
			thAction.setTextSize(getResources().getDimension(R.dimen.table_row_text_size));
			thAction.setGravity(Gravity.CENTER);
			trHeader.addView(thAction);
		}

		tablePO.addView(trHeader, 0, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

		//		final TableRow trHeaderIcon = new TableRow(this);
		//		trHeaderIcon.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));
		//		trHeaderIcon.setBackgroundColor(getResources().getColor(R.color.app_dark));
		//		trHeaderIcon.setMinimumHeight((int) getResources().getDimension(R.dimen.table_row_min_height));
		//		trHeaderIcon.setGravity(Gravity.CENTER_VERTICAL);
		//
		//		ImageView ivPONumberIcon = new ImageView(this);
		//		ivPONumberIcon.setLayoutParams(new TableRow.LayoutParams((int) getResources().getDimension(R.dimen.table_row_header_icon), (int) getResources().getDimension(R.dimen.table_row_header_icon),1));
		//		ivPONumberIcon.setBackground(getResources().getDrawable(R.drawable.ic_down_aero));
		//		trHeaderIcon.addView(ivPONumberIcon);
		//
		//		TextView tv = new TextView(this);
		//		tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,9));
		//		trHeaderIcon.addView(tv);
		//		
		//		tablePO.addView(trHeaderIcon, 1, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


	}

	private Drawable getResizedDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
		// Read your drawable from somewhere
		//		Drawable dr = getResources().getDrawable(R.drawable.somedrawable);
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		// Scale it to 50 x 50
		Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 20, 20, true));
		// Set your new, scaled drawable "d"
		return d;
	}

	public void displayPOSummary(String orderByCriteria) {
		// TODO Auto-generated method stub
		displayTableHeader();

		dbHelper.open();		
		List<POEntry> poList = dbHelper.getAllPOEntries(orderByCriteria);
		dbHelper.close();

		if(poList != null){
			int id;
			int cnt = 1;
			for(int i=0; i < poList.size(); i++){
				POEntry po = poList.get(i);
//				id = Integer.parseInt(po.getPoNumber()+"");

				final TableRow trDetails = new TableRow(this);
				trDetails.setId(cnt);
				trDetails.setLayoutParams(layoutParams);
				trDetails.setPadding(1, 3, 1, 3);
				trDetails.setMinimumHeight((int) getResources().getDimension(R.dimen.table_row_min_height));

				//				if(cnt%2 == 0){
				//					trDetails.setBackgroundColor(getResources().getColor(R.color.tablerow_color));
				//				}else{
				//					trDetails.setBackgroundColor(getResources().getColor(R.color.tablerow_border_color));
				//				}

				TextView tvPONumber = new TextView(this);
				tvPONumber.setId(Integer.parseInt((i+1)+"1"));
				tvPONumber.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,2));
				//				tvPONumber.setBackground(getResources().getDrawable(R.drawable.tablerow_border));
				tvPONumber.setText(po.getPoNumber()+"");
				tvPONumber.setTextSize(getResources().getDimension(R.dimen.table_row_text_size));
				tvPONumber.setPadding(0, 0, 2, 0);
				trDetails.addView(tvPONumber);

				TextView tvPrdName = new TextView(this);
				tvPrdName.setId(Integer.parseInt((i+1)+"2"));
				tvPrdName.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,5));
				//				tvPrdName.setBackground(getResources().getDrawable(R.drawable.tablerow_border));
				tvPrdName.setText(po.getProductName());
				tvPrdName.setTextSize(getResources().getDimension(R.dimen.table_row_text_size));
				tvPrdName.setPadding(0, 0, 2, 0);
				trDetails.addView(tvPrdName);

				TextView tvETD = new TextView(this);
				tvETD.setId(Integer.parseInt((i+1)+"3"));
				tvETD.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,2));
				//				tvQty.setBackground(getResources().getDrawable(R.drawable.tablerow_border));
				tvETD.setText(po.getETD()+"");
				tvETD.setTextSize(getResources().getDimension(R.dimen.table_row_text_size));
				tvETD.setPadding(0, 0, 2, 0);
				tvETD.setGravity(Gravity.CENTER);
				trDetails.addView(tvETD);


				//				ImageView ivView = new ImageView(this);
				//				ivView.setId(Integer.parseInt((i+1)+"4"));
				//				ivView.setBackgroundResource(R.drawable.ic_view);
				//				//				ivView.setLayoutParams(new TableRow.LayoutParams(3));
				//				//				ivView.setLayoutParams(new TableRow.LayoutParams(getResources().getDimensionPixelSize(R.dimen.table_row_icon), getResources().getDimensionPixelSize(R.dimen.table_row_icon)));
				//				ivView.setLayoutParams(new TableRow.LayoutParams(0,(int) getResources().getDimension(R.dimen.table_row_icon),1));
				//				ivView.setPadding(0, 0, 1, 0);
				//				ivView.setOnClickListener(new OnClickListener() {
				//
				//					@Override
				//					public void onClick(View v) {
				//						// TODO Auto-generated method stub
				//						Intent intent = new Intent(getApplicationContext(), AddPODetailsActivity.class);
				//						System.out.println("Selected Row id=== "+trDetails.getId());
				//						intent.putExtra("po_number", trDetails.getId());
				//						intent.putExtra("view_mode", true);
				//						startActivity(intent);
				//
				//					}
				//				});
				//				trDetails.addView(ivView);
				if(currentUserType.equalsIgnoreCase("Supplier")){
					ImageView ivEdit = new ImageView(this);
					ivEdit.setId(Integer.parseInt((i+1)+"5"));
					ivEdit.setBackgroundResource(R.drawable.ic_edit_doc);
					//				ivEdit.setLayoutParams(new TableRow.LayoutParams(3));
					//				ivEdit.setLayoutParams(new TableRow.LayoutParams(getResources().getDimensionPixelSize(R.dimen.table_row_icon), getResources().getDimensionPixelSize(R.dimen.table_row_icon)));
					ivEdit.setLayoutParams(new TableRow.LayoutParams(0,(int) getResources().getDimension(R.dimen.table_row_icon),1));
					ivEdit.setPadding(0, 0, 3, 0);
					ivEdit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getApplicationContext(), AddPODetailsActivity.class);
							
							String strPONumber = ((TextView) trDetails.getChildAt(0)).getText().toString();
							System.out.println("Selected Row id=== "+ strPONumber);
							
							intent.putExtra("po_number", strPONumber);
							intent.putExtra("view_mode", false);
							startActivity(intent);

						}
					});
					trDetails.addView(ivEdit);

					ImageView ivDelete = new ImageView(this);
					ivDelete.setId(Integer.parseInt((i+1)+"6"));
					ivDelete.setBackgroundResource(R.drawable.ic_delete);
					//				ivDelete.setLayoutParams(new TableRow.LayoutParams(3));
					//				ivDelete.setLayoutParams(new TableRow.LayoutParams(getResources().getDimensionPixelSize(R.dimen.table_row_icon), getResources().getDimensionPixelSize(R.dimen.table_row_icon)));
					ivDelete.setLayoutParams(new TableRow.LayoutParams(0,(int) getResources().getDimension(R.dimen.table_row_icon),1));
					ivDelete.setPadding(0, 0, 2, 0);
					ivDelete.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(currentUserType.equalsIgnoreCase("Supplier")){
								String poNumber = ((TextView) trDetails.getChildAt(0)).getText().toString();
								System.out.println("Selected Row id=== "+ poNumber);
								
								deletePOEntry(poNumber);
							}else{
								Toast.makeText(ViewPODetailsActivity.this,"You are not authorised for this action.",Toast.LENGTH_SHORT).show();
							}
						}
					});
					trDetails.addView(ivDelete);
				}else
				{
					TextView tvETA = new TextView(this);
					tvETA.setId(Integer.parseInt((i+1)+"3"));
					tvETA.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,2));
					//					tvQty.setBackground(getResources().getDrawable(R.drawable.tablerow_border));
					tvETA.setText(po.getETA()+"");
					tvETA.setTextSize(getResources().getDimension(R.dimen.table_row_text_size));
					tvETA.setPadding(0, 0, 2, 0);
					tvETA.setGravity(Gravity.CENTER);
					trDetails.addView(tvETA);
				}

				trDetails.setGravity(Gravity.CENTER_VERTICAL);
				trDetails.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), AddPODetailsActivity.class);
						String strPONumber = ((TextView) trDetails.getChildAt(0)).getText().toString();
						System.out.println("Selected Row id=== "+ strPONumber);
						intent.putExtra("po_number", strPONumber);
						intent.putExtra("view_mode", true);
						startActivity(intent);

					}
				});

				tablePO.addView(trDetails, cnt++, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

				final TableRow trBorder = new TableRow(getApplicationContext());
				trBorder.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
				trBorder.setBackground(getResources().getDrawable(R.drawable.tablerow_border));

				tablePO.addView(trBorder, cnt++, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

			}
		}
	}


	protected void deletePOEntry(final String poNumber) {
		// TODO Auto-generated method stub
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ViewPODetailsActivity.this);

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
			Toast.makeText(ViewPODetailsActivity.this, "PO entry deleted successfully.", Toast.LENGTH_SHORT).show();
			displayPOSummary("");
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
		super.onOptionsItemSelected(menuItem);
		int selectedMenuItemId = menuItem.getItemId();
		switch (selectedMenuItemId) {
		case R.id.actionbar_add_po:
			Intent nextActivityIntent = new Intent(ViewPODetailsActivity.this, AddPODetailsActivity.class);
			startActivity(nextActivityIntent);
			break;
		case R.id.logout:
			UserSession.clearUserName(getApplicationContext());
			Intent logout=new Intent(ViewPODetailsActivity.this,LoginActivity.class);
			startActivity(logout);
			finish();
			break;
		default:
			break;
		}
		return false;


	}

}
