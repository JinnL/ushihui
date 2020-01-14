package com.ekabao.oil.ui.fragment;

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
import com.ekabao.oil.adapter.CouponsAdapter;
import com.ekabao.oil.adapter.TiyanjinAdapter;
import com.ekabao.oil.bean.CouponsBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.view.ToastMaker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 易卡宝  App
 *
 * @time 2018/7/19 16:46
 * Created by lj on 2018/7/19 16:46.
 */

public class MeCouponsFragment extends BaseFragment {

    @BindView(R.id.rv_news)
    RecyclerView rvNews;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.tv_history_coupons)
    TextView tvHistoryCoupons;
    @BindView(R.id.tv_help)
    TextView tvHelp;
    @BindView(R.id.tv_call_phone)
    TextView tvCallPhone;
    Unbinder unbinder;

    private int type;//
    private List<CouponsBean> lslbs = new ArrayList<CouponsBean>();
    private CouponsAdapter adapter;
    private TiyanjinAdapter tiyanjinAdapter;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private String status="0";// 状态 0：未使用  1：已使用 2：已过期
    private String flag="-1";// //0 体验金 -1

    public static MeCouponsFragment newInstance(int type) {

        Bundle args = new Bundle();

        MeCouponsFragment fragment = new MeCouponsFragment();
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
        return R.layout.fragment_me_coupons;
    }

    @Override
    protected void initParams() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocalApplication.getInstance());
        rvNews.setLayoutManager(linearLayoutManager);

        if (type == 1) {
            tvHistoryCoupons.setVisibility(View.VISIBLE);

            adapter = new CouponsAdapter(rvNews, lslbs, R.layout.item_coupons);
            rvNews.setAdapter(adapter);
            //flag="1";
            status="0";
        } else {
            tvHistoryCoupons.setVisibility(View.GONE);
            tiyanjinAdapter = new TiyanjinAdapter(rvNews, lslbs, R.layout.item_tiyanjin);
            rvNews.setAdapter(tiyanjinAdapter);
            //flag="0";
            status="2";
        }




        getData();


    }

    private void getData() {
        OkHttpUtils.post()
                .url(UrlConfig.CONPONSUNUSE)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("status", status)
                .addParams("flag", flag)   //0 体验金
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {

                //ptr_conponsunuse.refreshComplete();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONArray objrows = objmap.getJSONArray("list");
                    List<CouponsBean> couponsBeans = JSON.parseArray(objrows.toJSONString(), CouponsBean.class);

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

                        lslbs.addAll(couponsBeans);
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }


                       /* Iterator<CouponsBean> it = couponsBeans.iterator();

                        if (type == 1) {
                            LogUtils.e("--->我的红包(未使用)：" + result);
                            lslbs.clear();
                            for (CouponsBean cbean : couponsBeans) {
                                if (cbean.getType()!= 3) {
                                    lslbs.add(cbean);
                                }
                            }
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            LogUtils.e("--->优惠券：" + lslbs.size());

                        } else {
                            LogUtils.e("--->体验金：" + result);
                            lslbs.clear();
                            for (CouponsBean cbean : couponsBeans) {
                                if (cbean.getType()== 3) {
                                    lslbs.add(cbean);
                                }
                            }
                            if (tiyanjinAdapter != null) {
                                tiyanjinAdapter.notifyDataSetChanged();
                            }
                            LogUtils.e("--->体验金：" + lslbs.size());
                        }*/

                     /*   while (it.hasNext()) {
                            LogUtils.e("--->我的红包(未使用)1：" + lslbs.size());
                            if (!"3".equals(((CouponsBean) it.next()).getType())) {
                                // cbean = (CouponsBean) it.next();
                                //it.remove();
                                lslbs.add(it.next());
                                LogUtils.e("--->我的红包(未使用)2：" + it.next().getType());
                            }
                        }*/



                       /* if (adapter == null) {
                            adapter = new NewConponsAdapter(mContext, lslbs, "hongbao", new NewConponsAdapter.onButtonClickL() {

                                @Override
                                public void onButtonClick(View view) {
                                    LocalApplication.getInstance().getMainActivity().isInvest = true;
                                    LocalApplication.getInstance().getMainActivity().isInvestChecked = true;
                                    getActivity().finish();

                                }
                            });
                            lv_conpons.setAdapter(adapter);
                        } else {
                            adapter.onDateChange(lslbs);
                        }*/


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
