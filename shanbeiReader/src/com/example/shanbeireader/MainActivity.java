package com.example.shanbeireader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity {
	// midle
	private DrawerLayout myDrawerLayout;
	private TextView articleTitleE;
	private TextView articleTitleC;
	private TextView articleContent;
	// left
	private ListView articleList;
	// right
	private ImageView importImageView;
	private SeekBar levelBar;
	private TextView levelText;
	private Switch filterSwitch;
	// variables
	private int curLevel = 6;
	private String curContent = "";
	private boolean filterStatus = false;
	private TestAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        // initial title and content
        articleTitleE = (TextView)findViewById(R.id.titleE);
        articleTitleE.setText("Welcome to use");
        articleTitleC = (TextView)findViewById(R.id.titleC);
        articleTitleC.setText("欢迎使用");
        articleContent = (TextView) findViewById(R.id.text);
        articleContent.setText("\n\n\n操作提示：\n\n向左滑动选择文章。\n点击文章标题打开设置选项。");

        // create database
        mDbHelper = new TestAdapter(this);         
    	mDbHelper.createDatabase();     
        
        // initial left list        
       	mDbHelper.open();        	
       	ArrayList<String> arr = mDbHelper.getTitlesFromDB(); 
       	mDbHelper.close();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        articleList = (ListView)findViewById(R.id.list2);
        articleList.setAdapter(arrayAdapter);
        articleList.setOnItemClickListener(new OnItemClickListener()
        {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String title = (String) articleList.getItemAtPosition(position);
				mDbHelper.open();        	
				curContent = mDbHelper.queryContentByTitle(title); 
		       	mDbHelper.close();
				showTitle(title);
				showContent(curContent);
			}
        	
        });
        
        // seek bar to set the filter level
        levelText = (TextView) findViewById(R.id.lev);
        levelBar = (SeekBar) findViewById(R.id.seekBar);
        levelBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       

            @Override       
            public void onStopTrackingTouch(SeekBar seekBar) {      
                // TODO Auto-generated method stub      
            }       

            @Override       
            public void onStartTrackingTouch(SeekBar seekBar) {     
                // TODO Auto-generated method stub      
            }       

            @Override       
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
                // TODO Auto-generated method stub 
            	curLevel = progress;
            	levelText.setText("高亮等级:" + curLevel);
            	// show content
            	showContent(curContent);
            }       
        });    
        
        // button to open/close words filter
        filterSwitch = (Switch) findViewById(R.id.mySwitch);
        filterSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

        	   @Override
        	   public void onCheckedChanged(CompoundButton buttonView,
        	     boolean isChecked) {
	
	        	    if(isChecked){
	        	    	filterStatus = true;
		        	}
	        	    else{
	        	    	filterStatus = false;
	        	    }
	        	    // show content
	        	    showContent(curContent);
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
    

    public void showTitle(String title) {
    	String[] arr = title.split(" ");
    	String str1 = "";
    	for(int i = 0; i < arr.length - 1; ++i) {
    		str1 += arr[i];
    	}
    	articleTitleE.setText(str1);
    	articleTitleC.setText(arr[arr.length - 1]);
    }
    
    public void showContent(String str) {
    	mDbHelper.open();   
    	if(filterStatus == true) {
    		ArrayList<String> arr = new ArrayList<String>();
    		SpannableString s = new SpannableString(str);
			arr = mDbHelper.queryWordsDBByLevel(Integer.toString(curLevel)); 
		       	
		       	
        	for(int i = 0; i < arr.size(); ++i) {
        		Pattern p = Pattern.compile(" " + arr.get(i).replaceAll("\t", "") + "[\\n\\t\\s]");
        		Matcher m = p.matcher(s);
        		while (m.find()) {
                       int start = m.start();
                       int end = m.end();
                       s.setSpan(new ForegroundColorSpan(Color.GREEN), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                   }
        	}
        articleContent.setText(s);
    	}
    	else {
    		articleContent.setText(str);
    	}
    	mDbHelper.close();
		
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
