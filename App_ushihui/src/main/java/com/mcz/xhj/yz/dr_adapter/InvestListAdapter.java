package com.mcz.xhj.yz.dr_adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.InvestListBean2;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.WaveProgressView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/*
* 描述：金服列表项2.0版
* */
public class InvestListAdapter extends BaseAdapter {
    private Context context;
    private List<InvestListBean2> lsct;
    private String flag;
    private LayoutInflater inflater;

    public InvestListAdapter(Context context, List<InvestListBean2> lsct, String flag) {
        this.context = context;
        this.lsct = lsct;
        this.flag = flag;
        inflater = LayoutInflater.from(context);
    }

    public void onDateChange(List<InvestListBean2> lsct) {
        this.lsct = lsct;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lsct.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return lsct.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class ViewHolder {
        private TextView title;
        private TextView rate;
        private TextView deadline;
        private TextView tv_cashcoupon;
        private TextView tv_ratecoupon;
        private TextView tv_doubleconpon;
        private TextView tv_hot;
        private TextView tv_saowei;
        private TextView tv_tag1;
        private TextView tv_tag2;
        private TextView tv_tag3;
        private WaveProgressView myWaveView;
        private LinearLayout ll_presell_time;
        private TextView tv_presell_time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.invest_item, null);
            vh = new ViewHolder();
            vh.title = (TextView) convertView.findViewById(R.id.title);
            vh.rate = (TextView) convertView.findViewById(R.id.rate);
            vh.deadline = (TextView) convertView.findViewById(R.id.deadline);
            vh.tv_cashcoupon = (TextView) convertView.findViewById(R.id.tv_cashcoupon);
            vh.tv_ratecoupon = (TextView) convertView.findViewById(R.id.tv_ratecoupon);
            vh.tv_doubleconpon = (TextView) convertView.findViewById(R.id.tv_doubleconpon);
            vh.tv_hot = (TextView) convertView.findViewById(R.id.tv_hot);
            vh.tv_saowei = (TextView) convertView.findViewById(R.id.tv_saowei);
            vh.tv_tag1 = (TextView) convertView.findViewById(R.id.tv_tag1);
            vh.tv_tag2 = (TextView) convertView.findViewById(R.id.tv_tag2);
            vh.tv_tag3 = (TextView) convertView.findViewById(R.id.tv_tag3);
            vh.myWaveView = (WaveProgressView) convertView.findViewById(R.id.myWaveView);
            vh.ll_presell_time = (LinearLayout) convertView.findViewById(R.id.ll_presell_time);
            vh.tv_presell_time = (TextView) convertView.findViewById(R.id.tv_presell_time);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final InvestListBean2 lb = lsct.get(position);
        if (lb.getIsCash() == 0) {//红包标签
            vh.tv_cashcoupon.setVisibility(View.GONE);
        } else {
            vh.tv_cashcoupon.setVisibility(View.VISIBLE);
        }
        if (lb.getIsInterest() == 0) {//加息标签
            vh.tv_ratecoupon.setVisibility(View.GONE);
        } else {
            vh.tv_ratecoupon.setVisibility(View.VISIBLE);
        }
        if (lb.getIsDouble() != null) {//翻倍标签
            if (lb.getIsDouble() == 0) {
                vh.tv_doubleconpon.setVisibility(View.GONE);
            } else {
                vh.tv_doubleconpon.setVisibility(View.VISIBLE);
            }
        } else {
            vh.tv_doubleconpon.setVisibility(View.GONE);
        }
        if(lb.getIsHot() != null){
            if (lb.getIsHot() == 0) {//热卖标签
                vh.tv_hot.setVisibility(View.GONE);
            } else {
                vh.tv_hot.setVisibility(View.VISIBLE);
            }
        }else{
            vh.tv_hot.setVisibility(View.GONE);
        }
        if(lb.isRoundOffFlag()){//扫尾
            vh.tv_saowei.setVisibility(View.VISIBLE);
        } else {
            vh.tv_saowei.setVisibility(View.GONE);
        }
        String tag = lb.getTag();//活动标签(可能多个) eg:"tag": "第一项,第二项"
        if(tag == null || tag.equals("")){
            vh.tv_tag1.setVisibility(View.GONE);
            vh.tv_tag2.setVisibility(View.GONE);
            vh.tv_tag3.setVisibility(View.GONE);
        }else{
            String[] tags = tag.split(",");
            if(tags.length == 1){
                vh.tv_tag1.setVisibility(View.VISIBLE);
                vh.tv_tag1.setText(tags[0]);
            }else if(tags.length == 2){
                vh.tv_tag1.setVisibility(View.VISIBLE);
                vh.tv_tag2.setVisibility(View.VISIBLE);
                vh.tv_tag1.setText(tags[0]);
                vh.tv_tag2.setText(tags[1]);
            }else if(tags.length == 3){
                vh.tv_tag1.setVisibility(View.VISIBLE);
                vh.tv_tag2.setVisibility(View.VISIBLE);
                vh.tv_tag3.setVisibility(View.VISIBLE);
                vh.tv_tag1.setText(tags[0]);
                vh.tv_tag2.setText(tags[1]);
                vh.tv_tag3.setText(tags[2]);
            }
        }

        //新手列表item

        //智盈宝item
        if(flag.equals("isSelling") || flag.equals("newhand")){
            vh.myWaveView.setVisibility(View.VISIBLE);
            vh.ll_presell_time.setVisibility(View.GONE);
            int pert = (int) lb.getPert();
            //LogUtils.i("普通标的百分比percent："+pert);
            vh.myWaveView.setWaveColor("#D33A31");
            vh.myWaveView.setText("#FFFFFF", 32);
            vh.myWaveView.setCurrent(pert, pert + "%");
        }
        //预售计划item
        if (flag.equals("willSell")){
            vh.myWaveView.setVisibility(View.GONE);
            vh.ll_presell_time.setVisibility(View.VISIBLE);
            String startDate = lb.getStartDate();
            long serverMillis = Long.valueOf(startDate);
            int dayOfYear_server = time2DayOfYear(serverMillis);
            //获取当前系统的时间毫秒值和对应的DAY_OF_YEAR
            Calendar calendar = Calendar.getInstance();
            long timeInMillis = calendar.getTimeInMillis();
            int dayOfYear_cur = time2DayOfYear(timeInMillis);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
            String date = simpleDateFormat.format(new Date(serverMillis));
            String date2 = simpleDateFormat2.format(new Date(serverMillis));
            //LogUtils.i("--->当前毫秒值timeInMillis："+timeInMillis+" ,服务器日期date："+date+" ,dayOfYear_server："+dayOfYear_server+" ,dayOfYear_cur："+dayOfYear_cur);
            if(dayOfYear_server - dayOfYear_cur == 0){
                vh.tv_presell_time.setText("今天"+date2);
            } else if(dayOfYear_server - dayOfYear_cur == 1){
                vh.tv_presell_time.setText("明天"+date2);
            }else if(dayOfYear_server - dayOfYear_cur == 2){
                vh.tv_presell_time.setText("后天"+date2);
            } else {
                vh.tv_presell_time.setText(date);
                vh.tv_presell_time.setTextSize(14);
            }
            //预售时间到了，显示圆球进度
            if(serverMillis - timeInMillis <= 0){
                vh.myWaveView.setVisibility(View.VISIBLE);
                vh.ll_presell_time.setVisibility(View.GONE);
                int pert = (int) lb.getPert();
                //LogUtils.i("普通标的百分比percent："+pert);
                vh.myWaveView.setWaveColor("#D33A31");
                vh.myWaveView.setText("#FFFFFF", 32);
                vh.myWaveView.setCurrent(pert, pert + "%");
            }

        }
        vh.title.setText(lb.getFullName());
        if(flag.equals("newhand")){
            vh.rate.setText(stringCut.getNumKbs(lb.getActivityRate() + lb.getRate()) + "%");
        }else{
            vh.rate.setText(stringCut.getNumKbs(lb.getMaxRate()) + "%");
        }
        vh.deadline.setText(lb.getDeadline() + "天");

        return convertView;
    }

    private void setDefault(TextView rate, TextView title, TextView deadline) {
        rate.setTextColor(Color.parseColor("#f18583"));
        title.setTextColor(Color.parseColor("#666666"));
        deadline.setTextColor(Color.parseColor("#8c8c8c"));
    }


    //将对应的秒值转成一年中的第几天：DAY_OF_YEAR
    private int time2DayOfYear(long time){
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
}

