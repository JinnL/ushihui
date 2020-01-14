package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_view.DialogMaker;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;
import com.mcz.xhj.yz.dr.psw_style_util.TradePwdPopUtils;
import com.mcz.xhj.yz.dr_adapter.NewConponsAdapter;
import com.mcz.xhj.yz.dr_adapter.SuccessPayAdapter;
import com.mcz.xhj.yz.dr_app.me.InvestmentDetails2Activity;
import com.mcz.xhj.yz.dr_app_fragment.CommonProblemFragment;
import com.mcz.xhj.yz.dr_app_fragment.Frag_ProDetails_record;
import com.mcz.xhj.yz.dr_app_fragment.InvestRankingListFragment;
import com.mcz.xhj.yz.dr_app_fragment.ProductIntroduceFragment;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.BankName_Pic;
import com.mcz.xhj.yz.dr_bean.ConponsBean;
import com.mcz.xhj.yz.dr_bean.NoticeBean;
import com.mcz.xhj.yz.dr_bean.ProIntroduceBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.SecurityUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.HistogramView;
import com.mcz.xhj.yz.dr_view.MarqueeView;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.mcz.xhj.yz.dr_view.waveview.WaveView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import cn.bleu.widget.slidedetails.SlideDetailsLayout;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

/**
 * Created by zhulang on 2017/8/15.
 * 描述：2.0版 普通标的详情页面
 */

