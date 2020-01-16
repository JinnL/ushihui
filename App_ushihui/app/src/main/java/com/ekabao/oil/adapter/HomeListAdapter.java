package com.ekabao.oil.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.bean.HomeInfoList;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/*
* 描述：金服列表项2.0版
* */
public class HomeListAdapter extends BaseAdapter {
    private Context context;
    private List<HomeInfoList.IsSellingListBean> lsct;
    private String flag;
    private LayoutInflater inflater;

    public HomeListAdapter(Context context, List<HomeInfoList.IsSellingListBean> lsct, String flag) {
        this.context = context;
        this.lsct = lsct;
        this.flag = flag;
        inflater = LayoutInflater.from(context);
    }

    public void onDateChange(List<HomeInfoList.IsSellingListBean> lsct) {
        this.lsct = lsct;
        this.notifyDataSetChanged();
    }

    //只显示3个
    @Override
    public int getCount() {
        if (lsct.size() <= 3) {
            return lsct.size();
        } else {
            return 3;
        }
    }

    @Override
    public Object getItem(int position) {
        return lsct.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       /* ViewHolder1 holder1 = null;
        if (convertView == null) {
            inflate = View.inflate(ETouApp.context, R.layout.item_home_search, null);
            holder1 = new ViewHolder1(inflate);
            convertView = inflate;
            convertView.setTag(holder1);
        } else {
            holder1 = (ViewHolder1) convertView.getTag();
        }*/

        ViewHolder vh = null;
        if (convertView == null) {
            //convertView = inflater.inflate(R.layout.item_project, null);
            convertView =  View.inflate(context, R.layout.item_home_project, null);
            vh = new ViewHolder(convertView);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final HomeInfoList.IsSellingListBean lb = lsct.get(position);

        if (lb.getIsCash() == 0) {//红包标签
            vh.tvCashcoupon.setVisibility(View.GONE);
        } else {
            vh.tvCashcoupon.setVisibility(View.VISIBLE);
        }
        if (lb.getIsInterest() == 0) {//加息标签
            vh.tvRatecoupon.setVisibility(View.GONE);
        } else {
            vh.tvRatecoupon.setVisibility(View.VISIBLE);
        }
        if (lb.getIsDouble() != 0) {//翻倍标签
            if (lb.getIsDouble() == 0) {
                vh.tvDoubleconpon.setVisibility(View.GONE);
            } else {
                vh.tvDoubleconpon.setVisibility(View.VISIBLE);
            }
        } else {
            vh.tvDoubleconpon.setVisibility(View.GONE);
        }
        if (lb.getIsHot() != 0) {
            if (lb.getIsHot() == 0) {//热卖标签
                vh.tvHot.setVisibility(View.GONE);
            } else {
                vh.tvHot.setVisibility(View.VISIBLE);
            }
        } else {
            vh.tvHot.setVisibility(View.GONE);
        }
        if (lb.isRoundOffFlag()) {//扫尾
            vh.tvSaowei.setVisibility(View.VISIBLE);
        } else {
            vh.tvSaowei.setVisibility(View.GONE);
        }

        String tag = lb.getTag();//活动标签(可能多个) eg:"tag": "第一项,第二项"
        if (tag == null || tag.equals("")) {
            vh.tvTag1.setVisibility(View.GONE);
            vh.tvTag2.setVisibility(View.GONE);
            vh.tvTag3.setVisibility(View.GONE);
        } else {
            String[] tags = tag.split(",");
            if (tags.length == 1) {
                vh.tvTag1.setVisibility(View.VISIBLE);
                vh.tvTag1.setText(tags[0]);
            } else if (tags.length == 2) {
                vh.tvTag1.setVisibility(View.VISIBLE);
                vh.tvTag2.setVisibility(View.VISIBLE);
                vh.tvTag1.setText(tags[0]);
                vh.tvTag2.setText(tags[1]);
            } else if (tags.length == 3) {
                vh.tvTag1.setVisibility(View.VISIBLE);
                vh.tvTag2.setVisibility(View.VISIBLE);
                vh.tvTag3.setVisibility(View.VISIBLE);
                vh.tvTag1.setText(tags[0]);
                vh.tvTag2.setText(tags[1]);
                vh.tvTag3.setText(tags[2]);
            }
        }

        //新手列表item

        vh.tvTitle.setText(lb.getFullName());

        int distributionMoney = lb.getDistributionMoney();
        if (distributionMoney == 0) {
            vh.tvReward.setVisibility(View.GONE);
        } else {
            vh.tvReward.setVisibility(View.VISIBLE);
            vh.tvReward.setText(distributionMoney + "元现金奖励");
        }
        //int rate = lb.getRate();//  rate - 基础利率
        //int activityRate = lb.getActivityRate();//活动利率

        AssetManager assets = context.getAssets();
        //根据路径得到Typeface
        Typeface tf = Typeface.createFromAsset(assets, "DIN Medium.ttf");
        //设置字体
        vh.tvInterest.setTypeface(tf);

        SpannableStringBuilder sp = new SpannableStringBuilder(lb.getRate() + "+" + lb.getActivityRate() + "%");
        //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        sp.setSpan(new AbsoluteSizeSpan(30, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
        vh.tvInterest.setText(sp);


        int deadline = lb.getDeadline();

        String msg = deadline + "";
       /* String s = deadline + "";
        LogUtils.e(msg.length() + "//" + s.length());*/
        SpannableStringBuilder sp2 = new SpannableStringBuilder(msg + "天");
        //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        sp2.setSpan(new AbsoluteSizeSpan(20, true), 0, sp2.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
        vh.tvTime.setText(sp2);


        vh.tvMoney.setText(lb.getSurplusAmount() + "");


        return convertView;
    }

    private void setDefault(TextView rate, TextView title, TextView deadline) {
        rate.setTextColor(Color.parseColor("#f18583"));
        title.setTextColor(Color.parseColor("#666666"));
        deadline.setTextColor(Color.parseColor("#8c8c8c"));
    }


    //将对应的秒值转成一年中的第几天：DAY_OF_YEAR
    private int time2DayOfYear(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        int dayOfYear = c.get(Calendar.DAY_OF_YEAR);
        return dayOfYear;
    }

    /*进度条动画*/
    private void proAnimator(int pert, final ProgressBar progressbar_pert) {
        ValueAnimator animator = ValueAnimator.ofInt(0, pert);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                progressbar_pert.setSecondaryProgress(progress);
            }
        });
        animator.setDuration(2000);
        animator.start();
    }

    class ViewHolder {
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
            ButterKnife.bind(this, view);
        }
    }
}

