
package com.mcz.xhj.yz.dr_app_fragment;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.bean_Detail_Info_pic;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.GridViewInScrollView;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bleu.widget.slidedetails.SlideDetailsLayout;
import okhttp3.Call;

public class Frag_ProDetails_picture extends BaseFragment {
	@BindView(R.id.gv_frag_pro_picture)
	GridViewInScrollView gv_frag_pro_picture;
	@BindView(R.id.gv_frag_pro_picture_bottem)
	GridViewInScrollView gv_frag_pro_picture_bottem;
	@BindView(R.id.tv_introduce)
	TextView tv_introduce;
	@BindView(R.id.tv_introduce_bottem)
	TextView tv_introduce_bottem;
	@BindView(R.id.tv_start_time)
	TextView tv_start_time;
	@BindView(R.id.tv_ing_time)
	TextView tv_ing_time;
	@BindView(R.id.tv_end_time)
	TextView tv_end_time;
	@BindView(R.id.sv_pic)
	ScrollView sv_pic;
	@BindView(R.id.iv_yuanli)
	SimpleDraweeView iv_yuanli;
	private ArrayList<String> al = new ArrayList<>();

	private String pid , ptype,principleH5 ;
	private String introduce,usefor,startTime,ingTime,endTime;

	private float mLastX;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.frag_prodetails_picture;
	}

	private SlideDetailsLayout mSlideDetailsLayout;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mSlideDetailsLayout = (SlideDetailsLayout) activity.findViewById(R.id.slidedetails_pro);
	}

	@Override
	protected void initParams() {
//		mSlideDetailsLayout = (SlideDetailsLayout) getActivity().findViewById(R.id.slidedetails);
//		mSlideDetailsLayout.setIsMove(false);\
		Bundle bundle = getArguments();
		pid = getArguments().getString("pid");
		ptype =  getArguments().getString("ptype") ;
		principleH5 =  getArguments().getString("principleH5");
		LogUtils.i("--->产品原理图principleH5: "+principleH5+" ,pid："+pid+" ,ptype："+ptype);
		detail_info();
		gv_frag_pro_picture.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub


				  showPopupWindow1(lsad_one.get(position).getBigUrl()) ;
			}
		}) ;
		gv_frag_pro_picture_bottem.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				showPopupWindow1(lsad_second.get(position).getBigUrl());
			}
		}) ;
		introduce = getArguments().getString("introduce");
		usefor = getArguments().getString("borrower");
		startTime = getArguments().getString("startTime");
		ingTime = getArguments().getString("ingTime");
		endTime = getArguments().getString("endTime");
		tv_introduce.setText(introduce);//项目介绍
		tv_introduce_bottem.setText(usefor);//借款用途
		if(principleH5!=null){
			Uri uri = Uri.parse(principleH5);
			iv_yuanli.setImageURI(uri);
		} else {
			iv_yuanli.setBackgroundResource(R.mipmap.default_yuanli);
		}
		tv_start_time.setText(stringCut.getDateYearToString(Long.parseLong(startTime)));
		tv_ing_time.setText(stringCut.getDateYearToString(Long.parseLong(ingTime)));
		if(!endTime.equalsIgnoreCase("null")){
			tv_end_time.setText(stringCut.getDateYearToString(Long.parseLong(endTime)));
		}
//		mSlideDetailsLayout.setIsMove(false);
		sv_pic.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				int height = sv_pic.getChildAt(0).getMeasuredHeight();
//				sv_pic.getHeight();
				if(sv_pic.getScrollY()==0){
//					mSlideDetailsLayout.isMove = false;
					mSlideDetailsLayout.smoothOpen(false);
//					mSlideDetailsLayout.setIsMove(false);
				}else{
//					mSlideDetailsLayout.isMove = true;
					mSlideDetailsLayout.smoothOpen(true);
//					mSlideDetailsLayout.setIsMove(true);
				}
