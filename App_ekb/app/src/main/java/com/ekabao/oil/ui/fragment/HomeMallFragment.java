package com.ekabao.oil.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.HomeListAdapter;
import com.ekabao.oil.adapter.HomePagerRecycleAdapter;
import com.ekabao.oil.adapter.RollViewAdapter;
import com.ekabao.oil.bean.Activity;
import com.ekabao.oil.bean.Add_Bean;
import com.ekabao.oil.bean.GoodsList;
import com.ekabao.oil.bean.GoodsMiddlebanner;
import com.ekabao.oil.bean.HomeBannerBean;
import com.ekabao.oil.bean.HomeHostProduct;
import com.ekabao.oil.bean.HomeInfoList;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.PostParams;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.LoginActivity;
import com.ekabao.oil.ui.activity.MainActivity;
import com.ekabao.oil.ui.activity.NoticeActivity;
import com.ekabao.oil.ui.activity.OilCardBuyActivity;
import com.ekabao.oil.ui.activity.OilCardImmediateActivity;
import com.ekabao.oil.ui.activity.OilCardPackageActivity;
import com.ekabao.oil.ui.activity.PhoneRechargeActivity;
import com.ekabao.oil.ui.activity.ProductActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/5/29.
 */

public class HomeMallFragment extends BaseFragment implements View.OnClickListener {


    //    @BindView(R.id.title_lefttextview)
//    TextView titleLefttextview;
//    @BindView(R.id.title_leftimageview)
//    ImageView titleLeftimageview;
//    @BindView(R.id.title_centertextview)
//    TextView titleCentertextview;
//    @BindView(R.id.title_centerimageview)
//    ImageView titleCenterimageview;
//    @BindView(R.id.title_righttextview)
//    TextView titleRighttextview;
//    @BindView(R.id.title_rightimageview)
//    ImageView titleRightimageview;
//    @BindView(R.id.view_line_bottom)
//    View viewLineBottom;
//    @BindView(R.id.rl_title)
//    RelativeLayout rlTitle;
    @BindView(R.id.rv_home)
    RecyclerView rvHome;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    Unbinder unbinder;
    @BindView(R.id.fillStatusBarView)
    View fillStatusBarView;

    private String pid = null; //这个怎么没有地方赋值啊

    private SharedPreferences preferences = LocalApplication.sharereferences;
    //List<HomeInfo.BannerBean> lsad = new ArrayList<HomeInfo.BannerBean>();
    List<HomeBannerBean> lsad = new ArrayList<HomeBannerBean>();
    private RollViewAdapter adapter;

    private static boolean isFirstEnter = true; //第一次进入触发自动刷新
    private int newhandPid;// 新手标项目id
    private List<HomeInfoList.IsSellingListBean> isSellingList = new ArrayList<>(); //精选

    private List<HomeInfoList.LogoListBean> logoList; // 安全保障 平台介绍..

    private List<Add_Bean> lsAd = new ArrayList<>();
    private HomeListAdapter willSell;

    View rootView;
    private HomePagerRecycleAdapter homepagerRecycleAdapter;

    int pageon = 1;//当前页
    int total;  //返回总共多少个
    int totalPage; //返回总共多少页
    private List<GoodsList> rows_List = new ArrayList<GoodsList>();// 每次加载的数据
    private List<Activity.PageBean.RowsBean> activity_List = new ArrayList<Activity.PageBean.RowsBean>();// 每次加载的数据

    private List<HomeHostProduct> homeHostProduct = new ArrayList<>();

    public static HomeMallFragment instance() {
        HomeMallFragment view = new HomeMallFragment();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home_mall, null);
            // 初始化View,添加Fragment
            // ...
        }


        unbinder = ButterKnife.bind(this, rootView);

        //refreshLayoutHead.setPrimaryColorsId(0xffEE4845);

        initParams();

        return rootView;
    }

    @Override
    protected int getLayoutId() {
        //return R.layout.fragment_home;
        return 0;
    }

    @Override
    protected void initParams() {
        pageon = 1;
        refreshLayout.setPrimaryColors(new int[]{0xffF4F4F4, 0xffEE4845});

        willSell = new HomeListAdapter(mContext, isSellingList, "willSell");

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) fillStatusBarView.getLayoutParams();
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = getStatusBar();
        fillStatusBarView.setLayoutParams(lp);

        /**
         * 设置view高度为statusbar的高度，并填充statusbar
         */
        // View mStatusBar = view.findViewById(R.id.fillStatusBarView);


        getHomeInfo();
        gethotList();

        //  获取公告
        Notice();

