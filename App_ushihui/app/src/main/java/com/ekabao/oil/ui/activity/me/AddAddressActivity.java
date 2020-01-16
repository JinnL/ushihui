package com.ekabao.oil.ui.activity.me;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.AddressBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.view.CityPick.CityPicker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.show_Dialog_IsLogin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 易卡宝  App
 * 添加收货地址
 *
 * @time 2018/8/1 17:19
 * Created by lj on 2018/8/1 17:19.
 */

public class AddAddressActivity extends BaseActivity {
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
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.ll_select_area)
    LinearLayout llSelectArea;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_postcode)
    EditText etPostcode;
    @BindView(R.id.bt_save_address)
    TextView btSaveAddress;
    @BindView(R.id.tv_address)
    TextView tvAddress;


    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    Pattern p = Pattern.compile(LocalApplication.context.getResources().getString(R.string.pPhone));
    Pattern pcode = Pattern.compile("^[0-9]{6}$");
    private AddressBean address;
    private boolean edit;

    private CityPicker mCityPicker;
    private String provinceid;
    private String cityId;
    private String areaid;

    //  AddressInfo addressInfo;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void initParams() {
        //titleCentertextview.setText("添加收货地址");
        edit = getIntent().getBooleanExtra("edit", false);
        address = (AddressBean) getIntent().getSerializableExtra("address");
        if (address != null) {
            etName.setText(address.getName());
            etPhone.setText(address.getPhone());
            etAddress.setText(address.getAddress());
            etPostcode.setText(address.getPostCode());
            provinceid = address.getProvinceName();
            cityId = address.getCityName();
            areaid = address.getAreaName();
            tvAddress.setText(provinceid + cityId
                    + areaid);


        }

        if (edit){
            titleCentertextview.setText("修改收货地址");
            btSaveAddress.setText("确认修改");
        }else {
            titleCentertextview.setText("添加收货地址");
            btSaveAddress.setText("确认添加");
        }
    }

    @OnClick({R.id.title_leftimageview, R.id.ll_select_area, R.id.bt_save_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.ll_select_area:
                /**
                 * 地址选择器
                 * */
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etAddress.getWindowToken(), 0);

                if (mCityPicker == null) {
                    mCityPicker = new CityPicker(this, findViewById(R.id.ll_select_area))
                            .setOnCitySelectListener(new CityPicker.OnCitySelectListener() {

                                @Override
                                public void onCitySelect(String province, int pid, String city, int cityid, String county, int cid) {
                                    tvAddress.setText(province + city + county);
                                    provinceid = province;
                                    cityId = city;
                                    areaid = county;
                                }
                            });
                }
                mCityPicker.show();

                break;
            case R.id.bt_save_address:
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    ToastMaker.showShortToast("请输入收货人姓名");
                    return;
                }
                Matcher m1 = p.matcher(StringCut.space_Cut(etPhone.getText().toString().trim()));
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
                .addParams("id", address.getId() + "")
                .addParams("name", etName.getText().toString().trim())
                .addParams("phone", StringCut.space_Cut(etPhone.getText().toString().trim()))
                .addParams("address", etAddress.getText().toString().trim())
                .addParams("postCode", etPostcode.getText().toString().trim())
                .addParams("provinceName", provinceid)
                .addParams("cityName", cityId)
                .addParams("areaName", areaid)
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
                    new show_Dialog_IsLogin(AddAddressActivity.this).show_Is_Login();
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
                .addParams("phone", StringCut.space_Cut(etPhone.getText().toString().trim()))
                .addParams("address", etAddress.getText().toString().trim())
                .addParams("postCode", etPostcode.getText().toString().trim())
                .addParams("provinceName", provinceid)
                .addParams("cityName", cityId)
                .addParams("areaName", areaid)

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
                    new show_Dialog_IsLogin(AddAddressActivity.this).show_Is_Login();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
