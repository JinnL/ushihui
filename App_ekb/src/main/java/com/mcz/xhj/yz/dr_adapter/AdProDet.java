package com.mcz.xhj.yz.dr_adapter;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import com.mcz.xhj.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mcz.xhj.yz.dr_bean.bean_Detail_Info_pic;


/**
 * Created by admin on 2015/12/6.
 */
public class AdProDet extends PagerAdapter implements OnClickListener{

    private List<bean_Detail_Info_pic> data;
    private Context context;
    private LayoutInflater inflater;
    private String pid;

    public AdProDet(List<bean_Detail_Info_pic> data, Context context, String pid) {
        this.data = data;
        this.context = context;
        this.pid = pid;
        inflater = LayoutInflater.from(context);
    }

    //更新数据源
    public void refreshData(List<bean_Detail_Info_pic> data){
        this.data = data;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
    	 return data.size();
//        return Integer.MAX_VALUE;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	PointF p = new PointF(0.5f, 0.5f);
        View view = inflater.inflate(R.layout.adapter_frag_prodetail_picture,null);
        SimpleDraweeView iv = (SimpleDraweeView) view.findViewById(R.id.image);
//      iv.getHierarchy().setActualImageFocusPoint(p);
//      iv.setImageResource(data[position%data.length]);
        Uri uri = Uri.parse(data.get(position).getBigUrl());
        iv.setImageURI(uri);
        ((ViewPager)container).addView(view);
//        iv.setOnClickListener(this);
        return view;
    }
private int pos ;
    private LinearLayout layout;
	private PopupWindow popupWindow;
	private SimpleDraweeView iv_regist;

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((View) object);
    }
    
    public void showPopupWindow1(String url_image) {
		// 加载布局
		layout = (LinearLayout) LayoutInflater
				.from(context).inflate(
						R.layout.dialog_image_big, null);
		// 找到布局的控件
		// 实例化popupWindow
		popupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		iv_regist = (SimpleDraweeView) (layout).findViewById(R.id.iv_regist);
		Uri uri = Uri.parse(url_image);
		// PointF focusPoint = new PointF(0f, 0f) ;
		// // your app populates the focus point
		// iv_regist
		// .getHierarchy()
		// .setActualImageFocusPoint(focusPoint);
		DraweeView<DraweeHierarchy> mDraweeView = new DraweeView<>(context);
		iv_regist.setAspectRatio(1f);
		int width = 110, height = 110;
		ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
				.setResizeOptions(new ResizeOptions(width, height)).build();
		PipelineDraweeController controller = (PipelineDraweeController) Fresco
				.newDraweeControllerBuilder()
				.setOldController(mDraweeView.getController())
				.setImageRequest(request).build();
		iv_regist.setController(controller);

		iv_regist.setImageURI(uri);
		// 控制键盘是否可以获得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// popupWindow.setBackgroundDrawable(new PaintDrawable());
		// popupWindow.setOutsideTouchable(true);

		// // 设置popupWindow弹出窗体的背景
		// WindowManager manager = (WindowManager)
		// getActivity().getSystemService(Context.WINDOW_SERVICE);
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
		// backgroundAlpha(0.5f);
	}
    /**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = ((Activity) this.context).getWindow()
				.getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		((Activity) this.context).getWindow().setAttributes(lp);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.image:
			showPopupWindow1(data.get(pos).getBigUrl()) ;
			break;

		default:
			break;
		}
	}

}
