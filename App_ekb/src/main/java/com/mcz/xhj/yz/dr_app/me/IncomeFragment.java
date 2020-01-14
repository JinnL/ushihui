package com.mcz.xhj.yz.dr_app.me;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.IncomeAdapter;
import com.mcz.xhj.yz.dr_app_fragment.BaseFragment;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.IncomeBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：累计收益
 * 创建人：shuc
 * 创建时间：2017/2/23 13:38
 * 修改人：DELL
 * 修改时间：2017/2/23 13:38
 * 修改备注：
 */
public class IncomeFragment extends BaseFragment {
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.lv_income)
    ListView lvIncome;
    @BindView(R.id.ptr_conponsunuse)
    PtrClassicFrameLayout ptrConponsunuse;

    private int page = 1;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private IncomeAdapter adapter;
    private List<IncomeBean> lslb = new ArrayList<IncomeBean>();
    private List<IncomeBean> lslbs = new ArrayList<IncomeBean>();

    private View view;
    private boolean isLastitem = false;
    private boolean isLoading;
    private View footer;
    private LinearLayout footerlayout;
    private TextView tv_footer;
    private ProgressBar pb;

    @Override
    protected int getLayoutId() {
        return R.layout.income_fragment;
    }

    @Override
    protected void initParams() {
        getData();
        ptrConponsunuse.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame,
                                             View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,
                        lvIncome, header);
            }
        });

        footer = View.inflate(mContext, R.layout.footer_layout, null);
        footerlayout = (LinearLayout) footer.findViewById(R.id.load_layout);
        pb = (ProgressBar) footer.findViewById(R.id.pb);
        tv_footer = (TextView) footer.findViewById(R.id.tv_footer);
        footerlayout.setVisibility(View.GONE);
        lvIncome.addFooterView(footer);
        lvIncome.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    isLastitem = true;
                }else{
                    isLastitem = false;
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isLastitem && scrollState == SCROLL_STATE_IDLE) {
                    if (!isLoading) {
                        isLoading = true;
                        footerlayout.setVisibility(View.VISIBLE);
                        getData();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void getData() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.LEIJISHOUYI)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("pageOn", page+"")
                .addParams("pageSize", "10")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                dismissDialog();
                ptrConponsunuse.refreshComplete();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONObject pageInfo = objmap.getJSONObject("page");
                    JSONArray rows = pageInfo.getJSONArray("rows");
                    tvAmount.setText(stringCut.getNumKb(stringCut.getDoubleFromJsonObject(objmap,"AccumulatedIncome")));
                    lslbs = JSON.parseArray(rows.toJSONString(),
                            IncomeBean.class);
                    if(lslbs.size()>0){
                        if(page>1){
                            lslb.addAll(lslbs);
                        }
                        else{
                            lslb=lslbs;
                        }
                        if (adapter == null||page==1) {
                            adapter = new IncomeAdapter(mContext, lslb);
                            lvIncome.setAdapter(adapter);
                        } else {
                            adapter.onDateChange(lslb);
                        }
                        page++;
                        loadComplete();
                        if(lslbs.size()==10){
                            tv_footer.setText("上拉加载更多");
                            footerlayout.setVisibility(View.VISIBLE);
                            pb.setVisibility(View.GONE);
                        }else{
                            tv_footer.setText("全部加载完毕");
                            footerlayout.setVisibility(View.VISIBLE);
                            pb.setVisibility(View.GONE);
                        }
                    }else{
                        if(page==1){
                            adapter = new IncomeAdapter(mContext, lslbs);
                            lvIncome.setAdapter(adapter);
                        }
                        tv_footer.setText("全部加载完毕");
                        footerlayout.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                    }

                } else if ("9998".equals(obj.getString("errorCode"))) {
                    new show_Dialog_IsLogin(mContext).show_Is_Login();
                } else {
                    ToastMaker.showShortToast("服务器异常");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ptrConponsunuse.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });

    }

    public void loadComplete(){
        isLoading = false;
        footerlayout.setVisibility(View.GONE);
    }
}
