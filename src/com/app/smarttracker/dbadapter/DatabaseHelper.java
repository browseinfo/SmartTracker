package com.app.smarttracker.dbadapter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.smarttracker.entity.POEntry;
import com.app.smarttracker.entity.UserAuth;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "smarttracker.db";
	private static final String TAG = "DatabaseHelper";
	public static SQLiteDatabase db;

	//	--------- USER_AUTH TABLE ----------
	public static final String TABLE_USER = "user_auth";
	public static final String KEY_USER_ID = "id";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_USER_TYPE = "user_type";


	String CREATE_TABLE_USER_AUTH = "CREATE TABLE "+TABLE_USER+ " (" +
			KEY_USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_USERNAME+" TEXT, "+
			KEY_PASSWORD+" TEXT, "+ 
			KEY_USER_TYPE+" TEXT )";

	//	--------- PO_ENTRY TABLE ----------

	public static final String TABLE_PO_ENTRY = "po_entry";
	public static final String KEY_ID = "_id";
	public static final String KEY_PO_NUMBER = "po_number";
	public static final String KEY_SUPL_NAME = "supl_name";
	public static final String KEY_PRD_NAME = "prd_name";
	public static final String KEY_QTY = "qty";
	public static final String KEY_PAYMENT_TERMS = "payment_terms";
	public static final String KEY_ETD = "etd";
	public static final String KEY_ATD = "atd";
	public static final String KEY_ETA = "eta";
	public static final String KEY_ATA = "ata";
	public static final String KEY_DEPARTURE_PORT_NAME = "departure_port_name";
	public static final String KEY_ARRIVAL_PORT_NAME = "arrival_port_name";
	public static final String KEY_DOC_SENT = "doc_sent";
	public static final String KEY_DOC_TRACK_NO = "doc_track_no";
	public static final String KEY_DOC_SENT_DATE = "doc_sent_date";
	public static final String KEY_DOC_RECV_DATE = "doc_recv_date";
	public static final String KEY_CUSTOM_CLEARANCE_DATE = "cust_clearance_date";
	public static final String KEY_FACTORY_ARRIVAL_DATE = "factory_arrival_date";	
	public static final String KEY_LAST_PORT_NAME = "last_port_name";
	public static final String KEY_DESTINATION_PORT_DATE = "destination_port_date";
	public static final String KEY_SAILING_DATE = "payment_date";
	public static final String KEY_CUST_REMARKS = "cust_remarks";
	public static final String KEY_SUPL_REMARKS = "supl_remarks";
	public static final String KEY_IS_DELETED = "is_deleted";
	public static final String KEY_IS_EDITED = "edit_count";
	public static final String KEY_IS_SYNCED = "is_synced";
	public static final String KEY_TIMESTAMP = "created_date";
	
	private static final int DATABASE_VERSION = 1;


	String CREATE_TABLE_PO_ENTRY = "CREATE TABLE "+TABLE_PO_ENTRY+ " (" +
			KEY_ID+" INTEGER PRIMARY KEY, "+
			KEY_PO_NUMBER+" INTEGER, "+
			KEY_PRD_NAME+" TEXT, "+
			KEY_SUPL_NAME+" TEXT, "+
			KEY_QTY+" REAL, " +
			KEY_PAYMENT_TERMS+" TEXT, "+
			KEY_ETD+" TEXT, " +
			KEY_ATD+" TEXT, " +
			KEY_ETA+" TEXT, " +
			KEY_ATA+" TEXT, " +
			KEY_DEPARTURE_PORT_NAME+" TEXT, " +
			KEY_ARRIVAL_PORT_NAME+" TEXT, " +
			KEY_DOC_SENT+" TINYINT DEFAULT 0, " +
			KEY_DOC_TRACK_NO+" INTEGER, " +
			KEY_DOC_SENT_DATE+" DATETIME, " +
			KEY_DOC_RECV_DATE+" DATETIME, " +
			KEY_CUSTOM_CLEARANCE_DATE+" DATETIME, " +
			KEY_FACTORY_ARRIVAL_DATE+" DATETIME, " +
			KEY_SAILING_DATE+" DATETIME, " +
			KEY_LAST_PORT_NAME+" TEXT, " +
			KEY_DESTINATION_PORT_DATE+" DATETIME, " +
			KEY_CUST_REMARKS+" TEXT, " +
			KEY_SUPL_REMARKS+" TEXT, " +
			KEY_IS_DELETED+" TINYINT DEFAULT 0, " +
			KEY_IS_EDITED+" INTEGER DEFAULT 0, " +
			KEY_IS_SYNCED+" TINYINT  DEFAULT 0," +
			KEY_TIMESTAMP+" DATETIME DEFAULT CURRENT_TIMESTAMP " +
			")";


	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub

	}

	public DatabaseHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		Log.i(TAG, CREATE_TABLE_USER_AUTH);
		db.execSQL(CREATE_TABLE_USER_AUTH);
		Log.i(TAG, TABLE_USER + " Table Created...");
		
		Log.i(TAG, CREATE_TABLE_PO_ENTRY);
		db.execSQL(CREATE_TABLE_PO_ENTRY);
		Log.i(TAG, TABLE_PO_ENTRY + " Table Created...");
		
	}

	public void insertInitialData() {
		// TODO Auto-generated method stub
//		db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PO_NUMBER, 101);
		values.put(KEY_PRD_NAME, "MotoG 1st Gen");
		values.put(KEY_ETA, "09/05/2015");
		values.put(KEY_ETD, "10/05/2015");
		values.put(KEY_QTY, 2);
		values.put(KEY_DOC_SENT, true);
		values.put(KEY_DOC_TRACK_NO, 1234);
		values.put(KEY_LAST_PORT_NAME, "Ambawadi");
		values.put(KEY_SAILING_DATE, "30/04/2015");
		values.put(KEY_DESTINATION_PORT_DATE, "02/05/2015");
		values.put(KEY_SUPL_REMARKS, "First Order");
		values.put(KEY_CUST_REMARKS, "");
		values.put(KEY_IS_DELETED, 0);
		values.put(KEY_IS_EDITED, 0);
		values.put(KEY_IS_SYNCED, 0);

		System.out.println((db.insert(TABLE_PO_ENTRY, null, values))+ " - record inserted..");
		
		values.clear();
		values.put(KEY_PO_NUMBER, 102);
		values.put(KEY_PRD_NAME, "Samsung Galaxy");
		values.put(KEY_ETA, "01/05/2015");
		values.put(KEY_ETD, "02/05/2015");
		values.put(KEY_QTY, 1);
		values.put(KEY_DOC_SENT, false);
		values.put(KEY_DOC_TRACK_NO, "");
		values.put(KEY_LAST_PORT_NAME, "Naranpura");
		values.put(KEY_SAILING_DATE, "01/05/2015");
		values.put(KEY_DESTINATION_PORT_DATE, "02/05/2015");
		values.put(KEY_SUPL_REMARKS, "");
		values.put(KEY_CUST_REMARKS, "");
		values.put(KEY_IS_DELETED, 0);
		values.put(KEY_IS_EDITED, 0);
		values.put(KEY_IS_SYNCED, 0);
		
		System.out.println((db.insert(TABLE_PO_ENTRY, null, values))+ " - record inserted..");
		
		values.clear();
		values.put(KEY_PO_NUMBER, 103);
		values.put(KEY_PRD_NAME, "Nexus S");
		values.put(KEY_ETA, "01/05/2015");
		values.put(KEY_ETD, "01/05/2015");
		values.put(KEY_QTY, 3);
		values.put(KEY_DOC_SENT, false);
		values.put(KEY_DOC_TRACK_NO, "");
		values.put(KEY_LAST_PORT_NAME, "Satellite");
		values.put(KEY_SAILING_DATE, "01/05/2015");
		values.put(KEY_DESTINATION_PORT_DATE, "05/05/2015");
		values.put(KEY_SUPL_REMARKS, "");
		values.put(KEY_CUST_REMARKS, "");
		values.put(KEY_IS_DELETED, 0);
		values.put(KEY_IS_EDITED, 0);
		values.put(KEY_IS_SYNCED, 0);
		
		System.out.println((db.insert(TABLE_PO_ENTRY, null, values))+ " - record inserted..");
//		db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PO_ENTRY);  

		onCreate(db);  
	}

	public void open() {
//		this.open();
		db = this.getWritableDatabase();
	}
	
	public void close() {
//		this.close();
		db.close();
	}

	public long insertUser(UserAuth user){
//		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_USERNAME, user.getUsername());
		values.put(DatabaseHelper.KEY_PASSWORD, user.getPassword());
		values.put(DatabaseHelper.KEY_USER_TYPE, user.getUserType());

		user.setId(db.insert(TABLE_USER, null, values));
		
//		db.close();
		
		return user.getId();

	}

