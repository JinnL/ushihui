package com.mcz.xhj.yz.dr_app_fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.SpannableString;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.ConponsBean;

import java.util.ArrayList;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * <b>Project:</b> SlideDetailsLayout<br>
 * <b>Create Date:</b> 16/1/25<br>
 * <b>Author:</b> Gordon<br>
 * <b>Description:</b> <br>
 */
public class Frag_Detail_Pro_Top extends BaseFragment {
    @BindView(R.id.ll_conpons)
    LinearLayout ll_conpons;
//    @BindView(R.id.ptr_pro_detail)
    PtrClassicFrameLayout ptr_pro_detail;
//    @BindView(R.id.slidedetails_front)
//    @Nullable
    FrameLayout slidedetails_front;

    public Frag_Detail_Pro_Top() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_detail_pro_top;

    }

    @Override
    protected void initParams() {
//        slidedetails_front = (FrameLayout) getActivity().findViewById(R.id.slidedetails_front);
//        ptr_pro_detail = (PtrClassicFrameLayout)getActivity().findViewById(R.id.ptr_pro_detail);
        preferences = LocalApplication.getInstance().sharereferences;
        Intent intent = getActivity().getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            pid = uri.getQueryParameter("product");
            ptype = uri.getQueryParameter("ptype");
            atid = uri.getQueryParameter("atid");
            intent_fid = uri.getQueryParameter("fid");
            intent_amount = uri.getQueryParameter("amount");
            intent_enableAmount = uri.getQueryParameter("enableAmount");
        }else{
            pid = intent.getStringExtra("pid");
            ptype = intent.getStringExtra("ptype");
            atid = intent.getStringExtra("atid");
            intent_fid = intent.getStringExtra("fid");
            intent_amount = intent.getStringExtra("amount");
            intent_enableAmount = intent.getStringExtra("enableAmount");
        }
        if(atid!=null){
            ll_conpons.setFocusable(false) ;
            ll_conpons.setEnabled(false) ;
            ll_conpons.setVisibility(View.GONE);
        }
        ptr_pro_detail.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                if(header==slidedetails_front){
                    return true;
                }else {
                    return false;
                }
//				return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, slidedetails_front);
            }
        });
    }

    private String maxAmount; // 产品投资限额
    private String balance, leastaAmount; // 余额，起投
    private int int_last; // 100整数
    private String increasAmount; // 递增
    private String windMeasure; // 风控措施
    private String introduce;// 产品介绍
    private String repaySource;// 还款来源
    private String borrower;// 债务人概况
    private String surplusAmount; // 剩余金额
    private String deadline;
    private String endDate;
    private String rate_h, rate = null;
    private String fid = ""; // 可用红包id
    private SpannableString ss;
    private ArrayList<ConponsBean> mlslb2; // 红包列表
    String fullname  ; //产品名称
    private boolean isOldUser = false;//是否是老用户
    private double specialRate1 = 0;//双旦活动利率
    private double exRate = 0;//总共增加的利率
    private String pid, ptype, uid,atid;
    private SharedPreferences preferences;
    private String intent_fid;
    private String intent_amount;
    private String intent_enableAmount;

