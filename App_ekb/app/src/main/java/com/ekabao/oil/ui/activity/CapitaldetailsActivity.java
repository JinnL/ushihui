package com.ekabao.oil.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.ekabao.oil.adapter.CapitaldetailsAdapter;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.CapitaldetailsBean;
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

/**
 * 资金明细
 *
 * @time 2018/8/6 9:55
 * Created by
 */

public class CapitaldetailsActivity extends AppCompatActivity {


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
    @BindView(R.id.rv_capital)
    RecyclerView rvCapital;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;


    private List<CapitaldetailsBean> rows_List = new ArrayList<CapitaldetailsBean>();// 每次加载的数据
    private CapitaldetailsAdapter noticeAdapter;
    private String uid;

    int pageon = 1;//当前页
    int total;  //返回总共多少个
    int totalPage; //返回总共多少页
    /**
     * @time 2018/7/13 17:45
     * Created by
     */
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capitaldetails);
        ButterKnife.bind(this);

        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");

        noticeAdapter = new CapitaldetailsAdapter(rvCapital, rows_List, R.layout.item_capital);

        rvCapital.setLayoutManager(new LinearLayoutManager(this));
        rvCapital.setAdapter(noticeAdapter);

        titleCentertextview.setText("资金明细");
        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        noticeAdapter.setonItemViewClickListener(new BaseRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onItemViewClick(BaseViewHolder view, int position) {


            }
        });
       /* noticeAdapter.setonItemViewClickListener(new BaseRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onItemViewClick(BaseViewHolder view, int position) {
                LogUtils.e("--->平台通知：onItemViewClick");

                *//*startActivity(new Intent(NoticeActivity.this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.WEBSITEAN + "?id=" + rows_List.get(position).getArtiId())
                        .putExtra("TITLE", "公告详情"));*//*
            }
        });*/


        getData();

        //refreshLayout.autoRefresh();
        //开启自动加载功能（非必须）设置是否监听列表在滚动到底部时触发加载事件
        refreshLayout.setEnableAutoLoadMore(true);
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                pageon = 1;
                // LogUtils.e("pageon+"+pageon+"totalPage"+totalPage);
                getData();
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
                    getData();
                    refreshLayout.finishLoadMore();
                    // LogUtils.e("pageon+"+pageon+"totalPage"+totalPage);
                }
            }
        });
    }

    private void getData() {
        //showWaitDialog("加载中...", true, "");

        OkHttpUtils.post()
                .url(UrlConfig.ASSETRECORD)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("tradeType", "0")
                // .addParam("time",groupTitle.get(groupPosition).getMonths())
                .addParams("pageOn", pageon + "")
                .addParams("pageSize", "10")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                //dismissDialog();
                LogUtils.i("月份详细记录：" + response);
                refreshLayout.finishRefresh();
                //LogUtils.e("--->平台通知：" + response);
                JSONObject obj = JSON.parseObject(response);

                if (obj.getBoolean(("success"))) { //
                    JSONObject map = obj.getJSONObject("map");
                    JSONArray arr = map.getJSONObject("page").getJSONArray("rows");
                    //JSONArray arr = map.getJSONArray("memberFundsRecordList");
                    JSONObject page = map.getJSONObject("page");
                    totalPage = page.getInteger("totalPage");

                    if (arr.size() > 0) {
                        //loadComplete();
                    } else {
                        // tv_footer.setText("全部加载完毕");
                        // footerlayout.setVisibility(View.VISIBLE);
                        // pb.setVisibility(View.GONE);
                    }
                    if (pageon == 1) {
                        rows_List.clear();
                    }

                    if (arr.size() <= 0) {
                        rvCapital.setVisibility(View.GONE);
                        llEmpty.setVisibility(View.VISIBLE);
                    } else {
                        rvCapital.setVisibility(View.VISIBLE);
                        llEmpty.setVisibility(View.GONE);
                        // mrows_List.clear();
                        //rlNoNomessage.setVisibility(View.GONE);
                        // lvPlatformMessage.setVisibility(View.VISIBLE);
                        List<CapitaldetailsBean> mrows_List = JSON.parseArray(arr.toJSONString(), CapitaldetailsBean.class);
                        rows_List.addAll(mrows_List);
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
                //dismissDialog();
                ToastMaker.showShortToast("请检查网络");
                refreshLayout.finishRefresh();
            }
        });
    }
}
