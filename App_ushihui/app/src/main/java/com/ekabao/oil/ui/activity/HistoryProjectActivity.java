package com.ekabao.oil.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.HistoryProjectAdapter;
import com.ekabao.oil.bean.HistoryProjectBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 油实惠  App
 * 历史项目
 *
 * @time 2018/7/23 15:23
 * Created by lj on 2018/7/23 15:23.
 */

public class HistoryProjectActivity extends BaseActivity {
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
    @BindView(R.id.rv_project)
    RecyclerView rvProject;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private String statuses = "6,8,9";//5=募集中 6=募集成功 8=待还款 9=已还款
    private List<HistoryProjectBean> lslb2 = new ArrayList<HistoryProjectBean>();
    private HistoryProjectAdapter historyProjectAdapter;

    private String type; //1=活动标，2=优选金服

    @Override
    protected int getLayoutId() {
        return R.layout.activity_project_history;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("历史项目");
        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        historyProjectAdapter = new HistoryProjectAdapter(rvProject, lslb2, R.layout.item_project_history);
        rvProject.setLayoutManager(new LinearLayoutManager(this));
        rvProject.setAdapter(historyProjectAdapter);

        //添加自定义分割线 感觉没效果啊

        /*DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.color.text_red));
        rvProject.addItemDecoration(divider);*/


        getData(1, type, 0);
    }

    private void getData(int page, String type, final int flag) {

        if (flag == 0) {
            statuses = "6,8,9";
        }
        if (flag == 1) {
            statuses = "9";
        }

        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.PRODUCTLISTINFO)
                //.addParams("type", type)
                .addParams("pageSize", "20")
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("pageOn", page + "")
                .addParams("statuses", statuses)//5=募集中 6=募集成功 8=待还款 9=已还款
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {

                        LogUtils.e("LF--->历史项目：" + response);
                        // isLoading = true;
                        dismissDialog();


                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean("success")) {
                            JSONObject objmap = obj.getJSONObject("map");
                            JSONObject objpage = objmap.getJSONObject("page");

                            JSONArray arr = objpage.getJSONArray("rows");
                            List<HistoryProjectBean> investListBeans = JSON.parseArray(arr.toJSONString(), HistoryProjectBean.class);

                            lslb2.clear();
                            lslb2.addAll(investListBeans);

                            historyProjectAdapter.notifyDataSetChanged();


                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {

                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
