package com.ekabao.oil.ui.activity.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.BankLimitAdapter;
import com.ekabao.oil.bean.BankNameBean;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 易卡宝  App
 *
 * @time 2018/7/18 16:35
 * Created by lj on 2018/7/18 16:35.
 */

public class BankLimitActivity extends BaseActivity {
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
    @BindView(R.id.rv_bank)
    RecyclerView rvBank;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bank_limit;
    }

    private List<BankNameBean> rows_List = new ArrayList<BankNameBean>();// 每次加载的数据
    private BankLimitAdapter newsAdapter;

    @Override
    protected void initParams() {
        titleCentertextview.setText("限额列表");
        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        newsAdapter = new BankLimitAdapter(rvBank, rows_List, R.layout.item_bank_limit);
        rvBank.setLayoutManager(new LinearLayoutManager(this));
        rvBank.setAdapter(newsAdapter);
        getBankData();
    }

    private void getBankData() {
        showWaitDialog("请稍后...", false, "");
        OkHttpUtils.post()
                .url(UrlConfig.BANKNAMELIST)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                dialog.dismiss();
                LogUtils.e(result);
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONArray objrows = objmap.getJSONArray("bankQuotaList");
                    List<BankNameBean> bankNameBeans = JSON.parseArray(objrows.toJSONString(), BankNameBean.class);

                    if (bankNameBeans.size() > 0) {

                        rows_List.clear();
                        rows_List.addAll(bankNameBeans);
                        newsAdapter.notifyDataSetChanged();

                    }
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    finish();
//					new show_Dialog_IsLogin(getActivity()).show_Is_Login() ;
                } else {
                    ToastMaker.showShortToast("服务器异常");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dialog.dismiss();
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
