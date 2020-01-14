package com.ekabao.oil.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
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
import com.ekabao.oil.R;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.SecurityUtils;
import com.ekabao.oil.util.StringCut;
import com.umeng.analytics.MobclickAgent;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.Call;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.img_close_login)
    ImageView imgCloseLogin;
    /* @BindView(R.id.tv_title_login)
     TextView tvTitleLogin;*/
    @BindView(R.id.ll_title_login)
    RelativeLayout llTitleLogin;
    @BindView(R.id.tv_day_login)
    TextView tvDayLogin;
    @BindView(R.id.tv_week_login)
    TextView tvWeekLogin;
    @BindView(R.id.ll_welcome)
    LinearLayout llWelcome;
    @BindView(R.id.cl_top)
    ConstraintLayout clTop;
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
    TextView tvGetyzm;
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
    @BindView(R.id.cb_user_protocol)
    CheckBox cbUserProtocol;
    @BindView(R.id.tv_user_protocol)
    TextView tvUserProtocol;
    @BindView(R.id.tv_reg)
    TextView tvReg;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    String regExp = LocalApplication.context.getResources().getString(R.string.pPhone);
    //Pattern p = Pattern.compile(LocalApplication.context.getResources().getString(R.string.pPhone));
    Pattern p = Pattern.compile(regExp);
    Pattern psw = Pattern.compile("^[a-zA-Z0-9]{6,18}$");
    Pattern pcode = Pattern.compile("^[0-9]{4}$");

    private boolean flag = false;
    private boolean isSendSms = false;
    private boolean isPswLogin = true;//默认是密码登录(true密码登录，false短信登录)
    private SharedPreferences preferences;
    private String type_login = "1";//登录类型(密码登录传1验证码登录传2)  必填
    private int isFrom = 0;//修改 0-登录密码，1-交易密码
    private long timeInMillis;
    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initParams() {

        //tvTitleLogin.setText("登录");
        //获取当前的日期和星期
        Calendar calendar = Calendar.getInstance();
        timeInMillis = calendar.getTimeInMillis();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        String weekDay = transformToWeek(week);
        System.out.println("hyjr：日: " + day + " ,星期：" + weekDay);
        tvDayLogin.setText(day + "");
        tvWeekLogin.setText(weekDay);
        tvUserProtocol.setText(Html.fromHtml("阅读并同意<font color='#FF6571'>《用户协议和隐私声明》</font>"));
        // <font color='#155CFC' border-bottom: 1px solid red; >
//        tvForgetPsw.setText(Html.fromHtml("忘记密码?<font color='#155CFC'><u>点此找回</u></font>"));


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

    @OnCheckedChanged(R.id.cb_user_protocol)
    void cbUserProtocolChanged(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @OnClick({R.id.img_close_login, R.id.img_eye, R.id.tv_getyzm,
            R.id.tv_forget_psw, R.id.tv_login, R.id.tv_way_login,
            R.id.tv_user_protocol, R.id.tv_reg})
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
                if (StringCut.space_Cut(etPhonenumber.getText().toString().trim()).length() < 11) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    stopTimer();
                    return;
                }
                Matcher m2 = p.matcher(StringCut.space_Cut(etPhonenumber.getText().toString().trim()));
                if (!m2.matches()) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    stopTimer();
                    return;
                }
                isSendSms = true;
                existMobilePhone();

                break;
            case R.id.tv_forget_psw: //忘记密码

                startActivity(new Intent(LoginActivity.this,
                        ForgetPswActivity.class).putExtra("isFrom", isFrom));

                break;
            case R.id.tv_login: //登录
                if (isChecked) {
                    goLogin();
                } else {
    ToastMaker.showShortToast("请先同意《用户协议和隐私声明》");
                }
                break;
            case R.id.tv_way_login://密码登录和验证码登录 方式切换
                isPswLogin = !isPswLogin;
                if (isPswLogin) {//账号密码登录
                    rlYzmLogin.setVisibility(View.GONE);
                    rlPswLogin.setVisibility(View.VISIBLE);

                    //rlForgetPsw.setVisibility(View.VISIBLE);

                    type_login = "1";
                    isSendSms = false;

                    tvTitle.setText("欢迎回到油实惠-密码登录");
                    tvWayLogin.setText("验证码登录");

                } else {
                    rlYzmLogin.setVisibility(View.VISIBLE);
                    rlPswLogin.setVisibility(View.GONE);

                    // rlForgetPsw.setVisibility(View.INVISIBLE);

                    tvTitle.setText("欢迎回到油实惠-验证码登录");
                    tvWayLogin.setText("密码登录");
                    type_login = "2";
                }
                break;
            case R.id.tv_user_protocol:
