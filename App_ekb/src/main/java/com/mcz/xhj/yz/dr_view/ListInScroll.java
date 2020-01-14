package com.mcz.xhj.yz.dr_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListInScroll extends ListView {

	public ListInScroll(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	 public ListInScroll(Context context, AttributeSet attrs) {  
	        // TODO Auto-generated method stub  
	        super(context, attrs);  
	    }  
	  
	    public ListInScroll(Context context, AttributeSet attrs, int defStyle) {  
	        // TODO Auto-generated method stub  
	        super(context, attrs, defStyle);  
	    }  
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);  
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
