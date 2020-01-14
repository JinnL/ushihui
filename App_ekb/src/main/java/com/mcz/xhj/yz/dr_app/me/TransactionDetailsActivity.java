package com.mcz.xhj.yz.dr_app.me;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.TransactionDetailsAdapter;
import com.mcz.xhj.yz.dr_app.BaseActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.TransactionDetailsBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
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
 * 类描述：交易明细
 * 创建人：shuc
 * 创建时间：2017/2/23 10:14
 * 修改人：DELL
 * 修改时间：2017/2/23 10:14
 * 修改备注：
 */
public class TransactionDetailsActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.lv_transaction_details)
    ListView lvTransactionDetails;
    @BindView(R.id.ptr_invest_weban)
    PtrClassicFrameLayout ptrInvestWeban;

    private View view;
    private boolean isLastitem = false;
    private boolean isLoading;
    private View footer;
    private LinearLayout footerlayout;
    private TextView tv_footer;
    private ProgressBar pb;

    private int page = 1;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private TransactionDetailsAdapter adapter;
    private List<TransactionDetailsBean> lslb = new ArrayList<TransactionDetailsBean>();
    private List<TransactionDetailsBean> lslbs = new ArrayList<TransactionDetailsBean>();
    @Override
    protected int getLayoutId() {
        return R.layout.transaction_details;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("交易明细");

        getData();
        ptrInvestWeban.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,
                        lvTransactionDetails, header);
            }
        });
        footer = View.inflate(this, R.layout.footer_layout, null);
        footerlayout = (LinearLayout) footer.findViewById(R.id.load_layout);
        pb = (ProgressBar) footer.findViewById(R.id.pb);
        tv_footer = (TextView) footer.findViewById(R.id.tv_footer);
        footerlayout.setVisibility(View.GONE);
        lvTransactionDetails.addFooterView(footer);
        lvTransactionDetails.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    isLastitem = true;
                }else{
                    isLastitem = false;
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.title_leftimageview)
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.title_leftimageview:
                finish();
                break;
        }

    }

    private void getData() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.MYDETAIL)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("tradeType", "0")
                .addParams("pageSize", "10")
                .addParams("pageOn", page + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        dismissDialog();
                        ptrInvestWeban.refreshComplete();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean("success")) {
                            JSONObject objmap = obj.getJSONObject("map");
                            JSONObject objpage = objmap.getJSONObject("page");
                            JSONArray objrows = objpage.getJSONArray("rows");
                            lslbs = JSON.parseArray(objrows.toJSONString(), TransactionDetailsBean.class);
                            if(lslbs.size()>0){
                                if(page>1){
                                    lslb.addAll(lslbs);
                                }
                                else{
                                    lslb=lslbs;
                                }
                                if (adapter == null||page==1) {
                                    adapter = new TransactionDetailsAdapter(TransactionDetailsActivity.this, lslb);
                                    lvTransactionDetails.setAdapter(adapter);
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
                                    adapter = new TransactionDetailsAdapter(TransactionDetailsActivity.this, lslbs);
                                    lvTransactionDetails.setAdapter(adapter);
                                }
                                tv_footer.setText("全部加载完毕");
                                footerlayout.setVisibility(View.VISIBLE);
                                pb.setVisibility(View.GONE);
                            }
                        }
                        else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
//							new show_Dialog_IsLogin(MyDetailAct.this).show_Is_Login() ;
                        }
                        else {
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

    public void loadComplete(){
        isLoading = false;
        footerlayout.setVisibility(View.GONE);
    }
}
