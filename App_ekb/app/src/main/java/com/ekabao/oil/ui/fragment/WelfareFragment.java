package com.ekabao.oil.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.RollViewAdapter;
import com.ekabao.oil.adapter.WelfareListAdapter;
import com.ekabao.oil.bean.Activity;
import com.ekabao.oil.bean.HomeBannerBean;
import com.ekabao.oil.bean.HomeInfoList;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.OkHttpEngine;
import com.ekabao.oil.http.PostParams;
import com.ekabao.oil.http.SimpleHttpCallback;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.InviteFriendsActivity;
import com.ekabao.oil.ui.activity.LoginActivity;
import com.ekabao.oil.ui.activity.MallHomeActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.view.ListInScroll;
import com.ekabao.oil.ui.view.MarqueeView;
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
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * $desc$
 * 主界面 福利
 *
 * @time $data$ $time$
 * Created by Administrator on 2018/7/13.
 */

public class WelfareFragment extends BaseFragment {


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
    @BindView(R.id.refreshLayout_head)
    BezierCircleHeader refreshLayoutHead;
    @BindView(R.id.rpv_banner)
    RollPagerView rpvBanner;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.rl_notice)
    LinearLayout rlNotice;
    @BindView(R.id.ib_welfare_mall)
    ImageButton ibWelfareMall;
    @BindView(R.id.ib_welfare_newpeople)
    ImageButton ibWelfareNewpeople;
    @BindView(R.id.ib_welfare_invest)
    ImageButton ibWelfareInvest;
    @BindView(R.id.lv_activity)
    ListInScroll lvActivity;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;

    private int type = 1;//
    private SharedPreferences preferences;
    private String uid;
    private List<Activity.PageBean.RowsBean> rows_List = new ArrayList<Activity.PageBean.RowsBean>();// 每次加载的数据
    private WelfareListAdapter newsAdapter;

    int pageon = 1;//当前页
    int total;  //返回总共多少个
    int totalPage; //返回总共多少页


    List<HomeBannerBean> lsad = new ArrayList<HomeBannerBean>();
    private RollViewAdapter adapter;

    public static WelfareFragment newInstance() {

        //  Bundle args = new Bundle();
        WelfareFragment fragment = new WelfareFragment();
        //  args.putInt("type", type);
        //  fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            //  type = args.getInt("type");
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_welfare;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("福利");
        titleLeftimageview.setVisibility(View.GONE);
        refreshLayout.setPrimaryColors(new int[]{0xffF4F4F4, 0xffEE4845});


        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");
        pageon = 1;
        //getData();
        //LogUtils.e("个人消息记录："+type);
        getActivitiesDoing();
        getHomeInfo();

        newsAdapter = new WelfareListAdapter(getActivity(), rows_List);

        lvActivity.setAdapter(newsAdapter);



       /* newsAdapter.setonItemViewClickListener(new BaseRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onItemViewClick(BaseViewHolder view, int position) {
                LogUtils.e("--->平台通知：onItemViewClick");
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL", UrlConfig.WEBSITEAN + "?id="
                                + rows_List.get(position).getId())
                        .putExtra("TITLE", "个人消息"));
            }
        });*/

        //refreshLayout.autoRefresh();
        //开启自动加载功能（非必须）设置是否监听列表在滚动到底部时触发加载事件
        refreshLayout.setEnableAutoLoadMore(true);
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                pageon = 1;
                LogUtils.e("pageon+" + pageon + "totalPage" + totalPage);
                getActivitiesDoing();
                getHomeInfo();
                //是否有更多数据
                refreshLayout.setNoMoreData(false);
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {

                if (pageon > totalPage) {

                    // refreshLayout.finishLoadMore();
                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件

                } else {
                    //getInitData();
                    getInitData();

                    refreshLayout.finishLoadMore();
                    LogUtils.e("pageon+" + pageon + "totalPage" + totalPage);
                }
            }
        });
        lvActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Activity.PageBean.RowsBean bean = rows_List.get(position);

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


        //设置播放时间间隔
        rpvBanner.setPlayDelay(5000);
        //设置透明度
        rpvBanner.setAnimationDurtion(500);
        //设置圆点指示器颜色
        rpvBanner.setHintView(new ColorPointHintView(mContext, 0xFFFF0000, Color.WHITE));
        //设置适配器
        adapter = new RollViewAdapter(rpvBanner,mContext, lsad);
        rpvBanner.setAdapter(adapter);
        //轮播图点击
        rpvBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                HomeBannerBean bean = lsad.get(position);
                if (bean.getLocation() == null || bean.getLocation().equalsIgnoreCase("")) {
                    return;
                }
                String location = bean.getLocation();
                String url;
                if (location.contains("?")){
                    url=location+"&app=true";
                }else {
                    url=location+"?app=true";
                }

                if (bean.getTitle().indexOf("注册送礼") != -1) {
                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("URL", url)
                            .putExtra("TITLE", bean.getTitle())
                            .putExtra("HTM", "立即注册")
                            // .putExtra("PID", pid)
                            .putExtra("BANNER", "banner")
                    );
                } else {
                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("URL", url)
                            .putExtra("TITLE", bean.getTitle())
                            //  .putExtra("PID", pid)
                            .putExtra("BANNER", "banner")
                    );
                }
            }
        });
    }

    @OnClick({R.id.ib_welfare_mall, R.id.ib_welfare_newpeople, R.id.ib_welfare_invest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_welfare_mall:
                mContext.startActivity(new Intent(mContext, MallHomeActivity.class)
                        //  .putExtra("is_presell_plan", true)
                        // .putExtra("startDate", isSellingList.get(0).getStartDate())
                        //.putExtra("pid", bean1.getId())
                        .putExtra("money", 1000));
                break;
            case R.id.ib_welfare_newpeople:
                mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra("URL", "https://m.youmochou.com/oilCardWelfare?upgrade=1&app=true")
                        .putExtra("TITLE", "新人专享")
                       // .putExtra("PID", pid)
                        .putExtra("BANNER", "banner")
                );
                break;
            case R.id.ib_welfare_invest:
                mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra("URL", "https://m.youmochou.com/invitation?upgrade=1&app=true")
                        .putExtra("TITLE", "邀请好友")
                        // .putExtra("PID", pid)
                        .putExtra("BANNER", "banner")
                );
                break;
        }
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
                LogUtils.e("进行中的活动"+data);

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
                    rows_List.clear();
                    rows_List.addAll(rows);
                    newsAdapter.notifyDataSetChanged();
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
                if (refreshLayout != null) {
                    RefreshState state = refreshLayout.getState();
                    if (state == RefreshState.Refreshing) {
                        refreshLayout.finishRefresh();
                    }
                }
            }
        });


      /*  OkHttpUtils.post()
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
                LogUtils.e("--->进行中的活动：" + result);
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONObject objinfo = objmap.getJSONObject("Page");
                    JSONArray objrows = objinfo.getJSONArray("rows");
                    List<FriendBean> friendBeans = JSON.parseArray(objrows.toJSONString(), FriendBean.class);
                    //lslbs = friendBeans;

                  *//*  if (friendBeans.size() > 0) {

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
                    }*//*
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
    private void getHomeInfo() {
        //showWaitDialog("加载中...", true, "");

        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("uid", preferences.getString("uid", ""));
        map.put("toFrom", LocalApplication.getInstance().channelName);
        map.put("version", UrlConfig.version);
        map.put("channel", "2");


        OkHttpUtils.post().url(UrlConfig.welfareBanner)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("toFrom", LocalApplication.getInstance().channelName)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("福利首页轮播图" + response);

                        dismissDialog();

                        if (refreshLayout != null) {
                            RefreshState state = refreshLayout.getState();
                            if (state == RefreshState.Refreshing) {
                                refreshLayout.finishRefresh();
                            }
                        }

                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");


                            HomeInfoList homeInfo = JSON.toJavaObject(map, HomeInfoList.class);

                            //HomeInfo homeInfo = GsonUtil.parseJsonToBean(data, HomeInfo.class);
                            //  HomeInfoList homeInfo = GsonUtil.parseJsonToBean(map, HomeInfoList.class);

                            List<HomeBannerBean> banner = homeInfo.getBanner();

                            if (banner.size() > 0) {
                                lsad.clear();
                                lsad.addAll(banner);
                                adapter.notifyDataSetChanged();
                            }

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

                        RefreshState state = refreshLayout.getState();
                        if (state == RefreshState.Refreshing) {
                            refreshLayout.finishRefresh();
                        }
                    }
                });


    }
    /**
     * 加载更多
     */
    private void getInitData() {
        LogUtils.e("getInitData()加载更多");

        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("uid", preferences.getString("uid", ""));
        map.put("type", "1");
        map.put("pageOn", (++pageon) + "");
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
                    //  newsAdapter.loadMore(rows);
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // pageon=1;

        //unbinder.unbind();
        unbinder.unbind();
    }


}
