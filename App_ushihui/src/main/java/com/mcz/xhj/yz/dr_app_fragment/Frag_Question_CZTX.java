package com.mcz.xhj.yz.dr_app_fragment;

import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.MyExpandableAdapter;
import com.mcz.xhj.yz.dr_bean.QuestionBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by DELL on 2017/11/1.
 */

public class Frag_Question_CZTX extends BaseFragment {
    @BindView(R.id.expandablelistviewfind)
    ExpandableListView expandablelistviewfind;

    private List<QuestionBean> questionList;
    private List<String> groupArray;
    private List<String> childArray;
    @Override
    protected int getLayoutId() {
        return R.layout.frag_question_cztx;
    }

    @Override
    protected void initParams() {
        getData();
    }

    private void getData() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.COMMONQUESTION)
                .addParams("type", 11+"")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        LogUtils.i("--->充值提现问题: "+response);
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
                        // TODO Auto-generated method stub
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
