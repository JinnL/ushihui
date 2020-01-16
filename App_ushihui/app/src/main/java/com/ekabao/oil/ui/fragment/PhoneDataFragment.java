package com.ekabao.oil.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.OilCardPackageAdapter;
import com.ekabao.oil.adapter.SpaceItemDecoration;
import com.ekabao.oil.bean.CouponsBean;
import com.ekabao.oil.bean.OilCardPackageBean;
import com.ekabao.oil.bean.OilOrderDetailBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.OilCardPayActivity;
import com.ekabao.oil.ui.activity.me.MeWelfareActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.util.Arith;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * ${APP_NAME}  App_oil
 * 流量
 *
 * @time 2018/12/4 17:26
 * Created by lj on 2018/12/4 17:26.
 */

public class PhoneDataFragment extends BaseFragment {


    @BindView(R.id.ib_add)
    ImageButton ibAdd;
    @BindView(R.id.ib_reduce)
    ImageButton ibReduce;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.rl_add)
    LinearLayout rlAdd;
    @BindView(R.id.tv_money_type)
    TextView tvMoneyType;
    @BindView(R.id.rv_package)
    RecyclerView rvPackage;

    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.ll_month)
    RelativeLayout llMonth;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.ll_coupon)
    RelativeLayout llCoupon;
    @BindView(R.id.tv_one_month)
    TextView tvOneMonth;
    @BindView(R.id.tv_all_money)
    TextView tvAllMoney;
    @BindView(R.id.tv_cheaper)
    TextView tvDown;
    @BindView(R.id.bt_submit)
    Button btSubmit;



    private SharedPreferences preferences = LocalApplication.sharereferences;
    private String uid;
    private ArrayList<OilCardPackageBean> oilInfo = new ArrayList<>();
    private OilCardPackageAdapter adapterSlowOil;
    private OilCardPackageBean oilCardPackageBean;
    private List<CouponsBean> couponsList = new ArrayList<CouponsBean>();
    private ArrayList<Integer> couponsUsedList = new ArrayList<Integer>();
    private CouponsBean couponsBean; //优惠券
    Unbinder unbinder;
    private String received;//作为容器的对象
    private Boolean isPay = true;//作为容器的对象
    private String phoneNum;
    private int type = 3;
    private int monthMoney = 50;// 月充值金额
    private double allMoney;// 总金额
    private int coupons = 0;// 优惠券的数量
    private int fuelCardId;
    private static final int loginCode = 10158; //选择优惠券
    private int isUsed = 0;
    private Boolean isUseCoupons = false;
    private int allMoneyLast;


    public static PhoneDataFragment newInstance() {
        Bundle args = new Bundle();
        PhoneDataFragment fragment = new PhoneDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            //type = args.getInt("type");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_phone;
    }

    @Override
    protected void initParams() {
        rlAdd.setVisibility(View.GONE);
        tvMoneyType.setText("选择流量");
        llMonth.setVisibility(View.GONE);
       // viewLine.setVisibility(View.GONE);
        tvOneMonth.setVisibility(View.GONE);

        Drawable image = getResources().getDrawable(R.drawable.icon_phone_data);
        image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());//非常重要，必须设置，否则图片不会显示
        tvMoneyType.setCompoundDrawables(image, null, null, null);

        getPorductList();
        loadTCData();
        getCoupons();

    }


    @OnClick({R.id.ib_reduce, R.id.ib_add, R.id.ll_month, R.id.ll_coupon, R.id.bt_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_reduce:
                if (monthMoney <= 50) {
                    ToastUtil.showToast("最低充值50元");
                    break;
                } else {
                    monthMoney = monthMoney - 50;
                    tvMoney.setText(monthMoney + "");
                    if (oilCardPackageBean != null) {
                        showCouponsUsed();
                        if (isUseCoupons) {
                            if (allMoneyLast < couponsBean.getEnableAmount()) {

                                showMoneyLast();

                            } else {
                                tvCoupon.setText(couponsBean.getName());
                                showMoney();

                            }
                        } else {
                            showMoneyLast();
                        }

                    }
                }
                break;
            case R.id.ib_add:
                monthMoney = monthMoney + 50;
                tvMoney.setText(monthMoney + "");
                if (oilCardPackageBean != null) {
                    showCouponsUsed();

                    if (isUseCoupons) {

                        if (allMoneyLast >= couponsBean.getEnableAmount()) {
                            tvCoupon.setText(couponsBean.getName());
                            showMoney();

                        } else {
                            showMoneyLast();
                        }

                    } else {
                        showMoneyLast();
                    }
                }
                break;
            case R.id.ll_month:
                if (oilCardPackageBean != null) {
                    int deadline = oilCardPackageBean.getDeadline();

                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    List<OilOrderDetailBean.RechargeListBean> list = new ArrayList<OilOrderDetailBean.RechargeListBean>();
                    Date now = new Date();
                    for (int i = 0; i < deadline; i++) {
                        Date d;
                        d = now;
                        Calendar c = Calendar.getInstance();
                        c.setTime(d);
                        if (deadline == 1) {
                            c.add(Calendar.MONTH, 1);
                        } else {
                            c.add(Calendar.MONTH, i);
                        }
                        Date temp_date = c.getTime();
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
                        String format = sf.format(temp_date);
                        list.add(new OilOrderDetailBean.RechargeListBean(format, 0, monthMoney, 0));
                    }

                    DialogMaker.showMonthDialog(getActivity(), list);

                }
                break;
            case R.id.ll_coupon:
                if (isUsed == 0) {
                    ToastUtil.showToast("暂无优惠券");
                } else {
                    if (oilCardPackageBean != null) {
                        startActivityForResult(new Intent(getActivity(), MeWelfareActivity.class)
                                        .putExtra("type", 4)
                                        .putExtra("isFromPerson", false)
                                        .putExtra("isUsedList", allMoneyLast)
                                        .putExtra("oilListMonth", oilCardPackageBean.getDeadline())
                                , loginCode);
                    }
                    oilCardPackageBean.getDeadline();

                }
                break;
            case R.id.bt_submit:
                if (!TextUtils.isEmpty(getReceived())) {

                    if (getIsPay()) {
                        uid = preferences.getString("uid", "");
                        int fid = 0;
                        if (couponsBean != null) {
                            fid = couponsBean.getId();
                        }
                        startActivity(new Intent(getActivity(), OilCardPayActivity.class)
                                .putExtra("uid", uid)
                                .putExtra("fuelCardId", formatPhoneNum(getReceived()))  //油卡id
                                .putExtra("amount", allMoney) //金额
                                .putExtra("monthMoney", monthMoney)
                                .putExtra("pid", oilCardPackageBean.getId()) //产品id
                                .putExtra("fid", fid) //优惠券id
                        );
                    } else {
                        ToastUtil.showToast("无法查询该手机号,请联系客服");
                    }

                }
                break;
        }
    }

    /**
     * 去掉手机号内除数字外的所有字符
     *
     * @param phoneNum 手机号
     * @return
     */
    public String formatPhoneNum(String phoneNum) {
        String regex = "(\\+86)|[^0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.replaceAll("");
    }

    private void getPorductList() {

        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.SetMeal)
                .addParams("type", type + "") //1是套餐，2是即时
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("--->taoc通知：" + response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray arr = map.getJSONArray("list");

                            List<OilCardPackageBean> mrowsList = JSON.parseArray(arr.toJSONString(), OilCardPackageBean.class);

                            if (mrowsList.size() > 0) {

                                oilInfo.clear();
                                oilInfo.addAll(mrowsList);
                                adapterSlowOil.notifyDataSetChanged();
                                oilCardPackageBean = oilInfo.get(0);
                                tvMonth.setText(oilCardPackageBean.getDeadline() + "个月");

                                showCouponsUsed();
                                showMoney();


                            }


                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastUtil.showToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            ToastUtil.showToast("系统异常");
                        } else {
                            ToastUtil.showToast("系统异常");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastUtil.showToast("请检查网络");
                    }
                });
    }

    private void loadTCData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvPackage.setLayoutManager(gridLayoutManager);
        rvPackage.addItemDecoration(new SpaceItemDecoration(5, 5, 5, 5));
        adapterSlowOil = new OilCardPackageAdapter(oilInfo, 0, 2);
        rvPackage.setAdapter(adapterSlowOil);

        //套餐点击
        adapterSlowOil.setOnItemClickLitener(new OilCardPackageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int positon) {
                adapterSlowOil.setPosition(positon);
                adapterSlowOil.notifyDataSetChanged();

                oilCardPackageBean = oilInfo.get(positon);
                tvMonth.setText(oilCardPackageBean.getDeadline() + "个月");

                if (oilCardPackageBean.getDeadline() == 1) {
                    tvOneMonth.setVisibility(View.VISIBLE);
                } else {
                    tvOneMonth.setVisibility(View.GONE);
                }

                if (oilCardPackageBean != null) {
                    isUseCoupons = false;
                    showCouponsUsed();
                    showMoneyLast();
                }

            }
        });

    }

    public void showMoney() {

        if (oilCardPackageBean == null) {
            return;
        } else {
            double money = Arith.mul(monthMoney, oilCardPackageBean.getDeadline());
            double rate = oilCardPackageBean.getRate();

            double div = Arith.sub(1.0, rate);//减 折扣了

            double mul2 = Arith.mul(money, rate);//乘 总计
            allMoney = mul2;

            double mul = Arith.mul(money, div);//节省

            double couponsAmount = 0.0;
            if (couponsBean != null) {
                couponsAmount = couponsBean.getAmount();
            }

            double mul1 = Arith.add(mul, couponsAmount); //加,优惠

            allMoney = Arith.sub(mul2, couponsAmount); //减


            tvAllMoney.setText(StringCut.getNumKb(allMoney) + "");
            tvDown.setText(Html.fromHtml("(省<font color='#F7741C'>" + StringCut.getNumKb(mul1) + "</font>" + "元)"));

        }
    }

    public void showMoneyLast() {
        double money = Arith.mul(monthMoney, oilCardPackageBean.getDeadline());
        double rate = oilCardPackageBean.getRate();

        double div = Arith.sub(1.0, rate);//减 折扣了

        double mul2 = Arith.mul(money, rate);//乘 总计
        double mul = Arith.mul(money, div);//节省

        allMoney = mul2;
        tvAllMoney.setText(StringCut.getNumKb(allMoney) + "");

        tvDown.setText(Html.fromHtml("(省<font color='#F7741C'>" + StringCut.getNumKb(mul) + "</font>" + "元)"));


    }


    //展示多少张优惠券可用
    private void showCouponsUsed() {
        if (oilCardPackageBean == null) {
            return;
        }

        allMoneyLast = monthMoney * oilCardPackageBean.getDeadline();
        isUsed = 0;
        if (couponsList != null && couponsList.size() > 0) {
            for (int i = 0; i < couponsList.size(); i++) {

                if (allMoneyLast >= couponsList.get(i).getEnableAmount() && oilCardPackageBean.getDeadline() >= couponsList.get(i).getProductDeadline()) {
                    isUsed += 1;
                    couponsUsedList.add(couponsList.get(i).getId());
                }
            }
            tvCoupon.setText(isUsed + "张");
        } else {
            tvCoupon.setText(0 + "张");
        }

    }

    /**
     * 优惠券
     */
    private void getCoupons() {
        OkHttpUtils.post()
                .url(UrlConfig.CONPONSUNUSE)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("status", "0")
                .addParams("type", "4")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {

                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONArray objrows = objmap.getJSONArray("list");
                    List<CouponsBean> couponsBeans = JSON.parseArray(objrows.toJSONString(), CouponsBean.class);

                    if (couponsBeans.size() > 0) {

                        couponsList.clear();
                        couponsList.addAll(couponsBeans);
                        coupons = couponsBeans.size();
                        showCouponsUsed();
                    } else {
                        tvCoupon.setText("无");
                    }
                } else if ("9998".equals(obj.getString("errorCode"))) {
                } else {
                    ToastUtil.showToast("服务器异常");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("请检查网络");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == loginCode && resultCode == RESULT_OK) {

            int positionId = data.getIntExtra("position", 0);
            LogUtils.e("positionPay==" + positionId);
            isUseCoupons = true;
            if (couponsList.size() > 0) {
                for (int i = 0; i < couponsList.size(); i++) {
                    if (positionId == couponsList.get(i).getId()) {
                        couponsBean = couponsList.get(i);
                    }
                }

                tvCoupon.setText(couponsBean.getName());
                if (oilCardPackageBean != null) {
                    showMoney();
                }

            }
        } else if (requestCode == loginCode && resultCode == RESULT_CANCELED) {
            tvCoupon.setText(isUsed + "张");
            isUseCoupons = false;
            showMoneyLast();

        }


    }


    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public Boolean getIsPay() {
        return isPay;
    }

    public void setIsPay(Boolean isPay) {
        this.isPay = isPay;
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