//        getActivitiesDoing();
        getMallInfo();
        homepagerRecycleAdapter = new HomePagerRecycleAdapter(mContext, 1);
        rvHome.setAdapter(homepagerRecycleAdapter);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocalApplication.getInstance());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // LogUtils.e("position"+position);
                return position < HomePagerRecycleAdapter.COUNT ? 2 : 1;
            }
        });
        rvHome.setLayoutManager(layoutManager);

        //  refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）设置是否监听列表在滚动到底部时触发加载事件
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                pageon = 1;
                // LogUtils.e("pageon+"+pageon+"totalPage"+totalPage);


                getHomeInfo();
                gethotList();
                //  获取公告
                Notice();

                //进行中的活动
//                getActivitiesDoing();

                getMallInfo();
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {

                LogUtils.e("加载更多pageon+" + pageon + "totalPage" + totalPage);
                if (pageon >= totalPage) {

                    // refreshLayout.finishLoadMore();
                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件

                } else {
                    //getInitData();
                    getHomeInfo();
                    gethotList();
                    //  获取公告
                    Notice();
//                    getActivitiesDoing();
                    getMallInfo();
                }

            }
        });
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt("oid_pid", 0);
        edit.putInt("oid_money", 0);
        edit.putInt("fragment_type", 0);
        // edit.putInt("oid_pid",homeHostProduct.get(position).getId());
        edit.commit();
        homepagerRecycleAdapter.setOnCenterItemClickListener(new HomePagerRecycleAdapter.OnCenterItemClickListener() {
            @Override
            public void onTypeItemClick(View view, int position) {
                LogUtils.e("onTypeItemClick" + position);
                if (position == 999) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.setCheckedFram(2);
                } else if (position == 666) {
                    MainActivity activity = (MainActivity) getActivity();

                    activity.setCheckedFram(3);
                } else {

                    if (homeHostProduct != null) {

                        int money = 500;
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putInt("oid_pid", homeHostProduct.get(position).getId());
                        edit.putInt("oid_money", money);
                        // edit.putInt("oid_pid",homeHostProduct.get(position).getId());
                        edit.commit();
                        LogUtils.e("setOnCenterItemClickListener+" + homeHostProduct.get(position).getId());

                        MainActivity activity = (MainActivity) getActivity();
                        activity.setCheckedFram(2);


                    }
                }

            }
        });


    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBar() {
        /**
         * 获取状态栏高度
         * */
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_notice://公告更多
            case R.id.title_leftimageview: //消息
                startActivity(new Intent(mContext, NoticeActivity.class));
                break;
            case R.id.ll_register: //安全保证 --> 购买油卡
                startActivity(new Intent(mContext, OilCardBuyActivity.class));

                break;
            case R.id.ll_card: //平台介绍  改为油卡直冲

                startActivity(new Intent(mContext, OilCardImmediateActivity.class)
                        //  .putExtra("is_presell_plan", true)
                        // .putExtra("startDate", isSellingList.get(0).getStartDate())
                        .putExtra("pid", newhandPid)
                        .putExtra("ptype", "1"));
                break;
            case R.id.ll_newpeople: //新人专享
                //startActivity(new Intent(mContext, InviteFriendsActivity.class));
                mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra("URL", "https://m.youmochou.com/oilCardWelfare?upgrade=1&app=true")
                        .putExtra("TITLE", "新人专享")
                        .putExtra("PID", pid)
                        .putExtra("BANNER", "banner")
                );

                break;
            case R.id.bt_newpeople_pay: //新手标 立即出借
                if (newhandPid != 0) {
                    //新手标详情页面
                    Intent intent = new Intent(mContext, ProductActivity.class);
                    intent.putExtra("pid", newhandPid);
                    intent.putExtra("ptype", "1");
                    startActivity(intent);
                }
                break;
            case R.id.ll_newhand_product: //新人专享
                if (newhandPid != 0) {
                    startActivity(new Intent(mContext, ProductActivity.class)
                            //  .putExtra("is_presell_plan", true)
                            // .putExtra("startDate", isSellingList.get(0).getStartDate())
                            .putExtra("pid", newhandPid)
                            .putExtra("ptype", "1"));
                }
                break;

            case R.id.ll_about: //关于我们
                startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.SAFE)
                        .putExtra("TITLE", "关于我们")
                        .putExtra("noWebChrome", "aboutMe"));

                break;
            case R.id.ib_oilcard_package:

                startActivity(new Intent(mContext, OilCardPackageActivity.class)
                        //  .putExtra("is_presell_plan", true)
                        // .putExtra("startDate", isSellingList.get(0).getStartDate())
                        .putExtra("pid", newhandPid)
                        .putExtra("ptype", "1"));

                break;
            case R.id.rl_oilcard_recharge: //-->手机套餐
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    // startActivity(new Intent(mContext, LoginActivity.class));
                    startActivity(new Intent(mContext, LoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, PhoneRechargeActivity.class));
                }


                break;
            case R.id.rl_novice_register: //注册享好礼 -->手机直值
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    // startActivity(new Intent(mContext, LoginActivity.class));
                    startActivity(new Intent(mContext, LoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, PhoneRechargeActivity.class)
                            .putExtra("position", 1)
                    );
                }

                break;

            //

        }

    }

    /**
     * 获取安全保障 的url
     */
    private HomeInfoList.LogoListBean getClickUrl(String text) {
        String clickUrl = null;
        HomeInfoList.LogoListBean Bean = new HomeInfoList.LogoListBean();
        if (logoList.size() > 0) {
            for (HomeInfoList.LogoListBean bean : logoList) {
                if (bean.getTitle().equalsIgnoreCase(text)) {
                    //clickUrl = bean.getClickUrl()+"?app=true";
                    Bean = bean;
                }
            }
        }
        return Bean;
    }

    private void getHomeInfo() {
        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("uid", preferences.getString("uid", ""));
        map.put("toFrom", LocalApplication.getInstance().channelName);
        map.put("version", UrlConfig.version);
        map.put("channel", "2");

        OkHttpUtils.post().url(UrlConfig.HOMEINFO)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("toFrom", LocalApplication.getInstance().channelName)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("新的3.0的首页--->homeinfo" + response);
                        Log.d("adasd", response);
                        if (refreshLayout != null) {
                            RefreshState state = refreshLayout.getState();
                            if (state == RefreshState.Refreshing) {
                                refreshLayout.finishRefresh();
                            }
                        }

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
                            JSONArray arr = map.getJSONArray("hostProduct");

                            if (arr != null) {

                                // 油卡充值套餐   新品上线
                                List<HomeHostProduct> homeHostProducts = JSON.parseArray(arr.toJSONString(), HomeHostProduct.class);

                                TreeSet<HomeHostProduct> ts = new TreeSet<>();
                                //2,将list集合中所有的元素添加到TrreSet集合中,对其排序,保留重复
                                //
                                ts.addAll(homeHostProducts);

                                //  ts.addAll(homeHostProducts);

                                homeHostProduct.clear();

                                homeHostProduct.addAll(ts);
                                LogUtils.e("homeHostProduct" + ts.size());
                                //homeHostProduct.addAll(ts);
                                //homeHostProduct =  homeHostProducts;


                                homepagerRecycleAdapter.setCenterBean(homeHostProduct);

                            }

                            //轮播图
                            homepagerRecycleAdapter.setheaderbean(homeInfo);

                            //分类
                            homepagerRecycleAdapter.setCategoryBean((ArrayList<HomeInfoList.LogoListBean>) homeInfo.getLogoList());


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
     * /首页的 下面的商城
     * *
     */
    private void gethotList() {

        LogUtils.e("pageon" + pageon);

        OkHttpUtils.post().url(UrlConfig.hotList)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("page", pageon + "")
                .addParams("rows", "10")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // LogUtils.e("新的3.0的商城--->" + response);

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


                            if (map.getJSONArray("middlebanner") != null) {
                                LogUtils.e("middlebanner" + map.getJSONArray("middlebanner").toString());
                                JSONArray arr = map.getJSONArray("middlebanner");
                                List<GoodsMiddlebanner> middlebanner = JSON.parseArray(arr.toJSONString(), GoodsMiddlebanner.class);
                                homepagerRecycleAdapter.setMiddlebanner(middlebanner);
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
     * /首页的 下面的商城
     * *
     */
    private void getMallInfo() {

        LogUtils.e("易商城--->page" + pageon);
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

                            if (map.getJSONArray("good") != null) {
                                JSONArray arr = map.getJSONArray("good");
                                List<GoodsList> goodslist = JSON.parseArray(arr.toJSONString(), GoodsList.class);


                                LogUtils.e(pageon + "易商城--->" + goodslist.size() + "/" + rows_List.size());


                                boolean flagFirst = true;

                                if (pageon == 1) {
                                    LogUtils.e("下拉刷新");
                                    flagFirst = true;

                                } else {
                                    flagFirst = false;
                                }
                               /* if (pageon == 1) {
                                    rows_List.clear();
                                }*/
                                rows_List.clear();

                                if (goodslist.size() < 10) {

                                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                                } else {
                                    pageon++;
                                }

                                rows_List.addAll(goodslist);


                                //  mallRecycleAdapter.setCenterBean(homeHostProduct);
                                homepagerRecycleAdapter.setRefreshBean(rows_List, flagFirst);

                                refreshLayout.finishLoadMore();
                            }


                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
//                            new show_Dialog_IsLogin(MessageCenterActivity.mContext).show_Is_Login();
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
     * 进行中的活动
     */
    /*
    private void getActivitiesDoing() {

        // showWaitDialog("加载中...", true, "");
        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("uid", preferences.getString("uid", ""));
        // map.put("type", "1");
        map.put("pageOn", pageon + "");
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


                pageon = page.getPageOn();
                total = page.getTotal();
                totalPage = page.getTotalPage();

                boolean flagFirst = true;

                if (pageon == 1) {
                    LogUtils.e("下拉刷新");
                    flagFirst = true;

                } else {
                    flagFirst = false;
                }
                activity_List.clear();

                if (rows.size() < 10) {


                    if (rows.size() > 0) {
                        activity_List.addAll(rows);
                        // newsAdapter.notifyDataSetChanged();
                    }

                    LogUtils.e(rows.size() + "加载完" + activity_List.size());

                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件

                } else {
                    activity_List.addAll(rows);
                    pageon++;
                }

                homepagerRecycleAdapter.setRefreshBean(activity_List, flagFirst);

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
*/

    /**
     * 通知
     */
    private void Notice() {
        OkHttpUtils
                .post()
                .url(UrlConfig.NOTICE)
                .addParam("limit", "3")
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("--->NOTICE " + response);
                        dismissDialog();
                        Log.d("HOmeMallFragment", response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            dismissDialog();
                            JSONObject objmap = obj.getJSONObject("map");
                            if (objmap.isEmpty()) {
                                return;
                            }
                            //JSONArray objnotice = objmap.getJSONArray("urgentNotice");
                            // objmap.getJSONObject("page").getJSONArray("urgentNotice")
                            JSONArray objnotice = objmap.getJSONArray("urgentNotice");

                            if (objnotice != null) {
                                //  rlNotice.setVisibility(View.VISIBLE);

                                lsAd = JSON.parseArray(objnotice.toJSONString(), Add_Bean.class);

                                homepagerRecycleAdapter.setNotice(lsAd);

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

    @Override
    public void onStart() {
        super.onStart();
        //  EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
