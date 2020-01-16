package com.mcz.xhj.yz.dr_app_fragment;

import android.widget.TextView;

import com.mcz.xhj.R;

import butterknife.BindView;


public class Frag_ProDetails_measure extends BaseFragment {
	@BindView(R.id.tv_winMeasure)
	TextView tv_winMeasure ;
	private String winMeasure ; //风控措施
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.frag_prodetails_measure;
	}

	@Override
	protected void initParams() {
		// TODO Auto-generated method stub
		winMeasure = getActivity().getIntent().getStringExtra("winMeasure") ;
		
		tv_winMeasure.setText(winMeasure) ;
		 
	}


}
