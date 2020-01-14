package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 项目名称：js_app
 * 类描述：3秒广告页面
 * 创建人：shuc
 * 创建时间：2017/2/17 13:09
 * 修改人：DELL
 * 修改时间：2017/2/17 13:09
 * 修改备注：
 */
public class AdverActivity extends BaseActivity implements View.OnClickListener{
    private String imgUrl,location,title;

    private int delayTime = 3;// 广告4秒倒计时
    private final int UPDATE_TEAY_TIME = 10000;
    @BindView(R.id.img_adver)
    ImageView img_adver;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.ll_time)
    LinearLayout ll_time;
    private Bitmap bp = null;

    @Override
    protected int getLayoutId() {
        return R.layout.adver;
    }

    @Override
    protected void initParams() {
        imgUrl = getIntent().getStringExtra("imgUrl");
        location = getIntent().getStringExtra("location");
        title = getIntent().getStringExtra("title");
        OkHttpUtils
                .get()//
                .url(imgUrl)//
                .build()//
                .connTimeOut(2000)
                .writeTimeOut(2000)
                .readTimeOut(2000)
                .execute(new BitmapCallback(){
                    @Override
                    public void onError(Call call, Exception e) {
                        startActivity(new Intent(AdverActivity.this, MainActivity.class));
                        finish();
                    }
                    @Override
                    public void onResponse(final Bitmap bitmap) {
                        if(bitmap != null){
                            img_adver.setImageBitmap(bitmap);
                        }
                        /*new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mHandler.sendEmptyMessage(UPDATE_TEAY_TIME);
                            }
                        }).start();*/
                        mHandler.sendEmptyMessage(UPDATE_TEAY_TIME);

                    }
                });

        img_adver.setOnClickListener(this);
        ll_time.setOnClickListener(this);
    }

    /*private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEAY_TIME:
                    if(!isGo){
                        if (delayTime > 0) {
                            tv_time.setText(delayTime+"s");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessageDelayed(UPDATE_TEAY_TIME, 1000);
                                }
                            }).start();
                            delayTime--;
                        } else {
                            startActivity(new Intent(AdverActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                    break;
            }
        }
    };*/

    @Override
    protected void onPause() {
        super.onPause();
        if(bp != null && !bp.isRecycled()){
            bp.recycle();
            bp = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_time:
                LogUtils.i("ll_time" );
                startActivity(new Intent(AdverActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.img_adver:
                LogUtils.i("img_adver location：" + location + ", title: " + title);
                startActivity(new Intent(AdverActivity.this, MainActivity.class)
                        .putExtra("location", location)
                        .putExtra("title", title)
                );
                finish();
                break;
        }
    }

    private final AdverHandler mHandler = new AdverHandler(this);
    private static class AdverHandler extends Handler{

        private WeakReference<AdverActivity> mContext;
        AdverHandler(AdverActivity av){
            mContext = new WeakReference<>(av);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mContext.get()==null){
                return;
            }
            mContext.get().receiveMsg(msg);

        }
    }

    /**
     * 处理Handler收到的消息
     */
    private void receiveMsg(Message msg){
        if(msg.what == UPDATE_TEAY_TIME){
            if (delayTime > 0) {
                //tv_time.setText(delayTime+"s");
                tv_time.setText(" "+delayTime);
                mHandler.sendEmptyMessageDelayed(UPDATE_TEAY_TIME, 1000);
                delayTime--;
            } else {
                startActivity(new Intent(AdverActivity.this, MainActivity.class));
                finish();
            }
        }
    }

}
