package com.app.smarttracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
/**
 * Class which has Utility methods
 * 
 */
public class Utility {
	private static Pattern pattern;
	private static Matcher matcher;
	//Email Pattern
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * Validate Email with regular expression
	 * 
	 * @param email
	 * @return true for Valid Email and false for Invalid Email
	 */
	public static boolean validate(String email) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();

	}
	/**
	 * Checks for Null String object
	 * 
	 * @param txt
	 * @return true for not null and false for null String object
	 */
	public static boolean isNull(String txt){
		//        return txt!=null && txt.trim().length()>0 ? true: false;
		return txt==null || txt.trim().length() == 0 ? true: false;
	}

	public static boolean isValidDate(String dateToValidate, String dateFromat){

		if(dateToValidate == null){
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);

		try {

			//if not valid, it will throw ParseException
			Date date = sdf.parse(dateToValidate);
			System.out.println(date);

		} catch (ParseException e) {

			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static String getSQLFormattedDate(String date) {
		try{
			
			System.out.println("Date before Conversion:getSQLFormattedDate()- "+ date);

			String myFormat = "dd/MM/yyyy"; //In which you need put here
			SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
//			date = dateFormat.format(date);

//			java.util.Date utilDate = dateFormat.parse(date);
			//		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

			String newFormat = "yyyy-MM-dd"; //In which you need put here
			SimpleDateFormat newDateFormat = new SimpleDateFormat(newFormat, Locale.US);
			date = newDateFormat.format(dateFormat.parse(date));
			System.out.println("Date after Conversion:getSQLFormattedDate()- "+ date);

		}catch(Exception e){
			Log.e("Utility.java", e.toString());
		}
		return date;
	}

	public static String getUserFormattedDate(String date) {

		try{
			System.out.println("Date Before Conversion:"+ date);
			
			String myFormat = "yyyy-MM-dd"; //In which you need put here
			SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
//			date = dateFormat.format(date);

			String newFormat = "dd/MM/yyyy";
			SimpleDateFormat newDateFormat = new SimpleDateFormat(newFormat, Locale.US);
			date = newDateFormat.format(dateFormat.parse(date));
			System.out.println("Date after Conversion:"+ date);

		}catch(Exception e){
			Log.e("Utility.java", e.toString());
		}
		return date;
	}

	public static String getFormattedDate(String date, String currentFormat, String newFormat) {
		try{
			
			SimpleDateFormat dateFormat = new SimpleDateFormat(currentFormat, Locale.US);
//			date = dateFormat.format(date);
			System.out.println("Date Before Conversion:getFormattedDate()- "+ date);
//			java.util.Date utilDate = dateFormat.parse(date);

			SimpleDateFormat newDateFormat = new SimpleDateFormat(newFormat, Locale.US);
			date = newDateFormat.format(dateFormat.parse(date));
			System.out.println("Date after Conversion:getFormattedDate()- "+ date);

		}catch(Exception e){
			Log.e("Utility.java", e.toString());
		}
		return date;
	}

	public static String getCurrentDate() {
		String formattedDate ="";
		try{
			java.util.Date today = new java.util.Date();
			String myFormat = "dd/MM/yyyy hh:mm:ss";
			SimpleDateFormat newDateFormat = new SimpleDateFormat(myFormat, Locale.US);
			formattedDate = newDateFormat.format(today);
		}catch(Exception e){
			Log.e("Utility.java", e.toString());
		}
		return formattedDate;
	}

}