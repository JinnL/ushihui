package com.ekabao.oil.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoviceRegisterActivity extends BaseActivity {


    @BindView(R.id.bt_register)
    Button btRegister;
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

    /**
     * 新人注册专享
     *
     * @time 2018/11/12 10:07
     * Created by lj
     */
  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novice_register);
    }*/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_novice_register;
    }



    @Override
    protected void initParams() {
        titleCentertextview.setText("新人专享");
       // titleCentertextview.setTextColor(Color.parseColor("#ffffff"));
        //rlTitle.setVisibility(View.GONE);
       // rlTitle.setBackgroundColor(Color.parseColor("#00000000"));
        viewLineBottom.setVisibility(View.GONE);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (LocalApplication.getInstance().sharereferences.getString("uid", "")
                        .equalsIgnoreCase("")) {
                    startActivity(new Intent(NoviceRegisterActivity.this,
                            RegisterActivity.class));
                } else {
                    ToastMaker.showShortToast("你的号码已注册过,请换个号码");
                }
            }
        });
        float statusBarHeight = getStatusBarHeight();
        LogUtils.e("statusBarHeight"+statusBarHeight);
       /* RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(rlTitle.getLayoutParams());
        layoutParams.setMargins(0,(int)statusBarHeight,0,0);
        rlTitle.setLayoutParams(layoutParams);*/

        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    //返回值就是状态栏的高度,得到的值单位px
    public float getStatusBarHeight() {
        float result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimension(resourceId);
        }
        return result;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
