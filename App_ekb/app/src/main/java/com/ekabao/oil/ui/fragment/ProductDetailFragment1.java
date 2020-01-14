package com.ekabao.oil.ui.fragment;

import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.ProductPicAdapter;
import com.ekabao.oil.bean.ProductDetail;
import com.ekabao.oil.bean.ProductDetailInfo;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.OkHttpEngine;
import com.ekabao.oil.http.PostParams;
import com.ekabao.oil.http.SimpleHttpCallback;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.ui.view.ListInScroll;
import com.ekabao.oil.util.GsonUtil;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/6/5.
 */

public class ProductDetailFragment1 extends BaseFragment {


    @BindView(R.id.tv_deadline)
    TextView tvDeadline;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_interestType)
    TextView tvInterestType;
    @BindView(R.id.tv_repayType)
    TextView tvRepayType;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_repaySource)
    TextView tvRepaySource;
    Unbinder unbinder;
    @BindView(R.id.lv_product_pic)
    ListInScroll lvProductPic;

    Unbinder unbinder1;


    private int pid;  //产品id
    private SharedPreferences preferences;
    private List<ProductDetailInfo.PicListBean> proIntroduceList =new ArrayList<>();
    private ProductPicAdapter productPicAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product_detail_1;
    }

    @Override
    protected void initParams() {

        pid = getArguments().getInt("pid");

        preferences = LocalApplication.getInstance().sharereferences;

        getMoreDetail();
        getDate();


        productPicAdapter = new ProductPicAdapter(proIntroduceList);

        lvProductPic.setAdapter(productPicAdapter);

        lvProductPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showPopupWindow1(proIntroduceList.get(position).getBigUrl());

            }
        });
    }

    /**
     * 获取资料清单 等列表
     *
     * */
    private void getMoreDetail() {

        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("pid", pid + "");
        map.put("uid", preferences.getString("uid", ""));
        map.put("type", 2 + "");
        map.put("version", UrlConfig.version);
        map.put("channel", "2");


        OkHttpEngine.create().setHeaders().post(UrlConfig.DETAIL_INFO, params, new SimpleHttpCallback() {
            @Override
            public void onLogicSuccess(String data) {
                LogUtils.e(data);
                //dismissDialog();


                ProductDetailInfo productDetail = GsonUtil.parseJsonToBean(data, ProductDetailInfo.class);

                List<ProductDetailInfo.PicListBean> picList = productDetail.getPicList();
                if (picList.size()>0){

                    proIntroduceList.clear();
                    proIntroduceList.addAll(picList);
                    productPicAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onLogicError(int code, String msg) {
                LogUtils.e(msg);
                //dismissDialog();
            }

            @Override
            public void onError(IOException e) {
                LogUtils.e(e.toString());
                //dismissDialog();
            }
        });


    }

    /**
     * 获取产品详情数据
     */
    private void getDate() {


        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("pid", pid + "");
        map.put("uid", preferences.getString("uid", ""));
        //map.put("uid", "46");
        map.put("version", UrlConfig.version);
        map.put("channel", "2");

        OkHttpEngine.create().setHeaders().post(UrlConfig.PRODUCT_DETAIL, params, new SimpleHttpCallback() {
            @Override
            public void onLogicSuccess(String data) {
                LogUtils.e(data);
                //dismissDialog();


                ProductDetail productDetail = GsonUtil.parseJsonToBean(data, ProductDetail.class);

                ProductDetail.InfoBean info = productDetail.getInfo();


                int deadline = info.getDeadline();//出借期限 多少天

                tvDeadline.setText(Html.fromHtml("出借期限  <font color='#999999'>" + deadline + "天</font>"));
                double amount = info.getAmount();//出借总金额
                tvAmount.setText(Html.fromHtml("项目金额  <font color='#999999'>" + StringCut.getNumKbs(amount) + "</font>"));

                double v = info.getRate() + info.getActivityRate();
                tvRate.setText(Html.fromHtml("近3个月年化收益  <font color='#999999'>" + (int) v + "%</font>"));


                String introduce = info.getIntroduce(); //"项目简介"
                tvContent.setText(introduce);

                String repaySource = info.getRepaySource();//还款来源
                tvRepaySource.setText(repaySource);


                List<ProductDetail.ExtendInfosBean> extendInfos = productDetail.getExtendInfos();

                for (ProductDetail.ExtendInfosBean bean :
                        extendInfos) {

                    if (bean.getTitle().contains("款方式")) {
                        tvRepayType.setText(Html.fromHtml("回款方式  <font color='#999999'>" + bean.getContent() + "</font>"));
                    }
                    if (bean.getTitle().contains("计息")) {
                        tvInterestType.setText(Html.fromHtml("计息方式  <font color='#999999'>" + bean.getContent() + "</font>"));
                    }

                }
            }


            @Override
            public void onLogicError(int code, String msg) {
                LogUtils.e(msg);
                //dismissDialog();
            }

            @Override
            public void onError(IOException e) {
                LogUtils.e(e.toString());
                //dismissDialog();
            }
        });


    }

    private LinearLayout layout;
    private PopupWindow popupWindow;
    private ImageView iv_regist;

    public void showPopupWindow1(String url_image) {
        // 加载布局
        layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.pop_image_big, null);
        // 找到布局的控件
        // 实例化popupWindow
        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        iv_regist = (ImageView) (layout).findViewById(R.id.iv_regist);


        Glide.with(this).load(url_image).placeholder(R.drawable.bg_activity_fail)
                .error(R.drawable.bg_activity_fail).into(iv_regist);


        // 控制键盘是否可以获得焦点
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//		popupWindow.setBackgroundDrawable(new PaintDrawable());
//		popupWindow.setOutsideTouchable(true);

//		// 设置popupWindow弹出窗体的背景
//		WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        // 监听
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

                backgroundAlpha(1f);
            }
        });
        popupWindow.showAsDropDown(iv_regist);
//		backgroundAlpha(0.5f);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     **/

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
    }
}
