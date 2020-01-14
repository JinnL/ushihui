package com.ekabao.oil.global;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;

import com.awen.photo.FrescoImageLoader;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.ui.activity.MainActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.util.LogUtils;
import com.scwang.smartrefresh.header.DropBoxHeader;
import com.scwang.smartrefresh.header.PhoenixHeader;
import com.scwang.smartrefresh.header.StoreHouseHeader;
import com.scwang.smartrefresh.header.TaurusHeader;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * Created by Administrator on 2018/5/28.
 */

public class LocalApplication extends Application {

    // 以键值对的形式存储用户名和密码
    public static SharedPreferences sharereferences;

    public static String localVersion;// 本地安装版本
    private static LocalApplication instance;
    public static Map<String, Long> map;
    public String channelName = "";
    public static int time = 1;

    // 当前屏幕的高宽
    public int screenW = 0;
    public int screenH = 0;


    // 小米的 user your appid the key.
    private static final String APP_ID = "2882303761517970955";
    //小米的  user your appid the key.
    private static final String APP_KEY = "5611797038955";

    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep
    // com.xiaomi.mipushdemo
    public static final String TAG = "com.akzj.oil";

    private PushAgent mPushAgent;
    private SharedPreferences.Editor editor;

    public static Context context;

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

        CrashHandler.getInstance().init(this);

        // 初始化键值对存储
        sharereferences = this.getSharedPreferences("userinfo", MODE_PRIVATE);
        editor = sharereferences.edit();
        context = this;
        instance = this;

        PackageManager packageManager = getApplicationContext().getPackageManager();
        if (packageManager != null) {
            //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
            ApplicationInfo applicationInfo;
            try {
                applicationInfo = packageManager.getApplicationInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
                channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
                if (channelName == null) {
                    channelName = "ekabao";
                }
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        LogUtils.e("channelName--->" + channelName);


        UMConfigure.init(this, UrlConfig.PUSHAPPKEY, channelName,
              UMConfigure.DEVICE_TYPE_PHONE, "789cd17412a343aa33d78c1e43a18fbf");

        //UMConfigure.init(this, "5ac18906f29d9824b40000a8", channelName,
        //    UMConfigure.DEVICE_TYPE_PHONE, "ac0fc92e803247c38e577c0a71d7808f");
        //5afab0f3f29d981d4200000c
       // UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "15129ca6123affa19a20252814aad404");

        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);

        //微信 appid appsecret wxb43c4c1d66c4ed74  091ab48accd680905dfe88842a86dd68
       /* PlatformConfig.setWeixin("wx7f4b34f55b5ab4f1", "4df65bbf57255e1132fbb400ba93ebe5");
        //QQ和Qzone appid appkey
        //PlatformConfig.setQQZone("1106431341", "xzVS26uAUUQBBs1f");
        PlatformConfig.setQQZone("1106870062", "hW0BWJ3wOi6jSCfN");*/

//        PlatformConfig.setWeixin("wxeaddf8c3c913a86e", "a49938065be4ed86218ae0a7dba65424");
        PlatformConfig.setWeixin("wx8ac0cabd077d3552", "e90d6a2346cdfaa0e4062128ffe4a4f1");
        //QQ和Qzone appid appkey
        //PlatformConfig.setQQZone("1106431341", "xzVS26uAUUQBBs1f");
//        PlatformConfig.setQQZone("101656472", "JpfjcFKsYoOMAR7C");
        PlatformConfig.setQQZone("1110123893", "deW8J3L6UEwqKDZj");
//        PlatformConfig.setQQZone("1109434094", "JpfjcFKsYoOMAR7C");

        Config.DEBUG = false;

        initUMengPush();



      //  MiPushRegistar.register(this, APP_ID, APP_KEY);

      //  HuaWeiRegister.register(this);



        //获取本地app的版本
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 得到屏幕的宽度和高度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenW = dm.widthPixels;
        screenH = dm.heightPixels;

        FrescoImageLoader.init(this);
        //下面是配置toolbar颜色和存储图片地址的
//        FrescoImageLoader.init(this,android.R.color.holo_blue_light);
//        FrescoImageLoader.init(this,android.R.color.holo_blue_light,"/storage/xxxx/xxx");

        closeAndroidPDialog();
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
                LogUtils.e("Umeng" + uMessage.title);
                // LogUtils.e("Umeng"+msg.custom+msg.msg_id);
               /* Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("URL", UrlConfig.WEBSITEAN + "?app=true&id=" + uMessage.msg_id);
                intent.putExtra("TITLE", "平台公告");
                startActivity(intent);*/

                if (sharereferences.getString("uid", "").equalsIgnoreCase("")) {
                    super.launchApp(context, uMessage);
                } else {

                    //startActivity(new Intent(context, NewMessageCenterActivity.class)
                    //       .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    //跳到主页
                    super.launchApp(context, uMessage);
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


    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置主题颜色
                layout.setPrimaryColors(new int[]{0xffF4F4F4, 0xffEE4845});
                StoreHouseHeader storeHouseHeader = new StoreHouseHeader(context);
                storeHouseHeader.initWithString("USHIHUI");
                return storeHouseHeader;//指定为经典Header，默认是 贝塞尔雷达Header
//                return new BezierCircleHeader(context);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }


    public MainActivity MainActivity;
    // private MainActivity TaskCenterActivity;
    private WebViewActivity WebViewActivity;
    // private Detail_Piaoju_ActFirst Detail_Piaoju_ActFirst;

   /* private FriendAct friendAct;

    public FriendAct getFriendAct() {
        return friendAct;
    }

    public void setFriendAct(FriendAct friendAct) {
        this.friendAct = friendAct;
    }*/

  /*  public Detail_Piaoju_ActFirst getDetail_Piaoju_ActFirst() {
        return Detail_Piaoju_ActFirst;
    }

    public void setmDetail_Piaoju_ActFirst(
            Detail_Piaoju_ActFirst Detail_Piaoju_ActFirst) {
        this.Detail_Piaoju_ActFirst = Detail_Piaoju_ActFirst;
    }*/

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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //方法数超过65535解决方法
        MultiDex.install(this);
    }

    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
