package com.mcz.xhj.yz.dr_adapter;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mcz.xhj.yz.dr_bean.FriendBean;

public class FriendAdapter extends BaseAdapter {
	private Context context;
	private List<FriendBean> lsct;
	private LayoutInflater inflater;
	public FriendAdapter(Context context, List<FriendBean> lsct) {
		super();
		this.context = context;
		this.lsct = lsct;
		inflater = LayoutInflater.from(context);
	}

	public interface onButtonClickL{
		void onButtonClick(View view);
	}
	
	public void onDateChange(List<FriendBean> lsct) {
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
//		private TextView tv_qishu;
		private TextView tv_name;
		private TextView tv_time;
		private TextView tv_hot;
		private LinearLayout ll_yinyin;
		private SimpleDraweeView iv_pic;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_invitefriend,null);
			vh = new ViewHolder();
			vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			vh.tv_hot = (TextView) convertView.findViewById(R.id.tv_hot);
//			vh.tv_qishu = (TextView) convertView.findViewById(R.id.tv_qishu);
			vh.ll_yinyin = (LinearLayout) convertView.findViewById(R.id.ll_yinyin);
			vh.iv_pic = (SimpleDraweeView) convertView.findViewById(R.id.iv_pic);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		FriendBean cb = lsct.get(position);
		vh.tv_name.setText(cb.getTitle());
//		vh.tv_time.setText("活动时间："+cb.getActivityDate());
		vh.tv_time.setText(cb.getActivityDate());
		cb.getAppPic();
		if(cb.getAppPic()!=null&&!cb.getAppPic().equalsIgnoreCase("")){
			Uri uri = Uri.parse(cb.getAppPic());
			vh.iv_pic.setImageURI(uri);
		}
//		vh.tv_qishu.setText("第"+cb.getPeriods()+"期");
//		switch (cb.getPeriods()){
//			case 2:
//				vh.iv_pic.setImageResource(R.mipmap.friend2);
//				break;
//		}
		if(cb.getIsTop()==1){
			vh.tv_hot.setVisibility(View.VISIBLE);
		}else{
			vh.tv_hot.setVisibility(View.GONE);
		}
		switch (cb.getStatus()) {
			case 1:
				vh.ll_yinyin.setVisibility(View.GONE);
				break;
			case 2:
				vh.ll_yinyin.setVisibility(View.VISIBLE);
				vh.tv_hot.setVisibility(View.GONE);
				break;
			default:
				break;
		}
		return convertView;
	}
}
