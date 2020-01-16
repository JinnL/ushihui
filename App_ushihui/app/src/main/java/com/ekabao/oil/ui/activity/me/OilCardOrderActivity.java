package com.ekabao.oil.ui.activity.me;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
import com.ekabao.oil.adapter.MyFragmentPagerAdapter;
import com.ekabao.oil.adapter.OilCardBuyAdapter;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.OilOrdersbean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.fragment.BaseFragment;
import com.ekabao.oil.ui.fragment.MeOilCardOrderFragment;
import com.ekabao.oil.ui.view.ColorFlipPagerTitleView;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class OilCardOrderActivity extends BaseActivity {

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
    @BindView(R.id.magic_indicator7)
    MagicIndicator magicIndicator7;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private SharedPreferences preferences;
    private String uid;
    private List<OilOrdersbean> lslbs = new ArrayList<OilOrdersbean>();
    private OilCardBuyAdapter adapter;
    private int type = 3;//1：油卡 2：手机 3：直购

    private static final String[] CHANNELS = new String[]{"全部", "待支付", "已完成", "已取消"};
    private List<String> mDataList = Arrays.asList(CHANNELS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_oil_card_order;
    }

    @Override
    protected void initParams() {
        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 3);

        titleCentertextview.setText("油卡领取订单");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocalApplication.getInstance());
        rvNews.setLayoutManager(linearLayoutManager);

        adapter = new OilCardBuyAdapter(rvNews, lslbs, R.layout.item_my_oilcard_buy);
        rvNews.setAdapter(adapter);
        getData();


        adapter.setonItemViewClickListener(new BaseRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onItemViewClick(BaseViewHolder view, int position) {
                startActivity(new Intent(OilCardOrderActivity.this,
                                MyOilCardBuyDetailsActivity.class)
                                .putExtra("orderId", lslbs.get(position).getId() + "")
                        // .putExtra("type", type)
                );
            }
        });

        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), preparePageInfo(), mDataList);
        viewPager.setAdapter(myFragmentPagerAdapter);
        initMagicIndicator7();




    }

    private ArrayList<BaseFragment> preparePageInfo() {
        ArrayList<BaseFragment> list = new ArrayList<>();
        //0=持有中(0,1)，1==已还款(3)
        ////1：油卡 2：手机 3：直购
        //状态 0-待支付，1-已支付，2-失败（已取消、退款），3-已完成，4-已退订，5-已发货，6-删除

       // "全部", "待支付", "已完成(1 3 5)", "已取消"
        list.add(MeOilCardOrderFragment.newInstance(type, 99));
        list.add(MeOilCardOrderFragment.newInstance(type, 0));
        list.add(MeOilCardOrderFragment.newInstance(type, 1));
        list.add(MeOilCardOrderFragment.newInstance(type, 2));


        return list;
    }

    private void initMagicIndicator7() {
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
                simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#333333"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;

              /*  SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;*/
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
//                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
//                indicator.setLineHeight(UIUtil.dip2px(context, 3));
//                indicator.setLineWidth(UIUtil.dip2px(context, 30));
//                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
//                indicator.setStartInterpolator(new AccelerateInterpolator());
//                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
//                indicator.setColors(Color.parseColor("#EE4845"));
//                return indicator;
                return null;

            }
        });
        magicIndicator.setNavigator(commonNavigator7);
        ViewPagerHelper.bind(magicIndicator, viewPager);

       /* magicIndicator.setNavigator(commonNavigator7);
        LinearLayout titleContainer = commonNavigator7.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerPadding(UIUtil.dip2px(this, 15));
        titleContainer.setDividerDrawable(getResources().getDrawable(R.drawable.simple_splitter));
        ViewPagerHelper.bind(magicIndicator, viewPager);*/

       /* magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return UIUtil.dip2px(FixedTabExampleActivity.this, 15);
            }
        });*/
    }


    private void getData() {
        // LogUtils.e("订单列表type"+type+"status"+status); myOrders canBuyFuelCard
        //preferences.getString("uid", "")
        OkHttpUtils.post()
                .url(UrlConfig.myOrders)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("type", type + "")
                //.addParams("status", preferences.getString("uid", "")) // TODO: 2018/12/6 1：进行中 3：已结束
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                LogUtils.e("订单列表" + result);
                //ptr_conponsunuse.refreshComplete();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONArray objrows = objmap.getJSONArray("myOrderList");
                    List<OilOrdersbean> couponsBeans = JSON.parseArray(objrows.toJSONString(), OilOrdersbean.class);

                    //版本兼容
//					for (int i = 0; i < lslbs.size(); i++) {
//						if(lslbs.get(i).getType()!=4&lslbs.get(i).getType()!=1&lslbs.get(i).getType()!=2&lslbs.get(i).getType()!=3){
//							lslbs.remove(i);
//							i--;
//						}
//					}
                    if (couponsBeans.size() > 0) {


                        rvNews.setVisibility(View.VISIBLE);
                        llEmpty.setVisibility(View.GONE);
                        lslbs.clear();
                        lslbs.addAll(couponsBeans);
                        if (adapter != null) {

                            adapter.notifyDataSetChanged();
                            LogUtils.e("油卡购买订单" + couponsBeans.size());
                        }

                    } else {
                        rvNews.setVisibility(View.GONE);
                        llEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText("暂无数据");
                        lslbs.clear();

                    }
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    finish();
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
}
