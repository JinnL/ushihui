package com.ekabao.oil.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.AtyWeChatInfo;
import com.ekabao.oil.bean.AtyZfbPayInfo;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.OkHttpEngine;
import com.ekabao.oil.http.PostParams;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.builder.PostFormBuilder;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.http.okhttp.request.RequestCall;
import com.ekabao.oil.ui.activity.me.MallOrderDetailsActivity;
import com.ekabao.oil.ui.activity.me.MyOilCardBuyDetailsActivity;
import com.ekabao.oil.ui.activity.me.MyOrderDetailsActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.AlipayUtil.PayResult;
import com.ekabao.oil.util.Arith;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.ToastUtil;
import com.ekabao.oil.wxapi.Constants;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

public class OilCardPayActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.cb_fuyou)
    CheckBox cbFuyou;
    @BindView(R.id.ll_unionpay)
    LinearLayout llUnionpay;
    @BindView(R.id.iv_bank)
    ImageView ivBank;
    @BindView(R.id.tv_bank_money)
    CheckBox tvBankMoney;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_bank_limit)
    TextView tvBankLimit;
    @BindView(R.id.rl_bank)
    RelativeLayout rlBank;
    @BindView(R.id.ll_weixin)
    LinearLayout llWeixin;
    @BindView(R.id.ll_alibaba)
    LinearLayout llAlibaba;
    @BindView(R.id.bt_pay)
    Button btPay;
    @BindView(R.id.cb_weixin)
    CheckBox cbWeixin;
    @BindView(R.id.cb_alibaba)
    CheckBox cbAlibaba;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.cb_balance)
    CheckBox cbBalance;
    @BindView(R.id.ll_balance)
    LinearLayout llBalance;
    @BindView(R.id.tv_surplusAmount)
    TextView tvSurplusAmount;

    private String uid;
    private int type = 2;// 1支付宝 、2微信支付、3云闪付
    private String fuelCardId = "";
    private int pid;
    private int fid;
    private double amount;
    private int money; //立即充值的显示
    private int monthMoney;
    private boolean fromPackage; //是否套餐充值

    private int activitytype = 1;// 1：油卡 2：手机 3：直购-套餐送油卡  4 商品  5油卡再支付
    private String orderId; //订单号-->下单后返回的
    private String bz = " "; //商品 备注
    private int addressid; //地址id 商品用的
    private int number;//商品用的 数量
    private int shoporderId;//商品用的 订单在支付

    private int orderDetail;//订单id-->从订单详情过来的


    private double balance = 0; //账号余额
    private double freeze; //冻结的金额
    private double surplusbalance = 0; //剩余应付
    private double interest; //余额支付金额 -->订单详情传过来


    private int MemberFunds = 2;// 1:需要余额，2：不需要余额（混合支付时使用，纯余额支付请看27）
    private boolean isMemberFunds;// 是否可以混合  1:需要余额，2：不需要余额（混合支付时使用，纯余额支付请看27）

    private int cardType; //油卡类型 1:中石化 2:中石油
    private int productCardId; //选择油卡类型 中的 油卡id


    private AtyWeChatInfo atyWeChatInfo;
    private IWXAPI api;
    private SharedPreferences preferences;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        // ToastUtil.showToast("支付成功");
                        DialogMaker.showPaySuccessDialog(OilCardPayActivity.this, callBack,
                                activitytype, orderId);
                        /*Intent inforIntent = new Intent(OilCardPayActivity.this,
                                OrderActivity.class);
                        // 放入当前点击的位置
                        startActivity(inforIntent);

                        finish();*/
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        LogUtils.e("payfail==" + resultInfo);
                        //ToastUtil
                        ToastMaker.showLongToast("订单支付失败");

                        // finish();

                        payfail();
                        // ToastUtil.showToast("支付失败");
                        // TODO 发请求问服务器支付结果
                    }
                    break;
            }
        }
    };
    DialogMaker.DialogCallBack callBack = new DialogMaker.DialogCallBack() {
        @Override
        public void onButtonClicked(Dialog dialog, int position, Object tag) {

        }

        @Override
        public void onCancelDialog(Dialog dialog, Object tag) {
            finish();
        }
    };
    /*****************************************************************
     * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     *****************************************************************/
    private final String mMode = "01";
    public static final int PLUGIN_VALID = 0;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_oil_card_pay);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_oil_card_pay;
    }

    @Override
    protected void initParams() {

        titleCentertextview.setText("支付");
        titleLeftimageview.setOnClickListener(this);
        llUnionpay.setOnClickListener(this);
        llAlibaba.setOnClickListener(this);
        llWeixin.setOnClickListener(this);
        btPay.setOnClickListener(this);
        rlBank.setOnClickListener(this);
        llBalance.setOnClickListener(this);

        preferences = LocalApplication.getInstance().sharereferences;

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);


        //油卡id
        fuelCardId = intent.getStringExtra("fuelCardId");
        pid = intent.getIntExtra("pid", 0);
        fid = intent.getIntExtra("fid", 0);
        amount = intent.getDoubleExtra("amount", 0);
        money = intent.getIntExtra("money", 0);//立即充值的显示
        fromPackage = intent.getBooleanExtra("fromPackage", false);
        monthMoney = intent.getIntExtra("monthMoney", 0);

        activitytype = intent.getIntExtra("activitytype", 0);
        addressid = intent.getIntExtra("addressid", 0);
        number = intent.getIntExtra("number", 0);
        shoporderId = intent.getIntExtra("shoporderId", 0);

        bz = intent.getStringExtra("bz");

        orderDetail = intent.getIntExtra("orderDetail", 0);  //订单id-->从订单详情过来的

        interest = intent.getDoubleExtra("interest", 0);

        cardType = intent.getIntExtra("cardType", 0);
        productCardId = intent.getIntExtra("productCardId", 0);


        LogUtils.e("amount" + amount + "money" + money + "fuelCardId" + fuelCardId + "monthMoney" + monthMoney + "orderDetail" + orderDetail);

        //应付款 显示的 实际支付的
        if (fromPackage) {
            tvMoney.setText("￥" + amount);
        } else {
            tvMoney.setText("￥" + amount);
        }

        //这里不用余额了
        tvSurplusAmount.setText("¥" + StringCut.getNumKb(amount));

        //冻结的金额
        if (interest != 0 && orderDetail != 0) {

            // MemberFunds = 2; // 有冻结的金额 不混合支付
            llBalance.setClickable(false);
            cbBalance.setVisibility(View.GONE);
            //  cbBalance.setChecked(true);
            // surplusbalance = Arith.sub(amount, interest);


            tvBalance.setText("-¥" + StringCut.getNumKb(interest));
        } else {
            llBalance.setClickable(true);

            cbBalance.setVisibility(View.VISIBLE);
        }


        //余额金额

      //  getBalanceData();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.rl_bank: //3云闪付
                // 1支付宝 、2微信支付、3云闪付
                type = 3;
                // cbFuyou.setChecked(true);
                tvBankMoney.setChecked(true);

                cbAlibaba.setChecked(false);
                cbWeixin.setChecked(false);

                if (isMemberFunds && cbBalance.isChecked()) {
                    // 混合支付
                    surplusbalance = Arith.sub(amount, balance);
                } else {
                    surplusbalance = amount;
                    cbBalance.setChecked(false);
                }


                //冻结的金额
                if (interest != 0 && orderDetail != 0) {
                    surplusbalance = Arith.sub(surplusbalance, interest);
                }

                tvSurplusAmount.setText("¥" + StringCut.getNumKb(surplusbalance));


                break;
            case R.id.ll_alibaba: //1支付宝
                type = 1;

                tvBankMoney.setChecked(false);
                cbAlibaba.setChecked(true);
                cbWeixin.setChecked(false);

                if (isMemberFunds && cbBalance.isChecked()) {
                    // 混合支付
                    surplusbalance = Arith.sub(amount, balance);
                } else {
                    surplusbalance = amount;
                    cbBalance.setChecked(false);
                }

                //冻结的金额
                if (interest != 0 && orderDetail != 0) {
                    surplusbalance = Arith.sub(surplusbalance, interest);
                }

                tvSurplusAmount.setText("¥" + StringCut.getNumKb(surplusbalance));

                break;

            case R.id.ll_weixin: //2微信支付
                type = 2;
                tvBankMoney.setChecked(false);
                cbAlibaba.setChecked(false);
                cbWeixin.setChecked(true);


                if (isMemberFunds && cbBalance.isChecked()) {
                    // 混合支付
                    surplusbalance = Arith.sub(amount, balance);
                } else {
                    surplusbalance = amount;
                    cbBalance.setChecked(false);
                }

                //冻结的金额
                if (interest != 0 && orderDetail != 0) {
                    surplusbalance = Arith.sub(surplusbalance, interest);
                }

                tvSurplusAmount.setText("¥" + StringCut.getNumKb(surplusbalance));

                break;
            case R.id.ll_balance: //4 余额


                if (isMemberFunds) {
                    //单选 多选
                    cbBalance.setChecked(!cbBalance.isChecked());

                    if (cbBalance.isChecked()) {
                        // 混合支付 多选
                        surplusbalance = Arith.sub(amount, balance);
                        MemberFunds = 1;

                    } else {
                        //单选

                        MemberFunds = 2;
                        surplusbalance = amount;
                    }


                } else {
                    //单选

                    type = 4;

                    tvBankMoney.setChecked(false);
                    cbAlibaba.setChecked(false);
                    cbWeixin.setChecked(false);

                    cbBalance.setChecked(true);

                    surplusbalance = 0;

                }

                tvSurplusAmount.setText("¥" + StringCut.getNumKb(surplusbalance));

                break;


            case R.id.bt_pay: //击进入充值问题页面

                if (activitytype == 4) { //4 商品
                    if (type == 4) {
                        shopRechargeByFunds();
                    } else {
                        //商品
                        goShoptopay();
                    }
                } else if (activitytype == 3) { //3：直购-套餐送油卡

                    if (type == 4) { //余额支付
                        //弹窗
                        DialogMaker.showRedSureDialog(OilCardPayActivity.this, " ", "确定使用账户余额支付" + amount + "元吗？", "取消", "确定", new DialogMaker.DialogCallBack() {
                            @Override
                            public void onButtonClicked(Dialog dialog, int position, Object tag) {
                                //套餐送油卡
                                getOilCard();
                            }

                            @Override
                            public void onCancelDialog(Dialog dialog, Object tag) {
                                ToastMaker.showShortToast("支付取消");
                            }
                        }, "");
                    } else {
                        //套餐送油卡
                        getOilCard();
                    }
                } else {
                    //油卡
                    if (type == 4) {
                        rechargeByFunds();
                    } else {
                        getData();
                    }
                }


                /*
                *  else if(activitytype == 5) { //油卡在支付
                    //油卡

                    if (type == 4) {
                        rechargeByFunds();
                    } else {
                        getData();
                    }
                      }
                * */

                break;
        }
    }

    /**
     * 8.下单
     */

    private void getData() {
        showWaitDialog("加载中...", true, "");

      /*  String money;
        if (fromPackage) {
            money = monthMoney + ""; //单笔金额;
        } else {
            money = new BigDecimal(amount).toString(); //单笔金额;
        }*/


        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("uid", uid);
        map.put("fuelCardId", fuelCardId + "");//油卡id

        map.put("type", type + ""); //1支付宝 、2微信支付、3云闪付

        map.put("pid", pid + ""); //产品id
        map.put("tradeType", "APP"); //微信支付类型 App；APP   H5:JSAPI


        map.put("isMemberFunds", MemberFunds + ""); //1:需要余额，2：不需要余额

        if (fid != 0) {
            map.put("fid", fid + ""); //优惠券id
        }

        if (orderDetail != 0) {
            map.put("id", orderDetail + "");//订单id-->从订单详情过来的
        }


        if (fromPackage) {
            map.put("amount", monthMoney + ""); //套餐 每月的金额
        } else {
            //new BigDecimal(amount).toString()
            map.put("amount", money + ""); //单笔金额;
        }

        map.put("token", LocalApplication.getInstance().sharereferences.getString("token", ""));
        map.put("version", UrlConfig.version);
        map.put("channel", "2");
        LogUtils.e("下单uid" + uid + "/fuelCardId" + fuelCardId + "/type"
                + type + "/amount" + amount + "/pid" + pid + "/fid" + fid
                + "/monthMoney" + monthMoney + "/money" + money + "MemberFunds" + MemberFunds);

        //LogUtils.e(amount+""+"下单-->"+new BigDecimal(amount).toString());

        OkHttpEngine.create().setHeaders().post(UrlConfig.rechargeFuelCardToPay, params,
                new OkHttpEngine.OkHttpCallback() {
                    @Override
                    public void onSuccess(String result) {
                        LogUtils.e("--->下单：" + result);
                        dismissDialog();

                        JSONObject obj = JSON.parseObject(result);
                        //true=成功处理，false=不成功处理
                        if (obj.getBoolean(("success"))) {

                            JSONObject map = obj.getJSONObject("map");

                            //JSONObject orderDetail = map.getJSONObject("orderDetail");
                          /*  if (type == 3) {
                               //  orderId =Integer.parseInt(map.getString("tn").trim());
                               // long tn = Long.parseLong(map.getString("tn").trim());
                                // LogUtils.e("/" + map.getString("tn"));
                                orderId = map.getString("tn");
                            } else {
                                orderId = map.getString("orderId");
                            }*/
                            orderId = map.getString("orderId");

                            LogUtils.e("orderId------>" + orderId);

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("orderId", orderId);
                            editor.putInt("activitytype", activitytype);
                            editor.commit();


                            if (type == 1) {
                                AtyZfbPayInfo atyPayTestInfo = JSON.toJavaObject(map, AtyZfbPayInfo.class);

                                String txtPay = atyPayTestInfo.getResult();
                                alipay(txtPay);

                                /*if (atyPayTestInfo.isSuccess()) {

                                } else {
                                    ToastUtil.showToast("支付异常,请稍后重试");
                                }*/
                            } else if (type == 2) {
                                atyWeChatInfo = JSON.toJavaObject(map, AtyWeChatInfo.class);

                                if (isWXAppInstalledAndSupported()) {
                                    //微信支付

                                    PayReq req = new PayReq();
                                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                                    req.appId = atyWeChatInfo.getWechatMap().getAppid();
                                    req.partnerId = atyWeChatInfo.getWechatMap().getPartnerid();
                                    req.prepayId = atyWeChatInfo.getWechatMap().getPrepayid();
                                    req.nonceStr = atyWeChatInfo.getWechatMap().getNoncestr();
                                    req.timeStamp = atyWeChatInfo.getWechatMap().getTimestamp();
                                    //req.packageValue = atyWeChatInfo.getWechatMap().getPackageX();
                                    req.packageValue = "Sign=WXPay";
                                    req.sign = atyWeChatInfo.getWechatMap().getPaySign();
                                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                    //  LogUtils.e("req"+atyWeChatInfo.getWechatMap().getPackageX());

                                    //api.registerApp(Constants.APP_ID);
                                    api.registerApp(req.appId);
                                    api.sendReq(req);

                                } else {
                                    ToastUtil.showToast("您未安装最新版本微信，不支持微信支付，请安装或升级微信版本");
                                }

                            } else {
                                //云闪付
                                // “00” – 银联正式环境
                                // “01” – 银联测试环境，该环境中不发生真实交易
                                String serverMode = "00";
                                JSONObject map1 = obj.getJSONObject("map");
                                String tn = map1.getString("tn");

                                if (!TextUtils.isEmpty(tn)) {

                                 /*   if (UPPayAssistEx.checkInstalled(OilCardPayActivity.this)) {
                                        //当判断用户手机上已安装银联Apk，商户客户端可以做相应个性化处理

                                    } else {
                                        ToastMaker.showShortToast("请先安装银联apk");
                                    }*/
                                    UPPayAssistEx.startPay(OilCardPayActivity.this, null, null, tn, serverMode);

                                }
                            }


                        } else if ("1004".equals(obj.getString("errorCode"))) {

                            if ((activitytype == 1 | activitytype == 5) & !fromPackage) {
                                showDialog(obj.getString("errorMsg"), obj.getString("msg"));
                            }
                            // ToastMaker.showShortToast(obj.getString("errorMsg"));

                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
//                            new show_Dialog_IsLogin(MessageCenterActivity.this).show_Is_Login();
                        } else {
                            ToastUtil.showToast("支付异常,请稍后重试");
                        }
                    }

                    @Override
                    public void onFail(IOException e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });


          /*  @Override
            public void onLogicSuccess(String data) {
                dismissDialog();
                if (!UtilString.isEmpty(data)) {
                    if (type == 1) {
                        AtyZfbPayInfo atyPayTestInfo = GsonUtil.parseJsonToBean(data, AtyZfbPayInfo.class);
                        //true=成功处理，false=不成功处理

                        String txtPay = atyPayTestInfo.getResult();
                        alipay(txtPay);
                    } else {

                        atyWeChatInfo = GsonUtil.parseJsonToBean(data, AtyWeChatInfo.class);
                        if (isWXAppInstalledAndSupported()) {
                            //微信支付

                            PayReq req = new PayReq();
                            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                            req.appId = atyWeChatInfo.getWechatMap().getAppid();
                            req.partnerId = atyWeChatInfo.getWechatMap().getPartnerid();
                            req.prepayId = atyWeChatInfo.getWechatMap().getPrepayid();
                            req.nonceStr = atyWeChatInfo.getWechatMap().getNoncestr();
                            req.timeStamp = atyWeChatInfo.getWechatMap().getTimestamp();
                            req.packageValue = atyWeChatInfo.getWechatMap().getPackageX();
                            req.sign = atyWeChatInfo.getWechatMap().getPaySign();
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            api.registerApp(Constants.APP_ID);
                            api.sendReq(req);
                        } else {
                            ToastUtil.showToast("您未安装最新版本微信，不支持微信支付，请安装或升级微信版本");
                        }

                    }


                } else {
                    ToastUtil.showToast("支付异常,请稍后重试");
                }

            }

            @Override
            public void onLogicError(int code, String msg) {
                dismissDialog();
                ToastUtil.showToast("请检查网络");
                //dismissDialog();
            }

            @Override
            public void onError(IOException e) {
                dismissDialog();
                ToastUtil.showToast("请检查网络");
            }
        });
        */


    }

    /**
     * 27.余额支付
     */
    public void rechargeByFunds() {
        //弹窗
        DialogMaker.showRedSureDialog(OilCardPayActivity.this, " ", "确定使用账户余额支付" + amount + "元吗？", "取消", "确定", new DialogMaker.DialogCallBack() {
            @Override
            public void onButtonClicked(Dialog dialog, int position, Object tag) {

                showWaitDialog("支付中...", true, "");

                LogUtils.e("uid" + uid + "fuelCardId" + fuelCardId + "amount" + amount + "pid" + pid);

                PostFormBuilder postFormBuilder = OkHttpUtils.post()
                        .url(UrlConfig.rechargeByFunds)
                        .addParams("uid", preferences.getString("uid", ""))
                        .addParams("fuelCardId", fuelCardId + "") //油卡id或手机号
                        //.addParams("amount", amount + "") //备注
                        .addParams("pid", pid + "");
                //.addParams("fid", fid + "");
                if (fid != 0) {
                    postFormBuilder.addParams("fid", fid + "");
                }

                if (orderDetail != 0) {
                    postFormBuilder.addParams("id", orderDetail + "");//订单id-->从订单详情过来的
                }

                if (fromPackage) {
                    postFormBuilder.addParams("amount", monthMoney + "");
                } else {
                    if (orderDetail != 0) {
                        postFormBuilder.addParams("amount", amount + "");//订单id-->从订单详情过来的 支付金额
                    } else {
                        postFormBuilder.addParams("amount", money + "");

                    }
                }
                postFormBuilder //红包id
                        .addParams("version", UrlConfig.version)
                        .addParams("channel", "2")
                        .build().execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        dismissDialog();

                        LogUtils.i("29.余额支付：" + response);

                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) { //

                            JSONObject map = obj.getJSONObject("map");
                            orderId = map.getString("investId");
                            DialogMaker.showPaySuccessDialog(OilCardPayActivity.this, callBack,
                                    activitytype, orderId);
                            //{"investId":7233}

                            // ToastMaker.showShortToast("支付成功");

                        } else if ("1006".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("余额不足");
                        } else if ("1004".equals(obj.getString("errorCode"))) {
                            if ((activitytype == 1 | activitytype == 5) & !fromPackage) {
                                showDialog(obj.getString("errorMsg"), obj.getString("msg"));
                            }
                            // ToastMaker.showShortToast(obj.getString("errorMsg"));

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
                        //refreshLayout.finishRefresh();
                    }
                });


            }

            @Override
            public void onCancelDialog(Dialog dialog, Object tag) {
                ToastMaker.showShortToast("支付取消");
            }
        }, "");


    }


    /**
     * 27.余额支付
     */
    public void shopRechargeByFunds() {
        //弹窗
        DialogMaker.showRedSureDialog(OilCardPayActivity.this, " ", "确定使用账户余额支付" + amount + "元吗？", "取消", "确定", new DialogMaker.DialogCallBack() {
            @Override
            public void onButtonClicked(Dialog dialog, int position, Object tag) {

                showWaitDialog("支付中...", true, "");

                LogUtils.e(UrlConfig.shopRechargeByFunds + "uid" + uid + "fuelCardId" + addressid
                        + "number" + number + "pid" + pid);

                PostFormBuilder postFormBuilder = OkHttpUtils.post()
                        .url(UrlConfig.shopRechargeByFunds)
                        .addParams("uid", preferences.getString("uid", ""))
                        .addParams("fuelCardId", addressid + "") //油卡id或手机号
                        .addParams("number", number + "") //备注
                        .addParams("pid", pid + "");
                //.addParams("fid", fid + "");

                //postFormBuilder.addParams("fid", addressid + ""); //地址主键
                //  if (orderDetail != 0) {
                //      postFormBuilder.addParams("id", orderDetail + "");//订单id-->从订单详情过来的
                //   }

                //    if (fromPackage) {
                //        postFormBuilder.addParams("amount", monthMoney + "");
                //  } else {
                //    if (orderDetail != 0) {
                //      postFormBuilder.addParams("amount", amount + "");//订单id-->从订单详情过来的 支付金额
                //} else {
                //  postFormBuilder.addParams("amount", money + "");
//
                //                  }
                //            }
                postFormBuilder //红包id
                        .addParams("version", UrlConfig.version)
                        .addParams("channel", "2")
                        .build().execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        dismissDialog();

                        LogUtils.i("33.余额支付-商城：" + response);

                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) { //

                            JSONObject map = obj.getJSONObject("map");
                            orderId = map.getString("investId");
                            DialogMaker.showPaySuccessDialog(OilCardPayActivity.this, callBack,
                                    activitytype, orderId);
                            //{"investId":7233}

                            // ToastMaker.showShortToast("支付成功");

                        } else if ("1006".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("余额不足");
                        } else if ("1004".equals(obj.getString("errorCode"))) {
                            if ((activitytype == 1 | activitytype == 5) & !fromPackage) {
                                showDialog(obj.getString("errorMsg"), obj.getString("msg"));
                            }
                            // ToastMaker.showShortToast(obj.getString("errorMsg"));

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
                        //refreshLayout.finishRefresh();
                    }
                });


            }

            @Override
            public void onCancelDialog(Dialog dialog, Object tag) {
                ToastMaker.showShortToast("支付取消");
            }
        }, "");


    }

    /**
     * //3：直购-套餐送油卡
     */
    private void getOilCard() {

        // TODO: 2019/2/21
        showWaitDialog("加载中...", true, "");
        LogUtils.e("套餐送油卡 pid" + pid + "/amount" + monthMoney + "/uid" + uid
                + "/cardType" + cardType + "/fid" + fid + "/payType" + type + "/fuelId" + addressid
                + "/productCardId" + productCardId + "/MemberFunds" + MemberFunds);
        if (bz == null) {
            bz = "";
        }
        PostFormBuilder postFormBuilder = OkHttpUtils.post()
                .url(UrlConfig.getOilCard)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("payType", type + "")
                .addParams("amount", monthMoney + "") //油卡id或手机号
                .addParams("productCardId", productCardId + "") //
                .addParams("fuelId", addressid + "") //地址主键
                .addParams("cardType", cardType + "") //油卡类型 1:中石化 2:中石油
                .addParams("tradeType", "APP") //微信支付类型 App；APP   H5:JSAPI
                .addParams("pid", pid + "");


        if (type == 4) { //余额支付
            postFormBuilder.addParams("isMemberFunds", "1");
        } else if (MemberFunds == 1) { //混合支付 1:需要余额 2：不需要余额   // MemberFunds = 1; //混合支付
            postFormBuilder.addParams("isMemberFunds", "1");
        } else {
            postFormBuilder.addParams("isMemberFunds", "2");
        }

        //二次下单使用
        if (orderDetail != 0) {
            postFormBuilder.addParams("id", orderDetail + "");//订单id-->从订单详情过来的
        }
        if (fid != 0) {
            postFormBuilder.addParams("fid", fid + "");  //优惠券id
        }


        postFormBuilder
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                //ptr_address.refreshComplete();
                LogUtils.e("4.下单" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {

                    JSONObject map = obj.getJSONObject("map");


                    orderId = map.getString("investId");

                    LogUtils.e("orderId------>" + orderId);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("orderId", orderId);
                    editor.putInt("activitytype", activitytype);
                    editor.commit();


                    if (type == 1) {
                        AtyZfbPayInfo atyPayTestInfo = JSON.toJavaObject(map, AtyZfbPayInfo.class);

                        String txtPay = atyPayTestInfo.getResult();
                        alipay(txtPay);


                    } else if (type == 2) {
                        atyWeChatInfo = JSON.toJavaObject(map, AtyWeChatInfo.class);

                        if (isWXAppInstalledAndSupported()) {
                            //微信支付

                            PayReq req = new PayReq();
                            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                            req.appId = atyWeChatInfo.getWechatMap().getAppid();
                            req.partnerId = atyWeChatInfo.getWechatMap().getPartnerid();
                            req.prepayId = atyWeChatInfo.getWechatMap().getPrepayid();
                            req.nonceStr = atyWeChatInfo.getWechatMap().getNoncestr();
                            req.timeStamp = atyWeChatInfo.getWechatMap().getTimestamp();
                            //req.packageValue = atyWeChatInfo.getWechatMap().getPackageX();
                            req.packageValue = "Sign=WXPay";
                            req.sign = atyWeChatInfo.getWechatMap().getPaySign();
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            //  LogUtils.e("req"+atyWeChatInfo.getWechatMap().getPackageX());

                            //api.registerApp(Constants.APP_ID);
                            api.registerApp(req.appId);
                            api.sendReq(req);

                        } else {
                            ToastUtil.showToast("您未安装最新版本微信，不支持微信支付，请安装或升级微信版本");
                        }

                    } else if (type == 4) { //余额支付
                        DialogMaker.showPaySuccessDialog(OilCardPayActivity.this, callBack,
                                activitytype, orderId);
                    } else {
                        //云闪付
                        // “00” – 银联正式环境
                        // “01” – 银联测试环境，该环境中不发生真实交易
                        String serverMode = "00";
                        JSONObject map1 = obj.getJSONObject("map");
                        String tn = map1.getString("tn");

                        if (!TextUtils.isEmpty(tn)) {

                                 /*   if (UPPayAssistEx.checkInstalled(OilCardPayActivity.this)) {
                                        //当判断用户手机上已安装银联Apk，商户客户端可以做相应个性化处理

                                    } else {
                                        ToastMaker.showShortToast("请先安装银联apk");
                                    }*/
                            UPPayAssistEx.startPay(OilCardPayActivity.this, null, null, tn, serverMode);

                        }
                    }

                } else if ("9999".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("系统异常");
                } else if ("1006".equals(obj.getString("errorCode"))) {
                    //库存不足，支付失败
                    ToastMaker.showShortToast(obj.getString("errorMsg"));
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("系统异常");
//                            new show_Dialog_IsLogin(MessageCenterActivity.this).show_Is_Login();
                } else {
                    ToastUtil.showToast("支付异常,请稍后重试");
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
     * 4.商品 下单
     */
    private void goShoptopay() {

        showWaitDialog("加载中...", true, "");
        LogUtils.e("商品 下单 pid" + pid + "/number" + number + "/uid" + uid
                + "/type" + type + "/fid" + addressid + "/bz" + bz + "/shoporderId" + shoporderId);
        if (bz == null) {
            bz = "";
        }

        PostFormBuilder postFormBuilder = OkHttpUtils.post()
                .url(UrlConfig.shoptopay)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("pid", pid + "");

        RequestCall build;
        if (shoporderId == 0) {
            postFormBuilder

                    .addParams("bz", bz) //备注
                    //.addParams("id", shoporderId + "") //商品的订单 在支付
                    .addParams("number", number + "") //数量
                    .addParams("type", type + "")
                    .addParams("fid", addressid + "") //地址主键
                    .addParams("tradeType", "APP")
            ;
        } else {
            postFormBuilder

                    .addParams("bz", bz) //备注
                    .addParams("id", shoporderId + "") //商品的订单 在支付
                    .addParams("number", number + "") //数量
                    .addParams("type", type + "")
                    .addParams("fid", addressid + "") //地址主键
                    .addParams("tradeType", "APP");

        }

        if (isMemberFunds) { //混合支付
            postFormBuilder.addParams("isMemberFunds", "1");
        }  else {
            postFormBuilder.addParams("isMemberFunds", "0");
        }


        postFormBuilder
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                //ptr_address.refreshComplete();
                LogUtils.e("4.下单" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {

                    JSONObject map = obj.getJSONObject("map");

                    if (shoporderId == 0) {
                        orderId = map.getString("orderId");

                    } else {
                        orderId = shoporderId + "";
                    }
                    //

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("orderId", orderId);
                    editor.putInt("activitytype", activitytype);
                    editor.commit();


                    if (type == 1) {
                        AtyZfbPayInfo atyPayTestInfo = JSON.toJavaObject(map, AtyZfbPayInfo.class);

                        String txtPay = atyPayTestInfo.getResult();
                        alipay(txtPay);


                    } else if (type == 2) {
                        atyWeChatInfo = JSON.toJavaObject(map, AtyWeChatInfo.class);

                        if (isWXAppInstalledAndSupported()) {
                            //微信支付

                            PayReq req = new PayReq();
                            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                            req.appId = atyWeChatInfo.getWechatMap().getAppid();
                            req.partnerId = atyWeChatInfo.getWechatMap().getPartnerid();
                            req.prepayId = atyWeChatInfo.getWechatMap().getPrepayid();
                            req.nonceStr = atyWeChatInfo.getWechatMap().getNoncestr();
                            req.timeStamp = atyWeChatInfo.getWechatMap().getTimestamp();
                            //req.packageValue = atyWeChatInfo.getWechatMap().getPackageX();
                            req.packageValue = "Sign=WXPay";
                            req.sign = atyWeChatInfo.getWechatMap().getPaySign();
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            //  LogUtils.e("req"+atyWeChatInfo.getWechatMap().getPackageX());

                            //api.registerApp(Constants.APP_ID);
                            api.registerApp(req.appId);
                            api.sendReq(req);

                        } else {
                            ToastUtil.showToast("您未安装最新版本微信，不支持微信支付，请安装或升级微信版本");
                        }

                    } else {
                        //云闪付
                        // “00” – 银联正式环境
                        // “01” – 银联测试环境，该环境中不发生真实交易
                        String serverMode = "00";
                        JSONObject map1 = obj.getJSONObject("map");
                        String tn = map1.getString("tn");

                        if (!TextUtils.isEmpty(tn)) {

                                 /*   if (UPPayAssistEx.checkInstalled(OilCardPayActivity.this)) {
                                        //当判断用户手机上已安装银联Apk，商户客户端可以做相应个性化处理

                                    } else {
                                        ToastMaker.showShortToast("请先安装银联apk");
                                    }*/
                            UPPayAssistEx.startPay(OilCardPayActivity.this, null, null, tn, serverMode);

                        }
                    }

                } else if ("9999".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("系统异常");
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    ToastMaker.showShortToast("系统异常");
//                            new show_Dialog_IsLogin(MessageCenterActivity.this).show_Is_Login();
                } else {
                    ToastUtil.showToast("支付异常,请稍后重试");
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

    private void showDialog(String title, String content) {
        DialogMaker.showAddOilCardSureDialog(this, title,
                content, "暂不需要", "去看看", new DialogMaker.DialogCallBack() {
                    @Override
                    public void onButtonClicked(Dialog dialog, int position, Object tag) {
                        // exit_dr();
                        // deleteOilCard(oillist.get(position1).getId());

                        startActivity(new Intent(OilCardPayActivity.this,
                                OilCardPackageActivity.class));
                        OilCardPayActivity.this.finish();

                    }

                    @Override
                    public void onCancelDialog(Dialog dialog, Object tag) {

                    }
                }, "");
    }

    /**
     * 调用支付宝的方法完成支付
     */
    protected void alipay(final String payInfo) {

        LogUtils.e("--->支付宝：" + payInfo);

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {


                PayTask alipay = new PayTask(OilCardPayActivity.this);
                Map<String, String> result = alipay.payV2(payInfo, true);
                LogUtils.e("--->下单111：" + result);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private boolean isWXAppInstalledAndSupported() {

        IWXAPI msgApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        msgApi.registerApp(Constants.APP_ID);

        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled();

        return sIsWXAppInstalledAndSupported;
    }

    /**
     * 云闪付
     * <p>
     * activity —— 用于启动支付控件的活动对象
     * spId —— 保留使用，这里输入null
     * sysProvider —— 保留使用，这里输入null
     * orderInfo —— 订单信息为交易流水号，即TN，为商户后台从银联后台获取。
     * mode —— 银联后台环境标识，“00”将在银联正式环境发起交易,“01”将在银联测试环境发起
     * 交易
     * 返回值：
     * UPPayAssistEx.PLUGIN_VALID —— 该终端已经安装控件，并启动控件
     * UPPayAssistEx.PLUGIN_NOT_FOUND — 手机终端尚未安装支付控件，需要先安装支付控件
     */

    public void doStartUnionPayPlugin(String tn) {
        // mMode参数解释：
        // 0 - 启动银联正式环境
        // 1 - 连接银联测试环境
        int ret = UPPayAssistEx.startPay(this, null, null, tn, mMode);
        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
            // 需要重新安装控件
            LogUtils.e(" plugin not found or need upgrade!!!");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UPPayAssistEx.installUPPayPlugin(OilCardPayActivity.this);
                            dialog.dismiss();
                        }
                    });

            builder.setPositiveButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();

        }
        LogUtils.e("" + ret);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        LogUtils.e("云闪付回调" + str);
        if (str.equalsIgnoreCase("success")) {

            // 如果想对结果数据验签，可使用下面这段代码，但建议不验签，直接去商户后台查询交易结果
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {

              /*  String result = data.getExtras().getString("result_data");
                try {
                   // JSONObject resultJson = new JSONObject(result);

                    String sign = data.getExtras().getString("sign");
                    String dataOrg = data.getExtras().getString("data");
                    // 此处的verify建议送去商户后台做验签
                    // 如要放在手机端验，则代码必须支持更新证书

                    //boolean ret = verify(dataOrg, sign, mMode);
                    boolean ret = verify(dataOrg, sign, mMode);

                    if (ret) {
                        // 验签成功，显示支付结果
                        msg = "支付成功！";
                    } else {
                        // 验签失败
                        msg = "支付失败！";
                    }

                } catch (JSONException e) {
                }*/


                String sign = data.getExtras().getString("result_data");

                LogUtils.e("云闪付回调" + str + sign);

                DialogMaker.showPaySuccessDialog(OilCardPayActivity.this, callBack,
                        activitytype, orderId);
                // ToastUtil.showToast("支付成功");


            }
            // 结果result_data为成功时，去商户后台查询一下再展示成功
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
            ToastMaker.showLongToast("订单支付失败");
            // finish();
            payfail();
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
            ToastMaker.showLongToast(msg);
            // finish();
            payfail();
        }

      /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();*/
    }

    /**
     * 第三方支付的时候,用户取消或者支付失败
     */
    private void payfail() {

        LogUtils.e("payfail" + orderId);
        if (!TextUtils.isEmpty(orderId)) {
            // 1：油卡 2：手机 3：直购
            switch (activitytype) {
                case 5: //油卡再支付
                case 1:
                    startActivity(new Intent(OilCardPayActivity.this, MyOrderDetailsActivity.class)
                            .putExtra("orderId", orderId)
                            .putExtra("type", activitytype)
                    );
                    finish();
                    break;
                case 2:
                    startActivity(new Intent(OilCardPayActivity.this, MyOrderDetailsActivity.class)
                            .putExtra("orderId", orderId)
                            .putExtra("type", activitytype)
                    );
                    finish();
                    break;
                case 3:

                    finish();

                    startActivity(new Intent(OilCardPayActivity.this, MyOilCardBuyDetailsActivity.class)
                            .putExtra("orderId", orderId)
                            .putExtra("type", activitytype)
                    );

                    break;
                case 4:
                    startActivity(new Intent(OilCardPayActivity.this, MallOrderDetailsActivity.class)
                            .putExtra("orderId", Integer.parseInt(orderId))
                            .putExtra("type", activitytype)
                    );
                    finish();
                    break;
            }
        } else {
            finish();
        }
    }

    /**
     * 28.余额查询
     */
    private void getBalanceData() {
        //showWaitDialog("加载中...", true, "");

        OkHttpUtils.post()
                .url(UrlConfig.myFunds)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                //dismissDialog();
                LogUtils.i("29.余额明细：" + response);
                //refreshLayout.finishRefresh();
                //{"map":{"freeze":0.00,"balance":10000.00},"success":true}
                JSONObject obj = JSON.parseObject(response);

                if (obj.getBoolean(("success"))) { //
                    JSONObject map = obj.getJSONObject("map");

                    balance = map.getDoubleValue("balance");
                    tvBalance.setText(StringCut.getNumKb(balance));


                    // TODO: 2019/1/22

                    if (balance > 0 && balance < amount) {

                        MemberFunds = 1; //混合支付

                        isMemberFunds = true;//可以混合

                        surplusbalance = Arith.sub(amount, balance);

                        llBalance.setClickable(true);
                        cbBalance.setChecked(true);

                    } else if (balance >= amount) {
                        // MemberFunds = 2;

                        isMemberFunds = false;

                        surplusbalance = 0;

                        llBalance.setClickable(true);
                        cbBalance.setChecked(true);

                        tvBankMoney.setChecked(false);
                        cbAlibaba.setChecked(false);
                        cbWeixin.setChecked(false);

                        type = 4; //余额支付

                    } else {
                        // MemberFunds = 2;
                        isMemberFunds = false;

                        llBalance.setClickable(false);
                        cbBalance.setChecked(false);

                        surplusbalance = amount;
                    }

                    /*
                    //冻结的金额
                    freeze = map.getDoubleValue("freeze");
                    if (freeze != 0) {
                        // MemberFunds = 2; // 有冻结的金额 不混合支付
                        llBalance.setClickable(false);
                        cbBalance.setVisibility(View.GONE);
                      //  cbBalance.setChecked(true);
                        surplusbalance = Arith.sub(amount, freeze);
                        //  surplusbalance=freeze;
                        // tvSurplusAmount.setText("-¥" + StringCut.getNumKb(surplusbalance));


                        tvBalance.setText("-¥" + StringCut.getNumKb(freeze));
                    } else {
                        llBalance.setClickable(false);
                        cbBalance.setChecked(false);
                        cbBalance.setVisibility(View.VISIBLE);
                    }*/

                    //冻结的金额
                    if (interest != 0 && orderDetail != 0) {

                        tvBalance.setText("-¥" + StringCut.getNumKb(interest));
                        surplusbalance = Arith.sub(amount, interest);
                    }

                    tvSurplusAmount.setText("¥" + StringCut.getNumKb(surplusbalance));


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
                //dismissDialog();
                ToastMaker.showShortToast("请检查网络");
                //refreshLayout.finishRefresh();
            }
        });
    }


    private void setCheckBox(int number) {

        if (balance == 0) {


        } else {

        }


        // 1支付宝 、2微信支付、3云闪付  4 余额
        type = 3;
        // cbFuyou.setChecked(true);
        tvBankMoney.setChecked(true);

        cbAlibaba.setChecked(false);
        cbWeixin.setChecked(false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // finish();
        LogUtils.e("onRestart");
        if (type == 2) {
            payfail();
        }

    }
}
