package com.ekabao.oil.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.bean.AddressBean;
import com.ekabao.oil.bean.OilCardPackageBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.me.AddAddressActivity;
import com.ekabao.oil.ui.activity.me.AddressManageActivity;
import com.ekabao.oil.ui.activity.me.CallCenterActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.Arith;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.ToastUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class OilCardBuyActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.ib_add_address)
    ImageButton ibAddAddress;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_all_money)
    TextView tvAllMoney;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.tv_cheaper)
    TextView tvCheaper;
    @BindView(R.id.bt_buy)
    Button btBuy;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.cl_company1)
    ConstraintLayout clCompany1;
    @BindView(R.id.cl_company2)
    ConstraintLayout clCompany2;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.tv_card_type_1)
    TextView tvCardType1;
    @BindView(R.id.tv_card_type_2)
    TextView tvCardType2;
    @BindView(R.id.tv_card_num_1)
    TextView tvCardNum1;
    @BindView(R.id.tv_card_num_2)
    TextView tvCardNum2;
    @BindView(R.id.tv_package_name)
    TextView tvPackageName;
    @BindView(R.id.rl_select_package)
    LinearLayout rlSelectPackage;
    @BindView(R.id.tv_package_money)
    TextView tvPackageMoney;

    @BindView(R.id.tv_freight)
    TextView tvFreight;

    /**
     * 购买油卡
     *
     * @time 2018/11/27 15:11
     * Created by lj
     */
    private SharedPreferences preferences;
    private String uid;
    private static final int addAddres = 16540; //去添加地址
    private static final int login = 16541; //登录
    private static final int oilpackage = 19541; //选择加油套餐

    private AddressBean address;//收货地址

    private ArrayList<OilCardPackageBean> list3 = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();
    private OilCardPackageBean oilCardPackageBean; //套餐
    private int cardNum1, cardNum2;

    private int pid;//加油套餐id
    private int fid;//优惠券id
    private int monthMoney = 0;// 月充值金额
    private double allMoney;// 总金额
    private double disMoney;//优惠金额
    private int months;//多少月

    private int cardType = 1; //油卡类型 1:中石化 2:中石油

    private double freight = 0;//运费

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_oil_card_buy);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_oil_card_buy;
    }

    @Override
    protected void initParams() {
//        rlTitle.setBackgroundColor(getResources().getColor(R.color.primary));
        viewLineBottom.setVisibility(View.GONE);
        titleLeftimageview.setImageResource(R.drawable.fanhui_black);
        titleCentertextview.setText("领取油卡");
        // titleRightimageview.setVisibility(View.VISIBLE);
        // titleRightimageview.setImageResource(R.drawable.icon_request_white);
        // titleRightimageview.setOnClickListener(this);
        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");

        titleLeftimageview.setOnClickListener(this);
        rlAddress.setOnClickListener(this);
        ibAddAddress.setOnClickListener(this);
        clCompany1.setOnClickListener(this);
        clCompany2.setOnClickListener(this);
        btBuy.setOnClickListener(this);
        tvRule.setOnClickListener(this);
        rlSelectPackage.setOnClickListener(this);

        getData();

        if (uid.equalsIgnoreCase("")) {
            //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
            ibAddAddress.setVisibility(View.VISIBLE);
            rlAddress.setVisibility(View.GONE);
        } else {
            //未登录或已登录但未添加收货地址时，显示此内容；
            //ibAddAddress.setVisibility(View.GONE);

            // rlAddress.setVisibility(View.VISIBLE);
            //llOilcard
            getReceiptAddress();

            // getCoupons();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:

                finish();
                break;
            case R.id.title_rightimageview:
                startActivity(new Intent(this, CallCenterActivity.class));
                break;
            case R.id.rl_select_package: //选择加油套餐
                startActivityForResult(new Intent(this, OilCardBuyPackageActivity.class),
                        oilpackage);
                break;

            case R.id.ib_add_address:
                if (uid.equalsIgnoreCase("")) {
                    startActivityForResult(new Intent(this, LoginActivity.class),
                            login);

                } else {
                    if (address == null) {
                        startActivityForResult(new Intent(this,
                                AddAddressActivity.class), addAddres);
                    } else {

                        //未登录或已登录但未添加收货地址时，显示此内容；
                        startActivityForResult(new Intent(this,
                                AddressManageActivity.class), addAddres);
                    }

                    // getCoupons();
                }
                break;

            case R.id.rl_address:

                //未登录或已登录但未添加收货地址时，显示此内容；
                startActivityForResult(new Intent(this,
                        AddressManageActivity.class), addAddres);

                break;

            case R.id.cl_company2: //中石油

                if (cardNum2 == 0) {
                    return;
                }

                if (list3.size() > 0) {
                    for (OilCardPackageBean bean : list3) {
                        if (bean.getSimpleName().contains("中国石油")) {
                            oilCardPackageBean = bean;
                        }
                    }
                }
                clCompany1.setBackgroundResource(R.drawable.bg_oil_card_home);
                clCompany2.setBackgroundResource(R.drawable.bg_oil_card_home_selected);
                tvCardType1.setBackgroundResource(R.color.line_f6);
                tvCardType1.setTextColor(getResources().getColor(R.color.color_3));
                tvCardType2.setBackgroundResource(R.drawable.bg_oil_package_selected);
                tvCardType2.setTextColor(getResources().getColor(R.color.white));

//                cbCompany1.setChecked(false);
//                cbCompany2.setChecked(true);
                cardType = 2; //油卡类型 1:中石化 2:中石油
                break;

            case R.id.cl_company1://中石化
                if (cardNum1 == 0) {
                    return;
                }
                if (list3.size() > 0) {
                    for (OilCardPackageBean bean : list3) {
                        if (bean.getSimpleName().contains("中国石化")) {
                            oilCardPackageBean = bean;
                        }
                    }
                }

                clCompany1.setBackgroundResource(R.drawable.bg_oil_card_home_selected);
                tvCardType1.setBackgroundResource(R.drawable.bg_oil_package_selected);
                tvCardType1.setTextColor(getResources().getColor(R.color.white));

                clCompany2.setBackgroundResource(R.drawable.bg_oil_card_home);
                tvCardType2.setBackgroundResource(R.color.line_f6);
                tvCardType2.setTextColor(getResources().getColor(R.color.color_3));


                cardType = 1; //油卡类型 1:中石化 2:中石油
                break;
            case R.id.tv_rule: //规则弹窗
               /* List<OilOrderDetailBean.RechargeListBean> list = new ArrayList<OilOrderDetailBean.RechargeListBean>();

                for (int i = 0; i < list.size(); i++) {
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

                }*/
                DialogMaker.showRuleDialog(this, list);
                break;
            case R.id.bt_buy:
                if (uid.equalsIgnoreCase("")) {
                    startActivityForResult(new Intent(this, LoginActivity.class), login);
                } else {
                    if (address == null) {
                        ToastUtil.showToast("请先添加收货地址");
                    } else {

                       /* int money = oilCardPackageBean.getLeastaAmount();
                        // tvAllMoney.setText(StringCut.getNumKb(money * oilCardPackageBean.getRate()) + "");

                        //BigDecimal b1 = new BigDecimal(Double.toString(money));
                        BigDecimal b1 = new BigDecimal(money + "");

                        BigDecimal b2 = new BigDecimal(Double.toString(oilCardPackageBean.getRate()));
                        double v1 = b1.multiply(b2).doubleValue();

                        LogUtils.e("--->Double.toString(money)" + v1);*/


                        /*  */

                        int stock = oilCardPackageBean.getStock();
                        if (stock == 0) {
                            ToastMaker.showShortToast("今日卡片已领完,请明日再来领取");
                        } else if (pid == 0) {
                            ToastMaker.showShortToast("请先选择加油套餐");
                        } else {
                            // TODO: 2019/2/25  
                            // getcanBuyFuelCard();


                            startActivity(new Intent(OilCardBuyActivity.this, OilCardPayActivity.class)
                                    .putExtra("uid", uid)

                                    .putExtra("addressid", address.getId())  //string 油卡id

                                    //.putExtra("amount", 10.0) //单笔金额
                                    .putExtra("productCardId", oilCardPackageBean.getId()) //产品id
                                    //.putExtra("money", 10)
                                    .putExtra("activitytype", 3)// 1：油卡 2：手机 3：直购-领取油卡

                                    .putExtra("amount", allMoney) //金额 freight
                                    .putExtra("monthMoney", monthMoney)//每月的金额freight
                                    .putExtra("pid", pid) //产品id
                                    .putExtra("cardType", cardType) //油卡类型 1:中石化 2:中石油
                                    .putExtra("fid", fid) //优惠券id
                                    .putExtra("fromPackage", false)
                            );
                        }
                    }
                }
                break;


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case addAddres:

                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("position", 0);
                    if (address != null) {
                        if (position != 0) {
                            refreshReceiptAddress(position);

                        } else {
                            getReceiptAddress();
                        }
                    } else {

                        getReceiptAddress();
                    }
                } else {
                    getReceiptAddress();
                }

                break;
            case login:
                getReceiptAddress();
                //getData();
                break;

            case oilpackage:
                if (resultCode == RESULT_OK) {
                    monthMoney = data.getIntExtra("monthMoney", 0);
                    months = data.getIntExtra("months", 0);
                    pid = data.getIntExtra("pid", 0);
                    fid = data.getIntExtra("fid", 0);
                    double amount = data.getDoubleExtra("amount", 0);
                    disMoney = data.getDoubleExtra("dismoney", 0);


                    tvPackageName.setText("月充" + monthMoney + "，" + months + "个月");
                    tvPackageMoney.setText(Html.fromHtml("<font color='#F95777'>￥" + amount + "</font> (省)" + disMoney));

                    allMoney = Arith.add(amount, freight);

                    tvAllMoney.setText(amount +"");
                    tvDiscount.setText("(省"+ disMoney + ")");

                } else {
                    //  getReceiptAddress();
                }

                //getData();
                break;
        }

    }

    /**
     * 获取收货地址
     */
    /*获取所有收货地址*/
    private void getReceiptAddress() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.RECEIPTADDRESS)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                //ptr_address.refreshComplete();
                LogUtils.i("所有的收货地址：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    JSONArray list = map.getJSONArray("addrlist");
                    // LogUtils.e("所有的收货地址：" + list.size());
                    if (list.size() > 0) {
                        List<AddressBean> addressBeans = JSON.parseArray(list.toJSONString(), AddressBean.class);

                        address = addressBeans.get(0);

                        ibAddAddress.setVisibility(View.GONE);

                        rlAddress.setVisibility(View.VISIBLE);
                        tvName.setText(address.getName() + " ");
                        tvPhone.setText(address.getPhone());
                        tvAddress.setText(address.getProvinceName() + address.getCityName()
                                + address.getAreaName() + address.getAddress());

                       /* llNorecord.setVisibility(View.GONE);
                        addressList = JSON.parseArray(list.toJSONString(), AddressBean.class);
                        addressAdapter = new AddressAdapter(AddressManageActivity.this, addressList);
                        addressAdapter.setOnAddressManageListener(AddressManageActivity.this);
                        lvAddress.setAdapter(addressAdapter);*/
                    } else {
                        // llNorecord.setVisibility(View.VISIBLE);
                        ibAddAddress.setVisibility(View.VISIBLE);
                        rlAddress.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                // ptr_address.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

    /**
     * 选择收货地址
     *
     * @param position
     */
    /*获取所有收货地址*/
    private void refreshReceiptAddress(final int position) {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.RECEIPTADDRESS)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    JSONArray list = map.getJSONArray("addrlist");
                    if (list != null) {
                        List<AddressBean> addressBeans = JSON.parseArray(list.toJSONString(), AddressBean.class);
                        address = addressBeans.get(position);
                        ibAddAddress.setVisibility(View.GONE);
                        rlAddress.setVisibility(View.VISIBLE);
                        tvName.setText(address.getName() + " ");
                        tvPhone.setText(address.getPhone());
                        tvAddress.setText(address.getProvinceName() + address.getCityName()
                                + address.getAreaName() + address.getAddress());


                    } else {
                        ibAddAddress.setVisibility(View.VISIBLE);
                        rlAddress.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ToastUtil.showToast("请检查网络");
            }
        });
    }

    /**
     * 查询是否可以购买油卡
     */
    private void getcanBuyFuelCard() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.canBuyFuelCard)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                //ptr_address.refreshComplete();
                LogUtils.e("查询是否可以购买油卡：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    // totalCount 总共已购买张数
                    //     singleCount 当天已购买张数
                    Integer totalCount = map.getInteger("totalCount");
                    Integer singleCount = map.getInteger("singleCount");


                    // Boolean canBuy = map.getBoolean("canBuy");
                    if (totalCount == 3) {
                        ToastMaker.showShortToast("您已领取3张油卡,不可再领");
                    } else if (singleCount == 1) {
                        ToastMaker.showShortToast("每日限领一张");
                    } else if (pid == 0) {
                        ToastMaker.showShortToast("请选择加油套餐");
                    } else {

                        LogUtils.e("fuelCardId" + address.getId());

                        startActivity(new Intent(OilCardBuyActivity.this, OilCardPayActivity.class)
                                .putExtra("uid", uid)

                                .putExtra("addressid", address.getId())  //string 油卡id

                                //.putExtra("amount", 10.0) //单笔金额
                                .putExtra("productCardId", oilCardPackageBean.getId()) //产品id
                                //.putExtra("money", 10)
                                .putExtra("activitytype", 3)// 1：油卡 2：手机 3：直购-领取油卡

                                .putExtra("amount", allMoney) //金额
                                .putExtra("monthMoney", monthMoney)//每月的金额
                                .putExtra("pid", pid) //产品id
                                .putExtra("cardType", cardType) //油卡类型 1:中石化 2:中石油
                                .putExtra("fid", fid) //优惠券id


                                .putExtra("fromPackage", false)
                        );
                    }
                }

            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                // ptr_address.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

    /**
     * 油卡列表
     */
    // 套餐列表
    private void getData() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.SetMeal)
                .addParams("type", "9") //1是套餐，2是即时 9公司油卡
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("--->油卡列表：" + response);
                        dismissDialog();

                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray arr = map.getJSONArray("list");
                            JSONArray rule = map.getJSONArray("rule");


                            List<String> rule_List = JSON.parseArray(rule.toJSONString(), String.class);


                            List<OilCardPackageBean> mrows_List = JSON.parseArray(arr.toJSONString(), OilCardPackageBean.class);
                            LogUtils.e("套餐" + mrows_List.size());

                            if (rule_List.size() > 0) {
                                list.clear();
                                list.addAll(rule_List);
                            }

                            if (mrows_List.size() > 0) {

                                list3.clear();
                                list3.addAll(mrows_List);

                                if (list3.size() > 0) {
                                    for (OilCardPackageBean bean : list3) {
                                        if (bean.getSimpleName().contains("中国石化")) {
                                            oilCardPackageBean = bean;

                                            freight = bean.getAmount();
                                            tvFreight.setText("￥" + freight);

                                            tvAllMoney.setText(freight + "");

                                            if (bean.getStock() == 0) {
//                                                rlCampany1.setBackgroundResource(R.drawable.bg_oil_card_home);
//                                                tvCardNum1.setTextColor(getResources().getColor(R.color.primary));
                                            } else {
//                                                rlCampany1.setBackgroundResource(R.drawable.bg_oil_card_home_selected);
//                                                tvCardNum1.setTextColor(getResources().getColor(R.color.white));
                                            }
                                            tvCardNum1.setText("剩余名额： " + bean.getStock());
                                            cardNum1 = bean.getStock();
                                        }
                                        if (bean.getSimpleName().contains("中国石油")) {
                                            if (bean.getStock() == 0) {
//                                                rlCampany2.setBackgroundResource(R.drawable.bg_oil_card_home);
//                                                tvCardNum2.setTextColor(getResources().getColor(R.color.primary));
                                            } else {
//                                                rlCampany2.setBackgroundResource(R.drawable.bg_oil_card_home_);
//                                                tvCardNum2.setTextColor(getResources().getColor(R.color.white));
                                            }
                                            tvCardNum2.setText("剩余名额： " + bean.getStock());
                                            cardNum2 = bean.getStock();
                                        }
                                    }
                                }
                                // oilCardPackageBean = list3.get(0);


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


}
