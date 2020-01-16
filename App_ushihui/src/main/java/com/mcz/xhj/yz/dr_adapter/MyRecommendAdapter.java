package com.mcz.xhj.yz.dr_adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.MyRecommend_bean;

public class MyRecommendAdapter extends BaseAdapter {
	private Context context;
	private List<MyRecommend_bean> lsct;
	private LayoutInflater inflater;
	
	public MyRecommendAdapter(Context context, List<MyRecommend_bean> lsct) {
		super();
		this.context = context;
		this.lsct = lsct;
		inflater = LayoutInflater.from(context);
	}

	public void onDateChange(List<MyRecommend_bean> lsct) {
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
		private TextView phone_tv , name_tv ,date_tv,touzi_tv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=inflater.inflate(R.layout.invitation_item, null);
			vh.phone_tv =  (TextView) convertView.findViewById(R.id.phone_tv) ;
			vh.name_tv =  (TextView) convertView.findViewById(R.id.name_tv) ;
			vh.date_tv =  (TextView) convertView.findViewById(R.id.date_tv) ;
			vh.touzi_tv =  (TextView) convertView.findViewById(R.id.touzi_tv) ;
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}

		vh.phone_tv.setText(lsct.get(position).getMobilePhone()) ;
		vh.name_tv.setText(lsct.get(position).getRealName()) ;
		vh.date_tv.setText(lsct.get(position).getRegTime()) ;
		vh.touzi_tv.setText(lsct.get(position).getIsInvest()) ;
		return convertView;
	}

}
