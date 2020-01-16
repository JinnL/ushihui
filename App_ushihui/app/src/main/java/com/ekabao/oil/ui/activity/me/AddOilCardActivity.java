package com.ekabao.oil.ui.activity.me;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;

import java.util.regex.Pattern;

import butterknife.BindView;
import okhttp3.Call;


public class AddOilCardActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.title_lefttextview)
    TextView titleLefttextview;
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_centerimageview)
    ImageView titleCenterimageview;
    @BindView(R.id.title_righttextview)
    TextView titleRighttextview;
    @BindView(R.id.title_rightimageview)
    ImageView titleRightimageview;
    @BindView(R.id.view_line_bottom)
    View viewLineBottom;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.cb_sinopec)
    CheckBox cbSinopec;
    @BindView(R.id.ll_sinopec)
    LinearLayout llSinopec;
    @BindView(R.id.cb_cnpc)
    CheckBox cbCnpc;
    @BindView(R.id.ll_cnpc)
    LinearLayout llCnpc;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.et_code_again)
    EditText etCodeAgain;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.cb_xieyi)
    CheckBox cbXieyi;
    @BindView(R.id.ll_xieyi)
    LinearLayout llXieyi;
    @BindView(R.id.bt_add)
    Button btAdd;

    /**
     * 添加油卡
     *
     * @time 2018/11/9 15:45
     * Created by lj
     */
    private String uid;
    private int type = 1;////油卡类型 1:中石化 2:中石油


    // String regExp = "^((13[0-9])|(15[^4])|(166)|(17[0-8])|(18[0-9])|(19[8-9])|(147,145))\\d{8}$";
    //Pattern p =  = Pattern.compilepile(regExp);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_add_oil_card);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_oil_card;
    }

    @Override
    protected void initParams() {
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        titleCentertextview.setText("添加油卡");
        titleLeftimageview.setOnClickListener(this);
        llSinopec.setOnClickListener(this);
        llXieyi.setOnClickListener(this);
        llCnpc.setOnClickListener(this);
        btAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.ll_sinopec: //中石化
                //油卡类型 1:中石化 2:中石油
                type = 1;

                cbSinopec.setChecked(true);
                cbCnpc.setChecked(false);

                //中石化：以100011开头的19位卡号、
                etCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(19)});
                etCodeAgain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(19)});
                // etCodeAgain.setMaxLines(19);
                break;
            case R.id.ll_cnpc: //中石油
                type = 2;
                cbSinopec.setChecked(false);
                cbCnpc.setChecked(true);
                // 中石油：以90开头的16位卡号
                etCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                etCodeAgain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});

                //etCode.setMaxLines(16);
                //etCodeAgain.setMaxLines(16);
                // Pattern phone = Pattern.compile("^1[9][0]{14}$");

                break;
            case R.id.ll_xieyi: //服务协议
               /* if (cbXieyi.isChecked()) {
                    cbXieyi.setChecked(false);
                } else {
                    cbXieyi.setChecked(true);
                }*/
                startActivity(new Intent(AddOilCardActivity.this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.FW+"?app=true")
                        .putExtra("TITLE", "服务协议"));
                break;

            case R.id.bt_add: //击进入充值问题页面

                String Code = etCode.getText().toString();
                String CodeAgain = etCodeAgain.getText().toString();
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();

                //  LogUtils.e("CodeAgain"+CodeAgain.length());
                Pattern pPhone = Pattern.compile(getResources().getString(R.string.pPhone));

                if (Code.isEmpty() || CodeAgain.isEmpty() || name.isEmpty() || phone.isEmpty()) {
                    ToastMaker.showShortToast("输入框不能为空");
                    break;
                } else if (!Code.equals(CodeAgain)) {
                    ToastMaker.showShortToast("两次输入的卡号不相同");
                    break;
                }else if( !cbXieyi.isChecked()){
                    ToastMaker.showShortToast("请勾选协议");
                    break;
                }

                if (type == 1) {
                    if (!Code.startsWith("100011") || !CodeAgain.startsWith("100011")
                            || CodeAgain.length() < 19) {
                        ToastMaker.showShortToast("请输入正确的卡号");
                        break;
                    } else if (phone.length() < 11 || !pPhone.matcher(phone).matches()) {
                        ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                        break;
                    } else {
                        showDialog(Code, 1, name, phone);


                    }

                } else {
                    if (!Code.startsWith("90") || !CodeAgain.startsWith("90")
                            || CodeAgain.length() < 16) {
                        ToastMaker.showShortToast("请输入正确的卡号");
                        break;
                    } else if (phone.length() < 11 || !pPhone.matcher(phone).matches()) {
                        ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                        break;
                    } else {
                        showDialog(Code, 2, name, phone);
                        // getData(uid, Code, 2 + "", name, phone);
                    }
                }

                break;
        }
    }

    private void showDialog(final String code, final int type, final String name, final String phone) {
        DialogMaker.showAddOilCardSureDialog(this, "请确认您要添加的油卡卡号",
                code, "取消", "确定", new DialogMaker.DialogCallBack() {
                    @Override
                    public void onButtonClicked(Dialog dialog, int position, Object tag) {
                        // exit_dr();
                        // deleteOilCard(oillist.get(position1).getId());
                        getData(uid, code, type + "", name, phone);
                    }

                    @Override
                    public void onCancelDialog(Dialog dialog, Object tag) {

                    }
                }, "");
    }


    // 绑定油卡
    private void getData(String uid, String cardnum, String type, String realname, String mobilephone) {
        showWaitDialog("加载中...", true, "");
        LogUtils.e("添加油卡" + uid);
        OkHttpUtils.post().url(UrlConfig.bindFuelCard)
                .addParams("uid", uid)
                .addParams("cardnum", cardnum)
                .addParams("type", type) //油卡类型 1:中石化 2:中石油
                .addParams("realname", realname)
                .addParams("mobilephone", mobilephone)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("--->绑定油卡：" + response);
                        dismissDialog();

                        JSONObject obj = JSON.parseObject(response);
                        //true=成功处理，false=不成功处理
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("添加成功");
                            setResult(RESULT_OK);
                            finish();
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
//                            new show_Dialog_IsLogin(MessageCenterActivity.this).show_Is_Login();
                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("油卡已存在");
                        } else {
                            ToastMaker.showShortToast("系统异常");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

}
