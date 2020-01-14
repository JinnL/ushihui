package com.mcz.xhj.yz.dr_adapter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mcz.xhj.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcz.xhj.yz.dr_app.BackRecondAct;
import com.mcz.xhj.yz.dr_app.WebViewActivity;
import com.mcz.xhj.yz.dr_bean.MyInvestListBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.stringCut;

public class MyInvestAdapter extends BaseExpandableListAdapter {
	
	private Context context;
	private List<MyInvestListBean> lsct;
	private LayoutInflater inflater;
	private String dateString;
	private Calendar calendar;
	private Date date;
	public MyInvestAdapter(Context context, List<MyInvestListBean> lsct) {
		super();
		this.context = context;
		this.lsct = lsct;
		inflater = LayoutInflater.from(context);
		date=new Date();//取时间
//		calendar = new GregorianCalendar();
//		calendar.setTime(date);
//		calendar.add(calendar.DATE,0);//把日期往后增加一天.整数往后推,负数往前移动
//		date=calendar.getTime(); //这个时间就是日期往后推一天的结果
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
//		dateString = formatter.format(date);
	}
	
	public void onDateChange(List<MyInvestListBean> lsct) {
		this.lsct = lsct;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return lsct.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return lsct.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return lsct.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}
	
	private class ViewHolder{
		private TextView tv_name;
		private TextView tv_investdate;
		private TextView tv_money;
		private TextView tv_deadline;
		private TextView tv_rate;
		private TextView tv_repaytype;
		private TextView tv_repaydate;
		private TextView tv_benjin;
		private TextView tv_lixi;
		private TextView tv_benjins;
		private TextView tv_lixis;
		private TextView tv_conpons;
		private TextView tv_conponstext;
		private TextView tv_xieyi;
		private TextView tv_status;
		private TextView tv_buildtime;
		private TextView tv_recond;
		private TextView tv_xutou_flag;

