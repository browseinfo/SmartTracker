package com.app.smarttracker.dbadapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.app.smarttracker.entity.UserAuth;

public class LoginDBHelper extends DatabaseHelper {
	
	public static final String DATABASE_NAME = "smarttracker.db";
//	public static final String TABLE_USER = "user_auth";
//	public static final String KEY_USER_ID = "id";
//	public static final String KEY_USERNAME = "username";
//	public static final String KEY_PASSWORD = "password";
//	public static final String KEY_USER_TYPE = "user_type";
//	
//	
//	String CREATE_TABLE_USER_AUTH = "CREATE TABLE "+TABLE_USER+ " (" +
//			KEY_USER_ID+" INTEGER PRIMARY KEY, "+KEY_USERNAME+" TEXT, "+
//			KEY_PASSWORD+" TEXT )";

	SQLiteDatabase db;
	public LoginDBHelper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		db = this.getWritableDatabase();
	}

//	public long insertUser(UserAuth user){
//		ContentValues values = new ContentValues();
//		values.put(DatabaseHelper.KEY_USERNAME, user.getUsername());
//		values.put(DatabaseHelper.KEY_PASSWORD, user.getPassword());
//		values.put(DatabaseHelper.KEY_USER_TYPE, user.getUserType());
//		
//		user.setId(db.insert(CREATE_TABLE_USER_AUTH, null, values));
//		return 0;
//		
//	}
	
}
