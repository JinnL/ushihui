package com.ekabao.oil.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.OilOrdersbean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.util.StringCut;

import java.util.List;

/**
 * 易卡宝  App
 * 油卡购买订单
 *
 * @time 2018/7/19 17:12
 * Created by lj on 2018/7/19 17:12.
 */

public class OilCardBuyAdapter extends BaseRecyclerViewAdapter {
    List<OilOrdersbean> lslbs;
    private int status;//

    public OilCardBuyAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);
        this.lslbs = list;
    }

    public OilCardBuyAdapter(RecyclerView view, List list, int itemLayoutId, int status) {
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
        // "status": 3(状态(0-未支付 1-支付成功 2-失败,3-已结束))
        //1：进行中 3：已结束  1：中石化，2：中石油
        // "status": //状态 0-待支付，1-已支付，2-失败（已取消、退款），3-已完成，4-已退订，5-已发货，6-删除

        // TODO: 2018/11/30
        int statuss = cb.getStatus();

        /*   ivState.setImageResource(R.drawable.icon_my_order_1);
                break;
            case 2:
                ivState.setImageResource(R.drawable.icon_my_order_2);
                break;
            case 3:*/
        switch (cb.getStatus()) {
            case 0:
//                ivState.setImageResource(R.drawable.icon_my_order_0);
                tvState.setText("待支付");
                tvState.setBackgroundResource(R.drawable.bg_oil_package_selected_round);
                break;
            case 1:
            case 3:
            case 5:
                tvState.setText("已完成");
                tvState.setBackgroundResource(R.drawable.bg_base_round);
                break;
            case 2:
            case 4:
                tvState.setText("已取消");
                tvState.setBackgroundResource(R.drawable.bg_gray_round);
                break;
        }
      /*  if (statuss == 1) {
            ivState.setImageResource(R.drawable.icon_zhongshihua_addcard);

        } else {
            ivState.setImageResource(R.drawable.icon_zhongshiyou_addcard);
        }*/

        //油卡类型 1:中石化 2:中石油 3:公司中石化 4:公司中石油

        if (cb.getCardType() != 0) {
            if (cb.getCardType() == 1 | cb.getCardType() == 3) {

                tvName.setCompoundDrawablesWithIntrinsicBounds(LocalApplication.context.getResources().getDrawable(
                        R.drawable.icon_zhongshihua_addcard),
                        null, null, null);

                //ivState.setImageResource(R.drawable.icon_zhongshihua_addcard);
            } else {
                tvName.setCompoundDrawablesWithIntrinsicBounds(LocalApplication.context.getResources().getDrawable(
                        R.drawable.icon_zhongshiyou_addcard),
                        null, null, null);
                //ivState.setImageResource(R.drawable.icon_zhongshiyou_addcard);
            }

        } else {
            if (cb.getFullName() != null) {
                String fullName = cb.getFullName();
                if (fullName.contains("石化")) {
                    // ivState.setImageResource(R.drawable.icon_zhongshihua_addcard);
                    tvName.setCompoundDrawablesWithIntrinsicBounds(LocalApplication.context.getResources().getDrawable(
                            R.drawable.icon_zhongshihua_addcard),
                            null, null, null);
                } else {
                    //ivState.setImageResource(R.drawable.icon_zhongshiyou_addcard);
                    tvName.setCompoundDrawablesWithIntrinsicBounds(LocalApplication.context.getResources().getDrawable(
                            R.drawable.icon_zhongshiyou_addcard),
                            null, null, null);
                }
            }
        }

        if (cb.getFullName() != null) {
            String fullName = cb.getFullName();
           /* if (fullName.contains("石化")) {
                ivState.setImageResource(R.drawable.icon_zhongshihua_addcard);
            } else {
                ivState.setImageResource(R.drawable.icon_zhongshiyou_addcard);
            }*/

            tvName.setText(fullName);
        }


        tvMoney.setText("￥" + cb.getAmount());
        //  LogUtils.e("OilCardBuyAdapter"+cb.getAmount());

        tvTime.setText(StringCut.getDateTimeToStringheng(cb.getInvestTime()));

    }


}
