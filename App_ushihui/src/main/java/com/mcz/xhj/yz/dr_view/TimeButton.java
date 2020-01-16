package com.mcz.xhj.yz.dr_view;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mcz.xhj.yz.dr_application.LocalApplication;

public class TimeButton extends Button implements OnClickListener {
	private long lenght = 60 * 1000;
	private String textafter = "";
	private String textbefore = "";
	private final String TIME = "time";
	private final String CTIME = "ctime";
	private OnClickListener mOnclickListener;
	private Timer t;
	private TimerTask tt;
	private long time;
	private boolean flag = true;
	Map<String, Long> map = new HashMap<String, Long>();

	public TimeButton(Context context) {
		super(context);
		setOnClickListener(this);

	}

	public TimeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	@SuppressLint("HandlerLeak")
	Handler han = new Handler() {
		public void handleMessage(android.os.Message msg) {
			TimeButton.this.setText(textafter+"("+time / 1000+"秒)");
			time -= 1000;
			if (time <= 0) {
				TimeButton.this.setEnabled(true);
				TimeButton.this.setText(textbefore);
				clearTimer();
				LocalApplication.time=1;
			}
		};
	};

	private void initTimer() {
		time = lenght;
		t = new Timer();
		tt = new TimerTask() {

			@Override
			public void run() {
//				Log.e("yung", time / 1000 + "");
				han.sendEmptyMessage(0x01);
			}
		};
	}

	private void clearTimer() {
		if (tt != null) {
			tt.cancel();
			tt = null;
		}
		if (t != null)
			t.cancel();
		t = null;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		if (l instanceof TimeButton) {
			super.setOnClickListener(l);
		} else
			this.mOnclickListener = l;
	}

	@Override
	public void onClick(View v) {
		if (mOnclickListener != null)
			mOnclickListener.onClick(v);
		if(flag){
			initTimer();
			this.setText(time / 1000 + textafter);
			this.setEnabled(false);
			t.schedule(tt, 0, 1000);
		}
		// t.scheduleAtFixedRate(task, delay, period);
	}

	
	public void onDestroy() {
		if (LocalApplication.map == null)
			LocalApplication.map = new HashMap<String, Long>();
		LocalApplication.map.put(TIME, time);
		LocalApplication.map.put(CTIME, System.currentTimeMillis());
		clearTimer();
		Log.e("yung", "onDestroy");
	}

	
	public void onCreate(Bundle bundle) {
		Log.e("yung", LocalApplication.map + "");
		if (LocalApplication.map == null)
			return;
		if (LocalApplication.map.size() <= 0)
			return;
		long time = System.currentTimeMillis() - LocalApplication.map.get(CTIME)
				- LocalApplication.map.get(TIME);
		LocalApplication.map.clear();
		if(time>0){
			LocalApplication.time=1;
		}
		if (time > 0)
			return;
		else {
			initTimer();
			this.time = Math.abs(time);
			t.schedule(tt, 0, 1000);
			this.setText(time + textafter);
			this.setEnabled(false);
		}
	}
	public TimeButton setTextAfter(String text1) {
		this.textafter = text1;
		return this;
	}

	public TimeButton setTextBefore(String text0) {
		this.textbefore = text0;
		this.setText(textbefore);
		return this;
	}

	public TimeButton setLenght(long lenght) {
		this.lenght = lenght;
		return this;
	}
	public TimeButton startTime() {
		if(time<=0){
			initTimer();
			this.setText(time / 1000 + textafter);
			this.setEnabled(false);
			t.schedule(tt, 0, 1000);
		}
		return null;
	}
	public TimeButton stopTime() {
		flag = false;
		if(time>=0){
			TimeButton.this.setEnabled(true);
			TimeButton.this.setText(textbefore);
			clearTimer();
		}
		return null;
	}
}