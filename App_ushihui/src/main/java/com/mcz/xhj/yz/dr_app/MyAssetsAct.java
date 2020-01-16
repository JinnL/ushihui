package com.mcz.xhj.yz.dr_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

public class MyAssetsAct extends BaseActivity {
	@BindView(R.id.tv_invest_amount)
	TextView tv_invest_amount;
	@BindView(R.id.tv_shouyi_amount)
	TextView tv_shouyi_amount;
//	@BindView(R.id.tv_amount)
//	private TextView tv_amount;
	@BindView(R.id.tv_balance_money)
	TextView tv_balance_money;
	@BindView(R.id.tv_winter_money)
	TextView tv_winter_money;
	@BindView(R.id.tv_invest_money)
	TextView tv_invest_money;
	@BindView(R.id.ptr_myassets)
	PtrClassicFrameLayout ptr_myassets;
	@BindView(R.id.title_centertextview)
	TextView centertv;
	@BindView(R.id.title_leftimageview)
	ImageView leftima;
	@BindView(R.id.title_righttextview)
	TextView righttv;
	@BindView(R.id.chart)
	PieChart mChart;
	
	private PieData mPieData;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_myassets;
	}
	@SuppressLint("NewApi")
	@Override
	protected void initParams() {
		centertv.setText("资产明细");
		righttv.setVisibility(View.VISIBLE);
		righttv.setText("明细");
		righttv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MyAssetsAct.this, MyDetailAct.class));
				
			}
		});
		leftima.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		if(preferences.getString("uid", "")!=""){
			getData();
		}
		ptr_myassets.setPtrHandler(new PtrHandler() {
			
			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				if(preferences.getString("uid", "")!=""){
					getData();
				}
			}
			
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content,
					View header) {
				// TODO Auto-generated method stub
				return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
			}
		});
	}

	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
