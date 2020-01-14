package com.ekabao.oil.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.ProfitAdapter;
import com.ekabao.oil.bean.Activity;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.OkHttpEngine;
import com.ekabao.oil.http.PostParams;
import com.ekabao.oil.http.SimpleHttpCallback;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.ui.activity.InviteFriendsActivity;
import com.ekabao.oil.ui.activity.LoginActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.GsonUtil;
import com.ekabao.oil.util.LogUtils;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 活动中心
 * Created by Administrator on 2018/5/29.
 */

public class ProfitFragment extends BaseFragment {
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
    /*@BindView(R.id.title)
    android.widget.merge title;*/
    @BindView(R.id.rv_invest)
    RecyclerView rvInvest;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.refreshLayout_head)
    BezierCircleHeader refreshLayoutHead;


    Unbinder unbinder;

    List<Activity.PageBean.RowsBean> rowsList = new ArrayList<>();

    private ProfitAdapter profitAdapter;
    private SharedPreferences preferences = LocalApplication.sharereferences;

    private static boolean isFirstEnter = true;
    int pageon = 1;//当前页
    int total;  //返回总共多少个
    int totalPage; //返回总共多少页

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity;
    }

    @Override
    protected void initParams() {


        titleLeftimageview.setVisibility(View.GONE);
        titleCentertextview.setText("活动中心");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocalApplication.getInstance());
        rvInvest.setLayoutManager(linearLayoutManager);
        profitAdapter = new ProfitAdapter(rowsList, getActivity());
        rvInvest.setAdapter(profitAdapter);

        refreshLayout.setPrimaryColors(new int[]{0xffffffff, 0xffEE4845});
        // refreshLayoutHead.setPrimaryColors(new int[]{0xffffffff, 0xffEE4845});
        //refreshLayout.setNoMoreData(true); //恢复没有更多数据的原始状态

       /* if (isFirstEnter) {
            isFirstEnter = false;
            refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        }*/
        getActivitiesDoing();

        //触发自动刷新
        //refreshLayout.autoRefresh();


        profitAdapter.setOnItemClickLitener(new ProfitAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Activity.PageBean.RowsBean bean = rowsList.get(position);

                String appUrl = bean.getAppUrl();
                String value;
                if (appUrl.contains("?")) {
                    if (appUrl.substring(appUrl.indexOf("?") + 1).length() > 0) {
                        value = appUrl + "&app=true";
                    } else {
                        value = appUrl + "app=true";
                    }

                } else {
                    value = appUrl + "?app=true";
                }


                if (bean.getStatus() == 1) {

                    if (bean.getAppUrl().contains("jumpTo=3")) { //邀请好友三重礼

                        // LocalApplication.getInstance().getMainActivity().setCheckedFram(3);//返利
                        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                            //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                            startActivity(new Intent(mContext, LoginActivity.class));
                        } else {
                            startActivity(new Intent(mContext, InviteFriendsActivity.class));
                        }

                    } else {
                        mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                                .putExtra("URL", value)
                                .putExtra("TITLE", bean.getTitle())
                                .putExtra("PID", bean.getId())
                                .putExtra("BANNER", "banner")
                        );
                    }
                }

            }
        });

        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）设置是否监听列表在滚动到底部时触发加载事件
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                LogUtils.e("第一次进入触发自动刷新，演示效果");
                getActivitiesDoing();
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {

                LogUtils.e("加载更多");

                if (pageon >= totalPage) {
                    refreshLayout.finishLoadMore();
                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件

                } else {
                    getInitData();

                    refreshLayout.finishLoadMore();
                }
            }
        });


    }


    /**
     * 进行中的活动
     */
    private void getActivitiesDoing() {

        // showWaitDialog("加载中...", true, "");
        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("uid", preferences.getString("uid", ""));
        map.put("type", "1");
        map.put("pageOn", "1");
        map.put("pageSize", "10");
        map.put("version", UrlConfig.version);
        map.put("channel", "2");

        OkHttpEngine.create().setHeaders().post(UrlConfig.ACTIVITYLIST, params, new SimpleHttpCallback() {
            @Override
            public void onLogicSuccess(String data) {
                LogUtils.e(data);

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

                if (rows.size() > 0) {
                    rowsList.clear();
                    rowsList.addAll(rows);
                    profitAdapter.notifyDataSetChanged();
                }

                pageon = page.getPageOn();
                total = page.getTotal();
                totalPage = page.getTotalPage();

                if (page.getPageOn() >= totalPage) {
                    //refreshLayout.setNoMoreData(false);
                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                }
              /*  HomeInfo homeInfo = GsonUtil.parseJsonToBean(data, HomeInfo.class);

                List<HomeInfo.BannerBean> banner = homeInfo.getBanner();

                if (banner.size() > 0) {
                    lsad.clear();
                    lsad.addAll(banner);
                    adapter.notifyDataSetChanged();
                }*/


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
                refreshLayout.finishRefresh();
            }
        });


        /*OkHttpUtils.post()
                .url(UrlConfig.ACTIVITYLIST)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("status", "1")
                .addParams("type", "1")
                .addParams("pageOn", "1")
                .addParams("pageSize", "200")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                LogUtils.i("--->进行中的活动：" + result);
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONObject objinfo = objmap.getJSONObject("Page");
                    JSONArray objrows = objinfo.getJSONArray("rows");
                    List<FriendBean> friendBeans = JSON.parseArray(objrows.toJSONString(), FriendBean.class);
                    //lslbs = friendBeans;

                    if (friendBeans.size() > 0) {

                        lslbs.clear();
                        lslbs.add(friendBeans.get(friendBeans.size() - 1));

                        lslbs.addAll(friendBeans);
                        lslbs.add(friendBeans.get(0));

                        LogUtils.e("lslbs" + lslbs.size());
                        ll_activities.setVisibility(View.VISIBLE);

                        coverFlowAdapter.notifyDataSetChanged();

                        pagerContainer.setListsize(lslbs.size());

                        //设置当前viewpager要显示第几个条目 显示在中间
                        //int item = Integer.MAX_VALUE /2 - (Integer.MAX_VALUE /2 % lslbs.size());
                        int item = 30;
                        vpOverlap.setCurrentItem(1);


                        if (adapter_activity == null) {
                            //adapter_activity = new FriendAdapter(mContext, lslbs);


                            adapter_custom_activity = new CustomActivityAdapter(mContext, lslbs);
                            lv_activities_doing.setAdapter(adapter_custom_activity);
                        } else {
                            //adapter_activity.onDateChange(lslbs);
                        }
                    } else {
                        ll_activities.setVisibility(View.GONE);
                    }
                } else if ("9998".equals(obj.getString("errorCode"))) {

                } else {
                    ToastMaker.showShortToast("服务器异常");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                LogUtils.i("--->进行中的活动：获取失败！");
                ToastMaker.showShortToast("请检查网络");
            }
        });*/

    }

    /**
     *
     * 加载更多
     */
    private void getInitData() {
        LogUtils.e( "getInitData()加载更多");

        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("uid", preferences.getString("uid", ""));
        map.put("type", "1");
        map.put("pageOn", (++pageon )+"");
        map.put("pageSize", "10");
        map.put("version", UrlConfig.version);
        map.put("channel", "2");


        OkHttpEngine.create().setHeaders().post(UrlConfig.ACTIVITYLIST, params, new SimpleHttpCallback() {
            @Override
            public void onLogicSuccess(String data) {
                LogUtils.e(data);
                refreshLayout.finishLoadMore();

                //dismissDialog();
                Activity activity = GsonUtil.parseJsonToBean(data, Activity.class);

                Activity.PageBean page = activity.getPage();
                List<Activity.PageBean.RowsBean> rows = page.getRows();

                if (rows.size() > 0) {
                    profitAdapter.loadMore(rows);
                    refreshLayout.finishLoadMore();
                }

                pageon = page.getPageOn();
                total = page.getTotal();
                totalPage = page.getTotalPage();

                if (page.getPageOn() >= totalPage) {
                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                    //refreshLayout.setNoMoreData(false);
                    ToastMaker.showShortToast("数据全部加载完毕");
                }
            }

            @Override
            public void onLogicError(int code, String msg) {
                LogUtils.e("LF--->HomeFragment--->homeinfo：" + msg);
                refreshLayout.finishLoadMore();
                //dismissDialog();
            }

            @Override
            public void onError(IOException e) {
                LogUtils.e("LF--->HomeFragment--->homeinfo：" + e.toString());
                refreshLayout.finishLoadMore();
                //dismissDialog();
            }
        });


    }

    public static ProfitFragment instance() {
        ProfitFragment view = new ProfitFragment();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
