package com.shanbay.reader.utils;

import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBaseAdapter {
	protected static final String TAG = "DataAdapter";
	private final Context myContext;
	private SQLiteDatabase myDB;
	private DataBaseHelper myDBHelper;

	public DataBaseAdapter(Context context) {
		this.myContext = context;
		myDBHelper = new DataBaseHelper(myContext);
	}

	public DataBaseAdapter createDatabase() throws SQLException {
		try {
			myDBHelper.createDataBase();
		} catch (IOException mIOException) {
			Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
			throw new Error("UnableToCreateDatabase");
		}
		return this;
	}

	public DataBaseAdapter open() throws SQLException {
		try {
			myDBHelper.openDataBase();
			myDBHelper.close();
			myDB = myDBHelper.getReadableDatabase();
		} catch (SQLException mSQLException) {
			Log.e(TAG, "open >>" + mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() {
		myDBHelper.close();
	}

	/**
	 * Get article titles from database
	 * @return
	 */
	public ArrayList<String> getTitlesFromDB() {
		ArrayList<String> arr = new ArrayList<String>();
		try {
			String sql = "SELECT title FROm articles_table";
			Cursor mCur = myDB.rawQuery(sql, null);
			while (mCur.moveToNext()) {
				arr.add(mCur.getString(mCur.getColumnIndex("title")));
			}
			if (mCur != null) {
				mCur.moveToNext();
			}
			return arr;
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	/**
	 * Get the article content from database, specified article title
	 * @param title
	 * @return
	 */
	public String getArticleContentByTitle(String title) {
		try {
			Cursor mCur = myDB.rawQuery(
					"select content from articles_table where title like ?",
					new String[] { title });
			mCur.moveToFirst();
			String content = mCur.getString(mCur
					.getColumnIndexOrThrow("content"));
			return content;
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	/**
	 * Get the highlight words from database, specified the level
	 * @param level
	 * @return
	 */
	public ArrayList<String> getWordsByLevel(String level) {
		try {
			Cursor mCur = myDB.rawQuery(
					"select word from words_table where level<=?",
					new String[] { level });
			ArrayList<String> arr = new ArrayList<String>();
			while (mCur.moveToNext()) {
				arr.add(mCur.getString(mCur.getColumnIndex("word")));
			}
			return arr;
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}
}
