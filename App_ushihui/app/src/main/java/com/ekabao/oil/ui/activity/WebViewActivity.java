package com.ekabao.oil.ui.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.InviteBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.me.CashInActivity;
import com.ekabao.oil.ui.activity.me.CashOutActivity;
import com.ekabao.oil.ui.activity.me.FourPartActivity;
import com.ekabao.oil.ui.activity.me.MeWelfareActivity;
import com.ekabao.oil.ui.view.CustomShareBoard;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.show_Dialog_IsLogin;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class WebViewActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.wv)
    WebView webView;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;


    private String TITLE;
    private String PID;
    private String afid;
    private String BANNER = null; //首页的轮播图点进来的
    private static String URL = null;

  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }


    @Override
    protected void onResume() {
        super.onResume();
//		webView.reload();
//		webView.goBack();
//		webView.reload();
//		preferences.getString("uid", "");
        if (URL.contains("special")) {  //iphone7活动
            titleRightimageview.setVisibility(View.VISIBLE);
        }

        if (URL.contains("app2lottery")) {//翻牌活动
            titleRightimageview.setVisibility(View.VISIBLE);
            if (URL.contains("?")) {
                webView.loadUrl(URL + "&uid=" + preferences.getString("uid", "") + "&channel=2" + "&version=" + UrlConfig.version);
                LogUtils.i("--->loadUrl URL1：" + URL + "&uid=" + preferences.getString("uid", "") + "&channel=2" + "&version=" + UrlConfig.version);
            } else {
//				String test = URL+"?uid="+preferences.getString("uid", "");
                webView.loadUrl(URL + "?uid=" + preferences.getString("uid", "") + "&channel=2" + "&version=" + UrlConfig.version);
                LogUtils.i("--->loadUrl URL2：" + URL + "?uid=" + preferences.getString("uid", "") + "&channel=2" + "&version=" + UrlConfig.version);
            }
        } else {
            if (BANNER != null && !BANNER.equalsIgnoreCase("")) {
                if (URL.contains("?")) { //加载首页轮播图的
                    webView.loadUrl(URL + "&uid=" + preferences.getString("uid", "") + "&token=" + preferences.getString("token", ""));
                    LogUtils.i("--->loadUrl URL3：" + URL + "&uid=" + preferences.getString("uid", "") + "&token=" + preferences.getString("token", ""));
                } else {
                    //String test = URL+"?uid="+preferences.getString("uid", "");
                    webView.loadUrl(URL + "?uid=" + preferences.getString("uid", "") + "&token=" + preferences.getString("token", ""));
                    LogUtils.i("--->loadUrl URL4：" + URL + "?uid=" + preferences.getString("uid", "") + "&token=" + preferences.getString("token", ""));
                }
            } else {

                if (afid != null && !afid.equalsIgnoreCase("")) {
                    if (URL.contains("?")) {
                        webView.loadUrl(URL + "&uid=" + preferences.getString("uid", "") + "&afid=" + afid + "&token=" + preferences.getString("token", ""));
                        LogUtils.i("--->loadUrl URL5：" + URL + "&uid=" + preferences.getString("uid", "") + "&afid=" + afid + "&token=" + preferences.getString("token", ""));
                    } else {
                        webView.loadUrl(URL + "?uid=" + preferences.getString("uid", "") + "&afid=" + afid + "&token=" + preferences.getString("token", ""));
                        LogUtils.i("--->loadUrl URL6：" + URL + "?uid=" + preferences.getString("uid", "") + "&afid=" + afid + "&token=" + preferences.getString("token", ""));
                    }
                } else {
                    webView.loadUrl(URL);
                    LogUtils.i("--->loadUrl URL7：" + URL);
                }
            }
        }
    }


    @JavascriptInterface
    @Override
    protected void initParams() {
        LocalApplication.getInstance().setWebViewActivity(this);
        URL = getIntent().getExtras().getString("URL");
//		URL = "https://m.v.qq.com/play/play.html?coverid=&vid=m0353ua4gmd";
//		URL = "https://v.qq.com/iframe/player.html?vid=m0353ua4gmd&tiny=0&auto=0";
        afid = getIntent().getExtras().getString("AFID");
        TITLE = getIntent().getExtras().getString("TITLE");
        PID = getIntent().getExtras().getString("PID");
        BANNER = getIntent().getExtras().getString("BANNER");

        LogUtils.e("--->WebView: URL=" + URL + " ,afid=" + afid + " ,Title=" + TITLE + " ,pid=" + PID + " ,banner=" + BANNER);
        titleCentertextview.setText(TITLE);
        titleLeftimageview.setOnClickListener(this);
        titleRightimageview.setOnClickListener(this);
        /**
         *
         * 设置WebView的属性，此时可以去执行JavaScript脚本
         */
        final WebSettings settings = webView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setSupportZoom(true);
        // int fontSize = 10;
        // settings.setTextSize(TextSize.NORMAL);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true); // 设置显示缩放按钮
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(20000);
        settings.setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new Object() {
                public void setLoadWithOverviewMode(boolean overview) {
                    settings.setLoadWithOverviewMode(overview);

                }
            }.setLoadWithOverviewMode(true);
        }
        //settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new MyWebView() {
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                // handler.cancel(); // Android默认的处理方式
                handler.proceed(); // 接受所有网站的证书
                // handleMessage(Message msg); // 进行其他处理
            }
        });
        webView.setWebViewClient(new MyWebView());
