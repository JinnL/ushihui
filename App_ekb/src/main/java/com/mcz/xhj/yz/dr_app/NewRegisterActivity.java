package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
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
 * Created by zhulang on 2017/8/7.
 * 描述：2.0版注册界面
 */

public class NewRegisterActivity extends BaseActivity {
    @BindView(R.id.img_close_register)
    ImageView imgCloseRegister;
    @BindView(R.id.tv_title_register)
    TextView tvTitleRegister;
    @BindView(R.id.ll_title_register)
    RelativeLayout llTitleRegister;
    @BindView(R.id.tv_day_register)
    TextView tvDayRegister;
    @BindView(R.id.tv_week_register)
    TextView tvWeekRegister;
    @BindView(R.id.ll_welcome_register)
    LinearLayout llWelcomeRegister;
    @BindView(R.id.et_phonenumber)
    EditText etPhonenumber;
    @BindView(R.id.et_login_psw)
    EditText etLoginPsw;
    @BindView(R.id.et_register_yzm)
    EditText etRegisterYzm;
    @BindView(R.id.tv_getyzm)
    TextView tv_getyzm;
    @BindView(R.id.rl_yzm_register)
    RelativeLayout rlYzmRegister;
    @BindView(R.id.et_inviteCode)
    EditText etInviteCode;
    @BindView(R.id.tv_agreement_register)
    TextView tvAgreementRegister;
    @BindView(R.id.rl_agreement_register)
    RelativeLayout rlAgreementRegister;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_inviteCode)
    TextView tvInviteCode;
    @BindView(R.id.tv_user_protocol)
    TextView tvUserProtocol;
    @BindView(R.id.tv_toLogin)
    TextView tvToLogin;

    private boolean isInviteCode = false;
    Pattern pPhone = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
    Pattern pCode = Pattern.compile("^[0-9]{4}$");
    Pattern psw = Pattern.compile("^[a-zA-Z0-9]{6,18}$");
    private SharedPreferences preferences;
    private boolean isSendSms = false;
    private String timee;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_new;
    }

    @Override
    protected void initParams() {
        tvTitleRegister.setText("注册");
        //获取当前的日期和星期
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        String weekDay = transformToWeek(week);
        System.out.println("hyjr：日: " + day + " ,星期：" + weekDay);
        tvDayRegister.setText(day + "");
        tvWeekRegister.setText(weekDay);
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

    @OnClick({R.id.img_close_register, R.id.tv_getyzm, R.id.tv_agreement_register, R.id.tv_register, R.id.tv_inviteCode, R.id.tv_user_protocol, R.id.tv_toLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close_register:
                finish();
                break;
            case R.id.tv_getyzm:
                if (stringCut.space_Cut(etPhonenumber.getText().toString().trim()).length() < 11) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    stopTimer();
                    return;
                }
                isSendSms = true;
                existMobilePhoneS();
                break;
            case R.id.tv_agreement_register:
                //ToastMaker.showShortToast("同意用户协议");
                break;
            case R.id.tv_register:
                MobclickAgent.onEvent(NewRegisterActivity.this, UrlConfig.point + 2 + "");
                if (stringCut.space_Cut(etPhonenumber.getText().toString().trim()).length() < 11) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    return;
                }
                if (TextUtils.isEmpty(etLoginPsw.getText().toString().trim())) {
                    ToastMaker.showShortToast("登录密码不能为空");
                    return;
                }
                Matcher m1 = pPhone.matcher(stringCut.space_Cut(etPhonenumber.getText().toString().trim()));
                Matcher mCode = pCode.matcher(etRegisterYzm.getText().toString().trim());
                Matcher mInvite = psw.matcher(stringCut.space_Cut(etInviteCode.getText().toString().trim()));
                if (!m1.matches()) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    return;
                } else if (!stringCut.isPsw(etLoginPsw.getText().toString().trim())) {
                    ToastMaker.showShortToast("请输入规则的登录密码");
                    return;
                } else if (!mCode.matches()) {
                    ToastMaker.showShortToast("请输入正确的短信验证码");
                    return;
                }
                LogUtils.i("--->注册 是否邀请码：isInviteCode：" + isInviteCode);
                if (isInviteCode) {//填写邀请码
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
                break;
            case R.id.tv_inviteCode:
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
                break;
            case R.id.tv_user_protocol:
                startActivity(new Intent(NewRegisterActivity.this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.ZHUCE)
                        .putExtra("TITLE", "注册协议"));
                break;
            case R.id.tv_toLogin:
                startActivity(new Intent(NewRegisterActivity.this, NewLoginActivity.class));
                setResult(3);
                finish();
                break;
        }
    }

    private void sendRegMsg() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.SENDREGMSGAPP)
                .addParams("mobilephone", stringCut.space_Cut(etPhonenumber.getText().toString()))
                .addParam("type", "1")
                .addParam("time", SecurityUtils.MD5AndSHA256(timee))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
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
                        // TODO Auto-generated method stub
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
                .addParam("mobilephone", stringCut.space_Cut(etPhonenumber.getText().toString().trim()))
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
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
                // TODO Auto-generated method stub
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
                .addParam("mobilephone", stringCut.space_Cut(etPhonenumber.getText().toString().trim()))
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
                        // TODO Auto-generated method stub
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
                            startActivityForResult(new Intent(NewRegisterActivity.this, Register_Success.class)
                                    .putExtra("newId", jsonObj.getString("pid"))
                                    .putExtra("regSendLabel", jsonObj.getString("regSendLabel")), 3
                            );
                            finish();
                            //showAlertDialog("注册成功", "是否立即设置手势密码", new String[]{"暂不设置","立即设置"}, false, true, "");
                        } else if ("XTWH".equals(obj.getString("errorCode"))) {
                            startActivity(new Intent(NewRegisterActivity.this, WebViewActivity.class)
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
        // TODO Auto-generated method stub
        if (position == 0) {
            finish();
            ToastMaker.showShortToast("可以在安全中心-手势密码 中进行修改");
        }
        if (position == 1) {
            startActivity(new Intent(NewRegisterActivity.this, GestureEditActivity.class));
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
