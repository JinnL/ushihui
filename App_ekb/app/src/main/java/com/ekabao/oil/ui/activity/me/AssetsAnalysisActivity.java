package com.ekabao.oil.ui.activity.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.FundsBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.activity.CapitaldetailsActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * $desc$
 *资产与收益
 * @time $data$ $time$
 * Created by Administrator on 2018/7/16.
 */

public class AssetsAnalysisActivity extends BaseActivity {


    @BindView(R.id.title_lefttextview)
    TextView titleLefttextview;
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_centerimageview)
    ImageView titleCenterimageview;

    @BindView(R.id.title_rightimageview)
    ImageView titleRightimageview;
    @BindView(R.id.view_line_bottom)
    View viewLineBottom;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.chart_view_total)
    PieChart chartViewTotal;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.title_righttextview)
    TextView titleRighttextview;
    @BindView(R.id.tv_huikuan)
    TextView tvHuikuan;
    @BindView(R.id.tv_freeze_amount)
    TextView tvFreezeAmount;
    @BindView(R.id.tv_total_shouyi)
    TextView tvTotalShouyi;
    @BindView(R.id.chart_view_shouyi)
    PieChart chartViewShouyi;
    @BindView(R.id.tv_tender_money)
    TextView tvTenderMoney;
    @BindView(R.id.tv_red_money)
    TextView tvRedMoney;
    @BindView(R.id.tv_activity_money)
    TextView tvActivityMoney;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private FundsBean fundsBean;
    private BigDecimal activityMoney;//活动收益
    private BigDecimal redpacket;//红包收益
    private BigDecimal tenderMoney;//标收益
    Long lastClick = 0L;

    /**
     * 资产 与收益分析
     *
     * @time 2018/7/16 15:42
     * Created by
     */
    @Override
    protected int getLayoutId() {

        return R.layout.activity_assets_analysis;
    }

    @Override
    protected void initParams() {

        titleCentertextview.setText("资产与收益");

        getData();


    }

    @OnClick({R.id.title_leftimageview, R.id.tv_capital})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.tv_capital:
                startActivity(new Intent(AssetsAnalysisActivity.this,
                        CapitaldetailsActivity.class));//资金明细
                //finish();
                break;
        }
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

                    activityMoney = map.getBigDecimal("activityMoney");     //活动奖励金额
                    redpacket = map.getBigDecimal("redpacket");//代收红包总额
                    tenderMoney = map.getBigDecimal("tenderMoney");  //标的收益

                    // `balance`  '可用余额',   freeze`  '冻结金额' `wprincipal`  '待收本金', `winterest`  '待收利息',
                    BigDecimal balance = fundsBean.getBalance();
                    BigDecimal total = balance.add(fundsBean.getWprincipal()).add(fundsBean.getWinterest()).add(fundsBean.getFreeze());
                    tvTotal.setText("资产统计  " + total);


                    tvBalance.setText("" + fundsBean.getBalance()); //余额
                    tvHuikuan.setText(fundsBean.getWprincipal().add(fundsBean.getWinterest()) + "");
                    tvFreezeAmount.setText(fundsBean.getFreeze()+ ""); //提现处理中


                    tvTotalShouyi.setText("收益统计  " + tenderMoney.add( redpacket).add(activityMoney));

                    tvTenderMoney.setText(tenderMoney+ ""); //项目收益
                    tvRedMoney.setText(redpacket+ "");
                    tvActivityMoney.setText(activityMoney+"");

                    //tvBalanceAcount.setText(fundsBean.getBalance().floatValue() + "");
                    //tvInvestAmount.setText(fundsBean.getInvestAmount().floatValue()+"");

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
        PieEntry entry3 = new PieEntry((fundsBean.getWprincipal().floatValue() + fundsBean.getWinterest().floatValue()), "回款中");
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
        mDataSet.setColors(Color.rgb(46, 175, 255),
                Color.rgb(238, 72, 69),
                Color.rgb(253, 195, 50));
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
        // mDataSet.setColors(Color.rgb(82, 165, 254), Color.rgb(255, 131, 70), Color.rgb(216, 216, 216));
        mDataSet.setColors(Color.rgb(46, 175, 255),
                Color.rgb(238, 72, 69),
                Color.rgb(253, 195, 50));
        mDataSet.setDrawValues(false);//不显示百分比
        mPieData.setDataSet(mDataSet);
        chartViewShouyi.setDescription(null);
        chartViewShouyi.setData(mPieData);
    }


    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }*/
}
