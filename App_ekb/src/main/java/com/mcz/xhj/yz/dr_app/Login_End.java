package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;

import butterknife.BindView;

public class Login_End extends BaseActivity implements OnClickListener {
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;// 抬头中间信息
	@BindView(R.id.title_rightimageview)
	ImageView title_rightimageview;// 抬头右边图片
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;// 抬头左边按钮

	@BindView(R.id.btn_again_lingqu)
	Button btn_again_lingqu;

	@BindView(R.id.tv_newr_shuom)
	TextView tv_newr_shuom;

	private String isCps ;  //0=不是cps ，1=是cps
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.login_end;
	}
	
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		MobclickAgent.onEvent(Login_End.this, "100009");
//	}
	private String newId ="";
	@Override
	protected void initParams() {
		title_centertextview.setText("新用户注册");
		title_leftimageview.setOnClickListener(this);
		title_rightimageview.setVisibility(View.GONE);
		btn_again_lingqu.setOnClickListener(this);


//		String tiyan_shuom = getResources().getString(R.string.new_user_regist);
//		tv_newr_shuom.setText(Html.fromHtml(tiyan_shuom));
		//tv_newr_shuom.setText(getIntent().getStringExtra("regSendLabel"));
//		isCps = getIntent().getStringExtra("isCps");
//		if("0".equalsIgnoreCase(isCps)){
//			btn_again_lingqu.setText("变现18000元体验金");
//		}else{
//			btn_again_lingqu.setText("去赚钱");
//		}
		if(getIntent().getStringExtra("newId")!=null){
			newId = getIntent().getStringExtra("newId");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// requestCode标示请求的标示 resultCode表示有数据
		if (requestCode == 1 && resultCode == 1) {
			finish();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
			case R.id.btn_again_lingqu:
				LocalApplication.getInstance().getMainActivity().isInvest = true;
				LocalApplication.getInstance().getMainActivity().isInvestChecked = true;
//				startActivity(new Intent(Login_End.this, Act_Detail_Pro_New.class).putExtra("pid", newId));
//				if("0".equalsIgnoreCase(isCps)){
//					startActivity(new Intent(Login_End.this, Detail_Tiyan.class));
//					finish();
//				}else{
////					startActivity(new Intent(Login_End.this, FourPartAct.class).putExtra("flag", "zhucequdao"));
//					LocalApplication.getInstance().getMainActivity().setCheckedFram(2);
//					finish();
//				}
				setResult(3);
				finish();
				break;
			case R.id.title_leftimageview:
				setResult(2);
				finish();
				break;
			default:
				break;
		}
	}
	
	
}
