package com.mcz.xhj.yz.dr_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.ActivityStack;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.DialogMaker;
import com.mcz.xhj.yz.dr_view.DialogMaker.DialogCallBack;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * BaseActivity
 *
 * @author blue
 */
public abstract class BaseActivity extends AutoLayoutActivity implements DialogCallBack {
    private final static String TAG = "JPUSH";

    protected Dialog dialog;

    private boolean isCreate = false;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private String imei;
    private String mac;
    private TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.getInstance().addActivity(this);
        //setImmersionStatus();
        setContentView(getLayoutId());
        isCreate = true;

        PushAgent.getInstance(this).onAppStart();

        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            return;
        } else {
            //获取手机的imei
            imei = telephonyManager.getDeviceId();
        }

        //获取手机的mac
        mac = getAdresseMAC(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        LogUtils.i("--->渠道toFrom：" + LocalApplication.getInstance().channelName);
        if (isCreate) {
            ButterKnife.bind(this);
            isCreate = false;
            initParams();
        }

        String checkOutTime = LocalApplication.getInstance().sharereferences.getString("checkOutTime", "");
        String realVerify = LocalApplication.getInstance().sharereferences.getString("realVerify", "");
//		long a = 1 ;
//		if(!checkOutTime.equals("")){
//			Long b = System.currentTimeMillis() - Long.parseLong(checkOutTime);
//			if(b>= 300000){
//				if(LocalApplication.getInstance().sharereferences.getBoolean("login", false)){
//					if(LocalApplication.getInstance().sharereferences.getBoolean("loginshoushi", false)){
//						startActivity(new Intent(BaseActivity.this, GestureEditActivity.class));
//					}
////					else if(LocalApplication.getInstance().sharereferences.getBoolean("isGesture", true) &&realVerify.equalsIgnoreCase("1")){
////						showAlertDialog("设置手势密码", "是否立即设置手势密码", new String[]{"暂不设置","立即设置"}, true, true, "");
////						LocalApplication.getInstance().sharereferences.edit().putBoolean("isGesture", false).commit() ;
////					}
//					}
//			}
//			LocalApplication.getInstance().sharereferences.edit().putString("checkOutTime", "").commit() ;
//		}

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (!isAppOnForeground()) {
            // app 进入后台
            LocalApplication.getInstance().sharereferences.edit().putString("checkOutTime", System.currentTimeMillis() + "").commit();
            // 全局变量isActive = false 记录当前已经进入后台
        }
    }

    @Override
    protected synchronized void onDestroy() {
        dismissDialog();
        ActivityStack.getInstance().removeActivity(this);
        LocalApplication.getInstance().sharereferences.edit().putString("checkOutTime", "").commit();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (permissions[0].equals(Manifest.permission.READ_PHONE_STATE)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取手机的imei
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    imei = telephonyManager.getDeviceId();

                } else {
                    ToastMaker.showLongToast("请先同意授权");
                    finish();
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private void setImmersionStatus() {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 初始化布局
     *
     * @author blue
     */
    protected abstract int getLayoutId();

    /**
     * 参数设置
     *
     * @author blue
     */
    protected abstract void initParams();

    /**
     * 弹出对话框
     *
     * @author blue
     */
    public Dialog showAlertDialog(String title, String msg, String[] btns, boolean isCanCancelabel, final boolean isDismissAfterClickBtn, Object tag) {
        if (null == dialog || !dialog.isShowing()) {
            dialog = DialogMaker.showCommonAlertDialog(this, title, msg, btns, this, isCanCancelabel, isDismissAfterClickBtn, tag);
        }
        return dialog;
    }

    /**
     * 等待对话框
     *
     * @author blue
     */
    public Dialog showWaitDialog(String msg, boolean isCanCancelabel, Object tag) {
        if (null == dialog || !dialog.isShowing()) {
            dialog = DialogMaker.showCommenWaitDialog(this, msg, this, isCanCancelabel, tag);
        }
        return dialog;
    }

    /**
     * 关闭对话框
     *
     * @author blue
     */
    public void dismissDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onButtonClicked(Dialog dialog, int position, Object tag) {
    }

    @Override
    public void onCancelDialog(Dialog dialog, Object tag) {
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }


    public void pushRegisterId() {
        String registerId = preferences.getString("deviceToken", ""); //友盟
        LogUtils.i("--->deviceToken："+registerId+" uid："+preferences.getString("uid", "")+" imei: "+imei+" ,mac: "+mac);

        if (TextUtils.isEmpty(registerId)) {

        }
        LogUtils.e("machineId"+imei+"|"+mac);
        OkHttpUtils
                .post()
                .url(UrlConfig.PUSHREGISTRATIONID)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("machineId",imei+"|"+mac)
                .addParam("registrationId", registerId)
                .addParam("appKey", UrlConfig.PUSHAPPKEY)
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("--->注册推送pushregistrationid："+response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
//							ToastMaker.showShortToast("registerId发送成功");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                    }
                });
    }


    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    public static String getAdresseMAC(Context context) {
        WifiManager wifiMan = (WifiManager)context.getSystemService(Context.WIFI_SERVICE) ;
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

        if(wifiInf !=null && marshmallowMacAddress.equals(wifiInf.getMacAddress())){
            String result = null;
            try {
                result= getAdressMacByInterface();
                if (result != null){
                    return result;
                } else {
                    result = getAddressMacByFile(wifiMan);
                    return result;
                }
            } catch (IOException e) {
                Log.e("MobileAccess", "Erreur lecture propriete Adresse MAC");
            } catch (Exception e) {
                Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
            }
        } else{
            if (wifiInf != null && wifiInf.getMacAddress() != null) {
                return wifiInf.getMacAddress();
            } else {
                return "";
            }
        }
        return marshmallowMacAddress;
    }

    private static String getAdressMacByInterface(){
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }

        } catch (Exception e) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
        }
        return null;
    }

    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String ret;
        int wifiState = wifiMan.getWifiState();

        wifiMan.setWifiEnabled(true);
        File fl = new File(fileAddressMac);
        FileInputStream fin = new FileInputStream(fl);
        ret = crunchifyGetStringFromStream(fin);
        fin.close();

        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    private static String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream != null) {
            Writer crunchifyWriter = new StringWriter();

            char[] crunchifyBuffer = new char[2048];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }
            return crunchifyWriter.toString();
        } else {
            return "No Contents";
        }
    }


    /*public void getAndSetTag() {
        post()
                .url(UrlConfig.GETJPUSHTAG)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject jsonObj = obj.getJSONObject("map");
                            int type = jsonObj.getIntValue("type");
                            if (type == 0) {
                                String audience = jsonObj.getString("audience");
                                //setTag(audience);
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                    }
                });
    }

    *//**
     * 极光的tag设置
     *
     * @param tag
     *//*
    public void setTag(String tag) {
        // 检查 tag 的有效性
        if (TextUtils.isEmpty(tag)) {
            Toast.makeText(this, R.string.error_tag_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
                Toast.makeText(this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            tagSet.add(sTagItme);
        }

        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

    }

    public final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

            ExampleUtil.showToast(logs, getApplicationContext());
        }

    };


    public final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

            ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

    public static final int MSG_SET_ALIAS = 1001;
    public static final int MSG_SET_TAGS = 1002;

    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };*/
}
