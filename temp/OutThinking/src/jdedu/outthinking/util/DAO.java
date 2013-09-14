package jdedu.outthinking.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DAO {

	// //
	public static final String DATABASE_NAME = "outThinking.db";
	private static final int DATABASE_VERSION = 1;
	private static final String LOG_ERROR = "DAO";

	// HISTORY TABLE
	// ==========================================================================================
	private static final String HISTORY_TABLE = "history";
	public static final String HISTORY_ID = "_id";
	public static final String HISTORY_DATE = "date";
	public static final String HISTORY_PICKID = "pickId";
	public static final String HISTORY_BTNID = "btnId";
	public static final String HISTORY_IMGURL = "imgUrl";

	private static final String HISTORY_CREATE = "create table if not exists "
							  + HISTORY_TABLE + "(" 
							  + HISTORY_ID + " integer primary key autoincrement,"
							  + HISTORY_DATE  + " integer not null,"
							  + HISTORY_PICKID + " integer not null,"
							  + HISTORY_BTNID + " integer not null,"
							  + HISTORY_IMGURL + " text);";

	// if insert failed, return -1
	public long insertHistory(int date, int pickId, int btnId) {
		if (date == 0 || pickId == 0 || btnId == 0) {
			new NullPointerException(LOG_ERROR);
		}

		ContentValues cv = new ContentValues();
		cv.put(HISTORY_DATE, date);
		cv.put(HISTORY_PICKID, pickId);
		cv.put(HISTORY_BTNID, btnId);
		return mDB.insert(HISTORY_TABLE, null, cv);
	}

	public long updateHistory(int _id, String imgUrl) {
		if (imgUrl == null ) {
			new NullPointerException(LOG_ERROR);
		}

		ContentValues cv = new ContentValues();
		cv.put(HISTORY_IMGURL, imgUrl);
		return mDB.update(HISTORY_TABLE, cv, HISTORY_ID + " = ?", new String[] { ""+ _id });
	}
	
	public int deleteHistory (int id) {

		if (id == 0) {
			new NullPointerException(LOG_ERROR);
		}

		return mDB.delete(HISTORY_TABLE, HISTORY_ID + " = ?", new String[] { "" + id });
	}


	public Cursor selectHistoryByPickID(int pickId) {
		Cursor cs = mDB.query(true, HISTORY_TABLE, new String[] {
															   HISTORY_ID,
															   HISTORY_DATE,
															   HISTORY_PICKID,
															   HISTORY_BTNID},
															   HISTORY_PICKID + " = ?", 
															   new String[] {"" + pickId}, 
															   null, null, null, null);
		if(cs != null)
			cs.moveToFirst();
		return cs;
	}
	
	public Cursor selectHistoryByDate(int date) {
		Cursor cs = mDB.query(true, HISTORY_TABLE, new String[] {
															   HISTORY_ID,
															   HISTORY_DATE,
															   HISTORY_PICKID,
															   HISTORY_BTNID,
															   HISTORY_IMGURL},
															   HISTORY_DATE + " = ?", 
															   new String[] {"" + date}, 
															   null, null, null, null);
		if(cs != null)
			cs.moveToFirst();
		return cs;
	}
	
	public Cursor selectAllHistory() {
		Cursor cs = mDB.query(true, HISTORY_TABLE, new String[] { 
															   HISTORY_ID,
															   HISTORY_DATE,
															   HISTORY_PICKID,
															   HISTORY_BTNID,
															   HISTORY_IMGURL},
															   null, null, null, null,null, null);
		if (cs != null)
			cs.moveToFirst();
		return cs;
	}


	//
	// ==========================================================================================
	
	
	// CARD TABLE
	// ==========================================================================================
	private static final String CARD_TABLE = "card";
	public static final String CARD_ID = "_id";
	public static final String CARD_WORD = "word";
	
	private static final String CARD_CREATE = "create table if not exists "
			  								  + CARD_TABLE + "(" 
											  + CARD_ID + " integer primary key autoincrement,"
											  + CARD_WORD  + " text not null);";
	
	public long insertCard(String word) {
		if (word == null) {
			new NullPointerException(LOG_ERROR);
		}

		ContentValues cv = new ContentValues();
		cv.put(CARD_WORD, word);
		return mDB.insert(CARD_TABLE, null, cv);
	}

	public long updateCard(int id, String word) {
		if (id == 0 || word == null) {
			new NullPointerException(LOG_ERROR);
		}

		ContentValues cv = new ContentValues();
		cv.put(CARD_WORD, word);
		return mDB.update(CARD_TABLE, cv, CARD_ID + " = ?", new String[] { ""+ id });
	}
	
	public int deleteCard (int id) {

		if (id == 0) {
			new NullPointerException(LOG_ERROR);
		}

		return mDB.delete(CARD_TABLE, CARD_ID + " = ?", new String[] { "" + id });
	}

	public Cursor selectCardByWord(String word) {
		Cursor cs = mDB.query(true, CARD_TABLE, new String[] {
																  CARD_ID,
																  CARD_WORD},
																  CARD_WORD + " = ?", 
															   new String[] {"" + word}, 
															   null, null, null, null);
		if(cs != null)
			cs.moveToFirst();
		return cs;
	}
	
	public Cursor selectCardByID(int id) {
		Cursor cs = mDB.query(CARD_TABLE, new String[] {
																  CARD_ID,
																  CARD_WORD},
																  "_id = ?", 
															   new String[] {"" + id}, 
															   null, null, null);
		if(cs != null)
			cs.moveToFirst();
		return cs;
	}
	
	public Cursor selectAllCard() {
		Cursor cs = mDB.query(true, CARD_TABLE, new String[] { 
															   CARD_ID,
															   CARD_WORD},
															   null, null, null, null,null, null);
		if (cs != null)
			cs.moveToFirst();
		return cs;
	}
	
	// ==========================================================================================
	
	private DBHelper mDBHelper;
	private SQLiteDatabase mDB;
	private final Context context;

	public DAO(Context context) {
		this.context = context;
	}

	/*
	 * public DAO delete() throws SQLException{ mDBHelper = new
	 * DBHelper(context); }
	 */
	public DAO open() throws SQLException {
		mDBHelper = new DBHelper(context);
		mDB = mDBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDBHelper.close();
	}
	// ==========================================================================================

	
	
	// ///////////////////////////////////////////////////////inner class
	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CARD_CREATE);
			db.execSQL(HISTORY_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(LOG_ERROR, "Upgrading db from version" + oldVersion + " to"
					+ newVersion + ", which will destroy all old data");

			db.execSQL("drop table if exists " + HISTORY_TABLE);

			onCreate(db);
		}
	}
	// ///////////////////////////////////////////////////////inner class
}
