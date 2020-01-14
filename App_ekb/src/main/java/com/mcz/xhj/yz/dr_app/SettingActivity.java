package com.mcz.xhj.yz.dr_app;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.umeng.message.IUmengCallback;
import com.umeng.message.PushAgent;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_service.UpdateService;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.DialogMaker;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.mcz.xhj.R.id.tv_banben;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：设置
 * 创建人：shuc
 * 创建时间：2017/3/7 18:37
 * 修改人：DELL
 * 修改时间：2017/3/7 18:37
 * 修改备注：
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.iv_kaiguan)
    ImageView ivKaiguan;
    @BindView(R.id.tv_huancun)
    TextView tvHuancun;
    @BindView(tv_banben)
    TextView tvBanben;
    @BindView(R.id.btn_exit)
    TextView btnExit;
    @BindView(R.id.rl_clear_huancun)
    LinearLayout rlClearHuancun;
    @BindView(R.id.rl_genxinbanben)
    LinearLayout rlGenxinbanben;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private SharedPreferences.Editor editor;
    private String serverVersion, maxVersion;// 服务器版本 ,最新版本号
    private String content = "发现新版本,是否更新";
    private boolean isOpenUpush;
    private PushAgent mPushAgent;

    @Override
    protected int getLayoutId() {
        return R.layout.setting;
    }

    String apkDownload = UrlConfig.APKDOWNLOAD;

    @Override
    protected void initParams() {
        tvBanben.setText(UrlConfig.version);
        if (LocalApplication.getInstance().channelName != null && !"".equalsIgnoreCase(LocalApplication.getInstance().channelName)) {
            if (LocalApplication.getInstance().channelName.equalsIgnoreCase("alibaba")) {
                apkDownload = UrlConfig.APKDOWNLOAD1;
            } else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("360")) {
                apkDownload = UrlConfig.APKDOWNLOAD360;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("xiaomi")) {
                apkDownload = UrlConfig.APKDOWNLOADXIAOMI;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("huawei")) {
                apkDownload = UrlConfig.APKDOWNLOADHUAWEI;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("oppo")) {
                apkDownload = UrlConfig.APKDOWNLOADOPPO;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("vivo")) {
                apkDownload = UrlConfig.APKDOWNLOADVIVO;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("qq")) {
                apkDownload = UrlConfig.APKDOWNLOADYINGYONGBAO;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("lenovo")) {
                apkDownload = UrlConfig.APKDOWNLOADLENOVO;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("anzhi")) {
                apkDownload = UrlConfig.APKDOWNLOADANZHI;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("mumayi")) {
                apkDownload = UrlConfig.APKDOWNLOADMUMAYI;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("yingyonghui")) {
                apkDownload = UrlConfig.APKDOWNLOAD11;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("meizu")) {
                apkDownload = UrlConfig.APKDOWNLOADMEIZU;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("letv")) {
                apkDownload = UrlConfig.APKDOWNLOADLETV;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("smartisan")) {
                apkDownload = UrlConfig.APKDOWNLOADCHUIZI;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("jinli")) {
                apkDownload = UrlConfig.APKDOWNLOAD15;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("sougou")) {
                apkDownload = UrlConfig.APKDOWNLOADSOUGOU;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("cool")) {
                apkDownload = UrlConfig.APKDOWNLOAD17;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("sansung")) {
                apkDownload = UrlConfig.APKDOWNLOADSANXING;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("baidu")) {
                apkDownload = UrlConfig.APKDOWNLOADBAIDU;
            }

            /*if (LocalApplication.getInstance().channelName.equalsIgnoreCase("1")) {
                apkDownload = UrlConfig.APKDOWNLOAD1;
            } else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("2")) {
                apkDownload = UrlConfig.APKDOWNLOAD360;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("3")) {
                apkDownload = UrlConfig.APKDOWNLOADXIAOMI;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("4")) {
                apkDownload = UrlConfig.APKDOWNLOADHUAWEI;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("5")) {
                apkDownload = UrlConfig.APKDOWNLOADOPPO;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("6")) {
                apkDownload = UrlConfig.APKDOWNLOADVIVO;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("7")) {
                apkDownload = UrlConfig.APKDOWNLOADYINGYONGBAO;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("8")) {
                apkDownload = UrlConfig.APKDOWNLOADLENOVO;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("9")) {
                apkDownload = UrlConfig.APKDOWNLOADANZHI;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("10")) {
                apkDownload = UrlConfig.APKDOWNLOADMUMAYI;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("11")) {
                apkDownload = UrlConfig.APKDOWNLOAD11;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("12")) {
                apkDownload = UrlConfig.APKDOWNLOADMEIZU;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("13")) {
                apkDownload = UrlConfig.APKDOWNLOADLETV;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("14")) {
                apkDownload = UrlConfig.APKDOWNLOADCHUIZI;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("15")) {
                apkDownload = UrlConfig.APKDOWNLOAD15;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("16")) {
                apkDownload = UrlConfig.APKDOWNLOADSOUGOU;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("17")) {
                apkDownload = UrlConfig.APKDOWNLOAD17;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("18")) {
                apkDownload = UrlConfig.APKDOWNLOADSANXING;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("feipao1")) {
                apkDownload = UrlConfig.APKDOWNLOADFEIPAO1;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("feipao2")) {
                apkDownload = UrlConfig.APKDOWNLOADFEIPAO2;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("dandanzhuan")) {
                apkDownload = UrlConfig.APKDOWNLOADDANDANZHUAN;
            }else if (LocalApplication.getInstance().channelName.equalsIgnoreCase("tangzhuan")) {
                apkDownload = UrlConfig.APKDOWNLOADTANGZHUAN;
            }else if(LocalApplication.getInstance().channelName.equalsIgnoreCase("shihai")){
                apkDownload = UrlConfig.APKDOWNLOADSHIHAI;
            }*/
        }
        editor = preferences.edit();
        mPushAgent = PushAgent.getInstance(this);
        titleCentertextview.setText("设置");
        try {
            tvHuancun.setText(getTotalCacheSize(this));//getFolderSize(getExternalCacheDir())
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvBanben.setText(LocalApplication.localVersion);
        isOpenUpush = preferences.getBoolean("isOpenUpush", false);
//        if(JPushInterface.isPushStopped(getApplicationContext())){
//            ivKaiguan.setImageResource(R.mipmap.push_close);
//        }else{
//            ivKaiguan.setImageResource(R.mipmap.push_open);
//        }
        if (isOpenUpush) {
            ivKaiguan.setImageResource(R.mipmap.push_open);
        } else {
            ivKaiguan.setImageResource(R.mipmap.push_close);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.title_leftimageview, R.id.iv_kaiguan, R.id.btn_exit, R.id.rl_clear_huancun, R.id.rl_genxinbanben})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.iv_kaiguan:
