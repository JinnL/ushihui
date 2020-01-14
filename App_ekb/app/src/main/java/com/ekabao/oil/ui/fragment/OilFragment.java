package com.ekabao.oil.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.MyFragmentPagerAdapter;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.ui.view.ColorFlipPagerTitleView;
import com.ekabao.oil.util.LogUtils;

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
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * ${APP_NAME}  App_akzj
 *
 * @time 2019/3/15 16:21
 * Created by lj on 2019/3/15 16:21.
 */

public class OilFragment extends BaseFragment {


    @BindView(R.id.fillStatusBarView)
    View fillStatusBarView;
    @BindView(R.id.magic_indicator7)
    MagicIndicator magicIndicator7;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    Unbinder unbinder;

    private static final String[] CHANNELS = new String[]{"油卡套餐", "油卡直充"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private OilPackageFragment oilPackageFragment;

    private int pid; //油卡套餐
    private int money;
    private int month;
    private SharedPreferences preferences = LocalApplication.sharereferences;

    public static OilFragment newInstance() {

        Bundle args = new Bundle();

        OilFragment fragment = new OilFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static OilFragment newInstance(int pid) {

        Bundle args = new Bundle();

        OilFragment fragment = new OilFragment();
        args.putInt("pid", pid);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            pid = args.getInt("pid");
            LogUtils.e("OilFragment+onCreate+pid+" + pid);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_oil;
    }

    @Override
    protected void initParams() {

        //getChildFragmentManager
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(
                getChildFragmentManager(),
                preparePageInfo(), mDataList);

        viewPager.setAdapter(myFragmentPagerAdapter);
        //viewPager.setAdapter(mExamplePagerAdapter);

        initMagicIndicator7();




    }

    private void initMagicIndicator7() {

        //magicIndicator.setBackgroundColor(Color.parseColor("#fafafa"));
        magicIndicator7.setBackgroundColor(Color.WHITE);

        CommonNavigator commonNavigator7 = new CommonNavigator(getActivity());
        commonNavigator7.setScrollPivotX(0.65f);
        //  commonNavigator7.setAdjustMode(true);
        commonNavigator7.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#9B9EA5"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#373A41"));
                simplePagerTitleView.setTextSize(18);
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
                //油卡套餐和油卡直充下方横线的颜色
                indicator.setColors(Color.parseColor("#000000"));
                return indicator;

            }
        });
        magicIndicator7.setNavigator(commonNavigator7);
        ViewPagerHelper.bind(magicIndicator7, viewPager);

       /* magicIndicator7.setNavigator(commonNavigator7);
        LinearLayout titleContainer = commonNavigator7.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerPadding(UIUtil.dip2px(this, 15));
        titleContainer.setDividerDrawable(getResources().getDrawable(R.drawable.simple_splitter));
        ViewPagerHelper.bind(magicIndicator7, viewPager);*/

       /* magicIndicator7.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return UIUtil.dip2px(FixedTabExampleActivity.this, 15);
            }
        });*/
    }

    private ArrayList<BaseFragment> preparePageInfo() {

        LogUtils.e("preparePageInfo" + pid);

        ArrayList<BaseFragment> list = new ArrayList<>();

        oilPackageFragment = OilPackageFragment.newInstance(pid);

        list.add(oilPackageFragment);
        // list.add(NewsFragment.newInstance(3));
        list.add(OilRechargeFragment.newInstance());

      /*  if (pid!=0) {
            oilPackageFragment.setPid(pid,money,month);
            LogUtils.e("设置油卡套餐22222");
        }*/

        return list;
    }

    /**
     * 设置消息的小红点 的显示隐藏
     */
    public void setOilPackageFragment(int Pid, int Money, int Month) {

        pid = Pid;
        money = Money;
        month = Month;

     /*   if (oilPackageFragment != null) {

            oilPackageFragment.setPid(pid, money, month);
            LogUtils.e("设置油卡套餐22222" + pid);
        }*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        int fragment_type = preferences.getInt("fragment_type", 0);
        // type=0 ;1油卡套餐  2 油卡直冲 4 话费套餐 5 话费直冲  是从项目立即支付里面过来的
        if (fragment_type != 0) {

            if (fragment_type == 1) {
                viewPager.setCurrentItem(0);
            } else {
                viewPager.setCurrentItem(1);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
