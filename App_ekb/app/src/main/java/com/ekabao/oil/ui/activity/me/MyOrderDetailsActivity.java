package com.ekabao.oil.ui.activity.me;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.OilOrderDetailBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.builder.PostFormBuilder;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.activity.OilCardPayActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.Arith;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class MyOrderDetailsActivity extends BaseActivity {

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
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_card_name)
    TextView tvCardName;
    @BindView(R.id.tv_card_code)
    TextView tvCardCode;
    @BindView(R.id.tv_month_money)
    TextView tvMonthMoney;
    @BindView(R.id.tv_fullName)
    TextView tvFullName;
    @BindView(R.id.tv_fAmount)
    TextView tvFAmount;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_rechargeList)
    TextView tvRechargeList;
    @BindView(R.id.ll_rechargeList)
    LinearLayout llRechargeList;
    @BindView(R.id.tv_agreementNo)
    TextView tvAgreementNo;
    @BindView(R.id.tv_payType)
    TextView tvPayType;
    @BindView(R.id.tv_investTime)
    TextView tvInvestTime;
    @BindView(R.id.ll_month_money)
    LinearLayout llMonthMoney;
    @BindView(R.id.ll_oilcard)
    LinearLayout llOilcard;
    @BindView(R.id.tv_card_title)
    TextView tvCardTitle;
    @BindView(R.id.tv_month_money_title)
    TextView tvMonthMoneyTitle;
    @BindView(R.id.bt_delete)
    Button btDelete;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.bt_refund)
    Button btRefund;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    @BindView(R.id.tv_interest)
    TextView tvInterest;
    @BindView(R.id.ll_interest)
    LinearLayout llInterest;
    @BindView(R.id.tv_amount_name)
    TextView tvAmountName;
    @BindView(R.id.tv_interest_name)
    TextView tvInterestName;
    @BindView(R.id.tv_refundTime)
    TextView tvRefundTime;
    @BindView(R.id.ll_refundTime)
    LinearLayout llRefundTime;
    @BindView(R.id.tv_invest_name)
    TextView tvInvestName;
    @BindView(R.id.ll_pay)
    LinearLayout llPay;
    @BindView(R.id.ll_paytype)
    LinearLayout llPaytype;

    private String orderId;
    private int type;//1：油卡 2：手机 3：直购  4 油卡套餐
    private List<OilOrderDetailBean.RechargeListBean> rechargeList;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;


    private double allAmount; //实际支付金额 彈窗的
    private double factAmount; //实际支付金额 -->第三方的
    private int shouldPrincipal; //月充值金额
    private int num; //已充值的月数
    private double interestRate; //手续费
    private double reFundAmount; //退的钱

    private int fid; //优惠券id
    private int pid; //产品id
    private double amount; //冲到油卡里金额

    private double monthamount; //冲到油卡里月金额
    private double monthMoney; //套餐显示的月充值金额

    private int cardId; //油卡或者手机号id
    private boolean fromPackage; //是否套餐充值
    private String fullName; //名字
    private double interest; //余额支付金额

    private int cardType;//
    private int status; //状态 油卡领取页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_order_details;
    }

    @Override
    protected void initParams() {

        Intent intent = getIntent();

        orderId = intent.getStringExtra("orderId");
        type = intent.getIntExtra("type", 1);
        cardType = intent.getIntExtra("cardType", 1);
        pid = intent.getIntExtra("pid", 1);
        status = intent.getIntExtra("status", 1);

        titleCentertextview.setText("订单详情");
        switch (type) { //1：油卡 2：手机 3：直购
            case 1:
                llOilcard.setVisibility(View.VISIBLE);
                tvCardTitle.setText("油卡卡号");
                // tvMonthMoneyTitle.setText();
                break;
            case 2:
                llOilcard.setVisibility(View.GONE);
                tvCardTitle.setText("手机号码");

                break;
            case 3:

                break;
            case 4:
                llOilcard.setVisibility(View.VISIBLE);
                tvCardTitle.setText("油卡卡号");
                tvCardCode.setText("待分配");
                break;

        }

        // "status": //状态 0-待支付，1-已支付，2-失败（已取消、退款），3-已完成，4-已退订，5-已发货，6-删除

        if (type == 4 && (status == 0 || status == 2)) {

            previewPackageProduct();
        } else {
            getOilCard();
        }

        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llRechargeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rechargeList != null) {
                    DialogMaker.showMonthDialog(MyOrderDetailsActivity.this, rechargeList);
                }
            }
        });


    }

    @OnClick({R.id.bt_refund, R.id.bt_delete, R.id.bt_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.bt_refund: //退订

                // TODO: 2019/1/23 手续费
                refundMoney();


               /* */

                break;
            case R.id.bt_delete:

                //0:取消订单  1:删除订单 2:确定收货
                //目前只有待支付的 取消订单

                setDialog("取消订单", 1);

                break;
            case R.id.bt_sure:


                monthamount = amount;
               /* if (fromPackage){
                    String substring = fullName.substring(0, 1);
                    int i = Integer.parseInt(substring);
                    if (i!=0) {
                        try {
                            double div = Arith.div(amount, i, 1);
                            monthamount=(int) div;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }*/


                LogUtils.e("piid" + orderId + "amount" + factAmount + "monthamount" + monthamount);

                startActivity(new Intent(this, OilCardPayActivity.class)
                        .putExtra("uid", preferences.getString("uid", ""))
                        .putExtra("fuelCardId", cardId + "")  //油卡id
                        .putExtra("amount", allAmount)  //金额 factAmount
                        .putExtra("monthMoney", (int) monthamount)
                        .putExtra("money", (int) monthamount)
                        .putExtra("pid", pid) //产品id
                        .putExtra("fid", fid) //优惠券id
                        .putExtra("interest", interest)//余额支付金额

                        .putExtra("orderDetail", Integer.parseInt(orderId)) //订单id
                        .putExtra("activitytype", 5)//   5 油卡再支付
                        .putExtra("fromPackage", fromPackage)
                );
                setResult(Activity.RESULT_OK);
                finish();

                break;
        }

    }

    private void setDialog(final String title, final int status) {
        DialogMaker.showRedSureDialog(this, title, "是否" + title + "？", "取消", "确定", new DialogMaker.DialogCallBack() {
            @Override
            public void onButtonClicked(Dialog dialog, int position, Object tag) {
                cancelInvest(title, status);
            }

            @Override
            public void onCancelDialog(Dialog dialog, Object tag) {

            }
        }, "");
    }

    /**
     * 订单详情
     * .addParams("uid", preferences.getString("uid", ""))
     */
    private void getOilCard() {
        LogUtils.e("订单详情" + orderId);
        OkHttpUtils.post().url(UrlConfig.orderDetail)

                .addParams("orderId", orderId)
                //   .addParams("fuelCardId", fuelCardId + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("--->订单详情：" + response);
                        dismissDialog();

                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONObject orderDetail = map.getJSONObject("orderDetail");

                            //OilOrderDetailBean bean = GsonUtil.parseJsonToBean(orderDetail.toString(), OilOrderDetailBean.class);

                            // LogUtils.e("订单详情"+bean.getFullName());
                            // LogUtils.e("订单详情"+bean.getFullName()+bean.getRechargeList().size());


                            OilOrderDetailBean bean = JSON.toJavaObject(orderDetail, OilOrderDetailBean.class);

                            pid = bean.getPid();
                            fid = bean.getFid();
                            amount = bean.getAmount();
                            cardId = bean.getCardId();
                            fullName = bean.getFullName();

                            rechargeList = bean.getRechargeList();
                            tvFullName.setText(Arith.mul(bean.getRate(), 10.0) + "折，" + bean.getFullName());

                            if (fullName.contains("个月")) {
                                String month = fullName.substring(0, fullName.indexOf("个月"));

                                try {
                                    monthMoney = Arith.div(amount, Integer.parseInt(month), 1);

                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }

                                //LogUtils.e(fullName+"month"+month);

                            }


                            //
                            // 1,(油卡类型1：中石化，2：中石油)
                            if (bean.getCardType() == 1 | bean.getCardType() == 3) {
                                tvCardName.setText("中石化 油卡");
                            } else {
                                tvCardName.setText("中石油 油卡");
                            }

                            factAmount = bean.getFactAmount();

                            allAmount = bean.getFactAmount();

                            if (bean.getInterest() != 0) {
                                llInterest.setVisibility(View.VISIBLE);
                                interest = bean.getInterest();
                                tvInterest.setText("-¥" + interest);
                                // TODO: 2019/1/24
                                allAmount = Arith.add(allAmount, bean.getInterest());

                            } else {
                                llInterest.setVisibility(View.GONE);
                            }


                            //tvAgreementNo.setText(bean.getAgreementNo());
                            tvAgreementNo.setText(bean.getPaynum());
                            //1支付宝 、2微信支付、3云闪付 4
                            switch (bean.getPayType()) {
                                case 1:
                                    tvPayType.setText("支付宝");
                                    break;
                                case 2:
                                    tvPayType.setText("微信支付");
                                    break;
                                case 3:
                                    tvPayType.setText("云闪付");
                                    break;
                                case 4:
                                    tvPayType.setText("余额支付");
                                    break;

                            }
                            tvInvestTime.setText(StringCut.getDateTimeToStringheng(bean.getInvestTime()));

                            tvFAmount.setText("-¥" + bean.getFAmount());

                            // "type": 1,( 1:套餐充值 2:即时充值)
                            // 1:油卡套餐、2油卡直充 ,3 话费套餐,4话费直冲,5移动流量直冲,6联通流量直冲,7电信流量直冲,8油卡直购
                            Drawable drawableLeft;
                          /*  if (bean.getStatus() == 3) {
                                drawableLeft = getResources().getDrawable(
                                        R.drawable.icon_my_order_complete);
                            } else {
                                drawableLeft = getResources().getDrawable(
                                        R.drawable.icon_my_order_going);
                            }*/
                            // "status": //状态 0-待支付，1-已支付，2-失败（已取消、退款），3-已完成，4-已退订，5-已发货，6-删除

                            switch (bean.getStatus()) {
                                case 0: //0-待支付
                                    llPaytype.setVisibility(View.GONE);

                                    drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_0);

                                    btDelete.setVisibility(View.VISIBLE);
                                    btDelete.setText("取消订单");
                                    btSure.setVisibility(View.VISIBLE);
                                    btSure.setText("立即支付");

                                    tvAmountName.setText("剩余待支付");
                                    tvAmount.setText("¥" + bean.getFactAmount());


                                    tvInvestName.setText("下单时间");
                                    break;
                                case 1:  //，1-已支付
                                    llPaytype.setVisibility(View.VISIBLE);
                                    drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_1);

                                    llInterest.setVisibility(View.GONE);
                                    tvAmountName.setText("支付金额");
                                    tvAmount.setText("¥" + allAmount);
                                    tvInvestName.setText("支付时间");
                                    break;
                                case 2:  //已取消
                                    llPaytype.setVisibility(View.GONE);
                                    drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_2);

                                    if (bean.getInterest() != 0) {
                                        llInterest.setVisibility(View.VISIBLE);
                                        interest = bean.getInterest();

                                        tvInterest.setText(Html.fromHtml("-¥" + interest + "<font color='#444444'>(已退款)</font>"));


                                    } else {
                                        llInterest.setVisibility(View.GONE);
                                    }

                                    tvAmountName.setText("剩余待支付");
                                    tvAmount.setText("¥" + bean.getFactAmount());
                                    tvInvestName.setText("下单时间");

                                    break;
                                case 3: //3-已完成
                                    llPaytype.setVisibility(View.VISIBLE);
                                    drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_3);

                                    tvAmountName.setText("支付金额");
                                    tvAmount.setText("¥" + allAmount);
                                    tvInvestName.setText("支付时间");
                                    break;
                                case 4: //4-已退订
                                    llPaytype.setVisibility(View.VISIBLE);
                                    drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_4);

                                    if (bean.getFactInterest() != 0) {

                                        llInterest.setVisibility(View.VISIBLE);
                                        // interest = bean.getInterest();

                                        tvInterestName.setText("支付金额");
                                        tvInterest.setText(Html.fromHtml("<font color='#FF623D'>¥" + allAmount + "</font>"));

                                        tvAmountName.setText("已退金额");

                                        //  tvAmount.setText("¥" + bean.getFactInterest());

                                        tvAmount.setText(Html.fromHtml("<font color='#FF623D'>¥" + bean.getFactInterest() + "</font>"));


                                    } else {
                                        llInterest.setVisibility(View.GONE);

                                        tvAmountName.setText("支付金额");
                                        tvAmount.setText(Html.fromHtml("<font color='#FF623D'>¥" + allAmount + "</font>"));

                                    }


                                    tvRecharge.setText(Html.fromHtml("充值计划<font color='#FF623D'>(已终止)</font>"));
                                    llRechargeList.setClickable(false);

                                    if (bean.getRefundTime() != 0) {
                                        llRefundTime.setVisibility(View.VISIBLE);
                                        tvRefundTime.setText(StringCut.getDateTimeToStringheng(bean.getRefundTime()));
                                    } else {
                                        llRefundTime.setVisibility(View.GONE);
                                    }

                                    tvInvestName.setText("下单时间");


                                    break;
                                default:
                                    llPaytype.setVisibility(View.GONE);
                                    tvInvestName.setText("下单时间");
                                    drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_complete);
                                    break;
                            }

                            // "type": 1,( 1:套餐充值 2:即时充值)
                            // 1:油卡套餐、2油卡直充 ,3 话费套餐,4话费直冲,5移动流量直冲,6联通流量直冲,7电信流量直冲,8油卡直购

                            switch (bean.getType()) {
                                case 1: //1:油卡套餐

                                    fromPackage = true;

                                    //"status": 3(状态(0-未支付 1-支付成功 2-失败,3-已结束))
                                    tvCardCode.setText(bean.getCardnum());


                                    tvName.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                                            null, null, null);
                                    llRechargeList.setVisibility(View.VISIBLE);
                                    llMonthMoney.setVisibility(View.VISIBLE);

                                    if (rechargeList.size() > 0) {

                                        tvName.setText(rechargeList.size() + "个月加油套餐");
                                        shouldPrincipal = rechargeList.get(0).getShouldPrincipal();
                                        tvMonthMoney.setText("¥" + shouldPrincipal);

                                        num = 0;
                                        for (int i = 0; i < rechargeList.size(); i++) {
                                            OilOrderDetailBean.RechargeListBean rechargeListBean = rechargeList.get(i);
                                            if (rechargeListBean.getStatus() == 1) {
                                                num++;
                                            }
                                        }

                                        tvRechargeList.setText(Html.fromHtml(num + "<font color='#444444'>/" + rechargeList.size() + "</font>"));
                                    } else {
                                        tvName.setText(bean.getFullName());
                                        tvMonthMoney.setText("¥" + monthMoney);

                                    }

                                    //退订
                                   /* if (bean.getStatus() == 1) {
                                        btRefund.setVisibility(View.VISIBLE);
                                    }*/

                                    break;
                                case 2://2油卡直充
                                    fromPackage = false;

                                    tvCardCode.setText(bean.getCardnum());

                                  /*  drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_complete);*/
                                    tvMonthMoney.setText(bean.getAmount() + "");

                                    tvName.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                                            null, null, null);

                                    tvName.setText("油卡直充-" + bean.getAmount() + "元");
                                    llRechargeList.setVisibility(View.GONE);
                                    llMonthMoney.setVisibility(View.GONE);
                                    break;
                                case 3: //3 话费套餐
                                    fromPackage = true;

                                    tvCardCode.setText(bean.getfuelId());
                                    tvMonthMoneyTitle.setText("月充值额");
                                    llMonthMoney.setVisibility(View.VISIBLE);
                                    llRechargeList.setVisibility(View.VISIBLE);

                                    tvName.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                                            null, null, null);
                                    tvName.setText(bean.getFullName());
                                    if (rechargeList.size() > 0) {

                                        //tvName.setText(rechargeList.size() + "个月话费套餐");
                                        tvMonthMoney.setText("¥" + rechargeList.get(0).getShouldPrincipal());

                                        int num = 0;
                                        for (int i = 0; i < rechargeList.size(); i++) {
                                            OilOrderDetailBean.RechargeListBean rechargeListBean = rechargeList.get(i);
                                            if (rechargeListBean.getStatus() == 1) {
                                                num++;
                                            }
                                        }
                                        //充值状态 0未充值，1已充值，2逾期(充值失败), 3充值中
                                        tvRechargeList.setText(Html.fromHtml(num + "<font color='#444444'>/" + rechargeList.size() + "</font>"));
                                    } else {
                                        tvMonthMoney.setText("¥" + monthMoney);
                                    }

                                    //退订
                                  /*  if (bean.getStatus() == 1) {
                                        btRefund.setVisibility(View.VISIBLE);
                                    }*/

                                    break;
                                case 4: //4话费直冲
                                    fromPackage = false;

                                    tvCardCode.setText(bean.getfuelId());
                                    tvMonthMoneyTitle.setText("充值金额");
                                    llMonthMoney.setVisibility(View.VISIBLE);
                                    llRechargeList.setVisibility(View.GONE);

                                    tvMonthMoney.setText(bean.getAmount() + "");
                                   /* drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_complete);*/

                                    tvName.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                                            null, null, null);

                                    tvName.setText(bean.getFullName());

                                    break;
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
     * 预览套餐订单金额
     */
    private void previewPackageProduct() {
        LogUtils.e("预览套餐订单金额" + orderId);
        OkHttpUtils.post().url(UrlConfig.previewPackageProduct)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("orderId", orderId)
                .addParams("cardType", cardType + "")
                .addParams("pid", pid + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {

                        LogUtils.e("--->预览套餐订单金额" + response);

                        dismissDialog();

                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");


                            String cardNum = map.getString("cardNum");
                            String content = map.getString("content");
                            String fullName = map.getString("fullName");
                            Double factAmount = map.getDouble("factAmount");
                            Double fAmount = map.getDouble("fAmount");
                            Double rate = map.getDouble("rate");
                            Double singleAmount = map.getDouble("singleAmount");
                            Integer cardType = map.getInteger("cardType");

                            JSONArray list = map.getJSONArray("list");


                            tvName.setText(fullName);
                            tvFullName.setText(content);
                            // 1,(油卡类型1：中石化，2：中石油)
                            if (cardType == 1 | cardType == 3) {
                                tvCardName.setText("中石化 油卡");
                            } else {
                                tvCardName.setText("中石油 油卡");
                            }
                            tvCardCode.setText(cardNum);

                            tvAmount.setText("¥" + factAmount);

                            //tvAgreementNo.setText(bean.getAgreementNo());
                            // tvAgreementNo.setText(bean.getPaynum());


                            tvFAmount.setText("-¥" + fAmount);

                            //   List<OilCardPackageBean> mrowsList = JSON.parseArray(arr.toJSONString(), OilCardPackageBean.class);
                            Double shouldPrincipal = 0.0;
                            if (list.size() > 0) {

                                String s = list.get(0).toString();

                                JSONObject obj1 = JSON.parseObject(s);
                                shouldPrincipal = obj1.getDouble("shouldPrincipal");

                                rechargeList = new ArrayList<>();

                                for (int i = 0; i < list.size(); i++) {
                                    String s1 = list.get(i).toString();

                                    JSONObject obj2 = JSON.parseObject(s1);

                                    Long shouldTime = obj2.getLong("shouldTime");
                                    String dateToString = StringCut.getDateToString(shouldTime);

                                    int shouldPrincipal2 = obj2.getInteger("shouldPrincipal");
                                    rechargeList.add(new OilOrderDetailBean.RechargeListBean(
                                            dateToString, 0, shouldPrincipal2, 0
                                    ));

                                }
                                tvRechargeList.setText(Html.fromHtml("0<font color='#444444'>/" + list.size() + "</font>"));

                            }
                            tvMonthMoney.setText("¥" + shouldPrincipal);


                            Drawable drawableLeft;
                            switch (status) {
                                case 0: //0-待支付
                                    drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_0);
                                    break;
                                case 1:  //，1-已支付
                                    drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_1);
                                    break;
                                case 2:  //已取消
                                    drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_2);
                                    break;
                                case 3: //3-已完成
                                    drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_3);

                                    break;
                                case 4: //4-已退订
                                    drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_4);

                                    break;
                                default:
                                    drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_complete);
                                    break;
                            }


                            tvName.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                                    null, null, null);

                            llPay.setVisibility(View.GONE);
                            btDelete.setVisibility(View.GONE);
                            btSure.setVisibility(View.GONE);


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
     * //取消订单  删除订单
     */
    private void cancelInvest(final String title, int status) {

        showWaitDialog("请稍后...", false, "");

        PostFormBuilder url = OkHttpUtils.post();
        if (status == 1) { //取消订单
            url.url(UrlConfig.cancelInvest);
        } else { //删除订单
            url.url(UrlConfig.deleteInvest);
        }


        url.addParams("uid", preferences.getString("uid", ""))
                .addParams("piid", orderId + "") //订单id
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                dismissDialog();
                LogUtils.e("取消订单" + result);

                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {

                    JSONObject objmap = obj.getJSONObject("map");
                    ToastMaker.showShortToast(title + "成功");

                    setResult(Activity.RESULT_OK);

                    finish();


                } else if ("9999".equals(obj.getString("errorCode"))) {

                    ToastMaker.showShortToast("系统错误");
                    //   getActivity().finish();
                    //new show_Dialog_IsLogin(getActivity()).show_Is_Login() ;
                    //1100订单Id不能为空
                } else {
                    ToastMaker.showShortToast("服务器异常");
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
     * 1.套餐退订
     */
    private void refund() {

        showWaitDialog("请稍后...", false, "");

        LogUtils.e("套餐退订uid" + preferences.getString("uid", "") + "id" + orderId);

        OkHttpUtils.post()
                .url(UrlConfig.refund)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("id", orderId + "") //订单id
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                dismissDialog();
                LogUtils.e("退订订单" + result);

                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {

                    // JSONObject objmap = obj.getJSONObject("map");
                    ToastMaker.showShortToast("退订成功");

                    setResult(Activity.RESULT_OK);

                    finish();

                } else if ("2100".equals(obj.getString("errorCode"))) {

                    ToastMaker.showShortToast("不能退订此套餐");
                    //   getActivity().finish();
                    //new show_Dialog_IsLogin(getActivity()).show_Is_Login() ;
                    //1100订单Id不能为空
                } else {
                    ToastMaker.showShortToast("服务器异常");
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
     * 32.退订金额
     */
    private void refundMoney() {

        showWaitDialog("请稍后...", false, "");

        LogUtils.e("套餐退订uid" + preferences.getString("uid", "") + "id" + orderId);

        OkHttpUtils.post()
                .url(UrlConfig.refundMoney)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("id", orderId + "") //订单id
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                dismissDialog();
                LogUtils.e("退订金额" + result);

                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {

                    JSONObject objmap = obj.getJSONObject("map");
                    Boolean serviceCharge = objmap.getBoolean("serviceCharge");
                    if (serviceCharge) {
                        interestRate = objmap.getDoubleValue("interestRate");
                    }
                    reFundAmount = objmap.getDoubleValue("reFundAmount");

                    double mul = Arith.sub(factAmount, (shouldPrincipal * num));

                    if (reFundAmount <= 0) {
                        ToastMaker.showShortToast("此订单充值金额已达到支付金额，无法退款");
                    } else {

                        if (shouldPrincipal == 0) {
                            shouldPrincipal = (int) monthMoney;
                        }
                        LogUtils.e(shouldPrincipal + "退订的月" + monthMoney);

                        DialogMaker.showrefundDialog(MyOrderDetailsActivity.this, allAmount, shouldPrincipal, num, reFundAmount, interestRate, "确定", new DialogMaker.DialogCallBack() {
                            @Override
                            public void onButtonClicked(Dialog dialog, int position, Object tag) {
                                refund();
                            }

                            @Override
                            public void onCancelDialog(Dialog dialog, Object tag) {

                            }
                        }, "");
                    }

                  /*  ToastMaker.showShortToast("退订成功");

                    setResult(Activity.RESULT_OK);

                    finish();*/

                } else if ("2100".equals(obj.getString("errorCode"))) {

                    ToastMaker.showShortToast("不能退订此套餐");
                    //   getActivity().finish();
                    //new show_Dialog_IsLogin(getActivity()).show_Is_Login() ;
                    //1100订单Id不能为空
                } else {
                    ToastMaker.showShortToast("服务器异常");
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
