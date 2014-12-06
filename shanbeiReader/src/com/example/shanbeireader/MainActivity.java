package com.example.shanbeireader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.nineoldandroids.view.ViewHelper;

import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends FragmentActivity {
	
	private DrawerLayout myDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        
        // title
        TextView title = (TextView)findViewById(R.id.title);
        //TextView text = (TextView)findViewById(R.id.text);
        title.setText("Finding fossil man 发现化石人");
        
        final TextView content = (TextView) findViewById(R.id.text);
        String str = "fsadgfag abc fdsfagate";
        SpannableString s = new SpannableString(str);
    
        Pattern p = Pattern.compile("abc");
        
        
        Matcher m = p.matcher(s);

        while (m.find()) {
            int start = m.start();
            int end = m.end();
            System.out.println("start " + start);
            System.out.println("end " + end);
            s.setSpan(new ForegroundColorSpan(Color.GREEN), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        content.setText(s);
        


        // import articles
        ImageView importArticles = (ImageView)findViewById(R.id.rdfile);
        importArticles.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				createWordsDB();
				queryWordsDB();
				return false;
			}
		});
        
        
        // list2 update!
        ListView list2 = (ListView)findViewById(R.id.list2);
        String[] arr = {"a", "b", "c"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        list2.setAdapter(arrayAdapter);
        list2.setOnItemClickListener(new OnItemClickListener()
        {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println("hello list!!!" + position + " " + id);
				if(position == 1) {
					content.setText("First listen and then answer the following question.\n听录音，然后回答以下问题");
				}
				else {
					content.setText("!!!!!!!!!!!!!!!!!!!!!");
				}
				
			}
        	
        });
        
        // acquire my drawer layout
        myDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
        myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        // listen to my drawer layout
        myDrawerLayout.setDrawerListener(new DrawerListener()
		{
			@Override
			public void onDrawerStateChanged(int newState)
			{
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset)
			{
				View mContent = myDrawerLayout.getChildAt(0);
				View mMenu = drawerView;
				float scale = 1 - slideOffset;
				float rightScale = 0.8f + scale * 0.2f;

				if (drawerView.getTag().equals("LEFT"))
				{

					float leftScale = 1 - 0.3f * scale;

					// use nineoldandroids API
					ViewHelper.setScaleX(mMenu, leftScale);
					ViewHelper.setScaleY(mMenu, leftScale);
					ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
					ViewHelper.setTranslationX(mContent,
							mMenu.getMeasuredWidth() * (1 - scale));
					ViewHelper.setPivotX(mContent, 0);
					ViewHelper.setPivotY(mContent,
							mContent.getMeasuredHeight() / 2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);
				} else
				{
					ViewHelper.setTranslationX(mContent,
							-mMenu.getMeasuredWidth() * slideOffset);
					ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
					ViewHelper.setPivotY(mContent,
							mContent.getMeasuredHeight() / 2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);
				}

			}

			@Override
			public void onDrawerOpened(View drawerView)
			{
			}

			@Override
			public void onDrawerClosed(View drawerView)
			{
				myDrawerLayout.setDrawerLockMode(
						DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
			}
		});
    }
    

    
    public void createWordsDB() {
    	MainActivity.this.deleteDatabase("wordsDB");
		WordsDBHelper dbHelper = new WordsDBHelper(MainActivity.this,"wordsDB",null,1);
		SQLiteDatabase db =dbHelper.getReadableDatabase();
		//生成ContentValues对象 //key:列名，value:想插入的值    
        ContentValues cv = new ContentValues();
        AssetManager am = MainActivity.this.getAssets();
        try {
        	// open words file
			InputStream is = am.open("nce4_words");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String str = "";
			if (is!=null) {	
				str = reader.readLine();
				while ((str = reader.readLine()) != null) {	
					System.out.println(str);
					cv.clear();
					cv.put("word", str.substring(0, str.length() - 1));
					cv.put("level", Integer.parseInt(str.substring(str.length() - 1, str.length())));
					db.insert("words_db", null, cv);
					System.out.println("word " + str.substring(0, str.length() - 1));
					System.out.println("level " + str.substring(str.length() - 1, str.length()));
				}				
			}		
			is.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        db.close();
    }
    
    public void queryWordsDB() {
    	WordsDBHelper dbHelper = new WordsDBHelper(MainActivity.this,"wordsDB",null,1);  
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from words_db where word like ?", new String[]{"%beast%"});
        while(cursor.moveToNext()){  
            String word = cursor.getString(cursor.getColumnIndex("word"));  
            String level = cursor.getString(cursor.getColumnIndex("level"));  
            System.out.println("query------->" + "单词："+word+" "+"级别："+level);  
        }  
        //关闭数据库  
        db.close();  
    }
    
    
    public void OpenRightMenu(View view)
	{
    	myDrawerLayout.openDrawer(Gravity.RIGHT);
    	myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
				Gravity.RIGHT);
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
