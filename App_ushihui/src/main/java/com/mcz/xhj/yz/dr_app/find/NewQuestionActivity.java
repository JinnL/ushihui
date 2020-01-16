package com.mcz.xhj.yz.dr_app.find;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_app.BaseActivity;
import com.mcz.xhj.yz.dr_app_fragment.Frag_Question_AQBZ;
import com.mcz.xhj.yz.dr_app_fragment.Frag_Question_CPJS;
import com.mcz.xhj.yz.dr_app_fragment.Frag_Question_CZTX;
import com.mcz.xhj.yz.dr_app_fragment.Frag_Question_QTWT;
import com.mcz.xhj.yz.dr_app_fragment.Frag_Question_RZZC;
import com.mcz.xhj.yz.dr_app_fragment.Frag_Question_TZFL;

import butterknife.BindView;

/**
 * Created by DELL on 2017/11/1.
 */

public class NewQuestionActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.indicator_question)
    TabPageIndicator indicatorQuestion;
    @BindView(R.id.vp_question)
    ViewPager vpQuestion;

    private int isWhat;
    private String[] titles;
    private Fragment frag;
    private Fragment frag1;
    private Fragment frag2;
    private Fragment frag3;
    private Fragment frag4;
    private Fragment frag5;
    private TabFragPA tabFragPA;

    @Override
    protected int getLayoutId() {
        return R.layout.find_question_new;
    }

    @Override
    protected void initParams() {
        isWhat = Integer.parseInt(getIntent().getStringExtra("isWhat"));
        titleCentertextview.setText("帮助与反馈");
        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titles = new String[]{"认证注册", "安全保障", "充值提现", "投资福利", "产品介绍", "其他问题"};
        // 给viewpager设置适配器;继承fragmentactivity
        tabFragPA = new TabFragPA(getSupportFragmentManager());
        vpQuestion.setAdapter(tabFragPA);
        // viewpagerindictor和viewpager关联
        indicatorQuestion.setViewPager(vpQuestion);
        vpQuestion.setOffscreenPageLimit(5);
        if(isWhat == 0){
            vpQuestion.setCurrentItem(0);
        }else if(isWhat == 1){
            vpQuestion.setCurrentItem(1);
        }else if(isWhat == 2){
            vpQuestion.setCurrentItem(2);
        }else if(isWhat == 3){
            vpQuestion.setCurrentItem(3);
        }else if(isWhat == 4){
            vpQuestion.setCurrentItem(4);
        }else if(isWhat == 5){
            vpQuestion.setCurrentItem(5);
        }

    }

    class TabFragPA extends FragmentPagerAdapter {

        public TabFragPA(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            switch (arg0) {
                case 0://认证注册
                    frag = frag == null ? new Frag_Question_RZZC() : frag;
                    return frag;
                case 1://安全保障
                    frag1 = frag1 == null ? new Frag_Question_AQBZ() : frag1;
                    return frag1;
                case 2://充值提现
                    frag2 = frag2 == null ? new Frag_Question_CZTX() : frag2;
                    return frag2;
                case 3://投资福利
                    frag3 = frag3 == null ? new Frag_Question_TZFL() : frag3;
                    return frag3;
                case 4://产品介绍
                    frag4 = frag4 == null ? new Frag_Question_CPJS() : frag4;
                    return frag4;
                case 5://其他问题
                    frag5 = frag5 == null ? new Frag_Question_QTWT() : frag5;
                    return frag5;
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position % titles.length];
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return titles.length;
        }

    }

}
