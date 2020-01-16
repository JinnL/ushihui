package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.InvitInvestAdapter;
import com.mcz.xhj.yz.dr_app_fragment.BaseFragment;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/10/20
 * 描述：返利三重礼界面2.0
 */

public class ProfitFrag extends BaseFragment {
    @BindView(R.id.toolbar)
    View toolbar;
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.ptr_invest)
    PtrClassicFrameLayout ptrInvest;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.invitaion_iv)
    SimpleDraweeView invitaionIv;
    @BindView(R.id.tv_leiji)
    TextView tvLeiji;
    @BindView(R.id.tv_leijirenshu)
    TextView tvLeijirenshu;
    @BindView(R.id.tv_endshoutou)
    TextView tvEndshoutou;
    @BindView(R.id.lv_jichu)
    ListView lvJichu;
    @BindView(R.id.iv_jichu_nojilu)
    ImageView ivJichuNojilu;
    @BindView(R.id.tv_leijirenshu_jinjie)
    TextView tvLeijirenshuJinjie;
    @BindView(R.id.tv_endfutou)
    TextView tvEndfutou;
    @BindView(R.id.lv_jinjie)
    ListView lvJinjie;
    @BindView(R.id.iv_jinjie_nojilu)
    ImageView ivJinjieNojilu;
    @BindView(R.id.tv_rank_third)
    TextView tv_rank_third;
    @BindView(R.id.lv_third)
    ListView lvThird;
    @BindView(R.id.iv_third_nojilu)
    ImageView iv_third_nojilu;
    @BindView(R.id.invitation_btn)
    TextView invitationBtn;

    private SharedPreferences preferences;
    private InvitInvestAdapter mFirstAdapter;
    private InvitInvestAdapter mRepeatAdapter;
    private InvitInvestAdapter mThirdAdapter;
    private List<InvitInvestBean> mFirstList = new ArrayList<InvitInvestBean>();
    private List<InvitInvestBean> mRepeatList = new ArrayList<InvitInvestBean>();
    private List<InvitInvestBean> mThirdList = new ArrayList<InvitInvestBean>();

    public static ProfitFrag instance() {
        ProfitFrag view = new ProfitFrag();
        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_treble_gift;
    }

    @Override
    protected void initParams() {
        ptrInvest.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // TODO Auto-generated method stub
                getData();
                getData2();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        preferences = LocalApplication.getInstance().sharereferences;
        titleLeftimageview.setVisibility(View.GONE);
        titleCentertextview.setText("返利");

        lvJichu.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        lvJinjie.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        lvThird.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        invitationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    startActivity(new Intent(mContext, NewLoginActivity.class));
                } else {
                    postShare(UrlConfig.ZHUCEZHENGCHANG);
                }
            }
        });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        LogUtils.i("ProfitFrag--->onResume----->getData(1,2)");
        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            showPopupWindowLogin();
        }
        getData();
        getData2();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(mContext);
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

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mContext, NewLoginActivity.class).putExtra("point", "personFrag"));
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    private void getData() {
        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            tvLeiji.setText("--");
            tvLeijirenshu.setText("--");
            tvEndshoutou.setText("--");
            mFirstList.clear();
            lvJichu.setVisibility(View.GONE);
            ivJichuNojilu.setVisibility(View.VISIBLE);
            tvLeijirenshuJinjie.setText("--");
            tvEndfutou.setText("--");
            mRepeatList.clear();
            lvJinjie.setVisibility(View.GONE);
            ivJinjieNojilu.setVisibility(View.VISIBLE);

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
                        LogUtils.i("--->ProfitFrag 我的邀请: " + obj);
                        if (obj.getBoolean(("success"))) {
                            mFirstList.clear();
                            mRepeatList.clear();
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray firstInvestList = map.getJSONArray("firstInvestList");
                            JSONArray repeatInvestList = map.getJSONArray("repeatInvestList");
                            mFirstList = JSON.parseArray(firstInvestList.toJSONString(), InvitInvestBean.class);
                            mRepeatList = JSON.parseArray(repeatInvestList.toJSONString(), InvitInvestBean.class);
                            JSONObject activity = map.getJSONObject("activity");
                            String appPutImg = activity.getString("appPutImg");
                            final String jumpUrl = activity.getString("appPutUrl");
                            /*if (appPutImg != null) {
                                Uri uri1 = Uri.parse(appPutImg);
                                invitaionIv.setImageURI(uri1);
                                if (jumpUrl != null) {
                                    invitaionIv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(mContext, WebViewActivity.class)
                                                    .putExtra("URL", jumpUrl)
                                                    .putExtra("TITLE", "好友邀请")
                                                    .putExtra("BANNER", "banner")
                                            );

                                        }
                                    });
                                }
                            }*/
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
                                lvJichu.setAdapter(new InvitInvestAdapter(mContext, mFirstList, ""));

                            } else {
                                lvJichu.setVisibility(View.GONE);
                                ivJichuNojilu.setVisibility(View.VISIBLE);
                            }

                            if (mRepeatList.size() > 0) {
                                lvJinjie.setVisibility(View.VISIBLE);
                                ivJinjieNojilu.setVisibility(View.GONE);
                                lvJinjie.setAdapter(new InvitInvestAdapter(mContext, mRepeatList, ""));
                            } else {
                                lvJinjie.setVisibility(View.GONE);
                                ivJinjieNojilu.setVisibility(View.VISIBLE);
                            }

                            //三重礼排名
                            String nowRanking = map.getString("nowRanking");
                            if (!TextUtils.isEmpty(nowRanking)) {
                                int nowRankingNumber = (int) Double.parseDouble(nowRanking);
                                if (nowRankingNumber > 10) {
                                    tv_rank_third.setText("您还未上榜，马上邀请冲榜！");
                                } else {
                                    String text = "您的当前排名第" + nowRanking + "名";
                                    SpannableString spannableString3 = new SpannableString(text);
                                    spannableString3.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5111")), 7, 7 + nowRanking.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    spannableString3.setSpan(new RelativeSizeSpan(1.5f), 7, 7 + nowRanking.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    tv_rank_third.setText(spannableString3);
                                }
                            } else {

                            }

                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(mContext).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统异常");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        if (ptrInvest != null) {
                            ptrInvest.refreshComplete();
                        }
                        ToastMaker.showShortToast("网络异常");
                        dismissDialog();
                    }
                });

    }

    private void getData2() {
        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            return;
        }
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.GETRANKINGLIST)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        ptrInvest.refreshComplete();
                        dismissDialog();
                        LogUtils.i("--->ProfitFrag 第三重礼: " + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray thirdInvestList = map.getJSONArray("top");
                            if (thirdInvestList != null) {
                                mThirdList = JSON.parseArray(thirdInvestList.toJSONString(), InvitInvestBean.class);

                                if (mThirdList.size() > 0) {
                                    lvThird.setVisibility(View.VISIBLE);
                                    iv_third_nojilu.setVisibility(View.GONE);
                                    lvThird.setAdapter(new InvitInvestAdapter(mContext, mThirdList, "third"));
                                } else {
                                    lvThird.setVisibility(View.GONE);
                                    iv_third_nojilu.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        if (ptrInvest != null) {
                            ptrInvest.refreshComplete();
                        }
                        ToastMaker.showShortToast("网络异常");
                        dismissDialog();
                    }
                });
    }

    /**
     * 分享
     */
    private void postShare(String url) {
        CustomShareBoard shareBoard = new CustomShareBoard(LocalApplication.getInstance().getMainActivity(), "", url, "zhengchang", "");
        shareBoard.showAtLocation(LocalApplication.getInstance().getMainActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

}
