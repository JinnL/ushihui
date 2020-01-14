package com.ekabao.oil.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListInScroll extends ListView {

	public ListInScroll(Context context) {
		super(context);
	}
	 public ListInScroll(Context context, AttributeSet attrs) {  
	        super(context, attrs);
	    }  
	  
	    public ListInScroll(Context context, AttributeSet attrs, int defStyle) {  
	        super(context, attrs, defStyle);
	    }  
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);  
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