public class NewProductDetailActivity extends BaseActivity implements ISlideCallback, View.OnClickListener {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_righttextview)
    TextView titleRighttextview;
    @BindView(R.id.tv_prodetail_rate)
    TextView tv_prodetail_rate;
    @BindView(R.id.tv_prodetail_rate_activity)
    TextView tv_prodetail_rate_activity;
    @BindView(R.id.tv_prodetail_deadline)
    TextView tv_prodetail_deadline;
    @BindView(R.id.tv_prodetail_repaytype)
    TextView tv_prodetail_repaytype;
    @BindView(R.id.tv_prodetail_leastaAmount)
    TextView tv_prodetail_leastaAmount;
    @BindView(R.id.tv_prodetail_balance)
    TextView tv_prodetail_balance;
    @BindView(R.id.tv_prodetail_total)
    TextView tv_prodetail_total;
    @BindView(R.id.wave_view)
    WaveView wave_view;
    @BindView(R.id.histogramView)
    HistogramView histogramView;
    /*各种标签tag*/
    @BindView(R.id.ll_tag_row1)
    LinearLayout ll_tag_row1;
    @BindView(R.id.tv_prodetail_red)
    TextView tv_prodetail_red;
    @BindView(R.id.tv_prodetail_interest)
    TextView tv_prodetail_interest;
    @BindView(R.id.tv_prodetail_double)
    TextView tv_prodetail_double;
    @BindView(R.id.tv_prodetail_hot)
    TextView tv_prodetail_hot;
    @BindView(R.id.tv_prodetail_saowei)
    TextView tv_prodetail_saowei;
    @BindView(R.id.ll_tag_row2)
    LinearLayout ll_tag_row2;
    @BindView(R.id.tv_tag1)
    TextView tv_tag1;
    @BindView(R.id.tv_tag2)
    TextView tv_tag2;
    @BindView(R.id.tv_tag3)
    TextView tv_tag3;
    @BindView(R.id.tv_tag4)
    TextView tv_tag4;

    @BindView(R.id.ll_notice)
    LinearLayout ll_notice;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;

    @BindView(R.id.tv_day_invest)
    TextView tv_day_invest;
    @BindView(R.id.tv_day_establish)
    TextView tv_day_establish;
    @BindView(R.id.tv_day_returned)
    TextView tv_day_returned;
    @BindView(R.id.ptr_pro_detail)
    PtrClassicFrameLayout ptrProDetail;
    @BindView(R.id.slidedetails_front)
    LinearLayout slidedetails_front;
    @BindView(R.id.title_leftimageview_add)
    ImageView titleLeftimageviewAdd;
    @BindView(R.id.title_centertextview_add)
    TextView titleCentertextviewAdd;
    @BindView(R.id.title_righttextview_add)
    TextView titleRighttextviewAdd;
    @BindView(R.id.pro_indicator)
    TabPageIndicator pro_indicator;
    @BindView(R.id.vp_pro)
    ViewPager vp_pro;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.slidedetails)
    SlideDetailsLayout slidedetails;
    @BindView(R.id.tv_wan_shouyi_normal)
    TextView tvWanShouyi;
    @BindView(R.id.ll_limit)
    LinearLayout llLimit;
    @BindView(R.id.bt_prodetail_invest)
    TextView btProdetailInvest;
    @BindView(R.id.rl_pro_new)
    RelativeLayout rlProNew;
    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;
    @BindView(R.id.ll_presell_timer)
    LinearLayout ll_presell_timer;
    @BindView(R.id.tvtime1)
    TextView tvtime1;
    @BindView(R.id.tvtime2)
    TextView tvtime2;
    @BindView(R.id.tvtime3)
    TextView tvtime3;


    private String[] tab;//标题
    private TabFragPA tabPA;
    private Fragment frag;
    private Fragment frag1;
    private Fragment frag2;
    private String pid;
    private String ptype = "2";
    public Bundle bundle;
    private SharedPreferences preferences;
    Long lastClick = 0l;//防重复点击计时

    private PopupWindow popupWindow;
    private PopupWindow popupWindow2;
    private PopupWindow popupWindowSuccess;
    private View layout;
    private View layout2;
    private View layoutSuccess;

    private String balance;//当前登录用户余额
    private boolean isOldUser;//是否是老用户
    private boolean isShowLabel;//是否显示激活体验金startTime
    private int tpwdFlag;//是否设置交易密码 0=未设置，1=已设置
    private int interestType;//计息方式
    private String startTime;//计息日期
    private String sysDate;//服务器时间
    private String establishTime;//成立日期
    private String endTime;//回款日期
    private String endDate;//募集结束日期
    private String repayType = "";//还款方式
    private String tag;//活动标签
    private Double activityRate;
    private Double rate;
    private String amount;
    private String increasAmount;
    private String surplusAmount;
    private String leastaAmount;
    private String maxAmount;
    private String deadline;
    private String pert;
    private String fullName;//产品全称
    private String principleH5;//产品原理图

    private EditText et_input_amount;//pop1输入金额框
    private TextView tv_saowei;
    private TextView tv_jifen;
    private TextView tv_prodetail_income_add;
    private TextView tv_prodetail_income;
    private TextView tv_prodetail_income_name;
    private TextView tv_num_conpons;
    private TextView tv_prodetail_conpons;
    private TextView bt_prodetail_pop_ok;
    private double shouyi_wanyuan;
    private Double rate_expect;
    private Double exRate_expect;
    private Boolean isRoundOff = false;//是否显示尾单
    private String roundOffMoney;//尾单奖励
    private JSONArray couponList;
    private JSONArray listFavourable;
    private ArrayList<ConponsBean> mlslb2 = new ArrayList<>(); // 红包列表
    private boolean isFirstGet = true;
    private boolean isFirstGetRate = true;
    double enableAmount = 0, amountRed = 0, raisedRate = 0, multiple = 1;//优惠券信息，依次是：启用金额，红包，加息，翻倍
    Double shouyi;
    Double shouyi_add;
    private String introduce;// 产品介绍
    private int resultNum = 0;
    private String fid = ""; //优惠券ID
    Pattern pcode = Pattern.compile("^[0-9]{4}$");
    private EditText et_sms_code;
    /**
     * nothing用于判断使用了那种优惠券
     * amount--返现券
     * multiple--翻倍券
     * raisedRate--加息券
     */
    private String intent_fid, atid;
    private String intent_amount;
    private String intent_enableAmount;

    private String experienceId = "";//选择体验金对应的id

    /*
    * 底部提示栏
    * */
    private LinearLayout ll_describe_conpons;
    private LinearLayout ll_bottom_tip1;
    private LinearLayout ll_bottom_tip2;
    private LinearLayout ll_bottom_tip3;


    private Long expireDate;//还款日期
    private Double interest;//预计收益
    private TextView tv_getYzm;//60s倒计时
    private String distributionId;//是展示排行榜还是投资记录
    private List<String> textStringList = new ArrayList<>();
    private String bankid;
    private BankName_Pic bp;
    private String bankName;
    private String bankNum;
    private Integer singleQuotaJYT;
    private Integer dayQuotaJYT;
    private List<ProIntroduceBean> proIntroduceList;//产品介绍
    private List<NoticeBean> noticeList;
    /*体验金*/
    private LinearLayout ll_describe_tiyanjin;
    private TextView tv_num_tiyanjin;
    private TextView tv_prodetail_tiyanjin;
    private List<ConponsBean> listTiYanJin;
    private List<ConponsBean> listConpons;
    private double enableAmount_tiYanJin;//体验金激活金额
    private boolean is_presell_plan;//是否是预售标

    private boolean isChooseCoupon = false; //是否选择了优惠券

    @Override
    protected int getLayoutId() {
        return R.layout.act_detail_product_new;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getDate();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
        if (handler_timer != null) {
            LogUtils.i("标详情----->onPause()");
            handler_timer.removeCallbacks(runnable);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (handler_timer != null) {
            LogUtils.i("标详情----->onStop()");
            handler_timer.removeCallbacks(runnable);
        }
    }

    @Override
    protected synchronized void onDestroy() {
        super.onDestroy();
        //LogUtils.i("标详情----->onDestroy()");

    }

    private long time_timer = 0l;
    private Handler handler_timer = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time_timer--;
            String formatLongToTimeStr = formatLongToTimeStr(time_timer);
            String[] split = formatLongToTimeStr.split("：");
            for (int i = 0; i < split.length; i++) {
                if (i == 0) {
                    tvtime1.setText(split[0] + "时");
                }
                if (i == 1) {
                    tvtime2.setText(split[1] + "分");
                }
                if (i == 2) {
                    tvtime3.setText(split[2] + "秒");
                }

            }
            if (time_timer > 0) {
                handler_timer.postDelayed(this, 1000);
            } else {
                ll_presell_timer.setVisibility(View.GONE);
                btProdetailInvest.setVisibility(View.VISIBLE);
                btProdetailInvest.setEnabled(true);
                btProdetailInvest.setClickable(true);
            }
        }
    };

    public String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue();
        if (second > 60) {
            minute = second / 60;         //取整
            second = second % 60;         //取余
        }

        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = hour + "：" + minute + "：" + second;
        return strtime;

    }

    @Override
    protected void initParams() {
        slidedetails.setOnSlideDetailsListener(new SlideDetailsLayout.OnSlideDetailsListener() {
            @Override
            public void onStatucChanged(SlideDetailsLayout.Status status) {
                if (status == SlideDetailsLayout.Status.OPEN) {
                    LogUtils.i("----->Status.OPEN");
                    //rl_bottom.setVisibility(View.GONE);

                } else if (status == SlideDetailsLayout.Status.CLOSE) {
                    LogUtils.i("----->Status.CLOSE");
                    rl_bottom.setVisibility(View.VISIBLE);
                }
            }
        });

        if (checkDeviceHasNavigationBar(NewProductDetailActivity.this)) {
            int virtualBarHeigh = getVirtualBarHeigh();
            boolean b = checkDeviceHasNavigationBar();
            LogUtils.i("虚拟按键的高度：virtualBarHeigh=" + virtualBarHeigh + "/" + b);
            rlProNew.setPadding(0, 0, 0, virtualBarHeigh);
        }
        ptrProDetail.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getDate();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        preferences = LocalApplication.getInstance().sharereferences;
        pid = getIntent().getStringExtra("pid");
        is_presell_plan = getIntent().getBooleanExtra("is_presell_plan", false);
        bundle = new Bundle();
        bundle.putString("pid", pid);
        bundle.putString("uid", preferences.getString("uid", ""));
        if (TextUtils.isEmpty(ptype)) {
            ptype = "2";
        }
        bundle.putString("ptype", ptype);

        tab = new String[]{"产品介绍", "投资排行", "常见问题"};
        // 给viewpager设置适配器
        tabPA = new NewProductDetailActivity.TabFragPA(getSupportFragmentManager());//继承fragmentactivity
        vp_pro.setAdapter(tabPA);
        //viewpagerindictor和viewpager关联
        pro_indicator.setViewPager(vp_pro);
        vp_pro.setOffscreenPageLimit(2);//预加载

        Intent intent = getIntent();
        Uri uri = intent.getData();
        LogUtils.i("--->Act_Detail_Pro：Uri：" + uri);
        if (uri != null) {
            pid = uri.getQueryParameter("pid");
            ptype = uri.getQueryParameter("ptype");
            atid = uri.getQueryParameter("atid");
            intent_fid = uri.getQueryParameter("fid");
            intent_amount = uri.getQueryParameter("amount");
            intent_enableAmount = uri.getQueryParameter("enableAmount");
            LogUtils.i("--->Act_Detail_Pro：pid=" + pid + " ,ptype=" + ptype + " ,atid=" + atid + " ,intent_fid=" + intent_fid + " ,intent_amount=" + intent_amount + " intent_enableAmount=" + intent_enableAmount);
        } else {
            pid = getIntent().getStringExtra("pid");
            ptype = getIntent().getStringExtra("ptype");
            atid = getIntent().getStringExtra("atid");
            intent_fid = getIntent().getStringExtra("fid");
            intent_amount = getIntent().getStringExtra("amount");
            intent_enableAmount = getIntent().getStringExtra("enableAmount");
            LogUtils.i("--->Act_Detail_Pro：pid=" + pid + " ,ptype=" + ptype + " ,atid=" + atid + " ,intent_fid=" + intent_fid + " ,intent_amount=" + intent_amount + " intent_enableAmount=" + intent_enableAmount);
        }

        titleLeftimageview.setOnClickListener(this);
        titleLeftimageviewAdd.setOnClickListener(this);
        titleRighttextview.setOnClickListener(this);
        titleRighttextviewAdd.setOnClickListener(this);
        btProdetailInvest.setOnClickListener(this);
    }

    public static boolean hasNavigationBar = false;

    // 获取是否存在NavigationBar（虚拟按键）
    public static boolean checkDeviceHasNavigationBar(Context context) {
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    /**
     * 获取是否存在NavigationBar
     *
     * @return
     */
    public boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

    /**
     * 获取虚拟功能键高度
     */
    public int getVirtualBarHeigh() {
        int vh = 0;
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO: 2018/4/26 全面屏导航键问题
        // return vh;
        return 0;
    }

    @Override
    public void openDetails(boolean smooth) {
        slidedetails.smoothOpen(smooth);

    }

    @Override
    public void closeDetails(boolean smooth) {
        slidedetails.smoothClose(smooth);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.title_leftimageview_add:
                finish();
                break;
            case R.id.title_righttextview:
                startActivity(new Intent(NewProductDetailActivity.this, WebViewActivity.class).putExtra("URL", UrlConfig.JIEKUAN).putExtra("TITLE", "投资协议"));
                break;
            case R.id.title_righttextview_add:
                startActivity(new Intent(NewProductDetailActivity.this, WebViewActivity.class).putExtra("URL", UrlConfig.JIEKUAN).putExtra("TITLE", "投资协议"));
                break;
            case R.id.bt_prodetail_invest://立即投资
                if (!preferences.getBoolean("login", false)) {
                    startActivityForResult(new Intent(NewProductDetailActivity.this, NewLoginActivity.class), 1);
                } else {
                    showPayDetailPopWindow1();
                }
                break;
            case R.id.title_centertextview_add://协议
                startActivity(new Intent(NewProductDetailActivity.this, WebViewActivity.class).putExtra("URL", UrlConfig.JIEKUAN).putExtra("TITLE", "投资协议"));
                break;
            case R.id.img_back:
                popupWindow.dismiss();
                break;
            case R.id.img_close:
                popupWindow.dismiss();
                break;
            case R.id.tv_saowei://扫尾
                double weidan = Double.valueOf(surplusAmount);
                int w = (int) weidan;
                et_input_amount.setText(w + "");
                et_input_amount.setSelection(et_input_amount.getText().toString().trim().length());
                Money_Get(surplusAmount);
                tv_jifen.setText(calculatePoints(Integer.valueOf(et_input_amount.getText().toString().trim())) + "");//积分
                ll_bottom_tip1.setVisibility(View.GONE);
                ll_bottom_tip2.setVisibility(View.GONE);
                ll_bottom_tip3.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_choose_tiyanjin://选择体验金
                MobclickAgent.onEvent(NewProductDetailActivity.this, UrlConfig.point + 23 + "");
                if (System.currentTimeMillis() - lastClick <= 100) {
                    //ToastMaker.showShortToast("点那么快干什么");
                    return;
                }
                lastClick = System.currentTimeMillis();
                if (preferences.getString("uid", "").equalsIgnoreCase("") && pid != null) {
                    startActivity(new Intent(NewProductDetailActivity.this, NewLoginActivity.class));
                } else {
                    if (!et_input_amount.getText().toString().equalsIgnoreCase("")) {
                        if (Double.valueOf(et_input_amount.getText().toString()) > Double.valueOf(surplusAmount)) {
                            ToastMaker.showLongToast("投资金额不能大于产品可投金额");
                            return;
                        }
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(layout.getWindowToken(), 0);

                        showTiYanJinList(listTiYanJin);
                    } else {
                        ToastMaker.showLongToast("请输入金额");
                    }
                }
                break;
            case R.id.ll_choose_conpons://选择优惠券
                MobclickAgent.onEvent(NewProductDetailActivity.this, UrlConfig.point + 23 + "");
                if (System.currentTimeMillis() - lastClick <= 100) {
                    //ToastMaker.showShortToast("点那么快干什么");
                    return;
                }
                lastClick = System.currentTimeMillis();
                if (preferences.getString("uid", "").equalsIgnoreCase("") && pid != null) {
                    startActivity(new Intent(NewProductDetailActivity.this, NewLoginActivity.class));
                } else {
                    if (!et_input_amount.getText().toString().equalsIgnoreCase("")) {
                        if (Double.valueOf(et_input_amount.getText().toString()) > Double.valueOf(surplusAmount)) {
                            ToastMaker.showLongToast("投资金额不能大于产品可投金额");
                            return;
                        }

                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(layout.getWindowToken(), 0);

                        showPopupWindowConpons(listConpons);
                    } else {
                        ToastMaker.showLongToast("请输入金额");
                    }
                }
                break;
            case R.id.img_back_conpons:
                if (popupWindowConpons != null) {
                    popupWindowConpons.dismiss();
                }
                if (popupWindowTiYanJin != null) {
                    popupWindowTiYanJin.dismiss();
                }
                break;
            case R.id.rl_notUse_conponslist:
                if (popupWindowConpons != null) {
                    popupWindowConpons.dismiss();
                    fid = "";
                    tv_prodetail_conpons.setVisibility(View.GONE);
                    ll_describe_conpons.setVisibility(View.VISIBLE);
                    tv_num_conpons.setText(couponList.size() + "");
                    Money_Get("");//收益

                }
                break;
            case R.id.rl_notUse_tiyanjin:
                if (popupWindowTiYanJin != null) {
                    popupWindowTiYanJin.dismiss();
                    experienceId = "";
                    tv_prodetail_tiyanjin.setVisibility(View.GONE);
                    ll_describe_tiyanjin.setVisibility(View.VISIBLE);
                    tv_num_tiyanjin.setText(listFavourable.size() + "");
                }
                break;
            case R.id.img_back2:
                popupWindow2.dismiss();
                systemOrders = "";//若回退，清除订单号systemOrders
                break;
            case R.id.img_close2:
                popupWindow2.dismiss();
                systemOrders = "";//若回退，清除订单号systemOrders
                break;
            case R.id.tv_getYzm:
                //支付发送验证码
                sendInvestMsg();
                break;

            case R.id.tv_invest_complete://投资完成
                isSuccessPay = false;
                popupWindowSuccess.dismiss();
                popupWindow2.dismiss();
                popupWindow.dismiss();
                getDate();
                break;
            case R.id.tv_see_investdetail://查看详情
                isSuccessPay = false;
                popupWindowSuccess.dismiss();
                popupWindow2.dismiss();
                popupWindow.dismiss();
                if (!preferences.getBoolean("login", false)) {
                    startActivityForResult(new Intent(NewProductDetailActivity.this, NewLoginActivity.class), 1);
                    finish();
                } else {
                    startActivity(new Intent(NewProductDetailActivity.this, InvestmentDetails2Activity.class).putExtra("investId", investId).putExtra("type", 2));
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void getDate() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.PRODUCT_DETAIL)
                .addParams("pid", pid)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        //LogPrintUtil.e("LF--->产品详情", response);
                        JSONObject obj = JSON.parseObject(response);
                        dismissDialog();
                        ptrProDetail.refreshComplete();
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            balance = map.getString("balance");//当前登录用户余额
                            isOldUser = map.getBoolean("isOldUser");//是否是老用户
                            isShowLabel = map.getBoolean("isShowLabel");//是否显示激活体验金
                            tpwdFlag = map.getInteger("tpwdFlag");

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("tpwdFlag", tpwdFlag+"");
                            editor.commit();
                            //interestType = map.getInteger("interestType");
                            startTime = map.getString("nowTime");//计息日期
                            sysDate = map.getString("sysDate");//计息日期
                            JSONArray extendInfos = map.getJSONArray("extendInfos");//产品介绍
                            proIntroduceList = JSON.parseArray(extendInfos.toJSONString(), ProIntroduceBean.class);
                            bundle.putSerializable("proIntroduceList", (Serializable) proIntroduceList);
                            JSONObject info = map.getJSONObject("info");
                            principleH5 = info.getString("principleH5");//产品原理图
                            String borrower = info.getString("borrower");
                            String accept = info.getString("accept");
                            String introduce = info.getString("introduce");
                            String repaySource = info.getString("repaySource");
                            bundle.putString("borrower", borrower);
                            bundle.putString("introduce", introduce);
                            bundle.putString("accept", accept);
                            bundle.putString("repaySource", repaySource);
                            bundle.putString("principleH5", principleH5);
                            fullName = info.getString("fullName");
                            rate = info.getDouble("rate");//利率
                            activityRate = info.getDouble("activityRate");//活动利率
                            deadline = info.getString("deadline");
                            amount = info.getString("amount");//投资总金额
                            increasAmount = info.getString("increasAmount");
                            surplusAmount = info.getString("surplusAmount");//剩余可投金额
                            leastaAmount = info.getString("leastaAmount");//起投金额
                            maxAmount = info.getString("maxAmount");//最多投资金额
                            establishTime = info.getString("establish");//成立日期
                            endTime = info.getString("expireDate");//回款日期
                            endDate = info.getString("endDate");//募集结束日期
                            pert = info.getString("pert");

                            distributionId = info.getString("distributionId");//是展示排行榜还是投资记录
                            LogUtils.e("distributionId" + distributionId);
                            titleCentertextview.setText(fullName);
                            titleCentertextviewAdd.setText(fullName);
                            tv_prodetail_rate.setText(rate + "");
                            tv_prodetail_rate_activity.setText(activityRate + "");
                            tv_prodetail_deadline.setText(deadline + "天");
                            tv_prodetail_leastaAmount.setText(leastaAmount + "元");
                            tv_prodetail_balance.setText(surplusAmount + "元");
                            tv_prodetail_total.setText(amount + "元");
                            histogramView.setPercent(Integer.valueOf(stringCut.pertCut(pert)).intValue());
                            wave_view.setProgress(Integer.valueOf(stringCut.pertCut(pert)).intValue());

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd");
                            String date_interest = simpleDateFormat.format(new Date(Long.valueOf(startTime)));
                            String date_establish = simpleDateFormat.format(new Date(Long.valueOf(establishTime)));
                            String date_expire = simpleDateFormat.format(new Date(Long.valueOf(endTime)));
                            //tv_day_invest.setText(date_establish);
                            tv_day_establish.setText(date_establish);
                            tv_day_returned.setText(date_expire);

                            isRoundOff = map.getBoolean("isRoundOff");//是否显示尾单
                            roundOffMoney = map.getString("roundOffMoney");//尾单奖励

                            //判断可用哪种优惠券的标签
                            if ("1".equals(info.getString("isInterest"))) {
                                tv_prodetail_interest.setVisibility(View.VISIBLE);
                            }
                            if ("1".equals(info.getString("isCash"))) {
                                tv_prodetail_red.setVisibility(View.VISIBLE);
                            }
                            if ("1".equals(info.getString("isDouble"))) {
                                tv_prodetail_double.setVisibility(View.VISIBLE);
                            }
                            if ("1".equals(info.getString("isHot"))) {
                                tv_prodetail_hot.setVisibility(View.VISIBLE);
                            }
                            if (isRoundOff) {
                                tv_prodetail_saowei.setVisibility(View.VISIBLE);
                            } else {
                                tv_prodetail_saowei.setVisibility(View.GONE);
                            }

                            tag = info.getString("tag");//活动标签(可能多个) eg:"tag": "第一项,第二项"
                            if (tag != null && !tag.equals("")) {
                                ll_tag_row2.setVisibility(View.VISIBLE);
                                String[] tags = tag.split(",");
                                if (tags.length == 1) {
                                    tv_tag1.setVisibility(View.VISIBLE);
                                    tv_tag1.setText(tags[0]);
                                } else if (tags.length == 2) {
                                    tv_tag1.setVisibility(View.VISIBLE);
                                    tv_tag2.setVisibility(View.VISIBLE);
                                    tv_tag1.setText(tags[0]);
                                    tv_tag2.setText(tags[1]);
                                } else if (tags.length == 3) {
                                    tv_tag1.setVisibility(View.VISIBLE);
                                    tv_tag2.setVisibility(View.VISIBLE);
                                    tv_tag3.setVisibility(View.VISIBLE);
                                    tv_tag1.setText(tags[0]);
                                    tv_tag2.setText(tags[1]);
                                    tv_tag3.setText(tags[2]);
                                } else if (tags.length == 4) {
                                    tv_tag1.setVisibility(View.VISIBLE);
                                    tv_tag2.setVisibility(View.VISIBLE);
                                    tv_tag3.setVisibility(View.VISIBLE);
                                    tv_tag4.setVisibility(View.VISIBLE);
                                    tv_tag1.setText(tags[0]);
                                    tv_tag2.setText(tags[1]);
                                    tv_tag3.setText(tags[2]);
                                    tv_tag4.setText(tags[3]);
                                }

                            } else {
                                ll_tag_row2.setVisibility(View.GONE);
                            }
                            //没有任何一个标签，则隐藏布局
                            if ("0".equals(info.getString("isInterest")) && "0".equals(info.getString("isCash")) && "0".equals(info.getString("isDouble")) && "0".equals(info.getString("isHot")) && TextUtils.isEmpty(tag)) {
                                ll_tag_row1.setVisibility(View.GONE);
                            }

                            //滚动的活动公告
                            JSONArray objnotice = map.getJSONArray("inProgressActivity");
                            if (objnotice != null) {
                                noticeList = JSON.parseArray(objnotice.toJSONString(), NoticeBean.class);
                                List<String> infos = new ArrayList<>();
                                for (int i = 0; i < noticeList.size(); i++) {
                                    infos.add(noticeList.get(i).getTitle());
                                }
                                ll_notice.setVisibility(View.VISIBLE);
                                marqueeView.startWithList(infos);
                                //公告跳转
                                marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position, TextView textView) {
                                        NoticeBean noticeBean = noticeList.get(position);

                                        if (noticeBean.getAppUrl().contains("jumpTo=3")) { //邀请好友三重礼
                                            finish();
                                            LocalApplication.getInstance().getMainActivity().setCheckedFram(3);//返利
                                        } else {
                                            startActivity(new Intent(NewProductDetailActivity.this, WebViewActivity.class)
                                                    .putExtra("URL", noticeBean.getAppUrl() + "?app=true")
                                                    .putExtra("TITLE", noticeBean.getTitle())
                                                    .putExtra("AFID", noticeBean.getId() + ""));
                                        }
                                    }
                                });
                            } else {
                                ll_notice.setVisibility(View.GONE);
                            }

                            if (info.getInteger("repayType") == 1) {
                                repayType = "到期还本付息";
                            } else if (info.getInteger("repayType") == 2) {
                                repayType = "按月付息到期还本";
                            }
                            tv_prodetail_repaytype.setText(repayType);

                            //万元预计收益
                            rate_expect = Double.valueOf(NewProductDetailActivity.this.rate) / 100;
                            exRate_expect = Double.valueOf(NewProductDetailActivity.this.activityRate) / 100;
                            Double day = Double.valueOf(NewProductDetailActivity.this.deadline);
                            shouyi_wanyuan = 10000 * (rate_expect + exRate_expect) * day / 360;
                            int sy = (int) (shouyi_wanyuan * 100);
                            shouyi_wanyuan = sy / 100.0;
                            LogUtils.i("--->shouyi_wanyuan：" + shouyi_wanyuan);
                            tvWanShouyi.setText(shouyi_wanyuan + "");

                            couponList = map.getJSONArray("couponList");//优惠券列表
                            listConpons = JSON.parseArray(couponList.toJSONString(), ConponsBean.class);
                            listFavourable = map.getJSONArray("listFavourable");//体验金列表
                            listTiYanJin = JSON.parseArray(listFavourable.toJSONString(), ConponsBean.class);

                            //如果是预售标，显示活动倒计时，不能点击
                            if (is_presell_plan) {
                                ll_presell_timer.setVisibility(View.VISIBLE);
                                btProdetailInvest.setVisibility(View.GONE);
                                //btProdetailInvest.setBackgroundResource(R.drawable.bg_corner_gray);
                                btProdetailInvest.setEnabled(false);
                                btProdetailInvest.setClickable(false);

                                //活动开始时间
                                String startDate = getIntent().getStringExtra("startDate");
                                long startMillis = Long.valueOf(startDate);
                                //获取服务器当前时间秒值
                                long curMillis = Long.valueOf(sysDate);
                                time_timer = (startMillis) / 1000 - curMillis;
                                handler_timer.postDelayed(runnable, 1000);

                            } else {
                                ll_presell_timer.setVisibility(View.GONE);
                                btProdetailInvest.setVisibility(View.VISIBLE);
                                //btProdetailInvest.setBackgroundResource(R.mipmap.bg_button);
                                btProdetailInvest.setEnabled(true);
                                btProdetailInvest.setClickable(true);
                            }

                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        // TODO Auto-generated method stub
                        ptrProDetail.refreshComplete();
                        dismissDialog();
                        ToastMaker.showShortToast("网络错误，请检查");
                    }
                });
    }


    /*
    * 投资支付流程1的弹窗
    * */
    private void showPayDetailPopWindow1() {
        bankInfo();//获取银行卡信息
        LogUtils.i("--->是否显示尾单 isRoundOff=" + isRoundOff);
        layout = LayoutInflater.from(NewProductDetailActivity.this).inflate(R.layout.pop_pay_detail1, null);
        LinearLayout ll_pro_detail = (LinearLayout) layout.findViewById(R.id.ll_pro_detail);
        ll_pro_detail.setBackgroundResource(R.mipmap.bg_pro_detail2);
        ImageView img_close = (ImageView) layout.findViewById(R.id.img_close);
        ImageView img_back = (ImageView) layout.findViewById(R.id.img_back);
        final TextView tv_prodetail_title = (TextView) layout.findViewById(R.id.tv_prodetail_title);
        final TextView tv_prodetail_rate = (TextView) layout.findViewById(R.id.tv_prodetail_rate);
        final TextView tv_prodetail_deadline = (TextView) layout.findViewById(R.id.tv_prodetail_deadline);
        final TextView tv_prodetail_leastAmount = (TextView) layout.findViewById(R.id.tv_prodetail_leastAmount);
        final TextView tv_prodetail_balance = (TextView) layout.findViewById(R.id.tv_prodetail_balance);
        et_input_amount = (EditText) layout.findViewById(R.id.et_input_amount);
        tv_saowei = (TextView) layout.findViewById(R.id.tv_saowei);
        tv_jifen = (TextView) layout.findViewById(R.id.tv_jifen);
        //体验金
        LinearLayout ll_choose_tiyanjin = (LinearLayout) layout.findViewById(R.id.ll_choose_tiyanjin);
        RelativeLayout rl_tiyanjin = (RelativeLayout) layout.findViewById(R.id.rl_tiyanjin);
        ll_describe_tiyanjin = (LinearLayout) layout.findViewById(R.id.ll_describe_tiyanjin);
        tv_num_tiyanjin = (TextView) layout.findViewById(R.id.tv_num_tiyanjin);
        tv_prodetail_tiyanjin = (TextView) layout.findViewById(R.id.tv_prodetail_tiyanjin);
        //优惠券
        LinearLayout ll_choose_conpons = (LinearLayout) layout.findViewById(R.id.ll_choose_conpons);
        ll_describe_conpons = (LinearLayout) layout.findViewById(R.id.ll_describe_conpons);
        tv_num_conpons = (TextView) layout.findViewById(R.id.tv_num_conpons);
        tv_prodetail_conpons = (TextView) layout.findViewById(R.id.tv_prodetail_conpons);

        tv_prodetail_income_add = (TextView) layout.findViewById(R.id.tv_prodetail_income_add);
        tv_prodetail_income = (TextView) layout.findViewById(R.id.tv_prodetail_income);
        tv_prodetail_income_name = (TextView) layout.findViewById(R.id.tv_prodetail_income_name);

        ll_bottom_tip1 = (LinearLayout) layout.findViewById(R.id.ll_bottom_tip1);
        final TextView tv_shouyi_wanyuan = (TextView) layout.findViewById(R.id.tv_shouyi_wanyuan);
        final TextView tv_jifen_wanyuan = (TextView) layout.findViewById(R.id.tv_jifen_wanyuan);
        ll_bottom_tip2 = (LinearLayout) layout.findViewById(R.id.ll_bottom_tip2);
        final TextView tv_amount_remian = (TextView) layout.findViewById(R.id.tv_amount_remian);
        final TextView tv_cash_red = (TextView) layout.findViewById(R.id.tv_cash_red);
        ll_bottom_tip3 = (LinearLayout) layout.findViewById(R.id.ll_bottom_tip3);
        bt_prodetail_pop_ok = (TextView) layout.findViewById(R.id.bt_prodetail_pop_ok);

        tv_prodetail_title.setText(fullName);
        double expert_rate = rate + activityRate;
        tv_prodetail_rate.setText(expert_rate + "%");
        tv_prodetail_deadline.setText(deadline);
        tv_prodetail_leastAmount.setText(leastaAmount);
        tv_prodetail_balance.setText(surplusAmount);
        Watcher watcher = new Watcher();
        et_input_amount.addTextChangedListener(watcher);


        if (isRoundOff) {//显示尾单
            tv_saowei.setVisibility(View.VISIBLE);
            tv_saowei.setText("扫尾");
            et_input_amount.setHint("投" + surplusAmount + "元，扫尾单得现金");
            ll_bottom_tip1.setVisibility(View.GONE);
            ll_bottom_tip2.setVisibility(View.VISIBLE);
            tv_amount_remian.setText(surplusAmount);
            tv_cash_red.setText(roundOffMoney);

        } else {//非尾单
            tv_saowei.setVisibility(View.GONE);
            et_input_amount.setHint(leastaAmount + "元起投,按倍数增加");
            ll_bottom_tip1.setVisibility(View.VISIBLE);
            ll_bottom_tip2.setVisibility(View.GONE);
            tv_shouyi_wanyuan.setText(shouyi_wanyuan + "");
            tv_jifen_wanyuan.setText(calculatePoints(10000) + "");

        }

        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            rl_tiyanjin.setVisibility(View.VISIBLE);
        } else {
            if (isShowLabel) {
                rl_tiyanjin.setVisibility(View.VISIBLE);
                if (null != listFavourable) {//体验金
                    if (listFavourable.size() <= 0) {
                        ll_choose_tiyanjin.setFocusable(false);
                        ll_choose_tiyanjin.setEnabled(false);
                        tv_num_tiyanjin.setText("0");
                    } else {
                        tv_num_tiyanjin.setText(listFavourable.size() + "");
                    }
                }
            } else {
                rl_tiyanjin.setVisibility(View.GONE);
            }
        }

        if (null != couponList) {//优惠券
            if (couponList.size() <= 0) {
                ll_choose_conpons.setFocusable(false);
                ll_choose_conpons.setEnabled(false);
                tv_num_conpons.setText("0");
            } else {
                tv_num_conpons.setText(couponList.size() + "");
            }
        }

        if (checkDeviceHasNavigationBar(NewProductDetailActivity.this) && Build.VERSION.SDK_INT <= 21) {//5.0及以下并带有虚拟按键的系统
            int virtualBarHeigh = getVirtualBarHeigh();
            LogUtils.i("showPayDetailPopWindow1:虚拟按键的高度：" + virtualBarHeigh);
            layout.setPadding(0, 0, 0, virtualBarHeigh);
        }
        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setContentView(layout);

        // 控制键盘是否可以获得焦点
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        img_back.setOnClickListener(NewProductDetailActivity.this);
        img_close.setOnClickListener(NewProductDetailActivity.this);
        tv_saowei.setOnClickListener(NewProductDetailActivity.this);
        ll_choose_tiyanjin.setOnClickListener(NewProductDetailActivity.this);
        ll_choose_conpons.setOnClickListener(NewProductDetailActivity.this);

        bt_prodetail_pop_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(NewProductDetailActivity.this, UrlConfig.point + 37 + "");
                    startActivity(new Intent(NewProductDetailActivity.this, NewLoginActivity.class));
                } else if (et_input_amount.getText().length() <= 0) {
                    ToastMaker.showShortToast("请输入投资金额");
                } else if (Double.parseDouble(stringCut.douHao_Cut(et_input_amount.getText().toString())) < Double.parseDouble(leastaAmount)) {
                    ToastMaker.showShortToast("投资金额最少为" + stringCut.getNumKbs(Double.parseDouble(leastaAmount)));
                } else if ((Double.parseDouble(stringCut.douHao_Cut(et_input_amount.getText().toString()))) % Double.parseDouble(increasAmount) != 0) {
                    ToastMaker.showShortToast("投资金额需为" + stringCut.getNumKbs(Double.parseDouble(increasAmount)) + "的倍数");
                } else if (Double.parseDouble(stringCut.douHao_Cut(et_input_amount.getText().toString())) > Double.parseDouble(surplusAmount)) {
                    ToastMaker.showShortToast("投资金额不能大于产品可投金额");
                } else if (!"1".equals(preferences.getString("realVerify", ""))) {
                    ToastMaker.showShortToast("还未认证 ，请认证");
                    MobclickAgent.onEvent(NewProductDetailActivity.this, UrlConfig.point + 38 + "");
                    startActivityForResult(new Intent(NewProductDetailActivity.this, FourPartAct.class)
                            .putExtra("proName", fullName)//产品名称
                            .putExtra("proDeadline", deadline)//投资期限
                            .putExtra("proRate", rate)//预期年化收益
                            .putExtra("specialRate", activityRate + "")//活动利率
                            .putExtra("proAmount", et_input_amount.getText().toString()), 4//投资金额
                    );
                } else {

                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(layout.getWindowToken(), 0);

                    boolean selectCoupon = isCanSelectCoupon(listConpons);

                    if (selectCoupon) {
                        //ToastMaker.showLongToast("您有优惠券未使用");

                        DialogMaker.showRedSureDialog(NewProductDetailActivity.this, "优惠券", "您有可使用的优惠券,是否去选择使用？", "不使用", "去使用", new DialogMaker.DialogCallBack() {
                            @Override
                            public void onButtonClicked(Dialog dialog, int position, Object tag) {
                                showPopupWindowConpons(listConpons);
                            }

                            @Override
                            public void onCancelDialog(Dialog dialog, Object tag) {
                                //支付流程2
                                showPayDetailPopWindow2(et_input_amount.getText().toString());
                            }
                        }, "");
                    } else {
                        //支付流程2
                        showPayDetailPopWindow2(et_input_amount.getText().toString());
                    }

                }
            }
        });
    }

    /*
  * 投资支付流程2的弹窗
  * */
    private void showPayDetailPopWindow2(final String payAmount) {
        stopTimer();//回退再进来，显示“获取验证码”
        systemOrders = "";//若回退，清除订单号systemOrders
        layout2 = LayoutInflater.from(NewProductDetailActivity.this).inflate(R.layout.pop_pay_detail2, null);
        ImageView img_close2 = (ImageView) layout2.findViewById(R.id.img_close2);
        ImageView img_back2 = (ImageView) layout2.findViewById(R.id.img_back2);
        ImageView iv_bank = (ImageView) layout2.findViewById(R.id.iv_bank);
        final TextView tv_account_balance = (TextView) layout2.findViewById(R.id.tv_account_balance);
        final CheckBox check_choose_acount_balance = (CheckBox) layout2.findViewById(R.id.check_choose_acount_balance);
        final TextView tv_amount_pay = (TextView) layout2.findViewById(R.id.tv_amount_pay);
        final TextView tv_amount_bank_pay = (TextView) layout2.findViewById(R.id.tv_amount_bank_pay);
        final TextView tv_banknum = (TextView) layout2.findViewById(R.id.tv_banknum);
        final TextView tv_limit = (TextView) layout2.findViewById(R.id.tv_limit);
        final TextView tv_limit_day = (TextView) layout2.findViewById(R.id.tv_limit_day);
        final CheckBox check_agree_protocal = (CheckBox) layout2.findViewById(R.id.check_agree_protocal);
        final TextView tv_user_protocol = (TextView) layout2.findViewById(R.id.tv_user_protocol);
        final RelativeLayout rl_send_sms = (RelativeLayout) layout2.findViewById(R.id.rl_send_sms);
        et_sms_code = (EditText) layout2.findViewById(R.id.et_sms_code);
        tv_getYzm = (TextView) layout2.findViewById(R.id.tv_getYzm);
        final TextView bt_prodetail_ok2 = (TextView) layout2.findViewById(R.id.bt_prodetail_ok2);

        tv_account_balance.setText(balance);//账户余额
        tv_amount_pay.setText(payAmount);
        //银行卡信息
        if (!TextUtils.isEmpty(bankid)) {
            if (bp == null) {
                bp = new BankName_Pic();
            }
            Integer pic = bp.bank_Pic(bankid);
            iv_bank.setImageResource(pic);
        }
        if (!TextUtils.isEmpty(bankNum)) {
            tv_banknum.setText(bankName + "(" + bankNum + ")");
        }
        if (singleQuotaJYT != null) {
            tv_limit.setText(stringCut.getNumKbs(singleQuotaJYT) + "元");
        }
        if (dayQuotaJYT != null) {
            tv_limit_day.setText(stringCut.getNumKbs(dayQuotaJYT) + "元");
        }

        bt_prodetail_ok2.setText("实际支付" + payAmount + "元");
        if (Double.valueOf(balance) <= 0) {//余额为0
            check_choose_acount_balance.setChecked(false);
            check_choose_acount_balance.setClickable(false);
            rechargeAmount = (Double.valueOf(payAmount)) + "";
            tv_amount_bank_pay.setText(rechargeAmount);
            rl_send_sms.setVisibility(View.VISIBLE);
            isNewPay = "1";

        } else {
            check_choose_acount_balance.setChecked(true);
            check_choose_acount_balance.setClickable(true);
            if (Double.valueOf(balance) < Double.valueOf(payAmount)) {//账户余额不足
                rechargeAmount = (Double.valueOf(payAmount) - Double.valueOf(balance)) + "";
                tv_amount_bank_pay.setText(rechargeAmount);
                rl_send_sms.setVisibility(View.VISIBLE);
                isNewPay = "1";
            } else {//账户余额充足
                rechargeAmount = "0";
                tv_amount_bank_pay.setText(rechargeAmount);
                rl_send_sms.setVisibility(View.GONE);
                isNewPay = "0";
            }
        }
        //监听是否打勾
        check_choose_acount_balance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//若打勾。投资加上余额
                    if (Double.valueOf(balance) < Double.valueOf(payAmount)) {//账户余额不足
                        rechargeAmount = (Double.valueOf(payAmount) - Double.valueOf(balance)) + "";
                        tv_amount_bank_pay.setText(rechargeAmount);
                        rl_send_sms.setVisibility(View.VISIBLE);
                        isNewPay = "1";
                    } else {//账户余额充足
                        rechargeAmount = "0";
                        tv_amount_bank_pay.setText(rechargeAmount);
                        rl_send_sms.setVisibility(View.GONE);
                        isNewPay = "0";
                    }
                } else {//若不打勾。投资只用银行卡
                    rechargeAmount = (Double.valueOf(payAmount)) + "";
                    tv_amount_bank_pay.setText(rechargeAmount);
                    rl_send_sms.setVisibility(View.VISIBLE);
                    isNewPay = "1";
                }
            }
        });

        if (checkDeviceHasNavigationBar(NewProductDetailActivity.this) && Build.VERSION.SDK_INT <= 21) {//5.0及以下并带有虚拟按键的系统
            int virtualBarHeigh = getVirtualBarHeigh();
            LogUtils.i("showPayDetailPopWindow2:虚拟按键的高度：" + virtualBarHeigh);
            layout2.setPadding(0, 0, 0, virtualBarHeigh);
        }
        popupWindow2 = new PopupWindow(layout2, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow2.setContentView(layout2);

        // 控制键盘是否可以获得焦点
        popupWindow2.setBackgroundDrawable(new BitmapDrawable());
        popupWindow2.setOutsideTouchable(true);
        popupWindow2.setFocusable(true);
        popupWindow2.showAtLocation(layout2, Gravity.CENTER, 0, 0);

        img_back2.setOnClickListener(NewProductDetailActivity.this);
        img_close2.setOnClickListener(NewProductDetailActivity.this);
        tv_getYzm.setOnClickListener(NewProductDetailActivity.this);
        tv_user_protocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewProductDetailActivity.this, WebViewActivity.class).putExtra("URL", UrlConfig.JIEKUAN).putExtra("TITLE", "投资协议"));
            }
        });

        bt_prodetail_ok2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!check_agree_protocal.isChecked()) {
                    ToastMaker.showShortToast("请同意协议");
                    return;
                }
                if (isNewPay.equals("1") && TextUtils.isEmpty(et_sms_code.getText().toString().toString())) {
                    ToastMaker.showShortToast("请输入验证码");
                    return;
                }
                if (isNewPay.equals("1") && et_sms_code.getText().toString().toString().length() < 6) {
                    ToastMaker.showShortToast("请输入正确的验证码");
                    return;
                }
                if (isNewPay.equals("1") && TextUtils.isEmpty(systemOrders)) {//带有银行卡充值投资的，订单号为空，需要重发获取验证码
                    ToastMaker.showShortToast("请重新获取短信验证码");
                    return;
                }
                if (isNewPay.equals("1") && !rechargeAmount.equals(preferences.getString("rechargeAmount", ""))) {
                    ToastMaker.showShortToast("投资金额前后不一致，请重新获取短信验证码");
                    return;
                }
                Invest_Begin_bl = true;
                LogUtils.i("--->投资方式：isNewPay=" + isNewPay);
                //获取用户银行卡信息
                memberSetting();

            }
        });
    }

    private void bankInfo() {
        OkHttpUtils.post().url(UrlConfig.MEMBERSETTING)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("--->memberSetting/获取用户银行卡信息：" + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            bankid = map.getString("bankId");
                            bankName = map.getString("bankName");
                            bankNum = map.getString("bankNum");
                            singleQuotaJYT = map.getInteger("singleQuotaJYT");
                            dayQuotaJYT = map.getInteger("dayQuotaJYT");
                        } else {
                            ToastMaker.showShortToast("获取用户银行卡信息失败");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    private void sendInvestMsg() {
        LogUtils.i("--->投资充值金额：" + rechargeAmount);
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.SENDINVESTMSG)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("pid", pid)
                .addParam("amount", rechargeAmount)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        dismissDialog();
                        LogUtils.i("--->投资发送FYMsg：" + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean("success")) {
                            ToastMaker.showShortToast("验证码已发送");
                            time();
                            SharedPreferences.Editor edit = preferences.edit();
                            edit.putString("rechargeAmount", rechargeAmount);
                            edit.commit();
                            JSONObject map = obj.getJSONObject("map");
                            systemOrders = map.getString("systemOrders");
                        } else if ("3002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast(obj.getString("errorMsg"));
                            stopTimer();
                        } else if ("3003".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast(obj.getString("errorMsg"));
                            stopTimer();
                        } else if ("1111".equals(obj.getString("errorCode"))) {
                            stopTimer();
                            ToastMaker.showShortToast("手机号码被锁，请联系客服");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
                            new show_Dialog_IsLogin(NewProductDetailActivity.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统异常");
                            stopTimer();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    private boolean isfirstPwd = false;//是否是第一次设置密码
    private TradePwdPopUtils pop;
    private String firstPwd = "";
    private String secondPwd;
    private boolean Invest_Begin_bl = false;

    private void memberSetting() {
        OkHttpUtils.post().url(UrlConfig.MEMBERSETTING)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        LogUtils.i("--->memberSetting/获取用户银行卡信息：" + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");

                            if (map.getInteger("tpwdFlag") == 0) {//未设置交易密码
                                pop = new TradePwdPopUtils();
                                pop.setCallBackTradePwd(new TradePwdPopUtils.CallBackTradePwd() {

                                    @Override
                                    public void callBaceTradePwd(String pwd) {
                                        pop.tv_tips.setText("");
                                        if (!isfirstPwd) {
                                            firstPwd = pwd;
                                            isfirstPwd = true;
                                            pop.tv_pwd1.setText("✔");
                                            pop.tv_pwd_line.setBackgroundColor(Color.parseColor("#fb5b6d"));
                                            pop.tv_pwd2.setBackgroundResource(R.drawable.bg_corner_red_yuan);
                                        } else {
                                            secondPwd = pwd;
                                            if (firstPwd.equalsIgnoreCase(secondPwd)) {
                                                pop.tv_tips.setText("");
                                                //去设置支付密码
                                                sendFirstTpwdCode();
                                            } else {
                                                pop.tv_tips.setText("您输入的确认密码和之前不一致");
                                            }
                                        }
                                    }
                                });
                                pop.showPopWindow(NewProductDetailActivity.this, NewProductDetailActivity.this, slidedetails_front);
                                pop.ll_invest_money.setVisibility(View.GONE);
                                pop.ll_pwd_title.setVisibility(View.VISIBLE);
                                pop.ll_pwd.setVisibility(View.VISIBLE);
                                pop.popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                    @Override
                                    public void onDismiss() {
                                        Invest_Begin_bl = false;
                                    }
                                });
                                pop.iv_key_close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pop.popWindow.dismiss();
                                    }
                                });
                                //忘记密码
                                pop.forget_pwd.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        pop.popWindow.dismiss();
                                        //startActivityForResult(new Intent(NewProductDetailActivity.this, TransactionPswAct.class), 1);
                                        startActivityForResult(new Intent(NewProductDetailActivity.this, NewForgetPswActivity.class).putExtra("isFrom", 1), 1);
                                    }
                                });
                                pop.popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                    @Override
                                    public void onDismiss() {
                                        pop.tv_tips.setText("");
                                        isfirstPwd = false;
                                    }
                                });
                            } else {
                                MobclickAgent.onEvent(NewProductDetailActivity.this, UrlConfig.point + 24 + "");
                                MobclickAgent.onEvent(NewProductDetailActivity.this, "100023");
                                pop = new TradePwdPopUtils();
                                pop.setCallBackTradePwd(new TradePwdPopUtils.CallBackTradePwd() {

                                    @Override
                                    public void callBaceTradePwd(String pwd) {
                                        firstPwd = pwd;
                                        MobclickAgent.onEvent(NewProductDetailActivity.this, UrlConfig.point + 24 + "");
                                        product_Invest();
                                    }
                                });
                                pop.showPopWindow(NewProductDetailActivity.this, NewProductDetailActivity.this, slidedetails_front);
                                pop.ll_invest_money.setVisibility(View.VISIBLE);
                                pop.tv_key_money.setText(et_input_amount.getText().toString());
                                pop.iv_key_close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pop.popWindow.dismiss();
                                    }
                                });
                                pop.popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                    @Override
                                    public void onDismiss() {
                                        Invest_Begin_bl = false;
                                    }
                                });
                                pop.forget_pwd.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        pop.popWindow.dismiss();
                                        //startActivityForResult(new Intent(NewProductDetailActivity.this, TransactionPswAct.class), 1);
                                        startActivityForResult(new Intent(NewProductDetailActivity.this, NewForgetPswActivity.class).putExtra("isFrom", 1), 1);
                                    }
                                });
                            }

                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(NewProductDetailActivity.this)
                                    .show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统错误");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        // TODO Auto-generated method stub
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    //设置交易密码
    private void sendFirstTpwdCode() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.UPDATETPWDBYSMS)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("tpwd", SecurityUtils.MD5AndSHA256(firstPwd))
