package com.ekabao.oil.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.BaseRecyclerViewAdapter;
import com.ekabao.oil.adapter.MallOrderAdapter;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.GoodsOrderList;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.MallHomeActivity;
import com.ekabao.oil.ui.activity.me.MallOrderDetailsActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

/**
 * 易卡宝  App
 * 商品订单
 *
 * @time 2018/7/19 16:46
 * Created by lj on 2018/11/19 16:46.
 */

public class MeMallOrderFragment extends BaseFragment {


    @BindView(R.id.rv_news)
    RecyclerView rvNews;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.bt_empty)
    Button btEmpty;
    Unbinder unbinder;


    private List<GoodsOrderList> lslbs = new ArrayList<GoodsOrderList>();
    private MallOrderAdapter adapter;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private int type = 1;  //"0待付款", "1待发货", "5待收货", "3已完成", "2已取消

    private int status;//
    //private String flag="-1";// //0 体验金 -1
    private static final int details = 10168; //选择优惠券

    public static MeMallOrderFragment newInstance(int type, int status) {

        Bundle args = new Bundle();

        MeMallOrderFragment fragment = new MeMallOrderFragment();
        args.putInt("type", type);
        args.putInt("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            type = args.getInt("type");
            status = args.getInt("status");
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycleview_mall;
    }

    @Override
    protected void initParams() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocalApplication.getInstance());
        rvNews.setLayoutManager(linearLayoutManager);

        //status = type;

        /*if (type == 1) {
            status = "1";
        } else {
            //flag="0";
            status = "3";
        }*/


        adapter = new MallOrderAdapter(getActivity(), rvNews, lslbs, R.layout.item_mall_order, status);
        rvNews.setAdapter(adapter);
        getData();
        adapter.setonItemViewClickListener(new BaseRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onItemViewClick(BaseViewHolder view, int position) {

                startActivityForResult(new Intent(getActivity(), MallOrderDetailsActivity.class)
                        .putExtra("orderId", lslbs.get(position).getOrderid())
                        .putExtra("type", type), details
                );
            }
        });

        btEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), MallHomeActivity.class)
                        //  .putExtra("orderId", lslbs.get(position).getId() + "")
                        //.putExtra("type", type)
                );
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("订单刷新页面" + requestCode + "resultCode" + resultCode);

        if (requestCode == details && resultCode == RESULT_OK) {

            getData();

        }


    }

    private void getData() {

        LogUtils.e("订单列表type" + type + "status" + status);
        // TODO: 2019/1/10
        OkHttpUtils.post()
                .url(UrlConfig.shoporderList)
                .addParams("uid", preferences.getString("uid", ""))
                //  .addParams("type", type + "")
                // .addParams("status", status + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                LogUtils.e("订单列表" + result);
                //ptr_conponsunuse.refreshComplete();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {

                    JSONObject objmap = obj.getJSONObject("map");

                    JSONArray objrows = objmap.getJSONArray("orderlist");

                    List<GoodsOrderList> couponsBeans = JSON.parseArray(objrows.toJSONString(), GoodsOrderList.class);


                    if (couponsBeans.size() > 0) {
                        lslbs.clear();
                        for (GoodsOrderList bean : couponsBeans) {
                            if (bean.getStatus() == type) {
                                lslbs.add(bean);
                            }
                        }
                        if (lslbs.size() > 0) {
                            rvNews.setVisibility(View.VISIBLE);
                            llEmpty.setVisibility(View.GONE);

                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                        }else {
                            rvNews.setVisibility(View.GONE);
                            llEmpty.setVisibility(View.VISIBLE);
                            tvEmpty.setText("暂无记录，空空如也...");
                        }

                    } else {
                        rvNews.setVisibility(View.GONE);
                        llEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText("暂无记录，空空如也...");
                        lslbs.clear();

                    }

                } else if ("9998".equals(obj.getString("errorCode"))) {
                    //   getActivity().finish();
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


   /* @Override
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
    }*/
}
