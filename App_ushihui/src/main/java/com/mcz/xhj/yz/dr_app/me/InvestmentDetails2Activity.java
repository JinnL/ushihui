package com.mcz.xhj.yz.dr_app.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_app.BackRecondAct;
import com.mcz.xhj.yz.dr_app.BaseActivity;
import com.mcz.xhj.yz.dr_app.Detail_Tiyan;
import com.mcz.xhj.yz.dr_app.NewProductDetailActivity;
import com.mcz.xhj.yz.dr_app.NewhandDetailActivity;
import com.mcz.xhj.yz.dr_app.WebViewActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by DELL on 2017/11/20.
 * 描述：投资详情页面2.0
 */

public class InvestmentDetails2Activity extends BaseActivity {
    @BindView(R.id.title_lefttextview)
    TextView titleLefttextview;
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_righttextview)
    TextView titleRighttextview;
    @BindView(R.id.title_rightimageview)
    ImageView titleRightimageview;
    /*@BindView(R.id.view_line_bottom)
    View viewLineBottom;*/
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_tubiao)
    ImageView ivTubiao;
    @BindView(R.id.tv_tubiao)
    TextView tvTubiao;
    @BindView(R.id.ll_tubiao)
    LinearLayout llTubiao;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.tv_line)
    View tvLine;
    @BindView(R.id.tv_lift_name)
    TextView tvLiftName;
    @BindView(R.id.rl_lipin)
    RelativeLayout rlLipin;
    @BindView(R.id.tv_nianhuashouyi)
    TextView tvNianhuashouyi;
    @BindView(R.id.tv_touziqixian)
    TextView tvTouziqixian;
    @BindView(R.id.tv_huankuanfangshi)
    TextView tvHuankuanfangshi;
    @BindView(R.id.tv_touzijine)
    TextView tvTouzijine;
    @BindView(R.id.tv_yingshoubenjin)
    TextView tvYingshoubenjin;
    @BindView(R.id.tv_yingshoulixi)
    TextView tvYingshoulixi;
    @BindView(R.id.tv_touzidate)
    TextView tvTouzidate;
    @BindView(R.id.tv_jixidate)
    TextView tvJixidate;
    @BindView(R.id.tv_huikuandate)
    TextView tvHuikuandate;
    @BindView(R.id.tv_juanname)
    TextView tvJuanname;
    @BindView(R.id.tv_youhuiquan)
    TextView tvYouhuiquan;
    @BindView(R.id.rl_youhuijuan)
    RelativeLayout rlYouhuijuan;
    @BindView(R.id.ll_xinxi)
    LinearLayout llXinxi;
    @BindView(R.id.rl_chanpindetails)
    RelativeLayout rlChanpindetails;
    @BindView(R.id.tv_huikuanjilu)
    TextView tvHuikuanjilu;

    private Integer investId;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private Integer uid;
    private Integer pid;
    private int type;//产品类型 1-新手标 2-普通标 5-体验标

    @Override
    protected int getLayoutId() {
        return R.layout.me_invest_details;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("投资详情");
        titleRighttextview.setText("协议");
        titleRighttextview.setVisibility(View.VISIBLE);
        investId = getIntent().getIntExtra("investId", -1);
        type = getIntent().getIntExtra("type", -1);
        getDetailData();
    }

    private void getDetailData() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.INVESTDETAIL)
                .addParams("tid", investId+"")
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        dismissDialog();
                        LogUtils.i("--->投资详情："+response);
                        JSONObject obj = JSON.parseObject(response);
                        if(obj.getBoolean("success")){
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
                            Double couponAmount = invest.getDouble("couponAmount");
                            Double couponRate = invest.getDouble("couponRate");
                            Double multiple = invest.getDouble("multiple");


                            tvName.setText(fullName);
                            if(rate!=null){
                                tvNianhuashouyi.setText(rate + "%");
                            }
                            tvTouziqixian.setText(deadline + "天");
                            if (repayType == 1) {
                                tvHuankuanfangshi.setText("到期还本付息");
                            } else if (repayType == 2) {
                                tvHuankuanfangshi.setText("按月付息，到期还本");
                            }
                            tvTouzijine.setText(stringCut.getNumKb(amount));
                            tvYingshoubenjin.setText(stringCut.getNumKb(amount));
                            tvYingshoulixi.setText(stringCut.getNumKb(interest));
                            tvTouzidate.setText(stringCut.getDateYearToString(Long.parseLong(investTime)));
                            tvJixidate.setText(stringCut.getDateYearToString(Long.parseLong(interestTime)));
                            if(expireDate!=null&&!"".equalsIgnoreCase(expireDate)){
                                tvHuikuandate.setText(stringCut.getDateYearToString(Long.parseLong(expireDate)));
                            }
                            if (couponType!= null) {
                                rlYouhuijuan.setVisibility(View.VISIBLE);
                                switch (couponType) {
                                    case 1://返现
                                        tvJuanname.setText("现金券");
                                        tvYouhuiquan.setText(stringCut.getNumKb(couponAmount) + "元");
                                        break;
                                    case 2://加息
                                        tvJuanname.setText("加息券");
                                        tvYouhuiquan.setText(stringCut.getNumKbs(couponRate) + "%");
                                        break;
                                    case 3://体验金金额
                                        rlYouhuijuan.setVisibility(View.GONE);
                                        break;
                                    case 4: //翻倍券
                                        tvJuanname.setText("翻倍券");
                                        tvYouhuiquan.setText(multiple + "倍");
                                        break;
                                }
                            }else{
                                rlYouhuijuan.setVisibility(View.GONE);
                            }

                            //0=投资中 1=待还款 2=投资失败 3=已还款
                            if ("3".equalsIgnoreCase(status)) {
                                titleRighttextview.setVisibility(View.VISIBLE);
                                tvStatus.setText("已还款");
                            } else if ("1".equalsIgnoreCase(status)) {
                                titleRighttextview.setVisibility(View.VISIBLE);
                                tvStatus.setText("待还款");
                            }  else if ("0".equalsIgnoreCase(status)) {
                                titleRighttextview.setVisibility(View.GONE);
                                tvStatus.setText("投资中");
                            }else if("2".equalsIgnoreCase(status)){
                                titleRighttextview.setVisibility(View.GONE);
                                tvStatus.setText("投资失败");
                            }

                            if(type == 5){
                                titleRighttextview.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    @OnClick({R.id.title_leftimageview, R.id.title_righttextview, R.id.rl_chanpindetails, R.id.tv_huikuanjilu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.title_righttextview: //协议
                startActivity(new Intent(InvestmentDetails2Activity.this,WebViewActivity.class).putExtra("URL", UrlConfig.JIEKUAN+"?pid="+pid+"&uid="+uid +"&investId="+investId).putExtra("TITLE", "投资协议"));
                break;

            case R.id.rl_chanpindetails:
                if(type == 1){
                    startActivityForResult(new Intent(InvestmentDetails2Activity.this,NewhandDetailActivity.class).putExtra("pid",pid.toString()),4);
                }else if(type == 2){
                    startActivityForResult(new Intent(InvestmentDetails2Activity.this,NewProductDetailActivity.class).putExtra("pid",pid.toString()),4);
                }else if(type == 5){
                    startActivity(new Intent(InvestmentDetails2Activity.this, Detail_Tiyan.class));
                }
                break;
            case R.id.tv_huikuanjilu:
                startActivity(new Intent(InvestmentDetails2Activity.this, BackRecondAct.class)
                        .putExtra("pid", pid.toString())
                        .putExtra("id", investId.toString()));
                break;
        }
    }
}
