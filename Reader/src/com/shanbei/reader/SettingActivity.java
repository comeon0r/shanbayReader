package com.shanbei.reader;

import com.shanbei.reader.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SettingActivity extends Activity {

	// UI Component
	private SeekBar levelBar;
	private TextView levelText;
	private Switch filterSwitch;
	// variables
	private int curHighlightLevel = 6;
	private boolean curHighlightStatus = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.setting_activity);

		// get intent values
		Intent intent = getIntent();
		curHighlightLevel = intent.getIntExtra("curLevel", curHighlightLevel);
		curHighlightStatus = intent.getBooleanExtra("filterStatus",
				curHighlightStatus);

		// initial highlight level text
		levelText = (TextView) findViewById(R.id.lev);
		//levelText.setText(R.string.level + curHighlightLevel);

		// initial highlight seek bar and add listener
		levelBar = (SeekBar) findViewById(R.id.seekBar);
		levelBar.setProgress(curHighlightLevel);
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
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				curHighlightLevel = progress;
				levelText.setText("高亮等级:" + curHighlightLevel);
			}
		});

		// initial highlight switch and add listener
		filterSwitch = (Switch) findViewById(R.id.mySwitch);
		filterSwitch.setChecked(curHighlightStatus);
		filterSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					curHighlightStatus = true;
				} else {
					curHighlightStatus = false;
				}
			}
		});
	}

	/**
	 * Return values to HomeActivity
	 * @param view
	 */
	public void backToMain(View view) {
		Intent myIntent = new Intent(SettingActivity.this, HomeActivity.class);
		myIntent.putExtra("curLevel", curHighlightLevel);
		myIntent.putExtra("filterStatus", curHighlightStatus);
		SettingActivity.this.setResult(0, myIntent);
		SettingActivity.this.finish();
	}

}
