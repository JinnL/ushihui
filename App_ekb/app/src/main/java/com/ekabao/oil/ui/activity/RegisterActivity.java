package com.ekabao.oil.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
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
import com.ekabao.oil.util.SecurityUtils;
import com.ekabao.oil.util.StringCut;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.img_close_register)
    ImageView imgCloseRegister;
    @BindView(R.id.tv_toLogin)
    TextView tvToLogin;
    @BindView(R.id.ll_title_register)
    RelativeLayout llTitleRegister;
    @BindView(R.id.et_phonenumber)
    EditText etPhonenumber;
    @BindView(R.id.et_login_psw)
    EditText etLoginPsw;
    @BindView(R.id.img_eye)
    CheckBox imgEye;
    @BindView(R.id.rl_psw_login)
    RelativeLayout rlPswLogin;
    @BindView(R.id.et_register_yzm)
    EditText etRegisterYzm;
    @BindView(R.id.tv_getyzm)
    TextView tvGetyzm;
    @BindView(R.id.rl_yzm_register)
    RelativeLayout rlYzmRegister;
    @BindView(R.id.et_inviteCode)
    EditText etInviteCode;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.cb_user_protocol)
    CheckBox cbUserProtocol;
    @BindView(R.id.tv_user_protocol)
    TextView tvUserProtocol;
    @BindView(R.id.tv_inviteCode)
    TextView tvInviteCode;

    private boolean isInviteCode = true; //是否邀请码
    Pattern pPhone = Pattern.compile(LocalApplication.context.getResources().getString(R.string.pPhone));
    Pattern pCode = Pattern.compile("^[0-9]{4}$");
    Pattern psw = Pattern.compile("^[a-zA-Z0-9]{6,18}$");
    private SharedPreferences preferences;
    private boolean isSendSms = false;
    private String timee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initParams() {

        tvUserProtocol.setText(Html.fromHtml("已阅读并同意<font color='#FF6571'>《用户协议和隐私声明》</font>"));

    }

//    @OnCheckedChanged(R.id.cb_user_protocol)
//    void cbUserProtocolChanged(boolean changed){
//
//    }

    @OnClick({R.id.img_close_register, R.id.tv_getyzm,
            R.id.tv_register, R.id.tv_inviteCode, R.id.img_eye,
            R.id.tv_user_protocol, R.id.tv_toLogin})
    public void onViewClicked(View view) {
        String phone = etPhonenumber.getText().toString().trim();

        switch (view.getId()) {
            case R.id.img_close_register:
                finish();
                break;
            case R.id.tv_getyzm: //验证码

                phone = StringCut.space_Cut(phone);
                if (TextUtils.isEmpty(phone)) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_empty));
                    return;
                } else if (phone.length() < 11 || !pPhone.matcher(phone).matches()) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    stopTimer();
                    return;
                }
                isSendSms = true;
                existMobilePhoneS();

                break;
            case R.id.img_eye:
                if (imgEye.isChecked()) {
                    etLoginPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etLoginPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.tv_register:
                MobclickAgent.onEvent(RegisterActivity.this, UrlConfig.point + 2 + "");

                String pwd = etLoginPsw.getText().toString().trim();
                String yzm = etRegisterYzm.getText().toString().trim();

                if (TextUtils.isEmpty(phone)) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_empty));
                    return;
                } else if (StringCut.space_Cut(phone).length() < 11) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    ToastMaker.showShortToast(getResources().getString(R.string.pwd_empty));
                    return;
                }
                if (TextUtils.isEmpty(yzm)) {
                    ToastMaker.showShortToast(getResources().getString(R.string.code_empty));
                    return;
                }

                Matcher m1 = pPhone.matcher(StringCut.space_Cut(phone));

                Matcher mCode = pCode.matcher(yzm);

                String trim = etInviteCode.getText().toString().trim();

                Matcher mInvite = psw.matcher(StringCut.space_Cut(trim));
                if (!m1.matches()) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    return;
                } else if (!StringCut.isPsw(pwd)) {
                    ToastMaker.showShortToast("请输入规则的登录密码");
                    return;
                } else if (!mCode.matches()) {
                    ToastMaker.showShortToast("请输入正确的短信验证码");
                    return;
                }
                LogUtils.e("--->注册 是否邀请码：isInviteCode：" + isInviteCode); //没用了

                if (cbUserProtocol.isChecked()) {
                    if (!TextUtils.isEmpty(trim)) {  //填写邀请码
                        if (!mInvite.matches()) {
                            ToastMaker.showShortToast("请输入正确的邀请码");
                            return;
                        } else {
                            //带邀请码去注册
                            register();
                        }
                    } else {
                        //去注册
                        register();
                    }
                } else {
                    ToastMaker.showShortToast("请先阅读并同意《用户协议和隐私声明》");
                }
                break;
           /* case R.id.tv_inviteCode: //是否显示邀请码
                isInviteCode = !isInviteCode;
                if (isInviteCode) {
                    etInviteCode.setVisibility(View.VISIBLE);
                    //邀请码右侧的箭头朝上
                    Drawable drawable = getResources().getDrawable(R.mipmap.arrow_up_reg);
                    //这一步必须要做,否则不会显示.
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tvInviteCode.setCompoundDrawables(null, null, drawable, null);
                } else {
                    etInviteCode.setVisibility(View.GONE);
                    //邀请码右侧的箭头朝下
                    Drawable drawable = getResources().getDrawable(R.mipmap.arrow_down_reg);
                    //这一步必须要做,否则不会显示.
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tvInviteCode.setCompoundDrawables(null, null, drawable, null);
                }
                break;*/
            case R.id.tv_user_protocol:
