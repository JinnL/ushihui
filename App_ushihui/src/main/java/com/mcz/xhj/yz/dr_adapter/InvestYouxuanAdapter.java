package com.mcz.xhj.yz.dr_adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.InvestListBean;

public class InvestYouxuanAdapter extends BaseAdapter {
	private Context context;
	private List<InvestListBean> lsct;
	private LayoutInflater inflater;
	
	public InvestYouxuanAdapter(Context context, List<InvestListBean> lsct) {
		super();
		this.context = context;
		this.lsct = lsct;
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
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.item_invest, null);
			vh=new ViewHolder();
			vh.title=(TextView) convertView.findViewById(R.id.title);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		InvestListBean lb=lsct.get(position);
		vh.title.setText(lb.getFullName());
		return convertView;
	}

}
