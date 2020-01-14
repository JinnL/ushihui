
package com.mcz.xhj.yz.dr_app_fragment;

import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_util.stringCut;

import butterknife.BindView;
import cn.bleu.widget.slidedetails.SlideDetailsLayout;

public class Frag_ProDetails_introduce extends BaseFragment {
	@BindView(R.id.tv_windMeasure)
	TextView tv_windMeasure;
	@BindView(R.id.tv_introduce)
	TextView tv_introduce;
	@BindView(R.id.tv_repaySource)
	TextView tv_repaySource;
	@BindView(R.id.tv_borrower)
	TextView tv_borrower;
	@BindView(R.id.sv_intro)
	ScrollView sv_intro;
	private String windMeasure; // 风控措施
	private String introduce;// 产品介绍
	private String repaySource;// 还款来源
	private String borrower ;// 债务人概况
	private String repayType ;// 还款方式
	private String expireDate ;// 还款开始日期

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.frag_prodetails_introduce;
	}


    private SlideDetailsLayout mSlideDetailsLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSlideDetailsLayout = (SlideDetailsLayout) activity.findViewById(R.id.slidedetails_pro);
    }
	@Override
	protected void initParams() {
		// TODO Auto-generated method stub
		windMeasure = getArguments().getString("windMeasure");
		introduce = getArguments().getString("introduce");
		repaySource = getArguments().getString("repaySource");
		borrower = getArguments().getString("borrower");
		repayType = getArguments().getString("repayType");
		expireDate = getArguments().getString("endTime");
		if(null == windMeasure){
			windMeasure = "暂无" ;
			tv_windMeasure.setText("") ;
		}else{
			tv_windMeasure.setText(stringCut.getDateToString(Long.parseLong(expireDate))) ;
		}
		if(null == introduce){
			introduce = "暂无" ;
		}
		if(null == repaySource){
			repaySource = "暂无" ;
		}
		if(null == borrower){
			borrower = "暂无" ;
		}
		tv_borrower.setText(repayType) ;
		tv_introduce.setText(windMeasure) ;//还款保障
		tv_repaySource.setText(repaySource) ;//还款来源
//		tv_windMeasure.setText(windMeasure) ;
		tv_introduce.setMovementMethod(ScrollingMovementMethod.getInstance());

//		mSlideDetailsLayoutm.setIsMove(false);
		sv_intro.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				int height = sv_pic.getChildAt(0).getMeasuredHeight();
//				sv_pic.getHeight();
				if(sv_intro.getScrollY()==0){
					mSlideDetailsLayout.smoothOpen(false);
//                    mSlideDetailsLayout.isMove = false;
//					mSlideDetailsLayout.setIsMove(false);
				}else{
					mSlideDetailsLayout.smoothOpen(true);
//                    mSlideDetailsLayout.isMove = true;
//					mSlideDetailsLayoutm.setIsMove(true);
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

}

