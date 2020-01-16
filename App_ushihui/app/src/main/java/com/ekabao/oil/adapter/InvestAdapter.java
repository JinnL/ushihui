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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.bean.Invest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/1.
 */

public class InvestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Invest.IsSellingListBean> list;

    private Context ctx;
    private int HEADER_TYPE = 1;
    private int BONTOM_TYPE = 2; //历史项目

    public InvestAdapter(List<Invest.IsSellingListBean> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 1:
                //View
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);

                ViewHolder viewHolder = new ViewHolder(view);

                return viewHolder;

            case 2:
                //
                View viewTwo = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_footer, parent, false);
                TypeTwoHolder twoHolder = new TypeTwoHolder(viewTwo);
                return twoHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //根据Position去调用不同的Holder类设置数据

        if (position == list.size()) {


            TypeTwoHolder twoHolder = (TypeTwoHolder) holder;

            if (this.mOnItemClickLitener != null) {
                twoHolder.tvHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onHistoryItemClick(v);
                    }
                });
            }

        } else {

            ViewHolder myHolder = (ViewHolder) holder;
            Invest.IsSellingListBean bean = list.get(position);

            if (bean.getIsCash() == 0) {//红包标签
                myHolder.tvCashcoupon.setVisibility(View.GONE);
            } else {
                myHolder.tvCashcoupon.setVisibility(View.VISIBLE);
            }
            if (bean.getIsInterest() == 0) {//加息标签
                myHolder.tvRatecoupon.setVisibility(View.GONE);
            } else {
                myHolder.tvRatecoupon.setVisibility(View.VISIBLE);
            }
            if (bean.getIsDouble() != 0) {//翻倍标签
                if (bean.getIsDouble() == 0) {
                    myHolder.tvDoubleconpon.setVisibility(View.GONE);
                } else {
                    myHolder.tvDoubleconpon.setVisibility(View.VISIBLE);
                }
            } else {
                myHolder.tvDoubleconpon.setVisibility(View.GONE);
            }
            if (bean.getIsHot() != 0) {
                if (bean.getIsHot() == 0) {//热卖标签
                    myHolder.tvHot.setVisibility(View.GONE);
                } else {
                    myHolder.tvHot.setVisibility(View.VISIBLE);
                }
            } else {
                myHolder.tvHot.setVisibility(View.GONE);
            }
            if (bean.isRoundOffFlag()) {//扫尾
                myHolder.tvSaowei.setVisibility(View.VISIBLE);
            } else {
                myHolder.tvSaowei.setVisibility(View.GONE);
            }
            String tag = bean.getTag();//活动标签(可能多个) eg:"tag": "第一项,第二项"
            if (tag == null || tag.equals("")) {
                myHolder.tvTag1.setVisibility(View.GONE);
                myHolder.tvTag2.setVisibility(View.GONE);
                myHolder.tvTag3.setVisibility(View.GONE);
            } else {
                String[] tags = tag.split(",");
                if (tags.length == 1) {
                    myHolder.tvTag1.setVisibility(View.VISIBLE);
                    myHolder.tvTag1.setText(tags[0]);
                } else if (tags.length == 2) {
                    myHolder.tvTag1.setVisibility(View.VISIBLE);
                    myHolder.tvTag2.setVisibility(View.VISIBLE);
                    myHolder.tvTag1.setText(tags[0]);
                    myHolder.tvTag2.setText(tags[1]);
                } else if (tags.length == 3) {
                    myHolder.tvTag1.setVisibility(View.VISIBLE);
                    myHolder.tvTag2.setVisibility(View.VISIBLE);
                    myHolder.tvTag3.setVisibility(View.VISIBLE);
                    myHolder.tvTag1.setText(tags[0]);
                    myHolder.tvTag2.setText(tags[1]);
                    myHolder.tvTag3.setText(tags[2]);
                }
            }


            myHolder.tvTitle.setText(bean.getFullName());

            int distributionMoney = bean.getDistributionMoney();
            if (distributionMoney == 0) {
                myHolder.tvReward.setVisibility(View.GONE);
            } else {
                myHolder.tvReward.setVisibility(View.VISIBLE);
                myHolder.tvReward.setText(distributionMoney + "元现金奖励");
            }
            //int rate = bean.getRate();//  rate - 基础利率
            //int activityRate = bean.getActivityRate();//活动利率

            AssetManager assets = ctx.getAssets();
            //根据路径得到Typeface
            Typeface tf=Typeface.createFromAsset(assets, "DIN Medium.ttf");
            //设置字体
            myHolder.tvInterest.setTypeface(tf);

            SpannableStringBuilder sp = new SpannableStringBuilder(bean.getRate() + "+" + bean.getActivityRate() + "%");
            //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            sp.setSpan(new AbsoluteSizeSpan(30, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
            myHolder.tvInterest.setText(sp);



            int deadline = bean.getDeadline();

            String msg = deadline + "";
       /* String s = deadline + "";
        LogUtils.e(msg.length() + "//" + s.length());*/
            SpannableStringBuilder sp2 = new SpannableStringBuilder(msg + "天");
            //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            sp2.setSpan(new AbsoluteSizeSpan(20, true), 0, sp2.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
            myHolder.tvTime.setText(sp2);


            myHolder.tvMoney.setText(bean.getSurplusAmount() + "");


            if (this.mOnItemClickLitener != null) {
                myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(v, position);
                    }
                });
            }

        }
    }


    /* //返回显示Item的数量，我们集合大小只有4，但是我们要显示显示5个Item
     @Override
     public int getItemCount() {
         return list.size()+1;//我在Size的基础上+1
     }*/
    @Override
    public int getItemViewType(int position) {
        //当position为0的时候，那个我们需要显示的是轮播图，所以我定义两个常量来标识
        if (position == list.size()) {
            //==0的情况
            return BONTOM_TYPE;
        } else {
            //！==0的时候就返回全是相同的Item
            return HEADER_TYPE;
        }

    }


    @Override
    public int getItemCount() {
        return list.size() + 1;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_cashcoupon)
        TextView tvCashcoupon;
        @BindView(R.id.tv_ratecoupon)
        TextView tvRatecoupon;
        @BindView(R.id.tv_doubleconpon)
        TextView tvDoubleconpon;
        @BindView(R.id.tv_hot)
        TextView tvHot;
        @BindView(R.id.tv_saowei)
        TextView tvSaowei;
        @BindView(R.id.tv_tag1)
        TextView tvTag1;
        @BindView(R.id.tv_tag2)
        TextView tvTag2;
        @BindView(R.id.tv_tag3)
        TextView tvTag3;
        @BindView(R.id.tv_reward)
        TextView tvReward;
        @BindView(R.id.linearLayout1)
        LinearLayout linearLayout1;
        @BindView(R.id.tv_interest)
        TextView tvInterest;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_month)
        TextView tvMonth;
        @BindView(R.id.tv_limit)
        TextView tvLimit;
        @BindView(R.id.tv_surplus)
        TextView tvSurplus;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class TypeTwoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_history)
        TextView tvHistory;

        TypeTwoHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onHistoryItemClick(View view);
     /*   void onItemClick2(int  toPosition, int position);
*/

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


}