//	String bz = "0";
//	String fz = "0";
//	String wz = "0";
	double balance;
	double freeze;
	double wprincipal;
	private void getData() {
		showWaitDialog("加载中...", true, "");
		OkHttpUtils.post()
		.url(UrlConfig.MYASSETSINFO)
		.addParams("uid", preferences.getString("uid", ""))
		.addParams("version", UrlConfig.version)
		.addParams("channel", "2")
		.build().execute(new StringCallback() {
			
			@Override
			public void onResponse(String result) {
				dismissDialog();
				ptr_myassets.refreshComplete();
				JSONObject obj = JSON.parseObject(result);
				if(obj.getBoolean("success")){
					JSONObject objmap = obj.getJSONObject("map");
					JSONObject objfunds = objmap.getJSONObject("funds");
					balance = objfunds.getDoubleValue("balance");//余额
					freeze = objfunds.getDoubleValue("freeze");//冻结金额
					wprincipal = objfunds.getDoubleValue("wprincipal");//代收本金
					double investAmount = objfunds.getDoubleValue("investAmount");//总投资金额
					double investProfit = objfunds.getDoubleValue("investProfit");//投资收益
					double spreadProfit = objfunds.getDoubleValue("spreadProfit");//推广收益
					double shouyi = investProfit + spreadProfit;
					double zichanamount = balance + freeze + wprincipal;
					tv_invest_amount.setText(stringCut.getNumKb(investAmount));
					tv_shouyi_amount.setText(stringCut.getNumKb(shouyi));
					tv_balance_money.setText("  "+stringCut.getNumKb(balance));
					tv_winter_money.setText("  "+stringCut.getNumKb(freeze));
					tv_invest_money.setText("  "+stringCut.getNumKb(wprincipal));
					if(zichanamount>0){
						if((balance/zichanamount)<0.01){
							balance = 0;
						}
						if((freeze/zichanamount)<0.01){
							freeze = 0;
						}
						if((wprincipal/zichanamount)<0.01){
							wprincipal = 0;
						}
//						fz = stringCut.getNumInt(freeze/zichanamount*100);
//						wz = stringCut.getNumInt(wprincipal/zichanamount*100);
					}else{
						balance = 0;
						freeze = 0;
						wprincipal = 0;
					}
					mPieData = getPieData(4, wprincipal,freeze,balance,zichanamount);
					showChart(mChart, mPieData,stringCut.getNumKb(zichanamount));
//					tv_amount.setText(stringCut.getNumKb(zichanamount));
				}
				else if ("9998".equals(obj.getString("errorCode"))) {
					finish();
					new show_Dialog_IsLogin(MyAssetsAct.this).show_Is_Login();
				} 
				else{
					ToastMaker.showShortToast("服务器异常");
				}
			}
			
			@Override
			public void onError(Call call, Exception e) {
				dismissDialog();
//				mChart.setDrawCenterText(true);  //饼状图中间可以添加文字    
//				mChart.setCenterText("暂无数据");
				ptr_myassets.refreshComplete();
				ToastMaker.showShortToast("请检查网络");
			}
		});
		
	}
	
	private void showChart(PieChart pieChart, PieData pieData,String text) {    

        pieChart.setHoleRadius(70f);  //半径    
        pieChart.setTransparentCircleRadius(0f); // 半透明圈    
        pieChart.setDrawSliceText(false);
        pieChart.highlightValues(null); 
        //pieChart.setHoleRadius(0)  //实心圆    
        pieChart.setDescription(null);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字    
        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(90); // 初始旋转角度    
        pieChart.setTouchEnabled(false); 
//        pieChart.setRotationEnabled(false); // 可以手动旋转    
        pieChart.setUsePercentValues(false);  //显示成百分比    
        pieChart.setCenterText(generateCenterSpannableText(text));  //饼状图中间的文字 
        pieChart.setCenterTextSize(14f);
        //设置数据    
        pieChart.setData(pieData);     
        Legend mLegend = pieChart.getLegend();  //设置比例图    
        mLegend.setEnabled(false);
        pieChart.animateXY(3000, 2000);  //设置动画    
    }    

    /**  
     *   
     * @param count 分成几部分  
     */
    private PieData getPieData(int count, double wprincipal,double freeze,double balance,double zichanamount) {    

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容    

        for (int i = 0; i < count; i++) {    
            xValues.add("Quarterly" + (i + 1));  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4    
        }    

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图数据    
        /**  
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38  
         * 所以 14代表的百分比就是14%   
         */ 
        float quarterly1 = (float) wprincipal;    
        float quarterly2 = (float) freeze;    
        float quarterly3 = (float) balance; 
        yValues.add(new PieEntry(quarterly1, 0));
        yValues.add(new PieEntry(quarterly2, 1));
        yValues.add(new PieEntry(quarterly3, 2));
        if(zichanamount==0){
        	yValues.add(new PieEntry(1f, 3));
        }
//        else{
//        	yValues.add(new Entry(0, 3));
//        }

        //y轴的集合    
        PieDataSet pieDataSet = new PieDataSet(yValues, "");
        pieDataSet.setSliceSpace(4f); //设置个饼状图之间的距离    

        ArrayList<Integer> colors = new ArrayList<Integer>();    

        // 饼图颜色    
        colors.add(Color.rgb(162, 172, 232));
        colors.add(Color.rgb(255, 181, 147));    
        colors.add(Color.rgb(226, 195, 152)); 
        if(zichanamount==0){
        	colors.add(Color.rgb(242, 242, 242));
        }

        pieDataSet.setColors(colors);    

        DisplayMetrics metrics = getResources().getDisplayMetrics();    
        float px = 5 * (metrics.densityDpi / 160f);    
        pieDataSet.setSelectionShift(px); // 选中态多出的长度    
        pieDataSet.setDrawValues(false);
        PieData pieData = new PieData();
		pieData.setDataSet(pieDataSet);
        return pieData;    
    } 
    private SpannableString generateCenterSpannableText(String text) {

        SpannableString s = new SpannableString("资产总额(元)\n"+text);
        s.setSpan(new RelativeSizeSpan(1.0f), 0, 7, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 7, s.length() -1 , 0);
        s.setSpan(new ForegroundColorSpan(Color.rgb(217, 46, 56)), 7, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.2f), 7, s.length(), 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), 7, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 7, s.length(), 0);
        return s;
    }

}
