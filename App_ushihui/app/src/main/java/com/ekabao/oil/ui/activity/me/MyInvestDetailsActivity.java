package com.ekabao.oil.ui.activity.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.activity.ProductActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * $desc$
 *
 * @time $data$ $time$
 * Created by Administrator on 2018/7/17.
 */

public class MyInvestDetailsActivity extends BaseActivity {

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
    @BindView(R.id.tv_nianhuashouyi)
    TextView tvNianhuashouyi;
    @BindView(R.id.tv_touziqixian)
    TextView tvTouziqixian;
    @BindView(R.id.tv_touzijine)
    TextView tvTouzijine;
    @BindView(R.id.tv_touzidate)
    TextView tvTouzidate;
    @BindView(R.id.tv_jixidate)
    TextView tvJixidate;
    @BindView(R.id.tv_huikuandate)
    TextView tvHuikuandate;
    @BindView(R.id.tv_yingshoubenjin)
    TextView tvYingshoubenjin;
    @BindView(R.id.tv_yingshoulixi)
    TextView tvYingshoulixi;
    @BindView(R.id.tv_huankuanfangshi)
    TextView tvHuankuanfangshi;
    @BindView(R.id.tv_loan_contract)
    TextView tvLoanContract;
    @BindView(R.id.tv_info_details)
    TextView tvInfoDetails;
    @BindView(R.id.tv_juanname)
    TextView tvJuanname;
    @BindView(R.id.tv_youhuiquan)
    TextView tvYouhuiquan;
    @BindView(R.id.rl_youhuijuan)
    RelativeLayout rlYouhuijuan;
    @BindView(R.id.tv_huikuanjilu)
    TextView tvHuikuanjilu;

    private Integer investId;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private Integer uid;
    private Integer pid;
    private int type;//产品类型 1-新手标 2-普通标 5-体验标

    /**
     * 我的出借-->出借详情
     *
     * @time 2018/7/17 14:30
     * Created by
     */
    @Override
    protected int getLayoutId() {

        return R.layout.activity_me_investe_details;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("出借详情");
      /*  titleRighttextview.setText("协议");
        titleRighttextview.setVisibility(View.VISIBLE);*/

        investId = getIntent().getIntExtra("investId", -1);
        type = getIntent().getIntExtra("type", -1);
        getDetailData();
    }

    @OnClick({R.id.title_leftimageview, R.id.tv_loan_contract,
            R.id.tv_info_details, R.id.tv_huikuanjilu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.tv_loan_contract: //协议
                LogUtils.e("type" + type + "uid" + uid + "pid" + pid);
                startActivity(new Intent(MyInvestDetailsActivity.this,
                        WebViewActivity.class)
                        .putExtra("URL", UrlConfig.JIEKUAN + "?pid=" + pid + "&uid=" + uid + "&investId=" + investId)
                        .putExtra("TITLE", "出借协议"));
                break;

            case R.id.tv_info_details:
                LogUtils.e("type" + type + "uid" + uid + "pid" + pid.toString());
                if (type == 1) {
                    startActivityForResult(new Intent(MyInvestDetailsActivity.this,
                                    ProductActivity.class)
                                    .putExtra("pid", pid),
                            4);
                } else if (type == 2) {
                    startActivityForResult(new Intent(MyInvestDetailsActivity.this,
                                    ProductActivity.class)
                                    .putExtra("pid", pid),
                            4);
                } else if (type == 5) {
                    startActivityForResult(new Intent(MyInvestDetailsActivity.this,
                                    ProductActivity.class)
                                    .putExtra("pid", pid)
                                    .putExtra("type", type),

                            4);
                    //startActivity(new Intent(MyInvestDetailsActivity.this, Detail_Tiyan.class));
                }
                break;
            case R.id.tv_huikuanjilu:
                /*startActivity(new Intent(MyInvestDetailsActivity.this, BackRecondAct.class)
                        .putExtra("pid", pid.toString())
                        .putExtra("id", investId.toString()));*/
                break;
        }
    }