//	public void insertUser(UserAuth user){
//		db = this.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put(DatabaseHelper.KEY_USERNAME, user.getUsername());
//		values.put(DatabaseHelper.KEY_PASSWORD, user.getPassword());
//		values.put(DatabaseHelper.KEY_USER_TYPE, user.getUserType());
//
//		db.insert(CREATE_TABLE_USER_AUTH, null, values);
//		
//
//	}

	public UserAuth searchRecords(String strUsername,String strPassword ){
//		db= this.getReadableDatabase();
		String searchQuery="SELECT * FROM " + TABLE_USER + " WHERE " + KEY_USERNAME + "='"+ strUsername + "' AND " + KEY_PASSWORD + "='" + strPassword +"'";
		System.out.println("Search query====" + searchQuery);
		Cursor cursor=db.rawQuery(searchQuery, null);
		UserAuth user=new UserAuth();
		if (cursor.moveToFirst()){
			cursor.moveToFirst();
			user.setId(Long.parseLong(cursor.getString(0)));
			user.setUsername(cursor.getString(1));
			user.setPassword(cursor.getString(2));
			user.setUserType(cursor.getString(3));
		} else {
			user=null;	}
//		db.close();

		return user;
	}

	public long insertPOEntry(POEntry po) {
//		db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PO_NUMBER, po.getPoNumber());
		values.put(KEY_SUPL_NAME, po.getSuplierName());
		values.put(KEY_PRD_NAME, po.getProductName());
		values.put(KEY_QTY, po.getQuantity());
		values.put(KEY_PAYMENT_TERMS, po.getPaymentTerms());	
		values.put(KEY_ETA, po.getETA());
		values.put(KEY_ETD, po.getETD());
		values.put(KEY_DEPARTURE_PORT_NAME, po.getDeparturePortName());
		values.put(KEY_ARRIVAL_PORT_NAME, po.getArrivalPortName());
		values.put(KEY_DOC_SENT, po.isDocSent());
		values.put(KEY_DOC_TRACK_NO, po.getDocTrackNumber());
		values.put(KEY_DOC_SENT_DATE, po.getDocSentDate());
		values.put(KEY_DOC_RECV_DATE, po.getDocRecvDate());
		values.put(KEY_CUSTOM_CLEARANCE_DATE, po.getCustomClearanceDate());
		values.put(KEY_FACTORY_ARRIVAL_DATE, po.getFactoryArrivalDate());
		values.put(KEY_SAILING_DATE, po.getSailingDate());
		values.put(KEY_LAST_PORT_NAME, po.getLastPortName());
		values.put(KEY_DESTINATION_PORT_DATE, po.getDestinationPortDate());
		values.put(KEY_SUPL_REMARKS, po.getSupplierRemarks());
		values.put(KEY_CUST_REMARKS, po.getCustomerRemarks());
		values.put(KEY_IS_DELETED, po.isDeleted());
		values.put(KEY_IS_EDITED, po.getEditCount());
		values.put(KEY_IS_SYNCED, po.isSynced());
		values.put(KEY_TIMESTAMP, po.getTimestamp());


		if(po.getId() != 0){
			values.put(KEY_ID, po.getId());
			values.put(KEY_IS_EDITED, (po.getEditCount())+1);
			String whereClause = KEY_PO_NUMBER + " = ?";
			String whereArgs[] = new String[]{po.getPoNumber()+""};
			
			db.update(TABLE_PO_ENTRY, values, whereClause, whereArgs);
		}else{
			po.setId(db.insert(TABLE_PO_ENTRY, null, values));
			System.out.println("inserted"+po.getId());
			
		}

//		db.close();
		
		return po.getId();
	}

	public List<POEntry> getAllPOEntries(String orderBy) {
//		db = this.getReadableDatabase();

		List<POEntry> poList = new ArrayList<POEntry>();
		String KEY_ORDER_BY = KEY_PO_NUMBER;
		if(orderBy.equalsIgnoreCase("Product Name")){
			KEY_ORDER_BY = KEY_PRD_NAME;
		}else if(orderBy.equalsIgnoreCase("ETD/ETA")){
			KEY_ORDER_BY = KEY_ETD;
		}else { //"PO Number"
			KEY_ORDER_BY = KEY_PO_NUMBER;
		}
		
		String whereClause = KEY_IS_DELETED + " = ? ";
		String whereArgs[] = new String[]{"0"};

		Cursor cursor = db.query(TABLE_PO_ENTRY, null, whereClause, whereArgs, null, null, KEY_ORDER_BY);

		while(cursor.moveToNext()){
			POEntry po = new POEntry();
			po.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
			po.setPoNumber(cursor.getLong(cursor.getColumnIndex(KEY_PO_NUMBER)));
			po.setProductName(cursor.getString(cursor.getColumnIndex(KEY_PRD_NAME)));
			po.setSuplierName(cursor.getString(cursor.getColumnIndex(KEY_SUPL_NAME)));
			po.setQuantity(cursor.getDouble(cursor.getColumnIndex(KEY_QTY)));
			po.setPaymentTerms(cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_TERMS)));
			po.setETD(cursor.getString(cursor.getColumnIndex(KEY_ETD)));
			po.setETA(cursor.getString(cursor.getColumnIndex(KEY_ETA)));
			po.setATD(cursor.getString(cursor.getColumnIndex(KEY_ATD)));
			po.setATA(cursor.getString(cursor.getColumnIndex(KEY_ATA)));
			po.setDeparturePortName(cursor.getString(cursor.getColumnIndex(KEY_DEPARTURE_PORT_NAME)));
			po.setArrivalPortName(cursor.getString(cursor.getColumnIndex(KEY_ARRIVAL_PORT_NAME)));
			po.setDocSent((cursor.getInt(cursor.getColumnIndex(KEY_DOC_SENT))== 1)? true:false);
			po.setDocTrackNumber(cursor.getString(cursor.getColumnIndex(KEY_DOC_TRACK_NO)));
			po.setDocSentDate(cursor.getString(cursor.getColumnIndex(KEY_DOC_SENT_DATE)));
			po.setDocRecvDate(cursor.getString(cursor.getColumnIndex(KEY_DOC_RECV_DATE)));
			po.setCustomClearanceDate(cursor.getString(cursor.getColumnIndex(KEY_CUSTOM_CLEARANCE_DATE)));
			po.setFactoryArrivalDate(cursor.getString(cursor.getColumnIndex(KEY_FACTORY_ARRIVAL_DATE)));
			po.setSailingDate(cursor.getString(cursor.getColumnIndex(KEY_SAILING_DATE)));
			po.setLastPortName(cursor.getString(cursor.getColumnIndex(KEY_LAST_PORT_NAME)));
			po.setDestinationPortDate(cursor.getString(cursor.getColumnIndex(KEY_DESTINATION_PORT_DATE)));
			po.setSupplierRemarks(cursor.getString(cursor.getColumnIndex(KEY_SUPL_REMARKS)));
			po.setCustomerRemarks(cursor.getString(cursor.getColumnIndex(KEY_CUST_REMARKS)));
			po.setTimestamp(cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP)));

			poList.add(po);
		}
