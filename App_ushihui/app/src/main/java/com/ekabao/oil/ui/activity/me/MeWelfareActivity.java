package com.ekabao.oil.ui.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.ekabao.oil.adapter.CouponsAdapter;
import com.ekabao.oil.adapter.TiyanjinAdapter;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.CouponsBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.fragment.BaseFragment;
import com.ekabao.oil.ui.fragment.MeCouponsFragment;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 易卡宝  App
 * 我的福利  优惠券 体验金不要了
 *
 * @time 2018/7/19 16:37
 * Created by lj on 2018/7/19 16:37.
 */

public class MeWelfareActivity extends BaseActivity {
   /*
    @BindView(R.id.magic_indicator7)
    MagicIndicator magicIndicator7;
    @BindView(R.id.view_pager)
    ViewPager viewPager;*/

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
    @BindView(R.id.rv_news)
    RecyclerView rvNews;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.tv_history_coupons)
    TextView tvHistoryCoupons;
    @BindView(R.id.tv_help)
    TextView tvHelp;
    @BindView(R.id.tv_call_phone)
    TextView tvCallPhone;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    private static final String[] CHANNELS = new String[]{"优惠券", "体验金"};
    private List<String> mDataList = Arrays.asList(CHANNELS);


    private int type=0 ;//1油卡套餐  2 油卡直冲 4 话费套餐 5 话费直冲  是从项目立即支付里面过来的
    private int pid;//标的id
    private List<CouponsBean> lslbs = new ArrayList<CouponsBean>();
    private CouponsAdapter adapter;
    private TiyanjinAdapter tiyanjinAdapter;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private String status = "0";// 状态 0：未使用  1：已使用 2：已过期
    private int flag =0;// 样式 是从那地方打开的, 我的还是充值里面的
    private String etMoney; //传过来的金额 限制金额
    private int deadline =0; //传过来的 限制的时间

    @Override
    protected int getLayoutId() {
        return R.layout.activity_me_welfare;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("优惠券");

       /* MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), preparePageInfo(), mDataList);
        viewPager.setAdapter(myFragmentPagerAdapter);
        initMagicIndicator7();*/

        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        pid = intent.getIntExtra("pid", 0);
        etMoney = intent.getStringExtra("etMoney");
        deadline= intent.getIntExtra("deadline", 0);
        flag= intent.getIntExtra("flag", 0);

        LogUtils.e("etMoney"+etMoney+"deadline"+deadline);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocalApplication.getInstance());
        rvNews.setLayoutManager(linearLayoutManager);


        if (flag == 1) {
            tvHistoryCoupons.setVisibility(View.VISIBLE);
            titleRighttextview.setVisibility(View.VISIBLE);
            titleRighttextview.setText("不使用");
            llBottom.setVisibility(View.GONE);
            //getDETAIL();
            getData();

            if (!TextUtils.isEmpty(etMoney)) {
                LogUtils.e("etMoney"+etMoney+"deadline"+deadline);
                adapter = new CouponsAdapter(rvNews, lslbs, R.layout.item_coupons, etMoney,deadline);
            } else {
                adapter = new CouponsAdapter(rvNews, lslbs, R.layout.item_coupons);
            }
        } else {
            titleRighttextview.setVisibility(View.GONE);
            llBottom.setVisibility(View.VISIBLE);
            getData();
            adapter = new CouponsAdapter(rvNews, lslbs, R.layout.item_coupons);
        }
        rvNews.setAdapter(adapter);

            /* 之前的体验金  现在不用了
            tvHistoryCoupons.setVisibility(View.GONE);
            tiyanjinAdapter = new TiyanjinAdapter(rvNews, lslbs, R.layout.item_tiyanjin);
            rvNews.setAdapter(tiyanjinAdapter);
            flag="0";
            status="";*/


        adapter.setonItemViewClickListener(new BaseRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onItemViewClick(BaseViewHolder view, int position) {

                LogUtils.e("setonItemViewClickListener"+position);

                if (flag == 1) {

                    if (!TextUtils.isEmpty(etMoney)) {
                        if (lslbs.get(position).getStatus() != 3) {
                            setResult(Activity.RESULT_OK, new Intent()
                                    .putExtra("position", position));
                            finish();
                        }
                    } else {
                        ToastMaker.showShortToast("不满足出借条件,无法使用");
                    }

                } else if (flag == 3){ //从我的里面过来

                    // type=0 ;1油卡套餐  2 油卡直冲 4 话费套餐 5 话费直冲  是从项目立即支付里面过来的

                    setResult(lslbs.get(position).getType());
                    LogUtils.e("setResult"+lslbs.get(position).getType());
                    finish();
                   /*startActivity(new Intent(MeWelfareActivity.this, OilCardPackageActivity.class)
                            //  .putExtra("is_presell_plan", true)
                            // .putExtra("startDate", isSellingList.get(0).getStartDate())
                           // .putExtra("pid", bean1.getId())
                            //.putExtra("money", 500)
                   );*/

                }
            }
        });
    }

    @OnClick({R.id.tv_history_coupons, R.id.tv_help, R.id.tv_call_phone, R.id.title_righttextview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_history_coupons:
                startActivity(new Intent(MeWelfareActivity.this,
                        CouponsUsedActivity.class));
                break;
            case R.id.tv_help:
                startActivity(new Intent(MeWelfareActivity.this, CallCenterActivity.class));
                break;
            case R.id.tv_call_phone:
                DialogMaker.showKufuPhoneDialog(MeWelfareActivity.this);
                break;
            case R.id.title_righttextview:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;


        }

    }


    private void getData() {
        OkHttpUtils.post()
                .url(UrlConfig.CONPONSUNUSE)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("status", status)
                .addParams("type", type+"")   //0 体验金
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                LogUtils.e("优惠券"+result);
                //ptr_conponsunuse.refreshComplete();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONArray objrows = objmap.getJSONArray("list");
                    List<CouponsBean> couponsBeans = JSON.parseArray(objrows.toJSONString(), CouponsBean.class);

                    if (couponsBeans.size() > 0) {
                        rvNews.setVisibility(View.VISIBLE);
                        llEmpty.setVisibility(View.GONE);

                        lslbs.clear();
                        lslbs.addAll(couponsBeans);
                        adapter.notifyDataSetChanged();


                        if (type == 1) {

                          /*  LogUtils.e("--->我的红包(未使用)：" + result);
                            lslbs.clear();
                            for (CouponsBean cbean : couponsBeans) {
                                if (cbean.getType() != 3) {
                                    lslbs.add(cbean);
                                }
                            }
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            LogUtils.e("--->优惠券：" + lslbs.size());*/
                        } else {
                            //LogUtils.e("--->体验金：" + result);


                          /*  for (CouponsBean cbean : couponsBeans) {
                                if (cbean.getType() == 3) {
                                    lslbs.add(cbean);
                                }
                            }
                            if (tiyanjinAdapter != null) {
                                tiyanjinAdapter.notifyDataSetChanged();
                            }
                            LogUtils.e("--->体验金：" + lslbs.size());*/
                        }

                    } else {
                        rvNews.setVisibility(View.GONE);
                        llEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText("暂无数据");
                        lslbs.clear();

                    }
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    MeWelfareActivity.this.finish();
//					new show_Dialog_IsLogin(getActivity()).show_Is_Login() ;
                } else {
                    ToastMaker.showShortToast("服务器异常");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                ToastMaker.showShortToast("请检查网络");
            }
        });

    }


    private void getDETAIL() {

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

                            JSONArray objrows = map.getJSONArray("couponList");
                            List<CouponsBean> couponsBeans = JSON.parseArray(objrows.toJSONString(), CouponsBean.class);
                            if (couponsBeans.size() > 0) {
                                rvNews.setVisibility(View.VISIBLE);
                                llEmpty.setVisibility(View.GONE);

                                lslbs.clear();
                                lslbs.addAll(couponsBeans);
                                adapter.notifyDataSetChanged();
                            }

                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            MeWelfareActivity.this.finish();

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
    }


    private ArrayList<BaseFragment> preparePageInfo() {
        ArrayList<BaseFragment> list = new ArrayList<>();
        //0=持有中(0,1)，1==已还款(3)
        list.add(MeCouponsFragment.newInstance(1));
        list.add(MeCouponsFragment.newInstance(2));
        return list;
    }

  /*  private void initMagicIndicator7() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator7);
        //magicIndicator.setBackgroundColor(Color.parseColor("#fafafa"));
        magicIndicator.setBackgroundColor(Color.WHITE);

        CommonNavigator commonNavigator7 = new CommonNavigator(this);
        commonNavigator7.setScrollPivotX(0.65f);
        commonNavigator7.setAdjustMode(true);
        commonNavigator7.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#666666"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#EE4845"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;

              *//*  SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;*//*
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 3));
                indicator.setLineWidth(UIUtil.dip2px(context, 30));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#EE4845"));
                return indicator;

            }
        });
        magicIndicator.setNavigator(commonNavigator7);
        ViewPagerHelper.bind(magicIndicator, viewPager);

       *//* magicIndicator.setNavigator(commonNavigator7);
        LinearLayout titleContainer = commonNavigator7.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerPadding(UIUtil.dip2px(this, 15));
        titleContainer.setDividerDrawable(getResources().getDrawable(R.drawable.simple_splitter));
        ViewPagerHelper.bind(magicIndicator, viewPager);*//*

       *//* magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return UIUtil.dip2px(FixedTabExampleActivity.this, 15);
            }
        });*//*
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