//                startActivity(new Intent(LoginActivity.this, WebViewActivity.class)
//                        .putExtra("URL", UrlConfig.ZHUCE)
//                        .putExtra("TITLE", "注册协议"));
                startActivity(new Intent(LoginActivity.this, ProtocolActivity.class));
                break;
            case R.id.tv_reg: //注册
                startActivityForResult(new Intent(LoginActivity.this,
                        RegisterActivity.class), 3);
                etLoginPsw.setText("");
                // finish();
                break;
        }
    }

    private void goLogin() {
        MobclickAgent.onEvent(LoginActivity.this, UrlConfig.point + 7 + "");
        String phone = etPhonenumber.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            //Toast.makeText(this,"请输入规则的密码",Toast.LENGTH_SHORT).show();
            ToastMaker.showShortToast(getResources().getString(R.string.phone_empty));
            return;
        } else if (StringCut.space_Cut(phone).length() < 11) {
            ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
            return;
        }
        Matcher m1 = p.matcher(StringCut.space_Cut(phone));
        if (!m1.matches()) {
            ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
            return;
        }
        if (isPswLogin) {//1.密码登录验证

            String pwd = etLoginPsw.getText().toString().trim();
            if (TextUtils.isEmpty(pwd)) {
                ToastMaker.showShortToast(getResources().getString(R.string.pwd_empty));
                return;
            }
            Matcher mPsw = psw.matcher(pwd);
            if (!flag) {
                if (!mPsw.matches()) {
                    ToastMaker.showLongToast(getResources().getString(R.string.pwd_Wrong));
                    return;
                }
            }
            existMobilePhone();
        } else {//2.验证码登录验证
            String yzm = etLoginYzm.getText().toString().toString();
            if (TextUtils.isEmpty(yzm)) {
                ToastMaker.showShortToast(getResources().getString(R.string.code_empty));
                return;
            }
            Matcher mCode = pcode.matcher(yzm);
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
                .addParam("mobilephone", StringCut.space_Cut(etPhonenumber.getText().toString().trim()))
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {

                LogUtils.e("--->登录sendLoginMsg：" + response);
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
                .addParam("mobilephone", StringCut.space_Cut(etPhonenumber.getText().toString().trim()))
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {

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
                .addParam("mobilephone", StringCut.space_Cut(etPhonenumber.getText().toString()))
                .addParam("passWord", SecurityUtils.MD5AndSHA256(etLoginPsw.getText().toString().trim()))
                .addParam("smsCode", StringCut.space_Cut(etLoginYzm.getText().toString()))
                .addParam("type", type_login)
                .addParam("version", UrlConfig.version)
                .addParam("channel", "3").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {

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
                            // TODO: 2018/6/27 手势密码先关闭
                           /* showAlertDialog("登录成功", "是否立即设置手势密码",
                                    new String[]{"暂不设置", "立即设置"},
                                    true,
                                    true, "");*/
                            setResult(RESULT_OK);
                            finish();

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

                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e(requestCode + "登录1" + resultCode);
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
        } else if (requestCode == 3 && resultCode == 3) {  //注册
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

        if (position == 0) {
            finish();
            ToastMaker.showShortToast("可以在安全中心-手势密码 中进行修改");
        }
        if (position == 1) {
            startActivity(new Intent(LoginActivity.this, GestureEditActivity.class));
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
        tvGetyzm.setText("重发验证码");
        // tvGetyzm.setBackgroundResource(R.drawable.shape_rectangle_gold);
        tvGetyzm.setTextColor(getResources().getColor(R.color.text_red));
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
                        tvGetyzm.setTextColor(getResources().getColor(R.color.text_red));
                        //tvGetyzm.setBackgroundResource(R.drawable.shape_rectangle_gold);
                        //tvGetyzm.setText("发送(" + count + ")秒");
                        tvGetyzm.setText(count + "秒  ");
                    }

                    break;
                default:
                    break;
            }
        }
    };

}
