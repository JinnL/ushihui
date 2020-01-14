package com.ekabao.oil.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.fragment.BaseFragment;
import com.ekabao.oil.ui.fragment.NewsFragment;
import com.ekabao.oil.ui.view.ColorFlipPagerTitleView;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.DateUtil;
import com.ekabao.oil.util.LogUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
import okhttp3.Call;

public class NewsActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.magic_indicator7)
    MagicIndicator magicIndicator7;
    @BindView(R.id.view_pager)
    ViewPager viewPager;


    @BindView(R.id.iv_system)
    ImageView ivSystem;
    @BindView(R.id.iv_red_new)
    ImageView ivRedNew;
    @BindView(R.id.tv_system)
    TextView tvSystem;
    @BindView(R.id.cl_system)
    ConstraintLayout clSystem;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.cl_notice)
    ConstraintLayout clNotice;
    @BindView(R.id.tv_media)
    TextView tvMedia;
    @BindView(R.id.cl_media)
    ConstraintLayout clMedia;
    @BindView(R.id.tv_news)
    TextView tvNews;
    @BindView(R.id.cl_news)
    ConstraintLayout clNews;

    /**
     * 个人消息
     *
     * @time 2018/7/13 15:52
     * Created by
     */
    private static final String[] CHANNELS = new String[]{"活动类", "交易类", "系统类"};
    @BindView(R.id.tv_system_time)
    TextView tvSystemTime;
    @BindView(R.id.tv_notice_time)
    TextView tvNoticeTime;
    @BindView(R.id.tv_media_time)
    TextView tvMediaTime;
    @BindView(R.id.tv_news_time)
    TextView tvNewsTime;


    private List<String> mDataList = Arrays.asList(CHANNELS);
    private SharedPreferences preferences = LocalApplication.sharereferences;
  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("消息通知");
        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       /* MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), preparePageInfo(), mDataList);

        viewPager.setAdapter(myFragmentPagerAdapter);*/
        //viewPager.setAdapter(mExamplePagerAdapter);

        // initMagicIndicator7();


        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            // rlSystem.setVisibility(View.GONE);
            clSystem.setClickable(false);

            tvSystem.setText("暂无相关内容");

        } else {
            clSystem.setClickable(true);
            // rlSystem.setVisibility(View.VISIBLE);

            getNotice(1);

        }
        getNotice(2);
        getNotice(3);
        getNotice(4);

        clSystem.setOnClickListener(this);
        clNews.setOnClickListener(this);
        clNotice.setOnClickListener(this);
        clMedia.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cl_system://系统通知 充值提醒
                if (!preferences.getString("uid", "").equalsIgnoreCase("")) {
                    startActivity(new Intent(this, NoticeActivity.class)
                            .putExtra("activity", 1)
                    );
                }
                break;
            case R.id.cl_notice://平台公告
                startActivity(new Intent(this, NoticeActivity.class)
                        .putExtra("activity", 2));
                break;
            case R.id.cl_media://媒体报道
                startActivity(new Intent(this, NoticeActivity.class)
                        .putExtra("activity", 3));
                break;
            case R.id.cl_news://行业资讯
                //ToastMaker.showShortToast("暂无相关内容");
                startActivity(new Intent(this, NoticeActivity.class)
                        .putExtra("activity", 4));
                break;
        }
    }


    // 获取通知列表
    private void getNotice(final int num) {

        //1.系统消息 2 平台公告/3  3.媒体报道'/22,

        String notice = UrlConfig.NOTICE;
        int type = 1;

        switch (num) {
            case 1:
                notice = UrlConfig.GETMESSAGE;
                break;
            case 2:
                notice = UrlConfig.WEB_AN;
                type = 14;
                break;
            case 3:
                notice = UrlConfig.WEB_AN;
                type = 22;
                break;
            case 4:
                notice = UrlConfig.WEB_AN;
                type = 18;
                break;

            default:

                break;
        }
        // showWaitDialog("加载中...", true, "");

        OkHttpUtils.post().url(notice)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("type", type + "")
                .addParams("proId", type + "")
                .addParams("pageOn", "1")
                .addParams("pageSize", "5")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {

                        //refreshLayout.finishRefresh();

                        LogUtils.e("--->消息：" + response);
Log.d("NewsActivity",response);
                        dismissDialog();
                        //ptrInvest.refreshComplete();
                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray arr = map.getJSONObject("page").getJSONArray("rows");

                            JSONObject page = map.getJSONObject("page");
                            //totalPage = page.getInteger("totalPage");

                            // LogUtils.e("arr.get(0)" + arr.get(0).toString());


                            if (arr.size() <= 0) {


                            } else {
                                // mrows_List.clear();
                                Log.d("asdasd   ", JSON.parseObject(arr.get(0).toString()) + "");
                                String timeStr = "";
                                String title = JSON.parseObject(arr.get(0).toString()).getString("title");

                                //LogUtils.e("arr.get(0)" + title);
                                if (title == null) {
                                    title = "暂无相关内容";
                                    timeStr = "";
                                } else {
                                    String time = "";
                                    if(null  != JSON.parseObject(arr.get(0).toString()).getString("createTime")){
                                        time =  JSON.parseObject(arr.get(0).toString()).getString("createTime");
                                    }else if(null!= JSON.parseObject(arr.get(0).toString()).getString("addTime")){
                                        time =  JSON.parseObject(arr.get(0).toString()).getString("addTime");
                                    }
                                    Log.d("asdasd"," time   " + time);
                                    timeStr = DateUtil.getDateFromMillis(Long.parseLong(time));
                                }
                                Log.d("asdasd  ", timeStr + "  ");


                                switch (num) {
                                    case 1:
                                      /*  List<NewsBean> mrows_List = JSON.parseArray(arr.toJSONString(), NewsBean.class);
                                        tvSystem.setText(mrows_List.get(0).getTitle());*/
                                        tvSystem.setText(title);
                                        tvSystemTime.setText(timeStr);
                                        Boolean isRead = JSON.parseObject(arr.get(0).toString()).getBoolean("isRead");
                                        //isRead=false;
                                        if (!isRead) {
                                            ivRedNew.setVisibility(View.VISIBLE);
                                        } else {
                                            ivRedNew.setVisibility(View.GONE);
                                        }

                                       /* MainActivity activity = (MainActivity) getActivity();
                                        activity.setMainRedCircle(isRead);*/

                                        break;
                                    case 2:
                                        //  List<MediaBean> List1 = JSON.parseArray(arr.toJSONString(), MediaBean.class);
                                        // tvNotice.setText(List1.get(0).getTitle());
                                        tvNotice.setText(title);
                                        tvNoticeTime.setText(timeStr);
                                        break;
                                    case 3:
                                        // List<MediaBean> List2 = JSON.parseArray(arr.toJSONString(), MediaBean.class);
                                        //  tvMedia.setText(List2.get(0).getTitle());
                                        tvMedia.setText(title);
                                        tvMediaTime.setText(timeStr);
                                        break;
                                    case 4:
                                        //List<MediaBean> List3 = JSON.parseArray(arr.toJSONString(), MediaBean.class);
                                        // tvNews.setText(List3.get(0).getTitle());

                                        tvNews.setText(title);
                                        tvNewsTime.setText(timeStr);
                                        break;
                                    default:
                                        break;
                                }


                            }

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
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }


    private ArrayList<BaseFragment> preparePageInfo() {
        ArrayList<BaseFragment> list = new ArrayList<>();
        // 1.系统消息 2 活动消息 3.交易消息',
        list.add(NewsFragment.newInstance(2)); //2 活动消息
        list.add(NewsFragment.newInstance(3));
        list.add(NewsFragment.newInstance(1)); //1.系统消息
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
