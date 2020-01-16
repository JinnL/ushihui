package com.mcz.xhj.yz.dr_app.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.InvestmentAdapter;
import com.mcz.xhj.yz.dr_app.BaseActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.MyInvestListBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
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
 * Created by zhulang on 2017/9/12.
 * 2.0版 我的投资
 */

public class NewMyInvestmentActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_benjin)
    TextView tvBenjin;
    @BindView(R.id.tv_zonglixi)
    TextView tvZonglixi;
    @BindView(R.id.lv_touzi)
    ListView lvTouzi;

    @BindView(R.id.ptr_do)
    PtrClassicFrameLayout ptrDo;
    @BindView(R.id.title_holding)
    TextView title_holding;
    @BindView(R.id.img_holding)
    ImageView img_holding;
    @BindView(R.id.ll_holding)
    LinearLayout ll_holding;
    @BindView(R.id.title_repayed)
    TextView titleRepayed;
    @BindView(R.id.img_repayed)
    ImageView imgRepayed;
    @BindView(R.id.ll_repayed)
    LinearLayout llRepayed;

    private boolean isLastitem = false;
    private boolean isLoading;
    private View footer;
    private LinearLayout footerlayout;
    private TextView tv_footer;
    private ProgressBar pb;
    private InvestmentAdapter adapter;
    private int page;
    private int flag = 0;//0=持有中(0,1)，1==已还款(3)
    private String statuses = "4";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myinvest_new;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("我的投资");
        adapter = null;
        page = 1;

        flag = Integer.parseInt(getIntent().getStringExtra("flag"));
        if (flag == 0){
            title_holding.setTextColor(getResources().getColor(R.color.red));
            img_holding.setVisibility(View.VISIBLE);
            titleRepayed.setTextColor(0XFFC9C4C3);
            imgRepayed.setVisibility(View.GONE);
        }else{
            title_holding.setTextColor(0XFFC9C4C3);
            img_holding.setVisibility(View.GONE);
            titleRepayed.setTextColor(getResources().getColor(R.color.red));
            imgRepayed.setVisibility(View.VISIBLE);
        }
        getData(1,flag);

        ptrDo.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getData(page,flag);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content,
                                             View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lvTouzi, header);
            }
        });
        footer = View.inflate(NewMyInvestmentActivity.this, R.layout.footer_layout, null);

        footerlayout = (LinearLayout) footer.findViewById(R.id.load_layout);
        pb = (ProgressBar) footer.findViewById(R.id.pb);
        tv_footer = (TextView) footer.findViewById(R.id.tv_footer);
        footerlayout.setVisibility(View.GONE);
        lvTouzi.addFooterView(footer);
        lvTouzi.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    isLastitem = true;
                } else {
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
                        page++;
                        getData(page,flag);
                    }
                }
            }
        });

        lvTouzi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                intent.setClass(NewMyInvestmentActivity.this, InvestmentDetails2Activity.class);
                intent.putExtra("investId",lslb.get(position).getId());
                intent.putExtra("type",lslb.get(position).getType());
                startActivityForResult(intent, 4);

                /*if (position < lslb.size()) {
                    Intent intent = new Intent();
                    intent.setClass(NewMyInvestmentActivity.this, InvestmentDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("investBean", lslb.get(position));
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 4);
                }*/
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4 && resultCode == 4) {//finish
            finish();
        }
    }

    @OnClick({R.id.title_leftimageview, R.id.ll_holding, R.id.ll_repayed})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;

            case R.id.ll_holding:
                if (flag == 0) {
                    return;
                }
                flag = 0;
                title_holding.setTextColor(getResources().getColor(R.color.red));
                img_holding.setVisibility(View.VISIBLE);
                titleRepayed.setTextColor(0XFFC9C4C3);
                imgRepayed.setVisibility(View.GONE);
                getData(1,flag);
                break;
            case R.id.ll_repayed:
                if (flag == 1) {
                    return;
                }
                flag = 1;
                title_holding.setTextColor(0XFFC9C4C3);
                img_holding.setVisibility(View.GONE);
                titleRepayed.setTextColor(getResources().getColor(R.color.red));
                imgRepayed.setVisibility(View.VISIBLE);
                getData(1,flag);
                break;
        }
    }

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private List<MyInvestListBean> lslb = new ArrayList<MyInvestListBean>();
    private List<MyInvestListBean> lslbs = new ArrayList<MyInvestListBean>();

    private void getData(int cur_page,int flag) {
        page = cur_page;
        showWaitDialog("加载中...", false, "");
        if (flag == 0) {
            statuses = "4";
        }
        if (flag == 1) {
            statuses = "3";
        }
        OkHttpUtils.post()
                .url(UrlConfig.MYINVESTDAISINFO)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("status", statuses)
                .addParams("pageSize", "10")
                .addParams("pageOn", page + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                //LogPrintUtil.e("LF--->我的投资", result);
                LogUtils.i("--->我的投资 result：" + result);
                ptrDo.refreshComplete();
                dismissDialog();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    dismissDialog();
                    JSONObject objmap = obj.getJSONObject("map");
                    double tenderSum = objmap.getDoubleValue("tenderSum");//代收金额
                    double accumulatedIncome = objmap.getDoubleValue("accumulatedIncome");//累计收益
                    double principal = objmap.getDoubleValue("principal");//投资总额
                    tvBenjin.setText(stringCut.getNumKb(tenderSum));
                    tvTotal.setText(stringCut.getNumKb(principal));
                    tvZonglixi.setText(stringCut.getNumKb(accumulatedIncome));

                    JSONObject objpage = objmap.getJSONObject("page");
                    JSONArray objrows = objpage.getJSONArray("rows");

                    lslbs = JSON.parseArray(objrows.toJSONString(), MyInvestListBean.class);

                    if (page > 1) {
                        lslb.addAll(lslbs);
                    } else {
                        lslb = lslbs;
                    }
                    if (lslb.size() == 0){
                        lslb = lslbs;
                    }
                    //LogUtils.i("--->我的投资：lslb=" + lslb);
                    if (adapter == null) {
                        adapter = new InvestmentAdapter(NewMyInvestmentActivity.this, lslb);
                        lvTouzi.setAdapter(adapter);
                    } else {
                        if (page == 1) {
                            adapter = new InvestmentAdapter(NewMyInvestmentActivity.this, lslb);
                            lvTouzi.setAdapter(adapter);
                        }
                        adapter.onDateChange(lslb);
                    }

                    if (lslbs.size() == 10) {
                        loadComplete();
                    } else {
                        tv_footer.setText("全部加载完毕");
                        footerlayout.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
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
                dismissDialog();
                ptrDo.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });

    }


    public void loadComplete() {
        isLoading = false;
        footerlayout.setVisibility(View.GONE);
    }
}
