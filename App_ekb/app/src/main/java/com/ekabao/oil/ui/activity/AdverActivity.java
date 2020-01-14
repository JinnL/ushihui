package com.ekabao.oil.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ekabao.oil.R;
import com.ekabao.oil.util.LogUtils;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：js_app
 * 类描述：3秒广告页面
 * 创建人：shuc
 * 创建时间：2017/2/17 13:09
 * 修改人：DELL
 * 修改时间：2017/2/17 13:09
 * 修改备注：
 */
public class AdverActivity extends BaseActivity implements View.OnClickListener {
   /* @BindView(R.id.img_adver)
    ImageView imgAdver;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_time)
    LinearLayout llTime;*/
    private String imgUrl, location, title;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.adver;
    }

    @Override
    protected void initParams() {
        imgUrl = getIntent().getStringExtra("imgUrl");
        location = getIntent().getStringExtra("location");
        title = getIntent().getStringExtra("title");

        LogUtils.e("3秒广告页面"+imgUrl);

        Glide.with(this).load(imgUrl).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                //加载异常
                LogUtils.e("onException");
                startActivity(new Intent(AdverActivity.this, MainActivity.class));
                finish();

                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                //加载成功
                //view.setImageDrawable(resource);
                 LogUtils.e("onResourceReady");
                mHandler.sendEmptyMessage(UPDATE_TEAY_TIME);
                return false;
            }
        }).into(img_adver);



       /* //示例二
        Glide.with(this).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                //图片加载完成
                img_adver.setImageBitmap(bitmap);
            }
        });*/
       /* Glide.with(this).load(imgUrl)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        // 可替换成进度条
                        Toast.makeText(context, "图片加载失败", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        // 图片加载完成，取消进度条
                        Toast.makeText(context, "图片加载成功", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }).error(R.mipmap.ic_launcher_round)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);*/

        //imgUrl = "https://img.zcool.cn/community/0174295541fe180000019ae91f2478.jpg@1280w_1l_2o_100sh.webp";

        //Glide.with(this).load(imgUrl).into(img_adver);
      /*  Glide.with(this).load(imgUrl).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(img_adver);*/

      /*  OkHttpUtils
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
                        *//*new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mHandler.sendEmptyMessage(UPDATE_TEAY_TIME);
                            }
                        }).start();*//*
                        mHandler.sendEmptyMessage(UPDATE_TEAY_TIME);

                    }
                });*/


        img_adver.setOnClickListener(this);
        ll_time.setOnClickListener(this);
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (bp != null && !bp.isRecycled()) {
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
        switch (view.getId()) {
            case R.id.ll_time:
                mHandler.removeCallbacksAndMessages(null);
                LogUtils.i("ll_time");
                startActivity(new Intent(AdverActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.img_adver:
                mHandler.removeCallbacksAndMessages(null);
                LogUtils.i("img_adver location：" + location + ", title: " + title);
                startActivity(new Intent(AdverActivity.this, MainActivity.class)
                        .putExtra("location", location+ "?app=true")
                        .putExtra("title", title)
                );
                finish();
                break;
        }
    }

    private final AdverHandler mHandler = new AdverHandler(this);



    private static class AdverHandler extends Handler {

        private WeakReference<AdverActivity> mContext;

        AdverHandler(AdverActivity av) {
            mContext = new WeakReference<>(av);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mContext.get() == null) {
                return;
            }
            mContext.get().receiveMsg(msg);

        }
    }


    /**
     * 处理Handler收到的消息
     */
    private void receiveMsg(Message msg) {
        if (msg.what == UPDATE_TEAY_TIME) {
            if (delayTime > 0) {
                //tv_time.setText(delayTime+"s");
                tv_time.setText(" " + delayTime);
                mHandler.sendEmptyMessageDelayed(UPDATE_TEAY_TIME, 1000);
                delayTime--;
            } else {
                startActivity(new Intent(AdverActivity.this, MainActivity.class));
                finish();
            }
        }
    }

}
