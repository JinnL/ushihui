package com.ekabao.oil.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.HistoryProjectBean;

import java.util.List;

/**
 * 易卡宝  App
 *
 * @time 2018/7/23 15:54
 * Created by lj on 2018/7/23 15:54.
 */

public class HistoryProjectAdapter extends BaseRecyclerViewAdapter {
    List<HistoryProjectBean> mlist;

    public HistoryProjectAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);
        this.mlist = list;
    }

    @Override
    public void bindConvert(BaseViewHolder holder, int position, Object item, boolean isScrolling) {

        HistoryProjectBean bean = mlist.get(position);
        TextView tvReward = holder.getView(R.id.tv_reward);
        TextView tvInterest = holder.getView(R.id.tv_interest);
        TextView tvTime = holder.getView(R.id.tv_time);

        holder.setText(R.id.tv_title, bean.getFullName());

        int distributionMoney = bean.getDistributionMoney();

        if (distributionMoney == 0) {
            tvReward.setVisibility(View.GONE);
        } else {
            tvReward.setText(distributionMoney + "元现金奖励");
        }

        SpannableStringBuilder sp = new SpannableStringBuilder(bean.getRate() + "+" + bean.getActivityRate() + "%");
        //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        sp.setSpan(new AbsoluteSizeSpan(28, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小

        tvInterest.setText(sp);


        int deadline = bean.getDeadline();
        String msg = deadline + "";
        SpannableStringBuilder sp2 = new SpannableStringBuilder(msg + "天");
        sp2.setSpan(new AbsoluteSizeSpan(20, true), 0, sp2.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
        tvTime.setText(sp2);

        //5=募集中 6=募集成功 8=待还款 9=已还款

        switch (bean.getStatus()) {
            case 6:
            case 8:
                holder.setText(R.id.tv_status, "计息中");
                break;
            case 9:
                holder.setText(R.id.tv_status, "已还款");
                break;
        }


    }

   /* @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }*/
}
