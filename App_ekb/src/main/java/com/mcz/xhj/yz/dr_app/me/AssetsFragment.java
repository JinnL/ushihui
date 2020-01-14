package com.mcz.xhj.yz.dr_app.me;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_app_fragment.BaseFragment;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：总资产
 * 创建人：shuc
 * 创建时间：2017/2/23 13:37
 * 修改人：DELL
 * 修改时间：2017/2/23 13:37
 * 修改备注：
 */
public class AssetsFragment extends BaseFragment {

    @BindView(R.id.tv_benjin)
    TextView tvBenjin;
    @BindView(R.id.tv_shouyi)
    TextView tvShouyi;
    @BindView(R.id.tv_yue)
    TextView tvYue;
    @BindView(R.id.tv_amount)
    TextView tvAmount;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    double balance;
    double wprincipal;
    double winterest;

    @Override
    protected int getLayoutId() {
        return R.layout.assets_fragment;
    }

    @Override
    protected void initParams() {
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void getData() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.MYASSETSINFO)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                dismissDialog();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONObject objfunds = objmap.getJSONObject("funds");
                    balance = objfunds.getDoubleValue("balance");//余额
                    wprincipal = objfunds.getDoubleValue("wprincipal");//待收本金
                    winterest = objfunds.getDoubleValue("winterest");//待收收益
                    double daishou = wprincipal + winterest + balance;
                    tvAmount.setText(stringCut.getNumKb(daishou));
                    tvBenjin.setText(stringCut.getNumKb(wprincipal));
                    tvShouyi.setText(stringCut.getNumKb(winterest));
                    tvYue.setText(stringCut.getNumKb(balance));
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    new show_Dialog_IsLogin(mContext).show_Is_Login();
                } else {
                    ToastMaker.showShortToast("服务器异常");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ToastMaker.showShortToast("请检查网络");
            }
        });

    }
}
