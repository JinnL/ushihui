package com.mcz.xhj.yz.dr_app_fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.yz.dr_adapter.PlatformMessageAdapter;
import com.mcz.xhj.yz.dr_app.WebViewActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.Add_Bean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.mcz.xhj.R;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/9/19
 * 描述：平台公告
 */

public class PlatformMessageFragment extends BaseFragment {
    @BindView(R.id.lv_platform_message)
    ListView lvPlatformMessage;
    @BindView(R.id.no_nomessage)
    TextView noNomessage;
    @BindView(R.id.rl_no_nomessage)
    RelativeLayout rlNoNomessage;

    private String uid;
    private SharedPreferences preferences;
    private boolean isLastitem = false;
    private View footer;
    private LinearLayout footerlayout;
    private TextView tv_footer;
    private ProgressBar pb;
    private boolean isLoading;
    private List<Add_Bean> rows_List = new ArrayList<Add_Bean>();// 每次加载的数据
    private List<Add_Bean> mrows_List = new ArrayList<Add_Bean>();//  每次加载的数据
    private PlatformMessageAdapter personalMessageAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_message_platform;
    }

    @Override
    protected void initParams() {
        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");
        footer = View.inflate(mContext, R.layout.footer_layout, null);
        footerlayout = (LinearLayout) footer.findViewById(R.id.load_layout);
        pb = (ProgressBar) footer.findViewById(R.id.pb);
        tv_footer = (TextView) footer.findViewById(R.id.tv_footer);
        footerlayout.setVisibility(View.GONE);
        lvPlatformMessage.addFooterView(footer, null, false);
        lvPlatformMessage.setOnScrollListener(new AbsListView.OnScrollListener() {

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
                        getNotice();
                    }
                }
            }
        });

        lvPlatformMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.WEBSITEAN + "?app=true&id=" + rows_List.get(position).getArtiId())
                        .putExtra("TITLE", "平台公告"));
            }
        });
        getNotice();
    }

    private int pageOn = 1;

    // 获取通知列表
    private void getNotice() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.NOTICE)
                .addParams("uid", uid)
                .addParams("type", "0")
                .addParams("pageOn", pageOn + "")
                .addParams("pageSize", "10")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        LogUtils.i("--->平台通知："+response);
                        dismissDialog();
                        //ptrInvest.refreshComplete();
                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray arr = map.getJSONObject("page").getJSONArray("rows");;
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
                                    lvPlatformMessage.setVisibility(View.GONE);
                                }
                            } else {
                                mrows_List.clear();
                                rlNoNomessage.setVisibility(View.GONE);
                                lvPlatformMessage.setVisibility(View.VISIBLE);
                                mrows_List = JSON.parseArray(arr.toJSONString(), Add_Bean.class);
                                rows_List.addAll(mrows_List);
                                if (personalMessageAdapter == null) {
                                    personalMessageAdapter = new PlatformMessageAdapter(mContext, rows_List);
                                    lvPlatformMessage.setAdapter(personalMessageAdapter);
                                } else {
                                    personalMessageAdapter.onDateChange(rows_List);
                                }
                            }

                            pageOn++;
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
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
