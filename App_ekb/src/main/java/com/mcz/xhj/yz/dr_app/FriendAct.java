package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.yz.dr_adapter.FriendAdapter;
import com.mcz.xhj.yz.dr_app.find.ActivityOffActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.FriendBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

/**
 * 活动中心
 */

public class FriendAct extends BaseActivity {
    @BindView(R.id.title_righttextview)
    TextView titleRighttextview;
    //    private String[] tab;//标题
    // 初始化数据
    @BindView(R.id.title_centertextview)
    TextView centertv;
    @BindView(R.id.title_leftimageview)
    ImageView leftima;
    @BindView(R.id.lv_conpons_unused)
    ListView lv_conpons;
    @BindView(R.id.ptr_conponsunuse)
    PtrClassicFrameLayout ptr_conponsunuse;
    @BindView(R.id.ll_norecord)
    LinearLayout ll_norecord;

    private List<FriendBean> lslbs = new ArrayList<FriendBean>();
    private FriendAdapter adapter;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_friend;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == 3) {
            finish();
        }
    }

    @Override
    protected void initParams() {
        getData();
        LocalApplication.getInstance().setFriendAct(this);
        centertv.setText("活动中心");
        titleRighttextview.setVisibility(View.VISIBLE);
        titleRighttextview.setText("往期活动");
        titleRighttextview.setTextColor(getResources().getColor(R.color.activity_center_right));
        leftima.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleRighttextview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendAct.this, ActivityOffActivity.class));
            }
        });
        ptr_conponsunuse.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content,
                                             View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv_conpons, header);
            }
        });
        lv_conpons.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (lslbs.get(position).getStatus() == 1) {
                    startActivityForResult(new Intent(FriendAct.this, WebViewActivity.class)
                            .putExtra("URL", lslbs.get(position).getAppUrl() + "?app=true")
                            .putExtra("TITLE", lslbs.get(position).getTitle())
                            .putExtra("AFID", lslbs.get(position).getId() + ""), 3
                    );
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        OkHttpUtils.post()
                .url(UrlConfig.ACTIVITYLIST)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("status", "1")
                .addParams("pageOn", "1")
                .addParams("pageSize", "200")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                LogUtils.i("--->活动中心："+result);
                ptr_conponsunuse.refreshComplete();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONObject objinfo = objmap.getJSONObject("Page");
                    JSONArray objrows = objinfo.getJSONArray("rows");
                    lslbs = JSON.parseArray(objrows.toJSONString(), FriendBean.class);
                    if (lslbs.size() > 0) {
                        ll_norecord.setVisibility(View.GONE);
                        if (adapter == null) {
                            adapter = new FriendAdapter(FriendAct.this, lslbs);
                            lv_conpons.setAdapter(adapter);
                        } else {
                            adapter.onDateChange(lslbs);
                        }
                    } else {
                        ll_norecord.setVisibility(View.VISIBLE);
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
