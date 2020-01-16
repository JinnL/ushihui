package com.mcz.xhj.yz.dr_app.me;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.FundsExpandableListAdapter;
import com.mcz.xhj.yz.dr_app.BaseActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.DateRecordBean;
import com.mcz.xhj.yz.dr_bean.TransactionDetailsBean;
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
import okhttp3.Call;

/**
 * Created by DELL on 2017/11/1.
 * 描述：2.0
 */

public class FundsDetailsActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.expand_list)
    ExpandableListView expandList;
    @BindView(R.id.ll_norecord)
    LinearLayout ll_norecord;
    /*@BindView(R.id.ptr_funds_detail)
    PtrClassicFrameLayout ptr_funds_detail;*/

    //private ArrayList<String> groupTitle = new ArrayList<>();
    private Map<Integer, List<TransactionDetailsBean>> childMap;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private int tradeType = 0;
    private List<DateRecordBean> groupTitle = new ArrayList<>();
    private FundsExpandableListAdapter myAdapter;
    private List<TransactionDetailsBean> detailsList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_funds_detail;
    }

    @Override
    protected void initParams() {
        //initData();
        getData();
        titleCentertextview.setText("资金明细");
        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*ptr_funds_detail.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });*/

    }

    private void getData() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.ASSETRECORD)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("tradeType",tradeType+"")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                LogUtils.i("资金记录：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    JSONArray list = map.getJSONArray("list");

                    if (list.size() > 0) {
                        ll_norecord.setVisibility(View.GONE);
                        groupTitle = JSON.parseArray(list.toJSONString(), DateRecordBean.class);
                        initAdapter();
                    } else {
                        ll_norecord.setVisibility(View.VISIBLE);
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

    private void initAdapter() {
        childMap = new HashMap<>();
        for (int i = 0; i < groupTitle.size(); i++) {
            childMap.put(i,new ArrayList<TransactionDetailsBean>());
        }
        myAdapter = new FundsExpandableListAdapter(FundsDetailsActivity.this,groupTitle,childMap);
        expandList.setGroupIndicator(null);//这里不显示系统默认的group indicator
        expandList.setAdapter(myAdapter);
        expandList.expandGroup(0);//默认展开第一项
        getChildData(0);
        expandList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //ToastMaker.showShortToast("第"+groupPosition);
                if(parent.isGroupExpanded(groupPosition)){
                    parent.collapseGroup( groupPosition );
                }else{
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
                .url(UrlConfig.ASSETRECORD)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("tradeType",tradeType+"")
                .addParam("time",groupTitle.get(groupPosition).getMonths())
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
                    JSONArray list = map.getJSONArray("list");
                    if (list.size() > 0) {
                        detailsList = JSON.parseArray(list.toJSONString(), TransactionDetailsBean.class);
                        childMap.put(groupPosition,detailsList);
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
