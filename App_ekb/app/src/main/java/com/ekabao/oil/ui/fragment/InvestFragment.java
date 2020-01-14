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
import com.ekabao.oil.adapter.InvestAdapter;
import com.ekabao.oil.bean.Invest;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.OkHttpEngine;
import com.ekabao.oil.http.PostParams;
import com.ekabao.oil.http.SimpleHttpCallback;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.ui.activity.HistoryProjectActivity;
import com.ekabao.oil.ui.activity.ProductActivity;
import com.ekabao.oil.util.GsonUtil;
import com.ekabao.oil.util.LogUtils;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/***
 * 
 * author: tonglj
 * date:2019/12/31
 */
public class InvestFragment extends BaseFragment {
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
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout_head)
    BezierCircleHeader refreshLayoutHead;

    private String type = "2";
    private int pageanxuan = 1; //?
    private int pageconfig = 1;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    List<Invest.IsSellingListBean> sellingList = new ArrayList<>();
    private InvestAdapter investAdapter;

    private static boolean isFirstEnter = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invest;
    }

    @Override
    protected void initParams() {
        LogUtils.e("initParams出借");

        titleLeftimageview.setVisibility(View.GONE);
        titleCentertextview.setText("出借");

        rvInvest.setHasFixedSize(true);
        rvInvest.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocalApplication.getInstance());
        rvInvest.setLayoutManager(linearLayoutManager);
        investAdapter = new InvestAdapter(sellingList, LocalApplication.getInstance());
        rvInvest.setAdapter(investAdapter);

        refreshLayout.setPrimaryColors(new int[]{0xffffffff, 0xffEE4845});
        getData(1, type);
     /*   if (isFirstEnter) {
            isFirstEnter = false;
            refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        } else {
            getData(1, type);
        }*/
        refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）设置是否监听列表在滚动到底部时触发加载事件
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                //LogUtils.e("第一次进入触发自动刷新，演示效果");
                // getHomeInfo();
                getData(1, type);
            }
        });


        investAdapter.setOnItemClickLitener(new InvestAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(mContext, ProductActivity.class)
                        .putExtra("is_presell_plan", true)
                        .putExtra("startDate", sellingList.get(position).getStartDate())
                        .putExtra("pid", sellingList.get(position).getId())
                        .putExtra("ptype", "2"));
            }

            @Override
            public void onHistoryItemClick(View view) {
                startActivity(new Intent(mContext, HistoryProjectActivity.class));
            }
        });

        tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, HistoryProjectActivity.class));
            }
        });
    }

    private void getData(int page, String type) {
        pageconfig = page; //?
        pageanxuan = page;
        //showWaitDialog("加载中...", true, "");

        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();

        map.put("type", type);
        map.put("status", "5");
        map.put("pageSize", "10");
        map.put("uid", preferences.getString("uid", ""));
        map.put("pageOn", pageconfig + "");
        //map.put("version", UrlConfig.version);
        //map.put("channel", "2");

        OkHttpEngine.create().setHeaders().post(UrlConfig.INVESTINFO, params, new SimpleHttpCallback() {
            @Override
            public void onLogicSuccess(String data) {
                //dismissDialog();
                LogUtils.e(data);

                if (refreshLayout != null) {
                    RefreshState state = refreshLayout.getState();
                    if (state == RefreshState.Refreshing) {
                        refreshLayout.finishRefresh();
                    }
                }

                Invest invest = GsonUtil.parseJsonToBean(data, Invest.class);
                List<Invest.IsSellingListBean> isSellingList = invest.getIsSellingList();
                List<Invest.NewHandBean> newHand = invest.getNewHand();
                List<Invest.WillSellListBean> willSellList = invest.getWillSellList();
                if (isSellingList.size() > 0) {
                    sellingList.clear();
                    sellingList.addAll(isSellingList);
                    investAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLogicError(int code, String msg) {
                // dismissDialog();
                refreshLayout.finishRefresh();
                LogUtils.e("onLogicError 获取失败！" + code + msg);
            }

            @Override
            public void onError(IOException e) {
                // dismissDialog();
                refreshLayout.finishRefresh();
                LogUtils.e("获取失败！" + e.toString());
            }
        });





     /*   //这里type改为固定的，原来是type，根据需求
        OkHttpUtils.post().url(UrlConfig.INVESTINFO)
                .addParams("type", type)
                .addParams("status", "5")
                .addParams("pageSize", "100")
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("pageOn", pageconfig + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String result) {
                        //LogPrintUtil.e("LF--->InvestFragment产品列表", result);
                        dismissDialog();
                        if (pageanxuan == 1 || pageconfig == 1) {//防止双radiobutton同时作用
                            listNewhand.clear();
                            listSelling.clear();
                            listWillSell.clear();
                            LogUtils.i("--->清一波InvestFrag列表数据！");
                        }
                        ptr_invest.refreshComplete();
                        JSONObject obj = JSON.parseObject(result);
                        if (obj.getBoolean("success")) {
                            JSONObject objmap = obj.getJSONObject("map");
                            JSONArray newobj = objmap.getJSONArray("newHand");
                            JSONArray isSellingObj = objmap.getJSONArray("isSellingList");
                            JSONArray willSellObj = objmap.getJSONArray("willSellList");
                            //新手专享
                            if (newobj != null && newobj.size() >0) {
                                ll_newhand.setVisibility(View.VISIBLE);
                                listNewhand = JSON.parseArray(newobj.toJSONString(), InvestListBean2.class);
                                if (adapter_newhand == null) {
                                    adapter_newhand = new InvestListAdapter(mContext, listNewhand, "newhand");
                                    lv_newhand.setAdapter(adapter_newhand);

                                } else {
                                    if (pageanxuan == 1) {
                                        adapter_newhand = new InvestListAdapter(mContext, listNewhand,"newhand");
                                        lv_newhand.setAdapter(adapter_newhand);
                                    } else {
                                        adapter_newhand.onDateChange(listNewhand);
                                    }
                                    adapter_newhand.notifyDataSetChanged();
                                }


                            } else {
                                ll_newhand.setVisibility(View.GONE);
                            }
                            //智盈宝
                            if (isSellingObj != null && isSellingObj.size() != 0) {
                                ll_is_selling.setVisibility(View.VISIBLE);
                                listSelling = JSON.parseArray(isSellingObj.toJSONString(), InvestListBean2.class);
                                if (adapter_sell == null) {
                                    adapter_sell = new InvestListAdapter(mContext, listSelling, "isSelling");
                                    lv_invest.setAdapter(adapter_sell);
                                    //setListViewHeightBasedOnChildren(lv_invest);
                                } else {
                                    if (pageanxuan == 1) {
                                        adapter_sell = new InvestListAdapter(mContext, listSelling,"isSelling");
                                        lv_invest.setAdapter(adapter_sell);
                                    } else {
                                        adapter_sell.onDateChange(listSelling);
                                    }
                                    adapter_sell.notifyDataSetChanged();
                                }
                            } else {
                                ll_is_selling.setVisibility(View.GONE);
                            }
                            //预售计划
                            if (willSellObj != null && willSellObj.size() != 0) {
                                ll_presell_plan.setVisibility(View.VISIBLE);
                                listWillSell = JSON.parseArray(willSellObj.toJSONString(), InvestListBean2.class);
                                if (adapter_presell == null) {
                                    adapter_presell = new InvestListAdapter(mContext, listWillSell, "willSell");
                                    lv_presell_plan.setAdapter(adapter_presell);
                                    //setListViewHeightBasedOnChildren(lv_invest);
                                } else {
                                    if (pageanxuan == 1) {
                                        adapter_presell = new InvestListAdapter(mContext, listWillSell,"willSell");
                                        lv_presell_plan.setAdapter(adapter_presell);
                                    } else {
                                        adapter_presell.onDateChange(listWillSell);
                                    }
                                    adapter_presell.notifyDataSetChanged();
                                }
                            } else {
                                ll_presell_plan.setVisibility(View.GONE);
                            }

                        } else {
                            //ll_is_selling.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.i("--->InvestFragment--->productList获取失败！");
                        dismissDialog();
                        ptr_invest.refreshComplete();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });*/
    }


    public static InvestFragment instance() {
        InvestFragment view = new InvestFragment();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
