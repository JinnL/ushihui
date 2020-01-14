package com.mcz.xhj.yz.dr_app.me;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_app.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：总资产与累计收益
 * 创建人：shuc
 * 创建时间：2017/2/23 11:37
 * 修改人：DELL
 * 修改时间：2017/2/23 11:37
 * 修改备注：
 */
public class AssetsAndIncomeActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_righttextview)
    TextView titleRighttextview;
    @BindView(R.id.tv_assets)
    TextView tvAssets;
    @BindView(R.id.tv_income)
    TextView tvIncome;
    @BindView(R.id.ll_details)
    LinearLayout ll_details;

    private int isFrom = 0;//0：总资产 1：累计收益

    @Override
    protected int getLayoutId() {
        return R.layout.assets_income;
    }

    @Override
    protected void initParams() {
        isFrom = Integer.parseInt(getIntent().getStringExtra("isFrom"));
        mHandler.sendEmptyMessage(isFrom);
        if(isFrom == 0){
            toFragment(new AssetsFragment());
        }else if(isFrom == 1){
            toFragment(new IncomeFragment());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.title_leftimageview, R.id.title_righttextview,R.id.tv_assets, R.id.tv_income})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.title_righttextview:
                startActivity(new Intent(AssetsAndIncomeActivity.this,TransactionDetailsActivity.class));
                break;
            case R.id.tv_assets:
                mHandler.sendEmptyMessage(0);
                toFragment(new AssetsFragment());
                break;
            case R.id.tv_income:
                mHandler.sendEmptyMessage(1);
                toFragment(new IncomeFragment());
                break;
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    tvIncome.setBackgroundResource(R.drawable.bg_corner_kong_yellow_you);
                    tvAssets.setBackgroundResource(R.drawable.bg_corner_yellow_zuo);
                    tvAssets.setTextColor(getResources().getColor(R.color.white));
                    tvIncome.setTextColor(getResources().getColor(R.color.base_yellow_color));
                    break;
                case 1:
                    tvIncome.setBackgroundResource(R.drawable.bg_corner_yellow_you);
                    tvAssets.setBackgroundResource(R.drawable.bg_corner_kong_yellow_zuo);
                    tvAssets.setTextColor(getResources().getColor(R.color.base_yellow_color));
                    tvIncome.setTextColor(getResources().getColor(R.color.white));
                    break;

            }
            super.handleMessage(msg);
        }
    };

    public void toFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ll_details,fragment);
        transaction.commit();
    }

}
