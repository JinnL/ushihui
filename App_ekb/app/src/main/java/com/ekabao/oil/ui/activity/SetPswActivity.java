package com.ekabao.oil.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.SecurityUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.show_Dialog_IsLogin;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 易卡宝  App
 *
 * @time 2018/7/27 17:36
 * Created by lj on 2018/7/27 17:36.
 */

public class SetPswActivity extends BaseActivity {


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
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.et_psw_again)
    EditText etPswAgain;
    @BindView(R.id.tv_submit_psw)
    TextView tvSubmitPsw;
    private String mobilephone;
    private String smsCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_psw;
    }

    @Override
    protected void initParams() {
       // titleCentertextview.setText("设置登录密码");

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
                if (!StringCut.isPsw(etPsw.getText().toString().trim())) {
                    ToastMaker.showShortToast("请输入规则的密码");
                } else if (!etPsw.getText().toString().equalsIgnoreCase(etPswAgain.getText().toString())) {
                    ToastMaker.showShortToast("确认密码和新密码不相同");
                } else {
                    //修改登录密码
                    updateLoginPassword();
                }
                break;
        }
    }

    private void updateLoginPassword() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.FORGETPASSWORD)
                .addParams("valicateCode", smsCode)
                .addParams("pwdnew", SecurityUtils.MD5AndSHA256(etPsw.getText().toString().trim()))
                .addParams("confirmPwd", SecurityUtils.MD5AndSHA256(etPswAgain.getText().toString().trim()))
                .addParam("mobilephone", mobilephone)
                .addParam("type", 1 + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {

                        LogUtils.i("--->(忘记密码)设置登录密码：" + response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("登录密码设置成功");
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
                            finish();
                            new show_Dialog_IsLogin(SetPswActivity.this).show_Is_Login();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
