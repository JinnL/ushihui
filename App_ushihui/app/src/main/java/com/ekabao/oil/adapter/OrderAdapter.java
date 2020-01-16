package com.ekabao.oil.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.OilOrdersbean;
import com.ekabao.oil.util.StringCut;

import java.util.List;

/**
 * 易卡宝  App
 * 我的订单
 *
 * @time 2018/7/19 17:12
 * Created by lj on 2018/7/19 17:12.
 */

public class OrderAdapter extends BaseRecyclerViewAdapter {
    List<OilOrdersbean> lslbs;
    private int status;//

    public OrderAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);
        this.lslbs = list;
    }

    public OrderAdapter(RecyclerView view, List list, int itemLayoutId, int status) {
        super(view, list, itemLayoutId);
        this.lslbs = list;
        this.status = status;
    }


    @Override
    public void bindConvert(BaseViewHolder holder, int position, Object item, boolean isScrolling) {
        OilOrdersbean cb = lslbs.get(position);

        TextView tvState = holder.getView(R.id.tv_state);
        TextView tvMoney = holder.getView(R.id.tv_money);
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvTime = holder.getView(R.id.tv_time);

        // "type": 1,( 1:套餐充值 2:即时充值)
        // "status": //状态 0-待支付，1-已支付，2-失败（已取消、退款），3-已完成，4-已退订，5-已发货，6-删除
        //1：进行中 3：已结束


        switch (cb.getStatus()) {
            case 0:
//                ivState.setImageResource(R.drawable.icon_my_order_0);
                tvState.setText("待支付");
                tvState.setBackgroundResource(R.drawable.bg_oil_package_selected_round);
                break;
            case 1:
//                 ivState.setImageResource(R.drawable.icon_my_order_1);
                tvState.setText("进行中");
                tvState.setBackgroundResource(R.drawable.bg_base_round);
                break;
            case 2:
//                 ivState.setImageResource(R.drawable.icon_my_order_2);
                tvState.setText("已取消");
                tvState.setBackgroundResource(R.drawable.bg_gray_round);
                break;
            case 3:
//                 ivState.setImageResource(R.drawable.icon_my_order_3);
                tvState.setText("已完成");
                tvState.setBackgroundResource(R.drawable.bg_gray_round);
                break;
            case 4:
//                 ivState.setImageResource(R.drawable.icon_my_order_4);
                tvState.setText("已退订");
                tvState.setBackgroundResource(R.drawable.bg_gray_round);
                break;
        }

       /* if (status == 1) {
            ivState.setImageResource(R.drawable.icon_my_order_going);
        } else {
            ivState.setImageResource(R.drawable.icon_my_order_complete);
        }*/

        tvMoney.setText("￥" + cb.getAmount());
        tvName.setText(cb.getFullName());
        tvTime.setText(StringCut.getDateTimeToStringheng(cb.getInvestTime()));

    }


}
