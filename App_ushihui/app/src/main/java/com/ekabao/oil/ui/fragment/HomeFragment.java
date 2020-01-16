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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.HomeListAdapter;
import com.ekabao.oil.adapter.RollViewAdapter;
import com.ekabao.oil.bean.Add_Bean;
import com.ekabao.oil.bean.HomeBannerBean;
import com.ekabao.oil.bean.HomeInfoList;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.PostParams;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.LoginActivity;
import com.ekabao.oil.ui.activity.NoticeActivity;
import com.ekabao.oil.ui.activity.OilCardBuyActivity;
import com.ekabao.oil.ui.activity.OilCardImmediateActivity;
import com.ekabao.oil.ui.activity.OilCardPackageActivity;
import com.ekabao.oil.ui.activity.PhoneRechargeActivity;
import com.ekabao.oil.ui.activity.ProductActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.activity.me.AboutActivity;
import com.ekabao.oil.ui.view.ListInScroll;
import com.ekabao.oil.ui.view.MarqueeView;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.RandomValue;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/***
 * 
 * author: tonglj
 * date:2019/12/31
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
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


    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    /*@BindView(R.id.title)
    android.widget.merge title;*/
    @BindView(R.id.rpv_banner)
    RollPagerView rpvBanner;
    @BindView(R.id.ll_register)
    ImageButton llRegister;
    @BindView(R.id.ll_card)
    ImageButton llCard;
    @BindView(R.id.ll_newpeople)
    ImageButton llNewpeople;
    @BindView(R.id.tv_title_newpeople)
    TextView tvTitleNewpeople;
    @BindView(R.id.tv_newpeople_limit)
    TextView tvNewpeopleLimit;
    @BindView(R.id.tv_newpeople_interest)
    TextView tvNewpeopleInterest;
    @BindView(R.id.tv_newpeople_interest_time)
    TextView tvNewpeopleInterestTime;
    @BindView(R.id.tv_newpeople_term)
    TextView tvNewpeopleTerm;
    @BindView(R.id.tv_newpeople_total)
    TextView tvNewpeopleTotal;
    @BindView(R.id.bt_newpeople_pay)
    Button btNewpeoplePay;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.view_line_bottom)
    View viewLineBottom;
    @BindView(R.id.refreshLayout_head)
    BezierCircleHeader refreshLayoutHead;
    @BindView(R.id.ll_newhand_product)
    LinearLayout llNewhandProduct;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.rl_notice)
    LinearLayout rlNotice;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.lv_product)
    ListInScroll lvProduct;


    Unbinder unbinder;

    @BindView(R.id.ll_about)
    LinearLayout llAbout;
    @BindView(R.id.rl_oilcard_recharge)
    RelativeLayout rlOilcardRecharge;
    @BindView(R.id.ib_oilcard_package)
    ImageButton ibOilcardPackage;
    @BindView(R.id.rl_novice_register)
    RelativeLayout rlNoviceRegister;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, null);
            // 初始化View,添加Fragment
            // ...
        }

        //  View rootView = super.onCreateView(inflater, container, savedInstanceState);

        unbinder = ButterKnife.bind(this, rootView);

        //https://blog.csdn.net/bingospunky/article/details/51352400
        //两层Fragment，内层空白

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
//        refreshLayout.setPrimaryColors(new int[]{0xffF4F4F4, 0xffEE4845});

        willSell = new HomeListAdapter(mContext, isSellingList, "willSell");
        lvProduct.setAdapter(willSell);

        getHomeInfo();
        //  获取公告
        Notice();


        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(mContext, ProductActivity.class)
                        .putExtra("is_presell_plan", true)
                        .putExtra("startDate", isSellingList.get(position).getStartDate())
                        .putExtra("pid", isSellingList.get(position).getId())
                        .putExtra("ptype", "2"));

            }
        });

       /* if (isFirstEnter) {
            isFirstEnter = false;
            refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        } else {
            getHomeInfo();
        }*/


        //refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）设置是否监听列表在滚动到底部时触发加载事件
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                // LogUtils.e("第一次进入触发自动刷新，演示效果");
                getHomeInfo();
            }
        });


        // titleCentertextview.setVisibility(View.GONE);
        //titleCenterimageview.setVisibility(View.VISIBLE);
        // viewLineBottom.setVisibility(View.GONE);
        rlTitle.setVisibility(View.GONE);
        /*titleCentertextview.setText("首页");
        titleLeftimageview.setVisibility(View.GONE);
        titleLeftimageview.setImageDrawable(getResources().getDrawable(R.drawable.icon_home_note));
        titleLeftimageview.setClickable(true);
        titleLeftimageview.setOnClickListener(this);*/

        llRegister.setOnClickListener(this);
        llCard.setOnClickListener(this);
        llNewpeople.setOnClickListener(this);
        btNewpeoplePay.setOnClickListener(this);
        tvNotice.setOnClickListener(this);
        llAbout.setOnClickListener(this);


        llNewhandProduct.setOnClickListener(this);
        rlOilcardRecharge.setOnClickListener(this);
        ibOilcardPackage.setOnClickListener(this);
        rlNoviceRegister.setOnClickListener(this);

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
                /*if (bean.getLocation().contains("jumpTo=3")) { //邀请好友三重礼
                    if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                        //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                        startActivity(new Intent(mContext, LoginActivity.class));
                    } else {
                        startActivity(new Intent(mContext, InviteFriendsActivity.class));
                    }

                    //LocalApplication.getInstance().getMainActivity().setCheckedFram(3);//返利
                } else */

                if (bean.getTitle().indexOf("注册送礼") != -1) {
                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("URL", bean.getLocation() + "&app=true")
                            .putExtra("TITLE", bean.getTitle())
                            .putExtra("HTM", "立即注册")
                            .putExtra("PID", pid)
                            .putExtra("BANNER", "banner")
                    );
                } else {
                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("URL", bean.getLocation() + "&app=true")
                            .putExtra("TITLE", bean.getTitle())
                            .putExtra("PID", pid)
                            .putExtra("BANNER", "banner")
                    );
                }


                                /*else if(bean.getTitle().indexOf("邀请")!=-1){
                mContext.startActivity(new Intent(mContext,WebViewActivity.class)
                        .putExtra("URL", bean.getLocation()+"&app=true")
                        .putExtra("TITLE", bean.getTitle())
                        .putExtra("PID", pid)
                        .putExtra("HTM", "立即邀请")
                        .putExtra("BANNER", "banner"));
            }*/

            }
        });

    }

    public static HomeFragment instance() {
        HomeFragment view = new HomeFragment();
        return view;
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
                //LocalApplication.getInstance().getMainActivity().setCheckedFram(2);

             /*   HomeInfoList.LogoListBean bean = getClickUrl("安全保障");
                if (bean != null) {
                    startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("URL", bean.getClickUrl() + "?app=true")
                            .putExtra("TITLE", "安全保障")
                            .putExtra("AFID", bean.getId() + "")
                    );
                }*/
                break;
            case R.id.ll_card: //平台介绍  改为油卡直冲

                startActivity(new Intent(mContext, OilCardImmediateActivity.class)
                        //  .putExtra("is_presell_plan", true)
                        // .putExtra("startDate", isSellingList.get(0).getStartDate())
                        .putExtra("pid", newhandPid)
                        .putExtra("ptype", "1"));

                //  startActivity(new Intent(mContext, CallCenterActivity.class));

                /*HomeInfoList.LogoListBean bean2 = getClickUrl("平台介绍");
                //String url2 = getClickUrl("平台介绍");
                if (bean2 != null) {
                    startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("URL", bean2.getClickUrl() + "?app=true")
                            .putExtra("TITLE", "平台介绍")
                            .putExtra("AFID", bean2.getId() + "")
                    );
                }*/
                break;
            case R.id.ll_newpeople: //新人专享
                //startActivity(new Intent(mContext, InviteFriendsActivity.class));
                mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra("URL", "https://m.youmochou.com/oilCardWelfare?upgrade=1&app=true")
                        .putExtra("TITLE", "新人专享")
                        .putExtra("PID", pid)
                        .putExtra("BANNER", "banner")
                );
                //startActivity(new Intent(mContext, NoviceRegisterActivity.class));

               /* HomeInfoList.LogoListBean bean3 = getClickUrl("新人福利");
                //String url3 = getClickUrl("新人福利");
                // LogUtils.e("新人福利"+url3);
                if (bean3 != null) {
                    startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("URL", bean3.getClickUrl() + "?app=true")
                            .putExtra("TITLE", "新人福利")
                            .putExtra("AFID", bean3.getId() + "")
                    );
                }*/
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

                startActivity(new Intent(mContext, AboutActivity.class)
                        //  .putExtra("is_presell_plan", true)
                        // .putExtra("startDate", isSellingList.get(0).getStartDate())
                        .putExtra("pid", newhandPid)
                        .putExtra("ptype", "1"));

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

               /* startActivity(new Intent(mContext, NoviceRegisterActivity.class)
                        //  .putExtra("is_presell_plan", true)
                        // .putExtra("startDate", isSellingList.get(0).getStartDate())
                        //.putExtra("pid", newhandPid)
                        //.putExtra("ptype", "1")
                );*/


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
        //showWaitDialog("加载中...", true, "");

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
                        LogUtils.e("LF--->HomeFragment--->homeinfo" + response);
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

                            JSONArray arr = map.getJSONArray("myFuelCardList");

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




       /* OkHttpEngine.create()
                .setHeaders()
                .post(UrlConfig.HOMEINFO, params, new SimpleHttpCallback() {
                    @Override
                    public void onLogicSuccess(String data) {

                        LogUtils.e("LF--->HomeFragment--->homeinfo：" + data);
                        //dismissDialog();
                        //refreshLayout.
                        RefreshState state = refreshLayout.getState();
                        if (state == RefreshState.Refreshing) {
                            refreshLayout.finishRefresh();
                        }
                        //HomeInfo homeInfo = GsonUtil.parseJsonToBean(data, HomeInfo.class);
                        HomeInfoList homeInfo = GsonUtil.parseJsonToBean(data, HomeInfoList.class);

                        List<HomeBannerBean> banner = homeInfo.getBanner();

                        if (banner.size() > 0) {
                            lsad.clear();
                            lsad.addAll(banner);
                            adapter.notifyDataSetChanged();
                        }


              *//*  HomeInfoList.NewHandBean newHand = homeInfo.getNewHand();


                if (newHand == null) {
                    llNewhandProduct.setVisibility(View.GONE);
                } else {
                    // TODO: 2018/11/2
                    // llNewhandProduct.setVisibility(View.VISIBLE);

                    newhandPid = newHand.getId();

                    double amount = newHand.getAmount();
                    String s = NumberToCN.number2CNMontrayUnit(new BigDecimal(amount));
                    tvNewpeopleLimit.setText("限额" + s);
                    tvTitleNewpeople.setText(newHand.getFullName());


                    double v = newHand.getRate() + newHand.getActivityRate();


                    SpannableStringBuilder sp = new SpannableStringBuilder(v + "%");
                    //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
                    sp.setSpan(new AbsoluteSizeSpan(42, true), 0, sp.toString().indexOf("."), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
                    tvNewpeopleInterest.setText(sp);

                    tvNewpeopleTerm.setText("出借期限  " + newHand.getDeadline() + "天");
                    tvNewpeopleTotal.setText("剩余金额  " + StringCut.getNumKbs((double) (newHand.getSurplusAmount())));
                }
                logoList = homeInfo.getLogoList();


                // isSellingList =
                List<HomeInfoList.IsSellingListBean> sellingList = homeInfo.getIsSellingList();

                if (sellingList.size() > 0) {

                    isSellingList.clear();
                    isSellingList.addAll(sellingList);
                    willSell.notifyDataSetChanged();

                }*//*

                    }

                    @Override
                    public void onLogicError(int code, String msg) {
                        LogUtils.e("LF--->HomeFragment--->homeinfo：" + msg);
                        RefreshState state = refreshLayout.getState();
                        if (state == RefreshState.Refreshing) {
                            refreshLayout.finishRefresh();
                        }
                        //dismissDialog();
                    }

                    @Override
                    public void onError(IOException e) {
                        RefreshState state = refreshLayout.getState();
                        if (state == RefreshState.Refreshing) {
                            refreshLayout.finishRefresh();
                        }
                        LogUtils.e("LF--->HomeFragment--->homeinfo：" + e.toString());
                        //dismissDialog();
                    }
                });*/


    }

    /**
     * 通知
     */
    private void Notice() {
        // rl_notice.setVisibility(View.VISIBLE);
        List note = RandomValue.getNote();
        List<String> info = new ArrayList<>();
        info.addAll(note);
        marqueeView.startWithList(info);

       /* OkHttpUtils
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
                                rlNotice.setVisibility(View.VISIBLE);

                                lsAd = JSON.parseArray(objnotice.toJSONString(), Add_Bean.class);
                                List<String> info = new ArrayList<>();
                                for (int i = 0; i < lsAd.size(); i++) {
                                    info.add(lsAd.get(i).getTitle().toString());
                                }

                                marqueeView.startWithList(info);

                                marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position, TextView textView) {
                                        *//*startActivity(new Intent(mContext, WebViewActivity.class)
                                                .putExtra("URL", UrlConfig.WEBSITEAN + "?artiId=" + lsAd.get(position).getArti_id())
                                                .putExtra("TITLE", "平台公告"));*//*
                                        startActivity(new Intent(mContext, WebViewActivity.class)
                                                .putExtra("URL", UrlConfig.WEBSITEAN + "?app=true&id=" + lsAd.get(position).getArtiId())
                                                .putExtra("TITLE", "平台公告"));
                                    }
                                });
                            } else {
                                rlNotice.setVisibility(View.GONE);
                            }
                        } else {
                            rlNotice.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
//				ToastMaker.showShortToast("请检查网络!");
                    }
                });*/

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
