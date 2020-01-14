package com.mcz.xhj.yz.dr_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mcz.xhj.R;
import com.umeng.socialize.UMShareAPI;
import com.mcz.xhj.yz.dr_adapter.InvitInvestAdapter;
import com.mcz.xhj.yz.dr_adapter.RedListAdapter;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.InvitInvestBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.CustomShareBoard;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

import static com.mcz.xhj.R.id.btn_lingqu;
import static com.mcz.xhj.R.id.invitation_btn;

/**
 * 邀请好友
 */

public class MyInviteAct extends BaseActivity implements OnClickListener {
    @BindView(R.id.title_centertextview)
    TextView centertv;
    @BindView(R.id.title_leftimageview)
    ImageView leftima;
    @BindView(R.id.title_righttextview)
    TextView title_righttextview;
    @BindView(R.id.tv_leiji)
    TextView tvLeiji;
//    @BindView(R.id.tv_weilingqu)
//    TextView tvWeilingqu;
    @BindView(btn_lingqu)
    Button btnLingqu;
    @BindView(R.id.tv_leijirenshu)
    TextView tvLeijirenshu;
    @BindView(R.id.tv_endshoutou)
    TextView tvEndshoutou;
    @BindView(R.id.lv_jichu)
    ListView lvJichu;
    @BindView(R.id.tv_leijirenshu_jinjie)
    TextView tvLeijirenshuJinjie;
    @BindView(R.id.tv_endfutou)
    TextView tvEndfutou;
    @BindView(R.id.lv_jinjie)
    ListView lvJinjie;
    @BindView(R.id.tv_no_paiming)
    TextView tvNoPaiming;
    @BindView(R.id.iv_shangbang)
    ImageView ivShangbang;
    @BindView(R.id.tv_bottom)
    TextView tvBottom;
    @BindView(R.id.ptr_invest)
    PtrClassicFrameLayout ptrInvest;
    @BindView(R.id.invitation_btn)
    Button invitationBtn;
    @BindView(R.id.invitaion_iv)
    SimpleDraweeView invitaionIv;
    @BindView(R.id.ll_haspaiming)
    LinearLayout llHaspaiming;
    @BindView(R.id.tv_paiming)
    TextView tvPaiming;
    @BindView(R.id.iv_jichu_nojilu)
    ImageView ivJichuNojilu;
    @BindView(R.id.iv_jinjie_nojilu)
    ImageView ivJinjieNojilu;
    @BindView(R.id.rl_lingqu)
    RelativeLayout rlLingqu;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private InvitInvestAdapter mFirstAdapter;
    private InvitInvestAdapter mRepeatAdapter;
    private SharedPreferences preferences;
    private List<InvitInvestBean> mFirstList = new ArrayList<InvitInvestBean>();
    private List<InvitInvestBean> mRepeatList = new ArrayList<InvitInvestBean>();
    private View footer;
    private LinearLayout footerlayout;
    private TextView tv_footer;
    private boolean isLastitem = false;
    private boolean isLoading;
    private ProgressBar pb;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.invitation;
    }

    @Override
    protected void initParams() {
        preferences = LocalApplication.getInstance().sharereferences;
        title_righttextview.setVisibility(View.GONE);
        title_righttextview.setText("邀请规则");
        centertv.setText("我的邀请");
        // setListViewHeightBasedOnChildren(invitation_lv);
        // invitation_sv.smoothScrollTo(0, 0);
        getData();
        ptrInvest.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // TODO Auto-generated method stub
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame,
                                             View content, View header) {
//                if (mlslb.size() > 0) {
//                    if (invitation_lv.getChildAt(0).getTop() == 0) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                } else {
//                    return false;
//                }
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,
                        content, header);
            }
        });
        leftima.setOnClickListener(this);
        btnLingqu.setOnClickListener(this);
        invitationBtn.setOnClickListener(this);
        footer = View.inflate(this, R.layout.footer_layout, null);
        footerlayout = (LinearLayout) footer.findViewById(R.id.load_layout);
        pb = (ProgressBar) footer.findViewById(R.id.pb);
        tv_footer = (TextView) footer.findViewById(R.id.tv_footer);
        footerlayout.setVisibility(View.GONE);

        lvJichu.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    scrollView.requestDisallowInterceptTouchEvent(false);
                }else{
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        lvJinjie.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    scrollView.requestDisallowInterceptTouchEvent(false);
                }else{
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
    }

    private boolean friendPop = false;
    private Integer afid;

    private void LinQu() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.LINQU)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("afid", afid + "")
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
                            if (map.getDoubleValue("unclaimed") > 0) {
                                btnLingqu.setVisibility(View.VISIBLE);
                            } else {
                                btnLingqu.setVisibility(View.GONE);
                            }
                            String money = map.getString("amount");
