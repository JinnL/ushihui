package com.ekabao.oil.ui.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.CoverFlowAdapter;
import com.ekabao.oil.bean.CouponsBean;
import com.ekabao.oil.bean.FriendBean;
import com.ekabao.oil.bean.OilCardBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.InviteFriendsActivity;
import com.ekabao.oil.ui.activity.LoginActivity;
import com.ekabao.oil.ui.activity.MainActivity;
import com.ekabao.oil.ui.activity.MainOilActivity;
import com.ekabao.oil.ui.activity.NewsActivity;
import com.ekabao.oil.ui.activity.PhoneRechargeActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.activity.me.AddOilCardActivity;
import com.ekabao.oil.ui.activity.me.AssetsAnalysisActivity;
import com.ekabao.oil.ui.activity.me.CallCenterActivity;
import com.ekabao.oil.ui.activity.me.CashInActivity;
import com.ekabao.oil.ui.activity.me.CashOutActivity;
import com.ekabao.oil.ui.activity.me.CustomerServiceActivity;
import com.ekabao.oil.ui.activity.me.MallOrderActivity;
import com.ekabao.oil.ui.activity.me.MeAboutActivity;
import com.ekabao.oil.ui.activity.me.MeBalanceActivity;
import com.ekabao.oil.ui.activity.me.MeWelfareActivity;
import com.ekabao.oil.ui.activity.me.OilCardActivity;
import com.ekabao.oil.ui.activity.me.OilCardOrderActivity;
import com.ekabao.oil.ui.activity.me.OrderActivity;
import com.ekabao.oil.ui.activity.me.RealNameActivity;
import com.ekabao.oil.ui.activity.me.SettingActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.ui.view.coverflow.CoverFlow;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.show_Dialog_IsLogin;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/5/29.
 */

public class PersonFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.fillStatusBarView)
    View fillStatusBarView;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;
    @BindView(R.id.vp_overlap)
    ViewPager vpOverlap;
    @BindView(R.id.pager_container)
    LinearLayout pagerContainer;
    @BindView(R.id.ll_list_oilcard)
    LinearLayout llListOilcard;
    @BindView(R.id.ll_no_login)
    LinearLayout llNoLogin;
    @BindView(R.id.tv_oil_card_num)
    TextView tvOilCardNum;
    @BindView(R.id.ll_oilcard)
    LinearLayout llOilcard;
    @BindView(R.id.tv_coupons_num)
    TextView tvCouponsNum;
    @BindView(R.id.ll_coupons)
    LinearLayout llCoupons;
    @BindView(R.id.tv_balance_money)
    TextView tvBalanceMoney;
    @BindView(R.id.ll_balance)
    LinearLayout llBalance;
    @BindView(R.id.tv_total_assets)
    TextView tvTotalAssets;
    @BindView(R.id.ib_eye)
    CheckBox ibEye;
    @BindView(R.id.tv_profit)
    TextView tvProfit;
    @BindView(R.id.rl_assets)
    RelativeLayout rlAssets;
    @BindView(R.id.tv_oilcard_order)
    TextView tvOilcardOrder;
    @BindView(R.id.tv_phone_recharge)
    TextView tvPhoneRecharge;
    @BindView(R.id.tv_mall_order)
    TextView tvMallOrder;
    @BindView(R.id.tv_oilcard)
    TextView tvOilcard;
    @BindView(R.id.tv_safe)
    TextView tvSafe;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.img_setting)
    ImageView imgSetting;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.tv_call_phone)
    TextView tvCallPhone;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.tv_commission)
    TextView tvCommission;
    @BindView(R.id.ll_invite)
    LinearLayout llInvite;
    @BindView(R.id.bt_recharge)
    Button btRecharge;
    @BindView(R.id.bt_reflect)
    Button btReflect;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;


    private SharedPreferences preferences = LocalApplication.sharereferences;
    //去登录
    private static final int loginCode = 10111;
    //充值和体现
    private static final int Cash = 10911;
    private static final int Add = 10611;
    private static final int COUPONS = 10612;//优惠券


    private double amount = 0;
    private double shouyi; //收益
    private double balance; //账号余额

    private String telPhone; //客服电话

    private CoverFlowAdapter coverFlowAdapter;
    private List<FriendBean> lslbs = new ArrayList<>();
    private List<OilCardBean> list = new ArrayList<>(); //我的油卡
    View rootView;
