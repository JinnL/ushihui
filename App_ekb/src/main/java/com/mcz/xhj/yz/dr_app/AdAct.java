package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.os.Handler;
import com.mcz.xhj.R;

public class AdAct extends BaseActivity {

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_ad;
	}

	@Override
	protected void initParams() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				startActivity(new Intent(AdAct.this, MainActivity.class));
				finish();
			}
		}, 2000);
	}

}
