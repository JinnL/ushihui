package com.ekabao.oil.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.util.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 易卡宝  App
 * 主界面 发现
 *
 * @time 2018/8/31 14:59
 * Created by lj on 2018/8/31 14:59.
 */

public class SafeFragment extends BaseFragment {


    @BindView(R.id.title_lefttextview)
    TextView titleLefttextview;
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_centerimageview)
    ImageView titleCenterimageview;
    @BindView(R.id.title_righttextview)
    TextView titleRighttextview;
    @BindView(R.id.title_rightimageview)
    ImageView titleRightimageview;
    @BindView(R.id.view_line_bottom)
    View viewLineBottom;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    Unbinder unbinder;
    @BindView(R.id.bt_xieyi)
    Button btXieyi;
    @BindView(R.id.wv_safe)
    WebView wvSafe;

    public static SafeFragment instance() {
        SafeFragment view = new SafeFragment();
        return view;
    }

    @Override
    protected int getLayoutId() {
      /*  //或者代码设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/

        return R.layout.fragment_safe;
    }

    @Override
    protected void initParams() {

        titleLeftimageview.setVisibility(View.GONE);
        titleCentertextview.setText("安全保障");
        titleCentertextview.setTextColor(Color.parseColor("#ffffff"));
        //rlTitle.setVisibility(View.GONE);
        rlTitle.setBackgroundColor(Color.parseColor("#00000000"));
        viewLineBottom.setVisibility(View.GONE);

        rlTitle.setVisibility(View.GONE);

        btXieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL", UrlConfig.supervise + "?app=true")
                        .putExtra("TITLE", "浙商银行资金监管协议"));
            }
        });


        /**
         *
         * 设置WebView的属性，此时可以去执行JavaScript脚本
         */
        final WebSettings settings = wvSafe.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setSupportZoom(true);
        // int fontSize = 10;
        // settings.setTextSize(TextSize.NORMAL);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(20000);
        settings.setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wvSafe.setWebContentsDebuggingEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new Object() {
                public void setLoadWithOverviewMode(boolean overview) {
                    settings.setLoadWithOverviewMode(overview);

                }
            }.setLoadWithOverviewMode(true);
        }
        //settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        wvSafe.setWebViewClient(new MyWebView());

        wvSafe.loadUrl( "https://m.youmochou.com/activitycenter?app=true");

       // LogUtils.i("--->loadUrl URL5：" + URL + "&uid=" + preferences.getString("uid", "") + "&afid=" + afid + "&token=" + preferences.getString("token", ""));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
       /* //或者代码设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    class MyWebView extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.e("--->shouldOverride url==" + url);

            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtils.i("--->onPageStarted url==" + url);
            super.onPageStarted(view, url, favicon);
            showWaitDialog("加载中...", true, "");

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtils.i("--->onPageFinished url==" + url);
            super.onPageFinished(view, url);

            dismissDialog();

        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            LogUtils.i("--->onReceivedError failingUrl==" + failingUrl + " ,description==" + description);
            super.onReceivedError(view, errorCode, description, failingUrl);

            //ToastMaker.showShortToast("加载失败");
        }

    }
}
