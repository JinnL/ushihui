package com.mcz.xhj.yz.dr_adapter;

import java.util.ArrayList;

import com.mcz.xhj.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Ad_Item_Auditing extends BaseAdapter {
	private ArrayList<String> auditing_item = new ArrayList<String>() ;;
	private Context context;
	private LayoutInflater inflater;

	public Ad_Item_Auditing(Context context,
			ArrayList<String> auditing_item) {
		super();
		this.auditing_item = auditing_item;
		inflater = LayoutInflater.from(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return auditing_item.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return auditing_item.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private class ViewHolder {
		private TextView Company_name;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_auditing, null);
			vh = new ViewHolder();
			vh.Company_name = (TextView) convertView
					.findViewById(R.id.Company_name);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.Company_name.setText(auditing_item.get(position)) ;
		
		return convertView;
	}

}
