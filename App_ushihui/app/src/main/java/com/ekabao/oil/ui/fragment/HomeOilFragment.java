package com.ekabao.oil.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.HomeBannerAdapter;
import com.ekabao.oil.adapter.HomeBtnAdapter;
import com.ekabao.oil.adapter.HomeDiscountAdapter;
import com.ekabao.oil.adapter.HomeOilCardAdapter;
import com.ekabao.oil.bean.Add_Bean;
import com.ekabao.oil.bean.HomeBannerBean;
import com.ekabao.oil.bean.HomeHostProduct;
import com.ekabao.oil.bean.HomeInfoList;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.LoginActivity;
import com.ekabao.oil.ui.activity.MainActivity;
import com.ekabao.oil.ui.activity.OilCardBuyActivity;
import com.ekabao.oil.ui.activity.PhoneRechargeActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.activity.me.CustomerServiceActivity;
import com.ekabao.oil.ui.view.MarqueeView;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.ui.view.recyclerview.BaseQuickAdapter;
import com.ekabao.oil.util.LogUtils;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.IconHintView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.Unbinder;
import okhttp3.Call;

/***
 * 油实惠主页
 * author: tonglj
 * date:2020/1/9
 */
public class HomeOilFragment extends BaseFragment implements View.OnClickListener {

    //布局刷新控件
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    //banner
    @BindView(R.id.rpv_banner)
    RollPagerView rpvBanner;

    @BindView(R.id.rv_btn)
    RecyclerView rvBtn;
    //公告
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    //
    @BindView(R.id.rv_card)
    RecyclerView rvCard;
    //
    @BindView(R.id.rv_discount)
    RecyclerView rvDiscount;
    //
    @BindView(R.id.iv_discount)
    ImageView ivDiscount;
    // 绑定
    Unbinder unbinder;


    // 缓存处理
    private SharedPreferences preferences = LocalApplication.sharereferences;
    // 实体油卡
    private HomeOilCardAdapter mHomeOilCardAdapter;
    //banner 下面的按钮
    private HomeBtnAdapter mHomeBtnAdapter;
    //banner
    private HomeBannerAdapter mHomeBannerAdapter;
    //banner 数据源
    private List<HomeBannerBean> banners;
    //加油福利
    private HomeDiscountAdapter mHomeDiscountAdapter;

    public static HomeOilFragment instance() {
        HomeOilFragment view = new HomeOilFragment();
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_oil;
    }

    @Override
    protected void initParams() {
        initRecyclerView();
//        Glide.with(mContext)
//                .load("https://m.ekabao.cn/upload/banner/2019-12/201912193b46e288-6037-4dad-8f08-d6354e5483fd.jpg")
//                .dontAnimate()
//                .into(ivTest);
    }


    private void initRecyclerView() {
        mHomeOilCardAdapter = new HomeOilCardAdapter();
        mHomeDiscountAdapter = new HomeDiscountAdapter();
        mHomeBtnAdapter = new HomeBtnAdapter();
        rvCard.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mHomeOilCardAdapter.bindToRecyclerView(rvCard);
        mHomeOilCardAdapter.addData("");
        mHomeOilCardAdapter.addData("");
        mHomeOilCardAdapter.addData("");
        mHomeOilCardAdapter.addData("");
        mHomeOilCardAdapter.addData("");

        rvBtn.setLayoutManager(new GridLayoutManager(mContext, 4));
        mHomeBtnAdapter.bindToRecyclerView(rvBtn);


        banners = new ArrayList<>();
//        HomeBannerBean b =  new HomeBannerBean();
//        b.imgUrl = "https://m.ekabao.cn/upload/banner/2019-12/201912193b46e288-6037-4dad-8f08-d6354e5483fd.jpg";
//        banners.add(b);
//        banners.add(b);
//        banners.add(b);
        mHomeBannerAdapter = new HomeBannerAdapter(rpvBanner, mContext, banners);
        rpvBanner.setAdapter(mHomeBannerAdapter);
        rpvBanner.setPlayDelay(3000);
        rvDiscount.setLayoutManager(new GridLayoutManager(mContext, 2));


        rpvBanner.setHintView(new IconHintView(mContext, R.drawable.bg_banner_selected, R.drawable.bg_banner_unselected));
        rpvBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                HomeBannerBean bean = banners.get(position);
                if (bean.getLocation() == null || bean.getLocation().equalsIgnoreCase("")) {
                    return;
                }

                if (bean.getTitle().indexOf("注册送礼") != -1) {
                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("URL", bean.getLocation() + "&app=true")
                            .putExtra("TITLE", bean.getTitle())
                            .putExtra("HTM", "立即注册")
                            //  .putExtra("PID", pid)
                            .putExtra("BANNER", "banner")
                    );
                } else {

                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("URL", bean.getLocation() + "&app=true")
                            .putExtra("TITLE", bean.getTitle())
                            //  .putExtra("PID", pid)
                            .putExtra("BANNER", "banner")
                    );
                }

            }
        });

        getHomeInfo();
        getNotice();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getHomeInfo();
                getNotice();
            }
        });

        rvDiscount.setAdapter(mHomeDiscountAdapter);
        mHomeDiscountAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int money = 500;
                SharedPreferences.Editor edit = preferences.edit();
                edit.putInt("oid_pid", mHomeDiscountAdapter.getItem(position).getId());
                edit.putInt("oid_money", money);
                // edit.putInt("oid_pid",homeHostProduct.get(position).getId());
                edit.commit();
