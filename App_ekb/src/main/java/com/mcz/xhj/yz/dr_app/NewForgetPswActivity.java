package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zhulang on 2017/8/6.
 * 描述：2.0版的找回密码界面(包括交易密码和登录密码)
 */

public class NewForgetPswActivity extends BaseActivity {
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.et_phonenumber)
    EditText etPhonenumber;
    @BindView(R.id.et_yzm)
    EditText etYzm;
    @BindView(R.id.tv_getyzm)
    TextView tv_getyzm;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;

    Pattern pPhone = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
    Pattern pCode = Pattern.compile("^[0-9]{4}$");
    private SharedPreferences preferences;
    private String uid;
    private int isFrom;//忘记 0-登录密码，1-交易密码

    @Override
    protected int getLayoutId() {
        return R.layout.new_forgetpsw_activity;
    }

    @Override
    protected void initParams() {
        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");
        isFrom = getIntent().getIntExtra("isFrom", -1);
        if (isFrom == 0) {
            titleCentertextview.setText("找回登录密码");
        } else {
            titleCentertextview.setText("找回交易密码");
        }

    }

    @OnClick({R.id.title_leftimageview, R.id.tv_getyzm, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.tv_getyzm:
                if (stringCut.space_Cut(etPhonenumber.getText().toString().trim()).length() < 11) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    stopTimer();
                    return;
                }
                existMobilePhoneS();

                break;
            case R.id.tv_next:
                if (stringCut.space_Cut(etPhonenumber.getText().toString().trim())
                        .length() < 11) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    return;
                }
                Matcher mPhone = pPhone.matcher(stringCut.space_Cut(etPhonenumber.getText().toString().trim()));
                Matcher mCode = pCode.matcher(stringCut.space_Cut(etYzm.getText().toString().trim()));
                if (!mPhone.matches()) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                } else if (!mCode.matches()) {
                    ToastMaker.showShortToast("请输入正确的短信验证码");
                } else {
                    //验证短信验证码是否正确
                    if (isFrom == 0) {
                        checkLoginSmsCode();
                    } else {
                        //TODO
                        checkLoginSmsCode();
                    }
                }
                break;
        }
    }

    private void existMobilePhoneS() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.EXISTMOBILEPHONE)
                .addParam("mobilephone", stringCut.space_Cut(etPhonenumber.getText().toString().trim()))
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                LogUtils.i("--->忘记密码existMobilePhone：" + response);
                dismissDialog();
                JSONObject obj = JSON.parseObject(response);
                String errorCode = obj.getString("errorCode");
                if (obj.getBoolean(("success"))) {
                    if (obj.getJSONObject("map").getBoolean("exists")) {
                        //ToastMaker.showShortToast("此号码已注册");
                        if (isFrom == 0) {
                            sendForgetLoginPswSmsCode();
                        } else {
                            //TODO发送忘记交易密码验证码
                            sendForgetLoginPswSmsCode();
                        }
                    } else {
                        stopTimer();
                        ToastMaker.showShortToast("此号码未注册");
                    }
                } else if ("1111".equals(obj.getString("errorCode"))) {
                    stopTimer();
                    ToastMaker.showShortToast("手机号码被锁，请联系客服");
                } else {
                    ToastMaker.showShortToast("系统错误");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                // TODO Auto-generated method stub
                dismissDialog();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }
    /**
     *  判断短信验证码是否正确
     * */
    private void checkLoginSmsCode() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.CHECKSMSCODE)
                .addParam("mobilephone", stringCut.space_Cut(etPhonenumber.getText().toString().trim()))
                .addParam("smsCode", stringCut.space_Cut(etYzm.getText().toString().trim()))
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {
                LogUtils.i("--->找回密码checkSmsCode：" + response);
                dismissDialog();
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean(("success"))) {
                    if (isFrom == 0) {
                        startActivity(new Intent(NewForgetPswActivity.this, SetLoginPswActivity.class)
                                .putExtra("smsCode", stringCut.space_Cut(etYzm.getText().toString().trim()))
                                .putExtra("phone", stringCut.space_Cut(etPhonenumber.getText().toString().trim())));
                        finish();
                    } else { //交易密码
                        startActivity(new Intent(NewForgetPswActivity.this, SetTradePswActivity.class)
                                .putExtra("smsCode", stringCut.space_Cut(etYzm.getText().toString().trim()))
                                .putExtra("phone", stringCut.space_Cut(etPhonenumber.getText().toString().trim())));
                        finish();
                    }

                } else if ("1001".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("验证码不正确");
                } else if ("9999".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("当前用户不存在");
                } else {
                    ToastMaker.showShortToast("系统错误");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ToastMaker.showShortToast(" 请检查网络");
            }
        });
    }

    private void sendForgetLoginPswSmsCode() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.FORGETPWDSMSCODE)
                .addParam("mobilephone", stringCut.space_Cut(etPhonenumber.getText().toString().trim()))
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                LogUtils.i("--->找回密码sendForgetPswSmsCode：" + response);
                dismissDialog();
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean(("success"))) {
                    ToastMaker.showShortToast("验证码已发送");
                    time();
                } else if ("1001".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("用户不存在");
                    stopTimer();
                } else if ("1002".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("1分钟内请勿重复发送短信");
                    stopTimer();
                } else if ("1111".equals(obj.getString("errorCode"))) {
                    stopTimer();
                    ToastMaker.showShortToast("手机号码被锁，请联系客服");
                } else if ("9999".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("系统错误");
                    stopTimer();
                } else {
                    ToastMaker.showShortToast("系统错误");
                    stopTimer();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                // TODO Auto-generated method stub
                dismissDialog();
                ToastMaker.showShortToast(" 请检查网络");
            }
        });
    }

    @Override
    protected synchronized void onDestroy() {
        super.onDestroy();
    }

    private int count;
    private Boolean isRun;
    private int time = 1;

    // 计时器
    public void time() {
        count = 60;
        isRun = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRun) {
                    count--;
                    handler.sendEmptyMessage(10);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    public void stopTimer() {
        tv_getyzm.setEnabled(true);
        tv_getyzm.setText("获取验证码");
        tv_getyzm.setBackgroundResource(R.drawable.bg_corner_kong_blue);
        tv_getyzm.setTextColor(0xFF20A3F9);
        isRun = false;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    if (count <= 0) {
                        time = 1;
                        stopTimer();
                    } else {
                        tv_getyzm.setEnabled(false);
                        tv_getyzm.setTextColor(0xffcccccc);
                        tv_getyzm.setBackgroundResource(R.drawable.bg_corner_blackline);
                        tv_getyzm.setText("发送(" + count + ")秒");
                    }

                    break;
                default:
                    break;
            }
        }
    };
}
