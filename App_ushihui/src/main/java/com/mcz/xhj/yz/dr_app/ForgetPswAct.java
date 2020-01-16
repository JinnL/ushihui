package com.mcz.xhj.yz.dr_app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.CircleTextProgressbar;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import okhttp3.Call;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class ForgetPswAct extends BaseActivity implements OnClickListener {
    @BindView(R.id.title_centertextview)
    TextView title_centertextview;// 抬头中间信息
    @BindView(R.id.title_rightimageview)
    ImageView title_rightimageview;// 抬头右边图片
    @BindView(R.id.title_leftimageview)
    ImageView title_leftimageview;// 抬头左边按钮
    @BindView(R.id.bt_login)
    Button bt_login;// 确定
    @BindView(R.id.tv_phone)
    TextView tv_phone; // 手机号码
    @BindView(R.id.tv_tip_limit)
    TextView tv_tip_limit; // 手机号码
    @BindView(R.id.code_et)
    EditText code_et; // 验证码
    @BindView(R.id.et_login_psw)
    EditText et_login_psw; // 登录密码
    @BindView(R.id.image_eye)
    CheckBox image_eye; // 显示登录密码
    @BindView(R.id.tv_red_progress_text)
    CircleTextProgressbar mTvProgressBar;
    @BindView(R.id.tv_getsmg)
    TextView tv_getsmg;
    @Nullable
    @BindView(R.id.check_tuijian)
    CheckBox check_tuijian;
    @BindView(R.id.et_login_referrer)
    EditText et_login_referrer; // 推荐人ID


    private SharedPreferences preferences;

    private String mobilephone, uid;
    Pattern p = Pattern.compile("^[0-9]{4}$");
    Pattern psw = Pattern.compile("^[a-zA-Z0-9]{6,18}$");
    Pattern pPhone = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
    private boolean isSend = false;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_forget_loginpsw;
    }

    private CircleTextProgressbar.OnCountdownProgressListener progressListener = new CircleTextProgressbar.OnCountdownProgressListener() {
        @Override
        public void onProgress(int what, int progress) {
            if (what == 1) {
                mTvProgressBar.setText(progress + "");
                if (progress == 0) {
                    tv_getsmg.setVisibility(View.VISIBLE);
                    mTvProgressBar.setVisibility(View.GONE);
                    isSend = true;
                    mTvProgressBar.setText("重新获取");
                    mTvProgressBar.setTextColor(Color.parseColor("#ec5c59"));
                }
            } else if (what == 2) {
                mTvProgressBar.setText(progress + "");
            }
            // 比如在首页，这里可以判断进度，进度到了100或者0的时候，你可以做跳过操作。
        }
    };

    @Override
    protected void initParams() {
        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");
        title_centertextview.setText("忘记密码");
        Intent intent = getIntent();
        if (intent.getStringExtra("phone_num") != null && !intent.getStringExtra("phone_num").equalsIgnoreCase("")) {
            mobilephone = intent.getStringExtra("phone_num");
        }
        et_login_psw.setHintTextColor(0xffA0A0A0);
        code_et.setHintTextColor(0xffA0A0A0);
        et_login_referrer.setHintTextColor(0xffA0A0A0);
        tv_phone.setText(stringCut.phoneCut(mobilephone.replaceAll(" ", "")));
        forgetPwdSmsCode_Mobile();
        bt_login.setOnClickListener(this);
        title_leftimageview.setOnClickListener(this);
        image_eye.setOnClickListener(this);
        check_tuijian.setOnClickListener(this);
        tv_getsmg.setOnClickListener(this);


        // 密码监听
        Watcher watcher = new Watcher();
        et_login_psw.addTextChangedListener(watcher);

        Watcher_code watcher_code = new Watcher_code();
        code_et.addTextChangedListener(watcher_code);

        Watcher_pwd_agin watcher_pwd_agin = new Watcher_pwd_agin();
        et_login_referrer.addTextChangedListener(watcher_pwd_agin);


        //短信验证码倒计时
        mTvProgressBar.setCountdownProgressListener(1, progressListener);
        mTvProgressBar.setProgressType(CircleTextProgressbar.ProgressType.COUNT);
        mTvProgressBar.setProgressColor(Color.parseColor("#ec5c59"));
        mTvProgressBar.setTimeMillis(60000);
        mTvProgressBar.setProgressType(CircleTextProgressbar.ProgressType.COUNT_BACK);
        mTvProgressBar.reStart();
        tv_getsmg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSend) {
                    tv_getsmg.setVisibility(View.GONE);
                    mTvProgressBar.setVisibility(View.VISIBLE);
                    mTvProgressBar.reStart();
                    forgetPwdSmsCode_Mobile();
                    isSend = false;
                }
            }
        });

    }

    int i = 0;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bt_login:
                Matcher m1;
                Matcher mPsw;
                Matcher mPhone;
                Matcher mmPhone;
                mPhone = pPhone.matcher(et_login_referrer.getText().toString());
                if (getIntent().getBooleanExtra("isForget", false)) {

                    m1 = p.matcher(code_et.getText().toString().trim());
                    mPsw = psw.matcher(et_login_psw.getText().toString().trim());
                    if (!m1.matches()) {
                        tv_tip_limit.setText("请输入正确的验证码");
                    } else if (!stringCut.isPsw(et_login_psw.getText().toString()
                            .trim())) {
                        tv_tip_limit.setText("请输入规则的密码");
                    }

                } else {
                    m1 = p.matcher(code_et.getText().toString().trim());
                    mPsw = psw.matcher(et_login_psw.getText().toString().trim());
                    if (!m1.matches()) {
                        tv_tip_limit.setText("请输入正确的验证码");
                    } else if (!stringCut.isPsw(et_login_psw.getText().toString().trim())) {
                        tv_tip_limit.setText("请输入规则的密码");
                    } else if (!et_login_psw.getText().toString().equalsIgnoreCase(et_login_referrer.getText().toString())) {
                        tv_tip_limit.setText("确认密码和新密码不相同");
                    } else {
                        MobclickAgent.onEvent(ForgetPswAct.this, "100013");
                        updateLoginPassWord_Mobile();// 忘记登录密码
                    }
                }
                break;
            case R.id.title_leftimageview:
                this.finish();
                break;
            case R.id.tv_getcode:
