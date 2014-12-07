package com.example.shanbeireader;

import java.io.IOException; 
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context; 
import android.database.Cursor; 
import android.database.SQLException; 
import android.database.sqlite.SQLiteDatabase; 
import android.util.Log; 
 
public class TestAdapter  
{ 
    protected static final String TAG = "DataAdapter"; 
 
    private final Context mContext; 
    private SQLiteDatabase mDb; 
    private DataBaseHelper mDbHelper; 
 
    public TestAdapter(Context context)  
    { 
        this.mContext = context; 
        mDbHelper = new DataBaseHelper(mContext); 
    } 
 
    public TestAdapter createDatabase() throws SQLException  
    { 
        try  
        { 
            mDbHelper.createDataBase(); 
        }  
        catch (IOException mIOException)  
        { 
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase"); 
            throw new Error("UnableToCreateDatabase"); 
        } 
        return this; 
    } 
 
    public TestAdapter open() throws SQLException  
    { 
        try  
        { 
            mDbHelper.openDataBase(); 
            mDbHelper.close(); 
            mDb = mDbHelper.getReadableDatabase(); 
        }  
        catch (SQLException mSQLException)  
        { 
            Log.e(TAG, "open >>"+ mSQLException.toString()); 
            throw mSQLException; 
        } 
        return this; 
    } 
 
    public void close()  
    { 
        mDbHelper.close(); 
    } 
 
     public ArrayList<String> getTitlesFromDB() 
     { 
    	 ArrayList<String> arr = new ArrayList<String>();
         try 
         { 
             //String sql ="SELECT title, Name, Email FROm articles_table"; 
        	 String sql ="SELECT title FROm articles_table";
             Cursor mCur = mDb.rawQuery(sql, null); 
        	 
        	 while(mCur.moveToNext()){  
             	//System.out.println(mCur.getString(mCur.getColumnIndex("title")));
        		 arr.add(mCur.getString(mCur.getColumnIndex("title")));
             	/*String str = cursor.getString(cursor.getColumnIndex("title"));
             	String tmp = str.substring(0, str.length() - 1) + "\n" + str.substring(str.length() - 1, str.length());
             	arr.add(tmp);*/
             } 
 
             if (mCur!=null) 
             { 
                mCur.moveToNext(); 
             } 
             return arr; 
         } 
         catch (SQLException mSQLException)  
         { 
             Log.e(TAG, "getTestData >>"+ mSQLException.toString()); 
             throw mSQLException; 
         } 
     }
     
     public String queryContentByTitle(String title) {
    	 
    	 
    	 try 
         { 
    		 Cursor mCur = mDb.rawQuery("select content from articles_table where title like ?", new String[]{title});
    		 
    		 mCur.moveToFirst();
             String content = mCur.getString(mCur.getColumnIndexOrThrow("content"));
             return content; 
         } 
         catch (SQLException mSQLException)  
         { 
             Log.e(TAG, "getTestData >>"+ mSQLException.toString()); 
             throw mSQLException; 
         } 
     }
     
     public ArrayList<String> queryWordsDBByLevel(String level) {
    	 try 
         { 
    		 //Cursor mCur = mDb.rawQuery("select word from words_table where level=?", new String[]{level});
    		 //Cursor mCur = mDb.rawQuery("select word from words_table where level=?", new String[]{level});
    		 Cursor mCur = mDb.rawQuery("select word from words_table where level<=?", new String[]{level});
    		 
    		 ArrayList<String> arr = new ArrayList<String>();
             while(mCur.moveToNext()){  
                 arr.add(mCur.getString(mCur.getColumnIndex("word"))); 
                 //System.out.println("query " + mCur.getString(mCur.getColumnIndex("word")));
             }  
             return arr;
         } 
         catch (SQLException mSQLException)  
         { 
             Log.e(TAG, "getTestData >>"+ mSQLException.toString()); 
             throw mSQLException; 
         } 
     }
     
     

 	public boolean SaveEmployee(String name, String email) 
 	{
 		try
 		{
 			ContentValues cv = new ContentValues();
 			cv.put("Name", name);
 			cv.put("Email", email);
 			
 			mDb.insert("Employees", null, cv);

 			Log.d("SaveEmployee", "informationsaved");
 			return true;
 			
 		}
 		catch(Exception ex)
 		{
 			Log.d("SaveEmployee", ex.toString());
 			return false;
 		}
 	}
     

} 