//				.addParams("smsCode", code_et.getText().toString().trim())
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        setResult(5, new Intent());
                        // TODO Auto-generated method stub
                        LogUtils.i("--->设置交易密码：" + response);
                        dismissDialog();
                        pop.popWindow.dismiss();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("交易密码设置成功");
                            MobclickAgent.onEvent(NewProductDetailActivity.this, UrlConfig.point + 24 + "");
                            product_Invest();
                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("验证码错误");
                        } else if ("1002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("密码为空");
                        } else if ("1003".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("交易密码不合法");
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
                            new show_Dialog_IsLogin(NewProductDetailActivity.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统错误");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    // 投资
    //显示成立日期和到期日期
    private String tv_establish, tv_expireDate, luckcode, luckcodesize;
    private Integer investId;
    private String isNewPay = "0";//投资方式(0位余额投资,1为充值投资)
    private String systemOrders = "";//订单编号(支付发送验证码接口返回)
    private String rechargeAmount = "";//充值金额
    private String nothing = "";
    private String jumpURLActivity = "";

    private void product_Invest() {
        showWaitDialog("加载中...", false, "");
        LogUtils.i("--->普通Invest pid:" + pid + ", uid:" + preferences.getString("uid", "") + ", tpwd:" + firstPwd + ", amount:" + (et_input_amount.getText().toString().trim()) + ", isNewPay:" + isNewPay + ", verifyCode:" + et_sms_code.getText().toString().trim() + ", systemOrders:" + systemOrders + ", rechargeAmount:" + rechargeAmount + ", fid:" + fid + ", experienceId:" + experienceId);
        OkHttpUtils
                .post()
                .url(UrlConfig.INVEST)
                .addParams("pid", pid)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("tpwd", SecurityUtils.MD5AndSHA256(firstPwd))
                .addParams("amount", (et_input_amount.getText().toString().trim()))
                .addParam("experienceId", experienceId)
                .addParam("isNewPay", isNewPay)
                .addParam("verifyCode", et_sms_code.getText().toString().trim())
                .addParam("systemOrders", systemOrders)
                .addParam("rechargeAmount", rechargeAmount)
                .addParams("fid", fid)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        LogUtils.i("--->普通product/invest：" + response);
                        dismissDialog();
                        ptrProDetail.refreshComplete();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            Invest_Begin_bl = false;
                            pop.popWindow.dismiss();
                            JSONObject map = obj.getJSONObject("map");
                            if (map != null) {
                                expireDate = map.getLong("expireDate");
                                interest = map.getDouble("interest");
                                investId = map.getInteger("investId");
                                luckcode = map.getString("luckCodes");
                                luckcodesize = map.getString("luckCodeCount");
                                JSONArray textList = map.getJSONArray("textList");
                                textStringList = JSON.parseArray(textList.toJSONString(), String.class);
                                //NewProductDetailActivity.this.investId = map.getString("investId");
                                jumpURLActivity = map.getString("activityUrl");
                                if (!map.getBoolean("isRepeats")) {
                                    is_Activity(map.getString("expireDate"));
                                } else {
                                    /*Intent in = new Intent();
                                    in.putExtra("pid", pid);
                                    in.putExtra("tv_name", fullName);
                                    in.putExtra("tv_money", stringCut.douHao_Cut(et_input_amount.getText().toString().trim()));
                                    in.putExtra("tv_day", deadline);
                                    in.putExtra("tv_rate", rate);
                                    if("amount".equalsIgnoreCase(nothing)){
                                        in.putExtra("tv_earn", shouyi+"");
                                    }else {
                                        in.putExtra("tv_earn", shouyi+shouyi_add+"");
                                    }
                                    in.putExtra("tv_red", resultNum+"");
                                    in.putExtra("nothing", nothing);
                                    in.putExtra("investId", investId);
                                    in.putExtra("tv_start", tv_establish);
                                    in.putExtra("isPicture", map.getString("activityURL"));
                                    in.putExtra("jumpURLActivity", map.getString("activityUrl"));
                                    in.putExtra("specialRate", activityRate+"");
                                    in.putExtra("jumpURL", map.getString("jumpURL"));
                                    in.putExtra("tv_end", tv_expireDate);
                                    in.putExtra("luckcode", luckcode);
                                    in.putExtra("luckcodesize", luckcodesize);
                                    in.putExtra("endTime", map.getString("expireDate"));
                                    in.setClass(NewProductDetailActivity.this,Act_Pro_End.class);
                                    startActivityForResult(in,4);*/
                                    //ToastMaker.showShortToast("支付成功");
                                    showPaySuccessPopWindow();
                                }
                            } else {
                                is_Activity(map.getString("expireDate"));
                            }
                        } else if ("2001".equals(obj.getString("errorCode"))) {
                            MobclickAgent.onEvent(NewProductDetailActivity.this, UrlConfig.point + 20 + "");
                            pop.tv_tips.setText("连续输错三次密码，交易密码锁定一小时！请稍后再试或点击忘记密码");
                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            MobclickAgent.onEvent(NewProductDetailActivity.this, UrlConfig.point + 20 + "");
                            pop.tv_tips.setText("密码输入错误，请重新输入");
                        } else {
                            MobclickAgent.onEvent(NewProductDetailActivity.this, UrlConfig.point + 20 + "");
                            Invest_Begin_bl = false;
                            pop.popWindow.dismiss();
                            if ("XTWH".equals(obj.getString("errorCode"))) {
                                startActivity(new Intent(NewProductDetailActivity.this, WebViewActivity.class)
                                        .putExtra("URL", UrlConfig.WEIHU)
                                        .putExtra("TITLE", "系统维护"));
                                return;
                            } else if ("1002".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("产品已募集完");
                            } else if ("1003".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("项目可投资金额不足");
                            } else if ("1004".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("小于起投金额");
                            } else if ("1005".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("非递增金额整数倍");
                            } else if ("1006".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("投资金额大于项目单笔投资限额");
                            } else if ("1007".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("账户可用余额不足");
                            } else if ("1008".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("已投资过产品，不能投资新手产品");
                            } else if ("1009".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("用户不存在");
                            } else if ("9998".equals(obj.getString("errorCode"))) {
                                new show_Dialog_IsLogin(NewProductDetailActivity.this).show_Is_Login();
                            } else if ("1010".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("不符合优惠券使用条件");
                            } else if ("1011".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("投资失败,请稍后再试");
                            } else if ("1012".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("非首投用户，不能使用系统赠送的翻倍券");
                            } else if ("1013".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("活动标不能使用优惠券");
                            } else if ("1015".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("翻倍券只能用于翻倍标");
                            } else if ("3001".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("该产品尚未开始募集，请耐心等待！");
                            } else if ("3004".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("充值失败");
                            } else if ("8888".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("请重新获取短信验证码");
                            } else if ("9999".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("系统异常");
                            } else {
                                ToastMaker.showShortToast("系统异常");
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        // TODO Auto-generated method stub
                        ptrProDetail.refreshComplete();
                        dismissDialog();
                        ToastMaker.showShortToast("网络异常，请检查");
                    }
                });
    }

    private void is_Activity(String endDate) {
        /*Intent in = new Intent();
        in.putExtra("pid", pid);
        in.putExtra("tv_name", fullName);
        in.putExtra("tv_money", stringCut.douHao_Cut(et_input_amount.getText().toString().trim()));
        in.putExtra("tv_day", deadline);
        in.putExtra("tv_rate", rate);
        if ("amount".equalsIgnoreCase(nothing)) {
            in.putExtra("tv_earn", shouyi + "");
        } else {
            in.putExtra("tv_earn", shouyi + shouyi_add + "");
        }
        in.putExtra("tv_red", resultNum + "");
        in.putExtra("nothing", nothing);
        in.putExtra("investId", investId);
        in.putExtra("tv_start", tv_establish);
        in.putExtra("jumpURLActivity", jumpURLActivity);
        in.putExtra("specialRate", activityRate + "");
        in.putExtra("tv_end", tv_expireDate);
        in.putExtra("luckcode", luckcode);
        in.putExtra("luckcodesize", luckcodesize);
        in.putExtra("endTime", endDate);
        in.setClass(NewProductDetailActivity.this, Act_Pro_End.class);
        startActivityForResult(in, 4);*/
        //ToastMaker.showShortToast("支付成功");
        showPaySuccessPopWindow();
    }

    private boolean isSuccessPay = false;

    private void showPaySuccessPopWindow() {
        isSuccessPay = true;
        popupWindow.dismiss();
        popupWindow2.dismiss();
        layoutSuccess = LayoutInflater.from(NewProductDetailActivity.this).inflate(R.layout.pop_pay_success, null);
        TextView tv_prodetail_title_success = (TextView) layoutSuccess.findViewById(R.id.tv_prodetail_title_success);
        TextView tv_day_repay = (TextView) layoutSuccess.findViewById(R.id.tv_day_repay);
        TextView tv_invest_amount = (TextView) layoutSuccess.findViewById(R.id.tv_invest_amount);
        TextView tv_actual_amount = (TextView) layoutSuccess.findViewById(R.id.tv_actual_amount);
        TextView tv_expected_profit = (TextView) layoutSuccess.findViewById(R.id.tv_expected_profit);
        ListView lv_success_msg = (ListView) (layoutSuccess).findViewById(R.id.lv_success_msg);
        TextView tv_invest_complete = (TextView) layoutSuccess.findViewById(R.id.tv_invest_complete);
        TextView tv_see_investdetail = (TextView) layoutSuccess.findViewById(R.id.tv_see_investdetail);
        if (checkDeviceHasNavigationBar(NewProductDetailActivity.this) && Build.VERSION.SDK_INT <= 21) {//5.0及以下并带有虚拟按键的系统
            int virtualBarHeigh = getVirtualBarHeigh();
            LogUtils.i("showPayDetailPopWindow2:虚拟按键的高度：" + virtualBarHeigh);
            layoutSuccess.setPadding(0, 0, 0, virtualBarHeigh);
        }
        popupWindowSuccess = new PopupWindow(layoutSuccess, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        //控制键盘是否可以获得焦点
        popupWindowSuccess.setBackgroundDrawable(new BitmapDrawable());
        popupWindowSuccess.setOutsideTouchable(false);
        popupWindowSuccess.setFocusable(false);
        popupWindowSuccess.showAtLocation(layoutSuccess, Gravity.CENTER, 0, 0);

        tv_prodetail_title_success.setText(fullName);
        tv_day_repay.setText(stringCut.getDateYearToString(expireDate));
        tv_invest_amount.setText(stringCut.getNumKb(Double.parseDouble(et_input_amount.getText().toString().trim())) + "元");
        tv_actual_amount.setText(stringCut.getNumKb(Double.parseDouble(et_input_amount.getText().toString().trim())) + "元");
        tv_expected_profit.setText(stringCut.getDoubleKb(interest) + "元");
        //double shouyi = Double.parseDouble(stringCut.douHao_Cut(et_input_amount.getText().toString().trim())) * ((rate + activityRate) / 360 / 100) * Double.parseDouble(deadline);
        lv_success_msg.setAdapter(new SuccessPayAdapter(NewProductDetailActivity.this, textStringList));
        tv_invest_complete.setOnClickListener(NewProductDetailActivity.this);
        tv_see_investdetail.setOnClickListener(NewProductDetailActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isSuccessPay) {
            return true;
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    /*
    * 体验金列表选择的弹框
    * */
    private View layoutTiYanJin;
    private PopupWindow popupWindowTiYanJin;

    public void showTiYanJinList(final List<ConponsBean> listTiYanJin) {
        layoutTiYanJin = LayoutInflater.from(this).inflate(R.layout.pop_chose_tiyanjin, null);
        popupWindowTiYanJin = new PopupWindow(layoutTiYanJin, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowTiYanJin.setContentView(layoutTiYanJin);
        ImageView img_back_conpons = (ImageView) layoutTiYanJin.findViewById(R.id.img_back_conpons);
        TextView tv_youhuijuan = (TextView) layoutTiYanJin.findViewById(R.id.tv_youhuijuan);
        tv_youhuijuan.setText("选择体验金");
        RelativeLayout rl_notUse_tiyanjin = (RelativeLayout) layoutTiYanJin.findViewById(R.id.rl_notUse_tiyanjin);
        ListView lv_act_coupons = (ListView) (layoutTiYanJin).findViewById(R.id.lv_act_coupons);
        lv_act_coupons.setAdapter(new NewConponsAdapter(NewProductDetailActivity.this, listTiYanJin, "tiyanjin", null, et_input_amount.getText().toString().trim()));
        lv_act_coupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listTiYanJin.get(position).getStatus() == 3) {
                    //3=已使用和已失效
                } else {
                    ll_describe_tiyanjin.setVisibility(View.GONE);
                    tv_prodetail_tiyanjin.setVisibility(View.VISIBLE);
                    experienceId = listTiYanJin.get(position).getId();
                    double amount = listTiYanJin.get(position).getAmount();
                    enableAmount_tiYanJin = listTiYanJin.get(position).getEnableAmount();
                    tv_prodetail_tiyanjin.setText("激活" + stringCut.getNumKbs(amount) + "元体验金");
                }
                popupWindowTiYanJin.dismiss();
            }
        });


        // 控制键盘是否可以获得焦点
        popupWindowTiYanJin.setBackgroundDrawable(new BitmapDrawable());
        popupWindowTiYanJin.setOutsideTouchable(true);
        popupWindowTiYanJin.setFocusable(true);

        img_back_conpons.setOnClickListener(NewProductDetailActivity.this);
        rl_notUse_tiyanjin.setOnClickListener(NewProductDetailActivity.this);

        popupWindowTiYanJin.showAtLocation(layoutTiYanJin, Gravity.CENTER, 0, 0);
    }

    /**
     * 优惠券列表选择的弹框
     */

    private View layoutConpons;
    private PopupWindow popupWindowConpons;


    public void showPopupWindowConpons(final List<ConponsBean> lscb) {
        layoutConpons = LayoutInflater.from(this).inflate(R.layout.pop_choseconpons, null);
        popupWindowConpons = new PopupWindow(layoutConpons, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowConpons.setContentView(layoutConpons);
        ImageView img_back_conpons = (ImageView) layoutConpons.findViewById(R.id.img_back_conpons);
        TextView tv_youhuijuan = (TextView) layoutConpons.findViewById(R.id.tv_youhuijuan);
        tv_youhuijuan.setText("选择优惠券");
        RelativeLayout rl_notUse_conponslist = (RelativeLayout) layoutConpons.findViewById(R.id.rl_notUse_conponslist);
        ListView lv_act_coupons = (ListView) (layoutConpons).findViewById(R.id.lv_act_coupons);
        lv_act_coupons.setAdapter(new NewConponsAdapter(NewProductDetailActivity.this, lscb, "youhuiquan", null, et_input_amount.getText().toString().trim()));

        if (lscb.size() != 0 && lscb.size() != 0) {
            lv_act_coupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    if (lscb.get(position).getStatus() == 3) {

                    } else {
                        ll_describe_conpons.setVisibility(View.GONE);
                        tv_prodetail_conpons.setVisibility(View.VISIBLE);
                        if (lscb.get(position).getType() == 1) {//使用返现券
                            isChooseCoupon = true;
                            raisedRate = 0;
                            multiple = 1;
                            fid = lscb.get(position).getId();
                            enableAmount = lscb.get(position).getEnableAmount();
                            amountRed = lscb.get(position).getAmount();
                            nothing = "amount";
                            tv_prodetail_conpons.setText("使用" + stringCut.getNumKbs(amountRed) + "元返现红包");
                            if (et_input_amount.getText().toString().trim().length() > 0) {
                                Money_Get("amountRed");
                            }

                        } else if (lscb.get(position).getType() == 2) { // 2=加息券
                            isChooseCoupon = true;
                            amountRed = 0;
                            multiple = 1;
                            fid = lscb.get(position).getId();
                            enableAmount = lscb.get(position).getEnableAmount();
                            raisedRate = lscb.get(position).getRaisedRates();
                            tv_prodetail_conpons.setText("使用" + raisedRate + "%加息券");
                            nothing = "raisedRate";
                            if (et_input_amount.getText().toString().trim().length() > 0) {
                                Money_Get("raisedRate");
                            }

                        } else if (lscb.get(position).getType() == 3) {

                        } else if (lscb.get(position).getType() == 4) { // 翻倍
                            isChooseCoupon = true;
                            raisedRate = 0;
                            amountRed = 0;
                            fid = lscb.get(position).getId();
                            enableAmount = lscb.get(position).getEnableAmount();
                            multiple = lscb.get(position).getMultiple();
                            tv_prodetail_conpons.setText("使用" + multiple + "倍翻倍券");
                            nothing = "multiple";
                            if (et_input_amount.getText().toString().trim().length() > 0) {
                                Money_Get("multiple");
                            }
                        }
                        popupWindowConpons.dismiss();
                    }
                }
            });
        }

        // 控制键盘是否可以获得焦点
        popupWindowConpons.setBackgroundDrawable(new BitmapDrawable());
        popupWindowConpons.setOutsideTouchable(true);
        popupWindowConpons.setFocusable(true);

        img_back_conpons.setOnClickListener(NewProductDetailActivity.this);
        rl_notUse_conponslist.setOnClickListener(NewProductDetailActivity.this);

        popupWindowConpons.showAtLocation(layoutConpons, Gravity.CENTER, 0, 0);
    }


    /*
   * 计算收益
   * */
    private void Money_Get(String flag) {
        if (flag.equalsIgnoreCase("amountRed")) {
            shouyi = Double.parseDouble(stringCut.douHao_Cut(et_input_amount.getText().toString().trim()))
                    * ((rate + activityRate) / 360 / 100)
                    * Double.parseDouble(deadline);
            shouyi_add = amountRed;
            tv_prodetail_income_add.setText("+");
            tv_prodetail_income_name.setText("元(返现红包)");
        } else if (flag.equalsIgnoreCase("raisedRate")) {
            shouyi = Double.parseDouble(stringCut.douHao_Cut(et_input_amount.getText().toString().trim()))
                    * ((rate + activityRate) / 360 / 100)
                    * Double.parseDouble(deadline);
            shouyi_add = Double.parseDouble(stringCut.douHao_Cut(et_input_amount.getText().toString().trim()))
                    * (raisedRate / 360 / 100)
                    * Double.parseDouble(deadline);
            tv_prodetail_income_add.setText("+");
            tv_prodetail_income_name.setText("元(加息收益)");
        } else if (flag.equalsIgnoreCase("multiple")) {
            shouyi = Double.parseDouble(stringCut.douHao_Cut(et_input_amount.getText().toString().trim()))
                    * ((rate + activityRate) / 360 / 100)
                    * Double.parseDouble(deadline);
            shouyi_add = Double.parseDouble(stringCut.douHao_Cut(et_input_amount.getText().toString().trim()))
                    * ((rate * (multiple - 1)) / 360 / 100)
                    * Double.parseDouble(deadline);
            tv_prodetail_income_add.setText("+");
            tv_prodetail_income_name.setText("元(翻倍收益)");
        } else {
            shouyi = Double.parseDouble(stringCut.douHao_Cut(et_input_amount.getText().toString().trim()))
                    * ((rate + activityRate) / 360 / 100)
                    * Double.parseDouble(deadline);
            shouyi_add = 0.00;
            if (shouyi < 0.01) {
                tv_prodetail_income.setText("0");
            } else {
                tv_prodetail_income.setText(stringCut.getDoubleKb(shouyi));
            }
            tv_prodetail_income_add.setText("");
            tv_prodetail_income_name.setText("");
            return;
        }

        if (shouyi < 0.01) {
            tv_prodetail_income.setText("0");
        } else {
            tv_prodetail_income.setText(stringCut.getDoubleKb(shouyi));
        }
        if (shouyi_add < 0.01) {
            tv_prodetail_income_add.setText("+0");
        } else {
            tv_prodetail_income_add.setText("+" + stringCut.getDoubleKb(shouyi_add));
        }

    }

    /*
   * 计算积分(积分=投资金额*周期/1000)
   * */
    private int calculatePoints(int money) {
        int point = 0;
        if (deadline == null) {
            return point;
        }
        point = (money * Integer.valueOf(deadline)) / 1000;
        /*if (Integer.valueOf(deadline) < 90) {
        } else {
            point = (money * Integer.valueOf(deadline) * 3) / 2000;
        }*/
        return point;
    }


    /**
     * 监听输入金额
     */
    class Watcher implements TextWatcher {
        int onTextLength = 0;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onTextLength = s.length();
            if (onTextLength > 0 && rate != null) {
                et_input_amount.setHint("");
                Money_Get("");//收益
                tv_jifen.setText(calculatePoints(Integer.valueOf(et_input_amount.getText().toString().trim())) + "");//积分
                bt_prodetail_pop_ok.setText("实际支付" + et_input_amount.getText().toString().trim() + "元");

            } else {
                et_input_amount.setHint(leastaAmount + "元起投，按倍数增加");
                tv_prodetail_income.setText("0");
                tv_jifen.setText("0");
                tv_prodetail_income_add.setText("");
                tv_prodetail_income_name.setText("");
                bt_prodetail_pop_ok.setText("实际支付0元");
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.length() > 0) {

                //如果投资金额不满足红包最低，需要去掉“使用XX红包”字样
                if (Double.parseDouble(s.toString()) < enableAmount) {
                    tv_prodetail_conpons.setVisibility(View.GONE);
                    ll_describe_conpons.setVisibility(View.VISIBLE);
                    tv_num_conpons.setText(couponList.size() + "");
                    Money_Get("");//收益
                    fid = "";
                }
                if (Double.parseDouble(s.toString()) < enableAmount_tiYanJin) {
                    tv_prodetail_tiyanjin.setVisibility(View.GONE);
                    ll_describe_tiyanjin.setVisibility(View.VISIBLE);
                    tv_num_tiyanjin.setText(listFavourable.size() + "");
                    experienceId = "";
                } else {

                }
            } else {
                //check_tiyanjin.setClickable(true);
                //check_tiyanjin.setChecked(false);
            }
        }
    }

    /**
     * 当没选择优惠券,就支付时,判断 是否有可使用的优惠券,
     */
    private boolean isCanSelectCoupon(List<ConponsBean> lsct) {
        String trim = et_input_amount.getText().toString().trim();
        LogUtils.e("isChooseCoupon" + isChooseCoupon);
        if (isChooseCoupon) {
            return false;
        } else {
            if (lsct.size() != 0) {
                for (int i = 0; i < lsct.size(); i++) {
                    if (Double.valueOf(trim) >= (lsct.get(i).getEnableAmount())) {//可用
                        return true;
                    }
                }
            }
        }
        return false;
    }

    class TabFragPA extends FragmentPagerAdapter {

        public TabFragPA(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            switch (arg0) {
                case 0://产品介绍
                    frag1 = frag1 == null ? new ProductIntroduceFragment() : frag1;
                    frag1.setArguments(bundle);
                    return frag1;
                case 1://投资排行？投资记录
                    if (!TextUtils.isEmpty(distributionId) && Integer.valueOf(distributionId) > 0) {
                        frag = frag == null ? new InvestRankingListFragment() : frag;
                    } else {
                        frag = frag == null ? new Frag_ProDetails_record() : frag;
                    }
                    frag.setArguments(bundle);
                    return frag;
                case 2://常见问题
                    frag2 = frag2 == null ? new CommonProblemFragment() : frag2;
                    frag2.setArguments(bundle);
                    return frag2;
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tab[position % tab.length];
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return tab.length;
        }
    }

    private int count;
    private Boolean isRun;
    private int time = 1;

    // 计时器
    public void time() {
        count = 60;
        isRun = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRun) {
                    count--;
                    handler.sendEmptyMessage(10);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    public void stopTimer() {
        if (tv_getYzm == null) {
            return;
        }
        tv_getYzm.setEnabled(true);
        tv_getYzm.setText("获取验证码");
        tv_getYzm.setBackgroundResource(R.drawable.bg_corner_kong_blue);
        tv_getYzm.setTextColor(0xFF20A3F9);
        isRun = false;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    if (count <= 0) {
                        time = 1;
                        stopTimer();
                    } else {
                        tv_getYzm.setEnabled(false);
                        tv_getYzm.setTextColor(0xffcccccc);
                        tv_getYzm.setBackgroundResource(R.drawable.bg_corner_blackline);
                        tv_getYzm.setText("发送(" + count + ")秒");
                    }

                    break;
                default:
                    break;
            }
        }
    };
}
