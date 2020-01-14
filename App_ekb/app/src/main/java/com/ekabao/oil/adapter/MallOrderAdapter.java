package com.ekabao.oil.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.GoodsOrderList;

import java.util.List;

/**
 * 易卡宝  App
 * 我的订单
 *
 * @time 2018/7/19 17:12
 * Created by lj on 2018/7/19 17:12.
 */

public class MallOrderAdapter extends BaseRecyclerViewAdapter {
    List<GoodsOrderList> lslbs;
    private int status;//
    private Context context;

    public MallOrderAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);
        this.lslbs = list;
    }

    public MallOrderAdapter(Context context, RecyclerView view, List list, int itemLayoutId, int status) {
        super(view, list, itemLayoutId);
        this.lslbs = list;
        this.context = context;
        this.status = status;
    }


    @Override
    public void bindConvert(BaseViewHolder holder, int position, Object item, boolean isScrolling) {
        GoodsOrderList cb = lslbs.get(position);

        ImageView ivState = holder.getView(R.id.iv_goods);

        Glide.with(context)
                .load(cb.getImages())
                .centerCrop()
                .into(ivState);


        TextView tvMoney = holder.getView(R.id.tv_price);
        tvMoney.setText("￥" + cb.getRetail_price());

        TextView tvName = holder.getView(R.id.tv_name);
        tvName.setText(cb.getGoodname());

        TextView tvNumber = holder.getView(R.id.tv_number);
        tvNumber.setText("x" + cb.getNumber());

        TextView tv_money = holder.getView(R.id.tv_money);
        tv_money.setText("￥" + cb.getAmount());

        TextView tv_goods_num = holder.getView(R.id.tv_goods_num);
        tv_goods_num.setText("共"+cb.getNumber()+"件商品  小计:");


        // "type": 1,( 1:套餐充值 2:即时充值)
        // "status": 3(状态(0-未支付 1-支付成功 2-失败,3-已结束))
        //1：进行中 3：已结束
      /*  if (status == 1) {
            ivState.setImageResource(R.drawable.icon_my_order_going);
        } else {
            ivState.setImageResource(R.drawable.icon_my_order_complete);
        }*/


    }


}
