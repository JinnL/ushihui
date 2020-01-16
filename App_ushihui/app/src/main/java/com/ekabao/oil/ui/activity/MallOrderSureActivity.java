package com.ekabao.oil.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.AddressBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.me.AddAddressActivity;
import com.ekabao.oil.ui.activity.me.AddressManageActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.Arith;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class MallOrderSureActivity extends BaseActivity {


    @BindView(R.id.title_lefttextview)
    TextView titleLefttextview;
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_centerimageview)
    ImageView titleCenterimageview;
    @BindView(R.id.title_righttextview)
    TextView titleRighttextview;
    @BindView(R.id.title_rightimageview)
    ImageView titleRightimageview;
    @BindView(R.id.view_line_bottom)
    View viewLineBottom;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ib_add_address)
    ImageView ibAddAddress;
    @BindView(R.id.ll_add_address)
    LinearLayout llAddAddress;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_specification)
    TextView tvSpecification;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_retailPrice)
    TextView tvRetailPrice;
    @BindView(R.id.btn_sku_quantity_minus)
    TextView btnSkuQuantityMinus;
    @BindView(R.id.et_sku_quantity_input)
    EditText etSkuQuantityInput;
    @BindView(R.id.btn_sku_quantity_plus)
    TextView btnSkuQuantityPlus;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.tv_all_money)
    TextView tvAllMoney;
    @BindView(R.id.tv_cheaper)
    TextView tvCheaper;
    @BindView(R.id.bt_buy)
    Button btBuy;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.et_bz)
    EditText etBz;


    private String id; //有货的,具体规格的商品id
    private int goodsId;//商品的id

    private int quantity; //数量
    private int goodsNumber; //库存

    private SharedPreferences preferences = LocalApplication.sharereferences;
    private String uid;

    private int type = 2;// 1支付宝 、2微信支付、3云闪付

    private String listPicUrl; //图片
    private String name; //名字
    private String specification; //名字

    private int retailPrice; //单价
    private double money; //单价

    private static final int addAddres = 16540; //去添加地址
    private AddressBean address;//收货地址
    private static final int login = 16541; //登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_mall_order_sure);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mall_order_sure;
    }

    @Override
    protected void initParams() {

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        quantity = intent.getIntExtra("quantity", 0);
        goodsNumber = intent.getIntExtra("goodsNumber", 0);

        goodsId = intent.getIntExtra("goodsId", 0);
        // TODO: 2019/1/10 double
        retailPrice = intent.getIntExtra("retailPrice", 0);

        specification = intent.getStringExtra("specification");
        listPicUrl = intent.getStringExtra("listPicUrl");
        name = intent.getStringExtra("name");
        uid = preferences.getString("uid", "");


        tvTitle.setText(name);

        tvSpecification.setText(specification);

        tvRetailPrice.setText("￥" + retailPrice);

        tvNum.setText("x" + quantity);

        showMoney();


        Glide.with(this)
                .load(listPicUrl)
                .placeholder(R.drawable.bg_activity_fail)
                .error(R.drawable.bg_activity_fail)
                .into(ivPhoto);

        getReceiptAddress();

        etSkuQuantityInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != EditorInfo.IME_ACTION_DONE) {
                    return false;
                }
                String etquantity = etSkuQuantityInput.getText().toString();

                if (TextUtils.isEmpty(etquantity)) {
                    return false;
                }

                int quantityInt = Integer.parseInt(etquantity);

                if (quantityInt <= 1) {
                    quantity = 1;

                } else if (quantityInt >= goodsNumber) {
                    quantity = goodsNumber;

                } else {
                    quantity = quantityInt;
                }

                showMoney();

                return false;
            }
        });


        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.btn_sku_quantity_minus, R.id.btn_sku_quantity_plus, R.id.bt_buy,
            R.id.ll_add_address, R.id.rl_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_add_address:
                if (uid.equalsIgnoreCase("")) {
                    startActivityForResult(new Intent(this, LoginActivity.class),
                            login);

                } else {
                    if (address == null) {
                        startActivityForResult(new Intent(this,
                                AddAddressActivity.class), addAddres);
                    } else {

                        //未登录或已登录但未添加收货地址时，显示此内容；
                        startActivityForResult(new Intent(this,
                                AddressManageActivity.class), addAddres);
                    }

                    // getCoupons();
                }
                break;
            case R.id.rl_address:

                //未登录或已登录但未添加收货地址时，显示此内容；
                startActivityForResult(new Intent(this,
                        AddressManageActivity.class), addAddres);
                break;
            case R.id.btn_sku_quantity_minus:

                if (quantity <= 1) {
                    btnSkuQuantityMinus.setEnabled(false);
                    btnSkuQuantityPlus.setEnabled(true);
                    break;
                } else {
                    btnSkuQuantityMinus.setEnabled(true);
                    btnSkuQuantityPlus.setEnabled(true);

                    quantity = quantity - 1;
                    showMoney();
                }


                break;
            case R.id.btn_sku_quantity_plus:

                if (quantity >= goodsNumber) {
                    btnSkuQuantityMinus.setEnabled(true);
                    btnSkuQuantityPlus.setEnabled(false);
                    break;
                } else {
                    btnSkuQuantityMinus.setEnabled(true);
                    btnSkuQuantityPlus.setEnabled(true);

                    quantity = quantity + 1;
                    showMoney();
                }


                break;
            case R.id.bt_buy:
                if (address == null) {
                    ToastUtil.showToast("请先添加收货地址");
                } else {
                    // getcanBuyFuelCard();

                    String bz = etBz.getText().toString().trim();

                    startActivity(new Intent(this, OilCardPayActivity.class)
                            .putExtra("uid", uid)
                            .putExtra("amount", money)  //油卡id
                            // .putExtra("type", type) //1支付宝 、2微信支付、3云闪付
                            .putExtra("number", quantity) //单笔金额
                            .putExtra("bz", bz)//备注
                            .putExtra("pid", Integer.parseInt(id)) //产品id
                            //  .putExtra("tradeType", UrlConfig.version) //微信支付类型 App；APP   H5:JSAPI
                            .putExtra("addressid", address.getId())  //地址主键
                            .putExtra("activitytype", 4)// 1：油卡 2：手机 3：直购  4

                    );
                }


               /* */

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case addAddres:


                if (resultCode == RESULT_OK) {

                    int position = data.getIntExtra("position", 0);

                    LogUtils.e("onActivityResult" + position);

                    if (address != null) {
                        if (position != 0) {
                            refreshReceiptAddress(position);

                        } else {
                            getReceiptAddress();
                        }
                    } else {

                        getReceiptAddress();
                    }
                } else {
                    getReceiptAddress();
                }


                break;
            case login:
                getReceiptAddress();
                //getData();
                break;
        }

    }

    /**
     * 4.下单
     */

    private void goShoptopay() {


        showWaitDialog("加载中...", true, "");
        LogUtils.e("pid" + id);
        OkHttpUtils.post()
                .url(UrlConfig.shoptopay)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("pid", id)
                // .addParams("bz", id) //备注

                .addParams("number", quantity + "")
                .addParams("type", type + "")
                .addParams("fid", address.getId() + "") //地址主键
                .addParams("tradeType", "APP")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                //ptr_address.refreshComplete();
                LogUtils.e("4.下单" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    // totalCount 总共已购买张数
                    //     singleCount 当天已购买张数
                    Integer totalCount = map.getInteger("totalCount");
                    Integer singleCount = map.getInteger("singleCount");

                }

            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                // ptr_address.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

    /**
     * 获取收货地址
     */
    /*获取所有收货地址*/
    private void getReceiptAddress() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.RECEIPTADDRESS)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                //ptr_address.refreshComplete();
                LogUtils.i("所有的收货地址：" + response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    JSONArray list = map.getJSONArray("addrlist");
                    // LogUtils.e("所有的收货地址：" + list.size());
                    if (list.size() > 0) {
                        List<AddressBean> addressBeans = JSON.parseArray(list.toJSONString(), AddressBean.class);

                        address = addressBeans.get(0);

                        llAddAddress.setVisibility(View.GONE);

                        rlAddress.setVisibility(View.VISIBLE);

                        tvName.setText(address.getName() + " ");
                        tvPhone.setText(address.getPhone());
                        tvAddress.setText(address.getProvinceName() + address.getCityName()
                                + address.getAreaName() + address.getAddress());

                       /* llNorecord.setVisibility(View.GONE);
                        addressList = JSON.parseArray(list.toJSONString(), AddressBean.class);
                        addressAdapter = new AddressAdapter(AddressManageActivity.this, addressList);
                        addressAdapter.setOnAddressManageListener(AddressManageActivity.this);
                        lvAddress.setAdapter(addressAdapter);*/
                    } else {
                        // llNorecord.setVisibility(View.VISIBLE);
                        llAddAddress.setVisibility(View.VISIBLE);
                        rlAddress.setVisibility(View.GONE);

                    }
                }

            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                // ptr_address.refreshComplete();
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

    /**
     * 选择收货地址
     *
     * @param position
     */
    /*获取所有收货地址*/
    private void refreshReceiptAddress(final int position) {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.RECEIPTADDRESS)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {
                    JSONObject map = obj.getJSONObject("map");
                    JSONArray list = map.getJSONArray("addrlist");
                    if (list != null) {
                        List<AddressBean> addressBeans = JSON.parseArray(list.toJSONString(), AddressBean.class);
                        address = addressBeans.get(position);
                        llAddAddress.setVisibility(View.GONE);
                        rlAddress.setVisibility(View.VISIBLE);
                        tvName.setText(address.getName() + " ");
                        tvPhone.setText(address.getPhone());
                        tvAddress.setText(address.getProvinceName() + address.getCityName()
                                + address.getAreaName() + address.getAddress());


                    } else {
                        llAddAddress.setVisibility(View.VISIBLE);
                        rlAddress.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ToastUtil.showToast("请检查网络");
            }
        });
    }

    /**
     * 计算 显示 金额
     */
    public void showMoney() {


        money = Arith.mul(retailPrice, quantity);

        tvMoney.setText(StringCut.getNumKb(money) + "");
        tvAllMoney.setText(StringCut.getNumKb(money) + "");

        etSkuQuantityInput.setText(quantity + "");
        tvNum.setText("x" + quantity);
       /* //int money = monthMoney * oilCardPackageBean.getDeadline();
        //int money = oilCardPackageBean.getLeastaAmount();
        double rate = oilCardPackageBean.getRate();

        double div = Arith.sub(1.0, rate);//减 折扣了

        double mul2 = Arith.mul(money, rate);//乘 总计
        //LogUtils.e(money+"mul2/"+mul2+"/"+rate);

        double mul = Arith.mul(money, div);//节省

        double coupons = 0.0;
        if (couponsBean != null) {
            //monthMoney money
            if (money >= couponsBean.getEnableAmount() && oilCardPackageBean.getDeadline() >= couponsBean.getProductDeadline()) {
                coupons = couponsBean.getAmount();
            } else {
                couponsBean = null;
            }

            //  coupons = couponsBean.getAmount();
        }

        double mul1 = Arith.add(mul, coupons); //加

        allMoney = Arith.sub(mul2, coupons); //减

        LogUtils.e(monthMoney + "//" + money + "//" + allMoney + "金额" + StringCut.getNumKb(allMoney) + "(省" + StringCut.getNumKb(mul1));

        tvAllMoney.setText(StringCut.getNumKb(allMoney) + "");

        tvCheaper.setText("(省" + StringCut.getNumKb(mul1) + ")");

        //充3个月 原价3000元 折扣价2910元 省90元
        Spanned spanned = Html.fromHtml("充" + oilCardPackageBean.getDeadline()
                + "个月 原价<del><font color='#FF623D'>" +
                money + "</font></del>元 折扣价<font color='#FF623D'>"
                + allMoney + "</font>元 省<font color='#FF623D'>" + mul1 + "</font>元");

        tvExplan.setText(spanned);

        showCouponsUsed();*/

    }
}
