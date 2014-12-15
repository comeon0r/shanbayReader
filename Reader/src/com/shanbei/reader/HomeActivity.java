package com.shanbei.reader;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.shanbei.reader.R;
import com.shanbei.reader.utils.DataBaseAdapter;
import com.shanbei.reader.utils.SlidingMenu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class HomeActivity extends Activity {
	// UI Component
	private TextView articleTitleEng;
	private TextView articleTitleChs;
	private TextView articleContent;
	private ListView articleList;
	// variables
	private DataBaseAdapter mDbHelper;
	private int curHighlightLevel = 6;
	private boolean curHighlightStatus = false;
	private String curTitle = "Welcome to use 欢迎使用";
	private String curContent = "\n\n\n操作提示：\n\n向右滑动选择文章。\n点击文章标题打开设置选项。";	
	private SlidingMenu mMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.home_activity);
		
		mMenu = (SlidingMenu) findViewById(R.id.id_menu);

		// create database
		mDbHelper = new DataBaseAdapter(this);
		mDbHelper.createDatabase();

		// initial the article title and content
		articleTitleEng = (TextView) findViewById(R.id.titleE);
		articleTitleChs = (TextView) findViewById(R.id.titleC);
		articleContent = (TextView) findViewById(R.id.text);
		showTitle(curTitle);
		showContent(curContent);

		// initial the article list
		mDbHelper.open();
		ArrayList<String> arr = mDbHelper.getTitlesFromDB();
		mDbHelper.close();
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, arr);
		articleList = (ListView) findViewById(R.id.article_list);
		articleList.setAdapter(arrayAdapter);
		articleList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				curTitle = (String) articleList.getItemAtPosition(position);
				mDbHelper.open();
				curContent = mDbHelper.queryContentByTitle(curTitle);
				mDbHelper.close();
				showTitle(curTitle);
				showContent(curContent);
				toggleMenu();
			}
		});
	}

	/**
	 * Show the article title
	 * @param title
	 */
	public void showTitle(String title) {
		String[] arr = title.split(" ");
		String str1 = "";
		for (int i = 0; i < arr.length - 1; ++i) {
			str1 = str1 + arr[i] + " ";
		}
		articleTitleEng.setText(str1);
		articleTitleChs.setText(arr[arr.length - 1]);
	}

	/**
	 * Show the article text content
	 * @param str
	 */
	public void showContent(String str) {
		if (curHighlightStatus == true) {
			mDbHelper.open();
			ArrayList<String> arr = new ArrayList<String>();
			SpannableString s = new SpannableString(str);
			arr = mDbHelper.queryWordsDBByLevel(Integer.toString(curHighlightLevel));

			for (int i = 0; i < arr.size(); ++i) {
				Pattern p = Pattern.compile(" "
						+ arr.get(i).replaceAll("\t", "") + "[\\n\\t\\s]");
				Matcher m = p.matcher(s);
				while (m.find()) {
					int start = m.start();
					int end = m.end();
					s.setSpan(new ForegroundColorSpan(Color.GREEN), start, end,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			articleContent.setText(s);
			mDbHelper.close();
		} else {
			articleContent.setText(str);
		}
	}

	public void openSettingActivity(View view) {
		Intent myIntent = new Intent(HomeActivity.this, SettingActivity.class);
		myIntent.putExtra("curLevel", curHighlightLevel);
		myIntent.putExtra("filterStatus", curHighlightStatus);
		startActivityForResult(myIntent, 0);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0 && resultCode == 0) {
			if (intent != null) {
				curHighlightLevel = intent.getIntExtra("curLevel", curHighlightLevel);
				curHighlightStatus = intent.getBooleanExtra("filterStatus",
						curHighlightStatus);
			}
			showTitle(curTitle);
			showContent(curContent);
			// 
			System.out.println("========================================");
		}
	}
	
	public void toggleMenu()
	{
		mMenu.toggle();
	}
}
