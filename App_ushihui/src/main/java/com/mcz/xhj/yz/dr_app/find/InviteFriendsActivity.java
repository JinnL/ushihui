package com.mcz.xhj.yz.dr_app.find;

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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.UMShareAPI;
import com.mcz.xhj.yz.dr_adapter.MyRecommendAdapter;
import com.mcz.xhj.yz.dr_adapter.RedListAdapter;
import com.mcz.xhj.yz.dr_app.BaseActivity;
import com.mcz.xhj.yz.dr_app.WebViewActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.MyRecommend_bean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.CustomShareBoard;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：发现里面的好友邀请
 * 创建人：shuc
 * 创建时间：2017/4/19 13:25
 * 修改人：DELL
 * 修改时间：2017/4/19 13:25
 * 修改备注：
 */
public class InviteFriendsActivity  extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_centertextview)
    TextView centertv;
    @BindView(R.id.title_leftimageview)
    ImageView leftima;
    @BindView(R.id.title_righttextview)
    TextView title_righttextview;

    @BindView(R.id.invitation_ll)
    LinearLayout invitation_ll; // 上部 的布局
    @BindView(R.id.invitaion_iv)
    SimpleDraweeView invitaion_iv; // 上部的图片


    @BindView(R.id.tv_return_total)
    TextView tv_return_total;
    @BindView(R.id.cumulative_tv)
    TextView cumulative_tv; // 累计邀请人数
    @BindView(R.id.hstouzi_tv)
    TextView hstouzi_tv; // 已完成投资人数
    @BindView(R.id.invitation_btn)
    Button invitation_btn; // 立即邀请按钮
    @BindView(R.id.invitation_lv)
    ListView invitation_lv; // 好友列表
    // @BindView(R.id.invitation_sv)
    // private MyScrollView invitation_sv;
    @BindView(R.id.ptr_invest)
    PtrClassicFrameLayout ptr_invest;
    @BindView(R.id.ll_nobody)
    LinearLayout llNobody;
    @BindView(R.id.ll_linqu)
    LinearLayout ll_linqu;
    @BindView(R.id.tv_linqu_money)
    TextView tv_linqu_money;
    @BindView(R.id.tv_linqu)
    TextView tv_linqu;

    private SharedPreferences preferences;
    private List<MyRecommend_bean> lslb = new ArrayList<MyRecommend_bean>();
    private List<MyRecommend_bean> mlslb = new ArrayList<MyRecommend_bean>();// 每次加载的数据
    private MyRecommendAdapter adapter;
    private String OrderBy = "1"; // 1=未投资，2=已投资，3=收益金额升序，4=收益金额降序
    private String recommCodes;
    int page = 1;

    private View footer;
    private LinearLayout footerlayout;
    private TextView tv_footer;
    private boolean isLastitem = false;
    private boolean isLoading;
    private ProgressBar pb;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.my_invitation;
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
        ptr_invest.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // TODO Auto-generated method stub
                page = 1;
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame,
                                             View content, View header) {
                if (mlslb.size() > 0) {
                    if (invitation_lv.getChildAt(0).getTop() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
//				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
//						content, header);
            }
        });
        leftima.setOnClickListener(this);
        invitation_btn.setOnClickListener(this);
        tv_linqu.setOnClickListener(this);
        if ("".equals(preferences.getString("recommCodes", ""))) {
            recommCodes = preferences.getString("phone", "");
        } else {
            recommCodes = preferences.getString("recommCodes", "");
        }

        footer = View.inflate(this, R.layout.footer_layout, null);
        footerlayout = (LinearLayout) footer.findViewById(R.id.load_layout);
        pb = (ProgressBar) footer.findViewById(R.id.pb);
        tv_footer = (TextView) footer.findViewById(R.id.tv_footer);
        footerlayout.setVisibility(View.GONE);
        //invitation_lv.addFooterView(footer);
        invitation_lv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    isLastitem = true;
                } else {
                    isLastitem = false;
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                if (isLastitem && scrollState == SCROLL_STATE_IDLE) {
                    if (!isLoading) {
                        isLoading = true;
                        footerlayout.setVisibility(View.VISIBLE);
                        getData();
                    }
                }
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
                            if(map.getDoubleValue("unclaimed")>0){
                                ll_linqu.setVisibility(View.VISIBLE);
                            }else{
                                ll_linqu.setVisibility(View.GONE);
                            }
                            String money = map.getString("amount");
                            friendPop = true;
//							getData();
                            showPopupWindow(money);
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
        layout = (RelativeLayout) LayoutInflater.from(InviteFriendsActivity.this).inflate(
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
        WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        // 获取xoff
                int xpos = manager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
        // xoff,yoff基于anchor的左下角进行偏移。
        tv_share.setOnClickListener(new View.OnClickListener() {

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
        tv_cancel.setOnClickListener(new View.OnClickListener() {
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
        OkHttpUtils.post().url(UrlConfig.MYINVITATION)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("pageSize", "10")
                .addParams("pageOn", page + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String result) {
                        LogUtils.i("--->邀请好友："+result);
                        dismissDialog();
                        ptr_invest.refreshComplete();
                        JSONObject obj = JSON.parseObject(result);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray pageArray = map.getJSONObject("page").getJSONArray("rows");
                            mlslb = JSON.parseArray(pageArray.toJSONString(), MyRecommend_bean.class);
                            // invitation_btn.setText(stringCut.getNumKbs(Double.
                            // parseDouble(total))) ;
                            tv_footer.setVisibility(View.VISIBLE);
                            llNobody.setVisibility(View.GONE);
                            String total = map.getString("total"); // 好友数量
                            String investors = map.getString("investors"); // 完成投资人数
                            String rewards = map.getString("rewards");//返现金额
                            recommCodes = map.getString("recommCodes");
                            String banner = map.getString("banner"); // 推送banner
                            final String jumpUrl = map.getString("jumpUrl"); // 跳转链接
                            String startTime = map.getString("startTime"); // 活动开始时间
                            String endTime = map.getString("endTime"); // 活动结束时间
                            String content = map.getString("content"); // 活动内容
                            String isPut = map.getString("isPut"); // 是否推送
                            if ("0".equalsIgnoreCase(isPut)) { // 默认为1,0-不推送，1-推送
                                invitation_ll.setVisibility(View.GONE);
                            } else {
                                invitation_ll.setVisibility(View.VISIBLE);
                            }
                            //invitation_time_tv.setText(stampToDate(startTime) + " - " + stampToDate2(endTime));
                            //invitation_describe_tv.setText(content);
                            cumulative_tv.setText(total);
                            hstouzi_tv.setText(investors);
                            if(!TextUtils.isEmpty(rewards)){
                                tv_return_total.setText(rewards);
                            } else {
                                tv_return_total.setText("0.00");
                            }

                            if (banner != null) {
                                Uri uri1 = Uri.parse(banner);
                                invitaion_iv.setImageURI(uri1);
                                if (jumpUrl != null) {
                                    invitaion_iv
                                            .setOnClickListener(new View.OnClickListener() {

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
                            }

                            afid = map.getInteger("afid");
                            if (map.getDoubleValue("unclaimed") > 0) {
                                ll_linqu.setVisibility(View.VISIBLE);
                                tv_linqu_money.setText(stringCut.getNumKb(map.getDoubleValue("unclaimed")));
                            } else {
                                ll_linqu.setVisibility(View.GONE);
                            }
                            if (mlslb.size() > 0) {
                                if (page > 1) {
                                    lslb.addAll(mlslb);
                                } else {
                                    lslb = mlslb;
                                }
                                if (adapter == null || page == 1) {
                                    adapter = new MyRecommendAdapter(
                                            InviteFriendsActivity.this, lslb);
                                    invitation_lv.setAdapter(adapter);
                                } else {
                                    adapter.onDateChange(lslb);
                                }
                                loadComplete();
                                if (mlslb.size() == 10) {
                                    tv_footer.setText("上拉加载更多");
                                    footerlayout.setVisibility(View.VISIBLE);
                                    pb.setVisibility(View.GONE);
                                } else {
                                    tv_footer.setText("全部加载完毕");
                                    footerlayout.setVisibility(View.VISIBLE);
                                    pb.setVisibility(View.GONE);
                                }
                            } else {
                                tv_footer.setVisibility(View.VISIBLE);
                                tv_footer.setText("全部加载完毕");
                                footerlayout.setVisibility(View.VISIBLE);
                                pb.setVisibility(View.GONE);

                                if (page == 1) {
                                    if (mlslb.size() == 0) {
                                        llNobody.setVisibility(View.VISIBLE);
                                        tv_footer.setVisibility(View.GONE);
                                    } else {
                                        adapter = new MyRecommendAdapter(
                                                InviteFriendsActivity.this, mlslb);
                                        invitation_lv.setAdapter(adapter);
                                    }
                                }

                            }
                            page++;
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
                            new show_Dialog_IsLogin(InviteFriendsActivity.this).show_Is_Login();
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
            case R.id.invitation_btn:
                postShare(UrlConfig.ZHUCEZHENGCHANG);
                break;
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.tv_linqu:
                LinQu();
                break;
            default:
                break;
        }
    }

    private void postShare(String url) {
        CustomShareBoard shareBoard = new CustomShareBoard(InviteFriendsActivity.this, "", url, "zhengchang", "");
        shareBoard.showAtLocation(InviteFriendsActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
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
        ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0);
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
