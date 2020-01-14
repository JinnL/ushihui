package com.mcz.xhj.yz.dr_app;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.stringCut;

import butterknife.BindView;

public class CashIn_End extends BaseActivity implements OnClickListener{
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;// 抬头左边按钮
	@BindView(R.id.title_centertextview)
	TextView centertv;
	@BindView(R.id.ll_cashin_end_failure)
	LinearLayout ll_cashin_end_failure;
	@BindView(R.id.ll_cashin_end_success)
	LinearLayout ll_cashin_end_success;
	@BindView(R.id.tv_failiure)
	TextView tv_failiure;
	@BindView(R.id.bt_failiure)
	Button bt_failiure;
	@BindView(R.id.tv_gouser)
	TextView tv_gouser;
	@BindView(R.id.tv_goinvest)
	TextView tv_goinvest;
	@BindView(R.id.tv_money)
	TextView tv_money;
	@BindView(R.id.tv_timeone)
	TextView tv_timeone;
	@BindView(R.id.tv_time_titletwo)
	TextView tv_time_titletwo;
	@BindView(R.id.tv_titlethree)
	TextView tv_titlethree;
	@BindView(R.id.tv_three)
	TextView tv_three;
	@BindView(R.id.tv_title)
	TextView tv_title;
	@BindView(R.id.line)
	View line;
	@BindView(R.id.tv_f)
	TextView tv_f;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.cashin_end;
	}

	@Override
	protected void initParams() {
		title_leftimageview.setOnClickListener(this);
		tv_gouser.setOnClickListener(this);
		tv_goinvest.setOnClickListener(this);
		bt_failiure.setOnClickListener(this);

		if(getIntent().getStringExtra("flag").equalsIgnoreCase("cashin-success")){
			ll_cashin_end_success.setVisibility(View.VISIBLE);

			tv_money.setText("成功充值"+getIntent().getStringExtra("money")+"元");
			tv_title.setText("恭喜你，\n充值成功！");
			centertv.setText("充值成功");
			line.setBackgroundColor(Color.parseColor("#1798FB"));
			//tv_titlethree.setTextColor(Color.parseColor("#ec5c59"));
			tv_three.setBackgroundResource(R.drawable.bg_corner_blue_yuan);
			/*tv_timeone.setText(stringCut.getDateTimeToStringheng(Long.parseLong(getIntent().getStringExtra("payTime"))));
			tv_timetwo.setText(stringCut.getDateTimeToStringheng(Long.parseLong(getIntent().getStringExtra("confirmTime"))));
			if(getIntent().getStringExtra("paySuccessTime")!=null){
				tv_timethree.setText(stringCut.getDateTimeToStringheng(Long.parseLong(getIntent().getStringExtra("paySuccessTime"))));
			}*/
		}
		else if(getIntent().getStringExtra("flag").equalsIgnoreCase("cashout-success")){
			ll_cashin_end_success.setVisibility(View.VISIBLE);
			long withdrawalsTime = getIntent().getLongExtra("withdrawalsTime", 0l);
			if(withdrawalsTime <= 0){
				tv_timeone.setVisibility(View.GONE);
			}else{
				tv_timeone.setVisibility(View.VISIBLE);
				tv_timeone.setText(stringCut.getDateTimeToStringheng(withdrawalsTime));
			}
			tv_money.setText("成功转出"+getIntent().getStringExtra("money")+"元");
			tv_title.setText("恭喜你，\n提现成功！");
			centertv.setText("提现成功");
			tv_titlethree.setText("提现成功");
			line.setBackgroundColor(Color.parseColor("#1798FB"));
			//tv_titlethree.setTextColor(Color.parseColor("#ec5c59"));
			tv_three.setBackgroundResource(R.drawable.bg_corner_blue_yuan);
			tv_goinvest.setVisibility(View.GONE);
			/*tv_timeone.setText(stringCut.getDateTimeToStringheng(Long.parseLong(getIntent().getStringExtra("payTime"))));
			tv_timetwo.setText(stringCut.getDateTimeToStringheng(Long.parseLong(getIntent().getStringExtra("confirmTime"))));
			if(getIntent().getStringExtra("paySuccessTime")!=null){
				tv_timethree.setText(stringCut.getDateTimeToStringheng(Long.parseLong(getIntent().getStringExtra("paySuccessTime"))));
			}*/
		}
		else if(getIntent().getStringExtra("flag").equalsIgnoreCase("failiure_cashin")){
			centertv.setText("充值失败");
			tv_f.setText("充值失败");
			ll_cashin_end_failure.setVisibility(View.VISIBLE);
			tv_failiure.setText("失败原因("+getIntent().getStringExtra("msgCode")+")："+getIntent().getStringExtra("msg"));
		}
		else if(getIntent().getStringExtra("flag").equalsIgnoreCase("failiure_cashout")){
			centertv.setText("提现失败");
			tv_f.setText("提现失败");
			ll_cashin_end_failure.setVisibility(View.VISIBLE);
			tv_failiure.setText("失败原因("+getIntent().getStringExtra("msgCode")+")："+getIntent().getStringExtra("msg"));
		}
		else if(getIntent().getStringExtra("flag").equalsIgnoreCase("failiure_fourpart")){
			centertv.setText("认证失败");
			tv_f.setText("认证失败");
			ll_cashin_end_failure.setVisibility(View.VISIBLE);
			tv_failiure.setText("失败原因："+getIntent().getStringExtra("msg"));
		}
//		else if(getIntent().getStringExtra("flag").equalsIgnoreCase("cashout-failiure")){
//			ll_cashin_end_failure.setVisibility(View.VISIBLE);
//			tv_failiure.setText("失败原因："+getIntent().getStringExtra("msg"));
//			bt_failiure.setText("重新提现");
//		}
		else if(getIntent().getStringExtra("flag").equalsIgnoreCase("cashin-progress")){

			centertv.setText("充值处理中");
			ll_cashin_end_success.setVisibility(View.VISIBLE);
			tv_money.setText("充值"+getIntent().getStringExtra("money")+"元");
			tv_title.setText("恭喜你，\n充值处理中！");
			line.setBackgroundColor(0xFF999999);
			tv_three.setBackgroundResource(R.drawable.bg_corner_gray_yuan);
			tv_titlethree.setText("充值成功");
			/*tv_timethree.setVisibility(View.GONE);
			tv_timeone.setText(stringCut.getDateTimeToStringheng(Long.parseLong(getIntent().getStringExtra("payTime"))));
			if(getIntent().getStringExtra("confirmTime")!=null){
				if(!getIntent().getStringExtra("confirmTime").equalsIgnoreCase("")){
					tv_timetwo.setText(stringCut.getDateTimeToStringheng(Long.parseLong(getIntent().getStringExtra("confirmTime"))));
				}
			}*/
		}
		else if(getIntent().getStringExtra("flag").equalsIgnoreCase("cashout-progress")){
			long withdrawalsTime = getIntent().getLongExtra("withdrawalsTime", 0l);
			if(withdrawalsTime <= 0){
				tv_timeone.setVisibility(View.GONE);
			}else{
				tv_timeone.setVisibility(View.VISIBLE);
				tv_timeone.setText(stringCut.getDateTimeToStringheng(withdrawalsTime));
			}
			centertv.setText("提现处理中");
			ll_cashin_end_success.setVisibility(View.VISIBLE);
			tv_money.setText("提现"+getIntent().getStringExtra("money")+"元");
			tv_title.setText("恭喜你，\n提现处理中！");
			tv_time_titletwo.setText("平台正在审核中");
			line.setBackgroundColor(0xFF999999);
			tv_three.setBackgroundResource(R.drawable.bg_corner_gray_yuan);
			tv_titlethree.setText("提现成功");
			tv_goinvest.setVisibility(View.GONE);
			/*tv_timethree.setVisibility(View.GONE);
			if(getIntent().getStringExtra("payTime")!=null){
				tv_timeone.setText(stringCut.getDateTimeToStringheng(Long.parseLong(getIntent().getStringExtra("payTime"))));
			}
			tv_timetwo.setText(stringCut.getDateTimeToStringheng(Long.parseLong(getIntent().getStringExtra("confirmTime"))));*/
		}

//		tv_name.setText(getIntent().getStringExtra("proName")) ;
//		tv_money.setText(getIntent().getStringExtra("proAmount")+"元") ;
//		tv_day.setText(getIntent().getStringExtra("proDeadline")+"天") ;
//		tv_rate.setText(getIntent().getStringExtra("proRate")+"%") ;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		if(getIntent().getStringExtra("flag").equalsIgnoreCase("cashin-success")){
			MobclickAgent.onEvent(CashIn_End.this, UrlConfig.point+17+"");
		}
		else if(getIntent().getStringExtra("flag").equalsIgnoreCase("failiure_cashin")){
			MobclickAgent.onEvent(CashIn_End.this, UrlConfig.point+20+"");
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_leftimageview:

			case R.id.tv_gouser://充值成功去账户
				MobclickAgent.onEvent(CashIn_End.this, UrlConfig.point+19+"");
				LocalApplication.getInstance().getMainActivity().setCheckedFram(4);
                setResult(4);
				finish();
				break;
			case R.id.tv_goinvest://充值成功去投资
				MobclickAgent.onEvent(CashIn_End.this, UrlConfig.point+18+"");
				LocalApplication.getInstance().getMainActivity().setCheckedFram(2);
				setResult(3);
				finish();
				break;
			case R.id.bt_failiure://充值失败
				if(getIntent().getStringExtra("flag").equalsIgnoreCase("failiure_cashin")){
					MobclickAgent.onEvent(CashIn_End.this, UrlConfig.point+21+"");
				}
				finish();
				break;
			default:
				break;
		}
	}

}
