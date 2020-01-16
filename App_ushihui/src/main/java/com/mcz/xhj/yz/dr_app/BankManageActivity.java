package com.mcz.xhj.yz.dr_app;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.BankName_Pic;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/10/12 0012.
 * 描述：银行卡管理
 */

public class BankManageActivity extends BaseActivity {
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.iv_bank)
    ImageView iv_bank;
    @BindView(R.id.tv_bankname)
    TextView tv_bankname;
    @BindView(R.id.tv_banknum)
    TextView tv_banknum;
    @BindView(R.id.tv_quota)
    TextView tv_quota;

    private String bankid;
    private String bankName;
    private String bankNum;
    private Integer singleQuotaJYT;
    private Integer dayQuotaJYT;
    private BankName_Pic bp;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bank_manage;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("银行卡管理");
        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bankInfo();//获取银行卡信息
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
                            if(bp==null){
                                bp = new BankName_Pic();
                            }
                            Integer pic = bp.bank_Pic(bankid);
                            iv_bank.setImageResource(pic);
                            tv_bankname.setText(bankName);
                            tv_banknum.setText("储蓄卡(" + bankNum + ")");
                            //"单笔5万/单日30万"
                            tv_quota.setText("单笔"+stringCut.getNumKbs(singleQuotaJYT) + "元/单日"+stringCut.getNumKbs(dayQuotaJYT) + "元");

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


}
