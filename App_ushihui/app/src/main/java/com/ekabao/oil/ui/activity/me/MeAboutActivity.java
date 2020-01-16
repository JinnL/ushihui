package com.ekabao.oil.ui.activity.me;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ekabao.oil.R;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.ekabao.oil.ui.activity.me.SettingActivity.getFolderSize;
import static com.ekabao.oil.ui.activity.me.SettingActivity.getFormatSize;

public class MeAboutActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.rl_click2)
    RelativeLayout rlClick2;
    @BindView(R.id.rl_click3)
    RelativeLayout rlClick3;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.ll_clear_cache)
    LinearLayout llClearCache;
    @BindView(R.id.rl_et)
    LinearLayout rlEt;
    @BindView(R.id.tv_bottom)
    TextView tvBottom;
    @BindView(R.id.activity_me_about)
    LinearLayout activityMeAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_me_about);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_me_about;
    }

    @Override
    protected void initParams() {

        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleCentertextview.setText("设置");


        llClearCache.setOnClickListener(this);
        rlClick2.setOnClickListener(this);
        rlClick3.setOnClickListener(this);

        try {
            String versionName = getVersionName();
            tvVersion.setText(getString(R.string.app_name)+versionName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            tvCache.setText(getTotalCacheSize(this));//getFolderSize(getExternalCacheDir())
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private String getVersionName() throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
          /*  case R.id.ib_back:
                finish();
                break;*/

            case R.id.rl_click2:
                startActivity(new Intent(this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.SAFE )
                        .putExtra("TITLE", "关于我们")
                        .putExtra("noWebChrome", "aboutMe"));

               // startActivity(new Intent(this, AboutActivity.class));

                break;
            case R.id.rl_click3:
                //  2017/2/24 去评分  MarketScoreUtils
                //test();
                //  goToMarket(this,getPackageName());

                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Couldn't launch the market !", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.ll_clear_cache: //清理缓存

                DialogMaker.showRedSureDialog(this, "提示", "您确定要清楚所有缓存数据吗？", "取消", "清除", new DialogMaker.DialogCallBack() {
                    @Override
                    public void onButtonClicked(Dialog dialog, int position, Object tag) {
                        clearAllCache(MeAboutActivity.this);
                        ToastMaker.showShortToast("清除完毕");
                        tvCache.setText("0M");
                    }

                    @Override
                    public void onCancelDialog(Dialog dialog, Object tag) {

                    }
                }, "");

                break;

        }

    }

    public void test() {
        // 判断360市场是否存在
        if (isAvilible(MeAboutActivity.this, "com.qihoo.appstore")) {
            // 市场存在

            Intent intent = new Intent(Intent.ACTION_VIEW);
            // 跳转到360市场评分
            ComponentName cn = new ComponentName("com.qihoo.appstore", "com.qihoo.appstore.activities.SearchDistributionActivity");
            intent.setComponent(cn);
            intent.setData(Uri.parse("market://details?id=com.paopaobeauty.meinv"));
            startActivity(intent);
        } else {
            // 市场不存在
            ToastUtil.showToast("请下载360手机助手");
        }
    }

    // 判断市场是否存在的方法
    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            goToMarket.setClassName("com.tencent.android.qqdownloader", "com.tencent.pangu.link.LinkProxyActivity");
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 三星市场
     */
    public static void goToSamsungappsMarket(Context context, String packageName) {
        Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + packageName);
        Intent goToMarket = new Intent();
        goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
        goToMarket.setData(uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 乐视手机上面
     */
    public void goToLeTVStoreDetail(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setClassName("com.letv.app.appstore", "com.letv.app.appstore.appmodule.details.DetailsActivity");
        intent.setAction("com.letv.app.appstore.appdetailactivity");
        intent.putExtra("packageName", packageName);
        context.startActivity(intent);
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
}
