package com.ekabao.oil.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.BaseRecyclerViewAdapter;
import com.ekabao.oil.adapter.OilCardBuyAdapter;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.OilOrdersbean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.builder.PostFormBuilder;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.me.MyOilCardBuyDetailsActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

/**
 * 易卡宝  App
 * 我的订单
 *
 * @time 2018/7/19 16:46
 * Created by lj on 2018/11/19 16:46.
 */

public class MeOilCardOrderFragment extends BaseFragment {


    @BindView(R.id.rv_news)
    RecyclerView rvNews;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;


    private List<OilOrdersbean> lslbs = new ArrayList<OilOrdersbean>();
  //  private OrderAdapter adapter;
    private OilCardBuyAdapter adapter;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private int status = 1;// 1：进行中 3：已结束
    private int type;//1：油卡 2：手机 3：直购
    //private String flag="-1";// //0 体验金 -1
    private static final int details = 10198;//订单详情

    public static MeOilCardOrderFragment newInstance(int type, int status) {

        Bundle args = new Bundle();

        MeOilCardOrderFragment fragment = new MeOilCardOrderFragment();
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
        return R.layout.fragment_recycleview;
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



        adapter = new OilCardBuyAdapter(rvNews, lslbs, R.layout.item_my_oilcard_buy);
        rvNews.setAdapter(adapter);
        getData();


        adapter.setonItemViewClickListener(new BaseRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onItemViewClick(BaseViewHolder view, int position) {
                startActivity(new Intent(getActivity(),
                                MyOilCardBuyDetailsActivity.class)
                                .putExtra("orderId", lslbs.get(position).getId() + "")
                        // .putExtra("type", type)
                );
            }
        });


        /*adapter = new OrderAdapter(rvNews, lslbs, R.layout.item_my_order, status);
        rvNews.setAdapter(adapter);
        getData();
        adapter.setonItemViewClickListener(new BaseRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onItemViewClick(BaseViewHolder view, int position) {
                startActivityForResult(new Intent(getActivity(), MyOrderDetailsActivity.class)
                                .putExtra("orderId", lslbs.get(position).getId() + "")
                                .putExtra("type", type)
                        , details);
            }
        });*/
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
        // TODO: 2018/11/8  preferences.getString("uid", "")
        LogUtils.e("订单列表type" + type + "status" + status);

        PostFormBuilder postFormBuilder = OkHttpUtils.post()
                .url(UrlConfig.myOrders)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("type", type + "")
                //.addParams("status", status + "")
                .addParams("version", UrlConfig.version);

      //  "已完成(1 3 5)"
        if (status == 99 ||status == 1) {
            //全部
        } else {
            postFormBuilder.addParams("status", status + "");
        }

        postFormBuilder
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                LogUtils.e("订单列表" + result);
                //ptr_conponsunuse.refreshComplete();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONArray objrows = objmap.getJSONArray("myOrderList");
                    List<OilOrdersbean> couponsBeans = JSON.parseArray(objrows.toJSONString(), OilOrdersbean.class);

                    //版本兼容
//					for (int i = 0; i < lslbs.size(); i++) {
//						if(lslbs.get(i).getType()!=4&lslbs.get(i).getType()!=1&lslbs.get(i).getType()!=2&lslbs.get(i).getType()!=3){
//							lslbs.remove(i);
//							i--;
//						}
//					}
                    if (couponsBeans.size() > 0) {
                        rvNews.setVisibility(View.VISIBLE);
                        llEmpty.setVisibility(View.GONE);

                        lslbs.clear();
                        if (status==1){
                            //  "已完成(1 3 5)"
                            for (int i = 0; i < couponsBeans.size(); i++) {
                                int status = couponsBeans.get(i).getStatus();
                                if (status ==1||status ==3||status ==5)

                                    lslbs.add(couponsBeans.get(i));
                            }
                        }else {
                            lslbs.addAll(couponsBeans);
                        }

                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }

                    } else {
                        rvNews.setVisibility(View.GONE);
                        llEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText("暂无数据");
                        lslbs.clear();

                    }
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    getActivity().finish();
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
