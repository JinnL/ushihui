package com.mcz.xhj.yz.dr_app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_adapter.RedListAdapter;
import com.mcz.xhj.yz.dr_app.me.AssetsAndIncomeActivity;
import com.mcz.xhj.yz.dr_app.me.MessageCenterActivity;
import com.mcz.xhj.yz.dr_app.me.MyInvestmentActivity;
import com.mcz.xhj.yz.dr_app.me.TiyanConponsListActivity;
import com.mcz.xhj.yz.dr_app_fragment.BaseFragment;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.RedListBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.PersonalToast;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

import static com.mcz.xhj.R.id.title_leftimageview;

public class PersonFrag extends BaseFragment implements OnClickListener {
    @BindView(R.id.ptr_person)
    PtrClassicFrameLayout ptr_person;
    @BindView(R.id.ll_myassets)
    LinearLayout ll_myassets;
    @BindView(R.id.ll_userinfo)
    LinearLayout ll_userinfo;
    @BindView(R.id.ll_myinvest)
    LinearLayout ll_myinvest;
    @BindView(R.id.ll_myInvite)
    LinearLayout ll_myInvite;
    @BindView(R.id.ll_conpons)
    LinearLayout ll_conpons;
    @BindView(R.id.ll_cashin)
    LinearLayout ll_cashin;
    @BindView(R.id.ll_cashout)
    LinearLayout ll_cashout;
    @BindView(R.id.ll_newinvest)
    LinearLayout ll_newinvest;
    @BindView(R.id.tv_phone)
    @Nullable
    TextView tv_phone;
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_amount_secret)
    TextView tv_amount_secret;
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.tv_balance_secret)
    TextView tv_balance_secret;
    @BindView(R.id.tv_shouyi)
    TextView tv_shouyi;
    @BindView(R.id.tv_shouyi_secret)
    TextView tv_shouyi_secret;
    @BindView(R.id.tv_red)
    TextView tv_red;
    @BindView(R.id.tv_safe_add)
    TextView tv_safe_add;
    @BindView(R.id.tv_tiyan)
    TextView tv_tiyan;
    @BindView(R.id.cb_eye)
    CheckBox cb_eye;
    @BindView(title_leftimageview)
    ImageView leftima;
    @BindView(R.id.title_centertextview)
    TextView centertv;
    @BindView(R.id.title_rightimageview)
    ImageView iv_rightiv;
    @BindView(R.id.iv_person_conpons)
    ImageView iv_person_conpons;
    @BindView(R.id.toolbar)
    View toolbar;
    @BindView(R.id.ll_zongzichan)
    LinearLayout llZongzichan;
    @BindView(R.id.ll_keyongyue)
    LinearLayout llKeyongyue;
    @BindView(R.id.iv_invite)
    ImageView iv_invite;
    private PersonalToast toast;
    private boolean friendPop = false;
    private int isFuiou = 1;//存管 0：未开通  1：开通

    public static PersonFrag instance() {
        PersonFrag view = new PersonFrag();
        return view;
    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.frag_person;
    }

    @Override
    protected void initParams() {
        leftima.setImageResource(R.mipmap.setting);
        leftima.setVisibility(View.VISIBLE);
        iv_rightiv.setVisibility(View.VISIBLE);
        if (LocalApplication.getInstance().getMainActivity().hasNavigationBar) {
            toolbar.setVisibility(View.GONE);
        }
        centertv.setText(stringCut.phoneCut(preferences.getString("phone", "")));
        ll_myassets.setOnClickListener(this);
        ll_userinfo.setOnClickListener(this);
        ll_myinvest.setOnClickListener(this);
        ll_conpons.setOnClickListener(this);
        ll_cashin.setOnClickListener(this);
        ll_cashout.setOnClickListener(this);
        ll_myInvite.setOnClickListener(this);
        iv_rightiv.setOnClickListener(this);
        leftima.setOnClickListener(this);
        iv_person_conpons.setOnClickListener(this);
        tv_shouyi.setOnClickListener(this);
        llZongzichan.setOnClickListener(this);
        llKeyongyue.setOnClickListener(this);
        tv_shouyi_secret.setOnClickListener(this);
        ll_newinvest.setOnClickListener(this);
        ptr_person.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content,
                                             View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        toast = new PersonalToast(mContext);

    }

    private String newHandPid = "";
    private boolean isPerfect = false;
    private String pid;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview://设置
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, SettingActivity.class));
                }
                break;
            case R.id.tv_shouyi://收益
            case R.id.tv_shouyi_secret://收益
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, AssetsAndIncomeActivity.class).putExtra("isFrom", "1"));
                }
