package com.mcz.xhj.yz.dr_adapter;

import java.util.ArrayList;

import com.mcz.xhj.yz.dr_bean.creditorBean;
import com.mcz.xhj.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class creditorAdapter extends BaseAdapter {
	private ArrayList<creditorBean> list;
	private Context context;
	private LayoutInflater inflater;
	
	
	public creditorAdapter(ArrayList<creditorBean> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	private class ViewHolder{
		 private TextView tv_content ,tv_title;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if(convertView==null){
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_creditor_list, null);
			vh.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		vh.tv_title.setText(list.get(position).getTitle());
		vh.tv_content.setText(list.get(position).getContent());
		return convertView;
	}

}
