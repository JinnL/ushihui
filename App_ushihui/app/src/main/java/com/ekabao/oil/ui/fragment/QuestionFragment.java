package com.ekabao.oil.ui.fragment;

import android.os.Bundle;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.MyExpandableAdapter;
import com.ekabao.oil.bean.QuestionBean;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 易卡宝  App
 *
 * @time 2018/7/24 17:31
 * Created by lj on 2018/7/24 17:31.
 */

public class QuestionFragment extends BaseFragment {


    private int type = 1;//

    public static QuestionFragment newInstance(int type) {

        Bundle args = new Bundle();
        QuestionFragment fragment = new QuestionFragment();
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

    @BindView(R.id.expandablelistviewfind)
    ExpandableListView expandablelistviewfind;

    private List<QuestionBean> questionList;
    private List<String> groupArray;
    private List<String> childArray;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question;
    }

    @Override
    protected void initParams() {
        getData();
    }

    private void getData() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.COMMONQUESTION)
                .addParams("type", type+"")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("--->其他问题: "+response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray articleList = map.getJSONArray("articleList");
                            questionList = JSON.parseArray(articleList.toJSONString(), QuestionBean.class);
                            initData(questionList);

                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("....");
                        } else {
                            ToastMaker.showShortToast("崩了");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {

                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    private void initData(List<QuestionBean> questionList) {
        if(groupArray == null){
            groupArray = new ArrayList<String>();
        }
        if(childArray == null){
            childArray = new ArrayList<String>();
        }
        for (int i = 0; i < questionList.size(); i++) {
            groupArray.add(questionList.get(i).getTitle());
            childArray.add(questionList.get(i).getContent());
        }

        expandablelistviewfind.setGroupIndicator(null);
        MyExpandableAdapter myExpandableAdapter = new MyExpandableAdapter(mContext, groupArray, childArray);
        expandablelistviewfind.setAdapter(myExpandableAdapter);
        expandablelistviewfind.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0, count = expandablelistviewfind
                        .getExpandableListAdapter().getGroupCount(); i < count; i++) {
                    if (groupPosition != i) {// 关闭其他分组
                        expandablelistviewfind.collapseGroup(i);
                    }
                }
            }
        });
    }
}
