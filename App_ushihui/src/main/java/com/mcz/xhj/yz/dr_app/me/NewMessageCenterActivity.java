package com.mcz.xhj.yz.dr_app.me;

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
import com.mcz.xhj.yz.dr_app_fragment.PersonalMessageFragment;
import com.mcz.xhj.yz.dr_app_fragment.PlatformMessageFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/19.
 * 描述：个人消息中心2.0
 */

public class NewMessageCenterActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView Leftimage;
    @BindView(R.id.title_centertextview)
    TextView title;
    @BindView(R.id.message_indicator)
    TabPageIndicator messageIndicator;
    @BindView(R.id.message_viewpager)
    ViewPager messageViewpager;

    private String[] tab;//标题
    private TabFragPA tabPA;
    private Fragment frag;
    private Fragment frag1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg_center_new;
    }

    @Override
    protected void initParams() {
        title.setText("消息");
        Leftimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tab = new String[] { "平台公告", "个人消息"};
        // 给viewpager设置适配器
        tabPA = new TabFragPA(getSupportFragmentManager());//继承fragmentactivity
        messageViewpager.setAdapter(tabPA);
        //viewpagerindictor和viewpager关联
        messageIndicator.setViewPager(messageViewpager);
        messageViewpager.setOffscreenPageLimit(1);
    }

    class TabFragPA extends FragmentPagerAdapter {

        public TabFragPA(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            switch (arg0) {
                case 0://平台信息
                    frag = frag==null?new PlatformMessageFragment():frag;
                    //frag.setArguments(bundle);
                    return frag;
                case 1://个人记录
                    frag1 = frag1==null?new PersonalMessageFragment():frag1;
                    //frag1.setArguments(bundle);
                    return frag1;
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tab[position % tab.length];
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return tab.length;
        }

    }

}
