package com.ekabao.oil.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.util.ActivityStack;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

//import android.support.v4.app.Fragment;

/**
 * BaseActivity
 *
 * @author blue
 */
public abstract class BaseFragment extends Fragment implements DialogMaker.DialogCallBack {
    protected Dialog dialog;
    public Context mContext;
//	private LoadingAndRetryManager loadingAndRetryManager;

    /*@Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        mContext = activity;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();    //通过fragment的Activity实例化mActivity
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
//		loadingAndRetryManager = LoadingAndRetryManager.generate(this, new OnLoadingAndRetryListener()
//        {
//            @Override
//            public void setRetryEvent(View retryView)
//            {
//            	this.setRetryEvent(retryView);
//            }
//        });
//		loadingAndRetryManager.showLoading();

        //View view = null;
        //避免切换Fragment 的时候重绘UI 。失去数据
       /* if (view == null) {
            view = inflater.inflate(getLayoutId(), null);
        }
        ButterKnife.bind(this, view);
        // 缓存的viewiew需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }*/

        initParams();

        return view;
    }

    //	public void setRetryEvent(View retryView)
//    {
//        View view = retryView.findViewById(R.id.id_btn_retry);
//        view.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                loadingAndRetryManager.showLoading();
//            }
//        });
//    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        //initParams();

    }

    @Override
    public void onResume() {

        super.onResume();
        //MobclickAgent.onResume(mContext);

        MobclickAgent.onPageStart("BaseFragment");
    }

    @Override
    public void onPause() {

        super.onPause();
        //MobclickAgent.onPause(mContext);

        MobclickAgent.onPageEnd("BaseFragment");

    }

    @Override
    public void onDestroy() {
        dismissDialog();
        ActivityStack.getInstance().removeActivity(getActivity());
        super.onDestroy();
    }

    //	@Nullable
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View view = inflater.inflate(getLayoutId(), container, false);
////		ButterKnife.bind(this,view);
////		loadingAndRetryManager = LoadingAndRetryManager.generate(this, new OnLoadingAndRetryListener()
////        {
////            @Override
////            public void setRetryEvent(View retryView)
////            {
////            	this.setRetryEvent(retryView);
////            }
////        });
////		loadingAndRetryManager.showLoading();
//		return view;
//	}


    /**
     * 初始化布局
     *
     * @author blue
     */
    protected abstract int getLayoutId();

    /**
     * 参数设置
     *
     * @author blue
     */
    protected abstract void initParams();

    /**
     * 弹出对话框
     *
     * @author blue
     */
    public Dialog showAlertDialog(String title, String msg, String[] btns, boolean isCanCancelabel, final boolean isDismissAfterClickBtn, Object tag) {
        if (null == dialog || !dialog.isShowing()) {
            dialog = DialogMaker.showCommonAlertDialog(getActivity(), title, msg, btns, this, isCanCancelabel, isDismissAfterClickBtn, tag);
        }
        return dialog;
    }

    /**
     * 等待对话框
     *
     * @author blue
     */
    public Dialog showWaitDialog(String msg, boolean isCanCancelabel, Object tag) {
        if (null == dialog || !dialog.isShowing()) {
            dialog = DialogMaker.showCommenWaitDialog(getActivity(), msg, this, isCanCancelabel, tag);
        }
        return dialog;
    }

    /**
     * 关闭对话框
     *
     * @author blue
     */
    public void dismissDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onButtonClicked(Dialog dialog, int position, Object tag) {
    }

    @Override
    public void onCancelDialog(Dialog dialog, Object tag) {
    }

    //	@Override
//	protected synchronized void onDestroy()
//	{
//		dismissDialog();
//		ActivityStack.getInstance().removeActivity(this);
//		super.onDestroy();
//	}
}
