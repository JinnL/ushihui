package com.mcz.xhj.yz.dr_app.find;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.mcz.xhj.yz.dr_adapter.MyExpandableAdapter;
import com.mcz.xhj.yz.dr_app.BaseActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.QuestionBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 项目名称：trunk
 * 类描述：客服中心
 * 创建人：shuc
 * 创建时间：2017/2/16 14:17
 * 修改人：DELL
 * 修改时间：2017/2/16 14:17
 * 修改备注：
 */
public class CallCenterActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.title_lefttextview)
    TextView titleLefttextview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.title_leftimageview)
    ImageView title_leftimageview;

    @BindView(R.id.ll_renzheng)
    RelativeLayout llCunguan;
    @BindView(R.id.ll_anquan)
    RelativeLayout llAnquan;
    @BindView(R.id.ll_chongzhi)
    RelativeLayout llChongzhi;
    @BindView(R.id.ll_touzi)
    RelativeLayout llTouzi;
    @BindView(R.id.ll_chanpin)
    RelativeLayout llChanpin;
    @BindView(R.id.ll_qita)
    RelativeLayout llQita;
    @BindView(R.id.expandablelistview)
    ExpandableListView expandablelistview;
    @BindView(R.id.ll_norecord)
    LinearLayout ll_norecord;


    private List<QuestionBean> questionList;
    private SharedPreferences preferences;
    private List<String> groupArray;
    private List<String> childArray;

    @Override
    protected int getLayoutId() {
        return R.layout.call_center;
    }

    @Override
    protected void initParams() {
        rl_title.setBackgroundResource(R.color.colorPrimary);
        titleCentertextview.setText("帮助与反馈");
        title_leftimageview.setOnClickListener(this);

        preferences = LocalApplication.getInstance().sharereferences;
        getData();

    }

    private void getData() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.COMMONQUESTION)
                .addParams("type", 20+"")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        LogUtils.i("--->热门问题: "+response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray articleList = map.getJSONArray("articleList");
                            questionList = JSON.parseArray(articleList.toJSONString(), QuestionBean.class);
                            if(questionList.size() >0){
                                initData(questionList);
                                ll_norecord.setVisibility(View.GONE);
                            }else{
                                ll_norecord.setVisibility(View.VISIBLE);
                            }

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

        expandablelistview.setGroupIndicator(null);
        MyExpandableAdapter myExpandableAdapter = new MyExpandableAdapter(CallCenterActivity.this, groupArray, childArray);
        expandablelistview.setAdapter(myExpandableAdapter);
        expandablelistview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0, count = expandablelistview
                        .getExpandableListAdapter().getGroupCount(); i < count; i++) {
                    if (groupPosition != i) {// 关闭其他分组
                        expandablelistview.collapseGroup(i);
                    }
                }
            }
        });
    }


    @OnClick({ R.id.ll_renzheng, R.id.ll_anquan, R.id.ll_chongzhi, R.id.ll_touzi, R.id.ll_chanpin, R.id.ll_qita})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.ll_renzheng:
                startActivity(new Intent(CallCenterActivity.this,NewQuestionActivity.class).putExtra("isWhat","0"));
                break;
            case R.id.ll_anquan:
                startActivity(new Intent(CallCenterActivity.this,NewQuestionActivity.class).putExtra("isWhat","1"));
                break;
            case R.id.ll_chongzhi:
                startActivity(new Intent(CallCenterActivity.this,NewQuestionActivity.class).putExtra("isWhat","2"));
                break;
            case R.id.ll_touzi:
                startActivity(new Intent(CallCenterActivity.this,NewQuestionActivity.class).putExtra("isWhat","3"));
                break;
            case R.id.ll_chanpin:
                startActivity(new Intent(CallCenterActivity.this,NewQuestionActivity.class).putExtra("isWhat","4"));
                break;
            case R.id.ll_qita:
                startActivity(new Intent(CallCenterActivity.this,NewQuestionActivity.class).putExtra("isWhat","5"));
                break;
        }
    }
}
