package com.ekabao.oil.ui.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.util.StringCut;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashResultActivity extends BaseActivity {
    @BindView(R.id.title_lefttextview)
    TextView titleLefttextview;
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_centerimageview)
    ImageView titleCenterimageview;
    @BindView(R.id.title_righttextview)
    TextView titleRighttextview;
    @BindView(R.id.title_rightimageview)
    ImageView titleRightimageview;
    @BindView(R.id.view_line_bottom)
    View viewLineBottom;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_state)
    ImageView ivState;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_call_phone)
    TextView tvCallPhone;
    @BindView(R.id.tv_ok)
    TextView tvOk;

    private String flag;
    private String money;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash_result;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("结果详情");
        titleLeftimageview.setVisibility(View.GONE);
        tvCallPhone.setText(Html.fromHtml("请稍后重新充值或:<font color='#1E9DFF'>联系客服</font>"));

        Intent intent = getIntent();

        flag = intent.getStringExtra("flag");

        money = intent.getStringExtra("money");

        String numKb = StringCut.getNumKbs(Double.parseDouble(money));
        tvMoney.setText(numKb);
/*LogUtils.e("10000/"+StringCut.getNumKb(10000)+"/"
                +StringCut.getDoubleKb(10000)+"/"+StringCut.getNumKbs(10000));*/
        if (flag.equalsIgnoreCase("cashin-success")) {


            tvState.setText("充值成功");
            ivState.setImageResource(R.drawable.icon_pay_success);

        } else if (flag.equalsIgnoreCase("cashout-success")) {

            tvState.setText("提现成功");
            ivState.setImageResource(R.drawable.icon_pay_success);


        } else {
            String msgCode = intent.getStringExtra("msgCode");

            String msg = intent.getStringExtra("msg");

            if (flag.equalsIgnoreCase("failiure_cashin")) {
                tvState.setText("充值失败");
                ivState.setImageResource(R.drawable.icon_pay_fail);
                tvCallPhone.setVisibility(View.VISIBLE);

            } else if (flag.equalsIgnoreCase("failiure_cashout")) {
                tvState.setText("提现失败");
                ivState.setImageResource(R.drawable.icon_pay_fail);


            } else if (flag.equalsIgnoreCase("failiure_fourpart")) {
                tvState.setText("认证失败");
                ivState.setImageResource(R.drawable.icon_pay_fail);


            } else if (flag.equalsIgnoreCase("cashin-progress")) {

                tvState.setText("充值处理中");
                ivState.setImageResource(R.drawable.icon_pay_wait);


            } else if (flag.equalsIgnoreCase("cashout-progress")) {
                /*long withdrawalsTime = intent.getLongExtra("withdrawalsTime", 0L);
                if (withdrawalsTime <= 0) {
                    tv_timeone.setVisibility(View.GONE);
                } else {
                    tv_timeone.setVisibility(View.VISIBLE);
                    tv_timeone.setText(StringCut.getDateTimeToStringheng(withdrawalsTime));
                }
                tvState.setText("提现处理中");
                ll_cashin_end_success.setVisibility(View.VISIBLE);
                tvMoney.setText("提现" + money + "元");
                tv_title.setText("恭喜你，\n提现处理中！");
                tv_time_titletwo.setText("平台正在审核中");
                line.setBackgroundColor(0xFF999999);
                tv_three.setBackgroundResource(R.drawable.bg_corner_gray_yuan);
                tv_titlethree.setText("提现成功");
                tv_goinvest.setVisibility(View.GONE);*/
            }
        }

        tvCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMaker.showKufuPhoneDialog(CashResultActivity.this);
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equalsIgnoreCase("cashin-success")) {
                    //充值成功去账户

                    MobclickAgent.onEvent(CashResultActivity.this, UrlConfig.point + 19 + "");
                    LocalApplication.getInstance().getMainActivity().setCheckedFram(4);
                    setResult(4);
                    finish();

                } else if (flag.equalsIgnoreCase("failiure_cashin")) {
                    if (getIntent().getStringExtra("flag").equalsIgnoreCase("failiure_cashin")) {
                        MobclickAgent.onEvent(CashResultActivity.this, UrlConfig.point + 21 + "");
                    }
                    finish();
                }


            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
