package com.ekabao.oil.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ekabao.oil.R;
import com.ekabao.oil.bean.AtyLocationInfo;
import com.ekabao.oil.util.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 闪油侠 Oil_syx
 *
 * @time 2019/1/2 16:04
 * Created by lbj on 2019/1/2 16:04.
 */

public class AdapterLocation extends RecyclerView.Adapter<AdapterLocation.ViewHolder> {


    private List<AtyLocationInfo.DataListBean> locatiobList;
    //public String oilType = "E90";
    public String oilType = "E93";
    private Context context;
    private String oilPrice;


    public AdapterLocation(List<AtyLocationInfo.DataListBean> locationList, Context context) {
        this.locatiobList = locationList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        ViewHolder viewHolder = holder;
        AtyLocationInfo.DataListBean dataListBean = locatiobList.get(position);
        viewHolder.tvOilName.setText(dataListBean.getName());
        viewHolder.tvOilAddress.setText(dataListBean.getAddress());
        viewHolder.tvOilDistance.setText(dataListBean.getDistance() + "m");
        viewHolder.tvOilPrice.setText(dataListBean.getName());

        String discount = dataListBean.getDiscount();
        String type = dataListBean.getType();
        if ("打折加油站".equals(discount)) {
            viewHolder.tvLocationZk.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvLocationZk.setVisibility(View.GONE);
        }
        if ("直营店".equals(type)) {
            viewHolder.tvLocationZy.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvLocationZy.setVisibility(View.GONE);
        }

        LogUtils.e("oilType==" + oilType);
        AtyLocationInfo.DataListBean.PriceBean price = dataListBean.getPrice();
        if ("E90".equals(oilType)) {
            oilPrice = price.getE90();
        } else if ("E93".equals(oilType)) {
            oilPrice = price.getE93();
        } else if ("E97".equals(oilType)) {
            oilPrice = price.getE97();
        } else if ("E0".equals(oilType)) {
            oilPrice = price.getE0();
        }


        if ("E90".equals(oilType)) {
            viewHolder.tvOilPrice.setText(oilPrice);
        } else {
            AssetManager assets = context.getAssets();
            //根据路径得到Typeface
            Typeface tf = Typeface.createFromAsset(assets, "DIN Medium.ttf");
            //设置字体
            viewHolder.tvOilPrice.setTypeface(tf);

            SpannableStringBuilder sp = new SpannableStringBuilder(oilPrice);
            //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            sp.setSpan(new AbsoluteSizeSpan(42, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
            viewHolder.tvOilPrice.setText(sp);
        }
        if (this.mOnItemClickLitener != null) {
            viewHolder.rlLocationGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(v, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return locatiobList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_oil_name)
        TextView tvOilName;
        @BindView(R.id.tv_location_zk)
        TextView tvLocationZk;
        @BindView(R.id.tv_location_zy)
        TextView tvLocationZy;
        @BindView(R.id.tv_oil_address)
        TextView tvOilAddress;
        @BindView(R.id.tv_oil_distance)
        TextView tvOilDistance;
        @BindView(R.id.tv_oil_price)
        TextView tvOilPrice;
        @BindView(R.id.rl_location_go)
        RelativeLayout rlLocationGo;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

     /*   void onItemClick2(int  toPosition, int position);

        void onDeleteItemClick(int id);*/
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
