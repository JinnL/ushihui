package com.mcz.xhj.yz.dr_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.mcz.xhj.R;


public class LoadListView extends ListView implements OnScrollListener {
	View footer;// �ײ����֣�
	int totalItemCount;// ��������
	int lastVisibleItem;// ���һ���ɼ��item��
	boolean isLoading;// ���ڼ��أ�
	ILoadListener iLoadListener;
	public LoadListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * ��ӵײ�������ʾ���ֵ�listview
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.footer_layout, null);
		footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
		this.addFooterView(footer);
		this.setOnScrollListener(this);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.lastVisibleItem = firstVisibleItem + visibleItemCount;
		this.totalItemCount = totalItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (totalItemCount == lastVisibleItem
				&& scrollState == SCROLL_STATE_IDLE) {
			if (!isLoading) {
				isLoading = true;
				footer.findViewById(R.id.load_layout).setVisibility(
						View.VISIBLE);
				iLoadListener.onLoad();
			}
		}
	}
	/**
	 * �������
	 */
	public void loadComplete(){
		isLoading = false;
		footer.findViewById(R.id.load_layout).setVisibility(
				View.GONE);
	}
	
	public void setInterface(ILoadListener iLoadListener){
		this.iLoadListener = iLoadListener;
	}
	//���ظ����ݵĻص��ӿ�
	public interface ILoadListener{
		public void onLoad();
	}
}
