package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.EventMessage.EvmInvest;
import com.mcz.xhj.yz.dr_adapter.InvestListAdapter;
import com.mcz.xhj.yz.dr_app_fragment.BaseFragment;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.InvestListBean;
import com.mcz.xhj.yz.dr_bean.InvestListBean2;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;

import com.mcz.xhj.yz.dr_view.ListInScroll;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;


public class InvestFrag extends BaseFragment {
    @BindView(R.id.toolbar)
    View toolbar;
    @BindView(R.id.ptr_invest)
    @Nullable
    PtrClassicFrameLayout ptr_invest;
    @BindView(R.id.sc_scroll)
    ScrollView scScroll;

    @BindView(R.id.ll_newhand)
    LinearLayout ll_newhand;
    @BindView(R.id.lv_newhand)
    @Nullable
    ListInScroll lv_newhand;

    @BindView(R.id.ll_is_selling)
    LinearLayout ll_is_selling;
    @BindView(R.id.lv_invest)
    @Nullable
    ListInScroll lv_invest;

    @BindView(R.id.ll_presell_plan)
    LinearLayout ll_presell_plan;
    @BindView(R.id.lv_presell_plan)
    @Nullable
    ListInScroll lv_presell_plan;

    @BindView(R.id.tv_title_newhand)
    TextView tv_title_newhand;

    @BindView(R.id.ll_raised_repayed)
    LinearLayout ll_raised_repayed;
    @BindView(R.id.tv_raised_repayed)
    TextView tv_raised_repayed;

    @BindView(R.id.title_leftimageview)
    @Nullable
    ImageView leftima;
    @BindView(R.id.title_centertextview)
    @Nullable
    TextView centertv;

    private boolean isLastitem = false;
    private boolean isLoading;
    private InvestListBean newhandinfo = new InvestListBean();

    public static InvestFrag instance() {
        InvestFrag view = new InvestFrag();
        return view;
    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.frag_invest;
    }


    @Override
    protected void initParams() {
        leftima.setVisibility(View.GONE);
        centertv.setText("投资");
        /*if (LocalApplication.getInstance().getMainActivity().hasNavigationBar) {
            toolbar.setVisibility(View.GONE);
        }*/

        lv_invest.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    isLastitem = true;
                } else {
                    isLastitem = false;
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                if (isLastitem && scrollState == SCROLL_STATE_IDLE) {
                    if (!isLoading) {
                        isLoading = true;
                        pageanxuan++;
                        getData(pageanxuan, type);
                    }
                }
            }
        });
        lv_newhand.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mContext, NewhandDetailActivity.class)
                        .putExtra("pid", listNewhand.get(position).getId())
                        .putExtra("ptype", listNewhand.get(position).getType()));
            }
        });
        lv_invest.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mContext, NewProductDetailActivity.class)
                        .putExtra("pid", listSelling.get(position).getId())
                        .putExtra("ptype", listSelling.get(position).getType()));
            }
        });
        lv_presell_plan.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mContext, NewProductDetailActivity.class)
                        .putExtra("is_presell_plan",true)
                        .putExtra("startDate",listWillSell.get(position).getStartDate())
                        .putExtra("pid", listWillSell.get(position).getId())
                        .putExtra("ptype", listWillSell.get(position).getType()));
            }
        });

        scScroll.setFocusable(false);
        scScroll.smoothScrollTo(0, 20);
        ptr_invest.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageanxuan = 1;
                getData(1, type);
                getRaisedAndRepayedNum();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, scScroll, header);
            }
        });
    }

    private void getRaisedAndRepayedNum() {
        OkHttpUtils.post().url(UrlConfig.RAISEDINFO)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                LogUtils.i("--->raisedinfo " + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    ll_raised_repayed.setVisibility(View.VISIBLE);
                    JSONObject map = obj.getJSONObject("map");
                    String raisedNum = map.getString("raisedNum");
                    String repayedNum = map.getString("repayedNum");
                    tv_raised_repayed.setText("已募集" + raisedNum + "个项目，已还款" + repayedNum + "个项目");

                } else {
                    ll_raised_repayed.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                LogUtils.i("--->raisedinfo " + "请求失败！");
            }
        });
    }

    private String type = "2";

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (LocalApplication.getInstance().getMainActivity().isActivity) {
            LocalApplication.getInstance().getMainActivity().isActivity = false;
        }
        //LogUtils.i("InvestFrag--->onResume----->getDataAll()");
        getData(1, type);
        getRaisedAndRepayedNum();

    }

    public void updateThis() {
        pageanxuan = 1;
        getDataAll("");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        if (!hidden) {
            //LogUtils.i("InvestFrag--->onHiddenChanged----->");

        } else {
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private boolean isfresh = false;

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(EvmInvest evmInvest) {
        isfresh = evmInvest.getIsFresh();
    }

    //
    private List<InvestListBean2> listNewhand = new ArrayList<InvestListBean2>();
    private List<InvestListBean2> listSelling = new ArrayList<InvestListBean2>();
    private List<InvestListBean2> listWillSell = new ArrayList<InvestListBean2>();
    private List<InvestListBean2> mlslb2 = new ArrayList<InvestListBean2>();// 每次加载的数据
    private InvestListAdapter adapter_newhand;
    private InvestListAdapter adapter_sell;
    private InvestListAdapter adapter_presell;
    private int pageanxuan = 1;
    private int pageconfig = 1;
    private String title;

    private void getData(int page, String type) {
        pageconfig = page;
        pageanxuan = page;
        showWaitDialog("加载中...", true, "");
        //这里type改为固定的，原来是type，根据需求
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
                });
    }

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private String pid = null;

    //判断是否显示新手标
    private void getDataAll(final String flag) {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.HOMEINFO)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {
                //LogPrintUtil.e("HY--->Invest_HOMEINFO", response);
                LogUtils.i("--->InvestFragment--->homeinfo：" + response);
                dismissDialog();
                ptr_invest.refreshComplete();
                JSONObject obj = JSON.parseObject(response);
                /*if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONObject newobj = objmap.getJSONObject("newHand");
                    if (newobj != null && !newobj.equals("")) {                        ll_newhand.setVisibility(View.VISIBLE);
                        pid = newobj.getString("id");
                        tv_deadline.setText(newobj.getString("deadline") + "天");
                        Double rate = newobj.getDoubleValue("rate")+newobj.getDouble("activityRate");
                        tvRateNew.setText(rate + "%");

                        if (objmap.getString("newHandLabel") != null) {
                            if (!objmap.getString("newHandLabel").equalsIgnoreCase("")) {
                                //tv_label.setVisibility(View.VISIBLE);
                                //tv_label.setText(objmap.getString("newHandLabel"));
                            }
                        }

                    } else {
                        ll_newhand.setVisibility(View.GONE);
                    }
                    if (!flag.equalsIgnoreCase("isnew")) {
                        getData(1, type);
                    }
                } else {

                }*/
            }

            @Override
            public void onError(Call call, Exception e) {
                LogUtils.i("--->InvestFragment--->homeinfo获取失败！");
                dismissDialog();
                ptr_invest.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });

    }

    @OnClick({R.id.ll_raised_repayed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_raised_repayed:
                startActivity(new Intent(mContext, InvestTwoLeveListActivity.class).putExtra("type", "2"));
                break;
            default:
                break;
        }
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
