package com.mcz.xhj.yz.dr_app.me;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.JifenExpandableListAdapter;
import com.mcz.xhj.yz.dr_app.BaseActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.DateRecordBean;
import com.mcz.xhj.yz.dr_bean.JifenDetailBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zhulang on 2017/9/12.
 * 2.0版 我的积分
 */

public class JifenActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.ptr_jifen)
    LinearLayout ptr_jifen;
    @BindView(R.id.tv_total_jifen)
    TextView tvTotalJifen;
    @BindView(R.id.title_jifen_detail)
    TextView titleJifenDetail;
    @BindView(R.id.img_jifen_detail)
    ImageView imgJifenDetail;
    @BindView(R.id.ll_jifen_detail)
    LinearLayout llJifenDetail;
    @BindView(R.id.title_jifen_exchange)
    TextView titleJifenExchange;
    @BindView(R.id.img_jifen_exchange)
    ImageView imgJifenExchange;
    @BindView(R.id.ll_jifen_exchange)
    LinearLayout llJifenExchange;
    @BindView(R.id.expand_list)
    ExpandableListView expandList;
    @BindView(R.id.ll_norecord)
    LinearLayout ll_norecord;
    @BindView(R.id.rl_record)
    RelativeLayout rl_record;

    private int flag = 0;//0=积分明细，1=兑换记录（暂无）
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private List<DateRecordBean> groupTitle;
    private Map<Integer, List<JifenDetailBean>> childMap;
    private JifenExpandableListAdapter myAdapter;
    private List<JifenDetailBean> jifenDetailsList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_jifen;
    }

    @Override
    protected void initParams() {
        //getData(1,flag);
        getData();
        /*ptr_jifen.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, expandList, header);
            }
        });*/
    }

    @OnClick({R.id.title_leftimageview, R.id.ll_jifen_detail, R.id.ll_jifen_exchange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.ll_jifen_detail:
                if (flag == 0) {
                    return;
                }
                expandList.setVisibility(View.VISIBLE);
                flag = 0;

                titleJifenDetail.setTextColor(getResources().getColor(R.color.red));
                imgJifenDetail.setVisibility(View.VISIBLE);
                titleJifenExchange.setTextColor(0XFFC9C4C3);
                imgJifenExchange.setVisibility(View.GONE);
                //getData(1,flag);
                getData();
                break;
            case R.id.ll_jifen_exchange:
                if (flag == 1) {
                    return;
                }
                expandList.setVisibility(View.GONE);
                flag = 1;
                titleJifenDetail.setTextColor(0XFFC9C4C3);
                imgJifenDetail.setVisibility(View.GONE);
                titleJifenExchange.setTextColor(getResources().getColor(R.color.red));
                imgJifenExchange.setVisibility(View.VISIBLE);
                ll_norecord.setVisibility(View.VISIBLE);
                //getData(1,flag);
                break;
        }
    }

    private void getData() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post()
                .url(UrlConfig.MYPOINTSRECORD)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                //ptr_jifen.refreshComplete();
                LogUtils.i("积分记录：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    JSONArray monthList = map.getJSONArray("monthList");

                    if (groupTitle != null && groupTitle.size() > 0) {
                        groupTitle.clear();
                    }
                    if (monthList.size() > 0) {
                        ll_norecord.setVisibility(View.GONE);
                        JSONObject myPoint = map.getJSONObject("myPoint");
                        String point = myPoint.getString("point");
                        if (!TextUtils.isEmpty(point)) {
                            tvTotalJifen.setText(point);
                        }else {
                            tvTotalJifen.setText("0");
                        }
                        groupTitle = JSON.parseArray(monthList.toJSONString(), DateRecordBean.class);
                        initAdapter();
                    } else {
                        ll_norecord.setVisibility(View.VISIBLE);
                        tvTotalJifen.setText("0");
                    }
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                //ptr_jifen.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

    private void initAdapter() {
        childMap = new HashMap<>();
        for (int i = 0; i < groupTitle.size(); i++) {
            childMap.put(i, new ArrayList<JifenDetailBean>());
        }
        myAdapter = new JifenExpandableListAdapter(JifenActivity.this, groupTitle, childMap);
        expandList.setGroupIndicator(null);//这里不显示系统默认的group indicator
        expandList.setAdapter(myAdapter);
        expandList.expandGroup(0);//默认展开第一项
        getChildData(0);
        expandList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //ToastMaker.showShortToast("第"+groupPosition);
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                } else {
                    //展开时请求数据
                    //parent.expandGroup( groupPosition );
                    getChildData(groupPosition);
                }
                return false;
            }

        });
    }

    private void getChildData(final int groupPosition) {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.MYPOINTSRECORD)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("time", groupTitle.get(groupPosition).getMonths())
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                LogUtils.i(groupTitle.get(groupPosition).getMonths()+"月份详细记录：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    JSONArray monthList = map.getJSONArray("monthList");
                    if (monthList.size() > 0) {
                        jifenDetailsList = JSON.parseArray(monthList.toJSONString(), JifenDetailBean.class);
                        childMap.put(groupPosition, jifenDetailsList);
                        myAdapter.notifyDataSetChanged();
                    } else {

                    }
                }

            }


            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }
}
