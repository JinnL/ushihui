package com.ekabao.oil.ui.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderSelectActivity extends BaseActivity {

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
    @BindView(R.id.ib_select_1)
    ImageButton ibSelect1;
    @BindView(R.id.ib_select_2)
    ImageButton ibSelect2;
    @BindView(R.id.ib_select_3)
    ImageButton ibSelect3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_order_select);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_select;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("选择订单类型");
    }

    @OnClick({R.id.title_leftimageview, R.id.ib_select_1, R.id.ib_select_2, R.id.ib_select_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.ib_select_1://1：油卡
                startActivity(new Intent(this, OrderActivity.class)
                        .putExtra("type", 1));
                break;
            case R.id.ib_select_2:  //油卡购买订单
                startActivity(new Intent(this,
                        OilCardOrderActivity.class)
                        .putExtra("type", 3)
                );
                break;
            case R.id.ib_select_3: //手机充值订单 1：油卡 2：手机 3：直购
                startActivity(new Intent(this, OrderActivity.class)
                        .putExtra("type", 2)
                );
                break;
        }
    }
}
