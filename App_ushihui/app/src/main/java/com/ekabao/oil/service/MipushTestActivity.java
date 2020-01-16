package com.ekabao.oil.service;

import android.content.Intent;
import android.os.Bundle;

import com.ekabao.oil.R;
import com.ekabao.oil.util.LogUtils;
import com.umeng.message.UmengNotifyClickActivity;

import org.android.agoo.common.AgooConstants;

/**
 * ${APP_NAME}  App_oil
 *  友盟的小米推送
 * @time 2019/3/25 14:03
 * Created by lj on 2019/3/25 14:03.
 */


public class MipushTestActivity extends UmengNotifyClickActivity {

    private static String TAG = MipushTestActivity.class.getName();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mipush);
    }

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        LogUtils.e(TAG+body);
    }
}