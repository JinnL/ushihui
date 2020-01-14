package com.ekabao.oil.ui.activity.me;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.BankName_Pic;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.NumberToCN;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 易卡宝  App
 *
 * @time 2018/7/23 16:32
 * Created by lj on 2018/7/23 16:32.
 */

public class MeBankActivity extends BaseActivity {
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
    @BindView(R.id.iv_bank)
    ImageView ivBank;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_bank_class)
    TextView tvBankClass;
    @BindView(R.id.tv_limit)
    TextView tvLimit;
    @BindView(R.id.tv_limit_day)
    TextView tvLimitDay;
    @BindView(R.id.tv_bank_number)
    TextView tvBankNumber;
    @BindView(R.id.rl_bank)
    RelativeLayout rlBank;


    private String bankid;
    private String bankName;
    private String bankNum;
    private Integer singleQuotaJYT;
    private Integer dayQuotaJYT;
    private BankName_Pic bp;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_me_bank;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("我的银行卡");

        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bankInfo();
    }

    private void bankInfo() {
        OkHttpUtils.post().url(UrlConfig.MEMBERSETTING)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("--->memberSetting/获取用户银行卡信息：" + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            bankid = map.getString("bankId");
                            bankName = map.getString("bankName");
                            bankNum = map.getString("bankNum");
                            singleQuotaJYT = map.getInteger("singleQuotaJYT");
                            dayQuotaJYT = map.getInteger("dayQuotaJYT");
                            if (bp == null) {
                                bp = new BankName_Pic();
                            }
                            Integer pic = bp.bank_bg(bankid);
                            rlBank.setBackgroundResource(pic);

                            Integer integer = bp.bank_Pic_white(bankid);
                            ivBank.setImageResource(integer);

                            tvBankName.setText(bankName);
                            tvBankNumber.setText(map.getString("idCards"));
                            tvBankClass.setText("储蓄卡");
                            //iv_bank.setImageResource(pic);
                            //tv_bankname.setText(bankName);
                            //tv_banknum.setText("储蓄卡(" + bankNum + ")");
                            //"单笔5万/单日30万"  StringCut.getNumKbs(singleQuotaJYT) + "元"
                            String s = NumberToCN.number2CNMontrayUnit(new BigDecimal(singleQuotaJYT));

                            tvLimit.setText("单笔" + s);

                            String s1 = NumberToCN.number2CNMontrayUnit(new BigDecimal(dayQuotaJYT));
                            tvLimitDay.setText("单日" + s1);


                        } else {
                            ToastMaker.showShortToast("获取用户银行卡信息失败");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
