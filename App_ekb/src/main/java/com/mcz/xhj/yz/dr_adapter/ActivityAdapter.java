package com.mcz.xhj.yz.dr_adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_app.Winner_Act;
import com.mcz.xhj.yz.dr_bean.ActivityBean;

public class ActivityAdapter extends BaseExpandableListAdapter {
	
	private Context context;
	private List<ActivityBean> lsct;
	private LayoutInflater inflater;
	public ActivityAdapter(Context context, List<ActivityBean> lsct) {
		super();
		this.context = context;
		this.lsct = lsct;
		inflater = LayoutInflater.from(context);
	}
	
	public void onDateChange(List<ActivityBean> lsct) {
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
		private TextView tv_status;
		private TextView tv_money;
		private TextView tv_pname;
		private TextView tv_aname;
		private TextView tv_gift;
		private TextView tv_gcode;
		private TextView tv_user;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_activity, null);//=====================
			vh.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			vh.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
			vh.tv_pname = (TextView) convertView.findViewById(R.id.tv_pname);
			vh.tv_aname = (TextView) convertView.findViewById(R.id.tv_aname);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		ActivityBean lb = lsct.get(groupPosition);
		vh.tv_aname.setText(lb.getActivityName());
		vh.tv_pname.setText(lb.getProductName());
		vh.tv_money.setText(lb.getAmount()+"");
		Integer status = lb.getPrizeStatus();
		switch (status) {
		case 0://待开奖
			vh.tv_status.setTextColor(Color.parseColor("#787878"));
			vh.tv_status.setText("未开奖");
			break;
		case 1://未中奖
			vh.tv_status.setTextColor(Color.parseColor("#549cff"));
			vh.tv_status.setText("未中奖");
			break;
		case 2://已中奖
			vh.tv_status.setTextColor(Color.parseColor("#D92E38"));
			vh.tv_status.setText("已中奖\n"+lb.getPrizeCode());
			break;
		default:
			break;
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
			convertView=inflater.inflate(R.layout.item_activity_down, null);//=================
			vh.tv_gift = (TextView) convertView.findViewById(R.id.tv_gift);
			vh.tv_gcode = (TextView) convertView.findViewById(R.id.tv_gcode);
			vh.tv_user = (TextView) convertView.findViewById(R.id.tv_user);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		final ActivityBean lb = lsct.get(groupPosition);
		vh.tv_gift.setText(lb.getActivityPrizes());
		if(lb.getPrizeStatus()==2){
			vh.tv_gift.setTextColor(Color.parseColor("#D92E38"));
		}else{
			vh.tv_gift.setTextColor(Color.parseColor("#090909"));
		}
		if(lb.getPrizeCode()!=null){
//			vh.tv_gift.setTextColor(Color.parseColor("#D92E38"));
			if(lb.getPrizeStatus()==2){
				int a = lb.getLuckCodes().indexOf(lb.getPrizeCode());
				int b = lb.getPrizeCode().length();
				if(a<0){
					vh.tv_gcode.setText(lb.getLuckCodes());
				}else{
					vh.tv_gcode.setText(generateCenterSpannableText(lb.getLuckCodes(),a,b));
				}
			}else{
				vh.tv_gcode.setText(lb.getLuckCodes());
			}
		}else{
			vh.tv_gcode.setText(lb.getLuckCodes());
		}
		vh.tv_user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lb.getPid();
				context.startActivity(new Intent(context, Winner_Act.class).putExtra("pid", lb.getPid()+""));
				
			}
		});
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	private SpannableString generateCenterSpannableText(String text,int a,int b) {

        SpannableString s = new SpannableString(""+text);
        s.setSpan(new RelativeSizeSpan(1.0f), 0, a, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), a, a+b , 0);
        s.setSpan(new ForegroundColorSpan(Color.rgb(217, 46, 56)), a, a+b, 0);
        s.setSpan(new RelativeSizeSpan(1.2f), a, a+b, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), 7, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 7, s.length(), 0);
        return s;
    }
	
}
