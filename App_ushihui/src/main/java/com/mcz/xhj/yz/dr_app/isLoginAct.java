package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.mcz.xhj.yz.dr_view.VerificationCodeView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import okhttp3.Call;

public class isLoginAct extends BaseActivity implements OnClickListener {
    @BindView(R.id.et_login_psw)
    EditText et_login_psw; // 登录密码
    @BindView(R.id.et_phonenumber)
    EditText et_phonenumber;
    @BindView(R.id.et_login_safe)//图形验证码
            EditText et_login_safe;
    //	@BindView(R.id.tv_phonenumber)
//	TextView tv_phonenumber;
//	@BindView(R.id.lin_phonenumber)
//	LinearLayout lin_phonenumber;
    @BindView(R.id.ll_xieyi)
    LinearLayout ll_xieyi;
    @BindView(R.id.ll_yanzhengma)
    LinearLayout ll_yanzhengma;
    @BindView(R.id.ll_mima)
    RelativeLayout ll_mima;
    @BindView(R.id.image_clean)
    ImageView image_clean;
    @BindView(R.id.image_sure)
    CheckBox image_sure;
    @BindView(R.id.bt_login)
    Button bt_login;
    @BindView(R.id.title_leftimageview)
    ImageView title_leftimageview;
    @BindView(R.id.title_centertextview)
    TextView title_centertextview;// 抬头中间信息
    @BindView(R.id.tv_forget_psw)
    TextView tv_forget_psw;//
    @BindView(R.id.tv_agreement_user)
    TextView tv_agreement_user;
    @BindView(R.id.tv_flag)
    TextView tv_flag;
    @BindView(R.id.bt_reg)
    TextView bt_reg;
    @BindView(R.id.verifycodeview)//图形验证码
            VerificationCodeView verifycodeview;
    @BindView(R.id.image_eye)
    CheckBox image_eye; // 查看密码
    private SharedPreferences preferences;
    Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
    Pattern psw = Pattern.compile("^[a-zA-Z0-9]{6,18}$");

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_isloginpsw;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(isLoginAct.this, UrlConfig.point + 6 + "");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private String phonenum = "";
    private boolean flag = false;

    @Override
    protected void initParams() {
        title_leftimageview.setOnClickListener(this);
        title_centertextview.setText("");
        et_phonenumber.setHintTextColor(Color.parseColor("#aaaaaa"));
        et_login_psw.setHintTextColor(Color.parseColor("#aaaaaa"));
        et_login_safe.setHintTextColor(Color.parseColor("#aaaaaa"));
//		SpannableString ss = new SpannableString("请输入手机号     (建议使用银行预留手机号)");
//		ss.setSpan(new RelativeSizeSpan(1f), 0, 6, TypedValue.COMPLEX_UNIT_PX);
//		ss.setSpan(new RelativeSizeSpan(0.9f), 7, 24, TypedValue.COMPLEX_UNIT_PX);
//		et_phonenumber.setHint(ss);
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (intent.getStringExtra("phone") != null && !intent.getStringExtra("phone").equalsIgnoreCase("")) {
            et_phonenumber.setText(intent.getStringExtra("phone"));
            title_centertextview.setText("新用户注册");
            flag = true;
            tv_flag.setVisibility(View.VISIBLE);
            ll_xieyi.setVisibility(View.VISIBLE);
            bt_reg.setVisibility(View.GONE);
            ll_mima.setVisibility(View.GONE);
            bt_login.setText("下一步");
        }
        if (intent.getStringExtra("point") != null && !intent.getStringExtra("point").equalsIgnoreCase("")) {
            if (intent.getStringExtra("point").equalsIgnoreCase("personFrag")) {
                MobclickAgent.onEvent(isLoginAct.this, UrlConfig.point + 36 + "");
            }
        }
        // 手机号间隔
        Watcher watcher = new Watcher();
        et_phonenumber.addTextChangedListener(watcher);
        PwdWatcher watcherpwd = new PwdWatcher();
        et_login_psw.addTextChangedListener(watcherpwd);
        et_phonenumber.addTextChangedListener(watcher);
        preferences = LocalApplication.getInstance().sharereferences;
        image_clean.setOnClickListener(this);
        image_sure.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        tv_agreement_user.setOnClickListener(this);
        image_eye.setOnClickListener(this);
        bt_reg.setOnClickListener(this);
        tv_forget_psw.setOnClickListener(this);
        tv_forget_psw.setOnClickListener(this);
        verifycodeview.setOnClickListener(this);
        if (uri != null) {
            phonenum = uri.getQueryParameter("mp");
            et_phonenumber.setText(phonenum);
        }
//		if (uri != null) {
//			if(uri.getQueryParameter("flag").equalsIgnoreCase("101yyb")){
//				if(uri.getQueryParameter("phone")!=null){
//					et_phonenumber.setText(uri.getQueryParameter("phone"));
//				}
//				title_centertextview.setText("新用户注册");
//				title_righttextview.setVisibility(View.GONE);
//				flag = true;
//				tv_flag.setVisibility(View.VISIBLE);
//				ll_xieyi.setVisibility(View.VISIBLE);
//				bt_reg.setVisibility(View.GONE);
//				ll_mima.setVisibility(View.GONE);
//				bt_login.setText("下一步");
//			}
//		}
    }


