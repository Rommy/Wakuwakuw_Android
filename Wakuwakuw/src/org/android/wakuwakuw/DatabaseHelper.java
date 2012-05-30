package org.android.wakuwakuw;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ArrayAdapter;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "wakuwakuw.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_EVENT = "event";
	public static final String TABLE_MEETUP = "meetup";
	public static final String TABLE_COMMUNITY = "community";
	public static final String TABLE_CITY_SEARCH = "city_search";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ID_LOGO = "id_logo";
	public static final String COLUMN_LOGO = "logo";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_LOCATION = "location";
	public static final String COLUMN_TIME_START = "time_start";
	public static final String COLUMN_TIME_END = "time_end";
	public static final String COLUMN_CATEGORY_NAME = "category_name";
	public static final String COLUMN_LAT = "lat";
	public static final String COLUMN_LNG = "lng";
	public static final String COLUMN_SLUG = "slug";
	public static final String COLUMN_TOTAL_GUESTS = "total_guests";
	public static final String COLUMN_TOTAL_COMMENTS = "total_comments";
	public static final String COLUMN_TOTAL_LIKES = "total_likes";

	public static final String COLUMN_CITY_NAME = "city";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LINK_URL = "name_uri";
	public static final String COLUMN_TOTAL_MEMBERS = "total_members";
	
	private static final String EVENT_CREATE = "create table " + TABLE_EVENT + "( " 
		+ COLUMN_ID + " text primary key, " + COLUMN_ID_LOGO + " text, " + COLUMN_LOGO + " blob, "
		+ COLUMN_TITLE + " text, " + COLUMN_DESCRIPTION + " text, " + COLUMN_LOCATION + " text, " 
		+ COLUMN_TIME_START + " text, " + COLUMN_TIME_END + " text, " + COLUMN_CATEGORY_NAME + " text, " 
		+ COLUMN_LAT + " text, " + COLUMN_LNG + " text, " + COLUMN_SLUG + " text, "
		+ COLUMN_TOTAL_GUESTS + " text, " + COLUMN_TOTAL_COMMENTS + " text, " + COLUMN_TOTAL_LIKES + " text);";
	
	private static final String MEETUP_CREATE = "create table " + TABLE_MEETUP + "( " 
		+ COLUMN_ID + " text primary key, " + COLUMN_ID_LOGO + " text, " + COLUMN_LOGO + " blob, "
		+ COLUMN_TITLE + " text, " + COLUMN_DESCRIPTION + " text, " + COLUMN_LOCATION + " text, " 
		+ COLUMN_TIME_START + " text, " + COLUMN_TIME_END + " text, " + COLUMN_CATEGORY_NAME + " text, " 
		+ COLUMN_LAT + " text, " + COLUMN_LNG + " text, " + COLUMN_SLUG + " text, "
		+ COLUMN_TOTAL_GUESTS + " text, " + COLUMN_TOTAL_COMMENTS + " text, " + COLUMN_TOTAL_LIKES + " text);";
	
	private static final String COMMUNITY_CREATE = "create table " + TABLE_COMMUNITY + "( " 
		+ COLUMN_ID + " text primary key, " + COLUMN_LOGO + " blob, " + COLUMN_NAME + " text, " 
		+ COLUMN_DESCRIPTION + " text, " + COLUMN_CATEGORY_NAME + " text, " + COLUMN_LINK_URL + " text, "
		+ COLUMN_TOTAL_MEMBERS + " text, " + COLUMN_TOTAL_COMMENTS + " text, " + COLUMN_TOTAL_LIKES + " text);";
	
	private static final String CITY_CREATE = "create table " + TABLE_CITY_SEARCH + "( " 
		+ COLUMN_ID + " integer primary key autoincrement, " + COLUMN_CITY_NAME + " text);";
	
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(EVENT_CREATE);
		db.execSQL(MEETUP_CREATE);
		db.execSQL(COMMUNITY_CREATE);
		db.execSQL(CITY_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEETUP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMUNITY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY_SEARCH);
		
		onCreate(db);
	}
	
	public ArrayAdapter<String> getListAdapter(Context context, SQLiteDatabase db, String perintahSQL) {
		ArrayAdapter<String> list = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line);
		Cursor c = db.rawQuery(perintahSQL, null); 
		
		if (c.getCount() > 0) {
			if (c.moveToFirst())
			{
				do {
					String[] names = c.getColumnNames();
					int length = names.length;
					for(int i = length-1; i >= 0; i--) {
					   list.add(c.getString(i));
					}
				}
				while (c.moveToNext());
			}
		}
		
		c.close();
		
		return list;
	}
	
	public ArrayList<String> getItemFromDatabase(Context context, SQLiteDatabase db, String perintahSQL, String columnName) {
		ArrayList<String> list = new ArrayList<String>();
		
		Cursor c = db.rawQuery(perintahSQL, null); 
		
		if (c.getCount() > 0) {
			if (c.moveToFirst())
			{
				do {
					list.add(c.getString(c.getColumnIndex(columnName)));
				}
				while (c.moveToNext());
			}
		}
		
		c.close();
		
		return list;
	}
	
	public ArrayList<Drawable> getImageFromDatabase(Context context, SQLiteDatabase db, String perintahSQL, String columnName) {
		ArrayList<Drawable> list = new ArrayList<Drawable>();
		
		Cursor c = db.rawQuery(perintahSQL, null); 
		
		if (c.getCount() > 0) {
			if (c.moveToFirst())
			{
				do {
					byte[] logo = c.getBlob(c.getColumnIndex(columnName));
					ByteArrayInputStream imageStream = new ByteArrayInputStream(logo);
					Bitmap bitmap= BitmapFactory.decodeStream(imageStream);
					Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
					
					list.add(drawable);
				}
				while (c.moveToNext());
			}
		}
		
		c.close();
		
		return list;
	}

	/*
	public void getItemFromDatabase(Context context, SQLiteDatabase db, String perintahSQL, ArrayList<Drawable> isiLogo, 
			ArrayList<String> isiId, ArrayList<String> isiIdLogo, ArrayList<String> isiJudul, ArrayList<String> isiPenjelasan, 
			ArrayList<String> isiAlamat, ArrayList<String> isiTanggalStart, ArrayList<String> isiTanggalEnd, 
			ArrayList<String> isiJenis, ArrayList<String> isiLinkURL, ArrayList<String> isiKoorLat, ArrayList<String> isiKoorLong, 
			ArrayList<String> isiJmlhMember, ArrayList<String> isiJmlhComment, ArrayList<String> isiJmlhLike) {
		
		isiId = new ArrayList<String>();
		isiIdLogo = new ArrayList<String>();
		isiLogo = new ArrayList<Drawable>();
		isiJudul = new ArrayList<String>();
		isiPenjelasan = new ArrayList<String>();
		isiAlamat = new ArrayList<String>();
		isiTanggalStart = new ArrayList<String>();
		isiTanggalEnd = new ArrayList<String>();
		isiJenis = new ArrayList<String>();
		isiLinkURL = new ArrayList<String>();
		isiKoorLat = new ArrayList<String>();
		isiKoorLong = new ArrayList<String>();
		isiJmlhMember = new ArrayList<String>();
		isiJmlhComment = new ArrayList<String>();
		isiJmlhLike = new ArrayList<String>();
		
		Cursor c = db.rawQuery(perintahSQL, null); 
		
		if (c.getCount() > 0) {
			if (c.moveToFirst())
			{
				do {
					byte[] logo= c.getBlob(c.getColumnIndex(COLUMN_LOGO));
					ByteArrayInputStream imageStream = new ByteArrayInputStream(logo);
					Bitmap bitmap= BitmapFactory.decodeStream(imageStream);
					Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
					
					isiLogo.add(drawable);
					isiId.add(c.getString(c.getColumnIndex(COLUMN_ID)));
					isiIdLogo.add(c.getString(c.getColumnIndex(COLUMN_ID_LOGO)));
					isiJudul.add(c.getString(c.getColumnIndex(COLUMN_TITLE)));
					isiPenjelasan.add(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)));
					isiAlamat.add(c.getString(c.getColumnIndex(COLUMN_LOCATION)));
					isiTanggalStart.add(c.getString(c.getColumnIndex(COLUMN_TIME_START)));
					isiTanggalEnd.add(c.getString(c.getColumnIndex(COLUMN_TIME_END)));
					isiJenis.add(c.getString(c.getColumnIndex(COLUMN_CATEGORY_NAME)));
					isiLinkURL.add(c.getString(c.getColumnIndex(COLUMN_SLUG)));
					isiKoorLat.add(c.getString(c.getColumnIndex(COLUMN_LAT)));
					isiKoorLong.add(c.getString(c.getColumnIndex(COLUMN_LNG)));
					isiJmlhMember.add(c.getString(c.getColumnIndex(COLUMN_TOTAL_GUESTS)));
					isiJmlhComment.add(c.getString(c.getColumnIndex(COLUMN_TOTAL_COMMENTS)));
					isiJmlhLike.add(c.getString(c.getColumnIndex(COLUMN_TOTAL_LIKES)));
				}
				while (c.moveToNext());
			}
		}
		
		c.close();
	}
	*/
}