//                            tvWeilingqu.setText("0");
                            friendPop = true;
                            showPopupWindow(money);
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(MyInviteAct.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统异常");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    private View layout;
    private PopupWindow popupWindow;
    private RedListAdapter rlAdapter;

    public void showPopupWindow(String money) {
        // 加载布局
        layout = (RelativeLayout) LayoutInflater.from(MyInviteAct.this).inflate(
                R.layout.dialog_lingqu, null);
        // 找到布局的控件
        // 实例化popupWindow
        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        TextView tv_money = (TextView) (layout).findViewById(R.id.tv_money);
        TextView tv_share = (TextView) (layout).findViewById(R.id.tv_share);
        TextView tv_cancel = (TextView) (layout).findViewById(R.id.tv_cancel);
        tv_money.setText(money + "元");
        // 控制键盘是否可以获得焦点
        popupWindow.setFocusable(true);
        // 设置popupWindow弹出窗体的背景
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        // 获取xoff
                int xpos = manager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
        // xoff,yoff基于anchor的左下角进行偏移。
        tv_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//			if(pid!=null&&!pid.equalsIgnoreCase("")){
//				startActivity(new Intent(mContext,Detail_New_ActFirst.class).putExtra("pid", pid));
//			}
                postShare(UrlConfig.ZHUCEZHENGCHANG);
//                LocalApplication.getInstance().getMainActivity().postShare("", UrlConfig.YAOZHUCE2);werwer
                popupWindow.dismiss();
            }
        });
        // 监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
        tv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAsDropDown(tv_cancel);
        backgroundAlpha(0.5f);
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

    private void getData() {

        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            return;
        }
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.FIRSTINVESTLIST)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String result) {
                        dismissDialog();
                        ptrInvest.refreshComplete();
                        JSONObject obj = JSON.parseObject(result);
                        LogUtils.i("我的邀请--->firstinvestlist "+obj);
                        if (obj.getBoolean(("success"))) {
                            mFirstList.clear();
                            mRepeatList.clear();
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray firstInvestList = map.getJSONArray("firstInvestList");
                            JSONArray repeatInvestList = map.getJSONArray("repeatInvestList");
                            mFirstList = JSON.parseArray(firstInvestList.toJSONString(), InvitInvestBean.class);
                            mRepeatList = JSON.parseArray(repeatInvestList.toJSONString(), InvitInvestBean.class);
                            JSONObject activity = map.getJSONObject("activity");
                            tv_footer.setVisibility(View.VISIBLE);
                            String appPutImg = activity.getString("appPutImg");
                            final String jumpUrl = activity.getString("appPutUrl");
                            if (appPutImg != null) {
                                Uri uri1 = Uri.parse(appPutImg);
                                invitaionIv.setImageURI(uri1);
                                if (jumpUrl != null) {
                                    invitaionIv.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(MyInviteAct.this, WebViewActivity.class)
                                                    .putExtra("URL", jumpUrl)
                                                    .putExtra("TITLE", "好友邀请")
                                                    .putExtra("BANNER", "banner")
                                            );

                                        }
                                    });
                                }
                            }
                            afid = map.getInteger("threePresentAfid");
                            tvLeiji.setText(stringCut.getNumKb(map.getDoubleValue("threePresentRewards")) + " 元");
                            tvLeijirenshu.setText(map.getString("recommendedCount"));
                            tvLeijirenshuJinjie.setText(map.getString("recommendedCount"));