    private void getDetailData() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.INVESTDETAIL)
                .addParams("tid", investId + "")
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        dismissDialog();
                        LogUtils.i("--->出借详情：" + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean("success")) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONObject invest = map.getJSONObject("invest");
                            String fullName = invest.getString("fullName");
                            Double amount = invest.getDouble("amount");
                            Double factAmount = invest.getDouble("factAmount");
                            Double factInterest = invest.getDouble("factInterest");
                            Double rate = invest.getDouble("rate");
                            Double interest = invest.getDouble("interest");
                            String deadline = invest.getString("deadline");
                            String expireDate = invest.getString("expireDate");
                            String investTime = invest.getString("investTime");
                            String interestTime = invest.getString("interestTime");
                            String status = invest.getString("status");
                            type = invest.getInteger("type");//产品类型 1-新手标 2-普通标 5-体验标
                            Integer repayType = invest.getInteger("repayType");
                            Integer couponType = invest.getInteger("couponType");
                            uid = invest.getInteger("uid");
                            pid = invest.getInteger("pid");
                            investId = invest.getInteger("id");
                            LogUtils.e("type" + type + "uid" + uid + "pid" + pid);
                            Double couponAmount = invest.getDouble("couponAmount");
                            Double couponRate = invest.getDouble("couponRate");
                            Double multiple = invest.getDouble("multiple");


                            // tvName.setText(fullName);
                            if (rate != null) {
                                tvNianhuashouyi.setText(rate + "%");
                            }
                            tvTouziqixian.setText(deadline + "天");
                            if (repayType == 1) {
                                tvHuankuanfangshi.setText("到期还本付息");
                            } else if (repayType == 2) {
                                tvHuankuanfangshi.setText("按月付息，到期还本");
                            }
                            tvTouzijine.setText(StringCut.getNumKb(amount));
                            tvYingshoubenjin.setText(StringCut.getNumKb(amount)); //本金
                            tvYingshoulixi.setText(StringCut.getNumKb(interest)); //利息
                            tvTouzidate.setText(StringCut.getDateYearToString(Long.parseLong(investTime)));
                            tvJixidate.setText(StringCut.getDateYearToString(Long.parseLong(interestTime)));
                            if (expireDate != null && !"".equalsIgnoreCase(expireDate)) {
                                tvHuikuandate.setText(StringCut.getDateYearToString(Long.parseLong(expireDate)));
                            }
                            if (couponType != null) {
                                rlYouhuijuan.setVisibility(View.VISIBLE);
                                switch (couponType) {
                                    case 1://返现
                                        tvJuanname.setText("现金券");
                                        tvYouhuiquan.setText(StringCut.getNumKb(couponAmount) + "元");
                                        break;
                                    case 2://加息
                                        tvJuanname.setText("加息券");
                                        tvYouhuiquan.setText(StringCut.getNumKbs(couponRate) + "%");
                                        break;
                                    case 3://体验金金额
                                        rlYouhuijuan.setVisibility(View.GONE);
                                        break;
                                    case 4: //翻倍券
                                        tvJuanname.setText("翻倍券");
                                        tvYouhuiquan.setText(multiple + "倍");
                                        break;
                                }
                            } else {
                                rlYouhuijuan.setVisibility(View.GONE);
                            }

                           /* //0=出借中 1=待还款 2=出借失败 3=已还款
                            if ("3".equalsIgnoreCase(status)) {
                                titleRighttextview.setVisibility(View.VISIBLE);
                                //tvStatus.setText("已还款");
                            } else if ("1".equalsIgnoreCase(status)) {
                                titleRighttextview.setVisibility(View.VISIBLE);
                                //tvStatus.setText("待还款");
                            } else if ("0".equalsIgnoreCase(status)) {
                                titleRighttextview.setVisibility(View.GONE);
                                //tvStatus.setText("出借中");
                            } else if ("2".equalsIgnoreCase(status)) {
                                titleRighttextview.setVisibility(View.GONE);
                                //tvStatus.setText("出借失败");
                            }

                            if (type == 5) {
                                titleRighttextview.setVisibility(View.GONE);
                            }*/

                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
