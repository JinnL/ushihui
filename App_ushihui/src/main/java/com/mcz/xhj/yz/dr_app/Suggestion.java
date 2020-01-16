package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_view.DialogMaker;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

public class Suggestion extends BaseActivity implements OnClickListener {
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;// 抬头中间信息
	@BindView(R.id.title_rightimageview)
	ImageView title_rightimageview;// 抬头右边图片
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;// 抬头左边按钮
	@BindView(R.id.opinion_submit)
	Button opinion_submit;// 抬头左边按钮
	@BindView(R.id.num_text)
	TextView num_text;// 抬头左边按钮
	@BindView(R.id.stepnew_text_file)
	EditText stepnew_text_file;// 抬头左边按钮

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.suggestion;
	}

	@Override
	protected void initParams() {
		title_centertextview.setText("意见反馈");
		title_rightimageview.setVisibility(View.VISIBLE);
		title_rightimageview.setImageResource(R.mipmap.find_feedback_kefu);
		title_rightimageview.setOnClickListener(this);
		title_leftimageview.setOnClickListener(this);
		opinion_submit.setOnClickListener(this);
		Watcher watcher = new Watcher(stepnew_text_file, this);
		stepnew_text_file.addTextChangedListener(watcher);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.title_leftimageview:
				finish();
				break;
			case R.id.opinion_submit:
				if (stepnew_text_file.getText().toString().trim().length() > 200) {
					ToastMaker.showShortToast("意见字数不能大于200");
				} else if (stepnew_text_file.getText().toString().trim().length() > 0) {
					if (null == preferences.getString("uid", "")) {
						feedback();
					} else {
						feedback_uid();
					}

				} else {
					ToastMaker.showShortToast("请留下您的宝贵意见");
				}
				break;
			case R.id.title_rightimageview:
//			showAlertDialog("拨打客服电话","400-671-7711\n\n热线服务时间：9:00 - 21:00\n周末节假日：9:00 - 18:00", new String[] { "取消", "呼叫" }, true, true, "");
				DialogMaker.showKufuPhoneDialog(Suggestion.this);
				break;
			default:
				break;
		}
	}

	class Watcher implements TextWatcher {
		Context context;
		int onTextLength = 0;
		EditText et_addCard;

		public Watcher(EditText et_addCard, Context context) {
			super();
			this.context = context;
			this.et_addCard = et_addCard;
		}

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			onTextLength = s.length();
			if (onTextLength >= 0 && onTextLength <= 200) {
				num_text.setText(onTextLength + "/200");
			} else {
				ToastMaker.showShortToast("字数限制为最多输入200字符");
			}

		}
	}

	private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;;

	// 设置jianyi
	private void feedback_uid() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils
				.post()
				.url(UrlConfig.FEEDBACK)
				.addParams("uid", preferences.getString("uid", ""))
				.addParams("content",
						stepnew_text_file.getText().toString().trim())
				.addParams("contactInformation", "")
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							showAlertDialog("提交成功", "小行家感谢您的反馈",
									new String[] { "返回" }, true, true, "");
						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统错误");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(Suggestion.this)
									.show_Is_Login();
						} else {
							ToastMaker.showShortToast("反馈失败");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						dismissDialog();
						ToastMaker.showShortToast("请检查网络");
					}
				});
	}

	@Override
	public void onButtonClicked(Dialog dialog, int position, Object tag) {
		if (position == 0) {
			finish();
		}
	}

	private void feedback() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils
				.post()
				.url(UrlConfig.FEEDBACK)
				.addParam("content",
						stepnew_text_file.getText().toString().trim())
				.addParam("contactInformation", "")
				.addParam("version", UrlConfig.version)
				.addParam("channel", "2").build().execute(new StringCallback() {

			@Override
			public void onResponse(String response) {
				dismissDialog();
				JSONObject obj = JSON.parseObject(response);
				if (obj.getBoolean(("success"))) {
					showAlertDialog("提交成功", "小行家感谢您的反馈",
							new String[] { "返回" }, true, true, "");
				} else if ("9999".equals(obj.getString("errorCode"))) {
					ToastMaker.showShortToast("系统错误");
				} else {
					ToastMaker.showShortToast("反馈失败");
				}
			}

			@Override
			public void onError(Call call, Exception e) {
				dismissDialog();
				ToastMaker.showShortToast("请检查网络");
			}
		});
	}

}