//                startActivity(new Intent(RegisterActivity.this, WebViewActivity.class)
//                        .putExtra("URL", UrlConfig.ZHUCE)
//                        .putExtra("TITLE", "注册协议"));
                startActivity(new Intent(RegisterActivity.this, ProtocolActivity.class));
                break;
            case R.id.tv_toLogin:

                finish();
               /* startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                setResult(3);
                finish();*/
                break;
        }
    }

    private void sendRegMsg() {
        showWaitDialog("加载中...", false, "");
        LogUtils.e("--->注册sendRegMsg：");
        OkHttpUtils.post().url(UrlConfig.SENDREGMSGAPP)
                .addParams("mobilephone", StringCut.space_Cut(etPhonenumber.getText().toString()))
                .addParam("type", "1")
                .addParam("time", SecurityUtils.MD5AndSHA256(timee))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("--->注册sendRegMsg：" + response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("验证码已发送");
                            time();
                        } else if ("1002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("每个手机号当天只能发送5条");
                            stopTimer();
                        } else if ("1003".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("提示验证码发送失败");
                            stopTimer();
                        } else if ("1111".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("注册失败次数超限，请联系客服");
                            stopTimer();
                        } else {
                            ToastMaker.showShortToast("系统错误");
                            stopTimer();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {

                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    private void existMobilePhoneS() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.EXISTMOBILEPHONE)
                .addParam("mobilephone", StringCut.space_Cut(etPhonenumber.getText().toString().trim()))
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("--->注册existMobilePhone：" + response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            String errorCode = obj.getString("errorCode");
                            timee = obj.getJSONObject("map").getString("time");
                            if (obj.getJSONObject("map").getBoolean("exists")) {
                                ToastMaker.showShortToast("此号码已注册");
                                stopTimer();
                            } else {
                                if (obj.getJSONObject("map").getString("time") != null) {
                                    if (isSendSms) {
                                        //发验证码
                                        sendRegMsg();
                                    }

                                } else {
                                    finish();
                                }
                            }
                        } else if ("1111".equals(obj.getString("errorCode"))) {
                            stopTimer();
                            ToastMaker.showShortToast("注册失败次数超限，请联系客服");
                        } else {
                            ToastMaker.showShortToast("系统错误");
                            stopTimer();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    private void register() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.REGISTER_REG)
                .addParam("mobilephone", StringCut.space_Cut(etPhonenumber.getText().toString().trim()))
                .addParam("passWord", SecurityUtils.MD5AndSHA256(etLoginPsw.getText().toString().trim()))
                .addParam("smsCode", etRegisterYzm.getText().toString().trim())
                .addParam("recommPhone", etInviteCode.getText().toString().trim())
                .addParam("toFrom", LocalApplication.getInstance().channelName)
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {

                        LogUtils.i("--->注册register：" + response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
//							ToastMaker.showShortToast("注册成功");
                            JSONObject jsonObj = obj.getJSONObject("map");
                            String token = jsonObj.getString("token");
                            JSONObject objmem = jsonObj.getJSONObject("member");
                            String mobilephone = objmem.getString("mobilephone");
                            String realVerify = objmem.getString("realVerify");
                            String uid = objmem.getString("uid");
                            String recommCodes = objmem.getString("recommCodes");
                            preferences = LocalApplication.getInstance().sharereferences;
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("login", true);
                            editor.putString("uid", uid);
                            editor.putString("recommCodes", recommCodes);
                            editor.putString("realVerify", realVerify);
                            editor.putString("phone", mobilephone);
                            editor.putString("token", token);
                            editor.commit();
                            pushRegisterId();
                            LocalApplication.getInstance().getMainActivity().setCheckedFram(1);
                            LocalApplication.getInstance().getMainActivity().isLoginPsw = true;

                            // 登录，请将示例中的userid-safei替换成用户登录的ID。
                            Map loginMap = new HashMap();
                            loginMap.put("userid", uid);
                            MobclickAgent.onEvent(RegisterActivity.this, "__register", loginMap);
                            setResult(3); //注册成功

                           /* startActivityForResult(new Intent(RegisterActivity.this, Register_Success.class)
                                    .putExtra("newId", jsonObj.getString("pid"))
                                    .putExtra("regSendLabel", jsonObj.getString("regSendLabel")), 3
                            );*/
                            finish();
                            //showAlertDialog("注册成功", "是否立即设置手势密码", new String[]{"暂不设置","立即设置"}, false, true, "");
                        } else if ("XTWH".equals(obj.getString("errorCode"))) {
                            startActivity(new Intent(RegisterActivity.this, WebViewActivity.class)
                                    .putExtra("URL", UrlConfig.WEIHU)
                                    .putExtra("TITLE", "系统维护"));
                            return;
                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("短信验证码为空");
                        } else if ("1002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("短信验证码错误");
                        } else if ("1003".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("手机号为空");
                        } else if ("1005".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("密码格式错误");
                        } else if ("1006".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("未勾选注册协议");
                        } else if ("1007".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("手机号已注册");
                        } else if ("1008".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("推荐人不存在");
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
        // requestCode标示请求的标示 resultCode表示有数据
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (LocalApplication.getInstance().getWebViewActivity() != null) {
                LocalApplication.getInstance().getWebViewActivity().finish();
            }
            finish();
        } else if (requestCode == 1 && resultCode == 2) {
            LocalApplication.getInstance().getMainActivity().isHomeChecked = true;
            LocalApplication.getInstance().getMainActivity().isHome = true;
            if (LocalApplication.getInstance().getWebViewActivity() != null) {
                LocalApplication.getInstance().getWebViewActivity().finish();
            }
            setResult(3, new Intent());
            finish();
        } else if (requestCode == 3 && resultCode == 3) {
            setResult(3);
            finish();
        } else if (requestCode == 3 && resultCode == 2) {
            setResult(2);
            finish();
        }

    }

    @Override
    public void onButtonClicked(Dialog dialog, int position, Object tag) {

        if (position == 0) {
            finish();
            ToastMaker.showShortToast("可以在安全中心-手势密码 中进行修改");
        }
        if (position == 1) {
            startActivity(new Intent(RegisterActivity.this, GestureEditActivity.class));
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
        tvGetyzm.setText("获取验证码");
        // tvGetyzm.setBackgroundResource(R.drawable.bg_corner_kong_blue);
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
                        //tvGetyzm.setBackgroundResource(R.drawable.bg_corner_blackline);
                        tvGetyzm.setText(count + "秒  ");
                    }

                    break;
                default:
                    break;
            }
        }
    };
}
