package com.ekabao.oil.ui.activity.me;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.GoodsOrderDetails;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.activity.OilCardPayActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.Arith;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class MallOrderDetailsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.tv_address_name)
    TextView tvAddressName;
    @BindView(R.id.tv_address_details)
    TextView tvAddressDetails;
    @BindView(R.id.iv_goods)
    ImageView ivGoods;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.tv_goods_num)
    TextView tvGoodsNum;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_orderid)
    TextView tvOrderid;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.bt_delete)
    Button btDelete;
    @BindView(R.id.tv_express)
    TextView tvExpress;
    @BindView(R.id.bt_copy)
    Button btCopy;
    @BindView(R.id.rl_express)
    RelativeLayout rlExpress;
    @BindView(R.id.tv_specification)
    TextView tvSpecification;
    @BindView(R.id.tv_pay_time)
    TextView tvPayTime;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.tv_interest_name)
    TextView tvInterestName;
    @BindView(R.id.tv_interest)
    TextView tvInterest;
    @BindView(R.id.ll_interest)
    LinearLayout llInterest;
    @BindView(R.id.tv_amount_name)
    TextView tvAmountName;


    private int type = 1;  //"0待付款", "1待发货", "5待收货", "3已完成", "2已取消

    private int orderId;
    private int pid;
    private int addressid;

    private double amount; //总金额
    private double factAmount; //剩余待支付金额
    private double interest; //余额支付金额

    private int number; //数量

    private String express;//快递单号
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_mall_order_details);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mall_order_details;
    }

    @Override
    protected void initParams() {
        Intent intent = getIntent();

        orderId = intent.getIntExtra("orderId", 0);
        type = intent.getIntExtra("type", 0);

        //"0待付款", "1待发货", "5待收货", "3已完成", "2已取消

        getData();


        int bg_background = R.drawable.bg_mall_order_details_0;
        switch (type) {
            case 0:
                bg_background = R.drawable.bg_mall_order_details_0;

                btDelete.setText("取消订单");
                btSure.setVisibility(View.VISIBLE);
                btSure.setText("立即支付");

                break;
            case 1:
                bg_background = R.drawable.bg_mall_order_details_1;

                btDelete.setVisibility(View.GONE);

                break;
            case 5:
                bg_background = R.drawable.bg_mall_order_details_5;

                btDelete.setText("确认收货");

                break;
            case 3:
                bg_background = R.drawable.bg_mall_order_details_3;

                btDelete.setVisibility(View.GONE);
                break;
            case 2:
                bg_background = R.drawable.bg_mall_order_details_2;

                btDelete.setText("删除订单");

                break;
        }
        rlTop.setBackgroundResource(bg_background);


        //
    }

    @OnClick({R.id.iv_back, R.id.bt_copy, R.id.bt_delete, R.id.bt_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                // cm.setText(express);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("快递单号", express);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.showToast("已复制到剪贴板");
                break;
            case R.id.bt_delete:
                //"0待付款", "1待发货", "5待收货", "3已完成", "2已取消

                //0:取消订单  1:删除订单 2:确定收货
                int status = 0;
                String title = "";
                switch (type) {
                    case 0:
                        status = 0;
                        title = "取消订单";
                        break;

                    case 5:
                        status = 2;
                        title = "确认收货";
                        break;
                    case 2:
                        status = 1;
                        title = "删除订单";
                        break;
                }
                setDialog(title, status);

                break;
            case R.id.bt_sure:

                LogUtils.e("pid" + pid);
                startActivity(new Intent(this, OilCardPayActivity.class)
                        .putExtra("uid", preferences.getString("uid", ""))
                        .putExtra("amount", factAmount)  //油卡id
                        .putExtra("number", number) //单笔金额
                        //   .putExtra("bz", bz)//备注
                        .putExtra("pid", pid) //产品id
                        // TODO: 2019/1/14  
                        .putExtra("addressid", addressid)  //地址主键
                        .putExtra("activitytype", 4)// 1：油卡 2：手机 3：直购  4
                        .putExtra("shoporderId", orderId)//

                );
                setResult(Activity.RESULT_OK);
                finish();

                break;
        }

    }

    private void setDialog(final String title, final int status) {
        DialogMaker.showRedSureDialog(this, title, "是否" + title + "？", "取消", "确定", new DialogMaker.DialogCallBack() {
            @Override
            public void onButtonClicked(Dialog dialog, int position, Object tag) {
                setData(status, title);
            }

            @Override
            public void onCancelDialog(Dialog dialog, Object tag) {

            }
        }, "");
    }


    private void getData() {

        LogUtils.e("订单詳情" + orderId);
        OkHttpUtils.post()
                .url(UrlConfig.shoporderDetail)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("id", orderId + "")
                //  .addParams("type", type + "")
                // .addParams("status", status + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                LogUtils.e("订单詳情" + result);
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {

                    JSONObject objmap = obj.getJSONObject("map");
                    //GoodsOrderDetails

                    GoodsOrderDetails bean = JSON.parseObject(objmap.toJSONString(), GoodsOrderDetails.class);

                    GoodsOrderDetails.OrderDetailBean orderDetail = bean.getOrderDetail();

                    pid = orderDetail.getPid();

                    addressid = orderDetail.getFuelId();

                    //"0待付款", "1待发货", "5待收货", "3已完成", "2已取消
                    int bg_background = R.drawable.bg_mall_order_details_0;


                    if (orderDetail.getAudittime() != 0) {
                        tvPayTime.setVisibility(View.VISIBLE);

                        if (orderDetail.getStatus() == 2) {
                            tvPayTime.setText("取消时间:           " + StringCut.getDateTimeToStringheng(orderDetail.getAudittime()));

                        } else {
                            tvPayTime.setText("支付时间:           " + StringCut.getDateTimeToStringheng(orderDetail.getAudittime()));
                        }
                    } else {
                        tvPayTime.setVisibility(View.GONE);
                    }

                    rlExpress.setVisibility(View.GONE);

                    if (orderDetail.getInterest() != 0) {

                        llInterest.setVisibility(View.VISIBLE);
                        interest = orderDetail.getInterest();
                        tvInterest.setText("-¥" + interest);
                        // TODO: 2019/1/24
                       // allAmount = Arith.add(allAmount, orderDetail.getInterest());

                    } else {
                        llInterest.setVisibility(View.GONE);
                    }
                    //"0待付款", "1待发货", "5待收货", "3已完成", "2已取消

                    switch (orderDetail.getStatus()) {
                        case 0:
                            bg_background = R.drawable.bg_mall_order_details_0;
                            btDelete.setText("取消订单");
                            btSure.setVisibility(View.VISIBLE);
                            btSure.setText("立即支付");

                            tvAmountName.setText("剩余待支付");
                           // tvMoney.setText("¥" + orderDetail.getFactAmount());
                            tvMoney.setText("￥" + StringCut.getNumKb(orderDetail.getFactAmount()));

                            break;
                        case 1:
                            bg_background = R.drawable.bg_mall_order_details_1;

                            btDelete.setVisibility(View.GONE);

                            llInterest.setVisibility(View.GONE);
                            tvAmountName.setText("支付金额");
                            tvMoney.setText("￥" + StringCut.getNumKb(orderDetail.getAmount()));

                            break;
                        case 5:
                            bg_background = R.drawable.bg_mall_order_details_5;
                            rlExpress.setVisibility(View.VISIBLE);
                            tvExpress.setText(orderDetail.getTrackingName() + "  " + orderDetail.getTrackingNumber());
                            btDelete.setText("确认收货");

                            llInterest.setVisibility(View.GONE);
                            tvAmountName.setText("支付金额");
                            tvMoney.setText("￥" + StringCut.getNumKb(orderDetail.getAmount()));

                            break;
                        case 3:
                            bg_background = R.drawable.bg_mall_order_details_3;
                            rlExpress.setVisibility(View.VISIBLE);
                            tvExpress.setText(orderDetail.getTrackingName() + "  " + orderDetail.getTrackingNumber());
                            btDelete.setVisibility(View.GONE);

                            llInterest.setVisibility(View.GONE);
                            tvAmountName.setText("支付金额");
                            tvMoney.setText("￥" + StringCut.getNumKb(orderDetail.getAmount()));

                            break;
                        case 2:
                            bg_background = R.drawable.bg_mall_order_details_2;
                            btDelete.setText("删除订单");


                            if (orderDetail.getInterest() != 0) {
                                llInterest.setVisibility(View.VISIBLE);
                                interest = orderDetail.getInterest();

                                tvInterest.setText(Html.fromHtml("-¥" + interest + "<font color='#444444'>(已退款)</font>"));


                            } else {
                                llInterest.setVisibility(View.GONE);
                            }


                            tvAmountName.setText("剩余待支付");
                            // tvMoney.setText("¥" + orderDetail.getFactAmount());
                            tvMoney.setText("￥" + StringCut.getNumKb(orderDetail.getFactAmount()));

                            break;
                    }
                    rlTop.setBackgroundResource(bg_background);

                    express = orderDetail.getTrackingNumber();

                    Glide.with(MallOrderDetailsActivity.this)
                            .load(orderDetail.getImages())
                            .error(R.drawable.bg_home_banner_fail)
                            .centerCrop()
                            .into(ivGoods);

                    tvGoodsName.setText(orderDetail.getGoodname());
                    tvGoodsPrice.setText("￥" + orderDetail.getRetail_price());

                    tvGoodsNum.setText("x" + orderDetail.getNumber());
                    double mul = Arith.mul(orderDetail.getRetail_price(), orderDetail.getNumber());

                    double sub = Arith.sub(mul, orderDetail.getAmount());

                    tvCoupon.setText("￥" + StringCut.getNumKb(sub)); //专属优惠:



                    amount = orderDetail.getAmount();

                    factAmount = orderDetail.getFactAmount();


                    number = orderDetail.getNumber();

                    tvOrderid.setText("订单编号:           " + orderDetail.getPaynum());
                    tvTime.setText("创建时间:           " + StringCut.getDateTimeToStringheng(orderDetail.getInvestTime()));

                    tvAddressName.setText(orderDetail.getReceiveName() + "  " + orderDetail.getReceivePhone());
                    tvAddressDetails.setText(orderDetail.getAddress());

                    tvSpecification.setText(orderDetail.getValue());


                } else if ("9998".equals(obj.getString("errorCode"))) {
                    //   getActivity().finish();
                    //new show_Dialog_IsLogin(getActivity()).show_Is_Login() ;
                    //1100订单Id不能为空
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

    private void setData(int status, final String title) {

        showWaitDialog("请稍后...", false, "");
        //0:取消订单  1:删除订单 2:确定收货
        OkHttpUtils.post()
                .url(UrlConfig.shoporderupdate)

                .addParams("uid", preferences.getString("uid", ""))
                .addParams("id", orderId + "")
                //  .addParams("type", type + "")
                .addParams("status", status + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                dismissDialog();
                LogUtils.e("7.订单详情修改" + result);
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {

                    JSONObject objmap = obj.getJSONObject("map");
                    ToastMaker.showShortToast(title + "成功");

                    setResult(Activity.RESULT_OK);

                    finish();


                } else if ("9999".equals(obj.getString("errorCode"))) {

                    ToastMaker.showShortToast("系统错误");
                    //   getActivity().finish();
                    //new show_Dialog_IsLogin(getActivity()).show_Is_Login() ;
                    //1100订单Id不能为空
                } else {
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


}
