package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;

import butterknife.BindView;

/**
 * 项目名称：js_app
 * 类描述：体验标投资成功
 * 创建人：shuc
 * 创建时间：2017/1/4 10:05
 * 修改人：DELL
 * 修改时间：2017/1/4 10:05
 * 修改备注：
 */
public class Act_Experience_End  extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_centertextview)
    TextView title_centertextview;
    @BindView(R.id.title_righttextview)
    TextView title_righttextview;
    @BindView(R.id.title_rightimageview)
    ImageView title_rightimageview;
    @BindView(R.id.title_leftimageview)
    ImageView title_leftimageview;

    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_day)
    TextView tv_day;
    @BindView(R.id.tv_rate)
    TextView tv_rate;
    @BindView(R.id.tv_earn)
    TextView tv_earn;
    @BindView(R.id.tv_shuoming)
    TextView tv_shuoming;
    @BindView(R.id.btn_Immediately_realized)
    Button btn_Immediately_realized;

    private Double redTotal;
    private String realverify;  //实名绑卡 1:已绑卡 0:未绑卡

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_experience_end;
    }

    @Override
    protected void initParams() {
        title_centertextview.setText("投资成功") ;
        title_leftimageview.setVisibility(View.GONE) ;
        title_leftimageview.setOnClickListener(this) ;
        btn_Immediately_realized.setOnClickListener(this) ;

        tv_money.setText(getIntent().getStringExtra("tv_money")+"元") ;
        tv_day.setText(getIntent().getStringExtra("tv_day")+"天") ;
        tv_rate.setText(getIntent().getStringExtra("tv_rate")+"%") ;
        tv_earn.setText(getIntent().getStringExtra("tv_earn")+"元") ;

        realverify = getIntent().getStringExtra("realverify");

        if("1".equalsIgnoreCase(realverify)){
            redTotal = Double.parseDouble(getIntent().getStringExtra("redTotal"));
            tv_shuoming.setText("您有"+redTotal.intValue()+"个红包未使用，激活即可变现哦！");
            btn_Immediately_realized.setText("立即变现");
            if(redTotal<1){
                tv_shuoming.setVisibility(View.GONE);
                btn_Immediately_realized.setVisibility(View.GONE);
            }
        }else {
            String tiyan_shuom = getResources().getString(R.string.tiyan_weibangka);
            tv_shuoming.setText(Html.fromHtml(tiyan_shuom));
            btn_Immediately_realized.setText("立即领取");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Immediately_realized:
                if("0".equalsIgnoreCase(realverify)){  //没有绑卡
                    startActivity(new Intent(Act_Experience_End.this, FourPartAct.class));
                    finish();
                }else if(("1".equalsIgnoreCase(realverify))){    //已经绑卡
                    startActivity(new Intent(Act_Experience_End.this, ConponsAct.class));
                    finish() ;
                }
                break;
            case R.id.title_leftimageview:
                finish() ;
                break;
            default:
                break;
        }
    }
}