//                LogUtils.e("setOnCenterItemClickListener+" + homeHostProduct.get(position).getId());

                MainActivity activity = (MainActivity) getActivity();
                activity.switchFragment(1);
            }
        });

        ivDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                activity.switchFragment(1);
            }
        });
    }


    private void getHomeInfo() {
        OkHttpUtils.post().url(UrlConfig.HOMEINFO)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("toFrom", LocalApplication.getInstance().channelName)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("新的3.0的首页--->homeinfo" + response);
                        Log.d("adasd", response);
                        if (refreshLayout != null) {
                            RefreshState state = refreshLayout.getState();
                            if (state == RefreshState.Refreshing) {
                                refreshLayout.finishRefresh();
                            }
                        }

                        dismissDialog();

                        if (refreshLayout != null) {
                            RefreshState state = refreshLayout.getState();
                            if (state == RefreshState.Refreshing) {
                                refreshLayout.finishRefresh();
                            }
                        }

                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");


                            HomeInfoList homeInfo = JSON.toJavaObject(map, HomeInfoList.class);
                            JSONArray arr = map.getJSONArray("hostProduct");

                            if (arr != null) {

                                // 油卡充值套餐   新品上线
                                List<HomeHostProduct> homeHostProducts = JSON.parseArray(arr.toJSONString(), HomeHostProduct.class);

                                TreeSet<HomeHostProduct> ts = new TreeSet<>();
                                //2,将list集合中所有的元素添加到TrreSet集合中,对其排序,保留重复
                                //

                                ts.addAll(homeHostProducts);

                                mHomeDiscountAdapter.setNewData(homeHostProducts.subList(0,4));



                            }

//                            //轮播图
//                            mHomeBtnAdapter.setheaderbean(homeInfo);
                            banners.clear();
                            banners.addAll(homeInfo.getBanner());
                            mHomeBannerAdapter.notifyDataSetChanged();
//
//                            //分类
//                            homepagerRecycleAdapter.setCategoryBean((ArrayList<HomeInfoList.LogoListBean>) homeInfo.getLogoList());
                            mHomeBtnAdapter.setNewData((ArrayList<HomeInfoList.LogoListBean>) homeInfo.getLogoList());
                            mHomeBtnAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    String title = mHomeBtnAdapter.getItem(position).getTitle();
                                    if (TextUtils.equals(title, "油卡套餐")) {
                                        ((MainActivity) getActivity()).switchFragment(1);
                                    } else if (TextUtils.equals("领取油卡", title)) {
                                        mContext.startActivity(new Intent(mContext, OilCardBuyActivity.class)
                                                //  .putExtra("is_presell_plan", true)
                                                // .putExtra("startDate", isSellingList.get(0).getStartDate())
                                                //.putExtra("pid", bean1.getId())
                                                .putExtra("money", 1000));
                                    } else if (TextUtils.equals("手机充值", title)) {
                                        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                                            //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                                            // startActivity(new Intent(mContext, LoginActivity.class));
                                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                                        } else {
                                            mContext.startActivity(new Intent(mContext, PhoneRechargeActivity.class));
                                        }
                                    } else {
                                        mContext.startActivity(new Intent(mContext, CustomerServiceActivity.class));
                                    }
                                }
                            });

//                        } else if ("9999".equals(obj.getString("errorCode"))) {
//                            ToastMaker.showShortToast("系统异常");
//                        } else if ("9998".equals(obj.getString("errorCode"))) {
//                            ToastMaker.showShortToast("系统异常");
//                            new show_Dialog_IsLogin(MessageCenterActivity.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统异常");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");

                        RefreshState state = refreshLayout.getState();
                        if (state == RefreshState.Refreshing) {
                            refreshLayout.finishRefresh();
                        }
                    }
                });
    }


    /**
     * 通知
     */
    private void getNotice() {
        OkHttpUtils
                .post()
                .url(UrlConfig.NOTICE)
                .addParam("limit", "3")
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("--->NOTICE " + response);
                        dismissDialog();
                        Log.d("HOmeMallFragment", response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            dismissDialog();
                            JSONObject objmap = obj.getJSONObject("map");
                            if (objmap.isEmpty()) {
                                return;
                            }
                            //JSONArray objnotice = objmap.getJSONArray("urgentNotice");
                            // objmap.getJSONObject("page").getJSONArray("urgentNotice")
                            JSONArray objnotice = objmap.getJSONArray("urgentNotice");

                            if (objnotice != null) {
                                //  rlNotice.setVisibility(View.VISIBLE);

                                final List<Add_Bean> lsAd = JSON.parseArray(objnotice.toJSONString(), Add_Bean.class);

                                List<String> info = new ArrayList<>();
                                for (int i = 0; i < lsAd.size(); i++) {
                                    info.add(lsAd.get(i).getTitle().toString());
                                }
                                marqueeView.startWithList(info);


                                marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position, TextView textView) {
                                        LogUtils.e("marqueeView" + position);

                                        mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                                                .putExtra("URL", UrlConfig.WEBSITEAN + "?app=true&id=" + lsAd.get(position).getArti_id())
                                                .putExtra("TITLE", "平台公告"));
                                    }
                                });
                                marqueeView.setNotices(info);

                            } else {
                                // rlNotice.setVisibility(View.GONE);
                            }
                        } else {
                            //rlNotice.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
//				ToastMaker.showShortToast("请检查网络!");
                    }
                });

    }

}
