package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zhulang on 2017/8/3.
 * 描述：2.0版的登录界面
 */

public class NewLoginActivity extends BaseActivity {

    @BindView(R.id.img_close_login)
    ImageView imgCloseLogin;
    @BindView(R.id.tv_title_login)
    TextView tvTitleLogin;
    @BindView(R.id.ll_title_login)
    RelativeLayout llTitleLogin;
    @BindView(R.id.tv_day_login)
    TextView tvDayLogin;
    @BindView(R.id.tv_week_login)
    TextView tvWeekLogin;
    @BindView(R.id.ll_welcome)
    LinearLayout llWelcome;
    @BindView(R.id.et_phonenumber)
    EditText etPhonenumber;
    @BindView(R.id.et_login_psw)
    EditText etLoginPsw;
    @BindView(R.id.img_eye)
    CheckBox imgEye;
    @BindView(R.id.rl_psw_login)
    RelativeLayout rlPswLogin;
    @BindView(R.id.et_login_yzm)
    EditText etLoginYzm;
    @BindView(R.id.tv_getyzm)
    TextView tv_getyzm;
    @BindView(R.id.rl_yzm_login)
    RelativeLayout rlYzmLogin;
    @BindView(R.id.tv_forget_psw)
    TextView tvForgetPsw;
    @BindView(R.id.rl_forget_psw)
    RelativeLayout rlForgetPsw;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_way_login)
    TextView tvWayLogin;
    @BindView(R.id.tv_user_protocol)
    TextView tvUserProtocol;
    @BindView(R.id.tv_reg)
    TextView tvReg;

    Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
    Pattern psw = Pattern.compile("^[a-zA-Z0-9]{6,18}$");
    Pattern pcode = Pattern.compile("^[0-9]{4}$");
    private boolean flag = false;
    private boolean isSendSms = false;
    private boolean isPswLogin = true;//默认是密码登录(true密码登录，false短信登录)
    private SharedPreferences preferences;
    private String type_login = "1";//登录类型(密码登录传1验证码登录传2)  必填
    private int isFrom = 0;//修改 0-登录密码，1-交易密码
    private long timeInMillis;

    @Override
    protected int getLayoutId() {
        return R.layout.act_newloginpsw;
    }

    @Override
    protected void initParams() {
        tvTitleLogin.setText("登录");
        //获取当前的日期和星期
        Calendar calendar = Calendar.getInstance();
        timeInMillis = calendar.getTimeInMillis();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        String weekDay = transformToWeek(week);
        System.out.println("hyjr：日: " + day + " ,星期：" + weekDay);
        tvDayLogin.setText(day + "");
        tvWeekLogin.setText(weekDay);


        etLoginPsw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“GO”键
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etLoginPsw.getWindowToken(), 0);
                    goLogin();
                    return true;
                }
                return false;
            }
        });

    }

    private String transformToWeek(int week) {
        String weedDay = "";
        if (week == 1) {
            weedDay = "星期日";
        } else if (week == 2) {
            weedDay = "星期一";
        } else if (week == 3) {
            weedDay = "星期二";
        } else if (week == 4) {
            weedDay = "星期三";
        } else if (week == 5) {
            weedDay = "星期四";
        } else if (week == 6) {
            weedDay = "星期五";
        } else if (week == 7) {
            weedDay = "星期六";
        }

        return weedDay;
    }

    @OnClick({R.id.img_close_login, R.id.img_eye, R.id.tv_getyzm, R.id.tv_forget_psw, R.id.tv_login, R.id.tv_way_login, R.id.tv_user_protocol, R.id.tv_reg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close_login:
                finish();
                break;
            case R.id.img_eye:
                if (imgEye.isChecked()) {
                    etLoginPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etLoginPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.tv_getyzm://发送登录验证码
                if (stringCut.space_Cut(etPhonenumber.getText().toString().trim()).length() < 11) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    stopTimer();
                    return;
                }
                Matcher m2 = p.matcher(stringCut.space_Cut(etPhonenumber.getText().toString().trim()));
                if (!m2.matches()) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    stopTimer();
                    return;
                }
                isSendSms = true;
                existMobilePhone();

                break;
            case R.id.tv_forget_psw:
                startActivity(new Intent(NewLoginActivity.this, NewForgetPswActivity.class).putExtra("isFrom", isFrom));
                break;
            case R.id.tv_login:

                goLogin();
                break;
            case R.id.tv_way_login://密码登录和验证码登录 方式切换
                isPswLogin = !isPswLogin;
                if (isPswLogin) {//账号密码登录
                    rlYzmLogin.setVisibility(View.GONE);
                    rlPswLogin.setVisibility(View.VISIBLE);
                    rlForgetPsw.setVisibility(View.VISIBLE);
                    tvWayLogin.setText("手机验证码登录");
                    type_login = "1";
                    isSendSms = false;
                } else {
                    rlYzmLogin.setVisibility(View.VISIBLE);
                    rlPswLogin.setVisibility(View.GONE);
                    rlForgetPsw.setVisibility(View.INVISIBLE);
                    tvWayLogin.setText("账号密码登录");
                    type_login = "2";
                }
                break;
            case R.id.tv_user_protocol:
                startActivity(new Intent(NewLoginActivity.this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.ZHUCE)
                        .putExtra("TITLE", "注册协议"));
                break;
            case R.id.tv_reg:
                startActivityForResult(new Intent(NewLoginActivity.this, NewRegisterActivity.class), 3);
                etLoginPsw.setText("");
                finish();
                break;
        }
    }

    private void goLogin() {
        MobclickAgent.onEvent(NewLoginActivity.this, UrlConfig.point + 7 + "");
        if (stringCut.space_Cut(etPhonenumber.getText().toString().trim())
                .length() < 11) {
            ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
            return;
        }
        Matcher m1 = p.matcher(stringCut.space_Cut(etPhonenumber.getText().toString().trim()));
        if (!m1.matches()) {
            ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
            return;
        }
        if (isPswLogin) {//1.密码登录验证
            Matcher mPsw = psw.matcher(etLoginPsw.getText().toString().trim());
            if (!flag) {
                if (!mPsw.matches()) {
                    ToastMaker.showLongToast("请输入规则的密码");
                    return;
                }
            }
            existMobilePhone();
        } else {//2.验证码登录验证
            Matcher mCode = pcode.matcher(etLoginYzm.getText().toString().toString());
            if (!mCode.matches()) {
                ToastMaker.showShortToast("请输入正确的验证码");
                return;
            }
            doLogin();
        }
    }

    //发送登录验证码
    private void sendLoginMsg() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.LOGINMSGSEND)
                .addParam("mobilephone", stringCut.space_Cut(etPhonenumber.getText().toString().trim()))
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                LogUtils.i("--->登录sendLoginMsg：" + response);
                dismissDialog();
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean(("success"))) {
                    ToastMaker.showShortToast("验证码已发送");
                    time();
                } else if ("1003".equals(obj.getString("errorCode"))) {
                    stopTimer();
                    ToastMaker.showShortToast("1分钟内请勿重复发送验证码");
                } else if ("1111".equals(obj.getString("errorCode"))) {
                    stopTimer();
                    ToastMaker.showShortToast("手机号码被锁，请联系客服");
                } else if ("9999".equals(obj.getString("errorCode"))) {
                    stopTimer();
                    ToastMaker.showShortToast("短信发送失败");
                } else {
                    stopTimer();
                    ToastMaker.showShortToast("系统错误");
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

    /*检验手机号是否存在*/
    private void existMobilePhone() {
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
                LogUtils.i("--->登录existMobilePhone：" + response);
                dismissDialog();
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean(("success"))) {
                    String errorCode = obj.getString("errorCode");
                    if (obj.getJSONObject("map").getBoolean("exists")) {
                        if (isSendSms) {
                            sendLoginMsg();//去发送验证码

                        } else if (!flag && !isSendSms) {
                            doLogin();//去密码登录

                        } else {
                            ToastMaker.showLongToast("此号码已注册");
                            stopTimer();
                        }
                    } else {
                        if (!flag) {
                            ToastMaker.showLongToast("此号码未注册");
                            stopTimer();
                        } else {

                        }
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

    Integer loginErrorNums = 0;

    private void doLogin() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.DOLOGIN)
                .addParam("mobilephone", stringCut.space_Cut(etPhonenumber.getText().toString()))
                .addParam("passWord", SecurityUtils.MD5AndSHA256(etLoginPsw.getText().toString().trim()))
                .addParam("smsCode", stringCut.space_Cut(etLoginYzm.getText().toString()))
                .addParam("type", type_login)
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        LogUtils.i("--->登录doLogin：" + response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            etLoginPsw.setText("");
                            JSONObject jsonObj = obj.getJSONObject("map");
                            String token = jsonObj.getString("token");
                            JSONObject objmem = jsonObj.getJSONObject("member");
                            String mobilephone = objmem.getString("mobilephone");
                            String realVerify = objmem.getString("realVerify");
                            String uid = objmem.getString("uid");
                            String recommCodes = objmem.getString("recommCodes");
                            String avatar_url = objmem.getString("photo");

                            preferences = LocalApplication.getInstance().sharereferences;
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("login", true);
                            editor.putString("uid", uid);
                            editor.putString("recommCodes", recommCodes);
                            editor.putString("realVerify", realVerify);
                            editor.putString("phone", mobilephone);
                            editor.putString("token", token);
                            editor.putString("avatar_url", avatar_url);
                            editor.commit();
                            pushRegisterId();
                            LocalApplication.getInstance().getMainActivity().isExit = false;
                            showAlertDialog("登录成功", "是否立即设置手势密码", new String[]{"暂不设置", "立即设置"}, true, true, "");

                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("账号或密码为空 ");
                        } else if ("1003".equals(obj.getString("errorCode"))) {
                            JSONObject jsonObj = obj.getJSONObject("map");
                            ToastMaker.showShortToast("账号或密码错误");
                            /*if (jsonObj.getInteger("loginErrorNums") != null) {
                                loginErrorNums = jsonObj.getInteger("loginErrorNums");//密码错误时返回错误次数
                                if (loginErrorNums >= 3) {
                                    getPicCode();
                                }
                            }*/
                        } else if ("1004".equals(obj.getString("errorCode"))) {
                            JSONObject jsonObj = obj.getJSONObject("map");
                            ToastMaker.showShortToast("验证码不能为空");
                            //getPicCode();
                        } else if ("1002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("图形验证码错误");
                        } else if ("1002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("图形验证码错误");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        // requestCode标示请求的标示 resultCode表示有数据
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (LocalApplication.getInstance().getWebViewActivity() != null) {
                LocalApplication.getInstance().getWebViewActivity().finish();
            }
            LogUtils.e("requestCode == 1 && resultCode == RESULT_OK");
            finish();
        } else if (requestCode == 1 && resultCode == 2) {
            LocalApplication.getInstance().getMainActivity().isHomeChecked = true;
            LocalApplication.getInstance().getMainActivity().isHome = true;
            if (LocalApplication.getInstance().getWebViewActivity() != null) {
                LocalApplication.getInstance().getWebViewActivity().finish();
            }
            setResult(3, new Intent());
            LogUtils.e("requestCode == 1 && resultCode == 2");
            finish();
        } else if (requestCode == 3 && resultCode == 3) {
            setResult(3);
            LogUtils.e("requestCode == 3 && resultCode == 3");
            finish();
        } else if (requestCode == 3 && resultCode == 2) {
            finish();
            LogUtils.e("requestCode == 3 && resultCode == 2");
        }

    }

    @Override
    public void onButtonClicked(Dialog dialog, int position, Object tag) {
        // TODO Auto-generated method stub
        if (position == 0) {
            finish();
            ToastMaker.showShortToast("可以在安全中心-手势密码 中进行修改");
        }
        if (position == 1) {
            startActivity(new Intent(NewLoginActivity.this, GestureEditActivity.class));
            finish();
        }
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
