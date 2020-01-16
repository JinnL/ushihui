package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.mcz.xhj.yz.dr_adapter.AdAdapter;
import com.mcz.xhj.yz.dr_adapter.InvestAdapter;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.Add_Bean;
import com.mcz.xhj.yz.dr_bean.BannerBean;
import com.mcz.xhj.yz.dr_bean.InvestListBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
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

import static com.mcz.xhj.R.id.lv_invest;
import static com.mcz.xhj.R.id.ptr_invest;


/**
 * 项目名称：JsAppAs2.0
 * 类描述：金服二级列表(已售罄已还款)
 * 创建人：shuc
 * 创建时间：2017/4/12 9:50
 * 修改人：DELL
 * 修改时间：2017/4/12 9:50
 * 修改备注：
 */
public class InvestTwoLeveListActivity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;

    @BindView(lv_invest)
    ListView lvInvest;
    @BindView(ptr_invest)
    PtrClassicFrameLayout ptrInvest;
    @BindView(R.id.title_sellout)
    TextView titleSellout;
    @BindView(R.id.img_sellout)
    ImageView imgSellout;
    @BindView(R.id.ll_sellout)
    LinearLayout llSellout;
    @BindView(R.id.title_repayed)
    TextView titleRepayed;
    @BindView(R.id.img_repayed)
    ImageView imgRepayed;
    @BindView(R.id.ll_repayed)
    LinearLayout llRepayed;

    private LinearLayout rl_neww;
    private TextView tv_rate_new;
    private boolean isLastitem = false;
    private boolean isLoading;
    private View footer;
    private LinearLayout footerlayout;
    private ProgressBar pb;
    private TextView tv_footer;
    private String type; //1=活动标，2=优选金服
    private String title;
    private String statuses = "6,8";//5=募集中 6=募集成功 8=待还款 9=已还款
    private int flag = 0;//0=已售罄(6,8)，1==已还款(9)

    @Override
    protected int getLayoutId() {
        return R.layout.invest_twolevel;
    }

    @Override
    protected void initParams() {
        type = getIntent().getStringExtra("type");
        if ("2".equalsIgnoreCase(type)) {

        } else {
            title = getIntent().getStringExtra("title");

        }
        footer = View.inflate(this, R.layout.footer_layout, null);
        footerlayout = (LinearLayout) footer.findViewById(R.id.load_layout);
        pb = (ProgressBar) footer.findViewById(R.id.pb);
        tv_footer = (TextView) footer.findViewById(R.id.tv_footer);
        footerlayout.setVisibility(View.GONE);
        lvInvest.addFooterView(footer, null, false);
        lvInvest.setOnScrollListener(new AbsListView.OnScrollListener() {

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
                        pageanxuan++;
                        getData(pageanxuan, type,flag);
                    }
                }
            }
        });

        ptrInvest.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if ("1".equalsIgnoreCase(type)) {

                }
                pageanxuan = 1;
                getData(1, type,flag);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame,
                                             View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lvInvest, header);
            }
        });

        getData(1, type,flag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4 && resultCode == 4) {
            setResult(4);
            finish();
        }
    }

    @OnClick({R.id.title_leftimageview,R.id.ll_sellout,R.id.ll_repayed})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.ll_sellout://已售罄
                if(flag == 0){
                    return;
                }
                flag = 0;
                titleSellout.setTextColor(0XFFFF8347);
                imgSellout.setVisibility(View.VISIBLE);
                titleRepayed.setTextColor(0XFF333333);
                imgRepayed.setVisibility(View.GONE);
                getData(1, type,flag);
                break;
            case R.id.ll_repayed://已还款
                if(flag == 1){
                    return;
                }
                flag = 1;
                titleSellout.setTextColor(0XFF333333);
                imgSellout.setVisibility(View.GONE);
                titleRepayed.setTextColor(0XFFFF8347);
                imgRepayed.setVisibility(View.VISIBLE);
                getData(1, type,flag);
                break;
        }
    }

    private int currentItem = 0;// 当前图片的索引
    private ImageView[] tips;


    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private List<InvestListBean> lslb2 = new ArrayList<InvestListBean>();
    private List<InvestListBean> mlslb2 = new ArrayList<InvestListBean>();// 每次加载的数据
    private InvestAdapter adapter;
    private int pageanxuan = 1;
    private int pageconfig = 1;

    private void getData(int page, String type,final int flag) {
        pageconfig = page;
        pageanxuan = page;

        if(flag==0){
            statuses = "6,8";
        }
        if(flag==1){
            statuses = "9";
        }

        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.PRODUCTLISTINFO)
                .addParams("type", type)
                .addParams("pageSize", "10")
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("pageOn", pageconfig + "")
                .addParams("statuses", statuses)//5=募集中 6=募集成功 8=待还款 9=已还款
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        //LogPrintUtil.e("LF--->售罄金服二级列表：",response);
                        isLoading = true;
                        dismissDialog();
                        if (pageanxuan == 1 || pageconfig == 1) {//防止双radiobutton同时作用
                            lslb2.clear();
                        }
                        ptrInvest.refreshComplete();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean("success")) {
                            JSONObject objmap = obj.getJSONObject("map");
                            JSONObject objpage = objmap.getJSONObject("page");

                            JSONArray arr = objpage.getJSONArray("rows");
                            mlslb2 = JSON.parseArray(arr.toJSONString(), InvestListBean.class);

                            lslb2.addAll(mlslb2);
                            if (adapter == null) {
                                adapter = new InvestAdapter(InvestTwoLeveListActivity.this, lslb2,flag);
                                lvInvest.setAdapter(adapter);
                            } else {
                                if (pageanxuan == 1) {
                                    adapter = new InvestAdapter(InvestTwoLeveListActivity.this, lslb2,flag);
                                    lvInvest.setAdapter(adapter);
                                } else {
                                    adapter.onDateChange(lslb2);
                                }
                            }
                            if (mlslb2.size() == 10) {
                                loadComplete();
                            } else {
                                tv_footer.setText("全部加载完毕");
                                footerlayout.setVisibility(View.VISIBLE);
                                pb.setVisibility(View.GONE);
                            }


                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {

                    }
                });
    }

    public void loadComplete() {
        isLoading = false;
        footerlayout.setVisibility(View.GONE);
    }


    /*
     * 获取首页数据
     */
    List<BannerBean> lsad = new ArrayList<BannerBean>();
    private List<Add_Bean> lsAd = new ArrayList<>();
    private AdAdapter addAdapter;
    private String pid = null;


}
