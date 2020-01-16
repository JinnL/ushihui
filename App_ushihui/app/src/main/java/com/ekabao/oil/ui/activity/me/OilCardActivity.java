package com.ekabao.oil.ui.activity.me;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.BaseRecyclerViewAdapter;
import com.ekabao.oil.adapter.OilCardAdapter;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.OilCardBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class OilCardActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.rv_news)
    RecyclerView rvNews;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;


    /**
     * 我的油卡
     *
     * @time 2018/11/8 19:53
     * Created by lj
     */
    private SharedPreferences preferences;
    private String uid;
    private OilCardAdapter oilCardAdapter;
    private ArrayList<OilCardBean> oillist = new ArrayList<>();
    private static final int Details = 16542; //删除
    private static final int Add = 16552; //删除

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_oil_card);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_oil_card;
    }

    @Override
    protected void initParams() {
        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");


        titleLeftimageview.setOnClickListener(this);
        titleRightimageview.setImageResource(R.drawable.icon_myoil_add);
        titleRightimageview.setVisibility(View.VISIBLE);
        titleRightimageview.setOnClickListener(this);
        titleCentertextview.setText("我的油卡");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocalApplication.getInstance());
        rvNews.setLayoutManager(linearLayoutManager);
        oilCardAdapter = new OilCardAdapter(rvNews, oillist, R.layout.item_my_oilcard);
        rvNews.setAdapter(oilCardAdapter);

        oilCardAdapter.setonItemViewClickListener(new BaseRecyclerViewAdapter.OnViewClickListener() {
            @Override
            public void onItemViewClick(View view, int position) {
                // ToastUtil.showToast("确定删除?");
                final int position1 = position;

                DialogMaker.showRedSureDialog(OilCardActivity.this, "删除",
                        "确定要删除这张卡片吗？", "取消", "确定", new DialogMaker.DialogCallBack() {
                            @Override
                            public void onButtonClicked(Dialog dialog, int position, Object tag) {
                                // exit_dr();
                                deleteOilCard(oillist.get(position1).getId());
                            }

                            @Override
                            public void onCancelDialog(Dialog dialog, Object tag) {

                            }
                        }, "");
            }

            @Override
            public void onItemViewClick(BaseViewHolder view, int position) {

                startActivityForResult(new Intent(OilCardActivity.this, OilCardDetailsActivity.class)
                        .putExtra("uid", uid)
                        .putExtra("fuelCardId", oillist.get(position).getId()), Details);
            }

        });

        getOilCard();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Details && resultCode == RESULT_OK) {
            getOilCard();
        }
        if (requestCode == Add && resultCode == RESULT_OK) {
            getOilCard();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.title_rightimageview: //击进入充值问题页面

                startActivityForResult(new Intent(this, AddOilCardActivity.class)
                        .putExtra("uid", uid), Add);
                break;
        }
    }

    /**
     * 7.删除油卡
     */
    private void deleteOilCard(int fuelCardId) {
        LogUtils.e("油卡列表 我的油卡" + fuelCardId);
        // TODO: 2018/11/8  preferences.getString("uid", "")
        OkHttpUtils.post().url(UrlConfig.deleteFuelCard)
                // .addParams("uid", "5")
                .addParams("fuelcardId", fuelCardId + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("--->油卡列表删除油卡：" + response);

                        dismissDialog();

                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            setResult(RESULT_OK);
                            // finish();
                            oillist.clear();
                            getOilCard();

                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("油卡不存在");
                        } else if ("1002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("该油卡有未完成的充值计划");
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

    /**
     * 我的油卡
     */
    private void getOilCard() {
        LogUtils.e("我的油卡" + uid);
        // TODO: 2018/11/8  preferences.getString("uid", "")
        OkHttpUtils.post().url(UrlConfig.myFuelCard)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("--->我的油卡：" + response);
                        dismissDialog();

                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray arr = map.getJSONArray("myFuelCardList");
                            // LogUtils.e("我的油卡" + arr.getString(0));
                           /* if (arr.isEmpty()) {
                                return;
                            }*/

                            List<OilCardBean> oilCardBeans = JSON.parseArray(arr.toJSONString(), OilCardBean.class);

                            LogUtils.e("我的油卡" + oilCardBeans.size());

                            if (oilCardBeans.size() > 0) {

                                oillist.clear();

                                //oilCardBean = oillist.get(0);
                                oilCardAdapter.notifyDataSetChanged();


                                llEmpty.setVisibility(View.GONE);
                                rvNews.setVisibility(View.VISIBLE);
                                // meEtouRechargeAdapter.notifyDataSetChanged();
                            } else {
                                llEmpty.setVisibility(View.VISIBLE);
                                rvNews.setVisibility(View.GONE);
                                // tv_footer.setText("全部加载完毕");
                                // footerlayout.setVisibility(View.VISIBLE);
                                // pb.setVisibility(View.GONE);
                            }

                            oillist.addAll(oilCardBeans);

                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
//                            new show_Dialog_IsLogin(MessageCenterActivity.this).show_Is_Login();
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
