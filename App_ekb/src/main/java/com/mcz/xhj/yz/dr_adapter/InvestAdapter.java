package com.mcz.xhj.yz.dr_adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.InvestListBean;
import com.mcz.xhj.yz.dr_util.stringCut;

import java.util.List;

/*
* 描述：已募集的金服列表项2.0版（已售罄和已还款）
* */

public class InvestAdapter extends BaseAdapter {
	private Context context;
	private List<InvestListBean> lsct;
	private LayoutInflater inflater;
	private int flag;


	public InvestAdapter(Context context, List<InvestListBean> lsct ,int flag) {
		this.context = context;
		this.lsct = lsct;
		this.flag = flag;

		inflater = LayoutInflater.from(context);
	}

	public void onDateChange(List<InvestListBean> lsct) {
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

	private class ViewHolder{
		private TextView title;
		private TextView rate;
		private TextView deadline;
		private TextView tv_per_invested;
		private TextView tv_cashcoupon;
		private TextView tv_ratecoupon;
		private TextView tv_doubleconpon;
		private TextView tv_hot;
		private TextView tv_saowei;
		private TextView tv_tag1;
		private TextView tv_tag2;
		private TextView tv_tag3;
		private TextView tv_ciri;
		private TextView tv_baozhang;
		private ImageView iv_type;
		private LinearLayout ll_danact;
		private LinearLayout ll_activity;
		private ImageView iv_activity;
		private ProgressBar progressbar;
	}
	private String type = null;
	private Integer status;
	private double activityRate = 0;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.item_invest_twolevel, null);
			vh=new ViewHolder();
			vh.title=(TextView) convertView.findViewById(R.id.title);
			vh.rate=(TextView) convertView.findViewById(R.id.rate);
			vh.deadline=(TextView) convertView.findViewById(R.id.deadline);
			vh.tv_cashcoupon = (TextView) convertView.findViewById(R.id.tv_cashcoupon);
			vh.tv_ratecoupon = (TextView) convertView.findViewById(R.id.tv_ratecoupon);
			vh.tv_doubleconpon = (TextView) convertView.findViewById(R.id.tv_doubleconpon);
			vh.tv_hot = (TextView) convertView.findViewById(R.id.tv_hot);
			vh.tv_saowei = (TextView) convertView.findViewById(R.id.tv_saowei);
			vh.tv_tag1 = (TextView) convertView.findViewById(R.id.tv_tag1);
			vh.tv_tag2 = (TextView) convertView.findViewById(R.id.tv_tag2);
			vh.tv_tag3 = (TextView) convertView.findViewById(R.id.tv_tag3);
			vh.tv_per_invested=(TextView) convertView.findViewById(R.id.tv_per_invested);
			vh.progressbar = (ProgressBar) convertView.findViewById(R.id.progressbar_pert);
			vh.iv_type=(ImageView) convertView.findViewById(R.id.iv_type);

			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		if(flag == 1){
			vh.tv_per_invested.setText("已还款");
		} else {
			vh.tv_per_invested.setText("已售罄");
		}
		final InvestListBean lb=lsct.get(position);
		vh.title.setText(lb.getFullName());
		vh.rate.setText(stringCut.getNumKbs(lb.getRate()+Double.valueOf(lb.getActivityRate()))+"%");
		SpannableString ss_Deadline = new SpannableString(lb.getDeadline()+"天");
		ss_Deadline.setSpan(new RelativeSizeSpan(1f), 0, ss_Deadline.length()-1, TypedValue.COMPLEX_UNIT_PX);
		ss_Deadline.setSpan(new RelativeSizeSpan(0.7f), ss_Deadline.length()-1, ss_Deadline.length(),TypedValue.COMPLEX_UNIT_PX);
		vh.deadline.setText(ss_Deadline);

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
		if(lb.isRoundOff() == true){//扫尾
			vh.tv_saowei.setVisibility(View.VISIBLE);
		} else {
			vh.tv_saowei.setVisibility(View.GONE);
		}
		String tag = lb.getTag();//活动标签(可能多个) eg:"tag": "第一项,第二项"
		if(tag != null && !tag.equals("")){
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
		}else{
			vh.tv_tag1.setVisibility(View.GONE);
			vh.tv_tag2.setVisibility(View.GONE);
			vh.tv_tag3.setVisibility(View.GONE);
		}

		int pert = (int) lb.getPert();
		vh.progressbar.setMax(100);
		status = lb.getStatus();
		type = lb.getBillType();
		//判断银票和商票
//		getType(vh.iv_type,type,status);
		if(status==8){//已计息
			vh.progressbar.setProgress(pert);
			vh.tv_cashcoupon.setTextColor(Color.parseColor("#cccccc"));
			vh.tv_cashcoupon.setBackgroundResource(R.drawable.bg_corner_grayline);
			//cpb_rate.setProgressTextFormatPattern("待还款");

		}
		else if(status==9){//已回款
			vh.progressbar.setProgress(pert);
			vh.tv_cashcoupon.setTextColor(Color.parseColor("#cccccc"));
			vh.tv_cashcoupon.setBackgroundResource(R.drawable.bg_corner_grayline);
			//cpb_rate.setProgressTextFormatPattern("已还款");

		}
		else if(status==6){//募集成功
			vh.progressbar.setProgress(pert);
			vh.tv_cashcoupon.setTextColor(Color.parseColor("#cccccc"));
			vh.tv_cashcoupon.setBackgroundResource(R.drawable.bg_corner_grayline);
			//cpb_rate.setProgressTextFormatPattern("抢光了");

		}
		else if(status==5){

			vh.rate.setTextColor(Color.parseColor("#ec5c59"));
			vh.title.setTextColor(Color.parseColor("#333333"));
			vh.deadline.setTextColor(Color.parseColor("#666666"));

			if(pert<1&&pert>0){
				//cpb_rate.setProgress(1);
				vh.progressbar.setProgress(1);
			}
			else if(pert>99&&pert<100){
				//proAnimator(99,vh.progressbar);
			}
			else{
				//proAnimator((int)pert,vh.progressbar);

			}
		}else {
			vh.progressbar.setProgress(0);
		}
		if(lb.getMaxActivityCoupon()!=null){

			if(lb.getIsEgg()==1){

			}
			else if (lb.getIsEgg()==2){

			}
		}else{

		}
		return convertView;
	}

	private void getType(ImageView iv,String type,Integer status){
		//判断银票和商票
		if(type!=null&&!type.equalsIgnoreCase("")){
			if(type.equalsIgnoreCase("1")){
				iv.setVisibility(View.VISIBLE);
				if(status==5){
					iv.setImageResource(R.mipmap.icon_trade_p);
				}else{
					iv.setImageResource(R.mipmap.icon_trade_p_gray);
				}
			}else if(type.equalsIgnoreCase("2")){
				iv.setVisibility(View.VISIBLE);
				if(status==5){
					iv.setImageResource(R.mipmap.icon_bank_p);
				}else{
					iv.setImageResource(R.mipmap.icon_bank_p_gray);
				}
			}else{
				iv.setVisibility(View.GONE);
			}
		}else{
			iv.setVisibility(View.GONE);
		}
	}


	/*进度条动画*/
	private void proAnimator(final int pert,final ProgressBar progressbar) {
		ValueAnimator animator = ValueAnimator.ofInt(0, pert);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int progress = (int) animation.getAnimatedValue();
				progressbar.setProgress(progress);
			}
		});
		animator.setDuration(2000);
		animator.start();
	}
	
}
