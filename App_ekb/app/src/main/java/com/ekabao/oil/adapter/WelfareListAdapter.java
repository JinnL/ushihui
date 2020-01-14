package com.ekabao.oil.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.Activity;
import com.ekabao.oil.util.GlideRoundTransform;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 福利页面 的下面的活动
 */
public class WelfareListAdapter extends BaseAdapter {
    private Context context;
    private List<Activity.PageBean.RowsBean> lsct;
    private String flag;

    public WelfareListAdapter(Context context, List<Activity.PageBean.RowsBean> lsct) {
        this.context = context;
        this.lsct = lsct;
    }

    public void onDateChange(List<Activity.PageBean.RowsBean> lsct) {
        this.lsct = lsct;
        this.notifyDataSetChanged();
    }

    //只显示3个
    @Override
    public int getCount() {
        return lsct.size();
      /*  if (lsct.size() <= 3) {
            return lsct.size();
        } else {
            return 3;
        }*/
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

        ViewHolder myHolder = null;
        if (convertView == null) {
            //convertView = inflater.inflate(R.layout.item_project, null);
            convertView = View.inflate(context, R.layout.item_welfare_activity, null);
            myHolder = new ViewHolder(convertView);

            convertView.setTag(myHolder);
        } else {
            myHolder = (ViewHolder) convertView.getTag();
        }

        Activity.PageBean.RowsBean bean = lsct.get(position);

        myHolder.tvTitle.setText(bean.getTitle());
        myHolder.tvContent.setText(bean.getActivityDate());

        int status = bean.getStatus();

        switch (status) {
            case 1:
                myHolder.tvState.setBackgroundResource(R.drawable.bg_welfare_status_1);
                // myHolder.tvState.setText("进行中");
                // myHolder.tvState.setBackgroundColor(context.getResources().getColor(R.color.gold));
                myHolder.viewFail.setVisibility(View.GONE);
                break;
            case 2:
                myHolder.tvState.setBackgroundResource(R.drawable.bg_welfare_status_2);

                //  myHolder.tvState.setText("已结束");
                //  myHolder.tvState.setBackgroundColor(context.getResources().getColor(R.color.text_cc));
                myHolder.viewFail.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        GlideRoundTransform glideRoundTransform = new GlideRoundTransform(context, 10);
        //.placeholder(R.mipmap.ic_launcher)
        Glide.with(context).load(bean.getAppPic())
                .error(R.drawable.bg_activity_fail)
                .transform(glideRoundTransform)
                .into(myHolder.ivPicture);


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

    static class ViewHolder {
        @BindView(R.id.iv_picture)
        ImageView ivPicture;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.view_fail)
        View viewFail;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}