		private ImageView imageView;
		private LinearLayout ll_1;
		private LinearLayout ll_2;
		private LinearLayout ll_3;
		private LinearLayout ll_conpons;
		private LinearLayout ll_buildtime;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_myinvest, null);//=====================
			vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			vh.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
			vh.tv_xutou_flag = (TextView) convertView.findViewById(R.id.tv_xutou_flag);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		MyInvestListBean lb = lsct.get(groupPosition);
		vh.tv_name.setText(lb.getFullName());
		String status = lb.getStatus();
		if(lb.getContinuePeriod()!=null){
			vh.tv_xutou_flag.setVisibility(View.VISIBLE);
			vh.tv_xutou_flag.setText("到期续投"+lb.getContinuePeriod()+"天标");
		}else{
			vh.tv_xutou_flag.setVisibility(View.GONE);
		}
		if(status.equalsIgnoreCase("0")){
			vh.tv_status.setVisibility(View.VISIBLE);
//			Date da = new Date(Long.parseLong(lb.getInvestTime()));
			try {
				if(stringCut.getDate(Long.parseLong(lb.getInvestTime())).before(stringCut.getDate(System.currentTimeMillis()))){
					vh.tv_status.setText("计息中");
				}else{
					vh.tv_status.setText("投资成功");
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		else{
			vh.tv_status.setVisibility(View.GONE);
			}
		if(isExpanded){
			convertView.findViewById(R.id.rl_title).setBackgroundColor(Color.parseColor("#f2f2f2"));
			convertView.findViewById(R.id.imageView).setBackgroundResource(R.mipmap.icon_rightto);
			convertView.findViewById(R.id.imageView).setRotation(90);
		}else{
			convertView.findViewById(R.id.rl_title).setBackgroundColor(Color.parseColor("#ffffff"));
			convertView.findViewById(R.id.imageView).setBackgroundResource(R.mipmap.icon_rightto);
			convertView.findViewById(R.id.imageView).setRotation(0);
			}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(groupPosition<childPosition){
			return null;
		}
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_myinvest_down, null);//=================
			vh.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			vh.tv_investdate = (TextView) convertView.findViewById(R.id.tv_investdate);
			vh.tv_deadline = (TextView) convertView.findViewById(R.id.tv_deadline);
			vh.tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
			vh.tv_repaytype = (TextView) convertView.findViewById(R.id.tv_repaytype);
			vh.tv_repaydate = (TextView) convertView.findViewById(R.id.tv_repaydate);
			vh.tv_benjin = (TextView) convertView.findViewById(R.id.tv_benjin);
			vh.tv_lixi = (TextView) convertView.findViewById(R.id.tv_lixi);
			vh.tv_benjins = (TextView) convertView.findViewById(R.id.tv_benjins);
			vh.tv_lixis = (TextView) convertView.findViewById(R.id.tv_lixis);
			vh.tv_conpons = (TextView) convertView.findViewById(R.id.tv_conpons);
			vh.tv_conponstext = (TextView) convertView.findViewById(R.id.tv_conponstext);
			vh.tv_xieyi = (TextView) convertView.findViewById(R.id.tv_xieyi);
			vh.tv_buildtime = (TextView) convertView.findViewById(R.id.tv_buildtime);
			vh.tv_recond = (TextView) convertView.findViewById(R.id.tv_recond);
			vh.ll_1 = (LinearLayout) convertView.findViewById(R.id.ll_1);
			vh.ll_2 = (LinearLayout) convertView.findViewById(R.id.ll_2);
			vh.ll_3 = (LinearLayout) convertView.findViewById(R.id.ll_3);
			vh.ll_conpons = (LinearLayout) convertView.findViewById(R.id.ll_conpons);
			vh.ll_buildtime = (LinearLayout) convertView.findViewById(R.id.ll_buildtime);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		final MyInvestListBean lb = lsct.get(groupPosition);
		vh.tv_deadline.setText(lb.getDeadline()+"天");
		vh.tv_investdate.setText(stringCut.getDateToString(Long.parseLong(lb.getInvestTime())));
		vh.tv_money.setText(stringCut.getNumKb(lb.getAmount())+"元");
		double exRate = lb.getSpecialRate() + Double.parseDouble(lb.getActivityRate());
		if(exRate!=0){
			vh.tv_rate.setText(stringCut.getNumKbs(lb.getRate())+"%+"+exRate+"%");
		}else{
			vh.tv_rate.setText(stringCut.getNumKbs(lb.getRate())+"%");
		}
		if(lb.getDeadline().equalsIgnoreCase("1")){
			vh.tv_xieyi.setVisibility(View.GONE);
		}else{
			vh.tv_xieyi.setVisibility(View.VISIBLE);
		}
		if(lb.getEstablish()!=null&&!lb.getEstablish().equalsIgnoreCase("")){
			vh.ll_buildtime.setVisibility(View.VISIBLE);
			vh.tv_buildtime.setText(stringCut.getDateToString(Long.parseLong(lb.getEstablish())));
		}else{
			vh.ll_buildtime.setVisibility(View.GONE);
		}
		if(lb.getRepayType()!=null&&lb.getRepayType()==1){
			vh.tv_repaytype.setText("到期还本付息");
		}else if(lb.getRepayType()==2){
			vh.tv_repaytype.setText("按月付息到期还本");
		}
		String status = lb.getStatus();
		if(lb.getCouponType()!=null){
			switch (lb.getCouponType()) {
			case 1:
				vh.ll_conpons.setVisibility(View.VISIBLE);
				vh.tv_conpons.setText("现金券：    ");
				vh.tv_xieyi.setVisibility(View.VISIBLE);
//				vh.tv_recond.setVisibility(View.VISIBLE);
				vh.tv_conponstext.setText(stringCut.getNumKb(lb.getCouponAmount())+"元");
				break;
			case 2:
				vh.ll_conpons.setVisibility(View.VISIBLE);
				vh.tv_conpons.setText("加息券：    ");
				vh.tv_xieyi.setVisibility(View.VISIBLE);
//				vh.tv_recond.setVisibility(View.VISIBLE);
				vh.tv_conponstext.setText(stringCut.getNumKbs(lb.getCouponRate())+"%");
				break;
			case 3:
				vh.tv_money.setText(stringCut.getNumKb(lb.getCouponAmount())+"元（体验金）");
				vh.ll_conpons.setVisibility(View.GONE);
//				vh.ll_conpons.setVisibility(View.VISIBLE);
//				vh.tv_conpons.setText("体验金：    ");
//				vh.tv_xieyi.setVisibility(View.GONE);
//				vh.tv_recond.setVisibility(View.GONE);
//				vh.tv_conponstext.setText(stringCut.getNumKb(lb.getCouponAmount())+"元(体验金)");
				break;
			case 4:
				vh.ll_conpons.setVisibility(View.VISIBLE);
				vh.tv_conpons.setText("翻倍券：    ");
				vh.tv_xieyi.setVisibility(View.VISIBLE);
//				vh.tv_recond.setVisibility(View.VISIBLE);
				vh.tv_conponstext.setText(lb.getMultiple()+"倍");
				break;

			default:
				vh.ll_conpons.setVisibility(View.GONE);
				break;
			}
		}else{
			vh.ll_conpons.setVisibility(View.GONE);
		}

		if(status.equalsIgnoreCase("0")){
			vh.ll_1.setVisibility(View.GONE);
			vh.ll_2.setVisibility(View.GONE);
			vh.ll_3.setVisibility(View.GONE);
			vh.tv_xieyi.setVisibility(View.GONE);
			vh.tv_recond.setVisibility(View.GONE);
			}
		else{
			vh.ll_1.setVisibility(View.VISIBLE);
			vh.ll_2.setVisibility(View.VISIBLE);
			vh.ll_3.setVisibility(View.VISIBLE);
			if(status.equalsIgnoreCase("3")){
				vh.tv_benjins.setText("已收本金：");
				vh.tv_lixis.setText("已收利息：");
			}
			String expire = lb.getExpireDate();
			if(expire!=null&&!expire.equalsIgnoreCase("")){
				vh.tv_repaydate.setText(stringCut.getDateToString(Long.parseLong(lb.getExpireDate())));
//				vh.tv_repaydate.setText(stringCut.getSpecifiedDayBefore(Long.parseLong(lb.getExpireDate())));
			}
			vh.tv_benjin.setText(stringCut.getNumKb(lb.getFactAmount())+"元");
			vh.tv_lixi.setText(stringCut.getNumKb(lb.getFactInterest())+"元");
		} 
		vh.tv_xieyi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lb.getPid();
				lb.getUid();
				lb.getId();
				if(lb.getSid()!=null){
					if(lb.getPrePid()!=null){
						context.startActivity(new Intent(context,WebViewActivity.class)
						.putExtra("URL", UrlConfig.ZHUANRANG+"?pid="+lb.getPid()+"&uid="+lb.getUid()+"&investId="+lb.getId())
						.putExtra("TITLE", "债权转让协议"));
					}else{
						context.startActivity(new Intent(context,WebViewActivity.class)
						.putExtra("URL", UrlConfig.JIEKUAN+"?pid="+lb.getPid()+"&uid="+lb.getUid()+"&investId="+lb.getId())
						.putExtra("TITLE", "借款协议"));
					}
				}else{
					context.startActivity(new Intent(context,WebViewActivity.class)
					.putExtra("URL", UrlConfig.JIEKUAN+"?pid="+lb.getPid()+"&uid="+lb.getUid()+"&investId="+lb.getId())
					.putExtra("TITLE", "投资协议"));
				}
			}
		});
		vh.tv_recond.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				context.startActivity(new Intent(context, BackRecondAct.class)
				.putExtra("pid", lb.getPid().toString())
				.putExtra("id", lb.getId().toString())
				);
				
			}
		});
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
