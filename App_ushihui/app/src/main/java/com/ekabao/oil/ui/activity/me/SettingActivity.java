package com.ekabao.oil.ui.activity.me;

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
import android.os.Bundle;
import android.os.Environment;
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
import com.alibaba.fastjson.JSONObject;
import com.awen.photo.photopick.bean.PhotoResultBean;
import com.awen.photo.photopick.controller.PhotoPickConfig;
import com.awen.photo.photopick.controller.PhotoPreviewConfig;
import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.BankName_Pic;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.service.UpdateService;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.activity.GestureEditActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.show_Dialog_IsLogin;
import com.umeng.message.IUmengCallback;
import com.umeng.message.PushAgent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 个人设置
 *
 * @time 2018/7/17 14:49
 * Created by
 */
public class SettingActivity extends BaseActivity {


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
    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_id_card)
    TextView tvIdCard;
    @BindView(R.id.rl_change_avatar)
    RelativeLayout rlChangeAvatar;
    @BindView(R.id.tv_phonenum)
    TextView tvPhonenum;
    @BindView(R.id.ll_bind_phonenum)
    LinearLayout llBindPhonenum;
    @BindView(R.id.tv_status_banks)
    TextView tvStatusBanks;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_bank_number)
    TextView tvBankNumber;
    @BindView(R.id.iv_bank)
    ImageView ivBank;
    @BindView(R.id.rl_bank)
    RelativeLayout rlBank;
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
    @BindView(R.id.tv_Gesture)
    TextView tvGesture;
    @BindView(R.id.tv_status_Gesture)
    TextView tvStatusGesture;
    @BindView(R.id.check_Gesture)
    CheckBox checkGesture;
    @BindView(R.id.rl_Gesture_psw)
    RelativeLayout rlGesturePsw;
    @BindView(R.id.tv_status_address)
    TextView tvStatusAddress;
    @BindView(R.id.ll_receive_address)
    LinearLayout llReceiveAddress;
    @BindView(R.id.tv_status_verify)
    TextView tvStatusVerify;
    @BindView(R.id.ll_identity_verify)
    LinearLayout llIdentityVerify;
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
    private boolean check_Gesture;//手势密码

    String mobilephone;
    String realVerify = ""; //是否实名认证 1 已经实名认证过
    private BankName_Pic bp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("个人资料");
        tvVersion.setText(LocalApplication.localVersion);
        editor = preferences.edit();

       /* if(LocalApplication.getInstance().sharereferences
                .getBoolean("loginshoushi", false)){
            check_Gesture= true;
            tvStatusGesture.setText("已开启");
        }else{
            check_Gesture= false;
            tvStatusGesture.setText("未开启");
        }*/

        String avatar_url = preferences.getString("avatar_url", "");
        if (!avatar_url.equals("")) {

           // GlideRoundTransform glideRoundTransform = new GlideRoundTransform(this,90);
            Glide.with(this)
                    .load(avatar_url)
                   // .placeholder(R.drawable.icon_person_default_login)
                    .error(R.drawable.icon_person_default_login)
                  //  .transform(glideRoundTransform)
                    .into(imgAvatar);
        }
        mPushAgent = PushAgent.getInstance(this);
        isOpenUpush = preferences.getBoolean("isOpenUpush", false);

       /* if (isOpenUpush) {
            ivKaiguan.setImageResource(R.mipmap.push_open);
        } else {
            ivKaiguan.setImageResource(R.mipmap.push_close);
        }*/

        try {
            tvCache.setText(getTotalCacheSize(this));//getFolderSize(getExternalCacheDir())
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateLoginPsw = preferences.getBoolean("updateLoginPsw", false);

        memberSetting();

        getReceiptAddress();
       /* if(LocalApplication.getInstance().sharereferences.getBoolean("loginshoushi", false)){
            check_Gesture.setChecked(true) ;
        }else{
            check_Gesture.setChecked(false) ;
        }*/


    }


    @Override
    protected void onResume() {
        super.onResume();
        updateLoginPsw = preferences.getBoolean("updateLoginPsw",false);
        memberSetting();
        getReceiptAddress();
        if(LocalApplication.getInstance().sharereferences
                .getBoolean("loginshoushi", false)){
            check_Gesture= true;
            tvStatusGesture.setText("已开启");
        }else{
            check_Gesture= false;
            tvStatusGesture.setText("未开启");
        }
        if ("1".equals(preferences.getString("tpwdFlag", ""))) {//tpwdFlag=1代表修改交易密码
            tvStatusPayPsw.setTextColor(0xFF999999);
            tvStatusPayPsw.setText("修改");
        } else {
            tvStatusPayPsw.setTextColor(0xFFEE4845);
            tvStatusPayPsw.setText("未设置");
        }

    }

    @OnClick({R.id.title_leftimageview, R.id.rl_change_avatar, R.id.ll_identity_verify,
            R.id.ll_receive_address, R.id.ll_banks_manage, R.id.ll_login_password,
            R.id.ll_pay_password, R.id.check_Gesture, R.id.iv_kaiguan, R.id.ll_clear_cache,
            R.id.ll_version, R.id.btn_exit, R.id.img_avatar ,R.id.rl_Gesture_psw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.img_avatar: //头像

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
            case R.id.ll_identity_verify: //身份认证
                // startActivity(new Intent(SettingActivity.this, FourPartAct.class));
                break;
            case R.id.ll_receive_address: //地址管理
                startActivity(new Intent(SettingActivity.this,
                        AddressManageActivity.class));
                break;

            case R.id.rl_change_avatar: //姓名手机号那一栏

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

               /* if (!"1".equals(realVerify)) {

                   *//* startActivity(new Intent(SettingActivity.this,
                            FourPartActivity.class));*//*
                    startActivityForResult(new Intent(SettingActivity.this,
                            RealNameActivity.class), 19);
                }*/

                break;
            case R.id.ll_banks_manage: //银行卡
                if ("1".equals(realVerify)) {
                    //这个感觉有问题,单笔 限额的
                    //startActivity(new Intent(SettingActivity.this, BankManageActivity.class));
                    //老版本的 后台没改好,还是用新的吧

                    startActivity(new Intent(SettingActivity.this,
                            MeBankActivity.class));
                } else {
//                    startActivity(new Intent(SettingActivity.this,
//                            FourPartActivity.class));
                    startActivityForResult(new Intent(SettingActivity.this,
                            RealNameActivity.class), 19);
                }

                break;
            case R.id.ll_login_password://修改登录密码

                 startActivity(new Intent(SettingActivity.this,
                         ModifyPswActivity.class)
                         .putExtra("isFrom",0));
                break;
            case R.id.ll_pay_password://设置?修改交易密码
                if ("1".equals(preferences.getString("tpwdFlag", ""))) {//tpwdFlag=1代表修改交易密码
                       startActivity(new Intent(SettingActivity.this,
                               ModifyPswActivity.class).putExtra("isFrom",1));
                } else {
                     startActivity(new Intent(SettingActivity.this,
                             SetTradePswActivity.class).putExtra("phone",mobilephone));
                }
                //startActivity(new Intent(SettingActivity.this, TransactionPswAct.class).putExtra("uid", preferences.getString("uid", "")).putExtra("phone_num", preferences.getString("phone", "")).putExtra("isForget", isSetTradePsw));
                break;

            case R.id.rl_Gesture_psw:
            case R.id.check_Gesture:

                if (!check_Gesture) { //没有设置时
                    if(!LocalApplication.getInstance().sharereferences.getBoolean("loginshoushi", false)){
                        startActivity(new Intent(SettingActivity.this,
                                GestureEditActivity.class));
                    }
                }else{

                    DialogMaker.showRedSureDialog(SettingActivity.this, "手势密码", "是否删除手势密码？", "取消", "确定", new DialogMaker.DialogCallBack() {
                        @Override
                        public void onButtonClicked(Dialog dialog, int position, Object tag) {
                            check_Gesture= false;
                            tvStatusGesture.setText("未开启");

                            ToastMaker.showShortToast("已删除手势密码") ;
                            editor.putBoolean("loginshoushi", false);
                            editor.putString("gesturePsd", "");
                            editor.commit();
                        }

                        @Override
                        public void onCancelDialog(Dialog dialog, Object tag) {

                        }
                    }, "");
                }

                break;

            case R.id.iv_kaiguan:
                if (isOpenUpush) {
                    LogUtils.i("--->关闭友盟推送");
                    closeUPush();
                } else {
                    LogUtils.i("--->开启友盟推送");
                    openUPush();
                }
                break;
            case R.id.ll_clear_cache: //清理缓存
                DialogMaker.showRedSureDialog(SettingActivity.this, "提示", "您确定要清楚所有缓存数据吗？", "取消", "清除", new DialogMaker.DialogCallBack() {
                    @Override
                    public void onButtonClicked(Dialog dialog, int position, Object tag) {
                        clearAllCache(SettingActivity.this);
                        ToastMaker.showShortToast("清除完毕");
                        tvCache.setText("0M");
                    }

                    @Override
                    public void onCancelDialog(Dialog dialog, Object tag) {

                    }
                }, "");
                break;
            case R.id.ll_version: //版本号
                getReNewAl();
                break;
            case R.id.btn_exit: //退出登录
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
                    if (photo_path.contains(".jpg")) {
                        type = ".jpg";
                    } else if (photo_path.contains(".png")) {
                        type = ".png";
                    } else if (photo_path.contains(".gif")) {
                        type = ".gif";
                    }
                    LogUtils.i("--->selected photos = " + photo_path);
                    LogUtils.i("--->selected photos type= " + type);
                    //Toast.makeText(this, "selected photos size = " + photoLists.size() + "\n" + photoLists.toString(), Toast.LENGTH_LONG).show();
                    File file = new File(photoLists.get(0));
                    try {
                        if (file.exists()) {
                            long imageSize = getFileSize(file);
                            LogUtils.i("--->imageSize：" + imageSize);
                            if (imageSize > 5 * 1024 * 1024) {
                                ToastMaker.showShortToast("照片不能超过5M");
                                return;
                            }
                            //you can do something
                            Bitmap bitmap = BitmapFactory.decodeFile(photoLists.get(0));
                            //img_avatar.setImageBitmap(bitmap);
                            //LogUtils.i("--->bitmap " + bitmap.getConfig());


                            String image = Bitmap2StrByBase64(bitmap);
                            uploadAvatar(image, type);

                            //需要先将bitmap对象转换为字节,在加载;
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] bytes = baos.toByteArray();

                            Glide.with(SettingActivity.this)
                                    .load(bytes)
                                    .placeholder(R.drawable.icon_person_default_login)
                                    .error(R.drawable.icon_person_default_login)
                                    .into(imgAvatar);

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
        LogUtils.i("上传头像 uid：" + preferences.getString("uid", "") + " suffix: " + type + " image: " + image);
        OkHttpUtils.post().url(UrlConfig.UPLOADAVATAR)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("image", image)
                .addParam("suffix", type)
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2")
                .build()
                .execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                ToastMaker.showShortToast("上传头像成功");
                LogUtils.i("上传头像 response：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean(("success"))) {
                    JSONObject map = obj.getJSONObject("map");
                    String picImg = map.getString("picImg");
                    editor.putString("avatar_url", picImg);
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

    /**
     * 收货地址
     * **/
    private void getReceiptAddress() {


        /*showWaitDialog("加载中...", true, "");
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
                    //list
                    JSONArray list = map.getJSONArray("jsMemberInfo");
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
        });*/
    }


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
                        LogUtils.e("个人设置" + response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            mobilephone = map.getString("mobilephone");
                            realVerify = map.getString("realVerify");
                            String tpwdFlag = map.getString("tpwdFlag");
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("realVerify", realVerify);
                            editor.putString("tpwdFlag", tpwdFlag);
                            editor.commit();

                            if ("1".equals(realVerify)) { //已经实名认证过
                                String realName = map.getString("realName");
                                String idCards = map.getString("idCards");
                                String bankid = map.getString("bankId");
                                String banknum = map.getString("bankNum");
                                String bankname = map.getString("bankName");

                                tvName.setText(realName);
                                tvIdCard.setText(idCards);

                                tvStatusBanks.setVisibility(View.GONE);
                                rlBank.setVisibility(View.VISIBLE);
                                tvBankName.setText(bankname);
                                tvBankNumber.setText("尾号" + banknum);

                                if (bp == null) {
                                    bp = new BankName_Pic();
                                }
                                Integer pic = bp.bank_Pic(bankid);
                                ivBank.setImageDrawable(getResources().getDrawable(pic));



                              /*  tvStatusVerify.setText(realName);
                                tvStatusVerify.setTextColor(0xFFAFB0B7);
                                llIdentityVerify.setClickable(false);
                                tvStatusBanks.setText("已添加");*/


                            } else {
                                tvName.setText(mobilephone);
                                tvIdCard.setText("为确保资金安全请先完成实名认证");
                                tvStatusBanks.setVisibility(View.VISIBLE);
                                rlBank.setVisibility(View.GONE);
                                tvStatusBanks.setText("未绑定");


                              /*  tvStatusVerify.setText("去设置");
                                tvStatusVerify.setTextColor(0xFFFF8347);
                                llIdentityVerify.setClickable(true);*/


                            }

                            if ("1".equals(tpwdFlag)) {
                                tvStatusPayPsw.setTextColor(0xFF999999);
                                tvStatusPayPsw.setText("修改");
                            } else {
                                tvStatusPayPsw.setTextColor(0xFFEE4845);
                                tvStatusPayPsw.setText("未设置");
                            }

                          /*  if(updateLoginPsw){
                                tvStatusLoginPsw.setText("已修改");
                            }else{
                                tvStatusLoginPsw.setText("去修改");
                            }*/
                            tvPhonenum.setText(mobilephone);


                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(SettingActivity.this).show_Is_Login();
                            //finish();
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

    /*
    * 将突破转化成base64
    * */
    public String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void openUPush() {
        mPushAgent.enable(new IUmengCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("--->开启友盟推送成功");
                //mHandler.sendEmptyMessage(0);
                editor.putBoolean("isOpenUpush", true);
                editor.commit();
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.i("--->开启友盟推送失败");
                LogUtils.i("--->开启友盟推送失败，s：" + s);
                LogUtils.i("--->开启友盟推送失败，s1：" + s1);
            }
        });
    }

   /* private Handler mHandler = new Handler() {
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
    };*/


    private void exit_dr() {
        String token = preferences.getString("token", "");
        editor.clear();
        editor.putBoolean("login", false);
        editor.putBoolean("FirstLog", false);
        editor.putBoolean("isOpenUpush", isOpenUpush);
        editor.putString("avatar_url", "");
        //editor.putString("token", token);
        editor.commit();
        finish();
        LocalApplication.getInstance().getMainActivity().isHomeChecked = true;
        LocalApplication.getInstance().getMainActivity().isHome = true;
        LocalApplication.getInstance().getMainActivity().isExit = true;

    }

    /**
     * 获取指定文件大小
     *
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
                LogUtils.e("版本"+response);
                dismissDialog();
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    maxVersion = map.getString("maxVersion");
                    if (maxVersion != null && !maxVersion.equalsIgnoreCase("")) {
                        if (StringCut.compareVersion(maxVersion, LocalApplication.localVersion) > 0) {
                            dialog = showAlertDialog("版本更新",
                                    StringCut.HuanHang_Cut(content),
                                    new String[]{"取消", "立即更新"},
                                    true, true,
                                    "update");
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
                //mHandler.sendEmptyMessage(1);
                editor.putBoolean("isOpenUpush", false);
                editor.commit();
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.i("--->关闭友盟推送失败，s：" + s);
                LogUtils.i("--->关闭友盟推送失败，s1：" + s1);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
