package com.ekabao.oil.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.MallDetailsRollViewAdapter;
import com.ekabao.oil.bean.GoodsDetails;
import com.ekabao.oil.bean.Product;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.view.ProductSkuDialog;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.wuhenzhizao.sku.bean.Sku;
import com.wuhenzhizao.sku.bean.SkuAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MallDetailsActivity extends BaseActivity {
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
    @BindView(R.id.refreshLayout_head)
    BezierCircleHeader refreshLayoutHead;
    @BindView(R.id.rpv_banner)
    RollPagerView rpvBanner;
    @BindView(R.id.tv_retailPrice)
    TextView tvRetailPrice;
    @BindView(R.id.tv_marketPrice)
    TextView tvMarketPrice;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_summary)
    TextView tvSummary;
    @BindView(R.id.ll_select)
    LinearLayout llSelect;
    @BindView(R.id.wv_content)
    WebView wvContent;
    @BindView(R.id.tv_deadline)
    TextView tvDeadline;
    @BindView(R.id.tv_info_condition)
    TextView tvInfoCondition;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.bt_buy)
    Button btBuy;


    private int pid;
    private List<GoodsDetails.GalleryBean> banner = new ArrayList<>();//轮播图
    private MallDetailsRollViewAdapter banneradapter;

    private List<GoodsDetails.ProductListBean> productList = new ArrayList<>(); //有货的
    private List<GoodsDetails.SpecificationListBean> specificationList = new ArrayList<>();//选择的


    private Product product = new Product();

    private SharedPreferences preferences = LocalApplication.sharereferences;
    private String uid;
    private static final int login = 10156; //

    private String listPicUrl; //图片
    private String name; //名字
    private int retailPrice; //单价

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_details);
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mall_details;
    }

    @Override
    protected void initParams() {
        refreshLayout.setPrimaryColors(new int[]{0xffF4F4F4, 0xffEE4845});

        uid = preferences.getString("uid", "");

        titleCentertextview.setText("商品详情");


        Intent intent = getIntent();
        pid = intent.getIntExtra("pid", 0);

        LogUtils.e("pid"+pid);
        getList();

        //设置播放时间间隔
        rpvBanner.setPlayDelay(5000);
        //设置透明度
        rpvBanner.setAnimationDurtion(500);
        //设置圆点指示器颜色
        rpvBanner.setHintView(new ColorPointHintView(this, 0xFFFF0000, Color.WHITE));
        //设置适配器
        banneradapter = new MallDetailsRollViewAdapter(this, banner);

        rpvBanner.setAdapter(banneradapter);


    }

    @OnClick({R.id.title_leftimageview, R.id.ll_select, R.id.bt_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.ll_select:
                showSkuDialog();
                break;
            case R.id.bt_buy:
                showSkuDialog();
                break;
        }
    }

    private ProductSkuDialog dialog;

    private void showSkuDialog() {
        // Log.e(""+Product.get(this).toString());

        if (getData() != null) {
            product.setSkus(getData());
        }

        if (dialog == null) {
            dialog = new ProductSkuDialog(this);
            //Product.get(this)

            dialog.setData(product, new ProductSkuDialog.Callback() {
                @Override
                public void onAdded(Sku sku, int quantity) {
                    List<SkuAttribute> attributes = sku.getAttributes();

                    StringBuffer specification = new StringBuffer();

                    for (SkuAttribute bean : attributes) {
                        specification.append(bean.getKey() + ":" + bean.getValue() + " ");
                    }


                    LogUtils.e("onAdded" + sku.getId());

                    if ( preferences.getString("uid", "") =="") {
                        startActivityForResult(new Intent(MallDetailsActivity.this, LoginActivity.class), login);
                    } else {
                        Intent intent = new Intent(MallDetailsActivity.this, MallOrderSureActivity.class);
                        intent.putExtra("id", sku.getId());
                        intent.putExtra("goodsId", pid);
                        intent.putExtra("quantity", quantity);
                        intent.putExtra("retailPrice", (int) sku.getSellingPrice());
                        intent.putExtra("specification", specification.toString());
                        intent.putExtra("listPicUrl", listPicUrl);
                        intent.putExtra("name", name);
                        intent.putExtra("goodsNumber", sku.getStockQuantity());


                        startActivity(intent);
                    }





                  /*  shoppingCartNum += quantity;
                    cartBinding.tvShoppingCartNum.setVisibility(View.VISIBLE);

                    // 获取SKU面板Logo拷贝
                    ImageView logoImageView = new ImageView(MainActivity.this);
                    binding.ivAddCartAnim.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(binding.ivAddCartAnim.getDrawingCache());
                    logoImageView.setImageBitmap(bitmap);
                    binding.ivAddCartAnim.setDrawingCacheEnabled(false);

                    int[] startLocation = new int[2];
                    binding.ivAddCartAnim.getLocationOnScreen(startLocation);
                    // 执行动画
                    startAddToCartAnimation(logoImageView, startLocation);*/
                }
            });
        }
        dialog.show();

    }

    /**
     * 商品详情
     * *
     */
    private void getList() {

        // TODO: 2019/1/8 "1181000"
        //showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.shopDetail)
                .addParams("id", pid + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("商品详情--->" + response);

                        dismissDialog();

                        if (refreshLayout != null) {
                            RefreshState state = refreshLayout.getState();
                            if (state == RefreshState.Refreshing) {
                                refreshLayout.finishRefresh();
                            }
                        }

                        JSONObject obj = JSON.parseObject(response);
                        // TODO: 2019/1/8
                        //!obj.getBoolean(("success")
                        if (obj.getJSONObject("map") != null) {
                            JSONObject map = obj.getJSONObject("map");

                            GoodsDetails goodsDetails = JSON.parseObject(map.toJSONString(), GoodsDetails.class);

                            if (goodsDetails.getGallery() != null) {
                                //轮播图
                                List<GoodsDetails.GalleryBean> gallery = goodsDetails.getGallery();
                                banner.clear();
                                banner.addAll(gallery);
                                banneradapter.notifyDataSetChanged();
                            }

                            if (goodsDetails.getInfo() != null) {
                                GoodsDetails.InfoBean info = goodsDetails.getInfo();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    wvContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
                                } else {
                                    wvContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                                }
                                wvContent.loadData(getHtmlData(info.getGoodsDesc()), "text/html; charset=utf-8", "utf-8");
                                /**
                                 * 什么鬼,没加这句话,会自动向上滑动
                                 * */
                                wvContent.setFocusable(false);


                                tvName.setText(info.getName());
                                tvSummary.setText(info.getGoodsBrief());
                                tvRetailPrice.setText("￥" + info.getRetailPrice());
                                tvMarketPrice.setText("￥" + info.getMarketPrice());
                                tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                tvMarketPrice.getPaint().setAntiAlias(true);// 抗锯齿


                                product.setId(info.getId() + "");
                                product.setName(info.getName());
                                product.setStatus("");
                                // TODO: 2019/1/10  
                                product.setMainImage(info.getPrimaryPicUrl());
                                product.setOriginPrice(info.getRetailPrice());
                                product.setSellingPrice(info.getRetailPrice());
                                product.setCurrencyUnit("￥");
                                product.setMeasurementUnit("件");
                                product.setStockQuantity(info.getGoodsNumber());

                                listPicUrl = info.getListPicUrl(); //图片
                                name = info.getName(); //名字
                                retailPrice = info.getRetailPrice(); //单价
                            }


                            if (goodsDetails.getProductList() != null) {
                                //轮播图
                                List<GoodsDetails.ProductListBean> productlist = goodsDetails.getProductList();


                                productList.clear();
                                productList.addAll(productlist);

                                if (goodsDetails.getSpecificationList() != null) {
                                    List<GoodsDetails.SpecificationListBean> specificationlist = goodsDetails.getSpecificationList();
                                    specificationList.clear();
                                    specificationList.addAll(specificationlist);
                                }

                            }

                            if (goodsDetails.getIssue() != null) {
                                //問題

                                List<GoodsDetails.IssueBean> issue = goodsDetails.getIssue();


                                StringBuffer s=new StringBuffer();
                                for (int i = 0; i < issue.size(); i++) {

                                    s.append(issue.get(i).getId()+"、 " +issue.get(i).getAnswer()+"\n");
                                }

                                LogUtils.e("商品详情--->" + s);

                                tvDeadline.setText(s);

                            }



                          /*  if (map.getJSONArray("category") != null) {

                                JSONArray arr = map.getJSONArray("category");
                                List<GoodsCategory> category = JSON.parseArray(arr.toJSONString(), GoodsCategory.class);
                                //  homepagerRecycleAdapter.setCenterBean(homeHostProduct);
                                categoryList.clear();
                                categoryList.addAll(category);

                                //   homepagerRecycleAdapter.setCategoryBean((ArrayList<GoodsCategory>) categoryList);

                            }

                            if (map.getJSONArray("goodList") != null) {
                                JSONArray arr = map.getJSONArray("goodList");
                                List<GoodsList> goodslist = JSON.parseArray(arr.toJSONString(), GoodsList.class);

                                lists.clear();
                                lists.addAll(goodslist);
                                mallClassifyAdapter.notifyDataSetChanged();
                                //  homepagerRecycleAdapter.setCenterBean(homeHostProduct);
                                //  homepagerRecycleAdapter.setRefreshBean(goodslist,true);
                            }*/





                           /* homepagerRecycleAdapter.setheaderbean(homeInfo);
                            homepagerRecycleAdapter.setCenterBean(homeInfo);

*/
                        /*    List<HomeInfoList.BannerBean> banner = homeInfo.getBanner();

                            if (banner.size() > 0) {
                                lsad.clear();
                                lsad.addAll(banner);
                                adapter.notifyDataSetChanged();
                            }*/


                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
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


    private List<Sku> getData() {


        if (productList.size() <= 0) {
            return null;
        }


        List<Sku> skuList = new ArrayList<>();


        for (int i = 0; i < productList.size(); i++) {
            GoodsDetails.ProductListBean productListBean = productList.get(i);
            String goodsSpecificationIds = productListBean.getGoodsSpecificationIds();
            List<String> idList = Arrays.asList(goodsSpecificationIds.split("_"));//根据逗号分隔转化为list

            Sku sku = new Sku();
            List<SkuAttribute> attributes = new ArrayList<>();

            for (int j = 0; j < idList.size(); j++) {

                String o = idList.get(j);
                //

                for (int k = 0; k < specificationList.size(); k++) {

                    GoodsDetails.SpecificationListBean specificationListBean = specificationList.get(k);

                    String name = specificationListBean.getName();
                    // LogUtils.e(specificationList.size()+"getData//"+name);

                    List<GoodsDetails.SpecificationListBean.ValueListBean> valueList = specificationListBean.getValueList();

                    for (int l = 0; l < valueList.size(); l++) {
                        GoodsDetails.SpecificationListBean.ValueListBean valueListBean = valueList.get(l);
                        if (valueListBean.getId() == Integer.parseInt(o)) {

                            attributes.add(new SkuAttribute(name, valueListBean.getValue()));

                            sku.setMainImage(valueListBean.getPicUrl());
                        }
                    }

                }
            }

            sku.setId(productListBean.getId() + "");
            sku.setAttributes(attributes);
            sku.setInStock(true); //????

            sku.setStockQuantity(productListBean.getGoodsNumber());
            sku.setOriginPrice(productListBean.getRetailPrice()); //??????
            sku.setSellingPrice(productListBean.getRetailPrice());
            LogUtils.e("getRetailPrice---" + productListBean.getRetailPrice());
            skuList.add(sku);

        }

        return skuList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}p{padding:0px; margin:0px;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

}
