package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.mcz.xhj.R;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_app.find.CallCenterActivity;
import com.mcz.xhj.yz.dr_app.me.AboutUsActivity;
import com.mcz.xhj.yz.dr_app.me.AssetsAnalysisActivity;
import com.mcz.xhj.yz.dr_app.me.FundsDetailsActivity;
import com.mcz.xhj.yz.dr_app.me.JifenActivity;
import com.mcz.xhj.yz.dr_app.me.NewMessageCenterActivity;
import com.mcz.xhj.yz.dr_app.me.NewMyInvestmentActivity;
import com.mcz.xhj.yz.dr_app.me.ExperienceConponsActivity;
import com.mcz.xhj.yz.dr_app_fragment.BaseFragment;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.SystemUtil;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.DialogMaker;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

/**
 * Created by zhulang on 2017/9/11.
 * 2.0版的个人中心板块
 */

public class NewPersonFrag extends BaseFragment {
    @BindView(R.id.toolbar)
    View toolbar;
    @BindView(R.id.img_avatar)
    CircleImageView img_avatar;
    @BindView(R.id.title_centertextview)
    TextView title_centertextview;
    @BindView(R.id.img_setting)
    ImageView img_setting;
    @BindView(R.id.img_message)
    ImageView img_message;
    @BindView(R.id.ll_total_asset)
    LinearLayout ll_total_asset;
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_amount_secret)
    TextView tv_amount_secret;
    @BindView(R.id.cb_eye)
    CheckBox cb_eye;
    @BindView(R.id.tv_jifen_secret)
    TextView tv_jifen_secret;
    @BindView(R.id.tv_jifen)
    TextView tv_jifen;
    @BindView(R.id.ll_jifen)
    LinearLayout ll_jifen;
    @BindView(R.id.tv_shouyi)
    TextView tv_shouyi;
    @BindView(R.id.tv_shouyi_secret)
    TextView tv_shouyi_secret;
    @BindView(R.id.ll_shouyi)
    LinearLayout ll_shouyi;
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.tv_balance_secret)
    TextView tv_balance_secret;
    @BindView(R.id.ll_keyongyue)
    LinearLayout ll_keyongyue;
    @BindView(R.id.ll_cashout)
    LinearLayout ll_cashout;
    @BindView(R.id.ll_cashin)
    LinearLayout ll_cashin;
    @BindView(R.id.tv_amount_holdingInvest)
    TextView tv_amount_holdingInvest;
    @BindView(R.id.tv_amount_redeem)//已赎回
    TextView tv_amount_redeem;
    @BindView(R.id.ll_myinvest)
    LinearLayout ll_myinvest;
    @BindView(R.id.ll_invest_holding)
    LinearLayout ll_invest_holding;
    @BindView(R.id.ll_invest_repayed)
    LinearLayout ll_invest_repayed;
    @BindView(R.id.tv_amount_conpons)
    TextView tv_amount_conpons;
    @BindView(R.id.ll_conpons)
    LinearLayout ll_conpons;
    @BindView(R.id.tv_tiyan)
    TextView tv_tiyan;
    @BindView(R.id.ll_tiyanjin)
    LinearLayout ll_tiyanjin;
    @BindView(R.id.tv_amount_jifen)
    TextView tv_amount_jifen;
    @BindView(R.id.ll_jifen_detail)
    LinearLayout ll_jifen_detail;
    @BindView(R.id.tv_amount_shouyi)
    TextView tv_amount_shouyi;
    @BindView(R.id.ll_myassets_detail)
    LinearLayout ll_myassets_detail;
    @BindView(R.id.ll_help_and_feedback)
    LinearLayout ll_help_and_feedback;
    @BindView(R.id.ll_kefu)
    LinearLayout ll_kefu;
    @BindView(R.id.tv_telPhone)
    TextView tv_telPhone;
    @BindView(R.id.ll_about)
    LinearLayout ll_about;
    @BindView(R.id.ll_guan_wei)
    LinearLayout ll_guan_wei;
    @BindView(R.id.ptr_person_new)
    PtrClassicFrameLayout ptr_person_new;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private double amount = 0;
    private double inviteMoney = 0;//邀请好友获得收益
    private Integer afid;
    private boolean isNewHand = false;
    private String investId;//投资id
    private String newHandPid = "";
    private boolean isPerfect = false;
    private String pid;
    private static final int CHANGE_AVATAIR = 1;
    private int scroll;
    private String telPhone;

    public static NewPersonFrag instance() {
        NewPersonFrag view = new NewPersonFrag();
        return view;
    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.frag_person_new;
    }

    @Override
    protected void initParams() {
        ptr_person_new.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //LocalApplication.getInstance().getMainActivity().isExit = false;
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

        });



        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            tv_shouyi.setText("--");
            tv_jifen.setText("--");
            tv_amount.setText("--");
            tv_balance.setText("--");
            title_centertextview.setText("小行家");
            tv_amount_holdingInvest.setText("0");
            tv_amount_redeem.setText("0");
            tv_amount_conpons.setText("0张");
            tv_tiyan.setText("0元");
            tv_amount_jifen.setText("0积分");
            tv_amount_shouyi.setText("0元");
            return;
        }

    }

    private void getData() {
        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            tv_shouyi.setText("--");
            tv_jifen.setText("--");
            tv_amount.setText("--");
            tv_balance.setText("--");
            title_centertextview.setText("小行家");
            tv_amount_holdingInvest.setText("0");
            tv_amount_redeem.setText("0");
            tv_amount_conpons.setText("0张");
            tv_tiyan.setText("0元");
            tv_amount_jifen.setText("0积分");
            tv_amount_shouyi.setText("0元");
            ptr_person_new.refreshComplete();

            LogUtils.i("--->登录弹框 isExit："+LocalApplication.getInstance().getMainActivity().isExit);
            if(preferences.getString("uid", "").equalsIgnoreCase("") && LocalApplication.getInstance().getMainActivity().isExit ){
                showPopupWindowLogin();
            }

            return;
        }
        OkHttpUtils.post()
                .url(UrlConfig.PERSONINFO)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                LogUtils.i("--->new_person_frag_Info " + result);
                if (ptr_person_new != null) {
                    ptr_person_new.refreshComplete();
                }
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    Integer realname = objmap.getInteger("realVerify");//是否实名
                    String sex = objmap.getString("sex");
                    String unTiedCardTitle = objmap.getString("unTiedCardTitle");
                    String realName = objmap.getString("realName");
                    if(TextUtils.isEmpty(realName)){
                        title_centertextview.setText(stringCut.phoneCut(preferences.getString("phone", "")));
                    }else{
                        title_centertextview.setText(realName);
                    }
                    double balance = objmap.getDoubleValue("balance");
                    double shouyi = objmap.getDoubleValue("accumulatedIncome");
                    double free = objmap.getDoubleValue("free");//冻结金额
                    double daishou = objmap.getDoubleValue("wprincipal");//代收本金
                    double myPoints = objmap.getDoubleValue("myPoints");//积分
                    double availableExperience = objmap.getDoubleValue("availableExperience");//体验金
