package com.mcz.xhj.yz.dr_app.find;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.MyExpandableAdapter;
import com.mcz.xhj.yz.dr_app.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：问题显示页
 * 创建人：shuc
 * 创建时间：2017/2/24 11:00
 * 修改人：DELL
 * 修改时间：2017/2/24 11:00
 * 修改备注：
 */
public class QuestionActivity extends BaseActivity {

    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.expandablelistviewfind)
    ExpandableListView expandablelistviewfind;

    private List<String> groupArray;
    private List<String> childArray;
    private int isWhat;

    @Override
    protected int getLayoutId() {
        return R.layout.find_question;
    }

    @Override
    protected void initParams() {
        isWhat = Integer.parseInt(getIntent().getStringExtra("isWhat"));

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
        // 展开第一组
//        expandablelistviewfind.expandGroup(0);
        groupArray = new ArrayList<String>();
        childArray = new ArrayList<String>();
        init(isWhat);
        expandablelistviewfind.setGroupIndicator(null);
        MyExpandableAdapter myExpandableAdapter = new MyExpandableAdapter(QuestionActivity.this, groupArray, childArray);
        expandablelistviewfind.setAdapter(myExpandableAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.title_leftimageview)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
        }
    }

    private void init(int what) {
        switch (what) {
            case 0:
                titleCentertextview.setText("认证注册");
                groupArray.add(getString(R.string.find_renzheng_ques_one));
                childArray.add(getString(R.string.find_renzheng_ans_one));
                groupArray.add(getString(R.string.find_renzheng_ques_two));
                childArray.add(getString(R.string.find_renzheng_ans_two));
                groupArray.add(getString(R.string.find_renzheng_ques_three));
                childArray.add(getString(R.string.find_renzheng_ans_three));

                break;
            case 1:
                titleCentertextview.setText("安全保障");
                groupArray.add(getString(R.string.find_anquan_ques_one));
                childArray.add(getString(R.string.find_anquan_ans_one));
                groupArray.add(getString(R.string.find_anquan_ques_two));
                childArray.add(getString(R.string.find_anquan_ans_two));
                groupArray.add(getString(R.string.find_anquan_ques_three));
                childArray.add(getString(R.string.find_anquan_ans_three));
                groupArray.add(getString(R.string.find_anquan_ques_four));
                childArray.add(getString(R.string.find_anquan_ans_four));
                groupArray.add(getString(R.string.find_anquan_ques_five));
                childArray.add(getString(R.string.find_anquan_ans_five));
                groupArray.add(getString(R.string.find_anquan_ques_six));
                childArray.add(getString(R.string.find_anquan_ans_six));
                break;
            case 2:
                titleCentertextview.setText("充值提现");
                groupArray.add(getString(R.string.find_chongzhi_ques_one));
                childArray.add(getString(R.string.find_chongzhi_ans_one));
                groupArray.add(getString(R.string.find_chongzhi_ques_two));
                childArray.add(getString(R.string.find_chongzhi_ans_two));
                groupArray.add(getString(R.string.find_chongzhi_ques_three));
                childArray.add(getString(R.string.find_chongzhi_ans_three));
                groupArray.add(getString(R.string.find_chongzhi_ques_four));
                childArray.add(getString(R.string.find_chongzhi_ans_four));
                groupArray.add(getString(R.string.find_chongzhi_ques_five));
                childArray.add(getString(R.string.find_chongzhi_ans_five));
                groupArray.add(getString(R.string.find_chongzhi_ques_six));
                childArray.add(getString(R.string.find_chongzhi_ans_six));
               /*groupArray.add(getString(R.string.find_chongzhi_ques_seven));
               childArray.add(getString(R.string.find_chongzhi_ans_seven));
               groupArray.add(getString(R.string.find_chongzhi_ques_eight));
               childArray.add(getString(R.string.find_chongzhi_ans_eight));
               groupArray.add(getString(R.string.find_chongzhi_ques_nine));
               childArray.add(getString(R.string.find_chongzhi_ans_nine));
               groupArray.add(getString(R.string.find_chongzhi_ques_ten));
               childArray.add(getString(R.string.find_chongzhi_ans_ten));*/
                break;
            case 3:
                titleCentertextview.setText("投资福利");
                groupArray.add(getString(R.string.find_touzi_ques_one));
                childArray.add(getString(R.string.find_touzi_ans_one));
                groupArray.add(getString(R.string.find_touzi_ques_two));
                childArray.add(getString(R.string.find_touzi_ans_two));
               /*groupArray.add(getString(R.string.find_touzi_ques_three));
               childArray.add(getString(R.string.find_touzi_ans_three));
               groupArray.add(getString(R.string.find_touzi_ques_four));
               childArray.add(getString(R.string.find_touzi_ans_four));*/
                break;
            case 4:
                titleCentertextview.setText("产品介绍");
                groupArray.add(getString(R.string.find_chanpin_ques_one));
                childArray.add(getString(R.string.find_chanpin_ans_one));
                groupArray.add(getString(R.string.find_chanpin_ques_two));
                childArray.add(getString(R.string.find_chanpin_ans_two));
                groupArray.add(getString(R.string.find_chanpin_ques_three));
                childArray.add(getString(R.string.find_chanpin_ans_three));
                groupArray.add(getString(R.string.find_chanpin_ques_four));
                childArray.add(getString(R.string.find_chanpin_ans_four));
//               groupArray.add(getString(R.string.find_chanpin_ques_seven));
//               childArray.add(getString(R.string.find_chanpin_ans_seven));
                break;
            case 5:
                titleCentertextview.setText("其他问题");
                groupArray.add(getString(R.string.find_other_ques_one));
                childArray.add(getString(R.string.find_other_ans_one));
                groupArray.add(getString(R.string.find_other_ques_two));
                childArray.add(getString(R.string.find_other_ans_two));
                groupArray.add(getString(R.string.find_other_ques_three));
                childArray.add(getString(R.string.find_other_ans_three));
               /*groupArray.add(getString(R.string.find_other_ques_four));
               childArray.add(getString(R.string.find_other_ans_four));
               groupArray.add(getString(R.string.find_other_ques_five));
               childArray.add(getString(R.string.find_other_ans_five));
               groupArray.add(getString(R.string.find_other_ques_six));
               childArray.add(getString(R.string.find_other_ans_six));
               groupArray.add(getString(R.string.find_other_ques_seven));
               childArray.add(getString(R.string.find_other_ans_seven));
               groupArray.add(getString(R.string.find_other_ques_eight));
               childArray.add(getString(R.string.find_other_ans_eight));*/
                break;

        }
    }
}
