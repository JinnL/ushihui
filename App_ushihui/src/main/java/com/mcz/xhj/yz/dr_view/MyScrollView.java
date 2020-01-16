package com.mcz.xhj.yz.dr_view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by zhulang on 2017/7/25.
 */

public class MyScrollView extends ScrollView{
    private OnScrollListener onScrollListener;
    private ListView listView;
    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */
    private int lastScrollY;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置滚动接口
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }


    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            int scrollY = MyScrollView.this.getScrollY();

            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if(lastScrollY != scrollY){
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if(onScrollListener != null){
                onScrollListener.onScroll(scrollY);
            }

        };

    };

    /**
     * 重写onTouchEvent， 当用户的手在MyScrollView上面的时候，
     * 直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候，
     * MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，在handler处理
     * MyScrollView滑动的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(onScrollListener != null){
            onScrollListener.onScroll(lastScrollY = this.getScrollY());
        }
        switch(ev.getAction()){
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
                break;
        }
        return super.onTouchEvent(ev);
    }


    /**
     *
     * 滚动的回调接口
     *
     * @author xiaanming
     *
     */
    public interface OnScrollListener{
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         * @param scrollY
         *
         */
        public void onScroll(int scrollY);
    }



    /**
     * 覆写onInterceptTouchEvent方法，点击操作发生在ListView的区域的时候,
     * 返回false让ScrollView的onTouchEvent接收不到MotionEvent，而是把Event传到下一级的控件中
     */
   /* @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (listView != null && checkArea(listView, ev)) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }*/

    /**
     *  测试view是否在点击范围内
     * @param
     * @param v
     * @return
     */
    private boolean checkArea(ListView v, MotionEvent event){
        float x = event.getRawX();
        float y = event.getRawY();
        int[] locate = new int[2];
        v.getLocationOnScreen(locate);
        int l = locate[0];
        int r = l + v.getWidth();
        int t = locate[1];
        int b = t + v.getHeight();
        if (l < x && x < r && t < y && y < b) {
            return true;
        }
        return false;
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

}
