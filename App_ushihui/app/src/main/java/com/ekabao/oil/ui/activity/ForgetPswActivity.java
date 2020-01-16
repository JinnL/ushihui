package com.ekabao.oil.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 易卡宝  App
 * 忘记密码 发送短信
 *
 * @time 2018/7/27 17:30
 * Created by lj on 2018/7/27 17:30.
 */

public class ForgetPswActivity extends BaseActivity {


    Pattern pPhone = Pattern.compile(LocalApplication.context.getResources().getString(R.string.pPhone));
    Pattern pCode = Pattern.compile("^[0-9]{4}$");
    @BindView(R.id.title_lefttextview)
    TextView titleLefttextview;
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.tv_find_2)
    TextView tvFind2;
    @BindView(R.id.tv_find_1)
    TextView tvFind1;
    @BindView(R.id.view_line_bottom)
    View viewLineBottom;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.et_phonenumber)
    EditText etPhonenumber;
    @BindView(R.id.et_yzm)
    EditText etYzm;
    @BindView(R.id.tv_getyzm)
    TextView tvGetyzm;
    @BindView(R.id.tv_next)
    TextView tvNext;
    private SharedPreferences preferences;
    private String uid;
    private int isFrom;//忘记 0-登录密码，1-交易密码

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgetpsw;
    }

    @Override
    protected void initParams() {


        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");
        isFrom = getIntent().getIntExtra("isFrom", -1);

      /*  if (isFrom == 0) {
            titleCentertextview.setText("找回登录密码");
        } else {
            titleCentertextview.setText("找回交易密码");
        }*/

    }

    @OnClick({R.id.title_leftimageview, R.id.tv_getyzm, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.tv_getyzm:
                if (StringCut.space_Cut(etPhonenumber.getText().toString().trim()).length() < 11) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    stopTimer();
                    return;
                }
                existMobilePhoneS();

                break;
            case R.id.tv_next:
                if (StringCut.space_Cut(etPhonenumber.getText().toString().trim())
                        .length() < 11) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    return;
                }
                Matcher mPhone = pPhone.matcher(StringCut.space_Cut(etPhonenumber.getText().toString().trim()));
                Matcher mCode = pCode.matcher(StringCut.space_Cut(etYzm.getText().toString().trim()));
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
                .addParam("mobilephone", StringCut.space_Cut(etPhonenumber.getText().toString().trim()))
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {

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

                dismissDialog();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

    /**
     * 判断短信验证码是否正确
     */
    private void checkLoginSmsCode() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.CHECKSMSCODE)
                .addParam("mobilephone", StringCut.space_Cut(etPhonenumber.getText().toString().trim()))
                .addParam("smsCode", StringCut.space_Cut(etYzm.getText().toString().trim()))
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {
                LogUtils.i("--->找回密码checkSmsCode：" + response);
                dismissDialog();
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean(("success"))) {
                    if (isFrom == 0) {
                        startActivity(new Intent(ForgetPswActivity.this, SetPswActivity.class)
                                .putExtra("smsCode", StringCut.space_Cut(etYzm.getText().toString().trim()))
                                .putExtra("phone", StringCut.space_Cut(etPhonenumber.getText().toString().trim())));
                        finish();
                    } else { //交易密码
                        startActivity(new Intent(ForgetPswActivity.this, SetPswActivity.class)
                                .putExtra("smsCode", StringCut.space_Cut(etYzm.getText().toString().trim()))
                                .putExtra("phone", StringCut.space_Cut(etPhonenumber.getText().toString().trim())));
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
                .addParam("mobilephone", StringCut.space_Cut(etPhonenumber.getText().toString().trim()))
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {

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
        tvGetyzm.setEnabled(true);
        tvGetyzm.setText("获取验证码");
        //tvGetyzm.setBackgroundResource(R.drawable.bg_corner_kong_blue);
        // tvGetyzm.setTextColor(0xFF20A3F9);
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
                        tvGetyzm.setEnabled(false);
                        // tvGetyzm.setTextColor(0xffcccccc);
                        //tvGetyzm.setBackgroundResource(R.drawable.bg_corner_blackline);
                        tvGetyzm.setText("发送(" + count + ")秒");
                    }

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
