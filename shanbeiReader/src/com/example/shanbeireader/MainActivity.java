package com.example.shanbeireader;

import com.nineoldandroids.view.ViewHelper;

import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends FragmentActivity {
	
	private DrawerLayout myDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
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
				System.out.println("hello list!!!");
			}
        	
        });
        
        // title
        TextView title = (TextView)findViewById(R.id.title);
        TextView text = (TextView)findViewById(R.id.text);
        title.setText("Finding fossil man 发现化石人");
        String tmp = "First listen and then answer the following question.\n听录音，然后回答以下问题。\n\nWhy are legends handed down by storytellers useful?\n\n    We can read of things that happened 5,000 years ago in the Near East, where people first learned to write. But there are some parts of the word where even now people cannot write. The only way that they can preserve their history is to recount it as sagas -- legends handed down from one generation of another. These legends are useful because they can tell us something about migrations of people who lived long ago, but none could write down what they did. Anthropologists wondered where the remote ancestors of the Polynesian peoples now living in the Pacific Islands came from. The sagas of these people explain that some of them came from Indonesia about 2,000 years ago.";
        for(int i = 0; i < 10; ++i)
        	tmp += tmp;
        text.setText(tmp);
        
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