//                ((MainActivity)getActivity()).getAndSetTag();
                break;
            case R.id.ll_zongzichan://总资产
            case R.id.ll_keyongyue://可用余额
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, AssetsAndIncomeActivity.class).putExtra("isFrom", "0"));
                }
                break;
            case R.id.ll_myInvite://我的邀请
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, MyInviteAct.class));
                }
                break;
            case R.id.ll_myassets://体验金
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, TiyanConponsListActivity.class));
                }
                break;
            case R.id.ll_userinfo://安全中心
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, UserInfoAct.class));
                }
                break;
            case R.id.ll_myinvest://我的投资
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, MyInvestmentActivity.class));
                }
                break;
            case R.id.ll_cashin://快速充值
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    memberSetting(1);
                }
                break;
            case R.id.ll_newinvest:
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
//                    if(newHandPid!=null&&!newHandPid.equalsIgnoreCase("")){
//                        startActivity(new Intent(mContext, Act_Detail_Pro_New.class).putExtra("pid", newHandPid));
//                    }

                }
                break;
            case R.id.ll_cashout://提现
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    memberSetting(2);
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
//		case R.id.ll_gift_song:
//			if(investSendUrl!=null&&!investSendUrl.equalsIgnoreCase("")){
//				startActivity(new Intent(mContext,WebViewActivity.class)
//						.putExtra("URL", investSendUrl)
//						.putExtra("TITLE", "投资即送")
//						.putExtra("BANNER", "banner"));
//			}
//			break;
            case R.id.title_rightimageview://消息中心
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    startActivity(new Intent(mContext, MessageCenterActivity.class));
                }
                break;
            case R.id.iv_person_conpons:
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                    return;
                }

                break;

            default:
                break;
        }

    }

    private View layout;
    private PopupWindow popupWindow;
    private RedListAdapter rlAdapter;
    private List<RedListBean> lsrb = new ArrayList<RedListBean>();

    @SuppressLint("NewApi")
    public void showPopupWindowLogin() {
        // 加载布局
        layout = LayoutInflater.from(mContext).inflate(R.layout.pop_person_login, null);
        // 找到布局的控件
        // 实例化popupWindow
        popupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        TextView peron_login_close = (TextView) (layout).findViewById(R.id.peron_login_close);
        TextView bt_reg = (Button) (layout).findViewById(R.id.bt_reg);
        TextView bt_login = (Button) (layout).findViewById(R.id.bt_login);
        // 控制键盘是否可以获得焦点
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
//		// 设置popupWindow弹出窗体的背景
//		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        bt_reg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewRegisterActivity.class));
            }
        });
        bt_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewLoginActivity.class));
            }
        });
        peron_login_close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//				backgroundAlpha(1f);
                popupWindow.dismiss();
                return true;
            }
        });
        popupWindow.showAsDropDown(peron_login_close);
        backgroundAlpha(0.5f);
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
                        if (obj.getBoolean(("success"))) {
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

    private boolean isFirst = false;

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            //LogUtils.i("PersonFrag--->onHiddenChanged----->");

        } else {
        }
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //LogUtils.i("PersonFrag--->onResume----->getData()");
        getData();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private double amount = 0;
    private double inviteMoney = 0;//邀请好友获得收益
    private Integer afid;
    private boolean isRedPacket = false;
    private boolean isPayment = false;
    private boolean isNewHand = false;
    private String investSendUrl = "";
    private String prizeType;//奖品类型 1 = 50元话费， 0 = 其他奖品
    private String investId;//投资id

    private void getData() {
        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            tv_shouyi.setText("--");
            tv_amount.setText("--");
            tv_balance.setText("--");
            tv_red.setText("");
            centertv.setText("小行家");
            ptr_person.refreshComplete();
            tv_tiyan.setText("");
            tv_safe_add.setText("");
            ll_newinvest.setVisibility(View.GONE);
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
                LogUtils.i("--->personInfo " + result);
                if (ptr_person != null) {
                    ptr_person.refreshComplete();
                }
                centertv.setText(stringCut.phoneCut(preferences.getString("phone", "")));
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    Integer realname = objmap.getInteger("realVerify");//是否实名
                    String sex = objmap.getString("sex");
                    String unTiedCardTitle = objmap.getString("unTiedCardTitle");
                    investSendUrl = objmap.getString("investSendUrl");
                    double balance = objmap.getDoubleValue("balance");
                    double shouyi = objmap.getDoubleValue("accumulatedIncome");
                    double free = objmap.getDoubleValue("free");//冻结金额
                    double daishou = objmap.getDoubleValue("wprincipal");//代收本金
//                    isFuiou = objmap.getIntValue("isFuiou");//是否开通存管
                    String tiyan_money = stringCut.getNumKbs(objmap.getDoubleValue("availableExperience"));//体验金
                    Integer unuseConponsNum = objmap.getInteger("unUseFavourable");
                    if (objmap.getString("isNewHand") != null) {
                        isNewHand = objmap.getBoolean("isNewHand");
                    } else {
                        isNewHand = false;
                    }
//                    if(realname==0){
//                        tv_safe_add.setText(unTiedCardTitle);
//                    }else{
//                        tv_safe_add.setText("");
//                    }
                    newHandPid = objmap.getString("newHandId");
                    if (objmap.getBoolean("isPerfect") != null) {
                        isPerfect = objmap.getBoolean("isPerfect");
                        pid = objmap.getString("pid");
                        investId = objmap.getString("investId");
                        prizeType = objmap.getString("prizeType");
                        if (!isPerfect) {
                            ll_newinvest.setVisibility(View.VISIBLE);
                        } else {
                            ll_newinvest.setVisibility(View.GONE);
                        }
                    }
//                    if(isNewHand){
//                        ll_newinvest.setVisibility(View.VISIBLE);
//                    }else {
//                        ll_newinvest.setVisibility(View.GONE);
//                    }
                    if (!tiyan_money.equalsIgnoreCase("0")) {
                        tv_tiyan.setText(tiyan_money + "元");
                    } else {
                        tv_tiyan.setText("");
                    }
                    amount = balance + free + daishou;
                    tv_balance.setText(stringCut.getNumKb(balance));
                    tv_shouyi.setText(stringCut.getNumKb(shouyi));
                    tv_amount.setText(stringCut.getNumKb(amount));
                    afid = objmap.getInteger("afid");
                    inviteMoney = objmap.getDoubleValue("unclaimed");
                    if (inviteMoney > 0) {
                        iv_invite.setVisibility(View.VISIBLE);
                    } else {
                        iv_invite.setVisibility(View.GONE);
                    }
//					isRedPacket = objmap.getBooleanValue("isRedPacket");
//					isPayment = objmap.getBooleanValue("isPayment");
//					if(isPayment){//是否有回款弹出框
////						iv_person_conpons.setVisibility(View.VISIBLE);
//						iv_person_conpons.setImageResource(R.mipmap.huikuan_red_icon);
//						if(preferences.getBoolean("login", false) && !preferences.getBoolean("autoInvest", false) ){
//							SharedPreferences.Editor editor = preferences.edit() ;
//							editor.putBoolean("autoInvest", true) ;
//							editor.commit() ;
//							showPopupWindowHuikuan();
//						}
//					}else{
//						if(isRedPacket){//是否有促投红包弹出框
////							iv_person_conpons.setVisibility(View.VISIBLE);
//							iv_person_conpons.setImageResource(R.mipmap.icon_person_conp);
////							TranslateAnimation animation1 = new TranslateAnimation(0, -5, 0, 0);
////							animation1.setInterpolator(new OvershootInterpolator());
////							animation1.setDuration(200);
////							animation1.setRepeatCount(10);
////							animation1.setRepeatMode(Animation.REVERSE);
////							iv_person_conpons.startAnimation(animation1);
//							if(preferences.getBoolean("login", false) && !preferences.getBoolean("autoInvest", false) ){
//								SharedPreferences.Editor editor = preferences.edit() ;
//								editor.putBoolean("autoInvest", true) ;
//								editor.commit() ;
//								getRedBao("conpons");
//							}
//						}else{
//							iv_person_conpons.setVisibility(View.GONE);
//						}
//					}
                    Integer msg = objmap.getInteger("unReadMsg");//未读信息
//                    if (msg > 0) {
//                        iv_rightiv.setImageResource(R.mipmap.icon_news);
//                    } else {
//                        iv_rightiv.setImageResource(R.mipmap.icon_message);
//                    }
                    if (unuseConponsNum != null) {
                        if (unuseConponsNum > 0) {
                            tv_red.setText(unuseConponsNum + "个未用");
                        } else {
                            tv_red.setText("");
                        }
                    }
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    new show_Dialog_IsLogin(mContext).show_Is_Login();
                } else {
                    ToastMaker.showShortToast("服务器异常");
                }
                cb_eye.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            tv_amount.setVisibility(View.GONE);
                            tv_balance.setVisibility(View.GONE);
                            tv_shouyi.setVisibility(View.GONE);
                            tv_amount_secret.setVisibility(View.VISIBLE);
                            tv_balance_secret.setVisibility(View.VISIBLE);
                            tv_shouyi_secret.setVisibility(View.VISIBLE);
                        } else {
                            tv_amount.setVisibility(View.VISIBLE);
                            tv_balance.setVisibility(View.VISIBLE);
                            tv_shouyi.setVisibility(View.VISIBLE);
                            tv_amount_secret.setVisibility(View.GONE);
                            tv_balance_secret.setVisibility(View.GONE);
                            tv_shouyi_secret.setVisibility(View.GONE);

                        }

                    }
                });

//                if(isFuiou == 0){ //未开通
//                    DialogMaker.showCunguanDialogTwo(mContext, new DialogMaker.DialogCallBack() {
//                        @Override
//                        public void onButtonClicked(Dialog dialog, int position, Object tag) {
//                            mContext.startActivity(new Intent(mContext, OpenAccountActivity.class));
//                        }
//                        @Override
//                        public void onCancelDialog(Dialog dialog, Object tag) {
//
//                        }
//                    },"");
//                }
            }

            @Override
            public void onError(Call call, Exception e) {
                if (ptr_person != null) {
                    ptr_person.refreshComplete();
                }
                toast.toastShow("请检查网络");
            }
        });

    }

    @Override
    public void onButtonClicked(Dialog dialog, int position, Object tag) {
        // TODO Auto-generated method stub
        if (position == 0) {
            startActivity(new Intent(mContext, NewLoginActivity.class));
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = LocalApplication.getInstance().getMainActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        LocalApplication.getInstance().getMainActivity().getWindow().setAttributes(lp);
    }

}
