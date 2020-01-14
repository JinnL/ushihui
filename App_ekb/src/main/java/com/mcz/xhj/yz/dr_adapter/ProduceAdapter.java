package com.mcz.xhj.yz.dr_adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.creditorBean;

public class ProduceAdapter extends BaseExpandableListAdapter {
	
	private Context context;
	private List<creditorBean> lsct;
	private LayoutInflater inflater;
	public ProduceAdapter(Context context, List<creditorBean> lsct) {
		super();
		this.context = context;
		this.lsct = lsct;
		inflater = LayoutInflater.from(context);
	}
	
	public void onDateChange(List<creditorBean> lsct) {
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
		private TextView tv_content;
//		private ImageView imageView;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_produce, null);//=====================
			vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		creditorBean lb = lsct.get(groupPosition);
		vh.tv_name.setText(lb.getTitle());
		if(isExpanded){
//			convertView.findViewById(R.id.rl_title).setBackgroundColor(Color.parseColor("#f2f2f2"));
			convertView.findViewById(R.id.imageView).setBackgroundResource(R.mipmap.icon_rightto);
			convertView.findViewById(R.id.imageView).setRotation(90);
		}else{
//			convertView.findViewById(R.id.rl_title).setBackgroundColor(Color.parseColor("#ffffff"));
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
			convertView=inflater.inflate(R.layout.item_produce_down, null);//=================
			vh.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		final creditorBean lb = lsct.get(groupPosition);
		vh.tv_content.setText(lb.getContent());
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
