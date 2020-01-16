package com.mcz.xhj.yz.dr_app.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
 * 类描述：我的投资
 * 创建人：shuc
 * 创建时间：2017/2/24 13:41
 * 修改人：DELL
 * 修改时间：2017/2/24 13:41
 * 修改备注：
 */
public class MyInvestmentActivity extends BaseActivity {

    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.tv_zichan)
    TextView tvZichan;
    @BindView(R.id.tv_benjin)
    TextView tvBenjin;
    @BindView(R.id.tv_zonglixi)
    TextView tvZonglixi;
    @BindView(R.id.lv_touzi)
    ListView lvTouzi;
    @BindView(R.id.tv_lishijilu)
    TextView tvLishijilu;
    @BindView(R.id.ptr_do)
    PtrClassicFrameLayout ptrDo;

    private boolean isLastitem = false;
    private boolean isLoading;
    private View footer;
    private LinearLayout footerlayout;
    private TextView tv_footer;
    private ProgressBar pb;
    private InvestmentAdapter adapter;
    private int page;

    @Override
    protected int getLayoutId() {
        return R.layout.me_investment;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("我的投资");

        adapter = null;
        page=1;
        getData();
        ptrDo.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content,
                                             View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lvTouzi, header);
            }
        });
        footer = View.inflate(MyInvestmentActivity.this,R.layout.footer_layout, null);
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
                if(firstVisibleItem+visibleItemCount == totalItemCount){
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

        lvTouzi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if(position < lslb.size()){
                    Intent intent = new Intent();
                    intent.setClass(MyInvestmentActivity.this, InvestmentDetails2Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("investBean", lslb.get(position));
                    intent.putExtras(bundle);
                    startActivityForResult(intent,4);
                }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.title_leftimageview, R.id.tv_lishijilu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.tv_lishijilu:

                break;
        }
    }

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private List<MyInvestListBean> lslb = new ArrayList<MyInvestListBean>();
    private List<MyInvestListBean> lslbs = new ArrayList<MyInvestListBean>();

    private void getData() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post()
                .url(UrlConfig.MYINVESTDAISINFO)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("status", "4")
                .addParams("pageSize", "10")
                .addParams("pageOn", page + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                ptrDo.refreshComplete();
                dismissDialog();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    dismissDialog();
                    JSONObject objmap = obj.getJSONObject("map");
                    double principal = objmap.getDoubleValue("principal");//本金总额
                    double interest = objmap.getDoubleValue("interest");//利息总额
                    tvZichan.setText(stringCut.getNumKb(principal+interest));
                    tvBenjin.setText(stringCut.getNumKb(principal));
                    tvZonglixi.setText(stringCut.getNumKb(interest));

                    JSONObject objpage = objmap.getJSONObject("page");
                    JSONArray objrows = objpage.getJSONArray("rows");
                    lslbs = JSON.parseArray(objrows.toJSONString(), MyInvestListBean.class);
                    if (lslbs.size() > 0) {
                        if (page > 1) {
                            lslb.addAll(lslbs);
                        } else {
                            lslb = lslbs;
                        }
                        if (adapter == null) {
                            adapter = new InvestmentAdapter(MyInvestmentActivity.this, lslb);
                            lvTouzi.setAdapter(adapter);
                        } else {
                            adapter.onDateChange(lslb);
                        }
                        loadComplete();
                        page++;
                        if (lslbs.size() == 10) {
                            tv_footer.setText("上拉加载更多");
                            footerlayout.setVisibility(View.VISIBLE);
                            pb.setVisibility(View.GONE);
                        } else {
                            tv_footer.setText("全部加载完毕");
                            footerlayout.setVisibility(View.VISIBLE);
                            pb.setVisibility(View.GONE);
                        }
                    } else {
                        if (page == 1) {
                            adapter = new InvestmentAdapter(MyInvestmentActivity.this, lslb);
                            lvTouzi.setAdapter(adapter);
                        }
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


    public void loadComplete(){
        isLoading = false;
        footerlayout.setVisibility(View.GONE);
    }
}
