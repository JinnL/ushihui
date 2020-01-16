package com.ekabao.oil.ui.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.InvitInvestAdapter;
import com.ekabao.oil.bean.InvitInvestBean;
import com.ekabao.oil.bean.InviteBean;
import com.ekabao.oil.bean.InviteRank;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.view.CustomShareBoard;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.show_Dialog_IsLogin;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class InviteFriendsActivity extends BaseActivity {


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
    @BindView(R.id.invitaion_iv)
    SimpleDraweeView invitaionIv;

    @BindView(R.id.tv_leiji)
    TextView tvLeiji;
    @BindView(R.id.lv_jichu)
    ListView lvJichu;

    @BindView(R.id.lv_jinjie)
    ListView lvJinjie;
    @BindView(R.id.tv_rank_third)
    TextView tvRankThird;
    @BindView(R.id.ll_no_board)
    LinearLayout llNoBoard;
    @BindView(R.id.lv_third)
    ListView lvThird;
    @BindView(R.id.iv_third_nojilu)
    ImageView ivThirdNojilu;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.invitation_btn)
    TextView invitationBtn;
    /**
     * 邀请好友三重礼
     *
     * @time 2018/7/24 16:02
     * Created by
     */

    private SharedPreferences preferences;
    private InvitInvestAdapter mFirstAdapter;
    private InvitInvestAdapter mRepeatAdapter;
    private InvitInvestAdapter mThirdAdapter;

    private List<InvitInvestBean> mFirstList = new ArrayList<InvitInvestBean>();
    private List<InvitInvestBean> mRepeatList = new ArrayList<InvitInvestBean>();
    private List<InvitInvestBean> mThirdList = new ArrayList<InvitInvestBean>();
    private List<InviteRank> mTop = new ArrayList<InviteRank>(); //好友邀请排行榜

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_invite_friends);
        ButterKnife.bind(this);
    }

    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite_friends;
    }

    @Override
    protected void initParams() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                getData();
                getData2();
            }
        });


        preferences = LocalApplication.getInstance().sharereferences;

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

        getData();
        getData2();

        invitationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add("2018年2月22日\n131****6761好友充值了6个月套餐1800");
                getList();
                DialogMaker.showInviteDialog(InviteFriendsActivity.this, list);



            /*    LogUtils.e("invitationBtn");
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    startActivity(new Intent(InviteFriendsActivity.this, LoginActivity.class));
                } else {
                    postShare(UrlConfig.ZHUCEZHENGCHANG);
                }*/
            }
        });

        titleCentertextview.setText("邀请好友赢奖励");
        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 好友充值记录
     */
    private void getList() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.friendRankingList)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                //ptr_address.refreshComplete();
                LogUtils.e("好友充值记录：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    boolean mylist = map.containsKey("mylist");
                    if (mylist) {
                        JSONArray list = map.getJSONArray("mylist");
                        // LogUtils.e("所有的收货地址：" + list.size());
                        if (list.size() > 0) {
                            list.clear();
                            List<InviteBean> list1 = JSON.parseArray(list.toJSONString(), InviteBean.class);
                            for (int i = 0; i < list1.size(); i++) {
                                String dateYearToStrings = StringCut.getDateYearToStrings(list1.get(i).getInvestTime());
                                String message = list1.get(i).getMessage();
                                LogUtils.e(dateYearToStrings + message);
                                list.add(dateYearToStrings + message);
                            }
                        }
                    }
                }

            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                // ptr_address.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }


    @Override
    public void onResume() {

        super.onResume();
        LogUtils.i("ProfitFrag--->onResume----->getData(1,2)");
        /*if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            showPopupWindowLogin();
        }*/
        getData();
        getData2();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPause(this);
    }

    private View layout;
    private PopupWindow popupWindow;

   /* public void showPopupWindowLogin() {
        // 加载布局
        layout = LayoutInflater.from(this).inflate(R.layout.new_pop_person_login, null);
        // 找到布局的控件
        // 实例化popupWindow
        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
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

        // 设置popupWindow弹出窗体的背景
        setBackgroundAlpha(0.4f);//设置屏幕透明度
        // 监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
            }
        });
        ImageView imageView7 = (ImageView) (layout).findViewById(R.id.imageView7);
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(InviteFriendsActivity.this, NewLoginActivity.class).putExtra("point", "personFrag"));
                popupWindow.dismiss();
            }
        });
        ImageView iv_exit = (ImageView) (layout).findViewById(R.id.iv_exit);
        iv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(InviteFriendsActivity.this, NewLoginActivity.class).putExtra("point", "personFrag"));
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }*/

    private void getData() {
        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            tvLeiji.setText("--");

            mFirstList.clear();
            lvJichu.setVisibility(View.GONE);


            mRepeatList.clear();
            lvJinjie.setVisibility(View.GONE);


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
                        refreshLayout.finishRefresh();
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
                                            startActivity(new Intent(InviteFriendsActivity.this, WebViewActivity.class)
                                                    .putExtra("URL", jumpUrl)
                                                    .putExtra("TITLE", "好友邀请")
                                                    .putExtra("BANNER", "banner")
                                            );

                                        }
                                    });
                                }
                            }*/
                            tvLeiji.setText(StringCut.getNumKb(map.getDoubleValue("threePresentRewards")) + " 元");


                            if (mFirstList.size() > 0) {
                                lvJichu.setVisibility(View.VISIBLE);

                                lvJichu.setAdapter(new InvitInvestAdapter(InviteFriendsActivity.this, mFirstList, ""));

                            } else {
                                lvJichu.setVisibility(View.GONE);

                            }

                            if (mRepeatList.size() > 0) {
                                lvJinjie.setVisibility(View.VISIBLE);

                                lvJinjie.setAdapter(new InvitInvestAdapter(InviteFriendsActivity.this, mRepeatList, ""));
                            } else {
                                lvJinjie.setVisibility(View.GONE);

                            }

                            //三重礼排名
                            String nowRanking = map.getString("nowRanking");
                            if (!TextUtils.isEmpty(nowRanking)) {
                                int nowRankingNumber = (int) Double.parseDouble(nowRanking);
                                if (nowRankingNumber > 10) {
                                    tvRankThird.setText("您还未上榜，马上邀请冲榜！");
                                } else {
                                    String text = "您的当前排名第" + nowRanking + "名";
                                    SpannableString spannableString3 = new SpannableString(text);
                                    spannableString3.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5111")), 7, 7 + nowRanking.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    spannableString3.setSpan(new RelativeSizeSpan(1.5f), 7, 7 + nowRanking.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    tvRankThird.setText(spannableString3);
                                }
                            } else {

                            }

                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(InviteFriendsActivity.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统异常");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        refreshLayout.finishRefresh();
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
                        refreshLayout.finishRefresh();
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
                                    ivThirdNojilu.setVisibility(View.GONE);
                                    lvThird.setAdapter(new InvitInvestAdapter(InviteFriendsActivity.this, mThirdList, "third"));
                                } else {
                                    lvThird.setVisibility(View.GONE);
                                    ivThirdNojilu.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {

                        refreshLayout.finishRefresh();
                        ToastMaker.showShortToast("网络异常");
                        dismissDialog();
                    }
                });
    }

    /**
     * 分享
     */
    private void postShare(String url) {
        CustomShareBoard shareBoard = new CustomShareBoard(this, "", url, "zhengchang", "");
        shareBoard.showAtLocation(invitationBtn, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }

}
