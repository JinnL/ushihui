package com.ekabao.oil.ui.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.AddressAdapter;
import com.ekabao.oil.bean.AddressBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.view.CityPick.CityPicker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.show_Dialog_IsLogin;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * $desc$
 * 地址管理
 *
 * @time $data$ $time$
 * Created by Administrator on 2018/7/17.
 */

public class AddressManageActivity extends BaseActivity implements AddressAdapter.AddressManageListener {
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
    @BindView(R.id.lv_address)
    ListView lvAddress;
    @BindView(R.id.ll_norecord)
    LinearLayout llNorecord;
    @BindView(R.id.tv_add_address)
    Button tvAddAddress;
    private CityPicker mCityPicker;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private AddressAdapter addressAdapter;
    private List<AddressBean> addressList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_manage;
    }

    @Override
    protected void initParams() {

        titleCentertextview.setText("地址管理");


        /**
         * 地址选择器
         * */
     /*   InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etDetailedaddress.getWindowToken(), 0);

        if (mCityPicker == null) {
            mCityPicker = new CityPicker(this, findViewById(R.id.rl_selectAddres))
                    .setOnCitySelectListener(new CityPicker.OnCitySelectListener() {

                        @Override
                        public void onCitySelect(String province, int pid, String city, int cityid, String county, int cid) {
                            tvAddress.setText(province + city + county);
                            provinceid = pid;
                            cityId = cityid;
                            areaid = cid;
                        }
                    });
        }
        mCityPicker.show();*/

        getReceiptAddress();



        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // currentPosition = position;
                setResult(Activity.RESULT_OK, new Intent().putExtra("position", position));
                finish();
            }
        });
    }

    @OnClick({R.id.title_leftimageview, R.id.tv_add_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.tv_add_address:
                startActivityForResult(new Intent(AddressManageActivity.this,
                        AddAddressActivity.class), 55551);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 55551) {
            getReceiptAddress();
        }

    }

    /*获取所有收货地址*/
    private void getReceiptAddress() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.RECEIPTADDRESS)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                //ptr_address.refreshComplete();
                LogUtils.i("所有的收货地址：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    JSONArray list = map.getJSONArray("addrlist");
                    // LogUtils.e("所有的收货地址：" + list.size());
                    if (list.size() > 0) {
                        llNorecord.setVisibility(View.GONE);
                        addressList = JSON.parseArray(list.toJSONString(), AddressBean.class);
                        addressAdapter = new AddressAdapter(AddressManageActivity.this, addressList);
                        addressAdapter.setOnAddressManageListener(AddressManageActivity.this);
                        lvAddress.setAdapter(addressAdapter);
                    } else {
                        llNorecord.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                // ptr_address.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }


    @Override
    public void onSelected(int position) {

        updateReceiptAddressDefault(position);
        //LogUtils.e("设置默认地址");
        addressAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEdit(int position) {
        startActivityForResult(new Intent(AddressManageActivity.this,
                AddAddressActivity.class).
                putExtra("address", addressList.get(position)).
                putExtra("edit", true), 55551);
    }

    @Override
    public void onDelete(int position) {
        deleteReceiptAddress(position);
        addressList.remove(position);
        addressAdapter.notifyDataSetChanged();
    }

    /*设为默认收货地址*/
    private void updateReceiptAddressDefault(int position) {
        showWaitDialog("加载中...", true, "");
        LogUtils.e("设置默认地址：" + preferences.getString("uid", "")+addressList.get(position).getId() );

        OkHttpUtils.post()
                .url(UrlConfig.RECEIPTADDRESSDEFAULT)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("id", addressList.get(position).getId() + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                // ptr_address.refreshComplete();
                LogUtils.i("设置默认地址：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    ToastMaker.showShortToast("设置默认地址成功");
                    getReceiptAddress();
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    new show_Dialog_IsLogin(AddressManageActivity.this).show_Is_Login();
                    finish();
                } else if ("9999".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("系统异常");
                }

            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                // ptr_address.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

    /*删除地址*/
    private void deleteReceiptAddress(final int position) {
        showWaitDialog("加载中...", true, "");
        LogUtils.e("uid"+ preferences.getString("uid", "")+"id"+addressList.get(position).getId());
        OkHttpUtils.post()
                .url(UrlConfig.DELETERECEIPTADDRESS)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("addrid", addressList.get(position).getId() + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                //ptr_address.refreshComplete();
                LogUtils.i("删除收货地址：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    ToastMaker.showShortToast("删除地址成功");
                    getReceiptAddress();
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    new show_Dialog_IsLogin(AddressManageActivity.this).show_Is_Login();
                    finish();
                } else if ("9999".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("系统异常");
                } else if ("1001".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("默认收货地址不能删除");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                //ptr_address.refreshComplete();
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
