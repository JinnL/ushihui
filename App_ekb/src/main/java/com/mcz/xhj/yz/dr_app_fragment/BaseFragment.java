package com.mcz.xhj.yz.dr_app_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_util.ActivityStack;
import com.mcz.xhj.yz.dr_view.DialogMaker;
import com.mcz.xhj.yz.dr_view.DialogMaker.DialogCallBack;

import butterknife.ButterKnife;

/**
 * BaseActivity
 *
 * @author blue
 */
public abstract class BaseFragment extends Fragment implements DialogCallBack {
    protected Dialog dialog;
    public Context mContext;
//	private LoadingAndRetryManager loadingAndRetryManager;

    /*@Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mContext = activity;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();    //通过fragment的Activity实例化mActivity
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(mContext);
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
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initParams();
    }


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

    @Override
    public void onDestroy() {
        dismissDialog();
        ActivityStack.getInstance().removeActivity(getActivity());
        super.onDestroy();
    }

//	@Override
//	protected synchronized void onDestroy()
//	{
//		dismissDialog();
//		ActivityStack.getInstance().removeActivity(this);
//		super.onDestroy();
//	}
}
