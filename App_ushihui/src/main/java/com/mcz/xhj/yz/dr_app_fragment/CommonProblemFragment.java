package com.mcz.xhj.yz.dr_app_fragment;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.CommonQuestionAdapter;
import com.mcz.xhj.yz.dr_bean.QuestionBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bleu.widget.slidedetails.SlideDetailsLayout;
import okhttp3.Call;

/**
 * Created by zhulang on 2017/8/9.
 * 描述：2.0版 标详情页-常见问题
 */

public class CommonProblemFragment extends BaseFragment {
    @BindView(R.id.lv_common_question)
    ListView lvCommonQuestion;

    private List<QuestionBean> list;
    private String pid;
    private String type = "20";
    private List<QuestionBean> questionList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_common_problem;
    }

    private SlideDetailsLayout mSlideDetailsLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSlideDetailsLayout = (SlideDetailsLayout) activity.findViewById(R.id.slidedetails);
    }

    @Override
    protected void initParams() {
        //initData();
        getCommonData();

        lvCommonQuestion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(lvCommonQuestion.getFirstVisiblePosition()==0){
                    mSlideDetailsLayout.smoothOpen(false);
//					mSlideDetailsLayout.isMove = false;
//					mSlideDetailsLayout.setIsMove(false);
                }else{
                    mSlideDetailsLayout.smoothOpen(true);
//					mSlideDetailsLayout.isMove = true;
//					mSlideDetailsLayout.setIsMove(true);
                }
                return false;
            }
        });
    }

    private void getCommonData() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.COMMONQUESTION)
                .addParams("type", type)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        LogUtils.i("--->常见问题: "+response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray articleList = map.getJSONArray("articleList");
                            questionList = JSON.parseArray(articleList.toJSONString(), QuestionBean.class);
                            lvCommonQuestion.setAdapter(new CommonQuestionAdapter(mContext,questionList));

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

    private void initData() {
        list = new ArrayList<QuestionBean>();
        for (int i = 0; i < 3; i++) {
            QuestionBean questionBean = new QuestionBean();
            questionBean.setTitle("什么时候开始计算收益？");
            questionBean.setContent("购买标的后，次日即可开始计息。购买标的后，次日即可开始计息");
            list.add(questionBean);
        }
    }

}
