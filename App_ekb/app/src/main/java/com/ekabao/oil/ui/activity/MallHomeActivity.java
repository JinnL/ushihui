package com.ekabao.oil.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.MallRecycleAdapter;
import com.ekabao.oil.bean.GoodsCategory;
import com.ekabao.oil.bean.GoodsList;
import com.ekabao.oil.bean.GoodsMiddlebanner;
import com.ekabao.oil.bean.GoodsNewList;
import com.ekabao.oil.bean.HomeBannerBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.me.CallCenterActivity;
import com.ekabao.oil.ui.activity.me.MallOrderActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class MallHomeActivity extends BaseActivity {

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
    @BindView(R.id.rv_home)
    RecyclerView rvHome;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.fillStatusBarView)
    View fillStatusBarView;

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_mall_home);
    }*/


    private SharedPreferences preferences = LocalApplication.sharereferences;


    int pageon = 1;//当前页
    int total;  //返回总共多少个
    int totalPage; //返回总共多少页
    private List<GoodsList> rows_List = new ArrayList<GoodsList>();// 每次加载的数据


    private MallRecycleAdapter homepagerRecycleAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initParams() {

        gethotList();

        fillStatusBarView.setVisibility(View.GONE);

        titleCentertextview.setText("易商城");
        titleRightimageview.setVisibility(View.VISIBLE);
        titleRightimageview.setImageResource(R.drawable.icon_mall_top_more);

        homepagerRecycleAdapter = new MallRecycleAdapter(this, 1);

        rvHome.setAdapter(homepagerRecycleAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocalApplication.getInstance());
        rvHome.setLayoutManager(linearLayoutManager);

        refreshLayout.setPrimaryColors(new int[]{0xffF4F4F4, 0xffEE4845});
        //开启自动加载功能（非必须）设置是否监听列表在滚动到底部时触发加载事件
        refreshLayout.setEnableAutoLoadMore(true);
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                pageon = 1;
                LogUtils.e("pageon+" + pageon + "totalPage" + totalPage);
                gethotList();
                //是否有更多数据
                //  refreshLayout.setNoMoreData(false);
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
/*
                if (pageon >= totalPage) {

                    // refreshLayout.finishLoadMore();
                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件

                } else { }*/
                //getInitData();
                gethotList();
                refreshLayout.finishLoadMore();

                LogUtils.e("pageon+" + pageon + "totalPage" + totalPage);

            }
        });


        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleRightimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopupWindowLogin(titleRightimageview);
            }
        });


    }

    private PopupWindow popupWindow;
    private View layout;

    public void showPopupWindowLogin(View titleRightimageview) {


        if (popupWindow == null) {

            // 加载布局
            layout = LayoutInflater.from(this).inflate(R.layout.pop_mall_home, null);
            // 找到布局的控件
            // 实例化popupWindow
            popupWindow = new PopupWindow(layout, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);


            // 控制键盘是否可以获得焦点
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return false;
                }
            });

            // 设置popupWindow弹出窗体的背景
            //setBackgroundAlpha(0.4f);//设置屏幕透明度
            // 监听
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    // popupWindow隐藏时恢复屏幕正常透明度
                    // setBackgroundAlpha(1.0f);

                }
            });
            TextView tvOrder = (TextView) layout.findViewById(R.id.tv_order);
            tvOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                        //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                        //startActivity(new Intent(mContext, LoginActivity.class));
                        startActivity(new Intent(MallHomeActivity.this, LoginActivity.class));
                    } else {
                        startActivity(new Intent(MallHomeActivity.this, MallOrderActivity.class)
                                .putExtra("type", 2)
                        );
                    }


                }
            });
            TextView tv_about = (TextView) layout.findViewById(R.id.tv_about);

            tv_about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2019/1/14
                    startActivity(new Intent(MallHomeActivity.this, CallCenterActivity.class));
                  /*  startActivity(new Intent(MallHomeActivity.this, WebViewActivity.class)
                            //.putExtra("URL", bean.getLocation() + "&app=true")

                            .putExtra("TITLE", "商城百科")
                            //  .putExtra("PID", pid)
                            .putExtra("BANNER", "banner")
                    );*/
                }
            });


        }

        // popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        popupWindow.showAsDropDown(titleRightimageview);


    }

    /**
     * /首页的 下面的商城
     * *
     */
    private void gethotList() {

        //showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.shopIndex)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("page", pageon + "")
                .addParams("rows", "10")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {

                        LogUtils.e("易商城--->" + response);
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

                            //JSONObject page = map.getJSONObject("page");
                            //   totalPage =  map.getJSONObject("goodslist").getInteger("totalPage");


                            if (map.getJSONArray("category") != null) {

                                JSONArray arr = map.getJSONArray("category");
                                List<GoodsCategory> categoryList = JSON.parseArray(arr.toJSONString(), GoodsCategory.class);
                                //  homepagerRecycleAdapter.setCenterBean(homeHostProduct);
                                homepagerRecycleAdapter.setCategoryBean((ArrayList<GoodsCategory>) categoryList);

                            }


                            if (map.getJSONArray("good") != null) {
                                JSONArray arr = map.getJSONArray("good");
                                List<GoodsList> goodslist = JSON.parseArray(arr.toJSONString(), GoodsList.class);
                                boolean flagFirst = true;

                                if (pageon == 1) {
                                    LogUtils.e("下拉刷新");
                                    flagFirst = true;

                                } else {
                                    flagFirst = false;
                                }
                                /*if (pageon == 1) {


                                }*/
                                rows_List.clear();
                                if (goodslist.size() < 10) {

                                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件

                                } else {
                                    pageon++;
                                }

                                rows_List.addAll(goodslist);
                                //  homepagerRecycleAdapter.setCenterBean(homeHostProduct);
                                homepagerRecycleAdapter.setRefreshBean(rows_List, flagFirst);

                                refreshLayout.finishLoadMore();
                            }
                            if (map.getJSONArray("bannerMiddle") != null) {

                                JSONArray arr = map.getJSONArray("bannerMiddle");
                                LogUtils.e("易商城--->bannerMiddle" + arr.toJSONString());

                                List<GoodsMiddlebanner> middlebanner = JSON.parseArray(arr.toJSONString(), GoodsMiddlebanner.class);
                                //  homepagerRecycleAdapter.setCenterBean(homeHostProduct);
                                homepagerRecycleAdapter.setMiddlebanner(middlebanner);

                            }

                            if (map.getJSONArray("banner") != null) {
                                JSONArray arr = map.getJSONArray("banner");
                                List<HomeBannerBean> homeHostProduct = JSON.parseArray(arr.toJSONString(), HomeBannerBean.class);

                                homepagerRecycleAdapter.setheaderbean(homeHostProduct);

                                LogUtils.e("易商城banner" + homeHostProduct.size());
                            }

                            if (map.getJSONArray("newgood") != null) {
                                JSONArray arr = map.getJSONArray("newgood");
                                // List<HomeBannerBean> homeHostProduct = JSON.parseArray(arr.toJSONString(), HomeBannerBean.class);
                                List<GoodsNewList> goodslist = JSON.parseArray(arr.toJSONString(), GoodsNewList.class);
                                homepagerRecycleAdapter.setNewGoods(goodslist);

                                LogUtils.e("易商城newgood" + goodslist.size());
                            }

                           /* homepagerRecycleAdapter.setheaderbean(homeInfo);
                            homepagerRecycleAdapter.setCenterBean(homeInfo);

*/
                        /*    List<HomeInfoList.BannerBean> banner = homeInfo.getBanner();

                            if (banner.size() > 0) {
                                lsad.clear();
                                lsad.addAll(banner);
                                adapter.notifyDataSetChanged();
                            }*/


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