//                if(JPushInterface.isPushStopped(getApplicationContext())){  //点击打开
//                    JPushInterface.resumePush(getApplicationContext());
//                    mHandler.sendEmptyMessage(0);
//                }else{
//                    JPushInterface.stopPush(getApplicationContext());
//                    mHandler.sendEmptyMessage(1);
//                }
                if (isOpenUpush) {
                    LogUtils.i("--->关闭友盟推送");
                    closeUPush();
                } else {
                    LogUtils.i("--->开启友盟推送");
                    openUPush();
                }
                break;
            case R.id.btn_exit:
                DialogMaker.showRedSureDialog(SettingActivity.this, "退出", "是否确认退出？", "取消", "确定", new DialogMaker.DialogCallBack() {
                    @Override
                    public void onButtonClicked(Dialog dialog, int position, Object tag) {
                        exit_dr();
                    }

                    @Override
                    public void onCancelDialog(Dialog dialog, Object tag) {

                    }
                }, "");

                break;
            case R.id.rl_clear_huancun:
                DialogMaker.showRedSureDialog(SettingActivity.this, "提示", "您确定要清楚所有缓存数据吗？", "取消", "清除", new DialogMaker.DialogCallBack() {
                    @Override
                    public void onButtonClicked(Dialog dialog, int position, Object tag) {
                        clearAllCache(SettingActivity.this);
                        ToastMaker.showShortToast("清除完毕");
                        tvHuancun.setText("0");
                    }

                    @Override
                    public void onCancelDialog(Dialog dialog, Object tag) {

                    }
                }, "");
                break;
            case R.id.rl_genxinbanben:
                getReNewAl();
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    isOpenUpush = true;
                    ToastMaker.showShortToast("开启成功");
                    ivKaiguan.setImageResource(R.mipmap.push_open);
                    break;
                case 1:
                    isOpenUpush = false;
                    ToastMaker.showShortToast("关闭成功");
                    ivKaiguan.setImageResource(R.mipmap.push_close);
                    break;
            }
        }
    };


    private void exit_dr() {
        String deviceToken = preferences.getString("deviceToken", "");
        editor.clear();
        editor.putBoolean("login", false);
        editor.putBoolean("FirstLog", false);
        editor.putBoolean("isOpenUpush", isOpenUpush);
        editor.putString("deviceToken", deviceToken);
        editor.commit();
        finish();
        LocalApplication.getInstance().getMainActivity().isHomeChecked = true;
        LocalApplication.getInstance().getMainActivity().isHome = true;
//		LocalApplication.getInstance().getMainActivity().isNewFinish = false;
    }

    /**
     * 获取缓存大小
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
//        File file = context.getExternalCacheDir();
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 清除缓存
     *
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


    @Override
    public void onButtonClicked(Dialog dialog, int position, Object tag) {
        // TODO Auto-generated method stub
        if (position == 1) {
            if ("update".equalsIgnoreCase(tag.toString())) {
                if (tag.equals("update")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            //申请权限
                            ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
                            return;
                        } else {
                            // 开启更新服务UpdateService
                            Intent updateIntent = new Intent(SettingActivity.this, UpdateService.class).putExtra("update", apkDownload);
                            startService(updateIntent);
                            ToastMaker.showLongToast("已转至后台下载");
                        }
                    } else {
                        // 开启更新服务UpdateService
                        Intent updateIntent = new Intent(SettingActivity.this, UpdateService.class).putExtra("update", apkDownload);
                        startService(updateIntent);
                        ToastMaker.showLongToast("已转至后台下载");
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 111);
                        return;
                    }
                }
            }
        }
    }


    private void getReNewAl() {
        showWaitDialog("正在检查版本...", false, "");
        OkHttpUtils.post().url(UrlConfig.RENEWAL)
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {

            @Override
            public void onResponse(String response) {
                dismissDialog();
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    maxVersion = map.getString("maxVersion");
                    if (maxVersion != null && !maxVersion.equalsIgnoreCase("")) {
                        if (stringCut.compareVersion(maxVersion, LocalApplication.localVersion) > 0) {
                            dialog = showAlertDialog("版本更新", stringCut.HuanHang_Cut(content), new String[]{"取消", "立即更新"}, true, true, "update");
                        } else {
                            ToastMaker.showShortToast("已经是最新版本");
                        }
                    } else {
                        ToastMaker.showShortToast("系统错误");
                    }
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
            }
        });
    }


    private void openUPush() {
        mPushAgent.enable(new IUmengCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("--->开启友盟推送成功");
                mHandler.sendEmptyMessage(0);
                editor.putBoolean("isOpenUpush", true);
                editor.commit();
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.i("--->开启友盟推送失败");
                LogUtils.i("--->开启友盟推送失败，s："+s);
                LogUtils.i("--->开启友盟推送失败，s1："+s1);
            }
        });
    }

    private void closeUPush() {
        mPushAgent.disable(new IUmengCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("--->关闭友盟推送成功");
                mHandler.sendEmptyMessage(1);
                editor.putBoolean("isOpenUpush", false);
                editor.commit();
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.i("--->关闭友盟推送失败，s："+s);
                LogUtils.i("--->关闭友盟推送失败，s1："+s1);
            }
        });
    }
}
