package com.mcz.xhj.yz.dr_adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.RedListBean;
import com.mcz.xhj.yz.dr_util.stringCut;

public class RedListAdapter extends BaseAdapter {
	private Context context;
	private List<RedListBean> lsct;
	private LayoutInflater inflater;
	public RedListAdapter(Context context, List<RedListBean> lsct) {
		super();
		this.context = context;
		this.lsct = lsct;
		inflater = LayoutInflater.from(context);
	}

	public interface onButtonClickL{
		void onButtonClick(View view);
	}
	
	public void onDateChange(List<RedListBean> lsct) {
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
		private TextView tv_time;
		private TextView tv_money;
		private TextView tv_wenan2;
		private TextView tv_dd;
		private TextView tv_qixian;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_redlist,null);
			vh = new ViewHolder();
			vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			vh.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			vh.tv_wenan2 = (TextView) convertView.findViewById(R.id.tv_wenan2);
			vh.tv_qixian = (TextView) convertView.findViewById(R.id.tv_qixian);
			vh.tv_dd = (TextView) convertView.findViewById(R.id.tv_dd);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final RedListBean cb = lsct.get(position);
		vh.tv_wenan2.setText(stringCut.getNumKbs(cb.getEnableAmount()));
		if (cb.getProductDeadline() != null) {
			if (cb.getProductDeadline() > 0) {
				vh.tv_qixian.setText("投资期限："+cb.getProductDeadline() + "天以上"+"（含"+cb.getProductDeadline()+"天）");
			} else if (cb.getProductDeadline() == 0) {
				vh.tv_qixian.setText("无限制");
			}
		}
		vh.tv_money.setText("￥"+stringCut.getNumKbs(cb.getAmount()));
		if(cb.getExpireDate()!=null&&!cb.getExpireDate().equalsIgnoreCase("")){
			vh.tv_dd.setText("有效期至：");
			vh.tv_time.setText(stringCut.getDateYearToString(Long.parseLong(cb
					.getExpireDate())));
		}else{
			vh.tv_dd.setText("");
			vh.tv_time.setText("永久有效");
		}
		
		return convertView;
	}
}
