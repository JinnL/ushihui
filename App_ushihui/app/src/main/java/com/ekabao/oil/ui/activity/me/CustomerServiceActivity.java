package com.ekabao.oil.ui.activity.me;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;

import java.util.List;

import butterknife.BindView;

public class CustomerServiceActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.cl_qq)
    ConstraintLayout clQq;
    @BindView(R.id.cl_weixin)
    ConstraintLayout clWeixin;
    @BindView(R.id.cl_call_phone)
    ConstraintLayout clCallPhone;
    @BindView(R.id.rl_feedback)
    LinearLayout rlFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_customer_service);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customer_service;

    }

    @Override
    protected void initParams() {
        titleLeftimageview.setOnClickListener(this);

        titleCentertextview.setText("客服中心");

        viewLineBottom.setVisibility(View.GONE);
        clWeixin.setOnClickListener(this);
        clCallPhone.setOnClickListener(this);
        rlFeedback.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.cl_call_phone: //客服电话
                DialogMaker.showKufuPhoneDialog(this);
                break;
            case R.id.rl_feedback: //意见反馈

                startActivity(new Intent(this, FeedbackActivity.class)
                );
                break;

            case R.id.cl_weixin:
                //微信
                if (!isAvilible(this, "com.tencent.mm")) {
                    ToastMaker.showShortToast("请先安装微信");
                    return;
                } else {
                    copy(this);
                    DialogMaker.showAddOilCardSureDialog(this, "微信号已经成功复制",
                            "请前往微信搜索并关注我们吧!",
                            "稍后再去", "去关注", new DialogMaker.DialogCallBack() {

                                @Override
                                public void onButtonClicked(Dialog dialog, int position, Object tag) {
                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, "a");
                                    sendIntent.setType("text/plain");

                                    ComponentName cmp = new ComponentName("com.tencent.mm",
                                            "com.tencent.mm.ui.LauncherUI");
                                    sendIntent.setAction(Intent.ACTION_MAIN);
                                    sendIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    sendIntent.setComponent(cmp);
                                    startActivity(sendIntent);
                                }

                                @Override
                                public void onCancelDialog(Dialog dialog, Object tag) {

                                }
                            }, "");

                }
                break;
        }
    }

    /**
     * 判断手机里有没有这个应用
     *
     * @param context
     * @param packageName
     * @return
     */
    public boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName
                    .equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    public void copy(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context

                .getSystemService(Context.CLIPBOARD_SERVICE);

        cmb.setText(getResources().getString(R.string.weixin));

    }
}
