package com.ekabao.oil.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.CouponsBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.util.StringCut;

import java.util.List;

/**
 * 易卡宝  App
 *
 * @time 2018/7/20 17:51
 * Created by lj on 2018/7/20 17:51.
 */

public class TiyanjinAdapter extends BaseRecyclerViewAdapter {
    List<CouponsBean> lslbs;

    public TiyanjinAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);
        this.lslbs = list;
    }

    @Override
    public void bindConvert(BaseViewHolder holder, int position, Object item, boolean isScrolling) {
        CouponsBean cb = lslbs.get(position);


        RelativeLayout ivBackground = holder.getView(R.id.rl_background);
        TextView tvMoney = holder.getView(R.id.tv_money);
        //TextView tvLimitMoney = holder.getView(R.id.tv_limit_money);
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvLimitDay = holder.getView(R.id.tv_limit_day);
        TextView tvTime = holder.getView(R.id.tv_time);
        TextView tvState = holder.getView(R.id.tv_state);
        TextView tvStateGo = holder.getView(R.id.tv_state_go);


        String text = "翻倍券-" + cb.getName();

        tvName.setText(text);

        tvMoney.setText("￥" + StringCut.getNumKbs(cb.getAmount()));

        // tvLimitMoney.setText("满" + StringCut.getNumKbs(cb.getEnableAmount()) + "元使用");

        tvLimitDay.setText("单笔出借满" + StringCut.getNumKbs(cb.getEnableAmount()) + "元，" + "出借期限≥" + cb.getProductDeadline() + "天");

        if (cb.getExpireDate() != 0) {
            tvTime.setText("有效期至：" + StringCut.getDateYearToString(cb.getExpireDate()));
        } else {
            // vh.tv_dd.setText("");
            tvTime.setText("永久有效");
        }


        //状态 0：未使用  1：已使用 2：已过期
        switch (cb.getStatus()) {
            case 3:// 选择优惠券的时候金额不可用
                ivBackground.setBackgroundResource(R.drawable.bg_tiyanjin_unuse);
                tvState.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_99));
                tvStateGo.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_99));
//                vh.cover_tv.setVisibility(View.VISIBLE);
                break;
            case 1:// 选择优惠券的时候金额不可用
                ivBackground.setBackgroundResource(R.drawable.bg_tiyanjin_unuse);
                tvState.setText("已使用");
                tvState.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_99));
                tvStateGo.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_99));
//                vh.cover_tv.setVisibility(View.VISIBLE);
                tvStateGo.setText("已失效");
                break;
            case 2:// 选择优惠券的时候金额不可用
                ivBackground.setBackgroundResource(R.drawable.bg_tiyanjin_unuse);
//                vh.cover_tv.setVisibility(View.VISIBLE);
                tvState.setText("已过期");
                tvStateGo.setText("已失效");
                tvState.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_99));
                tvStateGo.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_99));
                break;
            case 0: //0:系统赠送 1:客服发放 3:活动赠送  (体验金：99 已激活 100 未激活)
                if (cb.getSource() == 99) {
                    tvStateGo.setText("已激活");
                    tvState.setText("已激活");
                } else {
                    tvStateGo.setText("未激活");
                    tvState.setText("未激活");
                }
                ivBackground.setBackgroundResource(R.drawable.bg_tiyanjin);
                tvState.setTextColor(LocalApplication.context.getResources().getColor(R.color.white));
                tvStateGo.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_red));
                break;
        }


    }

  /*  @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }*/
}
