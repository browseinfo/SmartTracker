package com.app.smarttracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smarttracker.dbadapter.DatabaseHelper;
import com.app.smarttracker.entity.POEntry;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AddPODetailsActivity extends Activity {

	private final int MSG_CODE_BLANK = 0;
	private final int MSG_CODE_INVALID = 1;

	DatabaseHelper poDB;

	EditText etDocTrackNumber;
	EditText etPONumber;
	//	EditText etPrdName;
	EditText etQty;
	EditText etETD_ATD;
	EditText etETA_ADA;
	EditText etSailingDate;
	EditText etLastPortName;
	EditText etDestinationPortDate;
	EditText etCustRemarks;
	EditText etSuplRemarks;
	EditText etClickedField;

	EditText etSupplierName;
	EditText etDeparturePortName;
	EditText etArrivalPortName;
	EditText etDocSentDate;
	EditText etDocRecvDate;
	EditText etCustomClearanceDate;
	EditText etFactoryArrivalDate;

	LinearLayout layoutSuplRemarks, layoutCustRemarks; 
	CheckBox chkDocSent;
	TextView tvDocTrackNumber;
	TextView tvCreatedDate;
	Spinner spSuplName, spProductName, spPaymentTerm;
	String selectedSuplName, selectedProductName, selectedPaymentTerms;
	Button btnSave, btnCancel;
	long poEntryId;
	String currentUserType;
	int editCount = 0;

	String[] aryProductName;
	String[] arySupplierName;
	String[] aryPaymentTerms;

	ProgressDialog prgDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_po_details);

		poDB = new DatabaseHelper(getApplicationContext());

		tvCreatedDate = (TextView) findViewById(R.id.tvPOTimestamp);
		etPONumber = (EditText)findViewById(R.id.etPONumber);
		//		etPrdName = (EditText)findViewById(R.id.etPOProductName);
		etQty = (EditText)findViewById(R.id.etQuantity);
		etETD_ATD = (EditText)findViewById(R.id.etPOEtdAtd);
		etETA_ADA = (EditText)findViewById(R.id.etPOEtaAta);
		etSailingDate = (EditText)findViewById(R.id.etSailingDate);
		etLastPortName = (EditText)findViewById(R.id.etLastPortName);
		etDestinationPortDate = (EditText)findViewById(R.id.etDestinationPortDate);
		etCustRemarks = (EditText)findViewById(R.id.etCustRemarks);
		etSuplRemarks = (EditText)findViewById(R.id.etSuplRemarks);

		etDeparturePortName = (EditText)findViewById(R.id.etDeparturePortName);
		etArrivalPortName = (EditText)findViewById(R.id.etArrivalPortName);
		etDocSentDate = (EditText)findViewById(R.id.etDocSentDate);
		etDocRecvDate = (EditText)findViewById(R.id.etDocRecvDate);
		etCustomClearanceDate = (EditText)findViewById(R.id.etCustomClearanceDate);
		etFactoryArrivalDate = (EditText)findViewById(R.id.etFactoryArrivalDate);

		etLastPortName = (EditText)findViewById(R.id.etLastPortName);
		etDestinationPortDate = (EditText)findViewById(R.id.etDestinationPortDate);
		etCustRemarks = (EditText)findViewById(R.id.etCustRemarks);
		etSuplRemarks = (EditText)findViewById(R.id.etSuplRemarks);

		spProductName = (Spinner)findViewById(R.id.spinnerProductName);
		spSuplName = (Spinner)findViewById(R.id.spinnerSuplName);
		spPaymentTerm = (Spinner)findViewById(R.id.spinnerPaymentTerms);

		btnSave = (Button)findViewById(R.id.btnSave);
		btnCancel = (Button)findViewById(R.id.btnCancel);

		tvDocTrackNumber = (TextView)findViewById(R.id.tvDocTrackNo);
		etDocTrackNumber = (EditText)findViewById(R.id.etDocTrackNo);
		chkDocSent = (CheckBox)findViewById(R.id.chkDocSent);

		layoutSuplRemarks = (LinearLayout) findViewById(R.id.layout_supl_remarks);
		layoutCustRemarks = (LinearLayout) findViewById(R.id.layout_cust_remarks);

		currentUserType = UserSession.getUserType(getApplicationContext());

		initializeSpinner();

		Bundle b = new Bundle();
		b = getIntent().getExtras();

		long selectedEntryPONumber = 0;

		if(b != null && b.containsKey("po_number")){
			selectedEntryPONumber = Long.parseLong(b.getString("po_number"));
		}

		boolean isViewMode = false;
		if(b != null && b.containsKey("view_mode")){
			isViewMode = b.getBoolean("view_mode");
		}

		if(selectedEntryPONumber != 0){
			setFieldData(selectedEntryPONumber);	// set values if in EditMode	
		}

		if(currentUserType.equalsIgnoreCase("customer")){
			isViewMode = true;
		}
		setFieldsEnableOrDisable(!isViewMode);	// if ViewMode= true, disable all fields; else enable all fields.
		if(selectedEntryPONumber != 0){
			setFieldsNonEditable(!isViewMode);	// In EditMode some fields are not allowed to be edited. if isViewMode = false, mode=edit/add
		}else{
			tvCreatedDate.setText(Utility.getFormattedDate(Utility.getCurrentDate(),"dd/MM/yyyy hh:mm:ss","dd/MM/yyyy"));
		}

		setCheckboxClickListener();
		setDatePickerDialog();
		//		setETD_ATD_DatePickerDialog();
		//		setETA_ADA_DatePickerDialog();
		//		setSailingDatePickerDialog();
		//		setDestinationPortDatePickerDialog();
		setSaveButtonClickListener();
		setSaveCancelClickListener();

		// Instantiate Progress Dialog object
		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
		prgDialog.setMessage("Please wait...");
		// Set Cancelable as False
		prgDialog.setCancelable(false);

	}

	private void setFieldsNonEditable(boolean flag) {
		// TODO Auto-generated method stub
		etPONumber.setEnabled(flag);

		if(currentUserType.equalsIgnoreCase("Supplier")){
			btnSave.setVisibility(flag == false? View.GONE:View.VISIBLE);
			btnCancel.setVisibility(flag == false? View.GONE:View.VISIBLE);
			btnSave.setEnabled(flag);
			btnCancel.setEnabled(flag);

			etCustRemarks.setEnabled(false);
			etSuplRemarks.setEnabled(flag);
			etSuplRemarks.setClickable(flag);
			etSuplRemarks.setCursorVisible(flag);
			etSuplRemarks.setFocusableInTouchMode(flag);
			//			etSuplRemarks.setBackground(getResources().getDrawable(R.drawable.edittext_focused));
			if(flag == true){
				etSuplRemarks.requestFocus();
			}
			//			layoutCustRemarks.setVisibility(View.GONE);
			layoutSuplRemarks.setEnabled(flag);

			((EditText)layoutSuplRemarks.getChildAt(1)).setClickable(flag);
			((EditText)layoutSuplRemarks.getChildAt(1)).setCursorVisible(flag);

		}else{
			etSuplRemarks.setEnabled(false);
			etCustRemarks.setEnabled(true);
			etCustRemarks.setClickable(true);
			etCustRemarks.setCursorVisible(true);
			etCustRemarks.setFocusableInTouchMode(true);
			etCustRemarks.setBackground(getResources().getDrawable(R.drawable.edittext_focused));
			etCustRemarks.requestFocus();
			//			layoutSuplRemarks.setVisibility(View.GONE);
			layoutCustRemarks.setEnabled(true);
			((EditText)layoutCustRemarks.getChildAt(1)).setClickable(true);
			((EditText)layoutCustRemarks.getChildAt(1)).setCursorVisible(true);

			btnSave.setVisibility(View.VISIBLE);
			btnCancel.setVisibility(View.VISIBLE);
			btnSave.setEnabled(true);
			btnCancel.setEnabled(true);
		}
	}

	public void setFieldData(long poNumber) {
		// TODO Auto-generated method stub
		POEntry po = new POEntry();
		poDB.open();
		po = poDB.getPOEntry(poNumber);
		poDB.close();

		poEntryId = po.getId();

		tvCreatedDate.setText(Utility.getFormattedDate(po.getTimestamp(),"yyyy-MM-dd hh:mm:ss","dd/MM/yyyy"));
		etPONumber.setText(po.getPoNumber()+"");
		//		etPrdName.setText(po.getProductName());
		selectedProductName = po.getProductName();
		selectedSuplName = po.getSuplierName();
		selectedPaymentTerms = po.getPaymentTerms();
		setSpinnerItemSelection(aryProductName, selectedProductName, spProductName);
		setSpinnerItemSelection(arySupplierName, selectedSuplName, spSuplName);
		setSpinnerItemSelection(aryPaymentTerms, selectedPaymentTerms, spPaymentTerm);

		etQty.setText(String.format("%.03f", po.getQuantity()));
		//		etETD_ATD.setText(po.getATD());
		etETD_ATD.setText(Utility.getUserFormattedDate(po.getETD()));
		//		etETA_ADA.setText(po.getATA());
		etETA_ADA.setText(Utility.getUserFormattedDate(po.getETA()));

		etDeparturePortName.setText(po.getDeparturePortName());
		etArrivalPortName.setText(po.getArrivalPortName());
		chkDocSent.setChecked(po.isDocSent());
		etDocTrackNumber.setText((po.isDocSent() == false)? "":po.getDocTrackNumber()+"");
		setDocTrackNumberVisibility(chkDocSent.isChecked());

		etDocSentDate.setText(Utility.getUserFormattedDate(po.getDocSentDate()));
		etDocRecvDate.setText(Utility.getUserFormattedDate(po.getDocRecvDate()));
		etCustomClearanceDate.setText(Utility.getUserFormattedDate(po.getCustomClearanceDate()));
		etFactoryArrivalDate.setText(Utility.getUserFormattedDate(po.getFactoryArrivalDate()));

		etSailingDate.setText(Utility.getUserFormattedDate(po.getSailingDate()));
		etLastPortName.setText(po.getLastPortName());
		etDestinationPortDate.setText(Utility.getUserFormattedDate(po.getDestinationPortDate()));
		etSuplRemarks.setText(po.getSupplierRemarks());
		etCustRemarks.setText(po.getCustomerRemarks());

		editCount = po.getEditCount();

	}

	private void setSpinnerItemSelection(String[] arrayList, String dbValue, Spinner spinner) {
		// TODO Auto-generated method stub
		int selectionId = 0;
		for(int i=0; i< arrayList.length; i++){
			if(arrayList[i].equalsIgnoreCase(dbValue)){
				selectionId = i;
				break;
			}
		}
		spinner.setSelection(selectionId, true);

	}

	private void setFieldsEnableOrDisable(boolean flagEnable) {
		// TODO Auto-generated method stub
		LinearLayout layout = (LinearLayout)findViewById(R.id.parentLayout);
		for(int i=0; i<layout.getChildCount();i++){
			layout.getChildAt(i).setEnabled(flagEnable);
			if(layout.getChildAt(i).getClass() == LinearLayout.class){
				LinearLayout l = ((LinearLayout) layout.getChildAt(i));
				for(int j=0; j < l.getChildCount(); j++){
					l.getChildAt(j).setEnabled(flagEnable);
					if(l.getChildAt(j).getClass() == EditText.class){
						((EditText)l.getChildAt(j)).setClickable(flagEnable);
						((EditText)l.getChildAt(j)).setCursorVisible(flagEnable);
						if(!flagEnable){
							((EditText)l.getChildAt(j)).setBackground(getResources().getDrawable(R.drawable.edittext_normal));
							((EditText)l.getChildAt(j)).setTextColor(getResources().getColor(R.color.disabled_color));

						}
						//						if(l.getChildAt(j).isFocusable()){
						//							l.getChildAt(j).setFocusable(flagEnable);
						//						}
					}else if(l.getChildAt(j).getClass() == Button.class){
						if(!flagEnable){
							l.getChildAt(j).setVisibility(View.GONE);
						}
					}else if(l.getChildAt(j).getClass() == TextView.class){
						l.getChildAt(j).setEnabled(true);
					}else if(l.getChildAt(j).getClass() == Spinner.class){
						((Spinner)l.getChildAt(j)).setAlpha(1.0f);

					}
				}
			}
		}
		if(flagEnable){	//viewmode = false
			//			//			etPONumber.setBackground(getResources().getDrawable(R.drawable.edittext_focused));
			etPONumber.requestFocus();
			if(currentUserType.equalsIgnoreCase("Supplier")){
				layoutSuplRemarks.setVisibility(View.VISIBLE);
				layoutCustRemarks.setVisibility(View.GONE);
			}else{
				layoutCustRemarks.setVisibility(View.VISIBLE);
				layoutSuplRemarks.setVisibility(View.GONE);
			}
		}else{ // viewmode = true
			layoutSuplRemarks.setVisibility(View.VISIBLE);
			layoutCustRemarks.setVisibility(View.VISIBLE);
		}

	}

	private void initializeSpinner() {
		// TODO Auto-generated method stub
		aryProductName = getResources().getStringArray(R.array.product_name_list);
		ArrayAdapter<String> productAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, aryProductName);
		spProductName.setAdapter(productAdapter);

		arySupplierName = getResources().getStringArray(R.array.supplier_name_list);
		ArrayAdapter<String> supplierAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arySupplierName);
		spSuplName.setAdapter(supplierAdapter);

		aryPaymentTerms = getResources().getStringArray(R.array.payment_terms_list);
		ArrayAdapter<String> paymentTermsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, aryPaymentTerms);
		spPaymentTerm.setAdapter(paymentTermsAdapter);
	}

	private void setDatePickerDialog() {
		// TODO Auto-generated method stub
		etETD_ATD.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etClickedField = etETD_ATD;
				new DatePickerDialog(AddPODetailsActivity.this, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		etETA_ADA.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etClickedField = etETA_ADA;
				new DatePickerDialog(AddPODetailsActivity.this, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		etDestinationPortDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etClickedField = etDestinationPortDate;
				new DatePickerDialog(AddPODetailsActivity.this, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		etSailingDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etClickedField = etSailingDate;
				new DatePickerDialog(AddPODetailsActivity.this, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		etDocSentDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etClickedField = etDocSentDate;
				new DatePickerDialog(AddPODetailsActivity.this, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		etDocRecvDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etClickedField = etDocRecvDate;
				new DatePickerDialog(AddPODetailsActivity.this, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		etCustomClearanceDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etClickedField = etCustomClearanceDate;
				new DatePickerDialog(AddPODetailsActivity.this, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		etFactoryArrivalDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etClickedField = etFactoryArrivalDate;
				new DatePickerDialog(AddPODetailsActivity.this, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
	}

	Calendar myCalendar = Calendar.getInstance();

	DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			myCalendar.set(Calendar.YEAR, year);
			myCalendar.set(Calendar.MONTH, monthOfYear);
			myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateLabel();
		}

	};

	private void updateLabel() {

		String myFormat = "dd/MM/yyyy"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		System.out.println("calendar date ==== "+ myCalendar.getTime().toString());

		//		etClickedField.setText(Utility.getFormattedDate(sdf.format(myCalendar.getTime()),"dd-MM-yyyy","dd/MM/yyyy"));
		etClickedField.setText(sdf.format(myCalendar.getTime()));

		myCalendar.setTime(new java.util.Date(Utility.getFormattedDate(Utility.getCurrentDate(),"dd/MM/yyyy hh:mm:ss", "MM/dd/yyyy")));
	}

	private void setSaveCancelClickListener() {
		// TODO Auto-generated method stub
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void setSaveButtonClickListener() {
		// TODO Auto-generated method stub
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				savePurchaseOrder();
			}
		});
	}

	protected void savePurchaseOrder() {
		// TODO Auto-generated method stub

		if(validatePOEntry()){

			long poNumber = Long.parseLong(etPONumber.getText().toString());
			String docTrackNo = "";
			if(etDocTrackNumber.getText()  != null && !etDocTrackNumber.getText().toString().equals("")){
				docTrackNo = etDocTrackNumber.getText().toString();
			}
			double qty = Double.parseDouble(etQty.getText().toString());

			String strSuplName = spSuplName.getSelectedItem().toString();
			//			String strPrdName = etPrdName.getText().toString();
			String strPrdName = spProductName.getSelectedItem().toString();
			String strEtdAtd = Utility.getSQLFormattedDate(etETD_ATD.getText().toString());
			String strEtaAta = Utility.getSQLFormattedDate(etETA_ADA.getText().toString());
			String strPaymentTerms = spPaymentTerm.getSelectedItem().toString();
			String strDeparturePortName = etDeparturePortName.getText().toString();
			String strArrivalPortName = etArrivalPortName.getText().toString();
			String strDocSentDate = Utility.getSQLFormattedDate(etDocSentDate.getText().toString());
			String strDocRecvDate = Utility.getSQLFormattedDate(etDocRecvDate.getText().toString());
			String strCustomClearanceDate =  Utility.getSQLFormattedDate(etCustomClearanceDate.getText().toString());
			String strFactoryArrivalDate = Utility.getSQLFormattedDate(etFactoryArrivalDate.getText().toString());
			String strSailingDate =  Utility.getSQLFormattedDate(etSailingDate.getText().toString());
			String strLastPortName = etLastPortName.getText().toString();
			String strDestinationPortDate =  Utility.getSQLFormattedDate(etDestinationPortDate.getText().toString());
			String strSuplRemarks = etSuplRemarks.getText().toString();
			String strCustRemarks = etCustRemarks.getText().toString();
			String strCreatedDate = Utility.getFormattedDate(Utility.getCurrentDate(),"dd/MM/yyyy hh:mm:ss", "yyyy-MM-dd hh:mm:ss");

			boolean isDocSent = chkDocSent.isChecked();

			RequestParams params = new RequestParams();
			params.put("po_num", poNumber+"");
			params.put("supplier_name", strSuplName);
			params.put("prd_name", strPrdName);
			params.put("etd", strEtdAtd);
			params.put("eta", strEtaAta);
			params.put("atd", strEtdAtd);
			params.put("ata", strEtaAta);
			params.put("payment_terms", strPaymentTerms);
			params.put("doc_sent", isDocSent==true?"1":"0");
			params.put("doc_track_no", docTrackNo);
			params.put("departure_port_name", strDeparturePortName);
			params.put("arrival_port_name", strArrivalPortName);
			params.put("shipping_doc_sent_date", strDocSentDate);
			params.put("shipping_doc_rec_date", strDocRecvDate);
			params.put("custom_clearance_date", strCustomClearanceDate);
			params.put("material_factory_date", strFactoryArrivalDate);
			params.put("sailing_date", strSailingDate);
			params.put("last_port_name", strLastPortName);
			params.put("destination_port_date", strDestinationPortDate);
			params.put("cust_remarks", strCustRemarks);
			params.put("sup_remarks", strSuplRemarks);
			params.put("quantity", qty+"");
			params.put("is_deleted", "0");
			params.put("edit_count", editCount+"");
			params.put("is_synced", "0");
			params.put("created_date", strCreatedDate);

			//			06-04 07:22:24.133: I/System.out(12261): Parameters: sup_remarks=on time&shipping_doc_rec_date=2015-06-05&material_factory_date=2015-06-06&po_num=107&mode=add&is_synced=0&payment_terms=CAD&is_deleted=0&atd=04/06/2015&destination_port_date=2015-06-06&doc_track_no=&eta=04/06/2015&cust_remarks=&arrival_port_name=Ahmedabad&doc_sent=0&quantity=123.0&ata=04/06/2015&sailing_date=2015-06-06&created_date=04/06/2015 07:22:24&last_port_name=Ambawadi&shipping_doc_sent_date=2015-06-04&etd=04/06/2015&custom_clearance_date=2015-06-05&supplier_name=Ninesky&edit_count=0&departure_port_name=Gandhinagar&prd_name=Steel Tape

			if(poEntryId == 0){
				params.put("mode", "add");// set values if in AddMode
			}else{
				params.put("mode", "edit");// set values if in EditMode
			}

			System.out.println("Parameters: "+params);

			POEntry poEntry = new POEntry();
			poEntry.setId(poEntryId);
			poEntry.setPoNumber(poNumber);
			poEntry.setSuplierName(strSuplName);
			poEntry.setProductName(strPrdName);
			poEntry.setQuantity(qty);
			poEntry.setETD(strEtdAtd);
			poEntry.setATD(strEtdAtd);
			poEntry.setETA(strEtaAta);
			poEntry.setATA(strEtaAta);
			poEntry.setDeparturePortName(strDeparturePortName);
			poEntry.setArrivalPortName(strArrivalPortName);
			poEntry.setDocSent(isDocSent);
			poEntry.setDocTrackNumber(docTrackNo);
			poEntry.setDocSentDate(strDocSentDate);
			poEntry.setDocRecvDate(strDocRecvDate);
			poEntry.setCustomClearanceDate(strCustomClearanceDate);
			poEntry.setFactoryArrivalDate(strFactoryArrivalDate);
			poEntry.setSailingDate(strSailingDate);
			poEntry.setLastPortName(strLastPortName);
			poEntry.setDestinationPortDate(strDestinationPortDate);
			poEntry.setSupplierRemarks(strSuplRemarks);
			poEntry.setCustomerRemarks(strCustRemarks);
			poEntry.setTimestamp(strCreatedDate);

			invokePOEntryWS(params, poEntry);

		}

	}

	public void invokePOEntryWS(RequestParams params, final POEntry po){
		// Show Progress Dialog
		prgDialog.show();
		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		//         String webServiceURL = "http://192.168.2.2:9999/useraccount/login/dologin";
		String webServiceURL = "http://www.browseinfo.in/smart/insert_po_details.php";

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
						Toast.makeText(getApplicationContext(), "PO Entry saved successfully!", Toast.LENGTH_LONG).show();
						saveToLocalDB(po);

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


	protected void saveToLocalDB(POEntry po) {
		// TODO Auto-generated method stub
		poDB.open();
		System.out.println(poDB.insertPOEntry(po)+" record saved.");
		poDB.close();

		Intent intent = new Intent(getApplicationContext(), ListPOSummaryActivity.class);
		//		Intent intent = new Intent(getApplicationContext(), ViewPODetailsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	private boolean validatePOEntry() {
		// TODO Auto-generated method stub
		boolean valid = true;
		if(Utility.isNull(etPONumber.getText().toString())){
			setFieldError(etPONumber, MSG_CODE_BLANK);
			valid = false;
		}
		//		if(Utility.isNull(etPrdName.getText().toString())){
		//			setFieldError(etPrdName, MSG_CODE_BLANK);
		//			valid = false;
		//		}

		if(Utility.isNull(etQty.getText().toString())){
			setFieldError(etQty, MSG_CODE_BLANK);
			valid = false;
		}

		if(Utility.isNull(etETD_ATD.getText().toString())){
			setFieldError(etETD_ATD, MSG_CODE_BLANK);
			valid = false;
		}else if(!Utility.isValidDate(etETD_ATD.getText().toString(), "dd/MM/yyyy")){
			setFieldError(etETD_ATD, MSG_CODE_INVALID);
			valid = false;
		}

		if(Utility.isNull(etETA_ADA.getText().toString())){
			setFieldError(etETA_ADA, MSG_CODE_BLANK);
			valid = false;
		}else if(!Utility.isValidDate(etETA_ADA.getText().toString(), "dd/MM/yyyy")){
			setFieldError(etETA_ADA, MSG_CODE_INVALID);
			valid = false;
		}

		if(Utility.isNull(etDeparturePortName.getText().toString())){
			setFieldError(etDeparturePortName, MSG_CODE_BLANK);
			valid = false;
		}

		if(Utility.isNull(etArrivalPortName.getText().toString())){
			setFieldError(etArrivalPortName, MSG_CODE_BLANK);
			valid = false;
		}

		if(Utility.isNull(etDocSentDate.getText().toString())){
			setFieldError(etDocSentDate, MSG_CODE_BLANK);
			valid = false;
		}else if(!Utility.isValidDate(etDocSentDate.getText().toString(), "dd/MM/yyyy")){
			setFieldError(etDocSentDate, MSG_CODE_INVALID);
			valid = false;
		}
		if(Utility.isNull(etDocRecvDate.getText().toString())){
			setFieldError(etDocRecvDate, MSG_CODE_BLANK);
			valid = false;
		}else if(!Utility.isValidDate(etDocRecvDate.getText().toString(), "dd/MM/yyyy")){
			setFieldError(etDocRecvDate, MSG_CODE_INVALID);
			valid = false;
		}
		if(Utility.isNull(etCustomClearanceDate.getText().toString())){
			setFieldError(etCustomClearanceDate, MSG_CODE_BLANK);
			valid = false;
		}else if(!Utility.isValidDate(etCustomClearanceDate.getText().toString(), "dd/MM/yyyy")){
			setFieldError(etCustomClearanceDate, MSG_CODE_INVALID);
			valid = false;
		}
		if(Utility.isNull(etFactoryArrivalDate.getText().toString())){
			setFieldError(etFactoryArrivalDate, MSG_CODE_BLANK);
			valid = false;
		}else if(!Utility.isValidDate(etFactoryArrivalDate.getText().toString(), "dd/MM/yyyy")){
			setFieldError(etFactoryArrivalDate, MSG_CODE_INVALID);
			valid = false;
		}
		if(Utility.isNull(etSailingDate.getText().toString())){
			setFieldError(etSailingDate, MSG_CODE_BLANK);
			valid = false;
		}else if(!Utility.isValidDate(etSailingDate.getText().toString(), "dd/MM/yyyy")){
			setFieldError(etSailingDate, MSG_CODE_INVALID);
			valid = false;
		}
		if(Utility.isNull(etLastPortName.getText().toString())){
			setFieldError(etLastPortName, MSG_CODE_BLANK);
			valid = false;
		}
		if(Utility.isNull(etDestinationPortDate.getText().toString())){
			setFieldError(etDestinationPortDate, MSG_CODE_BLANK);
			valid = false;
		}else if(!Utility.isValidDate(etDestinationPortDate.getText().toString(), "dd/MM/yyyy")){
			setFieldError(etDestinationPortDate, MSG_CODE_INVALID);
			valid = false;
		}
		if(chkDocSent.isChecked()){
			if(Utility.isNull(etDocTrackNumber.getText().toString())){
				setFieldError(etDocTrackNumber, MSG_CODE_BLANK);
				valid = false;
			}
		}
		return valid;
	}

	private void setFieldError(EditText editText, int msgCode) {
		// TODO Auto-generated method stub
		TextView tv = (TextView) ((LinearLayout)editText.getParent()).getChildAt(0);
		String message = "";
		if(msgCode == MSG_CODE_BLANK){
			message = tv.getText() + " can not be blank.";
		}else if(msgCode == MSG_CODE_INVALID){
			message = "Invalid value for " + tv.getText();
		}
		editText.setError(message);

	}

	private void setCheckboxClickListener() {
		// TODO Auto-generated method stub
		chkDocSent.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				setDocTrackNumberVisibility(isChecked);

				//				Toast.makeText(getApplicationContext(), buttonView.getText() + " is " + isChecked, Toast.LENGTH_LONG).show();
			}
		});
	}

	protected void setDocTrackNumberVisibility(boolean isChecked) {
		// TODO Auto-generated method stub
		int visibility = View.INVISIBLE;
		if(isChecked){
			visibility = View.VISIBLE;
		}else{
			visibility= View.GONE;
		}
		((LinearLayout)tvDocTrackNumber.getParent()).setVisibility(visibility);
		etDocTrackNumber.setEnabled(isChecked);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem){
		super.onOptionsItemSelected(menuItem);
		int selectedMenuItemId = menuItem.getItemId();
		switch (selectedMenuItemId) {
		case R.id.logout:
			Utility.destroyUserSession(getApplicationContext());
			break;
		default:
			break;
		}
		return false;
	}
}

