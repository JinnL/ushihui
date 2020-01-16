package com.mcz.xhj.yz.dr_app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.mcz.xhj.yz.dr_adapter.APSTSViewPager;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.FriendBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.CustomShareBoard;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;
import lib.lhh.fiv.library.FrescoImageView;
import okhttp3.Call;


/*
*  1.0老界面（别被名称误导）
* */
public class NewMainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private FragmentTransaction transaction;// 开启fragment事務
    private HomeFrag home = null;
    private InvestFrag invest = null;
    private NewPersonFrag person = null;
    private ProfitFrag profitFrag = null;//返利
    @BindView(R.id.mAPSTS)
    @Nullable
    AdvancedPagerSlidingTabStrip mAPSTS;
    @BindView(R.id.mVP)
    @Nullable
    APSTSViewPager mVP;
    @BindView(R.id.pop_main)
    @Nullable
    RelativeLayout pop_main;
    private static final int VIEW_FIRST = 0;
    private static final int VIEW_SECOND = 1;
    private static final int VIEW_THIRD = 2;
    private static final int VIEW_FOURTH = 3;
    private static final int VIEW_SIZE = 4;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    public static Boolean isHome = false, isHomeChecked = false,
            isLoginPsw = false, isInvestChecked = false, isInvest = false,
            isLog = false, isMore = false, hasNavigationBar = false, isActivity = false, isExit = false,isPersonChecked = false;

    private InvestFrag frag;
    private String btnUrl;//中间按钮图片url
    private List<FriendBean> list;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_main;
    }

    public InvestFrag getFrag() {
        return frag;
    }

    public void setFrag(InvestFrag frag) {
        this.frag = frag;
    }

    // 获取是否存在NavigationBar
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
        LogUtils.i("hasNavigationBar："+hasNavigationBar);
        return hasNavigationBar;

    }

    @Override
    public void onButtonClicked(Dialog dialog, int position, Object tag) {
        // TODO Auto-generated method stub
        if (position == 0) {
            ToastMaker.showShortToast("可以在安全中心-手势密码 中进行修改");
        }
        if (position == 1) {
            startActivity(new Intent(NewMainActivity.this, GestureEditActivity.class));
        }
    }

    private Drawable img_top_home;
    private Drawable img_top_pro;
    private Drawable img_top_acount;
    private Drawable img_top_more;

    /**
     * 根据活动改变底部导航图标
     */
    private void getData() {
        OkHttpUtils.post()
                .url(UrlConfig.ACTIVITYLIST)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("status", "2")
                .addParams("type", "2")
                .addParams("pageOn", "1")
                .addParams("pageSize", "200")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                LogUtils.i("--->底部中间按钮的活动：" + result);
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONObject objinfo = objmap.getJSONObject("Page");
                    btnUrl = objmap.getString("btnUrl");
                    JSONArray objrows = objinfo.getJSONArray("rows");
                    list = JSON.parseArray(objrows.toJSONString(), FriendBean.class);
                    if(list.size()>0){
                        //显示底部中间按钮

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

    @Override
    protected void initParams() {
        //LocalApplication.getInstance().setMainActivity(this);
        getData();
        LogUtils.i("--->NewMainActivity----->initParams()");
        if (checkDeviceHasNavigationBar(NewMainActivity.this)) {
            pop_main.setFitsSystemWindows(true);
        }
        if (getIntent().getStringExtra("location") != null && getIntent().getStringExtra("title") != null) {
            startActivity(new Intent(NewMainActivity.this, WebViewActivity.class)
                    .putExtra("BANNER", "toujisong")
                    .putExtra("URL", getIntent().getStringExtra("location"))
                    .putExtra("TITLE", getIntent().getStringExtra("title")));
        }
        mVP.setOffscreenPageLimit(VIEW_SIZE);
        //FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        //adapter.notifyDataSetChanged();
        mVP.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        mAPSTS.setViewPager(mVP);
        mAPSTS.setOnPageChangeListener(this);
        mVP.setCurrentItem(VIEW_FIRST);

        if (LocalApplication.getInstance().sharereferences.getBoolean("login", false)) {
            if (LocalApplication.getInstance().sharereferences.getBoolean("loginshoushi", false)) {
                startActivity(new Intent(NewMainActivity.this, GestureEditActivity.class).putExtra("flag", "wellcome"));
            }
        }
        if (!TextUtils.isEmpty(preferences.getString("uid", ""))) {
            pushRegisterId();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class FragmentAdapter extends FragmentStatePagerAdapter implements AdvancedPagerSlidingTabStrip.ViewTabProvider {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position >= 0 && position < VIEW_SIZE) {
                switch (position) {
                    case VIEW_FIRST:
                        if (null == frag)
                            home = home.instance();
                        return home;

                    case VIEW_SECOND:
                        if (null == invest)
                            invest = invest.instance();
                        return invest;

                    case VIEW_THIRD:
                        if (null == profitFrag)
                            profitFrag = profitFrag.instance();
                        return profitFrag;

                    case VIEW_FOURTH:
                        if (null == person)
                            person = person.instance();
                        return person;
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return VIEW_SIZE;
        }

        @Override
        public View onSelectIconView(int position, View view, ViewGroup parent) {
            FrescoImageView draweeView;
            if (view == null) {
                draweeView = new FrescoImageView(NewMainActivity.this);
                draweeView.setLayoutParams(new RelativeLayout.LayoutParams(70, 70));
                view = draweeView;
            }
            draweeView = (FrescoImageView) view;
            switch (position) {
                case VIEW_FIRST:
                    draweeView.loadView("", R.mipmap.ico_home_press);
                    break;
                case VIEW_SECOND:
                    draweeView.loadView("", R.mipmap.ico_fund_press);
                    break;
                case VIEW_THIRD:
                    draweeView.loadView("", R.mipmap.ico_more_press);
                    break;
                case VIEW_FOURTH:
                    isPersonChecked = true;
                    draweeView.loadView("", R.mipmap.ico_account_press);
                    if (preferences.getString("uid", "").equalsIgnoreCase("") && isPersonChecked) {
                        showPopupWindowLogin();
                    }
                    break;
                default:
                    break;
            }
            return draweeView;
        }

        @Override
        public View onIconView(int position, View view, ViewGroup parent) {
            FrescoImageView draweeView;
            if (view == null) {
                draweeView = new FrescoImageView(NewMainActivity.this);
                draweeView.setLayoutParams(new RelativeLayout.LayoutParams(70, 70));
                view = draweeView;
            }
            draweeView = (FrescoImageView) view;
            switch (position) {
                case VIEW_FIRST:
                    draweeView.loadView("", R.mipmap.ico_home);
                    break;
                case VIEW_SECOND:
                    draweeView.loadView("", R.mipmap.ico_fund);
                    break;
                case VIEW_THIRD:
                    draweeView.loadView("", R.mipmap.ico_more);
                    break;
                case VIEW_FOURTH:
                    draweeView.loadView("", R.mipmap.ico_account);
                    break;
                default:
                    break;
            }
            return draweeView;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position >= 0 && position < VIEW_SIZE) {
                switch (position) {
                    case VIEW_FIRST:
                        return "发现";
                    case VIEW_SECOND:
                        return "投资";
                    case VIEW_THIRD:
                        return "返利";
                    case VIEW_FOURTH:
                        return "我的";
                    default:
                        break;
                }
            }
            return null;
        }

    }

    private View layout;
    private PopupWindow popupWindow;

    public void showPopupWindowLogin() {
        // 加载布局
        layout = LayoutInflater.from(NewMainActivity.this).inflate(R.layout.new_pop_person_login, null);
        // 找到布局的控件
        // 实例化popupWindow
        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        TextView bt_login = (TextView) (layout).findViewById(R.id.bt_login);
        // 控制键盘是否可以获得焦点
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
//		// 设置popupWindow弹出窗体的背景
//		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 监听
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroundAlpha(1f);
//            }
//        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(NewMainActivity.this, NewLoginActivity.class).putExtra("point", "personFrag"));
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
        /*if (isHomeChecked) {
            mVP.setCurrentItem(0);
            isHomeChecked = false;
        }
        if (isInvestChecked) {
            mVP.setCurrentItem(1);
            isInvestChecked = false;
        }
        if (isLog) {
            mVP.setCurrentItem(2);
            isLog = false;
        }
        if (isMore) {
            mVP.setCurrentItem(3);
            isMore = false;
        }*/
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }


    public void postShare(String pid, String url) {
        CustomShareBoard shareBoard = new CustomShareBoard(NewMainActivity.this, pid, url, "");
        shareBoard.showAtLocation(NewMainActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//友盟统计需要的回调
    }

    //设置首页跳转各个fragment
    public void setCheckedFram(int checkedFram) {
        switch (checkedFram) {
            case 1:
                mVP.setCurrentItem(0);
                break;
            case 2:
                mVP.setCurrentItem(1);
                break;
            case 3:
                mVP.setCurrentItem(2);
                break;
            case 4:
                mVP.setCurrentItem(3);
                break;
        }
    }

    private long mPressedTime = 0;

    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();// 获取第一次按键时间
        if ((mNowTime - mPressedTime) > 2000) {// 比较两次按键时间差
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        } else {// 退出程序
            this.finish();
            System.exit(0);
        }
    }
}
