package com.ekabao.oil.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.BaseRecyclerViewAdapter;
import com.ekabao.oil.adapter.NewsAdapter;
import com.ekabao.oil.adapter.NoticeAdapter;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.MediaBean;
import com.ekabao.oil.bean.NewsBean;
import com.ekabao.oil.bean.NoticeBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;


public class NoticeActivity extends BaseActivity {


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
    @BindView(R.id.rv_notice)
    RecyclerView rvNotice;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;

    private String uid;
    private SharedPreferences preferences;
    int pageon = 1;//当前页
    int total;  //返回总共多少个
    int totalPage; //返回总共多少页
    private List<NoticeBean> rows_List = new ArrayList<NoticeBean>();// 每次加载的数据

    private BaseRecyclerViewAdapter noticeAdapter;

    private int type = 1;//
    private int activity = 1;//从那个点击进来的    1.系统消息 2 平台公告 3.媒体报道',
    private List<NewsBean> newsList = new ArrayList<NewsBean>();// 1.系统消息
    private List<MediaBean> mediaList = new ArrayList<MediaBean>();//2 平台公告 3.媒体报道',

    /**
     * 平台公告
     *
     * @time 2018/7/12 16:46
     * Created by
     */

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
    }*/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice;
    }

    @Override
    protected void initParams() {
        Intent intent = getIntent();
        activity = intent.getIntExtra("activity", 0);

        // 1.系统消息 2 平台公告 3.媒体报道',
        switch (activity) {
            case 1:
                titleCentertextview.setText("系统通知");
                noticeAdapter = new NewsAdapter(rvNotice, newsList, R.layout.item_news);

                break;
            case 2:
                titleCentertextview.setText("平台公告");
                noticeAdapter = new NoticeAdapter(rvNotice, mediaList, R.layout.item_notice);
                break;
            case 3:
                titleCentertextview.setText("媒体报道");
                noticeAdapter = new NoticeAdapter(rvNotice, mediaList, R.layout.item_media);
                break;
            case 4:
                titleCentertextview.setText("行业资讯");
                noticeAdapter = new NoticeAdapter(rvNotice, mediaList, R.layout.item_media);
                break;
            default:
                titleCentertextview.setText("消息");

                break;
        }


        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");

        //item_news    item_notice
        // noticeAdapter = new NoticeAdapter(rvNotice, rows_List, R.layout.item_notice);

        rvNotice.setLayoutManager(new LinearLayoutManager(this));
        rvNotice.setAdapter(noticeAdapter);

        noticeAdapter.setonItemViewClickListener(new BaseRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onItemViewClick(BaseViewHolder view, int position) {
                LogUtils.e("--->平台通知：onItemViewClick" + position);
                switch (activity) {
                    case 1:
                        break;
                    case 2:
                        startActivity(new Intent(NoticeActivity.this, WebViewActivity.class)
                                .putExtra("URL", UrlConfig.WEBSITEAN + "?id=" + mediaList.get(position).getArtiId() + "&app=true")
                                .putExtra("TITLE", "平台公告"));
                        break;
                    case 3:
                        startActivity(new Intent(NoticeActivity.this, WebViewActivity.class)
                                .putExtra("URL", UrlConfig.WEBSITEAN + "?id=" + mediaList.get(position).getArtiId() + "&app=true")
                                .putExtra("TITLE", "媒体报道"));
                        break;
                    case 4:
                        startActivity(new Intent(NoticeActivity.this, WebViewActivity.class)
                                .putExtra("URL", UrlConfig.WEBSITEAN + "?id=" + mediaList.get(position).getArtiId() + "&app=true")
                                .putExtra("TITLE", "行业资讯"));
                        break;

                    default:
                        //titleCentertextview.setText("消息");
                        break;
                }
            }
        });

        getNotice();

        //refreshLayout.autoRefresh();
        //开启自动加载功能（非必须）设置是否监听列表在滚动到底部时触发加载事件
        refreshLayout.setEnableAutoLoadMore(true);
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                pageon = 1;
                LogUtils.e("pageon+" + pageon + "totalPage" + totalPage);
                getNotice();
                //是否有更多数据
                refreshLayout.setNoMoreData(false);
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {

                if (pageon >= totalPage) {

                    // refreshLayout.finishLoadMore();
                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件

                } else {
                    //getInitData();
                    getNotice();
                    refreshLayout.finishLoadMore();
                    LogUtils.e("pageon+" + pageon + "totalPage" + totalPage);
                }
            }
        });


    }

    // 获取通知列表
    private void getNotice() {

        //1.系统消息 2 平台公告/3  3.媒体报道'/22,

        String notice = UrlConfig.NOTICE;
        int type = 1;
        switch (activity) {
            case 1:
                notice = UrlConfig.GETMESSAGE;
                break;
            case 2:
                notice = UrlConfig.WEB_AN;
                type = 14;
                break;
            case 3:
                notice = UrlConfig.WEB_AN;
                type = 22;
                break;
            case 4:
                notice = UrlConfig.WEB_AN;
                type = 18;
                break;

            default:

                break;
        }
        showWaitDialog("加载中...", true, "");

        OkHttpUtils.post().url(notice)
                .addParams("uid", uid)
                .addParams("type", type + "")
                .addParams("proId", type + "")
                .addParams("pageOn", pageon + "")
                .addParams("pageSize", "10")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {

                        refreshLayout.finishRefresh();
                        LogUtils.e("--->平台通知：" + response);
                        dismissDialog();
                        //ptrInvest.refreshComplete();
                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray arr = map.getJSONObject("page").getJSONArray("rows");

                            JSONObject page = map.getJSONObject("page");
                            totalPage = page.getInteger("totalPage");

                            if (pageon == 1) {
                                newsList.clear();
                                mediaList.clear();
                            }

                            if (arr.size() <= 0) {

                                rvNotice.setVisibility(View.GONE);
                                llEmpty.setVisibility(View.VISIBLE);
                            } else {
                                // mrows_List.clear();
                                rvNotice.setVisibility(View.VISIBLE);
                                llEmpty.setVisibility(View.GONE);

                                switch (activity) {
                                    case 1:
                                        List<NewsBean> mrows_List = JSON.parseArray(arr.toJSONString(), NewsBean.class);
                                        newsList.addAll(mrows_List);
                                        break;
                                    case 2:
                                    case 3:
                                    case 4:
                                        List<MediaBean> mrowsList = JSON.parseArray(arr.toJSONString(), MediaBean.class);
                                        mediaList.addAll(mrowsList);
                                        break;
                                    default:

                                        break;
                                }


                            }

                            noticeAdapter.notifyDataSetChanged();
                            pageon++;

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
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                        refreshLayout.finishRefresh();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
