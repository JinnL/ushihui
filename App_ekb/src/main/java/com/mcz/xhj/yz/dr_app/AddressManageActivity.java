package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.AddressAdapter;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.AddressBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/10/13 0013.
 * 描述：收货地址管理
 */

public class AddressManageActivity extends BaseActivity implements AddressAdapter.AddressManageListener {
    @BindView(R.id.ptr_address)
    PtrClassicFrameLayout ptr_address;
    @BindView(R.id.ll_norecord)
    LinearLayout ll_norecord;
    @BindView(R.id.title_leftimageview)
    ImageView title_leftimageview;
    @BindView(R.id.title_centertextview)
    TextView title_centertextview;
    @BindView(R.id.lv_address)
    ListView lv_address;
    @BindView(R.id.bt_add_address)
    TextView bt_add_address;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private AddressAdapter addressAdapter;
    private List<AddressBean> addressList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_manage;
    }

    @Override
    protected void initParams() {
        title_centertextview.setText("地址管理");
        ptr_address.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getReceiptAddress();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv_address, header);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReceiptAddress();
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
                ptr_address.refreshComplete();
                LogUtils.i("所有的收货地址：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    JSONArray list = map.getJSONArray("list");
                    if (list.size() > 0) {
                        ll_norecord.setVisibility(View.GONE);
                        addressList = JSON.parseArray(list.toJSONString(), AddressBean.class);
                        addressAdapter = new AddressAdapter(AddressManageActivity.this, addressList);
                        addressAdapter.setOnAddressManageListener(AddressManageActivity.this);
                        lv_address.setAdapter(addressAdapter);
                    } else {
                        ll_norecord.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ptr_address.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

    @OnClick({R.id.title_leftimageview, R.id.bt_add_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.bt_add_address:
                startActivity(new Intent(AddressManageActivity.this, ReceiveAddressActivity.class));
                break;
        }
    }

    @Override
    public void onSelected(int position) {
        updateReceiptAddressDefault(position);
        addressAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEdit(int position) {
        startActivity(new Intent(AddressManageActivity.this, ReceiveAddressActivity.class).putExtra("address", addressList.get(position)).putExtra("edit", true));
    }

    @Override
    public void onDelete(int position) {
        deleteReceiptAddress(position);
        addressAdapter.notifyDataSetChanged();
    }

    /*设为默认收货地址*/
    private void updateReceiptAddressDefault(int position) {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.get()
                .url(UrlConfig.RECEIPTADDRESSDEFAULT)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("id", addressList.get(position).getId() + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                ptr_address.refreshComplete();
                LogUtils.i("设置默认地址：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    ToastMaker.showShortToast("设置默认地址成功");
                    getReceiptAddress();
                }else if ("9998".equals(obj.getString("errorCode"))) {
                    new show_Dialog_IsLogin(AddressManageActivity.this).show_Is_Login();
                    finish();
                }else if ("9999".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("系统异常");
                }

            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ptr_address.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

    /*删除地址*/
    private void deleteReceiptAddress(final int position) {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.DELETERECEIPTADDRESS)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("id", addressList.get(position).getId() + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                ptr_address.refreshComplete();
                LogUtils.i("删除收货地址：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    ToastMaker.showShortToast("删除地址成功");
                    getReceiptAddress();
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    new show_Dialog_IsLogin(AddressManageActivity.this).show_Is_Login();
                    finish();
                }else if ("9999".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("系统异常");
                }else if ("1001".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("默认收货地址不能删除");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ptr_address.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }
}
