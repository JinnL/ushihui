package com.ekabao.oil.ui.activity.me;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import com.ekabao.oil.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class MyOilCardBuyDetailsActivity extends BaseActivity {


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
    @BindView(R.id.tv_express)
    TextView tvExpress;
    @BindView(R.id.bt_copy)
    Button btCopy;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_fullName)
    TextView tvFullName;
    @BindView(R.id.tv_month_money)
    TextView tvMonthMoney;
    @BindView(R.id.ll_month_money)
    LinearLayout llMonthMoney;
    @BindView(R.id.tv_fAmount)
    TextView tvFAmount;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_agreementNo)
    TextView tvAgreementNo;
    @BindView(R.id.tv_payType)
    TextView tvPayType;
    @BindView(R.id.tv_investTime)
    TextView tvInvestTime;
    @BindView(R.id.iv_state)
    ImageView ivState;
    @BindView(R.id.tv_interest_name)
    TextView tvInterestName;
    @BindView(R.id.tv_interest)
    TextView tvInterest;
    @BindView(R.id.ll_interest)
    LinearLayout llInterest;
    @BindView(R.id.tv_amount_name)
    TextView tvAmountName;
    @BindView(R.id.tv_package_name)
    TextView tvPackageName;
    @BindView(R.id.ll_oil_package)
    LinearLayout llOilPackage;
    @BindView(R.id.tv_invest_name)
    TextView tvInvestName;
    @BindView(R.id.bt_delete)
    Button btDelete;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.rl_express)
    RelativeLayout rlExpress;
    @BindView(R.id.ll_pautype)
    LinearLayout llPautype;
    /**
     * 油卡购买订单-->订单详情
     *
     * @time 2018/11/30 16:45
     * Created by lj
     */
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private String orderId;
    private int type;//1：油卡 2：手机 3：直购
    private List<OilOrderDetailBean.RechargeListBean> rechargeList;
    private String express;//快递单号

    private double amount; //冲到油卡里金额+运费
    private double allAmount; //实际支付金额 -->加一块的
    private double interest; //余额支付金额
    private double freight; //运费

    private int cardId; //油卡或者手机号id
    private boolean fromPackage; //是否套餐充值

    private int fid; //优惠券id
    private int pid; //产品id

    private int cardType;//
    private int status; //状态 传到下个页面
    private int investId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_oilcard_buy_details;
    }

    @Override
    protected void initParams() {

        Intent intent = getIntent();

        orderId = intent.getStringExtra("orderId");
        type = intent.getIntExtra("type", 1);
        titleCentertextview.setText("订单详情");

       /* switch (type) {
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;

        }*/

        getOilCard();
        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                // cm.setText(express);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("快递单号", express);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.showToast("已复制到剪贴板");

            }
        });
    }


    @OnClick({R.id.bt_delete, R.id.bt_sure, R.id.ll_oil_package})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_oil_package:
                LogUtils.e("orderId" + orderId + "investId" + investId+"cardType"+cardType);

                startActivity(new Intent(this, MyOrderDetailsActivity.class)
                        .putExtra("orderId", investId + "")
                        .putExtra("cardType", cardType)
                        .putExtra("pid", pid)
                        .putExtra("type", 4)
                        .putExtra("status", status)

                );

                break;
            case R.id.bt_delete:

                //0:取消订单  1:删除订单 2:确定收货
                //目前只有待支付的 取消订单

                setDialog("取消订单", 1);

                break;
            case R.id.bt_sure:

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


                LogUtils.e("piid" + orderId + "amount" + allAmount);
                LogUtils.e("orderId" + orderId + "investId" + investId+"cardType"+cardType);


                startActivity(new Intent(this, OilCardPayActivity.class)
                        .putExtra("uid", preferences.getString("uid", ""))
                        .putExtra("fuelCardId", cardId + "")  //油卡id
                        .putExtra("amount", allAmount)  //金额 factAmount
                        .putExtra("monthMoney", (int) amount)
                        .putExtra("money", (int) allAmount)
                        .putExtra("pid", pid) //产品id
                        .putExtra("fid", fid) //优惠券id
                        .putExtra("interest", interest)//余额支付金额
                        .putExtra("orderDetail", Integer.parseInt(orderId)) //订单id
                        .putExtra("activitytype", 3)//
                        .putExtra("cardType", cardType)
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
     */
    private void getOilCard() {
        // LogUtils.e("我的油卡" + uid);
        OkHttpUtils.post().url(UrlConfig.orderDetail)
                .addParams("orderId", orderId)
                //   .addParams("fuelCardId", fuelCardId + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("--->油卡购买订单详情：" + response);
                        dismissDialog();

                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONObject orderDetail = map.getJSONObject("orderDetail");

                            //OilOrderDetailBean bean = GsonUtil.parseJsonToBean(orderDetail.toString(), OilOrderDetailBean.class);


                            OilOrderDetailBean bean = JSON.toJavaObject(orderDetail, OilOrderDetailBean.class);

                            rechargeList = bean.getRechargeList();

                            String fullName = bean.getFullName();
                            tvFullName.setText(fullName);

                            //
                            // 1,(油卡类型1：中石化，2：中石油)
                            //油卡类型 1:中石化 2:中石油 3:公司中石化 4:公司中石油
                            Drawable image;

                            //fullName.contains("石化")
                            //

                            cardType = bean.getCardType();

                            if (cardType == 1 | cardType == 3) {
                                // tvName.setText("中石化 油卡");
                                image = getResources().getDrawable(R.drawable.icon_zhongshihua_addcard);
                            } else {
                                image = getResources().getDrawable(R.drawable.icon_zhongshiyou_addcard);
                                // tvName.setText("中石油 油卡");
                            }
                            tvName.setText(fullName);


                            image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());//非常重要，必须设置，否则图片不会显示
                            tvName.setCompoundDrawables(image, null, null, null);

                            // TODO: 2018/11/30 复制单号  这个是否显示隐藏  express

                            amount = bean.getAmount();
                            freight = bean.getFreight();

                            cardId = bean.getCardId();
                            pid = bean.getPid();
                            fid = bean.getFid();

                            double interest = bean.getInterest();
                            double factAmount = bean.getFactAmount();

                            allAmount = Arith.add(interest, factAmount);

                            double sub = Arith.sub(amount, freight); //套餐金额

                            tvMonthMoney.setText("¥" + sub);

                            tvFAmount.setText("¥" + bean.getFreight());//快递费
                            tvAmount.setText("¥" + factAmount); //支付金额

                            if (interest != 0) {
                                llInterest.setVisibility(View.VISIBLE);
                                MyOilCardBuyDetailsActivity.this.interest = interest;
                                tvInterest.setText("-¥" + MyOilCardBuyDetailsActivity.this.interest);
                                // TODO: 2019/1/24
                                // allAmount = Arith.add(allAmount, bean.getInterest());

                            } else {
                                llInterest.setVisibility(View.GONE);
                            }

                            // "status": //状态 0-待支付，1-已支付，2-失败（已取消、退款），3-已完成，4-已退订，5-已发货，6-删除
                            int drawableLeft;
                            status = bean.getStatus();

                            if (bean.getInvestId() != 0) {
                                investId = bean.getInvestId();
                            } else {
                                investId = bean.getId();
                            }

                            // 1 .3. 5  已完成的角标
                            if (status == 1 || status == 3 || status == 5) {
                                rlExpress.setVisibility(View.VISIBLE);
                            } else {
                                rlExpress.setVisibility(View.GONE);
                            }
                            if (!TextUtils.isEmpty(bean.getTrackingNumber()) && !TextUtils.isEmpty(bean.getTrackingNumber())) {
                                express = bean.getTrackingNumber();
                                tvExpress.setText(bean.getTrackingName() + " " + bean.getTrackingNumber());
                                btCopy.setVisibility(View.VISIBLE);
                            } else {
                                tvExpress.setText("正在准备您的卡片");
                                btCopy.setVisibility(View.GONE);
                            }


                            switch (status) {
                                case 0: //0-待支付
                                    llPautype.setVisibility(View.GONE);

                                    drawableLeft = R.drawable.icon_my_order_0;

                                    btDelete.setVisibility(View.VISIBLE);
                                    btDelete.setText("取消订单");
                                    btSure.setVisibility(View.VISIBLE);
                                    btSure.setText("立即支付");

                                    tvAmountName.setText("剩余待支付");
                                    tvAmount.setText("¥" + factAmount);

                                    tvInvestName.setText("下单时间");
                                    //  investId = bean.getId();
                                    break;
                                case 1:  //，1-已支付
                                    // drawableLeft =  R.drawable.icon_my_order_1;
                                    llPautype.setVisibility(View.VISIBLE);
                                    drawableLeft = R.drawable.icon_my_order_3;

                                    llInterest.setVisibility(View.GONE);
                                    tvAmountName.setText("支付金额");
                                    tvAmount.setText("¥" + amount);
                                    tvInvestName.setText("支付时间");


                                    break;
                                case 2:  //已取消
                                    drawableLeft =
                                            R.drawable.icon_my_order_2;
                                    llPautype.setVisibility(View.GONE);
                                    if (interest != 0) {
                                        llInterest.setVisibility(View.VISIBLE);
                                        MyOilCardBuyDetailsActivity.this.interest = interest;

                                        tvInterest.setText(Html.fromHtml("-¥" + MyOilCardBuyDetailsActivity.this.interest + "<font color='#444444'>(已退款)</font>"));


                                    } else {
                                        llInterest.setVisibility(View.GONE);
                                    }

                                    tvAmountName.setText("剩余待支付");
                                    tvAmount.setText("¥" + factAmount);
                                    tvInvestName.setText("下单时间");
                                    break;
                                case 3: //3-已完成
                                    drawableLeft =
                                            R.drawable.icon_my_order_3;
                                    llPautype.setVisibility(View.VISIBLE);
                                    tvAmountName.setText("支付金额");
                                    tvAmount.setText("¥" + amount);
                                    tvInvestName.setText("支付时间");

                                    // investId = bean.getInvestId();
                                    break;
                                case 4: //4-已退订
                                    llPautype.setVisibility(View.VISIBLE);
                                    // drawableLeft =    R.drawable.icon_my_order_4;
                                    drawableLeft = R.drawable.icon_my_order_2;
                                    llPautype.setVisibility(View.GONE);
                                    if (bean.getFactInterest() != 0) {

                                        llInterest.setVisibility(View.VISIBLE);
                                        // interest = bean.getInterest();

                                        tvInterestName.setText("支付金额");
                                        tvInterest.setText(Html.fromHtml("<font color='#FF623D'>¥" + amount + "</font>"));

                                        tvAmountName.setText("已退金额");

                                        //  tvAmount.setText("¥" + bean.getFactInterest());

                                        tvAmount.setText(Html.fromHtml("<font color='#FF623D'>¥" + bean.getFactInterest() + "</font>"));


                                    } else {
                                        llInterest.setVisibility(View.GONE);

                                        tvAmountName.setText("支付金额");
                                        tvAmount.setText(Html.fromHtml("<font color='#FF623D'>¥" + amount + "</font>"));

                                    }

                                    tvInvestName.setText("下单时间");


                                    break;
                                case 5: //已发货
                                    drawableLeft = R.drawable.icon_my_order_3;
                                    llPautype.setVisibility(View.VISIBLE);
                                    break;
                                default:
                                    llPautype.setVisibility(View.GONE);
                                    tvInvestName.setText("下单时间");
                                    drawableLeft =
                                            R.drawable.icon_my_order_complete;
                                    break;
                            }

                            ivState.setImageResource(drawableLeft);


                            // tvCardCode.setText(bean.getCardnum());

                            // tvAmount.setText(bean.getFactAmount() + "");
                            //tvAgreementNo.setText(bean.getAgreementNo());
                            tvAgreementNo.setText(bean.getPaynum());
                            //1支付宝 、2微信支付、3云闪付


                            String paytype = "";
                            if (interest != 0) {
                                paytype = "余额+";
                            }
                            switch (bean.getPayType()) {
                                case 1:
                                    tvPayType.setText(paytype + "支付宝");
                                    break;
                                case 2:
                                    tvPayType.setText(paytype + "微信支付");
                                    break;
                                case 3:
                                    tvPayType.setText(paytype + "云闪付");
                                    break;
                                case 4:
                                    tvPayType.setText("余额");
                                    break;
                            }
                            tvInvestTime.setText(StringCut.getDateTimeToStringheng(bean.getInvestTime()));


                            //tvFAmount.setText("-¥" + bean.getFAmount());

                            // "type": 1,( 1:套餐充值 2:即时充值)

                           /* if (bean.getType() == 2) {
                                Drawable drawableLeft = getResources().getDrawable(
                                        R.drawable.icon_my_order_complete);

                                tvName.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                                        null, null, null);

                                tvName.setText("油卡直充-" + bean.getAmount() + "元");
                                llRechargeList.setVisibility(View.GONE);
                                llMonthMoney.setVisibility(View.GONE);
                            } else {

                                //"status": 3(状态(0-未支付 1-支付成功 2-失败,3-已结束))
                                Drawable drawableLeft;
                                if (bean.getStatus() == 3) {
                                    drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_complete);
                                } else {
                                    drawableLeft = getResources().getDrawable(
                                            R.drawable.icon_my_order_going);
                                }
                                tvName.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                                        null, null, null);
                                llRechargeList.setVisibility(View.VISIBLE);
                                llMonthMoney.setVisibility(View.VISIBLE);

                                if (rechargeList.size() > 0) {

                                    tvName.setText(rechargeList.size() + "个月加油套餐");
                                    tvMonthMoney.setText("¥" + rechargeList.get(0).getShouldPrincipal());

                                    int num = 0;
                                    for (int i = 0; i < rechargeList.size(); i++) {
                                        OilOrderDetailBean.RechargeListBean rechargeListBean = rechargeList.get(i);
                                        if (rechargeListBean.getStatus() == 1) {
                                            num++;
                                        }
                                    }
                                    tvRechargeList.setText(Html.fromHtml(num + "<font color='#444444'>/" + rechargeList.size() + "</font>"));
                                }

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

}