    @Override
    public void onCancelDialog(Dialog dialog, Object tag) {
        // TODO Auto-generated method stub

    }

    class PwdWatcher implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0 && !et_phonenumber.getText().toString().equalsIgnoreCase("")) {
                bt_login.setBackgroundResource(R.drawable.bg_corner_red);
            } else {
                bt_login.setBackgroundResource(R.drawable.bg_corner_gray);
            }
        }
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
////			if (onTextLength > 0) {
////				lin_phonenumber.setVisibility(View.VISIBLE);
////				image_clean.setVisibility(View.VISIBLE);
////			} else {
////				lin_phonenumber.setVisibility(View.GONE);
////				image_clean.setVisibility(View.GONE);
////			}
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
            if (s.length() > 0 && !et_login_psw.getText().toString().equalsIgnoreCase("")) {
                bt_login.setBackgroundResource(R.drawable.bg_btn_corner);
            } else {
                bt_login.setBackgroundResource(R.drawable.bg_corner_gray);
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
            case R.id.image_eye:
                if (image_eye.isChecked()) {
                    et_login_psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    et_login_psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.tv_agreement_user:
                startActivity(new Intent(isLoginAct.this, WebViewActivity.class)
                        .putExtra("URL", "file:///android_asset/agreement_user.html")
                        .putExtra("TITLE", "注册协议"));
                break;
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.image_clean:
                et_phonenumber.setText("");
                break;
            case R.id.image_sure:

                break;
            case R.id.verifycodeview:
                getPicCode();
                break;
            case R.id.tv_forget_psw:
                Matcher m11 = p.matcher(stringCut.space_Cut(et_phonenumber.getText().toString().trim()));
                if (!m11.matches()) {
                    ToastMaker.showShortToast(getResources().getString(
                            R.string.phone_Wrong));
                } else {
                    startActivity(new Intent(isLoginAct.this, ForgetPswAct.class).putExtra("phone_num", stringCut.space_Cut(et_phonenumber.getText().toString().trim())));
                }
                break;
            case R.id.bt_reg:
                startActivityForResult(new Intent(isLoginAct.this, RegFirstAct.class), 3);
                et_login_psw.setText("");
                break;
            case R.id.bt_login:
                MobclickAgent.onEvent(isLoginAct.this, UrlConfig.point + 7 + "");
                if (stringCut.space_Cut(et_phonenumber.getText().toString().trim())
                        .length() < 11) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    return;
                }
                Matcher m1 = p.matcher(stringCut.space_Cut(et_phonenumber.getText()
                        .toString().trim()));
                Matcher mPsw = psw.matcher(et_login_psw.getText().toString().trim());
                if (!flag) {
                    if (!mPsw.matches()) {
                        ToastMaker.showLongToast("请输入规则的密码");
                        return;
                    }
                }
                if (!m1.matches()) {
                    ToastMaker.showShortToast(getResources().getString(
                            R.string.phone_Wrong));
                } else if (!picCode.equalsIgnoreCase("") && et_login_safe.getText().toString().equalsIgnoreCase("")) {
                    ToastMaker.showShortToast("请输入图形验证码");
                } else {
                    existMobilePhone();// 下一步
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
//										isLoginAct.this, LoginAct.class)
//										.putExtra("phone_num", stringCut
//												.space_Cut(et_phonenumber
//														.getText().toString()
//														.trim())), 1);
                        if (!flag) {
                            doLogin();//去登录
                        } else {
                            ToastMaker.showLongToast("此号码已注册");
                        }
                    } else {
                        if (!flag) {
                            ToastMaker.showLongToast("您还未注册，请先注册");
                        } else {
                            startActivityForResult(
                                    new Intent(isLoginAct.this, LoginQQPswAct.class)
                                            .putExtra("phone_num", et_phonenumber.getText().toString())
                                    , 3);
                        }

//								startActivityForResult(new Intent(
//										isLoginAct.this, LoginPswAct.class)
//										.putExtra("phone_num", stringCut
//												.space_Cut(et_phonenumber
//														.getText().toString()
//														.trim())), 1);
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

    private String picCode = "";

    private void getPicCode() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.GETPICCODE)
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                dismissDialog();
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean(("success"))) {
                    JSONObject jsonObj = obj.getJSONObject("map");
                    ll_yanzhengma.setVisibility(View.VISIBLE);
                    picCode = jsonObj.getString("code");
                    verifycodeview.setvCode(jsonObj.getString("code"));
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
                .addParam("mobilephone", stringCut.space_Cut(et_phonenumber.getText().toString()))
                .addParam("passWord", SecurityUtils.MD5AndSHA256(et_login_psw.getText().toString().trim()))
                .addParam("picCode", et_login_safe.getText().toString())
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
//							ToastMaker.showShortToast("登录成功");
//							tv_phonenumber.setText("");
                            et_login_psw.setText("");
//                            LocalApplication.getInstance().getMainActivity().onResume();
                            JSONObject jsonObj = obj.getJSONObject("map");
                            String token = jsonObj.getString("token");
                            JSONObject objmem = jsonObj.getJSONObject("member");
//							 String emailVerify = objmem.getString("emailVerify") ;
//							 String lastLoginIp = objmem.getString("lastLoginIp") ;
//							 String lastLoginTime = objmem.getString("lastLoginTime") ;
//							 String loginVerify = objmem.getString("loginVerify") ;
//							 String mobileVerify = objmem.getString("mobileVerify") ;
                            String mobilephone = objmem.getString("mobilephone");
                            String realVerify = objmem.getString("realVerify");
                            String uid = objmem.getString("uid");
                            String recommCodes = objmem.getString("recommCodes");
//							String name = objmem.getString("realName") ;
//							String sex = objmem.getString("sex") ;
//							 String regDate = objmem.getString("regDate") ;
//							 String regFrom = objmem.getString("regFrom") ;
//							 String status = objmem.getString("status") ;
//							 String tpwdSetting = objmem.getString("tpwdSetting") ;

                            preferences = LocalApplication.getInstance().sharereferences;
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("login", true);
                            editor.putString("uid", uid);
                            editor.putString("recommCodes", recommCodes);
                            editor.putString("realVerify", realVerify);
                            editor.putString("phone", mobilephone);
//							editor.putString("name",name ) ;
                            editor.putString("token", token);
                            editor.commit();
                            pushRegisterId();
//				            setResult(RESULT_OK, new Intent());
                            showAlertDialog("登录成功", "是否立即设置手势密码", new String[]{"暂不设置", "立即设置"}, true, true, "");

                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("账号或密码为空 ");
                        } else if ("1003".equals(obj.getString("errorCode"))) {
                            JSONObject jsonObj = obj.getJSONObject("map");
                            ToastMaker.showShortToast("账号或密码错误");
                            if (jsonObj.getInteger("loginErrorNums") != null) {
                                loginErrorNums = jsonObj.getInteger("loginErrorNums");//密码错误时返回错误次数
                                if (loginErrorNums >= 3) {
                                    getPicCode();
                                }
                            }
                        } else if ("1004".equals(obj.getString("errorCode"))) {
                            JSONObject jsonObj = obj.getJSONObject("map");
                            ToastMaker.showShortToast("验证码不能为空");
                            getPicCode();
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
            startActivity(new Intent(isLoginAct.this, GestureEditActivity.class));
            finish();
        }
    }
}
