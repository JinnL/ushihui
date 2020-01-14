package com.ekabao.oil.ui.activity.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.BaseRecyclerViewAdapter;
import com.ekabao.oil.adapter.CapitaldetailsAdapter;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.CapitaldetailsBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.activity.CapitaldetailsActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class MeBalanceActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_details)
    LinearLayout llDetails;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.tv_balance)
    TextView tvBalance;

    /**
     * 我的--->余额
     *
     * @time 2019/1/21 15:12
     * Created by lj
     */


    private SharedPreferences preferences;

    private String uid;
    int pageon = 1;//当前页
    int total;  //返回总共多少个
    int totalPage; //返回总共多少页

    private double balance; //账号余额

    private CapitaldetailsAdapter noticeAdapter;
    private List<CapitaldetailsBean> rows_List = new ArrayList<CapitaldetailsBean>();// 每次加载的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_me_balance);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_me_balance;
    }

    @Override
    protected void initParams() {

        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");

        getData();
        getDataList();


        AssetManager assets = this.getAssets();
        //根据路径得到Typeface
        Typeface tf = Typeface.createFromAsset(assets, "DIN Medium.ttf");
        //设置字体
        tvBalance.setTypeface(tf);


       /* SpannableStringBuilder sp = new SpannableStringBuilder(bean.getRate() + "+" + bean.getActivityRate() + "%");
        //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        sp.setSpan(new AbsoluteSizeSpan(30, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
        myHolder.tvInterest.setText(sp);*/

        noticeAdapter = new CapitaldetailsAdapter(rvList, rows_List, R.layout.item_capital);

        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(noticeAdapter);

        tvTitle.setText("余额");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        noticeAdapter.setonItemViewClickListener(new BaseRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onItemViewClick(BaseViewHolder view, int position) {

            }
        });


        llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MeBalanceActivity.this, CapitaldetailsActivity.class));
            }
        });


    }

    /**
     * 28.余额查询
     */
    private void getData() {
        //showWaitDialog("加载中...", true, "");

        OkHttpUtils.post()
                .url(UrlConfig.myFunds)
                .addParams("uid", preferences.getString("uid", ""))
                //.addParam("tradeType", "0")
                // .addParam("time",groupTitle.get(groupPosition).getMonths())
                .addParams("pageOn", pageon + "")
                .addParams("pageSize", "10")
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

                    //freeze:冻结金额


                    //  JSONArray arr = map.getJSONObject("page").getJSONArray("rows");
                    //JSONArray arr = map.getJSONArray("memberFundsRecordList");
                    // JSONObject page = map.getJSONObject("page");
                    // totalPage = page.getInteger("totalPage");

                /*    if (arr.size() > 0) {
                        //loadComplete();
                    } else {
                        // tv_footer.setText("全部加载完毕");
                        // footerlayout.setVisibility(View.VISIBLE);
                        // pb.setVisibility(View.GONE);
                    }
                    if (pageon == 1) {
                        rows_List.clear();
                    }

                    if (arr.size() <= 0) {
                        rvCapital.setVisibility(View.GONE);
                        llEmpty.setVisibility(View.VISIBLE);
                    } else {
                        rvCapital.setVisibility(View.VISIBLE);
                        llEmpty.setVisibility(View.GONE);
                        // mrows_List.clear();
                        //rlNoNomessage.setVisibility(View.GONE);
                        // lvPlatformMessage.setVisibility(View.VISIBLE);
                        List<CapitaldetailsBean> mrows_List = JSON.parseArray(arr.toJSONString(), CapitaldetailsBean.class);
                        rows_List.addAll(mrows_List);
                    }
                    noticeAdapter.notifyDataSetChanged();
                    pageon++;*/

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

    /**
     * 29.余额明细
     */
    private void getDataList() {
        //showWaitDialog("加载中...", true, "");

        OkHttpUtils.post()
                .url(UrlConfig.myFundsList)
                .addParams("uid", preferences.getString("uid", ""))
                //.addParam("tradeType", "0")
                // .addParam("time",groupTitle.get(groupPosition).getMonths())
                .addParams("pageOn", pageon + "")
                .addParams("pageSize", "10")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                //dismissDialog();
                LogUtils.i("29.余额明细列表：" + response);
                //refreshLayout.finishRefresh();
                //{"map":{"freeze":0.00,"balance":10000.00},"success":true}
                JSONObject obj = JSON.parseObject(response);

                if (obj.getBoolean(("success"))) { //
                    JSONObject map = obj.getJSONObject("map");

                    JSONObject page = map.getJSONObject("page");
                    totalPage = page.getInteger("totalPage");

                    JSONArray arr = page.getJSONArray("rows");

                    //JSONArray arr = map.getJSONArray("memberFundsRecordList");
                    if (arr.size() > 0) {
                        rows_List.clear();
                        List<CapitaldetailsBean> mrows_List = JSON.parseArray(arr.toJSONString(), CapitaldetailsBean.class);
                        rows_List.addAll(mrows_List);
                        noticeAdapter.notifyDataSetChanged();

                        //loadComplete();
                    } else {
                        // tv_footer.setText("全部加载完毕");
                        // footerlayout.setVisibility(View.VISIBLE);
                        // pb.setVisibility(View.GONE);
                    }

                     /*
                    if (arr.size() <= 0) {
                        rvCapital.setVisibility(View.GONE);
                        llEmpty.setVisibility(View.VISIBLE);
                    } else {
                        rvCapital.setVisibility(View.VISIBLE);
                        llEmpty.setVisibility(View.GONE);
                        // mrows_List.clear();
                        //rlNoNomessage.setVisibility(View.GONE);
                        // lvPlatformMessage.setVisibility(View.VISIBLE);

                    }
                    pageon++;*/

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
}
