package com.mcz.xhj.yz.dr_app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.AdAdapter;
import com.mcz.xhj.yz.dr_adapter.CoverFlowAdapter;
import com.mcz.xhj.yz.dr_adapter.CustomActivityAdapter;
import com.mcz.xhj.yz.dr_adapter.FriendAdapter;
import com.mcz.xhj.yz.dr_adapter.LogoAdapter;
import com.mcz.xhj.yz.dr_adapter.RollViewAdapter;
import com.mcz.xhj.yz.dr_app.find.ActivityOffActivity;
import com.mcz.xhj.yz.dr_app.find.InviteFriendsActivity;
import com.mcz.xhj.yz.dr_app_fragment.BaseFragment;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.Add_Bean;
import com.mcz.xhj.yz.dr_bean.BannerBean;
import com.mcz.xhj.yz.dr_bean.FriendBean;
import com.mcz.xhj.yz.dr_bean.InvestListBean;
import com.mcz.xhj.yz.dr_bean.LogoBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.CustomNoScrollListView;
import com.mcz.xhj.yz.dr_view.MarqueeView;
import com.mcz.xhj.yz.dr_view.MyScrollView;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.mcz.xhj.yz.dr_view.coverflow.CoverFlow;
import com.mcz.xhj.yz.dr_view.coverflow.PageItemClickListener;
import com.mcz.xhj.yz.dr_view.coverflow.PagerContainer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

public class HomeFrag extends BaseFragment implements OnClickListener {
    @BindView(R.id.toolbar)
    @Nullable
    View toolbar;
    @BindView(R.id.myScroll)
    MyScrollView myScrollView;
    @BindView(R.id.rl_title_find)
    RelativeLayout rl_title_find;
    @BindView(R.id.vp_ad)
    RollPagerView vp_ad;
    @BindView(R.id.ptr_home)
    PtrClassicFrameLayout ptr_home;

    /*<!--安全保障的布局-->*/
    /*@BindView(R.id.ll_left)
    LinearLayout ll_left;
    @BindView(R.id.ll_middle1)
    LinearLayout ll_middle1;
    @BindView(R.id.ll_middle2)
    LinearLayout ll_middle2;
    @BindView(R.id.ll_right)
    LinearLayout ll_right;
    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.iv_right)
    ImageView iv_right;*/
    @BindView(R.id.gridview)
    GridView gridview;

    /*---新手标和精品推荐---*/
    @BindView(R.id.ll_newhand)
    LinearLayout ll_newhand;
    @BindView(R.id.tv_title_newhand)
    TextView tv_title_newhand;
    @BindView(R.id.tv_rate_newhand)
    TextView tv_rate_newhand;
    @BindView(R.id.tv_deadline_newhand)
    TextView tv_deadline_newhand;

    @BindView(R.id.ll_quality)
    LinearLayout ll_quality;
    @BindView(R.id.tv_title_quality)
    TextView tv_title_quality;
    @BindView(R.id.tv_rate_quality)
    TextView tv_rate_quality;
    @BindView(R.id.tv_deadline_quality)
    TextView tv_deadline_quality;

    /*---进行中的活动---*/
    @BindView(R.id.ll_activities)
    LinearLayout ll_activities;
    @BindView(R.id.lv_activities_doing)
    CustomNoScrollListView lv_activities_doing;
    @BindView(R.id.tv_activity_off)
    TextView tv_activity_off;


