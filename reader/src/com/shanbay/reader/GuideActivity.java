package com.shanbay.reader;

import java.util.ArrayList;
import java.util.List;
import com.shanbay.reader.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends Activity {

	private ViewPager myPager;
	private LinearLayout myDotsLayout;
	private ImageButton enterBtn;

	private List<View> viewList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//
		SharedPreferences sp = getSharedPreferences("isFirstIn", Activity.MODE_PRIVATE);
		boolean isFirstIn = sp.getBoolean("isFirstIn", true);
		if(!isFirstIn) {
			openHome();
		}
		else {
			SharedPreferences.Editor editor = sp.edit();
			editor.putBoolean("isFirstIn", false);
			editor.commit();
		}
		
		
		setContentView(R.layout.guide_activity);

		myPager = (ViewPager) findViewById(R.id.guide_viewpager);
		myDotsLayout = (LinearLayout) findViewById(R.id.guide_dots);
		enterBtn = (ImageButton) findViewById(R.id.guide_btn);

		initPager();
		myPager.setAdapter(new ViewPagerAdapter(viewList));
		myPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				for (int i = 0; i < myDotsLayout.getChildCount(); i++) {
					if (i == arg0) {
						myDotsLayout.getChildAt(i).setSelected(true);
					} else {
						myDotsLayout.getChildAt(i).setSelected(false);
					}
				}
				if (arg0 == myDotsLayout.getChildCount() - 1) {
					enterBtn.setVisibility(View.VISIBLE);
				} else {
					enterBtn.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		enterBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openHome();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_guide, menu);
		return true;
	}

	private void initPager() {
		viewList = new ArrayList<View>();
		int[] images = new int[] { R.drawable.r1, R.drawable.r2, R.drawable.r3,
				R.drawable.r4 };
		int[] texts = new int[] { R.drawable.text, R.drawable.text,
				R.drawable.text, R.drawable.text4 };
		for (int i = 0; i < images.length; i++) {
			viewList.add(initView(images[i], texts[i]));
		}
		initDots(images.length);
	}

	private void initDots(int count) {
		for (int j = 0; j < count; j++) {
			myDotsLayout.addView(initDot());
		}
		myDotsLayout.getChildAt(0).setSelected(true);
	}

	private View initDot() {
		return LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.layout_dot, null);
	}

	private View initView(int res, int text) {
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.item_guide, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.iguide_img);
		ImageView textview = (ImageView) view.findViewById(R.id.iguide_text);
		imageView.setImageResource(res);
		textview.setImageResource(text);
		return view;
	}

	private void openHome() {
		Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		finish();
	}

	class ViewPagerAdapter extends PagerAdapter {

		private List<View> data;

		public ViewPagerAdapter(List<View> data) {
			super();
			this.data = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(data.get(position));
			return data.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(data.get(position));
		}

	}

}
