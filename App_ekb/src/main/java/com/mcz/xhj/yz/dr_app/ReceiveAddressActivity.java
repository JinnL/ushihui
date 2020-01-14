package com.mcz.xhj.yz.dr_app;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.AddressBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/10/13 0013.
 * 描述：添加收货地址
 */

public class ReceiveAddressActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView title_leftimageview;
    @BindView(R.id.title_centertextview)
    TextView title_centertextview;
    @BindView(R.id.ll_select_area)
    LinearLayout ll_select_area;
    @BindView(R.id.bt_save_address)
    TextView bt_save_address;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_postcode)
    EditText etPostcode;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
    Pattern pcode = Pattern.compile("^[0-9]{6}$");
    private AddressBean address;
    private boolean edit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_receive_address;
    }

    @Override
    protected void initParams() {
        title_centertextview.setText("收货地址");
        edit = getIntent().getBooleanExtra("edit", false);
        address = (AddressBean) getIntent().getSerializableExtra("address");
        if (address != null) {
            etName.setText(address.getName());
            etPhone.setText(address.getPhone());
            etAddress.setText(address.getAddress());
            etPostcode.setText(address.getPostCode());
        }
    }

    @OnClick({R.id.title_leftimageview, R.id.ll_select_area, R.id.bt_save_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.ll_select_area:
                break;
            case R.id.bt_save_address:
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    ToastMaker.showShortToast("请输入收货人姓名");
                    return;
                }
                Matcher m1 = p.matcher(stringCut.space_Cut(etPhone.getText().toString().trim()));
                if (!m1.matches()) {
                    ToastMaker.showShortToast(getResources().getString(R.string.phone_Wrong));
                    return;
                }
                if (TextUtils.isEmpty(etAddress.getText().toString())) {
                    ToastMaker.showShortToast("请输入详细地址");
                    return;
                }
                Matcher mCode = pcode.matcher(etPostcode.getText().toString().toString());
                if (!mCode.matches()) {
                    ToastMaker.showShortToast("请输入正确的邮政编码");
                    return;
                }

                if (edit) {
                    updateReceiptAddress();//修改地址
                } else {
                    insertReceiptAddress();//新增地址
                }
                break;
        }
    }

    /*编辑收货地址*/
    private void updateReceiptAddress() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.UPDATERECEIPTADDRESS)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("name", etName.getText().toString().trim())
                .addParams("phone", stringCut.space_Cut(etPhone.getText().toString().trim()))
                .addParams("address", etAddress.getText().toString().trim())
                .addParams("postCode", etPostcode.getText().toString().trim())
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                LogUtils.i("修改收货地址：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    ToastMaker.showShortToast("修改地址成功");
                    finish();

                } else if ("9998".equals(obj.getString("errorCode"))) {
                    new show_Dialog_IsLogin(ReceiveAddressActivity.this).show_Is_Login();
                    finish();
                } else if ("9999".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("系统异常");
                } else if ("1001".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("参数错误");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

    private void insertReceiptAddress() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.INSERTRECEIPTADDRESS)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("name", etName.getText().toString().trim())
                .addParams("phone", stringCut.space_Cut(etPhone.getText().toString().trim()))
                .addParams("address", etAddress.getText().toString().trim())
                .addParams("postCode", etPostcode.getText().toString().trim())
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                LogUtils.i("新增收货地址：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    ToastMaker.showShortToast("添加地址成功");
                    finish();
                } else if ("1001".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("地址只能包含中文,数字,字码以及'-'");
                } else if ("1003".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("收货信息填写不完整");
                } else if ("1004".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("收货信息已存在");
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    new show_Dialog_IsLogin(ReceiveAddressActivity.this).show_Is_Login();
                    finish();
                } else if ("9999".equals(obj.getString("errorCode"))) {
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
