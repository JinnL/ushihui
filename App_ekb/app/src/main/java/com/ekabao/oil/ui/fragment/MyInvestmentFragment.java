package com.ekabao.oil.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.BaseRecyclerViewAdapter;
import com.ekabao.oil.adapter.MyInvestmentAdapter;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.MyInvestListBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.me.MyInvestDetailsActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * $desc$
 *
 * @time $data$ $time$
 * Created by Administrator on 2018/7/16.
 */

public class MyInvestmentFragment extends BaseFragment {

    @BindView(R.id.rv_news)
    RecyclerView rvNews;
    Unbinder unbinder;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;

    private int type;//
    private int page = 1;
    private String statuses = "0";
    private List<MyInvestListBean> rows_List = new ArrayList<MyInvestListBean>();// 每次加载的数据
    private MyInvestmentAdapter newsAdapter;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private List<MyInvestListBean> lslb = new ArrayList<MyInvestListBean>();

    public static MyInvestmentFragment newInstance(int type) {

        Bundle args = new Bundle();

        MyInvestmentFragment fragment = new MyInvestmentFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            type = args.getInt("type");
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycleview;
    }

    @Override
    protected void initParams() {
        newsAdapter = new MyInvestmentAdapter(rvNews, rows_List, R.layout.item_me_investment);
        rvNews.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvNews.setAdapter(newsAdapter);

        getData(1, type);


        newsAdapter.setonItemViewClickListener(new BaseRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onItemViewClick(BaseViewHolder view, int position) {

                Intent intent = new Intent(getActivity(), MyInvestDetailsActivity.class);
                intent.putExtra("investId", rows_List.get(position).getId());
                intent.putExtra("type", rows_List.get(position).getType());
                startActivityForResult(intent, 4);

            }
        });
    }

    private void getData(int cur_page, int flag) {
        page = cur_page;
        showWaitDialog("加载中...", false, "");
        if (flag == 0) {
            statuses = "4";
        }
        if (flag == 1) {
            statuses = "3";
        }
        LogUtils.e("statuses" + statuses + "/" + flag);
        OkHttpUtils.post()
                .url(UrlConfig.MYINVESTDAISINFO)
                .addParams("uid", preferences.getString("uid", ""))
                //.addParams("uid","5")
                .addParams("status", statuses)

                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                //LogPrintUtil.e("LF--->我的出借", result);
                LogUtils.i("--->我的出借 result：" + result);
                //ptrDo.refreshComplete();
                dismissDialog();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    dismissDialog();
                    JSONObject objmap = obj.getJSONObject("map");
                    double tenderSum = objmap.getDoubleValue("tenderSum");//代收金额
                    double accumulatedIncome = objmap.getDoubleValue("accumulatedIncome");//累计收益
                    double principal = objmap.getDoubleValue("principal");//出借总额

                    JSONObject objpage = objmap.getJSONObject("page");
                    JSONArray objrows = objpage.getJSONArray("rows");
                    lslb = JSON.parseArray(objrows.toJSONString(), MyInvestListBean.class);

                    if (lslb.size() > 0) {

                        rows_List.addAll(lslb);
                        LogUtils.e("rows_List" + rows_List.size());
                        newsAdapter.notifyDataSetChanged();

                        rvNews.setVisibility(View.VISIBLE);
                        llEmpty.setVisibility(View.GONE);
                    } else {
                        rvNews.setVisibility(View.GONE);
                        llEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText("暂无数据");
                        rows_List.clear();

                    }
                    /* tvBenjin.setText(stringCut.getNumKb(tenderSum));
                    tvTotal.setText(stringCut.getNumKb(principal));
                    tvZonglixi.setText(stringCut.getNumKb(accumulatedIncome));






                    if (page > 1) {
                        lslb.addAll(lslbs);
                    } else {
                        lslb = lslbs;
                    }
                    if (lslb.size() == 0){
                        lslb = lslbs;
                    }
                    //LogUtils.i("--->我的出借：lslb=" + lslb);
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
                    }*/
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    //finish();
//					new show_Dialog_IsLogin(getActivity()).show_Is_Login() ;
                } else {
                    ToastMaker.showShortToast("服务器异常");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                //ptrDo.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
