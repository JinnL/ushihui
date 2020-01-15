package com.ekabao.oil.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.CoverFlowAdapter;
import com.ekabao.oil.adapter.OilCardPackageFragmentAdapter;
import com.ekabao.oil.bean.CouponsBean;
import com.ekabao.oil.bean.OilCardBean;
import com.ekabao.oil.bean.OilCardPackageBean;
import com.ekabao.oil.bean.OilOrderDetailBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.LoginActivity;
import com.ekabao.oil.ui.activity.OilCardBuyActivity;
import com.ekabao.oil.ui.activity.OilCardPayActivity;
import com.ekabao.oil.ui.activity.me.AddOilCardActivity;
import com.ekabao.oil.ui.activity.me.CallCenterActivity;
import com.ekabao.oil.ui.activity.me.MeWelfareActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.ui.view.coverflow.PagerContainer;
import com.ekabao.oil.util.Arith;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.ToastUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * ${APP_NAME}  App_ekabao
 *
 * @time 2019/3/15 16:35
 * Created by lj on 2019/3/15 16:35.
 */

public class OilPackageFragment extends BaseFragment implements View.OnClickListener {


    Unbinder unbinder;

    public Context mContext;
    @BindView(R.id.vp_overlap)
    ViewPager vpOverlap;
    @BindView(R.id.pager_container)
    PagerContainer pagerContainer;
    @BindView(R.id.ll_oilcard)
    LinearLayout llOilcard;
    @BindView(R.id.ib_add_oilcard)
    ImageButton ibAddOilcard;
    @BindView(R.id.ib_oilcard_buy)
    ImageButton ibOilcardBuy;
    @BindView(R.id.rl_no_oilcard)
    RelativeLayout rlNoOilcard;
    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.ib_reduce)
    ImageButton ibReduce;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.ib_add)
    ImageButton ibAdd;
    @BindView(R.id.ll_recharge)
    LinearLayout llRecharge;
    @BindView(R.id.tv_package_title)
    TextView tvPackageTitle;
    @BindView(R.id.rv_package)
    RecyclerView rvPackage;
    @BindView(R.id.tv_explan)
    TextView tvExplan;
    @BindView(R.id.ll_detail)
    LinearLayout llDetail;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.cl_plan)
    ConstraintLayout clPlan;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.cl_coupon)
    ConstraintLayout clCoupon;
    @BindView(R.id.tv_all_money)
    TextView tvAllMoney;
    @BindView(R.id.tv_cheaper)
    TextView tvCheaper;
    @BindView(R.id.bt_pay)
    Button btPay;

    private SharedPreferences preferences;
    private String uid;

    private ArrayList<OilCardPackageBean> list3 = new ArrayList<>();
    private ArrayList<OilCardBean> oillist = new ArrayList<>();
    private OilCardBean oilCardBean; //默认的油卡
    private OilCardPackageFragmentAdapter meEtouRechargeAdapter;
    private CoverFlowAdapter coverFlowAdapter;
    private int monthMoney = 500;// 月充值金额
    private double allMoney;// 总金额
    private int coupons = 0;// 优惠券的数量

    private static final int addOilCard = 16540; //去添加油卡
    private static final int login = 16541; //登录
    private OilCardPackageBean oilCardPackageBean; //套餐

    private List<CouponsBean> couponsList = new ArrayList<CouponsBean>();
    private CouponsBean couponsBean; //优惠券
    private int isUsed; //可用优惠券数量
    private static final int loginCode = 10156; //选择优惠券


    private int pid;

    private double money;


    public static OilPackageFragment newInstance() {

        Bundle args = new Bundle();

        OilPackageFragment fragment = new OilPackageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static OilPackageFragment newInstance(int pid) {

        Bundle args = new Bundle();

        OilPackageFragment fragment = new OilPackageFragment();
        args.putInt("pid", pid);
        fragment.setArguments(args);


        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();    //通过fragment的Activity实例化mActivity

        Bundle args = getArguments();
        if (args != null) {
            pid = args.getInt("pid");
            LogUtils.e("onCreate+pid+" + pid);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_oil_card_package;
    }

    @Override
    protected void initParams() {
        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");

        pid = preferences.getInt("oid_pid", 0);

        // Intent intent = getIntent();

        int money = preferences.getInt("oid_money", 0);

        if (money != 0) {
            monthMoney = money;
            tvMoney.setText(monthMoney + "");
        }


        ibAdd.setOnClickListener(this);
        ibReduce.setOnClickListener(this);
        ibAddOilcard.setOnClickListener(this);
        ibOilcardBuy.setOnClickListener(this);
        clCoupon.setOnClickListener(this);
        clPlan.setOnClickListener(this);
        btPay.setOnClickListener(this);
      /*  list3.add(new OilCardPackageBean(1, 0.98f, "9.8折", "3个月加油套餐"));
        list3.add(new OilCardPackageBean(2, 0.92f, "9.2折", "6个月加油套餐"));
        list3.add(new OilCardPackageBean(3, 0.83f, "8.3折", "12个月加油套餐"));
        list3.add(new OilCardPackageBean(4, 0.75f, "7.5折", "20个月加油套餐"));
        list3.add(new OilCardPackageBean(5, 0.71f, "7.1折", "24个月加油套餐"));*/
        tvMonth.setTextColor(getResources().getColor(R.color.primary));


        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);

        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

        rvPackage.setLayoutManager(gridLayoutManager);

        // rvPackage.addItemDecoration(new SpaceItemDecoration(10, 10, 10, 10));
        meEtouRechargeAdapter = new OilCardPackageFragmentAdapter(list3, 0, 1);
        rvPackage.setAdapter(meEtouRechargeAdapter);

        meEtouRechargeAdapter.setOnItemClickLitener(new OilCardPackageFragmentAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int positon) {

                //  ToastUtil.showToast("positon"+positon);

                meEtouRechargeAdapter.setPosition(positon);


                meEtouRechargeAdapter.notifyDataSetChanged();

                oilCardPackageBean = list3.get(positon);

                SharedPreferences.Editor edit = preferences.edit();
                edit.putInt("oid_pid", oilCardPackageBean.getId());
                edit.commit();
                showMonthText();
                if (oilCardPackageBean != null) {
                    showMoney();
                }
            }
        });
        //套餐点击
       /* meEtouRechargeAdapter.setOnItemClickLitener(new OilCardPackageFragmentAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int positon) {


            }
        });*/

        //我的油卡
        ViewPager pager = pagerContainer.getViewPager();

        coverFlowAdapter = new CoverFlowAdapter(mContext, oillist, 2);

        pager.setAdapter(coverFlowAdapter);
        //pagerContainer.setClipChildren(true);
        //pagerContainer.setOverlapEnabled(true);
        //
        pager.setOffscreenPageLimit(15);

        vpOverlap.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                oilCardBean = oillist.get(position);
                LogUtils.e("onPageSelected" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       /* new CoverFlow.Builder()
                .with(pager)
                .scale(0f)
                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.dp_m_10))
                .spaceSize(0f)
                .build();*/
       /*
        MultiplePagerAdapter adapter = new MultiplePagerAdapter(mContext, list);
        vpOverlap.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/
        getData();
        if (uid.equalsIgnoreCase("")) {
            //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
            rlNoOilcard.setVisibility(View.VISIBLE);
            llOilcard.setVisibility(View.GONE);
        } else {
            //llOilcard
            getOilCard();
            getCoupons();
        }
        //油卡类型 1:中石化 2:中石油

        coverFlowAdapter.setItemClickListener(new CoverFlowAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
              /*  LogUtils.e("fuelCardId" + list.get(position).getId());
                startActivityForResult(new Intent(getActivity(), OilCardDetailsActivity.class)
                        .putExtra("uid", preferences.getString("uid", ""))
                        .putExtra("fuelCardId", list.get(position).getId()), Add);*/
            }

            @Override
            public void onAddOilCardClick(View view, int position) {
                // ToastMaker.showShortToast("onAddOilCardClick");
                startActivityForResult(new Intent(mContext, AddOilCardActivity.class)
                                .putExtra("uid", uid),
                        addOilCard);
            }
        });


    }



