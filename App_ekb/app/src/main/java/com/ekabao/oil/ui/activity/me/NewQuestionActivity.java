package com.ekabao.oil.ui.activity.me;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.fragment.QuestionFragment;
import com.ekabao.oil.ui.view.ColorFlipPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2017/11/1.
 */

public class NewQuestionActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;

    @BindView(R.id.vp_question)
    ViewPager vpQuestion;
    @BindView(R.id.title_lefttextview)
    TextView titleLefttextview;
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
    @BindView(R.id.magic_indicator7)
    MagicIndicator magicIndicator7;

    private int isWhat;
    private String[] titles;
    private Fragment frag;
    private Fragment frag1;
    private Fragment frag2;
    private Fragment frag3;
    private Fragment frag4;
    private Fragment frag5;
    private TabFragPA tabFragPA; //



    private static final String[] CHANNELS = new String[]{"油卡充值", "话费充值", "商城问题", "服务售后", "账号管理", "其他问题"};

    private List<String> mDataList = Arrays.asList(CHANNELS);



    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_question;
    }



    @Override
    protected void initParams() {
        isWhat = Integer.parseInt(getIntent().getStringExtra("isWhat"));
        titleCentertextview.setText("问题解答");
        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titles = new String[]{"油卡充值", "话费充值", "商城问题", "服务售后", "账号管理", "其他问题"};
        // 给viewpager设置适配器;继承fragmentactivity
        tabFragPA = new TabFragPA(getSupportFragmentManager());
        vpQuestion.setAdapter(tabFragPA);
        // viewpagerindictor和viewpager关联


        initMagicIndicator7();


        vpQuestion.setOffscreenPageLimit(5);
        if (isWhat == 0) {
            vpQuestion.setCurrentItem(0);
        } else if (isWhat == 1) {
            vpQuestion.setCurrentItem(1);
        } else if (isWhat == 2) {
            vpQuestion.setCurrentItem(2);
        } else if (isWhat == 3) {
            vpQuestion.setCurrentItem(3);
        } else if (isWhat == 4) {
            vpQuestion.setCurrentItem(4);
        } else if (isWhat == 5) {
            vpQuestion.setCurrentItem(5);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
    }

    class TabFragPA extends FragmentPagerAdapter {

        public TabFragPA(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int arg0) {
            switch (arg0) {
                case 0://认证注册
                    frag = frag == null ?  QuestionFragment.newInstance(12) : frag;
                    return frag;
                case 1://安全保障
                    frag1 = frag1 == null ?  QuestionFragment.newInstance(9) : frag1;
                    return frag1;
                case 2://充值提现
                    frag2 = frag2 == null ?  QuestionFragment.newInstance(11) : frag2;
                    return frag2;
                case 3://出借福利
                    frag3 = frag3 == null ? QuestionFragment.newInstance(7) : frag3;
                    return frag3;
                case 4://产品介绍
                    frag4 = frag4 == null ? QuestionFragment.newInstance(8) : frag4;
                    return frag4;
                case 5://其他问题
                    frag5 = frag5 == null ? QuestionFragment.newInstance(5) : frag5;
                    return frag5;
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position % titles.length];
        }

        @Override
        public int getCount() {

            return titles.length;
        }

    }
    private void initMagicIndicator7() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator7);
        //magicIndicator.setBackgroundColor(Color.parseColor("#fafafa"));
        magicIndicator.setBackgroundColor(Color.WHITE);

        CommonNavigator commonNavigator7 = new CommonNavigator(this);
        commonNavigator7.setScrollPivotX(0.65f);
        //commonNavigator7.setAdjustMode(true);
        commonNavigator7.setSkimOver(true);

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
                simplePagerTitleView.setSelectedColor(Color.parseColor("#155CFC"));

                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vpQuestion.setCurrentItem(index);
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
                        vpQuestion.setCurrentItem(index);
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
                indicator.setColors(Color.parseColor("#155CFC"));
                return indicator;

            }
        });
        magicIndicator.setNavigator(commonNavigator7);
        ViewPagerHelper.bind(magicIndicator, vpQuestion);

       /* magicIndicator.setNavigator(commonNavigator7);
        LinearLayout titleContainer = commonNavigator7.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerPadding(UIUtil.dip2px(this, 15));
        titleContainer.setDividerDrawable(getResources().getDrawable(R.drawable.simple_splitter));
        ViewPagerHelper.bind(magicIndicator, vpQuestion);*/

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

}
