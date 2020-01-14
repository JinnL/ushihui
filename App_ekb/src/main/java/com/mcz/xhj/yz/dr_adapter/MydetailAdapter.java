package com.mcz.xhj.yz.dr_adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.MyDetailBean;
import com.mcz.xhj.yz.dr_util.stringCut;

public class MydetailAdapter extends BaseAdapter {
	private Context context;
	private List<MyDetailBean> lsct;
	private LayoutInflater inflater;
	
	public MydetailAdapter(Context context, List<MyDetailBean> lsct) {
		super();
		this.context = context;
		this.lsct = lsct;
		inflater = LayoutInflater.from(context);
	}

	public void onDateChange(List<MyDetailBean> lsct) {
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
		private ImageView iv_icon;
		private TextView tv_money;
		private TextView tv_use;
		private TextView tv_status;
		private TextView tv_date;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.item_mydetail, null);
			vh=new ViewHolder();
			vh.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			vh.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			vh.tv_use = (TextView) convertView.findViewById(R.id.tv_use);
			vh.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
			vh.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		MyDetailBean mdb = lsct.get(position);
		vh.tv_money.setText(stringCut.getNumKb(mdb.getAmount())+"元");
		vh.tv_date.setText(stringCut.getDateTimeToString(Long.parseLong(mdb.getAddTime())));
		switch (mdb.getTradeType()) {
		case 1:
			vh.tv_use.setText("充值");
			break;
		case 2:
			vh.tv_use.setText("提现");
			break;
		case 3:
			vh.tv_use.setText("投资");
			break;
		case 4:
			vh.tv_use.setText("活动");
			break;
		case 5:
			vh.tv_use.setText("手续费");
			break;
		case 6:
			vh.tv_use.setText("回款");
			break;
		case 7:
			vh.tv_use.setText("体验金");
			break;
		default:
			break;
		}
		switch (mdb.getStatus()) {
		case 1:
			vh.tv_status.setText("处理中");
			vh.iv_icon.setImageResource(R.mipmap.ico_freeze);
			break;
		case 4:
			vh.tv_status.setText("募集中");
			vh.iv_icon.setImageResource(R.mipmap.ico_freeze);
			break;
		case 5:
			vh.tv_status.setText("待续投");
			vh.iv_icon.setImageResource(R.mipmap.ico_expend);
			break;
		case 2:
			vh.tv_status.setText("失败");
			if(mdb.getType()==1){
				vh.iv_icon.setImageResource(R.mipmap.ico_income);
			}else{
				vh.iv_icon.setImageResource(R.mipmap.ico_expend);
			}
			break;
		case 3:
			vh.tv_status.setText("成功");
			if(mdb.getType()==1){
				vh.iv_icon.setImageResource(R.mipmap.ico_income);
			}else{
				vh.iv_icon.setImageResource(R.mipmap.ico_expend);
			}
			break;
		default:
			break;
		}
		return convertView;
	}

}