//				if(sv_pic.fullScroll(ScrollView.FOCUS_UP)){
//					mSlideDetailsLayout.isMove = true;
//				}else{
//					mSlideDetailsLayout.isMove = false;
//				}
				return false;
			}
		});

	}

	ViewHolder vh ;
	class lv_adapter extends BaseAdapter {
		private List<bean_Detail_Info_pic> al;

		public lv_adapter(List<bean_Detail_Info_pic> al) {
			this.al = al;
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
			 vh = new ViewHolder();
			if (convertView == null) {
				convertView = View.inflate(getActivity(), R.layout.adapter_frag_prodetail_picture, null);
				vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				vh.image = (SimpleDraweeView) convertView.findViewById(R.id.image);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			
			vh.tv_name.setText(al.get(position).getName());
			Uri uri = Uri.parse(al.get(position).getBigUrl());
	        vh.image.setImageURI(uri);
			return convertView;
		}

	}

	private class ViewHolder {
		private TextView title, tv_name;
		private View adapter_view;
		private SimpleDraweeView image;
	}
	private LinearLayout layout;
	private PopupWindow popupWindow;
	private SimpleDraweeView iv_regist   ;
	public void showPopupWindow1(String url_image) {
		// 加载布局
		layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(
				R.layout.dialog_image_big, null);
		// 找到布局的控件
		// 实例化popupWindow
		popupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, true);
		iv_regist = (SimpleDraweeView) (layout).findViewById(R.id.iv_regist);
		Uri uri = Uri.parse(url_image);
		DraweeView<DraweeHierarchy> mDraweeView = new DraweeView<>(getContext()) ;
		iv_regist.setAspectRatio(1f) ;
		int width = 110, height = 110;
		ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
		    .setResizeOptions(new ResizeOptions(width, height))
		    .build();
		PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
		    .setOldController(mDraweeView.getController())
		    .setImageRequest(request)
		    .build();
		iv_regist.setController(controller);

		iv_regist.setImageURI(uri);
		// 控制键盘是否可以获得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
//		popupWindow.setBackgroundDrawable(new PaintDrawable());
//		popupWindow.setOutsideTouchable(true);

//		// 设置popupWindow弹出窗体的背景
//		WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		// 监听
		layout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popupWindow.dismiss();
				return true;
			}
		});
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				backgroundAlpha(1f);
			}
		});
		popupWindow.showAsDropDown(iv_regist);
//		backgroundAlpha(0.5f);
	}

	/**
	 * 设置添加屏幕的背景透明度
	 *
	 * @param bgAlpha
	 **/

	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getActivity().getWindow().setAttributes(lp);
	}
	private List<bean_Detail_Info_pic> lsad = new ArrayList<bean_Detail_Info_pic>();
	private List<bean_Detail_Info_pic> lsad_one = new ArrayList<bean_Detail_Info_pic>();
	private List<bean_Detail_Info_pic> lsad_second = new ArrayList<bean_Detail_Info_pic>();

	private void detail_info() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post()
				.url(UrlConfig.DETAIL_INFO)
				.addParams("pid", pid)
				.addParams("type", "2")
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2")
				.build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtils.i("--->产品投资记录及产品图片picture: "+response);
						  dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							JSONObject map = obj.getJSONObject("map");
							JSONArray picList = map.getJSONArray("picList");
							lsad = JSON.parseArray(picList.toJSONString(), bean_Detail_Info_pic.class);
							if(lsad.size()>0){
								for (int i = 0; i < lsad.size(); i++) {
									if(lsad.get(i).getType()!=null){
										if(lsad.get(i).getType()==0){
											lsad_one.add(lsad.get(i));
										}else if(lsad.get(i).getType()==1){
											lsad_second.add(lsad.get(i));
										}
									}
								}
							}
							gv_frag_pro_picture_bottem.setAdapter(new lv_adapter(lsad_second));
							gv_frag_pro_picture.setAdapter(new lv_adapter(lsad_one));

						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统错误");
						} else {
							ToastMaker.showShortToast("系统错误");
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