//		db.close();
		return poList;
	}
	
	public Cursor fetchAllPOEntries(String orderBy, String searchField, String searchFieldValue) {
//		db = this.getReadableDatabase();

		String KEY_ORDER_BY = KEY_PO_NUMBER;
		if(orderBy.equalsIgnoreCase("Product Name")){
			KEY_ORDER_BY = KEY_PRD_NAME;
		}else if(orderBy.equalsIgnoreCase("ETD/ETA")){
			KEY_ORDER_BY = KEY_ETD;
		}else { //"PO Number"
			KEY_ORDER_BY = KEY_PO_NUMBER;
		}
		
		String appendSearchCriteria = "";
		
		if(searchField != null && !searchField.equalsIgnoreCase("")){
			String fieldName = KEY_PO_NUMBER;
			if(searchField.equalsIgnoreCase("Product Name")){
				fieldName = KEY_PRD_NAME;
			}else if(searchField.equalsIgnoreCase("Supplier Name")){
				fieldName = KEY_SUPL_NAME;
			}
			
			appendSearchCriteria = " AND " + fieldName + " LIKE '%" + searchFieldValue + "%'";
		}
		
		String whereClause = KEY_IS_DELETED + " = ? AND " + KEY_IS_SYNCED + " = ? " + appendSearchCriteria;
		String whereArgs[] = new String[]{"0","0"};

		Cursor cursor = db.query(TABLE_PO_ENTRY, null, whereClause, whereArgs, null, null, KEY_ORDER_BY);

//		db.close();
		return cursor;
	}

	public POEntry getPOEntry(long poNumber) {
//		db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PO_ENTRY, null, KEY_PO_NUMBER+ " = ? ", new String[]{poNumber+""}, null, null, null);

		POEntry po = new POEntry();
		if(cursor.moveToNext()){
			po.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
			po.setPoNumber(cursor.getLong(cursor.getColumnIndex(KEY_PO_NUMBER)));
			po.setProductName(cursor.getString(cursor.getColumnIndex(KEY_PRD_NAME)));
			po.setSuplierName(cursor.getString(cursor.getColumnIndex(KEY_SUPL_NAME)));
			po.setQuantity(cursor.getDouble(cursor.getColumnIndex(KEY_QTY)));
			po.setPaymentTerms(cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_TERMS)));
			po.setETD(cursor.getString(cursor.getColumnIndex(KEY_ETD)));
			po.setETA(cursor.getString(cursor.getColumnIndex(KEY_ETA)));
			po.setATD(cursor.getString(cursor.getColumnIndex(KEY_ATD)));
			po.setATA(cursor.getString(cursor.getColumnIndex(KEY_ATA)));
			po.setDeparturePortName(cursor.getString(cursor.getColumnIndex(KEY_DEPARTURE_PORT_NAME)));
			po.setArrivalPortName(cursor.getString(cursor.getColumnIndex(KEY_ARRIVAL_PORT_NAME)));
			po.setDocSent((cursor.getInt(cursor.getColumnIndex(KEY_DOC_SENT))== 1)? true:false);
			po.setDocTrackNumber(cursor.getString(cursor.getColumnIndex(KEY_DOC_TRACK_NO)));
			po.setDocSentDate(cursor.getString(cursor.getColumnIndex(KEY_DOC_SENT_DATE)));
			po.setDocRecvDate(cursor.getString(cursor.getColumnIndex(KEY_DOC_RECV_DATE)));
			po.setCustomClearanceDate(cursor.getString(cursor.getColumnIndex(KEY_CUSTOM_CLEARANCE_DATE)));
			po.setFactoryArrivalDate(cursor.getString(cursor.getColumnIndex(KEY_FACTORY_ARRIVAL_DATE)));
			po.setSailingDate(cursor.getString(cursor.getColumnIndex(KEY_SAILING_DATE)));
			po.setLastPortName(cursor.getString(cursor.getColumnIndex(KEY_LAST_PORT_NAME)));
			po.setDestinationPortDate(cursor.getString(cursor.getColumnIndex(KEY_DESTINATION_PORT_DATE)));
			po.setSupplierRemarks(cursor.getString(cursor.getColumnIndex(KEY_SUPL_REMARKS)));
			po.setCustomerRemarks(cursor.getString(cursor.getColumnIndex(KEY_CUST_REMARKS)));
			po.setTimestamp(cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP)));

		}
		System.out.println("PO======"+po);
		return po;
	}

	public int deletePOEntry(long poNumber) {
		// TODO Auto-generated method stub
//		db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_IS_DELETED, 1);
		
		String whereClause = KEY_PO_NUMBER + " = ?";
		String whereArgs[] = new String[]{poNumber+""};
		int result = db.update(TABLE_PO_ENTRY, values, whereClause, whereArgs);
//		db.close();

		return result;		
		
	}

	public int deleteAllPOEntry() {
		// TODO Auto-generated method stub
//		db = this.getReadableDatabase();
		int result = db.delete(TABLE_PO_ENTRY, null, null);
//		db.close();

		return result;		
		
	}


}
