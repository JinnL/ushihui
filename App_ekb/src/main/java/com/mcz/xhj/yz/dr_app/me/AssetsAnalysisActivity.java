package com.mcz.xhj.yz.dr_app.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_app.BaseActivity;
import com.mcz.xhj.yz.dr_app.CashInAct;
import com.mcz.xhj.yz.dr_app.CashOutAct;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.FundsBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zhulang on 2017/9/11.
 * 2.0版 资产分析
 */

public class AssetsAnalysisActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_righttextview)
    TextView title_righttextview;
    @BindView(R.id.tv_balance_acount)
    TextView tvBalanceAcount;
    @BindView(R.id.tv_cash_out)
    TextView tvCashOut;
    @BindView(R.id.tv_cash_in)
    TextView tvCashIn;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.chart_view_total)
    PieChart chartViewTotal;
    @BindView(R.id.chart_view_shouyi)
    PieChart chartViewShouyi;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_invest_amount)
    TextView tvInvestAmount;
    @BindView(R.id.tv_huikuan)
    TextView tvHuikuan;
    @BindView(R.id.tv_freeze_amount)
    TextView tvFreezeAmount;
    @BindView(R.id.tv_total_shouyi)
    TextView tvTotalShouyi;
    @BindView(R.id.tv_tender_money)
    TextView tvTenderMoney;
    @BindView(R.id.tv_red_money)
    TextView tvRedMoney;
    @BindView(R.id.tv_activity_money)
    TextView tv_activity_money;


    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private FundsBean fundsBean;
    private BigDecimal activityMoney;//活动收益
    private BigDecimal redpacket;//红包收益
    private BigDecimal tenderMoney;//标收益
    Long lastClick = 0l;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_assets_analysis;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("资产分析");

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();

    }

    private void getData() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.ACCOUNTINDEX)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                LogUtils.i("资产分析：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    JSONObject funds = map.getJSONObject("funds");
                    fundsBean = JSON.parseObject(funds.toJSONString(), FundsBean.class);
                    activityMoney = map.getBigDecimal("activityMoney");
                    redpacket = map.getBigDecimal("redpacket");
                    tenderMoney = map.getBigDecimal("tenderMoney");

                    tvBalanceAcount.setText(fundsBean.getBalance().floatValue() + "");
                    tvTotal.setText((fundsBean.getBalance().floatValue()+fundsBean.getWprincipal().floatValue() +fundsBean.getWinterest().floatValue()+fundsBean.getFreeze().floatValue()+""));
                    tvBalance.setText(fundsBean.getBalance().floatValue()+"");
                    //tvInvestAmount.setText(fundsBean.getInvestAmount().floatValue()+"");
                    tvHuikuan.setText(fundsBean.getWprincipal().floatValue()+fundsBean.getWinterest().floatValue()+"");
                    tvFreezeAmount.setText(fundsBean.getFreeze().floatValue()+"");
                    tvTotalShouyi.setText(tenderMoney.floatValue()+redpacket.floatValue()+activityMoney.floatValue()+"");
                    tvTenderMoney.setText(tenderMoney.floatValue()+"");
                    tvRedMoney.setText(redpacket.floatValue()+"");
                    tv_activity_money.setText(activityMoney.floatValue()+"");

                    initFunds(fundsBean);
                    initShouyi();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

    private void initFunds(FundsBean fundsBean) {
        //假数据
        PieEntry entry = new PieEntry(fundsBean.getBalance().floatValue(), "余额");
        //PieEntry entry2 = new PieEntry(fundsBean.getInvestAmount().floatValue(), "智盈宝");
        PieEntry entry3 = new PieEntry((fundsBean.getWprincipal().floatValue()+fundsBean.getWinterest().floatValue()), "回款中");
        PieEntry entry4 = new PieEntry(fundsBean.getFreeze().floatValue(), "冻结中");

        chartViewTotal.animateXY(1000, 1000);//动画效果
        chartViewTotal.setDrawSliceText(false);
        chartViewTotal.setDrawSlicesUnderHole(false);
        Legend legend = chartViewTotal.getLegend();
        legend.setEnabled(false);//不显示图例描述
        PieData mPieData = new PieData();
        List<PieEntry> mEntry = new ArrayList<>();
        mEntry.add(entry);
        //mEntry.add(entry2);
        mEntry.add(entry3);
        mEntry.add(entry4);
        PieDataSet mDataSet = new PieDataSet(mEntry, "");
        mDataSet.setColors(Color.rgb(82, 165, 254), Color.rgb(255, 131, 70), Color.rgb(216, 216, 216));
        mDataSet.setDrawValues(false);//不显示百分比
        mPieData.setDataSet(mDataSet);
        chartViewTotal.setDescription(null);
        chartViewTotal.setData(mPieData);
    }

    private void initShouyi() {
        //假数据
        PieEntry entry = new PieEntry(tenderMoney.floatValue(), "标的收益");
        PieEntry entry2 = new PieEntry(redpacket.floatValue(), "返现红包");
        PieEntry entry3 = new PieEntry(activityMoney.floatValue(), "活动返现");

        chartViewShouyi.animateXY(1000, 1000);//动画效果
        chartViewShouyi.setDrawSliceText(false);
        chartViewShouyi.setDrawSlicesUnderHole(false);
        Legend legend = chartViewShouyi.getLegend();
        legend.setEnabled(false);//不显示图例描述
        PieData mPieData = new PieData();
        List<PieEntry> mEntry = new ArrayList<>();
        mEntry.add(entry);
        mEntry.add(entry2);
        mEntry.add(entry3);
        PieDataSet mDataSet = new PieDataSet(mEntry, "");
        mDataSet.setColors(Color.rgb(82, 165, 254), Color.rgb(255, 131, 70), Color.rgb(216, 216, 216));
        mDataSet.setDrawValues(false);//不显示百分比
        mPieData.setDataSet(mDataSet);
        chartViewShouyi.setDescription(null);
        chartViewShouyi.setData(mPieData);
    }

    @OnClick({R.id.title_leftimageview, R.id.title_righttextview, R.id.tv_cash_out, R.id.tv_cash_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.title_righttextview:
                startActivity(new Intent(AssetsAnalysisActivity.this, FundsDetailsActivity.class));//资金明细
                finish();
                break;
            case R.id.tv_cash_out:
                if (System.currentTimeMillis() - lastClick <= 1000) {
//					ToastMaker.showShortToast("点那么快干什么");
                    return;
                }
                lastClick = System.currentTimeMillis();
                startActivity(new Intent(AssetsAnalysisActivity.this, CashOutAct.class));
                finish();
                break;
            case R.id.tv_cash_in:
                if (System.currentTimeMillis() - lastClick <= 1000) {
//					ToastMaker.showShortToast("点那么快干什么");
                    return;
                }
                lastClick = System.currentTimeMillis();
                startActivity(new Intent(AssetsAnalysisActivity.this, CashInAct.class));
                finish();
                break;
        }
    }


}
