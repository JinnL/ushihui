package com.mcz.xhj.yz.dr_app.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.TiyanConponsAdapter;
import com.mcz.xhj.yz.dr_app.BaseActivity;
import com.mcz.xhj.yz.dr_app.Detail_Tiyan;
import com.mcz.xhj.yz.dr_app.WebViewActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.TiyanConponsBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：体验标红包列表
 * 创建人：shuc
 * 创建时间：2017/2/22 16:23
 * 修改人：DELL
 * 修改时间：2017/2/22 16:23
 * 修改备注：
 */
public class TiyanConponsListActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.lv_conpons_unused)
    ListView lvConponsUnused;
    @BindView(R.id.ll_norecord)
    LinearLayout llNorecord;

    @BindView(R.id.ptr_conponsunuse)
    PtrClassicFrameLayout ptrConponsunuse;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_wenhao)
    ImageView ivWenhao;
    @BindView(R.id.tv_amountsum)
    TextView tvAmountsum;

    private boolean canTiyan;
    private String newHandId;

    private List<TiyanConponsBean> lslbs = new ArrayList<TiyanConponsBean>();
    private TiyanConponsAdapter adapter;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;

    @Override
    protected int getLayoutId() {
        return R.layout.tiyan_conpons_liebiao;
    }

    @Override
    protected void initParams() {
        rlTitle.setBackgroundResource(R.drawable.bg_color);
        titleCentertextview.setText("体验金");
        //titleCentertextview.setTextColor(Color.WHITE);
        //titleLeftimageview.setImageResource(R.mipmap.fanhui);
        getData();
        ptrConponsunuse.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content,
                                             View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lvConponsUnused, header);
            }
        });

        lvConponsUnused.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("100".equalsIgnoreCase(lslbs.get(position).getSource()) && "0".equalsIgnoreCase(lslbs.get(position).getStatus())) {
//                    if (!TextUtils.isEmpty(newHandId)) {
//                        startActivity(new Intent(TiyanConponsListActivity.this, Act_Detail_Pro_New.class).putExtra("pid", newHandId));
//                        finish();
//                    }
                    LocalApplication.getInstance().getMainActivity().isInvest = true;
                    LocalApplication.getInstance().getMainActivity().isInvestChecked = true;
                    LocalApplication.getInstance().getMainActivity().setCheckedFram(2);
                    setResult(3);
                    finish();
                    finish();
                } else if (canTiyan) {
                    startActivity(new Intent(TiyanConponsListActivity.this, Detail_Tiyan.class));
                    finish();
                }

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.title_leftimageview,  R.id.iv_wenhao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.iv_wenhao:
//                startActivity(new Intent(TiyanConponsListActivity.this, TiyanExplainActivity.class));
                startActivity(new Intent(TiyanConponsListActivity.this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.EXPERIENCEGOLD)
                        .putExtra("TITLE", "体验金使用规则"));
                break;
            /*case R.id.btn_bottom:
                if (lslbs.size() == 1 && "100".equalsIgnoreCase(lslbs.get(0).getSource()) && "0".equalsIgnoreCase(lslbs.get(0).getStatus())) {
//                    if (!TextUtils.isEmpty(newHandId)) {
//                        startActivity(new Intent(TiyanConponsListActivity.this, Act_Detail_Pro_New.class).putExtra("pid", newHandId));
//                        finish();
//                    }
                    LocalApplication.getInstance().getMainActivity().isInvest = true;
                    LocalApplication.getInstance().getMainActivity().isActivity = true;
                    LocalApplication.getInstance().getMainActivity().isInvestChecked = true;
                    LocalApplication.getInstance().getMainActivity().setCheckedFram(2);
                    setResult(3);
                    finish();
                    finish();
                } else if (canTiyan) {
                    startActivity(new Intent(TiyanConponsListActivity.this, Detail_Tiyan.class));
                    finish();
                }

                break;*/
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
                    tvAmountsum.setText(stringCut.getNumKbs(amountSum));
                    newHandId = objmap.getString("newHandId");
                    JSONArray objrows = objmap.getJSONArray("list");
                    lslbs = JSON.parseArray(objrows.toJSONString(), TiyanConponsBean.class);
                    if (lslbs.size() > 0) {
                        for (int i = 0; i < lslbs.size(); i++) {  //存在可用体验金
                            if ("0".equalsIgnoreCase(lslbs.get(i).getStatus())) {
                                canTiyan = true;
                            }
                        }
                        lvConponsUnused.setVisibility(View.VISIBLE);
                        llNorecord.setVisibility(View.GONE);
                        if (adapter == null) {
                            adapter = new TiyanConponsAdapter(TiyanConponsListActivity.this, lslbs);
                            lvConponsUnused.setAdapter(adapter);
                        } else {
                            adapter.onDateChange(lslbs);
                        }
                    } else {
                        canTiyan = false;
                        lslbs.clear();
                        if (adapter == null) {
                            lvConponsUnused.setAdapter(adapter);
                        } else {
                            adapter.onDateChange(lslbs);
                        }
                        lvConponsUnused.setVisibility(View.GONE);
                        llNorecord.setVisibility(View.VISIBLE);
                    }

                    /*if (canTiyan) {
                        btnBottom.setBackgroundResource(R.drawable.bg_color_yellow);
                        if (lslbs.size() == 1 && "100".equalsIgnoreCase(lslbs.get(0).getSource()) && "0".equalsIgnoreCase(lslbs.get(0).getStatus())) {
                            btnBottom.setBackgroundResource(R.drawable.bg_color_yellow);
                            btnBottom.setText("立即激活");
                        } else {
                            btnBottom.setText("立即变现");
                        }
                    } else {
                        btnBottom.setBackgroundResource(R.drawable.bg_corner_grey_tiyan);
                    }*/

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
