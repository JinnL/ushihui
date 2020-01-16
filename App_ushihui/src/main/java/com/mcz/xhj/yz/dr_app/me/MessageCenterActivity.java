package com.mcz.xhj.yz.dr_app.me;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.MessageCenterAdapter;
import com.mcz.xhj.yz.dr_app.BaseActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.Rows_Message;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：消息中心列表
 * 创建人：shuc
 * 创建时间：2017/2/24 11:28
 * 修改人：DELL
 * 修改时间：2017/2/24 11:28
 * 修改备注：
 */
public class MessageCenterActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.expandablelistviewmes)
    ExpandableListView expandablelistviewmes;
    @BindView(R.id.rl_no_nomessage)
    RelativeLayout rlNoNomessage;
    @BindView(R.id.ptr_invest)
    PtrClassicFrameLayout ptrInvest;


    private String uid;
    private SharedPreferences preferences;
    private List<Rows_Message> rows_List = new ArrayList<Rows_Message>();// 每次加载的数据
    private List<Rows_Message> mrows_List = new ArrayList<Rows_Message>();//  每次加载的数据
    private MessageCenterAdapter adapter;

    private boolean isLastitem = false;
    private View footer;
    private LinearLayout footerlayout;
    private TextView tv_footer;
    private ProgressBar pb;
    private boolean isLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.me_message_center;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("消息中心");

        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");
        footer = View.inflate(MessageCenterActivity.this, R.layout.footer_layout, null);
        footerlayout = (LinearLayout) footer.findViewById(R.id.load_layout);
        pb = (ProgressBar) footer.findViewById(R.id.pb);
        tv_footer = (TextView) footer.findViewById(R.id.tv_footer);
        footerlayout.setVisibility(View.GONE);
        expandablelistviewmes.addFooterView(footer, null, false);
        expandablelistviewmes.setGroupIndicator(null);
        expandablelistviewmes.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0, count = expandablelistviewmes
                        .getExpandableListAdapter().getGroupCount(); i < count; i++) {
                    if (groupPosition != i) {// 关闭其他分组
                        expandablelistviewmes.collapseGroup(i);
                    }
                }
            }
        });
        expandablelistviewmes.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    isLastitem = true;
                } else {
                    isLastitem = false;
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isLastitem && scrollState == SCROLL_STATE_IDLE) {
                    if (!isLoading) {
                        isLoading = true;
                        footerlayout.setVisibility(View.VISIBLE);
                        getMessage();
                    }
                }
            }
        });
        ptrInvest.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageOn = 1;
                getMessage();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame,
                                             View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, expandablelistviewmes, header);
            }
        });
        getMessage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.title_leftimageview)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
        }
    }

    private int pageOn = 1;

    // 获取消息列表
    private void getMessage() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.GETMESSAGE).addParams("uid", uid)
                .addParams("type", "0")
                .addParams("pageOn", pageOn + "").addParams("pageSize", "10")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        ptrInvest.refreshComplete();
                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray arr = map.getJSONObject("page")
                                    .getJSONArray("rows");
                            if (arr.size() > 0) {
                                loadComplete();
                            } else {
                                tv_footer.setText("全部加载完毕");
                                footerlayout.setVisibility(View.VISIBLE);
                                pb.setVisibility(View.GONE);
                            }
                            if (pageOn == 1) {
                                rows_List.clear();
                            }
                            if (arr.size() <= 0) {
                                if (pageOn == 1) {
                                    rlNoNomessage.setVisibility(View.VISIBLE);
                                    expandablelistviewmes.setVisibility(View.GONE);
                                }
                            } else {
                                mrows_List.clear();
                                rlNoNomessage.setVisibility(View.GONE);
                                expandablelistviewmes.setVisibility(View.VISIBLE);
                                mrows_List = JSON.parseArray(arr.toJSONString(),
                                        Rows_Message.class);
                                rows_List.addAll(mrows_List);
                                if (adapter == null) {
                                    adapter = new MessageCenterAdapter(MessageCenterActivity.this, rows_List);
                                    expandablelistviewmes.setAdapter(adapter);
                                } else {
                                    adapter.onDateChange(rows_List);
                                }
                            }

                            pageOn++;
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
//                            new show_Dialog_IsLogin(MessageCenterActivity.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统异常");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    public void loadComplete() {
        isLoading = false;
        footerlayout.setVisibility(View.GONE);
    }

}
