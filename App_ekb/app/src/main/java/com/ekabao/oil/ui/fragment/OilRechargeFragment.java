package com.ekabao.oil.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.CoverFlowAdapter;
import com.ekabao.oil.adapter.OilCardPackageAdapter;
import com.ekabao.oil.bean.CouponsBean;
import com.ekabao.oil.bean.OilCardBean;
import com.ekabao.oil.bean.OilCardPackageBean;
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
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.Arith;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.ToastUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * ${APP_NAME}  App_akzj
 *
 * @time 2019/3/15 17:55
 * Created by lj on 2019/3/15 17:55.
 */

public class OilRechargeFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.vp_overlap)
    ViewPager vpOverlap;
   /* @BindView(R.id.pager_container)
    PagerContainer pagerContainer;*/
    @BindView(R.id.ll_oilcard)
    LinearLayout llOilcard;
    @BindView(R.id.ib_add_oilcard)
    ImageButton ibAddOilcard;
    @BindView(R.id.ib_oilcard_buy)
    ImageButton ibOilcardBuy;
    @BindView(R.id.rl_no_oilcard)
    RelativeLayout rlNoOilcard;
    @BindView(R.id.rv_package)
    RecyclerView rvPackage;
    @BindView(R.id.tv_explan)
    TextView tvExplan;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.ll_coupon)
    LinearLayout llCoupon;
    @BindView(R.id.tv_all_money)
    TextView tvAllMoney;
    @BindView(R.id.tv_cheaper)
    TextView tvCheaper;
    @BindView(R.id.bt_pay)
    Button btPay;
    Unbinder unbinder;


    /**
     * 油卡即时充值
     *
     * @time 2018/11/3 14:52
     * Created by lj
     */
    private SharedPreferences preferences;
    private String uid;

    private ArrayList<OilCardPackageBean> list3 = new ArrayList<>();
    OilCardPackageAdapter meEtouRechargeAdapter;
    private ArrayList<OilCardBean> oillist = new ArrayList<>();
    private CoverFlowAdapter coverFlowAdapter;
    private static final int addOilCard = 16540; //去添加油卡
    private static final int login = 16541; //登录

    private OilCardPackageBean oilCardPackageBean; //套餐
    private OilCardBean oilCardBean; //默认的油卡

    private List<CouponsBean> couponsList = new ArrayList<CouponsBean>();
    private CouponsBean couponsBean; //优惠券
    private int isUsed; //可用优惠券数量
    private int coupons = 0;// 优惠券的数量
    private static final int loginCode = 10156; //选择优惠券
    private double amount; //实际支付的金额

    public static OilRechargeFragment newInstance() {

        Bundle args = new Bundle();

        OilRechargeFragment fragment = new OilRechargeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_oil_card_recharge;
    }

    @Override
    protected void initParams() {
        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");


        ibAddOilcard.setOnClickListener(this);
        llCoupon.setOnClickListener(this);
        btPay.setOnClickListener(this);
        ibOilcardBuy.setOnClickListener(this);
        /*list3.add(new OilCardPackageBean(1, 0.98f, "100元", "支付98元"));
        list3.add(new OilCardPackageBean(2, 0.92f, "200元", "支付196元"));
        list3.add(new OilCardPackageBean(3, 0.83f, "500元", "支付494元"));
         list3.add(new OilCardPackageBean(4, 0.98f, "1000元", "支付98元"));
        list3.add(new OilCardPackageBean(5, 0.92f, "2000元", "支付1970元"));
        list3.add(new OilCardPackageBean(6, 0.83f, "5000元", "支付4910元"));*/

       /* String response="{map : {list:[{code:CP-2018102315320273912,isInterest:0,lid:1,interestType:0,type:1,repayType:1,sid:1,isDeductible:0,rate:0.97,id:95,activityRate:0,tag:,deadline:3,isCash:1,maxAmount:0,billType:1,fullName:9.7折3个月加油套餐,increasAmount:100,leastaAmount:100,simpleName:9.7折3个月加油套餐,raiseDeadline:0,startDate:1541382230000,isHot:0,status:5,isDouble:0},{code:CP-2018102315320273913,lid:1,billType:1,interestType:0,fullName:8.2折13个月加油套餐,type:1,repayType:1,increasAmount:100,sid:1,leastaAmount:100,simpleName:8.2折13个月加油套餐,rate:0.82,raiseDeadline:0,id:96,activityRate:0,tag:,deadline:13,maxAmount:0,startDate:1541382390000,status:5,isDouble:0},{code:CP-2018102315320273914,lid:1,billType:1,interestType:0,fullName:7.6折18个月加油套餐,type:1,repayType:1,increasAmount:100,sid:1,leastaAmount:100,simpleName:7.6折18个月加油套餐,rate:0.76,raiseDeadline:0,id:97,activityRate:0,tag:限量抢购,deadline:18,maxAmount:0,startDate:1541382446000,isHot:1,status:5,isDouble:0},{code:CP-2018102315320273915,lid:1,billType:1,interestType:0,fullName:7.1折24个月加油套餐,type:1,repayType:1,increasAmount:100,sid:1,leastaAmount:100,simpleName:7.1折24个月加油套餐,rate:0.71,raiseDeadline:0,id:98,activityRate:0,tag:人气套餐,deadline:24,maxAmount:0,startDate:1541382537000,isHot:1,status:5,isDouble:0}]}}";
        JSONObject obj = JSON.parseObject(response);

        JSONObject map = obj.getJSONObject("map");*/


        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        rvPackage.setLayoutManager(gridLayoutManager);
        // rvPackage.addItemDecoration(new SpaceItemDecoration(10, 10, 10, 10));
        meEtouRechargeAdapter = new OilCardPackageAdapter(list3, 0, 4);
        rvPackage.setAdapter(meEtouRechargeAdapter);


        meEtouRechargeAdapter.setOnItemClickLitener(new OilCardPackageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int positon) {
                meEtouRechargeAdapter.setPosition(positon);
                meEtouRechargeAdapter.notifyDataSetChanged();
                oilCardPackageBean = list3.get(positon);

                // tvMonth.setText(oilCardPackageBean.getDeadline() + "个月");

                if (oilCardPackageBean != null) {

                    int money = oilCardPackageBean.getLeastaAmount();
                    double rate = oilCardPackageBean.getRate();

                    //tvAllMoney.setText(StringCut.getNumKb(money * rate) + "");
                    //tvAllMoney.setText(StringCut.getNumKb(mul(money, rate)) + "");
                    // tvCheaper.setText("(省" + StringCut.getNumKb(money * (1 - rate)) + ")");
                    //tvCheaper.setText("(省" + StringCut.getNumKb(mul(money, (1 - rate))) + ")");

                    showMoney();
                   /* int money = monthMoney * oilCardPackageBean.getDeadline();

                    double amount = 0;
                    if (couponsBean != null) {
                        amount = couponsBean.getAmount();
                    }

                    double v2 = money * oilCardPackageBean.getRate() - amount;
                    tvAllMoney.setText(StringCut.getNumKb(v2) + "");

                    double v1 = money * (1 - oilCardPackageBean.getRate()) + amount;
                    tvCheaper.setText("(省" + StringCut.getNumKb(v1) + ")");*/
                }


            }
        });

        //我的油卡
       // ViewPager pager = pagerContainer.getViewPager();

        coverFlowAdapter = new CoverFlowAdapter(mContext, oillist, 2);

       // pager.setAdapter(coverFlowAdapter);
        vpOverlap.setAdapter(coverFlowAdapter);

      /*  new CoverFlow.Builder()
                .with(pager)
                .scale(0f)
                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.dp_m_10))
                .spaceSize(0f)
                .build();
        //pagerContainer.setClipChildren(true);
        //pagerContainer.setOverlapEnabled(true);
        //
        pager.setOffscreenPageLimit(15);*/


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
             //   ToastMaker.showShortToast("onAddOilCardClick");
                startActivityForResult(new Intent(mContext,
                                AddOilCardActivity.class)
                                .putExtra("uid", uid),
                        addOilCard);
            }
        });
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
            case R.id.ll_coupon:
                if (uid.equalsIgnoreCase("")) {
                    startActivityForResult(new Intent(mContext, LoginActivity.class), login);
                } else {
                    if (coupons == 0) {
                        ToastUtil.showToast("暂无优惠券");
                    } else {
                        startActivityForResult(new Intent(mContext, MeWelfareActivity.class)
                                        .putExtra("type", 2)
                                        .putExtra("flag", 1)
                                        .putExtra("etMoney", oilCardPackageBean.getLeastaAmount()+ "")
                                //.putExtra("pid", pid)
                                , loginCode);
                        // startActivity(new Intent(mContext, MeWelfareActivity.class));
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

                        int money = oilCardPackageBean.getLeastaAmount();

                        // tvAllMoney.setText(StringCut.getNumKb(money * oilCardPackageBean.getRate()) + "");

                        //BigDecimal b1 = new BigDecimal(Double.toString(money));
                        BigDecimal b1 = new BigDecimal(money + "");

                        BigDecimal b2 = new BigDecimal(Double.toString(oilCardPackageBean.getRate()));
                        double v1 = b1.multiply(b2).doubleValue();

                        int fid = 0;
                        if (couponsBean != null) {
                            fid = couponsBean.getId();
                        }

                        LogUtils.e("--->Double.toString(money)" + v1);

                        startActivity(new Intent(mContext, OilCardPayActivity.class)
                                .putExtra("uid", uid)
                                .putExtra("fuelCardId", oilCardBean.getId()+"")  //油卡id
                                // .putExtra("type", type) //1支付宝 、2微信支付、3云闪付
                                .putExtra("amount",amount ) //单笔金额 b1.doubleValue()
                                .putExtra("pid", oilCardPackageBean.getId()) //产品id
                                //  .putExtra("tradeType", UrlConfig.version) //微信支付类型 App；APP   H5:JSAPI
                                // .putExtra("money", v1)
                                .putExtra("fid", fid) //优惠券id
                                .putExtra("money", money)
                                .putExtra("activitytype", 1)// 1：油卡 2：手机 3：直购
                                .putExtra("fromPackage", false)
                        );
                    }
                }

                break;

        }
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

                         /*   int money = monthMoney * oilCardPackageBean.getDeadline();

                            double amount = 0;
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

                        couponsBean = null;
                        showMoney();

                       /* int money = monthMoney * oilCardPackageBean.getDeadline();
                        couponsBean = null;
                        double amount = 0;
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
                            //LogUtils.e("我的油卡" + arr.getString(0));
                            if (arr.isEmpty()) {
                                return;
                            }

                            List<OilCardBean> oilCardBeans = JSON.parseArray(arr.toJSONString(), OilCardBean.class);

                            LogUtils.e("我的油卡" + oilCardBeans.size());

                            if (oilCardBeans.size() > 0) {

                                oillist.clear();

                                oillist.addAll(oilCardBeans);
                                oillist.add(new OilCardBean(0,"",0,0,0,99,0));

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
//                            new show_Dialog_IsLogin(MessageCenterActivity.mContext).show_Is_Login();
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
                .addParams("type", "2") //1是套餐，2是即时
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
                            LogUtils.e("套餐" + mrows_List.size());

                            if (mrows_List.size() > 0) {

                                // oilCardPackageBean = mrows_List.get(0);
                                list3.clear();
                                list3.addAll(mrows_List);

                              //  sort(mrows_List);
                                //  list3.addAll(mrows_List);
                                meEtouRechargeAdapter.notifyDataSetChanged();

                                oilCardPackageBean = list3.get(0);

                                //int money = oilCardPackageBean.getLeastaAmount();
                                // tvAllMoney.setText(StringCut.getNumKb(money * oilCardPackageBean.getRate()) + "");
                                //tvCheaper.setText("(省" + StringCut.getNumKb(money * (1 - oilCardPackageBean.getRate())) + ")");


                                int money = oilCardPackageBean.getLeastaAmount();
                                double rate = oilCardPackageBean.getRate();

                                amount = oilCardPackageBean.getLeastaAmount();

                                showMoney();
                                //tvAllMoney.setText(StringCut.getNumKb(money * rate) + "");
                                //tvAllMoney.setText(StringCut.getNumKb(mul(money, rate)) + "");
                                // tvCheaper.setText("(省" + StringCut.getNumKb(money * (1 - rate)) + ")");
                                // tvCheaper.setText("(省" + StringCut.getNumKb(mul(money, (1 - rate))) + ")");

                            } else {
                                // tv_footer.setText("全部加载完毕");
                                // footerlayout.setVisibility(View.VISIBLE);
                                // pb.setVisibility(View.GONE);
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
                .addParams("type", "2")   //0 体验金
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
               // LogUtils.e(preferences.getString("uid", "") + "优惠券" + result);
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

                            for (int i = 0; i < couponsList.size(); i++) {
                                LogUtils.e("amount"+amount+"/"+couponsList.get(i).getEnableAmount());
                                if (amount >= couponsList.get(i).getEnableAmount()) {
                                    isUsed += 1;
                                    //couponsUsedList.add(couponsList.get(i).getId());
                                }
                            }
                           // tvCoupon.setText(isUsed + "张");


                           /* isUsed = 0;
                            for (int i = 0; i < couponsList.size(); i++) {
                                if (oilCardPackageBean.getDeadline() >= couponsList.get(i).getProductDeadline()) {
                                    isUsed += 1;
                                }
                            }
                            tvCoupon.setText(isUsed + "张");*/

                        } else {

                        }
                        tvCoupon.setText(couponsBeans.size()+"张");
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
     * double 相成
     */
    public double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 计算 显示 金额
     */
    public void showMoney() {

        if (oilCardPackageBean == null) {
            return;
        } else {
            int money = oilCardPackageBean.getLeastaAmount();
            double rate = oilCardPackageBean.getRate();

            double div = Arith.sub(1.0, rate);//减

            double mul2 = Arith.mul(money, rate);//乘 总计

            double mul = Arith.mul(money, div);//节省

            double coupons = 0;
            if (couponsBean != null) {
                if (amount >= couponsBean.getEnableAmount()) {
                    //couponsUsedList.add(couponsList.get(i).getId());
                    coupons = couponsBean.getAmount();
                }else {
                    couponsBean = null;
                }

            }

            double mul1 = Arith.add(mul, coupons); //加

            amount = Arith.sub(mul2, coupons); //减

            LogUtils.e("金额" + StringCut.getNumKb(amount) + "(省" + StringCut.getNumKb(mul1));

            tvAllMoney.setText(StringCut.getNumKb(amount) + "");

            tvCheaper.setText("(省" + StringCut.getNumKb(mul1) + ")");


            //您选择油卡直充 100.0 元,折扣价 99.5 元,共为 您省去 0.5 元。
            //充一个月套餐,原价 500.0 元,折扣价 490.0 元, 共为您省去 10.0 元。
            Spanned spanned = Html.fromHtml("您选择油卡直充 <del><font color='#373A41'>" +
                    money + "</font></del> 元,折扣价 <font color='#373A41'>"
                    + amount + "</font> 元, 共为您省去 <font color='#373A41'>" + mul1 + "</font>元");

            tvExplan.setText(spanned);


            showCouponsUsed();
        }
    }
    /**
     * 展示多少张优惠券可用
     * */
    private void showCouponsUsed() {
        if (oilCardPackageBean == null) {
            return;
        }

        // double money = Arith.mul(monthMoney, oilCardPackageBean.getDeadline());
        //allMoneyLast = monthMoney * oilCardPackageBean.getDeadline();
        isUsed = 0;
        if (couponsList.size()>0) {
            for (int i = 0; i < couponsList.size(); i++) {

                if (amount >= couponsList.get(i).getEnableAmount()) {
                    isUsed += 1;
                    //couponsUsedList.add(couponsList.get(i).getId());
                }
            }
            // tvCoupon.setText(isUsed + "张");
        } else {
            // tvCoupon.setText(0 + "张");
        }
        if (couponsBean!=null){
            tvCoupon.setText(couponsBean.getName());
        }else {
            tvCoupon.setText(isUsed + "张");
        }

    }
    /**
     * 排序
     */
    public void sort(List<OilCardPackageBean> list) {
        TreeSet<OilCardPackageBean> ts = new TreeSet<>();
        //2,将list集合中所有的元素添加到TrreSet集合中,对其排序,保留重复
        ts.addAll(list);
        //3,清空list集合
        list3.clear();
        //4,将TreeSet集合中排好序的元素添加到list中
        list3.addAll(ts);
    }


/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(mContext, rootView);
        return rootView;
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // unbinder.unbind();
    }
}