//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser&&preferences!=null) {
//            pid = preferences.getInt("oid_pid", 0);
//            meEtouRechargeAdapter.setPid(pid);
//            meEtouRechargeAdapter.notifyDataSetChanged();
//            //相当于Fragment的onResume，为true时，Fragment已经可见
//        } else {
//            //相当于Fragment的onPause，为false时，Fragment不可见
//        }
//    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden&&preferences!=null) {
            pid = preferences.getInt("oid_pid", 0);
            meEtouRechargeAdapter.setPid(pid);
            meEtouRechargeAdapter.notifyDataSetChanged();
            //相当于Fragment的onResume，为true时，Fragment已经可见
        } else {
            //相当于Fragment的onPause，为false时，Fragment不可见
        }
    }

    private void showMonthText() {
        if (oilCardPackageBean.getDeadline() == 1) {
            tvMonth.setText("下个月当天充值");
            tvMonth.setTextColor(getResources().getColor(R.color.primary));
        } else {
            tvMonth.setTextColor(getResources().getColor(R.color.text_black));
            tvMonth.setText(oilCardPackageBean.getDeadline() + "个月");
        }
    }

    public void setPid(int pid, int money, int month) {

        LogUtils.e("setPid+pid" + pid + month);
/*
        this.pid = pid;
        this.money = money;
        if (month != 0) {
            monthMoney = money;

        }

        if (pid != 0) {
            for (int i = 0; i < list3.size(); i++) {

                OilCardPackageBean bean = list3.get(i);

                if (bean.getId() == pid) {
                    LogUtils.e(pid + "套餐" + bean.getId() + "i" + i);
                    meEtouRechargeAdapter.setPosition(i);

                    oilCardPackageBean = list3.get(i);
                    showMoney();
                }
            }
        } else {
            oilCardPackageBean = list3.get(0);

            showMoney();
        }
        if (tvMoney != null) {
            //tvMoney.setText(monthMoney + "");
            //tvMonth.setText(month+ "个月");
            tvMonth.setText(oilCardPackageBean.getDeadline() + "个月");
        }*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.title_rightimageview: //击进入充值问题页面
                startActivity(new Intent(mContext, CallCenterActivity.class));
                break;
            case R.id.ib_add_oilcard:

                if (uid.equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivityForResult(new Intent(mContext, LoginActivity.class), login);
                } else {
                    //llOilcard
                    //去添加油卡
                    startActivityForResult(new Intent(mContext, AddOilCardActivity.class)
                                    .putExtra("uid", uid),
                            addOilCard);
                }

                break;
            case R.id.ib_oilcard_buy: //领取油卡
                if (uid.equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivityForResult(new Intent(mContext, LoginActivity.class), login);
                } else {
                    //llOilcard
                    //去添加油卡
                    startActivity(new Intent(mContext, OilCardBuyActivity.class));
                }

                break;
            case R.id.ib_add:
                if (monthMoney >= 5000) {
                    ToastMaker.showShortToast("单笔订单每月最高5000元，可多次下单");
                    break;
                } else {
                    monthMoney = monthMoney + 100;
                    tvMoney.setText(monthMoney + "");
                    if (oilCardPackageBean != null) {

                        showMoney();

                       /* int money = monthMoney * oilCardPackageBean.getDeadline();

                        double amount = 0;
                        if (couponsBean != null) {
                            amount = couponsBean.getAmount();
                        }

                        //double v2 = money * oilCardPackageBean.getRate() - amount;
                        double v2 = mul(money, oilCardPackageBean.getRate()) - amount;

                        allMoney = v2;

                        tvAllMoney.setText(StringCut.getNumKb(v2) + "");

                        //double v1 = money * (1 - oilCardPackageBean.getRate()) + amount;
                        //double v1 = mul(money, 1 - oilCardPackageBean.getRate()) + amount;
                        double v1 = add(mul(money, sub(1.0,oilCardPackageBean.getRate())),amount);
                        tvCheaper.setText("(省" + StringCut.getNumKb(v1) + ")");*/
                    }
                }

                break;
            case R.id.ib_reduce:
                if (monthMoney <= 100) {
                    // ToastUtil.showToastOnUIThread("单月最低充值100元");
                    ToastMaker.showShortToast("单月最低充值100元");
                    break;
                } else {
                    monthMoney = monthMoney - 100;
                    tvMoney.setText(monthMoney + "");
                    if (oilCardPackageBean != null) {

                        showMoney();

                    }
                }
                break;
            case R.id.cl_plan:
                if (oilCardPackageBean != null) {
                    int deadline = oilCardPackageBean.getDeadline();

                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    List<OilOrderDetailBean.RechargeListBean> list = new ArrayList<OilOrderDetailBean.RechargeListBean>();
                    Date now = new Date();
                    for (int i = 0; i < deadline; i++) {
                        Date d;
                        d = now;
                        Calendar c = Calendar.getInstance();
                        c.setTime(d);
                        if (deadline == 1) {
                            c.add(Calendar.MONTH, 1);
                        } else {
                            c.add(Calendar.MONTH, i);
                        }
                        Date temp_date = c.getTime();
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
                        String format = sf.format(temp_date);
                        // LogUtils.e("temp_date" + format);
                        list.add(new OilOrderDetailBean.RechargeListBean(format, 0, monthMoney, 0));
                    }

                    DialogMaker.showMonthDialog(mContext, list);

                }
                break;
            case R.id.cl_coupon:
                if (uid.equalsIgnoreCase("")) {
                    startActivityForResult(new Intent(mContext, LoginActivity.class), login);
                } else {
                    if (coupons == 0) {
                        ToastUtil.showToast("暂无优惠券");
                    } else {
                        startActivityForResult(new Intent(mContext, MeWelfareActivity.class)
                                        .putExtra("type", 1)
                                        .putExtra("flag", 1)
                                        .putExtra("deadline", oilCardPackageBean.getDeadline())
                                        .putExtra("etMoney", money + "")//monthMoney
                                //.putExtra("pid", pid)
                                , loginCode);
                        // startActivity(new Intent(this, MeWelfareActivity.class));
                    }
                }

                break;
            case R.id.bt_pay:
                if (uid.equalsIgnoreCase("")) {
                    startActivityForResult(new Intent(mContext, LoginActivity.class), login);
                } else {
                    if (oilCardBean == null) {
                        ToastUtil.showToast("请先添加油卡");
                    } else {
                        if (isUsed > 0 && couponsBean == null) {
                            DialogMaker.showCommonAlertDialog(mContext, "优惠券提醒", "您有" + isUsed + "张优惠券可用，确定不使用优惠券吗？", new String[]{"不使用", "使用"}, new DialogMaker.DialogCallBack() {
                                @Override
                                public void onButtonClicked(Dialog dialog, int position, Object tag) {
                                    if (position == 0) {
                                        createOrer();
                                    } else if (position == 1) {
                                        startActivityForResult(new Intent(mContext, MeWelfareActivity.class)
                                                        .putExtra("type", 1)
                                                        .putExtra("flag", 1)
                                                        .putExtra("deadline", oilCardPackageBean.getDeadline())
                                                        .putExtra("etMoney", money + "")//monthMoney
                                                //.putExtra("pid", pid)
                                                , loginCode);
                                    }
                                }

                                @Override
                                public void onCancelDialog(Dialog dialog, Object tag) {
                                    ToastUtil.showToast("cancel");

                                }
                            }, false, true, "c");
                        } else {
                            createOrer();
                        }


                    }
                }

                break;


        }
    }

    private void createOrer() {
        int fid = 0;
        if (couponsBean != null) {
            fid = couponsBean.getId();
        }
        if (oilCardPackageBean.getDeadline() == 1) {
            ToastUtil.showToast("当天购买，下月充值日到账");
        }
        startActivity(new Intent(mContext, OilCardPayActivity.class)
                .putExtra("uid", uid)
                .putExtra("fuelCardId", oilCardBean.getId() + "")  //油卡id
                // .putExtra("type", type) //1支付宝 、2微信支付、3云闪付
                .putExtra("amount", allMoney) //单笔金额
                .putExtra("monthMoney", monthMoney)//每月的金额
                .putExtra("pid", oilCardPackageBean.getId()) //产品id
                //  .putExtra("tradeType", UrlConfig.version) //微信支付类型 App；APP   H5:JSAPI
                .putExtra("fid", fid) //优惠券id
                .putExtra("activitytype", 1)// 1：油卡 2：手机 3：直购
                .putExtra("fromPackage", true)
                .putExtra("isUseCoupon", null != couponsBean)
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case addOilCard:
                uid = preferences.getString("uid", "");
                getOilCard();
                break;
            case login:
                uid = preferences.getString("uid", "");
                getOilCard();
                getData();
                getCoupons();
                break;
            case loginCode:
                LogUtils.e("resultCode" + resultCode);
                if (resultCode == Activity.RESULT_OK) {

                    int position = data.getIntExtra("position", 0);
                    LogUtils.e("position" + position);

                    if (couponsList.size() > 0) {

                        couponsBean = couponsList.get(position);

                        LogUtils.e("getName" + couponsBean.getName());

                        tvCoupon.setText(couponsBean.getName());
                        if (oilCardPackageBean != null) {

                            showMoney();

                            int money = monthMoney * oilCardPackageBean.getDeadline();

                       /*     double amount = 0;
                            if (couponsBean != null) {
                                amount = couponsBean.getAmount();
                            }

                            //double s = money * oilCardPackageBean.getRate() - amount;
                            double s = mul(money, oilCardPackageBean.getRate()) - amount;

                            allMoney = s;
                            String numKb = StringCut.getNumKb(s);
                            tvAllMoney.setText(numKb + "");
                            double s1 = add(mul(money, sub(1.0, oilCardPackageBean.getRate())), amount);
                            tvCheaper.setText("(省" + StringCut.getNumKb(s1) + ")");*/
                        }
                           /* String optimal;
                            if (isoptimal(couponsBean)) {

                                optimal = "(已选最优方案)";

                            } else {
                                optimal = "";
                            }
                            switch (couponsBean.getType()) {
                                case 1:// 返现
                                    Spanned spanned = Html.fromHtml(couponsBean.getEnableAmount() + "返(" + couponsBean.getAmount() + ")<font color='#EE4845'>" + optimal + "</font>");
                                    tvCoupon.setText(spanned);
                                    break;
                                case 2:// 加息
                                    tvCoupon.setText(Html.fromHtml("加息<font color='#EE4845'>(" + couponsBean.getRaisedRates() + "%)" + optimal + "</font>"));
                                    break;
                                case 4:// 翻倍券
                                    tvCoupon.setText(Html.fromHtml("翻倍<font color='#EE4845'>(" + couponsBean.getMultiple() + ")" + optimal + "</font>"));
                                    break;
                            }
                            fid = couponsBean.getId() + ""; //优惠券id*/
                    }

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    if (oilCardPackageBean != null) {

                        int money = monthMoney * oilCardPackageBean.getDeadline();

                        couponsBean = null;
                        showMoney();

                       /* double amount = 0;
                        // double s = money * oilCardPackageBean.getRate() - amount;
                        double s = mul(money, oilCardPackageBean.getRate()) - amount;
                        allMoney = s;
                        String numKb = StringCut.getNumKb(s);
                        tvAllMoney.setText(numKb + "");
                        double s1 = add(mul(money, sub(1.0, oilCardPackageBean.getRate())), amount);
                        tvCheaper.setText("(省" + StringCut.getNumKb(s1) + ")");


                        isUsed = 0;
                        for (int i = 0; i < couponsList.size(); i++) {
                            if (oilCardPackageBean.getDeadline() >= couponsList.get(i).getProductDeadline()) {
                                isUsed += 1;
                            }
                        }
                        tvCoupon.setText(isUsed + "张");*/
                    }

                }
                break;

        }

    }

    /**
     * 我的油卡
     */
    private void getOilCard() {
        LogUtils.e("我的油卡" + uid);
        // TODO: 2018/11/8
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
                            if (arr.isEmpty()) {
                                return;
                            }

                            List<OilCardBean> oilCardBeans = JSON.parseArray(arr.toJSONString(), OilCardBean.class);

                            // LogUtils.e("我的油卡" + oilCardBeans.size());

                            if (oilCardBeans.size() > 0) {

                                oillist.clear();

                                oillist.addAll(oilCardBeans);
                                oillist.add(new OilCardBean(0, "", 0, 0, 0, 99, 0));
                                oilCardBean = oillist.get(0);
                                coverFlowAdapter.notifyDataSetChanged();

                                rlNoOilcard.setVisibility(View.GONE);
                                llOilcard.setVisibility(View.VISIBLE);


                                // meEtouRechargeAdapter.notifyDataSetChanged();
                            } else {
                                rlNoOilcard.setVisibility(View.VISIBLE);
                                llOilcard.setVisibility(View.GONE);
                                // tv_footer.setText("全部加载完毕");
                                // footerlayout.setVisibility(View.VISIBLE);
                                // pb.setVisibility(View.GONE);
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
                    }
                });

    }

    // 套餐列表
    private void getData() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.SetMeal)
                .addParams("type", "1") //1是套餐，2是即时
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        //  LogUtils.e("--->套餐列表：" + response);
                        dismissDialog();

                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray arr = map.getJSONArray("list");

                            List<OilCardPackageBean> mrows_List = JSON.parseArray(arr.toJSONString(), OilCardPackageBean.class);
                            // LogUtils.e("套餐" + mrows_List.size());

                            if (mrows_List.size() > 0) {

//                                sort(mrows_List);

                                list3.clear();
                                list3.addAll(mrows_List);


                                if (pid != 0) {
                                    for (int i = 0; i < list3.size(); i++) {

                                        OilCardPackageBean bean = list3.get(i);

                                        LogUtils.e(bean.getFullName() + pid + "套餐" + bean.getId() + "i" + i);

                                        if (bean.getId() == pid) {

                                            meEtouRechargeAdapter.setPosition(i);

                                            oilCardPackageBean = list3.get(i);
                                            showMoney();
                                        }
                                    }
                                } else {
                                    oilCardPackageBean = list3.get(0);

                                    showMoney();
                                }

                                tvMonth.setText(oilCardPackageBean.getDeadline() + "个月");


                                int num = 0;
                                for (int i = 0; i < list3.size(); i++) {
                                    OilCardPackageBean bean = list3.get(i);
                                    if (bean.getId() == pid) {
                                        num = i;
                                    }
                                }
                                rvPackage.scrollToPosition(num);

                                meEtouRechargeAdapter.notifyDataSetChanged();

                                showMonthText();

                               /* int money = monthMoney * oilCardPackageBean.getDeadline();

                                double s = money * oilCardPackageBean.getRate();
                                allMoney = s;
                                tvAllMoney.setText(StringCut.getNumKb(s) + "");

                               // double s1 = add(mul(money, sub(1.0,oilCardPackageBean.getRate())),amount);
                                tvCheaper.setText("(省" + StringCut.getNumKb(money * (1 - oilCardPackageBean.getRate())) + ")");
                                    */

                            } else {
                                // tv_footer.setText("全部加载完毕");
                                // footerlayout.setVisibility(View.VISIBLE);
                                // pb.setVisibility(View.GONE);
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
                    }
                });
    }

    /**
     * 优惠券
     */
    private void getCoupons() {
        if (uid.equalsIgnoreCase("")) {
            return;
        }
        OkHttpUtils.post()
                .url(UrlConfig.CONPONSUNUSE)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("status", "0")
                .addParams("type", "1")   //0 体验金
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                //    LogUtils.e(preferences.getString("uid", "") + "优惠券" + result);
                //ptr_conponsunuse.refreshComplete();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONArray objrows = objmap.getJSONArray("list");
                    List<CouponsBean> couponsBeans = JSON.parseArray(objrows.toJSONString(), CouponsBean.class);

                    //版本兼容
//					for (int i = 0; i < lslbs.size(); i++) {
//						if(lslbs.get(i).getType()!=4&lslbs.get(i).getType()!=1&lslbs.get(i).getType()!=2&lslbs.get(i).getType()!=3){
//							lslbs.remove(i);
//							i--;
//						}
//					}
                    if (couponsBeans.size() > 0) {

                        couponsList.clear();
                        couponsList.addAll(couponsBeans);

                        if (oilCardPackageBean != null) {

                            isUsed = 0;
                            for (int i = 0; i < couponsList.size(); i++) {
                                if (oilCardPackageBean.getDeadline() >= couponsList.get(i).getProductDeadline()) {
                                    isUsed += 1;
                                }
                            }
                            // tvCoupon.setText(isUsed + "张");
                        } else {

                        }
                        tvCoupon.setText(couponsBeans.size() + "张");
                        coupons = couponsBeans.size();
                    } else {
                        tvCoupon.setText("0张");
                    }
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    //getActivity().finish();
//					new show_Dialog_IsLogin(getActivity()).show_Is_Login() ;
                } else {
                    ToastMaker.showShortToast("服务器异常");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                ToastMaker.showShortToast("请检查网络");
            }
        });

    }


    /**
     * 计算 显示 金额
     */
    public void showMoney() {

        if (oilCardPackageBean == null) {
            return;
        } else {

            money = Arith.mul(monthMoney, oilCardPackageBean.getDeadline());
            //int money = monthMoney * oilCardPackageBean.getDeadline();
            //int money = oilCardPackageBean.getLeastaAmount();
            double rate = oilCardPackageBean.getRate();

            double div = Arith.sub(1.0, rate);//减 折扣了

            double mul2 = Arith.mul(money, rate);//乘 总计
            //LogUtils.e(money+"mul2/"+mul2+"/"+rate);

            double mul = Arith.mul(money, div);//节省

            double coupons = 0.0;
            if (couponsBean != null) {
                //monthMoney money
                if (money >= couponsBean.getEnableAmount() && oilCardPackageBean.getDeadline() >= couponsBean.getProductDeadline()) {
                    coupons = couponsBean.getAmount();

                } else {
                    couponsBean = null;
                }

                //  coupons = couponsBean.getAmount();
            }

            double mul1 = Arith.add(mul, coupons); //加

            allMoney = Arith.sub(mul2, coupons); //减

            LogUtils.e(monthMoney + "//" + money + "//" + allMoney + "金额" + StringCut.getNumKb(allMoney) + "(省" + StringCut.getNumKb(mul1));

            tvAllMoney.setText(StringCut.getNumKb(allMoney) + "");

            tvCheaper.setText("(省" + StringCut.getNumKb(mul1) + ")");

            //充3个月 原价3000元 折扣价2910元 省90元
            //充一个月套餐,原价 500.0 元,折扣价 490.0 元, 共为您省去 10.0 元。
            Spanned spanned = Html.fromHtml("充" + oilCardPackageBean.getDeadline()
                    + "个月套餐,原价 <del><font color='#373A41'>" +
                    money + "</font></del> 元,折扣价 <font color='#373A41'>"
                    + allMoney + "</font> 元, 共为您省去 <font color='#373A41'>" + mul1 + "</font>元");

            tvExplan.setText(spanned);

            showCouponsUsed();
        }
    }

    /**
     * 展示多少张优惠券可用
     */
    private void showCouponsUsed() {
        if (oilCardPackageBean == null) {
            return;
        }

        double money = Arith.mul(monthMoney, oilCardPackageBean.getDeadline());
        //allMoneyLast = monthMoney * oilCardPackageBean.getDeadline();
        isUsed = 0;
        if (couponsList.size() > 0) {
            for (int i = 0; i < couponsList.size(); i++) {

                if (money >= couponsList.get(i).getEnableAmount() && oilCardPackageBean.getDeadline() >= couponsList.get(i).getProductDeadline()) {
                    isUsed += 1;
                    //couponsUsedList.add(couponsList.get(i).getId());
                }
            }
            // tvCoupon.setText(isUsed + "张");
        } else {

            // tvCoupon.setText(0 + "张");
        }

        if (couponsBean != null) {
            tvCoupon.setText(couponsBean.getName());
        } else {
            tvCoupon.setText(isUsed + "张");
        }
    }

    /**
     *      * 两个Double数相加
     *      * @param v1
     *      * @param v2
     *      * @return Double
     *      
     */
    public double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     *      * 两个double数相减
     *      * @param v1
     *      * @param v2
     *      * @return double
     *      
     */
    public double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }


    /**
     * double 相成
     */
    public double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }


    /**
     * 排序
     */
    public void sort(List<OilCardPackageBean> list) {

        //这边前面两个是推荐的，按照折扣力度最大的两个排，后面的是正常的，按照1个月、3个月6个月这样排、


        //1,创建TreeSet集合对象,因为String本身就具备比较功能,但是重复不会保留,所以我们用比较器
        TreeSet<OilCardPackageBean> ts2 = new TreeSet<>(new Comparator<OilCardPackageBean>() {

            @Override
            public int compare(OilCardPackageBean s1, OilCardPackageBean s2) {

                int num = s1.getDeadline() - s2.getDeadline();
                return num == 0 ? 1 : num;

                //  int num = s1.compareTo(s2);					//比较内容为主要条件
                // return num == 0 ? 1 : num;					//保留重复
            }
        });


        TreeSet<OilCardPackageBean> ts = new TreeSet<>();
        //2,将list集合中所有的元素添加到TrreSet集合中,对其排序,保留重复
        ts.addAll(list);

        List<OilCardPackageBean> list1 = new ArrayList<>();

        list1.addAll(ts);


        //3,清空list集合
        list3.clear();
        //4,将TreeSet集合中排好序的元素添加到list中

        // list3.addAll(ts);

        list3.addAll(list1.subList(0, 2));

        ts2.addAll(list1.subList(2, list1.size()));
        list3.addAll(ts2);


        LogUtils.e("套餐---------------------" + pid);

        if (pid != 0) {
            for (int i = 0; i < list3.size(); i++) {

                OilCardPackageBean bean = list3.get(i);

                LogUtils.e(bean.getFullName() + pid + "套餐" + bean.getId() + "i" + i);

                if (bean.getId() == pid) {

                    meEtouRechargeAdapter.setPosition(i);

                    oilCardPackageBean = list3.get(i);
                    showMoney();
                }
            }
        } else {
            oilCardPackageBean = list3.get(0);

            showMoney();
        }

        tvMonth.setText(oilCardPackageBean.getDeadline() + "个月");

        //List<OilCardPackageBean> oilCardPackageBeans = list3.subList(0, 2);

        // List<OilCardPackageBean> oilCardPackageBeans1 = list3.subList(2, list3.size());

        //  ts2.addAll(oilCardPackageBeans1);

        //list3.clear();
        // list3.addAll(oilCardPackageBeans);
        //  list3.addAll(ts2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();


        if (list3.size() == 0) {
            return;
        }
        pid = preferences.getInt("oid_pid", 0);

        LogUtils.e("套餐---------------------" + pid);

        if (pid != 0) {
            for (int i = 0; i < list3.size(); i++) {

                OilCardPackageBean bean = list3.get(i);

                LogUtils.e(bean.getFullName() + pid + "套餐" + bean.getId() + "i" + i);

                if (bean.getId() == pid) {

                    meEtouRechargeAdapter.setPosition(i);
                    oilCardPackageBean = list3.get(i);
                    showMoney();


                }
            }

            //rvPackage.smoothScrollToPosition(i + 2);
            //rvPackage.setSelected(true);
        }

//          EventBus.getDefault().register(this);
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