    //进度
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.rl_notice)
    LinearLayout rl_notice;
    @BindView(R.id.ll_tips)
    LinearLayout group;//修改。。。
    @BindView(R.id.vp_overlap)
    ViewPager vpOverlap;
    @BindView(R.id.pager_container)
    PagerContainer pagerContainer;

    Unbinder unbinder;


    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private String activityUrl;
    private String activityImgUrl;
    private String indexUrl;
    WindowManager mWindowManager;
    WindowManager.LayoutParams wmParams;
    LinearLayout mFloatLayout;
    private Button mFloatView;

    private String Pic1UrlJump1 = null;
    private String Pic1UrlJump2 = null;
    private String pic1Url3;
    private String pic1UrlJump3;
    private List<FriendBean> lslbs =new ArrayList<>();
    private CustomActivityAdapter adapter_custom_activity;
    private List<LogoBean> logoBeanList;
    private LogoAdapter logoAdapter;
    private CoverFlowAdapter coverFlowAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (preferences.getBoolean("login", false)) {
        }

        LogUtils.i("HomeFrag--->onResume----->getHomeInfo()");


        if (LocalApplication.getInstance().getMainActivity().isLoginPsw) {
            LocalApplication.getInstance().getMainActivity().isLoginPsw = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static HomeFrag instance() {
        HomeFrag view = new HomeFrag();
        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_home;
    }

    @Override
    protected void initParams() {
        if (LocalApplication.getInstance().getMainActivity().hasNavigationBar) {
            toolbar.setVisibility(View.GONE);
        }

        getHomeInfo();
        getActivitiesDoing();

        myScrollView.smoothScrollTo(0, 0);
        ptr_home.disableWhenHorizontalMove(true);
        ptr_home.setResistance(2.0f);

        /*ll_left.setOnClickListener(this);
        ll_middle1.setOnClickListener(this);
        ll_middle2.setOnClickListener(this);
        ll_right.setOnClickListener(this);*/
        ll_newhand.setOnClickListener(this);
        ll_quality.setOnClickListener(this);
        tv_activity_off.setOnClickListener(this);

        ptr_home.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getHomeInfo();
                getActivitiesDoing();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        //公告
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                showAlertDialog("公告", "    " + textView.getText().toString(), new String[]{"确定"}, true, true, "notice");
            }
        });


        /*myScrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                //LogUtils.i("----->scrollY："+scrollY);
                if (scrollY >= 320) {
                    rl_title_find.setVisibility(View.VISIBLE);
                } else {
                    rl_title_find.setVisibility(View.GONE);
                }
            }
        });*/

        ////首页轮播图下面的四个 任务中心 安全保障 投资榜单 邀请好友 --> 图标
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (logoBeanList.size() != 0) {

                    LogUtils.e("四个图标+" + logoBeanList.get(position).getClickUrl());

                    if (logoBeanList.get(position).getClickUrl().equals("1")) {//邀请好友：1
                        if (!preferences.getBoolean("login", false)) {
                            startActivityForResult(new Intent(mContext, NewLoginActivity.class), 1);
                        } else {
                            LocalApplication.getInstance().getMainActivity().setCheckedFram(3);//返利
                        }
                    } else if (logoBeanList.get(position).getClickUrl().equals("2")) {//金服列表：2
                        LocalApplication.getInstance().getMainActivity().setCheckedFram(2);
                    } else if (logoBeanList.get(position).getClickUrl().equals("3")) {//登录：3
                        if (preferences.getBoolean("login", false)) {
                            ToastMaker.showLongToast("当前账号已登录请退出后重试");
                        } else {
                            mContext.startActivity(new Intent(mContext, NewLoginActivity.class));
                        }
                    } else if (logoBeanList.get(position).getClickUrl().equals("4")) {//注册：4
                        if (preferences.getBoolean("login", false)) {
                            ToastMaker.showLongToast("当前账号已登录请退出后重试");
                        } else {
                            startActivity(new Intent(mContext, NewRegisterActivity.class));
                        }
                    } else if (logoBeanList.get(position).getClickUrl().equals("5")) {//金服详情：5

                    } else if (logoBeanList.get(position).getClickUrl().equals("6")) {//我的账户：6
                        LocalApplication.getInstance().getMainActivity().setCheckedFram(4);
                    } else if (logoBeanList.get(position).getClickUrl().equals("7")) {//充值：7
                        memberSetting(1);
                    } else if (logoBeanList.get(position).getClickUrl().equals("8")) {//提现：8
                        memberSetting(2);
                    } else if (logoBeanList.get(position).getClickUrl().equals("9")) {//银行卡认证：9
                        memberSetting(3);
                    } else if (logoBeanList.get(position).getClickUrl().equals("10")) {//跳到微信：10
                        try {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setComponent(cmp);
                            startActivity(intent);

                            ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("gongzhonghao", "xhjlc2018");
                            cm.setPrimaryClip(clip);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(mContext, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
                        }
                    } else if (logoBeanList.get(position).getClickUrl().equals("11")) {//优惠券页面：11
                        if (!preferences.getBoolean("login", false)) {
                            startActivityForResult(new Intent(mContext, NewLoginActivity.class), 1);
                        } else {
                            startActivity(new Intent(mContext, ConponsAct.class));
                        }
                    } else if (logoBeanList.get(position).getClickUrl().equals("12")) {//任务中心：12
                        if (!preferences.getBoolean("login", false)) {
                            startActivityForResult(new Intent(mContext, NewLoginActivity.class), 1);
                        } else {
                            startActivity(new Intent(mContext, TaskCenterActivity.class));
                        }
                    } else if (logoBeanList.get(position).getClickUrl().equals("13")) {//积分榜单：13
                        if (!preferences.getBoolean("login", false)) {
                            LogUtils.e("login");
                            startActivityForResult(new Intent(mContext, NewLoginActivity.class), 1);
                        } else {
                            LogUtils.e("ScoreboardActivity");
                            startActivity(new Intent(mContext, ScoreboardActivity.class));
                        }
                    } else if (logoBeanList.get(position).getClickUrl().equals("14")) {//我的邀请：14
                        if (!preferences.getBoolean("login", false)) {
                            startActivityForResult(new Intent(mContext, NewLoginActivity.class), 1);
                        } else {
                            startActivity(new Intent(mContext, InviteFriendsActivity.class));
                        }
                    } else {//其他情况，跳转H5链接

                        startActivity(new Intent(mContext, WebViewActivity.class)
                                .putExtra("URL", logoBeanList.get(position).getClickUrl() + "?app=true")
                                .putExtra("TITLE", logoBeanList.get(position).getTitle())
                        );
                    }
                    //{"addTime":1522052172000,"clickUrl":"http://192.168.1.2/safe",
                    // "id":2,"imgUrl":"http://192.168.1.2/upload/productPic/2018-03/null/2018032602285557-21b7-403e-86b9-46c20666ca08.png",
                    // "orders":7,
                    // "retainFir":"http://localhost:8088/safe","status":0,"title":"安全保障"}

                }
            }
        });

        /**
         * 进行中的活动
         * */
        lv_activities_doing.setOnItemClickListener(new CustomNoScrollListView.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, Object obj, int position) {
                FriendBean friendBean = lslbs.get(position);
                if (friendBean.getStatus() == 1) {

                    if (friendBean.getAppUrl().contains("jumpTo=3")) { //邀请好友三重礼
                        LocalApplication.getInstance().getMainActivity().setCheckedFram(3);//返利
                    } else {
                        startActivityForResult(new Intent(mContext, WebViewActivity.class)
                                .putExtra("URL", lslbs.get(position).getAppUrl() + "?app=true")
                                .putExtra("TITLE", lslbs.get(position).getTitle())
                                .putExtra("AFID", lslbs.get(position).getId() + ""), 3
                        );
                    }
                }
            }
        });

        ViewPager pager = pagerContainer.getViewPager();

        coverFlowAdapter = new CoverFlowAdapter(mContext, lslbs);

        pager.setAdapter(coverFlowAdapter);
        //pagerContainer.setClipChildren(true);
        //pagerContainer.setOverlapEnabled(true);
        //
        pager.setOffscreenPageLimit(15);

        //ViewPager无限循环滑动
        vpOverlap.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }
            int currentPosition;

            @Override
            public void onPageScrollStateChanged(int state) {
                // ViewPager.SCROLL_STATE_IDLE 标识的状态是当前页面完全展现，并且没有动画正在进行中，如果不
                // 是此状态下执行 setCurrentItem 方法回在首位替换的时候会出现跳动！
                if (state != ViewPager.SCROLL_STATE_IDLE) return;

                // 当视图在第一个时，将页面号设置为图片的最后一张。
                if (currentPosition == 0) {
                    vpOverlap.setCurrentItem(lslbs.size() - 2, false);

                } else if (currentPosition == lslbs.size() - 1) {
                    // 当视图在最后一个是,将页面号设置为图片的第一张。
                    vpOverlap.setCurrentItem(1, false);
                }
            }
        });
       /* pagerContainer.setPageItemClickListener(new PageItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                LogUtils.e("position"+position);
                //Toast.makeText(NormalActivity.this,"position:" + position,Toast.LENGTH_SHORT).show();
                FriendBean friendBean = lslbs.get(position);
                if (friendBean.getStatus() == 1) {

                    if (friendBean.getAppUrl().contains("jumpTo=3")) { //邀请好友三重礼
                        LocalApplication.getInstance().getMainActivity().setCheckedFram(3);//返利
                    } else {
                        startActivityForResult(new Intent(mContext, WebViewActivity.class)
                                .putExtra("URL", lslbs.get(position).getAppUrl() + "?app=true")
                                .putExtra("TITLE", lslbs.get(position).getTitle())
                                .putExtra("AFID", lslbs.get(position).getId() + ""), 3
                        );
                    }
                }
            }
        });*/
       // pagerContainer.setOverlapEnabled(true);


        coverFlowAdapter.setItemClickListener(new CoverFlowAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LogUtils.e("position"+position);

                int newposition =  position % lslbs.size();

                if (newposition>=lslbs.size()){
                    newposition=lslbs.size()-1;
                }

                FriendBean friendBean = lslbs.get(newposition);

                if (friendBean.getStatus() == 1) {

                    if (friendBean.getAppUrl().contains("jumpTo=3")) { //邀请好友三重礼
                        LocalApplication.getInstance().getMainActivity().setCheckedFram(3);//返利
                    } else {
                        startActivityForResult(new Intent(mContext, WebViewActivity.class)
                                .putExtra("URL", lslbs.get(newposition).getAppUrl() + "?app=true")
                                .putExtra("TITLE", lslbs.get(newposition).getTitle())
                                .putExtra("AFID", lslbs.get(newposition).getId() + ""), 3
                        );
                    }
                }
            }
        });

        new CoverFlow.Builder()
                .with(pager)
                .scale(0.3f)
                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.pager_margin))
                .spaceSize(0f)
                .build();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            /*case R.id.ll_left://任务中心
                if (!preferences.getBoolean("login", false)) {
                    startActivityForResult(new Intent(mContext, NewLoginActivity.class), 1);
                } else {
                    startActivity(new Intent(mContext, TaskCenterActivity.class));
                }
                break;

            case R.id.ll_middle1://安全保障
                if (Pic1UrlJump1 != null) {
                    if (!Pic1UrlJump1.equalsIgnoreCase("")) {
                        startActivity(new Intent(mContext, WebViewActivity.class)
                                .putExtra("URL", bannerDownUrl1)
                                .putExtra("TITLE", "安全保障")
                        );
                    }
                }
                break;

            case R.id.ll_middle2://积分榜单
                if (!preferences.getBoolean("login", false)) {
                    startActivityForResult(new Intent(mContext, NewLoginActivity.class), 1);
                } else {
                    startActivity(new Intent(mContext, ScoreboardActivity.class));
                }
                break;

            case R.id.ll_right://邀请好友
                if (!preferences.getBoolean("login", false)) {
                    startActivityForResult(new Intent(mContext, NewLoginActivity.class), 1);
                } else {
                    startActivity(new Intent(mContext, InviteFriendsActivity.class));
                }
                break;*/
            case R.id.ll_newhand:
                if (newhandPid != null) {
                    //新手标详情页面
                    startActivity(new Intent(mContext, NewhandDetailActivity.class).putExtra("pid", newhandPid));
                }
                break;

            case R.id.ll_quality:
                if (preferPid != null) {
                    //普通标的详情页面
                    startActivity(new Intent(mContext, NewProductDetailActivity.class).putExtra("pid", preferPid));
                }
                break;

            case R.id.tv_activity_off:
                startActivity(new Intent(mContext, ActivityOffActivity.class));
                break;
            default:
                break;
        }

    }


    @Override
    public void onButtonClicked(Dialog dialog, int position, Object tag) {
        if (position == 0) {
            startActivity(new Intent(mContext, NewLoginActivity.class));
        }
    }

    private List<Add_Bean> lsAd = new ArrayList<>();
    private AdAdapter adapter;
    private String newhandPid;
    private String preferPid;
    private String activityPid;
    /*
     * 获取首页数据
     */
    List<BannerBean> lsad = new ArrayList<BannerBean>();
    private String pid = null;
    private InvestListBean newhandinfo = new InvestListBean();
    private InvestListBean preferinfo = new InvestListBean();
    private String bannerDownPic1, bannerDownTitle1, bannerDownDescribe1, bannerDownUrl1;//左边
    private String bannerDownPic2, bannerDownTitle2, bannerDownDescribe2, bannerDownUrl2;//右边
    private String iphoneDeatilUrl, iphoneName;

    private void getHomeInfo() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.HOMEINFO)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("version", UrlConfig.version)
                .addParam("toFrom", LocalApplication.getInstance().channelName)
                .addParam("channel", "2")
                .build().execute(new StringCallback() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(String response) {
                LogUtils.e("LF--->HomeFragment--->homeinfo：" + response);
                dismissDialog();
                ptr_home.refreshComplete();
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONArray arr = objmap.getJSONArray("banner");
                    JSONArray logoObj = objmap.getJSONArray("logoList");
                    JSONObject newobj = objmap.getJSONObject("newHand");
                    JSONObject preferredInvest = objmap.getJSONObject("preferredInvest");  //跳转投资列表
                    Pic1UrlJump1 = objmap.getString("videoUrl1");
                    Pic1UrlJump2 = objmap.getString("videoUrl2");
                    pic1Url3 = objmap.getString("videoImgUrl3");
                    pic1UrlJump3 = objmap.getString("videoUrl3");
                    bannerDownPic1 = objmap.getString("bannerDownPic1");
                    bannerDownTitle1 = objmap.getString("bannerDownTitle1");
                    bannerDownDescribe1 = objmap.getString("bannerDownDescribe1");
                    bannerDownUrl1 = objmap.getString("bannerDownUrl1");
                    bannerDownPic2 = objmap.getString("bannerDownPic2");
                    bannerDownTitle2 = objmap.getString("bannerDownTitle2");
                    bannerDownDescribe2 = objmap.getString("bannerDownDescribe2");
                    bannerDownUrl2 = objmap.getString("bannerDownUrl2");
                    if (pic1Url3 != null && pic1UrlJump3 != null) {
                        //弹出活动图片的对话框
                        showPopupWindowActImg(pic1Url3, pic1UrlJump3);
                    }

                    if (newobj != null && !newobj.equals("")) {
                        ll_newhand.setVisibility(View.VISIBLE);
                        newhandinfo = JSON.parseObject(newobj.toJSONString(), InvestListBean.class);
                        newhandPid = newhandinfo.getId();
                        tv_title_newhand.setText(newhandinfo.getFullName());
                        tv_rate_newhand.setText(stringCut.getNumKbs(newhandinfo.getRate() + Double.valueOf(newhandinfo.getActivityRate())) + "%");
                        tv_deadline_newhand.setText(newhandinfo.getDeadline() + "天");

                    } else {
                        ll_newhand.setVisibility(View.GONE);
                    }

                    if (preferredInvest != null) {
                        preferinfo = JSON.parseObject(preferredInvest.toJSONString(), InvestListBean.class);
                        tv_title_quality.setText(preferinfo.getFullName());
                        tv_rate_quality.setText(stringCut.getNumKbs(preferinfo.getMaxRate()) + "%");
                        tv_deadline_quality.setText(preferinfo.getMinDeadline() + "天");
                        preferPid = preferinfo.getId();
                        ll_quality.setVisibility(View.VISIBLE);
                    } else {
                        ll_quality.setVisibility(View.GONE);
                    }
                    //获取公告
                    Notice();
                    if (arr.size() != 0) {
                        lsad = JSON.parseArray(arr.toJSONString(), BannerBean.class);
                        //设置播放时间间隔
                        vp_ad.setPlayDelay(5000);
                        //设置透明度
                        vp_ad.setAnimationDurtion(500);
                        //设置适配器
                        vp_ad.setAdapter(new RollViewAdapter(mContext, lsad));
                        //轮播图点击
                        vp_ad.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                BannerBean bean = lsad.get(position);
                                if (bean.getLocation() == null || bean.getLocation().equalsIgnoreCase("")) {
                                    return;
                                }
                                if (bean.getLocation().contains("jumpTo=3")) { //邀请好友三重礼
                                    LocalApplication.getInstance().getMainActivity().setCheckedFram(3);//返利
                                } else if (bean.getTitle().indexOf("注册送礼") != -1) {
                                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                                            .putExtra("URL", bean.getLocation() + "&app=true")
                                            .putExtra("TITLE", bean.getTitle())
                                            .putExtra("HTM", "立即注册")
                                            .putExtra("PID", pid)
                                            .putExtra("BANNER", "banner")
                                    );
                                } else {
                                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                                            .putExtra("URL", bean.getLocation() + "&app=true")
                                            .putExtra("TITLE", bean.getTitle())
                                            .putExtra("PID", pid)
                                            .putExtra("BANNER", "banner")
                                    );
                                }


                                /*else if(bean.getTitle().indexOf("邀请")!=-1){
                                    mContext.startActivity(new Intent(mContext,WebViewActivity.class)
                                            .putExtra("URL", bean.getLocation()+"&app=true")
                                            .putExtra("TITLE", bean.getTitle())
                                            .putExtra("PID", pid)
                                            .putExtra("HTM", "立即邀请")
                                            .putExtra("BANNER", "banner"));
                                }*/

                            }
                        });
                    }

                    if (logoObj != null && logoObj.size() != 0) {
                        //首页轮播图下面的四个 任务中心 安全保障 投资榜单 邀请好友 --> 图标
                        logoBeanList = JSON.parseArray(logoObj.toJSONString(), LogoBean.class);
                        logoAdapter = new LogoAdapter(logoBeanList, mContext);
                        gridview.setAdapter(logoAdapter);
                    }
                } else {
                    if ("9998".equals(obj.getString("errorCode"))) {
                    }
                }
            }


            @Override
            public void onError(Call call, Exception e) {
                LogUtils.i("--->HomeFragment--->homeinfo获取失败！");
                dismissDialog();
                ptr_home.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });

    }

    private FriendAdapter adapter_activity;

    private void getActivitiesDoing() {
        OkHttpUtils.post()
                .url(UrlConfig.ACTIVITYLIST)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("status", "1")
                .addParams("type", "1")
                .addParams("pageOn", "1")
                .addParams("pageSize", "200")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                LogUtils.i("--->进行中的活动：" + result);
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONObject objinfo = objmap.getJSONObject("Page");
                    JSONArray objrows = objinfo.getJSONArray("rows");
                    List<FriendBean> friendBeans = JSON.parseArray(objrows.toJSONString(), FriendBean.class);
                    //lslbs = friendBeans;

                    if (friendBeans.size() > 0) {

                        lslbs.clear();
                        lslbs.add(friendBeans.get(friendBeans.size()-1));

                        lslbs.addAll(friendBeans);
                        lslbs.add(friendBeans.get(0));

                        LogUtils.e("lslbs"+lslbs.size());
                        ll_activities.setVisibility(View.VISIBLE);

                        coverFlowAdapter.notifyDataSetChanged();

                        pagerContainer.setListsize(lslbs.size());

                        //设置当前viewpager要显示第几个条目 显示在中间
                        //int item = Integer.MAX_VALUE /2 - (Integer.MAX_VALUE /2 % lslbs.size());
                        int item = 30;
                        vpOverlap.setCurrentItem(1);



                        if (adapter_activity == null) {
                            //adapter_activity = new FriendAdapter(mContext, lslbs);



                            adapter_custom_activity = new CustomActivityAdapter(mContext, lslbs);
                            lv_activities_doing.setAdapter(adapter_custom_activity);
                        } else {
                            //adapter_activity.onDateChange(lslbs);
                        }
                    } else {
                        ll_activities.setVisibility(View.GONE);
                    }
                } else if ("9998".equals(obj.getString("errorCode"))) {

                } else {
                    ToastMaker.showShortToast("服务器异常");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                LogUtils.i("--->进行中的活动：获取失败！");
                ToastMaker.showShortToast("请检查网络");
            }
        });

    }

    private String PID;

    private void showPopupWindowActImg(String picUrl, final String picUrlJump) {
        // 加载布局
        layout = LayoutInflater.from(mContext).inflate(R.layout.pop_img_act, null);
        // 找到布局的控件
        // 实例化popupWindow
        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        TextView tv_close = (TextView) layout.findViewById(R.id.peron_login_close);
        SimpleDraweeView iv_pic_act = (SimpleDraweeView) layout.findViewById(R.id.iv_pic_act);
        iv_pic_act.setImageURI(picUrl);
        // 控制键盘是否可以获得焦点
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        iv_pic_act.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

                if (picUrlJump.startsWith("jsmp") && picUrlJump.substring(7).equalsIgnoreCase("page=1")) {//邀请好友三重礼1
                    if (!preferences.getBoolean("login", false)) {
                        startActivityForResult(new Intent(mContext, NewLoginActivity.class), 1);
                    } else {
                        LocalApplication.getInstance().getMainActivity().setCheckedFram(3);//返利
                    }
                } else if (picUrlJump.startsWith("jsmp") && picUrlJump.substring(7).equalsIgnoreCase("page=2")) {//我要投资(投资悦)2
                    LocalApplication.getInstance().getMainActivity().setCheckedFram(2);
                } else if (picUrlJump.startsWith("jsmp") && picUrlJump.substring(7).equalsIgnoreCase("page=3")) {//登录3
                    if (preferences.getBoolean("login", false)) {
                        ToastMaker.showLongToast("当前账号已登录请退出后重试");
                    } else {
                        mContext.startActivity(new Intent(mContext, NewLoginActivity.class));
                    }
                } else if (picUrlJump.startsWith("jsmp") && picUrlJump.substring(7).equalsIgnoreCase("page=4")) {//注册4
                    if (preferences.getBoolean("login", false)) {
                        ToastMaker.showLongToast("当前账号已登录请退出后重试");
                    } else {
                        startActivity(new Intent(mContext, NewRegisterActivity.class));
                        //mContext.startActivity(new Intent(mContext, LoginQQPswAct.class).putExtra("phone", picUrlJump.substring(picUrlJump.indexOf("?") + 7, picUrlJump.length())));
                    }
                } else if (picUrlJump.startsWith("jsmp") && picUrlJump.substring(7, 13).equalsIgnoreCase("page=5")) {//金服详情5
                    PID = picUrlJump.substring(18);
                    LogUtils.i("--->跳转金服详情的PID：" + PID);
                    startActivity(new Intent(mContext, NewProductDetailActivity.class).putExtra("pid", PID));
                } else if (picUrlJump.startsWith("jsmp") && picUrlJump.substring(7).equalsIgnoreCase("page=6")) {//我的账户6
                    LocalApplication.getInstance().getMainActivity().setCheckedFram(4);

                } else if (picUrlJump.startsWith("jsmp") && picUrlJump.substring(7).equalsIgnoreCase("page=7")) {//充值7
                    memberSetting(1);
                } else if (picUrlJump.startsWith("jsmp") && picUrlJump.substring(7).equalsIgnoreCase("page=8")) {//提现8
                    memberSetting(2);
                } else if (picUrlJump.startsWith("jsmp") && picUrlJump.substring(7).equalsIgnoreCase("page=9")) {//银行卡认证9
                    memberSetting(3);
                } else if (picUrlJump.startsWith("jsmp") && picUrlJump.substring(7).equalsIgnoreCase("page=10")) {//跳转微信10
                    try {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setComponent(cmp);
                        startActivity(intent);

                        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("gongzhonghao", "xhjlc2018");
                        cm.setPrimaryClip(clip);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(mContext, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
                    }
                } else if (picUrlJump.startsWith("jsmp") && picUrlJump.substring(7).equalsIgnoreCase("page=11")) {//优惠券11
                    if (!preferences.getBoolean("login", false)) {
                        startActivityForResult(new Intent(mContext, NewLoginActivity.class), 1);
                    } else {
                        startActivity(new Intent(mContext, ConponsAct.class));
                    }
                } else if (picUrlJump.startsWith("jsmp") && picUrlJump.substring(7).equalsIgnoreCase("page=12")) {//任务中心12
                    if (!preferences.getBoolean("login", false)) {
                        startActivityForResult(new Intent(mContext, NewLoginActivity.class), 1);
                    } else {
                        startActivity(new Intent(mContext, TaskCenterActivity.class));
                    }
                } else if (picUrlJump.startsWith("jsmp") && picUrlJump.substring(7).equalsIgnoreCase("page=13")) {//积分榜单13
                    if (!preferences.getBoolean("login", false)) {
                        startActivityForResult(new Intent(mContext, NewLoginActivity.class), 1);
                    } else {
                        startActivity(new Intent(mContext, ScoreboardActivity.class));
                    }
                } else if (picUrlJump.startsWith("jsmp") && picUrlJump.substring(7).equalsIgnoreCase("page=14")) {//我的邀请: 14
                    if (!preferences.getBoolean("login", false)) {
                        startActivityForResult(new Intent(mContext, NewLoginActivity.class), 1);
                    } else {
                        startActivity(new Intent(mContext, InviteFriendsActivity.class));
                    }
                }
            }
        });
        tv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAsDropDown(tv_close);
    }


    //通知
    private void Notice() {
       // rl_notice.setVisibility(View.VISIBLE);
        OkHttpUtils
                .post()
                .url(UrlConfig.NOTICE)
                .addParam("limit", "3")
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("--->NOTICE " + response);
                        dismissDialog();
                        // TODO Auto-generated method stub
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            dismissDialog();
                            JSONObject objmap = obj.getJSONObject("map");
                            //JSONArray objnotice = objmap.getJSONArray("urgentNotice");
                            JSONArray objnotice = objmap.getJSONObject("page").getJSONArray("rows");;
                            if (objnotice != null) {
                                lsAd = JSON.parseArray(objnotice.toJSONString(), Add_Bean.class);
                                List<String> info = new ArrayList<>();
                                for (int i = 0; i < lsAd.size(); i++) {
                                    info.add(lsAd.get(i).getTitle().toString());
                                }
                                rl_notice.setVisibility(View.VISIBLE);
                                marqueeView.startWithList(info);
                                marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position, TextView textView) {
                                        /*startActivity(new Intent(mContext, WebViewActivity.class)
                                                .putExtra("URL", UrlConfig.WEBSITEAN + "?artiId=" + lsAd.get(position).getArti_id())
                                                .putExtra("TITLE", "平台公告"));*/
                                        startActivity(new Intent(mContext, WebViewActivity.class)
                                                .putExtra("URL", UrlConfig.WEBSITEAN + "?app=true&id=" + lsAd.get(position).getArtiId())
                                                .putExtra("TITLE", "平台公告"));
                                    }
                                });
                            } else {
                                rl_notice.setVisibility(View.GONE);
                            }
                        } else {
                            rl_notice.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
//				ToastMaker.showShortToast("请检查网络!");
                    }
                });

    }

    private boolean isUpdate = true;
    private View layout;
    private PopupWindow popupWindow;

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = LocalApplication.getInstance().getMainActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        LocalApplication.getInstance().getMainActivity().getWindow().setAttributes(lp);
    }


    private void memberSetting(final int flag) {
        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            return;
        }
        showWaitDialog("请稍后...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.MEMBERSETTING)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        dismissDialog();
                        // TODO Auto-generated method stub
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            String isRealName = map.getString("realVerify");
                            if ("1".equals(isRealName)) {
                                if (flag == 1) {
                                    startActivity(new Intent(mContext, CashInAct.class));
                                } else if (flag == 2) {
                                    startActivity(new Intent(mContext, CashOutAct.class));
                                } else if (flag == 3) {
                                    startActivity(new Intent(mContext, FourPartAct.class));
                                }
                            } else {
                                ToastMaker.showShortToast("您还未实名认证");
                                startActivity(new Intent(mContext, FourPartAct.class));
                            }
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(mContext).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统错误");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

}

