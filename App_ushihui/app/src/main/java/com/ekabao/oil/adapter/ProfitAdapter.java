package com.ekabao.oil.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.Activity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/1.
 */

public class ProfitAdapter extends RecyclerView.Adapter<ProfitAdapter.ViewHolder> {

    private List<Activity.PageBean.RowsBean> list;

    private Context ctx;

    public ProfitAdapter(List<Activity.PageBean.RowsBean> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ViewHolder myHolder = (ViewHolder) holder;
        Activity.PageBean.RowsBean bean = list.get(position);

        myHolder.tvTitle.setText(bean.getTitle());
        myHolder.tvContent.setText(bean.getActivityDate());

        int status = bean.getStatus();

        switch (status) {
            case 1:
                myHolder.tvState.setText("进行中");
                myHolder.tvState.setBackgroundColor(ctx.getResources().getColor(R.color.gold));
                myHolder.viewFail.setVisibility(View.GONE);
                break;
            case 2:
                myHolder.tvState.setText("已结束");
                myHolder.tvState.setBackgroundColor(ctx.getResources().getColor(R.color.text_cc));
                myHolder.viewFail.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        //.placeholder(R.mipmap.ic_launcher)
        Glide.with(ctx).load(bean.getAppPic())
                .error(R.drawable.bg_activity_fail).into(myHolder.ivPicture);


        /*int distributionMoney = bean.getDistributionMoney();
        if (distributionMoney == 0) {
            myHolder.tvReward.setVisibility(View.GONE);
        } else {
            myHolder.tvReward.setText(distributionMoney + "元现金奖励");
        }
        int rate = bean.getRate();//  rate - 基础利率
        int activityRate = bean.getActivityRate();//活动利率

        SpannableStringBuilder sp = new SpannableStringBuilder(rate + "+" + activityRate + "%");
        //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        sp.setSpan(new AbsoluteSizeSpan(28, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
        myHolder.tvInterest.setText(sp);

        int deadline = bean.getDeadline();

        String msg = deadline + "";
       *//* String s = deadline + "";
        LogUtils.e(msg.length() + "//" + s.length());*//*
        SpannableStringBuilder sp2 = new SpannableStringBuilder(msg + "天");
        //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        sp2.setSpan(new AbsoluteSizeSpan(20, true), 0, sp2.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
        myHolder.tvTime.setText(sp2);


        myHolder.tvMoney.setText(bean.getSurplusAmount() + "");*/


        if (this.mOnItemClickLitener != null) {
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(v, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     *
     * */
    public void loadMore(List<Activity.PageBean.RowsBean> collection) {

        //List<Activity.PageBean.RowsBean> list;

        list.addAll(collection);
        notifyDataSetChanged();
        //notifyListDataSetChanged();

        //return this;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_picture)
        ImageView ivPicture;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.view_fail)
        View viewFail;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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
