package com.ekabao.oil.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.ekabao.oil.ui.activity.me.CallCenterActivity;
import com.ekabao.oil.ui.activity.me.MeWelfareActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.Arith;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.ToastUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class OilCardBuyPackageActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.ib_add_oilcard)
    ImageButton ibAddOilcard;
    @BindView(R.id.ib_oilcard_buy)
    TextView ibOilcardBuy;
    @BindView(R.id.rl_no_oilcard)
    RelativeLayout rlNoOilcard;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.ib_add)
    ImageButton ibAdd;
    @BindView(R.id.ib_reduce)
    ImageButton ibReduce;
    @BindView(R.id.rv_package)
    RecyclerView rvPackage;
    @BindView(R.id.tv_explan)
    TextView tvExplan;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.ll_month)
    RelativeLayout llMonth;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.ll_coupon)
    RelativeLayout llCoupon;
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
    private OilCardPackageFragmentAdapter meEtouRechargeAdapter; // OilCardPackageAdapter
    private CoverFlowAdapter coverFlowAdapter;
    private int monthMoney = 500;// 月充值金额
    private double allMoney;// 总金额
    private int coupons = 0;// 优惠券的数量
    private double dismoney;//优惠金额

    private static final int addOilCard = 16540; //去添加油卡
    private static final int login = 16541; //登录
    private OilCardPackageBean oilCardPackageBean; //套餐

    private List<CouponsBean> couponsList = new ArrayList<CouponsBean>();
    private CouponsBean couponsBean; //优惠券
    private int isUsed; //可用优惠券数量
    private static final int loginCode = 10156; //选择优惠券


    private int pid;
    private double money; //优惠券用到的,原总价


    /**
     * 领取油卡-->选择套餐
     *
     * @time 2019/2/21 14:47
     * Created by
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_oil_card_buy_package);
        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_oil_card_buy_package;
    }

    @Override
    protected void initParams() {

        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");

        Intent intent = getIntent();
        pid = intent.getIntExtra("pid", 0);

        int money = intent.getIntExtra("money", 0);
        if (money != 0) {
            monthMoney = money;
            tvMoney.setText(monthMoney + "");
        }


        titleLeftimageview.setOnClickListener(this);
        titleRightimageview.setVisibility(View.VISIBLE);
        titleRightimageview.setOnClickListener(this);
        titleCentertextview.setText("油卡优惠套餐");
        ibAdd.setOnClickListener(this);
        ibReduce.setOnClickListener(this);
        ibAddOilcard.setOnClickListener(this);
        ibOilcardBuy.setOnClickListener(this);
        llCoupon.setOnClickListener(this);
        llMonth.setOnClickListener(this);
        btPay.setOnClickListener(this);
      /*  list3.add(new OilCardPackageBean(1, 0.98f, "9.8折", "3个月加油套餐"));
        list3.add(new OilCardPackageBean(2, 0.92f, "9.2折", "6个月加油套餐"));
        list3.add(new OilCardPackageBean(3, 0.83f, "8.3折", "12个月加油套餐"));
        list3.add(new OilCardPackageBean(4, 0.75f, "7.5折", "20个月加油套餐"));
        list3.add(new OilCardPackageBean(5, 0.71f, "7.1折", "24个月加油套餐"));*/
        tvMonth.setTextColor(getResources().getColor(R.color.primary));


      /*  GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvPackage.setLayoutManager(gridLayoutManager);
        // rvPackage.addItemDecoration(new SpaceItemDecoration(10, 10, 10, 10));
        meEtouRechargeAdapter = new OilCardPackageAdapter(list3, 0, 1);
        rvPackage.setAdapter(meEtouRechargeAdapter);*/

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rvPackage.setLayoutManager(linearLayoutManager);

        // rvPackage.addItemDecoration(new SpaceItemDecoration(10, 10, 10, 10));
        meEtouRechargeAdapter = new OilCardPackageFragmentAdapter(list3, 0, 1);
        rvPackage.setAdapter(meEtouRechargeAdapter);

        //套餐点击
        meEtouRechargeAdapter.setOnItemClickLitener(new OilCardPackageFragmentAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int positon) {
                meEtouRechargeAdapter.setPosition(positon);


                meEtouRechargeAdapter.notifyDataSetChanged();

                oilCardPackageBean = list3.get(positon);

                if (oilCardPackageBean.getDeadline() == 1) {
                    tvMonth.setText("一个月套餐将在次月当天充值");
                    tvMonth.setTextColor(getResources().getColor(R.color.primary));

                } else {

                    tvMonth.setTextColor(getResources().getColor(R.color.text_black));
                    tvMonth.setText(oilCardPackageBean.getDeadline() + "个月");
                }


                if (oilCardPackageBean != null) {

                    showMoney();


                }

            }
        });


       /*
        MultiplePagerAdapter adapter = new MultiplePagerAdapter(mContext, list);
        vpOverlap.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/
        getData();
        if (uid.equalsIgnoreCase("")) {
            //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
            //rlNoOilcard.setVisibility(View.VISIBLE);
            // llOilcard.setVisibility(View.GONE);
        } else {
            //llOilcard
            //  getOilCard();
            getCoupons();
        }
        //油卡类型 1:中石化 2:中石油




     /*   rvPackage.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });*/
      /*  //解决数据加载不完的问题
        rvPackage.setNestedScrollingEnabled(false);
        rvPackage.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        rvPackage.setFocusable(false);*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.title_rightimageview: //击进入充值问题页面
                startActivity(new Intent(OilCardBuyPackageActivity.this, CallCenterActivity.class));
                break;

           /* case R.id.ib_add_oilcard:

                if (uid.equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivityForResult(new Intent(this, LoginActivity.class), login);
                } else {
                    //llOilcard
                    //去添加油卡
                    startActivityForResult(new Intent(this, AddOilCardActivity.class)
                                    .putExtra("uid", uid),
                            addOilCard);
                }

                break;
            case R.id.ib_oilcard_buy: //领取油卡
                if (uid.equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivityForResult(new Intent(this, LoginActivity.class), login);
                } else {
                    //llOilcard
                    //去添加油卡
                    startActivity(new Intent(this, OilCardBuyActivity.class));
                }

                break;*/

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
                    ToastMaker.showShortToast("单月最低充值100元");
                    break;
                } else {
                    monthMoney = monthMoney - 100;
                    tvMoney.setText(monthMoney + "");
                    if (oilCardPackageBean != null) {

                        showMoney();



                    /*    int money = monthMoney * oilCardPackageBean.getDeadline();

                        double amount = 0;
                        if (couponsBean != null) {
                            amount = couponsBean.getAmount();
                        }

                        double s = money * oilCardPackageBean.getRate() - amount;
                        allMoney = s;
                        String numKb = StringCut.getNumKb(s);
                        tvAllMoney.setText(numKb + "");
                        //double s1 = money * (1 - oilCardPackageBean.getRate()) + amount;
                        double s1 = add(mul(money, sub(1.0,oilCardPackageBean.getRate())),amount);
                        tvCheaper.setText("(省" + StringCut.getNumKb(s1) + ")");*/
                    }
                }
                break;
            case R.id.ll_month:
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

                    DialogMaker.showMonthDialog(this, list);

                }
                break;
            case R.id.ll_coupon:
                if (uid.equalsIgnoreCase("")) {
                    startActivityForResult(new Intent(this, LoginActivity.class), login);
                } else {
                    if (coupons == 0) {
                        ToastUtil.showToast("暂无优惠券");
                    } else {
                        startActivityForResult(new Intent(this, MeWelfareActivity.class)
                                        .putExtra("type", 1)
                                        .putExtra("flag", 1)
                                        .putExtra("deadline", oilCardPackageBean.getDeadline())
                                        .putExtra("etMoney", money + "") //monthMoney
                                //.putExtra("pid", pid)
                                , loginCode);
                        // startActivity(new Intent(this, MeWelfareActivity.class));
                    }
                }

                break;
            case R.id.bt_pay:
                if (isUsed > 0 && couponsBean == null) {
                    DialogMaker.showCommonAlertDialog(this, "优惠券提醒", "您有" +isUsed + "张优惠券可用，确定不使用优惠券吗？", new String[]{"不使用", "使用"}, new DialogMaker.DialogCallBack() {
                        @Override
                        public void onButtonClicked(Dialog dialog, int position, Object tag) {
                            if (position == 0) {
                               createOrder();
                            }else if(position == 1){
                                startActivityForResult(new Intent(OilCardBuyPackageActivity.this, MeWelfareActivity.class)
                                                .putExtra("type", 1)
                                                .putExtra("flag", 1)
                                                .putExtra("deadline", oilCardPackageBean.getDeadline())
                                                .putExtra("etMoney", money + "") //monthMoney
                                        //.putExtra("pid", pid)
                                        , loginCode);
                            }
                        }

                        @Override
                        public void onCancelDialog(Dialog dialog, Object tag) {
                            ToastUtil.showToast("cancel");

                        }
                    }, false, true, "c");
                }else {
                    createOrder();
                }

                break;


        }
    }

    private void createOrder(){
        int fid = 0;
        if (couponsBean != null) {
            fid = couponsBean.getId();
        }

        setResult(Activity.RESULT_OK, new Intent()
                .putExtra("monthMoney", monthMoney)//每月的金额
                .putExtra("months", oilCardPackageBean.getDeadline())//多少月
                .putExtra("amount", allMoney) //单笔金额
                .putExtra("dismoney", dismoney) //优惠金额
                .putExtra("pid", oilCardPackageBean.getId()) //产品id
                //  .putExtra("tradeType", UrlConfig.version) //微信支付类型 App；APP   H5:JSAPI
                .putExtra("fid", fid) //优惠券id);
        );

        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case addOilCard:
                uid = preferences.getString("uid", "");
                //getOilCard();
                break;
            case login:
                uid = preferences.getString("uid", "");
                // getOilCard();
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
                        LogUtils.e("--->套餐列表：" + response);
                        dismissDialog();

                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray arr = map.getJSONArray("list");

                            List<OilCardPackageBean> mrows_List = JSON.parseArray(arr.toJSONString(), OilCardPackageBean.class);
                            LogUtils.e("套餐" + mrows_List.size());

                            if (mrows_List.size() > 0) {
//                                sort(mrows_List);
//                                list3.addAll(mrows_List);
                                list3.clear();
                                for (int i = 0; i < mrows_List.size(); i++) {
                                    if (mrows_List.get(i).getDeadline() != 1) {
                                        list3.add(mrows_List.get(i));
                                    }
                                }
                                sort(list3);
                                meEtouRechargeAdapter.notifyDataSetChanged();


                                if (pid != 0) {
                                    for (int i = 0; i < mrows_List.size(); i++) {
                                        OilCardPackageBean bean = mrows_List.get(i);
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
                LogUtils.e(preferences.getString("uid", "") + "优惠券" + result);
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
                            tvCoupon.setText(isUsed + "张");
                        } else {
                            tvCoupon.setText("0张");
                        }
                        coupons = couponsBeans.size();
                    } else {
                        tvCoupon.setText("无");
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


            //加
            dismoney = Arith.add(mul, coupons);

            allMoney = Arith.sub(mul2, coupons); //减

            LogUtils.e(monthMoney + "//" + money + "//" + allMoney + "金额" + StringCut.getNumKb(allMoney) + "(省" + StringCut.getNumKb(dismoney));

            tvAllMoney.setText(StringCut.getNumKb(allMoney) + "");

            tvCheaper.setText("(省" + StringCut.getNumKb(dismoney) + ")");

            //充3个月 原价3000元 折扣价2910元 省90元
         /*   Spanned spanned = Html.fromHtml("充" + oilCardPackageBean.getDeadline()
                    + "个月 原价<del><font color='#FF623D'>" +
                    money + "</font></del>元 折扣价<font color='#FF623D'>"
                    + allMoney + "</font>元 省<font color='#FF623D'>" + dismoney + "</font>元");*/

            //充一个月套餐,原价 500.0 元,折扣价 490.0 元, 共为您省去 10.0 元。
            Spanned spanned = Html.fromHtml("充" + oilCardPackageBean.getDeadline()
                    + "个月套餐,原价 <del><font color='#373A41'>" +
                    money + "</font></del> 元,折扣价 <font color='#373A41'>"
                    + allMoney + "</font> 元, 共为您省去 <font color='#373A41'>" + dismoney + "</font>元");

            tvExplan.setText(spanned);

            tvMonth.setText(oilCardPackageBean.getDeadline() + "个月");

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
        //1,创建TreeSet集合对象,因为String本身就具备比较功能,但是重复不会保留,所以我们用比较器
       /* TreeSet<OilCardPackageBean> ts = new TreeSet<>(new Comparator<OilCardPackageBean>() {

            @Override
            public int compare(OilCardPackageBean s1, OilCardPackageBean s2) {


                int num = s1.compareTo(s2);					//比较内容为主要条件
                return num == 0 ? 1 : num;					//保留重复
            }
        });*/

        TreeSet<OilCardPackageBean> ts = new TreeSet<>();
        //2,将list集合中所有的元素添加到TrreSet集合中,对其排序,保留重复
        ts.addAll(list);
        //3,清空list集合
        list3.clear();
        //4,将TreeSet集合中排好序的元素添加到list中
        list3.addAll(ts);
    }
}
