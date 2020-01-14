package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2017/11/23.
 * 描述：注册成功界面
 */

public class Register_Success extends BaseActivity {
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_leftimageview)
    ImageView title_leftimageview;

    @BindView(R.id.tv_real_authentification)
    TextView tvRealAuthentification;
    @BindView(R.id.tv_invest)
    TextView tvInvest;

    @Override
    protected int getLayoutId() {
        return R.layout.register_success;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("注册成功");
        title_leftimageview.setVisibility(View.GONE);
    }


    @OnClick({R.id.tv_real_authentification, R.id.tv_invest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_real_authentification:
                startActivity(new Intent(this, FourPartAct.class));
                finish();
                break;
            case R.id.tv_invest:
                LocalApplication.getInstance().getMainActivity().setCheckedFram(2);
                finish();
                break;
        }
    }
}
