package com.mcz.xhj.yz.dr_app;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.awen.photo.photopick.bean.PhotoResultBean;
import com.awen.photo.photopick.controller.PhotoPickConfig;
import com.awen.photo.photopick.controller.PhotoPreviewConfig;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.umeng.message.IUmengCallback;
import com.umeng.message.PushAgent;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_service.UpdateService;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.DialogMaker;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/10/9.
 * 描述：2.0设置中心
 */

public class NewSettingActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
   /* @BindView(R.id.view_line_bottom)
    View viewLineBottom;*/
    @BindView(R.id.img_avatar)//头像
    CircleImageView img_avatar;
    @BindView(R.id.rl_change_avatar)
    RelativeLayout rlChangeAvatar;
    @BindView(R.id.tv_status_verify)
    TextView tvStatusVerify;
    @BindView(R.id.ll_identity_verify)
    LinearLayout llIdentityVerify;
    @BindView(R.id.tv_phonenum)
    TextView tvPhonenum;
    @BindView(R.id.ll_bind_phonenum)
    LinearLayout llBindPhonenum;
    @BindView(R.id.tv_status_address)
    TextView tvStatusAddress;
    @BindView(R.id.ll_receive_address)
    LinearLayout llReceiveAddress;
    @BindView(R.id.tv_status_banks)
    TextView tvStatusBanks;
    @BindView(R.id.ll_banks_manage)
    LinearLayout llBanksManage;
    @BindView(R.id.tv_status_loginPsw)
    TextView tvStatusLoginPsw;
    @BindView(R.id.ll_login_password)
    LinearLayout llLoginPassword;
    @BindView(R.id.tv_status_payPsw)
    TextView tvStatusPayPsw;
    @BindView(R.id.ll_pay_password)
    LinearLayout llPayPassword;
    @BindView(R.id.check_Gesture)
    CheckBox check_Gesture;
    @BindView(R.id.rl_Gesture_psw)
    RelativeLayout rlGesture;
    @BindView(R.id.iv_kaiguan)
    ImageView ivKaiguan;
    @BindView(R.id.ll_push)
    LinearLayout llPush;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.ll_clear_cache)
    LinearLayout llClearCache;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ll_version)
    LinearLayout llVersion;
    @BindView(R.id.btn_exit)
    TextView btnExit;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private SharedPreferences.Editor editor;
    private String serverVersion, maxVersion;// 服务器版本 ,最新版本号
    private String content = "发现新版本,是否更新";
    private boolean isOpenUpush;
    private PushAgent mPushAgent;
    String apkDownload = UrlConfig.APKDOWNLOAD;
    private boolean isSetTradePsw;//是否设置过交易密码
    private boolean updateLoginPsw;//是否修改登录密码

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("设置");
        tvVersion.setText(LocalApplication.localVersion);
        editor = preferences.edit();
        String avatar_url = preferences.getString("avatar_url", "");
        if(!avatar_url.equals("")){
            Picasso.with(NewSettingActivity.this).load(avatar_url).memoryPolicy(MemoryPolicy.NO_CACHE).into(img_avatar);
        }
        mPushAgent = PushAgent.getInstance(this);
        isOpenUpush = preferences.getBoolean("isOpenUpush", false);
        if (isOpenUpush) {
            ivKaiguan.setImageResource(R.mipmap.push_open);
        } else {
            ivKaiguan.setImageResource(R.mipmap.push_close);
        }
        try {
            tvCache.setText(getTotalCacheSize(this));//getFolderSize(getExternalCacheDir())
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLoginPsw = preferences.getBoolean("updateLoginPsw",false);
        memberSetting();
        getReceiptAddress();
        if(LocalApplication.getInstance().sharereferences.getBoolean("loginshoushi", false)){
            check_Gesture.setChecked(true) ;
        }else{
            check_Gesture.setChecked(false) ;
        }
    }

    @OnClick({R.id.title_leftimageview, R.id.rl_change_avatar, R.id.ll_identity_verify, R.id.ll_receive_address, R.id.ll_banks_manage, R.id.ll_login_password, R.id.ll_pay_password, R.id.check_Gesture, R.id.iv_kaiguan, R.id.ll_clear_cache, R.id.ll_version, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.rl_change_avatar:
                new PhotoPickConfig.Builder(this)
                        .pickMode(PhotoPickConfig.MODE_SINGLE_PICK)
                        .maxPickSize(15)
                        .showCamera(true)
                        .setOriginalPicture(true)//让用户可以选择原图
                        .setOnPhotoResultCallback(new PhotoPickConfig.Builder.OnPhotoResultCallback() {
                            @Override
                            public void onResult(PhotoResultBean result) {
                                Log.i("MainActivity", "result = " + result.getPhotoLists().size());
                            }
                        })
                        .build();

                break;
            case R.id.ll_identity_verify:
                startActivity(new Intent(NewSettingActivity.this, FourPartAct.class));
                break;
            case R.id.ll_receive_address:
                startActivity(new Intent(NewSettingActivity.this, AddressManageActivity.class));
                break;
            case R.id.ll_banks_manage:
                if("1".equals(realVerify)){
                    //这个感觉有问题,单笔 限额的
                    startActivity(new Intent(NewSettingActivity.this, BankManageActivity.class));
                    //老版本的 后台没改好,还是用新的吧
                    //startActivity(new Intent(NewSettingActivity.this , BankDetailAct.class)) ;
                }else{
                    startActivity(new Intent(NewSettingActivity.this, FourPartAct.class));
                }
                break;
            case R.id.ll_login_password://修改登录密码
                startActivity(new Intent(NewSettingActivity.this, ModifyPswActivity.class).putExtra("isFrom",0));
                break;
            case R.id.ll_pay_password://设置?修改交易密码
                if ("1".equals(preferences.getString("tpwdFlag", ""))){//tpwdFlag=1代表修改交易密码
                    startActivity(new Intent(NewSettingActivity.this, ModifyPswActivity.class).putExtra("isFrom",1));
                }else {
                    startActivity(new Intent(NewSettingActivity.this, SetTradePswActivity.class).putExtra("phone",mobilephone));
                }
                //startActivity(new Intent(NewSettingActivity.this, TransactionPswAct.class).putExtra("uid", preferences.getString("uid", "")).putExtra("phone_num", preferences.getString("phone", "")).putExtra("isForget", isSetTradePsw));
                break;
            case R.id.check_Gesture:
                if (check_Gesture.isChecked()) {
                    if(!LocalApplication.getInstance().sharereferences.getBoolean("loginshoushi", false)){
                        startActivity(new Intent(NewSettingActivity.this, GestureEditActivity.class));
                    }
                }else{
				    //ToastMaker.showShortToast("可以在我的信息-手势密码 中进行修改") ;
                    editor.putBoolean("loginshoushi", false);
                    editor.putString("gesturePsd", "");
                    editor.commit();
                }
                break ;
            case R.id.iv_kaiguan:
                if (isOpenUpush) {
                    LogUtils.i("--->关闭友盟推送");
                    closeUPush();
                } else {
                    LogUtils.i("--->开启友盟推送");
                    openUPush();
                }
                break;
            case R.id.ll_clear_cache:
                DialogMaker.showRedSureDialog(NewSettingActivity.this, "提示", "您确定要清楚所有缓存数据吗？", "取消", "清除", new DialogMaker.DialogCallBack() {
                    @Override
                    public void onButtonClicked(Dialog dialog, int position, Object tag) {
                        clearAllCache(NewSettingActivity.this);
                        ToastMaker.showShortToast("清除完毕");
                        tvCache.setText("0M");
                    }

                    @Override
                    public void onCancelDialog(Dialog dialog, Object tag) {

                    }
                }, "");
                break;
            case R.id.ll_version:
                getReNewAl();
                break;
            case R.id.btn_exit:
                DialogMaker.showRedSureDialog(NewSettingActivity.this, "退出", "是否确认退出？", "取消", "确定", new DialogMaker.DialogCallBack() {
                    @Override
                    public void onButtonClicked(Dialog dialog, int position, Object tag) {
                        exit_dr();
                    }

                    @Override
                    public void onCancelDialog(Dialog dialog, Object tag) {

                    }
                }, "");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case PhotoPickConfig.PICK_REQUEST_CODE://图片
                ArrayList<String> photoLists = data.getStringArrayListExtra(PhotoPickConfig.EXTRA_STRING_ARRAYLIST);
                //用户选择的是否是原图
                boolean isOriginalPicture = data.getBooleanExtra(PhotoPreviewConfig.EXTRA_ORIGINAL_PIC, false);
                if (photoLists != null && !photoLists.isEmpty()) {
                    String photo_path = photoLists.toString();
                    String type = "";
                    if(photo_path.contains(".jpg")){
                        type = ".jpg";
                    }else if(photo_path.contains(".png")){
                        type = ".png";
                    }else if(photo_path.contains(".gif")){
                        type = ".gif";
                    }
                    LogUtils.i("--->selected photos = " + photo_path);
                    LogUtils.i("--->selected photos type= " + type);
                    //Toast.makeText(this, "selected photos size = " + photoLists.size() + "\n" + photoLists.toString(), Toast.LENGTH_LONG).show();
                    File file = new File(photoLists.get(0));
                    try {
                        if (file.exists()) {
                            long imageSize = getFileSize(file);
                            LogUtils.i("--->imageSize："+imageSize);
                            if(imageSize > 5*1024*1024){
                                ToastMaker.showShortToast("照片不能超过5M");
                                return;
                            }
                            //you can do something
                            Bitmap bitmap = BitmapFactory.decodeFile(photoLists.get(0));
                            img_avatar.setImageBitmap(bitmap);
                            String image = Bitmap2StrByBase64(bitmap);
                            uploadAvatar(image,type);
                        } else {
                            //toast error

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }

    private void uploadAvatar(String image, String type) {
        showWaitDialog("上传中...", true, "");
        LogUtils.i("上传头像 uid："+preferences.getString("uid", "")+" suffix: "+type+" image: "+image);
        OkHttpUtils.post().url(UrlConfig.UPLOADAVATAR)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("image",image)
                .addParam("suffix",type)
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                ToastMaker.showShortToast("上传头像成功");
                LogUtils.i("上传头像 response："+response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean(("success"))) {
                    JSONObject map = obj.getJSONObject("map");
                    String picImg = map.getString("picImg");
                    editor.putString("avatar_url",picImg);
                    editor.commit();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ToastMaker.showShortToast("上传失败，请检查网络");
            }
        });
    }

    private void getReceiptAddress() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.RECEIPTADDRESS)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                LogUtils.i("setting获取所有的收货地址：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    JSONArray list = map.getJSONArray("list");
                    if (list.size() > 0) {
                        tvStatusAddress.setText("已设置");
                    } else {
                        tvStatusAddress.setText("未设置");
                    }
                }

            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

    String mobilephone ;
    String realVerify="";
    private void memberSetting() {
        showWaitDialog("加载中...", false, "");
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
                            JSONObject map = obj.getJSONObject("map") ;
                            mobilephone = map.getString("mobilephone") ;
                            realVerify = map.getString("realVerify");
                            String tpwdFlag = map.getString("tpwdFlag") ;
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("realVerify",realVerify ) ;
                            editor.putString("tpwdFlag",tpwdFlag);
                            editor.commit() ;
                            if("1".equals(tpwdFlag)){
                                tvStatusPayPsw.setText("已设置") ;
                            }else{
                                tvStatusPayPsw.setText("未设置") ;
                            }
                            if(updateLoginPsw){
                                tvStatusLoginPsw.setText("已修改");
                            }else{
                                tvStatusLoginPsw.setText("去修改");
                            }
                            tvPhonenum.setText(mobilephone) ;

                            if("1".equals(realVerify)){
                                String realName = map.getString("realName");
                                String idCards = map.getString("idCards");
                                String bankid = map.getString("bankId");
                                tvStatusVerify.setText(realName);
                                tvStatusVerify.setTextColor(0xFFAFB0B7);
                                llIdentityVerify.setClickable(false);
                                tvStatusBanks.setText("已添加");


                            } else {
                                tvStatusVerify.setText("去设置");
                                tvStatusVerify.setTextColor(0xFFFF8347);
                                llIdentityVerify.setClickable(true);
                                tvStatusBanks.setText("未绑定");

                            }

                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish() ;
//							new show_Dialog_IsLogin(UserInfoAct.this).show_Is_Login() ;
                        } else {
                            ToastMaker.showShortToast("系统错误");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        // TODO Auto-generated method stub
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    /*
    * 将突破转化成base64
    * */
    public String Bitmap2StrByBase64(Bitmap bit){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩  
        byte[] bytes=bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
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
        String token = preferences.getString("token", "");
        editor.clear();
        editor.putBoolean("login", false);
        editor.putBoolean("FirstLog", false);
        editor.putBoolean("isOpenUpush", isOpenUpush);
        //editor.putString("token", token);
        editor.commit();
        finish();
        LocalApplication.getInstance().getMainActivity().isHomeChecked = true;
        LocalApplication.getInstance().getMainActivity().isHome = true;
        LocalApplication.getInstance().getMainActivity().isExit = true;

    }

    /**
     * 获取指定文件大小
     * @param
     * @return
     * @throws Exception 　　
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
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
                        if (ContextCompat.checkSelfPermission(NewSettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            //申请权限
                            ActivityCompat.requestPermissions(NewSettingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
                            return;
                        } else {
                            // 开启更新服务UpdateService
                            Intent updateIntent = new Intent(NewSettingActivity.this, UpdateService.class).putExtra("update", apkDownload);
                            startService(updateIntent);
                            ToastMaker.showLongToast("已转至后台下载");
                        }
                    } else {
                        // 开启更新服务UpdateService
                        Intent updateIntent = new Intent(NewSettingActivity.this, UpdateService.class).putExtra("update", apkDownload);
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
