package com.ekabao.oil.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.BankName_Pic;
import com.ekabao.oil.bean.CouponsBean;
import com.ekabao.oil.bean.ProductDetail;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.OkHttpEngine;
import com.ekabao.oil.http.PostParams;
import com.ekabao.oil.http.SimpleHttpCallback;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.me.MeWelfareActivity;
import com.ekabao.oil.ui.activity.me.RealNameActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.GsonUtil;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.SecurityUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.ui.view.PwdEdittext.TradePwdPopUtils;
import com.ekabao.oil.util.show_Dialog_IsLogin;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class InvestActivity extends BaseActivity {

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

    @BindView(R.id.tv_surplusAmount)
    TextView tvSurplusAmount;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.ll_coupon)
    LinearLayout llCoupon;
    @BindView(R.id.cb_balance)
    CheckBox cbBalance;
    @BindView(R.id.iv_bank)
    ImageView ivBank;
    @BindView(R.id.tv_bank_money)
    TextView tvBankMoney;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_bank_limit)
    TextView tvBankLimit;
    @BindView(R.id.tv_loan_agreement)
    TextView tvLoanagreement;
    @BindView(R.id.bt_pay)
    Button btPay;
    @BindView(R.id.rl_bottom)
    LinearLayout rlBottom;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_saowei)
    TextView tvSaowei;
    @BindView(R.id.tv_prodetail_income_add)
    TextView tv_prodetail_income_add;
    @BindView(R.id.tv_prodetail_income_name)
    TextView tv_prodetail_income_name;


    /**
     * 项目详情 --> 立即出借
     */
    private double leastaAmount; //起投金额
    private SharedPreferences preferences;
    private static final int loginCode = 10156; //选择优惠券
    private String fullName;//产品全称
    private int pid;  //产品id
    private String fid = ""; //优惠券ID
    CouponsBean mbean;//优惠券
    private Double activityRate; //活动利率
    private Double rate; //利率
    private double surplusAmount; //剩余可投金额
    private long endTime;//回款日期
    private double balance;//当前登录用户余额
    private double increasAmount; //出借金额需为  100 倍数
    private int deadline; //出借期限 多少天
    private Boolean isRoundOff = false;//是否显示尾单
    private String roundOffMoney;//尾单奖励
    double enableAmount = 0, amountRed = 0, raisedRate = 0, multiple = 1;//优惠券信息，依次是：启用金额，红包，加息，翻倍
    //private List<CouponsBean> listConpons;
    Double shouyi; //预计收益
    Double shouyi_add = 0.0; //优惠券的收益
    Long lastClick = 0L;//防重复点击计时

    private String bankid;
    private BankName_Pic bp;
    private String bankName;
    private String bankNum;
    private Integer singleQuotaJYT;
    private Integer dayQuotaJYT;

    // private JSONArray couponList;
    private List<CouponsBean> couponList;
    private String experienceId = "";//选择体验金对应的id

    String SmsCode = "";//短信验证码
    private Long expireDate;//还款日期
    private Double interest;//预计收益
    private List<String> textStringList = new ArrayList<>(); //支付成功返回的信息

    private View layout2;
    private TextView tv_getYzm;//60s倒计时
    private String mobilephone; //银行卡里的手机好

    private boolean sendSmsSuccess = false;//支付验证码是否发送成功

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_invest);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invest;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("立即出借");

        preferences = LocalApplication.getInstance().sharereferences;
        Intent intent = getIntent();

        pid = intent.getIntExtra("pid", 0);


        etMoney.setHint(setetMoneyHint());

        etMoney.setCursorVisible(false);
        etMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMoney.setFocusableInTouchMode(true);
                etMoney.setFocusable(true);
                etMoney.setCursorVisible(true);
                //etMoney.sethin
            }
        });

        tvLoanagreement.setText(Html.fromHtml("点击确认，即同意<font color='#1E9DFF'>《借款协议》</font>"));

        getDate();
        bankInfo();

        Watcher watcher = new Watcher();
        etMoney.addTextChangedListener(watcher); //输入监听

        /**
         * 选择支付方式
         * */
        cbBalance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String payAmount = etMoney.getText().toString();

                if (isChecked) {//若打勾。出借加上余额
                    if (Double.valueOf(balance) < Double.valueOf(payAmount)) {//账户余额不足
                        rechargeAmount = (Double.valueOf(payAmount) - Double.valueOf(balance)) + "";
                        isNewPay = "1";
                        tvBankMoney.setText(Html.fromHtml("支付 <font color='#EE4845'>(" + rechargeAmount + " )</font>元"));

                    } else {//账户余额充足
                        rechargeAmount = "0";

                        isNewPay = "0";
                        tvBankMoney.setText("无需支付");

                    }
                } else {//若不打勾。出借只用银行卡
                    rechargeAmount = (Double.valueOf(payAmount)) + "";
                    isNewPay = "1";
                    tvBankMoney.setText(Html.fromHtml("支付 <font color='#EE4845'>(" + rechargeAmount + " )</font>元"));

                }
            }
        });

    }


    @OnClick({R.id.title_leftimageview, R.id.tv_loan_agreement, R.id.cb_balance, R.id.bt_pay,
            R.id.tv_saowei, R.id.ll_coupon})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.tv_loan_agreement: //借款协议
                startActivity(new Intent(InvestActivity.this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.JIEKUAN)
                        .putExtra("TITLE", "出借协议"));

                break;
            /*case R.id.cb_balance:
                break;*/
            case R.id.bt_pay:

                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(InvestActivity.this, UrlConfig.point + 37 + "");
                    startActivity(new Intent(InvestActivity.this, LoginActivity.class));
                } else if (etMoney.getText().length() <= 0) {
                    ToastMaker.showShortToast("请输入出借金额");
                } else if (Double.parseDouble(StringCut.douHao_Cut(etMoney.getText().toString())) < leastaAmount) {
                    ToastMaker.showShortToast("出借金额最少为" + StringCut.getNumKbs(leastaAmount));
                } else if ((Double.parseDouble(StringCut.douHao_Cut(etMoney.getText().toString()))) % increasAmount != 0) {
                    ToastMaker.showShortToast("出借金额需为" + StringCut.getNumKbs(increasAmount) + "的倍数");
                } else if (Double.parseDouble(StringCut.douHao_Cut(etMoney.getText().toString())) > surplusAmount) {
                    ToastMaker.showShortToast("出借金额不能大于产品可投金额");
                } else if (!"1".equals(preferences.getString("realVerify", ""))) {
                    ToastMaker.showShortToast("还未认证 ，请认证");
                    MobclickAgent.onEvent(InvestActivity.this, UrlConfig.point + 38 + "");
                    startActivityForResult(new Intent(InvestActivity.this, RealNameActivity.class)
                            .putExtra("proName", fullName)//产品名称
                            .putExtra("proDeadline", deadline)//出借期限
                            .putExtra("proRate", rate)//预期年化收益
                            .putExtra("specialRate", activityRate + "")//活动利率
                            .putExtra("proAmount", etMoney.getText().toString()), 4//出借金额
                    );
                } else {

                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(rlBottom.getWindowToken(), 0);

                    LogUtils.i("--->出借方式：isNewPay=" + isNewPay);
                    //获取用户银行卡信息


                    if (TextUtils.equals(isNewPay, "1")) {
                        ShowSms();
                    } else {


                        memberSetting();
                    }

                }


                break;
            case R.id.tv_saowei://扫尾
                double weidan = Double.valueOf(surplusAmount);
                int w = (int) weidan;
                etMoney.setText(w + "");
                etMoney.setSelection(etMoney.getText().toString().trim().length());
                Money_Get(surplusAmount + "");
                // tv_jifen.setText(calculatePoints(Integer.valueOf(etMoney.getText().toString().trim())) + "");//积分
                // ll_bottom_tip1.setVisibility(View.GONE); //下面的 每万元收益
                //ll_bottom_tip2.setVisibility(View.GONE);
                // ll_bottom_tip3.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_coupon:


                MobclickAgent.onEvent(InvestActivity.this, UrlConfig.point + 23 + "");
                if (System.currentTimeMillis() - lastClick <= 100) {
                    //ToastMaker.showShortToast("点那么快干什么");
                    return;
                }
                lastClick = System.currentTimeMillis();
                if (preferences.getString("uid", "").equalsIgnoreCase("") && pid != 0) {
                    startActivity(new Intent(InvestActivity.this, LoginActivity.class));
                } else {
                    if (!etMoney.getText().toString().equalsIgnoreCase("")) {
                        if (Double.valueOf(etMoney.getText().toString()) > Double.valueOf(surplusAmount)) {
                            ToastMaker.showLongToast("出借金额不能大于产品可投金额");
                            return;
                        }

                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(etMoney.getWindowToken(), 0);

                        // showPopupWindowConpons(listConpons);

                    } /*else {
                        ToastMaker.showLongToast("请输入金额");
                    }*/

                    startActivityForResult(new Intent(InvestActivity.this, MeWelfareActivity.class)
                            .putExtra("type", 1)
                            .putExtra("etMoney", etMoney.getText().toString())
                            .putExtra("pid", pid), loginCode);
                }
                break;
        }

    }

    /**
     * 获取产品详情数据
     */
    private void getDate() {

        OkHttpUtils.post()
                .url(UrlConfig.PRODUCT_DETAIL)
                .addParams("pid", pid + "")
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        //LogPrintUtil.e("LF--->产品详情", response);
                        JSONObject obj = JSON.parseObject(response);
                        dismissDialog();
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");


                            String data = obj.getString("map");
                            ProductDetail productDetail = GsonUtil.parseJsonToBean(data, ProductDetail.class);

                            ProductDetail.InfoBean info = productDetail.getInfo();

                            rate = info.getRate();  //利率
                            activityRate = info.getActivityRate();//活动利率
                            surplusAmount = info.getSurplusAmount(); //剩余可投金额
                            endTime = info.getExpireDate();  //回款日期

                            balance = productDetail.getBalance();

                            increasAmount = info.getIncreasAmount();

                            fullName = info.getFullName();
                            leastaAmount = info.getLeastaAmount(); //起投金额
                            tvSurplusAmount.setText(Html.fromHtml("本期剩余金额  :<font color='#333333'>" + (int) surplusAmount + "元</font>"));

                            if (balance == 0) {
                                tvBalance.setText("账户余额 0 元");
                                cbBalance.setVisibility(View.GONE);
                            } else {
                                tvBalance.setText("账户余额  " + StringCut.getDoubleKb(balance) + "元");
                                cbBalance.setVisibility(View.VISIBLE);
                            }


                            etMoney.setHint(setetMoneyHint());

                            isRoundOff = productDetail.isIsRoundOff();//是否显示尾单

                            if (isRoundOff) {
                                roundOffMoney = productDetail.getroundOffMoney();//尾单奖励  只有isRoundOff为true
                                //投 10,000.00元，拿扫尾奖励 10.00元
                                tvRate.setText(Html.fromHtml("投 <font color='#333333'>" + StringCut.getNumKbs(surplusAmount) + "</font>元，拿扫尾奖励 <font color='#EE4845'>" + roundOffMoney + "</font>元现金"));
                                tvSaowei.setVisibility(View.VISIBLE);
                                btPay.setText("确认支付" + StringCut.getNumKbs(surplusAmount) + "元");

                            } else {
                                tvRate.setText(Html.fromHtml("预计收益  <font color='#EE4845'>0.00</font>元,预计还款日" + StringCut.getDateToString(endTime)));
                                tvSaowei.setVisibility(View.GONE);
                                //tvRate.setText(Html.fromHtml("预计收益  <font color='#EE4845'>" + StringCut.getDoubleKb(shouyi) + "</font>元,预计还款日" + StringCut.getDateToString(endTime)));

                   /* tvWanShouyiNew.setText(Html.fromHtml("本期剩余金额 :<font color='#EE4845'>" + StringCut.getNumKbs(surplusAmount)
                            + "</font>元"));*/
                            }

                            deadline = info.getDeadline();//出借期限 多少天

                            //优惠券列表
                            //couponList = productDetail.getCouponList();//优惠券列表
                            JSONArray objrows = map.getJSONArray("couponList");
                            couponList = JSON.parseArray(objrows.toJSONString(), CouponsBean.class);

                            if (couponList.size() <= 0) {
                                llCoupon.setFocusable(false);
                                llCoupon.setEnabled(false);
                                llCoupon.setClickable(false);
                                tvCoupon.setText("无可用优惠券");
                            } else {
                                tvCoupon.setText(Html.fromHtml("您有 <font color='#EE4845'>" + couponList.size() + "</font> 张优惠券可选"));
                                //tvCoupon.setText("您有" + couponList.size() + "张优惠券可选");
                            }


                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(InvestActivity.this).show_Is_Login();

                        } else {
                            ToastMaker.showShortToast("服务器异常");
                        }
                    }


                    @Override
                    public void onError(Call call, Exception e) {

                        dismissDialog();
                        ToastMaker.showShortToast("网络错误，请检查");
                    }
                });


        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("pid", pid + "");
        map.put("uid", preferences.getString("uid", ""));
        //map.put("uid", "46");
        map.put("version", UrlConfig.version);
        map.put("channel", "2");

        OkHttpEngine.create().setHeaders().post(UrlConfig.PRODUCT_DETAIL, params, new SimpleHttpCallback() {
            @Override
            public void onLogicSuccess(String data) {
                LogUtils.e(data);
                //dismissDialog();



                /*principleH5 = info.getPrincipleH5();


                fullName = info.getFullName();


                amount = info.getAmount();//出借总金额
                increasAmount = info.getIncreasAmount();



                leastaAmount = info.getLeastaAmount();//起投金额
                maxAmount = info.getMaxAmount();//最多出借金额
                establishTime = info.getEstablish();//成立日期

                endDate = info.getEndDate();  //募集结束日期
                pert = info.getPert();


                distributionId = info.getDistributionId(); //是展示排行榜还是出借记录

                investNums = productDetail.getInvestNums();


                //LogUtils.e("distributionId" + distributionId);

                principleH5 = info.getPrincipleH5(); //产品原理图
                String borrower = info.getBorrower(); //债务人概况
                String accept = info.getAccept(); //承兑方概况
                String introduce = info.getIntroduce();//产品说明
                String repaySource = info.getRepaySource();//还款来源

                bundle.putString("borrower", TextUtils.isEmpty(borrower) ? " " : borrower);
                bundle.putString("introduce", TextUtils.isEmpty(introduce) ? " " : introduce);
                bundle.putString("accept", TextUtils.isEmpty(accept) ? " " : accept);
                bundle.putString("repaySource", TextUtils.isEmpty(repaySource) ? " " : repaySource);
                bundle.putString("principleH5", TextUtils.isEmpty(borrower) ? " " : principleH5);



                SpannableStringBuilder sp = new SpannableStringBuilder(rate + "+" + activityRate + "%");
                //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
                sp.setSpan(new AbsoluteSizeSpan(42, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
                tvRate.setText(sp);

                tvTime.setText(deadline + "天");
                tvLeastaAmount.setText(leastaAmount + "元");
                tvAmount.setText(StringCut.getNumKbs(amount));
                pbProgress.setProgress((int) pert);

                tvPert.setText(Html.fromHtml("已募集 :<font color='#EE4845'>" + (int) pert + "%</font>"));
                tvSurplusAmount.setText(Html.fromHtml("剩余金额 :<font color='#EE4845'>" + StringCut.getNumKbs(surplusAmount) + "</font>"));

                tvExpireDate.setText(StringCut.getDateToString(endTime));
                tvInvestNums.setText(Html.fromHtml("出借记录: <font color='#EE4845'>" + investNums
                        + "</font> 人"));


                if ( productDetail.getDistribution_persion_count()!=0){
                    tvDistributionMoney.setVisibility(View.VISIBLE);
                    tvDistributionMoney.setText("前"+productDetail.getDistribution_persion_count()
                            +"名瓜分"+productDetail.getTender_money_distribution_sum()+"元现金");

                }else {
                    tvDistributionMoney.setVisibility(View.GONE);
                }*/

            }

            @Override
            public void onLogicError(int code, String msg) {
                LogUtils.e(msg);
                //dismissDialog();
            }

            @Override
            public void onError(IOException e) {
                LogUtils.e(e.toString());
                //dismissDialog();
            }
        });


    }

    /**
     * 获取用户银行卡
     */
    private void bankInfo() {
        OkHttpUtils.post().url(UrlConfig.MEMBERSETTING)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("--->memberSetting/获取用户银行卡信息：" + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            bankid = map.getString("bankId");
                            bankName = map.getString("bankName");
                            bankNum = map.getString("bankNum");
                            singleQuotaJYT = map.getInteger("singleQuotaJYT");
                            dayQuotaJYT = map.getInteger("dayQuotaJYT");
                            //银行卡信息
                            if (!TextUtils.isEmpty(bankid)) {
                                if (bp == null) {
                                    bp = new BankName_Pic();
                                }
                                Integer pic = bp.bank_Pic(bankid);
                                ivBank.setImageResource(pic);
                            }
                            if (!TextUtils.isEmpty(bankNum)) {
                                tvBankName.setText(bankName + "(" + bankNum + ")");
                            }
                            if (singleQuotaJYT != null) {
                                tvBankLimit.setText(StringCut.getNumKbs(singleQuotaJYT) + "元");
                            }
                            if (dayQuotaJYT != null) {
                                tvBankLimit.setText(StringCut.getNumKbs(dayQuotaJYT) + "元");
                            }

                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
                            new show_Dialog_IsLogin(InvestActivity.this)
                                    .show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("获取用户银行卡信息失败");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == android.app.Activity.RESULT_OK) {

            switch (requestCode) {
                case loginCode:
                    int position = data.getIntExtra("position", 0);
                    if (position != 0) {
                        if (couponList.size() > 0) {
                            String optimal;
                            CouponsBean couponsBean = couponList.get(position);
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
                            fid = couponsBean.getId() + ""; //优惠券id
                        }
                    }
                    break;

            }
        }

    }


    /**
     * 支付密码
     */
    private boolean isfirstPwd = false;//是否是第一次设置密码
    private TradePwdPopUtils pop;
    private String firstPwd = "";
    private String secondPwd;
    private boolean Invest_Begin_bl = false;

    private void memberSetting() {
        OkHttpUtils.post().url(UrlConfig.MEMBERSETTING)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("--->memberSetting/获取用户银行卡信息：" + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            mobilephone = map.getString("mobilephone");
                            if (map.getInteger("tpwdFlag") == 0) {//未设置交易密码

                                pop = new TradePwdPopUtils();
                                pop.setCallBackTradePwd(new TradePwdPopUtils.CallBackTradePwd() {

                                    @Override
                                    public void callBaceTradePwd(String pwd) {
                                        pop.tv_tips.setText("");
                                        if (!isfirstPwd) {
                                            firstPwd = pwd;
                                            isfirstPwd = true;
                                            pop.tv_pwd1.setText("✔");
                                            pop.tv_pwd_line.setBackgroundColor(Color.parseColor("#fb5b6d"));
                                            pop.tv_pwd2.setBackgroundResource(R.drawable.bg_corner_red_yuan);
                                        } else {
                                            secondPwd = pwd;
                                            if (firstPwd.equalsIgnoreCase(secondPwd)) {
                                                pop.tv_tips.setText("");
                                                //去设置支付密码
                                                sendFirstTpwdCode();
                                            } else {
                                                pop.tv_tips.setText("您输入的确认密码和之前不一致");
                                            }
                                        }
                                    }
                                });
                                pop.showPopWindow(InvestActivity.this, InvestActivity.this,
                                        rlBottom);
                                pop.ll_invest_money.setVisibility(View.GONE);
                                pop.ll_pwd_title.setVisibility(View.VISIBLE);
                                pop.ll_pwd.setVisibility(View.VISIBLE);
                                pop.popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                    @Override
                                    public void onDismiss() {
                                        Invest_Begin_bl = false;
                                    }
                                });
                                pop.iv_key_close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pop.popWindow.dismiss();
                                    }
                                });
                                //忘记密码
                                pop.forget_pwd.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        pop.popWindow.dismiss();

                                        //startActivityForResult(new Intent(InvestActivity.this, TransactionPswAct.class), 1);
                                        startActivityForResult(new Intent(InvestActivity.this, ForgetPswActivity.class)
                                                .putExtra("isFrom", 1), 1);
                                    }
                                });
                                pop.popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                    @Override
                                    public void onDismiss() {
                                        pop.tv_tips.setText("");
                                        isfirstPwd = false;
                                    }
                                });
                            } else {
                                MobclickAgent.onEvent(InvestActivity.this, UrlConfig.point + 24 + "");
                                MobclickAgent.onEvent(InvestActivity.this, "100023");
                                pop = new TradePwdPopUtils();
                                //密码正确
                                pop.setCallBackTradePwd(new TradePwdPopUtils.CallBackTradePwd() {
                                    @Override
                                    public void callBaceTradePwd(String pwd) {
                                        firstPwd = pwd;
                                        //LogUtils.e("pwd"+pwd);
                                        MobclickAgent.onEvent(InvestActivity.this, UrlConfig.point + 24 + "");
                                        // product_Invest();
                                        ShowSms();//发送短信
                                    }
                                });
                                pop.showPopWindow(InvestActivity.this, InvestActivity.this, rlBottom);
                                pop.ll_invest_money.setVisibility(View.VISIBLE);
                                pop.tv_key_money.setText(etMoney.getText().toString());
                                pop.iv_key_close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pop.popWindow.dismiss();
                                    }
                                });
                                pop.popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                    @Override
                                    public void onDismiss() {
                                        Invest_Begin_bl = false;
                                    }
                                });
                                pop.forget_pwd.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        pop.popWindow.dismiss();
                                        //startActivityForResult(new Intent(InvestActivity.this, TransactionPswAct.class), 1);
                                        startActivityForResult(new Intent(InvestActivity.this, ForgetPswActivity.class)
                                                .putExtra("isFrom", 1), 1);
                                    }
                                });
                            }

                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(InvestActivity.this)
                                    .show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统错误");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    /**
     * 设置交易密码
     */
    private void sendFirstTpwdCode() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.UPDATETPWDBYSMS)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("tpwd", SecurityUtils.MD5AndSHA256(firstPwd))
