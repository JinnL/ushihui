package com.ekabao.oil.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
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
 * @time 2018/7/19 17:12
 * Created by lj on 2018/7/19 17:12.
 */

public class CouponsUsedAdapter extends BaseRecyclerViewAdapter {
    List<CouponsBean> lslbs;
    private String amount;//

    public CouponsUsedAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);
        this.lslbs = list;
    }

    public CouponsUsedAdapter(RecyclerView view, List list, int itemLayoutId, String money) {
        super(view, list, itemLayoutId);
        this.lslbs = list;
        this.amount = money;
    }


    @Override
    public void bindConvert(BaseViewHolder holder, int position, Object item, boolean isScrolling) {
        CouponsBean cb = lslbs.get(position);

        ImageView ivBackground = holder.getView(R.id.iv_background);
        TextView tvMoney = holder.getView(R.id.tv_money);
        TextView tvLimitMoney = holder.getView(R.id.tv_limit_money);
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvLimitDay = holder.getView(R.id.tv_limit_day);
        TextView tvTime = holder.getView(R.id.tv_time);
        TextView tvState = holder.getView(R.id.tv_state);


        ivBackground.setBackgroundResource(R.drawable.bg_fanxian);
        //"返现红包-"
        tvName.setText(cb.getName());
        //vh.tv_wenan1.setText(cb.getRemark());//发放途径
        if (cb.getEnableAmount()==0){
            tvLimitMoney.setVisibility(View.GONE);

        }else {
            tvLimitMoney.setVisibility(View.VISIBLE);
            tvLimitMoney.setText("满" + StringCut.getNumKbs(cb.getEnableAmount()) + "元使用");

        }
      //  tvLimitMoney.setText("满" + StringCut.getNumKbs(cb.getEnableAmount()) + "元使用");
//"单笔出借满"+StringCut.getNumKbs(cb.getEnableAmount())+"元，出借期限≥"+cb.getProductDeadline()+"天"

        tvLimitDay.setText("适用套餐时间≥" + cb.getProductDeadline() + "个月");

        String xian_h = "￥" + StringCut.getNumKbs(cb.getAmount());
        SpannableString ss = new SpannableString(xian_h);
        ss.setSpan(new RelativeSizeSpan(0.6f), 0, 1, TypedValue.COMPLEX_UNIT_PX);

        tvMoney.setText(xian_h);

        // tvMoney.setText("￥" + StringCut.getNumKbs(cb.getAmount()));
        if (cb.getExpireDate() != 0) {
            tvTime.setText("有效期至：" + StringCut.getDateYearToString(cb.getExpireDate()));
        } else {
            // vh.tv_dd.setText("");
            tvTime.setText("永久有效");
        }


        switch (cb.getType()) {
            case 1:// 返现
                tvLimitDay.setText("加油套餐≥" + cb.getProductDeadline() + "个月");
                break;

            case 2:// 加息
                tvLimitDay.setText("油卡直冲");

                break;
            case 4:// 翻倍券

                tvLimitDay.setText("话费套餐≥" + cb.getProductDeadline() + "个月");
            case 5: //"话费直冲"
                tvLimitDay.setText("话费直冲");
                break;
            default:
                tvLimitDay.setText("");
                break;

        }

        switch (cb.getStatus()) {
            case 3:// 选择优惠券的时候金额不可用
                ivBackground.setBackgroundResource(R.drawable.bg_unuse);
                tvState.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_99));
//                vh.cover_tv.setVisibility(View.VISIBLE);
                break;
            case 1:// 选择优惠券的时候金额不可用
                ivBackground.setBackgroundResource(R.drawable.bg_unuse);
                tvState.setText("已使用");
                tvState.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_99));
//                vh.cover_tv.setVisibility(View.VISIBLE);
                break;
            case 2:// 选择优惠券的时候金额不可用
                ivBackground.setBackgroundResource(R.drawable.bg_unuse);
//                vh.cover_tv.setVisibility(View.VISIBLE);
                tvState.setText("已过期");
                tvState.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_99));
                break;
            default:
                tvState.setText("立即使用");
                //tvState.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_red));
                break;
        }


        if (!TextUtils.isEmpty(amount)) {
            //vh.tv_tab.setVisibility(View.GONE);
            for (int i = 0; i < lslbs.size(); i++) {
                if (Double.valueOf(amount) < (lslbs.get(position).getEnableAmount())) {
                    ivBackground.setBackgroundResource(R.drawable.bg_unuse);
                    lslbs.get(position).setStatus(3);
                    tvState.setText("无法使用");
                } else {
                    //  vh.ll_conpons_bg.setBackgroundResource(R.mipmap.bg_conpons_red);
                    lslbs.get(position).setStatus(0);
                    tvState.setText("立即使用");
                }
            }
        }
        tvState.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_99));
    }


}
