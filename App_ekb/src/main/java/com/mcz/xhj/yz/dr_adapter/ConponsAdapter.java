package com.mcz.xhj.yz.dr_adapter;

import java.util.List;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.ConponsBean;
import com.mcz.xhj.yz.dr_util.stringCut;

public class ConponsAdapter extends BaseAdapter {
	private Context context;
	private List<ConponsBean> lsct;
	private LayoutInflater inflater;
	private int flag;

	public ConponsAdapter(Context context, List<ConponsBean> lsct, int flag) {
		super();
		this.context = context;
		this.lsct = lsct;
		this.flag = flag;
		inflater = LayoutInflater.from(context);
	}

	public void onDateChange(List<ConponsBean> lsct) {
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
		private TextView tv_name;
		private TextView tv_time;
		private TextView tv_money;
		private TextView tv_wenan1;
		private TextView tv_wenan2;
		private TextView tv_dd;
		private TextView tv_qixian;
		private TextView textView1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			if (flag == 1) {
				convertView = inflater.inflate(R.layout.item_conpons_unuse1,
						null);
			} else if (flag == 2) {
				convertView = inflater
						.inflate(R.layout.item_conpons_used, null);
			} else {
				convertView = inflater.inflate(R.layout.item_conpons_offtime,
						null);
			}
			vh = new ViewHolder();
			vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			vh.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			vh.tv_wenan1 = (TextView) convertView.findViewById(R.id.tv_wenan1);
			vh.tv_wenan2 = (TextView) convertView.findViewById(R.id.tv_wenan2);
			vh.tv_dd = (TextView) convertView.findViewById(R.id.tv_dd);
			vh.tv_qixian = (TextView) convertView.findViewById(R.id.tv_qixian);
			vh.textView1 = (TextView) convertView.findViewById(R.id.textView1);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		ConponsBean cb = lsct.get(position);
		switch (cb.getType()) {
		case 1:// 返现
			vh.tv_wenan2.setVisibility(View.VISIBLE);
			vh.textView1.setVisibility(View.VISIBLE);
			vh.tv_name.setText("返现红包");
			vh.tv_wenan1.setText("红包来源："+cb.getRemark());
			vh.tv_wenan2.setText("投资金额：单笔满"+stringCut.getNumKbs(cb.getEnableAmount()) + "元");
			if (cb.getProductDeadline() != null) {
				if (cb.getProductDeadline() > 0) {
					vh.tv_qixian.setText("投资期限："+cb.getProductDeadline() + "天以上"+"（含"+cb.getProductDeadline()+"天）");
				} else if (cb.getProductDeadline() == 0) {
					vh.tv_qixian.setText("产品期限：无限制");
				}
			}
//			String xian_h = stringCut.getNumKbs(cb.getAmount()) + "元";
//			SpannableString ss = new SpannableString(xian_h);
//			ss.setSpan(new RelativeSizeSpan(0.6f), xian_h.length() - 1,
//					xian_h.length(), TypedValue.COMPLEX_UNIT_PX);
//			vh.tv_money.setText(ss);
			vh.tv_money.setText("￥"+stringCut.getNumKbs(cb.getAmount()));
			if(cb.getExpireDate()!=null&&!cb.getExpireDate().equalsIgnoreCase("")){
				vh.tv_dd.setText("有效期至：");
				vh.tv_time.setText(stringCut.getDateYearToString(Long.parseLong(cb
						.getExpireDate())));
			}else{
				vh.tv_dd.setText("");
				vh.tv_time.setText("永久有效");
			}
			break;
		case 2:// 加息
		case 3:// 体验
			if(cb.getType() == 2){
				vh.tv_name.setText("加息券");
				vh.tv_wenan1.setText("加息券来源："+cb.getRemark());
				String rate_h = stringCut.getNumKbs(cb.getRaisedRates()) + "%";
				SpannableString sss = new SpannableString(rate_h);
				sss.setSpan(new RelativeSizeSpan(0.6f), rate_h.length() - 1,
						rate_h.length(), TypedValue.COMPLEX_UNIT_PX);
				vh.tv_money.setText("+"+sss);
			}else{
				vh.tv_name.setText("体验金");
				vh.tv_wenan1.setText("体验金来源："+cb.getRemark());
				vh.tv_money.setText("￥"+stringCut.getNumKbs(cb.getAmount()));
				vh.tv_wenan1.setText("体验金来源："+cb.getRemark());
			}
			vh.tv_wenan2.setVisibility(View.GONE);
			vh.textView1.setVisibility(View.GONE);
//			vh.tv_wenan2.setText("投资金额：单笔满"+stringCut.getNumKbs(cb.getEnableAmount()) + "元");
			if (cb.getProductDeadline() != null) {
				if (cb.getProductDeadline() > 0) {
					if(cb.getPid()!=null){
							vh.tv_qixian.setText("限用标的："+cb.getFullName());
						}else{
							vh.tv_qixian.setText("投资期限："+cb.getProductDeadline() + "天以上"+"（含"+cb.getProductDeadline()+"天）");
						}
				} else if (cb.getProductDeadline() == 0) {
					vh.tv_qixian.setText("产品期限：无限制");
				}
			}
			if(cb.getExpireDate()!=null&&!cb.getExpireDate().equalsIgnoreCase("")){
				vh.tv_dd.setText("有效期至：");
				if(cb.getPid()!=null){
					vh.tv_time.setText("满标过期");
				}else{
					vh.tv_time.setText(stringCut.getDateYearToString(Long.parseLong(cb
							.getExpireDate())));
				}
			}else{
				vh.tv_dd.setText("");
				vh.tv_time.setText("永久有效");
			}
			break;
		case 4:// 翻倍券
			vh.tv_wenan2.setVisibility(View.VISIBLE);
			vh.textView1.setVisibility(View.GONE);
			vh.tv_wenan2.setVisibility(View.GONE);
			vh.tv_name.setText("翻倍券");
			vh.tv_wenan1.setText("作用描述："+cb.getRemark()); //基础利率翻倍"
			vh.tv_wenan2.setText("投资金额：单笔满"+ stringCut.getNumKbs(cb.getEnableAmount()) + "元");
			if (cb.getProductDeadline() != null) {
				if (cb.getProductDeadline() > 0) {
					vh.tv_qixian.setText("投资期限："+cb.getProductDeadline() + "天以上"+"（含"+cb.getProductDeadline()+"天）");
				} else if (cb.getProductDeadline() == 0) {
					vh.tv_qixian.setText("产品期限：无限制");
				}
			}
			String jin_hh = stringCut.getNumKbs(cb.getMultiple()) + "倍";
//			SpannableString sssss = new SpannableString(jin_hh);
//			sssss.setSpan(new RelativeSizeSpan(0.6f), jin_hh.length() - 1,
//					jin_hh.length(), TypedValue.COMPLEX_UNIT_PX);
//			vh.tv_money.setText(sssss);
			vh.tv_money.setText(jin_hh);
			if(cb.getExpireDate()!=null&&!cb.getExpireDate().equalsIgnoreCase("")){
				vh.tv_dd.setText("有效期至：");
				vh.tv_time.setText(stringCut.getDateYearToString(Long.parseLong(cb
						.getExpireDate())));
			}else{
				vh.tv_dd.setText("");
				vh.tv_time.setText("永久有效");
			}
			break;
//		case 3:// 体验
//			vh.tv_name.setText("体验金");
//			vh.tv_wenan1.setText("红包作用：增加投资本金");
//			vh.tv_wenan2.setText("投资金额：≥"
//					+ stringCut.getNumKbs(cb.getEnableAmount()) + "元");
//			if (cb.getProductDeadline() != null) {
//				if (cb.getProductDeadline() > 0) {
//					vh.tv_qixian.setText("使用条件：产品期限" + "≥" + cb.getProductDeadline() + "天且产品可用体验金");
//				} else if (cb.getProductDeadline() == 0) {
//					vh.tv_qixian.setText("产品期限：无限制");
//				}
//			}
//			String jin_h = stringCut.getNumKbs(cb.getAmount()) + "元";
//			SpannableString ssss = new SpannableString(jin_h);
//			ssss.setSpan(new RelativeSizeSpan(0.6f), jin_h.length() - 1,
//					jin_h.length(), TypedValue.COMPLEX_UNIT_PX);
//			vh.tv_money.setText(ssss);
//			vh.tv_dd.setText("");
//			vh.tv_time.setText("仅限新手标");
//			break;

		default:
			break;
		}
		return convertView;
	}

	private SpannableString ss;
}