//    private void getDate() {
//        showWaitDialog("加载中...", true, "");
//        OkHttpUtils.post().url(UrlConfig.PRODUCT_DETAIL).addParams("pid", pid)
//                .addParams("uid", preferences.getString("uid", ""))
//                .addParams("version", UrlConfig.version)
//                .addParams("channel", "2").build()
//                .execute(new StringCallback() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        // TODO Auto-generated method stub
//                        JSONObject obj = JSON.parseObject(response);
//                        dismissDialog();
//                        ptr_pro_detail.refreshComplete();
//                        if (obj.getBoolean(("success"))) {
//                            JSONObject map = obj.getJSONObject("map");
//                            balance = map.getString("balance");
//                            isOldUser = map.getBoolean("isOldUser");
//                            specialRate1 = map.getDouble("specialRate");
//                            JSONObject info = map.getJSONObject("info");
//                            exRate = specialRate1 + info.getDouble("activityRate");
//                            if(exRate!=0){
//                                tv_rate_jiaxi.setVisibility(View.VISIBLE);
//                                if(specialRate1!=0&&isOldUser){
//                                    if(isFirstGetRate){
//                                        tv_rate_jiaxi.setText("+" + exRate + "%");
//                                        isFirstGetRate = false;
//                                    }
//                                }else{
//                                    if(info.getDouble("activityRate")!=0){
//                                        tv_rate_jiaxi.setText("+"+info.getDouble("activityRate")+"%");
//                                    }else {
//                                        tv_rate_jiaxi.setVisibility(View.GONE);
//                                    }
//                                    exRate = info.getDouble("activityRate");
//                                }
//                            }else {
//                                tv_rate_jiaxi.setVisibility(View.GONE);
//                            }
//                            tv_balance.setText(stringCut.getNumKbs(Double.parseDouble(balance)));
//
//                            is_fid(info.getString("fid"));
//                            // 票据类型 1=商票，2=银票
//                            status_type(info.getString("billType") , false) ;
//                            //成立日期,到期日期
//                            pro_Str_Or(info.getString("establish"), info.getString("expireDate")) ;
//
//                            if ("1".equals(info.getString("isInterest"))) {
//                                image_isInterest.setVisibility(View.VISIBLE);
//                            }
//                            if ("1".equals(info.getString("isCash"))) {
//                                image_isCash.setVisibility(View.VISIBLE);
//                            }
//                            if ("1".equals(info.getString("isDouble"))) {
//                                image_isDouble.setVisibility(View.VISIBLE);
//                            }
//
//                            fullname  = info.getString("fullName") ;
//                            tv_name_detail.setText(fullname);
//
//                            proAnimator(Integer.valueOf(stringCut.pertCut(info
//                                    .getString("pert")))
//                                    .intValue()) ;
//
//                            rate = info.getString("rate");
//                            deadline = info.getString("deadline");
//                            maxAmount = info.getString("maxAmount");
//                            increasAmount = info.getString("increasAmount");
//                            surplusAmount = info.getString("surplusAmount");
//                            leastaAmount = info.getString("leastaAmount");
//                            rate_h = stringCut.getNumKbs(Double.parseDouble(rate)) + "%";
//                            tv_piaofrom.setText(stringCut.getNumKbs(Double.parseDouble(surplusAmount)));
//                            tv_rate.setText(rate_h);
//                            tv_deadline.setText("期限" + deadline + "天 ");
//                            tv_leastaAmount.setText("起投" + stringCut.getNumKbs(Double.parseDouble(leastaAmount)) + "元");
//                            tv_maxAmount.setText("次日计息");
//                            if (et_invest_money.getText().length() <= 0) {
//                                et_invest_money.setHint("需为" + stringCut.getNumKbs(Double.parseDouble(increasAmount)) + "的倍数");
//                            }
//                            // 详情信息
//                            windMeasure = info.getString("windMeasure");
//                            introduce = info.getString("introduce");
//                            repaySource = info.getString("repaySource");
//                            borrower = info.getString("borrower");
//                            JSONArray list = map.getJSONArray("couponList");
//                            if (null == list) {
//                            } else {
//                                if (list.size() <= 0) {
//                                    hongbao_user.setFocusable(false);
//                                    hongbao_user.setEnabled(false);
//                                    greater.setVisibility(View.GONE);
//                                    tv_chose_conpons.setText("无可用优惠券");
//                                } else {
//                                    mlslb2 = (ArrayList<ConponsBean>) JSON.parseArray(list.toJSONString(),ConponsBean.class);
//
//                                    if(intent_fid!=null&&!intent_fid.equalsIgnoreCase("")){
//                                        if(isFirstGet){
//                                            isFirstGet = false;
//                                            resultNum = 1 ;
//                                            raisedRate = 0;
//                                            multiple = 1;
//                                            fid = intent_fid;
//                                            enableAmount = Double.parseDouble(intent_enableAmount);
//                                            amount = Double.parseDouble(intent_amount);
//                                            lv_fanxian.setVisibility(View.VISIBLE);
//                                            tv_shouyi_fanxian.setText(stringCut.getNumKbs(amount));
//                                            nothing = stringCut.getNumKbs(amount) ;
//                                            tv_chose_conpons.setText("已使用返现券");
//                                            tv_chose_conpons.setGravity(Gravity.CENTER);
//                                            if (et_invest_money.getText().toString().trim().length() > 0) {
//                                                Money_Get();
//                                                tv_shouyi.setText(stringCut.getNumKbs(shouyi));
//                                            }
//                                        }
//                                    }else{
//                                        for (int i = 0; i < mlslb2.size(); i++) {
//                                            if(mlslb2.get(i).getPid()!=null){
//                                                if(isFirstGet){
//                                                    if(mlslb2.get(i).getPid().equalsIgnoreCase(pid)){
//                                                        isFirstGet = false;
//                                                        resultNum = 2 ;
//                                                        raisedRate = 0;
//                                                        multiple = 1;
//                                                        tv_rate_jiaxi.setText("");
//                                                        tv_hongbao_fanxian.setText("");
//                                                        tv_chose_conpons.setText("已使用加息券");
//                                                        tv_chose_conpons.setGravity(Gravity.CENTER);
//                                                        fid = mlslb2.get(i).getId();
//                                                        enableAmount = mlslb2.get(i).getEnableAmount();
//                                                        raisedRate = mlslb2.get(i).getRaisedRates();
//                                                        double exRates = raisedRate + exRate;
//                                                        tv_rate_jiaxi.setVisibility(View.VISIBLE);
//                                                        tv_rate_jiaxi.setText("+" + exRates + "%");
//                                                        nothing = raisedRate+"" ;
//                                                        if (et_invest_money.getText().toString().trim().length() > 0) {
//                                                            Money_Get();
//                                                            tv_shouyi.setText(stringCut.getNumKbs(shouyi));
//                                                        }
//                                                    }
//                                                }
//                                                if (!mlslb2.get(i).getPid().equalsIgnoreCase(pid)&&mlslb2.get(i).getType()==2) {
//                                                    mlslb2.remove(i);
//                                                    i--;
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            if ("6".equals(info.getString("status"))) {
//                                no_checked();
//                                touzi_now.setText("募集结束 ");
//                                status_type(info.getString("billType") , true) ;
//                            } else if ("8".equals(info.getString("status"))) {
//                                no_checked();
//                                touzi_now.setText("已计息");
//                                status_type(info.getString("billType") , true) ;
//                            } else if ("9".equals(info.getString("status"))) {
//                                no_checked();
//                                touzi_now.setText("已回款");
//                                status_type(info.getString("billType") , true) ;
//                            } else {
//                                endDate = info.getString("endDate");
//                            }
//
//                            memberSetting();
//                        } else if ("9999".equals(obj.getString("errorCode"))) {
//                            ToastMaker.showShortToast("系统异常");
//                        } else if ("9998".equals(obj.getString("errorCode"))) {
//                            new show_Dialog_IsLogin(Detail_Piaoju_Act.this)
//                                    .show_Is_Login();
//                        } else {
//                            ToastMaker.showShortToast("系统异常");
//                        }
//                    }
//                    private void proAnimator(int pert) {
//                        ValueAnimator animator = ValueAnimator.ofInt(0, pert);
//                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animation) {
//                                int progress = (int) animation.getAnimatedValue();
//                                progressbar_pert.setSecondaryProgress(progress);
//                            }
//                        });
//                        animator.setDuration(2000);
//                        animator.start();
//                    }
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        // TODO Auto-generated method stub
//                        ptr_pro_detail.refreshComplete();
//                        dismissDialog();
//                        ToastMaker.showShortToast("网络错误，请检查");
//                    }
//                });
//    }
}
