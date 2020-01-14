package com.mcz.xhj.yz.dr_app;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.BankLimitationAdapter;
import com.mcz.xhj.yz.dr_bean.BankNameBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/11/6
 * 描述：银行限额列表2.0
 */

public class BankLimitationActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.lv_bank_limotation)
    ListView lvBankLimotation;
    private BankLimitationAdapter bankLimitationAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bank_limitation;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("限额列表");
        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getBankData();
    }

    private List<BankNameBean> lslbs = new ArrayList<BankNameBean>();
    private void getBankData() {
        showWaitDialog("请稍后...", false, "");
        OkHttpUtils.post()
                .url(UrlConfig.BANKNAMELIST)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                dialog.dismiss();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONArray objrows = objmap.getJSONArray("bankQuotaList");
                    lslbs = JSON.parseArray(objrows.toJSONString(), BankNameBean.class);
                    if (lslbs.size() > 0) {
                        bankLimitationAdapter = new BankLimitationAdapter(BankLimitationActivity.this,lslbs);
                        lvBankLimotation.setAdapter(bankLimitationAdapter);
                    }
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    finish();
//					new show_Dialog_IsLogin(getActivity()).show_Is_Login() ;
                } else {
                    ToastMaker.showShortToast("服务器异常");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dialog.dismiss();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

}
