package com.mcz.xhj.yz.dr_app.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.ExperienceConponsAdapter;
import com.mcz.xhj.yz.dr_app.BaseActivity;
import com.mcz.xhj.yz.dr_app.Detail_Tiyan;
import com.mcz.xhj.yz.dr_app.WebViewActivity;
import com.mcz.xhj.yz.dr_app.find.CallCenterActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.TiyanConponsBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.DialogMaker;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/11/3
 * 描述：2.0版体验金界面
 */

public class ExperienceConponsActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.iv_wenhao)
    ImageView ivWenhao;
    @BindView(R.id.lv_experience)
    ListView lvExperience;
    @BindView(R.id.ll_norecord)
    LinearLayout llNorecord;
    @BindView(R.id.tv_commom_question)
    TextView tvCommomQuestion;
    @BindView(R.id.tv_contact_us)
    TextView tvContactUs;
    @BindView(R.id.ptr_conponsunuse)
    PtrClassicFrameLayout ptrConponsunuse;

    private boolean canTiyan;
    private String newHandId;

    private List<TiyanConponsBean> lslbs = new ArrayList<TiyanConponsBean>();
    private ExperienceConponsAdapter adapter;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_experience_conpons;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("体验金");
        getData();

        ptrConponsunuse.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lvExperience, header);
            }
        });

        lvExperience.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("100".equalsIgnoreCase(lslbs.get(position).getSource()) && "0".equalsIgnoreCase(lslbs.get(position).getStatus())) {

                    LocalApplication.getInstance().getMainActivity().isInvest = true;
                    LocalApplication.getInstance().getMainActivity().isInvestChecked = true;
                    LocalApplication.getInstance().getMainActivity().setCheckedFram(2);
                    setResult(3);
                    finish();
                    finish();
                } else if (canTiyan) {
                    startActivity(new Intent(ExperienceConponsActivity.this, Detail_Tiyan.class));
                    finish();
                }

            }
        });

    }

    @OnClick({R.id.title_leftimageview, R.id.iv_wenhao, R.id.tv_commom_question, R.id.tv_contact_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.iv_wenhao:
                startActivity(new Intent(ExperienceConponsActivity.this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.EXPERIENCEGOLD + "&app=true")
                        .putExtra("TITLE", "体验金使用规则"));
                break;
            case R.id.tv_commom_question:
                startActivity(new Intent(ExperienceConponsActivity.this,CallCenterActivity.class)) ;
                break;
            case R.id.tv_contact_us:
                DialogMaker.showKufuPhoneDialog(ExperienceConponsActivity.this);
                break;
        }
    }

    private void getData() {
        canTiyan = false;
        OkHttpUtils.post()
                .url(UrlConfig.CONPONSUNUSE)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("flag", "0")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                LogUtils.i("--->未使用红包conponsUnuse "+result);
                ptrConponsunuse.refreshComplete();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    double amountSum = objmap.getDoubleValue("amountSum");
                    newHandId = objmap.getString("newHandId");
                    JSONArray objrows = objmap.getJSONArray("list");
                    lslbs = JSON.parseArray(objrows.toJSONString(), TiyanConponsBean.class);
                    if (lslbs.size() > 0) {
                        for (int i = 0; i < lslbs.size(); i++) {  //存在可用体验金
                            if ("0".equalsIgnoreCase(lslbs.get(i).getStatus())) {
                                canTiyan = true;
                            }
                        }
                        lvExperience.setVisibility(View.VISIBLE);
                        llNorecord.setVisibility(View.GONE);
                        if (adapter == null) {
                            adapter = new ExperienceConponsAdapter(ExperienceConponsActivity.this, lslbs);
                            lvExperience.setAdapter(adapter);
                        } else {
                            adapter.onDateChange(lslbs);
                        }
                    } else {
                        canTiyan = false;
                        lslbs.clear();
                        if (adapter == null) {
                            lvExperience.setAdapter(adapter);
                        } else {
                            adapter.onDateChange(lslbs);
                        }
                        lvExperience.setVisibility(View.GONE);
                        llNorecord.setVisibility(View.VISIBLE);
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
                ToastMaker.showShortToast("请检查网络");
            }
        });

    }
}
