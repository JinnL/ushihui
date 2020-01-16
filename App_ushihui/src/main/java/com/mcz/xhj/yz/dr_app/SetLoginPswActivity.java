package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zhulang on 2017/8/6.
 * 描述：设置登录密码
 */

public class SetLoginPswActivity extends BaseActivity {
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_login_psw;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("设置登录密码");
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
                if (!stringCut.isPsw(etPsw.getText().toString().trim())) {
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
                .addParam("mobilephone",mobilephone)
                .addParam("type",1+"")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        LogUtils.i("--->(忘记密码)设置登录密码："+response);
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
                            new show_Dialog_IsLogin(SetLoginPswActivity.this).show_Is_Login();
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
}
