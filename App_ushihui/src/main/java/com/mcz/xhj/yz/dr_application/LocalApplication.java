package com.mcz.xhj.yz.dr_application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.mcz.xhj.yz.dr_app.Detail_Piaoju_ActFirst;
import com.mcz.xhj.yz.dr_app.FriendAct;
import com.mcz.xhj.yz.dr_app.MainActivity;
import com.mcz.xhj.yz.dr_app.WebViewActivity;
import com.mcz.xhj.yz.dr_app.me.NewMessageCenterActivity;
import com.mcz.xhj.yz.dr_util.LogUtils;

import java.util.Map;

/**
 * 在开发应用时都会和Activity打交道，而Application使用的就相对较少 Application是用来管理应用程序的全局状态的，
 * 比如载入资源文件 在应用程序启动的时候Application会首先创建，然后才会根据情况(Intent)启动相应的Activity或者Service
 *
 * @author blue
 */
public class LocalApplication extends BaseApplication {
    public static String localVersion;// 本地安装版本
    private static LocalApplication instance;
    public static Map<String, Long> map;
    public String channelName = "";
    public static int time = 1;

//	public DbUtils dbUtils = null;

//	public HttpUtils httpUtils = null;

    // 当前屏幕的高宽
    public int screenW = 0;
    public int screenH = 0;

    // 单例模式中获取唯一的MyApplication实例
    public static LocalApplication getInstance() {
        if (instance == null) {
            instance = new LocalApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PackageManager packageManager = getApplicationContext().getPackageManager();
        if (packageManager != null) {
            //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
            ApplicationInfo applicationInfo;
            try {
                applicationInfo = packageManager.getApplicationInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
                channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
                if (channelName == null) {
                    channelName = "xhj";
                }
            } catch (NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //UMConfigure.init(this, "5ac18906f29d9824b40000a8", "Umeng",
         //       UMConfigure.DEVICE_TYPE_PHONE, "ac0fc92e803247c38e577c0a71d7808f");

         //UMConfigure.init(this, "5ac18906f29d9824b40000a8", channelName,
            //    UMConfigure.DEVICE_TYPE_PHONE, "ac0fc92e803247c38e577c0a71d7808f");
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "ac0fc92e803247c38e577c0a71d7808f");

        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);

        //微信 appid appsecret
        PlatformConfig.setWeixin("wxb43c4c1d66c4ed74", "091ab48accd680905dfe88842a86dd68");
        //QQ和Qzone appid appkey
        //PlatformConfig.setQQZone("1106431341", "xzVS26uAUUQBBs1f");
        PlatformConfig.setQQZone("1106713639", "TS2yGoJIv6hhLSEn");
        Config.DEBUG = true;

        initUMengPush();


        //获取本地app的版本
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 初始化数据库
//		dbUtils = DbUtils.create(LocalDaoConfig.getInstance(getApplicationContext()));

        // 初始化网络模块
//		httpUtils = new HttpUtils();
//		OkHttpUtils.getInstance().setConnectTimeout(15000, TimeUnit.MILLISECONDS);
//		OkHttpUtils.getInstance().setCertificates();//默认信任所有https
//		OkHttpClient okHttpClient = new OkHttpClient.Builder()
//      .addInterceptor(new LoggerInterceptor("TAG"))
//        .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//        .readTimeout(10000L, TimeUnit.MILLISECONDS)
//        //其他配置
//       .build();
//
//		OkHttpUtils.initClient(okHttpClient);
        //初始化图片加载框架
        Fresco.initialize(getApplicationContext());
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);

        //Initialize ImageLoader with configuration.  
        ImageLoader.getInstance().init(configuration);

        instance = this;
        editor = sharereferences.edit();
        // 得到屏幕的宽度和高度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenW = dm.widthPixels;
        screenH = dm.heightPixels;


        CrashHandler.getInstance().init(this);


        String umeng_channel = getChannel(this, "UMENG_CHANNEL");
        LogUtils.e("渠道"+umeng_channel);
    }
    //key为渠道名的key，对应友盟的 UMENG_CHANNEL
    private String getChannel(Context context,String key) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";
    }


    public MainActivity MainActivity;


    private MainActivity TaskCenterActivity;
    private WebViewActivity WebViewActivity;
    private Detail_Piaoju_ActFirst Detail_Piaoju_ActFirst;
    private FriendAct friendAct;
    private SharedPreferences.Editor editor;
    private PushAgent mPushAgent;

    public FriendAct getFriendAct() {
        return friendAct;
    }

    public void setFriendAct(FriendAct friendAct) {
        this.friendAct = friendAct;
    }

    public Detail_Piaoju_ActFirst getDetail_Piaoju_ActFirst() {
        return Detail_Piaoju_ActFirst;
    }

    public void setmDetail_Piaoju_ActFirst(
            Detail_Piaoju_ActFirst Detail_Piaoju_ActFirst) {
        this.Detail_Piaoju_ActFirst = Detail_Piaoju_ActFirst;
    }

    public WebViewActivity getWebViewActivity() {
        return WebViewActivity;
    }

    public void setWebViewActivity(WebViewActivity webViewActivity) {
        WebViewActivity = webViewActivity;
    }

    public MainActivity getMainActivity() {
        return MainActivity;
    }

    public void setMainActivity(MainActivity MainActivity) {
        this.MainActivity = MainActivity;
    }


