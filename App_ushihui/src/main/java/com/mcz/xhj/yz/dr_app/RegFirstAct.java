package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import okhttp3.Call;

public class RegFirstAct extends BaseActivity implements OnClickListener {
    @BindView(R.id.et_phonenumber)
    EditText et_phonenumber;
    @BindView(R.id.title_leftimageview)
    ImageView title_leftimageview;
    @BindView(R.id.title_centertextview)
    TextView title_centertextview;// 抬头中间信息
    @BindView(R.id.title_righttextview)
    TextView title_righttextview;//
    @BindView(R.id.tv_agreement_user)
    TextView tv_agreement_user;
    @Nullable
    @BindView(R.id.tv_tip_limit)
    TextView tv_tip_limit;
    @BindView(R.id.bt_reg)
    Button bt_reg;
    @BindView(R.id.ll_login)
    LinearLayout ll_login;
    Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_regfirst;
    }


    private String phonenum = "";

    @Override
    protected void initParams() {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        title_centertextview.setText("注册");
        ll_login.setOnClickListener(this);
        title_leftimageview.setOnClickListener(this);
        et_phonenumber.setHintTextColor(Color.parseColor("#aaaaaa"));
        // 手机号间隔
        Watcher watcher = new Watcher();
        et_phonenumber.addTextChangedListener(watcher);
        tv_agreement_user.setOnClickListener(this);
        bt_reg.setOnClickListener(this);
        title_righttextview.setOnClickListener(this);
        if (uri != null) {
            phonenum = uri.getQueryParameter("mp");
            et_phonenumber.setText(phonenum);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
		MobclickAgent.onResume(this);
        MobclickAgent.onEvent(RegFirstAct.this, UrlConfig.point+1+"");
    }
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

    @Override
    public void onCancelDialog(Dialog dialog, Object tag) {
        // TODO Auto-generated method stub

    }

    class Watcher implements TextWatcher {
//        int beforeTextLength = 0;
//        int onTextLength = 0;
//        boolean isChanged = false;
//
//        int location = 0;// 记录光标的位置
//        private char[] tempChar;
//        private StringBuffer buffer = new StringBuffer();
//        int konggeNumberB = 0;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
//            onTextLength = s.length();
//            buffer.append(s.toString());
//            if (onTextLength == beforeTextLength || isChanged) {
//                isChanged = false;
//                return;
//            }
//            isChanged = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
//            beforeTextLength = s.length();
//            if (buffer.length() > 0) {
//                buffer.delete(0, buffer.length());
//            }
//            konggeNumberB = 0;
//            for (int i = 0; i < s.length(); i++) {
//                if (s.charAt(i) == ' ') {
//                    konggeNumberB++;
//                }
//            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if(s.length()>0){
                bt_reg.setBackgroundResource(R.drawable.bg_btn_corner);
            }else{
                bt_reg.setBackgroundResource(R.drawable.bg_corner_gray);
            }
//            if (isChanged) {
//                location = et_phonenumber.getSelectionEnd();
//                int index = 0;
//                while (index < buffer.length()) {
//                    if (buffer.charAt(index) == ' ') {
//                        buffer.deleteCharAt(index);
//                    } else {
//                        index++;
//                    }
//                }
//
//                index = 0;
//                int konggeNumberC = 0;
//                while (index < buffer.length()) {
//                    if ((index == 3 || index == 8)) {
//                        buffer.insert(index, ' ');
//                        konggeNumberC++;
//                    }
//                    index++;
//                }
//                if (konggeNumberC > konggeNumberB) {
//                    location += (konggeNumberC - konggeNumberB);
//                }
//
//                tempChar = new char[buffer.length()];
//                buffer.getChars(0, buffer.length(), tempChar, 0);
//                String str = buffer.toString();
//                if (location > str.length()) {
//                    location = str.length();
//                } else if (location < 0) {
//                    location = 0;
//                }
//
//                et_phonenumber.setText(str);
////				tv_phonenumber.setText(str);
//                Editable etable = et_phonenumber.getText();
//                Selection.setSelection(etable, location);
//                isChanged = false;
//            }
        }
    }

    int i = 0;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_agreement_user:
                startActivity(new Intent(RegFirstAct.this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.ZHUCE)
                        .putExtra("TITLE", "注册协议"));
                break;
            case R.id.ll_login:
                startActivity(new Intent(RegFirstAct.this, NewLoginActivity.class));
                setResult(3);
                finish();
                break;
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.bt_reg:
                MobclickAgent.onEvent(RegFirstAct.this, UrlConfig.point+2+"");
                if (stringCut.space_Cut(et_phonenumber.getText().toString().trim()).length() < 11) {
                    tv_tip_limit.setText(getResources().getString(R.string.phone_Wrong));
                    return;
                }
                Matcher m1 = p.matcher(stringCut.space_Cut(et_phonenumber.getText()
                        .toString().trim()));
                if (!m1.matches()) {
                    tv_tip_limit.setText(getResources().getString(R.string.phone_Wrong));
                } else {
//                    existMobilePhone();// 下一步
                    existMobilePhoneS();// 下一步
                }
                break;
            default:
                break;
        }
    }

    private void existMobilePhone() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.EXISTMOBILEPHONE)
                .addParam("mobilephone", stringCut.space_Cut(et_phonenumber.getText().toString().trim()))
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                dismissDialog();
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean(("success"))) {
                    String errorCode = obj.getString("errorCode");
                    if (obj.getJSONObject("map").getBoolean("exists")) {
//								startActivityForResult(new Intent(
//										NewLoginActivity.this, LoginAct.class)
//										.putExtra("phone_num", stringCut
//												.space_Cut(et_phonenumber
//														.getText().toString()
//														.trim())), 1);
                        tv_tip_limit.setText("此号码已注册");
                    } else {
                        startActivityForResult(new Intent(RegFirstAct.this, LoginQQPswAct.class).putExtra("phone", et_phonenumber.getText().toString()), 3);
//                      startActivity(new Intent(RegFirstAct.this, LoginQQPswAct.class).putExtra("phone", et_phonenumber.getText().toString()));
                    }

//								startActivityForResult(new Intent(
//										NewLoginActivity.this, LoginPswAct.class)
//										.putExtra("phone_num", stringCut
//												.space_Cut(et_phonenumber
//														.getText().toString()
//														.trim())), 1);
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
    private void existMobilePhoneS() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.EXISTMOBILEPHONE)
                .addParam("mobilephone", stringCut.space_Cut(et_phonenumber.getText().toString().trim()))
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                dismissDialog();
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean(("success"))) {
                    String errorCode = obj.getString("errorCode");
                    if (obj.getJSONObject("map").getBoolean("exists")) {
                        tv_tip_limit.setText("此号码已注册");
                    } else {
                        if(obj.getJSONObject("map").getString("time")!=null){
                            startActivityForResult(new Intent(RegFirstAct.this, LoginQQPswAct.class).putExtra("time", obj.getJSONObject("map").getString("time")).putExtra("phone", et_phonenumber.getText().toString()), 3);
                        }else {
                            finish();
                        }
                    }
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
        }else if (requestCode == 3 && resultCode == 2) {
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
            startActivity(new Intent(RegFirstAct.this, GestureEditActivity.class));
            finish();
        }
    }
}