//    private RollViewPersonAdapter adapter;

    @Override
    protected int getLayoutId() {
//        return R.layout.fragment_person;
        return 0;
    }


    @Override
    public void onResume() {

        super.onResume();


        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
           /* Glide.with(getActivity())
                    .load(R.drawable.icon_head_default)
                    .placeholder(R.drawable.icon_head_default)
                    .error(R.drawable.icon_head_default)
                    .into(imgAvatar);*/
            tvPhone.setText("登录/注册");

            tvTotalAssets.setText("****");
            tvProfit.setText("****");
            tvBalanceMoney.setText("****");

            tvCouponsNum.setText("0张");
            tvOilCardNum.setText("0张");

            //viewTopbarWhite.setVisibility(View.VISIBLE);
            llNoLogin.setVisibility(View.VISIBLE);
        } else {

            getOilCard();
            getData();
            //客服电话
            getPlatFormInfo();
        }

        // getData();


        MobclickAgent.onResume(mContext);


    }

    @Override
    protected void initParams() {

        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            tvPhone.setText("登录/注册");
            //viewTopbarWhite.setVisibility(View.VISIBLE);

            llNoLogin.setVisibility(View.VISIBLE);
            // TODO: 2018/11/15  去登录
            //btLogin.setImageDrawable(getResources().getDrawable(R.drawable.icon_person_login));

        }

        tvMessage.setOnClickListener(this);
        llBalance.setOnClickListener(this);
        rlAssets.setOnClickListener(this);

        llOilcard.setOnClickListener(this);
        llNoLogin.setOnClickListener(this);
        tvCommission.setText(Html.fromHtml("赚<font color='#EE4845'>15%</font>佣金"));

        // getData();

        //  getOilCard();
        //客服电话
        // getPlatFormInfo();

        refreshLayout.setPrimaryColors(new int[]{0xffffffff, 0xffEE4845});
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                getData();
                getOilCard();
                getPlatFormInfo();

                //是否有更多数据
                refreshLayout.setNoMoreData(false);
            }
        });


        coverFlowAdapter = new CoverFlowAdapter(mContext, list, 2);

        vpOverlap.setAdapter(coverFlowAdapter);
        //pagerContainer.setClipChildren(true);
        //pagerContainer.setOverlapEnabled(true);
        //
        vpOverlap.setOffscreenPageLimit(15);


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
        coverFlowAdapter.setItemClickListener(new CoverFlowAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onAddOilCardClick(View view, int position) {
                //  ToastMaker.showShortToast("onAddOilCardClick");
                String uid = preferences.getString("uid", "");
                if (uid.equalsIgnoreCase("")) {
                    /* MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");*/
                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {
                    //startActivity(new Intent(mContext, NoticeActivity.class));
                    startActivityForResult(new Intent(mContext, AddOilCardActivity.class)
                            .putExtra("uid", uid), Add);
                }

            }
        });

        coverFlowAdapter.notifyDataSetChanged();

        new CoverFlow.Builder()
                .with(vpOverlap)
                .scale(0.3f)
                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.pager_margin))
                .spaceSize(0f)
                .build();


        LogUtils.e((int) getResources().getDimension(R.dimen.dp_90) + rlSetting.getHeight() + "获取状态栏高度" + getStatusBar());

        /**
         * 设置view高度为statusbar的高度，并填充statusbar
         */
        // View mStatusBar = view.findViewById(R.id.fillStatusBarView);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) fillStatusBarView.getLayoutParams();
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = getStatusBar();
        fillStatusBarView.setLayoutParams(lp);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBar() {
        /**
         * 获取状态栏高度
         * */
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    @OnClick({R.id.ll_no_login, R.id.rl_setting,
            R.id.tv_message, R.id.tv_setting,
            R.id.ll_balance, R.id.rl_assets, R.id.tv_oilcard_order, R.id.tv_oilcard,
            R.id.tv_about, R.id.ll_coupons, R.id.ll_invite, R.id.tv_safe,
            R.id.tv_question, R.id.bt_recharge, R.id.bt_reflect,
            R.id.ll_oilcard, R.id.tv_mall_order, R.id.tv_call_phone, R.id.tv_phone_recharge})
    @Override
    public void onClick(View v) {
        String uid = preferences.getString("uid", "");
        switch (v.getId()) {
            case R.id.ll_no_login:
//            case R.id .bt_login: //添加油卡
                if (uid.equalsIgnoreCase("")) {
                    /* MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");*/
                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {
                    //startActivity(new Intent(mContext, NoticeActivity.class));
                    startActivityForResult(new Intent(mContext, AddOilCardActivity.class)
                            .putExtra("uid", uid), Add);
                }


                // Intent intent = new Intent(getActivity(), LoginActivity.class);
                //intent.putExtra("point", "personFrag");
                // startActivityForResult(intent, loginCode);

                break;
            case R.id.rl_setting://设置 //入个人资料页面
                if (uid.equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {
                    startActivityForResult(new Intent(mContext, SettingActivity.class), loginCode);
                }
//                startActivity(new Intent(mContext, MainOilActivity.class));
                break;

            case R.id.tv_setting: //设置页

                startActivity(new Intent(mContext, MeAboutActivity.class));
                /* if (uid.equalsIgnoreCase("")) {
                 *//* MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");*//*
                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {
                    //startActivity(new Intent(mContext, NoticeActivity.class));
                    startActivity(new Intent(mContext, NewsActivity.class));

                }*/
                break;
            case R.id.tv_message:
               /* Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("point", "personFrag");
                startActivityForResult(intent, loginCode);*/
                //startActivity(intent);
                // showPopupWindowLogin();

                if (uid.equalsIgnoreCase("")) {
                    /* MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");*/
                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {
                    //startActivity(new Intent(mContext, NoticeActivity.class));
                    startActivity(new Intent(mContext, NewsActivity.class));
                }
                break;
            case R.id.ll_balance://账号余额--->资金明细
                if (uid.equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    // startActivity(new Intent(mContext, LoginActivity.class));
                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {
                    // startActivity(new Intent(mContext, CapitaldetailsActivity.class));
                    startActivity(new Intent(mContext, MeBalanceActivity.class));

                }
                break;

            case R.id.rl_assets://总资产
                if (uid.equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, LoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, AssetsAnalysisActivity.class));
                }
                break;
            //case R.id.ll_oilcard:// 我的油卡
            case R.id.ll_oilcard:
                if (uid.equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    //startActivity(new Intent(mContext, LoginActivity.class));
                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {
                    startActivityForResult(new Intent(mContext, OilCardActivity.class), Add);
                }
                break;
            // case R.id.ll_invest:// 我的出借  我的订单
            case R.id.tv_oilcard_order: //油卡相关订单
                if (uid.equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    //startActivity(new Intent(mContext, LoginActivity.class));
                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {
                    // startActivity(new Intent(mContext, MyInvestmentActivity.class));
                    // OrderActivity
                    startActivity(new Intent(mContext, OrderActivity.class)
                            .putExtra("type", 1));
                  /*  startActivity(new Intent(mContext, OrderSelectActivity.class)
                            .putExtra("type", 1));*/
                }
                break;
            case R.id.tv_safe:
                startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.HTTPTITLE + "/activitycenter?app=true")
                        .putExtra("TITLE", "安全中心"));
                break;
            case R.id.tv_oilcard: //领卡订单
                if (uid.equalsIgnoreCase("")) {

                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {

                    startActivity(new Intent(mContext, OilCardOrderActivity.class)
                            .putExtra("type", 3));

                }
                break;
            case R.id.tv_phone_recharge: //手机充值
                if (uid.equalsIgnoreCase("")) {

                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {

                    startActivity(new Intent(mContext, OrderActivity.class)
                            .putExtra("type", 2));

                }
                break;

            case R.id.bt_recharge: //充值
                if (uid.equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    //startActivity(new Intent(mContext, LoginActivity.class));
                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {
                    memberSetting(1);
                }
                break;
            case R.id.bt_reflect: //提现
                if (uid.equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    // startActivity(new Intent(mContext, LoginActivity.class));
                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {
                    memberSetting(2);
                }
                break;

            //case R.id.ll_welfare:// 我的福利
            case R.id.ll_coupons: //优惠券
                if (uid.equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    //  startActivity(new Intent(mContext, LoginActivity.class));
                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {

                    startActivityForResult(new Intent(mContext, MeWelfareActivity.class)
                            .putExtra("flag", 3), COUPONS);
                }
                break;
            case R.id.ll_invite: //邀请好友
                if (uid.equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    // startActivity(new Intent(mContext, LoginActivity.class));
                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {
                    startActivity(new Intent(mContext, InviteFriendsActivity.class));
                }

                break;
            case R.id.tv_about: //关于我们
                startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.SAFE)
                        .putExtra("TITLE", "关于我们")
                        .putExtra("noWebChrome", "aboutMe"));
/*
                startActivity(new Intent(mContext, AboutActivity.class)
                        //  .putExtra("is_presell_plan", true)
                        // .putExtra("startDate", isSellingList.get(0).getStartDate())
                        // .putExtra("pid", newhandPid)
                        //.putExtra("ptype", "1")
                );*/

              /*  if (uid.equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, LoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, InviteFriendsActivity.class));
                }*/
                break;

            case R.id.tv_call_phone: //客服电话

                startActivity(new Intent(mContext, CustomerServiceActivity.class));
                // DialogMaker.showKufuPhoneDialog(mContext);
                break;
            case R.id.tv_question: //帮助反馈
              /*  if (uid.equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, LoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, InviteFriendsActivity.class));
                }*/
                startActivity(new Intent(mContext, CallCenterActivity.class));
                // startActivity(new Intent(mContext, MallOrderDetailsActivity.class));
                break;
          /*  case R.id.ll_weixin:
                //微信

                if (!isAvilible(getContext(), "com.tencent.mm")) {
                    ToastMaker.showShortToast("请先安装微信");
                    return;
                } else {
                    copy(mContext);
                    DialogMaker.showRedSureDialog(getContext(), "易卡宝公众号", "微信号已成功复制，请前往微信搜索并关注我们吧~",
                            "稍后再去", "去关注", new DialogMaker.DialogCallBack() {

                                @Override
                                public void onButtonClicked(Dialog dialog, int position, Object tag) {
                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, "a");
                                    sendIntent.setType("text/plain");

                                    ComponentName cmp = new ComponentName("com.tencent.mm",
                                            "com.tencent.mm.ui.LauncherUI");
                                    sendIntent.setAction(Intent.ACTION_MAIN);
                                    sendIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    sendIntent.setComponent(cmp);
                                    mContext.startActivity(sendIntent);
                                }

                                @Override
                                public void onCancelDialog(Dialog dialog, Object tag) {

                                }
                            }, "");

                }
                break;*/
            case R.id.tv_mall_order: //手机充值订单 1：油卡 2：手机 3：直购
                if (uid.equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    //startActivity(new Intent(mContext, LoginActivity.class));
                    startActivityForResult(new Intent(mContext, LoginActivity.class), loginCode);
                } else {
                    // startActivity(new Intent(mContext, MyInvestmentActivity.class));

                    //OrderActivity
                    startActivity(new Intent(mContext, MallOrderActivity.class)
                            .putExtra("type", 2)
                    );


                }

                break;


            default:

        }
    }


    /**
     * 用户信息
     */
    private void getData() {
        String uid = preferences.getString("uid", "");

        if (TextUtils.isEmpty(uid)) {

            RefreshState state = refreshLayout.getState();
            if (state == RefreshState.Refreshing) {
                refreshLayout.finishRefresh();
            }
           /* tv_shouyi.setText("--");
            tv_jifen.setText("--");
            tv_amount.setText("--");
            tv_balance.setText("--");
            title_centertextview.setText("易卡宝");
            tv_amount_holdingInvest.setText("0");
            tv_amount_redeem.setText("0");
            tv_amount_conpons.setText("0张");
            tv_tiyan.setText("0元");
            tv_amount_jifen.setText("0积分");
            tv_amount_shouyi.setText("0元");
            ptr_person_new.refreshComplete();*/

            // TODO: 2018/7/2  登录弹框 getInstance().
            LogUtils.i("--->登录弹框 isExit：" + MainActivity.isExit);
            if (uid.equalsIgnoreCase("") && LocalApplication.getInstance().getMainActivity().isExit) {
                //showPopupWindowLogin();
            }

            return;
        }
        OkHttpUtils.post()
                .url(UrlConfig.PERSONINFO)
                .addParams("uid", uid)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                LogUtils.e("我的里面的信息--->new_person_frag_Info " + result);

                if (refreshLayout != null) {
                    RefreshState state = refreshLayout.getState();
                    if (state == RefreshState.Refreshing) {
                        refreshLayout.finishRefresh();
                    }
                }

                /* if (ptr_person_new != null) {
                    ptr_person_new.refreshComplete();
                }*/

                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    Integer realname = objmap.getInteger("realVerify");//是否实名
                    String sex = objmap.getString("sex");
                    String unTiedCardTitle = objmap.getString("unTiedCardTitle");
                    String realName = objmap.getString("realName");
                    String mobilePhone = objmap.getString("mobilePhone");

//                    if(tvPhone==null){
//                       return;
//                    }
                    //objmap.containsKey("realName")
                    if (TextUtils.isEmpty(realName)) {
                        tvPhone.setText(mobilePhone);

                        // imgAvatar.setText(StringCut.phoneCut(preferences.getString("phone", "")));
                    } else {

                     /*   Glide.with(getActivity())
                                .load(preferences.getString("avatar_url", ""))
                                // .placeholder(R.drawable.icon_head_default)
                                // .error(R.drawable.icon_head_default)
                                .into(imgAvatar);

                        LogUtils.e("avatar_url" + preferences.getString("avatar_url", ""));
                        */
                        tvPhone.setText(realName);
                        //title_centertextview.setText(realName);
                    }

                    String avatar_url = preferences.getString("avatar_url", "");


//                    if (!avatar_url.equals("")) {
//                        LogUtils.e("头像" + avatar_url);
//
//                        Glide.with(getActivity())
//                                .load(avatar_url)
//                                //.placeholder(R.drawable.icon_person_default_login)
//                                .error(R.drawable.icon_person_default_login)
//                                .into(imgAvatar);
//                    } else {
//                        LogUtils.e("头像222");
//                        Glide.with(getActivity())
//                                .load(R.drawable.icon_person_default)
//                                //.placeholder(R.drawable.icon_person_default_login)
//                                .error(R.drawable.icon_person_default_login)
//                                .into(imgAvatar);
//                    }

                    balance = objmap.getDoubleValue("balance");
                    shouyi = objmap.getDoubleValue("accumulatedIncome");
                    double free = objmap.getDoubleValue("free");//冻结金额
                    double daishou = objmap.getDoubleValue("wprincipal");//代收本金
                    double winterest = objmap.getDoubleValue("winterest");//代收利息
                    double myPoints = objmap.getDoubleValue("myPoints");//积分
                    double availableExperience = objmap.getDoubleValue("availableExperience");//体验金
                    Integer unuseConponsNum = objmap.getInteger("unUseFavourable");
                    Integer tender = objmap.getInteger("tender");
                    Integer tenderFinished = objmap.getInteger("tenderFinished");

                    amount = balance + free + daishou + winterest;
                    tvTotalAssets.setText(StringCut.getNumKb(amount));
                    tvProfit.setText(StringCut.getNumKb(shouyi));

                    tvBalanceMoney.setText(StringCut.getNumKb(balance));

                } else if ("9998".equals(obj.getString("errorCode"))) {
                    new show_Dialog_IsLogin(mContext).show_Is_Login();

                } else {
                    ToastMaker.showShortToast("服务器异常");
                }


                ibEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            tvTotalAssets.setText("****");
                            tvProfit.setText("****");
                            tvBalanceMoney.setText("****");
                        } else {

                            tvTotalAssets.setText(StringCut.getNumKb(amount));
                            tvProfit.setText(StringCut.getNumKb(shouyi));
                            tvBalanceMoney.setText(StringCut.getNumKb(balance));

                        }

                    }
                });

            }

            @Override
            public void onError(Call call, Exception e) {
               /* if (ptr_person_new != null) {
                    ptr_person_new.refreshComplete();
                }*/
                refreshLayout.finishRefresh();
                ToastMaker.showShortToast("请检查网络");
            }
        });

    }

    /**
     * 我的油卡
     */
    private void getOilCard() {

        // LogUtils.e("我的油卡"+uid);

        String uid = preferences.getString("uid", "");

        if (uid.equalsIgnoreCase("")) {
            RefreshState state = refreshLayout.getState();
            if (state == RefreshState.Refreshing) {
                refreshLayout.finishRefresh();
            }
            llListOilcard.setVisibility(View.GONE);
            llNoLogin.setVisibility(View.VISIBLE);
            list.clear();

            // coverFlowAdapter.notifyDataSetChanged();
            return;
        }
        //优惠券
        getCoupons();

        OkHttpUtils.post().url(UrlConfig.myFuelCard)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("--->我的油卡：" + response);
                        dismissDialog();

                        RefreshState state = refreshLayout.getState();
                        if (state == RefreshState.Refreshing) {
                            refreshLayout.finishRefresh();
                        }

                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray arr = map.getJSONArray("myFuelCardList");
                            //  LogUtils.e("我的油卡" + arr.getString(0));
                          /*  if (arr.isEmpty()) {
                                return;
                            }*/

                            List<OilCardBean> oilCardBeans = JSON.parseArray(arr.toJSONString(), OilCardBean.class);


                           /* JSONObject map = obj.getJSONObject("map");
                            JSONArray arr = map.getJSONArray("list");

                            List<OilCardPackageBean> mrows_List = JSON.parseArray(arr.toJSONString(), OilCardPackageBean.class);*/


                            LogUtils.e("我的油卡" + oilCardBeans.size());

                            if (oilCardBeans.size() > 0) {

                                tvOilCardNum.setText(oilCardBeans.size() + "张");


                                llListOilcard.setVisibility(View.VISIBLE);
                                list.clear();

                                list.addAll(oilCardBeans);

                                list.add(new OilCardBean(0, "", 0, 0, 0, 99, 0));

                                //  oilCardBean = oillist.get(0);
                                //  coverFlowAdapter.notifyDataSetChanged();

                                // TODO: 2019/3/14  
                                coverFlowAdapter.notifyDataSetChanged();

                                llNoLogin.setVisibility(View.GONE);
                                //1.0版本的,移到下面了
                                // llOilcard.setVisibility(View.VISIBLE);
                                // meEtouRechargeAdapter.notifyDataSetChanged();
                            } else {

                                list.clear();

//                                btLogin.setImageDrawable(getResources().getDrawable(R.drawable.icon_person_add));

                                llListOilcard.setVisibility(View.GONE);
                                llNoLogin.setVisibility(View.VISIBLE);
                                // llOilcard.setVisibility(View.GONE);
                                // tv_footer.setText("全部加载完毕");
                                // footerlayout.setVisibility(View.VISIBLE);
                                // pb.setVisibility(View.GONE);
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

                        llNoLogin.setVisibility(View.VISIBLE);

                        RefreshState state = refreshLayout.getState();
                        if (state == RefreshState.Refreshing) {
                            refreshLayout.finishRefresh();
                        }
                    }
                });

    }

    /**
     * 优惠券
     */
    private void getCoupons() {

        OkHttpUtils.post()
                .url(UrlConfig.CONPONSUNUSE)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("status", "0")
                .addParams("type", "0")   //0 体验金
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                LogUtils.e(preferences.getString("uid", "") + "优惠券" + result);
                //ptr_conponsunuse.refreshComplete();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");


                    if (result.contains("list")) {

                        JSONArray objrows = objmap.getJSONArray("list");
                        List<CouponsBean> couponsBeans = JSON.parseArray(objrows.toJSONString(), CouponsBean.class);

                        LogUtils.e(objrows.size() + "优惠券" + couponsBeans.size());

                        if (couponsBeans.size() > 0) {

                            tvCouponsNum.setText(couponsBeans.size() + "张");
                        } else {
                            tvCouponsNum.setText("0张");
                        }
                    } else {
                        tvCouponsNum.setText("0张");
                    }
                    //版本兼容
//					for (int i = 0; i < lslbs.size(); i++) {
//						if(lslbs.get(i).getType()!=4&lslbs.get(i).getType()!=1&lslbs.get(i).getType()!=2&lslbs.get(i).getType()!=3){
//							lslbs.remove(i);
//							i--;
//						}
//					}

                } else if ("9998".equals(obj.getString("errorCode"))) {
                    //getActivity().finish();
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

    /**
     * 充值的时候 获取用户银行卡信息
     */
    private void memberSetting(final int flag) {
        showWaitDialog("加载中...", true, "");
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

                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean("success")) {
                            JSONObject map = obj.getJSONObject("map");
                            String isRealName = map.getString("realVerify");
                            String ispwd = map.getString("tpwdFlag");
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("tpwdFlag", ispwd);
                            editor.commit();
                            //已认证
                            if ("1".equals(isRealName)) {
                                //快速充值
                                if (flag == 1) {
                                    MobclickAgent.onEvent(mContext, UrlConfig.point + 42 + "");
                                    startActivityForResult(new Intent(mContext, CashInActivity.class), Cash);

                                } else if (flag == 2) {
                                    //提现
                                    startActivityForResult(new Intent(mContext, CashOutActivity.class), Cash);
                                }
                            } else {//未认证
                                if (flag == 1) {
                                    MobclickAgent.onEvent(mContext, UrlConfig.point + 39 + "");
                                } else if (flag == 2) {
                                    MobclickAgent.onEvent(mContext, UrlConfig.point + 40 + "");
                                }
                                startActivity(new Intent(mContext, RealNameActivity.class));
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

    /**
     * 获取客服电话
     */
    private void getPlatFormInfo() {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("telPhone", "400-665-8575");
        editor.commit();

        //tvCallPhone.setText(Html.fromHtml("客服电话: <font color='#379DEA'>" + "400-665-8575" + "</font>"));


      /*  OkHttpUtils
                .post()
                .url(UrlConfig.GETPLATFORMINFO)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("---》客服电话" + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean("success")) {
                            JSONObject map = obj.getJSONObject("map");
                            telPhone = map.getString("platForm");
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("telPhone", telPhone);
                            editor.commit();
                            if (!TextUtils.isEmpty(telPhone)) {
                                tvCallPhone.setText(Html.fromHtml("客服电话: <font color='#379DEA'>" + telPhone + "</font>"));
                            }

                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        ToastMaker.showShortToast("请检查网络");
                    }
                });*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LogUtils.e(requestCode + "登录成功onActivityResult" + resultCode);


        if (requestCode == loginCode && resultCode == RESULT_OK) {
            LogUtils.e("登录成功");

        } else if (requestCode == loginCode && resultCode == 3) { //注册

            showPopupWindowLogin();

        } else if (requestCode == COUPONS) { //优惠券

            // type=0 ;1油卡套餐  2 油卡直冲 4 话费套餐 5 话费直冲  是从项目立即支付里面过来的

            MainActivity activity = (MainActivity) getActivity();
            SharedPreferences.Editor edit = preferences.edit();
            edit.putInt("fragment_type", resultCode);
            edit.commit();


            switch (resultCode) {
                case 1:

                    // activity.switchFragment(1);
                    // activity.resetTabState();
                    activity.setCheckedFram(2);
                    break;
                case 2:
                    //activity.switchFragment(1);
                    // activity.resetTabState();

                    activity.setCheckedFram(2);
                    break;
                case 4:
                    mContext.startActivity(new Intent(mContext, PhoneRechargeActivity.class));
                    break;
                case 5:
                    mContext.startActivity(new Intent(mContext, PhoneRechargeActivity.class));
                    break;
                case 7:
                case 8:
                    activity.setCheckedFram(3);

                default:
                    //  activity.switchFragment(1);
                    //  activity.resetTabState();
                    break;

            }


        }

        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
//            imgAvatar.setImageResource(R.drawable.icon_person_default);
            tvPhone.setText("登录/注册");
            //viewTopbarWhite.setVisibility(View.VISIBLE);
            llNoLogin.setVisibility(View.VISIBLE);
//            btLogin.setImageDrawable(getResources().getDrawable(R.drawable.icon_person_login));

            tvCouponsNum.setText("0张");
            tvOilCardNum.setText("0张");

        } else {
            //llNoLogin.setVisibility(View.GONE);
            llNoLogin.setVisibility(View.GONE);
        }

        if (requestCode != COUPONS) {
            getData();
            getPlatFormInfo();
            getOilCard();
        }

    }

    public static PersonFragment instance() {
        PersonFragment view = new PersonFragment();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_person, null);
            // 初始化View,添加Fragment
            // ...
        }
        unbinder = ButterKnife.bind(this, rootView);
        initParams();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 恭喜注册成功
     */
    public void showPopupWindowLogin() {
        // 加载布局
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.pop_person_register_success, null);
        // 找到布局的控件
        // 实例化popupWindow
        final PopupWindow popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        TextView tvContent = (TextView) (layout).findViewById(R.id.tv_content);

      /*  tvContent.setText(Html.fromHtml("您已获得<font color='#EE4845'>888</font>元新手红包<br>" +
                "以及<font color='#EE4845'>88888</font>元体验金<br>" +
                "请在 <font color='#333333'>我的-我的福利</font> 中查看"));*/

        // 控制键盘是否可以获得焦点
        //popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setFocusable(true);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //popupWindow.dismiss();
                return false;
            }
        });
        // 设置popupWindow弹出窗体的背景
        setBackgroundAlpha(0.4f);//设置屏幕透明度

//		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
            }
        });
        ImageView imageView7 = (ImageView) (layout).findViewById(R.id.iv_regist);
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // startActivity(new Intent(MainActivity.this, NewLoginActivity.class).putExtra("point", "personFrag"));
                popupWindow.dismiss();
            }
        });
      /*  ImageView iv_exit = (ImageView) (layout).findViewById(R.id.iv_exit);
        iv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              *//*  startActivity(new Intent(MainActivity.this, NewLoginActivity.class).putExtra("point", "personFrag"));
                popupWindow.dismiss();*//*
            }
        });*/
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 判断手机里有没有这个应用
     *
     * @param context
     * @param packageName
     * @return
     */
    public boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName
                    .equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    public void copy(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context

                .getSystemService(Context.CLIPBOARD_SERVICE);

        cmb.setText(getResources().getString(R.string.weixin));

    }

}