//                    isFuiou = objmap.getIntValue("isFuiou");//是否开通存管
                    String tiyan_money = stringCut.getNumKbs(objmap.getDoubleValue("availableExperience"));//体验金
                    Integer unuseConponsNum = objmap.getInteger("unUseFavourable");
                    Integer tender = objmap.getInteger("tender");
                    Integer tenderFinished = objmap.getInteger("tenderFinished");
                    tv_amount_holdingInvest.setText(""+tender);
                    tv_amount_redeem.setText(""+tenderFinished);
                    tv_amount_shouyi.setText(shouyi+"元");
                    tv_amount_jifen.setText(myPoints+"积分");
                    if (objmap.getString("isNewHand") != null) {
                        isNewHand = objmap.getBoolean("isNewHand");
                    } else {
                        isNewHand = false;
                    }

                    newHandPid = objmap.getString("newHandId");
                    if (objmap.getBoolean("isPerfect") != null) {
                        isPerfect = objmap.getBoolean("isPerfect");
                        pid = objmap.getString("pid");
                        investId = objmap.getString("investId");

                    }

                    amount = balance + free + daishou;
                    tv_balance.setText(stringCut.getNumKb(balance));
                    tv_shouyi.setText(stringCut.getNumKb(shouyi));
                    tv_amount.setText(stringCut.getNumKb(amount));
                    tv_jifen.setText(stringCut.getNumKb(myPoints));
                    afid = objmap.getInteger("afid");
                    inviteMoney = objmap.getDoubleValue("unclaimed");


                    Integer msg = objmap.getInteger("unReadMsg");//未读信息
                    tv_tiyan.setText(availableExperience+"元");//可用体验金

                    if (unuseConponsNum != null) {
                        if (unuseConponsNum > 0) {
                            tv_amount_conpons.setText(unuseConponsNum +"张");
                        } else {
                            tv_amount_conpons.setText("0张");
                        }
                    }
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    new show_Dialog_IsLogin(mContext).show_Is_Login();
                } else {
                    ToastMaker.showShortToast("服务器异常");
                }
                cb_eye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            tv_amount.setVisibility(View.GONE);
                            tv_jifen.setVisibility(View.GONE);
                            tv_balance.setVisibility(View.GONE);
                            tv_shouyi.setVisibility(View.GONE);
                            tv_amount_secret.setVisibility(View.VISIBLE);
                            tv_jifen_secret.setVisibility(View.VISIBLE);
                            tv_balance_secret.setVisibility(View.VISIBLE);
                            tv_shouyi_secret.setVisibility(View.VISIBLE);
                        } else {
                            tv_amount.setVisibility(View.VISIBLE);
                            tv_jifen.setVisibility(View.VISIBLE);
                            tv_balance.setVisibility(View.VISIBLE);
                            tv_shouyi.setVisibility(View.VISIBLE);
                            tv_amount_secret.setVisibility(View.GONE);
                            tv_jifen_secret.setVisibility(View.GONE);
                            tv_balance_secret.setVisibility(View.GONE);
                            tv_shouyi_secret.setVisibility(View.GONE);

                        }

                    }
                });

            }

            @Override
            public void onError(Call call, Exception e) {
                if (ptr_person_new != null) {
                    ptr_person_new.refreshComplete();
                }
                ToastMaker.showShortToast("请检查网络");
            }
        });

    }

    private View layout;
    private PopupWindow popupWindow;

    public void showPopupWindowLogin() {
        // 加载布局
        layout = LayoutInflater.from(mContext).inflate(R.layout.new_pop_person_login, null);
        // 找到布局的控件
        // 实例化popupWindow
        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        TextView bt_login = (TextView) (layout).findViewById(R.id.bt_login);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bt_login.getLayoutParams();
        lp.setMargins(20, 10, 20, 30);
        bt_login.setLayoutParams(lp);
        // 控制键盘是否可以获得焦点
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return false;
            }
        });
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

                startActivity(new Intent(mContext, NewLoginActivity.class).putExtra("point", "personFrag"));
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    @OnClick({R.id.ll_tiyanjin,R.id.img_avatar, R.id.img_setting, R.id.img_message, R.id.ll_total_asset, R.id.ll_jifen, R.id.ll_shouyi, R.id.ll_keyongyue, R.id.ll_cashout, R.id.ll_cashin, R.id.ll_myinvest,R.id.ll_invest_holding,R.id.ll_invest_repayed, R.id.ll_conpons,R.id.ll_jifen_detail, R.id.ll_myassets_detail, R.id.ll_help_and_feedback, R.id.ll_kefu, R.id.ll_about, R.id.ll_guan_wei})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_avatar://头像
            case R.id.img_setting://设置
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, NewSettingActivity.class));
                }
                break;
            case R.id.img_message://消息中心
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, NewMessageCenterActivity.class));
                }
                break;

            case R.id.ll_total_asset://总资产
            case R.id.ll_shouyi://累计收益
            case R.id.ll_keyongyue://可用余额
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, AssetsAnalysisActivity.class));
                }
                break;
            case R.id.ll_jifen_detail:
            case R.id.ll_jifen: //我的积分
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, JifenActivity.class));
                }
                break;
            case R.id.ll_cashout:
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    memberSetting(2);
                }
                break;
            case R.id.ll_cashin:
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    memberSetting(1);
                }
                break;
            case R.id.ll_myinvest://我的投资
            case R.id.ll_invest_holding://我的投资-持有中
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, NewMyInvestmentActivity.class).putExtra("flag", "0"));
                }
                break;
            case R.id.ll_invest_repayed://我的投资-已还款
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, NewMyInvestmentActivity.class).putExtra("flag", "1"));
                }
                break;
            case R.id.ll_conpons://优惠券
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, ConponsAct.class));
                }
                break;
            case R.id.ll_tiyanjin://体验金
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, ExperienceConponsActivity.class));
                }
                break;
            case R.id.ll_myassets_detail://资金明细
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, FundsDetailsActivity.class));
                }
                break;
            case R.id.ll_help_and_feedback://帮助与反馈
                startActivity(new Intent(mContext, CallCenterActivity.class));
                break;
            case R.id.ll_kefu://联系客服
                DialogMaker.showKufuPhoneDialog(mContext);
                break;
            case R.id.ll_about:
                startActivity(new Intent(mContext, AboutUsActivity.class));
                break;
            case R.id.ll_guan_wei:
                if (!isAvilible(getContext(), "com.tencent.mm")) {
                    ToastMaker.showShortToast("请先安装微信");
                    return;
                } else {
                    copy(mContext);
                    DialogMaker.showRedSureDialog(getContext(), "小行家公众号", "微信号已成功复制，请前往微信搜索并关注我们吧~",
                            "稍后再去", "去关注", new DialogMaker.DialogCallBack() {

                                @Override
                                public void onButtonClicked(Dialog dialog, int position, Object tag) {
                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, "a");
                                    sendIntent.setType("text/plain");

                                    ComponentName cmp = new ComponentName("com.tencent.mm",
                                            "com.tencent.mm.ui.LauncherUI");
                                    sendIntent.setAction(Intent.ACTION_MAIN);
                                    sendIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    sendIntent.setComponent(cmp);
                                    mContext.startActivity(sendIntent);
                                }

                                @Override
                                public void onCancelDialog(Dialog dialog, Object tag) {

                                }
                            }, "");

                }
                break;
        }
    }

    public void copy(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context

                .getSystemService(Context.CLIPBOARD_SERVICE);

        cmb.setText(getResources().getString(R.string.weixin));

    }

    /**
     * 判断手机里有没有这个应用
     *
     * @param context
     * @param packageName
     * @return
     */
    public boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName
                    .equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //LogUtils.i("PersonFrag--->onResume----->getData()");
        String avatar_url = preferences.getString("avatar_url", "");
        if(!avatar_url.equals("")){
            Picasso.with(mContext).load(avatar_url).memoryPolicy(MemoryPolicy.NO_CACHE).into(img_avatar);
        }
        getData();
        getPlatFormInfo();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    private void getPlatFormInfo(){
        OkHttpUtils
                .post()
                .url(UrlConfig.GETPLATFORMINFO)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("---》客服电话"+response);
                        JSONObject obj = JSON.parseObject(response);
                        if(obj.getBoolean("success")){
                            JSONObject map = obj.getJSONObject("map");
                            telPhone = map.getString("platForm");
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("telPhone",telPhone);
                            editor.commit();
                            if(!TextUtils.isEmpty(telPhone)){
                                tv_telPhone.setText(telPhone);
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    private void memberSetting(final int flag) {
        showWaitDialog("加载中...", true, "");
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
                        if (obj.getBoolean("success")) {
                            JSONObject map = obj.getJSONObject("map");
                            String isRealName = map.getString("realVerify");
                            String ispwd = map.getString("tpwdFlag");
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("tpwdFlag", ispwd);
                            editor.commit();
                            if ("1".equals(isRealName)) {//已认证
                                if (flag == 1) {//快速充值
                                    MobclickAgent.onEvent(mContext, UrlConfig.point + 42 + "");
                                    startActivity(new Intent(mContext, CashInAct.class));
                                } else if (flag == 2) {//提现
                                    startActivity(new Intent(mContext, CashOutAct.class));
                                }
                            } else {//未认证
                                if (flag == 1) {
                                    MobclickAgent.onEvent(mContext, UrlConfig.point + 39 + "");
                                } else if (flag == 2) {
                                    MobclickAgent.onEvent(mContext, UrlConfig.point + 40 + "");
                                }
                                startActivity(new Intent(mContext, FourPartAct.class));
                            }
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(mContext).show_Is_Login();
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



}
