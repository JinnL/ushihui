package com.mcz.xhj.yz.dr_app_fragment;


import android.content.SharedPreferences;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.PersonMsgExpandListAdapter;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.DateRecordBean;
import com.mcz.xhj.yz.dr_bean.Rows_Message;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;


/**
 * Created by Administrator on 2017/9/19
 * 描述：个人消息
 */

public class PersonalMessageFragment extends BaseFragment {
    @BindView(R.id.expand_list_personal)
    ExpandableListView expand_list_personal;
    @BindView(R.id.ll_norecord_personal)
    LinearLayout ll_norecord_personal;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private int type = 0;//0 全部
    private List<DateRecordBean> groupTitle = new ArrayList<>();
    private HashMap<Integer, List<Rows_Message>> childMap;
    private PersonMsgExpandListAdapter myAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_message_personal;
    }

    @Override
    protected void initParams() {
        getData();
    }

    private void getData() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.GETMESSAGE)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("type",type+"")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                LogUtils.i("个人消息记录：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    JSONArray list = map.getJSONArray("list");
                    if (list.size() > 0) {
                        ll_norecord_personal.setVisibility(View.GONE);
                        groupTitle = JSON.parseArray(list.toJSONString(), DateRecordBean.class);
                        initAdapter();
                    } else {
                        ll_norecord_personal.setVisibility(View.VISIBLE);
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
            childMap.put(i,new ArrayList<Rows_Message>());
        }
        myAdapter = new PersonMsgExpandListAdapter(mContext, groupTitle, childMap);
        expand_list_personal.setGroupIndicator(null);//这里不显示系统默认的group indicator
        expand_list_personal.setAdapter(myAdapter);
        expand_list_personal.expandGroup(0);//默认展开第一项
        getChildData(0);
        expand_list_personal.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

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
                .url(UrlConfig.GETMESSAGE)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("type",type+"")
                .addParam("time",groupTitle.get(groupPosition).getTime())
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                LogUtils.i(groupTitle.get(groupPosition).getTime()+"详细消息记录：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    JSONArray list = map.getJSONArray("list");
                    if (list.size() > 0) {
                        List<Rows_Message> messagesList = JSON.parseArray(list.toJSONString(), Rows_Message.class);
                        childMap.put(groupPosition,messagesList);
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
