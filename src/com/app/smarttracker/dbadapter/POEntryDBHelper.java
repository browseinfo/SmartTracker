package com.app.smarttracker.dbadapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class POEntryDBHelper extends DatabaseHelper {
	
	public static final String DATABASE_NAME = "smarttracker.db";
	private static final String TAG = "POEntryDBHelper";
	SQLiteDatabase db;
//	public static final String TABLE_PO_ENTRY = "po_entry";
//	public static final String KEY_ID = "id";
//	public static final String KEY_PO_NUMBER = "po_number";
//	public static final String KEY_PRD_NAME = "prd_name";
//	public static final String KEY_QTY = "qty";
//	public static final String KEY_ETD = "etd";
//	public static final String KEY_ATD = "atd";
//	public static final String KEY_ETA = "eta";
//	public static final String KEY_ATA = "ata";
//	public static final String KEY_DOC_SENT = "doc_sent";
//	public static final String KEY_DOC_TRACK_NO = "doc_track_no";
//	public static final String KEY_SAILING_DATE = "sailing_date";
//	public static final String KEY_LAST_PORT_NAME = "last_port_name";
//	public static final String KEY_DESTINATION_PORT_DATE = "destination_port_date";
//	public static final String KEY_CUST_REMARKS = "cust_remarks";
//	public static final String KEY_SUPL_REMARKS = "supl_remarks";
//	public static final String KEY_IS_DELETED = "is_deleted";
//	public static final String KEY_IS_EDITED = "is_edited";
//	public static final String KEY_IS_SYNCED = "is_synced";
//	
//	
//	String CREATE_TABLE_PO_ENTRY = "CREATE TABLE "+TABLE_PO_ENTRY+ " (" +
//			KEY_ID+" INTEGER PRIMARY KEY, "+
//			KEY_PO_NUMBER+" INTEGER, "+
//			KEY_PRD_NAME+" TEXT, "+
//			KEY_QTY+" INTEGER, " +
//			KEY_ETD+" TEXT, " +
//			KEY_ATD+" TEXT, " +
//			KEY_ETA+" TEXT, " +
//			KEY_ATA+" TEXT, " +
//			KEY_DOC_SENT+" TINYINT, " +
//			KEY_DOC_TRACK_NO+" INTEGER, " +
//			KEY_SAILING_DATE+" DATETIME, " +
//			KEY_LAST_PORT_NAME+" TEXT, " +
//			KEY_DESTINATION_PORT_DATE+" DATETIME, " +
//			KEY_CUST_REMARKS+" TEXT, " +
//			KEY_SUPL_REMARKS+" TEXT, " +
//			KEY_IS_DELETED+" TINYINT, " +
//			KEY_IS_EDITED+" TINYINT, " +
//			KEY_IS_SYNCED+" TINYINT " +
//			")";

	public POEntryDBHelper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		db = this.getWritableDatabase();
		
	}


}
