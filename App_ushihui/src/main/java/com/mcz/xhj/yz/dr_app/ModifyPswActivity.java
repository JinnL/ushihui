package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.ActivityStack;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by DELL on 2017/11/2.
 * 描述：修改密码界面（包括交易密码和登录密码）
 */

public class ModifyPswActivity extends BaseActivity {

    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.et_psw_old)
    EditText etPswOld;
    @BindView(R.id.et_psw_new)
    EditText etPswNew;
    @BindView(R.id.et_psw_new_again)
    EditText etPswNewAgain;
    @BindView(R.id.tv_forget_psw)
    TextView tvForgetPsw;
    @BindView(R.id.tv_submit_psw)
    TextView tvSubmitPsw;

    private int isFrom;//修改 0-登录密码，1-交易密码
    Pattern psw = Pattern.compile("^[a-zA-Z0-9]{6,18}$");
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_psw;
    }

    @Override
    protected void initParams() {
        Intent intent = getIntent();
        isFrom = intent.getIntExtra("isFrom", -1);
        LogUtils.i("--->修改密码（0-登录密码，1-交易密码）：isFrom："+isFrom);
        if (isFrom == 0) {
            titleCentertextview.setText("修改登录密码");
            etPswOld.setHint("请输入原登录密码");
            etPswOld.setInputType(InputType.TYPE_CLASS_TEXT);
            etPswOld.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)}); //最大输入长度18
            etPswOld.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
            etPswNew.setHint("请输入6-18位字母、数字组合");
            etPswNew.setInputType(InputType.TYPE_CLASS_TEXT);
            etPswNew.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)}); //最大输入长度18
            etPswNew.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
            etPswNewAgain.setHint("请再输入新登录密码");
            etPswNewAgain.setInputType(InputType.TYPE_CLASS_TEXT);
            etPswNewAgain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)}); //最大输入长度18
            etPswNewAgain.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框

        } else if (isFrom == 1) {
            titleCentertextview.setText("修改交易密码");
            etPswOld.setHint("请输入原交易密码");
            etPswOld.setInputType(InputType.TYPE_CLASS_NUMBER);
            etPswOld.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度6
            etPswOld.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
            etPswNew.setHint("请输入6位新交易密码");
            etPswNew.setInputType(InputType.TYPE_CLASS_NUMBER);
            etPswNew.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度6
            etPswNew.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
            etPswNewAgain.setHint("请再输入新交易密码");
            etPswNewAgain.setInputType(InputType.TYPE_CLASS_NUMBER);
            etPswNewAgain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度6
            etPswNewAgain.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
        }
    }

    @OnClick({R.id.title_leftimageview, R.id.tv_forget_psw, R.id.tv_submit_psw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;

            case R.id.tv_forget_psw:
                startActivity(new Intent(this, NewForgetPswActivity.class).putExtra("isFrom", isFrom));
                finish();
                break;

            case R.id.tv_submit_psw:
                if (isFrom == 0) {//修改登录密码
                    if (TextUtils.isEmpty(etPswOld.getText().toString().trim())) {
                        ToastMaker.showShortToast("请输入原登录密码");
                    } else if (TextUtils.isEmpty(etPswNew.getText().toString().trim())) {
                        ToastMaker.showShortToast("请输入6-18位字母、数字组合");
                    } else if (TextUtils.isEmpty(etPswNewAgain.getText().toString().trim())) {
                        ToastMaker.showShortToast("请再输入新登录密码");
                    }
                    Matcher psw_matcher = psw.matcher(etPswNew.getText().toString().trim());
                    if (!psw_matcher.matches()) {
                        ToastMaker.showShortToast("请输入规则的密码");
                    }
                    if (!etPswNew.getText().toString().trim().equals(etPswNewAgain.getText().toString().trim())) {
                        ToastMaker.showShortToast("两次登录密码不一致，请重新输入");
                    }
                    updateLoginPsw();
                }
                if (isFrom == 1) {//修改交易密码
                    if (TextUtils.isEmpty(etPswOld.getText().toString().trim())) {
                        ToastMaker.showShortToast("请输入原交易密码");
                    } else if (TextUtils.isEmpty(etPswNew.getText().toString().trim())) {
                        ToastMaker.showShortToast("请输入6位新交易密码");
                    } else if (TextUtils.isEmpty(etPswNewAgain.getText().toString().trim())) {
                        ToastMaker.showShortToast("请再输入新交易密码");
                    }
                    if (!etPswNew.getText().toString().trim().equals(etPswNewAgain.getText().toString().trim())) {
                        ToastMaker.showShortToast("两次交易密码不一致，请重新输入");
                    }
                    updateTradePsw();
                }
                break;
        }
    }

    //TODO 修改登录密码接口
    private void updateLoginPsw() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.UPDATELOGINPASSWORD)
                .addParam("uid", preferences.getString("uid", ""))
                .addParam("token",preferences.getString("token",""))
                .addParams("pwd", SecurityUtils.MD5AndSHA256(etPswOld.getText().toString().trim()))
                .addParams("pwdnew", SecurityUtils.MD5AndSHA256(etPswNew.getText().toString().trim()))
                .addParams("confirmPwd", SecurityUtils.MD5AndSHA256(etPswNewAgain.getText().toString().trim()))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        LogUtils.i("--->修改登录密码："+response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("登录密码设置成功");
                            exit_dr();
                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("验证码错误");
                        } else if ("1002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("密码为空");
                        } else if ("1003".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("登录密码不合法");
                        } else if ("3004".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("原密码输入错误");
                        } else if ("3005".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("新密码和确认密码不一致！");
                        }else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(ModifyPswActivity.this).show_Is_Login();
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

    private void exit_dr() {
        boolean isOpenUpush = preferences.getBoolean("isOpenUpush",false);
        String token = preferences.getString("token","");
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear() ;
        editor.putBoolean("updateLoginPsw",true);
        editor.putBoolean("login",false) ;
        editor.putBoolean("FirstLog",false) ;
        editor.putBoolean("isOpenUpush",isOpenUpush) ;
        //editor.putString("token",token) ;
        editor.commit();
        finish();
        ActivityStack.getInstance().finishToActivity(MainActivity.class,false);
        startActivity(new Intent(ModifyPswActivity.this,NewLoginActivity.class));

    }

    //TODO 修改交易密码接口
    private void updateTradePsw() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.UPDATETRADEPASSWORD)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("token",preferences.getString("token",""))
                .addParams("pwd", SecurityUtils.MD5AndSHA256(etPswOld.getText().toString().trim()))
                .addParams("pwdnew", SecurityUtils.MD5AndSHA256(etPswNew.getText().toString().trim()))
                .addParams("confirmPwd", SecurityUtils.MD5AndSHA256(etPswNewAgain.getText().toString().trim()))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        LogUtils.i("--->修改交易密码："+response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("交易密码修改成功");
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
                            new show_Dialog_IsLogin(ModifyPswActivity.this).show_Is_Login() ;
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
