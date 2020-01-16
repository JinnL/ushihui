package com.mcz.xhj.yz.dr_app.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_app.BaseActivity;
import com.mcz.xhj.yz.dr_app.WebViewActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zhulang on 2017/9/12.
 * 2.0版 关于小行家
 */

public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.title_centertextview)
    TextView title;
    @BindView(R.id.title_leftimageview)
    ImageView title_leftimageview;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_curren_version)
    TextView tvCurrenVersion;
    @BindView(R.id.ll_new_function)
    LinearLayout llNewFunction;
    @BindView(R.id.ll_go_estimate)
    LinearLayout llGoEstimate;
    @BindView(R.id.ll_goto_zm)
    LinearLayout llGotoZm;
    @BindView(R.id.ll_company_qualification)
    LinearLayout llCompanyQualification;
    @BindView(R.id.tv_telPhone)
    TextView tv_telPhone;

    public static String localVersion;// 本地安装版本
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initParams() {
        title.setText("关于小行家");
        tvVersion.setText("版本 "+ LocalApplication.localVersion);
        tvCurrenVersion.setText("当前版本 "+LocalApplication.localVersion);
        tvCurrenVersion.setText(" "+LocalApplication.localVersion);

        getPlatFormInfo();

    }

    @OnClick({R.id.title_leftimageview, R.id.ll_goto_zm, R.id.ll_company_qualification})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.ll_goto_zm:
                startActivity(new Intent(AboutUsActivity.this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.GONGSI + "?app=true")
                        .putExtra("TITLE", "走进小行家").putExtra("noWebChrome", "aboutMe"));
                break;
            case R.id.ll_company_qualification:
                startActivity(new Intent(AboutUsActivity.this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.GSZZ + "?app=true")
                        .putExtra("TITLE", "公司资质")
                        .putExtra("noWebChrome", "aboutMe"));
                break;
        }
    }

    private void getPlatFormInfo(){
        OkHttpUtils
                .post()
                .url(UrlConfig.GETPLATFORMINFO)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("---》客服电话"+response);
                        JSONObject obj = JSON.parseObject(response);
                        if(obj.getBoolean("success")){
                            JSONObject map = obj.getJSONObject("map");
                            String telPhone = map.getString("platForm");
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("telPhone",telPhone);
                            editor.commit();
                            if(!TextUtils.isEmpty(telPhone)){
                                tv_telPhone.setText("客服电话："+telPhone);
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }
}
