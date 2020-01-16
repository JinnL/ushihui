package com.ekabao.oil.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.NewsAdapter;
import com.ekabao.oil.bean.NewsBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * $desc$
 *
 * @time $data$ $time$
 * Created by Administrator on 2018/7/13.
 */

public class NewsFragment extends BaseFragment {


    @BindView(R.id.rv_news)
    RecyclerView rvNews;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;

    private int type = 1;//
    private SharedPreferences preferences;
    private String uid;
    private List<NewsBean> rows_List = new ArrayList<NewsBean>();// 每次加载的数据
    private NewsAdapter newsAdapter;

    int pageon = 1;//当前页
    int total;  //返回总共多少个
    int totalPage; //返回总共多少页

    public static NewsFragment newInstance(int type) {

        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            type = args.getInt("type");
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initParams() {
        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");
        pageon = 1;
        //getData();
        //LogUtils.e("个人消息记录："+type);
        getChildData();

        newsAdapter = new NewsAdapter(rvNews, rows_List, R.layout.item_news);
        rvNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNews.setAdapter(newsAdapter);

       /* newsAdapter.setonItemViewClickListener(new BaseRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onItemViewClick(BaseViewHolder view, int position) {
                LogUtils.e("--->平台通知：onItemViewClick");
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL", UrlConfig.WEBSITEAN + "?id="
                                + rows_List.get(position).getId())
                        .putExtra("TITLE", "个人消息"));
            }
        });*/

        //refreshLayout.autoRefresh();
        //开启自动加载功能（非必须）设置是否监听列表在滚动到底部时触发加载事件
        refreshLayout.setEnableAutoLoadMore(true);
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                pageon = 1;
                LogUtils.e("pageon+" + pageon + "totalPage" + totalPage);
                getChildData();
                //是否有更多数据
                refreshLayout.setNoMoreData(false);
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {

                if (pageon > totalPage) {

                    // refreshLayout.finishLoadMore();
                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件

                } else {
                    //getInitData();
                    getChildData();
                    refreshLayout.finishLoadMore();
                    LogUtils.e("pageon+" + pageon + "totalPage" + totalPage);
                }
            }
        });
    }

    private void getData() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.GETMESSAGE)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("type", type + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                LogUtils.e("个人消息记录：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    JSONArray list = map.getJSONArray("list");
                   /* if (list.size() > 0) {
                        ll_norecord_personal.setVisibility(View.GONE);
                        groupTitle = JSON.parseArray(list.toJSONString(), DateRecordBean.class);
                        initAdapter();
                    } else {
                        ll_norecord_personal.setVisibility(View.VISIBLE);
                    }*/
                }

            }


            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

    private void getChildData() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.GETMESSAGE)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("type", type + "")
                .addParams("pageOn", pageon + "")
                .addParams("pageSize", "10")
                // .addParam("time","2018-07")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();

                if (refreshLayout != null) {
                    RefreshState state = refreshLayout.getState();
                    if (state == RefreshState.Refreshing) {
                        refreshLayout.finishRefresh();
                    }
                }
                LogUtils.e("详细消息记录：" +  preferences.getString("uid", "")+
                "pageon"+pageon);
                LogUtils.i("详细消息记录////：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    //JSONArray rows = map.getJSONArray("rows");
                    JSONObject page = map.getJSONObject("page");
                    JSONArray rows = page.getJSONArray("rows");
                    totalPage = page.getInteger("totalPage");
                    if (pageon == 1) {
                        rows_List.clear();
                    }
                    if (rows.size() <= 0) {

                        rvNews.setVisibility(View.GONE);
                        llEmpty.setVisibility(View.VISIBLE);


                    } else {
                        // mrows_List.clear();
                        rvNews.setVisibility(View.VISIBLE);
                        llEmpty.setVisibility(View.GONE);

                        // NewsBean


                        List<NewsBean> mrows_List = JSON.parseArray(rows.toJSONString(), NewsBean.class);
                        //LogUtils.e("详细消息记录：" + mrows_List.size());
                       // rows_List.clear();
                        rows_List.addAll(mrows_List);


                    }
                    newsAdapter.notifyDataSetChanged();
                    pageon++;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // pageon=1;

        //unbinder.unbind();
    }
}