//                            if (map.getDoubleValue("threePresentUnclaimed") > 0) {
//                                rlLingqu.setVisibility(View.VISIBLE);
//                                tvWeilingqu.setText(stringCut.getNumKb(map.getDoubleValue("threePresentUnclaimed")));
//                            } else {
//                                rlLingqu.setVisibility(View.GONE);
//                            }

                            tvEndshoutou.setText(map.getString("firstInvestCount"));
                            tvEndfutou.setText(map.getString("reInvestCount"));

                            if (mFirstList.size() > 0) {
                                lvJichu.setVisibility(View.VISIBLE);
                                ivJichuNojilu.setVisibility(View.GONE);
                                if (mFirstAdapter == null) {
                                    mFirstAdapter = new InvitInvestAdapter(MyInviteAct.this, mFirstList,"");
                                    lvJichu.setAdapter(mFirstAdapter);
                                } else {
                                    mFirstAdapter.onDateChange(mFirstList);
                                }

                            }else {
                                lvJichu.setVisibility(View.GONE);
                                ivJichuNojilu.setVisibility(View.VISIBLE);
                            }

                            if (mRepeatList.size() > 0) {
                                lvJinjie.setVisibility(View.VISIBLE);
                                ivJinjieNojilu.setVisibility(View.GONE);
                                if (mRepeatAdapter == null) {
                                    mRepeatAdapter = new InvitInvestAdapter(
                                            MyInviteAct.this, mRepeatList ,"");
                                    lvJinjie.setAdapter(mRepeatAdapter);
                                } else {
                                    mRepeatAdapter.onDateChange(mRepeatList);
                                }
                            }else {
                                lvJinjie.setVisibility(View.GONE);
                                ivJinjieNojilu.setVisibility(View.VISIBLE);
                            }

                            String nowRanking = map.getString("nowRanking");
                            if (!TextUtils.isEmpty(nowRanking)) {
                                int nowRankingNumber = (int)Double.parseDouble(nowRanking);
                                if(nowRankingNumber<11){
                                    llHaspaiming.setVisibility(View.VISIBLE);
                                    tvPaiming.setText(nowRankingNumber+"");
                                    tvBottom.setText(R.string.invint_has_paiming);
                                    ivShangbang.setImageResource(R.mipmap.has_mingci);
                                    tvNoPaiming.setVisibility(View.GONE);
                                }else{
                                    llHaspaiming.setVisibility(View.GONE);
                                    tvNoPaiming.setVisibility(View.VISIBLE);
                                    ivShangbang.setImageResource(R.mipmap.no_mingci);
                                    tvBottom.setText(R.string.invint_no_paiming);
                                }
                            } else {
                                llHaspaiming.setVisibility(View.GONE);
                                tvNoPaiming.setVisibility(View.VISIBLE);
                                ivShangbang.setImageResource(R.mipmap.no_mingci);
                                tvBottom.setText(R.string.invint_no_paiming);
                            }

                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
                            new show_Dialog_IsLogin(MyInviteAct.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统异常");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        ToastMaker.showShortToast("网络异常");
                        dismissDialog();
                    }
                });

    }

    // adapter.onDateChange(lslb);

    public void loadComplete() {
        isLoading = false;
        footerlayout.setVisibility(View.GONE);
    }

    // 1=未投资，2=已投资，3=收益金额升序，4=收益金额降序
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case invitation_btn:
                postShare(UrlConfig.ZHUCEZHENGCHANG);
//                startActivity(new Intent(MyInviteAct.this, InviteFriendsActivity.class));
                break;
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.btn_lingqu:
                LinQu();
                break;
            default:
                break;
        }
    }

    private void postShare(String url) {
        CustomShareBoard shareBoard = new CustomShareBoard(MyInviteAct.this, "", url, "zhengchang", "");
        shareBoard.showAtLocation(MyInviteAct.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 动态改变listView的高度
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            // totalHeight += 80;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // params.height = 80 * (listAdapter.getCount() - 1);
        // params.height = 80 * (listAdapter.getCount());
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        ((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        listView.setLayoutParams(params);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return super.onTouchEvent(event);
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate2(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
