package com.ekabao.oil.ui.activity.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.SecurityUtils;
import com.ekabao.oil.util.show_Dialog_IsLogin;


import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zhulang on 2017/8/6.
 * 描述：设置交易密码
 */

public class SetTradePswActivity extends BaseActivity {
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.et_psw_again)
    EditText etPswAgain;
    @BindView(R.id.tv_submit_psw)
    TextView tvSubmitPsw;
    private String mobilephone;
    private String smsCode;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_trade_psw;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("设置交易密码");
        Intent intent = getIntent();
        mobilephone = intent.getStringExtra("phone");
        smsCode = intent.getStringExtra("smsCode");
    }

    @OnClick({R.id.title_leftimageview, R.id.tv_submit_psw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;

            case R.id.tv_submit_psw:
                if (TextUtils.isEmpty(etPsw.getText().toString().trim())) {
                    ToastMaker.showShortToast("请输入交易密码");
                } else if (!etPsw.getText().toString().equalsIgnoreCase(etPswAgain.getText().toString())) {
                    ToastMaker.showShortToast("密码和确认密码不相同");
                } else {

                    LogUtils.e("tpwdFlag"+preferences.getString("tpwdFlag", ""));
                    LogUtils.e("设置过交易密码"+"1".equals(preferences.getString("tpwdFlag", "")));
                    if ("1".equals(preferences.getString("tpwdFlag", ""))){//1代表设置过交易密码
                        //(忘记)设置交易密码
                        updateTradePassword();
                    }else{
                        //第一次设置交易密码
                        sendFirstTpwdCode();
                    }
                }
                break;
        }
    }

    private void updateTradePassword() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.FORGETPASSWORD)
                .addParams("valicateCode", smsCode)
                .addParams("pwdnew", SecurityUtils.MD5AndSHA256(etPsw.getText().toString().trim()))
                .addParams("confirmPwd", SecurityUtils.MD5AndSHA256(etPswAgain.getText().toString().trim()))
                .addParam("mobilephone",mobilephone)
                .addParam("type",2+"")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("--->(忘记密码)设置交易密码："+response);

                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("交易密码设置成功");
                            finish();
                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("验证码错误");
                        } else if ("1002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("密码为空");
                        } else if ("1003".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("交易密码不合法");
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
                            new show_Dialog_IsLogin(SetTradePswActivity.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统错误");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    private void sendFirstTpwdCode() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.UPDATETPWDBYSMS)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("tpwd", SecurityUtils.MD5AndSHA256(etPsw.getText().toString().trim()))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        setResult(5, new Intent());
                        LogUtils.i("--->设置交易密码：" + response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("交易密码设置成功");
                            finish();
                        }  else if ("1002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("密码为空");
                        } else if ("1003".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("交易密码不合法");
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
                            new show_Dialog_IsLogin(SetTradePswActivity.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统错误");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }
}
