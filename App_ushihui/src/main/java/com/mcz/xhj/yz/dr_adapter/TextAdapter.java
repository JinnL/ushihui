package com.mcz.xhj.yz.dr_adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mcz.xhj.R;

public class TextAdapter extends BaseAdapter {
	private String[] list;
	private Context context;
	private LayoutInflater inflater;
	
	
	public TextAdapter(String[] list, Context context) {
		super();
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	private class ViewHolder{
		 private TextView tv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if(convertView==null){
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_text, null);
			vh.tv = (TextView) convertView.findViewById(R.id.tv);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		vh.tv.setText(list[position]);
		return convertView;
	}

}
