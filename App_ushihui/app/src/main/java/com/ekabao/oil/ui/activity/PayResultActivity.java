package com.ekabao.oil.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.SecurityUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.show_Dialog_IsLogin;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 易卡宝  App
 * 立即支付 --->支付结果
 *
 * @time 2018/8/2 16:03
 * Created by lj on 2018/8/2 16:03.
 */

public class PayResultActivity extends BaseActivity {
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
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_cycle)
    TextView tvCycle;
    @BindView(R.id.tv_profit)
    TextView tvProfit;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.iv_state)
    ImageView ivState;

    private String name;
    private int deadline;

   // private String money;

    private double interest;
    private long investTime;


    private String pid;  //产品id
    private String experienceId = "";//选择体验金对应的id
    private String isNewPay = "0";//出借方式(0位余额出借,1为充值出借)
    private String SmsCode = "";//短信验证码
    private String systemOrders = "";//订单编号(支付发送验证码接口返回)
    private String rechargeAmount = "";//充值金额
    private String fid = ""; //优惠券ID
    private String amount = ""; //总金额
    private String firstPwd = ""; //密码
    private String uid;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_result;
    }

    @Override
    protected void initParams() {
        titleLeftimageview.setVisibility(View.GONE);
        titleCentertextview.setText("结果详情");

        tvName.setText(name);
        tvCycle.setText(deadline + "天");

        tvMoney.setText(StringCut.getNumKb(Double.parseDouble(amount)));
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        product_Invest();

    }

    private void product_Invest() {
        showWaitDialog("加载中...", false, "");
        LogUtils.i("--->普通Invest pid:" + pid + ", uid:" + uid + ", tpwd:" + firstPwd +
                ", amount:" + amount +
                ", isNewPay:" + isNewPay + ", verifyCode:" + SmsCode
                + ", systemOrders:" + systemOrders + ", rechargeAmount:" +
                rechargeAmount + ", fid:" + fid + ", experienceId:" + experienceId);


        OkHttpUtils
                .post()
                .url(UrlConfig.INVEST)
                .addParams("pid", pid)
                .addParams("uid", uid)
                .addParams("tpwd", SecurityUtils.MD5AndSHA256(firstPwd))
                .addParams("amount", amount)
                .addParam("experienceId", experienceId)
                .addParam("isNewPay", isNewPay)
                .addParam("verifyCode", SmsCode)
                .addParam("systemOrders", systemOrders)
                .addParam("rechargeAmount", rechargeAmount)
                .addParams("fid", fid)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("--->支付结果：" + response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {

                            JSONObject map = obj.getJSONObject("map");
                            if (map != null) {
                                interest = map.getDouble("interest");
                                long investTime = map.getLong("investTime");

                                /*expireDate = map.getLong("expireDate");

                                investId = map.getInteger("investId");
                                luckcode = map.getString("luckCodes");
                                luckcodesize = map.getString("luckCodeCount");
                                JSONArray textList = map.getJSONArray("textList");
                                textStringList = JSON.parseArray(textList.toJSONString(), String.class);

                                jumpURLActivity = map.getString("activityUrl");*/

                                tvState.setText("支付成功");
                                ivState.setImageResource(R.drawable.icon_pay_success);
                                tvTime.setText(StringCut.getDateTimeToStringheng(investTime));
                                tvProfit.setText(StringCut.getDoubleKb(interest) + "元");


                            } else {

                            }

                        } else if ("2001".equals(obj.getString("errorCode"))) {
                            tvState.setText("支付失败");
                            ivState.setImageResource(R.drawable.icon_pay_fail);
                            MobclickAgent.onEvent(PayResultActivity.this, UrlConfig.point + 20 + "");
                            ToastMaker.showShortToast("连续输错三次密码，交易密码锁定一小时！请稍后再试或点击忘记密码");
                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            tvState.setText("支付失败");
                            ivState.setImageResource(R.drawable.icon_pay_fail);
                            MobclickAgent.onEvent(PayResultActivity.this, UrlConfig.point + 20 + "");
                            ToastMaker.showShortToast("密码输入错误，请重新输入");
                        } else {
                            tvState.setText("支付失败");
                            ivState.setImageResource(R.drawable.icon_pay_fail);

                            MobclickAgent.onEvent(PayResultActivity.this, UrlConfig.point + 20 + "");

                            if ("XTWH".equals(obj.getString("errorCode"))) {
                                startActivity(new Intent(PayResultActivity.this, WebViewActivity.class)
                                        .putExtra("URL", UrlConfig.WEIHU)
                                        .putExtra("TITLE", "系统维护"));
                                return;
                            } else if ("1002".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("产品已募集完");
                            } else if ("1003".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("项目可出借金额不足");
                            } else if ("1004".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("小于起投金额");
                            } else if ("1005".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("非递增金额整数倍");
                            } else if ("1006".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("出借金额大于项目单笔出借限额");
                            } else if ("1007".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("账户可用余额不足");
                            } else if ("1008".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("已出借过产品，不能出借新手产品");
                            } else if ("1009".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("用户不存在");
                            } else if ("9998".equals(obj.getString("errorCode"))) {
                                new show_Dialog_IsLogin(PayResultActivity.this).show_Is_Login();
                            } else if ("1010".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("不符合优惠券使用条件");
                            } else if ("1011".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("出借失败,请稍后再试");
                            } else if ("1012".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("非首投用户，不能使用系统赠送的翻倍券");
                            } else if ("1013".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("活动标不能使用优惠券");
                            } else if ("1015".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("翻倍券只能用于翻倍标");
                            } else if ("3001".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("该产品尚未开始募集，请耐心等待！");
                            } else if ("3004".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("充值失败");
                            } else if ("8888".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("请重新获取短信验证码");
                            } else if ("9999".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("系统异常");
                            } else {
                                ToastMaker.showShortToast("系统异常");
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        //ptrProDetail.refreshComplete();
                        dismissDialog();
                        ToastMaker.showShortToast("网络异常，请检查");
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {

            pid = intent.getStringExtra("pid");
            uid = intent.getStringExtra("uid");
            firstPwd = intent.getStringExtra("tpwd");
            amount = intent.getStringExtra("amount");
            experienceId = intent.getStringExtra("experienceId");
            isNewPay = intent.getStringExtra("isNewPay");
            SmsCode = intent.getStringExtra("verifyCode");
            systemOrders = intent.getStringExtra("systemOrders");
            rechargeAmount = intent.getStringExtra("rechargeAmount");
            fid = intent.getStringExtra("fid");
            name=intent.getStringExtra("name");
            deadline=intent.getIntExtra("deadline",0);

        }
    }
}