//	@Override
//	public BaseExceptionHandler getDefaultUncaughtExceptionHandler()
//	{
//		return new LocalFileHandler(applicationContext);
//	}

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void initUMengPush() {

        //UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "ac0fc92e803247c38e577c0a71d7808f");


        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setNoDisturbMode(0, 0, 0, 0); //关闭免打扰时间
        mPushAgent.setDisplayNotificationNumber(0);//0-10 当参数为0时，表示不合并通知
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //声音
        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);//呼吸灯
        mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动
       // mPushAgent.setDebugMode(false);
        //注册推送服务，每次调用register方法都会回调该接口
        LogUtils.e("注册友盟推送");



        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
//				System.out.println("sc======deviceToken=="+deviceToken);
                //注册成功会返回device token
//				SharedPreferences.Editor editor = preferences.edit();
                LogUtils.e("--->注册友盟推送，deviceToken："+deviceToken);
                editor.putString("deviceToken", deviceToken);
                if (LocalApplication.getInstance().sharereferences.getBoolean("FirstLog", true)) {
                    editor.putBoolean("isOpenUpush", true);
                }
                editor.commit();
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.e("--->注册友盟推送失败，s=="+ s + " ,s1==" + s1);
                System.out.println("sc======onFailure==" + s + "==s1==" + s1);
//				SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isOpenUpush", false);
                editor.commit();
            }
        });




        /**
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                // Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                // LogUtils.e("自定义行为的回调处理");
                // LogUtils.e("Umeng"+msg.toString());

            }

            @Override
            public void launchApp(Context context, UMessage uMessage) {

                //super.launchApp(context, uMessage);
                //LogUtils.e("自定义行为的回调处理");
                LogUtils.e("Umeng"+uMessage.title);
                // LogUtils.e("Umeng"+msg.custom+msg.msg_id);
               /* Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("URL", UrlConfig.WEBSITEAN + "?app=true&id=" + uMessage.msg_id);
                intent.putExtra("TITLE", "平台公告");
                startActivity(intent);*/

                if (sharereferences.getString("uid", "").equalsIgnoreCase("")) {
                    super.launchApp(context, uMessage);
                } else {
                    startActivity(new Intent(context, NewMessageCenterActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }


            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        //mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
        //注册推送服务 每次调用register都会回调该接口
       /* mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                LogUtils.e("device token: " + deviceToken);
                //sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.e("register failed: " + s + " " + s1);
                //sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }
        });*/
       /* new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).run();*/

    }
}