//		webView.setWebChromeClient(new WebChromeClient());
//		webView.setWebChromeClient(new WebChromeClient() {
//			@Override
//			public boolean onJsAlert(WebView view, String url, String message,
//									 JsResult result) {
//				//加这段可以证webview中的alert弹出来
//				return super.onJsAlert(view, url, message, result);
//			}
//		});
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_leftimageview) {
            if (webView.canGoBack()) {    //判断是否返回
                webView.goBack();
            } else {
                finish();
            }
//			webView.goBack();
//			finish();
        } else if (v.getId() == R.id.title_rightimageview) {
            if (preferences.getBoolean("login", false)) {
                if (URL.contains("app2lottery")) {
                    postShare(UrlConfig.FAIPAI, "app2lottery", "");//翻牌活动
                } else if (URL.contains("special")) {
                    postShare(UrlConfig.IPHONE7, "special", "");//iphone7活动
                }
            } else {
                WebViewActivity.this.startActivityForResult(new Intent(WebViewActivity.this, LoginActivity.class), 3);
            }
        }
    }

    private void postShare(String url, String flag, String giftMoney) {
//		CustomShareBoard shareBoard = new CustomShareBoard(WebViewActivity.this,"",url);
        CustomShareBoard shareBoard = new CustomShareBoard(WebViewActivity.this, "", url, flag, giftMoney);
        shareBoard.showAtLocation(WebViewActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private boolean flag = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("requestCode " + requestCode + "resultCode" + resultCode);
        if (requestCode == 3 && resultCode == 3) {
            setResult(3);
            finish();
        } else if (requestCode == 3 && resultCode == 0) { //从登录页面返回回来时
            //webView.reload(); //刷新
            finish();
            webView.clearHistory();
            tvEmpty.setVisibility(View.GONE);
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private SharedPreferences preferences = LocalApplication.sharereferences;

    class MyWebView extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.e("--->shouldOverride url==" + url);
            if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=1")) {//邀请好友三重礼1
                if (!preferences.getBoolean("login", false)) {
                    startActivityForResult(new Intent(WebViewActivity.this, LoginActivity.class), 1);
                    return true;
                } else {
                    startActivity(new Intent(WebViewActivity.this, InviteFriendsActivity.class));

                    //LocalApplication.getInstance().getMainActivity().setCheckedFram(3);
                    setResult(3);
                    finish();
                }
                return true;
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=2")) {//我要出借(出借悦)2
                if (!preferences.getBoolean("login", false)) {
                    startActivityForResult(new Intent(WebViewActivity.this, LoginActivity.class), 1);
                    return true;
                } else {
                    LocalApplication.getInstance().getMainActivity().setCheckedFram(2);
                    setResult(3);
                    finish();
                }
                return true;
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=3")) {//登录3
                if (preferences.getBoolean("login", false)) {
                    ToastMaker.showLongToast("当前账号已登录请退出后重试");
                    return true;
                } else {

                    WebViewActivity.this.startActivityForResult(new Intent(WebViewActivity.this,
                            LoginActivity.class), 3);
                    finish();
                }
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=4")) {//注册4
                flag = true;
                if (preferences.getBoolean("login", false)) {
                    ToastMaker.showLongToast("当前账号已登录请退出后重试");
                    return true;
                } else {
                    //WebViewActivity.this.startActivityForResult(new Intent(WebViewActivity.this, LoginQQPswAct.class).putExtra("phone",url.substring(url.indexOf("?")+7,url.length())),3);
                    WebViewActivity.this.startActivityForResult(
                            new Intent(WebViewActivity.this,
                                    RegisterActivity.class), 3);
                    finish();
                }
            } else if (url.startsWith("jsmp") && url.substring(7, 13).equalsIgnoreCase("page=5")) {//金服详情5
                PID = url.substring(18);
                LogUtils.i("--->跳转金服详情的PID：" + PID);
                startActivity(new Intent(WebViewActivity.this, ProductDetailActivity.class).putExtra("pid", PID));
                finish();
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=6")) {//我的账户6
                LocalApplication.getInstance().getMainActivity().setCheckedFram(4);
                setResult(3);
                finish();
                return true;
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=7")) {//套餐充值
                // memberSetting(1);
                startActivity(new Intent(WebViewActivity.this,
                        OilCardPackageActivity.class));

                return true;
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=8")) {//立即充值
                // memberSetting(2);

                startActivity(new Intent(WebViewActivity.this,
                        OilCardImmediateActivity.class));

                return true;
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=9")) {//银行卡认证9
                memberSetting(3);
                return true;
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=10")) {//跳转微信10
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);

                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("gongzh onghao", getResources().getString(R.string.weixin));
                    cm.setPrimaryClip(clip);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(WebViewActivity.this, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
                }
                return true;
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=11")) {//优惠券11
                if (!preferences.getBoolean("login", false)) {
                    startActivityForResult(new Intent(WebViewActivity.this, LoginActivity.class), 1);
                    return true;
                } else {
                    startActivity(new Intent(WebViewActivity.this, MeWelfareActivity.class));
                    return true;
                }
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=12")) {//任务中心12
                if (!preferences.getBoolean("login", false)) {
                    startActivityForResult(new Intent(WebViewActivity.this, LoginActivity.class), 1);
                    return true;
                } else {
                    startActivity(new Intent(WebViewActivity.this, TaskCenterActivity.class));
                    return true;
                }
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=13")) {//积分榜单13
                if (!preferences.getBoolean("login", false)) {
                    startActivityForResult(new Intent(WebViewActivity.this, LoginActivity.class), 1);
                    return true;
                } else {
                    startActivity(new Intent(WebViewActivity.this, ScoreboardActivity.class));
                    return true;
                }
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=14")) {//我的邀请: 14
                if (!preferences.getBoolean("login", false)) {
                    startActivityForResult(new Intent(WebViewActivity.this, LoginActivity.class), 1);
                } else {
                    startActivity(new Intent(WebViewActivity.this, InviteFriendsActivity.class));
                }
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=15")) {//h5-->我的邀请:15
                if (!preferences.getBoolean("login", false)) {
                    startActivityForResult(new Intent(WebViewActivity.this, LoginActivity.class), 1);
                    return true;
                } else {
                    // startActivity(new Intent(WebViewActivity.this, InviteFriendsActivity.class));
                    postShare(UrlConfig.ZHUCEZHENGCHANG);
                    return true;
                }
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=16")) {//h5-->我的邀请:15
                if (!preferences.getBoolean("login", false)) {
                    startActivityForResult(new Intent(WebViewActivity.this, LoginActivity.class), 1);
                    return true;
                } else {
                    // startActivity(new Intent(WebViewActivity.this, InviteFriendsActivity.class));
                    //postShare(UrlConfig.ZHUCEZHENGCHANG);
                    getList();

                    return true;
                }
            } else if (url.startsWith("jsmp") && url.substring(7).equalsIgnoreCase("page=17")) {//h5-->商城

                MainActivity activity = new MainActivity();
                //  activity.setOilFragment(homeHostProduct.get(position).getId(), 500, homeHostProduct.get(position).getDeadline());
                activity.switchFragment(2);
//                activity.resetTabState();

               // startActivity(new Intent(WebViewActivity.this, MallHomeActivity.class));

                finish();
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            LogUtils.i("--->onPageStarted url==" + url);
            super.onPageStarted(view, url, favicon);
            showWaitDialog("加载中...", true, "");

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            LogUtils.i("--->onPageFinished url==" + url);
            super.onPageFinished(view, url);
            if (flag) {
                webView.clearHistory();
                flag = false;
            }
            dismissDialog();

        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {

            LogUtils.i("--->onReceivedError failingUrl==" + failingUrl + " ,description==" + description);
            super.onReceivedError(view, errorCode, description, failingUrl);
            tvEmpty.setVisibility(View.VISIBLE);
            //ToastMaker.showShortToast("加载失败");
        }

    }

    private boolean bl() {
        return false;
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

                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            String isRealName = map.getString("realVerify");
                            if ("1".equals(isRealName)) {
                                if (flag == 1) {
                                    startActivity(new Intent(WebViewActivity.this, CashInActivity.class));
                                    finish();
                                } else if (flag == 2) {
                                    startActivity(new Intent(WebViewActivity.this, CashOutActivity.class));
                                    finish();
                                } else if (flag == 3) {
                                    startActivity(new Intent(WebViewActivity.this, FourPartActivity.class));
                                    finish();
                                }
                            } else {
                                ToastMaker.showShortToast("您还未实名认证");
                                startActivity(new Intent(WebViewActivity.this, FourPartActivity.class));
                                finish();
                            }
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(WebViewActivity.this).show_Is_Login();
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

    @Override
    protected synchronized void onDestroy() {
        super.onDestroy();
        webView.setWebChromeClient(null);
        webView.setWebChromeClient(null);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.clearCache(true);
    }

    @Override
    public void onButtonClicked(Dialog dialog, int position, Object tag) {

        super.onButtonClicked(dialog, position, tag);
        if (position == 1 && PID != null && !PID.equalsIgnoreCase("")) {

            //新手体验
           /* WebViewActivity.this.startActivity(new Intent(WebViewActivity.this,
                    Detail_New_ActFirst.class).putExtra("pid", PID));*/
            finish();
        }

    }

    /**
     * 分享
     */
    private void postShare(String url) {
        CustomShareBoard shareBoard = new CustomShareBoard(this, "", url, "zhengchang", "");
        shareBoard.showAtLocation(webView, Gravity.BOTTOM, 0, 0);
    }


    /**
     * 好友充值记录
     */
    private ArrayList<String> list = new ArrayList<>();

    private void getList() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.friendRankingList)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                //ptr_address.refreshComplete();
                LogUtils.e("好友充值记录：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    boolean mylist = map.containsKey("mylist");
                    if (mylist) {
                        JSONArray list = map.getJSONArray("mylist");
                        // LogUtils.e("所有的收货地址：" + list.size());
                        if (list.size() > 0) {
                            list.clear();
                            List<InviteBean> list1 = JSON.parseArray(list.toJSONString(), InviteBean.class);
                            for (int i = 0; i < list1.size(); i++) {
                                String dateYearToStrings = StringCut.getDateYearToStrings(list1.get(i).getInvestTime());
                                String message = list1.get(i).getMessage();
                                LogUtils.e(dateYearToStrings + message);
                                list.add(dateYearToStrings + message);
                            }
                        }
                    }
                    DialogMaker.showInviteDialog(WebViewActivity.this, list);
                }

            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                // ptr_address.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }
}
