
package com.mcz.xhj.yz.dr_app_fragment;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.bean_Detail_Info;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bleu.widget.slidedetails.SlideDetailsLayout;
import okhttp3.Call;

public class Frag_ProDetails_record extends BaseFragment {
	@BindView(R.id.lv_frag_pro_record)
	ListView lv_frag_pro_record;

	private ArrayList<String> al = new ArrayList<>();

	private lv_adapter adapter_lv;
	private String pid , ptype ;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.frag_prodetails_record;
	}
	private SlideDetailsLayout mSlideDetailsLayout;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mSlideDetailsLayout = (SlideDetailsLayout) activity.findViewById(R.id.slidedetails);
	}
	@Override
	protected void initParams() {
//		mSlideDetailsLayout = (SlideDetailsLayout) getActivity().findViewById(R.id.slidedetails);
//		mSlideDetailsLayout.setIsMove(false);
		pid = getArguments().getString("pid");
		ptype = getArguments().getString("ptype") ;
		LogUtils.i("--->产品记录pid："+pid+" ,ptype："+ptype);
		detail_info();
//		mSlideDetailsLayout.setIsMove(false);
		lv_frag_pro_record.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(lv_frag_pro_record.getFirstVisiblePosition()==0){
					mSlideDetailsLayout.smoothOpen(false);
//					mSlideDetailsLayout.isMove = false;
//					mSlideDetailsLayout.setIsMove(false);
				}else{
					mSlideDetailsLayout.smoothOpen(true);
//					mSlideDetailsLayout.isMove = true;
//					mSlideDetailsLayout.setIsMove(true);
				}
				return false;
			}
		});
	}

	class lv_adapter extends BaseAdapter {
		private List<bean_Detail_Info> al;

		public lv_adapter(List<bean_Detail_Info> lsad) {
			this.al = lsad;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return al.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return al.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = new ViewHolder();
			if (convertView == null) {
				convertView = View.inflate(getActivity(), R.layout.adapter_frag_prodetail_record, null);
				vh.adapter_view = convertView.findViewById(R.id.adapter_view);
				vh.tv_person = (TextView) convertView.findViewById(R.id.tv_person);
				vh.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
				vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				//vh.iv_investfrom = (ImageView) convertView.findViewById(R.id.iv_investfrom);

				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.tv_person.setText(lsad.get(position).getRealName());
			vh.tv_money.setText(lsad.get(position).getAmount()+"元");
			vh.tv_time.setText(stringCut.getDateYearToString(Long
					.parseLong(lsad.get(position).getInvestTime()))
					+ "\n"
					+ stringCut.getDateHourToString(Long.parseLong(lsad.get(
							position).getInvestTime())));
			/*if(lsad.get(position).getJoinType()==0){
				vh.iv_investfrom.setImageResource(R.mipmap.invest_list_pc);
			}else {
				vh.iv_investfrom.setImageResource(R.mipmap.invest_list_app);
			}*/

			if (al.size() - 1 != position) {
				vh.adapter_view.setBackgroundColor(0xffD2D2D2);
			}
			return convertView;
		}

	}

	private class ViewHolder {
		private TextView title, tv_person, tv_money, tv_time;
		private ImageView iv_investfrom;
		private View adapter_view;
	}

	private List<bean_Detail_Info> lsad = new ArrayList<bean_Detail_Info>();

	private void detail_info() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.DETAIL_INFO)
				.addParams("pid", pid)
				.addParams("type", ptype).addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtils.i("--->投资记录: "+response);
						dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							JSONObject map = obj.getJSONObject("map");
							JSONArray investList = map.getJSONArray("investList");
							lsad = JSON.parseArray(investList.toJSONString(), bean_Detail_Info.class);
							lv_frag_pro_record.setAdapter(new lv_adapter(lsad));

						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("....");
						} else {
							ToastMaker.showShortToast("崩了");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						  dismissDialog();
					}
				});
	}

}

