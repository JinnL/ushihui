package com.ekabao.oil.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.PlatformActAdapter;
import com.ekabao.oil.bean.Activity;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.OkHttpEngine;
import com.ekabao.oil.http.PostParams;
import com.ekabao.oil.http.SimpleHttpCallback;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.MallActivity;
import com.ekabao.oil.ui.activity.NoticeActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.activity.find.AtyLocationOil;
import com.ekabao.oil.ui.activity.find.AtyOilCity;
import com.ekabao.oil.ui.view.MarqueeView;
import com.ekabao.oil.util.GsonUtil;
import com.ekabao.oil.util.LogUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * ${APP_NAME}  App_akzj
 *
 * @time 2019/3/19 14:18
 * Created by lj on 2019/3/19 14:18.
 */

public class DiscoverFragment extends BaseFragment {

    @BindView(R.id.fillStatusBarView)
    View fillStatusBarView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.view_title)
    View viewTitle;
    @BindView(R.id.iv_safe)
    ImageView ivSafe;
    @BindView(R.id.tv_oil_price)
    TextView tvOilPrice;
    @BindView(R.id.cl_oil_price)
    ConstraintLayout clOilPrice;
    @BindView(R.id.tv_oil_station)
    TextView tvOilStation;
    @BindView(R.id.cl_oil_station)
    ConstraintLayout clOilStation;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.cl_trade_info)
    ConstraintLayout clTradeInfo;
    @BindView(R.id.iv_invite)
    ImageView ivInvite;
    @BindView(R.id.tv_info_title)
    TextView tvInfoTitle;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.iv_info_more)
    ImageView ivInfoMore;
    @BindView(R.id.cl_info)
    ConstraintLayout clInfo;
    @BindView(R.id.tv_mall_entrance)
    TextView tvMallEntrance;
    @BindView(R.id.iv_mall)
    ImageView ivMall;
    @BindView(R.id.tv_activity)
    TextView tvActivity;
    @BindView(R.id.rcv_activity)
    RecyclerView rcvActivity;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private Unbinder unbinder;


    private SharedPreferences preferences = LocalApplication.sharereferences;
    int pageOn = 1;//当前页
    private List<Activity.PageBean.RowsBean> activity_List = new ArrayList<Activity.PageBean.RowsBean>();// 每次加载的数据
    private PlatformActAdapter adapter;

    public static DiscoverFragment newInstance() {
        Bundle args = new Bundle();
        DiscoverFragment fragment = new DiscoverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initParams() {
        pageOn = 1;
        adapter = new PlatformActAdapter(getContext(), activity_List);
        rcvActivity.setLayoutManager(new LinearLayoutManager(mContext));
        rcvActivity.setAdapter(adapter);
        rcvActivity.setNestedScrollingEnabled(false);
        getActivitiesDoing(false);
        initInfo();
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageOn++;
                getActivitiesDoing(true);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageOn = 1;
                refreshLayout.setNoMoreData(false);
                getActivitiesDoing(false);
            }
        });

    }

    /**
     * 安全保障
     */
    @OnClick(R.id.iv_safe)
    void ivSafeClick() {
        startActivity(new Intent(mContext, WebViewActivity.class)
                .putExtra("URL", "https://m.ekabao.cn/activitycenter?upgrade=1&app=true")
                .putExtra("TITLE", "安全保障")
                .putExtra("BANNER", "banner")
        );
    }

    /**
     * 今日油价
     */
    @OnClick(R.id.cl_oil_price)
    void clOilPriceClick() {
        mContext.startActivity(new Intent(mContext, AtyOilCity.class)
                .putExtra("city", "浙江")
        );

    }

    /**
     * 附近油站
     */
    @OnClick(R.id.cl_oil_station)
    void clOilStationClick() {
        startActivity(new Intent(mContext, AtyLocationOil.class));
    }


    /**
     * 行业资讯
     */
    @OnClick(R.id.cl_trade_info)
    void clTradeInfoClick() {
        startActivity(new Intent(mContext, NoticeActivity.class)
                .putExtra("activity", 4));
    }

    /**
     * 最新资讯
     */
    private void initInfo() {
        OkHttpUtils
                .post()
                .url(UrlConfig.DISCOVER_INFO)
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("--->INFO " + response);
                        dismissDialog();
                        Log.d("DiscoverFragment", response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            dismissDialog();
                            JSONObject objmap = obj.getJSONObject("map");
                            if (objmap.isEmpty()) {
                                return;
                            }
                            //JSONArray objnotice = objmap.getJSONArray("urgentNotice");
                            // objmap.getJSONObject("page").getJSONArray("urgentNotice")
                            JSONArray objnotice = objmap.getJSONArray("list");

                            if (objnotice != null) {
                                //  rlNotice.setVisibility(View.VISIBLE);

                                List<String> lsInfo = JSON.parseArray(objnotice.toJSONString(), String.class);

                                marqueeView.startWithList(lsInfo);

                            } else {
                                // rlNotice.setVisibility(View.GONE);
                            }
                        } else {
                            //rlNotice.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
//				ToastMaker.showShortToast("请检查网络!");
                    }
                });
    }

    /**
     * 邀请好友
     */
    @OnClick(R.id.iv_invite)
    void ivInviteClick() {
        startActivity(new Intent(mContext, WebViewActivity.class)
                .putExtra("URL", "https://m.ekabao.cn/invitation?upgrade=1&app=true")
                .putExtra("TITLE", "邀请好友")
                // .putExtra("PID", pid)
                .putExtra("BANNER", "banner")
        );
    }

    /**
     * 易卡宝商城
     */
    @OnClick(R.id.iv_mall)
    void ivMallClick() {
        startActivity(new Intent(mContext, MallActivity.class));
    }

    /**
     * 获取进行中的活动
     */
    private void getActivitiesDoing(final boolean isLoad) {

        // showWaitDialog("加载中...", true, "");
        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("uid", preferences.getString("uid", ""));
        // map.put("type", "1");
        map.put("pageOn", pageOn + "");
        map.put("pageSize", "10");
        map.put("version", UrlConfig.version);
        map.put("channel", "2");

        OkHttpEngine.create().setHeaders().post(UrlConfig.ACTIVITYLIST, params, new SimpleHttpCallback() {
            @Override
            public void onLogicSuccess(String data) {
                LogUtils.e("进行中的活动" + data);

                if (refreshLayout != null) {
                    RefreshState state = refreshLayout.getState();
                    if (state == RefreshState.Refreshing) {
                        refreshLayout.finishRefresh();
                    }
                }

                //dismissDialog();
                Activity activity = GsonUtil.parseJsonToBean(data, Activity.class);

                Activity.PageBean page = activity.getPage();
                List<Activity.PageBean.RowsBean> rows = page.getRows();

                if (rows.size() == 0) {
                    LogUtils.e(rows.size() + "加载完" + activity_List.size());
                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                }
                if (isLoad) {
                    activity_List.addAll(rows);
                } else {
                    activity_List = rows;
                }


                adapter.setList(activity_List);

//                homepagerRecycleAdapter.setRefreshBean(activity_List, flagFirst);

            }

            @Override
            public void onLogicError(int code, String msg) {
                LogUtils.e("LF--->HomeFragment--->homeinfo：" + msg);
                if (refreshLayout != null) {
                    RefreshState state = refreshLayout.getState();
                    if (state == RefreshState.Refreshing) {
                        refreshLayout.finishRefresh();
                    }
                }
            }

            @Override
            public void onError(IOException e) {
                LogUtils.e("LF--->HomeFragment--->homeinfo：" + e.toString());
                if (refreshLayout != null) {
                    RefreshState state = refreshLayout.getState();
                    if (state == RefreshState.Refreshing) {
                        refreshLayout.finishRefresh();
                    }
                }
            }
        });

    }

    private void initRecyclerView() {


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