//			time();
                MobclickAgent.onEvent(ForgetPswAct.this, "100012");
                forgetPwdSmsCode_Mobile();
                break;
            case R.id.image_eye:
                if (image_eye.isChecked()) {
                    et_login_psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    et_login_psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.check_tuijian:
                if (check_tuijian.isChecked()) {
                    et_login_referrer.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    et_login_referrer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            default:
                break;
        }
    }

    class Watcher_code implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length()>0&&!et_login_referrer.getText().toString().equalsIgnoreCase("")&&!et_login_psw.getText().toString().equalsIgnoreCase("")){
                bt_login.setBackgroundResource(R.drawable.bg_btn_corner);
            }else{
                bt_login.setBackgroundResource(R.drawable.bg_corner_gray);
            }
        }
    }
    class Watcher_pwd_agin implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if(s.length()>0&&!et_login_referrer.getText().toString().equalsIgnoreCase("")&&!code_et.getText().toString().equalsIgnoreCase("")){
                bt_login.setBackgroundResource(R.drawable.bg_btn_corner);
            }else{
                bt_login.setBackgroundResource(R.drawable.bg_corner_gray);
            }
        }
    }

    class Watcher implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if(s.length()>0&&!et_login_referrer.getText().toString().equalsIgnoreCase("")&&!code_et.getText().toString().equalsIgnoreCase("")){
                bt_login.setBackgroundResource(R.drawable.bg_btn_corner);
            }else{
                bt_login.setBackgroundResource(R.drawable.bg_corner_gray);
            }
        }
    }

    private void updateLoginPassWord_Mobile() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.UPDATELOGINPASSWORD)
                .addParam("mobilephone", mobilephone)
                .addParams("pwd", SecurityUtils.MD5AndSHA256(et_login_psw.getText().toString().trim()))
                .addParams("smsCode", code_et.getText().toString().trim())
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("登录密码重置成功");
                            finish();
                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("验证码错误");
                        } else if ("1002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("密码为空");
                        } else if ("1003".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("登录密码不合法");
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(ForgetPswAct.this).show_Is_Login();
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

    private void forgetPwdSmsCode_Mobile() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.FORGETPWDSMSCODE)
                .addParam("mobilephone", mobilephone)
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                dismissDialog();
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean(("success"))) {
                    ToastMaker.showShortToast("验证码已发送");
                } else if ("1001".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("短信发送失败");
                } else if ("9999".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("系统错误");
                } else {
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
}
