package com.ekabao.oil.ui.activity.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.MyExpandableAdapter;
import com.ekabao.oil.bean.QuestionBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 项目名称：trunk
 * 类描述：帮助与反馈
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


    @BindView(R.id.expandablelistview)
    ExpandableListView expandablelistview;
    @BindView(R.id.ll_norecord)
    LinearLayout ll_norecord;
    @BindView(R.id.tv_oil_card_recharge)
    TextView tvOilCardRecharge;
    @BindView(R.id.tv_phone_recharge)
    TextView tvPhoneRecharge;
    @BindView(R.id.tv_mall)
    TextView tvMall;
    @BindView(R.id.tv_sale)
    TextView tvSale;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_other)
    TextView tvOther;


    private List<QuestionBean> questionList;
    private SharedPreferences preferences;
    private List<String> groupArray;
    private List<String> childArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call_center;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("问题解答");
        title_leftimageview.setOnClickListener(this);

        preferences = LocalApplication.getInstance().sharereferences;
        getData();
    }

    private void getData() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.COMMONQUESTION)
                .addParams("type", 20 + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {

                        LogUtils.i("--->热门问题: " + response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray articleList = map.getJSONArray("articleList");
                            questionList = JSON.parseArray(articleList.toJSONString(), QuestionBean.class);
                            if (questionList.size() > 0) {
                                initData(questionList);
                                ll_norecord.setVisibility(View.GONE);
                            } else {
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

                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    private void initData(List<QuestionBean> questionList) {
        if (groupArray == null) {
            groupArray = new ArrayList<String>();
        }
        if (childArray == null) {
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

    @OnClick({R.id.tv_oil_card_recharge, R.id.tv_phone_recharge, R.id.tv_mall, R.id.tv_sale, R.id.tv_account, R.id.tv_other})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.tv_oil_card_recharge:
                startActivity(new Intent(CallCenterActivity.this, NewQuestionActivity.class).putExtra("isWhat", "0"));
                break;
            case R.id.tv_phone_recharge:
                startActivity(new Intent(CallCenterActivity.this, NewQuestionActivity.class).putExtra("isWhat", "1"));
                break;
            case R.id.tv_mall:
                startActivity(new Intent(CallCenterActivity.this, NewQuestionActivity.class).putExtra("isWhat", "2"));
                break;
            case R.id.tv_sale:
                startActivity(new Intent(CallCenterActivity.this, NewQuestionActivity.class).putExtra("isWhat", "3"));
                break;
            case R.id.tv_account:
                startActivity(new Intent(CallCenterActivity.this, NewQuestionActivity.class).putExtra("isWhat", "4"));
                break;
            case R.id.tv_other:
                startActivity(new Intent(CallCenterActivity.this, NewQuestionActivity.class).putExtra("isWhat", "5"));
                break;
        }
    }
}
