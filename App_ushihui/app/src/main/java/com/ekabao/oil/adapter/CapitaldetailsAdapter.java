package com.ekabao.oil.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.CapitaldetailsBean;
import com.ekabao.oil.util.StringCut;

import java.util.List;

/**
 * $desc$
 *  资金明细
 * @time $data$ $time$
 * Created by Administrator on 2018/7/16.
 */

public class CapitaldetailsAdapter extends BaseRecyclerViewAdapter {

    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;

    private List<CapitaldetailsBean> list;

    public CapitaldetailsAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);
        this.list= list;
    }

    @Override
    public void bindConvert(BaseViewHolder holder, int position, Object item, boolean isScrolling) {

        CapitaldetailsBean bean = list.get(position);


        TextView tvMonth = holder.getView(R.id.tv_month);
        RelativeLayout rlContent = holder.getView(R.id.rl_content);
        String month = "";
        long time = bean.getAddTime();

        if (StringCut.isMonth(time)) {
            month = "本月";
            holder.setText(R.id.tv_month,"本月");
        }else{
            holder.setText(R.id.tv_month,StringCut.getMonth(time));
            month = StringCut.getMonth(time);
        }

        if (position == 0) {
            tvMonth.setVisibility(View.VISIBLE);
            holder.itemView.setTag(FIRST_STICKY_VIEW);
        } else {
            if (!TextUtils.equals(StringCut.getMonth(bean.getAddTime()), StringCut.getMonth(list.get(position - 1).getAddTime()))) {
                tvMonth.setVisibility(View.VISIBLE);
                holder.itemView.setTag(HAS_STICKY_VIEW);
            } else {
                tvMonth.setVisibility(View.GONE);
                holder.itemView.setTag(NONE_STICKY_VIEW);
            }
        }
        holder.itemView.setContentDescription(month);


        tvMonth.setVisibility(View.GONE);


        //0 支出 1 收入
        holder.setText(R.id.tv_time, StringCut.getDateTimeToStringheng(bean.getAddTime()));
        holder.setText(R.id.tv_name,bean.getRemark());

        TextView tvMoney = holder.getView(R.id.tv_money);

        if (bean.getType()==0){
            //holder.setText(R.id.tv_money,"-"+bean.getAmount());
          //  tvMoney.setTextColor(0x444444);
            tvMoney.setText("-"+bean.getAmount());
          //  tvMoney.setTextColor(0x444444);
            tvMoney.setTextColor(Color.parseColor("#444444"));

        }else {

           // holder.setText(R.id.tv_money,"+"+bean.getAmount());
           // tvMoney.setTextColor(0xFF623D);
            tvMoney.setText("+"+bean.getAmount());
            tvMoney.setTextColor(Color.parseColor("#FF623D"));
        }

        holder.setText(R.id.tv_surplusAmount,"余额"+bean.getBalance());


    }

   /* @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }*/



}
