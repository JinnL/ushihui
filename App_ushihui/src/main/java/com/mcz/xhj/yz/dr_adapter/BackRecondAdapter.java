package com.mcz.xhj.yz.dr_adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.BackRecond;
import com.mcz.xhj.yz.dr_util.stringCut;

public class BackRecondAdapter extends BaseAdapter {
	private Context context;
	private List<BackRecond> lsct;
	private LayoutInflater inflater;
	
	public BackRecondAdapter(Context context, List<BackRecond> lsct) {
		super();
		this.context = context;
		this.lsct = lsct;
		inflater = LayoutInflater.from(context);
	}

	public void onDateChange(List<BackRecond> lsct) {
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
		private TextView tv_money;
		private TextView tv_status;
		private TextView tv_date;
		private TextView tv_qishu;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.item_backrecond, null);
			vh=new ViewHolder();
			vh.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			vh.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
			vh.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			vh.tv_qishu = (TextView) convertView.findViewById(R.id.tv_qishu);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		
		BackRecond mdb = lsct.get(position);
		vh.tv_money.setText(stringCut.getNumKb(mdb.getShouldSum()));
		if(lsct.size() == position+1){
			vh.tv_qishu.setText(stringCut.formatFractionalPart(position+1)+"期收益+本金");
		}else{
			vh.tv_qishu.setText(stringCut.formatFractionalPart(position+1)+"期收益");
		}
		vh.tv_date.setText(mdb.getDate());
		
		switch (mdb.getStatus()) {
		case 0:
			vh.tv_status.setText("未到账");
			vh.tv_status.setTextColor(context.getResources().getColor(R.color.weidaozhang));
			break;
		case 1:
			vh.tv_status.setText("已到账");
			vh.tv_status.setTextColor(context.getResources().getColor(R.color.red));
			break;
		default:
			break;
		}
		return convertView;
	}

}