//				.addParams("smsCode", code_et.getText().toString().trim())
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        setResult(5, new Intent());
                        LogUtils.i("--->设置交易密码：" + response);
                        dismissDialog();
                        pop.popWindow.dismiss();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("交易密码设置成功");
                            MobclickAgent.onEvent(InvestActivity.this, UrlConfig.point + 24 + "");
                            // product_Invest();
                            ShowSms(); //发送短信
                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("验证码错误");
                        } else if ("1002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("密码为空");
                        } else if ("1003".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("交易密码不合法");
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
                            new show_Dialog_IsLogin(InvestActivity.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统错误");
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
     * 发送短信验证码
     */
    private void sendInvestMsg() {
        LogUtils.i("--->出借充值金额：" + rechargeAmount);

        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.SENDINVESTMSG)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("pid", pid + "")
                .addParam("amount", rechargeAmount)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        dismissDialog();
                        LogUtils.i("--->出借发送FYMsg：" + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean("success")) {
                            sendSmsSuccess = true;
                            ToastMaker.showShortToast("验证码已发送");

                            time();
                            SharedPreferences.Editor edit = preferences.edit();
                            edit.putString("rechargeAmount", rechargeAmount);
                            edit.commit();
                            JSONObject map = obj.getJSONObject("map");
                            systemOrders = map.getString("systemOrders");
                        } else if ("3002".equals(obj.getString("errorCode"))) {
                            sendSmsSuccess = false;
                            ToastMaker.showShortToast(obj.getString("errorMsg"));
                            stopTimer();
                        } else if ("3003".equals(obj.getString("errorCode"))) {
                            sendSmsSuccess = false;
                            ToastMaker.showShortToast(obj.getString("errorMsg"));
                            stopTimer();
                        } else if ("1111".equals(obj.getString("errorCode"))) {
                            sendSmsSuccess = false;
                            stopTimer();
                            ToastMaker.showShortToast("手机号码被锁，请联系客服");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            sendSmsSuccess = false;
                            finish();
                            new show_Dialog_IsLogin(InvestActivity.this)
                                    .show_Is_Login();
                        } else {
                            sendSmsSuccess = false;
                            ToastMaker.showShortToast("系统异常");
                            stopTimer();
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
     * 最后走的支付方法   出借
     */
    //显示成立日期和到期日期
    private String tv_establish, tv_expireDate, luckcode, luckcodesize;
    private Integer investId;
    private String isNewPay = "0";//出借方式(0位余额出借,1为充值出借)
    private String systemOrders = "";//订单编号(支付发送验证码接口返回)
    private String rechargeAmount = "";//充值金额
    private String nothing = "";
    private String jumpURLActivity = "";

    /**
     * 显示发送短信的
     */
    private void ShowSms() {


        if (isNewPay.equals("1")) {

            //pop.popWindow.dismiss();

            String text;
            if (TextUtils.isEmpty(mobilephone)) {
                text = "为了确保你的资金安全已向你预留手机号发送支付验证码";
            } else {
                text = "为了确保你的资金安全已向" + mobilephone + " 发送支付验证码";
            }

//            DialogMaker.showInvestDialog(InvestActivity.this,
//                    "确认付款", text, "提交", callBack, "规则");
            showInvestDialog(InvestActivity.this,
                    "确认付款", text, "提交", callBack, "规则");
            sendInvestMsg();


        } else {

            product_Invest();
        }
    }

    /**
     * 显示两个个按钮对话框  支付 短信验证码
     */
    public void showInvestDialog(final Context context, String tile, String content, String sureStr, final DialogMaker.DialogCallBack callBack, final Object obj) {
        final Dialog dialog = new Dialog(context, R.style.DialogNoTitleStyleTranslucentBg);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_invest_sms, null);
        dialog.setCanceledOnTouchOutside(false);
//		dialog.setCancelable(isCanCancelabel);
        dialog.setContentView(contentView);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) contentView.findViewById(R.id.tv_content);
        TextView tv_sure = (TextView) contentView.findViewById(R.id.tv_sure);
        final EditText et_sms = (EditText) contentView.findViewById(R.id.et_sms);
        tv_getYzm = (TextView) contentView.findViewById(R.id.tv_send_sms);
        TextView tv_cacel = (TextView) contentView.findViewById(R.id.tv_cacel);

        tv_title.setText(tile);
        tv_content.setText(content);
        tv_sure.setText(sureStr);

        tv_getYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onButtonClicked(dialog, 1, obj);
                //dialog.dismiss();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = et_sms.getText().toString();
                callBack.onButtonClicked(dialog, 0, s);
                //dialog.dismiss();
            }
        });
        tv_cacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onCancelDialog(dialog, null);
                dialog.dismiss();
            }
        });
    }

    DialogMaker.DialogCallBack callBack = new DialogMaker.DialogCallBack() {
        @Override
        public void onButtonClicked(Dialog dialog, int position, Object tag) {
            LogUtils.e("position" + position + "tag" + tag.toString());

            if (position == 1) {
                sendInvestMsg();
            } else {

                if (!sendSmsSuccess) {
                    sendInvestMsg();
                    return;
                }
                String sms = tag.toString();
                SmsCode = sms;

                if (isNewPay.equals("1") && TextUtils.isEmpty(sms)) {
                    ToastMaker.showShortToast("请输入验证码");
                    return;
                }
                if (isNewPay.equals("1") && sms.length() < 6) {
                    ToastMaker.showShortToast("请输入正确的验证码");
                    return;
                }
                if (isNewPay.equals("1") && TextUtils.isEmpty(systemOrders)) {//带有银行卡充值出借的，订单号为空，需要重发获取验证码
                    ToastMaker.showShortToast("请重新获取短信验证码");
                    return;
                }
                if (isNewPay.equals("1") && !rechargeAmount.equals(preferences.getString("rechargeAmount", ""))) {
                    ToastMaker.showShortToast("出借金额前后不一致，请重新获取短信验证码");
                    return;
                }

                Invest_Begin_bl = true;

                product_Invest();
                dialog.dismiss();
            }

        }

        @Override
        public void onCancelDialog(Dialog dialog, Object tag) {

            // LogUtils.e("tag"+tag.toString());

        }
    };

    private void product_Invest() {
        showWaitDialog("加载中...", false, "");

        LogUtils.i("--->普通Invest pid:" + pid + ", uid:" + preferences.getString("uid", "") + ", tpwd:" + firstPwd +
                ", amount:" + (etMoney.getText().toString().trim()) +
                ", isNewPay:" + isNewPay + ", verifyCode:" + SmsCode
                + ", systemOrders:" + systemOrders + ", rechargeAmount:" +
                rechargeAmount + ", fid:" + fid + ", experienceId:" + experienceId);
        if (pop != null) {
            pop.popWindow.dismiss();
        }
        dismissDialog();

        startActivity(new Intent(InvestActivity.this,
                PayResultActivity.class)
                .putExtra("pid", pid + "")
                .putExtra("uid", preferences.getString("uid", ""))
                .putExtra("tpwd", firstPwd)
                .putExtra("amount", (etMoney.getText().toString().trim()))
                .putExtra("experienceId", experienceId)
                .putExtra("isNewPay", isNewPay)
                .putExtra("verifyCode", SmsCode)
                .putExtra("systemOrders", systemOrders)
                .putExtra("rechargeAmount", rechargeAmount)
                .putExtra("fid", fid)
                .putExtra("deadline", deadline)
                .putExtra("name", fullName)
                .putExtra("money", etMoney.getText().toString().trim())
        );

       /* OkHttpUtils
                .post()
                .url(UrlConfig.INVEST)
                .addParams("pid", pid + "")
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("tpwd", SecurityUtils.MD5AndSHA256(firstPwd))
                .addParams("amount", (etMoney.getText().toString().trim()))
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
                        LogUtils.i("--->普通product/invest：" + response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            Invest_Begin_bl = false;
                            if (pop != null) {
                                pop.popWindow.dismiss();
                            }
                            JSONObject map = obj.getJSONObject("map");
                            if (map != null) {
                                expireDate = map.getLong("expireDate");
                                interest = map.getDouble("interest");
                                investId = map.getInteger("investId");
                                luckcode = map.getString("luckCodes");
                                luckcodesize = map.getString("luckCodeCount");
                                JSONArray textList = map.getJSONArray("textList");
                                textStringList = JSON.parseArray(textList.toJSONString(), String.class);
                                long investTime = map.getLong("investTime");
                                jumpURLActivity = map.getString("activityUrl");


                                //pop.popWindow.dismiss();
                                startActivity(new Intent(InvestActivity.this,
                                        PayResultActivity.class)
                                        .putExtra("money", etMoney.getText().toString().trim())
                                        .putExtra("name", fullName)
                                        .putExtra("interest", interest)
                                        .putExtra("deadline", deadline)
                                        .putExtra("investTime", investTime)

                                );


                                if (!map.getBoolean("isRepeats")) {


                                    // TODO: 2018/7/25  支付成功
                                    //showPaySuccessPopWindow();
                                    // is_Activity(map.getString("expireDate"));
                                } else {
                                       *//*Intent in = new Intent();
                                    in.putExtra("pid", pid);
                                    in.putExtra("tv_name", fullName);
                                    in.putExtra("tv_money", stringCut.douHao_Cut(etMoney.getText().toString().trim()));
                                    in.putExtra("tv_day", deadline);
                                    in.putExtra("tv_rate", rate);
                                    if ("amount".equalsIgnoreCase(nothing)) {
                                        in.putExtra("tv_earn", shouyi + "");
                                    } else {
                                        in.putExtra("tv_earn", shouyi + shouyi_add + "");
                                    }
                                    in.putExtra("tv_red", resultNum + "");
                                    in.putExtra("nothing", nothing);
                                    in.putExtra("investId", investId);
                                    in.putExtra("tv_start", tv_establish);
                                    in.putExtra("isPicture", map.getString("activityURL"));
                                    in.putExtra("jumpURLActivity", map.getString("activityUrl"));
                                    in.putExtra("specialRate", activityRate + "");
                                    in.putExtra("jumpURL", map.getString("jumpURL"));
                                    in.putExtra("tv_end", tv_expireDate);
                                    in.putExtra("luckcode", luckcode);
                                    in.putExtra("luckcodesize", luckcodesize);
                                    in.putExtra("endTime", map.getString("expireDate"));
                                    in.setClass(InvestActivity.this, Act_Pro_End.class);
                                    startActivityForResult(in, 4);*//*
                                    //ToastMaker.showShortToast("支付成功");
                                    // TODO: 2018/7/25  支付成功
                                    //showPaySuccessPopWindow();

                                }
                            } else {
                                pop.popWindow.dismiss();
                                startActivity(new Intent(InvestActivity.this,
                                        PayResultActivity.class));
                                // TODO: 2018/7/25  支付成功
                                //showPaySuccessPopWindow();
                                //is_Activity(map.getString("expireDate"));
                            }

                        } else if ("2001".equals(obj.getString("errorCode"))) {
                            MobclickAgent.onEvent(InvestActivity.this, UrlConfig.point + 20 + "");
                            pop.tv_tips.setText("连续输错三次密码，交易密码锁定一小时！请稍后再试或点击忘记密码");
                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            MobclickAgent.onEvent(InvestActivity.this, UrlConfig.point + 20 + "");
                            pop.tv_tips.setText("密码输入错误，请重新输入");
                        } else {
                            MobclickAgent.onEvent(InvestActivity.this, UrlConfig.point + 20 + "");
                            Invest_Begin_bl = false;
                            pop.popWindow.dismiss();
                            if ("XTWH".equals(obj.getString("errorCode"))) {
                                startActivity(new Intent(InvestActivity.this, WebViewActivity.class)
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
                                new show_Dialog_IsLogin(InvestActivity.this).show_Is_Login();
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
                });*/
    }


    /**
     * 监听输入金额
     */
    class Watcher implements TextWatcher {

        int onTextLength = 0;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onTextLength = s.length();
            if (onTextLength > 0 && rate != null) {
                etMoney.setHint("");
                LogUtils.e("start" + start + "before" + before);
                if (before > 0) {
                    shouyi_add = 0.0;
                    fid = "";
                    mbean = null;

                }

                String s1 = s.toString();

                double v = Double.parseDouble(s1);
                if (v <= 0) {
                    return;
                }

                //不能超过剩余的金额
                if (v > surplusAmount) {
                    ToastMaker.showShortToast("不能超过剩余的金额");
                }
                if (couponList.size() > 0) {
                    Double coupon = 0.0;

                    boolean isCoupons = false;

                    for (CouponsBean bean : couponList) {

                        if (bean.getEnableAmount() <= v) {
                            isCoupons = true;
                        }
                    }
                    if (!isCoupons) {
                        shouyi_add = 0.0;
                        tvCoupon.setText(Html.fromHtml("您有 <font color='#EE4845'>" + couponList.size() + "</font> 张优惠券可选"));
                        return;
                    }
                    for (CouponsBean bean : couponList) {

                        if (bean.getEnableAmount() <= v) {

                            switch (bean.getType()) {
                                case 1:// 返现
                                    coupon = bean.getAmount();
                                    break;
                                case 2:// 加息
                                    coupon = Double.parseDouble(StringCut.douHao_Cut(s1))
                                            * (bean.getRaisedRates() / 360 / 100)
                                            * deadline;
                                    break;
                                case 4:// 翻倍券
                                    coupon = Double.parseDouble(StringCut.douHao_Cut(s1))
                                            * ((rate * (bean.getMultiple() - 1)) / 360 / 100)
                                            * deadline;
                                    break;
                            }
                            if (shouyi_add <= coupon) {
                                shouyi_add = coupon;
                                fid = bean.getId() + "";
                                mbean = bean;
                            }
                        }
                    }

                    LogUtils.e("shouyi_add" + shouyi_add + "coupon" + coupon + mbean.getName());

                }


                Money_Get("");//收益

                //tv_jifen.setText(calculatePoints(Integer.valueOf(etMoney.getText().toString().trim())) + "");//积分
                btPay.setText("确认支付" + etMoney.getText().toString().trim() + "元");

            } else {
                setetMoneyHint();
                tvRate.setText(Html.fromHtml("预计收益  <font color='#EE4845'>0.00</font>元,预计还款日" + StringCut.getDateToString(endTime)));

                //  tv_prodetail_income.setText("0");
                //tv_jifen.setText("0");
                tv_prodetail_income_add.setText("");
                tv_prodetail_income_name.setText("");

                btPay.setText("确认支付0元");
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                String s1 = s.toString();
                btPay.setText("确认支付" + s1 + "元");


                if (mbean != null) {
                    //如果出借金额不满足红包最低，需要去掉“使用XX红包”字样
                    if (Double.parseDouble(s1) < mbean.getEnableAmount()) {
                   /* tv_prodetail_conpons.setVisibility(View.GONE);
                    ll_describe_conpons.setVisibility(View.VISIBLE);
                    tv_num_conpons.setText(couponList.size() + "");
                    Money_Get("");//收益
                    fid = "";*/
                    } else {
                        switch (mbean.getType()) {
                            case 1:// 返现
                                Spanned spanned = Html.fromHtml(mbean.getEnableAmount() + "返(" + mbean.getAmount() + ")<font color='#EE4845'>(已选最优方案)</font>");
                                tvCoupon.setText(spanned);

                                break;
                            case 2:// 加息
                                tvCoupon.setText(Html.fromHtml("加息<font color='#EE4845'>(" + mbean.getRaisedRates() + "%)(已选最优方案)</font>"));
                                break;
                            case 4:// 翻倍券
                                tvCoupon.setText(Html.fromHtml("翻倍<font color='#EE4845'>(" + mbean.getMultiple() + ")(已选最优方案)</font>"));
                                break;
                        }

                    }

                }

                if (Double.valueOf(balance) < Double.valueOf(s1)) {//账户余额不足

                    rechargeAmount = (Double.valueOf(s1) - Double.valueOf(balance)) + "";

                    tvBankMoney.setText(Html.fromHtml("支付 <font color='#EE4845'>(" + rechargeAmount + ") </font>元"));
                    //tv_amount_bank_pay.setText(rechargeAmount);
                    // rl_send_sms.setVisibility(View.VISIBLE);
                    isNewPay = "1";
                } else {//账户余额充足
                    rechargeAmount = "0";
                    // tv_amount_bank_pay.setText(rechargeAmount);
                    //rl_send_sms.setVisibility(View.GONE);
                    isNewPay = "0";
                }



              /*  if (Double.parseDouble(s.toString()) < enableAmount_tiYanJin) {
                    tv_prodetail_tiyanjin.setVisibility(View.GONE);
                    ll_describe_tiyanjin.setVisibility(View.VISIBLE);
                    tv_num_tiyanjin.setText(listFavourable.size() + "");
                    experienceId = "";
                } else {

                }*/
            } else {
                //check_tiyanjin.setClickable(true);
                //check_tiyanjin.setChecked(false);
            }
        }

    }

    /*
     * 计算收益
     * */
    private void Money_Get(String flag) {
        String etmongy = etMoney.getText().toString().trim();
        if (flag.equalsIgnoreCase("amountRed")) {
            shouyi = Double.parseDouble(StringCut.douHao_Cut(etmongy))
                    * ((rate + activityRate) / 360 / 100)
                    * deadline;
            shouyi_add = amountRed;
            tv_prodetail_income_add.setText("+");
            tv_prodetail_income_name.setText("元(返现红包)");
        } else if (flag.equalsIgnoreCase("raisedRate")) {
            shouyi = Double.parseDouble(StringCut.douHao_Cut(etmongy))
                    * ((rate + activityRate) / 360 / 100)
                    * deadline;
            shouyi_add = Double.parseDouble(StringCut.douHao_Cut(etmongy))
                    * (raisedRate / 360 / 100)
                    * deadline;
            tv_prodetail_income_add.setText("+");
            tv_prodetail_income_name.setText("元(加息收益)");
        } else if (flag.equalsIgnoreCase("multiple")) {
            shouyi = Double.parseDouble(StringCut.douHao_Cut(etmongy))
                    * ((rate + activityRate) / 360 / 100)
                    * deadline;
            shouyi_add = Double.parseDouble(StringCut.douHao_Cut(etmongy))
                    * ((rate * (multiple - 1)) / 360 / 100)
                    * deadline;
            tv_prodetail_income_add.setText("+");
            tv_prodetail_income_name.setText("元(翻倍收益)");
        } else {
            shouyi = Double.parseDouble(StringCut.douHao_Cut(etmongy))
                    * ((rate + activityRate) / 360 / 100)
                    * deadline;
            shouyi_add = 0.00;
            if (shouyi < 0.01) {
                //tv_prodetail_income.setText("0");
                tvRate.setText(Html.fromHtml("预计收益  <font color='#EE4845'>0.00</font>元,预计还款日" + StringCut.getDateToString(endTime)));

            } else {
                //tv_prodetail_income.setText(StringCut.getDoubleKb(shouyi));
                tvRate.setText(Html.fromHtml("预计收益  <font color='#EE4845'>" + StringCut.getDoubleKb(shouyi) + "</font>元,预计还款日" + StringCut.getDateToString(endTime)));

            }
            tv_prodetail_income_add.setText("");
            tv_prodetail_income_name.setText("");
            return;
        }

        if (shouyi < 0.01) {
            // tv_prodetail_income.setText("0");
            tvRate.setText(Html.fromHtml("预计收益  <font color='#EE4845'>0.00</font>元,预计还款日" + StringCut.getDateToString(endTime)));

        } else {
            //tv_prodetail_income.setText(StringCut.getDoubleKb(shouyi));
            tvRate.setText(Html.fromHtml("预计收益  <font color='#EE4845'>" + StringCut.getDoubleKb(shouyi) + "</font>元,预计还款日" + StringCut.getDateToString(endTime)));

        }
        if (shouyi_add < 0.01) {
            tv_prodetail_income_add.setText("+0");
        } else {
            tv_prodetail_income_add.setText("+" + StringCut.getDoubleKb(shouyi_add));
        }

    }

    /**
     * 判断选择的优惠券是不是最优化的
     */
    public boolean isoptimal(CouponsBean couponsBean) {
        String money = etMoney.getText().toString().trim();
        double v = Double.parseDouble(money);
        // CouponsBean mcouponsBean;
        int cid = 0; //优惠券id
        Double mshouyi = 0.0; //优惠券的收益

        Double coupon = 0.0;
     /*   if (v > surplusAmount) {
            ToastMaker.showShortToast("不能超过剩余的金额");
        }*/

        for (CouponsBean bean : couponList) {

            if (bean.getEnableAmount() <= v) {

                switch (bean.getType()) {
                    case 1:// 返现
                        coupon = bean.getAmount();
                        break;
                    case 2:// 加息
                        coupon = Double.parseDouble(StringCut.douHao_Cut(money))
                                * (bean.getRaisedRates() / 360 / 100)
                                * deadline;
                        break;
                    case 4:// 翻倍券
                        coupon = Double.parseDouble(StringCut.douHao_Cut(money))
                                * ((rate * (bean.getMultiple() - 1)) / 360 / 100)
                                * deadline;
                        break;
                }
                if (mshouyi <= coupon) {
                    mshouyi = coupon;
                    cid = bean.getId();
                    //mcouponsBean = bean;
                }
            }
        }

        if (couponsBean.getId() != cid) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 设置 etMoney setHint
     */
    private SpannableString setetMoneyHint() {
        SpannableString s = new SpannableString("输入出借金额，" + (int) increasAmount + "起投且整数倍增加");
        AbsoluteSizeSpan textSize = new AbsoluteSizeSpan(14, true);
        s.setSpan(new ForegroundColorSpan(0xFFcccccc), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        s.setSpan(textSize, 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    /*
   * 计算积分(积分=出借金额*周期/1000)
   * */
    private int calculatePoints(int money) {
        int point = 0;
        if (deadline == 0) {
            return point;
        }
        point = (money * Integer.valueOf(deadline)) / 1000;
        /*if (Integer.valueOf(deadline) < 90) {
        } else {
            point = (money * Integer.valueOf(deadline) * 3) / 2000;
        }*/
        return point;
    }


    private int count;
    private Boolean isRun;
    private int time = 1;

    // 计时器
    public void time() {
        count = 60;
        isRun = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    count--;
                    handler.sendEmptyMessage(10);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    public void stopTimer() {
        if (tv_getYzm == null) {
            return;
        }
        tv_getYzm.setEnabled(true);
        tv_getYzm.setText("发送");
        // tv_getYzm.setBackgroundResource(R.drawable.bg_corner_kong_blue);
        tv_getYzm.setTextColor(getResources().getColor(R.color.sms));
        isRun = false;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    if (count <= 0) {
                        time = 1;
                        stopTimer();
                    } else {
                        tv_getYzm.setEnabled(false);
                        tv_getYzm.setTextColor(getResources().getColor(R.color.sms_ss));
                        //tv_getYzm.setBackgroundResource(R.drawable.bg_corner_blackline);
                        tv_getYzm.setText("" + count + " 秒");
                        // LogUtils.e("" + count + " 秒");
                    }

                    break;
                default:
                    break;
            }
        }
    };
}
