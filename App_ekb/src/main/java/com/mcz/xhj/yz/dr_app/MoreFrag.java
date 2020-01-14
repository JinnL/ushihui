package com.mcz.xhj.yz.dr_app;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_app_fragment.BaseFragment;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_service.UpdateService;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

public class MoreFrag extends BaseFragment implements OnClickListener {

	private String serverVersion, maxVersion;// 服务器版本 ,最新版本号
	private String updateUrl;

	public static MoreFrag instance() {
		MoreFrag view = new MoreFrag();
		return view;
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.frag_find;
	}


	@Override
	protected void initParams() {
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		if (hidden) {

		}
	}

	@Override
	public void onButtonClicked(Dialog dialog, int position, Object tag) {
		// TODO Auto-generated method stub
		if (position == 1) {
			if (tag.equals("update")) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
						//申请权限
						ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
						return;
					} else {
						// 开启更新服务UpdateService
						Intent updateIntent = new Intent(mContext, UpdateService.class).putExtra("update", UrlConfig.APKDOWNLOAD);
						mContext.startService(updateIntent);
						ToastMaker.showLongToast("已转至后台下载");
					}
				} else {
					// 开启更新服务UpdateService
					Intent updateIntent = new Intent(mContext, UpdateService.class).putExtra("update", UrlConfig.APKDOWNLOAD);
					mContext.startService(updateIntent);
					ToastMaker.showLongToast("已转至后台下载");
				}
			} else {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 111);
					return;
				}
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode == 111){
			if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+getResources().getText(R.string.kefu_tel_num_work)));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}else {
				ToastMaker.showLongToast("请先同意授权");
			}
		}else if(requestCode == 110){
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
				// 开启更新服务UpdateService
				Intent updateIntent = new Intent(mContext, UpdateService.class).putExtra("update", UrlConfig.APKDOWNLOAD);
				mContext.startService(updateIntent);
				ToastMaker.showLongToast("已转至后台下载");
			}else{
				ToastMaker.showLongToast("请先同意授权");
			}
		}
	}

	@Override
	public void onCancelDialog(Dialog dialog, Object tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

			default:
				break;
		}
	}

	public void copy(Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context

				.getSystemService(Context.CLIPBOARD_SERVICE);

		cmb.setText(getResources().getString(R.string.weixin));

	}

	/**
	 * 判断手机里有没有这个应用
	 *
	 * @param context
	 * @param packageName
	 * @return
	 */
	public boolean isAvilible(Context context, String packageName) {
		PackageManager packageManager = context.getPackageManager();

		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		for (int i = 0; i < pinfo.size(); i++) {
			if (((PackageInfo) pinfo.get(i)).packageName
					.equalsIgnoreCase(packageName))
				return true;
		}
		return false;
	}
	// 更新2.0
	private String isForceUpdate = null;
	private String version_renewal = null;
	private String content = "发现新版本,是否更新";
	private Dialog dialog;
	private Boolean checked_dismiss = false;
	private long mPressedTime = 0;


	private void getReNewAl() {
		showWaitDialog("正在检查版本...", false, "");
		OkHttpUtils.post().url(UrlConfig.RENEWAL)
				.addParam("version", UrlConfig.version)
				.addParam("channel", "2").build().execute(new StringCallback() {

			@Override
			public void onResponse(String response) {
				dismissDialog();
				JSONObject obj = JSON.parseObject(response);
				if (obj.getBoolean("success")) {
					JSONObject map = obj.getJSONObject("map");
					maxVersion = map.getString("maxVersion");
					if(maxVersion!=null&&!maxVersion.equalsIgnoreCase("")){
						if (stringCut.compareVersion(maxVersion,LocalApplication.localVersion)>0) {
							dialog = showAlertDialog("版本更新", stringCut.HuanHang_Cut(content), new String[]{"取消","立即更新"}, true, true, "update");
						}else {
							ToastMaker.showShortToast("已经是最新版本");
						}
					}else{
						ToastMaker.showShortToast("系统错误");
					}
				}
			}

			@Override
			public void onError(Call call, Exception e) {
				dismissDialog();
			}
		});
	}

}
