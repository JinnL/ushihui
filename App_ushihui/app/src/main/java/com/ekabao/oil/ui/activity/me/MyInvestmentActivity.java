package com.ekabao.oil.ui.activity.me;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.MyFragmentPagerAdapter;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.fragment.BaseFragment;
import com.ekabao.oil.ui.fragment.MyInvestmentFragment;
import com.ekabao.oil.ui.view.ColorFlipPagerTitleView;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * $desc$
 *我的出借
 * @time $data$ $time$
 * Created by Administrator on 2018/7/16.
 */

public class MyInvestmentActivity extends BaseActivity {
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
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_zichan)
    TextView tvZichan;
    @BindView(R.id.tv_benjin)
    TextView tvBenjin;
    @BindView(R.id.tv_zonglixi)
    TextView tvZonglixi;
    @BindView(R.id.ll_head)
    LinearLayout llHead;
    @BindView(R.id.ll_total)
    LinearLayout llTotal;
    @BindView(R.id.magic_indicator7)
    MagicIndicator magicIndicator7;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    /**
     * 我的出借
     * @time 2018/7/16 17:16
     * Created by
     */
    private static final String[] CHANNELS = new String[]{"待还款", "已还款"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_me_investment;
    }

    @Override
    protected void initParams() {
        getData();

        titleCentertextview.setText("我的出借");
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), preparePageInfo(), mDataList);

        viewPager.setAdapter(myFragmentPagerAdapter);
        initMagicIndicator7();

        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private ArrayList<BaseFragment> preparePageInfo() {
        ArrayList<BaseFragment> list = new ArrayList<>();
        //0=持有中(0,1)，1==已还款(3)
        list.add(MyInvestmentFragment.newInstance(0));
        list.add(MyInvestmentFragment.newInstance(1));
        return list;
    }

    private void getData() {
        showWaitDialog("加载中...", false, "");

        OkHttpUtils.post()
                .url(UrlConfig.MYINVESTDAISINFO)
                .addParams("uid", preferences.getString("uid", ""))
                //.addParams("uid","5")
                .addParams("status", "4")

                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                //LogPrintUtil.e("LF--->我的出借", result);
                LogUtils.i("--->我的出借 result：" + result);
                //ptrDo.refreshComplete();
                dismissDialog();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    dismissDialog();
                    JSONObject objmap = obj.getJSONObject("map");
                    double tenderSum = objmap.getDoubleValue("tenderSum");//代收金额
                    double accumulatedIncome = objmap.getDoubleValue("accumulatedIncome");//累计收益
                    double principal = objmap.getDoubleValue("principal");//出借总额


                    tvBenjin.setText(StringCut.getNumKb(tenderSum));
                    tvTotal.setText(StringCut.getNumKb(principal));
                    tvZonglixi.setText(StringCut.getNumKb(accumulatedIncome));

                    /*
                    if (page > 1) {
                        lslb.addAll(lslbs);
                    } else {
                        lslb = lslbs;
                    }
                    if (lslb.size() == 0){
                        lslb = lslbs;
                    }
                    //LogUtils.i("--->我的出借：lslb=" + lslb);
                    if (adapter == null) {
                        adapter = new InvestmentAdapter(NewMyInvestmentActivity.this, lslb);
                        lvTouzi.setAdapter(adapter);
                    } else {
                        if (page == 1) {
                            adapter = new InvestmentAdapter(NewMyInvestmentActivity.this, lslb);
                            lvTouzi.setAdapter(adapter);
                        }
                        adapter.onDateChange(lslb);
                    }

                    if (lslbs.size() == 10) {
                        loadComplete();
                    } else {
                        tv_footer.setText("全部加载完毕");
                        footerlayout.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                    }*/

                } else if ("9998".equals(obj.getString("errorCode"))) {
                    //finish();
//					new show_Dialog_IsLogin(getActivity()).show_Is_Login() ;
                } else {
                    ToastMaker.showShortToast("服务器异常");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                //ptrDo.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });

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
                simplePagerTitleView.setNormalColor(Color.parseColor("#666666"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#EE4845"));
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

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }*/
}
