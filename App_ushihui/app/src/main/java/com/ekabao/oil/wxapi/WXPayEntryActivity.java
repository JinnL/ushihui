package com.ekabao.oil.wxapi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.ekabao.oil.R;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.ui.activity.me.MallOrderDetailsActivity;
import com.ekabao.oil.ui.activity.me.MyOilCardBuyDetailsActivity;
import com.ekabao.oil.ui.activity.me.MyOrderDetailsActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    @BindView(R.id.pb_result)
    ProgressBar pbResult;


    private IWXAPI api;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result_wx);
        ButterKnife.bind(this);


        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        api.handleIntent(getIntent(), this);

        preferences = LocalApplication.getInstance().sharereferences;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtils.e("WXPayEntryActivity" + resp);

        int code = resp.errCode;


        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

          //  LogUtils.e("payfail支付取消");


            if (BaseResp.ErrCode.ERR_OK == code) {


                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        pbResult.setVisibility(View.GONE);



                        DialogMaker.showPaySuccessDialog(WXPayEntryActivity.this, callBack,
                                preferences.getInt("activitytype", 0),
                                preferences.getString("orderId", ""));

                       /* Intent inforIntent = new Intent(WXPayEntryActivity.this, OrderActivity.class);
                        // 放入当前点击的位置
                        startActivity(inforIntent);
                       */
                    }
                }, 100);   //5秒


            } else if (BaseResp.ErrCode.ERR_USER_CANCEL == code) {
                LogUtils.e("payfail支付取消");

               // payfail();



                ToastUtil.showToast("支付取消");

                finish();




            } else {

            }


        }


    }

    /**
     * 第三方支付的时候,用户取消或者支付失败
     * */
    private void payfail() {
        String orderId = preferences.getString("orderId", "");
        LogUtils.e("payfail"+orderId);

        if (!TextUtils.isEmpty(orderId)){

            // 1：油卡 2：手机 3：直购
            int activitytype = preferences.getInt("activitytype", 0);

            switch (activitytype) {
                case 5: //油卡再支付
                case 1:
                    startActivity(new Intent(WXPayEntryActivity.this, MyOrderDetailsActivity.class)
                            .putExtra("orderId", orderId)
                            .putExtra("type", activitytype)
                    );
                    finish();
                    break;
                case 2:
                    startActivity(new Intent(WXPayEntryActivity.this, MyOrderDetailsActivity.class)
                            .putExtra("orderId", orderId)
                            .putExtra("type", activitytype)
                    );
                    finish();
                    break;
                case 3:
                    startActivity(new Intent(WXPayEntryActivity.this, MyOilCardBuyDetailsActivity.class)
                            .putExtra("orderId", orderId)
                            .putExtra("type", activitytype)
                    );
                    finish();
                    break;
                case 4:
                    startActivity(new Intent(WXPayEntryActivity.this, MallOrderDetailsActivity.class)
                            .putExtra("orderId", Integer.parseInt(orderId))
                            .putExtra("type", activitytype)
                    );
                    finish();
                    break;
            }
        }
    }

    DialogMaker.DialogCallBack callBack = new DialogMaker.DialogCallBack() {
        @Override
        public void onButtonClicked(Dialog dialog, int position, Object tag) {

        }

        @Override
        public void onCancelDialog(Dialog dialog, Object tag) {
            LogUtils.e("支付弹窗"+"隐藏了"+tag);

            finish();
        }
    };
    @Override
    public void onReq(BaseReq baseReq) {

    }

//    @OnClick({R.id.jmui_return_btn, R.id.tv_pay_sure})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.jmui_return_btn:
//                break;
//            case R.id.tv_pay_sure:
//                break;
//        }
//    }
}