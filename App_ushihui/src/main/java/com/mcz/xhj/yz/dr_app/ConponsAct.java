package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.viewpagerindicator.TabPageIndicator;
import com.mcz.xhj.yz.dr_app.find.CallCenterActivity;
import com.mcz.xhj.yz.dr_app_fragment.Frag_Myconpons_offtime;
import com.mcz.xhj.yz.dr_app_fragment.Frag_Myconpons_unuse;
import com.mcz.xhj.yz.dr_app_fragment.Frag_Myconpons_used;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_view.DialogMaker;

import butterknife.BindView;


public class ConponsAct extends BaseActivity {
    private String[] tab;//标题
    // 初始化数据
    private TabFragPA tabPA;
    @BindView(R.id.vp_conpons)
    ViewPager vp_conpons;
    @BindView(R.id.more_indicator)
    TabPageIndicator tabin;
    @BindView(R.id.title_centertextview)
    TextView centertv;
    @BindView(R.id.title_righttextview)
    TextView title_righttextview;
    @BindView(R.id.tv_usedconpons)
    TextView tv_usedconpons;
    @BindView(R.id.title_leftimageview)
    ImageView leftima;
    @BindView(R.id.tv_commom_question)
    TextView tv_commom_question;
    @BindView(R.id.tv_contact_us)
    TextView tv_contact_us;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_conpons;
    }

    private Fragment frag;
    private Fragment frag1;
    private Fragment frag2;

    @Override
    protected void initParams() {
        centertv.setText("优惠券 ");
        Drawable nav_up = getResources().getDrawable(R.mipmap.wenhao_plus);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        centertv.setCompoundDrawables(null, null, nav_up, null);
        title_righttextview.setVisibility(View.VISIBLE);
        title_righttextview.setText("去投资");
        title_righttextview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalApplication.getInstance().getMainActivity().setCheckedFram(2);
                finish();
            }
        });
        leftima.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tab = new String[]{"返现红包", "加息券", "翻倍券"};
        // 给viewpager设置适配器
        tabPA = new TabFragPA(getSupportFragmentManager());//继承fragmentactivity
        vp_conpons.setAdapter(tabPA);
        // viewpagerindictor和viewpager关联
        tabin.setViewPager(vp_conpons);
        vp_conpons.setOffscreenPageLimit(2);
        tv_usedconpons.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConponsAct.this, ConponsUsed.class));
            }
        });
        centertv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConponsAct.this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.REMINDER + "?app=true")
                        .putExtra("TITLE", "优惠券温馨提示")
                        .putExtra("BANNER", "banner"));
            }
        });

        tv_commom_question.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConponsAct.this, CallCenterActivity.class));
            }
        });
        tv_contact_us.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMaker.showKufuPhoneDialog(ConponsAct.this);
            }
        });

    }

    class TabFragPA extends FragmentPagerAdapter {

        public TabFragPA(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            switch (arg0) {
                case 0://返现
                    frag = frag == null ? new Frag_Myconpons_unuse() : frag;
                    return frag;
                case 1://加息券
                    frag1 = frag1 == null ? new Frag_Myconpons_used() : frag1;
                    return frag1;
                case 2://翻倍券
                    frag2 = frag2 == null ? new Frag_Myconpons_offtime() : frag2;
                    return frag2;
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
