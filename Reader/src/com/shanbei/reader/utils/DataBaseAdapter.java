package com.shanbei.reader.utils;

import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBaseAdapter {
	protected static final String TAG = "DataAdapter";
	private final Context mContext;
	private SQLiteDatabase mDb;
	private DataBaseHelper mDbHelper;

	public DataBaseAdapter(Context context) {
		this.mContext = context;
		mDbHelper = new DataBaseHelper(mContext);
	}

	public DataBaseAdapter createDatabase() throws SQLException {
		try {
			mDbHelper.createDataBase();
		} catch (IOException mIOException) {
			Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
			throw new Error("UnableToCreateDatabase");
		}
		return this;
	}

	public DataBaseAdapter open() throws SQLException {
		try {
			mDbHelper.openDataBase();
			mDbHelper.close();
			mDb = mDbHelper.getReadableDatabase();
		} catch (SQLException mSQLException) {
			Log.e(TAG, "open >>" + mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public ArrayList<String> getTitlesFromDB() {
		ArrayList<String> arr = new ArrayList<String>();
		try {
			String sql = "SELECT title FROm articles_table";
			Cursor mCur = mDb.rawQuery(sql, null);
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

	public String queryContentByTitle(String title) {
		try {
			Cursor mCur = mDb.rawQuery(
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

	public ArrayList<String> queryWordsDBByLevel(String level) {
		try {
			Cursor mCur = mDb.rawQuery(
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
